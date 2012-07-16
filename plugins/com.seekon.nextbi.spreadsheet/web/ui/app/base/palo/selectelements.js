Jedox.wss.palo.SelectElements = function(pv, servT, dbT, dimT, inEditDataT) {
	var phpPaloServerCbHandlersForOtherElems = {
		getDims : function(result) {
			if (!result[2]) {
				storeDimNames.removeAll();
				for ( var tmpRec, i = 0; i < result[1].length; i++) {
					tmpRec = new DimNameRecord( {
						name : result[1][i]
					});
					storeDimNames.add(tmpRec)
				}
			} else {
				_showErrorMsg(result[3])
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
				if (storeCubes.getCount() > 0) {
					cmbCube.setValue(storeCubes.getAt(0).get("name"))
				} else {
					cmbCube.setValue("")
				}
			} else {
				_showErrorMsg(result[3])
			}
		}
	};
	var phpPaloServerCbHandlers = {
		getServList : function(result) {
			if (result[0]) {
				storeServDb.loadData(result[1]);
				if (result[2]) {
					preselectedServDb = result[2]
				}
				_refillcmbDb()
			} else {
				_showErrorMsg(result[1])
			}
		},
		getDBs : function(result) {
			if (result[0]) {
				var tmpIndex = storeServDb.find("id", result[1][0]);
				var tmpRec;
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
			if (servId) {
				if (storeDims.getCount() == 0) {
					var tmpId = storeServDb.findBy(function(rec, id) {
						return ((rec.get("parent_id") == servId) && (rec
								.get("name") == dbName))
					});
					if (tmpId >= 0) {
						cmbDbState = -1;
						cmbDb.setValue(storeServDb.getAt(tmpId).get("id"));
						_cmbDbSelectionHandler(cmbDb, storeServDb.getAt(tmpId),
								tmpId);
						cmbDb.disable()
					}
				}
			} else {
				numOfRequests -= (numOfRequests == 0) ? 0 : 1;
				if (preselectedServDb && (numOfRequests == 0)
						&& (cmbDbState == -1)) {
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
					cmbDb.setValue(storeServDb.getAt(tmpIndex).get("id"));
					_cmbDbSelectionHandler(cmbDb, storeServDb.getAt(tmpIndex),
							tmpIndex)
				}
			}
		},
		getDims : function(result) {
			if (!result[2]) {
				var tmpRec;
				_removeAllDimsFromPanel(panelDims);
				for ( var i = 0; i < result[1].length; i++) {
					_addDimPanel(result[1][i], ((i == 0) ? true : false))
				}
			} else {
				_showErrorMsg(result[3])
			}
		},
		getDimStringAttrs : function(result) {
			if (!result[2]) {
				storeAttrs.removeAll();
				var tmpRec;
				tmpRec = new AttrRecord( {
					name : "[none]".localize()
				});
				storeAttrs.add(tmpRec);
				for ( var i = 0; i < result[1].length; i++) {
					tmpRec = new AttrRecord( {
						name : result[1][i].name
					});
					storeAttrs.add(tmpRec)
				}
				var tmpAttrId = storeDims
						.getAt(storeDims.find("name", dimName)).get(
								"selectedAttr");
				cmbAttr.setValue(storeAttrs.getAt(tmpAttrId).get("name"))
			} else {
				_showErrorMsg(result[3])
			}
		},
		searchElem : function(result) {
			if (!result[2]) {
				if (result[0]) {
					treeMain.selectPath(result[1]);
					var tmpArr = result[1].split("/");
					selectedElem = tmpArr[tmpArr.length - 1]
				} else {
					_showErrorMsg("Element not found!".localize())
				}
			} else {
				_showErrorMsg(result[3])
			}
		},
		getAttributedElems : function(result) {
			if (!result[2]) {
				var tmpRec;
				storeListView.removeAll();
				if (result[1].length > 0) {
					for ( var i = 0; i < result[1].length; i++) {
						tmpRec = new ElemListRecord( {
							id : result[1][i].id,
							name : result[1][i].name,
							type : result[1][i].type,
							attr : result[1][i].attr,
							dim : dimName
						});
						storeListView.add(tmpRec)
					}
					_notEmptyTreeList()
				}
			} else {
				_showErrorMsg(result[3])
			}
		}
	};
	var parentPasteView = pv;
	var _config = Jedox.wss.palo.config;
	var that = this;
	var phpPaloServer = new Palo(phpPaloServerCbHandlers);
	var phpPaloServerOtherElems = new Palo(phpPaloServerCbHandlersForOtherElems);
	var servId, dbName, preselectedServDb = null, dimName, displayMode;
	var win, listView;
	var panelMain, panelTopBtns, panelBottomTreeBtns, panelbtnSearch, panelbtnOk, panelbtnClose, paneltxtSearch, panelcmbAttr, panelbtnMids, panellistView, panelcmbDb, panelDims, panelbtnSelectBranch, panelbtnSelectAll, panelbtnInvertSelect, panelbtnPasteVert, panelbtnClearList, panelbtnAsc, panelbtnDesc, panelbtnTreeList, panelchbShowHideB, panelcmbCube, panellistOfDims, panelbtnsChooseCubeType;
	var treeMain, expandMoreLevels, expandAllLevels, treeLevel, syncCounter, treeMaxLevelReached, hasOnlyLeaves, treeMode;
	var txtSearch, lblcmbDb, lblList, lblBottomInfo, cmbAttr, cmbDb, btnTreeList, chbShowHideB, chbInsertOtherElems;
	var btnTreeControls, btnOk, btnClose, btnSearch, btnMids, btnSelectBranch, btnSelectAll, btnInvertSelect, btnPasteVert, btnAsc, btnDesc, btnClearList, rbServerDb, rbCubeName, rbDimName, cmbCube, listOfDims, btnsChooseCubeType;
	var selectedElem, cmbDbState, showAsTree, editData, editMode, numOfRequests, dataMode;
	var AttrRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var storeAttrs = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	var DimNameRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var storeDimNames = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	var DimRecord = new Ext.data.Record.create( [ {
		name : "name"
	}, {
		name : "type"
	}, {
		name : "dataView"
	}, {
		name : "selectedAttr"
	} ]);
	var storeDims = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		}, {
			name : "type"
		}, {
			name : "dataView"
		}, {
			name : "selectedAttr"
		} ]
	});
	var storeListView = new Ext.data.SimpleStore( {
		fields : [ {
			name : "id"
		}, {
			name : "name"
		}, {
			name : "type"
		}, {
			name : "attr"
		}, {
			name : "dim"
		} ]
	});
	var ElemListRecord = new Ext.data.Record.create( [ {
		name : "id"
	}, {
		name : "name"
	}, {
		name : "type"
	}, {
		name : "attr"
	}, {
		name : "dim"
	} ]);
	var storeElemList = new Ext.data.SimpleStore( {
		fields : [ {
			name : "id"
		}, {
			name : "name"
		}, {
			name : "type"
		}, {
			name : "attr"
		}, {
			name : "dim"
		} ],
		listeners : {
			add : function() {
				if (storeElemList.getCount() > 0) {
					_showhidePasteButtons(storeElemList.getCount())
				} else {
					if (treeMain) {
						_showhidePasteButtons(treeMain.getSelectionModel()
								.getSelectedNodes().length)
					} else {
						_showhidePasteButtons(storeElemList.getCount())
					}
				}
			},
			remove : function() {
				if (storeElemList.getCount() > 0) {
					_showhidePasteButtons(storeElemList.getCount())
				} else {
					if (treeMain) {
						_showhidePasteButtons(treeMain.getSelectionModel()
								.getSelectedNodes().length)
					} else {
						_showhidePasteButtons(storeElemList.getCount())
					}
				}
			},
			clear : function() {
				if (!chbInsertOtherElems.getValue()) {
					if (treeMain) {
						_showhidePasteButtons(treeMain.getSelectionModel()
								.getSelectedNodes().length)
					} else {
						_showhidePasteButtons(storeElemList.getCount())
					}
				}
			}
		}
	});
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
	this.init = function(serv, db, dim, inEditData) {
		servId = serv;
		dbName = db;
		dimName = dim;
		cmbDbState = -1;
		treeLevel = 0;
		syncCounter = 0;
		selectedElem = null;
		showAsTree = true;
		displayMode = 0;
		numOfRequests = 0;
		expandAllLevels = false;
		hasOnlyLeaves = false;
		treeMode = true;
		dataMode = 0;
		editMode = ((inEditData) ? true : false);
		if (editMode) {
			editData = inEditData
		} else {
			editData = []
		}
		phpPaloServer.getServList();
		listView = new Ext.DataView( {
			store : storeElemList,
			itemSelector : "div.row-modeller",
			style : "overflow:auto",
			multiSelect : true,
			cls : "modellerDataViewSelect",
			tpl : new Ext.XTemplate('<tpl for=".">',
					'<div class="row-modeller">', "<span>&#160;{name}</span>",
					"</div>", "</tpl>"),
			listeners : {
				click : _singleClickListView,
				dblclick : function(dataView, index, node, e) {
					btnMids.up.disable();
					btnMids.left.disable();
					btnMids.down.disable();
					_dblClickListView(dataView, index, node, e)
				}
			}
		});
		panellistView = new Ext.Panel( {
			layout : "fit",
			items : [ listView ]
		});
		btnTreeControls = {
			plusB : new Ext.Button( {
				cls : "modellerImageButton",
				style : "vertical-align:bottom;",
				iconCls : "palo_icon_plus",
				handler : function() {
					treeLevel++;
					_expandTreeToLevel(treeMain.getRootNode())
				}
			}),
			minusB : new Ext.Button( {
				cls : "modellerImageButton",
				style : "vertical-align:bottom;",
				iconCls : "palo_icon_minus",
				handler : function() {
					if (treeLevel > 0) {
						treeLevel--;
						_expandTreeToLevel(treeMain.getRootNode())
					}
				}
			}),
			expandAllB : new Ext.Button(
					{
						cls : "modellerImageButton",
						style : "vertical-align:bottom;",
						iconCls : "palo_icon_expandall",
						handler : function() {
							expandAllLevels = true;
							treeMain.expandAll();
							treeLevel = (treeMaxLevelReached >= 0) ? treeMaxLevelReached
									: 0
						}
					}),
			collapseAllB : new Ext.Button( {
				cls : "modellerImageButton",
				style : "vertical-align:bottom;",
				iconCls : "palo_icon_colapseall",
				handler : function() {
					treeMain.collapseAll();
					treeLevel = 0
				}
			}),
			oneB : new Ext.Button( {
				text : "1",
				ctCls : "modellerButtonWithMargin",
				cls : "modellerButtonWithMargin",
				listeners : {
					click : function() {
						treeLevel = 0;
						if (treeMain) {
							treeMain.getSelectionModel().clearSelections()
						}
						_expandTreeToLevel(treeMain.getRootNode())
					}
				}
			}),
			twoB : new Ext.Button( {
				text : "2",
				ctCls : "modellerButtonWithMargin",
				cls : "modellerButtonWithMargin",
				listeners : {
					click : function() {
						treeLevel = 1;
						if (treeMain) {
							treeMain.getSelectionModel().clearSelections()
						}
						_expandTreeToLevel(treeMain.getRootNode())
					}
				}
			}),
			threeB : new Ext.Button( {
				text : "3",
				ctCls : "modellerButtonWithMargin",
				cls : "modellerButtonWithMargin",
				listeners : {
					click : function() {
						treeLevel = 2;
						if (treeMain) {
							treeMain.getSelectionModel().clearSelections()
						}
						_expandTreeToLevel(treeMain.getRootNode())
					}
				}
			}),
			fourB : new Ext.Button( {
				text : "4",
				ctCls : "modellerButtonWithMargin",
				cls : "modellerButtonWithMargin",
				listeners : {
					click : function() {
						treeLevel = 3;
						if (treeMain) {
							treeMain.getSelectionModel().clearSelections()
						}
						_expandTreeToLevel(treeMain.getRootNode())
					}
				}
			}),
			fiveB : new Ext.Button( {
				text : "5",
				ctCls : "modellerButtonWithMargin",
				cls : "modellerButtonWithMargin",
				listeners : {
					click : function() {
						treeLevel = 4;
						if (treeMain) {
							treeMain.getSelectionModel().clearSelections()
						}
						_expandTreeToLevel(treeMain.getRootNode())
					}
				}
			}),
			branchB : new Ext.Button(
					{
						text : "B".localize(),
						ctCls : "modellerButtonWithMargin",
						cls : "modellerButtonWithMargin",
						listeners : {
							click : function() {
								if (displayMode == 0) {
									treeMain
											.getRootNode()
											.cascade(
													function(node) {
														if (node.parentNode) {
															if (node.isLeaf()
																	&& node.parentNode
																			.isExpanded()) {
																if (!node
																		.isSelected()) {
																	treeMain
																			.getSelectionModel()
																			.select(
																					node,
																					null,
																					true)
																}
															} else {
																if (node
																		.isSelected()) {
																	treeMain
																			.getSelectionModel()
																			.unselect(
																					node)
																}
															}
														}
													})
								} else {
									if (displayMode == 1) {
										var tmpDv = storeDims
												.getAt(
														storeDims.find("name",
																dimName)).get(
														"dataView");
										tmpDv.clearSelections(true);
										for ( var i = 0; i < storeListView
												.getCount(); i++) {
											if (storeListView.getAt(i).get(
													"type") != "consolidated") {
												tmpDv.select(i, true, true)
											}
										}
										if (storeElemList.getCount() == 0) {
											_showhidePasteButtons(tmpDv
													.getSelectionCount())
										}
									}
								}
							}
						}
					})
		};
		panelTopBtns = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "table",
			items : [ btnTreeControls.plusB, btnTreeControls.minusB,
					btnTreeControls.expandAllB, btnTreeControls.collapseAllB ]
		});
		panelBottomTreeBtns = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "table",
			items : [ btnTreeControls.oneB, btnTreeControls.twoB,
					btnTreeControls.threeB, btnTreeControls.fourB,
					btnTreeControls.fiveB, btnTreeControls.branchB ]
		});
		btnSearch = new Ext.Button( {
			text : "Search & Select".localize(),
			cls : "seButton1",
			hidden : true,
			listeners : {
				click : function() {
					if (txtSearch.getValue() != "") {
						if (displayMode == 0) {
							var tmpAttrId = storeDims.getAt(
									storeDims.find("name", dimName)).get(
									"selectedAttr");
							phpPaloServer.searchElem(servId, dbName, dimName,
									txtSearch.getValue(),
									((tmpAttrId == 0) ? null : storeAttrs
											.getAt(tmpAttrId).get("name")))
						} else {
							if (displayMode == 1) {
								var tmpIndex = storeListView.find("name",
										txtSearch.getValue());
								if (tmpIndex >= 0) {
									var tmpDv = storeDims.getAt(
											storeDims.find("name", dimName))
											.get("dataView");
									tmpDv.clearSelections(true);
									tmpDv.select(tmpIndex, true, false)
								} else {
									_showErrorMsg("Element not found!"
											.localize())
								}
							}
						}
					}
				}
			}
		});
		panelbtnSearch = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnSearch ]
		});
		btnOk = new Ext.Button(
				{
					text : ((parentPasteView) ? "OK".localize() : "Paste"
							.localize()),
					cls : "seButton1",
					listeners : {
						click : function() {
							if (parentPasteView) {
								var elems = [];
								if (storeElemList.getCount() > 0) {
									for ( var i = 0; i < storeElemList
											.getCount(); i++) {
										elems[i] = [
												storeElemList.getAt(i)
														.get("id"),
												storeElemList.getAt(i).get(
														"attr"),
												((storeElemList.getAt(i).get(
														"type") == "consolidated") ? "C"
														: "") ]
									}
								} else {
									if ((displayMode == 0) && (treeMain)) {
										var tmpNodes = treeMain
												.getSelectionModel()
												.getSelectedNodes();
										for ( var i = 0; i < tmpNodes.length; i++) {
											elems[i] = [
													tmpNodes[i].id,
													((cmbAttr.getValue() == "[none]"
															.localize()) ? null
															: cmbAttr
																	.getValue()),
													((tmpNodes[i].isLeaf()) ? ""
															: "C") ]
										}
									} else {
										if (displayMode == 1) {
											var tmpRecs = storeDims.getAt(
													storeDims.find("name",
															dimName)).get(
													"dataView")
													.getSelectedRecords();
											for ( var i = 0; i < tmpRecs.length; i++) {
												elems[i] = [
														tmpRecs[i].get("id"),
														tmpRecs[i].get("attr"),
														((tmpRecs[i]
																.get("type") == "consolidated") ? "C"
																: "") ]
											}
										}
									}
								}
								parentPasteView.setSelectedElements(dimName,
										elems)
							} else {
								_doPasteOnGrid(true)
							}
							win.close()
						}
					}
				});
		panelbtnOk = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnOk ]
		});
		btnClose = new Ext.Button( {
			text : "Cancel".localize(),
			cls : "seButton1",
			listeners : {
				click : function() {
					win.close()
				}
			}
		});
		panelbtnClose = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnClose ]
		});
		btnMids = {
			up : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				iconCls : "palo_icon_up",
				handler : _upBtnHandler
			}),
			right : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				iconCls : "palo_icon_right",
				handler : _rightBtnHandler
			}),
			left : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				iconCls : "palo_icon_left",
				handler : _leftBtnHandler
			}),
			down : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				iconCls : "palo_icon_down",
				handler : _downBtnHandler
			})
		};
		panelbtnMids = new Ext.Panel(
				{
					border : false,
					bodyStyle : "background-color: transparent; vertical-align: middle;",
					layout : "form",
					width : 31,
					autoHeight : true,
					items : [ btnMids.up, btnMids.right, btnMids.left,
							btnMids.down ]
				});
		btnSelectBranch = new Ext.Button( {
			text : "Select Branch".localize(),
			cls : "seButton1",
			listeners : {
				click : function() {
					var tmpNodes = treeMain.getSelectionModel()
							.getSelectedNodes();
					for ( var i = 0; i < tmpNodes.length; i++) {
						_selectSubNodes(tmpNodes[i])
					}
				}
			}
		});
		panelbtnSelectBranch = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnSelectBranch ]
		});
		btnSelectAll = new Ext.Button(
				{
					text : "Select All".localize(),
					cls : "seButton1",
					listeners : {
						click : function() {
							if (displayMode == 0) {
								treeMain
										.getRootNode()
										.cascade(
												function(node) {
													if (node.parentNode) {
														if (!node.isSelected()
																&& node.parentNode
																		.isExpanded()) {
															treeMain
																	.getSelectionModel()
																	.select(
																			node,
																			null,
																			true)
														} else {
															if (!node.parentNode
																	.isExpanded()
																	&& node
																			.isSelected()) {
																treeMain
																		.getSelectionModel()
																		.unselect(
																				node)
															}
														}
													}
												})
							} else {
								if (displayMode == 1) {
									storeDims
											.getAt(
													storeDims.find("name",
															dimName))
											.get("dataView")
											.selectRange(
													0,
													storeListView.getCount() - 1)
								}
							}
						}
					}
				});
		panelbtnSelectAll = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnSelectAll ]
		});
		btnInvertSelect = new Ext.Button(
				{
					text : "Invert Select".localize(),
					cls : "seButton1",
					hidden : true,
					listeners : {
						click : function() {
							if (displayMode == 0) {
								treeMain
										.getRootNode()
										.cascade(
												function(node) {
													if (node.parentNode) {
														if (!node.isSelected()
																&& node.parentNode
																		.isExpanded()) {
															treeMain
																	.getSelectionModel()
																	.select(
																			node,
																			null,
																			true)
														} else {
															if (node
																	.isSelected()) {
																treeMain
																		.getSelectionModel()
																		.unselect(
																				node)
															}
														}
													}
												})
							} else {
								if (displayMode == 1) {
									var tmpDv = storeDims.getAt(
											storeDims.find("name", dimName))
											.get("dataView");
									for ( var i = 0; i < storeListView
											.getCount(); i++) {
										if (!tmpDv.isSelected(i)) {
											tmpDv.select(i, true, true)
										} else {
											tmpDv.deselect(i)
										}
									}
									if (storeElemList.getCount() == 0) {
										_showhidePasteButtons(tmpDv
												.getSelectionCount())
									}
								}
							}
						}
					}
				});
		panelbtnInvertSelect = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnInvertSelect ]
		});
		btnPasteVert = new Ext.Button( {
			text : "Paste Vertically".localize(),
			cls : "seButton1",
			hidden : true,
			listeners : {
				click : function() {
					_doPasteOnGrid(false);
					win.close()
				}
			}
		});
		panelbtnPasteVert = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnPasteVert ]
		});
		btnAsc = new Ext.Button( {
			text : "Ascending".localize(),
			cls : "seButton1",
			listeners : {
				click : function() {
					storeElemList.sort("name")
				}
			}
		});
		panelbtnAsc = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnAsc ]
		});
		btnDesc = new Ext.Button( {
			text : "Descending".localize(),
			cls : "seButton1",
			listeners : {
				click : function() {
					storeElemList.sort("name", "DESC")
				}
			}
		});
		panelbtnDesc = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnDesc ]
		});
		btnClearList = new Ext.Button( {
			text : "Clear list".localize(),
			cls : "seButton1",
			listeners : {
				click : function() {
					storeElemList.removeAll()
				}
			}
		});
		panelbtnClearList = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnClearList ]
		});
		lblcmbDb = new Ext.form.MiscField( {
			value : "Choose Server/Database".localize() + ":",
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
		cmbAttr = new Ext.form.ComboBox(
				{
					store : storeAttrs,
					bodyStyle : "background-color: transparent;",
					width : 150,
					typeAhead : false,
					selectOnFocus : true,
					hideLabel : true,
					editable : false,
					forceSelection : true,
					triggerAction : "all",
					mode : "local",
					listeners : {
						select : function(combo, record, index) {
							var tmpAttrId = storeDims.getAt(
									storeDims.find("name", dimName)).get(
									"selectedAttr");
							if (index != tmpAttrId) {
								if (displayMode == 0) {
									if (index != 0) {
										treeMain.getLoader().dataUrl = phpPaloServer.dispatcher.serverUrl
												+ "&wam="
												+ Jedox.wss.app.appModeS
												+ "&c="
												+ phpPaloServer.className
												+ "&m=getTreeNodes&servId="
												+ servId
												+ "&dbName="
												+ dbName
												+ "&dimName="
												+ dimName
												+ "&attr=" + record.get("name")
									} else {
										treeMain.getLoader().dataUrl = phpPaloServer.dispatcher.serverUrl
												+ "&wam="
												+ Jedox.wss.app.appModeS
												+ "&c="
												+ phpPaloServer.className
												+ "&m=getTreeNodes&servId="
												+ servId
												+ "&dbName="
												+ dbName
												+ "&dimName=" + dimName
									}
									treeMain.getRootNode().reload();
									storeDims.getAt(
											storeDims.find("name", dimName))
											.set("selectedAttr", index)
								} else {
									if (displayMode == 1) {
										phpPaloServer
												.getAttributedElems(
														servId,
														dbName,
														dimName,
														((cmbAttr.getValue() == "[none]"
																.localize()) ? null
																: cmbAttr
																		.getValue()),
														0,
														_config.numberOfElements * 5);
										storeDims
												.getAt(
														storeDims.find("name",
																dimName)).set(
														"selectedAttr", index)
									}
								}
							}
						}
					},
					valueField : "name",
					displayField : "name"
				});
		panelcmbAttr = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoHeight : true,
			items : [ cmbAttr ]
		});
		txtSearch = new Ext.form.TextField( {
			hideLabel : true,
			hidden : true
		});
		paneltxtSearch = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ txtSearch ]
		});
		lblList = new Ext.form.MiscField( {
			value : "Pick list".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		btnTreeList = new Ext.Button(
				{
					cls : "modellerImageButton",
					style : "vertical-align:bottom;",
					iconCls : "palo_icon_showHR",
					handler : function() {
						treeMode = (treeMode) ? false : true;
						if (treeMode) {
							this.setIconClass("palo_icon_showHR");
							displayMode = 0;
							var tmpNum = panelDims.items.length;
							for ( var i = 0; i < tmpNum; i++) {
								var tmpTreeP = _makeTreePanel(panelDims
										.getComponent(i).title);
								var tmpRec = storeDims
										.getAt(storeDims.find("name", panelDims
												.getComponent(i).title));
								var tmpCount = storeListView.getCount();
								tmpRec.set("dataView", null);
								tmpRec.set("type", "tree");
								panelDims.getComponent(i).add(tmpTreeP);
								panelDims.getComponent(i).remove(
										panelDims.getComponent(i).getComponent(
												0));
								if (dimName == panelDims.getComponent(i).title) {
									treeMain = tmpTreeP;
									if (tmpCount > 0) {
										_notEmptyTreeList()
									}
								}
							}
							if (storeElemList.getCount() == 0) {
								_showhidePasteButtons(0)
							}
							panelDims.doLayout()
						} else {
							this.setIconClass("palo_icon_showHRflat");
							displayMode = 1;
							var tmpNum = panelDims.items.length;
							for ( var i = 0; i < tmpNum; i++) {
								var tmpDv = _makeDataView();
								var tmpRec = storeDims
										.getAt(storeDims.find("name", panelDims
												.getComponent(i).title));
								tmpRec.set("dataView", tmpDv);
								tmpRec.set("type", "list");
								panelDims.getComponent(i).add(tmpDv);
								panelDims.getComponent(i).remove(
										panelDims.getComponent(i).getComponent(
												0))
							}
							phpPaloServer.getAttributedElems(servId, dbName,
									dimName, ((cmbAttr.getValue() == "[none]"
											.localize()) ? null : cmbAttr
											.getValue()), 0,
									_config.numberOfElements * 5);
							_emptyTreeList();
							panelDims.doLayout()
						}
					}
				});
		panelbtnTreeList = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ btnTreeList ]
		});
		lblBottomInfo = new Ext.form.MiscField( {
			value : "_msg: se_Tip".localize(),
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true,
			hidden : true
		});
		chbShowHideB = new Ext.form.Checkbox( {
			hideLabel : true,
			boxLabel : "Show all selection tools".localize(),
			listeners : {
				check : function(checkBox, checked) {
					if (checked) {
						btnInvertSelect.show();
						txtSearch.show();
						btnSearch.show();
						lblBottomInfo.show()
					} else {
						btnInvertSelect.hide();
						txtSearch.hide();
						btnSearch.hide();
						lblBottomInfo.hide()
					}
					_resizeAll()
				}
			}
		});
		panelchbShowHideB = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ chbShowHideB ]
		});
		chbInsertOtherElems = new Ext.form.Checkbox( {
			hidden : parentPasteView,
			hideLabel : true,
			boxLabel : "insert database elements".localize(),
			listeners : {
				check : function(checkBox, checked) {
					if (checked) {
						panelTopBtns.hide();
						panelDims.hide();
						panelBottomTreeBtns.hide();
						panelbtnSearch.hide();
						panelcmbAttr.hide();
						paneltxtSearch.hide();
						panelbtnMids.hide();
						panellistView.hide();
						lblList.hide();
						panelbtnSelectBranch.hide();
						panelbtnInvertSelect.hide();
						panelbtnSelectAll.hide();
						panelbtnClearList.hide();
						panelbtnAsc.hide();
						panelbtnDesc.hide();
						panelbtnTreeList.hide();
						lblBottomInfo.hide();
						panelchbShowHideB.hide();
						rbServerDb.show();
						rbCubeName.show();
						rbDimName.show();
						panelcmbCube.show();
						panellistOfDims.show();
						panelbtnsChooseCubeType.show();
						if (rbDimName.getValue()) {
							_showhidePasteButtons(listOfDims
									.getSelectedIndexes().length)
						} else {
							_showhidePasteButtons(1)
						}
					} else {
						panelTopBtns.show();
						panelDims.show();
						panelBottomTreeBtns.show();
						panelbtnSearch.show();
						panelcmbAttr.show();
						paneltxtSearch.show();
						panelbtnMids.show();
						panellistView.show();
						lblList.show();
						panelbtnSelectBranch.show();
						panelbtnInvertSelect.show();
						panelbtnSelectAll.show();
						panelbtnClearList.show();
						panelbtnAsc.show();
						panelbtnDesc.show();
						panelbtnTreeList.show();
						lblBottomInfo.show();
						panelchbShowHideB.show();
						rbServerDb.hide();
						rbCubeName.hide();
						rbDimName.hide();
						panelcmbCube.hide();
						panellistOfDims.hide();
						panelbtnsChooseCubeType.hide();
						if ((displayMode == 0) && treeMain) {
							_showhidePasteButtons(treeMain.getSelectionModel()
									.getSelectedNodes().length)
						} else {
							if (displayMode == 1) {
								var tmpDv = storeDims.getAt(
										storeDims.find("name", dimName)).get(
										"dataView");
								_showhidePasteButtons(tmpDv
										.getSelectedIndexes().length)
							}
						}
					}
				}
			}
		});
		rbServerDb = new Ext.form.Radio( {
			hidden : true,
			name : "pasteOtherElems",
			boxLabel : "insert server/database (connection)".localize(),
			hideLabel : true,
			checked : true,
			listeners : {
				check : function(thisRb, isChecked) {
					if (isChecked) {
						_showhidePasteButtons(1)
					}
				}
			}
		});
		rbCubeName = new Ext.form.Radio( {
			hidden : true,
			name : "pasteOtherElems",
			boxLabel : "insert cube name".localize(),
			hideLabel : true,
			listeners : {
				check : function(thisRb, isChecked) {
					if (isChecked) {
						_showhidePasteButtons((cmbCube.getValue() ? 1 : 0))
					}
				}
			}
		});
		rbDimName = new Ext.form.Radio(
				{
					hidden : true,
					name : "pasteOtherElems",
					boxLabel : "insert dimension names".localize(),
					hideLabel : true,
					listeners : {
						check : function(thisRb, isChecked) {
							if (isChecked) {
								_showhidePasteButtons(listOfDims
										.getSelectedIndexes().length)
							}
						}
					}
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
			valueField : "name",
			displayField : "name",
			listeners : {
				select : function() {
					_setOtherElemsRadios(2);
					_showhidePasteButtons(1)
				}
			}
		});
		panelcmbCube = new Ext.Panel( {
			hidden : true,
			border : false,
			hideMode : "offsets",
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoHeight : true,
			items : [ cmbCube ]
		});
		btnsChooseCubeType = {
			metaDataB : new Ext.Button( {
				disabled : true,
				iconCls : "icon_palo_cube",
				cls : "modellerImageButton",
				handler : function() {
					_setDataMode(0);
					phpPaloServerOtherElems.getCubeNames(servId, dbName,
							dataMode)
				}
			}),
			attribsB : new Ext.Button( {
				disabled : true,
				iconCls : "icon_palo_table",
				cls : "modellerImageButton",
				handler : function() {
					_setDataMode(2);
					phpPaloServerOtherElems.getCubeNames(servId, dbName,
							dataMode)
				}
			}),
			usersB : new Ext.Button( {
				disabled : true,
				iconCls : "icon_palo_user",
				cls : "modellerImageButton",
				handler : function() {
					_setDataMode(1);
					phpPaloServerOtherElems.getCubeNames(servId, dbName,
							dataMode)
				}
			})
		};
		panelbtnsChooseCubeType = new Ext.Panel(
				{
					hidden : true,
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
		listOfDims = new Ext.DataView( {
			store : storeDimNames,
			itemSelector : "div.row-modeller",
			style : "overflow:auto",
			multiSelect : true,
			cls : "modellerDataViewSelect",
			tpl : new Ext.XTemplate('<tpl for=".">',
					'<div class="row-modeller">', "<span>&#160;{name}</span>",
					"</div>", "</tpl>"),
			listeners : {
				selectionchange : function(thisDv, selections) {
					rbDimName.setValue(true);
					_setOtherElemsRadios(3);
					_showhidePasteButtons(selections.length)
				}
			}
		});
		panellistOfDims = new Ext.Panel( {
			hidden : true,
			layout : "fit",
			items : [ listOfDims ]
		});
		panelDims = new Ext.Panel( {
			id : "se_panelDims",
			layout : "accordion",
			autoScroll : true
		});
		panelMain = new Ext.Panel( {
			id : "se_mainPanel",
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			anchor : "100% 100%",
			monitorResize : true,
			listeners : {
				resize : _resizeAll
			},
			items : [ panelTopBtns, panelDims, panelBottomTreeBtns,
					panelbtnSearch, panelbtnOk, panelbtnClose, panelcmbAttr,
					paneltxtSearch, panelbtnMids, panellistView, lblcmbDb,
					panelcmbDb, lblList, panelbtnSelectBranch,
					panelbtnInvertSelect, panelbtnSelectAll, panelbtnPasteVert,
					panelbtnClearList, panelbtnAsc, panelbtnDesc,
					panelbtnTreeList, lblBottomInfo, panelchbShowHideB,
					chbInsertOtherElems, rbServerDb, rbCubeName, rbDimName,
					panelcmbCube, panellistOfDims, panelbtnsChooseCubeType ]
		});
		win = new Ext.Window(
				{
					id : "selectelementsWizardWindow",
					layout : "fit",
					title : ((parentPasteView) ? "Select Elements".localize()
							: "Paste Elements".localize()),
					width : _config.seWinW,
					height : _config.seWinH,
					minWidth : _config.seWinW,
					minHeight : _config.seWinH,
					closeAction : "close",
					cls : "default-format-window",
					autoDestroy : true,
					plain : true,
					modal : true,
					resizable : true,
					listeners : {
						activate : _resizeAll,
						close : function() {
							if (!parentPasteView) {
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
								Jedox.wss.app
										.unload(Jedox.wss.app.dynJSRegistry.selectElements);
								phpPaloServer
										.setPreselectServDb(servId, dbName)
							}
						}
					},
					items : [ panelMain ]
				})
	};
	this.show = function() {
		if (win) {
			btnOk.disable();
			if (!parentPasteView) {
				Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
				Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
			}
			win.show();
			storeAttrs.add(new AttrRecord( {
				name : "[none]".localize()
			}));
			cmbAttr.setValue("[none]".localize());
			if (editMode) {
				for (i = 0; i < editData.length; i++) {
					tmpRec = new ElemListRecord( {
						id : editData[i][0],
						name : editData[i][0],
						type : ((editData[i][2] != "C") ? "" : "consolidated"),
						attr : editData[i][1],
						dim : dimName
					});
					storeElemList.add(tmpRec)
				}
			}
		}
	};
	var _cmbDbSelectionHandler = function(combo, record, index) {
		if (chbInsertOtherElems.getValue()) {
			listOfDims.clearSelections();
			_setOtherElemsRadios(1);
			_showhidePasteButtons(1)
		}
		if (cmbDbState != index) {
			cmbDbState = index;
			storeAttrs.removeAll();
			storeAttrs.add(new AttrRecord( {
				name : "[none]".localize()
			}));
			cmbAttr.setValue("[none]".localize());
			if (record.get("type") == "database") {
				servId = record.get("parent_id");
				dbName = record.get("name");
				if (parentPasteView) {
					_addDimPanel(dimName, true)
				} else {
					phpPaloServer.getDims(servId, dbName, 0);
					_setDataMode(dataMode);
					phpPaloServerOtherElems.getCubeNames(servId, dbName,
							dataMode);
					phpPaloServerOtherElems.getDims(servId, dbName, 0)
				}
			} else {
				if (record.get("type") == "server") {
					for ( var i = (panelDims.items.length - 1); i >= 0; i--) {
						panelDims.remove(panelDims.getComponent(i))
					}
				}
			}
			if ((storeElemList.getCount() > 0) && (editData.length == 0)) {
				storeElemList.removeAll()
			}
			_emptyTreeList()
		}
	};
	var _upBtnHandler = function() {
		var tmpElem;
		var tmpSelectedIds = listView.getSelectedIndexes();
		for ( var i = 1; i < storeElemList.getCount(); i++) {
			if (listView.isSelected(listView.getNode(i))) {
				if (!listView.isSelected(listView.getNode(i - 1))) {
					tmpElem = storeElemList.getAt(i);
					storeElemList.remove(tmpElem);
					storeElemList.insert(i - 1, tmpElem);
					for ( var j = 0; j < tmpSelectedIds.length; j++) {
						if (tmpSelectedIds[j] == i) {
							tmpSelectedIds[j] = i - 1;
							break
						}
					}
					listView.select(tmpSelectedIds)
				}
			}
		}
	};
	var _rightBtnHandler = function() {
		if (displayMode == 0) {
			var tmpRec;
			var tmpNodes = treeMain.getSelectionModel().getSelectedNodes();
			for ( var i = 0; i < tmpNodes.length; i++) {
				tmpRec = new ElemListRecord( {
					id : tmpNodes[i].id,
					name : tmpNodes[i].text,
					type : ((tmpNodes[i].isLeaf()) ? "" : "consolidated"),
					attr : ((cmbAttr.getValue() == "[none]".localize()) ? null
							: cmbAttr.getValue()),
					dim : dimName
				});
				storeElemList.add(tmpRec)
			}
		} else {
			if (displayMode == 1) {
				var tmpDv = storeDims.getAt(storeDims.find("name", dimName))
						.get("dataView");
				var tmpIndexes = tmpDv.getSelectedIndexes();
				for ( var i = 0; i < tmpIndexes.length; i++) {
					_dblClickListViewHandler(tmpDv, tmpIndexes[i], tmpDv
							.getNode(tmpIndexes[i]), null)
				}
			}
		}
	};
	var _leftBtnHandler = function() {
		btnMids.up.disable();
		btnMids.left.disable();
		btnMids.down.disable();
		var tmpElems = listView.getSelectedRecords();
		for ( var i = 0; i < tmpElems.length; i++) {
			storeElemList.remove(tmpElems[i])
		}
	};
	var _downBtnHandler = function() {
		var tmpElem;
		var tmpSelectedIds = listView.getSelectedIndexes();
		for ( var i = (storeElemList.getCount() - 2); i >= 0; i--) {
			if (listView.isSelected(listView.getNode(i))) {
				if (!listView.isSelected(listView.getNode(i + 1))) {
					tmpElem = storeElemList.getAt(i);
					storeElemList.remove(tmpElem);
					storeElemList.insert(i + 1, tmpElem);
					for ( var j = 0; j < tmpSelectedIds.length; j++) {
						if (tmpSelectedIds[j] == i) {
							tmpSelectedIds[j] = i + 1;
							break
						}
					}
					listView.select(tmpSelectedIds)
				}
			}
		}
	};
	var _singleClickListView = function(dataView, index, record, e) {
		btnMids.up.enable();
		btnMids.left.enable();
		btnMids.down.enable()
	};
	var _dblClickListView = function(dataView, index, node, e) {
		storeElemList.remove(storeElemList.getAt(index))
	};
	var _singleClickListViewHandler = function(dataView, index, node, evnt) {
		btnMids.right.enable()
	};
	var _dblClickListViewHandler = function(dataView, index, node, evnt) {
		var tmpRec = new ElemListRecord( {
			id : storeListView.getAt(index).get("id"),
			name : storeListView.getAt(index).get("name"),
			type : storeListView.getAt(index).get("type"),
			attr : storeListView.getAt(index).get("attr"),
			dim : dimName
		});
		storeElemList.add(tmpRec)
	};
	var _expandTreeToLevel = function(node) {
		expandMoreLevels = true;
		_doExpandigTreeToLevel(node);
		if ((treeLevel > (treeMaxLevelReached)) && (syncCounter == 0)) {
			treeLevel = treeMaxLevelReached
		}
		if (syncCounter == 0) {
			expandMoreLevels = false
		}
	};
	var _doExpandigTreeToLevel = function(node) {
		if (node.getDepth() > treeMaxLevelReached) {
			if ((node.childNodes.length == 0)
					&& ((node.getDepth() - 1) >= treeMaxLevelReached)) {
				treeMaxLevelReached = node.getDepth() - 1
			} else {
				treeMaxLevelReached = node.getDepth()
			}
		}
		if (node.getDepth() == treeLevel) {
			node.collapseChildNodes(true);
			if (treeMain) {
				for ( var i = (node.childNodes.length - 1); i >= 0; i--) {
					treeMain.getSelectionModel().select(node.childNodes[i],
							null, true)
				}
			}
		} else {
			if (node.childNodes.length > 0) {
				for ( var i = (node.childNodes.length - 1); i >= 0; i--) {
					if (node.childNodes[i].getDepth() <= treeLevel) {
						node.childNodes[i].expand();
						_doExpandigTreeToLevel(node.childNodes[i])
					}
				}
			}
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
	var _refillcmbDb = function() {
		var executed = false;
		for ( var i = 0; i < storeServDb.getCount(); i++) {
			if (storeServDb.getAt(i).get("connected") == 1) {
				executed = true;
				phpPaloServer.getDBs(storeServDb.getAt(i).get("id"));
				numOfRequests++
			}
		}
		if (!executed && (cmbDbState == -1)) {
			var tmpIndex = 0;
			cmbDbState = -1;
			cmbDb.setValue(storeServDb.getAt(tmpIndex).get("name"));
			_cmbDbSelectionHandler(cmbDb, storeServDb.getAt(tmpIndex), tmpIndex)
		}
	};
	var _removeAllDimsFromPanel = function(panel) {
		for ( var i = (panel.items.length - 1); i >= 0; i--) {
			panel.remove(panel.getComponent(i))
		}
	};
	var _addDimPanel = function(pName, inExpand) {
		var tmpPan = new Ext.Panel( {
			title : pName,
			border : false,
			collapsed : true,
			draggable : false,
			autoHeight : true,
			animCollapse : true,
			listeners : {
				expand : function(p) {
					if (dimName != p.title) {
						storeElemList.removeAll()
					}
					dimName = p.title;
					if (displayMode == 0) {
						treeLevel = 0;
						syncCounter = 0;
						treeMaxLevelReached = 0;
						if (treeMain) {
							treeMain.getSelectionModel().clearSelections()
						}
						treeMain = p.getComponent(0);
						if (treeMain.getRootNode().childNodes.length > 0) {
							_notEmptyTreeList();
							phpPaloServer.getDimStringAttrs(servId, dbName,
									dimName)
						} else {
							p.collapse()
						}
					} else {
						if (displayMode == 1) {
							storeListView.removeAll();
							phpPaloServer.getAttributedElems(servId, dbName,
									dimName, ((cmbAttr.getValue() == "[none]"
											.localize()) ? null : cmbAttr
											.getValue()), 0,
									_config.numberOfElements * 5)
						}
					}
					btnMids.right.disable()
				},
				collapse : function(p) {
					_emptyTreeList();
					btnMids.right.disable()
				}
			}
		});
		if (displayMode == 0) {
			tmpPan.add(_makeTreePanel(pName, tmpPan, inExpand));
			panelDims.add(tmpPan);
			storeDims.add(new DimRecord( {
				name : pName,
				type : "tree",
				dataView : null,
				selectedAttr : 0
			}))
		} else {
			var tmpDv = _makeDataView();
			tmpPan.add(tmpDv);
			panelDims.add(tmpPan);
			storeDims.add(new DimRecord( {
				name : pName,
				type : "list",
				dataView : tmpDv,
				selectedAttr : 0
			}))
		}
		panelDims.doLayout();
		if (inExpand) {
			tmpPan.expand()
		}
	};
	var _makeDataView = function() {
		var tmpDv = new Ext.DataView(
				{
					itemSelector : "div.row-modeller",
					style : "overflow:auto",
					multiSelect : true,
					store : storeListView,
					cls : "modellerDataViewSelect",
					tpl : new Ext.XTemplate(
							'<tpl for=".">',
							'<div class="row-modeller">',
							"<tpl if=\"type == 'string'\">",
							'<img class="palo_selelem_string" src="../lib/ext/resources/images/default/s.gif" width="16" height="16"/>',
							"</tpl>",
							"<tpl if=\"type == 'numeric'\">",
							'<img class="palo_selelem_numeric" src="../lib/ext/resources/images/default/s.gif"/ width="16" height="16">',
							"</tpl>",
							"<tpl if=\"type == 'consolidated'\">",
							'<img class="palo_selelem_consolidated" src="../lib/ext/resources/images/default/s.gif"/ width="16" height="16">',
							"</tpl>", "<span>&#160;{name}</span>", "</div>",
							"</tpl>"),
					listeners : {
						dblclick : function(dataView, index, node, e) {
							_dblClickListViewHandler(dataView, index, node, e)
						},
						click : function(dataView, index, node, e) {
							_singleClickListViewHandler(dataView, index, node,
									e)
						},
						contextmenu : function(dataView, index, node, e) {
							e.stopEvent()
						},
						selectionchange : function(dataView, indexes) {
							if (storeElemList.getCount() == 0) {
								_showhidePasteButtons(indexes.length)
							}
						}
					}
				});
		return tmpDv
	};
	var _makeTreePanel = function(dimensionName, inPanel, inExpand) {
		var tree = new Ext.tree.TreePanel( {
			cls : "x-tree-noicon",
			border : false,
			autoScroll : true,
			animate : false,
			enableDD : false,
			containerScroll : true,
			rootVisible : false,
			selModel : new Ext.tree.MultiSelectionModel(),
			loader : new Ext.tree.TreeLoader( {
				dataUrl : phpPaloServer.dispatcher.serverUrl
						+ "&wam="
						+ Jedox.wss.app.appModeS
						+ "&c="
						+ phpPaloServer.className
						+ "&m=getTreeNodes&servId="
						+ servId
						+ "&dbName="
						+ dbName
						+ "&dimName="
						+ dimensionName
						+ ((cmbAttr.getValue() == "[none]".localize()) ? ""
								: "&attr=" + cmbAttr.getValue()),
				listeners : {
					beforeload : function() {
						if (expandMoreLevels || expandAllLevels) {
							if (!hasOnlyLeaves) {
								hasOnlyLeaves = true
							}
							syncCounter++
						}
					},
					load : function(thisS, node, response) {
						if (node.getDepth() > treeMaxLevelReached) {
							treeMaxLevelReached = node.getDepth()
						}
						if (syncCounter > 0) {
							if (hasOnlyLeaves) {
								node.eachChild(function(chNode) {
									if (!chNode.isLeaf()) {
										hasOnlyLeaves = false;
										return false
									}
								})
							}
							syncCounter--;
							if (expandMoreLevels) {
								_expandTreeToLevel(node)
							} else {
								if ((expandAllLevels) && (syncCounter == 0)
										&& (hasOnlyLeaves)) {
									hasOnlyLeaves = false;
									expandAllLevels = false;
									if (treeMaxLevelReached > treeLevel) {
										treeLevel = treeMaxLevelReached
									}
								}
							}
						}
					}
				}
			}),
			listeners : {
				dblclick : function(node) {
					tmpRec = new ElemListRecord(
							{
								id : node.id,
								name : node.text,
								type : ((node.isLeaf()) ? "" : "consolidated"),
								attr : ((cmbAttr.getValue() == "[none]"
										.localize()) ? null : cmbAttr
										.getValue()),
								dim : dimName
							});
					storeElemList.add(tmpRec)
				}
			}
		});
		tree.getSelectionModel().onNodeClick = function(node, e) {
			if (node.isSelected() && e.ctrlKey) {
				this.unselect(node)
			} else {
				this.select(node, e, e.ctrlKey)
			}
		};
		var root = new Ext.tree.AsyncTreeNode( {
			text : "Root",
			draggable : false,
			id : "root",
			listeners : {
				load : function(tmp) {
					if (inExpand) {
						inPanel.expand()
					}
				}
			}
		});
		tree.setRootNode(root);
		tree.getSelectionModel().on("selectionchange", function(thisM, nodes) {
			if (storeElemList.getCount() == 0) {
				_showhidePasteButtons(nodes.length)
			}
		});
		return tree
	};
	var _showhidePasteButtons = function(num) {
		if ((num > 1) && (!parentPasteView)) {
			btnMids.right.enable();
			btnOk.enable();
			btnOk.setText("Paste horizontaly".localize());
			btnPasteVert.show()
		} else {
			if ((num == 1) || ((num > 1) && (parentPasteView))) {
				btnMids.right.enable();
				btnOk.enable();
				btnOk.setText((parentPasteView) ? "OK".localize() : "Paste"
						.localize());
				btnPasteVert.hide()
			} else {
				btnMids.right.disable();
				btnOk.disable();
				btnOk.setText((parentPasteView) ? "OK".localize() : "Paste"
						.localize());
				btnPasteVert.hide()
			}
		}
	};
	var _emptyTreeList = function() {
		btnTreeControls.minusB.disable();
		btnTreeControls.plusB.disable();
		btnTreeControls.expandAllB.disable();
		btnTreeControls.collapseAllB.disable();
		btnTreeControls.oneB.disable();
		btnTreeControls.twoB.disable();
		btnTreeControls.threeB.disable();
		btnTreeControls.fourB.disable();
		btnTreeControls.fiveB.disable();
		btnTreeControls.branchB.disable();
		btnSearch.disable();
		btnSelectAll.disable();
		btnSelectBranch.disable();
		btnInvertSelect.disable();
		txtSearch.disable();
		cmbAttr.disable();
		btnTreeList.disable()
	};
	var _notEmptyTreeList = function() {
		if (displayMode == 0) {
			btnTreeControls.minusB.enable();
			btnTreeControls.plusB.enable();
			btnTreeControls.expandAllB.enable();
			btnTreeControls.collapseAllB.enable();
			btnTreeControls.oneB.enable();
			btnTreeControls.twoB.enable();
			btnTreeControls.threeB.enable();
			btnTreeControls.fourB.enable();
			btnTreeControls.fiveB.enable();
			btnSelectBranch.enable()
		}
		btnTreeControls.branchB.enable();
		btnSearch.enable();
		btnSelectAll.enable();
		btnInvertSelect.enable();
		txtSearch.enable();
		cmbAttr.enable();
		btnTreeList.enable()
	};
	var _selectSubNodes = function(node) {
		if (node.parentNode) {
			if (!node.isSelected() && node.parentNode.isExpanded()) {
				treeMain.getSelectionModel().select(node, null, true)
			} else {
				if (node.isSelected() && (!node.parentNode.isExpanded())) {
					treeMain.getSelectionModel().unselect(node)
				}
			}
			for ( var i = 0; i < node.childNodes.length; i++) {
				_selectSubNodes(node.childNodes[i])
			}
		}
	};
	var _doPasteOnGrid = function(horizVert) {
		var tmpRec, dims = {};
		if (chbInsertOtherElems.getValue()) {
			var type = "", values = [];
			if (rbServerDb.getValue()) {
				type = "serv_db";
				values[values.length] = storeServDb.getAt(
						storeServDb.find("id", servId)).get("name")
						+ "/" + dbName
			} else {
				if (rbCubeName.getValue()) {
					type = "cube";
					values[values.length] = cmbCube.getValue()
				} else {
					if (rbDimName.getValue()) {
						type = "dim_name";
						var listOfRecs = listOfDims.getSelectedRecords();
						for ( var i = 0; i < listOfRecs.length; i++) {
							values[values.length] = listOfRecs[i].get("name")
						}
					}
				}
			}
			if (Jedox.wss.app.activeBook) {
				var env = Jedox.wss.app.environment;
				var activeBook = Jedox.wss.app.activeBook;
				var upperLeftCoords = env.defaultSelection.getActiveRange()
						.getUpperLeft();
				var settings = [ upperLeftCoords.getX(),
						upperLeftCoords.getY(), servId, dbName, horizVert, type ];
				Jedox.wss.app.activeBook.cb("palo_handlerPutValuesOnGrid", [
						settings, values ])
			} else {
				var settings = [ 1, 1, servId, dbName, horizVert ];
				phpPaloServerOtherElems
						.handlerPutValuesOnGrid(settings, values)
			}
		} else {
			if (storeElemList.getCount() > 0) {
				for ( var i = 0; i < storeElemList.getCount(); i++) {
					tmpRec = storeElemList.getAt(i);
					if (!(dims[tmpRec.get("dim")])) {
						dims[tmpRec.get("dim")] = []
					}
					dims[tmpRec.get("dim")][dims[tmpRec.get("dim")].length] = [
							tmpRec.get("id"), tmpRec.get("attr"),
							((tmpRec.get("type") == "consolidated") ? "C" : "") ]
				}
			} else {
				if ((displayMode == 0) && (treeMain)) {
					var tmpNodes = treeMain.getSelectionModel()
							.getSelectedNodes();
					dims[dimName] = [];
					for ( var i = 0; i < tmpNodes.length; i++) {
						dims[dimName][i] = [
								tmpNodes[i].id,
								((cmbAttr.getValue() == "[none]".localize()) ? null
										: cmbAttr.getValue()),
								((tmpNodes[i].isLeaf()) ? "" : "C") ]
					}
				} else {
					if (displayMode == 1) {
						var tmpRecs = storeDims.getAt(
								storeDims.find("name", dimName))
								.get("dataView").getSelectedRecords();
						dims[dimName] = [];
						for ( var i = 0; i < tmpRecs.length; i++) {
							dims[dimName][i] = [
									tmpRecs[i].get("id"),
									tmpRecs[i].get("attr"),
									((tmpRecs[i].get("type") == "consolidated") ? "C"
											: "") ]
						}
					}
				}
			}
			if (Jedox.wss.app.activeBook) {
				var env = Jedox.wss.app.environment;
				var activeBook = Jedox.wss.app.activeBook;
				var upperLeftCoords = env.defaultSelection.getActiveRange()
						.getUpperLeft();
				var settings = [ upperLeftCoords.getX(),
						upperLeftCoords.getY(), servId, dbName, horizVert ];
				activeBook.cb("palo_handlerSelectElements",
						[ [ settings, dims ] ]);
				var cursorValue = activeBook.getCellValue(upperLeftCoords
						.getX(), upperLeftCoords.getY());
				env.cursorField.innerHTML = (cursorValue == undefined) ? ""
						: cursorValue;
				Jedox.wss.style.cellTransfer(env.cursorField)
			} else {
				var settings = [ 1, 1, servId, dbName, horizVert ];
				phpPaloServer.handlerSelectElements( [ settings, dims ])
			}
		}
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
	var _setOtherElemsRadios = function(radioId) {
		if (radioId == 2) {
			rbServerDb.setValue(false);
			rbCubeName.setValue(true);
			rbDimName.setValue(false)
		} else {
			if (radioId == 3) {
				rbServerDb.setValue(false);
				rbCubeName.setValue(false);
				rbDimName.setValue(true)
			} else {
				rbServerDb.setValue(true);
				rbCubeName.setValue(false);
				rbDimName.setValue(false)
			}
		}
	};
	var _resizeAll = function() {
		if (panelMain.rendered) {
			var lineH = 23;
			var marginSize = 3;
			var w = panelMain.getSize().width;
			var h = panelMain.getSize().height;
			var listW = _config.seWinW * (13 / 21);
			var listH = h - 9 * marginSize - 7 * lineH;
			var treeW = w - 2 * marginSize - listW - _config.widthMidButtons;
			var treeH = h - 6 * marginSize - 4 * lineH;
			panelDims.setSize(treeW, treeH);
			panelDims.setPosition(marginSize, 4 * marginSize + 3 * lineH);
			panellistView.setSize(listW, listH);
			panellistView.setPosition(marginSize + treeW
					+ _config.widthMidButtons, 3 * marginSize + 2 * lineH);
			panelTopBtns.setPosition(marginSize, 3 * marginSize + 2 * lineH);
			panelBottomTreeBtns.setPosition(marginSize + treeW
					+ _config.widthMidButtons, listH + 6 * marginSize + 4
					* lineH);
			if (panelbtnClose.rendered && btnClose.rendered
					&& panelbtnOk.rendered && btnOk.rendered
					&& panelbtnSelectBranch.rendered
					&& btnSelectBranch.rendered && panelbtnSearch.rendered
					&& btnSearch.rendered && panelbtnSelectAll.rendered
					&& btnSelectAll.rendered && panelbtnInvertSelect.rendered
					&& btnInvertSelect.rendered && panelbtnPasteVert.rendered
					&& btnPasteVert.rendered) {
				panelbtnSelectAll.setPosition(marginSize + treeW
						+ _config.widthMidButtons, listH + 5 * marginSize + 3
						* lineH);
				panelbtnSelectBranch.setPosition(w
						- btnOk.getEl().getBox().width
						- btnSelectBranch.getEl().getBox().width - 2
						* marginSize, listH + 5 * marginSize + 3 * lineH);
				panelbtnOk.setPosition(w - btnOk.getEl().getBox().width
						- marginSize, listH + 5 * marginSize + 3 * lineH);
				panelbtnInvertSelect.setPosition(w
						- btnOk.getEl().getBox().width
						- btnInvertSelect.getEl().getBox().width - 2
						* marginSize, listH + 6 * marginSize + 4 * lineH);
				panelbtnPasteVert.setPosition(w - btnOk.getEl().getBox().width
						- marginSize, listH + 6 * marginSize + 4 * lineH);
				panelbtnSearch.setPosition(w - btnClose.getEl().getBox().width
						- btnSearch.getEl().getBox().width - 2 * marginSize,
						treeH + 4 * marginSize + 2 * lineH);
				panelbtnClose.setPosition(w - btnClose.getEl().getBox().width
						- marginSize, treeH + 4 * marginSize + 2 * lineH)
			}
			panelbtnTreeList.setPosition(treeW - lineH, 3 * marginSize + 2
					* lineH);
			if (panelbtnClearList.rendered && btnClearList.rendered
					&& panelbtnAsc.rendered && btnAsc.rendered
					&& panelbtnDesc.rendered && btnDesc.rendered) {
				panelbtnClearList.setPosition(w
						- btnClearList.getEl().getBox().width - marginSize, 2
						* marginSize + 1 * lineH);
				panelbtnDesc.setPosition(w
						- btnClearList.getEl().getBox().width
						- btnDesc.getEl().getBox().width - 2 * marginSize, 2
						* marginSize + 1 * lineH);
				panelbtnAsc.setPosition(w - btnClearList.getEl().getBox().width
						- btnDesc.getEl().getBox().width
						- btnAsc.getEl().getBox().width - 3 * marginSize, 2
						* marginSize + 1 * lineH)
			}
			if (panelbtnMids.rendered) {
				panelbtnMids.setPosition(treeW + 2 * marginSize + 1, 2
						* marginSize + lineH
						+ (listH / 2 - panelbtnMids.getSize().height / 2))
			}
			panelcmbAttr.setPosition(marginSize, treeH + 5 * marginSize + 3
					* lineH);
			panelcmbDb.setPosition(marginSize, 2 * marginSize + lineH);
			lblcmbDb.setPosition(marginSize, 4 * marginSize);
			lblList.setPosition(marginSize + treeW + _config.widthMidButtons, 5
					* marginSize + lineH);
			lblBottomInfo.setPosition(marginSize + treeW
					+ _config.widthMidButtons, treeH + 5 * marginSize + 3
					* lineH);
			paneltxtSearch.setPosition(marginSize + treeW
					+ _config.widthMidButtons, treeH + 4 * marginSize + 2
					* lineH);
			if (paneltxtSearch.rendered && panelBottomTreeBtns.rendered) {
				txtSearch.setWidth(panelBottomTreeBtns.getSize().width)
			}
			panelchbShowHideB.setPosition(marginSize + treeW
					+ _config.widthMidButtons, listH + 4 * marginSize + 2
					* lineH);
			chbInsertOtherElems.setPosition(marginSize + treeW
					+ _config.widthMidButtons, marginSize);
			rbServerDb.setPosition(marginSize, 3 * marginSize + 2 * lineH);
			rbCubeName.setPosition(marginSize, 4 * marginSize + 3 * lineH);
			rbDimName.setPosition(marginSize, 6 * marginSize + 5 * lineH);
			panelcmbCube.setPosition(marginSize, 5 * marginSize + 4 * lineH);
			panellistOfDims.setPosition(marginSize, 7 * marginSize + 6 * lineH);
			if (panelbtnClose.rendered && btnClose.rendered) {
				cmbCube.setWidth(w - btnClose.getEl().getBox().width - 8
						* marginSize);
				panelcmbCube.setWidth(w - btnClose.getEl().getBox().width - 6
						* marginSize);
				panellistOfDims.setSize(w - btnClose.getEl().getBox().width - 8
						* marginSize, h - 9 * marginSize - 7 * lineH);
				panelbtnsChooseCubeType.setPosition(w
						- btnClose.getEl().getBox().width - marginSize, 5
						* marginSize + 4 * lineH)
			}
		}
	};
	this.init(servT, dbT, dimT, inEditDataT);
	this.show()
};