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
 * Mladen Todorovic <mladen.todorovic@develabs.com>
 * Drazen Kljajic <drazen.kljajic@develabs.com>
 *
 * \version
 * SVN: $Id: FormatConvertor.php 2534 2009-12-11 14:45:01Z mladent $
 *
 */

class FormatConvertor {
   public static $fontMapping = array('Calibri'          => 'CALIBRI.TTF',
                                      'Tahoma'           => 'tahoma.ttf',
                                      'Sans Serif'       => 'micross.ttf',
                                      'Times New Roman'  => 'times.ttf',
                                      'Arial'            => 'arial.ttf',
                                      'Arial Narrow'     => 'arialn.ttf',
                                      'Verdana'          => 'verdana.ttf',
                                      'Monotype Corsiva' => 'mtcorsva.ttf',
                                      'Georgia'          => 'georgia.ttf',
                                      'Impact'           => 'impact.ttf',
                                      'Trebuchet MS'     => 'trebuc.ttf',
                                      'Comic Sans MS'    => 'comic.ttf',
                                      'Arial Black'      => 'ariblk.ttf',
                                      'Lucida Console'   => 'lucon.ttf');

	/**
	 * FUNCTION TO BE REMOVED
	 **/
	public static function hexColor($color) {
		return $color;
	}

	public function colorObject(&$Chart, $refcontext = null, $path = null) {
		if (is_null($path))
			return 'Transparent';
		else {
			eval('$colorObj = $' . str_replace('.', '->', $path) . ';');
			return $this->RGBExcelToCD($colorObj->RGB);
		}
	}

   /**
    * Convert Excel RGB format(BGR) to CD format
    **/
	public static function RGBExcelToCD($RGB) {
		if ($RGB < 0x0 || $RGB > 0xFFFFFF)
		   return 'Transparent';
		else {
         $RGB = substr($RGB, 2);
         $RGB = str_pad($RGB, 6, '0', STR_PAD_LEFT);
		   $BGR = str_split($RGB, 2);
		   return '0x' . $BGR[2] . $BGR[1] . $BGR[0];
		}
	}

	/**
	 * FUNCTION TO BE REMOVED
	 **/
	public static function hexNumber($hex) {
		return $hex;
	}

	public function textObject(&$Chart, $refcontext = null, $path = null) {
	   if (is_null($path))
         return '';

      $CDMLString = '<*';
		eval('$textObj = $' . str_replace('.', '->', $path) . ';');
		if ( isset(FormatConvertor::$fontMapping[$textObj->Font->Name]) ) {
		   $CDMLString .= 'font=' . FormatConvertor::$fontMapping[$textObj->Font->Name];
         if ( $textObj->Font->Bold || stripos( $textObj->Font->FontStyle, 'Bold') !== false )
            $CDMLString .= ' Bold';
         if ( $textObj->Font->Italic || stripos( $textObj->Font->FontStyle, 'Italic') !== false )
            $CDMLString .= ' Italic';
		   $CDMLString .= ',';
		}

      $CDMLString .= 'size=' . $textObj->Font->Size . ',';
      if ( $textObj->Font->Subscript )
         $CDMLString .= 'sub,';
      if ( $textObj->Font->Superscript )
         $CDMLString .= 'super,';

      if ( $textObj->Font->Underline != 606358669 )
         $CDMLString .= 'underline=1,';
      $CDMLString .= 'color=' . str_replace('0x', '', $this->RGBExcelToCD($textObj->Font->Color)) . ',';
      //if ( $textObj->Interior->Color >= 0 && $textObj->Interior->Color <= 0xFFFFFF )
      //   $CDMLString .= 'bgColor=' . substr($textObj->Interior->Color,2) . '';

		return '\'' . $CDMLString . '*>' . addcslashes($textObj->Text, '\'\\') . '\'';
	}

	public static function escapedText($text)
	{
		return '\'' . addcslashes($text, '\'\\') . '\'';
	}

	public static function fontFileName($font) {
		return '\'' . (isset(FormatConvertor::$fontMapping[$font]) ? FormatConvertor::$fontMapping[$font] : 'CALIBRI.TTF') . '\'';
	}

	public function getPieCenterX(&$Chart, $refcontext = null, $path = null)
	{
		return UnitConvertor::pointToPixel($Chart->ChartArea->Width / 2);
	}

	public function getPieCenterY(&$Chart, $refcontext = null, $path = null)
	{
		$topPart = ($Chart->HasTitle) ? $Chart->PlotArea->InsideTop : 0;
		return UnitConvertor::pointToPixel(($Chart->ChartArea->Height - $topPart) / 2 + $topPart);
	}

	public function getPieRadius(&$Chart, $refcontext = null, $path = null)
	{
		$topPart = ($Chart->HasTitle) ? $Chart->PlotArea->InsideTop : 0;
		$inPoints = (($Chart->ChartArea->Height - $topPart) > $Chart->ChartArea->Width) ? $Chart->ChartArea->Width / 2 : ($Chart->ChartArea->Height - $topPart) / 2;
//		$inPoints = ($Chart->PlotArea->InsideHeight > $Chart->PlotArea->InsideWidth) ? $Chart->PlotArea->InsideWidth / 2 : $Chart->PlotArea->InsideHeight / 2;

		// reduce radius to add small space between edges and pie
		$inPoints -= XLSConfig::$configConst['pieChartsRadiusReduction'];

		// calculate where labels will be
		$totalVal = array_sum($Chart->SeriesCollection(1)->Values);
		$curAngle = pi()/2;
		$xSize = 33; $ySize = 23;
		$maxW = $Chart->ChartArea->Width / 2 - XLSConfig::$configConst['pieChartsRadiusReduction']; $maxH = ($Chart->ChartArea->Height - $topPart) / 2 - XLSConfig::$configConst['pieChartsRadiusReduction'];

		for ($i=0; $i<count($Chart->SeriesCollection(1)->Values); $i++)
		{
			$val = $Chart->SeriesCollection(1)->Values[$i];
			$piePart = ($val / $totalVal) * pi();
			$alpha = $curAngle - $piePart;

			if (in_array($Chart->ChartType, array(XlChartType::xlPieExploded, XlChartType::xlDoughnutExploded, XlChartType::xl3DPieExploded)))
				$explosionDist = $Chart->SeriesCollection(1)->Points($i+1)->Explosion;
			else
				$explosionDist = 0;

			// calc total size of X/Y need for chart
			$xt = abs(cos($alpha)) * ($inPoints + $explosionDist) + $xSize;
			if (!in_array($Chart->ChartType, array(XlChartType::xl3DPie, XlChartType::xl3DPieExploded)))
				$yt = abs(sin($alpha)) * ($inPoints + $explosionDist) + $ySize;
			else
				$yt = abs(sin($alpha)) * (($inPoints + $explosionDist) * sin(pi()/3.9)) + $ySize; // number 3.9 comes from angle of PI/4 with some modifications (that's angle in 3D)

			if ($xt > $maxW)
				$inPoints = ($maxW - $xSize) / abs(cos($alpha)) - $explosionDist;

			if ($yt > $maxH)
				if (!in_array($Chart->ChartType, array(XlChartType::xl3DPie, XlChartType::xl3DPieExploded)))
					$inPoints = ($maxH - $ySize) / abs(sin($alpha)) - $explosionDist;
				else
					$inPoints = (($maxH - $ySize) / abs(sin($alpha))) / sin(pi()/3.9) - $explosionDist;

			$curAngle -= 2*$piePart;
		}

		return UnitConvertor::pointToPixel($inPoints);
	}

	public function getPieInnerRadius(&$Chart, $refcontext = null, $path = null)
	{
		$chartType = intval($Chart->ChartType + 0);

		switch ($chartType)
		{
			case XlChartType::xlDoughnut:
			case XlChartType::xlDoughnutExploded:

				if (isset($Chart->ChartGroups(1)->DoughnutHoleSize))
					return UnitConvertor::pointToPixel($this->getPieRadius($Chart, $refcontext, $path) * UnitConvertor::percentToNumber($Chart->ChartGroups(1)->DoughnutHoleSize));
				else
					// if empty -> return 45%
					return UnitConvertor::pointToPixel($this->getPieRadius($Chart, $refcontext, $path) * 0.45);

			default:
				return 0;
		}
	}

	public function getRadarCenterX(&$Chart, $refcontext = null, $path = null)
	{
		return UnitConvertor::pointToPixel($Chart->PlotArea->InsideWidth / 2 + $Chart->PlotArea->InsideLeft);
	}

	public function getRadarCenterY(&$Chart, $refcontext = null, $path = null)
	{
		return UnitConvertor::pointToPixel($Chart->PlotArea->InsideHeight / 2 + $Chart->PlotArea->InsideTop) - 20;
	}

	public function getRadarRadius(&$Chart, $refcontext = null, $path = null)
	{
		$inPoints = ($Chart->PlotArea->InsideHeight > $Chart->PlotArea->InsideWidth) ? $Chart->PlotArea->InsideWidth / 2 : $Chart->PlotArea->InsideHeight / 2;
		$inPoints -= 25; // space for labels
		return UnitConvertor::pointToPixel($inPoints);
	}

	public static function fontAngleExcelToCD($fontAngle)
	{
		switch ($fontAngle)
		{
			case XlOrientation::xlDownward:
				return 270;
			case XlOrientation::xlUpward:
				return 90;
			case Constants::xlAutomatic:
				return 0;
			default:
				return (-$fontAngle);
		}
	}

	public function getBorderColor(&$Chart, $refcontext = null, $path = null)
	{
		if (is_null($path))
			return 'Transparent';
		else
		{
			eval('$borderObj = $' . str_replace('.', '->', $path) . ';');
			if ((intval($borderObj->ColorIndex + 0) == XlColorIndex::xlColorIndexAutomatic ) || (intval($borderObj->ColorIndex + 0) == XlColorIndex::xlColorIndexNone))
			{
				if ($path == 'Chart.Axes(2).MajorGridlines.Border')
					return $borderObj->Color;
				else
					return 'Transparent';
			}
			else
				return $borderObj->Color;
		}
	}

	public static function getOdometerCenterX(&$Chart, $refcontext = null, $path = null)
	{
		return UnitConvertor::pointToPixel($Chart->ChartArea->Width / 2);
	}

	public static function getOdometerCenterY(&$Chart, $refcontext = null, $path = null)
	{
		if (in_array($Chart->ChartType, array(XlChartType::xlMeterOdoFull100, XlChartType::xlMeterOdoFull)))
			return UnitConvertor::pointToPixel(($Chart->ChartArea->Height + $Chart->PlotArea->InsideTop) / 2);
		else if (in_array($Chart->ChartType, array(XlChartType::xlMeterOdoHalf, XlChartType::xlMeterOdoHalf100)))
			return UnitConvertor::pointToPixel($Chart->ChartArea->Height - 15);
		else
			return UnitConvertor::pointToPixel($Chart->PlotArea->InsideTop) + FormatConvertor::getOdometerRadius($Chart);
	}

	public static function getOdometerRadius(&$Chart, $refcontext = null, $path = null)
	{
		if (in_array($Chart->ChartType, array(XlChartType::xlMeterOdoFull100, XlChartType::xlMeterOdoFull)))
			$inPoints = (($Chart->ChartArea->Height - $Chart->PlotArea->InsideTop) < $Chart->ChartArea->Width) ?
						($Chart->ChartArea->Height - $Chart->PlotArea->InsideTop) / 2 - 10 :
						$Chart->ChartArea->Width / 2 - 10;
		else if (in_array($Chart->ChartType, array(XlChartType::xlMeterOdoHalf, XlChartType::xlMeterOdoHalf100)))
			$inPoints = (($Chart->ChartArea->Height - $Chart->PlotArea->InsideTop) < ($Chart->ChartArea->Width/2)) ?
						($Chart->ChartArea->Height - $Chart->PlotArea->InsideTop) - 10 :
						$Chart->ChartArea->Width / 2 - 10;
		else
		{
			$alfa = 1.1345; // in rads

			$wSpace = $Chart->ChartArea->Width / (2*cos($alfa)) - 10;
			$hSpace = ($Chart->ChartArea->Height - $Chart->PlotArea->InsideTop - 25) / (1 - sin($alfa)) - 10;

			$inPoints = ($wSpace < $hSpace) ? $wSpace : $hSpace;
		}

		// abs is used because of problems with negative radius values
		return UnitConvertor::pointToPixel(abs($inPoints));
	}

	public static function getOdometerStart(&$Chart, $refcontext = null, $path = null)
	{
		if (isset($Chart->Axes) && $Chart->Axes(1)->MinimumScaleIsAuto === false && !empty($Chart->Axes(1)->MinimumScale))
			return $Chart->Axes(1)->MinimumScale;
		else
		{
			if (!in_array($Chart->ChartType, array(XlChartType::xlMeterOdoFull100, XlChartType::xlMeterOdoHalf100)))
			{
				$numOfSeries = $Chart->SeriesCollection->Count();
				if ($numOfSeries > 1)
				{
					if (count($Chart->SeriesCollection(2)->Values) > 0)
					{
						$minVal = $Chart->SeriesCollection(2)->Values[0];
						for ($i = 2; $i < ($numOfSeries + 1); $i++)
						{
							$seriesLen = count($Chart->SeriesCollection($i)->Values);
							for ($j = 0; $j < $seriesLen; $j++)
								if (strlen(strval($Chart->SeriesCollection($i)->Values[$j])) > 0)
									$minVal = min($minVal, $Chart->SeriesCollection($i)->Values[$j]);
						}

						return $minVal;
					}
				}
				else if (count($Chart->SeriesCollection(1)->Values) > 0)
				{
					$sign = $Chart->SeriesCollection(1)->Values[0] / abs($Chart->SeriesCollection(1)->Values[0]);
					$startExp = floor(log10(abs($Chart->SeriesCollection(1)->Values[0])));
					for ($i = 1; $i < count($Chart->SeriesCollection(1)->Values); $i++)
					{
						$absVal = abs($Chart->SeriesCollection(1)->Values[$i]);
						$sign = min($Chart->SeriesCollection(1)->Values[$i] / $absVal, $sign);
						$startExp = max(floor(log10($absVal)), $startExp);
					}

					return (($sign == -1) ? $sign * pow(10, $startExp+1) : 0);
				}
			}
		}

		return 0;
	}

	public static function getOdometerEnd(&$Chart, $refcontext = null, $path = null)
	{
		if (isset($Chart->Axes) && $Chart->Axes(1)->MaximumScaleIsAuto === false && !empty($Chart->Axes(1)->MaximumScale))
			return $Chart->Axes(1)->MaximumScale;
		else
		{
			if (!in_array($Chart->ChartType, array(XlChartType::xlMeterOdoFull100, XlChartType::xlMeterOdoHalf100)))
			{
				$numOfSeries = $Chart->SeriesCollection->Count();
				if ($numOfSeries > 1)
				{
					if (count($Chart->SeriesCollection(2)->Values) > 0)
					{
						$maxVal = $Chart->SeriesCollection(2)->Values[0];
						for ($i = 2; $i < ($numOfSeries + 1); $i++)
						{
							$seriesLen = count($Chart->SeriesCollection($i)->Values);
							for ($j = 0; $j < $seriesLen; $j++)
								if (strlen(strval($Chart->SeriesCollection($i)->Values[$j])) > 0)
									$maxVal = max($maxVal, $Chart->SeriesCollection($i)->Values[$j]);
						}

						return $maxVal;
					}
				}
				else if (count($Chart->SeriesCollection(1)->Values) > 0)
				{
					$sign = $Chart->SeriesCollection(1)->Values[0] / abs($Chart->SeriesCollection(1)->Values[0]);
					$startExp = floor(log10(abs($Chart->SeriesCollection(1)->Values[0])));
					for ($i = 1; $i < count($Chart->SeriesCollection(1)->Values); $i++)
					{
						$absVal = abs($Chart->SeriesCollection(1)->Values[$i]);
						$sign = max($Chart->SeriesCollection(1)->Values[$i] / $absVal, $sign);
						$startExp = max(floor(log10($absVal)), $startExp);
					}

					return (($sign == -1) ? 0 : $sign * pow(10, $startExp+1));
				}
			}
		}

		return 100;
	}

	public static function getOdometerMajorTick(&$Chart, $refcontext = null, $path = null)
	{
		if (isset($Chart->Axes) && $Chart->Axes(1)->MajorUnitIsAuto === false && !empty($Chart->Axes(1)->MajorUnit))
			return $Chart->Axes(1)->MajorUnit;

		return ((in_array($Chart->ChartType, array(XlChartType::xlMeterOdoHalf, XlChartType::xlMeterOdoHalf100, XlChartType::xlMeterAngularWide)))
					? round((FormatConvertor::getOdometerEnd($Chart) - FormatConvertor::getOdometerStart($Chart)) / 5)
					: round((FormatConvertor::getOdometerEnd($Chart) - FormatConvertor::getOdometerStart($Chart)) / 10));
	}

	public static function getOdometerMinorTick(&$Chart, $refcontext = null, $path = null)
	{
		return ((isset($Chart->Axes) && $Chart->Axes(1)->MinorUnitIsAuto === false && !empty($Chart->Axes(1)->MinorUnit))
				? $Chart->Axes(1)->MinorUnit
				: round((FormatConvertor::getOdometerEnd($Chart) - FormatConvertor::getOdometerStart($Chart)) / 50));
	}

	public static function getOdometerMicroTick(&$Chart, $refcontext = null, $path = null)
	{
		// Micro ticks are hidden
		return 0;

//		return ($Chart->Axes(1)->MinorUnitIsAuto === false)
//				? round($Chart->Axes(1)->MinorUnit / 2)
//				: round((FormatConvertor::getOdometerEnd($Chart) - FormatConvertor::getOdometerStart($Chart)) / 100);
	}

	public static function getOdometerStartAngle(&$Chart, $refcontext = null, $path = null)
	{
		if (in_array($Chart->ChartType, array(XlChartType::xlMeterOdoFull100, XlChartType::xlMeterOdoFull)))
			return -135;
		else if (in_array($Chart->ChartType, array(XlChartType::xlMeterOdoHalf, XlChartType::xlMeterOdoHalf100)))
			return -90;
		else
			return -25;
	}

	public static function getOdometerEndAngle(&$Chart, $refcontext = null, $path = null)
	{
		return abs(FormatConvertor::getOdometerStartAngle($Chart));
	}

	public static function getLinearMeterTopY(&$Chart, $refcontext = null, $path = null)
	{
		return UnitConvertor::pointToPixel($Chart->PlotArea->InsideTop) + 10;
	}
}

?>