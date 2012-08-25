<?php

/*
 * \brief Studio front file
 *
 * \file index.php
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
 * Drazen Kljajic <drazen.kljajic@develabs.com>
 * Srdjan Vukadinovic <srdjan.vukadinovic@develabs.com>
 *
 * \version
 * SVN: $Id: index.php 3069 2010-03-31 12:57:16Z predragm $
 *
 */

require '../../../etc/config.php';

require '../../../lib/AccessPolicy.php';
require '../../../lib/util/XMLData.php';
require '../../../lib/Prefs.php';

session_name('STUDIO_SESSID');
session_start();

if (!isset($_SESSION['accessPolicy']))
{
	session_destroy();
	setcookie('app', 'studio/', 0, '/spreadsheet/root/ui/');
	header('Location: /spreadsheet/root/ui/login/' . ($_SERVER['QUERY_STRING'] ? '?' . $_SERVER['QUERY_STRING'] : 'index.php'));
	die();
}

if (isset($_COOKIE['app']))
	setcookie('app', '', time() - 604800, '/spreadsheet/root/ui/');

$prefs = $_SESSION['prefs'];

if (isset($_GET['_']))
{
	$params = json_decode(gzuncompress(base64_decode($_GET['_'])), true);

	if (is_array($params))
		$prefs->set('', $params, 0);

	unset($_GET['_']);
}

foreach ($_GET as $key => $val)
	$prefs->set($key, $val, 0);

$prefs_l10n = $prefs->search('general/l10n');

$ui_ver = is_readable($ui_ver_file = '../../../version') ? split("\t", trim(file_get_contents($ui_ver_file))) : array('dev', date('Y-m-d H:i:s O'));

$apol = $_SESSION['accessPolicy'];
$ld_conf = '';

foreach (array('reports', 'files', 'palo', 'users', 'etl', 'conns') as $module)
	if ($apol->getRule('ste_' . $module) != AccessPolicy::PERM_NONE)
		$ld_conf .= ',' . 'studio/' . $module;

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head id="head">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="gwt:module" content="app" />
	<meta name="gwt:property" content="locale=<?php print $prefs_l10n; ?>" />
	<title>Palo Web</title>
<?php if ($ui_ver[0] != 'dev') { ?>
	<link rel="stylesheet" type="text/css" href="/spreadsheet/root/ui/ld/index.php?studio/main.<?php print $ui_ver[0]; ?>.css" media="screen" />
<?php } else { require('../ld/dev.php'); ld('studio/main.css'); } ?>
	<link rel="stylesheet" type="text/css" media="screen" id="theme" href="/spreadsheet/root/ui/lib/ext/resources/css/xtheme-<?php print $prefs->search('general/theme'); ?>.css" />
</head>

<body onContextMenu="return false;">

	<div id="testWidth" style="position: absolute; visibility: hidden; white-space: nowrap;">Width</div>

	<!--
	<div id="header">
		<h1>Palo Web</h1>
		<div id="mainMenu"></div>
		<div id="toolbar"><div style="float: right; margin: 5px;"></div></div>
	</div>
	-->

	<div id="HTML_AJAX_LOADING" class="HTML_AJAX_Loading" style="font-family: Arial, Helvetica, sans-serif; font-size: 8.25pt; position: absolute; top: 50px; right: 50px; text-align: right; width: 100px; padding: 4px; display: none; z-index: 200; background: url(/spreadsheet/root/ui/app/res/img/snake_transparent.gif) no-repeat center left;">Please wait ...</div>

	<script type="text/javascript">

		function getCookie (name)
		{
			var re = new RegExp('(^|; ?)'.concat(name, '=([^;]+)')),
					match = document.cookie.match(re);

			return match instanceof Array ? unescape(match[2]) : '';
		}

		window.name = 'wetl-main';

		// ETL setup properties
		var etl_web_mode_options = {
		  mode: 'integrated'
		, app_path: '/tc/web-etl/app/'
		};

	</script>
	<script type="text/javascript" src="/spreadsheet/root/cc/studio.php?stub=Studio,Palo&client=Util,Main,Request,HttpClient,Dispatcher,Behavior,Loading,JSON,iframe,orderedQueue"></script>
<?php if ($ui_ver[0] != 'dev') { ?>
	<script type="text/javascript" src="/spreadsheet/root/ui/ld/index.php?studio/main<?php print $ld_conf . ',studio/' . $prefs_l10n . '.' . $ui_ver[0]; ?>.js"></script>
<?php } else ld('studio/main' . $ld_conf . ',studio/' . $prefs_l10n . '.js'); ?>
	<script type="text/javascript">

		Ext.BLANK_IMAGE_URL = '/spreadsheet/root/ui/lib/ext/resources/images/default/s.gif';

		HTML_AJAX.defaultServerUrl = '/spreadsheet/root/cc/studio.php';

		Jedox.studio.app.uiVer = <?php print '[ \'' . CFG_VERSION . ($ui_ver[0] != 'dev' ? '.' : '-') . $ui_ver[0] . '\', \'' . $ui_ver[1] . '\' ]'; ?>;

		Jedox.studio.app.params = <?php print json_encode($prefs->dump(0)); ?>;

		//Jedox.studio.access.rules = <?php print json_encode($apol->getRules()); ?>;
		//Jedox.studio.access.perm_g = <?php print $apol->getRule('database'); ?>;

		Jedox.studio.access.rules = {"user":7,"password":7,"group":7,"database":7,"cube":7,"dimension":7,"dimension element":7,"cell data":15,"rights":7,"system operations":7,"event processor":7,"sub-set view":7,"user info":7,"rule":7,"ste_reports":7,"ste_files":7,"ste_palo":7,"ste_users":7,"ste_etl":7,"ste_conns":7};
		Jedox.studio.access.perm_g = 7; 
		
		Jedox.studio.app.defaultFiles = <?php print ($studio_def_files = $prefs->search('studio/default/files')) ? json_encode($studio_def_files) : '\'\'' ?>;
		Jedox.studio.app.defaultReports = <?php print ($studio_def_reports = $prefs->search('studio/default/reports')) ? json_encode($studio_def_reports) : '\'\'' ?>;
<?php
	if ($mypalo = $prefs->search('mypalo'))
		print '		Jedox.studio.app.myPalo = ' . json_encode($mypalo) . ";\n";
?>

		Jedox.wss.backend.conn.xhrHdrs['X-WSS-AM'] = 'studio';

		Jedox.wss.backend.conn.ping_cmd = '[["ping","<?php print $_SESSION['WSSU_SESSID'] . '","' . $_SESSION['WSSD_SESSID']; ?>"]]';
		setTimeout(Jedox.wss.backend.conn.ping, Jedox.wss.backend.conn.ping_interval);

		window.onbeforeunload = function () { return 'leaving_app_msg'.localize(); };

	</script>

	<iframe src="" id="etl_download" style="width: 0px; height: 0px; border: 0px; visibility: hidden;"></iframe>

</body>

</html>