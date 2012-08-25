<?php

/*
 * \brief class representing worksheet elements collection
 *
 * \file WorksheetElementsCollection.php
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
 * Mladen Todorovic <mladen.todorovic@develabs.com>
 * Predrag Malicevic <predrag.malicevic@develabs.com>
 *
 * \version
 * SVN: $Id: WorksheetElementsCollection.php 2984 2010-03-24 15:44:42Z mladent $
 *
 */

class WorksheetElementsCollection {
	const DEFAULT_CHART_LAYOUT = 1;
	const DEFAULT_CHART_STYLE = 2;
//	const DEFAULT_CHART_FONT = 'CALIBRI.TTF';
	const DEFAULT_CHART_FONT = 'Arial';
	const DEFAULT_CHART_FONT_SIZE = 16;

	private $storage = array();

	private function prepare_storage($workbook_id, $worksheet_id) {
		// Check if workbook and worksheet storage exist and create it if necessary.
		if (!array_key_exists($workbook_id, $this->storage))
			$this->storage[$workbook_id] = array();

		if (!array_key_exists($worksheet_id, $this->storage[$workbook_id]))
			$this->storage[$workbook_id][$worksheet_id] = array();
	}

	public function get_element($workbook_id, $worksheet_id, $element_id) {
		if ((!is_null($this->storage[$workbook_id][$worksheet_id])) && isset($this->storage[$workbook_id][$worksheet_id][$element_id]))
			return $this->storage[$workbook_id][$worksheet_id][$element_id];

		return false;
	}

	public function pixel_to_point($pixels) {
		return floatval($pixels * 72 / 96);
	}

	private function &_gen_chart_data($data, $numOfRows)
	{
		if (!is_array($data))
			$data = array($data);

		$parsed_arr = array();
		$len = count($data);
		$col_count = $len / $numOfRows;

		for ($row = array(), $i = 0; $i < $len; )
		{
			$row[] = addcslashes($data[$i], '\'\\');

			if (++$i % $col_count)
				continue;

			$parsed_arr[] = $row;
			$row = array();
		}

		return $parsed_arr;
	}


	// refresh when source data has been changed from UI
	private function _refresh_chart(&$chart, $raw_range_data, $chart_props, $sep_element)
	{
		try
		{
			$enum_refl = new ReflectionClass('XlChartType');
			$enum_ids = $enum_refl->getConstants();

			$rngs = explode($sep_element, $chart_props['props']['SourceData']['General']['Source']);

			$uls = $lrs = array();
			$or1 = $or2 = false;
			$ident = true;

			foreach ($rngs as &$rng)
			{
				$pts = explode(':', strtoupper($rng));

				preg_match('/\$?[A-Z]+\$?([0-9]+)$/', $pts[0], $r1);

				if (!isset($r1[1]))
					continue;

				$r1 = intval($r1[1]);

				if (isset($pts[1]))
				{
				 preg_match('/\$?[A-Z]+\$?([0-9]+)$/', $pts[1], $r2);

				 $r2 = isset($r2[1]) ? intval($r2[1]) : $r1;
				}
				else
					$r2 = $r1;

				if ($or1 !== false && ($or1 != $r1 || $or2 != $r2))
					$ident = false;

				$uls[] = $or1 = $r1;
				$lrs[] = $or2 = $r2;
			}

			if (count($uls) < 1)
				$num_rows = 1;
			else if ($ident)
				$num_rows = $lrs[0] - $uls[0] + 1;
			else
				for ($num_rows = 0, $i = count($uls) - 1; $i >= 0; --$i)
					$num_rows += $lrs[$i] - $uls[$i] + 1;

			$parsed_arr = &$this->_gen_chart_data($raw_range_data, $num_rows);


			$sd_g = $chart_props['props']['SourceData']['General'];

			$chart->SetSourceData($parsed_arr, ($sd_g['GroupBy'] == 'auto') ? null : (($sd_g['GroupBy'] == 'cols') ? XlRowCol::xlColumns : XlRowCol::xlRows));
			$chart->RefreshSourceData((($sd_g['SeriesLabels'] == 'auto') ? null : ($sd_g['SeriesLabels'] == 'yes')), (($sd_g['CategoryLabels'] == 'auto') ? null : ($sd_g['CategoryLabels'] == 'yes')));
			return true;

		}
		catch (Exception $e)
		{
			return false;
		}
	}




	private function _gen_chart($raw_range_data, $chart_props, $sep_element)
	{
		try
		{
			$enum_refl = new ReflectionClass('XlChartType');
			$enum_ids = $enum_refl->getConstants();

			$rngs = explode($sep_element, $chart_props['props']['SourceData']['General']['Source']);

			$uls = $lrs = array();
			$or1 = $or2 = false;
			$ident = true;

			foreach ($rngs as &$rng)
			{
				$pts = explode(':', strtoupper($rng));

				preg_match('/\$?[A-Z]+\$?([0-9]+)$/', $pts[0], $r1);

				if (!isset($r1[1]))
					continue;

				$r1 = intval($r1[1]);

				if (isset($pts[1]))
				{
				 preg_match('/\$?[A-Z]+\$?([0-9]+)$/', $pts[1], $r2);

				 $r2 = isset($r2[1]) ? intval($r2[1]) : $r1;
				}
				else
					$r2 = $r1;

				if ($or1 !== false && ($or1 != $r1 || $or2 != $r2))
					$ident = false;

				$uls[] = $or1 = $r1;
				$lrs[] = $or2 = $r2;
			}

			if (count($uls) < 1)
				$num_rows = 1;
			else if ($ident)
				$num_rows = $lrs[0] - $uls[0] + 1;
			else
				for ($num_rows = 0, $i = count($uls) - 1; $i >= 0; --$i)
					$num_rows += $lrs[$i] - $uls[$i] + 1;

			$parsed_arr = &$this->_gen_chart_data($raw_range_data, $num_rows);


		 	//add for new added properties
			$props = &$chart_props['props'];


			$chart = new Chart();

			//ChartType
			$chart->ChartType = $enum_ids[$props['ChartType']['General']['Type']];

			//General
			$chart->ApplyLayout(((int) $props['General']['Layout'] < 0) ? self::DEFAULT_CHART_LAYOUT : (int) $props['General']['Layout']);
			$chart->ChartStyle = ((int) $props['General']['Style'] < 0) ? self::DEFAULT_CHART_STYLE : (int) $props['General']['Style'];

			//SourceData
			$sd_g = &$chart_props['props']['SourceData']['General'];
			$chart->SetSourceData($parsed_arr, ($sd_g['GroupBy'] == 'auto') ? null : (($sd_g['GroupBy'] == 'cols') ? XlRowCol::xlColumns : XlRowCol::xlRows));
			$chart->InitChart(0, (($sd_g['SeriesLabels'] == 'auto') ? null : ($sd_g['SeriesLabels'] == 'yes')), (($sd_g['CategoryLabels'] == 'auto') ? null : ($sd_g['CategoryLabels'] == 'yes')));

			//ChartArea (only when Edit Chart)
			$chart->ChartArea->Width = $this->pixel_to_point($chart_props['size'][0]);
			$chart->ChartArea->Height = $this->pixel_to_point($chart_props['size'][1]);


			//Legend
			if(!in_array($chart->ChartType, array(XlChartType::xlStockHLC, XlChartType::xlStockOHLC, XlChartType::xlDoughnut, XlChartType::xlDoughnutExploded, XlChartType::xlPie, XlChartType::xlPieExploded, XlChartType::xl3DPie, XlChartType::xl3DPieExploded))){
				//Legend
				$chart->HasLegend = $props['Legend']['General']['HasLegend'];
			}


			//Title
			if(!in_array($chart->ChartType, array(XlChartType::xlDoughnut, XlChartType::xlDoughnutExploded, XlChartType::xlPie, XlChartType::xlPieExploded, XlChartType::xl3DPie, XlChartType::xl3DPieExploded))){
				$chart->HasTitle = $props['Title']['General']['HasTitle']; //change in get char props
				$chart->ChartTitle->Text = $props['Title']['General']['Name'];

				$chart->ChartTitle->Font->Name = self::DEFAULT_CHART_FONT;
				$chart->ChartTitle->Font->Size = self::DEFAULT_CHART_FONT_SIZE;
			}
			else {
				$chart->HasTitle = false;
			}


			//When edit chart TODO: rewrite
			if ($chart_props['operation'] !== 'cct'){


				//Axes
				if (isset($chart->Axes)) {
					if (isset($chart->Axes(1)->AxisTitle)) {
	//					if (!isset($chart->Axes(1)->AxisTitle->Caption))
							$chart->Axes(1)->AxisTitle->__add('Caption', NULL, 'Object');
						$chart->Axes(1)->AxisTitle->Caption = $props['Axes']['HorizontalAxis']['Name'];
					}

					if (isset($chart->Axes(2)->AxisTitle)) {
	//					if (!isset($chart->Axes(2)->AxisTitle->Caption))
							$chart->Axes(2)->AxisTitle->__add('Caption', NULL, 'Object');
						$chart->Axes(2)->AxisTitle->Caption = $props['Axes']['VerticalAxis']['Name'];
					}
				}

	//			(Extended properties used ONLY when editing Chart )
				if ($chart_props['operation'] == 'format'){

	//				ChartArea (only when Edit Chart)
					$chart->ChartArea->Fill->BackColor->RGB = $this->RGBExcelToCD($props['ChartArea']['Fill']['BackColor']); //to BGR format
					$chart->ChartArea->Border->Color = $this->RGBExcelToCD($props['ChartArea']['Border']['Color']); //to BGR format

	//				PlotArea (only when Edit Chart)
					$chart->PlotArea->Fill->BackColor->RGB = $this->RGBExcelToCD($props['PlotArea']['Fill']['BackColor']); //to BGR format
					$chart->PlotArea->Border->Color = $this->RGBExcelToCD($props['PlotArea']['Border']['Color']); //to BGR format

					//Series Data to set
					$this->setSeriesCollectionData($chart, $props['Series']['General']['SeriesCollection']);



	//added for FONT
					$chart->ChartTitle->Font->Name = $props['Title']['Font']['Type'];
					$chart->ChartTitle->Font->Size = $props['Title']['Font']['Size'];
					$chart->ChartTitle->Font->Color = $this->RGBExcelToCD($props['Title']['Font']['Color']);
					$chart->ChartTitle->Font->Italic = $prop_arr['Title']['Font']['Style'] == 'italic'? true:false;
	//				$chart->ChartTitle->Font->Underline = $prop_arr['Title']['Font']['Decoration'] == 'underline'? true:false;
					$chart->ChartTitle->Font->Bold = $prop_arr['Title']['Font']['Weight'] == 'bold' ? true:false;

	//				Legend (only when Edit Chart)
					$chart->Legend->Fill->BackColor->RGB = $this->RGBExcelToCD($props['Legend']['Fill']['BackColor']); //to BGR format
					$chart->Legend->Border->Color = $this->RGBExcelToCD($props['Legend']['Border']['Color']); //to BGR format

					$chart->Legend->Font->Name = $props['Legend']['Font']['Type'];
					$chart->Legend->Font->Size = $props['Legend']['Font']['Size'];
					$chart->Legend->Font->Color = $this->RGBExcelToCD($props['Legend']['Font']['Color']);

					//Axes
					if (isset($chart->Axes)) {
						if (isset($chart->Axes(1)->Parent)) {
	//						Axes - Horizontal Axis (only when Edit Chart)
							$chart->Axes(1)->MinimumScaleIsAuto = $props['Axes']['HorizontalAxis']['MinimumScaleIsAuto'];
							$chart->Axes(1)->MinimumScale = $props['Axes']['HorizontalAxis']['MinimumScale'];
							$chart->Axes(1)->MaximumScaleIsAuto = $props['Axes']['HorizontalAxis']['MaximumScaleIsAuto'];
							$chart->Axes(1)->MaximumScale = $props['Axes']['HorizontalAxis']['MaximumScale'];
							$chart->Axes(1)->MajorUnitIsAuto = $props['Axes']['HorizontalAxis']['MajorUnitIsAuto'];
							$chart->Axes(1)->MajorUnit = $props['Axes']['HorizontalAxis']['MajorUnit'];
							$chart->Axes(1)->MinorUnitIsAuto = $props['Axes']['HorizontalAxis']['MinorUnitIsAuto'];
							$chart->Axes(1)->MinorUnit = $props['Axes']['HorizontalAxis']['MinorUnit'];

							$chart->Axes(1)->TickLabels->Font->Name = $props['Axes']['Font']['Type'];
							$chart->Axes(1)->TickLabels->Font->Size = $props['Axes']['Font']['Size'];
							$chart->Axes(1)->TickLabels->Font->Color = $this->RGBExcelToCD($props['Axes']['Font']['Color']);

						}

						if (isset($chart->Axes(2)->Parent)) {
	//						Axes - Horizontal Axis (only when Edit Chart)
							$chart->Axes(2)->MinimumScaleIsAuto = $props['Axes']['VerticalAxis']['MinimumScaleIsAuto'];
							$chart->Axes(2)->MinimumScale = $props['Axes']['VerticalAxis']['MinimumScale'];
							$chart->Axes(2)->MaximumScaleIsAuto = $props['Axes']['VerticalAxis']['MaximumScaleIsAuto'];
							$chart->Axes(2)->MaximumScale = $props['Axes']['VerticalAxis']['MaximumScale'];
							$chart->Axes(2)->MajorUnitIsAuto = $props['Axes']['VerticalAxis']['MajorUnitIsAuto'];
							$chart->Axes(2)->MajorUnit = $props['Axes']['VerticalAxis']['MajorUnit'];
							$chart->Axes(2)->MinorUnitIsAuto = $props['Axes']['VerticalAxis']['MinorUnitIsAuto'];
							$chart->Axes(2)->MinorUnit = $props['Axes']['VerticalAxis']['MinorUnit'];

							$chart->Axes(2)->TickLabels->Font->Name = $props['Axes']['Font']['Type'];
							$chart->Axes(2)->TickLabels->Font->Size = $props['Axes']['Font']['Size'];
							$chart->Axes(2)->TickLabels->Font->Color = $this->RGBExcelToCD($props['Axes']['Font']['Color']);

						}
					}
				}
		}

			return $chart;

		}
		catch (Exception $e)
		{
			return false;
		}
	}

// ***********************************************************************************



	// Create new chart.
	public function add_chart($conn, $chart_props) {
		$workbook_id = $conn->getCurrWbId();
		$worksheet_id = $conn->getCurrWsId();

		try {
			$cmds = json_encode(
				array(
					array('wadd', '', array(
						'e_type' => 'chart',
						'size' => $chart_props['size'],
						'subtype' => $chart_props['props']['ChartType']['General']['Type'],
						'props' => $chart_props['props'],
						'n_refers_to' => $chart_props['props']['SourceData']['General']['Source'],
						'n_location' => $chart_props['n_location'],
						'pos_offsets' => $chart_props['pos_offsets']
					))
				)
			);
			$res = json_decode($conn->exec($cmds), true);

			if ($res[0][0])
			{
				$chart_id = $res[0][1][0];
				$res = json_decode($conn->exec('[["wget","",[],["n_get_val","n_location"],{"e_id":"' . $chart_id . '"}]]'), true);

				if ($res[0][0])
				{
					$raw_range_data = $res[0][1][0]['n_get_val'];

					if (($chart = $this->_gen_chart($raw_range_data, $chart_props, $conn->getSeparator(WSS::SEP_ELEMENT))) !== false)
					{
						$this->storage[$workbook_id][$worksheet_id][$chart_id] = $chart;
						return $chart_id;
					}
				}
			}
		} catch (Exception $e) {}

		return false;
	}



	public function update_chart($conn, $chart_props) {
		$workbook_id = $conn->getCurrWbId();
		$worksheet_id = $conn->getCurrWsId();

		try {
			if (($chart = $this->get_element($workbook_id, $worksheet_id, $chart_props['id'])) === false)
				return false; // TODO: throw exception

			$chart_id = $chart_props['id'];
			$cmds = json_encode(
				array(
					array('wupd', '', array($chart_id => array(
						'props' => $chart_props['props'],
						'n_refers_to' => $chart_props['props']['SourceData']['General']['Source']
					))),
					array('wget', '', array(), array('n_get_val'/*, 'size'*/), array('e_id' => $chart_id))
				)
			);
			$res = json_decode($conn->exec($cmds), true);

			if ($res[0][0] && $res[1][0])
			{
				$raw_range_data = $res[1][1][0]['n_get_val'];
//				$chart_props['size'] = $res[1][1][0]['size'];


				if ($chart_props['operation'] == 'ssd'){ //set source data
					if (($this->_refresh_chart($chart, $raw_range_data, $chart_props, $conn->getSeparator(WSS::SEP_ELEMENT))) !== false)
						return true;
				}
				else {
					if (($chart = $this->_gen_chart($raw_range_data, $chart_props, $conn->getSeparator(WSS::SEP_ELEMENT))) !== false)
					{
						$this->storage[$workbook_id][$worksheet_id][$chart_id] = $chart;
						return true;
					}
				}

			}
		} catch (Exception $e) {}

		return false;
	}

	// Resize chart.
	public function resize_chart($conn, $chart_props) {
		$workbook_id = $conn->getCurrWbId();
		$worksheet_id = $conn->getCurrWsId();

		try {
			if (($chart = $this->get_element($workbook_id, $worksheet_id, $chart_props['id'])) === false)
				return false; // TODO: throw exception

			if (isset($chart_props['size']))
			{
				$chart->ChartArea->Width = $this->pixel_to_point($chart_props['size'][0]);
				$chart->ChartArea->Height = $this->pixel_to_point($chart_props['size'][1]);
			}

			$cmds = json_encode(
				array(
					array('wupd', '', array($chart_props['id'] => array(
						'n_location' => $chart_props['n_location'],
						'pos_offsets' => $chart_props['pos_offsets'],
						'size' => $chart_props['size']
					)))
				)
			);
			$res = json_decode($conn->exec($cmds), true);

			return $res[0][0];
		} catch (Exception $e) {
			return false;
		}
	}

	// Fetch chart elements.
	public function get_worksheet_elements($conn) {
		$workbook_id = $conn->getCurrWbId();
		$worksheet_id = $conn->getCurrWsId();

		$element_arr = array();
		if (!is_null($this->storage[$workbook_id][$worksheet_id]))
		{
			$res = json_decode($conn->exec('[["wget","",[],["e_id","e_type","size","n_location","pos_offsets"],{"e_type":"chart"}]]'), true);

			if ($res[0][0])
				foreach ($res[0][1] as $ws_element)

					$element_arr[] = array(
						'id' => $ws_element['e_id'],
						'type' => $ws_element['e_type'],

						'n_location' => $ws_element['n_location'],
						'pos_offsets' => $ws_element['pos_offsets'],

						'width' => $ws_element['size'][0],
						'height' => $ws_element['size'][1]
					);
		}

		return $element_arr;
	}

	public function get_workbook_storage($workbook_id) {
		return array_key_exists($workbook_id, $this->storage) ? $this->storage[$workbook_id] : null;
	}

	public function set_workbook_storage($workbook_id, $workbook_elems) {
		$this->storage[$workbook_id] = $workbook_elems;
	}

	public function copy_worksheet_storage($workbook_id, $orig_worksheet_id, $new_worksheet_id) {
		if (array_key_exists($workbook_id, $this->storage) && array_key_exists($orig_worksheet_id, $this->storage[$workbook_id]))
			$this->storage[$workbook_id][$new_worksheet_id] = $this->storage[$workbook_id][$orig_worksheet_id];
	}

	public function remove_worksheet_storage($workbook_id, $worksheet_id) {
		if (array_key_exists($workbook_id, $this->storage) && array_key_exists($worksheet_id, $this->storage[$workbook_id]))
			unset($this->storage[$workbook_id][$worksheet_id]);
	}

	public function refresh_source_data($workbook_id, $worksheet_id, $elem_id, $data, $formula, $sep_element)
	{
		try
		{
			if (($chart = $this->get_element($workbook_id, $worksheet_id, $elem_id)) === false)
				return false; // TODO: throw exception

			$rngs = explode($sep_element, $formula);

			$uls = $lrs = array();
			$or1 = $or2 = false;
			$ident = true;

			foreach ($rngs as &$rng)
			{
				$pts = explode(':', $rng);

				preg_match('/\$?[A-Z]+\$?([0-9]+)$/', $pts[0], $r1);

				if (!isset($r1[1]))
					continue;

				$r1 = intval($r1[1]);

				if (isset($pts[1]))
				{
				 preg_match('/\$?[A-Z]+\$?([0-9]+)$/', $pts[1], $r2);

				 $r2 = isset($r2[1]) ? intval($r2[1]) : $r1;
				}
				else
					$r2 = $r1;

				if ($or1 !== false && ($or1 != $r1 || $or2 != $r2))
					$ident = false;

				$uls[] = $or1 = $r1;
				$lrs[] = $or2 = $r2;
			}

			if (count($uls) < 1)
				$num_rows = 1;
			else if ($ident)
				$num_rows = $lrs[0] - $uls[0] + 1;
			else
				for ($num_rows = 0, $i = count($uls) - 1; $i >= 0; --$i)
					$num_rows += $lrs[$i] - $uls[$i] + 1;

			$parsed_arr = &$this->_gen_chart_data($data, $num_rows);

			$chart->SetSourceData($parsed_arr);
			$chart->RefreshSourceData();


			return true;
		}
		catch (Exception $e)
		{
			return false;
		}
	}

	// Delete element.
	public function delete_element($conn, $element_id) {
		$workbook_id = $conn->getCurrWbId();
		$worksheet_id = $conn->getCurrWsId();

		try {
			if (!is_null($this->storage[$workbook_id][$worksheet_id]) && isset($this->storage[$workbook_id][$worksheet_id][$element_id]))
				unset($this->storage[$workbook_id][$worksheet_id][$element_id]);

			$res = json_decode($conn->exec('[["wdel","",["' . $element_id . '"]]]'), true);

			return $res[0][0];
		} catch (Exception $e) {
			return false;
		}
	}


	public static function RGBExcelToCD($RGB) {

		if ($RGB < 0x0 || $RGB > 0xFFFFFF || $RGB == 'Transparent')
			return '0xFFFFFFFF'; //transparent
		else {
         $RGB = substr($RGB, 2);
         $RGB = str_pad($RGB, 6, '0', STR_PAD_LEFT);
		   $BGR = str_split($RGB, 2);
		   return '0x' . $BGR[2] . $BGR[1] . $BGR[0];
		}
	}

	public function CDToRGBExcel($BGR) {

		if ($BGR < 0x0 || $BGR > 0xFFFFFF)
		   return 'Transparent';
		else {
         $BGR = substr($BGR, 2);
         $BGR = str_pad($BGR, 6, '0', STR_PAD_LEFT);
		   $RGB = str_split($BGR, 2);
		   return '0x' . $RGB[2] . $RGB[1] . $RGB[0];
		}
	}

	public function get_chart_type($conn, $chart_id) {
		$workbook_id = $conn->getCurrWbId();
		$worksheet_id = $conn->getCurrWsId();

		try {
			if (($chart = $this->get_element($workbook_id, $worksheet_id, $chart_id)) === false)
				return false; // TODO: throw exception

			$enum_refl = new ReflectionClass('XlChartType');
			$enum_ids = $enum_refl->getConstants();
			$enum_ids = array_flip($enum_ids);

			return $enum_ids[$chart->ChartType];
		}
		catch (Exception $e) {}

		return false;
	}

	function getRGBPalete(){
		$BRGPalette = XlSConfig::$paletteData;
		$BGRPaletteInfo = XlSConfig::$paletteInfo;
		$RGBPalette = array();

		foreach ($BGRPaletteInfo as $key => $value){

			if (!$value[1])
				continue;

			$palette = strtolower($value[0]);
			$RGBPalette[$palette] = array();

			foreach ($BRGPalette[$key] as $bgr){
				if (isset($bgr))
					$RGBPalette[$palette][] = str_replace("0x", "#", $this->CDToRGBExcel($bgr));
			}

		}

		return $RGBPalette;
	}

	function getSeriesCollectionData(&$chartObj){

		$cType = $chartObj->ChartType;
		$seriesCollectionData = array();

		if (in_array($cType, array(XlChartType::xlPie, XlChartType::xlPieExploded, XlChartType::xl3DPie, XlChartType::xl3DPieExploded, XlChartType::xlDoughnut, XlChartType::xlDoughnutExploded))){

			for ($i = 0, $points = $chartObj->SeriesCollection(1)->Points, $count = $points->Count(); $i<$count; $i++){
				$seriesCollectionData[$i] = array();
				$seriesCollectionData[$i][] = $i+1;
				$seriesCollectionData[$i][] = str_replace("0x", "#", $this->CDToRGBExcel($points->item($i+1)->Interior->Color));
				$seriesCollectionData[$i][] = 'none';
			}
		}
		//Line type chart
		elseif (in_array($cType, array(XlChartType::xlLine, XlChartType::xlLineMarkers, XlChartType::xlLineMarkersStacked, XlChartType::xlLineMarkersStacked100, XlChartType::xlLineStacked, XlChartType::xlLineStacked100, XlChartType::xlLineRotated, XlChartType::xlLineMarkersRotated, XlChartType::xl3DLine))){

			for ($i = 0, $series = $chartObj->SeriesCollection, $count = $series->Count(); $i<$count; $i++){
				$seriesCollectionData[$i] = array();
				$seriesCollectionData[$i][] = $series->item($i+1)->Name;
				$seriesCollectionData[$i][] = str_replace("0x", "#", $this->CDToRGBExcel($series->item($i+1)->Border->Color));
				$seriesCollectionData[$i][] = 'none';
			}
		}

		elseif (in_array($cType, array(XlChartType::xlMeterLineHorizontal, XlChartType::xlMeterLineVertical, XlChartType::xlMeterOdoFull100, XlChartType::xlMeterOdoFull, XlChartType::xlMeterOdoHalf, XlChartType::xlMeterOdoHalf100))){

			//points
			$pointsCollectionData = array();
			if ($points = $chartObj->SeriesCollection(1)->Points){
				for ($i = 0, $count = $points->Count(); $i<$count; $i++){
					$pointsCollectionData[$i] = array();
					$pointsCollectionData[$i][] = $i+1;
					$pointsCollectionData[$i][] = str_replace("0x", "#", $this->CDToRGBExcel($points->item($i+1)->Interior->Color));
					$pointsCollectionData[$i][] = 'none';
				}
			}
			else {
				$pointsCollectionData[0] = null;
			}

			$seriesCollectionData[0] = $pointsCollectionData;

			//zones
			for ($i = 1, $series = $chartObj->SeriesCollection, $count = $series->Count(); $i<$count; $i++){
				$seriesCollectionData[$i] = array();
				$seriesCollectionData[$i][] =  $series->item($i+1)->Name;
				$seriesCollectionData[$i][] = str_replace("0x", "#", $this->CDToRGBExcel($series->item($i+1)->Interior->Color));
				$seriesCollectionData[$i][] = 'none';
			}
		}

		else {

			for ($i = 0, $series = $chartObj->SeriesCollection, $count = $series->Count(); $i<$count; $i++){
				$seriesCollectionData[$i] = array();
				$seriesCollectionData[$i][] = $series->item($i+1)->Name;
				$seriesCollectionData[$i][] = str_replace("0x", "#", $this->CDToRGBExcel($series->item($i+1)->Interior->Color));
				$seriesCollectionData[$i][] = $series->item($i+1)->Border->LineStyle == XlLineStyle::xlContinuous? str_replace("0x", "#", $this->CDToRGBExcel($series->item($i+1)->Border->Color)):'none';
			}

		}

		return $seriesCollectionData;
	}


	function setSeriesCollectionData(&$chartObj, $seriesCollectionData){

		$cType = $chartObj->ChartType;

		if (in_array($cType, array(XlChartType::xlPie, XlChartType::xlPieExploded, XlChartType::xl3DPie, XlChartType::xl3DPieExploded, XlChartType::xlDoughnut, XlChartType::xlDoughnutExploded))){

			for ($i = 0, $points = $chartObj->SeriesCollection(1)->Points, $count = $points->Count(); $i<$count; $i++){
				$points->item($i+1)->Interior->Color = $this->RGBExcelToCD(str_replace("#", "0x", $seriesCollectionData[$i][1]));
			}
		}

		elseif (in_array($cType, array(XlChartType::xlLine, XlChartType::xlLineMarkers, XlChartType::xlLineMarkersStacked, XlChartType::xlLineMarkersStacked100, XlChartType::xlLineStacked, XlChartType::xlLineStacked100, XlChartType::xlLineRotated, XlChartType::xlLineMarkersRotated, XlChartType::xl3DLine))){

			for ($i = 0, $series = $chartObj->SeriesCollection, $count = $series->Count(); $i<$count; $i++){
				$series->item($i+1)->Border->LineStyle = XlLineStyle::xlContinuous;
				$series->item($i+1)->Border->Color = $this->RGBExcelToCD(str_replace("#", "0x", $seriesCollectionData[$i][1]));
			}

		}

		elseif (in_array($cType, array(XlChartType::xlMeterLineHorizontal, XlChartType::xlMeterLineVertical, XlChartType::xlMeterOdoFull100, XlChartType::xlMeterOdoFull, XlChartType::xlMeterOdoHalf, XlChartType::xlMeterOdoHalf100))){

			//points
			for ($i = 0, $points = $chartObj->SeriesCollection(1)->Points, $count = $points->Count(); $i<$count; $i++){
				$points->item($i+1)->Interior->Color = $this->RGBExcelToCD(str_replace("#", "0x", $seriesCollectionData[0][$i][1])); //
			}
			//zones
			for ($i = 1, $series = $chartObj->SeriesCollection, $count = $series->Count(); $i<$count; $i++){
				$series->item($i+1)->Interior->Color = $this->RGBExcelToCD(str_replace("#", "0x", $seriesCollectionData[$i][1]));
			}

		}

		else {

			for ($i = 0, $series = $chartObj->SeriesCollection, $count = $series->Count(); $i<$count; $i++){
				$series->item($i+1)->Interior->Color = $this->RGBExcelToCD(str_replace("#", "0x", $seriesCollectionData[$i][1]));
				$series->item($i+1)->Border->LineStyle = $seriesCollectionData[$i][2] == 'none'? XlLineStyle::xlLineStyleNone:XlLineStyle::xlContinuous;
				$series->item($i+1)->Border->Color = $this->RGBExcelToCD(str_replace("#", "0x", $seriesCollectionData[$i][2]));
			}

		}

	}


	public function get_chart_properties($conn, $chart_id) {
		$workbook_id = $conn->getCurrWbId();
		$worksheet_id = $conn->getCurrWsId();

		try {
			if (($chart = $this->get_element($workbook_id, $worksheet_id, $chart_id)) === false)
				return false; // TODO: throw exception

			$res = json_decode($conn->exec('[["wget","",[],["props", "n_refers_to"],{"e_id":"' . $chart_id . '"}]]'), true);

			if ($res[0][0])
			{
				$res = $res[0][1][0];
				$prop_arr = array();

				$enum_refl = new ReflectionClass('XlChartType');
				$enum_ids = $enum_refl->getConstants();
				$enum_ids = array_flip($enum_ids);

				$prop_arr = array(
					'General' => array(
						'Layout' => $chart->GetLayout(),
						'Style' => $chart->ChartStyle
					),
					'ChartType' => array (
						'General' => array(
							'Type' => $enum_ids[$chart->ChartType]
						)
					),
					'ChartArea' => array (
						'Fill' => array(
							'BackColor' => $this->CDToRGBExcel($chart->ChartArea->Fill->BackColor->RGB)
						),
						'Border' => array(
							'Color' => $this->CDToRGBExcel($chart->ChartArea->Border->Color)
						),
					),
					'PlotArea' => array (
						'Fill' => array(
							'BackColor' => $this->CDToRGBExcel($chart->PlotArea->Fill->BackColor->RGB)
						),
						'Border' => array(
							'Color' => $this->CDToRGBExcel($chart->PlotArea->Border->Color)
						),
					),
					'SourceData' => array (
						'General' => array(
							'Source' => $res['n_refers_to'], //<- get relative source (if rows or columns are inserted into source)
							'GroupBy' => $res['props']['SourceData']['General']['GroupBy'],
							'SeriesLabels' => $res['props']['SourceData']['General']['SeriesLabels'],
							'CategoryLabels' => $res['props']['SourceData']['General']['CategoryLabels']
						)
					),
					'Series' => array (
						'General' => array(
							'Palette' => $this->getRGBPalete(),
							'SeriesCollection' => $this->getSeriesCollectionData($chart)
						)
					),
					'Title' => array (
						'General' => array(
							'HasTitle' => $chart->HasTitle,
							'Name' => $chart->ChartTitle->Text
						),
						'Font' => array(
							'Type' => $chart->ChartTitle->Font->Name,
							'Size' => $chart->ChartTitle->Font->Size,
							'Color' => $this->CDToRGBExcel($chart->ChartTitle->Font->Color),
							'Style' => $chart->ChartTitle->Font->Italic ? 'italic':'regular',
							'Weight' => $chart->ChartTitle->Font->Bold ? 'bold':false
						)
					),
					'Legend' => array (
						'General' => array(
							'HasLegend' => $chart->HasLegend,
						),
						'Fill' => array(
							'BackColor' => $this->CDToRGBExcel($chart->Legend->Fill->BackColor->RGB)
						),
						'Border' => array(
							'Color' => $this->CDToRGBExcel($chart->Legend->Border->Color)
						),
						'Font' => array(
							'Type' => $chart->Legend->Font->Name,
							'Size' => $chart->Legend->Font->Size,
							'Color' => $this->CDToRGBExcel($chart->Legend->Font->Color)
						)
					)
				);


				if(!in_array($chart->ChartType, array(XlChartType::xlDoughnut, XlChartType::xlDoughnutExploded, XlChartType::xlPie, XlChartType::xlPieExploded, XlChartType::xl3DPie, XlChartType::xl3DPieExploded))){
					//Horizontal Axis

					if (isset($chart->Axes)){
						if (isset($chart->Axes(1)->Parent)){
							$prop_arr['Axes']['HorizontalAxis']['Name'] = $chart->Axes(1)->AxisTitle->Caption;
							$prop_arr['Axes']['HorizontalAxis']['MinimumScaleIsAuto'] = $chart->Axes(1)->MinimumScaleIsAuto;
							$prop_arr['Axes']['HorizontalAxis']['MinimumScale'] = $chart->Axes(1)->MinimumScale;
							$prop_arr['Axes']['HorizontalAxis']['MaximumScaleIsAuto'] = $chart->Axes(1)->MaximumScaleIsAuto;
							$prop_arr['Axes']['HorizontalAxis']['MaximumScale'] = $chart->Axes(1)->MaximumScale;
							$prop_arr['Axes']['HorizontalAxis']['MajorUnitIsAuto'] = $chart->Axes(1)->MajorUnitIsAuto;
							$prop_arr['Axes']['HorizontalAxis']['MajorUnit'] = $chart->Axes(1)->MajorUnit;
							$prop_arr['Axes']['HorizontalAxis']['MinorUnitIsAuto'] = $chart->Axes(1)->MinorUnitIsAuto;
							$prop_arr['Axes']['HorizontalAxis']['MinorUnit'] = $chart->Axes(1)->MinorUnit;

							$prop_arr['Axes']['Font']['Type'] = $chart->Axes(1)->TickLabels->Font->Name;
							$prop_arr['Axes']['Font']['Size'] = $chart->Axes(1)->TickLabels->Font->Size;
							$prop_arr['Axes']['Font']['Color'] = $this->CDToRGBExcel($chart->Axes(1)->TickLabels->Font->Color);
						}
						if (isset($chart->Axes(2)->Parent)){
							$prop_arr['Axes']['VerticalAxis']['Name'] = $chart->Axes(2)->AxisTitle->Caption;
							$prop_arr['Axes']['VerticalAxis']['MinimumScaleIsAuto'] = $chart->Axes(2)->MinimumScaleIsAuto;
							$prop_arr['Axes']['VerticalAxis']['MinimumScale'] = $chart->Axes(2)->MinimumScale;
							$prop_arr['Axes']['VerticalAxis']['MaximumScaleIsAuto'] = $chart->Axes(2)->MaximumScaleIsAuto;
							$prop_arr['Axes']['VerticalAxis']['MaximumScale'] = $chart->Axes(2)->MaximumScale;
							$prop_arr['Axes']['VerticalAxis']['MajorUnitIsAuto'] = $chart->Axes(2)->MajorUnitIsAuto;
							$prop_arr['Axes']['VerticalAxis']['MajorUnit'] = $chart->Axes(2)->MajorUnit;
							$prop_arr['Axes']['VerticalAxis']['MinorUnitIsAuto'] = $chart->Axes(2)->MinorUnitIsAuto;
							$prop_arr['Axes']['VerticalAxis']['MinorUnit'] = $chart->Axes(2)->MinorUnit;

							$prop_arr['Axes']['Font']['Type'] = $chart->Axes(1)->TickLabels->Font->Name;
							$prop_arr['Axes']['Font']['Size'] = $chart->Axes(1)->TickLabels->Font->Size;
							$prop_arr['Axes']['Font']['Color'] = $this->CDToRGBExcel($chart->Axes(1)->TickLabels->Font->Color);
						}
					}

				}

				return $prop_arr;
			}
		}
		catch (Exception $e) {}

		return false;
	}


}


?>