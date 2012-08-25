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
 * Mladen Todorovic <mladen.todorovic@develabs.com>
 *
 * \version
 * SVN: $Id: PaloConf.php 3008 2010-03-26 15:09:16Z mladent $
 *
 */

class PaloConf {
	public static $paloConf = array(
		'filterDims' => array('#viewcolumns', '#viewrows', '#subsetrows', '#subsetcolumns', '#views', '#_USER_PROPERTIES_', '#_RIGHT_OBJECT_', '#_GROUP_PROPERTIES_', '#_ROLE_PROPERTIES_'),
		'filterCubes' => array('#views', '#_#viewcolumns', '#_#viewrows', '#_VIEW_LOCAL', '#_VIEW_GLOBAL', '#_GROUP_DIMENSION_DATA_#viewcolumns', '#_GROUP_DIMENSION_DATA_#viewrows'),
		'maxWinSize' => 200, // maximum nambers for window size
		'workingMode' => 'elements', // it's posible to be more 2 modes: attributes and subsets
		'pointToPixel' => 1.35,

		'colorHostDb' => '#A1BDDB',
		'colorPageElems' => '#DCE6F1',
		'colorData' => '#FFFFFF',
		'colorDataBorder' => '#D1D8E5',

		'pvMaxCols' => 256,
		'pvMaxRows' => 65536,

		'colStyle1' => 'background-color:#DCE6F1;',
		'colStyle2' => 'background-color:#A1BDDB;',
		'colStyle0' => 'background-color:#31659C;color:#FFFFFF;',

		'rowStyle1' => 'background-color:#E0E0E0;',
		'rowStyle2' => 'background-color:#BEBEBE;',
		'rowStyle0' => 'background-color:#808080;color:#FFFFFF;',

		'styleConsolidated' => 'font-weight:bold;',
		'styleNormal' => 'font-weight:normal;',
		'wrapText' => 'white-space:normal;',

		'handlerExpandCollapsePasteView_inJS' => 'hnd_dblCpv',
		'getPasteViewInitData_inJS' => 'hnd_dblCpvOpen',
		'nofnc_openChooseElement_inJS' => 'hnd_dblCceOpen',

		'paloSubesetFuncSeparator' => ',',
		'paloSubesetFuncSeparatorArr' => ';',
		'paloAttrsSeparator' => ',',

		'paloSubsetGrammarFile' => 'subset_grammar.xslt',
		'paloSubsetQucikPreviewSize' => 4,
		'paloSubsetQucikPreviewListSize' => 20
	);

	public static $errStr = array(
		1 => 'Exception Msg: ',

		101 => 'Action failed.',

		1001 => 'Dimension used by some Cubes.',
		1002 => 'Dimension doesn\'t exist. Please reopen dialog!',
		1003 => 'Database name is not correct.',
		1004 => 'Dimension already exists.',
		1005 => 'Cube doesn\'t exist.',
		1006 => 'Element already exists.',
		1007 => 'Workbook/Sheet is not correct.',
		1008 => 'Server not found.',
		1009 => 'Subset not saved.',
		1010 => 'Subset not deleted.',
		1011 => 'Subset not renamed.',

		1000 => ''
	);

	public static $paloSubsetDesc = array(
		'_name' => 'PALO.SUBSET',
		'_objName' => 'subset',
		'_isSet' => array('type' => 'xpath', 'path' => '/subset:subset'),
		'_hb_hasDbName' => true,
		'@1' => array(
			'type' => 'variable',
			'data_type' => 'string',
			'var_name' => 'server/database',
			'_objName' => 'serv_db'
		),
		'@2' => array(
			'type' => 'variable',
			'data_type' => 'string',
			'var_name' => 'dimension',
			'_objName' => 'dim'
		),
		'@3' => array(
			'type' => 'xpath',
			'path' => '/subset:subset/subset:indent/subset:value',
			'_objName' => 'indent'
		),
		'@4' => array(
			'type' => 'xpath_palo_attribute',
			'path' => '/subset:subset/subset:alias_filter',
//			'path' => '/subset:subset/subset:alias_filter/subset:alias1/subset:value | /subset:subset/subset:alias_filter/subset:alias2/subset:value',
//			'param' => '/subset:subset/subset:alias_filter/subset:alias1/subset:parameter | /subset:subset/subset:alias_filter/subset:alias2/subset:parameter',
			'_objName' => array('alias1', 'alias2')
		),
		'@5' => array('type' => 'function', 'function_def' => array(
			'_name' => 'PALO.HFILTER',
			'_objName' => 'hier',
			'_isSet' => array('type' => 'xpath', 'path' => '/subset:subset/subset:hierarchical_filter'),
			'@1' => array(
				'type' => 'xpath',
				'data_type' => 'palo_element',
				'path' => '/subset:subset/subset:hierarchical_filter/subset:element/subset:value',
				'param' => '/subset:subset/subset:hierarchical_filter/subset:element/subset:parameter',
				'_objName' => 'element'
			),
			'@2' => array(
				'type' => 'xpath',
				'data_type' => 'bool',
				'path' => '/subset:subset/subset:hierarchical_filter/subset:above/subset:value',
				'param' => '/subset:subset/subset:hierarchical_filter/subset:above/subset:parameter',
				'_objName' => 'above'
			),
			'@3' => array(
				'type' => 'xpath',
				'data_type' => 'bool',
				'path' => '/subset:subset/subset:hierarchical_filter/subset:exclusive/subset:value',
				'param' => '/subset:subset/subset:hierarchical_filter/subset:exclusive/subset:parameter',
				'_objName' => 'exclusive'
			),
			'@4' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:hierarchical_filter/subset:hide/subset:value',
				'param' => '/subset:subset/subset:hierarchical_filter/subset:hide/subset:parameter',
				'_objName' => 'hide'
			),
			'@5' => array(
				'type' => 'xpath',
				'data_type' => 'palo_element',
				'path' => '/subset:subset/subset:hierarchical_filter/subset:revolve_element/subset:value',
				'param' => '/subset:subset/subset:hierarchical_filter/subset:revolve_element/subset:parameter',
				'_objName' => 'revolve_element'
			),
			'@6' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:hierarchical_filter/subset:revolve_count/subset:value',
				'param' => '/subset:subset/subset:hierarchical_filter/subset:revolve_count/subset:parameter',
				'_objName' => 'revolve_count'
			),
			'@7' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:hierarchical_filter/subset:revolve_add/subset:value',
				'param' => '/subset:subset/subset:hierarchical_filter/subset:revolve_add/subset:parameter',
				'_objName' => 'revolve_add'
			),
			'@8' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:hierarchical_filter/subset:level_start/subset:value',
				'param' => '/subset:subset/subset:hierarchical_filter/subset:level_start/subset:parameter',
				'_objName' => 'level_start'
			),
			'@9' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:hierarchical_filter/subset:level_end/subset:value',
				'param' => '/subset:subset/subset:hierarchical_filter/subset:level_end/subset:parameter',
				'_objName' => 'level_end'
			)
		)),
		'@6' => array('type' => 'function', 'function_def' => array(
			'_name' => 'PALO.TFILTER',
			'_objName' => 'text',
			'_isSet' => array('type' => 'xpath', 'path' => '/subset:subset/subset:text_filter'),
			'@1' => array(
				'type' => 'xpath',
				'data_type' => 'string',
				'path' => '/subset:subset/subset:text_filter/subset:regexes/subset:value/subset:expression',
				'param' => '/subset:subset/subset:text_filter/subset:regexes/subset:parameter',
				'_objName' => 'regexes'
			),
			'@2' => array(
				'type' => 'xpath',
				'data_type' => 'bool',
				'path' => '/subset:subset/subset:text_filter/subset:extended/subset:value',
				'param' => '/subset:subset/subset:text_filter/subset:extended/subset:parameter',
				'_objName' => 'extended'
			)
		)),
		'@7' => array('type' => 'function', 'function_def' => array(
			'_name' => 'PALO.PICKLIST',
			'_objName' => 'pick',
			'_isSet' => array('type' => 'xpath', 'path' => '/subset:subset/subset:picklist_filter'),
			'@1' => array(
				'type' => 'xpath',
				'data_type' => 'palo_element',
				'path' => '/subset:subset/subset:picklist_filter/subset:manual_definition/subset:value/subset:pick_elem',
				'param' => '/subset:subset/subset:picklist_filter/subset:manual_definition/subset:parameter',
				'_objName' => 'elems'
			),
			'@2' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:picklist_filter/subset:pick_type/subset:value',
				'param' => '/subset:subset/subset:picklist_filter/subset:pick_type/subset:parameter',
				'_objName' => 'pick_type'
			)
		)),
		'@8' => array('type' => 'function', 'function_def' => array(
			'_name' => 'PALO.AFILTER',
			'_objName' => 'attr',
			'_isSet' => array('type' => 'xpath', 'path' => '/subset:subset/subset:attribute_filter'),
			'@1' => array(
				'type' => 'xpath_palo_attribute_filter',
				'data_type' => 'string',
				'path' => '/subset:subset/subset:attribute_filter/subset:attribute_filters/subset:value/subset:filter_col',
				'param' => '/subset:subset/subset:attribute_filter/subset:attribute_filters/subset:parameter',
				'_objName' => 'attribute_filter'
			)
		)),
		'@9' => array('type' => 'function', 'function_def' => array(
			'_name' => 'PALO.DFILTER',
			'_objName' => 'data',
			'_isSet' => array('type' => 'xpath', 'path' => '/subset:subset/subset:data_filter'),
			'@1' => array(
				'type' => 'function',
				'function_def' => array(
					'_name' => 'PALO.SUBCUBE',
					'_objName' => 'subcube',
					'_isSet' => array('type' => 'xpath', 'path' => '/subset:subset/subset:data_filter/subset:subcube'),
					'@1' => array(
						'type' => 'xpath',
						'data_type' => 'palo_cube',
						'path' => '/subset:subset/subset:data_filter/subset:subcube/subset:source_cube/subset:value',
						'param' => '/subset:subset/subset:data_filter/subset:subcube/subset:source_cube/subset:parameter',
						'_objName' => 'source_cube'
					),
					'@2' => array(
						'type' => 'xpath_palo_cube_element',
						'path' => '/subset:subset/subset:data_filter/subset:subcube',
						'_objName' => 'subcube'
					)
				)
			),
			'@2' => array(
				'type' => 'xpath_palo_criteria',
				'path' => '/subset:subset/subset:data_filter/subset:criteria',
				'_objName' => 'criteria'
			),
			'@3' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:data_filter/subset:top/subset:value',
				'param' => '/subset:subset/subset:data_filter/subset:top/subset:parameter',
				'_objName' => 'top'
			),
			'@4' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:data_filter/subset:upper_percentage/subset:value',
				'param' => '/subset:subset/subset:data_filter/subset:upper_percentage/subset:parameter',
				'_objName' => 'upper_percentage'
			),
			'@5' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:data_filter/subset:lower_percentage/subset:value',
				'param' => '/subset:subset/subset:data_filter/subset:lower_percentage/subset:parameter',
				'_objName' => 'lower_percentage'
			),
			'@6' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:data_filter/subset:cell_operator/subset:value',
				'param' => '/subset:subset/subset:data_filter/subset:cell_operator/subset:parameter',
				'_objName' => 'cell_operator'
			)
		)),
		'@10' => array('type' => 'function', 'function_def' => array(
			'_name' => 'PALO.SORT',
			'_objName' => 'sort',
			'_isSet' => array('type' => 'xpath', 'path' => '/subset:subset/subset:sorting_filter'),
			'@1' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:sorting_filter/subset:whole/subset:value',
				'_objName' => 'whole'
			),
			'@2' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:sorting_filter/subset:sorting_criteria/subset:value',
				'param' => '/subset:subset/subset:sorting_filter/subset:sorting_criteria/subset:parameter',
				'_objName' => 'sorting_criteria'
			),
			'@3' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:sorting_filter/subset:attribute/subset:value',
				'param' => '/subset:subset/subset:sorting_filter/subset:attribute/subset:parameter',
				'_objName' => 'attribute'
			),
			'@4' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:sorting_filter/subset:type_limitation/subset:value',
				'param' => '/subset:subset/subset:sorting_filter/subset:type_limitation/subset:parameter',
				'_objName' => 'type_limitation'
			),
			'@5' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:sorting_filter/subset:level/subset:value',
				'param' => '/subset:subset/subset:sorting_filter/subset:level/subset:parameter',
				'_objName' => 'level'
			),
			'@6' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:sorting_filter/subset:reverse/subset:value',
				'param' => '/subset:subset/subset:sorting_filter/subset:reverse/subset:parameter',
				'_objName' => 'reverse'
			),
			'@7' => array(
				'type' => 'xpath',
				'path' => '/subset:subset/subset:sorting_filter/subset:show_duplicates/subset:value',
				'_objName' => 'show_duplicates'
			)
		))
	);
}

?>