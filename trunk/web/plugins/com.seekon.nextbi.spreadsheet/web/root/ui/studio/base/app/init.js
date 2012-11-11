Ext.onReady(function() {
	Ext.QuickTips.init();
	Jedox.studio.backend.wssStudio = new Studio();
	Jedox.studio.frames = {};
	Ext.applyIf(Jedox.studio.i18n.strings, Jedox.wss.tmp.i18n_strings);
	delete Jedox.wss.tmp.i18n_strings;
	document.onkeydown = Jedox.studio.keyboard.checkGlobalInput;
	var viewport = new Ext.Viewport( {
		layout : "border",
		items : Jedox.studio.app.initModules(),
		listeners : {
			afterrender : function() {
				function doOpen(data) {
					Ext.getCmp("reports-tree-holder").expand();
					if (Jedox.studio.access.rules.ste_reports
							& Jedox.studio.access.permType.WRITE) {
						Ext.getCmp("studioReportsTree").fireEvent("switchToQV")
					}
					setTimeout(function() {
						if (data) {
							var g = data.group;
							var h = data.hierarchy;
							var n = data.node;
							var p = data.path;
							var vars = data["var"];
							var type = "spreadsheet";
							if (Jedox.studio.app.params["var"]) {
								Ext.getCmp("reports-content-panel").fireEvent(
										"openExternalHL", {
											g : g,
											h : h,
											n : n,
											v : vars
										}, p.split("/").pop(), type)
							} else {
								Ext.getCmp("quickViewStudioReportsTree")
										.fireEvent("preselect", {
											g : g,
											h : h,
											n : n,
											rPath : p,
											open : true
										})
							}
						}
					}, 500)
				}
				if (Jedox.studio.app.defaultReports) {
					var data = Jedox.studio.app.defaultReports;
					if (data.group && data.hierarchy && data.node) {
						setTimeout(function() {
							doOpen(data)
						}, 200)
					}
				}
			}
		}
	})
});