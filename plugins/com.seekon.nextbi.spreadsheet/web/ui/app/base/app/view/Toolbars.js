Jedox.wss.app.initActiveToolbars = function() {
	Jedox.wss.app.initStandardToolbar()
};
Jedox.wss.app.initStandardToolbar = function() {
	var stdTbar = Jedox.wss.app.standardToolbar = new Ext.Toolbar( {
		cls : "wssUserMenubar",
		renderTo : "wssStandardToolbar",
		items : [ {
			iconCls : "iconrecalcnow",
			text : "Refresh".localize(),
			tooltip : "Refresh / Recalculate".localize(),
			handler : function() {
				Jedox.wss.app.activeSheet.recalc()
			}
		}, "-", {
			iconCls : "icon_copy",
			cls : "x-btn-icon",
			tooltip : "Copy".localize(),
			handler : function() {
				Jedox.wss.action.copy(false)
			}
		}, {
			iconCls : "icon_cut",
			cls : "x-btn-icon",
			tooltip : "Cut".localize(),
			handler : function() {
				Jedox.wss.action.cut(false)
			}
		}, {
			iconCls : "icon_paste",
			cls : "x-btn-icon",
			tooltip : "Paste".localize(),
			handler : Jedox.wss.action.paste
		}, "-", {
			iconCls : "iconprintpreview",
			text : "Print Preview".localize(),
			tooltip : "Print Preview".localize(),
			handler : Jedox.wss.action.exportToHTML
		}, {
			iconCls : "iconprinttopdf",
			text : "PDF".localize(),
			tooltip : "Print To PDF".localize(),
			handler : Jedox.wss.action.exportToPDF,
			disabled : !Jedox.wss.app.fopper
		} ]
	});
	if (window.opener != null) {
		stdTbar.add("-", {
			iconCls : "iconclose",
			text : "Close".localize(),
			tooltip : "Close".localize(),
			handler : Jedox.wss.action.closeWindow
		})
	}
	stdTbar.doLayout()
};