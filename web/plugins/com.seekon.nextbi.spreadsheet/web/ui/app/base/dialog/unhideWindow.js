Jedox.wss.dialog.openUnhideDialog = function() {
	var _config = {
		winW : 200,
		listH : 100
	};
	var WinNameRecord = new Ext.data.Record.create( [ {
		name : "index",
		name : "name"
	} ]);
	var storeWinNames = new Ext.data.SimpleStore( {
		fields : [ {
			name : "index",
			name : "name"
		} ]
	});
	var dvWins = new Ext.DataView( {
		itemSelector : "div.row-modeller",
		style : "overflow:auto",
		singleSelect : true,
		store : storeWinNames,
		cls : "modellerDataViewSelect",
		tpl : new Ext.XTemplate('<tpl for=".">', '<div class="row-modeller">',
				"<span>&#160;{name}</span>", "</div>", "</tpl>")
	});
	var paneldvWins = new Ext.Panel( {
		layout : "fit",
		style : "margin-bottom: 5px",
		height : _config.listH,
		items : [ dvWins ]
	});
	var btnOk = new Ext.Button(
			{
				text : "OK".localize(),
				listeners : {
					click : function() {
						if (dvWins.getSelectionCount() > 0) {
							try {
								win.close();
								var tmpWin = winList[storeWinNames.getAt(
										dvWins.getSelectedIndexes()[0]).get(
										"index")], metaData = Jedox.wss.workspace
										.getMetaByWinId(tmpWin._winId);
								for ( var triggers = Jedox.wss.events.triggers.unhideWorkbook_before, i = triggers.length - 1, wbMeta = metaData; i >= 0; i--) {
									triggers[i][0]["unhideWorkbook_before"]
											.call(parent, triggers[i][1],
													wbMeta.ghn, wbMeta.name)
								}
								tmpWin.show();
								tmpWin.toFront();
								Jedox.wss.workspace.activeWin = tmpWin;
								Jedox.wss.action.adjustToACL();
								for ( var triggers = Jedox.wss.events.triggers.unhideWorkbook_after, i = triggers.length - 1, wbMeta = metaData; i >= 0; i--) {
									triggers[i][0]["unhideWorkbook_after"]
											.call(parent, triggers[i][1],
													wbMeta.ghn, wbMeta.name)
								}
							} catch (e) {
								Jedox.wss.general.showMsg("Application Error"
										.localize(), e.message.localize(),
										Ext.MessageBox.ERROR)
							}
						}
					}
				}
			});
	var btnClancel = new Ext.Button( {
		text : "Cancel".localize(),
		listeners : {
			click : function() {
				win.close()
			}
		}
	});
	var panelMain = new Ext.Panel( {
		autoHeight : true,
		layout : "form",
		bodyStyle : "padding: 5px 5px 0px; background-color: transparent;",
		items : [ {
			bodyStyle : "background-color:transparent;border:none;",
			html : "Unhide workbook".localize() + ":"
		}, paneldvWins ]
	});
	var win = new Ext.Window( {
		hidden : true,
		layout : "fit",
		title : "Unhide".localize(),
		cls : "default-format-window",
		width : _config.winW,
		autoHeight : true,
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		modal : true,
		resizable : false,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.unhideWindow)
			}
		},
		items : [ panelMain ],
		buttons : [ btnOk, btnClancel ]
	});
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var winList = Jedox.wss.workspace.getHiddenWinsList();
	for ( var tmpRec, i = (winList.length - 1); i >= 0; i--) {
		tmpRec = new WinNameRecord( {
			index : i,
			name : winList[i].title
		});
		storeWinNames.add(tmpRec)
	}
	win.show()
};