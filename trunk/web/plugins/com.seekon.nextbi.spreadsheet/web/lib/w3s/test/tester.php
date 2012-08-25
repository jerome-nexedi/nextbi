<?php

/*
 * @brief wss file
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
 * Predrag Malicevic <predrag.malicevic@develabs.com>
 *
 * \version
 * SVN: $Id: tester.php 3063 2010-03-30 20:26:36Z predragm $
 *
 */

$class_dir = 'lib' . DIRECTORY_SEPARATOR;

require_once('../WSS_Exception.php');

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

$apol = new AccessPolicy(HOST, PORT, USER, PASS, USER, PASS);



// ##########################
// ### GROUP MANIPULATION ###
// ##########################

//$group = W3S_Group::addGroup($apol, 'TreeTest', 'report', array('name' => 'Some group', 'desc' => 'Some group description...'));
$group = new W3S_Group($apol, 'TreeTest');
//W3S_Group::removeGroup($apol, 'TreeTest');



// ##############################
// ### HIERARCHY MANIPULATION ###
// ##############################

//$h = $group->addHierarchy('report', array('name' => 'Some hierarchy', 'desc' => 'Some hierarchy description...'));
$h = $group->getHierarchy('h1');
//$group->removeHierarchy($h);



// #########################
// ### NODE MANIPULATION ###
// #########################


// ### ADDING NODE ###

// parent_id, operation, leaf, type, params
//$h->node('root', 'addNode', true, 'template', array('name' => 'Template One', 'desc' => 'Info for T1', 'workbook' => array('group' => '_test5', 'hierarchy' => 'h1', node => 'n3')));
//$h->node('root', 'addNode', false, 'rfolder', array('name' => 'Folder One', 'desc' => 'Info for F1'));
//$h->node('root', 'addNode', true, 'template', array('name' => 'Template Two', 'desc' => 'Info for T2', 'workbook' => array('group' => '_test5', 'hierarchy' => 'h1', node => 'n4')));
//$h->node('root', 'addNode', false, 'rfolder', array('name' => 'Folder Two', 'desc' => 'Info for F2'));
//
//$h->node('n2', 'addNode', true, 'template', array('name' => 'Template Three', 'desc' => 'Info for T3', 'workbook' => array('group' => '_test5', 'hierarchy' => 'h1', node => 'n5')));
//$h->node('n2', 'addNode', false, 'rfolder', array('name' => 'Folder Three', 'desc' => 'Info for F3'));
//
//$h->node('n6', 'addNode', true, 'template', array('name' => 'Template Four', 'desc' => 'Info for T4', 'workbook' => array('group' => '_test5', 'hierarchy' => 'h1', node => 'n6')));
//
//$h->node('n4', 'addNode', false, 'rfolder', array('name' => 'Folder Four', 'desc' => 'Info for F4'));
//$h->node('n4', 'addNode', true, 'template', array('name' => 'Template Five', 'desc' => 'Info for T5', 'workbook' => array('group' => '_test5', 'hierarchy' => 'h1', node => 'n7')));
//
//$h->node('n8', 'addNode', true, 'template', array('name' => 'Template Six', 'desc' => 'Info for T6', 'workbook' => array('group' => '_test5', 'hierarchy' => 'h1', node => 'n8')));
//$h->node('n8', 'addNode', true, 'template', array('name' => 'Template Seven', 'desc' => 'Info for T7', 'workbook' => array('group' => '_test5', 'hierarchy' => 'h1', node => 'n9')));

// parent_id, operation, leaf, type, params, position (int)
//$h->node('n2', 'addNode', true, 'template', array('name' => 'Template X', 'desc' => 'Info for TX', 'workbook' => array('group' => '_test5', 'hierarchy' => 'h1', node => 'n16')), 1);

// parent_id, operation, leaf, type, params, position (id)
//$h->node('root', 'addNode', true, 'rfolder', array('name' => 'D-Folder X', 'desc' => 'Info for DFX'), 'n1');



// ### REMOVING NODE ###

// parent_id, operation, node_id
//$h->node('n12', 'removeNode', 'n255');


// ### MOVING NODE ###

// node_id, operation, new_parent_id
//$h->node('n248', 'moveNode', 'n12');

// node_id, operation, new_parent_id, new_position (int)
//$h->node('n248', 'moveNode', 'n12', 2);

// node_id, operation, new_parent_id, new_position (id)
//$h->node('n6', 'moveNode', 'n2', 'n5');


// ### COPYING NODE ###

// node_id, operation, new_parent_id
//$h->node('n8', 'copyNode', 'n2');

// node_id, operation, new_parent_id, new_position (int)
//$h->node('n9', 'copyNode', 'n2', 0);

// node_id, operation, new_parent_id, new_position (id)
//$h->node('n9', 'copyNode', 'n2', 'n3');


// ### RENAMING NODE ###

// parent_id, operation, node_id, new_name
//$h->node('root', 'renameNode', 'n1', 'Renamed Template');


print "\n";

$h->dumpASCII();

print '</pre>';

?>