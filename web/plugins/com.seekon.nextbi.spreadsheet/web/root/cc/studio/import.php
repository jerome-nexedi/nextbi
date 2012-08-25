<?php

/*
 * \brief form action file for Studio import dialog
 *
 * \file import.php
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
 *
 * \version
 * SVN: $Id: import.php 2776 2010-02-16 21:31:26Z predragm $
 *
 */

require '../../../etc/config.php';

require 'autoload.php';
require 'ccmd.php';
require 'rpc/Studio.php';

session_name('STUDIO_SESSID');
session_start();

try
{
	if (!isset($_FILES['import_file']) || !isset($_POST['importParentNode']))
		throw new Exception($res = 'false, { loc: "errFileImport", params: { name: "" } }');

	$uplfile_path = $_FILES['import_file']['tmp_name'];
	$origfile_name = $_FILES['import_file']['name'];

	if (strtolower(substr($origfile_name, -4)) == '.wss')
	{
		$origfile_name = substr($origfile_name, 0, -4);
		$uplfile_type = 'workbook';
	}
	else
		$uplfile_type = 'static';

	$studio = isset($_SESSION[Studio::SESSION_STORE_NAME]) ? $_SESSION[Studio::SESSION_STORE_NAME] : new Studio();

	$node_meta = array('name' => $origfile_name, 'desc' => $origfile_name, 'uplpath' => $uplfile_path);

	if ($uplfile_type == 'workbook')
	{
		$var_list = $studio->getUsedVariablesEx($uplfile_path);

		if (count($var_list))
			$node_meta['vars'] = $var_list;
	}

	$nuid = $studio->treeMngNode('file', $_POST['importParentNode'], 'importNode', true, $uplfile_type, $node_meta);

	$res = 'true, { nodeId: "' . $nuid . '" , name: "' . $origfile_name . '" }';
}
catch (Exception $e)
{
	if (!isset($res))
		$res = 'false, { loc: "errFileImport", params: { name: "' . $origfile_name . '" } }';
}

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">

function init ()
{
	parent.Jedox.studio.files.processImport(<?php print $res; ?>);
}

</script>
</head>
<body onload="init();"></body>
</html>