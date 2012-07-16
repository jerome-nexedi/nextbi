Jedox.wss.dialog.openRenameSheet = function(sheetID, sheetTitle) {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var sheetNewNameTxf = new Ext.form.TextField( {
		fieldLabel : "New Name".localize(),
		enableKeyEvents : true,
		width : 135
	});
	var mainPanel = new Ext.Panel( {
		modal : true,
		layout : "form",
		baseCls : "main-panel",
		border : false,
		items : sheetNewNameTxf
	});
	function isNameValid() {
		var name = sheetNewNameTxf.getValue();
		if (name.length < 3) {
			sheetNewNameTxf.markInvalid("Sheet Name specified is not a valid!");
			return false
		} else {
			return true
		}
	}
	function rename() {
		if (!isNameValid()) {
			return
		}
		var newName = sheetNewNameTxf.getValue();
		Jedox.wss.backend.conn.cmd( [ this, rename_post, newName ],
				[ "oren", 2 ], [ newName ])
	}
	function rename_post(res, newName) {
		if (res[0][0] === true) {
			Ext.getCmp(sheetID).setTitle(newName)
		}
		win.close()
	}
	function showWarrningMessage(name) {
		win.hide();
		var informationMsg = "informationMsg".localize();
		var adviceMsg = "adviceMsg".localize();
		Ext.Msg.show( {
			title : "Rename Sheet".localize() + "?",
			msg : "<b>" + name + "</b> " + informationMsg + "?<br>" + adviceMsg
					+ ".",
			buttons : Ext.Msg.OK,
			fn : function() {
				win.show();
				sheetNewNameTxf.selectText();
				sheetNewNameTxf.focus(true, true)
			},
			animEl : "elId",
			width : 320,
			icon : Ext.MessageBox.ERROR
		})
	}
	sheetNewNameTxf.on("specialKey", function(txf, e) {
		if (e.getKey() == 13) {
			rename()
		}
	});
	var win = new Ext.Window( {
		defaults : {
			bodyStyle : "padding:10px"
		},
		title : "Rename Sheet".localize(),
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
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.renameSheet)
			}
		},
		buttons : [ {
			text : "Rename".localize(),
			handler : rename
		}, {
			text : "Cancel".localize(),
			handler : function() {
				win.close()
			}
		} ]
	});
	win.show(this);
	sheetNewNameTxf.setValue(sheetTitle);
	sheetNewNameTxf.selectText();
	sheetNewNameTxf.focus(true, true)
};