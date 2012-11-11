Jedox.studio.app.initMenubar = function() {
	var fileMenu = new Ext.menu.Menu( {
		id : "fileMenu",
		enableScrolling : false,
		listeners : {
			hide : function(menu) {
				menu.destroy()
			}
		},
		items : [ {
			text : "Options".localize()
		}, "-", {
			text : "Exit".localize()
		} ]
	});
	var actionMenu = new Ext.menu.Menu( {
		id : "actionMenu",
		enableScrolling : false,
		listeners : {
			hide : function(menu) {
				menu.destroy()
			}
		},
		items : [ {
			text : "New User".localize().concat("...")
		} ]
	});
	var helpMenu = new Ext.menu.Menu( {
		id : "helpMenu",
		enableScrolling : false,
		listeners : {
			hide : function(menu) {
				menu.destroy()
			}
		},
		items : [ {
			text : "Help Topics".localize()
		} ]
	});
	var mainMenu = new Ext.Toolbar( {
		cls : "extmenubar"
	});
	mainMenu.render("mainMenu");
	mainMenu.add( {
		text : "File".localize(),
		cls : "extmenubaritem",
		menu : fileMenu
	}, {
		text : "Action".localize(),
		menu : actionMenu,
		cls : "extmenubaritem"
	}, {
		text : "Help".localize(),
		menu : helpMenu,
		cls : "extmenubaritem"
	})
};