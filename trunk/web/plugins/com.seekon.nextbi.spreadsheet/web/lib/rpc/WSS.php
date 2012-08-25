<?php

/*
 * \brief RPC routines
 *
 * \file WSS.php
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
 * Drazen Kljajic <drazen.kljajic@develabs.com>
 * Mladen Todorovic <mladen.todorovic@develabs.com>
 *
 * \version
 * SVN: $Id: WSS.php 3081 2010-04-07 13:15:46Z drazenk $
 *
 */

class WSS
{
	const UNDEFINED = -1;
	const SESSION_STORE_NAME = 'WSS';

	const MAX_COL = 255;
	const MAX_ROW = 65535;

	const Q_VALUE = 1;
	const Q_FORMULA = 2;
	const Q_STYLE = 4;
	const Q_FORMULA_WE = 8;
	const Q_ATTRS = 16;
	const Q_DIMS = 32;
	const Q_FMT_VAL = 64;
	const Q_FMT = 128;
	const Q_MERGE = 256;
	const Q_FORMULA_NF = 512;
	const Q_LOCK = 1024;
	const Q_ALL = 2013; // all except FORMULA and DIMS

	const D_NONE = 0;
	const D_COLS = 1;
	const D_ROWS = 2;
	const D_BOTH = 3;

	const WB_EXTENSION = '.wss';
	const WB_TEMPLATE = 'blank_workbook_template';
	const WB_TEMPLATE_RELPATH = '../../lib/templates/';
	const WB_INIT = 'Workbook#id';

	const RE_RANGE = '/^=\$([A-Z]+)\$([0-9]+)[:,]\$([A-Z]+)\$([0-9]+)(,.+)?$/';

	const MACRO_FILE_PFX = 'mmdl_';

	const SEP_DECIMAL = 0;
	const SEP_THOUSAND = 1;
	const SEP_ELEMENT = 2;

	const CCMD_STOP_ON_ERR = 1;

	private static $l10n_seps = array(
	  'en_US' => array('.', ',', ',')
	, 'de_DE' => array(',', '.', ';')
	, 'fr_FR' => array(',', '.', ';')
	, 'zh_CN' => array(',', '.', ';')
	);

	private static $l10n_bool = array(
	  'en_US' => array(1 => 'TRUE', 0 => 'FALSE')
	, 'de_DE' => array(1 => 'WAHR', 0 => 'FALSCH')
	, 'fr_FR' => array(1 => 'VRAI', 0 => 'FAUX')
	, 'zh_CN' => array(1 => 'TRUE', 0 => 'FALSE')
	);

	private static $l10n_book = array(
	  'en_US' => 'Workbook'
	, 'de_DE' => 'Arbeitsmappe'
	, 'fr_FR' => 'Classeur'
	, 'zh_CN' => 'Workbook'
	);

	private static $l10n_sheet = array(
	  'en_US' => 'Sheet'
	, 'de_DE' => 'Tabellenblatt'
	, 'fr_FR' => 'Feuille'
	, 'zh_CN' => 'Sheet'
	);

	private $curr_appid;
	private $curr_wbid;
	private $curr_wsid;

	private $worksheet_elems;
	private $worksheet_clones;

	private $cb_handles;
	private $hb_catalog;

	public $palo_handler;

	public function __construct ($auto_login = null)
	{
		if ($auto_login != null)
		{
			if (!isset($auto_login['wb']) || !isset($auto_login['grp']) || !isset($auto_login['hrc']))
				throw new Exception('Unable to perform auto login.');

			$this->cb_handles = array();
			$this->palo_handler = new Palo($this);
			$this->hb_catalog = new DynarangeCatalog();
			$this->worksheet_elems = new WorksheetElementsCollection();

			$res = json_decode(ccmd('[["oadd",0,"' . ('A' . mt_rand()) . '"]]'), true);

			$this->curr_appid = $res[0][1];
			ccmd('[["osel",0,"' . $this->curr_appid . '"]]');

			$ext = array('vars' => $auto_login['vars'], 'rPath' => $auto_login['rPath'], 'appmode' => $auto_login['appmode']);
			if (array_key_exists('opar', $auto_login))
				$ext['opar'] = $auto_login['opar'];

			 $_SESSION['preload'] = array_merge(array(array($auto_login['wb'], $auto_login['grp'], $auto_login['hrc'], $ext)), $this->load_workbook($auto_login['wb'], $auto_login['grp'], $auto_login['hrc'], $ext));
		}
		else if (isset($_SESSION[self::SESSION_STORE_NAME]))
		{
			$tmp = $_SESSION[self::SESSION_STORE_NAME];

			foreach ($tmp as $variable_name => $variable_value)
				$this->$variable_name = $variable_value;

			$this->cb_handles = array();
			$this->palo_handler = new Palo($this);
		}
		else
		{
			$this->cb_handles = array();
			$this->palo_handler = new Palo($this);
			$this->hb_catalog = new DynarangeCatalog();
			$this->worksheet_elems = new WorksheetElementsCollection();

			$res = json_decode(ccmd('[["oadd",0,"' . ('A' . mt_rand()) . '"]]'), true);

			$this->curr_appid = $res[0][1];
			ccmd('[["osel",0,"' . $this->curr_appid . '"]]');

			$this->load_workbook();
		}

		$this->cb_register('hb_run', array($this->hb_catalog, 'run'), false);
		$this->cb_register('hb_ec', array($this->hb_catalog, 'ec'), false);

		$this->cb_register('mc_getFuncFromState', 'MicroChartStreamer::getFuncFromState');
	}

	public function __destruct()
	{
		$this->cb_handles = null;
		$this->palo_handler = null;

		$_SESSION[self::SESSION_STORE_NAME] = $this;
	}

	public function getSeparator ($type = self::SEP_DECIMAL)
	{
		$l10n = $_SESSION['prefs']->search('general/l10n');

		return isset(self::$l10n_seps[$l10n][$type]) ? self::$l10n_seps[$l10n][$type] : self::$l10n_seps[$l10n][self::SEP_DECIMAL];
	}

	public function getPreload() {
		$preload = $_SESSION['preload'];
		unset($_SESSION['preload']);

		return $preload;
	}


	/*
	 * #############
	 * ### PROXY ###
	 * #############
	 */

	public function wsel ($name)
	{
		$func_args = func_get_args();
		$func_args[0] = $this;

		return call_user_func_array(array($this->worksheet_elems, $name), $func_args);
	}

	public function wsel_update ($worksheet_id, $arr_kv, $wsel_id = null)
	{
		if (!isset($wsel_id))
		{
			$res = json_decode(ccmd('[["wget","",[],["e_id"],{"e_type":"' . $arr_kv['e_type'] . '"}]]'), true);

			if ($res[0][0] && isset($res[0][1]) && isset($res[0][1][0]) && isset($res[0][1][0]['e_id']))
				$wsel_id = $res[0][1][0]['e_id'];
		}

		if (isset($wsel_id))
			ccmd(array(array('wupd', $worksheet_id, array($wsel_id => $arr_kv))));
		else
			ccmd(array(array('wadd', $worksheet_id, $arr_kv)));
	}

	public function wsel_get_value ($worksheet_id, $el_id, $type, $key)
	{
		$res = json_decode(ccmd('[["wget","' . $worksheet_id . '",[' . (isset($el_id) ? '"' . $el_id . '"' : '') . '],["' . $key . '"],{"e_type":"' . $type . '"}]]'), true);

		if ($res[0][0])
			return $res[0][1][0][$key];

		return null;
	}

	/*
	 * ######################
	 * ### WORKBOOK/SHEET ###
	 * ######################
	 */

	public function getCurrWbId() {
		return $this->curr_wbid;
	}

	public function getCurrWsId() {
		return $this->curr_wsid;
	}

	public function get_worksheet_elements() {
		return $this->worksheet_elems;
	}

	public function load_workbook($name = null, $group_name = null, $hierarchy_name = null, &$ext = null) {
		$used_vars = array();
		$blank = !isset($name);
		$from_studio = (isset($group_name) && isset($hierarchy_name));
		$from_reports = false;
		$save_recent = isset($ext['saverec']) ? $ext['saverec'] : true;
		$apol = $_SESSION['accessPolicy'];

		// if Studio initiated load, fetch name, path and meta data.
		if ($from_studio) {
			try {
				$node = null;
				$meta = '{"group": "' . $group_name . '", "hierarchy": "' . $hierarchy_name . '", "node": "' . $name . '", "relpath": "';

				$group = new W3S_Group($apol, $group_name);
				$hierarchy = $group->getHierarchy($hierarchy_name);

				$vars = $ext['vars'];

				if (isset($vars) && is_array($vars)) {
					$from_reports = true;
					$rep_node = $hierarchy->getNode($name);
					$meta .= $rep_node->getRelPath($rep_node, false) . '"';
					$meta .= ', "ghpath": "/' . $group->getData()->getName() . '/' . $hierarchy->getData()->getName() . '"';

					if ($group_name[0] == 'f') {
						$node = $rep_node;
						$maps = array();
					} else {
						$rep_node_data = $rep_node->getData();
						$rep_name = $rep_node_data->getName();
						$real_wb = $rep_node_data->getWorkbook();

						$group = new W3S_Group($apol, $real_wb->getGroup());
						$hierarchy = $group->getHierarchy($real_wb->getHierarchy());
						$node = $hierarchy->getNode($real_wb->getNode());

						$meta .= ', "fgroup": "' . $real_wb->getGroup() . '", "fhierarchy": "' . $real_wb->getHierarchy() . '", "fnode": "' . $real_wb->getNode() . '"';

						$maps = $rep_node_data->getMap();
					}

					if (array_key_exists('nodes', $vars) || array_key_exists('vars', $vars)) {
						foreach ($maps as $map_var => $map_list)

							if (get_class($map_list['list']) == 'W3S_LinkList') {
								if (array_key_exists($map_list['list']->getElement(), $vars['nodes']))
									$used_vars[$map_var] = $vars['nodes'][$map_list['list']->getElement()];

							} elseif (array_key_exists($map_var, $vars['vars']))
								$used_vars[$map_var] = $vars['vars'][$map_var];
					}

					if (array_key_exists('opar', $ext) && is_array($opars = json_decode($ext['opar'], true))) {
						$save_recent = false;

						foreach ($opars as $o_key => $o_val)
							$used_vars[$o_key] = $o_val;
					} else {
						$recent_vars = array();
						foreach ($vars as $type => $vlist)
							foreach ($vlist as $key => $val)
								$recent_vars[] = array('type' => $type, 'name' => $key, 'val' => $val);
					}
				} else {
					$node = $hierarchy->getNode($name);

					if (!isset($node))
						return array(false, 'follHLInvalidDoc');

					$meta .= $node->getRelPath($node, false) . '"';
					$meta .= ', "ghpath": "/' . $group->getData()->getName() . '/' . $hierarchy->getData()->getName() . '"';
				}

				if (!isset($node))
					return array(false, 'errLoadWB_noNode');

				$node_name = $name;
				$name = isset($rep_name) ? $rep_name : $node->getData()->getName();

				// Check Autosave files.
				if (!isset($ext) || !array_key_exists('asid', $ext)) {
					$node_dir = str_replace('\\', '/', $node->getParent()->getSysPath());
					$as_list = array('recov' => array());

					if (is_dir($node_dir) && ($dir_files = scandir($node_dir))) {
						$node_sys_name = $node->getUID() . '-' . urlencode($node->getData()->getName()) . self::WB_EXTENSION;
						$node_sys_name_len = strlen($node_sys_name) + 1;
						$node_sys_name_user = $node_sys_name . '.' . $apol->getUser() . '.';

						for ($i = array_search($node_sys_name, $dir_files), $len = count($dir_files); $i < $len; $i++)
							if (stripos($dir_files[$i], $node_sys_name) !== false) {
								$file_stat = stat($node_dir . '/' . $dir_files[$i]);

								if (!array_key_exists('orig', $as_list) && $dir_files[$i] === $node_sys_name)
									$as_list['orig'] = array(
															'meta' => array('ghn' => array('g' => $group_name, 'h' => $hierarchy_name, 'n' => $node_name), 'name' => $name),
															'date' => date('d.m.Y H:i:s', $file_stat['mtime']),
															'size' => $file_stat['size']
														);
								elseif (stripos($dir_files[$i], $node_sys_name_user) !== false)
									$as_list['recov'][] = array(substr($dir_files[$i], $node_sys_name_len), date('d.m.Y H:i:s', $file_stat['mtime']), $file_stat['size']);
							} else
								break;
					}

					if (count($as_list['recov']))
						return array(false, 'recoveryList', $as_list);
				}

				$path = str_replace('\\', '/', $node->getSysPath() . self::WB_EXTENSION);

				if (!file_exists($path))
					return array(false, 'errLoadWB_noFile');

				if (($perm = $node->getPermN()) < AccessPolicy::PERM_READ)
					return array(false, 'errLoadWB_noRights');

				$meta .= ', "name": "' . $name . '", "perm": "' . $perm . '", "hidden": ' . ($ext['hidden'] ? 'true'  : 'false') . '}';
			} catch (Exception $e) {
				return array(false, 'errLoadWB_noNode');
			}
		}

		// get list of loaded books
		if (!isset($ext['bels'])) {
			$llist = json_decode(ccmd('[["bels"]]'), true);
			$llist = $llist[0][0] === true && is_array($llist[0][1]) ? $llist[0][1] : array();
		} else
			$llist = $ext['bels'];

		$prev_wbid = $this->curr_wbid; $prev_wsid = $this->curr_wsid;

		if (!$blank)
			// Select already loaded workbook.
			for ($i = count($llist) - 1; $i >= 0; --$i)
				if ($from_studio ? (str_replace('\\', '/', $llist[$i]['path']) == $path) : ($llist[$i]['name'] == $name)) {
					try {
						$wbsel_ccmd = '[["osel",1,"' . ($this->curr_wbid = $llist[$i]['id']) . '"]';
						$lwb_meta = json_decode($llist[$i]['meta'], true);

						if (!$ext['hidden'] && $lwb_meta['hidden']) {
							$lwb_meta['hidden'] = false;
							$wbsel_ccmd .= ',["bcmd",' . json_encode(str_replace('\\', '', json_encode($lwb_meta))) . ']';
						}

						ccmd($wbsel_ccmd . ']');

						$sel_sheet_res = isset($ext['sheet_name']) ? $this->selectSheet(null, false, !($ext['appmode'] == 'user' || $from_reports), $ext['sheet_name']) : $this->selectSheet();
						if (!$sel_sheet_res[0]) {
							ccmd('[["osel",1,"' . ($this->curr_wbid = $prev_wbid) . '"]]');
							$this->selectSheet($prev_wsid);
							return array(false, 'follHLInvalidSheet');
						}

						if (isset($ext['nrange'])) {
							$nget_res = json_decode(ccmd('[["nget",[1,1,"' . $ext['nrange'] . '","' . $this->curr_wsid . '"]]]'), true);

							if (!$nget_res[0][0])
								$nget_res = json_decode(ccmd('[["nget",[1,1,"' . $ext['nrange'] . '",""]]]'), true);

							if (!$nget_res[0][0]) {
								ccmd('[["osel",1,"' . ($this->curr_wbid = $prev_wbid) . '"]]');
								$this->selectSheet($prev_wsid);
								return array(false, 'follHLInvTrgNRange');
							}
						}

						$imported = (($xlsx_pos = strripos($name, '.xlsx')) !== false && $xlsx_pos == strlen($name) - strlen('.xlsx'));

						if ($imported) {
							$name = str_ireplace('.xlsx', '', $name);
							ccmd('[["oren",1,' . json_encode($name) . ']]');
						}

						$res_arr = array('wbid' => $this->curr_wbid, 'name' => $name, 'imp' => !$imported, 'perm' => $perm);

						if (isset($ext['sheet_name']))
							$res_arr['wsid'] = $this->curr_wsid;

						if (isset($ext['cbkey'])) {
							$res_arr[$ext['cbkey']] = array('wsid' => $this->curr_wsid);

							if (isset($nget_res))
								$res_arr[$ext['cbkey']][$nget_res[0][1][0]['name']] = $nget_res[0][1][0]['refers_to'];
						}

						return array(true, $res_arr);
					} catch (Exception $e) {
						return array(false, 'errLoadWB_selErr');
					}
				}

		try {
			if ($from_studio && count($resources = $node->getData()->getResources())) {
				if (!$ext['hidden'])
					$dep_ext = array('asid' => '', saverec => false, 'hidden' => true, 'lmark' => array($group_name . '-' . $hierarchy_name . '-' . $node_name), 'bels' => $llist);
				else {
					$dep_ext = $ext;
					$dep_ext['lmark'][] = $group_name . '-' . $hierarchy_name . '-' . $node_name;
				}

				foreach ($resources as $resource) {
					if (in_array($resource['group'] . '-' . $resource['hierarchy'] . '-' . $resource['node'], $dep_ext['lmark'], true))
						return array(false, 'errLoadWB_cyclicDep');

					$load_res = $this->load_workbook($resource['node'], $resource['group'], $resource['hierarchy'], $dep_ext);

					if (!$load_res[0])
						return $load_res;
				}
			}

			$l10n = $_SESSION['prefs']->search('general/l10n');

			if ($from_studio) {
				$lpath = $path . (isset($ext) && array_key_exists('asid', $ext) && strlen($ext['asid']) > 0 ? '.' . $ext['asid'] : '');
				$ccmd = '[["load",' . json_encode($lpath) . ',' . json_encode($meta) . ',true,' . ($ext['appmode'] == 'user' ? 'true' : 'false') . '],["oren",1,' . json_encode($name) . ']]';
			} else{
				$templateFilePath = str_replace('\\', '/', realpath(self::WB_TEMPLATE_RELPATH . self::WB_TEMPLATE . '_' . $l10n . self::WB_EXTENSION));
				$dirPath = 'C:/wss/lib/templates';
				$realFilePath = $dirPath.'/'. self::WB_TEMPLATE . '_' . $l10n . self::WB_EXTENSION;
				if (file_exists($realFilePath) == false) {
					if(file_exists($dirPath) == false){
						mkdir($dirPath, 0777, true);
					}
					$fp = fopen($realFilePath, "w+");
					fclose($fp);
					
					copy($templateFilePath, $realFilePath);
				}
				
				$ccmd = '[["load",' . json_encode($realFilePath) . ',"",true,true]]';
			}
			
			$res = json_decode(ccmd($ccmd), true);

			ccmd('[["osel",1,"' . ($this->curr_wbid = $res[0][1]) . '"]]');

			if ($from_studio)
				$llist[] = array('id' => $this->curr_wbid, 'name' => $name, 'path' => $lpath);

			if (isset($used_vars))
			{
				$set_vars_ccmd = '[';

				foreach ($used_vars as $var_name => $var_value)
					$set_vars_ccmd .= '["svar",' . json_encode($var_name) . ',' . json_encode($var_value) . '],';

				if (($ccmd_len = strlen($set_vars_ccmd)) > 1)
				{
					$set_vars_ccmd[$ccmd_len - 1] = ']';
					ccmd($set_vars_ccmd);
				}
			}

			$sel_sheet_res = $this->selectSheet(null, false, !($ext['appmode'] == 'user' || $from_reports), $ext['sheet_name']);
			if (!$sel_sheet_res[0]) {
				$del_wbid = $this->curr_wbid;
				ccmd('[["osel",1,"' . ($this->curr_wbid = $prev_wbid) . '"]]');
				$this->selectSheet($prev_wsid);
				$this->removeBook($del_wbid);
				return array(false, 'follHLInvalidSheet');
			}

			if (isset($ext['nrange'])) {
				$nget_res = json_decode(ccmd('[["nget",[1,1,"' . $ext['nrange'] . '","' . $this->curr_wsid . '"]]]'), true);

				if (!$nget_res[0][0])
					$nget_res = json_decode(ccmd('[["nget",[1,1,"' . $ext['nrange'] . '",""]]]'), true);

				if (!$nget_res[0][0]) {
					$del_wbid = $this->curr_wbid;
					ccmd('[["osel",1,"' . ($this->curr_wbid = $prev_wbid) . '"]]');
					$this->selectSheet($prev_wsid);
					$this->removeBook($del_wbid);
					return array(false, 'follHLInvTrgNRange');
				}
			}

			if ($blank) {
				$id = isset($_SESSION['blwbcnum']) ? $_SESSION['blwbcnum'] + 1 : 1;
				$it = 1;

				do {
					$name = self::$l10n_book[$l10n] . $id;

					$wb_exists = false;
					for ($i = count($llist) - 1; $i >= 0; --$i)
						if ($llist[$i]['name'] == $name) {
							$wb_exists = true;
							break;
						}

					if (!$wb_exists) {
						ccmd('[["oren",1,' . json_encode($name) . ']]');
						break;
					} elseif ($id < 1000000)
						$id++;
					else {
						$id = 1;
						$it++;
					}
				} while ($it <= 2);

				$_SESSION['blwbcnum'] = $id;

				$init_sheets = $this->getSheets();
				if (($loc_sheet_name = str_replace('Sheet', self::$l10n_sheet[$l10n], $init_sheets[0][1])) != $init_sheets[0][1])
					ccmd('[["oren",2,"' . $loc_sheet_name . '"]]');
			}

			// load sheet elements and palo data into session
			$wb_elems_storage = array();
			$wb_palo_storage = array();
			$wb_pagesetup_storage = array();

			$list = json_decode(ccmd('[["olst",2]]'), true);
			$list = $list[0][0] && is_array($list[0][1]) ? $list[0][1] : array();

			foreach ($list as $sheetUid => $sheetName) {
				$el_map = $this->wsel_get_value($sheetUid, null, 'charts', 'map');
				if (isset($el_map))
					$wb_elems_storage[$sheetUid] = unserialize(gzuncompress(base64_decode($el_map)));

				$palo_map = $this->wsel_get_value($sheetUid, null, 'palo', 'map');
				if (isset($palo_map))
					$wb_palo_storage[$sheetUid] = unserialize(gzuncompress(base64_decode($palo_map)));

				$pagesetup_map = $this->wsel_get_value($sheetUid, null, 'pagesetup', 'map');
				if (isset($pagesetup_map))
					$wb_pagesetup_storage[$sheetUid] = unserialize(gzuncompress(base64_decode($pagesetup_map)));
			}

			if (count($wb_elems_storage) > 0) {
				$this->worksheet_elems->set_workbook_storage($this->curr_wbid, $wb_elems_storage);

				// Refresh source data for all charts on current sheet.
				$res = json_decode(ccmd('[["wget","' . $this->curr_wsid . '",[],["e_id","n_get_val","n_refers_to"],{"e_type":"chart"}]]'), true);

				if ($res[0][0])
					foreach($res[0][1] as $chart_el)
						$this->worksheet_elems->refresh_source_data($this->curr_wbid, $this->curr_wsid, $chart_el['e_id'], $chart_el['n_get_val'], $chart_el['n_refers_to'], self::$l10n_seps[$l10n][self::SEP_ELEMENT]);
			}

			$this->palo_handler->set_palo_data($this->curr_wbid, $wb_palo_storage);

			if (count($wb_pagesetup_storage) > 0)
				$this->palo_handler->set_page_setup_data($this->curr_wbid, $wb_pagesetup_storage);

			// Load Macros
			$this->exportMacros();

			// Auto-correct Workbook name if copied from blank_workbook_template.
			if ($this->getWbName($this->curr_wbid) == self::WB_TEMPLATE . '_' . $l10n)
				ccmd('[["oren",1,' . json_encode($name) . '],["save","' . $this->curr_wbid . '",""]]');

			$res_arr = array('wbid' => $this->curr_wbid, 'name' => $name, 'imp' => false, 'perm' => $perm);

			if (isset($ext['sheet_name']))
				$res_arr['wsid'] = $this->curr_wsid;

			if (isset($ext['cbkey'])) {
				$res_arr[$ext['cbkey']] = array('wsid' => $this->curr_wsid);

				if (isset($nget_res))
					$res_arr[$ext['cbkey']][$nget_res[0][1][0]['name']] = $nget_res[0][1][0]['refers_to'];
			}

			// Add to list of recent items.
			if ($save_recent && !$blank) {
				$meta_arr = json_decode($meta, true);
				$location = array(
					'group' => $meta_arr['group'],
					'hierarchy' => $meta_arr['hierarchy'],
					'node' => $meta_arr['node'],
					'path' => $from_reports && array_key_exists('rPath', $ext) ? $ext['rPath'] : '/' . $meta_arr['ghpath'] . $meta_arr['relpath']
				);

				if ($from_reports && count($recent_vars))
					$location['var'] = $recent_vars;

				$recent = new Recent($apol);
				$recent->add($from_reports || $ext['appmode'] == 'user' ? 'reports' : 'files', 'spreadsheet', $location);
				$recent->save();
			}

			return array(true, $res_arr);
		} catch (Exception $e) {
			return array(false, 'errLoadWB_coreErr');
		}
	}

	private function sess_to_wsel() {
		// Save workbook elements and palo data from session.
		$wb_elems_storage = $this->worksheet_elems->get_workbook_storage($this->curr_wbid);
		$wb_palo_storage = $this->palo_handler->get_palo_data($this->curr_wbid);
		$wb_pagesetup_storage = $this->palo_handler->get_page_setup_data($this->curr_wbid);

		if ($wb_elems_storage != null || count($wb_palo_storage) > 0 || count($wb_pagesetup_storage) > 0) {
			if ($wb_elems_storage != null)
				foreach ($wb_elems_storage as $ws_id => $ws_elems)
					$this->wsel_update($ws_id, array('e_type' => 'charts', 'map' => base64_encode(gzcompress(serialize($ws_elems), 1))));

			if (count($wb_palo_storage) > 0)
				foreach ($wb_palo_storage as $ws_id => $ws_palo_data)
					$this->wsel_update($ws_id, array('e_type' => 'palo', 'map' => base64_encode(gzcompress(serialize($ws_palo_data), 1))));

			if (count($wb_pagesetup_storage) > 0)
				foreach ($wb_pagesetup_storage as $ws_id => $ws_pagesetup_data)
					$this->wsel_update($ws_id, array('e_type' => 'pagesetup', 'map' => base64_encode(gzcompress(serialize($ws_pagesetup_data), 1))));
		}
	}

	public function save_workbook($group, $hierarchy, $name, $parent_name = null) {
		// Get list of used variables in workbook.
		$var_list = json_decode(ccmd('[["gbvl"]]'), true);
		$var_list = $var_list[0][0] && is_array($var_list[0][1]) ? $var_list[0][1] : array();

		$apol = $_SESSION['accessPolicy'];
		$conn = $apol->getSuperConn();

		if (isset($parent_name)) {
			$slist = json_decode(ccmd('[["bels"]]'), true);
			$slist = $slist[0][0] === true && is_array($slist[0][1]) ? $slist[0][1] : array();

			for ($i = count($slist) - 1; $i >= 0; --$i)
				if (strlen($slist[$i]['meta']) && $slist[$i]['name'] == $name)
					return array(false, 'errWBSave_nameExists', array('name' => $name));

			$g = new W3S_Group($apol, $group);
			$h = $g->getHierarchy($hierarchy);
			$parent_node = $h->getNode($parent_name);

			if (!isset($parent_node))
				return array(false, 'errWBSave_noParent', array('name' => $parent_name));

			if ($parent_node->getPermN() < AccessPolicy::PERM_WRITE)
				return array(false, 'errWBSave_noParentAccess');

			$wb_meta = array('name' => $name, 'desc' => $name);
			if (count($var_list) > 0)
				$wb_meta['vars'] = $var_list;

			$wb_id = $parent_node->addNode(true, 'workbook', $wb_meta)->getUID();
			$path = str_replace('\\', '/', $parent_node->getSysPath()) . '/' . $wb_id . '-' . urlencode($name) . self::WB_EXTENSION;

			$node = $h->getNode($wb_id);
			$meta = isset($node) ? '{"group": "' . $group . '", "hierarchy": "' . $hierarchy . '", "node": "' . $wb_id . '", "relpath": "' . $node->getRelPath($node, false) .
					'", "ghpath": "/' . $g->getData()->getName() . '/' . $h->getData()->getName() . '", "name": "' . $name . '", "perm": "' . AccessPolicy::PERM_DELETE . '", "hidden": false}' : '';

			$this->sess_to_wsel();
			ccmd('[["oren",1,' . json_encode($name) . '],["bcmd",' . json_encode($meta) . '],["save","' . $this->curr_wbid . '",' . json_encode($path) . ']]');

			// Add to list of recent items.
			$recent = new Recent($apol);
			$recent->add('files', 'spreadsheet', array('group' => $group, 'hierarchy' => $hierarchy, 'node' => $wb_id, 'path' => '//' . $g->getData()->getName() . '/' . $h->getData()->getName() . $parent_node->getRelPath($parent_node, false) . '/' . $name));
			$recent->save();

			return array(true, array('wbid' => $this->curr_wbid, 'group' => $group, 'hierarchy' => $hierarchy, 'node' => $wb_id, 'perm' => AccessPolicy::PERM_DELETE));
		} else {
			$g = new W3S_Group($apol, $group);
			$h = $g->getHierarchy($hierarchy);
			$n = $h->getNode($name);

			if ($n->getPermN() < AccessPolicy::PERM_WRITE)
				return array(false, 'errWBSave_noAccess');

	        $this->sess_to_wsel();
			W3S_NodeData::setVariables($conn, $group, $hierarchy, $name, $var_list);
			ccmd('[["save","' . $this->curr_wbid . '",""]]');
			return array(true, array('wbid' => $this->curr_wbid));
		}
	}

	public function getWbName ($uid = null)
	{
		if ($uid == null)
			$uid = $this->curr_wbid;

		$list = json_decode(ccmd('[["olst",1]]'), true);

		if ($list[0][0] !== true)
			return false;

		$list = &$list[0][1];

		return isset($list[$uid]) ? $list[$uid] : false;
	}

	// add a sheet if it doesn't already exist and also select it
	public function addSheet($nextSheetId = '') {
		$sheets = $this->getSheets();

		$i = 0;
		$name_found = false;
		$l10n = $_SESSION['prefs']->search('general/l10n');

		do {
			$name = self::$l10n_sheet[$l10n] . ++$i;
			if (!in_array($name, $sheets[0])) {
				$name_found = true;
				break;
			}
		} while ($id < 1000);

		$name = ($name_found) ? $name : 'SheetA';

		try
		{
			$res = json_decode(ccmd('[["oadd",2,' . json_encode($name) . ',' . json_encode($nextSheetId) . ']]'), true);
			$this->curr_wsid = $res[0][1];

			ccmd('[["osel",2,"' . $this->curr_wsid . '"]]');
		}
		catch (Exception $e)
		{
			return array('errcode' => 1);
		}

		return array('errcode' => 0, 'wsid' => $this->curr_wsid, 'name' => $name);
	}

	public function copySheet ($sheetUid, $nextSheetUid = '', $bookUid = '')
	{
		$res = json_decode(ccmd('[["scp","' . $sheetUid . '","' . $nextSheetUid . '","' . $bookUid . '"]]'), true);

		if ($res[0][0] === false)
			return array('errcode' => $res[0][1], 'errdesc' => $res[0][2]);

		$res = json_decode(ccmd('[' . ($bookUid == '' ? '' : '["osel",1,"' . ($this->curr_wbid = $bookUid) . '"],') . '["osel",2,"' . ($this->curr_wsid = $res[0][1]) . '"],["olst",2]]'), true);

		return array('errcode' => 0, 'wsid' => $this->curr_wsid, 'name' => $res[$bookUid == '' ? 1 : 2][1][$this->curr_wsid]);
	}

	public function moveSheet ($sheetUid, $nextSheetUid = '', $bookUid = '')
	{
		$res = json_decode(ccmd('[["smv","' . $sheetUid . '","' . $nextSheetUid . '","' . $bookUid . '"]' . ($bookUid == '' ? ',["olst",2]' : '') . ']'), true);

		if ($res[0][0] === false)
			return array('errcode' => $res[0][1], 'errdesc' => $res[0][2]);

		if ($bookUid == '')
			$sheetName = $res[1][1][$this->curr_wsid];
		else
		{
			$res = json_decode(ccmd('[["osel",1,"' . ($this->curr_wbid = $bookUid) . '"],["osel",2,"' . ($this->curr_wsid = $res[0][1]) . '"],["olst",2]]'), true);
			$sheetName = $res[2][1][$this->curr_wsid];
		}

		return array('errcode' => 0, 'wsid' => $this->curr_wsid, 'name' => $sheetName);
	}

	public function addCloneWorksheet() {
		$curr_wsid = $this->curr_wsid;

		try {
			$new_wsid = json_decode(ccmd('[["sclo","' . ('S' . mt_rand()) . '"]]'), true);

			if ($new_wsid[0][0] !== true)
				return false;

			$new_wsid = $new_wsid[0][1];

			$this->worksheet_elems->copy_worksheet_storage($this->curr_wbid, $curr_wsid, $new_wsid);
			$this->worksheet_clones[$new_wsid] = $curr_wsid;
			ccmd('[["osel",2,"' . ($this->curr_wsid = $new_wsid) . '"]]');

			return $new_wsid;
		} catch(Exception $e) {
			ccmd('[["osel",2,"' . ($this->curr_wsid = $curr_wsid) . '"]]');
			return false;
		}
	}

	public function removeCloneWorksheet() {
		try {
			$cloned_wsid = $this->curr_wsid;
			ccmd('[["osel",2,"' . ($this->curr_wsid = $this->worksheet_clones[$cloned_wsid]) . '"]]');
			unset($this->worksheet_clones[$cloned_wsid]);
			ccmd('[["srcl","' . $cloned_wsid . '"]]');
			$this->worksheet_elems->remove_worksheet_storage($this->curr_wbid, $cloned_wsid);

			return $this->curr_wsid;
		} catch(Exception $e) {
			return false;
		}
	}

	public function getSheets ($bookUid = null)
	{
		$res = json_decode(ccmd('[["olst",2' . ($bookUid === null ? '' : ',"' . $bookUid . '"') . ']]'), true);

		if ($res[0][0] !== true || !is_array($res[0][1]))
			return array();

		$list = array();

		foreach ($res[0][1] as $uid => $name)
			array_push($list, $uid, $name);

		return array($list, $this->curr_wsid);
	}

	public function getLoadedBooks ()
	{
		$res = json_decode(ccmd('[["bels"]]'), true);

		if ($res[0][0] !== true || !is_array($res[0][1]))
			return array();

		$list = array();

		foreach ($res[0][1] as &$book)
			$list[] = array($book['id'], $book['name'], $book['meta']);

		return $list;
	}

	public function removeBook ($uid)
	{
		// Get the list of loaded workbooks.
		$bels = json_decode(ccmd('[["bels"]]'), true);

		if ($bels[0][0] !== true)
			return false;

		if (!is_array($bels = $bels[0][1]) || !count($bels))
			return true;

		$bels_metas = array();

		foreach($bels as $wb)
			if ($wb['id'] == $uid)
				$wb_meta = !strlen($wb['meta']) ? array() : json_decode($wb['meta'], true);
			elseif (strlen($wb['meta'])) {
				$ometa = json_decode($wb['meta'], true);
				$ometa['id'] = $wb['id'];

				$bels_metas[] = $ometa;
			}

		if (!isset($wb_meta))
			return true;

		if (count($wb_meta) && count($bels_metas)) {
			try {
				$apol = $_SESSION['accessPolicy'];
				$g_idx = $wb_meta['group'][0] == 'r' ? 'f' : '';

				$g = new W3S_Group($apol, $wb_meta[$g_idx . 'group']);
				$h = $g->getHierarchy($wb_meta[$g_idx . 'hierarchy']);
				$n = $h->getNode($wb_meta[$g_idx . 'node']);

				if (!isset($n))
					return false;

				if (count($deps = $n->getData()->getDependents()))
					foreach ($bels_metas as $bels_meta) {
						$g_idx = $bels_meta['group'][0] == 'r' ? 'f' : '';

						foreach ($deps as $dep)
							if ($dep['group'] == $bels_meta[$g_idx . 'group'] && $dep['hierarchy'] == $bels_meta[$g_idx . 'hierarchy'] && $dep['node'] == $bels_meta[$g_idx . 'node']) {
								if (!$wb_meta['hidden']) {
									$wb_meta['hidden'] = true;
									ccmd('[["bcmd",' . json_encode(str_replace('\\', '', json_encode($wb_meta))) . ',"' . $uid . '"]]');
								}

								return true;
							}
					}
			} catch (Exception $e) {
				return false;
			}
		}

		$del_res = json_decode(ccmd('[["odel",1,"' . $uid . '"]]'), true);

		if ($del_res[0][0] !== true)
			return false;

		if (isset($n) && count($ress = $n->getData()->getResources())) {
				foreach ($bels_metas as $bels_meta) {
					$g_idx = $bels_meta['group'][0] == 'r' ? 'f' : '';

					foreach ($ress as $res)
						if ($bels_meta['hidden'] && $res['group'] == $bels_meta[$g_idx . 'group'] && $res['hierarchy'] == $bels_meta[$g_idx . 'hierarchy'] && $res['node'] == $bels_meta[$g_idx . 'node'])
							$this->removeBook($bels_meta['id']);
				}
		}

		$list = json_decode(ccmd('[["olst",1]]'), true);

		if (!count($list[0][1]))
			$this->curr_wsid = $this->curr_wbid = 0;

		return true;
	}

	public function selectSheet ($uid = null, $cloned = false, $designer_mode = true, $name = null, $tmp = false)
	{
		if ($uid !== null && $cloned)
		{
			ccmd('[["osel",2,"' . ($this->curr_wsid = $uid) . '"]]');
			return array(true, false, $this->curr_wsid);
		}

		$list = json_decode(ccmd('[["olst",2]]'), true);

		if ($list[0][0] !== true || !is_array($list = $list[0][1]) || !count($list))
			return array(false);

		if ($name !== null)
		{
			if (($uid = array_search($name, $list)) === false)
				return array(false);
		}
		else if ($uid !== null && !isset($list[$uid]))
				return array(false);

		reset($list);

		$wsid = $uid !== null ? $uid : key($list);

		if (!$tmp)
			$this->curr_wsid = $wsid;

		ccmd('[["osel",2,"' . $wsid . '"]]');

		// clone if necessary
		if (!$cloned && !$designer_mode)
		{
			$res = json_decode(ccmd('[["wget","",[],["e_id"],{"e_type":"hb"}]]'), true);

			if ($res[0][0] && count($res[0][1]) && $this->addCloneWorksheet() !== false)
			{
				$this->cb('hb_run', array());
				return array(true, true, $this->curr_wsid);
			}
		}

		return array(true, false, $this->curr_wsid);;
	}

	public function selectBook ($uid, $designer_mode = true, $sel_first_sheet = true)
	{
		$res = json_decode(ccmd('[["osel",1,"' . $uid . '"]]'), true);

		if ($res[0][0] !== true)
			return array(false);

		$this->curr_wbid = $uid;

		// make the first sheet in the book active, otherwise return false
		$res = $this->selectSheet($sel_first_sheet ? null : $this->curr_wsid, false, $designer_mode);

		return array(true, $res[1], $res[2]);
	}

	public function resolveNodePath($path, $type = 'file') {
		$path_elems = explode('/', $path[0] == '/' ? substr($path, 1) : $path, 3);

		if (count($path_elems) < 3)
			return null;

		$ghnt = array();
		$apol = $_SESSION['accessPolicy'];

		// Resolve Group.
		$groups = W3S_Group::listGroups($apol, array($type));

		foreach ($groups as $group => $meta)
			if ($meta['name'] == $path_elems[0]) {
				$ghnt['g'] = $group;
				break;
			}

		if (!isset($ghnt['g']))
			return null;

		$group = new W3S_Group($apol, $ghnt['g']);

		// Resolve Hierarchy.
		$hierarchies = $group->listHierarchies();

		foreach ($hierarchies as $hierarchy => $meta)
			if ($meta['name'] == $path_elems[1]) {
				$ghnt['h'] = $hierarchy;
				break;
			}

		if (!isset($ghnt['h']))
			return null;

		$hierarchy = $group->getHierarchy($ghnt['h']);

		// Resolve Node.
		$node = $hierarchy->getNodeByPath($path_elems[2]);

		if (!isset($node))
			return null;

		$ghnt['n'] = $node->getUID();
		$ghnt['t'] = $node->getData()->getEffectiveType();

		return $ghnt;
	}


	/*
	 * ##############
	 * ### EXPORT ###
	 * ##############
	 */

	public function exportRange ($range = null)
	{
		if ($range == null)
		{
			$res = json_decode(ccmd('[["gurn"]]'), true);
			$res = $res[0][1];

			$range = array(1, 1, $res[0], $res[1]);
		}

		$query_what = self::Q_VALUE | self::Q_STYLE | self::Q_FMT_VAL | self::Q_MERGE;

		$ccmd = '[["gusl"],["gval",' . $query_what . ',' . $range[0] . ',' . $range[1] . ',' . $range[2] . ',' . $range[3] . '],["gdcr",2],'
					.  '["gcr",0,' . $range[0] . ',' . ($range[2] - 1) . '],["gcr",1,' . $range[1] . ',' . ($range[3] - 1) . ']]';

		$res = json_decode(ccmd($ccmd), true);

		$l10n = array_shift($res);
		$l10n = $l10n[1];

		$cells = &$res[0][1];

		$rows = array();

		for ($i = -1, $r = $range[1]; $r <= $range[3]; ++$r)
		{
			$row = array();

			for ($c = $range[0]; $c <= $range[2]; ++$c)
			{
				$cell = array();

				if (($v = $cells[++$i]) !== '')
				{
					if (is_numeric($v) && !is_string($v))
					{
						$v = strval($v);

						if (($dec_sep = self::$l10n_seps[$l10n][self::SEP_DECIMAL]) != '.')
							$v = str_replace('.', $dec_sep, $v);

						$t = 'n';
					}
					else if (is_bool($v))
					{
						$v = self::$l10n_bool[$l10n][$v];
						$t = 'b';
					}
					else
						$t = substr($v, 0, 1) == '<' ? 'h' : 's';

					$cell['v'] = $v;
					$cell['t'] = $t;
				}

				if (($v = $cells[++$i]) !== '')
					$cell['s'] = $v;

				if (($v = $cells[++$i]) !== '')
					$cell['v'] = $v;

				if (($v = $cells[++$i]) !== '')
					$cell['m'] = $v;

				$row[] = $cell;
			}

			$rows[] = $row;
		}


		$def_dims = &$res[1][1];

		$col_widths = array_fill(0, $range[2] - $range[0] + 1, $def_dims[0]);
		$row_heights = array_fill(0, $range[3] - $range[1] + 1, $def_dims[1]);

		$col_w_sp = &$res[2][1];
		for ($cnt = count($col_w_sp), $i = 0; $i < $cnt; )
			$col_widths[$col_w_sp[$i++] - $range[0] + 1] = $col_w_sp[$i++];

		$row_h_sp = &$res[3][1];
		for ($cnt = count($row_h_sp), $i = 0; $i < $cnt; )
			$row_heights[$row_h_sp[$i++] - $range[0] + 1] = $row_h_sp[$i++];

		return array('col_widths' => $col_widths, 'row_heights' => $row_heights, 'rows' => $rows);
	}


	/*
	 * ##############
	 * ### MACROS ###
	 * ##############
	 */

  public function exportMacros ()
  {
		$ccmd = '[["ocurr",1],["bget","",[],["e_id","name","src"],{"e_type":"macromdl"}],["em","php","em.php","gt",[]]]';

		$res = json_decode(ccmd($ccmd), true);

		$bookUid = $res[0][1];
		$mdls = &$res[1][1];

		if (!count($mdls))
			return true;


		$tmppath = $res[2][1];

		if (!is_writable($tmppath))
			return false;

		$tmppath = $tmppath . DIRECTORY_SEPARATOR . self::MACRO_FILE_PFX . $bookUid . '.php';

		if (file_exists($tmppath))
		{
			if (is_dir($tmppath))
				return false;

			unlink($tmppath);
		}


		$fp = fopen($tmppath, 'a');
		fwrite($fp, "<?php\n\n");


		foreach ($mdls as $idx => &$mdl)
		{
			fwrite($fp, '// ' . $mdl['name'] . ' (' . $mdl['e_id'] . ")\n\n");
			fwrite($fp, $mdl['src']);
			fwrite($fp, "\n\n\n");
		}

		fwrite($fp, '?>');
		fclose($fp);

		ccmd('[["em","php","register.php","_",[]]]');

		return true;
  }


	/*
	 * ################
	 * ### GRID OPS ###
	 * ################
	 */

	private static function _lettersToNumber ($ltrs)
	{
		$num = 0;

		for ($f = 1, $i = strlen($ltrs = strtoupper($ltrs)) - 1; $i >= 0; --$i, $f *= 26)
			$num += (ord($ltrs[$i]) - 64) * $f;

		return $num;
	}

	private function _handle_changed_wsels (array &$res)
	{
		if (array_shift($res) !== true)
			return $res = array();

		$l10n = $_SESSION['prefs']->search('general/l10n');

		$ui_ccmd = array();

		foreach ($res as $idx => &$cmd)
			switch ($cmd[0])
			{
				case 'wmvd':

					$wmv_cmd = array('wmv');

					foreach ($cmd[1] as &$wsel)
					{
						$pos = preg_match(self::RE_RANGE, $wsel['n_location'], $pos) ? array(self::_lettersToNumber($pos[1]), intval($pos[2]), self::_lettersToNumber($pos[3]), intval($pos[4])) : array(0, 0, 0, 0);

						$elem = array('id' => $wsel['e_id'], 'type' => $wsel['e_type'], 'loc' => $wsel['n_location'], 'pos' => $pos, 'offsets' => $wsel['pos_offsets']);

						if ($wsel['e_type'] == 'formel')
							$elem['formel_type'] = $wsel['formel_type'];

						$wmv_cmd[] = $elem;
					}

					if (count($wmv_cmd) > 1)
						$ui_ccmd[] = $wmv_cmd;

					unset($res[$idx]);

					break;

				case 'wdrt':

					$rw_cmd = array('rw');
					$rf_cmd = array('rf');

					foreach ($cmd[1] as &$wsel)
						switch ($wsel['e_type'])
						{
							case 'chart':
								if ($this->worksheet_elems->refresh_source_data($this->curr_wbid, $this->curr_wsid, $wsel['e_id'], $wsel['n_get_val'], $wsel['n_refers_to'], self::$l10n_seps[$l10n][self::SEP_ELEMENT]))
									$rw_cmd[] = $wsel['e_id'];
								break;

							case 'hb':
								$this->hb_catalog->genDataList($wsel['e_id'], $wsel['id'], $wsel['n_get_val'], true);
								break;

							case 'formel':
								$rf_cmd[] = array('id' => $wsel['e_id'], 'type' => $wsel['formel_type'], 'val' => $wsel['n_get_val'], 'ref' => $wsel['n_refers_to']);
								break;
						}

					if (count($rw_cmd) > 1)
						$ui_ccmd[] = $rw_cmd;

					if (count($rf_cmd) > 1)
						$ui_ccmd[] = $rf_cmd;

					unset($res[$idx]);

					break;

				case 'wtrd':

					if (count($cmd[1]))
					{
						array_unshift($cmd[1], 'wtrd');
						$ui_ccmd[] = $cmd[1];
					}

					unset($res[$idx]);

					break;
			}

		return $ui_ccmd;
	}

  public function recalc (array $range, $auto_calc = true)
	{
		$ccmd = '["bcc"],["grar",' . self::Q_ALL . ',' . implode(',', $range) . '],["gcwe"],["ecc"],["gcr",0,1,' . self::MAX_COL . '],["gcr",1,1,' . self::MAX_ROW . '],["gurn"]';
		$ccmd = $auto_calc ? ('[' . $ccmd . ']') : ('[["sac",1],' . $ccmd . ',["sac",0]]');

		$ccmd = json_decode(ccmd($ccmd), true);

		if (!$auto_calc)
		{
			array_shift($ccmd);
			array_pop($ccmd);
		}

		array_shift($ccmd);

		if (!is_array($ccmd[0]) || $ccmd[0][0] !== true)
			return array();

		array_splice($ccmd[0], 0, 1, array('crgn', (object) array()));

		array_splice($ccmd, 3, 2, array(array('ncr', $ccmd[3][1], $ccmd[4][1])));

		$ccmd[4] = $ccmd[4][1];
		array_unshift($ccmd[4], 'curn');

		array_splice($ccmd, 1, 2, $this->_handle_changed_wsels($ccmd[1]));

		return array($ccmd);
	}

	public function cb_register ($handle, $method_ptr, $passive = true)
	{
		$handle = strval($handle);

		if (isset($this->cb_handles[$handle]) || !is_callable($method_ptr))
			return false;

		$this->cb_handles[$handle] = array($method_ptr, $passive);

		return true;
	}

	public function cb ($handle, $params, $grid = null, $dimension_set = self::D_BOTH, $property_set = self::Q_ALL)
	{
		if (!isset($this->cb_handles[$handle]))
			return '[]';

		$hndl = $this->cb_handles[$handle];

		if (!is_callable($hndl[0]))
			return '[]';

		$l10n = $_SESSION['prefs']->search('general/l10n');

		if ($hndl[1])
		{
			if (!is_array($ccmd = call_user_func_array($hndl[0], $params)))
				return '[]';

			if ($l10n != 'en_US')
			{
				array_unshift($ccmd, array('susl', 'en_US'));
				$ccmd[] = array('susl', $l10n);
			}

			return $this->exec(json_encode($ccmd), $grid, $dimension_set, $property_set, self::CCMD_STOP_ON_ERR);
		}

		$ccmd = array();

		if ($grid)
			$ccmd[] = array('snpc', $grid[0][0], $grid[0][1], $grid[1][0], $grid[1][1], $property_set, $dimension_set);

		if ($l10n != 'en_US')
			$ccmd[] = array('susl', 'en_US');

		if (count($ccmd))
			ccmd($ccmd);

		$res = call_user_func_array($hndl[0], $params);

		$wsel_res = json_decode(ccmd('[["gcwe"]]'), true);

		$ui_ccmd = $this->_handle_changed_wsels($wsel_res[0]);

		$ccmd = array();

		if ($l10n != 'en_US')
			$ccmd[] = array('susl', $l10n);

		if ($grid)
			$ccmd[] = array('snpd');

		if (count($ccmd))
		{
			$diff_res = ccmd($ccmd);

			if ($l10n != 'en_US')
				array_shift($diff_res);

			if (isset($diff_res[0]))
			{
				array_splice($ui_ccmd, -1, 0, $this->_handle_changed_wsels($diff_res[0]));
				array_splice($ui_ccmd, 0, 0, $diff_res[0]);
			}
		}

		return array($ui_ccmd, $res);
	}

	public function exec ($ccmd, $grid = null, $dimension_set = self::D_BOTH, $property_set = self::Q_ALL, $flags = -1)
	{
		if ($grid == null)
			return ccmd($ccmd);

		$ccmd = substr_replace(substr_replace($ccmd, '["snpc",' . $grid[0][0] . ',' . $grid[0][1] . ',' . $grid[1][0] . ',' . $grid[1][1] . ',' . $property_set . ',' . $dimension_set . '],', 1, 0), ',["gcwe"]', -1, 0);

		$res = json_decode(ccmd($ccmd, $flags), true);
		array_shift($res);

		$ui_ccmd = $res[count($res) - 1][0] ? $this->_handle_changed_wsels(array_pop($res)) : array();

		$diff_res = json_decode(ccmd('[["snpd"]]'), true);

		array_splice($ui_ccmd, -1, 0, $this->_handle_changed_wsels($diff_res[0]));
		array_splice($ui_ccmd, 0, 0, $diff_res[0]);

		return array($ui_ccmd, $res);
	}


	/*
	 * ############
	 * ### MISC ###
	 * ############
	 */

	private function _inflateDate ($d)
	{
		return substr($d, 0, 4) . '-' . substr($d, 4, 2) . '-' . substr($d, 6, 2) . ' ' . substr($d, 8, 2) . ':' . substr($d, 10, 2) . ':' . substr($d, 12, 2) . ' ' . (substr($d, 14, 1) == '0' ? '+' : '-') . substr($d, 15, 4);
	}

	public function getVersions ()
	{
		$core_ver = json_decode(ccmd('[["gsi"]]'), true);

		if ($core_ver[0][0] !== true || !is_string($core_ver[0][1]))
			$core_ver = array('n/a', 'n/a', 'n/a');
		else if (preg_match('/^.+version ([0-9\.]+) \(build: ([0-9]{19}), repository: ([0-9]{19})\)$/', $core_ver[0][1], $core_ver))
			$core_ver = array(CFG_VERSION . '.' . substr($core_ver[1], strrpos($core_ver[1], '.') + 1), $this->_inflateDate($core_ver[3]), $this->_inflateDate($core_ver[2]));
		else
			$core_ver = array('n/a', 'n/a', 'n/a');


		$lib_ver = palo_version();
		$lib_ver = array(isset($lib_ver['libpalo']) ? $lib_ver['libpalo'] : 'n/a', isset($lib_ver['php_palo']) ? $lib_ver['php_palo'] : 'n/a');


		$ch = curl_init('http://' . CFG_PALO_HOST . ':' . CFG_PALO_PORT . '/server/info');

		curl_setopt_array($ch, array(
		  CURLOPT_HEADER => false
		, CURLOPT_RETURNTRANSFER => true
		, CURLOPT_FAILONERROR => true
		));

		if (($palo_ver = curl_exec($ch)) === false)
			$palo_ver = 'n/a';
		else
			$palo_ver = implode('.', array_slice(split(';', $palo_ver), 0, 4));

		return array('core' => $core_ver, 'lib' => $lib_ver, 'palo' => $palo_ver);
	}


	/*
	 * ###############
	 * ### TESTING ###
	 * ###############
	 */

	public function ccmd ($ccmd, $flags = -1, $sess_id = '')
	{
		return ccmd($ccmd, $flags, $sess_id);
	}

}

?>