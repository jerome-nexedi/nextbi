<?php

/*
 * \brief W3S Hierarchy class
 *
 * \file W3S_Hierarchy.php
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
 * SVN: $Id: W3S_Hierarchy.php 3078 2010-04-07 11:51:14Z predragm $
 *
 */

class W3S_Hierarchy
{
	private $uid,
					$group,

					$type,
					$data,
					$perm = -1,
					$perm_h,
					$perm_n,

					$root,

					$nodemap,

					$triggersOn = true,
					$triggers = array(
						'addNode_before' => array(),
						'addNode_after' => array(),
						'removeNode_before' => array(),
						'removeNode_after' => array(),
						'moveNode_before' => array(),
						'moveNode_after' => array(),
						'copyNode_before' => array(),
						'copyNode_after' => array(),
						'renameNode_before' => array(),
						'renameNode_after' => array(),
						'importNode_before' => array(),
						'importNode_after' => array(),
						'getHierarchyProperties' => array(),
						'getNodeProperties' => array()
					);

	public function __construct ($uid, W3S_Group $group, $type = '', W3S_HierarchyData $data = null, $isNew = false)
	{
		$this->uid = (string) $uid;

		$this->group = $group;

		$this->type = (string) $type;
		$this->data = $data;

		$this->root = new W3S_Node($this, null, false, '', 'root');
		$this->nodemap = array('root' => $this->root);

		if ($isNew)
		{
			$this->perm_h = $group->getPermH();
			$this->perm_n = $this->perm_h & $group->apol->getRule('dimension element');

			$this->root->setPermN($this->perm_n);
		}
		else
			$this->_palo_load();
	}

	public function __sleep ()
	{
		return array('uid', 'type', 'data', 'perm', 'perm_h', 'perm_n', 'root', 'nodemap', 'triggersOn', 'triggers');
	}

	public function __wakeup ()
	{
		foreach ($this->nodemap as $node)
			$node->_sess_setHierarchy($this);
	}

	public function calcPerms ()
	{
		$apol = $this->group->apol;

		$perm = $apol->calcPerms($this->group->getUID(), 'meta', array($this->uid));
		$this->perm = $perm[$this->uid];

		if ($this->perm != -1)
		{
			$this->perm_h = $this->perm & $apol->getRule('dimension');
			$this->perm_n = $this->perm & $apol->getRule('dimension element');
		}
		else
		{
			$this->perm_h = $this->group->getPermH();
			$this->perm_n = $this->perm_h & $apol->getRule('dimension element');
		}

		$this->root->setPermN($this->perm_n);
		$this->root->refreshPerms();
	}

	private function _palo_load ()
	{
		$data = palo_datav($this->group->apol->getConn(), $this->group->getUID(), '#_meta', array(1, 2, 'type', 'data'), array(1, 1, $this->uid));

		$this->type = $data[2];

		$this->data = new W3S_HierarchyData();

		try
		{
			$this->data->import($data[3]);
		}
		catch (Exception $e)
		{
			return false;
		}

		return $this->_palo_load_nodes();
	}

	private function _palo_load_nodes ()
	{
		$apol = $this->group->apol;
		$conn = $apol->getConn();

		$g_uid = $this->group->getUID();

		$elem_list = palo_dimension_list_elements2($conn, $g_uid, $this->uid, true);

		if (empty($elem_list))
		{
			$this->calcPerms();
			return true;
		}

		$all_node_uids = array();
		$elem_children_map = array();

		foreach ($elem_list as $elem)
		{
			$all_node_uids[] = $node_uid = $elem['name'];

			$this->nodemap[$node_uid] = new W3S_Node($this, null, $elem['type'] == 'string', $node_uid);

			if (intval($elem['num_parents']) == 0)
				$elem_children_map['root'][] = $elem;

			if (count($elem['children']))
				$elem_children_map[$node_uid] = $elem['children'];
		}

		$data = palo_datav($conn, $g_uid, '#_' . $this->uid, array(1, 2, 'data', 'type'), array_merge(array(count($all_node_uids), 1), $all_node_uids));
		array_splice($data, 0, 2);

		$perms = $apol->calcPerms($g_uid, $this->uid, $all_node_uids);

		$i = -1;
		foreach ($all_node_uids as $node_uid)
		{
			$node_data = new W3S_NodeData();

			try
			{
				$node_data->import($data[++$i]);
			}
			catch (Exception $e)
			{
				unset($this->nodemap[$node_uid]);
				unset($elem_children_map[$node_uid]);

				++$i;
				continue;
			}

			$this->nodemap[$node_uid]->_load_setAttrs($data[++$i], $node_data, $perms[$node_uid]);
		}

		foreach ($elem_children_map as $parent_uid => $elem_children)
		{
			$parent_obj = $this->nodemap[$parent_uid];

			$children = array();
			$childmap = array();

			foreach ($elem_children as $elem_child)
			{
				$child_uid = $elem_child['name'];

				if (!isset($this->nodemap[$child_uid]))
					continue;

				$child_obj = $this->nodemap[$child_uid];

				$child_obj->_load_setParent($parent_obj);

				$childmap[$child_uid] = array_push($children, $child_obj) - 1;
			}

			$parent_obj->_load_setChildren($children, $childmap);
		}

		$this->calcPerms();

		return true;
	}

	public function _sess_setGroup (W3S_Group $group)
	{
		$this->group = $group;
	}

	public function registerPlugin (W3S_Plugin $plugin)
	{
		foreach ($plugin->getTriggerInfo() as $event => $ident)
			if (isset($this->triggers[$event]))
				$this->triggers[$event][] = array($plugin, $ident);

		$this->data->registerPlugin($plugin);

		return true;
	}

	public function disableTriggers ()
	{
		$this->triggersOn = false;
	}

	public function enableTriggers ()
	{
		$this->triggersOn = true;
	}

	public function getTriggers ($event)
	{
		return $this->triggersOn && isset($this->triggers[$event]) ? $this->triggers[$event] : array();
	}

	public function getGroup ()
	{
		return $this->group;
	}

	public function getUID ()
	{
		return $this->uid;
	}

	public function getType ()
	{
		return $this->type;
	}

	public function getData ()
	{
		return $this->data;
	}

	public function getPermH ()
	{
		return $this->perm_h;
	}

	public function getPermN ()
	{
		return $this->perm_n;
	}

	private function _palo_saveData ()
	{
		palo_setdata($this->data->export(), 'false', $this->group->apol->getConn(), $this->group->getUID(), '#_meta', 'data', $this->uid);

		return true;
	}

	public function saveData ()
	{
		return $this->data instanceof W3S_HierarchyData ? $this->_palo_saveData() : false;
	}

	public function refreshPerms ()
	{
		if ($this->perm != -1)
			return;

		$this->perm_h = $this->group->getPermH();
		$this->perm_n = $this->perm_h & $this->group->apol->getRule('dimension element');

		$this->root->setPermN($this->perm_n);
		$this->root->refreshPerms();
	}

	public function reloadNodes ()
	{
		$this->nodemap = array('root' => $this->root);

		$this->_palo_load_nodes();
	}

	public function registerNode ($node_uid, W3S_Node $node)
	{
		$this->nodemap[$node_uid] = $node;

		return true;
	}

	public function unregisterNode ($node_uid)
	{
		unset($this->nodemap[$node_uid]);

		return true;
	}

	public function paloSync ()
	{
		palo_ping($this->group->apol->getConn());

		return true;
	}

	public function paloNode ($op)
	{
		$args = func_get_args();

		if (function_exists($func = 'palo_e' . $op))
			return call_user_func_array($func, array_merge(array($this->group->apol->getConn(), $this->group->getUID(), $this->uid), array_slice($args, 1)));
	}

	public function paloNodeAttrGet ($uid, $attr_name)
	{
		return palo_data($this->group->apol->getConn(), $this->group->getUID(), '#_' . $this->uid, $attr_name, $uid);
	}

	public function paloNodeAttrSet ($uid, $attr_name, $attr_val)
	{
		return palo_setdata($attr_val, 'false', $this->group->apol->getConn(), $this->group->getUID(), '#_' . $this->uid, $attr_name, $uid);
	}

	public function getNode ($uid)
	{
		if (strpos($uid, '_') !== false)
		{
			list ($h_uid, $uid) = split('_', $uid);

			if ($h_uid != $this->uid)
				return null;
		}

		return isset($this->nodemap[$uid]) ? $this->nodemap[$uid] : null;
	}

	public function getRoot ()
	{
		return $this->root;
	}

	public function node ($node, $method)
	{
		if (!(($node = $this->getNode($node)) instanceof W3S_Node) || !is_callable($callback = array($node, $method)))
			return false;

		$args = func_get_args();
		array_splice($args, 0, 2);

		$refl_method = new ReflectionMethod('W3S_Node', $method);

		foreach ($refl_method->getParameters() as $i => $param)
			if (($refl_class = $param->getClass()) instanceof ReflectionClass && $refl_class->getName() == 'W3S_Node')
			{
				if (!(($node = $this->getNode($args[$i])) instanceof W3S_Node))
					return false;

				$args[$i] = $node;
			}

		$res = call_user_func_array($callback, $args);

		return ($res instanceof W3S_Node) ? $res->getUID() : $res;
	}

	// Returns full filesystem path.
	public function getSysPath()
	{
		return $this->getData()->getBackend()->getLocation() . '/' . $this->getUID() . '-' . urlencode($this->getData()->getName());
	}

	public function getNodeByPath($path)
	{
		foreach (array_merge(array('wss'), W3S_NodeData::$subtypesStatic) as $ext) {
			$ext_length = strlen($ext) + 1;

			if (substr(strtolower($path), $ext_length * -1) == '.' . $ext) {
				$path = substr($path, 0, $ext_length * -1);
				$node_ext = $ext;
				break;
			}
		}

		$path_elems = explode('/', $path[0] == '/' ? substr($path, 1) : $path);

		if (count($path_elems) == 0)
			return null;

		$node = $this->getRoot();
		$path_elems_count = count($path_elems);
		$node_found = true;

		for ($i = 0; $node_found && $i < $path_elems_count; $i++) {
			$node_found = false;

			for ($j = 0, $child_cnt = $node->numOfChildren(); $j < $child_cnt; $j++) {
				$child_node = $node->getChildAtPos($j);
				$child_type = $child_node->getData()->getEffectiveType();

				if ($child_node->getData()->getName() == $path_elems[$i] && !(isset($node_ext) && $child_type != 'folder' && $child_type != ($node_ext == 'wss' ? 'workbook' : $node_ext))) {
					$node = $child_node;
					$node_found = true;
					break;
				}
			}
		}

		return $node_found ? $node : null;
	}

	public function getProperties()
	{
		$props = array();

		foreach ($this->getTriggers('getHierarchyProperties') as $trigger)
			$props[] = call_user_func(array($trigger[0], 'getHierarchyProperties'), $trigger[1], $this);

		return $props;
	}

}

?>