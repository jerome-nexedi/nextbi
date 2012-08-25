<?php

/*
 * \brief front file for view mode
 *
 * \file view.php
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
 * Predrag Malicevic <predrag.malicevic@develabs.com>
 *
 * \version
 * SVN: $Id: view.php 3026 2010-03-29 13:00:57Z drazenk $
 *
 */

require '../../../etc/config.php';

require '../../../lib/util/XMLData.php';
require '../../../lib/Prefs.php';

define('APP_MODE', 'USER');
define('WAM', isset($_GET['wam']) ? strtoupper($_GET['wam']) : 'USER');

session_name('WSS' . substr(WAM, 0, 1) . '_SESSID');
session_start();

if (!isset($_SESSION['accessPolicy']))
{
	session_destroy();
	setcookie('app', 'app/view.php', 0, '/spreadsheet/root/ui/');
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
$prefs_iface = $prefs->search('wss/interface');

$ui_ver = is_readable($ui_ver_file = '../../../version') ? split("\t", trim(file_get_contents($ui_ver_file))) : array('dev', date('Y-m-d H:i:s O'));

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>WSS - User Mode</title>
<?php if ($ui_ver[0] != 'dev') { ?>
	<link rel="stylesheet" type="text/css" media="screen" href="/spreadsheet/root/ui/ld/index.php?wss/user/main,wss/user/<?php print $prefs_iface; ?>.<?php print $ui_ver[0]; ?>.css" />
<?php } else { require('../ld/dev.php'); ld('wss/user/main,wss/user/' . $prefs_iface . '.css'); } ?>
	<link rel="stylesheet" type="text/css" id="theme" media="screen" href="/spreadsheet/root/ui/lib/ext/resources/css/xtheme-<?php print $prefs->search('general/theme'); ?>.css" />
</head>

<body id="mainBody" style="overflow: hidden;" onunload="Jedox.wss.general.appUnload();" onload="Jedox.wss.general.startUp();" onContextMenu="return false;" scroll="no">

	<!-- Menubar, Toolbars and Formula Bar Container -->
	<div id="barsContainer">

		<?php switch ($prefs_iface) { case 'toolbar': ?>

		<!-- Menubar -->
		<div id="MenuPlaceholder" style="height: 25px; background-color: #BBD4F9;"></div>

		<!-- Toolbars -->
		<div id="Toolbar" class="wsstoolbar" style="height: 26px; overflow: hidden;">
			<!-- Standard Toolbar -->
			<div id="wssStandardBar" class="bar" style="position: absolute;">
				<div id="wssStandardToolbar" class="bar-end"></div>
			</div>
		</div>

		<?php break; case 'ribbon': ?>

		<!--Ribbon -->
		<div id="ribbon" style="height: 118px; background-color: #BBD4F9;"></div>

		<?php } ?>

	</div> <!-- end of barsContainer -->

	<!--  Workspace -->
	<div id="workspace" class="workspace">
	</div>

	<!-- Status Bar -->
	<div id="statusBarContainer" style="width: 100%;"></div>

	<div id="CursorMarker"></div>
	<div id="marker" onmouseup="stopTracking();"></div>

	<div id="HTML_AJAX_LOADING" class="HTML_AJAX_Loading" style="font-family: Arial, Helvetica, sans-serif; font-size: 8.25pt; position: absolute; top: 50px; right: 50px; text-align: right; width: 100px; padding: 4px; display: none; z-index: 200; background: url(/spreadsheet/root/ui/app/res/img/snake_transparent.gif) no-repeat center left;">Please wait ...</div>

	<script type="text/javascript" src="/spreadsheet/root/cc/ha.php?stub=WSS,Palo,MicroChartStreamer,Studio&client=Util,Main,Request,HttpClient,Dispatcher,Behavior,Loading,JSON,iframe,orderedQueue&wam=<?php print WAM; ?>"></script>
<?php if ($ui_ver[0] != 'dev') { ?>
	<script type="text/javascript" src="/spreadsheet/root/ui/ld/index.php?wss/user/main,wss/user/<?php print $prefs_iface; ?>,wss/<?php print $prefs_l10n . '.' . $ui_ver[0]; ?>.js"></script>
<?php } else ld('wss/user/main,wss/user/' . $prefs_iface . ',wss/' . $prefs_l10n . '.js'); ?>
	<script type="text/javascript">

		Ext.BLANK_IMAGE_URL = '/spreadsheet/root/ui/lib/ext/resources/images/default/s.gif';

		HTML_AJAX.defaultServerUrl = '/spreadsheet/root/cc/ha.php';

		Jedox.wss.app.uiVer = <?php print '[ \'' . CFG_VERSION . ($ui_ver[0] != 'dev' ? '.' : '-') . $ui_ver[0] . '\', \'' . $ui_ver[1] . '\' ]'; ?>;

		Jedox.wss.app.params = <?php print json_encode($prefs->dump(0)); ?>;

		Jedox.wss.app.defaultFiles = <?php print ($studio_def_files = $prefs->search('studio/default/files')) ? json_encode($studio_def_files) : '\'\'' ?>;

		Jedox.wss.app.appMode = Jedox.wss.grid.viewMode.<?php print APP_MODE; ?>;
		Jedox.wss.app.appModeS = Jedox.wss.grid.viewMode.<?php print WAM; ?>;
		Jedox.wss.app.toolbarLayout = '<?php print $prefs_iface; ?>';
		Jedox.wss.app.fopper = <?php print(strlen(CFG_FOPPER_PATH) ? 'true' : 'false'); ?>;

		Jedox.wss.backend.conn.xhrHdrs['X-WSS-AM'] = HTML_AJAX_Request.prototype.customHeaders['X-WSS-AM'] = Jedox.wss.app.appModeS;

		Jedox.wss.app.theme = '<?php print $theme; ?>';

		if (window == window.parent)
			window.onbeforeunload = function () { return ''; };

	</script>

</body>

</html>