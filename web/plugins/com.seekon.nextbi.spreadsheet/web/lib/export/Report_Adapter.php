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
 * Drazen Kljajic <drazen.kljajic@develabs.com>
 * Mladen Todorovic <mladen.todorovic@develabs.com>
 *
 * \version
 * SVN: $Id: Report_Adapter.php 2776 2010-02-16 21:31:26Z predragm $
 *
 */

abstract class Report_Adapter {
	const XSLT_PATH = '../../lib/export/xslt';
	const TYPE_NORMAL = 'worksheet';
	const TYPE_ERR = 'mesg';
	const FORMAT_HTML = 'html';
	const FORMAT_PDF = 'fo';
	const CHART_URL = '/spreadsheet/root/cc/gen_element.php?';
	const IMGS_URL = '/spreadsheet/root/ui/common/res/img/export/';
	const HOME_URL = '/spreadsheet/root/ui/app/';
	const TOP_LEFT_DIFF = 7; // used for adjusting top and left cordinate for Charts
	const TOP_DIFF_PDF = 2; // used for adjusting top conrdinate for Charts
	const PX2PT = 0.75;
	const INCH2PT = 72;
	const PDF_A4_PAGE_PX_WIDTH = 793; // pixels of PDF A4 PAGE
	const PDF_A4_PAGE_PX_HEIGHT = 1122; // pixels of PDF A4 PAGE
	const PDF_LETTER_PAGE_PX_WIDTH = 816; // pixels of PDF LETTER PAGE
	const PDF_LETTER_PAGE_PX_HEIGHT = 1054; // pixels of PDF LETTER PAGE
	const MAX_FOOTER_LINE_HEIGHT = 0.5; // in inches (it need to be height enough to take maximum posible font size)
	const GRIDLINE_SIZE = 0.3; // size is in points

	// Header/Footer Formats
	const HF_DATE = 'd M Y';
	const HF_TIME = 'G:i';

	protected $wss_ajax;
	protected $data_xml;
	protected $type = self::TYPE_NORMAL;
	protected $show_header_footer; // 1st bit is to show footer, 2nd bit is to show header
	protected $home_url;
	protected $imgs_url;
	protected $chart_url;
	protected $default_styles;
	protected $default_margins;
	protected $gridline_style;
	protected $error_values;

	abstract protected function _create_data_xml($format);

	public function __construct() {
		// *** init
		$tmpHost = ((isset($_SERVER['HTTPS'])) ? 'https://' : 'http://') .
						((!empty($_SERVER['HTTP_HOST'])) ? $_SERVER['HTTP_HOST'] : ($_SERVER['SERVER_NAME'] . ':' . $_SERVER['SERVER_PORT']));
		$this->chart_url = $tmpHost . self::CHART_URL;
		$this->home_url = $tmpHost . self::HOME_URL;
		$this->imgs_url = $tmpHost . self::IMGS_URL;

		$this->show_header_footer = 0x03;
		$this->default_styles = array('font-family' => 'Arial,Helvetica,sans-serif', 'font-size' => '10pt', 'white-space' => 'nowrap');
		$this->default_margins = array('top' => 0.75, 'bottom' => 0.75, 'left' => 0.7, 'right' => 0.7, 'header' => 0.3, 'footer' => 0.3);
		$this->gridline_style = array('border-top' => '0.3pt dotted #000000',
									'border-bottom' => '0.3pt dotted #000000',
									'border-left' => '0.3pt dotted #000000',
									'border-right' => '0.3pt dotted #000000'
		);
		$this->error_values = array('#NULL!', '#DIV/0!', '#VALUE!', '#REF!', '#NAME?', '#NUM!', '#N/A', '#INSUFFICIENT_RIGHTS', '#EXTENSION_ERROR', '#MAX_ITERATIONS_EXCEEDED');
		// ***

		$this->wss_ajax = $_SESSION[WSS::SESSION_STORE_NAME];
	}

	public function get_fname ()
	{
		$fname = $this->wss_ajax->getWbName();

		if (substr($fname, -4) == '.wss')
			$fname = substr($fname, 0, -4);

		if (preg_match('/^n[0-9]+-/', $fname))
			$fname = substr($fname, strpos($fname, '-') + 1);

		return $fname;
	}

	// XLSX
	public function gen_xlsx() {
		list($usec, $sec) = explode(' ', microtime(), 2);
		mt_srand((float) $sec + ((float) $usec * 100000));

		$tmp_file = tempnam('dummy', '');
		$tmp_path = str_replace('\\', '/', dirname($tmp_file));
		unlink($tmp_file);

		$fname = $this->get_fname();
		$file = $tmp_path . '/' . $fname . '_' . mt_rand() . '.xlsx';
		ccmd('[["lexp","xlsx","' . $file . '"]]');

		if (!file_exists($file))
			return;

		header('Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
		header('Content-Length: ' . filesize($file));
		header('Content-Disposition: attachment; filename=' . urlencode($fname) . '.xlsx');
		header('Last-Modified: ' . gmdate('D, d M Y H:i:s \G\M\T'));
		header('Expires: Fri, 03 Sep 1999 01:00:00 GMT');
		header('Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0');
		header('Pragma: no-cache');

		ob_clean();
		flush();
		readfile($file);
		unlink($file);
	}

	// PDF
	public function gen_pdf() {
		$this->_create_data_xml(self::FORMAT_PDF);

		session_write_close();

		// Debug:
//		die($this->data_xml->saveXML());
//		die($this->make_xsl_tranformation(self::FORMAT_PDF));

		$res = ccmd(array('base64Encoded' => true, 'doc' => base64_encode($this->make_xsl_tranformation(self::FORMAT_PDF))), -1, null, CFG_FOPPER_PATH);

		$res = $res['base64Encoded'] ? base64_decode($res['doc']) : $res['doc'];

		header('Content-Type: application/pdf');
		header('Content-Length: ' . strlen($res));
		header('Content-Disposition: inline; filename=' . urlencode($this->get_fname()) . '.pdf');
		header('Last-Modified: ' . gmdate('D, d M Y H:i:s \G\M\T'));
		header('Expires: Fri, 03 Sep 1999 01:00:00 GMT');
		header('Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0');
		header('Pragma: no-cache');

		print $res;
	}

	// HTML
	public function gen_html() {
		$this->_create_data_xml(self::FORMAT_HTML);

		$res = $this->make_xsl_tranformation(self::FORMAT_HTML);

		header('Content-Type: text/html; charset=utf-8; charset=UTF-8');
		header('Content-Length: ' . strlen($res));
		header('Content-Disposition: inline; filename=' . urlencode($this->get_fname()) . '.html');
		header('Last-Modified: ' . gmdate('D, d M Y H:i:s \G\M\T'));
		header('Expires: Fri, 03 Sep 1999 01:00:00 GMT');
		header('Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0');
		header('Pragma: no-cache');

		print $res;
	}

	protected function make_xsl_tranformation($format) {
		$xsl = new DomDocument;
		$xsl->load(self::XSLT_PATH . '/' . $this->type .  '_' . $format . '.xslt');

		/* Configure the transformer */
		$xslt = new XsltProcessor();
		$xslt->importStylesheet($xsl);

		return $xslt->transformToXML($this->data_xml);
	}
}

?>