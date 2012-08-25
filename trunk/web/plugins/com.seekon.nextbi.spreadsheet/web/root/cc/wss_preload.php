<?php

/*
 * \brief used for loading WSS inside Studio
 *
 * \file wss_preload.php
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
 * Predrag Malicevic <predrag.malicevic@develabs.com>
 *
 * \version
 * SVN: $Id: wss_preload.php 2969 2010-03-23 09:36:51Z drazenk $
 *
 */

require '../../etc/config.php';

require 'autoload.php';
require 'ccmd.php';
require 'session_start.php';

define('DUMPS_FILE', '../../lib/charts/chartinit/chartinit.dat');

set_time_limit(0);

if (!isset($_SESSION['accessPolicy']))
{
	header('Location: /spreadsheet/root/ui/login/index.php');
	die();
}

$auto_login = array();
$err_msgs = array();

if (isset($_GET['wb']))
{
	$auto_login['wb'] = $_GET['wb'];
	unset($_GET['wb']);
}
else
	$err_msgs[] = 'ERROR: Missing parameter \'wb\' (workbook name).';

if (isset($_GET['grp']))
{
	$auto_login['grp'] = $_GET['grp'];
	unset($_GET['grp']);
}
else
	$err_msgs[] = 'ERROR: Missing parameter \'grp\' (group name).';

if (isset($_GET['hrc']))
{
	$auto_login['hrc'] = $_GET['hrc'];
	unset($_GET['hrc']);
}
else
	$err_msgs[] = 'ERROR: Missing parameter \'hrc\' (hierarchy name).';

/*
if (count($err_msgs) > 0)
{
	foreach($err_msgs as $err_msg)
		print $err_msg . '<br>';

	die();
}
*/

$vars = array('nodes' => array(), 'vars' => array());
$var_params = array('n_', 'v_');

foreach ($_GET as $name => $val) {
	$param_begin = substr($name, 0, 2);

	if (in_array($param_begin, $var_params)) {
		if (is_numeric($val))
			$val = floatval($val);
		else if (!strcasecmp($val, 'true'))
			$val = true;
		else if (!strcasecmp($val, 'false'))
			$val = false;

		$vars[$param_begin == 'n_' ? 'nodes' : 'vars'][substr($name, 2)] = $val;
	}
}

if (isset($_GET['vempty']) || count($vars['nodes']) > 0 || count($vars['vars']) > 0) {
	$auto_login['vars'] = $vars;

	if (isset($_GET['rPath'])) {
		$auto_login['rPath'] = $_GET['rPath'];
		unset($_GET['rPath']);
	}
}

if (isset($_GET['opar'])) {
	$auto_login['opar'] = $_GET['opar'];
	unset($_GET['opar']);
}

$auto_login['appmode'] = isset($_GET['wam']) && strtoupper($_GET['wam'][0]) == 'U' ? 'user' : 'designer';



try
{
	$wssajax = new WSS($auto_login);
}
catch(Exception $e)
{
	die('ERROR: Unable to instantiate class WSS:<br>' . $e);
}

header('Location: /spreadsheet/root/ui/app/' . (isset($_GET['wam']) && $_GET['wam'] == 'user' ? 'view' : 'main') . '.php');

?>