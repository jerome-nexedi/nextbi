<?php

/*
 * \brief front URL for exporting to XLSX/PDF/HTML
 *
 * \file export.php
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
 * SVN: $Id: export.php 2675 2010-01-20 22:14:07Z predragm $
 *
 */

require '../../etc/config.php';

require 'autoload.php';
require 'ccmd.php';
require 'export/Report_Adapter.php';
require 'export/Report.php';
require 'session_start.php';

set_time_limit(0);

$report = new Report();

switch ($_GET['format'])
{
	case 'xlsx':
		$report->gen_xlsx();
		break;

	case 'pdf':
		$report->gen_pdf();
		break;

	case 'html':
		$report->gen_html();
		break;
}

?>