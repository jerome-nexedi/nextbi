<?php

/* 
 * @brief ajax
 *
 * @file Group.js
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
 * SVN: $Id: W3S_PaloSubsetList.php 2212 2009-09-30 13:16:31Z predragm $
 *
 */

final class W3S_PaloSubsetList extends W3S_BaseList {

	public function __construct($data) {
		$this->chkData(array('data' => $data));

		$this->type = 'palosubset';
		$this->value = $data;
	}

	private function chkData($data) {
		foreach ($data as $key => $value) {
			switch ($key) {
				// Check data.
				case 'data':
					if (!isset($value) || strlen($value) == 0)
						throw new WSS_Exception('W3S_PaloSubsetList-no_data', array(), 'Subset data is not set.');
					break;
			}
		}
	}

	/**
	 * Get list data.
	 * @return string List data.
	 */
	public function getData() {
		return $this->value;
	}

	/**
	 * Set list data.
	 * @param string List data.
	 */
	public function setData($data) {
		$this->chkData(array('data' => $data));
		$this->value = $data;
	}

}

?>