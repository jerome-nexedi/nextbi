Jedox.wss.dialog.chart.editChartTypeDialog = function(mode, chartID) {
	var _charProps;
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var chartTypesSizeDefs = Jedox.wss.wsel.chart.type_sizeDefs, chartMinSizeFactor = Jedox.wss.wsel.chart.min_sizeFactor, chartsMinWidthHeightRatio = Jedox.wss.wsel.chart.min_whRatio, widthHeightRatio = Jedox.wss.wsel.chart.whRatio;
	var currentChartCategory = "nocat";
	var fireSelectType = true;
	var _doFirstSelection = true;
	var chartCategoriesData = [ [ "all", "All".localize(), "ico_cat_all" ],
			[ "column", "Column".localize(), "ico_cat_column" ],
			[ "line", "Line".localize(), "ico_cat_line" ],
			[ "pie", "Pie".localize(), "ico_cat_pie" ],
			[ "bar", "Bar".localize(), "ico_cat_bar" ],
			[ "area", "Area".localize(), "ico_cat_area" ],
			[ "xyscatter", "X Y (Scatter)".localize(), "ico_cat_xyscatter" ],
			[ "stock", "Stock".localize(), "ico_cat_stock" ],
			[ "doughnut", "Doughnut".localize(), "ico_cat_doughnut" ],
			[ "bubble", "Bubble".localize(), "ico_cat_bubble" ],
			[ "radar", "Radar".localize(), "ico_cat_radar" ],
			[ "meter", "Meter".localize(), "ico_cat_meter" ] ];
	var chartCategoriesStore = new Ext.data.SimpleStore( {
		fields : [ "catname", "catlabel", "icon" ],
		data : chartCategoriesData
	});
	var tplChartCategories = new Ext.XTemplate(
			'<div class="charttypes" style="width: 92%;">',
			"Categories".localize(),
			"</div>",
			'<tpl for=".">',
			'<div class="thumb-wrap" id="{catlabel}">',
			'<div class="thumb"><img class="{icon}" width="16" height="16" src="../lib/ext/resources/images/default/s.gif" >&nbsp;&nbsp;&nbsp;{catlabel}</div></div>',
			"</tpl>");
	var chartTypesData = [
			[ "Column".localize(), "xlColumnClustered",
					"Clustered Column".localize(), "defHor", 2, 3 ],
			[ "Column".localize(), "xlColumnStacked",
					"Stacked Column".localize(), "defHor", 2, 3 ],
			[ "Column".localize(), "xlColumnStacked100",
					"100% Stacked Column".localize(), "defHor", 2, 3 ],
			[ "Column".localize(), "xl3DColumnClustered",
					"3-D Clustered Column".localize(), "defHor", 2, 3 ],
			[ "Column".localize(), "xl3DColumnStacked",
					"Stacked Column in 3-D".localize(), "defHor", 2, 3 ],
			[ "Column".localize(), "xl3DColumnStacked100",
					"100% Stacked Column in 3-D".localize(), "defHor", 2, 3 ],
			[ "Column".localize(), "xlCylinderColClustered",
					"Clustered Cylinder".localize(), "defHor", 2, 3 ],
			[ "Column".localize(), "xlCylinderColStacked",
					"Stacked Cylinder".localize(), "defHor", 2, 3 ],
			[ "Column".localize(), "xlCylinderColStacked100",
					"100% Stacked Cylinder".localize(), "defHor", 2, 3 ],
			[ "Line".localize(), "xlLine", "Line".localize(), "defHor", 2, 3 ],
			[ "Line".localize(), "xlLineStacked", "Stacked Line".localize(),
					"defHor", 2, 3 ],
			[ "Line".localize(), "xlLineStacked100",
					"100% Stacked Line".localize(), "defHor", 2, 3 ],
			[ "Line".localize(), "xlLineRotated", "Rotated Line".localize(),
					"defVer", 1, 3 ],
			[ "Line".localize(), "xlLineMarkers",
					"Line with Markers".localize(), "defHor", 2, 3 ],
			[ "Line".localize(), "xlLineMarkersStacked",
					"Stacked Line with Markers".localize(), "defHor", 2, 3 ],
			[ "Line".localize(), "xlLineMarkersStacked100",
					"100% Stacked Line with Markers".localize(), "defHor", 2, 3 ],
			[ "Line".localize(), "xlLineMarkersRotated",
					"Rotated Line with Markers".localize(), "defVer", 1, 3 ],
			[ "Line".localize(), "xl3DLine", "3-D Line".localize(), "defHor",
					2, 3 ],
			[ "Pie".localize(), "xlPie", "Pie".localize(), "defHor", 0, 0 ],
			[ "Pie".localize(), "xl3DPie", "Pie in 3-D".localize(), "defHor",
					0, 0 ],
			[ "Pie".localize(), "xlPieExploded", "Exploded Pie".localize(),
					"defHor", 0, 0 ],
			[ "Pie".localize(), "xl3DPieExploded",
					"Exploded Pie in 3-D".localize(), "defHor", 0, 0 ],
			[ "Bar".localize(), "xlBarClustered", "Clustered Bar".localize(),
					"defHor", 2, 3 ],
			[ "Bar".localize(), "xlBarStacked", "Stacked Bar".localize(),
					"defHor", 2, 3 ],
			[ "Bar".localize(), "xlBarStacked100",
					"100% Stacked Bar".localize(), "defHor", 2, 3 ],
			[ "Bar".localize(), "xl3DBarClustered",
					"Clustered Bar in 3-D".localize(), "defHor", 2, 3 ],
			[ "Bar".localize(), "xl3DBarStacked",
					"Stacked Bar in 3-D".localize(), "defHor", 2, 3 ],
			[ "Bar".localize(), "xl3DBarStacked100",
					"100% Stacked Bar in 3-D".localize(), "defHor", 2, 3 ],
			[ "Bar".localize(), "xlCylinderBarClustered",
					"Clustered Horizontal Cylinder".localize(), "defHor", 2, 3 ],
			[ "Bar".localize(), "xlCylinderBarStacked",
					"Stacked Horizontal Cylinder".localize(), "defHor", 2, 3 ],
			[ "Bar".localize(), "xlCylinderBarStacked100",
					"100% Stacked Horizontal Cylinder".localize(), "defHor", 2,
					3 ],
			[ "Area".localize(), "xlArea", "Area".localize(), "defHor", 2, 3 ],
			[ "Area".localize(), "xlAreaStacked", "Stacked Area".localize(),
					"defHor", 2, 3 ],
			[ "Area".localize(), "xlAreaStacked100",
					"100% Stacked Area".localize(), "defHor", 2, 3 ],
			[ "Area".localize(), "xl3DArea", "3-D Area".localize(), "defHor",
					2, 3 ],
			[ "Area".localize(), "xl3DAreaStacked",
					"Stacked Area in 3-D".localize(), "defHor", 2, 3 ],
			[ "Area".localize(), "xl3DAreaStacked100",
					"100% Stacked Area in 3-D".localize(), "defHor", 2, 3 ],
			[ "X Y (Scatter)".localize(), "xlXYScatter",
					"Scatter with only Markers".localize(), "defHor", 3, 3 ],
			[ "X Y (Scatter)".localize(), "xlXYScatterSmooth",
					"Scatter with Smooth Lines and Markers".localize(),
					"defHor", 3, 3 ],
			[ "X Y (Scatter)".localize(), "xlXYScatterSmoothNoMarkers",
					"Scatter with Smooth Lines".localize(), "defHor", 3, 3 ],
			[ "X Y (Scatter)".localize(), "xlXYScatterLines",
					"Scatter with Straight Lines and Markers".localize(),
					"defHor", 3, 3 ],
			[ "X Y (Scatter)".localize(), "xlXYScatterLinesNoMarkers",
					"Scatter with Straight Lines".localize(), "defHor", 3, 3 ],
			[ "Stock".localize(), "xlStockHLC", "High-Low-Close".localize(),
					"defHor", 3, 3 ],
			[ "Stock".localize(), "xlStockOHLC",
					"Open-High-Low-Close".localize(), "defHor", 3, 3 ],
			[ "Doughnut".localize(), "xlDoughnut", "Doughnut".localize(),
					"defHor", 0, 0 ],
			[ "Doughnut".localize(), "xlDoughnutExploded",
					"Exploded Doughnut".localize(), "defHor", 0, 0 ],
			[ "Bubble".localize(), "xlBubble", "Bubble".localize(), "defHor",
					3, 3 ],
			[ "Bubble".localize(), "xlBubble3DEffect",
					"Bubble with a 3-D Effect".localize(), "defHor", 3, 3 ],
			[ "Radar".localize(), "xlRadar", "Radar".localize(), "defHor", 3, 3 ],
			[ "Radar".localize(), "xlRadarMarkers",
					"Radar with Markers".localize(), "defHor", 3, 3 ],
			[ "Radar".localize(), "xlRadarFilled", "Filled Radar".localize(),
					"defHor", 3, 3 ],
			[ "Meter".localize(), "xlMeterOdoFull", "Odometer Full".localize(),
					"equal", 3, 0 ],
			[ "Meter".localize(), "xlMeterOdoFull100",
					"Odometer Full Percentage".localize(), "equal", 3, 0 ],
			[ "Meter".localize(), "xlMeterOdoHalf", "Odometer Half".localize(),
					"odoHalf", 3, 0 ],
			[ "Meter".localize(), "xlMeterOdoHalf100",
					"Odometer Half Percentage".localize(), "odoHalf", 3, 0 ],
			[ "Meter".localize(), "xlMeterAngularWide",
					"Wide Angular Meter".localize(), "angulWide", 3, 0 ],
			[ "Meter".localize(), "xlMeterLineHorizontal",
					"Horizontal Line Meter".localize(), "lineHor", 3, 0 ],
			[ "Meter".localize(), "xlMeterLineVertical",
					"Vertical Line Meter".localize(), "lineVer", 3, 0 ] ];
	var chartTypesStore = new Ext.data.SimpleStore( {
		fields : [ "catname", "charttype", "chartlabel", "WHRatio",
				"hasAxesOptions", "hasAxesLabels" ],
		data : chartTypesData
	});
	var tplChartTypes = new Ext.XTemplate(
			'<tpl for=".">',
			'<tpl if="this.isCurrCat(catname)">',
			'<div class="x-clear"></div>',
			'<div class="charttypes">{catname}</div>',
			"</tpl>",
			'<div class="thumb-wrap" id="{charttype}">',
			'<div class="thumb"><img class="{charttype}" src="../lib/ext/resources/images/default/s.gif" width="46" height="46" title="{chartlabel}"></div></div>',
			"</tpl>", '<div class="x-clear"></div>', {
				isCurrCat : function(cat) {
					var result = (cat != currentChartCategory);
					currentChartCategory = cat;
					return result
				}
			});
	var chartTypeGeneralPanel = new Ext.Panel( {
		id : "chartselectionpanel",
		layout : "column",
		border : false,
		bodyStyle : "background-color: transparent;",
		items : [ new Ext.Panel( {
			id : "chartcategoriespanel",
			width : 110,
			layout : "form",
			height : 351,
			autoScroll : true,
			items : [ new Ext.DataView( {
				id : "chartcategories",
				store : chartCategoriesStore,
				tpl : tplChartCategories,
				autoHeight : true,
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				listeners : {
					beforeselect : {
						fn : onCategorySelect,
						scope : this
					},
					containerclick : {
						fn : onContainerClick,
						scope : this
					}
				}
			}) ]
		}), new Ext.Panel( {
			id : "charttypespanel",
			columnWidth : 1,
			layout : "fit",
			anchor : "100%",
			height : 351,
			autoScroll : true,
			items : [ new Ext.DataView( {
				id : "charttypes",
				store : chartTypesStore,
				tpl : tplChartTypes,
				autoHeight : true,
				autoWidth : true,
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				listeners : {
					beforeselect : {
						fn : onTypeSelect,
						scope : this
					},
					containerclick : {
						fn : onContainerClick,
						scope : this
					}
				}
			}) ]
		}) ]
	});
	function onCategorySelect(dView, node, selections) {
		Jedox.wss.app.currentDialogControl = dView.getId();
		Jedox.wss.app.currentDialogControlItemsCnt = chartCategoriesStore
				.getCount();
		currentChartCategory = "nocat";
		if (node.id == "All".localize()) {
			chartTypesStore.clearFilter()
		} else {
			chartTypesStore.filter("catname", node.id)
		}
		fireSelectType = false;
		Ext.getCmp("charttypes").select(0);
		fireSelectType = true
	}
	function onTypeSelect(dView, node, selections) {
		var selection = dView.getRecord(node);
		if (fireSelectType) {
			Jedox.wss.app.currentDialogControl = dView.getId();
			Jedox.wss.app.currentDialogControlItemsCnt = chartTypesStore
					.getCount()
		}
	}
	function onContainerClick(dView, e) {
		e.stopEvent();
		return false
	}
	function getWHRatio() {
		var selChartType = Ext.getCmp("charttypes").getSelectedRecords()[0];
		var whRatio = widthHeightRatio[selChartType.get("WHRatio")];
		return whRatio
	}
	function getChartTypeGeneralValue() {
		var selChartType = Ext.getCmp("charttypes").getSelectedRecords()[0];
		return {
			Type : selChartType.get("charttype")
		}
	}
	function setChartTypeGeneralValue(obj) {
		_doFirstSelection = false;
		Ext.getCmp("chartcategories").select(
				chartTypesStore.getAt(
						chartTypesStore.find("charttype", obj.Type)).get(
						"catname"), false, false);
		Ext.getCmp("charttypes").select(
				chartTypesStore.getAt(
						chartTypesStore.find("charttype", obj.Type)).get(
						"charttype"));
		_doFirstSelection = true
	}
	var mainPanel = new Ext.Panel( {
		layout : "fit",
		baseCls : "x-plain",
		border : false,
		items : [ chartTypeGeneralPanel ]
	});
	var okBtn = {
		text : "OK".localize(),
		handler : function() {
			doEditChart();
			win.close()
		}
	};
	var cancelBtn = {
		text : "Cancel".localize(),
		handler : function() {
			win.close()
		}
	};
	function fillChartDialog(chartID) {
		var chartProps;
		if ((chartProps = Jedox.wss.backend.ha.wsel("get_chart_properties",
				chartID)) == false) {
			Ext.MessageBox
					.show( {
						title : "Operation Error".localize(),
						msg : "chartDlg_EditError".localize(),
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR,
						fn : function() {
							Jedox.wss.general
									.setInputMode(Jedox.wss.app.lastInputModeDlg);
							Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.editChartType)
						}
					});
			win.destroy();
			return
		}
		win.show();
		setChartTypeGeneralValue(chartProps.ChartType.General);
		_charProps = chartProps
	}
	var doEditChart = function() {
		var chartProps = collectProperties();
		var winID = Jedox.wss.app.activeBook.getDomId();
		if (chartProps != false) {
			chartProps.id = chartID;
			var chartType = Jedox.wss.backend.ha
					.wsel("get_chart_type", chartID);
			var chartElem = Ext.get("".concat(winID, "_ws_element_", chartID));
			chartProps.size = [ chartElem.getWidth(), chartElem.getHeight() ];
			var newMinW = 0, newMinH = 0;
			if (chartType != chartProps.props.ChartType.General.Type) {
				var whRatio = chartsMinWidthHeightRatio[chartTypesSizeDefs[chartProps.props.ChartType.General.Type]];
				if (chartElem.getWidth() < (whRatio[0] * chartMinSizeFactor)) {
					newMinW = whRatio[0] * chartMinSizeFactor;
					chartProps.size[0] = newMinW
				}
				if (chartElem.getHeight() < (whRatio[1] * chartMinSizeFactor)) {
					newMinH = whRatio[1] * chartMinSizeFactor;
					chartProps.size[1] = newMinH
				}
			}
			if (Jedox.wss.backend.ha.wsel("update_chart", chartProps)) {
				if (chartType != chartProps.props.ChartType.General.Type) {
					chartElem.elemSubtype = chartProps.props.ChartType.General.Type;
					var chartElemWrap = Ext.get("".concat(winID,
							"_ws_element_", chartID, "-rzwrap"));
					if (newMinW) {
						chartElemWrap.setWidth(newMinW);
						chartElem.setWidth(newMinW)
					}
					if (newMinH) {
						chartElemWrap.setHeight(newMinH);
						chartElem.setHeight(newMinH)
					}
				}
				var currDate = new Date();
				document.getElementById("".concat(winID, "_ws_element_",
						chartID)).src = "cc/gen_element.php?wam=".concat(
						Jedox.wss.app.appModeS, "&id=", chartID, "&w=",
						chartProps.size[0], "&h=", chartProps.size[1], "&ts=",
						currDate.getTime())
			}
		}
	};
	function collectProperties() {
		var whRatio = getWHRatio();
		var vportPos = Jedox.wss.app.activeBook.getViewportPos();
		var elWidth = whRatio[0] * 100;
		var elHeight = whRatio[1] * 100;
		var elTop = parseInt(vportPos[0][1]
				+ (vportPos[1][1] - vportPos[0][1] - elHeight) / 2);
		var elLeft = parseInt(vportPos[0][0]
				+ (vportPos[1][0] - vportPos[0][0] - elWidth) / 2);
		var nLoc = Jedox.wss.wsel.getNLoc(elLeft, elTop, elWidth, elHeight);
		var chartProps = {
			props : {
				ChartType : {
					General : getChartTypeGeneralValue()
				},
				Title : _charProps.Title,
				SourceData : _charProps.SourceData,
				Legend : _charProps.Legend,
				General : {
					WH_ratio : whRatio,
					Layout : -1,
					Style : -1,
					ActiveSheet : Jedox.wss.app.activeSheetName
				}
			},
			n_location : nLoc.n_location,
			pos_offsets : nLoc.pos_offsets,
			size : [ elWidth, elHeight ],
			operation : "cct"
		};
		if (!chartProps.props.ChartType.General.Type.indexOf("xl") == 0) {
			Ext.MessageBox.show( {
				title : "Input Error".localize(),
				msg : "chartDlg_invalidChartType".localize(),
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.WARNING
			});
			return false
		}
		console.log(chartProps);
		return chartProps
	}
	var win = new Ext.Window( {
		title : "Change Chart Type".localize(),
		closable : true,
		closeAction : "close",
		cls : "default-format-window",
		autoDestroy : true,
		plain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 490,
		height : 425,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.editChartType)
			}
		},
		buttons : [ okBtn, cancelBtn ]
	});
	if (mode == "edit") {
		fillChartDialog(chartID)
	}
};