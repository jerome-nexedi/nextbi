<?php

/*
 * \brief spreadsheet functions for hiding columns/rows
 *
 * \file hidecolrow.php
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
 * Predrag Malicevic <predrag.malicevic@develabs.com>
 *
 * \version
 * SVN: $Id: hidecolrow.php 2151 2009-09-15 08:11:45Z predragm $
 *
 */

function HIDECOLUMN ()
{
	if (preg_match('/^([A-Z]+)[0-9]+$/', activerange()->address(), $col) !== 1)
		return '#ERR!';

	$ltrs = $col[1];
	$col = 0;

	for ($f = 1, $i = strlen($ltrs) - 1; $i >= 0; --$i, $f *= 26)
		$col += (ord($ltrs[$i]) - 64) * $f;

	$col = activesheet()->column($col);
	$width = $col->width();

	$arg = func_get_arg(0);

	if ($arg)
	{
		if ($width)
		{
			$col->width = 0;
			activerange()->attribute(new Attribute('"cw"', strval($width)));
		}
	}
	else if (!$width)
		$col->width = intval(activerange()->attribute('"cw"'));

	return $arg ? 1 : 0;
}

function HIDEROW ()
{
	if (preg_match('/^[A-Z]+([0-9]+)$/', activerange()->address(), $row) !== 1)
		return '#ERR!';

	$row = activesheet()->row(intval($row[1]));
	$height = $row->height();

	$arg = func_get_arg(0);

	if ($arg)
	{
		if ($height)
		{
			$row->height = 0;
			activerange()->attribute(new Attribute('"rh"', strval($height)));
		}
	}
	else if (!$height)
		$row->height = intval(activerange()->attribute('"rh"'));

	return $arg ? 1 : 0;
}

?>