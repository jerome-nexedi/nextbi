<?php

/*
 * \brief autoloading script
 *
 * \file autoload.php
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
 * SVN: $Id: autoload.php 2625 2010-01-13 09:50:55Z predragm $
 *
 */

set_include_path(
'../../../lib' . PATH_SEPARATOR . '../../../lib/util' . PATH_SEPARATOR . '../../../lib/w3s' . PATH_SEPARATOR . '../../../lib/rpc' . PATH_SEPARATOR . '../../../lib/dynarange' . PATH_SEPARATOR
. '../../../lib/charts' . PATH_SEPARATOR . '../../../lib/charts/phpxls' . PATH_SEPARATOR . '../../../lib/charts/phpxls/enum' . PATH_SEPARATOR . '../../../lib/charts/chartinit' . PATH_SEPARATOR
. '../../../lib/charts/chartmapper' . PATH_SEPARATOR . '../../../lib/charts/chartmapper/adapters' . PATH_SEPARATOR . '../../../lib/charts/chartdir' . PATH_SEPARATOR . '../../../lib/palo_import'
);

function __autoload ($class_name)
{
	require $class_name . '.php';
}

?>