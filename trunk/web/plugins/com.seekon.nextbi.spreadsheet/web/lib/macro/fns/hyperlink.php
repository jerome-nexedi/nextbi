<?php

/*
 * \brief hyperlink spreadsheet functions
 *
 * \file hyperlink.php
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
 * SVN: $Id: hyperlink.php 2800 2010-02-20 20:22:36Z predragm $
 *
 */

class ArgumentSplitter
{
    const RE_DQUOTED = '/"[^"]*"/';
    const RE_SQUOTED = '/\'[^\']*\'/';
    const RE_ARRAY = '/\{[^\}]*\}/';

    private static function _genPadding ($match)
    {
        return str_pad('', strlen($match[0]), ' ');
    }

    public static function split ($formula, $delim = ',')
    {
        $formula_s = preg_replace_callback(self::RE_ARRAY, 'ArgumentSplitter::_genPadding', preg_replace_callback(self::RE_SQUOTED, 'ArgumentSplitter::_genPadding', preg_replace_callback(self::RE_DQUOTED, 'ArgumentSplitter::_genPadding', $formula)));

        $args = preg_split('/\\' . $delim . '/', $formula_s, -1, PREG_SPLIT_OFFSET_CAPTURE);

        foreach ($args as &$arg)
            $arg = trim(substr($formula, $arg[1], strlen($arg[0])));

        return $args;
    }
}

function RESOLVE ($arg1) {
	$args = func_get_args();

	return json_encode($args);
}

function HYPERLINK ($link, $text, $tip) {
	$errStr = '#ERR!';
	$separators = array('en_US' => ',', 'de_DE' => ';', 'fr_FR' => ';', 'zh_CN' => ';');
	$args = func_get_args();
	$args_count = count($args);

	if ($args_count < 3)
		return $errStr;

	$act_range = activerange();
	$locale = locale();
	$fdelimiter = $separators[array_key_exists($locale, $separators) ? $locale : 'zh_CN'];

	preg_match('/HYPERLINK\(.*\)/i', $act_range->formula(), $fargs);
	$fargs = ArgumentSplitter::split(substr($fargs[0], 10, -1), $fdelimiter);

	$data = array(
		'dyn' => true,
		'link' => array(trim($fargs[0], '"\''), $args[0]),
		'text' => array(trim($fargs[1], '"\''), $args[1]),
		'tip' => array(trim($fargs[2], '"\''), $args[2]),
		'trans' => array()
	);

	for ($i = 3; $i < $args_count; $i += 2) {
		if ($i + 1 >= $args_count)
			break;

		$data['trans'][] = array('src' => array(trim($fargs[$i], '"\''), $args[$i]), 'dst' => array(trim($fargs[$i + 1], '"\''), $args[$i + 1]));
	}

	$act_range->attribute(new Attribute('"mousedown"', json_encode(array('hl', $data))));
	$act_range->style = 'text-decoration: underline; color: #0000ff;';

	return '<span class="hl" onmouseover="Jedox.wss.hl.toolTip(event, true);" onmouseout="Jedox.wss.hl.toolTip(event, false);">' . $args[1] . '</span>';
}

?>