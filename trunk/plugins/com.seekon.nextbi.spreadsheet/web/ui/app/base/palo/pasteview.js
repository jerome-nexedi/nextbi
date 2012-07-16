Jedox.wss.palo.PasteView = function(pasteViewId, x, y) {
	pasteViewId = ((pasteViewId == 0) ? 0 : (pasteViewId || false));
	x = ((x == 0) ? 0 : (x || false));
	y = ((y == 0) ? 0 : (y || false));
	var phpPaloServerCbHandlers = {
		getServList : function(result) {
			if (result[0]) {
				storeServDb.loadData(result[1]);
				if (result[2]) {
					preselectedServDb = result[2]
				}
				_init();
				if (editMode) {
					phpPaloServer.getPasteViewInitData(editPasteViewId);
					editData.length = 0
				} else {
					_refillComboBox();
					_show()
				}
			} else {
				_showErrorMsg(result[1])
			}
		},
		getPasteViewInitData : function(result) {
			if (result[0]) {
				editData = result[1];
				_refillComboBox();
				_show()
			} else {
				_showErrorMsg(result[1].localize())
			}
		},
		getDBs : function(result) {
			if (result[0]) {
				var tmpIndex = storeServDb.find("id", result[1][0]);
				var tmpRec;
				if (result[1].length > 0) {
					tmpRec = new ServDbRecord( {
						id : "id-" + result[1][0] + "-system",
						parent_id : result[1][0],
						type : "database",
						name : "System"
					});
					storeServDb.insert(tmpIndex + 1, tmpRec)
				}
				for ( var j = (result[1].length - 1); j > 0; j--) {
					tmpRec = new ServDbRecord( {
						id : "id-" + result[1][0] + "-" + j,
						parent_id : result[1][0],
						type : "database",
						name : result[1][j]
					});
					storeServDb.insert(tmpIndex + 1, tmpRec)
				}
			} else {
				_showErrorMsg(result[1], result[2])
			}
			numOfRequests -= (numOfRequests == 0) ? 0 : 1;
			if (preselectedServDb && (numOfRequests == 0) && (cmbDbState == -1)) {
				cmbDbState = storeServDb
						.findBy(function(rec, id) {
							return (rec.get("name") == preselectedServDb[1]
									&& rec.get("parent_id") == preselectedServDb[0] && rec
									.get("type") == "database")
						})
			}
			if ((numOfRequests == 0) && (cmbDbState == -1)) {
				cmbDbState = storeServDb.find("type", "database")
			}
			if (cmbDbState != -1) {
				tmpIndex = cmbDbState;
				cmbDbState = -1;
				if (editMode && (editData.length > 0)) {
					tmpIndex = storeServDb.find("name", editData[0][3])
				}
				cmbDb.setValue(storeServDb.getAt(tmpIndex).get("id"));
				_cmbDbSelectionHandler(cmbDb, storeServDb.getAt(tmpIndex),
						tmpIndex)
			}
		},
		getCubeNames : function(result) {
			if (!result[2]) {
				storeCubes.removeAll();
				cmbCube.setValue("");
				var tmpRec;
				for ( var i = 0; i < result[1].length; i++) {
					tmpRec = new CubeRecord( {
						name : result[1][i]
					});
					storeCubes.add(tmpRec)
				}
				cmbCubeState = -1;
				if (result[1].length > 0) {
					var tmpIndex;
					if (editMode && (editData.length > 0)) {
						tmpIndex = storeCubes.find("name", editData[0][4])
					}
					tmpIndex = (tmpIndex) ? tmpIndex : 0;
					cmbCube.setValue(storeCubes.getAt(tmpIndex).get("name"));
					_cmbCubeSelectionHandler(cmbCube, storeCubes
							.getAt(tmpIndex), tmpIndex)
				} else {
					storeDims.removeAll();
					_removeAllDimsFromPanel(panelPage);
					_removeAllDimsFromPanel(panelRow);
					_removeAllDimsFromPanel(panelColumn)
				}
			} else {
				_showErrorMsg(result[3])
			}
		},
		getCubeDims : function(result) {
			if (!result[2]) {
				var tmpRec;
				storeDims.removeAll();
				_removeAllDimsFromPanel(panelPage);
				_removeAllDimsFromPanel(panelRow);
				_removeAllDimsFromPanel(panelColumn);
				if (editMode && (editData.length > 0)) {
					for ( var i = 1; i < editData.length; i++) {
						for ( var j = 0; j < editData[i].length; j++) {
							if (i == 1) {
								_addDdPanel(editData[i][j][0], panelPage,
										editData[i][j][1])
							} else {
								if (i == 2) {
									_addDdPanel(editData[i][j][0], panelColumn,
											editData[i][j][1])
								} else {
									if (i == 3) {
										_addDdPanel(editData[i][j][0],
												panelRow, editData[i][j][1])
									}
								}
							}
						}
					}
				} else {
					if (dataMode == 2) {
						_addDdPanel(result[1][0], panelColumn, null);
						_addDdPanel(result[1][1], panelRow, null)
					} else {
						for ( var i = 0; i < result[1].length; i++) {
							_addDdPanel(result[1][i], panelPage, null)
						}
					}
				}
			} else {
				_showErrorMsg(result[3])
			}
		}
	};
	var _config = Jedox.wss.palo.config;
	var that = this;
	var phpPaloServer = new Palo(phpPaloServerCbHandlers);
	var servId, dbName, preselectedServDb = null;
	var win, panelMain, panelUpL, panelUpR, panelDownL, panelDownR, panelcmbDb, panelcmbCube, panelchbIndent, panelchbWl, panelchbFw, paneltxtFw, panelbtnClose, panelbtnPaste, panelcmbPf, panelbtnsChooseCubeType, panelchbMorePasteViews;
	var panelPage, panelColumn, panelRow;
	var lblServDbCombo, lblCubeCombo, cmbDb, cmbCube, cmbPf, chbIndent, chbWl, chbFw, chbMorePasteViews, btnClose, btnPaste, btnsChooseCubeType, txtFw;
	var cmbDbState, cmbCubeState, numOfRequests, cmbPfState, dataMode, editMode, editPasteViewId, editX, editY, editData = [];
	var dragdropPanels = [];
	var ServDbRecord = new Ext.data.Record.create( [ {
		name : "id"
	}, {
		name : "parent_id"
	}, {
		name : "connected"
	}, {
		name : "type"
	}, {
		name : "name"
	}, {
		name : "host"
	}, {
		name : "port"
	}, {
		name : "username"
	}, {
		name : "password"
	} ]);
	var storeServDb = new Ext.data.SimpleStore( {
		fields : [ {
			name : "id"
		}, {
			name : "parent_id"
		}, {
			name : "connected"
		}, {
			name : "type"
		}, {
			name : "name"
		}, {
			name : "host"
		}, {
			name : "port"
		}, {
			name : "username"
		}, {
			name : "password"
		} ]
	});
	var CubeRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var storeCubes = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	var storePf = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	var DimRecord = new Ext.data.Record.create( [ {
		name : "name"
	}, {
		name : "parentPanel"
	}, {
		name : "elements"
	} ]);
	var storeDims = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		}, {
			name : "parentPanel"
		}, {
			name : "elements"
		} ]
	});
	this.init = function(inPasteViewId, inEditX, inEditY) {
		cmbDbState = -1;
		dataMode = 0;
		cmbPfState = 0;
		numOfRequests = 0;
		editMode = false;
		editPasteViewId = inPasteViewId;
		editX = inEditX;
		editY = inEditY;
		if (arguments.length > 0) {
			editMode = true;
			editData = arguments
		}
		var paloFnList = [ [ "PALO.DATA" ], [ "PALO.DATAC" ], [ "PALO.DATAV" ] ];
		storePf.loadData(paloFnList);
		phpPaloServer.getServList()
	};
	var _init = function() {
		lblServDbCombo = new Ext.form.MiscField( {
			value : "Choose Server/Database".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		lblCubeCombo = new Ext.form.MiscField( {
			value : "Choose Cube".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		cmbDb = new Ext.form.ComboBox(
				{
					store : storeServDb,
					bodyStyle : "background-color: transparent;",
					typeAhead : true,
					selectOnFocus : true,
					hideLabel : true,
					editable : false,
					forceSelection : true,
					triggerAction : "all",
					mode : "local",
					tpl : new Ext.XTemplate(
							'<tpl for=".">',
							'<div class="x-combo-list-item">',
							"<tpl if=\"type == 'database'\">",
							'<span style="cursor: default;">&#160;&#160;&#160;{name}</span>',
							"</tpl>", "<tpl if=\"type == 'server'\">",
							'<span style="cursor: default;">{name}</span>',
							"</tpl>", "</div>", "</tpl>"),
					listeners : {
						select : _cmbDbSelectionHandler
					},
					valueField : "id",
					displayField : "name"
				});
		panelcmbDb = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ cmbDb ]
		});
		cmbCube = new Ext.form.ComboBox( {
			store : storeCubes,
			bodyStyle : "background-color: transparent;",
			typeAhead : true,
			selectOnFocus : true,
			hideLabel : true,
			editable : false,
			forceSelection : true,
			triggerAction : "all",
			mode : "local",
			listeners : {
				select : _cmbCubeSelectionHandler
			},
			valueField : "name",
			displayField : "name"
		});
		panelcmbCube = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ cmbCube ]
		});
		cmbPf = new Ext.form.ComboBox( {
			disabled : true,
			store : storePf,
			bodyStyle : "background-color: transparent;",
			typeAhead : true,
			selectOnFocus : true,
			hideLabel : true,
			editable : false,
			forceSelection : true,
			triggerAction : "all",
			mode : "local",
			valueField : "name",
			displayField : "name"
		});
		cmbPf.setValue(storePf.getAt(cmbPfState).get("name"));
		panelcmbPf = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ cmbPf ]
		});
		txtFw = new Ext.form.TextField( {
			hideLabel : true,
			value : _config.fixWidth
		});
		paneltxtFw = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ txtFw ]
		});
		chbIndent = new Ext.form.Checkbox( {
			checked : true,
			hideLabel : true,
			boxLabel : "Indent".localize()
		});
		panelchbIndent = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ chbIndent ]
		});
		chbWl = new Ext.form.Checkbox( {
			hideLabel : true,
			checked : true,
			boxLabel : "Wrap Labels".localize()
		});
		panelchbWl = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ chbWl ]
		});
		chbFw = new Ext.form.Checkbox( {
			hideLabel : true,
			checked : true,
			boxLabel : "Fixed width".localize() + ":",
			listeners : {
				check : function() {
					if (chbFw.getValue()) {
						txtFw.enable()
					} else {
						txtFw.disable()
					}
				}
			}
		});
		panelchbFw = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ chbFw ]
		});
		chbMorePasteViews = new Ext.form.Checkbox( {
			hideLabel : true,
			boxLabel : "Paste at selected cell".localize()
		});
		panelchbMorePasteViews = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ chbMorePasteViews ]
		});
		panelPage = new Ext.Panel( {
			id : "pv_panelPage",
			layout : "accordion",
			autoScroll : true
		});
		dragdropPanels.pv_panelPage = panelPage;
		panelColumn = new Ext.Panel( {
			id : "pv_panelColumn",
			layout : "accordion",
			autoScroll : true
		});
		dragdropPanels.pv_panelColumn = panelColumn;
		panelRow = new Ext.Panel( {
			id : "pv_panelRow",
			layout : "accordion",
			autoScroll : true
		});
		dragdropPanels.pv_panelRow = panelRow;
		panelUpL = new Ext.Panel( {
			title : "Page selector".localize(),
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			items : [ panelPage ]
		});
		panelUpR = new Ext.Panel( {
			title : "Column titles".localize(),
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			items : [ panelColumn, panelchbWl, panelchbFw, paneltxtFw ]
		});
		panelDownL = new Ext.Panel( {
			title : "Row titles".localize(),
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			items : [ panelRow, panelchbIndent ]
		});
		panelDownR = new Ext.Panel( {
			title : "Data".localize(),
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			items : [ panelcmbPf, panelchbMorePasteViews ]
		});
		btnPaste = new Ext.Button(
				{
					text : "Paste".localize(),
					listeners : {
						click : function() {
							var pPage = [];
							var pColumn = [];
							var pRow = [];
							storeDims
									.each(function(inRec) {
										if (inRec.get("parentPanel").id == "pv_panelPage") {
											pPage[pPage.length] = [
													inRec.get("name"),
													inRec.get("elements") ]
										} else {
											if (inRec.get("parentPanel").id == "pv_panelColumn") {
												pColumn[pColumn.length] = [
														inRec.get("name"),
														inRec.get("elements") ]
											} else {
												if (inRec.get("parentPanel").id == "pv_panelRow") {
													pRow[pRow.length] = [
															inRec.get("name"),
															inRec
																	.get("elements") ]
												}
											}
										}
									});
							Ext.MessageBox.show( {
								title : "Please wait".localize(),
								msg : "<b><br>" + "Obtaining data!".localize()
										+ "</b>",
								closable : false,
								icon : "largeLoadingImage"
							});
							if (Jedox.wss.app.activeBook) {
								var env = Jedox.wss.app.environment;
								var activeBook = Jedox.wss.app.activeBook;
								var upperLeftCoords = env.defaultSelection
										.getActiveRange().getUpperLeft();
								var editSettingsData = [ editPasteViewId,
										editX, editY ];
								var settings = [
										upperLeftCoords.getX(),
										upperLeftCoords.getY(),
										servId,
										dbName,
										cmbCube.getValue(),
										chbWl.getValue(),
										((chbFw.getValue()) ? parseFloat(txtFw
												.getValue()) : -1),
										chbIndent.getValue(),
										chbMorePasteViews.getValue(), editMode,
										editSettingsData ];
								setTimeout(
										function() {
											activeBook.cb(
													"palo_handlerPasteView",
													[ [ settings, pPage,
															pColumn, pRow ] ]);
											Ext.MessageBox.hide()
										}, 0)
							} else {
								var editSettingsData = [ editPasteViewId,
										editX, editY ];
								var settings = [
										1,
										1,
										servId,
										dbName,
										cmbCube.getValue(),
										chbWl.getValue(),
										((chbFw.getValue()) ? parseFloat(txtFw
												.getValue()) : -1),
										chbIndent.getValue(),
										chbMorePasteViews.getValue(), editMode,
										editSettingsData ];
								phpPaloServer.handlerPasteView( [ settings,
										pPage, pColumn, pRow ]);
								Ext.MessageBox.hide()
							}
							win.close()
						}
					}
				});
		btnClose = new Ext.Button( {
			text : "Close".localize(),
			listeners : {
				click : function() {
					win.close()
				}
			}
		});
		panelbtnPaste = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			buttonAlign : "left",
			buttons : [ btnPaste ]
		});
		panelbtnClose = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			buttonAlign : "left",
			buttons : [ btnClose ]
		});
		btnsChooseCubeType = {
			metaDataB : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				iconCls : "icon_palo_cube",
				handler : function() {
					_setDataMode(0);
					phpPaloServer.getCubeNames(servId, dbName, dataMode)
				}
			}),
			attribsB : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				iconCls : "icon_palo_table",
				handler : function() {
					_setDataMode(2);
					phpPaloServer.getCubeNames(servId, dbName, dataMode, true)
				}
			}),
			usersB : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				iconCls : "icon_palo_user",
				handler : function() {
					_setDataMode(1);
					phpPaloServer.getCubeNames(servId, dbName, dataMode)
				}
			})
		};
		panelbtnsChooseCubeType = new Ext.Panel(
				{
					border : false,
					bodyStyle : "background-color: transparent; vertical-align: middle;",
					layout : "table",
					width : 85,
					height : 40,
					autoHeight : true,
					items : [ btnsChooseCubeType.metaDataB,
							btnsChooseCubeType.attribsB,
							btnsChooseCubeType.usersB ]
				});
		panelMain = new Ext.Panel( {
			id : "pv_mainPanel",
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			anchor : "100% 100%",
			monitorResize : true,
			listeners : {
				resize : _resizeAll
			},
			items : [ lblServDbCombo, lblCubeCombo, panelcmbDb, panelcmbCube,
					panelUpL, panelUpR, panelDownL, panelDownR, panelbtnClose,
					panelbtnPaste, panelbtnsChooseCubeType ]
		});
		win = new Ext.Window(
				{
					id : "pasteViewWindow",
					layout : "fit",
					cls : "default-format-window",
					width : _config.pvWinW,
					height : _config.pvWinH,
					minWidth : _config.pvWinW,
					minHeight : _config.pvWinH,
					closeAction : "close",
					autoDestroy : true,
					plain : true,
					modal : true,
					resizable : false,
					title : "Paste View".localize(),
					listeners : {
						close : function() {
							Jedox.wss.general
									.setInputMode(Jedox.wss.app.lastInputModeDlg);
							Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.pasteView);
							phpPaloServer.setPreselectServDb(servId, dbName)
						}
					},
					items : [ panelMain ]
				})
	};
	var _setSelectedElements = function(dim, elems) {
		if (elems.length >= 0) {
			storeDims.getAt(storeDims.find("name", dim)).set("elements", elems)
		}
	};
	this.setSelectedElements = function(dim, elems) {
		_setSelectedElements(dim, elems)
	};
	var _show = function() {
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
		if (editMode && (editData.length > 0)) {
			chbWl.setValue(editData[0][5]);
			if (editData[0][6] != -1) {
				txtFw.setValue(editData[0][6])
			} else {
				chbFw.setValue(false)
			}
			chbIndent.setValue(editData[0][7]);
			chbMorePasteViews.setValue(editData[0][8])
		}
		win.show();
		_resizeAll()
	};
	var _onDragDrop = function(e, id) {
		var tmpRecDim = storeDims.getAt(storeDims
				.find("name", this.panel.title));
		if (tmpRecDim.get("parentPanel").id != id) {
			var tmpToPanel = dragdropPanels[id];
			if (tmpToPanel) {
				if ((this.panel.title == "pv_panelPage")
						|| (id == "pv_panelPage")) {
					var elems = null
				} else {
					var elems = tmpRecDim.get("elements")
				}
				tmpRecDim.get("parentPanel").remove(this.panel);
				storeDims.remove(tmpRecDim);
				_addDdPanel(this.panel.title, tmpToPanel, elems)
			}
		} else {
			var panelFound = false, tmpPan, tmpSubP, tmpPos, parentP = tmpRecDim
					.get("parentPanel");
			for (i = 0; i < parentP.items.length; i++) {
				tmpSubP = parentP.getComponent(i);
				tmpPos = tmpSubP.getPosition();
				if ((tmpPos[0] < e.getPageX())
						&& (tmpPos[1] < e.getPageY())
						&& ((tmpPos[0] + tmpSubP.getInnerWidth()) > e
								.getPageX())
						&& ((tmpPos[1] + tmpSubP.getFrameHeight()) > e
								.getPageY())) {
					tmpPan = new Ext.Panel( {
						title : this.panel.title,
						border : false,
						collapsed : true,
						draggable : true,
						autoHeight : true
					});
					parentP.insert(i, tmpPan);
					tmpPan.add(_makeTreePanel(this.panel.title, parentP.id));
					parentP.remove(this.panel);
					storeDims.remove(tmpRecDim);
					storeDims.insert(storeDims.find("name", tmpSubP.title),
							tmpRecDim);
					parentP.doLayout();
					tmpPan.dd.onDragDrop = _onDragDrop;
					panelFound = true;
					break
				}
			}
		}
	};
	var _cmbDbSelectionHandler = function(combo, record, index) {
		if (cmbDbState != index) {
			cmbDbState = index;
			if (record.get("type") == "database") {
				servId = record.get("parent_id");
				dbName = record.get("name");
				if (dbName == "System") {
					btnsChooseCubeType.metaDataB.disable();
					btnsChooseCubeType.attribsB.disable();
					btnsChooseCubeType.usersB.disable();
					phpPaloServer.getCubeNames(servId, dbName, 1, true)
				} else {
					_setDataMode(dataMode);
					phpPaloServer.getCubeNames(servId, dbName, dataMode)
				}
			}
		}
	};
	var _cmbCubeSelectionHandler = function(combo, record, index) {
		if (cmbCubeState != index) {
			cmbCubeState = index;
			phpPaloServer.getCubeDims(servId, dbName, record.get("name"))
		}
	};
	var _showWaitMsg = function(msgText, onBarText) {
		Ext.MessageBox.show( {
			msg : msgText,
			progressText : onBarText,
			width : 300,
			wait : true,
			waitConfig : {
				interval : 200
			}
		});
		timerOut = false;
		timer = setTimeout(function() {
			if (!timerOut) {
				timerOut = true;
				_hideWaitMsg()
			}
		}, timerOutTime)
	};
	var _hideWaitMsg = function() {
		Ext.MessageBox.hide();
		if (timerOut) {
			Ext.MessageBox.show( {
				title : "Error".localize(),
				msg : "_err: Timer".localize(),
				buttons : Ext.Msg.OK,
				icon : Ext.MessageBox.ERROR
			})
		}
	};
	var _showErrorMsg = function(message, props) {
		Ext.MessageBox.show( {
			title : "Error".localize(),
			msg : message.localize(props),
			buttons : Ext.Msg.OK,
			icon : Ext.MessageBox.ERROR
		})
	};
	var _refillComboBox = function() {
		var executed = false;
		for ( var i = 0; i < storeServDb.getCount(); i++) {
			tmpRec = storeServDb.getAt(i);
			if ((tmpRec.get("type") == "server")
					&& (tmpRec.get("connected") == 1)) {
				if (storeServDb.find("parent_id", tmpRec.get("id")) == -1) {
					executed = true;
					phpPaloServer.getDBs(tmpRec.get("id"));
					numOfRequests++
				}
			}
		}
		if (!executed && (cmbDbState == -1)) {
			var tmpIndex = 0;
			cmbDbState = -1;
			cmbDb.setValue(storeServDb.getAt(tmpIndex).get("id"));
			_cmbDbSelectionHandler(cmbDb, storeServDb.getAt(tmpIndex), tmpIndex)
		}
	};
	var _removeAllDimsFromPanel = function(panel) {
		for ( var i = (panel.items.length - 1); i >= 0; i--) {
			panel.remove(panel.getComponent(i))
		}
	};
	var _addDdPanel = function(pName, parentP, elems) {
		var tmpPan = new Ext.Panel( {
			title : pName,
			border : false,
			collapsed : true,
			draggable : true,
			autoHeight : true
		});
		parentP.add(tmpPan);
		elems = (elems) ? elems : null;
		storeDims.add(new DimRecord( {
			name : pName,
			parentPanel : parentP,
			elements : elems
		}));
		tmpPan.add(_makeTreePanel(pName, parentP.id));
		parentP.doLayout();
		tmpPan.dd.onDragDrop = _onDragDrop
	};
	var _makeTreePanel = function(dimName, panelId) {
		var tree = new Ext.tree.TreePanel( {
			cls : "x-tree-noicon",
			border : false,
			autoScroll : true,
			animate : true,
			enableDD : false,
			containerScroll : true,
			rootVisible : false
		});
		var root = new Ext.tree.TreeNode( {
			text : "Root",
			draggable : false,
			id : "root"
		});
		tree.setRootNode(root);
		var tmpNode = new Ext.tree.TreeNode( {
			text : ((panelId == "pv_panelPage") ? "Choose Element".localize()
					: "Select Elements".localize()),
			draggable : false,
			id : "title",
			listeners : {
				dblclick : function() {
					if (panelId == "pv_panelPage") {
						if (Jedox.wss.app.activeBook) {
							Jedox.wss.app.load(
									Jedox.wss.app.dynJSRegistry.chooseElement,
									[ {
										working_mode : 2,
										parent_object : {
											cb_fnc : _setSelectedElements
										},
										serv_id : servId,
										db_name : dbName,
										dim_name : dimName,
										edit_data : storeDims
												.getAt(
														storeDims.find("name",
																dimName)).get(
														"elements")
									} ])
						} else {
							Jedox.wss.palo.ChooseElement( {
								working_mode : 2,
								parent_object : {
									cb_fnc : _setSelectedElements
								},
								serv_id : servId,
								db_name : dbName,
								dim_name : dimName,
								edit_data : storeDims.getAt(
										storeDims.find("name", dimName)).get(
										"elements")
							})
						}
					} else {
						if (Jedox.wss.app.activeBook) {
							Jedox.wss.app.load(
									Jedox.wss.app.dynJSRegistry.selectElements,
									[
											that,
											servId,
											dbName,
											dimName,
											storeDims.getAt(
													storeDims.find("name",
															dimName)).get(
													"elements") ])
						} else {
							Jedox.wss.palo.SelectElements(that, servId, dbName,
									dimName, storeDims.getAt(
											storeDims.find("name", dimName))
											.get("elements"))
						}
					}
				}
			}
		});
		root.appendChild(tmpNode);
		return tree
	};
	var _setDataMode = function(newDataMode) {
		if (newDataMode == -1) {
			btnsChooseCubeType.metaDataB.disable();
			btnsChooseCubeType.attribsB.disable();
			btnsChooseCubeType.usersB.disable();
			return
		}
		if (newDataMode == 0) {
			btnsChooseCubeType.metaDataB.disable();
			btnsChooseCubeType.attribsB.enable();
			btnsChooseCubeType.usersB.enable()
		} else {
			if (newDataMode == 2) {
				btnsChooseCubeType.metaDataB.enable();
				btnsChooseCubeType.attribsB.disable();
				btnsChooseCubeType.usersB.enable()
			} else {
				if (newDataMode == 1) {
					btnsChooseCubeType.metaDataB.enable();
					btnsChooseCubeType.attribsB.enable();
					btnsChooseCubeType.usersB.disable()
				}
			}
		}
		dataMode = newDataMode
	};
	var _resizeAll = function() {
		if (panelMain.rendered) {
			var lineH = 25;
			var marginSize = 3;
			var btnPosAdjuster = 8;
			var w = panelMain.getSize().width;
			var h = panelMain.getSize().height;
			var innerPanelW = (w - marginSize * 3) / 2;
			var innerPanelH = (h - lineH * 2 - marginSize * 3) / 2;
			panelUpL.setSize(innerPanelW, innerPanelH);
			panelUpL.setPosition(marginSize, marginSize + lineH * 2);
			panelUpR.setSize(innerPanelW, innerPanelH);
			panelUpR.setPosition(innerPanelW + marginSize * 2, marginSize
					+ lineH * 2);
			panelDownL.setSize(innerPanelW, innerPanelH);
			panelDownL.setPosition(marginSize, innerPanelH + marginSize * 2
					+ lineH * 2);
			panelDownR.setSize(innerPanelW, innerPanelH - marginSize - lineH);
			panelDownR.setPosition(innerPanelW + marginSize * 2, innerPanelH
					+ marginSize * 2 + lineH * 2);
			lblServDbCombo.setPosition(marginSize, marginSize);
			lblCubeCombo.setPosition(innerPanelW + marginSize * 2, marginSize);
			panelcmbDb.setPosition(marginSize, lineH - marginSize);
			cmbDb.setWidth(innerPanelW);
			if (panelbtnClose.rendered && panelbtnPaste.rendered
					&& btnClose.rendered && btnPaste.rendered) {
				panelbtnClose.setPosition(w - btnClose.getEl().getBox().width
						- marginSize - btnPosAdjuster, h - lineH
						- btnPosAdjuster);
				panelbtnPaste.setPosition(w - btnClose.getEl().getBox().width
						- btnPaste.getEl().getBox().width - marginSize * 2
						- btnPosAdjuster, h - lineH - btnPosAdjuster)
			}
			if (panelbtnsChooseCubeType.rendered) {
				panelbtnsChooseCubeType.setPosition(w - marginSize
						- panelbtnsChooseCubeType.getSize().width, lineH
						- marginSize);
				panelcmbCube.setPosition(innerPanelW + marginSize * 2, lineH
						- marginSize);
				cmbCube.setWidth(innerPanelW
						- panelbtnsChooseCubeType.getSize().width - marginSize)
			}
			panelchbIndent.setPosition(marginSize * 2, innerPanelH - lineH * 2);
			panelchbWl.setPosition(marginSize * 2, innerPanelH - lineH * 2);
			if (panelchbWl.rendered) {
				panelchbFw.setPosition(marginSize * 3
						+ panelchbWl.getSize().width + 15, innerPanelH - lineH
						* 2);
				if (panelchbFw.rendered) {
					paneltxtFw.setPosition(marginSize * 4
							+ panelchbWl.getSize().width
							+ panelchbFw.getSize().width + 15, innerPanelH
							- lineH * 2 - marginSize);
					txtFw.setWidth(innerPanelW
							- (marginSize * 6 + panelchbWl.getSize().width
									+ panelchbFw.getSize().width + 15))
				}
			}
			panelchbMorePasteViews.setPosition(marginSize * 2, innerPanelH
					- lineH * 3 - marginSize);
			panelcmbPf.setPosition(marginSize, marginSize);
			panelPage.setSize(innerPanelW - marginSize * 3, innerPanelH - lineH
					* 2 - marginSize * 3);
			panelPage.setPosition(marginSize, marginSize);
			panelColumn.setSize(innerPanelW - marginSize * 3, innerPanelH
					- lineH * 2 - marginSize * 3);
			panelColumn.setPosition(marginSize, marginSize);
			panelRow.setSize(innerPanelW - marginSize * 3, innerPanelH - lineH
					* 2 - marginSize * 3);
			panelRow.setPosition(marginSize, marginSize);
			if (panelPage.rendered && panelRow.rendered && panelColumn.rendered) {
				var tmpVar = new Ext.dd.DragDrop("pv_panelPage");
				tmpVar = new Ext.dd.DragDrop("pv_panelRow");
				tmpVar = new Ext.dd.DragDrop("pv_panelColumn")
			}
		}
	};
	if (pasteViewId || x || y) {
		this.init(pasteViewId, x, y)
	} else {
		this.init()
	}
};