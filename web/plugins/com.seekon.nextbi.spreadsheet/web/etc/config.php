<?php

 /*
 * \brief config file
 *
 * \file config.php
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
 * Vladislav Malicevic <vladislav.malicevic@jedox.com>
 *
 * \version
 * SVN: $Id: config.php 2917 2010-03-10 12:32:26Z predragm $
 *
 */

// suite version
define('CFG_VERSION', '3.1.1');

// license holder
define('CFG_LICENSEE', 'Jedox AG');

// shared secret - needs to be 16 chars long
define('CFG_SECRET', 'uqtPiM5Nw7MYC2Pl');

// palo
define('CFG_PALO_HOST', '127.0.0.1');
define('CFG_PALO_PORT', '7921');
define('CFG_PALO_USER', 'admin');
define('CFG_PALO_PASS', 'admin');

// ub
define('CFG_UB_PORT', 8090);
define('CFG_UB_PATH', '/spreadsheet/ub/ccmd');

// fopper
define('CFG_FOPPER_PATH', '');

// ad hoc viewer
define('CFG_AHVIEWER_PATH', '/web-palo-ng/com.tensegrity.wpalo.WPalo/WPalo.html'); // /cc/studio/jpalo.php

// default prefs
define('DEF_PREFS_L10N', 'en_US');
define('DEF_PREFS_THEME', 'default');
define('DEF_PREFS_IFACE', 'toolbar');

?>