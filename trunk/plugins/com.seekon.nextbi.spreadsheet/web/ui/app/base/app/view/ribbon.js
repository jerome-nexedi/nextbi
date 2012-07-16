Jedox.wss.app.initRibbon = function() {
	var ribbon = Jedox.wss.app.toolbar = {}, enaRestr = !Jedox.wss.app.UPRestrictMode, fileItems = [ {
		text : "Refresh".localize(),
		scale : "large",
		rowspan : 3,
		width : 40,
		iconCls : "printpreview-icon32",
		iconAlign : "top",
		tooltip : "Refresh / Recalculate".localize(),
		handler : function() {
			Jedox.wss.app.activeSheet.recalc()
		}
	} ];
	if (enaRestr) {
		fileItems.push( {
			text : "&nbsp;".concat("Open".localize(), "&nbsp;"),
			scale : "large",
			rowspan : 3,
			iconCls : "open-icon32",
			iconAlign : "top",
			width : 40,
			tooltip : "Open document".localize(),
			handler : function() {
				Jedox.wss.app
						.load(Jedox.wss.app.dynJSRegistry.open, [ "open" ])
			}
		})
	}
	if (window.opener != null) {
		fileItems.push( {
			text : "Close",
			iconCls : "close-icon",
			tooltip : "Close".localize(),
			handler : Jedox.wss.action.closeWindow
		})
	}
	var home = new Ext.Panel( {
		title : "Home",
		tbar : [ {
			xtype : "buttongroup",
			columns : 2,
			title : "File",
			height : 90,
			items : fileItems
		}, {
			xtype : "buttongroup",
			columns : 2,
			title : "Clipboard",
			height : 90,
			items : [ {
				text : "Paste",
				scale : "large",
				rowspan : 3,
				iconCls : "paste-icon32",
				iconAlign : "top",
				width : 40,
				arrowAlign : "bottom",
				tooltip : "Paste".localize(),
				disabled : true,
				handler : Jedox.wss.action.paste
			}, {
				text : "Cut",
				iconCls : "cut-icon",
				handler : function() {
					Jedox.wss.action.cut(false)
				}
			}, {
				text : "Copy",
				iconCls : "copy-icon",
				handler : function() {
					Jedox.wss.action.copy(false)
				}
			} ]
		}, {
			xtype : "buttongroup",
			columns : 2,
			title : "Export".localize(),
			height : 90,
			items : [ {
				text : "HTML".localize(),
				scale : "large",
				rowspan : 3,
				width : 40,
				iconCls : "printpreview-icon32",
				iconAlign : "top",
				handler : Jedox.wss.action.exportToHTML
			}, {
				text : "&nbsp;".concat("PDF".localize(), "&nbsp;"),
				scale : "large",
				rowspan : 3,
				width : 40,
				iconCls : "printtopdf-icon32",
				iconAlign : "top",
				tooltip : "Print To PDF".localize(),
				handler : Jedox.wss.action.exportToPDF,
				disabled : !Jedox.wss.app.fopper
			} ]
		} ]
	});
	var pageLayout = new Ext.Panel( {
		title : "Page Layout",
		tbar : [ {
			xtype : "buttongroup",
			columns : 1,
			title : "Themes".localize(),
			bodyStyle : "padding:0px 10px 0px 10px",
			height : 90,
			items : [ {
				xtype : "splitbutton",
				text : "Theme".localize(),
				iconCls : "themeblue-icon32",
				scale : "large",
				rowspan : 3,
				width : 40,
				iconAlign : "top",
				arrowAlign : "bottom",
				menu : (ribbon.themeMenu = new Ext.menu.Menu( {
					items : [ {
						text : "Blue (default)".localize(),
						id : "theme-default",
						enableToggle : true,
						checked : true,
						checkHandler : Jedox.wss.action.switchTheme
					}, {
						text : "Gray".localize(),
						id : "theme-gray",
						enableToggle : true,
						checked : false,
						checkHandler : Jedox.wss.action.switchTheme
					}, {
						text : "Dark".localize(),
						id : "theme-slate",
						enableToggle : true,
						checked : false,
						checkHandler : Jedox.wss.action.switchTheme
					} ]
				}))
			} ]
		}, {
			xtype : "buttongroup",
			columns : 2,
			title : "Page Setup",
			height : 90,
			items : [ {
				text : "Print<br>Preview",
				scale : "large",
				rowspan : 3,
				width : 40,
				iconCls : "printpreview-icon32",
				iconAlign : "top",
				handler : Jedox.wss.action.exportToHTML
			}, {
				text : "Page Setup",
				iconCls : "pagesetup-icon",
				handler : function() {
					Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.pageSetup)
				}
			} ]
		} ]
	});
	var formulas = new Ext.Panel(
			{
				title : "Formulas",
				tbar : [ {
					xtype : "buttongroup",
					columns : 2,
					height : 90,
					title : "Calculation",
					items : [
							{
								text : "Refresh<br>Data".localize(),
								iconCls : "refresh-icon",
								scale : "large",
								rowspan : 3,
								width : 50,
								iconAlign : "top",
								handler : function() {
									Jedox.wss.app.activeSheet.recalc()
								}
							},
							{
								xtype : "checkbox",
								id : "autoRefreshDataMenu",
								boxLabel : "Auto - Refresh Data",
								enableToggle : true,
								checked : false,
								handler : function() {
									this.checked ? Jedox.wss.app
											.load(Jedox.wss.app.dynJSRegistry.autoRefreshStart)
											: Jedox.wss.book.autoRefresh(0)
								},
								listeners : {
									beforeshow : function() {
										this.items
												.get("autoRefreshDataMenu")
												.setChecked(
														Jedox.wss.app.activeBook._autoRefresh > 0,
														true)
									}
								}
							} ]
				} ]
			});
	var view = new Ext.Panel( {
		title : "View",
		tbar : [ {
			xtype : "buttongroup",
			columns : 1,
			title : "Show/Hide",
			bodyStyle : "padding:0px 10px 0px 10px; ",
			width : 400,
			height : 90,
			items : [ {
				xtype : "checkbox",
				boxLabel : "Status Bar".localize(),
				enableToggle : true,
				checked : true,
				handler : function(btn, state) {
					Jedox.wss.app.statusBar.hideShow(state)
				}
			} ]
		}, {
			xtype : "buttongroup",
			columns : 2,
			bodyStyle : "padding:0px 10px 10px 10px; ",
			height : 90,
			title : "Window",
			items : [ {
				text : "Arrange <br>All",
				scale : "large",
				width : 50,
				rowspan : 3,
				iconCls : "arrangeall-icon32",
				iconAlign : "top",
				handler : function() {
					Jedox.wss.dialog.openArrangeWindows()
				}
			}, {
				text : "Hide",
				iconCls : "hide-icon",
				handler : function() {
					Jedox.wss.workspace.hideActiveWin()
				}
			}, {
				text : "Unhide",
				iconCls : "unhide-icon",
				handler : function() {
					Jedox.wss.dialog.openUnhideDialog()
				}
			}, {
				xtype : "splitbutton",
				text : "Open documents".localize(),
				iconCls : "unhide-icon",
				disabled : !enaRestr,
				menu : (ribbon.openDocs = new Ext.menu.Menu())
			} ]
		} ]
	});
	var help = new Ext.Panel( {
		title : "Help",
		tbar : [ {
			xtype : "buttongroup",
			columns : 1,
			bodyStyle : "padding:0px 10px 10px 10px; ",
			height : 90,
			title : "Window",
			items : [ {
				text : "Jedox Online".localize(),
				iconCls : "hide-icon",
				handler : function() {
					window.open("http://www.jedox.com", "_blank")
				}
			} ]
		} ]
	});
	Jedox.wss.app.ribbon = new Ext.TabPanel( {
		id : "ribbon-view-panel",
		layoutOnTabChange : true,
		activeTab : 0,
		renderTo : "ribbon",
		items : [ home, pageLayout, formulas, view, help ]
	})
};