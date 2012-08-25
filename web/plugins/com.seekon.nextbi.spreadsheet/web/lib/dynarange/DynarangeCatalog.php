<?php

/*
 * \brief class representing a catalog of all DynaRanges on a sheet
 *
 * \file DynarangeCatalog.php
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
 * SVN: $Id: DynarangeCatalog.php 3110 2010-04-14 11:24:19Z predragm $
 *
 */

class DynarangeCatalog
{
	const RE_LOCATION = '/^=\$([A-Z]+)\$([0-9]+)[:,]\$([A-Z]+)\$([0-9]+)$/';

	const CCMD_STOP_ON_ERR = 1;

	private $storage = array();

	private $dataLists;
	private $dynaranges;

	public $expandLimit;

	private $cmds = array();
	private $ecData = array();

	public function __construct ()
	{
	}

	public function __sleep ()
	{
		return array('storage');
	}

	private function _setup ($sheetUid = null)
	{
		if ($doUpdate = $sheetUid == null)
		{
			$cmds = '[["ocurr",2],["wget","",[],["id","n_location"],{"e_type":"hb"}]]';

			$res = json_decode(ccmd($cmds), true);

			if (!isset($res[0][0]) || $res[0][0] !== true || !isset($res[1][0]) || $res[1][0] !== true || !is_array($res[1][1]) || !count($res[1][1]))
				return;

			$sheetUid = $res[0][1];
		}

		if (!isset($this->storage[$sheetUid]))
			return;

		$this->dataLists = &$this->storage[$sheetUid]['dl'];
		$this->dynaranges = &$this->storage[$sheetUid]['hb'];
		$this->expandLimit = &$this->storage[$sheetUid]['el'];

		if (!$doUpdate)
			return;

		foreach ($this->dynaranges as $drId => $dr)
		{
			$dr->setCatalog($this);

			if (is_array($this->dataLists[$drId]))
				$dr->setDataList($this->dataLists[$drId]);
		}

		foreach ($res[1][1] as &$wsel)
		{
			$dr = $this->dynaranges[$wsel['id']];

			if ($dr->isInner())
				$innerLctns[$wsel['id']] = $wsel['n_location'];
			else
				$dr->setLocation($wsel['n_location']);
		}

		if (isset($innerLctns))
			foreach ($innerLctns as $id => $lctn)
				$this->dynaranges[$id]->setLocation($lctn);
	}

	private function _rewriteDeps ()
	{
		foreach ($this->ecData as $drId => &$ecData)
		{
			$dr = $this->dynaranges[$drId];

			foreach ($ecData as &$ecInfo)
				DynarangeDepsRewriter::rewrite($dr->getSrcCoords(), $dr->getIdxBase(), $dr->getIncrs(), $ecInfo[0], $ecInfo[1]);
		}
	}

	public function genDataList ($wseId, $drId, &$vals, $externCall = false)
	{
		if ($externCall)
			$this->_setup();

		if (!isset($this->dynaranges[$drId]))
			return false;

		$dataList = array();

		if (!is_array($vals))
			$dataList[] = array('n' => strval($vals));
		else if (($len = count($vals)) == 0)
			$dataList[] = array('n' => '');
		else if (is_int($vals[2]) && $len % 3 == 0)
		{
			$name = $vals[0];
			$alias = $vals[1];
			$curr_lvl = $vals[2];
			$curr_lists = array($curr_lvl => &$dataList);

			for ($lvl_prev = $curr_lvl, $i = 3; $i < $len; ++$i)
			{
				$lvl = $vals[$i + 2];

				if ($lvl > $lvl_prev)
					$curr_lists[$lvl] = &$curr_lists[$curr_lvl][array_push($curr_lists[$curr_lvl], array('n' => $name, 'a' => $alias, 'l' => array())) - 1]['l'];
				else
					$curr_lists[$curr_lvl][] = array('n' => $name, 'a' => $alias);

				$lvl_prev = $lvl;

				if (isset($curr_lists[$lvl]))
					$curr_lvl = $lvl;

				$name = $vals[$i++];
				$alias = $vals[$i++];
			}

			$curr_lists[$curr_lvl][] = array('n' => $name, 'a' => $alias);
		}
		else
			foreach ($vals as $val)
				$dataList[] = array('n' => $val);

		$dr = $this->dynaranges[$drId];

		try
		{
			if ($dr->activated())
				$dr->deactivate();

			$this->dataLists[$drId] = array('r' => $wseId, 'l' => &$dataList);
			$dr->setDataList($this->dataLists[$drId]);

			if ($dr->activated())
			{
				$dr->reactivate();
				$this->commit($dr);

				$this->_rewriteDeps();
				$this->ecData = array();
			}
		}
		catch (DynarangeException $de)
		{
			return false;
		}

		return true;
	}

	public function getFirstElem ($drId)
	{
		return isset($this->dataLists[$drId]) ? $this->dataLists[$drId]['l'][0]['n'] : '';
	}

	public function addCmd (array $cmd)
	{
		$this->cmds[] = $cmd;
	}

	public function getCmds ()
	{
		return count($this->cmds) ? array_splice($this->cmds, 0) : null;
	}

	public function getDynaranges ()
	{
		return $this->dynaranges;
	}

	public function getDynarange ($drId)
	{
		return isset($this->dynaranges[$drId]) ? $this->dynaranges[$drId] : null;
	}

	public function regDynarange (Dynarange $dr)
	{
		$this->dynaranges[$dr->getId()] = $dr;
	}

	public function removeDynaranges (array &$drIds)
	{
		$wseUids = array();

		foreach ($drIds as $drId)
		{
			if (isset($this->dataLists[$drId]))
				$wseUids[] = $this->dataLists[$drId]['r'];

			unset($this->dataLists[$drId]);
			unset($this->dynaranges[$drId]);
		}

		$this->addCmd(array('wdel', '', $wseUids));

		return true;
	}

	public function saveECData ($drId, $distance, $count)
	{
		if (!isset($this->ecData[$drId]))
			$this->ecData[$drId] = array();

		$this->ecData[$drId][] = array($distance, $count);
	}

	public function commit (Dynarange $dr, $readLctns = true)
	{
		if (!count($this->cmds))
			return;

		$this->addCmd(array('wupd', '', array($dr->getUid() => array('n_location' => $dr->getLocation()))));

		if (!$readLctns)
		{
			$res = ccmd($this->getCmds(), self::CCMD_STOP_ON_ERR);
			$res = end($res);

			if (!$res[0])
				throw new DynarangeException($res);

			return;
		}

		$this->addCmd(array('wget', '', array(), array('id', 'n_location'), array('e_type' => 'hb')));

		$res = ccmd($this->getCmds(), self::CCMD_STOP_ON_ERR);
		$res = end($res);

		if (!$res[0])
			throw new DynarangeException($res);

		$id = $dr->getId();

		foreach ($res[1] as &$lctn)
		{
			if ($lctn['id'] == $id)
				continue;

			$dr = $this->dynaranges[$lctn['id']];

			if ($dr->isInner())
				$innerLctns[$lctn['id']] = $lctn['n_location'];
			else
				$dr->setLocation($lctn['n_location']);
		}

		if (isset($innerLctns))
			foreach ($innerLctns as $id => $lctn)
				$this->dynaranges[$id]->setLocation($lctn);
	}

	public function activate ()
	{
		foreach ($this->dynaranges as $dr)
			$dr->activate();
	}

	public function run ($expandLimit = null)
	{
		$cmds = '[["wget","",[],["n_location","e_id","id","hbdata"],{"e_type":"hb"}],["ocurr",2]]';

		$res = json_decode(ccmd($cmds), true);

		if (!isset($res[0][0]) || $res[0][0] !== true || !is_array($res[0][1]) || !count($res[0][1]) || !isset($res[1][0]) || $res[1][0] !== true)
			return array(array(false, 100, ''));

		$wsElems = &$res[0][1];
		$sheetUid = $res[1][1];

		$this->storage[$sheetUid] = array('dl' => array(), 'hb' => array(), 'el' => $expandLimit);
		$this->_setup($sheetUid);

		$clone_cws_ids = array();
		$get_cws_ccmd = array(array('gdcr', 0));

		foreach ($wsElems as &$drData)
		{
			if (!preg_match(self::RE_LOCATION, $drData['n_location'], $src))
				continue;

			$src = array(Dynarange::ltrs2num($src[1]), intval($src[2]), Dynarange::ltrs2num($src[3]), intval($src[4]));

			$uid = $drData['e_id'];
			$id = $drData['id'];

			$drData = $drData['hbdata'];

			$dr = $this->dynaranges[$id] = new Dynarange($this, $uid, $id, $src, $drData['dir'], $drData['dcell'], $drData['ss_func']);

			if ($dr === null)
				continue;

			if (isset($drData['alias']))
				$dr->setShowAlias($drData['alias']);

			if (isset($drData['drill']))
				$dr->setDrillDown($drData['drill']);

			if (isset($drData['level']))
				$dr->setBeginLevel($drData['level']);

			if (isset($drData['border']))
				$dr->setBorderStyle($drData['border']);

			if (isset($drData['cwidth']))
				$dr->setColWidths(is_numeric($drData['cwidth']) ? array(round(intval($drData['cwidth']) * 7.407)) : array());
			else if ($drData['dir'])
			{
				$clone_cws_ids[] = $id;
				$get_cws_ccmd[] = array('gcr', 0, $src[0], $src[2]);
			}

			if (isset($drData['indent']))
				$dr->setIndentData($drData['indent']);
		}

		if (count($clone_cws_ids))
		{
			$get_cws_res = ccmd($get_cws_ccmd);

			$cw_def = array_pop(array_shift($get_cws_res));
			array_shift($get_cws_ccmd);

			foreach ($clone_cws_ids as $i => $id)
			{
				if (!($cnt_sparse = count($cws_sparse = &$get_cws_res[$i][1])))
					continue;

				$cols = array_slice($get_cws_ccmd[$i], 2, 2);
				$cws = array_fill(0, $cols[1] - $cols[0] + 1, $cw_def);

				for ($s = 0; $s < $cnt_sparse; )
					$cws[$cws_sparse[$s++] + 1 - $cols[0]] = $cws_sparse[$s++];

				$this->dynaranges[$id]->setColWidths($cws);
			}
		}

		$reFindDeps = '/(?<=,|;|\() *(' . join('|', array_keys($this->dynaranges)) . ') *(?=,|;|\))(?=(?:[^"]*$)|(?:[^"]*"[^"]*"[^"]*)*$)/';

		foreach ($this->dynaranges as $dr)
		{
			preg_match_all($reFindDeps, $dr->getFormula(), $matches, PREG_PATTERN_ORDER | PREG_OFFSET_CAPTURE);

			if (count($matches[1]))
			{
				$precedents = array();

				foreach ($matches[1] as $match)
					$precedents[$match[0]] = array('offset' => $match[1]);

				$dr->setPrecedents($precedents);
			}
		}

		$drs = $this->dynaranges;

		while (true)
		{
			$wseUpdate = array();
			$wseUids = array();

			foreach ($drs as $drId => $dr)
			{
				$formula = $dr->getFormula();

				if (count($prcds = &$dr->getPrecedents()))
				{
					if (count(array_intersect_key($prcds, $drs)))
						continue;

					$offsetCorr = 0;

					foreach ($prcds as $prcdId => &$prcdData)
					{
						$prcdData['val'] = $val = $this->getFirstElem($prcdId);

						if ($val != '')
							$val = '"' . $val . '"';

						$prcdIdLen = strlen($prcdId);
						$formula = substr_replace($formula, $val, $prcdData['offset'] + $offsetCorr, $prcdIdLen);

						$offsetCorr += strlen($val) - $prcdIdLen;
					}
				}

				$uid = $dr->getUid();

				$wseUpdate[$uid] = array('n_refers_to' => $formula, 'n_location' => $dr->getLocation());
				$wseUids[] = $uid;
			}

			if (!count($wseUids))
				break;

			$cmds = array(array('wupd', '', $wseUpdate), array('wget', '', $wseUids, array('e_id', 'id', 'n_get_val'), (object) array()));

			$wsElems = ccmd($cmds);
			$wsElems = $wsElems[1][1];

			foreach ($wsElems as &$wsElem)
			{
				$this->genDataList($wsElem['e_id'], $wsElem['id'], $wsElem['n_get_val']);
				unset($drs[$wsElem['id']]);
			}

			if (!count($drs))
				break;
		}

		try
		{
			$this->activate();

			$this->_rewriteDeps();
		}
		catch (DynarangeException $de)
		{
			return array($de->err);
		}

		return array(array(true));
	}

	public function ec ($drId, array $elemCoords, array $idxPath)
	{
		try
		{
			$this->_setup();

			if (!isset($this->dynaranges[$drId]))
				throw new DynarangeException(array(false, 100, ''));

			$dr = $this->dynaranges[$drId];

			if (!$dr->expandCollapse($elemCoords, $idxPath))
				throw new DynarangeException(array(false, 100, ''));

			$this->_rewriteDeps();
		}
		catch (DynarangeException $de)
		{
			return array($de->err);
		}

		return array(array(true));
	}

}

class DynarangeException extends Exception
{
	public $err;

	public function __construct (array $err)
	{
		$this->err = $err;
	}
}

?>