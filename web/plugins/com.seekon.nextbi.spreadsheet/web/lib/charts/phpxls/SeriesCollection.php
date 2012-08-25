<?php

/*
 * Copyright (C) 2006-2009 Jedox AG, Freiburg, Germany
 * http://www.jedox.com/
 *
 * \author
 * Predrag Malicevic <predrag.malicevic@develabs.com>
 *
 * \version
 * SVN: $Id: SeriesCollection.php 2450 2009-11-25 20:47:40Z predragm $
 *
 * \see
 * LICENSE.txt
 *
 */

/**
 * A collection of all the Series objects in the specified chart or chart group.
 * @package wsscharts
 **/
class SeriesCollection extends Collection
{
	function Add (array $Source, $Rowcol = null, $SeriesLabels = null, $CategoryLabels = null, $Replace = false)
	{
		if ($this->Parent instanceof Chart)
			$chartobj = $this->Parent;
		else if ($this->Parent->Parent instanceof Chart)
			$chartobj = $this->Parent->Parent;
		else if ($this->Parent->Parent->Parent instanceof Chart)
			$chartobj = $this->Parent->Parent->Parent;
		else
			return false;

		$typeConf = XLSConfig::$chartTypeConfig[$chartobj->ChartType];

		$source_len = count($Source);
		$item_len = count($Source[0]);

		if ($Source[0][0] == '')
		{
			$left_labels = $item_len > 1;
			$top_labels = $source_len > 1;
		}
		else
		{
			$top_labels = !is_numeric($tr = $Source[0][$item_len - 1]) && $tr != '' ? true : false;
			$left_labels = !is_numeric($bl = $Source[$source_len - 1][0]) && $bl != '' ? true : false;
		}

		if ($Rowcol == null)
		{
			$rownum = $top_labels ? $source_len - 1 : $source_len;
			$colnum = $left_labels ? $item_len - 1 : $item_len;

			if ($rownum > $colnum || ($rownum == 1 && $colnum == 1 && !$left_labels))
			{
				$chartobj->PlotBy = $Rowcol = XlRowCol::xlColumns;
				$SeriesLabels = $top_labels;
				$CategoryLabels = $left_labels;
			}
			else
			{
				$chartobj->PlotBy = $Rowcol = XlRowCol::xlRows;
				$SeriesLabels = $left_labels;
				$CategoryLabels = $top_labels;
			}
		}
		else if ($Rowcol == XlRowCol::xlRows)
		{
			if (!is_bool($SeriesLabels))
				$SeriesLabels = $left_labels;

			if (!is_bool($CategoryLabels))
				$CategoryLabels =  $top_labels;
		}
		else
		{
			if (!is_bool($SeriesLabels))
				$SeriesLabels = $top_labels;

			if (!is_bool($CategoryLabels))
				$CategoryLabels =  $left_labels;
		}

		if ($Rowcol != XlRowCol::xlRows)
		{
			$_Source = array();

			for ($i = 0; $i < $source_len; ++$i)
				for ($_len = count($Source[$i]), $j = 0; $j < $_len; ++$j)
					$_Source[$j][$i] = $Source[$i][$j];

			$Source = &$_Source;

			$source_len = count($Source);
			$item_len = count($Source[0]);
		}

		if (isset($typeConf['forceCatLbls']) && $source_len > 1)
		{
			$CategoryLabels = true;

			if ($SeriesLabels)
			{
				$cat_start = 1;
				$cat_end = $item_len - 1;
			}
			else
			{
				$cat_start = 0;
				$cat_end = $item_len;
			}

			for ($i = $cat_start; $i < $item_len; ++$i)
				if (!is_numeric($Source[0][$i]))
				{
					$cat_invalid = true;
					break;
				}

			if (isset($cat_invalid))
				array_splice($Source[0], $cat_start, $cat_end, range(1, $cat_end));
		}

		$chartobj->_has_ser_lbls = $SeriesLabels;
		$chartobj->_has_cat_lbls = $CategoryLabels;

		$old_series_num = $this->Count;

		if ($old_series_num == 0 || ($Replace == true && $CategoryLabels == true))
		{
			$category_labels = $CategoryLabels ? array_slice($Source[0], $SeriesLabels ? 1 : 0) : range(1, $SeriesLabels ? $item_len - 1 : $item_len);

			if ($old_series_num > 0 && isset($this->Item(1)->XValues))
				for ($i = 1; $i <= $old_series_num; ++$i)
					$this->Item($i)->XValues = $category_labels;

			for ($cnt = $chartobj->Axes->Count, $i = 1; $i <= $cnt; ++$i)
			{
				$axis = $chartobj->Axes->Item($i);

				if ($axis->Type == XlAxisType::xlCategory)
				{
					$axis->CategoryNames = $category_labels;
					break;
				}
			}
		}
		else
			for ($cnt = $chartobj->Axes->Count, $i = 1; $i <= $cnt; ++$i)
			{
				$axis = $chartobj->Axes->Item($i);

				if ($axis->Type == XlAxisType::xlCategory)
				{
					$category_labels = $axis->CategoryNames;
					break;
				}
			}


		$series_num = $CategoryLabels ? $source_len - 1 : $source_len;
		$series = array_merge($this->_take_members(), $chartobj->_init_part(ChartInit::INIT_SERIES, $this, $series_num - $old_series_num));

		if ($this->Parent instanceof ChartGroup)
			$chartobj->SeriesCollection->_empty();
		else if ($this->Parent instanceof Chart)
			$chartobj->_get_set_chartgroup()->SeriesCollection->_empty();

		if (isset($chartobj->HasLegend) && $chartobj->HasLegend == true && isset($chartobj->Legend)
				&& $chartobj->_has_part(ChartInit::INIT_LEGENDENTRY))
			$legendentries = array_merge($chartobj->Legend->LegendEntries->_take_members(), $chartobj->_init_part(ChartInit::INIT_LEGENDENTRY, $chartobj->Legend->LegendEntries, $series_num - $old_series_num));

		$palette = XLSConfig::$paletteData[isset($typeConf['defPalette']) ? $typeConf['defPalette'] : XLSConfig::PALETTE_DEFAULT];

		$do_point = isset($series[0]->Points) && $chartobj->_has_part(ChartInit::INIT_POINT);
		$do_dl = isset($series[0]->HasDataLabels) && $series[0]->HasDataLabels == true && $chartobj->_has_part(ChartInit::INIT_DATALABEL);

		for ($p = 0, $j = $CategoryLabels ? 1 : 0, $i = 0; $i < $series_num; ++$i, ++$j)
		{
			$series_len = count($Source[$j]);

			if ($SeriesLabels)
			{
				$series[$i]->Name = $Source[$j][0] != '' ? $Source[$j][0] : '   ';
				$series[$i]->Values = array_slice($Source[$j], 1);
			}
			else
			{
				$series[$i]->Name = 'Series' . ($i + 1);
				$series[$i]->Values = $Source[$j];
			}

			if (isset($series[$i]->XValues))
				$series[$i]->XValues = $category_labels;

			$scolor_set = true;

			if ($i >= $old_series_num)
			{
				if (isset($series[$i]->Interior))
					$series[$i]->Interior->Color = $palette[$p];
				else if (isset($series[$i]->Border))
					$series[$i]->Border->Color = $palette[$p];
				else
					$scolor_set = false;
			}

			if ($scolor_set && $palette[++$p] == null)
				$p = 0;

			if ($do_point)
			{
				$old_points_num = $series[$i]->Points->Count;
				$points_num = $SeriesLabels ? $series_len - 1 : $series_len;
				$points = array_merge($series[$i]->Points->_take_members(), $chartobj->_init_part(ChartInit::INIT_POINT, $series[$i]->Points, $points_num - $old_points_num));

				if ($do_dl)
				{
					if ($i >= $old_series_num)
					{
						$obj = new DataLabels($series[$i]);
						$series[$i]->__add('DataLabels', $obj, 'Object');
					}

					$datalabels = array_merge($series[$i]->DataLabels->_take_members(), $chartobj->_init_part(ChartInit::INIT_DATALABEL, null, $points_num - $old_points_num));
				}

				for ($pk = 0, $k = 0; $k < $points_num; ++$k)
				{
					if (isset($datalabels[$k]))
					{
						if (isset($category_labels[$k]))
							$datalabels[$k]->Text = $datalabels[$k]->Caption = $category_labels[$k];

						if ($k >= $old_points_num)
						{
							$datalabels[$k]->__m['Parent'] = $points[$k];

							$points[$k]->__add('DataLabel', $datalabels[$k], 'Object');
							$series[$i]->DataLabels->_add($datalabels[$k]);
						}
					}

					$pcolor_set = !$scolor_set;

					if ($k >= $old_points_num)
					{
						if (isset($points[$k]->Interior))
							$points[$k]->Interior->Color = $scolor_set ? $series[$i]->Interior->Color : $palette[$pk];
						else
							$pcolor_set = false;
					}

					if ($pcolor_set && $palette[++$pk] == null)
						$pk = 0;

					$series[$i]->Points->_add($points[$k], $k + 1);
				}
			}

			$pos = $i + 1;

			$this->_add($series[$i], $pos);

			if ($this->Parent instanceof ChartGroup)
				$chartobj->SeriesCollection->_add($series[$i], $pos);
			else if ($this->Parent instanceof Chart)
				$chartobj->_get_set_chartgroup()->SeriesCollection->_add($series[$i], $pos);

			if (isset($legendentries[$i]))
				$chartobj->Legend->LegendEntries->_add($legendentries[$i], $pos);
		}

		if (isset($typeConf['fixAxisLeft']))
		{
			// Adjustment for long numbers on Y Axis
			$charW = XLSConfig::$configConst['pointsPerCharForAxis'];
			$leftTitle = XLSConfig::$configConst['leftSpaceWithAxisTitle'];
			$leftNoTitle = XLSConfig::$configConst['leftSpaceWithoutAxisTitle'];
			if (!isset($typeConf['swapXY']))
			{
				if (!isset($typeConf['is100']))
				{
					$max = strlen(strval(round($chartobj->SeriesCollection(1)->Values[0], 2)));
					for ($i = 1; $i <= $chartobj->SeriesCollection->Count(); $i ++)
					{
						for ($j = 0; $j < count($chartobj->SeriesCollection($i)->Values); $j++)
						{
							if ($max < strlen(strval(round($chartobj->SeriesCollection($i)->Values[$j], 2))))
								$max = strlen(strval(round($chartobj->SeriesCollection($i)->Values[$j], 2)));
						}
					}
				}
				else
					$max = XLSConfig::$configConst['leftNumOfCharsFor100'];

				if (!empty($chartobj->Axes))
					$diff = $max * $charW + (($chartobj->Axes(2)->HasTitle) ? $leftTitle : $leftNoTitle) - $chartobj->PlotArea->InsideLeft;
				else
					$diff = $max * $charW + $leftNoTitle - $chartobj->PlotArea->InsideLeft;
			}
			else
			{
				$max = strlen($chartobj->Axes(1)->CategoryNames[0]);
				for ($i = 1; $i <= count($chartobj->Axes(1)->CategoryNames); $i ++)
				{
					if ($max < strlen($chartobj->Axes(1)->CategoryNames[$i]))
						$max = strlen($chartobj->Axes(1)->CategoryNames[$i]);
				}

				if (!empty($chartobj->Axes))
					$diff = $max * $charW + (($chartobj->Axes(1)->HasTitle) ? $leftTitle : $leftNoTitle) - $chartobj->PlotArea->InsideLeft;
				else
					$diff = $max * $charW + $leftNoTitle - $chartobj->PlotArea->InsideLeft;
			}

			$chartobj->PlotArea->InsideLeft += $diff;
			$chartobj->PlotArea->InsideWidth -= $diff;
		}

		// Adjusting Legend size due to Long Legend Names
		if ($chartobj->HasLegend)
		{
			$maxLen = 0;
			for ($i=1; $i<=$chartobj->SeriesCollection->Count(); $i++)
			{
				if (strlen($chartobj->SeriesCollection->Item($i)->Name) > $maxLen)
					$maxLen = strlen($chartobj->SeriesCollection->Item($i)->Name);
			}

			$totalLegendW = $maxLen * XLSConfig::$configConst['pointPerCharForLegendNames']; // + 20;
			if ($totalLegendW > $chartobj->Legend->Width)
			{
				$diff = 0;
				if ($totalLegendW > ($chartobj->ChartArea->Width * XLSConfig::$configConst['maxLegendWofChartW']))
				{
					$diff = $chartobj->ChartArea->Width * XLSConfig::$configConst['maxLegendWofChartW'] - $chartobj->Legend->Width;
					$chartobj->Legend->Width = $chartobj->ChartArea->Width * XLSConfig::$configConst['maxLegendWofChartW'];
				}
				else
				{
					$diff = $totalLegendW - $chartobj->Legend->Width;
					$chartobj->Legend->Width = $maxLen * XLSConfig::$configConst['pointPerCharForLegendNames'];
				}

				$chartobj->PlotArea->InsideWidth -= $diff;
				$chartobj->Legend->Left -= $diff;
			}
		}

		// Adjustment for center valign of legend
		if(isset($chartobj->Legend->Top) && isset($chartobj->Legend->LegendEntries))
			$chartobj->Legend->Top += XLSConfig::$configConst['legendNameH']*(XLSConfig::$configConst['dump_numOfSeries'] - $chartobj->Legend->LegendEntries->Count());
	}
}

?>