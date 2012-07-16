Jedox.wss.app.initStatusBar = function() {
	var modes = {
		designer : "Designer".localize(),
		user : "User".localize()
	};
	var itemInputMode = new Ext.Toolbar.TextItem( {
		text : "Ready".localize(),
		cls : "statusBarStyle",
		style : "width:60px; padding:0px 8px 0px 5px;"
	});
	var itemWorkingMode = new Ext.Toolbar.TextItem( {
		text : modes[Jedox.wss.app.appMode],
		cls : "statusBarStyle",
		style : "width:60px; padding:0px 8px 0px 5px;"
	});
	Jedox.wss.app.statusBar = new Ext.ux.StatusBar( {
		renderTo : "statusBarContainer",
		items : [ new Ext.Toolbar.TextItem( {
			text : "Status".localize() + ": ",
			cls : "wssStatusBar"
		}), itemInputMode, " ", new Ext.Toolbar.TextItem( {
			text : "Mode".localize() + ": ",
			cls : "wssStatusBar"
		}), itemWorkingMode, " " ]
	});
	Ext.get("statusBarContainer").unselectable();
	Jedox.wss.app.statusBar.setInputMode = function(text) {
		Ext.fly(itemInputMode.getEl()).update(text)
	};
	Jedox.wss.app.statusBar.setWorkingMode = function(text) {
		Ext.fly(itemWorkingMode.getEl()).update(text)
	};
	Jedox.wss.app.statusBar.hideShow = function(state) {
		if (state) {
			Jedox.wss.app.showBar("statusBarContainer")
		} else {
			Jedox.wss.app.hideBar("statusBarContainer")
		}
	};
	Jedox.wss.app.statusBar.doLayout()
};