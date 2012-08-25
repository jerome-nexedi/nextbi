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
 * SVN: $Id: test_fsplugin.php 3063 2010-03-30 20:26:36Z predragm $
 *
 */

$class_dir = '..' . DIRECTORY_SEPARATOR;

require_once('../../../WSS_Exception.php');

function __autoload ($class_name)
{
	global $class_dir;

	if (file_exists($class_dir . $class_name . '.php'))
		require_once($class_dir . $class_name . '.php');
}

define('HOST', '127.0.0.1');
define('PORT', '7777');
define('USER', 'admin');
define('PASS', 'admin');

print '<pre>';

try {
	$apol = new AccessPolicy(HOST, PORT, USER, PASS, USER, PASS);

	$fs_plugin = new W3S_FSPlugin();

	// GROUP MANIPULATION

	//$group = W3S_Group::addGroup($apol, '_test6', 'file', array('name' => 'Some group', 'desc' => 'Some description...'));
	$group = new W3S_Group($apol, '_test6');
	//W3S_Group::removeGroup($apol, '_test3');

	if ($group->getType() == 'file')
		$group->registerPlugin($fs_plugin);


	// HIERARCHY MANIPULATION

	//$h = $group->addHierarchy('file', array('name' => 'Local hierarchy 1', 'desc' => 'Some description...', backend => array('type' => 'local', 'location' => 'C:/Data/projects/jedox/WSS3_Core2/storage/jedox_ng')));
	$h = $group->getHierarchy('h1');
	//$group->removeHierarchy($h);

	if ($h->getType() == 'file')
		$h->registerPlugin($fs_plugin);

	// NODE MANIPULATION

	// parent_id, operation, leaf, type, params
	//$h->node('root', 'addNode', true, 'workbook', array('name' => 'Neki pod templ X', 'desc' => 'Odje Te X'));
	//$h->node('n1', 'addNode', false, 'folder', array('name' => 'Neki pod st f 2', 'desc' => 'Odje ST F 2'));
	//$h->node('root', 'addNode', true, 'workbook', array('name' => 'Neki pod templ 2', 'desc' => 'Odje Te 2'));
	//$h->node('n1', 'addNode', true, 'workbook', array('name' => 'Neki templ X 3', 'desc' => 'Odje Te X 2'));
	//$h->node('n10', 'addNode', false, 'folder', array('name' => 'QiuckDemo', 'desc' => 'Odje ST X'));
	//$h->node('n2', 'addNode', true, 'workbook', array('name' => 'Neki templ n2 2', 'desc' => 'Odje Te n2 2'));

	//$n = $h->getNode('n2');
	//print($n->getData()->getEffectiveType());

	//print($n->getUID());
	//print_r($n);

	//print($n->getNodePath());


	// parent_id, operation, node_id
	//$h->node('n2', 'removeNode', 'n7');

	// node_id, operation, new_parent_id
	//$h->node('n4', 'moveNode', 'n1');

	// node_id, operation, new_parent_id, new_position
	//$h->node('n4', 'moveNode', 'n2', 0);

	// node_id, operation, new_parent_id
	//$h->node('n3', 'copyNode', 'n2');

	// node_id, operation, new_parent_id, new_position
	//$h->node('n258', 'copyNode', 'e0', 0);

	// parent_id, operation, node_id, new_name
	//$h->node('root', 'renameNode', 'n2', 'Renamed Static Folder');

	print "\n";

	$h->dumpASCII();

} catch (WSS_Exception $e) {
	print('Code: ' . $e->getCode() . '<br>Msg: ' . $e->getMessage());
}

print '</pre>';

?>