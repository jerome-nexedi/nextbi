<?php

/*
 * \brief RPC proxy
 *
 * \file rpc.php
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
 * SVN: $Id: rpc.php 2776 2010-02-16 21:31:26Z predragm $
 *
 */

require '../../etc/config.php';

require 'autoload.php';
require 'ccmd.php';
require 'session_start.php';

define('DUMPS_FILE', '../../lib/charts/chartinit/chartinit.dat');

set_time_limit(0);

list ($class, $method, $params) = json_decode(file_get_contents('php://input', FILE_BINARY), true);

header('Content-Type: application/json; charset=utf-8; charset=UTF-8');
header('Last-Modified: ' . gmdate('D, d M Y H:i:s \G\M\T'));
header('Expires: Fri, 03 Sep 1999 01:00:00 GMT');
header('Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0');
header('Pragma: no-cache');

if (!preg_match('/\w/i', $class))
	die('ERROR: invalid class name');

if (!class_exists($class, false))
	require 'rpc/' . $class . '.php';

if (ctype_upper($class[0]))
	$class = new $class();

if (!method_exists($class, $method))
	die('ERROR: unknown method');

$res = call_user_func_array(array($class, $method), $params);

print is_string($res) && substr($res, 0, 2) == '[' ? $res : json_encode($res);

?>