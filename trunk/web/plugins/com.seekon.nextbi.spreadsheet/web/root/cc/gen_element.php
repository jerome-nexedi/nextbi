<?php

/*
 * \brief used as URL source for images and charts
 *
 * \file gen_element.php
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
 * Mladen Todorovic <mladen.todorovic@develabs.com>
 *
 * \version
 * SVN: $Id: gen_element.php 3046 2010-03-30 08:30:30Z drazenk $
 *
 */

require '../../etc/config.php';

require 'autoload.php';
require 'ccmd.php';

if (isset($_GET['sid']))
	session_id($_GET['sid']);

require 'session_start.php';

set_time_limit(0);

if (!isset($_GET['id']))
	die();

$id = $_GET['id'];
$type = isset($_GET['t']) ? $_GET['t'] : 'chart';

switch ($type)
{
	case 'img':

		$res = ccmd(array(array('wget', '', array(), array('img_type', 'payload'), array('e_id' => $id))));

		if (!is_array($res[0][1][0]))
			die();

		$res = &$res[0][1][0];

		header('Content-Type: ' . $res['img_type']);
		print gzuncompress(base64_decode($res['payload']));

		break;

	default:

		if (!isset($_SESSION['WSS']))
			die();

		$wss = $_SESSION['WSS'];

		if (!isset($_GET['wbid']) || !isset($_GET['wsid']))
			$curr_res = json_decode(ccmd('[["ocurr",1],["ocurr",2]]'), true);

		$bookUid = isset($_GET['wbid']) ? $_GET['wbid'] : $curr_res[0][1];
		$sheetUid = isset($_GET['wsid']) ? $_GET['wsid'] : $curr_res[1][1];

		if (($chart = $wss->get_worksheet_elements()->get_element($bookUid, $sheetUid, $id)) === false)
			die();

		if (isset($_GET['w']) && isset($_GET['h']) && $chart->ChartArea->Width != $_GET['w'] && $chart->ChartArea->Height != $_GET['h'])
		{
			$chart->ChartArea->Width = $wss->get_worksheet_elements()->pixel_to_point($_GET['w']);
			$chart->ChartArea->Height = $wss->get_worksheet_elements()->pixel_to_point($_GET['h']);

			// update new size in wsel
			$res = ccmd(array(array('wupd', '', array($id => array("size" => array($_GET['w'], $_GET['h']))))));
		}

		require 'phpchartdir.php';

		$chart->MakeChart('PNG');
}

?>