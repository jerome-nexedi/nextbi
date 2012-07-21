Jedox.wss.dialog.openNameManager = function(fromHL, preselection) {
	if (!fromHL) {
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
	}
	var _selectedIndex;
	var selectedRange = "";
	var selectedScope = "";
	var namesRecords = [];
	var namesScopedToWorksheetFlag = false;
	var namesScopedToWorkbookFlag = false;
	var namesWithErrorsFlag = false;
	var namesWithoutErrorsFlag = false;
	var AttrRecord = new Ext.data.Record.create( [ {
		name : "name"
	}, {
		name : "value"
	}, {
		name : "refersTo"
	}, {
		name : "scope"
	}, {
		name : "comment"
	} ]);
	var nameManagerStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		}, {
			name : "value"
		}, {
			name : "refersTo"
		}, {
			name : "scope"
		}, {
			name : "comment"
		}, {
			name : "uuid"
		} ]
	});
	initNames();
	var mainDV = new Ext.grid.GridPanel( {
		id : "main-details-view",
		colModel : new Ext.grid.ColumnModel( [ {
			header : "Name".localize(),
			width : 30,
			sortable : true
		}, {
			header : "Value".localize(),
			width : 20,
			sortable : true
		}, {
			header : "Refers To".localize(),
			width : 20,
			sortable : true
		}, {
			header : "Scope".localize(),
			width : 20,
			sortable : true
		}, {
			header : "Comment".localize(),
			width : 20,
			sortable : true
		} ]),
		store : nameManagerStore,
		viewConfig : {
			forceFit : true
		},
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : true
		}),
		width : 475,
		height : 250,
		border : false,
		iconCls : "icon-grid",
		listeners : {
			rowclick : {
				fn : function(gridView, index, e) {
					_selectedIndex = index;
					toggleButtonsState(true);
					setRefersTo();
					toggleRefersToButtonsState(true)
				}
			}
		}
	});
	var saveRangeBtn = new Ext.Toolbar.Button( {
		iconCls : "icon_save_doc",
		cls : "x-btn-icon",
		tooltip : "Save Range".localize(),
		disabled : true,
		handler : setNewRange
	});
	var cancelRangeBtn = new Ext.Toolbar.Button( {
		iconCls : "icon_cancel",
		cls : "x-btn-icon",
		tooltip : "Cancel".localize(),
		disabled : true,
		handler : toggleRefersToButtonsState
	});
	var refersToTxf = new Ext.form.TextField( {
		hideLabel : true,
		disabled : true,
		value : "",
		width : 375
	});
	var selectRangeBtn = new Ext.Toolbar.Button( {
		iconCls : "select-range-icon",
		cls : "x-btn-icon",
		tooltip : "Select Range".localize(),
		disabled : true,
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRangeHandleFnc ],
				rng : refersToTxf.getValue()
			} ]);
			win.hide()
		}
	});
	var selRangeHandleFnc = function(selected) {
		var record = nameManagerStore.getAt(_selectedIndex);
		selectedScope = (record.get("scope") === "Workbook" || record
				.get("scope") === "Arbeitsmappe") ? "" : record.get("scope");
		win.show();
		refersToTxf.setValue(selected)
	};
	function initNames() {
		var position = Jedox.wss.app.environment.defaultSelection
				.getActiveRange().getActiveCell();
		var tmpResponse = Jedox.wss.backend.conn.cmd(0, [ "nlst" ], [ [
				position._x, position._y ] ]);
		var names = tmpResponse[0][1][0];
		var tmpRec;
		for ( var i = 0; i < names.length; i++) {
			tmpRec = new AttrRecord( {
				name : names[i]["name"],
				value : names[i]["value"],
				refersTo : names[i]["refers_to"],
				scope : names[i]["scope"],
				comment : names[i]["comment"],
				uuid : names[i]["uuid"]
			});
			nameManagerStore.add(tmpRec)
		}
	}
	var newNameHandleFnc = function(newName) {
		if (newName) {
			var position = Jedox.wss.app.environment.defaultSelection
					.getActiveRange().getActiveCell();
			var tmpScope = (newName[3] === "Workbook" || newName[3] === "Arbeitsmappe") ? ""
					: newName[3];
			var tmpResponse = Jedox.wss.backend.conn.cmd(0, [ "nadd" ], [ [
					position._x, position._y, {
						name : newName[0],
						refers_to : newName[2],
						scope : tmpScope,
						comment : newName[4]
					} ] ]);
			if (tmpResponse[0][0]) {
				var tmpRec = new AttrRecord( {
					name : newName[0],
					value : tmpResponse[0][1][0]["value"],
					refersTo : newName[2],
					scope : newName[3],
					comment : newName[4],
					uuid : tmpResponse[0][1][0]["uuid"]
				});
				nameManagerStore.add(tmpRec);
				win.show()
			} else {
				showWarrningMessage()
			}
		} else {
			win.show()
		}
	};
	function showWarrningMessage() {
		Ext.Msg.show( {
			title : "New Name".localize() + "?",
			msg : "Named formula couldn't be created".localize() + ".",
			buttons : Ext.Msg.OK,
			fn : function() {
				win.show()
			},
			animEl : "elId",
			width : 320,
			icon : Ext.MessageBox.ERROR
		})
	}
	var editNameHandleFnc = function(editedName) {
		if (editedName) {
			var record = nameManagerStore.getAt(_selectedIndex);
			var position = Jedox.wss.app.environment.defaultSelection
					.getActiveRange().getActiveCell();
			var tmpScope = (editedName[3] === "Workbook" || editedName[3] === "Arbeitsmappe") ? ""
					: editedName[3];
			var tmpResponse = Jedox.wss.backend.conn.cmd(0, [ "nupd" ], [ [
					record.get("uuid"), position._x, position._y, {
						name : editedName[0],
						refers_to : editedName[2],
						scope : tmpScope,
						comment : editedName[4]
					} ] ]);
			if (tmpResponse && tmpResponse[0] && tmpResponse[0][0]) {
				var record = nameManagerStore.getAt(_selectedIndex);
				record.set("name", editedName[0]);
				record.set("value", tmpResponse[0][1][0]["value"]);
				record.set("refersTo", editedName[2]);
				record.set("comment", editedName[4])
			} else {
				showWarrningMessage()
			}
		}
		win.show()
	};
	var removeName = function() {
		var record = nameManagerStore.getAt(_selectedIndex);
		Jedox.wss.backend.conn.cmd(0, [ "ndel" ], [ record.get("uuid") ]);
		nameManagerStore.remove(nameManagerStore.getAt(_selectedIndex));
		toggleButtonsState()
	};
	function toggleButtonsState(enable) {
		if (enable) {
			Ext.getCmp("edit-btn").enable();
			Ext.getCmp("delete-btn").enable();
			return
		}
		Ext.getCmp("edit-btn").disable();
		Ext.getCmp("delete-btn").disable()
	}
	function toggleRefersToButtonsState(enable) {
		if (enable) {
			saveRangeBtn.enable();
			cancelRangeBtn.enable();
			selectRangeBtn.enable();
			refersToTxf.enable();
			return
		}
		saveRangeBtn.disable();
		cancelRangeBtn.disable();
		selectRangeBtn.disable();
		refersToTxf.setValue("");
		refersToTxf.disable()
	}
	function setRefersTo() {
		var record = nameManagerStore.getAt(_selectedIndex);
		var s = record.get("refersTo");
		refersToTxf.setValue(s)
	}
	function setNewRange() {
		var position = Jedox.wss.app.environment.defaultSelection
				.getActiveRange().getActiveCell();
		var record = nameManagerStore.getAt(_selectedIndex);
		var tmpScope = (record.get("scope") === "Workbook" || record
				.get("scope") === "Arbeitsmappe") ? "" : record.get("scope");
		var tmpResponse = Jedox.wss.backend.conn.cmd(0, [ "nupd" ], [ [
				record.get("uuid"), position._x, position._y, {
					scope : tmpScope,
					refers_to : refersToTxf.getValue()
				} ] ]);
		record.set("value", tmpResponse[0][1][0]["value"]);
		record.set("refersTo", refersToTxf.getValue());
		toggleRefersToButtonsState()
	}
	function tmpFormatRange(sr) {
		var tmp = sr.split(":");
		var formatedRange = "";
		for ( var i = 0; i < tmp.length; i++) {
			if (i > 0) {
				formatedRange += ":"
			}
			for ( var j = 0; j < tmp[i].length; j++) {
				if (j == 0
						|| (parseInt(tmp[i].charAt(j)) && !(parseInt(tmp[i]
								.charAt(j - 1))))) {
					formatedRange += "$" + tmp[i].charAt(j)
				} else {
					formatedRange += tmp[i].charAt(j)
				}
			}
		}
		return formatedRange
	}
	var _errCONST = {
		"#NULL" : true,
		"#DIV/0!" : true,
		"#VALUE!" : true,
		"#REF!" : true,
		"#NAME?" : true,
		"#NUM!" : true,
		"#N/A" : true,
		"#INSUFFICIENT_RIGHTS" : true,
		"#MAX_ITERATIONS_EXCEEDED" : true,
		"#EXTENSION_ERROR" : true
	};
	function filterFunc(record, id) {
		var retVal = true;
		if (namesScopedToWorksheetFlag) {
			retVal = (record.get("scope") !== "Workbook" || record.get("scope") === "Arbeitsmappe") ? true
					: false
		} else {
			if (namesScopedToWorkbookFlag) {
				retVal = (record.get("scope") === "Workbook" || record
						.get("scope") === "Arbeitsmappe") ? true : false
			}
		}
		if (retVal) {
			if (namesWithErrorsFlag) {
				retVal = (record.get("value") in _errCONST || record
						.get("refersTo") in _errCONST) ? true : false
			} else {
				if (namesWithoutErrorsFlag) {
					retVal = (record.get("value") in _errCONST || record
							.get("refersTo") in _errCONST) ? false : true
				}
			}
		}
		return retVal
	}
	function clearCheckedMenu() {
		var filterMenu = Ext.getCmp("filter-btn").menu.items.items;
		for ( var i = 2; i < filterMenu.length; i++) {
			if (i !== 4 && i !== 7) {
				filterMenu[i].setChecked(false)
			}
		}
		namesScopedToWorksheetFlag = false;
		namesScopedToWorkbookFlag = false;
		namesWithErrorsFlag = false;
		namesWithoutErrorsFlag = false;
		nameManagerStore.clearFilter(false)
	}
	var mainPanel = new Ext.Panel(
			{
				layout : "absolute",
				baseCls : "x-plain",
				items : [
						{
							layout : "column",
							border : false,
							baseCls : "main-panel",
							width : 500,
							height : 40,
							x : 0,
							y : 0,
							items : [
									{
										layout : "form",
										border : false,
										baseCls : "top-left-bottom-panel",
										width : 250,
										height : 30,
										buttons : [
												{
													text : "New".localize()
															.concat("..."),
													handler : function() {
														Jedox.wss.app
																.load(
																		Jedox.wss.app.dynJSRegistry.newName,
																		[
																				newNameHandleFnc,
																				"",
																				nameManagerStore ]);
														win.hide()
													}
												},
												{
													id : "edit-btn",
													text : "Edit".localize(),
													disabled : true,
													handler : function() {
														Jedox.wss.app
																.load(
																		Jedox.wss.app.dynJSRegistry.newName,
																		[
																				editNameHandleFnc,
																				nameManagerStore
																						.getAt(_selectedIndex),
																				nameManagerStore ]);
														win.hide()
													}
												}, {
													id : "delete-btn",
													text : "Delete".localize(),
													disabled : true,
													handler : removeName
												} ]
									},
									{
										layout : "form",
										border : false,
										baseCls : "toolbar-panel",
										width : 235,
										height : 30,
										buttons : [ {
											id : "filter-btn",
											text : "Filter".localize(),
											split : true,
											menu : {
												id : "filter-menu",
												cls : "view-menu",
												width : 220,
												items : [
														{
															text : "Clear"
																	.localize(),
															handler : clearCheckedMenu
														},
														"-",
														{
															text : "Names Scoped to Worksheet"
																	.localize(),
															checked : false,
															group : "names-group",
															handler : function() {
																namesScopedToWorksheetFlag = true;
																namesScopedToWorkbookFlag = false;
																nameManagerStore
																		.filterBy(filterFunc)
															}
														},
														{
															text : "Names Scoped to Workbook"
																	.localize(),
															checked : false,
															group : "names-group",
															handler : function() {
																namesScopedToWorksheetFlag = false;
																namesScopedToWorkbookFlag = true;
																nameManagerStore
																		.filterBy(filterFunc)
															}
														},
														"-",
														{
															text : "Names With Errors"
																	.localize(),
															checked : false,
															group : "errors-group",
															handler : function() {
																namesWithErrorsFlag = true;
																namesWithoutErrorsFlag = false;
																nameManagerStore
																		.filterBy(filterFunc)
															}
														},
														{
															text : "Names Without Errors"
																	.localize(),
															checked : false,
															group : "errors-group",
															handler : function() {
																namesWithErrorsFlag = false;
																namesWithoutErrorsFlag = true;
																nameManagerStore
																		.filterBy(filterFunc)
															}
														} ]
											}
										} ]
									} ]
						}, {
							layout : "fit",
							defaults : {
								bodyStyle : "padding:0px"
							},
							width : 475,
							height : 250,
							autoScroll : true,
							x : 5,
							y : 40,
							items : mainDV
						}, {
							html : "Refers To".localize() + ":",
							baseCls : "x-plain",
							x : 5,
							y : 300
						}, {
							layout : "column",
							border : false,
							baseCls : "top-left-bottom-panel",
							width : 500,
							height : 30,
							x : 5,
							y : 320,
							items : [ {
								layout : "form",
								border : false,
								width : 30,
								baseCls : "main-panel",
								items : cancelRangeBtn
							}, {
								layout : "form",
								border : false,
								width : 30,
								baseCls : "main-panel",
								items : saveRangeBtn
							}, {
								layout : "form",
								border : false,
								width : 380,
								baseCls : "main-panel",
								items : refersToTxf
							}, {
								layout : "form",
								border : false,
								width : 30,
								baseCls : "main-panel",
								items : selectRangeBtn
							} ]
						} ]
			});
	var win = new Ext.Window( {
		id : "name-manager-dlg",
		title : "Name Manager".localize(),
		closable : true,
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		constrain : true,
		cls : "default-format-window",
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 500,
		height : 430,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.nameManager)
			}
		},
		buttons : [ {
			text : "Close".localize(),
			handler : function() {
				win.close()
			}
		} ]
	});
	if (fromHL) {
		mainDV.autoHeight = false;
		mainDV.height = 250;
		mainDV.enableHdMenu = false;
		fromHL(mainDV, preselection)
	} else {
		win.show()
	}
};