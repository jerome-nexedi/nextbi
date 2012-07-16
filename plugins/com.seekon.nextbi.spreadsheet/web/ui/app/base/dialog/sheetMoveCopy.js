Jedox.wss.dialog.openSheetMoveCopy = function() {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var selectedSheetID = "";
	var selectedSheetName = "";
	var selectedIndex = "";
	var AWName = Jedox.wss.workspace.activeWin.title;
	var AWID = Jedox.wss.app.activeBook.getWbId();
	var workbook_id = AWID;
	var worksheet_id = Jedox.wss.app.activeBook.getWsId();
	var next_worksheet_id = "";
	var AWName = Jedox.wss.workspace.activeWin.title;
	var AWID = Jedox.wss.app.activeBook.getWbId();
	var tmpWData = Jedox.wss.backend.ha.getLoadedBooks();
	var WData = [];
	for ( var i = 0; i < tmpWData.length; i++) {
		if (tmpWData[i][2].length > 0) {
			eval("var wbMeta = ".concat(tmpWData[i][2]))
		}
		WData.push( [ tmpWData[i][0],
				tmpWData[i][2].length > 0 ? wbMeta.name : tmpWData[i][1] ])
	}
	var wStore = new Ext.data.SimpleStore( {
		fields : [ "workbookID", "workbookName" ],
		data : WData
	});
	var SData = [];
	var sStore = new Ext.data.SimpleStore( {
		fields : [ "sheetID", "sheetName" ]
	});
	loadSheetData();
	var toBookCmb = new Ext.form.ComboBox( {
		fieldLabel : "To Book".localize(),
		width : 250,
		store : wStore,
		displayField : "workbookName",
		editable : false,
		mode : "local",
		triggerAction : "all",
		listeners : {
			select : {
				fn : function(combo, record, index) {
					workbook_id = WData[index][0];
					loadSheetData()
				}
			}
		}
	});
	var sheetList = new Ext.DataView(
			{
				id : "sheet-list",
				store : sStore,
				tpl : new Ext.XTemplate(
						'<div class="sheet-list"><tpl for=".">',
						'<div class="thumb-wrap">',
						'<div class="thumb" style="padding: 0px; text-align: left;  ">{sheetName}</div></div>',
						"</tpl></div>"),
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				emptyText : "No images to display".localize(),
				autoWidth : true,
				listeners : {
					containerclick : {
						fn : onContainerClick,
						scope : this
					},
					click : {
						fn : function(dataView, index, node, e) {
							next_worksheet_id = SData[index][0]
						}
					},
					dblclick : {
						fn : function(dataView, index, node, e) {
							next_worksheet_id = SData[index][0];
							moveWorksheet();
							win.close()
						}
					}
				}
			});
	var createCopyChb = new Ext.form.Checkbox( {
		hideLabel : true,
		boxLabel : "Create a copy".localize(),
		name : "rb-order",
		inputValue : 8
	});
	function loadSheetData() {
		var worksheets = Jedox.wss.backend.ha.getSheets(workbook_id)[0];
		SData = [];
		for ( var i = 0; i < worksheets.length; i += 2) {
			SData.push( [ worksheets[i], worksheets[i + 1] ])
		}
		SData.push( [ "", "(move to end)".localize() ]);
		sStore.loadData(SData)
	}
	function onContainerClick(dView, e) {
		e.stopEvent();
		return false
	}
	function moveWorksheet() {
		if (sheetList.getSelectionCount() > 0) {
			if (workbook_id === AWID) {
				workbook_id = ""
			}
			if (createCopyChb.checked) {
				Jedox.wss.app.activeBook.getSheetSelector().copySheet(
						worksheet_id, next_worksheet_id, workbook_id)
			} else {
				Jedox.wss.app.activeBook.getSheetSelector().moveSheet(
						worksheet_id, next_worksheet_id, workbook_id)
			}
		}
	}
	var mainPanel = new Ext.Panel( {
		labelAlign : "top",
		modal : true,
		layout : "form",
		baseCls : "main-panel",
		border : false,
		items : [ toBookCmb, {
			html : "Before sheet".localize() + ":",
			baseCls : "x-plain"
		}, {
			autoScroll : true,
			layout : "fit",
			border : true,
			width : 250,
			height : 150,
			items : sheetList
		}, createCopyChb ]
	});
	var win = new Ext.Window( {
		defaults : {
			bodyStyle : "padding:10px 5px 5px 5px"
		},
		title : "Move or Copy".localize(),
		closable : true,
		closeAction : "close",
		autoDestroy : true,
		cls : "default-format-window",
		plain : true,
		constrain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 280,
		height : 330,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.sheetMoveCopy)
			}
		},
		buttons : [ {
			text : "OK".localize(),
			handler : function() {
				moveWorksheet();
				win.close()
			}
		}, {
			text : "Cancel".localize(),
			handler : function() {
				win.close()
			}
		} ]
	});
	win.show(this);
	toBookCmb.setValue(AWName)
};