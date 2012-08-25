<?php

/*
 * \brief HTML_AJAX server file
 *
 * \file ha.php
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
 * SVN: $Id: ha.php 2632 2010-01-13 15:56:29Z predragm $
 *
 */

require '../../etc/config.php';

require 'autoload.php';
require 'ccmd.php';
require 'html_ajax/AJAX/Server.php';
require 'session_start.php';

define('DUMPS_FILE', '../../lib/charts/chartinit/chartinit.dat');

set_time_limit(0);

$server = new HTML_AJAX_Server('/spreadsheet/root/cc/ha.php');

if (isset($_GET['c']))
{
	if (!preg_match('/\w/i', $class = $_GET['c']))
		die('ERROR: invalid class name');

	if (!class_exists($class, false))
		require 'rpc/' . $class . '.php';

	if ($class == 'Palo')
	{
		$server->registerClass($wss = new WSS());
		$server->registerClass($wss->palo_handler);
	}
	else
		$server->registerClass(new $class());
}
else if (isset($_GET['stub']))
{
	$server->registerClass($wssajax = new WSS());
	$server->registerClass($wssajax->palo_handler);
	$server->registerClass(new MicroChartStreamer());
	$server->registerClass(new Studio());
}

$server->handleRequest();

?>