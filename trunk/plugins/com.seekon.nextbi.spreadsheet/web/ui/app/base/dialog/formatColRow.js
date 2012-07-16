Jedox.wss.dialog.openFormatColRow = function(type, size) {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var rowSize = new Ext.form.NumberField( {
		fieldLabel : "Row height".localize(),
		width : 35,
		layout : "form",
		hideLabel : false,
		autoHeight : true,
		allowDecimals : false,
		enableKeyEvents : true,
		value : size,
		allowBlank : false,
		labelSeparator : ":",
		labelStyle : "margin: 0px;"
	});
	rowSize.on("specialKey", function(rowSize, e) {
		if (e.getKey() == 13) {
			if (type == 0) {
				size = colSize.getValue()
			} else {
				if (type == 1) {
					size = rowSize.getValue()
				}
			}
			Jedox.wss.action.resizeRowCol(type, size);
			win.close()
		}
	});
	var colSize = new Ext.form.NumberField( {
		width : 35,
		allowBlank : false,
		autoHeight : true,
		allowDecimals : false,
		value : size,
		layout : "form",
		hideLabel : false,
		enableKeyEvents : true,
		fieldLabel : "Column width".localize(),
		labelStyle : "margin: 0px;"
	});
	colSize.on("specialKey", function(colSize, e) {
		if (e.getKey() == 13) {
			if (type == 0) {
				size = colSize.getValue()
			} else {
				if (type == 1) {
					size = rowSize.getValue()
				}
			}
			win.hide();
			Jedox.wss.action.resizeRowCol(type, size);
			Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
			Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
			Jedox.wss.general.refreshCursorField()
		}
	});
	if (type == 0) {
		var displayField = colSize;
		var winTitle = "Column Width".localize()
	} else {
		if (type == 1) {
			var displayField = rowSize;
			var winTitle = "Row Height".localize()
		}
	}
	var win = new Ext.Window(
			{
				title : winTitle,
				closable : true,
				cls : "default-format-window",
				autoDestroy : true,
				plain : true,
				constrain : true,
				modal : true,
				resizable : false,
				animCollapse : false,
				width : 180,
				autoHeight : true,
				layout : "form",
				items : [ new Ext.Panel(
						{
							id : "sizetab",
							bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
							border : false,
							frame : false,
							autoHeight : true,
							layout : "form",
							items : [ displayField ]
						}) ],
				listeners : {
					close : function() {
						Jedox.wss.general
								.setInputMode(Jedox.wss.app.lastInputModeDlg);
						Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
						Jedox.wss.general.refreshCursorField();
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.formatColRow)
					},
					activate : function() {
						console.log("act");
						setTimeout(function() {
							if (type == 0) {
								colSize.focus(true, true)
							} else {
								if (type == 1) {
									rowSize.focus(true, true)
								}
							}
						}, 200)
					}
				},
				buttons : [ {
					text : "OK".localize(),
					handler : function() {
						if (type == 0) {
							size = colSize.getValue()
						} else {
							if (type == 1) {
								size = rowSize.getValue()
							}
						}
						Jedox.wss.action.resizeRowCol(type, size);
						win.close()
					}
				}, {
					text : "Cancel".localize(),
					handler : function() {
						win.close()
					}
				} ]
			});
	win.show(this)
};