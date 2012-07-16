<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>电子报表</title>
	<link rel="stylesheet" type="text/css" media="screen" href="ui/app/res/css/main.css?1335790887" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/app/res/css/workspace.css?1335790887" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/app/res/css/workbook.css?1335790887" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/app/res/css/bar.css?1335790886" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/common/res/css/main.css?1335790888" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/lib/ext/resources/css/ext-all.css?1335790889" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/lib/ext/resources/css/miscfield.css?1335790889" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/lib/ext/resources/css/spinner.css?1335790889" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/app/res/css/ext_wa.css?1335790887" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/app/res/css/bars.css?1335790886" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/app/res/css/dialogs.css?1335790887" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/lib/ext/resources/css/color-picker.ux.css?1335790889" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/lib/ext/resources/css/RowEditor.css?1335790889" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/lib/ext/extensions/tristateCB/checkbox.css?1335790889" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/common/res/css/palo.css?1335790888" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/app/res/css/menubar.css?1335790887" />
	<link rel="stylesheet" type="text/css" media="screen" href="ui/app/res/css/toolbar.css?1335790887" />
 	<link rel="stylesheet" type="text/css" media="screen" id="theme" href="ui/lib/ext/resources/css/xtheme-default.css" />
</head>

<body id="mainBody" style="overflow: hidden;" onunload="document.getElementById('mainBody').style.display = 'none';" onload="Jedox.wss.general.startUp();" onContextMenu="if (!Jedox.wss.app.showBrowserCtx) return false;" scroll="no">

	<!-- Menubar, Toolbars and Formula Bar Container -->
	<div id="barsContainer">

		
		<!-- Menubar -->
		<div id="MenuPlaceholder" style="height: 25px;"></div>

		<!-- Toolbars -->
		<div id="Toolbar" class="wsstoolbar" style="height: 26px; overflow: hidden;">
			<!-- Standard Toolbar -->
			<div id="wssStandardBar" class="bar" style="position: absolute;">
				<div id="wssStandardToolbar" class="bar-end"></div>
			</div>
			<!-- Format Toolbar -->
			<div id="wssFormatBar" class="bar" style="position: absolute; left: 143px;"><!-- With undo/redo left=200px -->
				<div id="wssFormatToolbar" class="bar-end"></div>
			</div>
			<!-- Dynarange Toolbar -->
			<div id="wssDynarangeBar" class="bar" style="position: absolute; left: 683px;"><!-- With undo/redo left=740px -->
				<div id="wssDynarangeToolbar" class="bar-end"></div>
			</div>
		</div>

		
		<!-- Formula Bar -->
		<div id="formulaBar" class="default-format-window">
			<table id="formulaTable" class="formula-table">
				<tbody>
					<tr>
						<td id="addressInfo">
							<table id="formulaInner" class="formula-inner">
								<tbody>
									<tr>
										<td class="currCell"><input id="currCoord" name="currCoord" readonly="readonly" type="text" /></td>
										<td><input type="button" id="formulaSign" value="&fnof;&times;" /></td>
									</tr>
								</tbody>
							</table>
						</td>
						<td id="formulaInfo"><input style="width: 100%; max-width: 100%;" id="currFormula" name="currFormula" type="text" onblur="Jedox.wss.app.fromFormulaField = false;" onmousedown="Jedox.wss.general.mouseDownOnFormFld(event);" onfocus="Jedox.wss.general.focusOnFormFld(event)" onkeyup="Jedox.wss.keyboard.setFieldContent(event);" /></td>
					</tr>
				</tbody>
			</table>
		</div>

	</div> <!-- end of barsContainer -->

	<!--  Workspace -->
	<div id="workspace" class="workspace">
	</div>

	<!-- Status Bar -->
	<div id="statusBarContainer" style="width: 100%;"></div>

	<div id="CursorMarker"></div>
	<div id="marker" onmouseup="stopTracking();"></div>

	<div id="hideRows" style="background-color:white;z-Index:100;position:absolute;display:none;"></div>
	<div id="hideCols" style="background-color:white;z-Index:100;position:absolute;display:none;"></div>

	<div id="HTML_AJAX_LOADING" class="HTML_AJAX_Loading" style="font-family: Arial, Helvetica, sans-serif; font-size: 8.25pt; position: absolute; top: 50px; right: 50px; text-align: right; width: 100px; padding: 4px; display: none; z-index: 200; background: url(ui/app/res/img/snake_transparent.gif) no-repeat center left;">Please wait ...</div>

	<!-- 
	<script type="text/javascript" src="cc/ha.php?stub=WSS,Palo,MicroChartStreamer,Studio&client=Util,Main,Request,HttpClient,Dispatcher,Behavior,Loading,JSON,iframe,orderedQueue&wam=DESIGNER"></script>
	 -->
	<script type="text/javascript" src="ui/main.js"></script> 
	<script type="text/javascript" src="ui/lib/ext/adapter/ext/ext-base.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/ext-all-debug.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/miscfield.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/StatusBar.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/spinner/Spinner.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/spinner/SpinnerStrategy.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/color-picker/color-picker.ux.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/IconCombo.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/FileUploadField.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/clone.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/data-view-plugins.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/MyTreeLoader/TreeNodeProvider.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/MyTreeLoader/MyTreeLoader.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/RowEditor.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/ButtonField.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/tristateCB/Ext.ux.form.TriCheckbox.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/ext_wa.js?1335790889"></script>
	<script type="text/javascript" src="ui/lib/util/firebugx.js?1335790892"></script>
	<script type="text/javascript" src="ui/lib/yui/build/yahoo-dom-event/yahoo-dom-event.js?1335790892"></script>
	<script type="text/javascript" src="ui/lib/yui/build/dragdrop/dragdrop-min.js?1335790892"></script>
	<script type="text/javascript" src="ui/lib/yui/build/slider/slider-min.js?1335790892"></script>
	<script type="text/javascript" src="ui/lib/jsgraphics/wz_jsgraphics.js?1335790892"></script>
	<script type="text/javascript" src="ui/app/base/Jedox.js?1335966651"></script>
	<script type="text/javascript" src="ui/lib/util/misc.js?1335790892"></script>
	<script type="text/javascript" src="ui/lib/util/Interface.js?1335790892"></script>
	<script type="text/javascript" src="ui/app/base/gen/Observer.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/gen/SparseVector.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/gen/Point.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/i18n/i18n.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/error.js?1335790886"></script>
	<script type="text/javascript" src="ui/common/base/backend/conn.js?1335968394"></script>
	<script type="text/javascript" src="ui/app/base/backend/ha.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/cls/MagicDot.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/cls/Environment.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/cls/SharedEnvironment.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/cls/SheetSelector.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/cls/AutoScroll.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/cls/CursorField.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/workspace.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/range.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/PaneStorage.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/PaneCache.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/Pane.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/SpreadSheet.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/Book.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/Selection.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/DefaultSelection.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/FormulaSelectionCls.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/CopySelection.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/DynarangeSelection.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/DynarangeSubSelection.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/FillSelection.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/Range.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/DefaultRange.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/FormulaRange.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/CopyRangeCls.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/DynarangeRange.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/DynarangeSubRange.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/grid/FillRangeCls.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/keyboard.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/mouse.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/general.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/style.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/sheet.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/book.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/cndfmt.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/action.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/dynarange.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/hyperlink.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/wsel/wsel.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/wsel/Base.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/wsel/FormBase.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/wsel/ComboBox.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/wsel/CheckBox.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/wsel/Button.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/events.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/formula.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/macro.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/dialog/chart/createChart.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/dialog/Browse.js?1335790886"></script>
	<script type="text/javascript" src="ui/common/base/dialog/ConstListEditor.js?1335790888"></script>
	<script type="text/javascript" src="ui/app/base/dialog/insertPicture.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/app/globals.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/app/dynJSRegistry.js?1335790886"></script>
	<script type="text/javascript" src="ui/common/base/app/dynload.js?1335790888"></script>
	<script type="text/javascript" src="ui/app/base/app/StatusBar.js?1335790886"></script>
	<script type="text/javascript" src="ui/common/base/app/fileTypesRegistry.js?1335790888"></script>
	<script type="text/javascript" src="ui/common/base/cm/codemirror.js?1335790888"></script>
	<script type="text/javascript" src="ui/lib/ext/extensions/imagebutton.js?1335790889"></script>
	<script type="text/javascript" src="ui/common/base/palo/utils.js?1335790888"></script>
	<script type="text/javascript" src="ui/app/base/app/handlers.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/app/MenuBar.js?1335790886"></script>
	<script type="text/javascript" src="ui/app/base/app/Toolbars.js?1335790886"></script>
	<script type="text/javascript" src="ui/common/base/i18n/loc_zh_CN.js?1335966553"></script>
	<script type="text/javascript" src="ui/app/base/i18n/loc_zh_CN.js"></script>
	<script type="text/javascript">

		Ext.BLANK_IMAGE_URL = 'ui/lib/ext/resources/images/default/s.gif';

		HTML_AJAX.defaultServerUrl = 'http://localhost:8090/cc/ha.php';

		Jedox.wss.app.uiVer = [ '3.1.1-dev', '2012-05-03 09:09:37 +0000' ];

		Jedox.wss.app.params = [];

		Jedox.wss.app.defaultFiles = '';

		Jedox.wss.app.appModeS = Jedox.wss.app.appMode = Jedox.wss.grid.viewMode.DESIGNER;
		Jedox.wss.app.toolbarLayout = 'toolbar';
		Jedox.wss.app.fopper = false;

		Jedox.wss.backend.conn.xhrHdrs['X-WSS-AM'] = HTML_AJAX_Request.prototype.customHeaders['X-WSS-AM'] = Jedox.wss.app.appModeS;

		if (window == window.parent)
			window.onbeforeunload = function () { return ''; };

	</script>

</body>

</html>
