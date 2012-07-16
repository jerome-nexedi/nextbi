Jedox.wss.palo.ChooseElement = function(confDataT) {
	var phpPaloServerCbHandlers = {
		getDimStringAttrs : function(result) {
			if (!result[2]) {
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
				if (editMode && (editData.length > 0)) {
					if (editData[0][1] != null) {
						selectedAttr = storeAttrs.find("name", editData[0][1])
					}
				}
				cmbAttr.setValue(storeAttrs.getAt(selectedAttr).get("name"))
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
		}
	};
	var _config = Jedox.wss.palo.config;
	var that = this;
	var phpPaloServer = new Palo(phpPaloServerCbHandlers);
	var servId, dbName, dimName, parentObject;
	var win, panelMain, panelTopBtns, panelBottomTreeBtns, panelbtnSearch, panelbtnOk, panelbtnClose, paneltxtSearch, panelcmbAttr;
	var treeMain, treeRoot, expandMoreLevels, treeLevel, syncCounter, treeMaxLevelReached, expandAllLevels, hasOnlyLeaves;
	var txtSearch, cmbAttr, btnTreeControls, btnOk, btnClose, btnSearch;
	var selectedAttr, selectedElem, editMode, editData, editY, editPasteViewId, dblClickData;
	var AttrRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var storeAttrs = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	this.init = function(confData) {
		workingMode = confData.working_mode;
		parentObject = confData.parent_object;
		servId = confData.serv_id;
		dbName = confData.db_name;
		dimName = confData.dim_name;
		editPasteViewId = confData.pasteview_id;
		editY = confData.edit_y;
		dblClickData = confData.dblclick_data;
		treeLevel = 0;
		syncCounter = 0;
		treeMaxLevelReached = 0;
		selectedAttr = 0;
		selectedElem = null;
		expandAllLevels = false;
		hasOnlyLeaves = false;
		editMode = ((confData.edit_data) ? true : false);
		if (editMode) {
			editData = confData.edit_data
		} else {
			editData = new Array()
		}
		btnTreeControls = {
			plusB : new Ext.Button( {
				cls : "modellerImageButton",
				style : "vertical-align:bottom;",
				iconCls : "palo_icon_plus",
				handler : function() {
					treeLevel++;
					_expandTreeToLevel(treeRoot)
				}
			}),
			minusB : new Ext.Button( {
				cls : "modellerImageButton",
				iconCls : "palo_icon_minus",
				style : "vertical-align:bottom;",
				handler : function() {
					if (treeLevel > 0) {
						treeLevel--;
						_expandTreeToLevel(treeRoot)
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
						_expandTreeToLevel(treeRoot)
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
						_expandTreeToLevel(treeRoot)
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
						_expandTreeToLevel(treeRoot)
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
						_expandTreeToLevel(treeRoot)
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
						_expandTreeToLevel(treeRoot)
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
					btnTreeControls.fiveB ]
		});
		btnSearch = new Ext.Button( {
			text : "Search & Select".localize(),
			cls : "modellerButtonWithMargin",
			listeners : {
				click : function() {
					if (txtSearch.getValue() != "") {
						phpPaloServer.searchElem(servId, dbName, dimName,
								txtSearch.getValue(),
								((selectedAttr == 0) ? null : storeAttrs.getAt(
										selectedAttr).get("name")))
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
		btnOk = new Ext.Button( {
			text : "OK".localize(),
			ctCls : "stdButtons",
			listeners : {
				click : _btnOkHandler
			}
		});
		btnClose = new Ext.Button( {
			text : "Close".localize(),
			ctCls : "stdButtons",
			listeners : {
				click : function() {
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
		panelbtnClose = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnClose ]
		});
		cmbAttr = new Ext.form.ComboBox(
				{
					store : storeAttrs,
					bodyStyle : "background-color: transparent;",
					typeAhead : false,
					selectOnFocus : true,
					hideLabel : true,
					editable : false,
					forceSelection : true,
					triggerAction : "all",
					mode : "local",
					listeners : {
						select : function(combo, record, index) {
							if (index != selectedAttr) {
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
											+ "&attr="
											+ record.get("name")
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
								treeRoot.reload();
								selectedAttr = index
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
			autoWidth : true,
			autoHeight : true,
			items : [ cmbAttr ]
		});
		txtSearch = new Ext.form.TextField( {
			hideLabel : true
		});
		paneltxtSearch = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ txtSearch ]
		});
		treeMain = new Ext.tree.TreePanel(
				{
					cls : "x-tree-noicon",
					selModel : ((workingMode == 4) ? new Ext.tree.MultiSelectionModel()
							: new Ext.tree.DefaultSelectionModel()),
					border : false,
					autoScroll : true,
					animate : false,
					enableDD : false,
					containerScroll : true,
					rootVisible : false,
					listeners : {
						contextmenu : function(node, e) {
							e.stopEvent()
						},
						click : function(node, e) {
							selectedElem = node.id
						},
						dblclick : function(node, e) {
							selectedElem = node.id;
							_btnOkHandler()
						}
					},
					loader : new Ext.tree.TreeLoader(
							{
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
										+ dimName
										+ ((editMode && ((editData.length > 0) && (editData[0][1] != null))) ? "&attr="
												+ editData[0][1]
												: ""),
								listeners : {
									beforeload : function(thisL, node, cb) {
										if (expandMoreLevels || expandAllLevels) {
											if (!hasOnlyLeaves) {
												hasOnlyLeaves = true
											}
											syncCounter++
										}
									},
									load : function(thisS, node, response) {
										for ( var i = 0; i < node.childNodes.length; i++) {
											node.childNodes[i].ui.onDblClick = function(
													e) {
												e.preventDefault();
												if (this.disabled) {
													return
												}
												this.fireEvent("dblclick",
														this.node, e)
											}
										}
										if (node.getDepth() > treeMaxLevelReached) {
											treeMaxLevelReached = node
													.getDepth()
										}
										if (syncCounter > 0) {
											if (hasOnlyLeaves) {
												node
														.eachChild(function(
																chNode) {
															if (!chNode
																	.isLeaf()) {
																hasOnlyLeaves = false;
																return false
															}
														})
											}
											syncCounter--;
											if (expandMoreLevels) {
												_expandTreeToLevel(node)
											} else {
												if ((expandAllLevels)
														&& (syncCounter == 0)
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
							})
				});
		treeRoot = new Ext.tree.AsyncTreeNode( {
			text : "Test",
			draggable : false,
			id : "root"
		});
		treeMain.setRootNode(treeRoot);
		panelMain = new Ext.Panel( {
			id : "ce_mainPanel",
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			anchor : "100% 100%",
			monitorResize : true,
			listeners : {
				resize : _resizeAll
			},
			items : [ panelTopBtns, treeMain, panelBottomTreeBtns,
					panelbtnSearch, panelbtnOk, panelbtnClose, panelcmbAttr,
					paneltxtSearch ]
		});
		win = new Ext.Window(
				{
					id : "chooseelementWizardWindow",
					layout : "fit",
					cls : "default-format-window",
					title : "Choose Element".localize(),
					width : _config.ceWinW,
					height : _config.ceWinH,
					minWidth : _config.ceWinW,
					minHeight : _config.ceWinH,
					closeAction : "close",
					autoDestroy : true,
					plain : true,
					modal : true,
					resizable : true,
					listeners : {
						activate : _resizeAll,
						close : function() {
							if (!parentObject) {
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
							}
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.chooseElement)
						}
					},
					items : [ panelMain ]
				});
		if (editMode && (editData.length > 0)) {
			phpPaloServer.searchElem(servId, dbName, dimName, editData[0][0],
					null)
		}
	};
	this.show = function() {
		if (win) {
			phpPaloServer.getDimStringAttrs(servId, dbName, dimName);
			if (!parentObject) {
				Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
				Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
			}
			win.show()
		}
	};
	var _btnOkHandler = function() {
		if (selectedElem != null) {
			if (workingMode == 1) {
				var tmpNode = treeMain.getSelectionModel().getSelectedNode();
				if (tmpNode.id != editData[0]) {
					var tmpDim = [
							dimName,
							[ [
									tmpNode.id,
									((cmbAttr.getValue() == "[none]".localize()) ? null
											: cmbAttr.getValue()),
									((tmpNode.isLeaf()) ? "" : "C") ] ] ];
					var settings = [ dblClickData.c, dblClickData.r, servId,
							dbName, editPasteViewId, editY ];
					if (Jedox.wss.app.activeBook) {
						Jedox.wss.app.activeBook.cb(
								"palo_handlerChooseElements", [ [ settings,
										tmpDim ] ])
					} else {
						phpPaloServer
								.handlerChooseElements( [ settings, tmpDim ])
					}
				}
			} else {
				if (workingMode == 2) {
					var tmpNode = treeMain.getSelectionModel()
							.getSelectedNode();
					parentObject.cb_fnc(dimName, [ [
							tmpNode.id,
							((cmbAttr.getValue() == "[none]".localize()) ? null
									: cmbAttr.getValue()),
							((tmpNode.isLeaf()) ? "" : "C") ] ])
				} else {
					if (workingMode == 4) {
						var tmpElems = [], tmpNodes = treeMain
								.getSelectionModel().getSelectedNodes();
						for ( var i = 0; i < tmpNodes.length; i++) {
							tmpElems[tmpElems.length] = [
									tmpNodes[i].attributes._num_id,
									tmpNodes[i].id ]
						}
						parentObject.cb_fnc(tmpElems)
					} else {
						if (workingMode == 8) {
							var tmpNode = treeMain.getSelectionModel()
									.getSelectedNode();
							if (tmpNode.id != editData[0]) {
								var tmpDim = {};
								tmpDim[dimName] = [ [
										tmpNode.id,
										((cmbAttr.getValue() == "[none]"
												.localize()) ? null : cmbAttr
												.getValue()),
										((tmpNode.isLeaf()) ? "" : "C") ] ];
								var settings = [ dblClickData.c,
										dblClickData.r, servId, dbName, 0 ];
								if (Jedox.wss.app.activeBook) {
									Jedox.wss.app.activeBook.cb(
											"palo_handlerSelectElements", [ [
													settings, tmpDim ] ])
								} else {
									phpPaloServer.handlerSelectElements( [
											settings, tmpDim ])
								}
							}
						}
					}
				}
			}
		} else {
			if (parentObject) {
				parentObject.cb_fnc(dimName, new Array())
			}
		}
		win.close()
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
			treeMaxLevelReached = (((node.childNodes.length == 0) && ((node
					.getDepth() - 1) >= treeMaxLevelReached)) ? node.getDepth() - 1
					: node.getDepth())
		}
		if (node.getDepth() == treeLevel) {
			node.collapseChildNodes(true)
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
	var _showErrorMsg = function(message) {
		Ext.MessageBox.show( {
			title : "Error".localize(),
			msg : message,
			buttons : Ext.Msg.OK,
			icon : Ext.MessageBox.ERROR
		})
	};
	var _resizeAll = function() {
		if (panelMain.rendered) {
			var lineH = 23;
			var marginSize = 3;
			var w = panelMain.getSize().width;
			var h = panelMain.getSize().height;
			var treeW = w - 2 * marginSize;
			var treeH = h - 7 * marginSize - 5 * lineH;
			treeMain.setSize(treeW, treeH);
			treeMain.setPosition(marginSize, 2 * marginSize + lineH);
			panelTopBtns.setPosition(marginSize, marginSize);
			panelBottomTreeBtns.setPosition(marginSize, treeH + 5 * marginSize
					+ 3 * lineH);
			if (panelbtnSearch.rendered && btnSearch.rendered) {
				panelbtnSearch.setPosition(w - marginSize
						- btnSearch.getEl().getBox().width, treeH + 4
						* marginSize + 2 * lineH)
			}
			if (panelbtnClose.rendered && panelbtnOk.rendered
					&& btnClose.rendered && btnOk.rendered) {
				panelbtnClose.setPosition(w - btnClose.getEl().getBox().width
						- marginSize, h - lineH - marginSize);
				panelbtnOk.setPosition(w - btnClose.getEl().getBox().width
						- btnOk.getEl().getBox().width - 2 * marginSize, h
						- lineH - marginSize)
			}
			panelcmbAttr
					.setPosition(marginSize, treeH + 3 * marginSize + lineH);
			paneltxtSearch.setPosition(marginSize, treeH + 4 * marginSize + 2
					* lineH);
			if (cmbAttr.rendered && btnSearch.rendered) {
				cmbAttr.setWidth(w - 3 * marginSize
						- panelbtnSearch.getEl().getBox().width);
				txtSearch.setWidth(w - 3 * marginSize
						- panelbtnSearch.getEl().getBox().width)
			}
		}
	};
	this.init(confDataT);
	this.show()
};