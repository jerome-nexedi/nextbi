<?php

/*
 * \brief commmon RPC routines
 *
 * \file common.php
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
 * SVN: $Id: common.php 3094 2010-04-09 12:07:40Z predragm $
 *
 */

class common
{
	public static function fetchRecent ($context = null, $type = null)
	{
		$recent = new Recent($_SESSION['accessPolicy']);

		return $recent->fetch($context, $type);
	}

	public static function clearRecent ($context = null, $type = null)
	{
		$recent = new Recent($_SESSION['accessPolicy']);

		$recent->clear($context, $type);

		return $recent->save();
	}

	private static function _getPaloConn ()
	{
		if (!isset($_SESSION['paloConnData']))
			return $_SESSION['accessPolicy']->getConn();

		$connData = &$_SESSION['paloConnData'];

		$conn = palo_init($connData['host'], $connData['port'], $connData['username'], $connData['password']);
		palo_use_unicode(true);

		return $conn;
	}

	public static function paloGet ($db, $cube, array $order = array(), array $coords = array())
	{
		if (!is_resource($conn = self::_getPaloConn()))
			return array();

		$paloData = new PaloData($conn);

		return $paloData->get($db, $cube, $order, $coords);
	}

	public static function paloSet ($db, $cube, array $data, array $order = array(), $add = true)
	{
		if (!is_resource($conn = self::_getPaloConn()))
			return false;

		$paloData = new PaloData($conn);

		return $paloData->set($db, $cube, $data, $order, $add);
	}

	public static function paloRemove ($db, $dim, array $coords)
	{
		if (!is_resource($conn = self::_getPaloConn()))
			return false;

		$paloData = new PaloData($conn);

		return $paloData->remove($db, $dim, $coords);
	}

	public static function paloRename ($db, $dim, array $names)
	{
		if (!is_resource($conn = self::_getPaloConn()))
			return false;

		$paloData = new PaloData($conn);

		return $paloData->rename($db, $dim, $names);
	}

}

?>