Jedox.wss.dialog.openFindDialog = function(pattern, callBackFnc) {
	var _fromDlgF = false;
	if (Jedox.wss.app.environment.inputMode === Jedox.wss.grid.GridMode.DIALOG) {
		_fromDlgF = true
	} else {
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
	}
	var patternTxf = new Ext.form.TextField( {
		fieldLabel : "Find what".localize(),
		enableKeyEvents : true,
		value : pattern,
		width : 170
	});
	var findBtn = {
		text : "Find".localize(),
		handler : function() {
			callBackFnc(patternTxf.getValue())
		}
	};
	var cancelBtn = {
		text : "Cancel".localize(),
		handler : function() {
			win.close()
		}
	};
	var findBtnPanel = new Ext.Panel( {
		layout : "card",
		baseCls : "x-plain",
		activeItem : 0,
		items : [ {
			layout : "form",
			border : false,
			buttons : [ findBtn ]
		} ]
	});
	var cancelBtnPanel = new Ext.Panel( {
		layout : "card",
		baseCls : "x-plain",
		activeItem : 0,
		items : [ {
			layout : "form",
			border : false,
			buttons : [ cancelBtn ]
		} ]
	});
	var mainPanel = new Ext.Panel( {
		layout : "absolute",
		baseCls : "x-plain",
		border : false,
		items : [ {
			layout : "form",
			border : false,
			baseCls : "x-plain",
			x : 5,
			y : 12,
			width : 285,
			height : 100,
			items : [ patternTxf ]
		}, {
			layout : "form",
			baseCls : "x-plain",
			x : 290,
			y : 5,
			height : 90,
			items : [ findBtnPanel, cancelBtnPanel ]
		} ]
	});
	patternTxf.on("specialKey", function(txf, e) {
		if (e.getKey() == 13) {
			callBackFnc(patternTxf.getValue())
		}
	});
	var win = new Ext.Window( {
		defaults : {
			bodyStyle : "padding:10px"
		},
		title : "Find".localize(),
		closable : true,
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		constrain : true,
		cls : "default-format-window",
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 400,
		height : 130,
		items : mainPanel,
		listeners : {
			close : function() {
				if (!_fromDlgF) {
					Jedox.wss.general
							.setInputMode(Jedox.wss.app.lastInputModeDlg);
					Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
				}
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.find)
			}
		}
	});
	win.show(this)
};