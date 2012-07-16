Jedox.wss.dialog.openArrangeWindows = function() {
	var _config = {
		winW : 200,
		tiled : {
			1 : [ {
				w : 1,
				h : 1
			} ],
			2 : [ {
				w : 1 / 2,
				h : 1
			}, {
				w : 1 / 2,
				h : 1
			} ],
			3 : [ {
				w : 1 / 2,
				h : 1
			}, {
				w : 1 / 2,
				h : 1 / 2
			}, {
				w : 1 / 2,
				h : 1 / 2
			} ],
			4 : [ {
				w : 1 / 2,
				h : 1 / 2
			}, {
				w : 1 / 2,
				h : 1 / 2
			}, {
				w : 1 / 2,
				h : 1 / 2
			}, {
				w : 1 / 2,
				h : 1 / 2
			} ],
			5 : [ {
				w : 1 / 3,
				h : 1
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			} ],
			6 : [ {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			} ],
			7 : [ {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			} ],
			8 : [ {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 2
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			} ],
			9 : [ {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			}, {
				w : 1 / 3,
				h : 1 / 3
			} ],
			10 : [ {
				w : 1 / 4,
				h : 1 / 2
			}, {
				w : 1 / 4,
				h : 1 / 2
			}, {
				w : 1 / 4,
				h : 1 / 2
			}, {
				w : 1 / 4,
				h : 1 / 2
			}, {
				w : 1 / 4,
				h : 1 / 3
			}, {
				w : 1 / 4,
				h : 1 / 3
			}, {
				w : 1 / 4,
				h : 1 / 3
			}, {
				w : 1 / 4,
				h : 1 / 3
			}, {
				w : 1 / 4,
				h : 1 / 3
			}, {
				w : 1 / 4,
				h : 1 / 3
			} ]
		}
	};
	var winsArrangement = ((Jedox.wss.workspace.windowsArrangement) ? Jedox.wss.workspace.windowsArrangement
			: "tiled");
	var btnOk = new Ext.Button(
			{
				text : "OK".localize(),
				listeners : {
					click : function() {
						var arrWins = Jedox.wss.workspace.getVisibleWinsList();
						var numOfWins = arrWins.length;
						if (numOfWins > 0) {
							var workspaceSize = Ext.get("workspace").getSize();
							if (Jedox.wss.workspace.hasMinimized()) {
								workspaceSize.height -= arrWins[0].header
										.getSize().height + 4
							}
							if (rbTiled.getValue()) {
								if (Jedox.wss.workspace.windowsArrangement) {
									Jedox.wss.workspace.windowsArrangement = "tiled"
								}
								var widthAdjuster = ((arrWins.length == 1) ? 1
										: 0);
								if (arrWins.length <= 10) {
									for ( var winW, winH, cordX = 0, cordY = 0, i = 0; i < arrWins.length; i++) {
										winW = workspaceSize.width
												* _config.tiled[arrWins.length][i].w
												- widthAdjuster;
										winH = workspaceSize.height
												* _config.tiled[arrWins.length][i].h;
										if (arrWins[i].maximized) {
											arrWins[i].restore()
										}
										arrWins[i].setSize(winW, winH);
										arrWins[i].setPosition(cordX, cordY);
										if ((cordY + winH + 1) > workspaceSize.height) {
											cordY = 0;
											cordX += winW
										} else {
											cordY += winH
										}
									}
								}
							} else {
								if (rbHorizontal.getValue()) {
									if (Jedox.wss.workspace.windowsArrangement) {
										Jedox.wss.workspace.windowsArrangement = "horizontal"
									}
									var winW = workspaceSize.width
											- ((arrWins.length == 1) ? 1 : 0);
									var winH = workspaceSize.height / numOfWins;
									var cordX = 0;
									var cordY = 0;
									for ( var i = 0; i < numOfWins; i++) {
										if (arrWins[i].maximized) {
											arrWins[i].restore()
										}
										arrWins[i].setSize(winW, winH);
										arrWins[i].setPosition(cordX, cordY);
										cordY += winH
									}
								} else {
									if (rbVertical.getValue()) {
										if (Jedox.wss.workspace.windowsArrangement) {
											Jedox.wss.workspace.windowsArrangement = "vertical"
										}
										var winW = workspaceSize.width
												/ numOfWins
												- ((arrWins.length == 1) ? 1
														: 0);
										var winH = workspaceSize.height;
										var cordX = 0;
										var cordY = 0;
										for ( var i = 0; i < numOfWins; i++) {
											if (arrWins[i].maximized) {
												arrWins[i].restore()
											}
											arrWins[i].setSize(winW, winH);
											arrWins[i]
													.setPosition(cordX, cordY);
											cordX += winW
										}
									} else {
										if (rbCascade.getValue()) {
											if (Jedox.wss.workspace.windowsArrangement) {
												Jedox.wss.workspace.windowsArrangement = "cascade"
											}
											if (arrWins.length) {
												var menuH = arrWins[0].header
														.getSize().height
											} else {
												var menuH = 0
											}
											var winW = workspaceSize.width
													- (menuH * (numOfWins - 1))
													- ((arrWins.length == 1) ? 1
															: 0);
											var winH = workspaceSize.height
													- (menuH * (numOfWins - 1));
											var cordX = 0;
											var cordY = 0;
											for ( var i = (numOfWins - 1); i >= 0; i--) {
												if (arrWins[i].maximized) {
													arrWins[i].restore()
												}
												arrWins[i].setSize(winW, winH);
												arrWins[i].setPosition(cordX,
														cordY);
												cordX += menuH;
												cordY += menuH
											}
										}
									}
								}
							}
						}
						win.close()
					}
				}
			});
	var btnCancel = new Ext.Button( {
		text : "Cancel".localize(),
		listeners : {
			click : function() {
				win.close()
			}
		}
	});
	var rbTiled = new Ext.form.Radio( {
		checked : (winsArrangement == "tiled"),
		name : "radioArrange",
		boxLabel : "Tiled".localize(),
		hideLabel : true
	});
	var rbHorizontal = new Ext.form.Radio( {
		checked : (winsArrangement == "horizontal"),
		name : "radioArrange",
		boxLabel : "Horizontal".localize(),
		hideLabel : true
	});
	var rbVertical = new Ext.form.Radio( {
		checked : (winsArrangement == "vertical"),
		name : "radioArrange",
		boxLabel : "Vertical".localize(),
		hideLabel : true
	});
	var rbCascade = new Ext.form.Radio( {
		checked : (winsArrangement == "cascade"),
		name : "radioArrange",
		boxLabel : "Cascade".localize(),
		hideLabel : true
	});
	var fsArrange = new Ext.form.FieldSet( {
		title : "Arrange".localize(),
		autoHeight : true,
		collapsible : false,
		items : [ rbTiled, rbHorizontal, rbVertical, rbCascade ]
	});
	var panelMain = new Ext.Panel( {
		autoHeight : true,
		layout : "fit",
		bodyStyle : "padding: 5px 5px 0px; background-color: transparent;",
		items : [ fsArrange ]
	});
	var win = new Ext.Window( {
		hidden : true,
		layout : "fit",
		title : "Arrange Windows".localize(),
		width : _config.winW,
		autoHeight : true,
		closeAction : "close",
		cls : "default-format-window",
		autoDestroy : true,
		plain : true,
		modal : true,
		resizable : false,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app
						.unload(Jedox.wss.app.dynJSRegistry.arrangeWindows)
			}
		},
		items : [ panelMain ],
		buttons : [ btnOk, btnCancel ]
	});
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	win.show()
};