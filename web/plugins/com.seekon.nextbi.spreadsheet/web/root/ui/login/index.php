<?php

/*
 * \brief login form
 *
 * \file index.php
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
 * SVN: $Id: index.php 2917 2010-03-10 12:32:26Z predragm $
 *
 */

require '../../../etc/config.php';

// logout
if ($_SERVER['QUERY_STRING'] == 'r')
{
	require '../../../lib/ccmd.php';

	$expire = time() - 604800;

	foreach (array('WSSU_SESSID', 'WSSD_SESSID') as $sess_name)
	{
		session_name($sess_name);
		session_start();

		ccmd('[["logo"]]');

		session_destroy();
		setcookie($sess_name, '', $expire, '/');
	}

	session_name('STUDIO_SESSID');
	session_start();
	session_destroy();
	setcookie('STUDIO_SESSID', '', $expire, '/');

	header('Location: /spreadsheet/root/ui/login/index.php');
	die();
}

// auto-login
if (isset($_GET['user']) && isset($_GET['pass']))
{
	require '../../../lib/ccmd.php';
	require '../../../lib/AccessPolicy.php';
	require '../../../lib/util/XMLData.php';
	require '../../../lib/Prefs.php';
	require '../../../lib/rpc/Login.php';

	$user = $_GET['user'];
	$pass = base64_decode($_GET['pass']);

	$td = mcrypt_module_open('rijndael-128', '', 'cfb', '');

	mcrypt_generic_init($td, CFG_SECRET, md5($user, true));
	$pass = trim(mdecrypt_generic($td, $pass));
	mcrypt_generic_deinit($td);

	mcrypt_module_close($td);

	$login = new Login();
	$res = $login->log_in($user, $pass);

	if ($res[0])
	{
		header('Location: /spreadsheet/root/ui/' . (isset($_COOKIE['app']) ? $_COOKIE['app'] : 'studio/') . ($_SERVER['QUERY_STRING'] ? '?' . $_SERVER['QUERY_STRING'] : ''));
		die();
	}
}

DEFINE('VERSION_FILE', '../../../version');

if (is_readable(VERSION_FILE))
	$version = file_get_contents(VERSION_FILE);

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Palo Web | Login</title>
<style type="text/css">

*
{
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-weight: bold;
	color: #1C3F93;
}

body
{
	margin: 0px;
	padding: 0px;
	background-image: url(/spreadsheet/root/ui/studio/res/img/login/hg_top.jpg);
	background-position: top left;
	background-repeat: repeat-x;

}

#board
{
	position: relative;
	width: 340px;
	margin: 0px auto 0px auto;
	padding: 0 0 0 0;
	background-color: #3C3C3C;
	background-position: top left;
	background-repeat: repeat-y;
}

#login_box
{
	position: absolute;
	background-image: url(/spreadsheet/root/ui/studio/res/img/login/box.jpg);
	background-repeat: no-repeat;
	top: 165px;
	padding: 45px;
	width: 346px;
	height: 875px;
	padding-top: 140px;
}

#login_button
{
	background-color: #448855;
	margin-top: 0px;
	clear: both;
	border: 1px #B3B9C3 solid;
	background-color: #C2C9CF;
}

#rel_info_box
{
	position: absolute;
	top: 264px;
	left: 10px;
	padding: 10px;
	width: 330px;
	white-space: nowrap;
}

#rel_box
{
	width: 180px;
	font-size: 11px;
	font-weight: bold;
	float: left
}

#info_box
{
	font-size: 11px;
	font-weight: bold;
}

#username
{
  position: absolute;
  margin-top: 8px;
  width: 80px;
}

#userinput
{
  position: absolute;
  padding-left: 80px;
  margin-top: 8px;
  width: 150px;
}

#password
{
  position: absolute;
  margin-top: 38px;
  width: 80px;
}

#passinput
{
  position: absolute;
  padding-left: 80px;
  margin-top: 38px;
  width: 150px;
}

#nwindowcheck
{
  position: absolute;
  margin-top: 76px;
  margin-left: 140px;
  width: 150px;
  z-index: 100;
}

#login
{
  position: absolute;
  padding-left: 80px;
  margin-top: 76px;
  width: 100px;
}

</style>
</head>

<body onLoad="setTimeout(function(){document.getElementById('user').focus();}, 1)">

	<div id="board">
		<div id="login_box">
			<div id="username">Username:</div>
			<div id="userinput">
				<input id="user" type="text" style="" maxlength="20" onKeyPress="chkInput(event)">
			</div>
			<div id="password">Password:</div>
			<div id="passinput">
				<input id="pass" type="password" value="" onKeyPress="chkInput(event)">
			</div>
			<div id="nwindowcheck">
				<input id="newwin" type="checkbox" name="newwin">&nbsp;New Window
			</div>
			<div id="login">
				<input id="login_button" type="button" value="Login" onClick="login()">
			</div>
			<div id="rel_info_box">
				<div id="rel_box">Release: <?php print CFG_VERSION . (isset($version) ? '.' . substr($version, 0, strpos($version, "\t")) : '-dev'); ?></div>
				<div id="info_box">Licensed to: <?php print CFG_LICENSEE; ?></div>
			</div>
		</div>
	</div>

	<div id="HTML_AJAX_LOADING" class="HTML_AJAX_Loading" style="font-family: Arial, Helvetica, sans-serif; font-size: 8.25pt; position: absolute; top: 50px; right: 50px; text-align: right; width: 100px; padding: 4px; display: none; z-index: 200; background: url(/spreadsheet/root/ui/app/res/img/snake_transparent.gif) no-repeat center left;">Please wait ...</div>

	<script type="text/javascript" src="/spreadsheet/root/cc/login.php?stub=Login&client=Util,Main,Request,HttpClient,Dispatcher,Behavior,Loading,JSON,iframe,orderedQueue"></script>
	<script type="text/javascript">

		HTML_AJAX.defaultServerUrl = '/spreadsheet/root/cc/login.php';

		function chkInput (ev)
		{
			if ((document.all ? window.event.keyCode : ev.which) == 13)
				login();
		}

		function login ()
		{
			var userField = document.getElementById('user'),
					passField = document.getElementById('pass');

			if (userField.value.length < 1)
			{
				alert('Please, input your username!');
				userField.focus();
				return;
			}

			if (passField.value.length < 1)
			{
				alert('Please, input your password!');
				passField.focus();
				return;
			}

			var login = new Login(),
					res = login.log_in(userField.value, passField.value);

			if (res[0] !== true)
			{
				alert(res[1]);
				userField.focus();
				return;
			}

			var app = document.cookie.match(/(^|; ?)app=([^;]+)/),
					url = '/spreadsheet/root/ui/'.concat(app ? unescape(app[2]) : 'studio/index.php', window.location.search);

			if (document.getElementById('newwin').checked)
				window.open(url, 'app_win', 'directories=no,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=no');
			else
				window.location.href = url;
		}

	</script>

</body>

</html>