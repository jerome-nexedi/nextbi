Jedox.wss.dialog.openGoTo = function() {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var goToTxf = new Ext.form.TextField( {
		fieldLabel : "Reference".localize(),
		enableKeyEvents : true,
		width : 178
	});
	var mainPanel = new Ext.Panel( {
		labelWidth : 60,
		modal : true,
		layout : "form",
		baseCls : "main-panel",
		border : false,
		items : goToTxf
	});
	function doGoTo() {
		Jedox.wss.book.goTo(goToTxf.getValue())
	}
	goToTxf.on("specialKey", function(txf, e) {
		if (e.getKey() == 13) {
			e.stopEvent();
			doGoTo();
			win.close()
		}
	});
	var win = new Ext.Window( {
		defaults : {
			bodyStyle : "padding:10px"
		},
		title : "Go To".localize(),
		closable : true,
		cls : "default-format-window",
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		constrain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 280,
		height : 110,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.goTo)
			},
			show : function() {
				setTimeout(function() {
					goToTxf.focus(true, 100)
				})
			}
		},
		buttons : [ {
			text : "OK".localize(),
			handler : function() {
				doGoTo();
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