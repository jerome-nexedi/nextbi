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
 * SVN: $Id: test.php 2443 2009-11-24 11:14:51Z drazenk $
 *
 */

	require_once('../../../lib/WSS_Exception.php');

	function __autoload($class_name) {
		require_once('../../../lib/w3s/' . $class_name . '.php');
	}

	try {
		/*define('HOST', '127.0.0.1');
		define('PORT', '7777');
		define('USER', 'admin');
		define('PASS', 'admin');

		$conn = palo_init(HOST, PORT, USER, PASS);
		palo_use_unicode(true);

		print('<pre>');
		var_dump(W3S_NodeData::getVariables($conn, 'fgrp1', 'h1', 'n29')); exit;
		print('</pre>');

		$contents = W3S_NodeData::setVariables($conn, 'fgrp1', 'h1', 'n4', array('Year', 'Month', 'Product', 'Year', 'Pero'));
		print($contents); exit;
		*/
//		$data = new W3S_NodeData(
//			array(
//	 			'el' => 'n5',
//	  			'type' => 'urlplugin',
//	 			'subtype' => 'ahview',
//	  			'name' => 'Some name',
//	  			'desc' => 'Some description',
////	  			'hyperlink' => array('type' => 'url', 'target' => 'self', 'url' => 'http://host.domain.tld/'),
//	  			'params' => array('hideViewTabs' => 'yes', 'hideNavigator' => 'no')
//	 		)
//		);

//		$data = new W3S_NodeData(
//			array(
//	 			'el' => 'n5',
//	  			'type' => 'rurlplugin',
//	  			'name' => 'Some name',
//	  			'desc' => 'Some description',
//	  			'ref' => array('group' => 'some_grp', 'hierarchy' => 'h1', 'node' => 'n2', 'type' => 'ahview'),	// only reference to ahview files
//	  			'params' => array('hideViewTabs' => 'yes', 'hideNavigator' => 'no')
//	 		)
//		);

		// Test import
		$data = new W3S_NodeData();
		//$data = new W3S_HierarchyData();
		//$data = new W3S_GroupData();

		// get contents of a file into a string
		$filename = "../../../lib/w3s/test/node_XMLs/rurlplugin.xml";
		$handle = fopen($filename, "r");
		$contents = fread($handle, filesize($filename));
		fclose($handle);
		//print($contents);

		$data->import($contents);

		//$data->rearrangeMaps(array('Years' => 1, 'Products' => 2));

		/*$res_array = $data->getMap('Productssss');
		print($res_array['list']->getData()); exit;*/

		print('<pre>');
		print_r($data);
		print('</pre>');

		/*$data_clone = clone $data;

		print('<br><br><br><br><pre>');
		print_r($data_clone);
		print('</pre>');*/

		// Export:
		print('<br><br><br><pre>');
		print($data->export());
		print('</pre>');

	} catch (Exception $e) {
		print('Code: ' . $e->getCode() . '<br>Msg: ' . $e->getMessage());
	}

?>