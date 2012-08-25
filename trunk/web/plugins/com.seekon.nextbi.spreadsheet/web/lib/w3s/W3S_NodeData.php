<?php

/*
 * @brief ajax
 *
 * @file Group.js
 *
 * Copyright (C) 2006-2009 Jedox AG
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
 * Drazen Kljajic <drazen.kljajic@develabs.com>
 *
 * \version
 * SVN: $Id: W3S_NodeData.php 2946 2010-03-17 11:43:27Z drazenk $
 *
 */

class W3S_NodeData extends W3S_BaseData {

	// Constants
	public static $def_subtype = 'unknown';
	private static $types = array('folder', 'rfolder', 'template', 'workbook', 'hyperlink', 'rhyperlink', 'static', 'rstatic', 'urlplugin', 'rurlplugin');
	public static $subtypesStatic = array('pdf', 'html', 'htm', 'jpg', 'gif', 'png', 'txt', 'rtf', 'doc', 'ppt', 'pps', 'csv', 'xls', 'xlsx', 'zip', 'rar');
	public static $subtypesUrlplugin = array('ahview');
	private static $mapTypes = array('rfolder', 'template');
	private static $typesWithReferences = array('template', 'rhyperlink', 'rstatic', 'rurlplugin');
	private static $typesWithReferrers = array('workbook', 'hyperlink', 'static', 'urlplugin');
	private static $typesWithResources = array('workbook');
	private static $typesWithDependents = array('workbook');
	private static $typesWithHyperlinks = array('hyperlink', 'rhyperlink', 'urlplugin');
	private static $typesWithParams = array('urlplugin', 'rurlplugin');
	private static $listClasses = array('W3S_LinkList', 'W3S_PaloList', 'W3S_PaloSubsetList', 'W3S_ODBCList', 'W3S_ConstList', 'W3S_ConstValue');
	private static $linkClasses = array('W3S_URLLink');
	private static $pluginConfMap = array('ahview_url' => CFG_AHVIEWER_PATH);

	public static $referrer = 'referrer';
	public static $dependent = 'dependent';
	public static $resource = 'resource';

	private $ref;
	private $vars = array();

	/*
		referrers:
		array of W3S_NodeLink objects
	 */
	private $referrers = array();

	/*
		resources:
		array of W3S_NodeLink objects
	 */
	private $resources = array();

	/*
		dependents:
		array of W3S_NodeLink objects
	 */
	private $dependents = array();

	/*
	 	maps:
		array(
			'rfolder' => array('defval' => '2000', 'list' => W3S_LinkList | W3S_PaloList | W3S_ODBCList | W3S_ConstList | W3S_ConstValue)
			or
			'Year' => array('defval' => '2000', 'list' => W3S_LinkList | W3S_PaloList | W3S_ODBCList | W3S_ConstList | W3S_ConstValue, 'ordnum' => 0)
			...
		);
	 */
	private $maps = array();

	/*
	 	hyperlinks:
	 	array('url' => W3S_URLLink)
	 */
	private $hyperlinks = array();

	/*
	 	params:
	 	array('key1' => 'value1', 'key2' => 'value2', ...)
	 */
	private $params = array();

	/**
	 * Constructor.
	 * @param array Basic node data:
	 * 		array(
	 * 			'el' => 'e0',
	 * 			'type' => 'folder' | 'rfolder' | 'template' | 'workbook' | 'hyperlink' | 'rhyperlink' | 'static' | 'rstatic' | 'urlplugin' | 'rurlplugin',
	 * 			'subtype' => 'ahview' | 'some other subtype',
	 * 			'name' => 'Some name',
	 * 			'desc' => 'Some description',
	 * 			'ref' => array('group' => 'some_grp', 'hierarchy' => 'h1', 'node' => 'n2', 'type' => 'workbook'|'hyperlink'|'html'|'pdf'|... ),
	 * 			'hyperlink' => array('type' => 'url', 'target' => 'self', 'url' => 'http://host.domain.tld/'),
	 * 			'params' => array('viewtabs' => 'yes', 'navigator' => 'no', ...)
	 * 		)
	 */
	public function __construct($params = null) {
		parent::__construct($params);

		if (isset($params)) {
			$this->chkData(array('element' => $params['el'], 'type' => $params['type']));

			$this->element = $params['el'];
			$this->type = $this->subtype = $params['type'];

			switch ($this->type) {

				// workbook
				case 'workbook':
					if (array_key_exists('vars', $params))
						$this->vars = array_unique($params['vars']);
					break;

				// template
				case 'template':
					$ref = array_key_exists('ref', $params) ? $params['ref'] : $params['workbook'];
					$this->ref = new W3S_NodeLink($ref['group'], $ref['hierarchy'], $ref['node'], $ref['type']);
					break;

				// hyperlink
				case 'hyperlink':
					if (array_key_exists('hyperlink', $params))
						$this->setHyperlinkArr($params['hyperlink']);
					break;

				// rhyperlink
				case 'rhyperlink':
					if (array_key_exists('hyperlink', $params))
						$this->setHyperlinkArr($params['hyperlink']);
					else
						$this->ref = new W3S_NodeLink($params['ref']['group'], $params['ref']['hierarchy'], $params['ref']['node'], $params['ref']['type']);
					break;

				// static
				case 'static':
					foreach (self::$subtypesStatic as $ext) {
						$ext_length = strlen($ext) + 1;

						if (substr(strtolower($this->name), $ext_length * -1) == '.' . $ext) {
							$this->name = substr($this->name, 0, $ext_length * -1);
							$this->subtype = $ext;
							break;
						}
					}

					if ($this->type == $this->subtype)
						$this->subtype = self::$def_subtype;

					break;

				// rstatic
				case 'rstatic':
					$this->ref = new W3S_NodeLink($params['ref']['group'], $params['ref']['hierarchy'], $params['ref']['node'], $params['ref']['type']);
					$this->subtype = $params['ref']['type'];
					break;

				// urlplugin
				case 'urlplugin':
					$this->subtype = in_array($params['subtype'], self::$subtypesUrlplugin, true) ? $params['subtype'] : self::$def_subtype;
					$this->setHyperlinkArr(array_key_exists('hyperlink', $params) ? $params['hyperlink'] : array('type' => 'url', 'target' => 'self', 'url' => self::$pluginConfMap[$this->subtype . '_url']));

					if (array_key_exists('params', $params) && is_array($params['params']))
						$this->params = $params['params'];

					break;

				// rurlplugin
				case 'rurlplugin':
					$this->ref = new W3S_NodeLink($params['ref']['group'], $params['ref']['hierarchy'], $params['ref']['node'], $params['ref']['type']);
					$this->subtype = in_array($params['ref']['type'], self::$subtypesUrlplugin, true) ? $params['ref']['type'] : self::$def_subtype;

					if (array_key_exists('params', $params) && is_array($params['params']))
						$this->params = $params['params'];

					break;
			}
		}
	}

	public function __clone() {
		$maps_clone = array();

		foreach ($this->maps as $key => $value) {
			if (get_class($value['list']) == 'W3S_LinkList')
				continue;

			$maps_clone[$key] = array('defval' => $value['defval'], 'list' => clone $value['list']);
		}

		$this->maps = $maps_clone;
		$this->dependents = array();
	}

	/**
	 * Check data function.
	 * @access private
	 * @param array Data to check: array('element' => $el, 'type' => $type, 'name' => $name)
	 */
	protected function chkData($data) {
		foreach ($data as $key => $value) {
			switch ($key) {

				// Check element.
				case 'element':
					if (!preg_match('/^(n)(\\d+)$/is', $value))
						throw new WSS_Exception('W3S_NodeData-inv_el', array(), 'Invalid element.');
					break;

				// Check type.
				case 'type':
					if (!in_array($value, self::$types, true))
						throw new WSS_Exception('W3S_NodeData-inv_el_type', array(), 'Invalid element type.');
					break;

				// Check list.
				case 'list':
					if (!in_array(get_class($value), self::$listClasses, true))
						throw new WSS_Exception('W3S_NodeData-inv_list', array(), 'Invalid list.');
					break;

				// Check subtype for static.
				case 'subtype_static':
					if ($value != self::$def_subtype && !in_array($value, self::$subtypesStatic, true))
						throw new WSS_Exception('W3S_NodeData-inv_static_type', array(), 'Invalid subtype in static node.');
					break;

				// Check subtype for urlplugin.
				case 'subtype_urlplugin':
					if ($value != self::$def_subtype && !in_array($value, self::$subtypesUrlplugin, true))
						throw new WSS_Exception('W3S_NodeData-inv_plugin_type', array(), 'Invalid subtype in plugin node.');
					break;
			}
		}
	}

	/**
	 * XML import function.
	 * @access public
	 * @param string XML data to import.
	 */
	public function import($data) {
		$root = null; $xpath = null;
		$this->importInit($data, $root, $xpath);
		$this->chkData(array('element' => $this->element, 'type' => $this->type));

		// Process variables.
		if ($this->type == 'workbook') {
			$nl_vars = $xpath->query('vars', $root);
			$this->chkNode('vars', $nl_vars, false, false);

			if ($nl_vars->length > 0)
				$this->importVars($xpath, $nl_vars->item(0));
		}

		// Process hyperlinks.
		if (in_array($this->type, self::$typesWithHyperlinks, true)) {
			$nl_hyperlink = $xpath->query('hyperlink', $root);
			$this->chkNode('hyperlink', $nl_hyperlink, $this->type == 'hyperlink' || $this->type == 'urlplugin', false);

			if ($nl_hyperlink->length > 0)
				$this->importHyperlink($xpath, $nl_hyperlink->item(0));
		}

		// Process reference.
		if (in_array($this->type, self::$typesWithReferences, true) && !($this->type == 'rhyperlink' && count($this->hyperlinks) > 0)) {
			$nl_ref = $xpath->query('ref', $root);

			try {
				$this->chkNode('ref', $nl_ref, true, false);
			} catch (WSS_Exception $wsse) {
				if ($wsse->getId() == 'W3S_BaseData-no_mandatory_el' && $this->type == 'template') {
					$nl_ref = $xpath->query('workbook', $root);
					$this->chkNode('workbook', $nl_ref, true, false);
				} else
					throw $wsse;
			}

			$this->importReference($xpath, $nl_ref->item(0));
		}

		// Process referrers.
		if (in_array($this->type, self::$typesWithReferrers, true)) {
			$nl_referrers = $xpath->query('referrers', $root);
			$this->chkNode('referrers', $nl_referrers, false, false);

			if ($nl_referrers->length)
				$this->importLinks($xpath, $nl_referrers->item(0), self::$referrer, $this->referrers);
		}

		// Process resources.
		if (in_array($this->type, self::$typesWithResources, true)) {
			$nl_resources = $xpath->query('resources', $root);
			$this->chkNode('resources', $nl_resources, false, false);

			if ($nl_resources->length)
				$this->importLinks($xpath, $nl_resources->item(0), self::$resource, $this->resources);
		}

		// Process dependents.
		if (in_array($this->type, self::$typesWithDependents, true)) {
			$nl_dependents = $xpath->query('dependents', $root);
			$this->chkNode('dependents', $nl_dependents, false, false);

			if ($nl_dependents->length)
				$this->importLinks($xpath, $nl_dependents->item(0), self::$dependent, $this->dependents);
		}

		// Process maps.
		if (in_array($this->type, self::$mapTypes, true)) {
			$nl_maps = $xpath->query('maps', $root);
			$this->chkNode('maps', $nl_maps, false, false);
			$this->importMaps($xpath, $nl_maps->item(0));
		}

		// Process subtype.
		if ($this->type == 'static') {
			$nl_file = $xpath->query('file', $root);
			$this->chkNode('file', $nl_file, true, false);
			$this->importFile($xpath, $nl_file->item(0));
		}

		// Process URL plugin subtype.
		if ($this->type == 'urlplugin') {
			$nl_plugin = $xpath->query('plugin', $root);
			$this->chkNode('plugin', $nl_plugin, true, false);
			$this->importPlugin($xpath, $nl_plugin->item(0));
		}

		// Process params.
		if (in_array($this->type, self::$typesWithParams, true)) {
			$nl_params = $xpath->query('params', $root);
			$this->chkNode('params', $nl_params, false, false);

			if ($nl_params->length)
				$this->importParams($xpath, $nl_params->item(0));
		}
	}

	private function importVars(&$xpath, $vars) {
		$this->vars = array();

		// Process var.
		$nl_var = $xpath->query('var', $vars);
		$this->chkNode('var', $nl_var, false, true);

		for ($i = 0; $i < $nl_var->length; $i++) {
			$el_var = $nl_var->item($i);

			if (!$el_var->hasAttribute('name'))
				throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'var', 'attr' => 'name'), 'No "name" attribute in var element.');

			if (in_array($el_var->getAttribute('name'), $this->vars, true))
				continue;

			$this->vars[] = $el_var->getAttribute('name');
		}
	}

	private function importReference(&$xpath, $el_ref) {
		if (!$el_ref->hasAttribute('group'))
			throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'ref', 'attr' => 'group'), 'No "group" attribute in ref element.');

		if (!$el_ref->hasAttribute('hierarchy'))
			throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'ref', 'attr' => 'hierarchy'), 'No "hierarchy" attribute in ref element.');

		if (!$el_ref->hasAttribute('node'))
			throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'ref', 'attr' => 'node'), 'No "node" attribute in ref element.');

		$ref_type = $el_ref->hasAttribute('type') ? $el_ref->getAttribute('type') : 'unknown';

		$this->ref = new W3S_NodeLink($el_ref->getAttribute('group'), $el_ref->getAttribute('hierarchy'), $el_ref->getAttribute('node'), $ref_type);

		if ($this->type == 'rstatic' || $this->type == 'rurlplugin')
			$this->subtype = $ref_type;
	}

	private function importLinks(&$xpath, $links, $type, &$storage) {
		$storage = array();

		// Process links.
		$nl_link = $xpath->query($type, $links);
		$this->chkNode($type, $nl_link, false, true);

		for ($i = 0, $nl_link_len = $nl_link->length; $i < $nl_link_len; $i++) {
			$el_link = $nl_link->item($i);

			if (!$el_link->hasAttribute('group'))
				throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => $type, 'attr' => 'group'), 'No "group" attribute in ' . $type . ' element.');

			if (!$el_link->hasAttribute('hierarchy'))
				throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => $type, 'attr' => 'hierarchy'), 'No "hierarchy" attribute in ' . $type . ' element.');

			if (!$el_link->hasAttribute('node'))
				throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => $type, 'attr' => 'node'), 'No "node" attribute in ' . $type . ' element.');

			$link_type = $el_link->hasAttribute('type') ? $el_link->getAttribute('type') : 'unknown';

			$storage[] = new W3S_NodeLink($el_link->getAttribute('group'), $el_link->getAttribute('hierarchy'), $el_link->getAttribute('node'), $link_type);
		}
	}

	private function importMaps(&$xpath, $maps) {
		$this->maps = array();

		// Process map.
		$nl_map = $xpath->query('map', $maps);
		$this->chkNode('map', $nl_map, false, $this->type == 'template');

		for ($i = 0; $i < $nl_map->length; $i++) {
			// Process list.
			$nl_list = $xpath->query('list', $nl_map->item($i));
			$this->chkNode('maps/map' . $i . '/list', $nl_list, true, false);
			$el_list = $nl_list->item(0);

			$el_var;
			if ($this->type == 'template') {
				// Process var.
				$nl_var = $xpath->query('var', $nl_map->item($i));
				$this->chkNode('maps/map' . $i . '/var', $nl_var, true, false);
				$el_var = $nl_var->item(0);

				if (!$el_var->hasAttribute('name'))
					throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'var', 'attr' => 'name'), 'No "name" attribute in var element.');

				if (!$el_var->hasAttribute('defval'))
					throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'var', 'attr' => 'defval'), 'No "defval" attribute in var element.');

				$ord_num = 0;
				if ($nl_map->item($i)->hasAttribute('ordnum')) {
					$attr_ord_num = $nl_map->item($i)->getAttribute('ordnum');

					if (is_int($attr_ord_num + 0))
						$ord_num = $attr_ord_num;
				}

				$this->maps[$el_var->getAttribute('name')] = array(
					'defval' => $el_var->getAttribute('defval'),
					'list' => $this->importList($xpath, $el_list),
					'ordnum' => $ord_num
				);
			} else
				$this->maps['rfolder'] = array('defval' => null, 'list' => $this->importList($xpath, $el_list));
		}
	}

	private function importHyperlink(&$xpath, $el_hyperlink) {
		if (!$el_hyperlink->hasAttribute('type'))
			throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'hyperlink', 'attr' => 'type'), 'No "type" attribute in hyperlink element.');

		if (!$el_hyperlink->hasAttribute('target'))
			throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'hyperlink', 'attr' => 'target'), 'No "target" attribute in hyperlink element.');

		$params = array('type' => $el_hyperlink->getAttribute('type'), 'target' => $el_hyperlink->getAttribute('target'));

		switch ($el_hyperlink->getAttribute('type')) {
			// URL.
			case 'url':
				// Process data.
				$nl_data = $xpath->query('data', $el_hyperlink);
				$this->chkNode('hyperlink/data', $nl_data, true, false);
				$el_data = $nl_data->item(0);

				if (!$el_data->hasChildNodes() || strlen($el_data->nodeValue) == 0)
					throw new WSS_Exception('W3S_NodeData-hldata_empty', array(), 'No content in hyperlink/data element.');

				$params['url'] = $el_data->nodeValue;

				break;
		}

		$this->setHyperlinkArr($params);
	}

	private function importFile(&$xpath, $el_file) {
		if (!$el_file->hasAttribute('type'))
			throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'file', 'attr' => 'type'), 'No "type" attribute in file element.');

		$this->chkData(array('subtype_static' => $el_file->getAttribute('type')));
		$this->subtype = $el_file->getAttribute('type');
	}

	private function importPlugin(&$xpath, $el_plugin) {
		if (!$el_plugin->hasAttribute('type'))
			throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'plugin', 'attr' => 'type'), 'No "type" attribute in plugin element.');

		$this->chkData(array('subtype_urlplugin' => $el_plugin->getAttribute('type')));
		$this->subtype = $el_plugin->getAttribute('type');
	}

	private function importList(&$xpath, $el_list) {
		$list = null;

		switch ($el_list->getAttribute('type')) {
			// Link list.
			case 'link':
				if ($this->type == 'rfolder')
					throw new WSS_Exception('W3S_NodeData-no_linklist_on_rfolder', array(), 'Report folder can\'t be mapped to link list.');
				else {
					$link_id = $el_list->getAttribute('id');
					$this->chkData(array('element' => $link_id));
					$list = new W3S_LinkList($link_id);
				}
				break;

			// Palo list.
			case 'palo':
				// Process conn.
				$nl_conn = $xpath->query('conn', $el_list);
				$this->chkNode('list/conn', $nl_conn, true, false);
				$el_conn = $nl_conn->item(0);

				if (!$el_conn->hasAttribute('name'))
					throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'list/conn', 'attr' => 'name'), 'No "name" attribute in list/conn element.');

				if (!$el_conn->hasAttribute('db'))
					throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'list/conn', 'attr' => 'db'), 'No "db" attribute in list/conn element.');

				// Process values.
				$nl_values = $xpath->query('values', $el_list);
				$this->chkNode('list/value', $nl_values, true, false);
				$el_values = $nl_values->item(0);

				if (!$el_values->hasAttribute('type'))
					throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'list/values', 'attr' => 'type'), 'No "type" attribute in list/values element.');

				if (!$el_values->hasAttribute('dimension'))
					throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'list/values', 'attr' => 'dimension'), 'No "dimension" attribute in list/values element.');

				$list = new W3S_PaloList($el_conn->getAttribute('name'), $el_conn->getAttribute('db'), $el_values->getAttribute('dimension'), $el_values->hasAttribute('subset') ? $el_values->getAttribute('subset') : null);
				break;

			// Palo subset list.
			case 'palosubset':
				// Process values.
				$nl_values = $xpath->query('values', $el_list);
				$this->chkNode('list/value', $nl_values, true, false);
				$el_values = $nl_values->item(0);

				if (!$el_values->hasChildNodes() || strlen($el_values->nodeValue) == 0)
					throw new WSS_Exception('W3S_NodeData-elvals_empty', array(), 'No content in list/values element.');

				$list = new W3S_PaloSubsetList($el_values->nodeValue);
				break;

			// ODBC list.
			case 'odbc':
				// Process conn.
				$nl_conn = $xpath->query('conn', $el_list);
				$this->chkNode('list/conn', $nl_conn, true, false);
				$el_conn = $nl_conn->item(0);

				if (!$el_conn->hasAttribute('name'))
					throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'list/conn', 'attr' => 'name'), 'No "name" attribute in list/conn element.');

				if (!$el_conn->hasAttribute('db'))
					throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'list/conn', 'attr' => 'db'), 'No "db" attribute in list/conn element.');

				// Process values.
				$nl_values = $xpath->query('values', $el_list);
				$this->chkNode('list/value', $nl_values, true, false);
				$el_values = $nl_values->item(0);

				if (!$el_values->hasAttribute('type'))
					throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'list/values', 'attr' => 'type'), 'No "type" attribute in list/values element.');

				if (!$el_values->hasChildNodes() || strlen($el_values->nodeValue) == 0)
					throw new WSS_Exception('W3S_NodeData-elvals_empty', array(), 'No content in list/values element.');

				$list = new W3S_ODBCList($el_conn->getAttribute('name'), $el_conn->getAttribute('db'), $el_values->getAttribute('type'), $el_values->nodeValue);
				break;

			// Const List
			case 'clist':
				// Process value.
				$nl_value = $xpath->query('value', $el_list);
				$this->chkNode('list/value', $nl_value, true, true);

				$val_list = array();
				for ($i = 0; $i < $nl_value->length; $i++)
					$val_list[] = $nl_value->item($i)->nodeValue;

				$list = new W3S_ConstList($val_list);
				break;

			// Const Value
			case 'cval':
				// Process value.
				$nl_value = $xpath->query('value', $el_list);
				$this->chkNode('list/value', $nl_value, true, false);

				$list = new W3S_ConstValue($nl_value->item(0)->nodeValue);
				break;
		}

		if (isset($list))
			return $list;
		else
			throw new WSS_Exception('W3S_NodeData-unsupported_list_type', array('type' => $el_list->getAttribute('type')), 'List of type "' . $el_list->getAttribute('type') . '" is not supported.');
	}

	private function importParams(&$xpath, $params) {
		$this->params = array();

		// Process params.
		$nl_param = $xpath->query('param', $params);
		$this->chkNode('param', $nl_param, false, true);

		for ($i = 0; $i < $nl_param->length; $i++) {
			// Process param.
			$el_param = $nl_param->item($i);

			if (!$el_param->hasAttribute('name'))
				throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'param', 'attr' => 'name'), 'No "name" attribute in param element.');

			if (!$el_param->hasAttribute('value'))
				throw new WSS_Exception('W3S_NodeData-el_no_attr', array('el' => 'param', 'attr' => 'value'), 'No "value" attribute in param element.');

			$this->params[$el_param->getAttribute('name')] = $el_param->getAttribute('value');
		}
	}

	/**
	 * XML export function.
	 * @access public
	 * @return string XML data.
	 */
	public function export() {
		$dom = $this->exportInit();
		$el_element = $dom->documentElement;

		if ($this->type == 'workbook') {
			// Create vars element.
			$el_vars = $dom->createElement('vars');

			foreach ($this->vars as $value) {
				$el_var = $dom->createElement('var');
				$el_var->setAttribute('name', $value);
				$el_vars->appendChild($el_var);
			}

			$el_element->appendChild($el_vars);
		}

		if (in_array($this->type, self::$typesWithReferences, true) && !($this->type == 'rhyperlink' && count($this->hyperlinks) > 0)) {
			// Create ref element.
			$el_ref = $dom->createElement('ref');

			$el_ref->setAttribute('group', $this->ref->getGroup());
			$el_ref->setAttribute('hierarchy', $this->ref->getHierarchy());
			$el_ref->setAttribute('node', $this->ref->getNode());
			$el_ref->setAttribute('type', $this->ref->getType());

			$el_element->appendChild($el_ref);
		}

		if (in_array($this->type, self::$typesWithReferrers, true))
			// Create referrers element.
			$el_element->appendChild($this->exportLink($dom, self::$referrer, $this->referrers));

		if (in_array($this->type, self::$typesWithResources, true))
			// Create resources element.
			$el_element->appendChild($this->exportLink($dom, self::$resource, $this->resources));

		if (in_array($this->type, self::$typesWithDependents, true))
			// Create dependents element.
			$el_element->appendChild($this->exportLink($dom, self::$dependent, $this->dependents));

		if (in_array($this->type, self::$typesWithHyperlinks, true)) {
			foreach ($this->hyperlinks as $key => $value) {
				// Create hyperlink element.
				$el_hyperlink = $dom->createElement('hyperlink');

				$el_hyperlink->setAttribute('type', $value->getType());
				$el_hyperlink->setAttribute('target', $value->getTarget());

				$el_data = $dom->createElement('data');
				$data = $value->getLink();
				$el_data->appendChild($dom->createCDATASection($data[$value->getType()]));

				$el_hyperlink->appendChild($el_data);
				$el_element->appendChild($el_hyperlink);

				break; // Currently only one hyperlink allowed.
			}
		}

		if ($this->type == 'static') {
			// Create file element.
			$el_file = $dom->createElement('file');
			$el_file->setAttribute('type', $this->subtype);

			$el_element->appendChild($el_file);
		}

		if ($this->type == 'urlplugin') {
			// Create plugin element.
			$el_plugin = $dom->createElement('plugin');
			$el_plugin->setAttribute('type', $this->subtype);

			$el_element->appendChild($el_plugin);
		}

		if (in_array($this->type, self::$mapTypes, true)) {
			// Create maps element.
			$el_maps = $dom->createElement('maps');

			foreach ($this->maps as $key => $value) {
				$el_map = $dom->createElement('map');

				// Create var element.
				if ($this->type == 'template') {
					$el_map->setAttribute('ordnum', $value['ordnum']);

					$el_var = $dom->createElement('var');
					$el_var->setAttribute('name', $key);
					$el_var->setAttribute('defval', $value['defval']);
					$el_map->appendChild($el_var);
				}

				// Create list element.
				$el_list = $dom->createElement('list');
				$el_list->setAttribute('type', $value['list']->getType());

				switch (get_class($value['list'])) {
					// Link List
					case 'W3S_LinkList':
						$el_list->setAttribute('id', $value['list']->getElement());
						break;

					// Palo List
					case 'W3S_PaloList':
						// Create conn element.
						$el_conn = $dom->createElement('conn');
						$el_conn->setAttribute('name', $value['list']->getConnection());
						$el_conn->setAttribute('db', $value['list']->getDb());
						$el_list->appendChild($el_conn);

						// Create value element.
						$vals = $value['list']->getList();

						$el_values = $dom->createElement('values');
						$el_values->setAttribute('type', $vals['type']);
						$el_values->setAttribute('dimension', $vals['dimension']);

						if (isset($vals['subset']))
							$el_values->setAttribute('subset', $vals['subset']);

						$el_list->appendChild($el_values);
						break;

					// Palo Subset List
					case 'W3S_PaloSubsetList':
						// Create value element.
						$el_values = $dom->createElement('values');
						$el_values->appendChild($dom->createCDATASection($value['list']->getData()));

						$el_list->appendChild($el_values);
						break;

					// ODBC List
					case 'W3S_ODBCList':
						// Create conn element.
						$el_conn = $dom->createElement('conn');
						$el_conn->setAttribute('name', $value['list']->getConnection());
						$el_conn->setAttribute('db', $value['list']->getDb());
						$el_list->appendChild($el_conn);

						// Create value element.
						$vals = $value['list']->getList();

						$el_values = $dom->createElement('values', $vals['list']);
						$el_values->setAttribute('type', $vals['type']);
						$el_list->appendChild($el_values);
						break;

					// Const List
					case 'W3S_ConstList':
						foreach($value['list']->getValues() as $val) {
							$el_val = $dom->createElement('value', $val);
							$el_list->appendChild($el_val);
						}
						break;

					// W3S_ConstValue
					case 'W3S_ConstValue':
						$el_val = $dom->createElement('value', $value['list']->getValue());
						$el_list->appendChild($el_val);
						break;
				}

				$el_map->appendChild($el_list);
				$el_maps->appendChild($el_map);
			}

			$el_element->appendChild($el_maps);
		}

		if (in_array($this->type, self::$typesWithParams, true)) {
			// Create params element.
			$el_params = $dom->createElement('params');

			foreach ($this->params as $key => $value) {
				$el_param = $dom->createElement('param');
				$el_param->setAttribute('name', $key);
				$el_param->setAttribute('value', $value);

				$el_params->appendChild($el_param);
			}

			$el_element->appendChild($el_params);
		}

		return $dom->saveXML($el_element);
	}

	private function exportLink($dom, $type, $storage) {
		// Create link element.
		$el_links = $dom->createElement($type . 's');

		foreach ($storage as $link) {
			$el_link = $dom->createElement($type);

			$el_link->setAttribute('group', $link->getGroup());
			$el_link->setAttribute('hierarchy', $link->getHierarchy());
			$el_link->setAttribute('node', $link->getNode());

			$el_links->appendChild($el_link);
		}

		return $el_links;
	}

	// ###################
	// Getters and setters
	// ###################

	/**
	 * Set map.
	 * @param W3S_LinkList|W3S_PaloList|W3S_ODBCList|W3S_ConstList|W3S_ConstValue List object.
	 * @param string Default (fallback) value (optional - not needed by rfolder).
	 * @param string Template variable (optional - not needed by rfolder).
	 */
	public function setMap($list, $defval = null, $var = null, $ord_num = 0) {
		$this->chkData(array('list' => $list));

		switch ($this->type) {
			// Report folder
			case 'rfolder':
				$this->maps['rfolder'] = array('defval' => $defval, 'list' => $list);
				break;

			// Template
			case 'template':
				if (!isset($var) || strlen($var) == 0)
					throw new WSS_Exception('W3S_NodeData-no_tpl_var', array(), 'Tamplate variable is not set.');

				$this->maps[$var] = array('defval' => $defval, 'list' => $list, 'ordnum' => $ord_num);
				break;

			default:
				throw new WSS_Exception('W3S_NodeData-err_addmap', array(), 'Unable to add map to this node type.');
		}
	}

	/**
	 * Delete all maps or only one named in argument.
	 * @param string Template variable. Note: If not set delete all maps.
	 */
	public function deleteMap($var = null) {
		switch ($this->type) {
			// Report folder
			case 'rfolder':
				$this->maps = array();
				break;

			// Template
			case 'template':
				if (!isset($var) || strlen($var) == 0)
					$this->maps = array();
				else
					unset($this->maps[$var]);

				break;

			default:
				throw new WSS_Exception('W3S_NodeData-err_del_map', array(), 'Unable to delete map from this node type.');
		}
	}

	/**
	 * Get map data.
	 *
	 * Report folder - returns only a list object.
	 * Template - returns whole map or the list object if variable name is specified in argument.
	 * @param string Template variable. Note: If not set fetch all maps.
	 * @return array Map in format: if 'rfolder' or one var than array('defval' => 'Default value', 'list' => W3S_PaloList) or if all vars than array('Year' => array('defval' => '2005', 'list' => W3S_LinkList)).
	 */
	public function getMap($var = null) {
		switch ($this->type) {
			// Report folder
			case 'rfolder':
				return $this->maps['rfolder'];
				break;

			// Template
			case 'template':
				if (!isset($var) || strlen($var) == 0)
					return $this->maps;
				else
					return $this->maps[$var];

				break;

			default:
				throw new WSS_Exception('W3S_NodeData-err_get_map', array(), 'Unable to get map from this node type.');
		}
	}

	/**
	 * Rearrange mapped variables.
	 *
	 * @param array Assoc array variable/order number map with chnaged items only. Format: array('Years' => 1, 'Products' => 2)
	 * @return void.
	 */
	public function rearrangeMaps(array $var_ords) {
		if ($this->type != 'template')
			throw new WSS_Exception('W3S_NodeData-err_ordnum_not_supported', array(), 'Unable to rearrange maps on this node type.');

		foreach ($var_ords as $var => $ord_num)
			if (is_int($ord_num += 0) && $ord_num >= 0) {
				if (array_key_exists($var, $this->maps))
					$this->maps[$var]['ordnum'] = $ord_num;
			} else
				throw new WSS_Exception('W3S_NodeData-err_ordnum', array(), 'Unable to rearrange maps. Variable order number is not allowed.');
	}

	/**
	 * Get effective type.
	 * @return string Node's effective type (folder, [rfolder_s | rfolder_d], template or workbook).
	 */
	public function getEffectiveType() {
		return $this->subtype . (in_array($this->subtype, self::$mapTypes, true) ? (count($this->maps) > 0 ? '_d' : '_s') : '');
	}

	/**
	 * Alias to getReference function.
	 */
	public function getWorkbook() {
		return $this->getReference();
	}

	/**
	 * Get Group, Hierarchy & Node of reference joined in report hierarchy.
	 * @return W3S_NodeLink W3S_NodeLink object or null if node type is not template.
	 */
	public function getReference() {
		return $this->ref;
	}

	/**
	 * Set vars.
	 * @param array Array with all variables used in workbook (format: array("Year", "Month", "Product")).
	 */
	public function setVars(array $vars) {
		$this->vars = array_unique($vars);
	}

	/**
	 * Get array with all variables used in workbook.
	 * @return array Array with all variables used in workbook (format: array("Year", "Month", "Product")).
	 */
	public function getVars() {
		return $this->vars;
	}

	public static function getNodeData($conn, $group, $hierarchy, $node) {
		$xml_data = @palo_datav($conn, $group, '#_' . $hierarchy, array(1, 1, 'data'), array(1, 1, $node));

		$data = new W3S_NodeData();
		$data->import($xml_data[2]);

		return $data;
	}

	public static function setNodeData($conn, W3S_NodeData $data, $group, $hierarchy, $node) {
		@palo_setdata($data->export(), 'false', $conn, $group, '#_' . $hierarchy, 'data', $node);
	}

	public static function setVariables($conn, $group, $hierarchy, $node, array $vars) {
		try {
			$data = self::getNodeData($conn, $group, $hierarchy, $node);
			$data->setVars($vars);
			self::setNodeData($conn, $data, $group, $hierarchy, $node);

			return true;
		} catch (Exception $e) {
			return false;
		}
	}

	public static function getVariables($conn, $group, $hierarchy, $node) {
		try {
			$data = self::getNodeData($conn, $group, $hierarchy, $node);
			return $data->getVars();
		} catch (Exception $e) {
			return array();
		}
	}

	/*
	 * Set hyperlink.
	 * @param array Hyperlink parameters in format: array('type' => 'url', 'target' => 'self', 'url' => 'http://host.domain.tld/')
	 */
	private function setHyperlinkArr($params) {
		if (array_key_exists('type', $params)) {
			switch ($params['type']) {

				case 'url':
					$this->hyperlinks['url'] = new W3S_URLLink($params['url'], $params['target']);
					break;

				default:
					throw new WSS_Exception('W3S_NodeData-inv_hyperlink_type', array(), 'Unable to set hyperlink. Invalid type.');
			}
		} else
			throw new WSS_Exception('W3S_NodeData-no_hyperlink_type', array(), 'Unable to set hyperlink. No type.');
	}

	/*
	 * Set hyperlink.
	 * @param W3S_URLLink Hyperlink object.
	 */
	public function setHyperlink($link) {
		if (($link_type = get_class($link)) == false)
			throw new WSS_Exception('W3S_NodeData-inv_hyperlink_type', array(), 'Unable to set hyperlink. Invalid type.');

		if (!in_array($link_type, self::$linkClasses, true))
			throw new WSS_Exception('W3S_NodeData-err_set_hyperlink', array(), 'Unable to set hyperlink for this node type.');

		switch ($link_type) {
			case 'W3S_URLLink':
				$this->hyperlinks['url'] = $link;
				break;

			default:
				throw new WSS_Exception('W3S_NodeData-inv_hyperlink_type', array(), 'Unable to set hyperlink. Invalid type.');
		}
	}

	/**
	 * Get Hyperlink.
	 * @return W3S_URLLink Hyperlink object.
	 */
	public function getHyperlink($type = 'url') {
		return $this->hyperlinks[$type];
	}

	private function addLink($group, $hierarchy, $node, $type, &$storage, $chk) {
		if(!in_array($this->type, $chk, true))
			throw new WSS_Exception('W3S_NodeData-err_' . $type . '_not_supported', array(), ucfirst($type) . 's are not supported on this node type.');

		foreach ($storage as $link)
			if ($link->getGroup() == $group && $link->getHierarchy() == $hierarchy && $link->getNode() == $node)
				return;

		$storage[] = new W3S_NodeLink($group, $hierarchy, $node);
	}

	private function deleteLink($group, $hierarchy, $node, $type, &$storage, $chk) {
		if(!in_array($this->type, $chk, true))
			throw new WSS_Exception('W3S_NodeData-err_' . $type . '_not_supported', array(), ucfirst($type) . 's are not supported on this node type.');

		foreach ($storage as $i => $link)
			if ($link->getGroup() == $group && $link->getHierarchy() == $hierarchy && $link->getNode() == $node) {
				array_splice($storage, $i, 1);
				break;
			}
	}

	public function getLinks($type) {
		switch ($type) {
			case self::$referrer:
				$storage = $this->referrers;
				break;

			case self::$resource:
				$storage = $this->resources;
				break;

			case self::$dependent:
				$storage = $this->dependents;
				break;
		}

		$links = array();

		foreach ($storage as $link)
			$links[] = $link->getGHNAssoc();

		return $links;
	}

	/*
	 * Add referrer.
	 * @param string Group UID.
	 * @param string Hierarchy UID.
	 * @param string Node UID.
	 */
	public function addReferrer($group, $hierarchy, $node) {
		$this->addLink($group, $hierarchy, $node, self::$referrer, $this->referrers, self::$typesWithReferrers);
	}

	/*
	 * Delete referrer.
	 * @param string Group UID.
	 * @param string Hierarchy UID.
	 * @param string Node UID.
	 */
	public function deleteReferrer($group, $hierarchy, $node) {
		$this->deleteLink($group, $hierarchy, $node, self::$referrer, $this->referrers, self::$typesWithReferrers);
	}

	/**
	 * Get referrers.
	 * @return array Referrers array in format: array(array('group' => 'some_grp', 'hierarchy' => 'h1', 'node' => 'n2'), ....)
	 */
	public function getReferrers() {
		return $this->getLinks(self::$referrer);
	}

	/*
	 * Add resource.
	 * @param string Group UID.
	 * @param string Hierarchy UID.
	 * @param string Node UID.
	 */
	public function addResource($group, $hierarchy, $node) {
		$this->addLink($group, $hierarchy, $node, self::$resource, $this->resources, self::$typesWithResources);
	}

	/*
	 * Delete resource.
	 * @param string Group UID.
	 * @param string Hierarchy UID.
	 * @param string Node UID.
	 */
	public function deleteResource($group, $hierarchy, $node) {
		$this->deleteLink($group, $hierarchy, $node, self::$resource, $this->resources, self::$typesWithResources);
	}

	/*
	 * Delete all resources.
	 */
	public function deleteAllResources() {
		$this->resources = array();
	}

	/**
	 * Get resources.
	 * @return array Resource array in format: array(array('group' => 'some_grp', 'hierarchy' => 'h1', 'node' => 'n2'), ....)
	 */
	public function getResources($sys = false) {
		return $sys ? $this->resources : $this->getLinks(self::$resource);
	}

	/*
	 * Add dependent.
	 * @param string Group UID.
	 * @param string Hierarchy UID.
	 * @param string Node UID.
	 */
	public function addDependent($group, $hierarchy, $node) {
		$this->addLink($group, $hierarchy, $node, self::$dependent, $this->dependents, self::$typesWithDependents);
	}

	/*
	 * Delete dependent.
	 * @param string Group UID.
	 * @param string Hierarchy UID.
	 * @param string Node UID.
	 */
	public function deleteDependent($group, $hierarchy, $node) {
		$this->deleteLink($group, $hierarchy, $node, self::$dependent, $this->dependents, self::$typesWithDependents);
	}

	/*
	 * Delete all dependents.
	 */
	public function deleteAllDependents() {
		$this->dependents = array();
	}

	/**
	 * Get dependents.
	 * @return array Dependent array in format: array(array('group' => 'some_grp', 'hierarchy' => 'h1', 'node' => 'n2'), ....)
	 */
	public function getDependents($sys = false) {
		return $sys ? $this->dependents : $this->getLinks(self::$dependent);
	}

	/**
	 * Get parameters.
	 * @return array Parameters array in format: array('key1' => 'value1', 'key2' => 'value2', ...).
	 */
	public function getParams() {
		return $this->params;
	}

	private function chkParams() {
		if(!in_array($this->type, self::$typesWithParams, true))
			throw new WSS_Exception('W3S_NodeData-err_params_not_supported', array(), 'Parameters are not supported on this node type.');
	}

	/**
	 * Set parameters.
	 * @param array Parameters array in format: array('key1' => 'value1', 'key2' => 'value2', ...).
	 */
	public function setParams(array $params) {
		$this->chkParams();

		if (is_array($params))
			$this->params = $params;
	}

	/**
	 * Update parameters.
	 * @param array Parameters array in format: array('key1' => 'value1', 'key2' => 'value2', ...).
	 */
	public function updateParams(array $params) {
		$this->chkParams();

		foreach ($params as $key => $val)
			$this->params[$key] = $val;
	}

	/**
	 * Delete parameters named in array.
	 * @param array Parameters array in format: array('key1', 'key2', ...).
	 */
	public function deleteParams(array $params) {
		$this->chkParams();

		foreach ($params as $key)
			unset($this->params[$key]);
	}

	/**
	 * Delete all parameters.
	 */
	public function deleteAllParams() {
		$this->chkParams();

		$this->params = array();
	}
}

?>