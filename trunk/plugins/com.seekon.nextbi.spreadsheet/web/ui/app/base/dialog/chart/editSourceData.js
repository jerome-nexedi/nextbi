Jedox.wss.dialog.chart.editSourceDataDialog = function(mode, chartID) {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var _chartProps;
	var generalLbl = new Ext.form.Label( {
		text : "Source Data Options".localize().concat(":"),
		cls : "edit-chart-title"
	});
	var BRLbl = {
		html : "<br/>",
		baseCls : "x-plain"
	};
	var refersToTxf = new Ext.form.TextField( {
		fieldLabel : "Chart Data Range".localize(),
		width : 160
	});
	var selectRangeBtn = new Ext.Toolbar.Button( {
		id : "selRangeBegin",
		iconCls : "select-range-icon",
		cls : "x-btn-icon",
		tooltip : "Select Range".localize(),
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRangeHandleFnc ],
				rng : refersToTxf.getValue()
			} ]);
			win.hide()
		}
	});
	var groupDataByCmb = new Ext.form.ComboBox( {
		fieldLabel : "Group Data By".localize(),
		store : new Ext.data.SimpleStore( {
			data : [ [ "Auto".localize(), "auto" ],
					[ "Columns".localize(), "cols" ],
					[ "Rows".localize(), "rows" ] ],
			fields : [ "itemName", "itemValue" ]
		}),
		displayField : "itemName",
		valueField : "itemValue",
		mode : "local",
		triggerAction : "all",
		value : "auto",
		listWidth : 70,
		width : 70,
		editable : false,
		allowBlank : false,
		selectOnFocus : false,
		listeners : {
			select : {
				fn : onSelGroupBy,
				scope : this
			}
		}
	});
	var useSeriesLabelsCmb = new Ext.form.ComboBox( {
		fieldLabel : "Use series labels".localize(),
		id : "firstRowLabels",
		store : new Ext.data.SimpleStore( {
			data : [ [ "Auto".localize(), "auto" ],
					[ "Yes".localize(), "yes" ], [ "No".localize(), "no" ] ],
			fields : [ "itemName", "itemValue" ]
		}),
		displayField : "itemName",
		valueField : "itemValue",
		mode : "local",
		triggerAction : "all",
		value : "auto",
		listWidth : 70,
		width : 70,
		editable : false,
		allowBlank : false,
		selectOnFocus : false,
		disabled : true
	});
	var useCategoryLabelsCmb = new Ext.form.ComboBox( {
		id : "firstColLabels",
		fieldLabel : "Use category labels".localize(),
		store : new Ext.data.SimpleStore( {
			data : [ [ "Auto".localize(), "auto" ],
					[ "Yes".localize(), "yes" ], [ "No".localize(), "no" ] ],
			fields : [ "itemName", "itemValue" ]
		}),
		displayField : "itemName",
		valueField : "itemValue",
		mode : "local",
		triggerAction : "all",
		value : "auto",
		listWidth : 70,
		width : 70,
		editable : false,
		allowBlank : false,
		selectOnFocus : false,
		disabled : true
	});
	var sourceDataPanel = {
		border : false,
		items : [ {
			layout : "form",
			baseCls : "x-plain",
			labelWidth : 135,
			labelAlign : "left",
			items : [ BRLbl, generalLbl, BRLbl, {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				items : [ {
					layout : "form",
					labelWidth : 135,
					labelAlign : "left",
					width : 305,
					baseCls : "x-plain",
					items : refersToTxf
				}, {
					layout : "form",
					border : false,
					width : 30,
					baseCls : "x-plain",
					items : selectRangeBtn
				} ]
			}, groupDataByCmb, useSeriesLabelsCmb, useCategoryLabelsCmb ]
		} ]
	};
	function selRangeHandleFnc(selected) {
		win.show();
		refersToTxf.setValue(selected)
	}
	function onSelGroupBy(combo, record, index) {
		if (combo.getValue() == "auto") {
			var fRowLbl = Ext.getCmp("firstRowLabels");
			fRowLbl.setValue("auto");
			fRowLbl.disable();
			var fColLbl = Ext.getCmp("firstColLabels");
			fColLbl.setValue("auto");
			fColLbl.disable()
		} else {
			Ext.getCmp("firstRowLabels").enable();
			Ext.getCmp("firstColLabels").enable()
		}
	}
	function getSourceDataOptionsValue() {
		var sourceData = refersToTxf.getValue();
		var groupBy = groupDataByCmb.getValue();
		var useSeriesLabels = useSeriesLabelsCmb.getValue();
		var useCategoryLabels = useCategoryLabelsCmb.getValue();
		return {
			General : {
				Source : sourceData,
				GroupBy : groupBy,
				SeriesLabels : useSeriesLabels,
				CategoryLabels : useCategoryLabels
			}
		}
	}
	function setSourceDataOptionsValue(obj) {
		refersToTxf.setValue(obj.Source);
		groupDataByCmb.setValue(obj.GroupBy);
		if (obj.GroupBy != "auto") {
			useSeriesLabelsCmb.enable();
			useSeriesLabelsCmb.setValue(obj.SeriesLabels);
			useCategoryLabelsCmb.enable();
			useCategoryLabelsCmb.setValue(obj.CategoryLabels)
		}
	}
	var mainPanel = new Ext.Panel(
			{
				layout : "fit",
				baseCls : "x-plain",
				defaults : {
					bodyStyle : "background-color: transparent; padding:0px 5px 5px 20px;"
				},
				border : false,
				items : [ sourceDataPanel ]
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
									.unload(Jedox.wss.app.dynJSRegistry.editSourceData)
						}
					});
			win.destroy();
			return
		}
		win.show();
		setSourceDataOptionsValue(chartProps.SourceData.General);
		_chartProps = chartProps
	}
	var doEditChart = function() {
		var chartProps = collectProperties();
		var winID = Jedox.wss.app.activeBook.getDomId();
		if (chartProps != false) {
			chartProps.id = chartID;
			var chartElem = Ext.get("".concat(winID, "_ws_element_", chartID));
			chartProps.size = [ chartElem.getWidth(), chartElem.getHeight() ];
			if (Jedox.wss.backend.ha.wsel("update_chart", chartProps)) {
				var currDate = new Date();
				document.getElementById("".concat(winID, "_ws_element_",
						chartID)).src = "cc/gen_element.php?wam=".concat(
						Jedox.wss.app.appModeS, "&id=", chartID, "&ts=",
						currDate.getTime())
			}
		}
	};
	function collectProperties() {
		var chartProps = {
			props : {
				SourceData : getSourceDataOptionsValue()
			},
			operation : "ssd"
		};
		if (chartProps.props.SourceData.General.Source.length == 0) {
			Ext.MessageBox.show( {
				title : "Input Error".localize(),
				msg : "chartDlg_rangeEmpty".localize(),
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.WARNING,
				fn : function() {
					Ext.getCmp("dataRange").focus()
				}
			});
			return false
		}
		return chartProps
	}
	var win = new Ext.Window( {
		title : "Select Source Data".localize(),
		closable : true,
		closeAction : "close",
		cls : "default-format-window",
		autoDestroy : true,
		plain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 400,
		height : 300,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app
						.unload(Jedox.wss.app.dynJSRegistry.editSourceData)
			}
		},
		buttons : [ okBtn, cancelBtn ]
	});
	if (mode == "edit") {
		fillChartDialog(chartID)
	}
};