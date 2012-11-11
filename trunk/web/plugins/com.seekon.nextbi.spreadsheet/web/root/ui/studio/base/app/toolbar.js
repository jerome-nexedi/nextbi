Jedox.studio.app.initToolbar = function() {
	var tb = new Ext.Toolbar( {
		cls : "exttoolbar"
	});
	tb.render("toolbar");
	tb.add( {
		id : "home",
		text : "Home".localize(),
		iconCls : "home-icon",
		tooltip : {
			text : "Go to home".localize()
		}
	}, {
		id : "refresh",
		text : "Refresh".localize(),
		iconCls : "refresh-icon",
		tooltip : {
			text : "Refresh the page".localize()
		}
	}, "-", {
		id : "copy",
		text : "Copy".localize(),
		iconCls : "copy-icon",
		tooltip : {
			text : "Copy".localize()
		}
	}, {
		id : "cut",
		text : "Cut".localize(),
		iconCls : "cut-icon",
		tooltip : {
			text : "Cut".localize()
		}
	}, {
		id : "paste",
		text : "Paste".localize(),
		iconCls : "paste-icon",
		tooltip : {
			text : "Paste".localize()
		}
	}, "-", {
		id : "options",
		text : "Options".localize(),
		iconCls : "properties-icon",
		tooltip : {
			text : "Options".localize()
		}
	}, {
		id : "help",
		text : "Help".localize(),
		iconCls : "help-icon",
		tooltip : {
			text : "Help".localize()
		}
	}, {
		id : "close",
		text : "Close".localize(),
		iconCls : "close-icon",
		tooltip : {
			text : "Close".localize()
		}
	})
};