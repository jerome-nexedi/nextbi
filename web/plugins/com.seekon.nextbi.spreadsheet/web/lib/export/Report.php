<?php

/*
 * @brief wss file
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
 * Mladen Todorovic <mladen.todorovic@develabs.com>
 * Drazen Kljajic <drazen.kljajic@develabs.com>
 *
 * \version
 * SVN: $Id: Report.php 2446 2009-11-24 14:53:51Z mladent $
 *
 */

class Report extends Report_Adapter
{
	private static function _lettersToNumber ($ltrs)
	{
		$num = 0;

		for ($f = 1, $i = strlen($ltrs = strtoupper($ltrs)) - 1; $i >= 0; --$i, $f *= 26)
			$num += (ord($ltrs[$i]) - 64) * $f;

		return $num;
	}

	protected function _create_data_xml($format)
	{
		if (!isset($this->wss_ajax)) {
			$this->_gen_error_data('It\'s not posible to generate report.');
			return;
		}

		$curr_wbid = $this->wss_ajax->getCurrWbId();
		$curr_wsid = $this->wss_ajax->getCurrWsId();

		if (isset($_SESSION['wss_page_setup']) && isset($_SESSION['wss_page_setup'][$curr_wbid]) && isset($_SESSION['wss_page_setup'][$curr_wbid][$curr_wsid]))
			$ps = $_SESSION['wss_page_setup'][$curr_wbid][$curr_wsid];

		$initialXml = '<?xml version="1.0" encoding="UTF-8"?><worksheet><pageproperties></pageproperties><sheetproperties></sheetproperties><grid></grid></worksheet>';
		$xml_doc = new DOMDocument();
		$xml_doc->loadXML($initialXml);

		$showErrors = true;
		if (isset($ps) && (strtoupper($ps['sheet']['cell_errors']) != 'DISPLAYED'))
		{
			$showErrors = false;
			if (strtoupper($ps['sheet']['cell_errors']) == 'BLANK')
				$errorString = '';
			else
				$errorString = $ps['sheet']['cell_errors'];
		}

		if (isset($ps) && (is_array($ps['sheet']['print_area'])))
			$gridData = $this->wss_ajax->exportRange($ps['sheet']['print_area']);
		else
			$gridData = $this->wss_ajax->exportRange();

		// setup page size/margins/header-footer size
		if (isset($ps) && ($ps['page']['paper_size'] == 'A4'))
		{
			$pageW = self::PDF_A4_PAGE_PX_WIDTH * self::PX2PT / self::INCH2PT; // in inches
			$pageH = self::PDF_A4_PAGE_PX_HEIGHT * self::PX2PT / self::INCH2PT; // in inches
		}
		else
		{
			$pageW = self::PDF_LETTER_PAGE_PX_WIDTH * self::PX2PT / self::INCH2PT; // in inches
			$pageH = self::PDF_LETTER_PAGE_PX_HEIGHT * self::PX2PT / self::INCH2PT; // in inches
		}

		// landscape
		if (isset($ps) && (!$ps['page']['portrait']))
		{
			$tmp = $pageW;
			$pageW = $pageH;
			$pageH = $tmp;
		}

		// values are in inches (default values are used in case, that there is no page setup values)
		$margins = array(
			'top' => (isset($ps) ? $ps['margins']['top'] : $this->default_margins['top']),
			'bottom' => (isset($ps) ? $ps['margins']['bottom'] : $this->default_margins['bottom']),
			'left' => (isset($ps) ? $ps['margins']['left'] : $this->default_margins['left']),
			'right' => (isset($ps) ? $ps['margins']['right'] : $this->default_margins['right']),
			'header' => (isset($ps) ? $ps['margins']['header'] : $this->default_margins['header']),
			'footer' => (isset($ps) ? $ps['margins']['footer'] : $this->default_margins['footer'])
		);

		// init some "constant" variables
		$pageBodyWpx = ($pageW - $margins['left'] - $margins['right']) * self::INCH2PT / self::PX2PT;
		$pageBodyHpx = ($pageH - $margins['top'] - $margins['bottom']) * self::INCH2PT / self::PX2PT;

		// Adjust col/row width/height Size
		$sizeAdjuster = 1;
		if (($format == self::FORMAT_PDF) && isset($ps) && $ps['page']['adjust']['enabled'] && ($ps['page']['adjust']['size'] != 100))
		{
			$sizeAdjuster = $ps['page']['adjust']['size'] / 100;
			$fontSizeAdjuster = $sizeAdjuster;
		}
		else if (($format == self::FORMAT_PDF) && isset($ps) && $ps['page']['fit']['enabled'])
		{
			// cols
			$totalW = 0;
			for ($i=0; $i<count($gridData['col_widths']); $i++)
				$totalW += $gridData['col_widths'][$i];

			// rows
			$totalH = 0;
			for ($i=0; $i<count($gridData['row_heights']); $i++)
				$totalH += $gridData['row_heights'][$i];

			if (($totalW != 0) && ($totalH != 0))
			{
				$wCoeff = $pageBodyWpx * $ps['page']['fit']['wide'] / $totalW;
				$hCoeff = $pageBodyHpx * $ps['page']['fit']['tall'] / $totalH;

				if (($wCoeff < 1) || ($hCoeff < 1))
				{
					$sizeAdjuster = (($wCoeff < $hCoeff) ? $wCoeff : $hCoeff);
					$fontSizeAdjuster = $sizeAdjuster;
				}
			}
		}
		if (isset($sizeAdjuster))
		{
			// cols
			for ($i=0; $i<count($gridData['col_widths']); $i++)
				$gridData['col_widths'][$i] *= $sizeAdjuster;

			// rows
			for ($i=0; $i<count($gridData['row_heights']); $i++)
				$gridData['row_heights'][$i] *= $sizeAdjuster;
		}

		$pagepropertiesNode = $xml_doc->getElementsByTagName('pageproperties')->item(0);
		// add page size to XML
		$pageElem = $xml_doc->createElement('page');
		$pageElem->setAttribute('width', $pageW . 'in');
		$pageElem->setAttribute('height', $pageH . 'in');
		$pageElem->setAttribute('first_page_number', (((isset($ps) && (strtoupper($ps['page']['first_page']) === 'AUTO')) || (!isset($ps))) ? 1 : $ps['page']['first_page']));
		$pagepropertiesNode->appendChild($pageElem);
		// add margins to XML
		$marginsElem = $xml_doc->createElement('margins');
		$marginsElem->setAttribute('top', $margins['top'] . 'in');
		$marginsElem->setAttribute('bottom', $margins['bottom'] . 'in');
		$marginsElem->setAttribute('left', $margins['left'] . 'in');
		$marginsElem->setAttribute('right', $margins['right'] . 'in');
		$marginsElem->setAttribute('header', $margins['header'] . 'in');
		$marginsElem->setAttribute('footer', $margins['footer'] . 'in');
		$marginsElem->setAttribute('total_header', (($margins['top'] > $margins['header']) ? $margins['top'] : $margins['header']) . 'in');
		$marginsElem->setAttribute('total_footer', (($margins['bottom'] > $margins['footer']) ? $margins['bottom'] : ($margins['footer'] + self::MAX_FOOTER_LINE_HEIGHT)) . 'in');
		$marginsElem->setAttribute('header_height', (($margins['top'] > $margins['header']) ? $margins['top'] - $margins['header'] : 0) . 'in');
		$marginsElem->setAttribute('footer_height', (($margins['bottom'] > $margins['footer']) ? $margins['bottom'] - $margins['footer'] : self::MAX_FOOTER_LINE_HEIGHT) . 'in');
		$marginsElem->setAttribute('vertical_center', ((isset($ps) && $ps['margins']['vert']) ? 'TRUE' : 'FALSE'));
		$marginsElem->setAttribute('horizontal_center', ((isset($ps) && $ps['margins']['horiz']) ? 'TRUE' : 'FALSE'));
		$pagepropertiesNode->appendChild($marginsElem);
		// add sheet
		$sheetElem = $xml_doc->createElement('sheet');
		$sheetElem->setAttribute('gridlines', ((isset($ps) && $ps['sheet']['gridlines']) ? 'TRUE' : 'FALSE'));
		$pagepropertiesNode->appendChild($sheetElem);

		// prepare charts and images for easier searching
		$res = json_decode($this->wss_ajax->exec('[["wget","",[],["e_id","e_type","n_location","pos_offsets","size"],{"e_type":"chart"}],' .
												  '["wget","",[],["e_id","e_type","n_location","pos_offsets","size"],{"e_type":"img"}]]'), true);

		$wselSearchArr = array();
		if ($res[0][0] && $res[1][0])
		{
			for ($i=0; $i<count($res); $i++)
			{
				foreach($res[$i][1] as $wsElem)
				{
					if (preg_match(WSS::RE_RANGE, $wsElem['n_location'], $pos))
					{
						$pos = array(self::_lettersToNumber($pos[1]) - 1, intval($pos[2]) - 1, self::_lettersToNumber($pos[3]) - 1, intval($pos[4]) - 1);

						if (!isset($wselSearchArr[$pos[2]]))
							$wselSearchArr[$pos[2]] = array();
						if (!isset($wselSearchArr[$pos[2]][$pos[3]]))
							$wselSearchArr[$pos[2]][$pos[3]] = array();

						$wselSearchArr[$pos[2]][$pos[3]][] = array(
							'id' => $wsElem['e_id'],
							'type' => $wsElem['e_type'],
							'pos' => $pos,
							'offsets' => $wsElem['pos_offsets'],
							'width' => $wsElem['size'][0],
							'height' => $wsElem['size'][1]
						);
					}
				}
			}
		}

		// generate grid data
		$heightsOfPages = array();
		$grid = $xml_doc->getElementsByTagName('grid')->item(0);

		// add charts node for HTML export
		if (($format != self::FORMAT_PDF) && (count($wselSearchArr) > 0))
			$chartsNode = $grid->appendChild($xml_doc->createElement('charts'));

		if (is_array($gridData) && isset($gridData['rows']) && (count($gridData['rows']) > 0))
		{
			$additionalRows = array();
			$totalHeight = 0; $pageHeight = 0;
			for ($i=0; $i<count($gridData['rows']); $i++)
			{
				$rowElem = $xml_doc->createElement('row');
				$rowElem->setAttribute('height', $this->_px2pt($gridData['row_heights'][$i]) . 'pt');
				$totalHeight += $gridData['row_heights'][$i];
				$pageHeight += $gridData['row_heights'][$i];

				if (($format == self::FORMAT_PDF) && ($pageHeight > $pageBodyHpx))
				{
					$heightsOfPages[] = $pageHeight - $gridData['row_heights'][$i];
					$pageHeight = $gridData['row_heights'][$i];
				}

				$cells = $rowElem->appendChild($xml_doc->createElement('cells'));

				$pageNum = 0; $pageWidth = 0; $totalWidth = 0;
				for ($j=0; $j<count($gridData['col_widths']); $j++)
				{
					$totalWidth += $gridData['col_widths'][$j];
					$pageWidth += $gridData['col_widths'][$j];
					if (($pageWidth > $pageBodyWpx) && ($j > 0) && ($format == self::FORMAT_PDF))
					{
						if (!isset($additionalRows[$pageNum]))
							$additionalRows[$pageNum] = array();

						$additionalRows[$pageNum][$i] = $rowElem;

						$rowElem = $xml_doc->createElement('row');
						$rowElem->setAttribute('height', $this->_px2pt($gridData['row_heights'][$i]) . 'pt');

						$cells = $rowElem->appendChild($xml_doc->createElement('cells'));

						$pageNum++;
						$pageWidth = $gridData['col_widths'][$j];
					}

					if (!isset($gridData['rows'][$i][$j]['m']) || (isset($gridData['rows'][$i][$j]['m']) && ($gridData['rows'][$i][$j]['m'][0])))
					{
						$cellElem = $xml_doc->createElement('cell');

						// MERGE
						if (isset($gridData['rows'][$i][$j]['m']))
							$cellElem->setAttribute('colspan', $gridData['rows'][$i][$j]['m'][2]);
						else
							$cellElem->setAttribute('width', $this->_px2pt($gridData['col_widths'][$j]) . 'pt');

						if (count($gridData['rows'][$i][$j]) != 0)
						{
							$styles = $this->default_styles;

							if (isset($ps) && $ps['sheet']['gridlines'])
								foreach($this->gridline_style as $gl_sn => $gl_s)
									$styles[$gl_sn] = $gl_s;

							if ($gridData['rows'][$i][$j]['t'] == 's')
								$styles['text-align'] = 'left';
							else if ($gridData['rows'][$i][$j]['t'] == 'n')
								$styles['text-align'] = 'right';
							else if ($gridData['rows'][$i][$j]['t'] == 'b')
								$styles['text-align'] = 'center';

							if (isset($gridData['rows'][$i][$j]['s']))
								$styles = $this->_parseStyle($gridData['rows'][$i][$j]['s'], $styles);

							// MERGE
							if (isset($gridData['rows'][$i][$j]['m']))
								$styles['rowspan'] = $gridData['rows'][$i][$j]['m'][1];

							// Adjustment for TEXT WRAP for PDF
							if (($format == self::FORMAT_PDF) &&
									(isset($styles['white-space']) && ($styles['white-space'] == 'nowrap')) &&
									(isset($styles['text-align']) && ($styles['text-align'] != 'left')) &&
									(strpos($gridData['rows'][$i][$j]['s'], 'white-space') !== false)
								)
								$styles['text-align'] = 'left';

							// font size adjustment
							if (isset($fontSizeAdjuster))
							{
								if (strpos($styles['font-size'], 'pt') === false)
									$styles['font-size'] = strval(intval($styles['font-size']) * $fontSizeAdjuster) . 'pt';
								else
									$styles['font-size'] = strval(intval(substr($styles['font-size'], 0, -2)) * $fontSizeAdjuster) . 'pt';
							}

							foreach($styles as $styleName => $styleValue)
							{
								$tmpStyleValue = $this->_checkStyle($styleName, $styleValue);
								$cellElem->setAttribute($styleName, $tmpStyleValue);
							}
						}
						else if (isset($ps) && $ps['sheet']['gridlines'])
							foreach($this->gridline_style as $gl_sn => $gl_s)
								$cellElem->setAttribute($gl_sn, $gl_s);

						if (isset($gridData['rows'][$i][$j]['t']) && ($gridData['rows'][$i][$j]['t'] == 'h') && isset($gridData['rows'][$i][$j]['v']))
							$gridData['rows'][$i][$j]['v'] = strip_tags($gridData['rows'][$i][$j]['v']);

						$cellElem->appendChild($xml_doc->createElement('value', ((count($gridData['rows'][$i][$j]) == 0) ? '' :
							(($showErrors) ?
								$gridData['rows'][$i][$j]['v'] :
								((in_array($gridData['rows'][$i][$j]['v'], $this->error_values)) ? $errorString : $gridData['rows'][$i][$j]['v'])
							)
						)));

						// chart and image in PDF documents
						if ($format == self::FORMAT_PDF)
						{
							$cellSize = array($gridData['row_heights'][$i], $gridData['col_widths'][$j]);
							if (isset($gridData['rows'][$i][$j]['m']))
								$cellSize = $this->_getMergedCellSize(
												array($i, $j),
												array($gridData['rows'][$i][$j]['m'][1], $gridData['rows'][$i][$j]['m'][2]),
												$gridData['row_heights'],
												$gridData['col_widths']
											);
						}

						// adding charts and images
						if ((count($wselSearchArr) > 0) && isset($wselSearchArr[$j]) && isset($wselSearchArr[$j][$i]))
						{
							foreach ($wselSearchArr[$j][$i] as $wsElem)
							{
								$chartElem = $xml_doc->createElement('chart');

								$chartElem->setAttribute('width', $this->_px2pt($wsElem['width'] * $sizeAdjuster) . 'pt');
								$chartElem->setAttribute('height', $this->_px2pt($wsElem['height'] * $sizeAdjuster) . 'pt');

								$chartElem->setAttribute('top', $this->_px2pt($pageHeight - $gridData['row_heights'][$i] - ($wsElem['height'] - $wsElem['offsets'][3]) * $sizeAdjuster) - 2);
								$chartElem->setAttribute('left', $this->_px2pt($pageWidth - $gridData['col_widths'][$j] - ($wsElem['width'] - $wsElem['offsets'][2])  * $sizeAdjuster));

								if ($format == self::FORMAT_HTML)
								{
									$chartElem->setAttribute('src', $this->chart_url . 'wam=' . $_GET['wam'] . '&t=' . $wsElem['type'] . '&id=' . $wsElem['id'] . '&ts=' . (microtime(true) * 1000));
									$chartsNode->appendChild($chartElem);
								}
								else
								{
									$chartElem->setAttribute('src', '\'' . $this->chart_url . 'wam=' . $_GET['wam'] . '&sid=' . session_id() . '&t=' . $wsElem['type'] . '&id=' . $wsElem['id'] . '&ts=' . (microtime(true) * 1000) . '\'');
									$cellElem->appendChild($chartElem);
								}
							}
						}

						$cells->appendChild($cellElem);
					}
				}

				if (!isset($additionalRows[$pageNum]))
					$additionalRows[$pageNum] = array();

				$additionalRows[$pageNum][$i] = $rowElem;
			}
			$heightsOfPages[] = $pageHeight;

			$downThenOver = ((isset($ps)) ? $ps['sheet']['page_order'] : true);
			if ($format != self::FORMAT_PDF)
			{
				$rows = $grid->appendChild($xml_doc->createElement('rows'));
				$col_widths = $rows->appendChild($xml_doc->createElement('col_widths'));
				for ($i=0; $i<count($gridData['col_widths']); $i++)
				{
					$col_w = $xml_doc->createElement('col_w');
					$col_w->setAttribute('w', $this->_px2pt($gridData['col_widths'][$i]) . 'pt');
					$col_widths->appendChild($col_w);
				}

				for ($pageNum=0; $pageNum<count($additionalRows); $pageNum++)
					for ($i=0; $i<count($additionalRows[$pageNum]); $i++)
						$rows->appendChild($additionalRows[$pageNum][$i]);
			}
			else if ($downThenOver && ($format == self::FORMAT_PDF))
			{
				$colCount = 0;
				for ($pageNum=0; $pageNum<count($additionalRows); $pageNum++)
				{
					$col_widths = $xml_doc->createElement('col_widths');
					$pageWidth = 0;
					for (; $colCount < count($gridData['col_widths']); $colCount++)
					{
						$pageWidth += $gridData['col_widths'][$colCount];
						if (($pageWidth > $pageBodyWpx) && ($colCount > 0))
						{
							$pageWidth -= $gridData['col_widths'][$colCount];
							break;
						}
						else
						{
							$col_w = $xml_doc->createElement('col_w');
							$col_w->setAttribute('w', $this->_px2pt($gridData['col_widths'][$colCount]) . 'pt');
							$col_widths->appendChild($col_w);
						}
					}

					$rows = $xml_doc->createElement('rows');
					if ($pageNum < (count($additionalRows) - 1))
						$rows->setAttribute('page_breaker', 'page');
					$rows->setAttribute('rows_width', $this->_px2pt($pageWidth) . 'pt');
					$rows->setAttribute('space_width', $this->_px2pt(($pageBodyWpx - $pageWidth) / 2));
					$pageHeightIndex = 0;
					$rows->setAttribute('space_height', $this->_px2pt(($pageBodyHpx - $heightsOfPages[$pageHeightIndex++]) / 2));
					$rows = $grid->appendChild($rows);

					$rows->appendChild($col_widths);

					$pageHeight = 0;
					for ($i=0; $i<count($additionalRows[$pageNum]); $i++)
					{
						$pageHeight += $gridData['row_heights'][$i];
						// Workaround for gridlines size
						if (isset($ps) && $ps['sheet']['gridlines'])
							$pageHeight += self::GRIDLINE_SIZE / self::PX2PT;

						if (($pageHeight > $pageBodyHpx) && ($i > 0))
						{
							$pageHeight = 0;

							$rows = $xml_doc->createElement('rows');
							if ($pageNum < (count($additionalRows) - 1))
								$rows->setAttribute('page_breaker', 'page');
							$rows->setAttribute('rows_width', $this->_px2pt($pageWidth) . 'pt');
							$rows->setAttribute('space_width', $this->_px2pt(($pageBodyWpx - $pageWidth) / 2));
							$rows->setAttribute('space_height', $this->_px2pt(($heightsOfPages[$pageHeightIndex++]) / 2)); // set space for verticaly centered
							$rows = $grid->appendChild($rows);
							$rows->appendChild($col_widths->cloneNode(TRUE));
						}

						$rows->appendChild($additionalRows[$pageNum][$i]);
					}
				}
			}
			else
			{
				$pageNum = 0; $pageWidth = 0; $pageColWidths = array(); $pageWidths = array();
				for ($i=0; $i<count($gridData['col_widths']); $i++)
				{
					$pageWidth += $gridData['col_widths'][$i];
					if (($pageWidth > $pageBodyWpx) && ($i > 0))
					{
						$pageWidth -= $gridData['col_widths'][$i];
						$pageWidths[$pageNum] = $pageWidth;
						$pageNum++;
						$pageWidth = $gridData['col_widths'][$i];
					}

					if (!isset($pageColWidths[$pageNum]))
						$pageColWidths[$pageNum] = array();
					$pageColWidths[$pageNum][] = $gridData['col_widths'][$i];
				}
				$pageWidths[$pageNum] = $pageWidth;

				$strtRowId = 0; $pageHeight = 0; $pageHeightIndex = 0;
				for ($i=0; $i<count($gridData['row_heights']); $i++)
				{
					$pageHeight += $gridData['row_heights'][$i];
					// Workaround for gridlines size
					if (isset($ps) && $ps['sheet']['gridlines'])
						$pageHeight += self::GRIDLINE_SIZE / self::PX2PT;

					if (($pageHeight > $pageBodyHpx) || ($i == (count($gridData['row_heights']) - 1)))
					{
						for ($pageNum=0; $pageNum<count($additionalRows); $pageNum++)
						{
							$rows = $xml_doc->createElement('rows');
							if ($pageNum < (count($additionalRows) - 1))
								$rows->setAttribute('page_breaker', 'page');
							$rows->setAttribute('rows_width', $this->_px2pt($pageWidths[$pageNum]) . 'pt');
							$rows->setAttribute('space_width', $this->_px2pt(($pageBodyWpx - $pageWidth) / 2));
							$rows->setAttribute('space_height', $this->_px2pt(($pageBodyHpx - $heightsOfPages[$pageHeightIndex++]) / 2));
							$rows = $grid->appendChild($rows);

							$col_widths = $rows->appendChild($xml_doc->createElement('col_widths'));
							$tmpCells = $pageColWidths[$pageNum];
							foreach ($tmpCells as $cellW)
							{
								$col_w = $xml_doc->createElement('col_w');
								$col_w->setAttribute('w', $this->_px2pt($cellW) . 'pt');
								$col_widths->appendChild($col_w);
							}

							$lastRowId = (($i == (count($gridData['row_heights']) - 1)) ? ($i + 1) : $i);
							for ($j=$strtRowId; $j<$lastRowId; $j++)
								$rows->appendChild($additionalRows[$pageNum][$j]);
						}

						$strtRowId = $i;
						$pageHeight = $gridData['row_heights'][$i];
					}
				}
			}
		}

		// setup main table width
		$totalWidth = 0;
		for ($i=0; $i<count($gridData['col_widths']); $i++)
			$totalWidth += $gridData['col_widths'][$i];

		$grid->setAttribute('width', ($totalWidth) . 'px');

		if (isset($ps) && isset($ps['hf']))
			$hf = $ps['hf'];

		$sheetpropertiesNode = $xml_doc->getElementsByTagName('sheetproperties')->item(0);
		$hfData = $this->_getHeaderFooterData();
		// setting header
		$headerElem = $xml_doc->createElement('header');
		$headerElem->setAttribute('show', (($this->show_header_footer & 0x02) ? 'TRUE' : 'FALSE'));
		$tmpElem = $xml_doc->createElement('left_field', $hfData['header']['left']);
		if (isset($hf))
			$this->_setHeaderFooterCellStyle($hf['header']['left_style'], $tmpElem);
		$headerElem->appendChild($tmpElem);
		$tmpElem = $xml_doc->createElement('center_field', $hfData['header']['center']);
		if (isset($hf))
			$this->_setHeaderFooterCellStyle($hf['header']['center_style'], $tmpElem);
		$headerElem->appendChild($tmpElem);
		$tmpElem = $xml_doc->createElement('right_field', $hfData['header']['right']);
		if (isset($hf))
			$this->_setHeaderFooterCellStyle($hf['header']['right_style'], $tmpElem);
		$headerElem->appendChild($tmpElem);
		$sheetpropertiesNode->appendChild($headerElem);
		// setting footer
		$footerElem = $xml_doc->createElement('footer');
		$footerElem->setAttribute('show', (($this->show_header_footer & 0x01) ? 'TRUE' : 'FALSE'));
		$tmpElem = $xml_doc->createElement('left_field', $hfData['footer']['left']);
		if (isset($hf))
			$this->_setHeaderFooterCellStyle($hf['footer']['left_style'], $tmpElem);
		$footerElem->appendChild($tmpElem);
		$tmpElem = $xml_doc->createElement('center_field', $hfData['footer']['center']);
		if (isset($hf))
			$this->_setHeaderFooterCellStyle($hf['footer']['center_style'], $tmpElem);
		$footerElem->appendChild($tmpElem);
		$tmpElem = $xml_doc->createElement('right_field', $hfData['footer']['right']);
		if (isset($hf))
			$this->_setHeaderFooterCellStyle($hf['footer']['right_style'], $tmpElem);
		$footerElem->appendChild($tmpElem);
		$sheetpropertiesNode->appendChild($footerElem);

		$this->data_xml = $xml_doc;
	}

	private function _parseStyle($style, &$styles =null)
	{
		if (!is_array($styles))
			$styles = array();

		$dataStyles = explode(';', $style);
		for($i=0; $i<(count($dataStyles) - 1); $i++)
		{
			$styleArr = explode(':', $dataStyles[$i]);
			$styles[trim($styleArr[0])] = trim($styleArr[1]);
		}

		return $styles;
	}

	private function _gen_error_data($msg) {
		$initialXML = '<?xml version="1.0" encoding="UTF-8"?><message>' .
			'<img path="' . $this->imgs_url . 'warning.png" w="48px" h="48px"></img>' .
			'<text>' . $msg . '</text>' .
		'</message>';

		$this->data_xml = new DOMDocument();
		$this->data_xml->loadXML($initialXML);

		$this->type = self::TYPE_ERR;
	}

	private function _px2pt($pixels)
	{
		return $pixels*self::PX2PT;
	}

	private function _getHeaderFooterData()
	{
		$vars = array('&[Page]', '&[Pages]', '&[Date]', '&[Time]', '&[File]', '&[Tab]', '&[Picture]');
		$vals = array('#@|Page|@#', '#@|Pages|@#', date(self::HF_DATE), date(self::HF_TIME), '', '', '');

		// &[Tab]
		$worksheets = $this->wss_ajax->getSheets();
		$curr_wsid = $this->wss_ajax->getCurrWsId();
		for ($i=0; $i<count($worksheets); $i+=2)
			if ($worksheets[$i][0] == $curr_wsid)
			{
				$vals[5] = $worksheets[$i][1];
				break;
			}

		// &[File]
		$workbooks = $this->wss_ajax->getLoadedBooks();
		$curr_wbid = $this->wss_ajax->getCurrWbId();
		for ($i=0; $i<count($workbooks); $i+=2)
			if ($workbooks[$i][0] == $curr_wbid)
			{
				$vals[4] = $workbooks[$i][1];
				break;
			}

		// Page Setup Object
		if (isset($_SESSION['wss_page_setup']) && isset($_SESSION['wss_page_setup'][$curr_wbid]) && isset($_SESSION['wss_page_setup'][$curr_wbid][$curr_wsid]) && isset($_SESSION['wss_page_setup'][$curr_wbid][$curr_wsid]['hf']))
		{
			$hf = $_SESSION['wss_page_setup'][$curr_wbid][$curr_wsid]['hf'];
			return array(
				'header' => array('left' => str_replace($vars, $vals, $hf['header']['left_value']), 'center' => str_replace($vars, $vals, $hf['header']['center_value']), 'right' => str_replace($vars, $vals, $hf['header']['right_value'])),
				'footer' => array('left' => str_replace($vars, $vals, $hf['footer']['left_value']), 'center' => str_replace($vars, $vals, $hf['footer']['center_value']), 'right' => str_replace($vars, $vals, $hf['footer']['right_value']))
			);
		}
		else
			return array(
				'header' => array('left' => '', 'center' => '', 'right' => ''),
				'footer' => array('left' => '', 'center' => '', 'right' => '')
			);
	}

	private function _setHeaderFooterCellStyle($style, $elem)
	{
		$styles = $this->_parseStyle($style);

		foreach($styles as $styleName => $styleValue)
		{
			$tmpStyleValue = $this->_checkStyle($styleName, $styleValue);
			$elem->setAttribute($styleName, $tmpStyleValue);
		}
	}

	private function _checkStyle($styleName, $styleValue)
	{
		switch ($styleName)
		{
			case 'background-image':
				if ($styleValue != 'none')
					return $this->home_url . substr($styleValue, 4, -1);
				break;
		}

		return $styleValue;
	}

	private function _getMergedCellSize($pos, $size, $row_heights, $col_widths)
	{
		$h = 0;
		for ($i=$pos[0]; $i<($pos[0]+$size[0]); $i++)
			$h += $row_heights[$i];

		$w = 0;
		for ($i=$pos[1]; $i<($pos[1]+$size[1]); $i++)
			$w += $col_widths[$i];

		return array($h, $w);
	}
}

?>