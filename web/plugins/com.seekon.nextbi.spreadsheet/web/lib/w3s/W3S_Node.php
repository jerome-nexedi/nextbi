<?php

/*
 * \brief W3S Node class
 *
 * \file W3S_Node.php
 *
 * Copyright (C) 2006-2010 Jedox AG
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License (Version 2) as published
 * by the Free Software Foundation at http://www.gnu.org/copyleft/gpl.html.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * You may obtain a copy of the License at
 *
 * <a href="http://www.jedox.com/license_palo_bi_suite.txt">
 *   http://www.jedox.com/license_palo_bi_suite.txt
 * </a>
 *
 * If you are developing and distributing open source applications under the
 * GPL License, then you are free to use Palo under the GPL License.  For OEMs,
 * ISVs, and VARs who distribute Palo with their products, and do not license
 * and distribute their source code under the GPL, Jedox provides a flexible
 * OEM Commercial License.
 *
 * \author
 * Predrag Malicevic <predrag.malicevic@develabs.com>
 *
 * \version
 * SVN: $Id: W3S_Node.php 3063 2010-03-30 20:26:36Z predragm $
 *
 */

class W3S_Node
{
	private $uid,
					$leaf,

					$type,
					$data,
					$perm = -1,
					$perm_n,

					$hierarchy,
					$parent,

					$children = array(),
					$childmap = array();

	public function __construct (W3S_Hierarchy $hierarchy, W3S_Node $parent = null, $leaf, $uid, $type = '', W3S_NodeData $data = null)
	{
		$this->hierarchy = $hierarchy;
		$this->parent = $parent;

		$this->uid = (string) $uid;
		$this->leaf = (bool) $leaf;

		$this->type = (string) $type;
		$this->data = $data;
	}

	public function __sleep ()
	{
		return array('uid', 'leaf', 'type', 'data', 'perm', 'perm_n', 'data', 'children', 'childmap');
	}

	public function __wakeup ()
	{
		foreach ($this->children as $child)
			$child->parent = $this;
	}

	private function _palo_addNode ($uid, $leaf, $parent_uid, $type, $data_dump)
	{
		$this->hierarchy->paloNode('add', $leaf ? 'S' : 'N', $uid, $parent_uid, 1, false, true);

		$this->hierarchy->paloSync();

		$this->hierarchy->paloNodeAttrSet($uid, 'type', $type);
		$this->hierarchy->paloNodeAttrSet($uid, 'data', $data_dump);
	}

	private function _palo_removeNodes (array $uids)
	{
		foreach ($uids as $uid)
		{
			$this->hierarchy->paloNode('delete', $uid, true);
			$this->hierarchy->unregisterNode($uid);
		}
	}

	private function _palo_updateCnsldtd ($uid, array $children)
	{
		$cons_elems = array();

		foreach ($children as $child)
			$cons_elems[] = array($child->uid, 1);

		$this->hierarchy->paloNode('update', $uid, 'C', $cons_elems);
	}

	private function _palo_reposNode ($to_uid, $from_uid, $pos_delta)
	{
		$pos = intval($this->hierarchy->paloNode('index', $from_uid, true)) - 1;

		if (($pos += $pos_delta) < 0)
			$pos = 0;

		$this->hierarchy->paloNode('move', $to_uid, $pos);
	}

	public function _load_setParent (W3S_Node $parent)
	{
		$this->parent = $parent;
	}

	public function _load_setChildren (array $children, array $childmap)
	{
		$this->children = $children;
		$this->childmap = $childmap;
	}

	public function _load_setAttrs ($type, W3S_NodeData $data, $perm)
	{
		$this->type = $type;
		$this->data = $data;
		$this->perm = $perm;
	}

	public function _sess_setHierarchy (W3S_Hierarchy $hierarchy)
	{
		$this->hierarchy = $hierarchy;
	}

	public function getUID ()
	{
		return $this->uid;
	}

	public function isChildOf (W3S_Node $parent)
	{
		return isset($parent->childmap[$this->uid]);
	}

	public function isParentOf (W3S_Node $child)
	{
		return $child->parent === $this;
	}

	public function numOfChildren ()
	{
		return count($this->children);
	}

	public function posOfChild (W3S_Node $child)
	{
		return isset($this->childmap[$child->uid]) ? $this->childmap[$child->uid] : -1;
	}

	public function getChildAtPos ($pos)
	{
		return $pos >= 0 && $pos < count($this->children) ? $this->children[$pos] : null;
	}

	public function getSubUIDs (array &$uids, array &$referrers = null)
	{
		$uids = array_merge($uids, array_keys($this->childmap));

		if (is_array($referrers) && $this->data instanceof W3S_NodeData && count($refs = $this->data->getReferrers()))
			$referrers = array_merge($referrers, $refs);

		foreach ($this->children as $child)
			if (!$child->leaf)
				$child->getSubUIDs($uids, $referrers);
			else if (is_array($referrers) && $this->data instanceof W3S_NodeData && count($refs = $this->data->getReferrers()))
				$referrers = array_merge($referrers, $refs);
	}

	public function getSuperUIDs (array &$uids)
	{
		$super = $this->parent;

		while ($super->parent)
		{
			$uids[] = $super->uid;
			$super = $super->parent;
		}
	}

	public function getParent ()
	{
		return $this->parent;
	}

	public function getHierarchy ()
	{
		return $this->hierarchy;
	}

	public function getType ()
	{
		return $this->type;
	}

	public function getData ()
	{
		return $this->data;
	}

	public function getPerm ()
	{
		return $this->perm;
	}

	public function getPermN ()
	{
		return $this->perm_n;
	}

	public function setPermN ($perm_n)
	{
		if ($this->parent == null)
			$this->perm_n = $perm_n;
	}

	public function calcPerms ()
	{
		$group = $this->hierarchy->getGroup();

		$perm = $group->apol->calcPerms($group->getUID(), $this->hierarchy->getUID(), array($this->uid));
		$this->perm = $perm[$this->uid];

		$this->perm_n = $this->perm != -1 ? $this->perm : $this->parent->perm_n;

		if ($this->perm == AccessPolicy::PERM_NONE)
		{
			$this->hierarchy->reloadNodes();
			return;
		}

		if (!$this->leaf)
			$this->refreshPerms();
	}

	public function refreshPerms ()
	{
		foreach ($this->children as $child)
			if ($child->perm == -1)
			{
				$child->perm_n = $this->perm_n;

				if (!$child->leaf)
					$child->refreshPerms();
			}
			else if (!isset($child->perm_n))
			{
				$child->perm_n = $child->perm;

				if (!$child->leaf)
					$child->refreshPerms();
			}
	}

	public function saveData ()
	{
		if ($this->data instanceof W3S_NodeData)
			$this->hierarchy->paloNodeAttrSet($this->uid, 'data', $this->data->export());
	}

	public function addNode ($leaf, $type, $data, $pos = -1)
	{
		if ($this->leaf)
			return null;

		if (!is_int($pos))
			$pos = is_string($pos) && isset($this->childmap[$pos]) ? $this->childmap[$pos] : -1;

		if ($pos < 0 || $pos > count($this->children) - 1)
		{
			$append = true;
			$parent_uid = $this->uid;
		}
		else
		{
			$append = false;
			$parent_uid = '';
		}

		$group = $this->hierarchy->getGroup();
		$h_uid = $this->hierarchy->getUID();

		do
		{
			$new_uid = $group->getSequence($h_uid, 'n');
		}
		while ($this->hierarchy->getNode($new_uid));

		if ($data instanceof W3S_NodeData)
		{
			$data = clone $data;
			$data->setSystemName($new_uid);
		}
		else
		{
			if (is_array($data))
			{
				$data['el'] = $new_uid;
				$data['type'] = $type;
			}
			else
				$data = array('el' => $new_uid, 'type' => $type, 'name' => strtoupper($new_uid));

			$data = new W3S_NodeData($data);
		}

		$node = new W3S_Node($this->hierarchy, $this, $leaf, $new_uid, $type, $data);

		$node->perm_n = $this->perm_n;

		foreach ($this->hierarchy->getTriggers('addNode_before') as $trigger)
			call_user_func(array($trigger[0], 'addNode_before'), $trigger[1], $node);


		$this->_palo_addNode($new_uid, $leaf, $parent_uid, $type, $data->export());

		if ($append)
			$this->childmap[$new_uid] = array_push($this->children, $node) - 1;
		else
		{
			array_splice($this->children, $pos, 0, array($node));
			$this->childmap[$new_uid] = $pos;

			for ($c = &$this->children, $cm = &$this->childmap, $i = count($c) - 1; $i > $pos; $cm[$c[$i]->uid] = $i--);

			if ($this->uid != '')
				$this->_palo_updateCnsldtd($this->uid, $this->children);
			else
				$this->_palo_reposNode($new_uid, $this->children[$pos + 1]->uid, 0);
		}

		$this->hierarchy->registerNode($new_uid, $node);


		$conn = $group->apol->getConn();
		$g_uid = $group->getUID();

		if (($lnk = $data->getReference()) instanceof W3S_NodeLink
				&& ($ref = W3S_NodeData::getNodeData($conn, $lnk_g_uid = $lnk->getGroup(), $lnk_h_uid = $lnk->getHierarchy(), $lnk_n_uid = $lnk->getNode())) instanceof W3S_NodeData)
		{
			$ref->addReferrer($g_uid, $h_uid, $new_uid);
			W3S_NodeData::setNodeData($conn, $ref, $lnk_g_uid, $lnk_h_uid, $lnk_n_uid);
		}

		foreach ($data->getResources(true) as $lnk)
		{
			if (!($lnk instanceof W3S_NodeLink))
				continue;

			$lnk_g_uid = $lnk->getGroup();
			$lnk_h_uid = $lnk->getHierarchy();
			$lnk_n_uid = $lnk->getNode();

			if ($lnk_g_uid == $g_uid && ($res = $group->getHierarchy($lnk_h_uid, false)) instanceof W3S_Hierarchy
				&& ($res = $res->getNode($lnk_n_uid)) instanceof W3S_Node && $res->data instanceof W3S_NodeData)
			{
				$res->data->addDependent($g_uid, $h_uid, $new_uid);
				$res->saveData();
			}
			else if (($res = W3S_NodeData::getNodeData($conn, $lnk_g_uid, $lnk_h_uid, $lnk_n_uid)) instanceof W3S_NodeData)
			{
				$res->addDependent($g_uid, $h_uid, $new_uid);
				W3S_NodeData::setNodeData($conn, $res, $lnk_g_uid, $lnk_h_uid, $lnk_n_uid);
			}
		}

		foreach ($this->hierarchy->getTriggers('addNode_after') as $trigger)
			call_user_func(array($trigger[0], 'addNode_after'), $trigger[1], $node);

		return $node;
	}

	public function removeNode (W3S_Node $node)
	{
		if ($node->parent !== $this || !isset($this->childmap[$node->uid]))
			return false;

		foreach ($this->hierarchy->getTriggers('removeNode_before') as $trigger)
			call_user_func(array($trigger[0], 'removeNode_before'), $trigger[1], $node);

		$uids = array($node->uid);
		$referrers = array();
		$node->getSubUIDs($uids, $referrers);

		$this->_palo_removeNodes($uids);

		$group = $this->hierarchy->getGroup();
		W3S_Group::removeNodes($group->apol, $referrers);

		if ($node->data instanceof W3S_NodeData)
		{
			$conn = $group->apol->getConn();
			$g_uid = $group->getUID();
			$h_uid = $this->hierarchy->getUID();

			if (($lnk = $node->data->getReference()) instanceof W3S_NodeLink
					&& ($ref = W3S_NodeData::getNodeData($conn, $lnk_g_uid = $lnk->getGroup(), $lnk_h_uid = $lnk->getHierarchy(), $lnk_n_uid = $lnk->getNode())) instanceof W3S_NodeData)
			{
				$ref->deleteReferrer($g_uid, $h_uid, $node->uid);
				W3S_NodeData::setNodeData($conn, $ref, $lnk_g_uid, $lnk_h_uid, $lnk_n_uid);
			}

			foreach ($node->data->getResources(true) as $lnk)
			{
				if (!($lnk instanceof W3S_NodeLink))
					continue;

				$lnk_g_uid = $lnk->getGroup();
				$lnk_h_uid = $lnk->getHierarchy();
				$lnk_n_uid = $lnk->getNode();

				if ($lnk_g_uid == $g_uid && ($res = $group->getHierarchy($lnk_h_uid, false)) instanceof W3S_Hierarchy
					&& ($res = $res->getNode($lnk_n_uid)) instanceof W3S_Node && $res->data instanceof W3S_NodeData)
				{
					$res->data->deleteDependent($g_uid, $h_uid, $node->uid);
					$res->saveData();
				}
				else if (($res = W3S_NodeData::getNodeData($conn, $lnk_g_uid, $lnk_h_uid, $lnk_n_uid)) instanceof W3S_NodeData)
				{
					$res->deleteDependent($g_uid, $h_uid, $node->uid);
					W3S_NodeData::setNodeData($conn, $res, $lnk_g_uid, $lnk_h_uid, $lnk_n_uid);
				}
			}

			foreach ($node->data->getDependents(true) as $lnk)
			{
				if (!($lnk instanceof W3S_NodeLink))
					continue;

				$lnk_g_uid = $lnk->getGroup();
				$lnk_h_uid = $lnk->getHierarchy();
				$lnk_n_uid = $lnk->getNode();

				if ($lnk_g_uid == $g_uid && ($dep = $group->getHierarchy($lnk_h_uid, false)) instanceof W3S_Hierarchy
					&& ($dep = $dep->getNode($lnk_n_uid)) instanceof W3S_Node && $dep->data instanceof W3S_NodeData)
				{
					$dep->data->deleteResource($g_uid, $h_uid, $node->uid);
					$dep->saveData();
				}
				else if (($dep = W3S_NodeData::getNodeData($conn, $lnk_g_uid, $lnk_h_uid, $lnk_n_uid)) instanceof W3S_NodeData)
				{
					$dep->deleteResource($g_uid, $h_uid, $node->uid);
					W3S_NodeData::setNodeData($conn, $dep, $lnk_g_uid, $lnk_h_uid, $lnk_n_uid);
				}
			}
		}

		$this->hierarchy->paloSync();


		$pos = $this->childmap[$node->uid];

		array_splice($this->children, $pos, 1);
		unset($parent->childmap[$node->uid]);

		--$pos;
		for ($c = &$this->children, $cm = &$this->childmap, $i = count($c) - 1; $i > $pos; $cm[$c[$i]->uid] = $i--);

		foreach ($this->hierarchy->getTriggers('removeNode_after') as $trigger)
			call_user_func(array($trigger[0], 'removeNode_after'), $trigger[1], $node);

		return true;
	}

	public function moveNode (W3S_Node $new_parent, $new_pos = -1)
	{
		if ($new_parent->leaf)
			return false;

		if (!is_int($new_pos))
			$new_pos = is_string($new_pos) && isset($new_parent->childmap[$new_pos]) ? $new_parent->childmap[$new_pos] : -1;

		foreach ($this->hierarchy->getTriggers('moveNode_before') as $trigger)
			call_user_func(array($trigger[0], 'moveNode_before'), $trigger[1], $this, $new_parent, $new_pos);

		$curr_parent = $this->parent;
		$curr_pos = $curr_parent->childmap[$this->uid];

		if ($curr_parent !== $new_parent)
		{
			array_splice($curr_parent->children, $curr_pos, 1);
			unset($curr_parent->childmap[$this->uid]);

			for ($c_pos = $curr_pos - 1, $c = &$curr_parent->children, $cm = &$curr_parent->childmap, $i = count($c) - 1; $i > $c_pos; $cm[$c[$i]->uid] = $i--);

			if ($curr_parent->uid != '')
				$this->_palo_updateCnsldtd($curr_parent->uid, $curr_parent->children);

			$this->parent = $new_parent;

			if ($new_pos < 0 || $new_pos > count($new_parent->children) - 1)
				$new_parent->childmap[$this->uid] = $end_pos = array_push($new_parent->children, $this) - 1;
			else
			{
				array_splice($new_parent->children, $new_pos, 0, array($this));
				$new_parent->childmap[$this->uid] = $new_pos;

				for ($c = &$new_parent->children, $cm = &$new_parent->childmap, $i = count($c) - 1; $i > $new_pos; $cm[$c[$i]->uid] = $i--);
			}

			if ($new_parent->uid != '')
				$this->_palo_updateCnsldtd($new_parent->uid, $new_parent->children);
			else if (isset($end_pos))
				$this->_palo_reposNode($this->uid, $new_parent->children[$end_pos - 1]->uid, 1);
			else
				$this->_palo_reposNode($this->uid, $new_parent->children[$new_pos + 1]->uid, -1);

			if ($this->perm == -1)
			{
				$this->perm_n = $new_parent->perm_n;
				$this->refreshPerms();
			}
		}
		else
		{
			$end_pos = count($curr_parent->children) - 1;

			if ($new_pos < 0 || $new_pos > $end_pos)
			{
				$new_pos = $end_pos;

				if ($curr_pos == $new_pos)
					return true;

				array_splice($curr_parent->children, $curr_pos, 1);
				array_push($curr_parent->children, $this);

				$f_beg = $new_pos - 1;
				$f_end = $curr_pos - 1;
			}
			else
			{
				unset($end_pos);

				if ($new_pos > $curr_pos)
				{
					if (--$new_pos == $curr_pos)
						return true;

					$f_beg = $new_pos - 1;
					$f_end = $curr_pos - 1;
				}
				else
				{
					$f_beg = $curr_pos;
					$f_end = $new_pos;
				}

				array_splice($curr_parent->children, $new_pos, 0, array_splice($curr_parent->children, $curr_pos, 1));
			}

			$curr_parent->childmap[$this->uid] = $new_pos;

			for ($c = &$curr_parent->children, $cm = &$curr_parent->childmap, $i = $f_beg; $i > $f_end; $cm[$c[$i]->uid] = $i--);

			if ($curr_parent->uid != '')
				$this->_palo_updateCnsldtd($curr_parent->uid, $curr_parent->children);
			else if (isset($end_pos))
				$this->_palo_reposNode($this->uid, $curr_parent->children[$end_pos - 1]->uid, 1);
			else
				$this->_palo_reposNode($this->uid, $curr_parent->children[$new_pos + 1]->uid, -1);
		}

		foreach ($this->hierarchy->getTriggers('moveNode_after') as $trigger)
			call_user_func(array($trigger[0], 'moveNode_after'), $trigger[1], $this, $curr_parent, $curr_pos);

		return true;
	}

	private function _copyRecursive (W3S_Node $new_parent, $new_pos = -1)
	{
		$copied_node = $new_parent->addNode($this->leaf, $this->type, $this->data, $new_pos);

		if (!$this->leaf)
			foreach ($this->children as $child)
					$child->_copyRecursive($copied_node);

		return $copied_node;
	}

	public function copyNode (W3S_Node $new_parent, $new_pos = -1)
	{
		if (!is_int($new_pos))
			$new_pos = is_string($new_pos) && isset($new_parent->childmap[$new_pos]) ? $new_parent->childmap[$new_pos] : -1;

		$im_data = array();

		foreach ($this->hierarchy->getTriggers('copyNode_before') as $trigger)
			call_user_func(array($trigger[0], 'copyNode_before'), $trigger[1], $this, $new_parent, $new_pos, &$im_data);

		$this->hierarchy->disableTriggers();

		$copied_node = $this->_copyRecursive($new_parent, $new_pos);

		$this->hierarchy->enableTriggers();

		foreach ($this->hierarchy->getTriggers('copyNode_after') as $trigger)
			call_user_func(array($trigger[0], 'copyNode_after'), $trigger[1], $this, $copied_node, &$im_data);

		return $copied_node;
	}

	public function renameNode (W3S_Node $node, $new_name)
	{
		if (!($node->data instanceof W3S_NodeData))
			return false;

		$new_name = strval($new_name);

		foreach ($this->hierarchy->getTriggers('renameNode_before') as $trigger)
			call_user_func(array($trigger[0], 'renameNode_before'), $trigger[1], $node, $new_name);

		$old_name = $node->data->getName();

		$node->data->setName($new_name);
		$this->hierarchy->paloSync();
		$node->saveData();

		foreach ($this->hierarchy->getTriggers('renameNode_after') as $trigger)
			call_user_func(array($trigger[0], 'renameNode_after'), $trigger[1], $node, $old_name);

		return true;
	}

	public function importNode ($leaf, $type, $data, $pos = -1)
	{
		if (!is_array($data))
			return null;

		foreach ($this->hierarchy->getTriggers('importNode_before') as $trigger)
			call_user_func(array($trigger[0], 'importNode_before'), $trigger[1], $this, $leaf, $type, &$data, $pos);

		$this->hierarchy->disableTriggers();

		$imported_node = $this->addNode($leaf, $type, $data, $pos);

		$this->hierarchy->enableTriggers();

		foreach ($this->hierarchy->getTriggers('importNode_after') as $trigger)
			call_user_func(array($trigger[0], 'importNode_after'), $trigger[1], $type, &$data, $imported_node);

		return $imported_node;
	}

	public function dumpASCII ($level = 0)
	{
		if ($this->uid == '')
			print '|' . $this->hierarchy->getUID() . '|';
		else
			print ($pad = str_pad('', $level, ' ')) . "|\n" . $pad . '+-' . ($this->leaf ? $this->uid : ('[' . $this->uid . ']')) . ':' . $this->parent->childmap[$this->uid] . ' (' . $this->type . ')';

		print "\n";

		foreach ($this->children as $child)
			$child->dumpASCII($level + 3);
	}

	public function getLinks ($type, array &$links, W3S_Node $refNode = null)
	{
		if ($this->data instanceof W3S_NodeData && count($node_links = $this->data->getLinks($type)))
			$links[] = array('uid' => $this->uid, 'path' => $refNode instanceof W3S_Node ? $this->getRelPath($this, false, false, $refNode) : '', 'links' => $node_links);

		foreach ($this->children as $child)
			$child->getLinks($type, $links, $refNode instanceof W3S_Node ? $refNode : $this);
	}

	public function addReferrer ($group, $hierarchy, $node)
	{
		if ($this->data instanceof W3S_NodeData)
			$this->data->addReferrer($group, $hierarchy, $node);
	}

	public function removeReferrer ($group, $hierarchy, $node)
	{
		if ($this->data instanceof W3S_NodeData)
			$this->data->deleteReferrer($group, $hierarchy, $node);
	}

	public function addResource ($group, $hierarchy, $node)
	{
		if ($this->data instanceof W3S_NodeData)
			$this->data->addResource($group, $hierarchy, $node);
	}

	public function removeResource ($group, $hierarchy, $node)
	{
		if ($this->data instanceof W3S_NodeData)
			$this->data->deleteResource($group, $hierarchy, $node);
	}

	public function removeAllResources ()
	{
		if ($this->data instanceof W3S_NodeData)
			$this->data->deleteAllResources();
	}

	public function addDependent ($group, $hierarchy, $node)
	{
		if ($this->data instanceof W3S_NodeData)
			$this->data->addDependent($group, $hierarchy, $node);
	}

	public function removeDependent ($group, $hierarchy, $node)
	{
		if ($this->data instanceof W3S_NodeData)
			$this->data->deleteDependent($group, $hierarchy, $node);
	}

	public function dump ($level = -1, array $filter = null, $multi_h = false)
	{
		if (empty($this->children))
			return array();

		$h_uid = $this->hierarchy->getUID();
		$uid_pfx = $multi_h ? ($h_uid . '_') : '';

		$dump = array();
		$curr = &$dump;

		$kids = &$this->children;
		$kid = $kids[$idx = 0];

		$stack = array();

		while (true)
		{
			if (!is_array($filter) || in_array($kid->type, $filter))
			{
				$curr_kid = &$curr[array_push($curr, array('id' => $uid_pfx . $kid->uid, 'leaf' => $kid->leaf, 'text' => $kid->data->getName(), 'perm_n' => $kid->perm_n, 'iconCls' => 'w3s_' . $kid->data->getEffectiveType(), 'qtip' => $kid->data->getName())) - 1];

				if ($multi_h)
				{
					$curr_kid['h_uid'] = $h_uid;
					$curr_kid['n_uid'] = $kid->uid;
				}

				if (!$kid->leaf && $level != 0 && !empty($kid->children))
				{
					if ($kids[++$idx])
						$stack[] = array(&$curr, &$kids, $level, $idx);

					$curr_kid['children'] = array();
					$curr = &$curr_kid['children'];

					$kids = &$kid->children;
					$kid = $kids[$idx = 0];
					--$level;

					continue;
				}
			}

			if ($kid = $kids[++$idx])
				continue;

			if (($pop = array_pop($stack)) === null)
				break;

			list ( , , $level, $idx) = $pop;

			$curr = &$pop[0];
			$kids = &$pop[1];
			$kid = $kids[$idx];
		}

		return $dump;
	}

	public function getProperties()
	{
		$props = array();

		foreach ($this->hierarchy->getTriggers('getNodeProperties') as $trigger)
			$props[] = call_user_func(array($trigger[0], 'getNodeProperties'), $trigger[1], $this);

		return $props;
	}

	// returns full filesystem path
	public function getSysPath ()
	{
		return $this->getHierarchy()->getSysPath() . $this->getRelPath($this);
	}

	// returns relative path for given node (/group/hierarchy/node1/.../node3)
	public function getRelPath (W3S_Node $node, $fs_name = true, $with_ext = false, W3S_Node $top_node = null)
	{
		if ($node->type == 'root')
			return '';

		if ($top_node instanceof W3S_Node && $top_node->uid == $node->uid)
			return $top_node->data->getName();

		$ext = '';

		if ($with_ext)
		{
			$effect_type = $node->data->getEffectiveType();
			$ext = in_array($effect_type, array_merge(array('workbook'), W3S_NodeData::$subtypesStatic), true) ? '.' . ($effect_type == 'workbook' ? 'wss' : $effect_type) : '';
		}

		return $this->getRelPath($node->parent, $fs_name, false, $top_node) . '/' . ($fs_name ? $node->uid . '-' . urlencode($node->data->getName()) : $node->data->getName()) . $ext;
	}

}

?>