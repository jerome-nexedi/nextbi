Jedox.wss.dialog.autoRefreshStart = function() {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var autoRefreshLbl = new Ext.form.Label( {
		text : "Refresh every".localize()
	});
	var unitTxf = new Ext.form.TextField( {
		hideLabel : true,
		allowBlank : false,
		width : 50
	});
	var unitLbl = new Ext.form.Label( {
		text : "seconds".localize().concat(".")
	});
	var mainPanel = {
		layout : "absolute",
		border : false,
		bodyStyle : "background-color: transparent;",
		width : 200,
		height : 100,
		defaults : {
			baseCls : "x-plain"
		},
		items : [ {
			x : 10,
			y : 20,
			items : autoRefreshLbl
		}, {
			x : 94,
			y : 17,
			items : unitTxf
		}, {
			x : 150,
			y : 20,
			items : unitLbl
		} ]
	};
	var win = new Ext.Window( {
		defaults : {
			bodyStyle : "padding:10px 5px 5px 5px"
		},
		title : "Auto Refresh".localize(),
		closable : true,
		closeAction : "close",
		autoDestroy : true,
		cls : "default-format-window",
		plain : true,
		constrain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 280,
		height : 130,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app
						.unload(Jedox.wss.app.dynJSRegistry.autoRefreshStart)
			}
		},
		buttons : [ {
			text : "OK".localize(),
			handler : function() {
				startTimer();
				win.close()
			}
		}, {
			text : "Cancel".localize(),
			handler : function() {
				win.close()
			}
		} ]
	});
	win.show(this);
	function startTimer() {
		Jedox.wss.book.autoRefresh(unitTxf.getValue())
	}
};