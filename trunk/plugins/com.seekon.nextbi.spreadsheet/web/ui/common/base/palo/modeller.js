Jedox.wss.palo.openModellerDialog = function() {
	var mod = new Jedox.wss.palo.Modeller();
	mod.showDialog()
};
Jedox.wss.palo.Modeller = function() {
	var InlineEditor = function(cfg, field) {
		InlineEditor.superclass.constructor.call(this, field
				|| new Ext.form.TextField( {
					allowBlank : true,
					growMin : 290,
					growMax : 290,
					grow : true,
					selectOnFocus : true
				}), cfg)
	};
	Ext.extend(InlineEditor, Ext.Editor, {
		alignment : "tl-tl",
		hideEl : false,
		cls : "x-small-editor",
		shim : false,
		completeOnEnter : true,
		cancelOnEsc : true,
		init : function(view) {
			this.view = view
		}
	});
	var phpPaloServerCbHandlers = {
		getServList : function(result) {
			if (result[0]) {
				storeServDb.loadData(result[1]);
				if (result[2]) {
					preselectedServDb = result[2]
				}
				_refillComboBox()
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
			numOfRequests -= (numOfRequests == 0) ? 0 : 1;
			if (preselectedServDb && (numOfRequests == 0) && (cmbDbState == -1)) {
				cmbDbState = storeServDb
						.findBy(function(rec, id) {
							return (rec.get("name") == preselectedServDb[1]
									&& rec.get("parent_id") == preselectedServDb[0] && rec
									.get("type") == "database")
						})
			}
			if ((numOfRequests == 0)
					&& ((cmbDbState == -1) || (storeServDb.getCount() <= cmbDbState))) {
				cmbDbState = storeServDb.find("type", "database")
			}
			if ((numOfRequests == 0) && (cmbDbState != -1)) {
				tmpIndex = cmbDbState;
				cmbDbState = -1;
				cmbDb.setValue(storeServDb.getAt(tmpIndex).get("id"));
				_cmbDbSelectionHandler(cmbDb, storeServDb.getAt(tmpIndex),
						tmpIndex)
			}
		},
		setPreselectServDb : function(result) {
			if (!result[0]) {
				_showErrorMsg(result[1])
			}
		},
		getDims : function(result) {
			if (!result[2]) {
				for ( var i = 0; i < result[1].length; i++) {
					storeDims.add(new DimRecord( {
						name : result[1][i]
					}))
				}
				for ( var i = 0; i < storeDims.getCount(); i++) {
					dVs[i] = new Jedox.wss.palo.DimensionViewer();
					dVs[i].init(servId, dbName, storeDims.getAt(i), dataMode);
					tabs.add(dVs[i].getView())
				}
				for ( var i = 0; i < storeDims.getCount(); i++) {
					dVs[i].bloomIn(that)
				}
			} else {
				_showErrorMsg(result[3])
			}
			_checkEmptyDimView()
		},
		getCubes : function(result) {
			if (!result[2]) {
				var tmpTreeNode;
				for ( var i = 0; i < result[1].length; i++) {
					tmpTreeNode = new Ext.tree.TreeNode( {
						text : result[1][i][0]
					});
					rootNodeCubes.appendChild(tmpTreeNode);
					for ( var j = 0; j < result[1][i][1].length; j++) {
						tmpTreeNode.appendChild(new Ext.tree.TreeNode( {
							leaf : true,
							text : result[1][i][1][j]
						}))
					}
				}
			} else {
				_showErrorMsg(result[3])
			}
			if (!rootNodeCubes.hasChildNodes()) {
				rootNodeCubes.appendChild(new Ext.tree.TreeNode( {
					text : 'Click on the "New Cube" button to add Cube'
							.localize().concat("...")
				}))
			}
		},
		addDim : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				var selectedDims = viewLeft.getSelectedRecords();
				if (!result[2]) {
					var dimV = new Jedox.wss.palo.DimensionViewer();
					dimV.init(servId, dbName, selectedDims[0], dataMode);
					tabs.add(dimV.getView());
					dimV.bloomIn(that);
					_hideWaitMsg();
					timerOut = true;
					var t = setTimeout(function() {
						viewLeft.retainFocus()
					}, 0)
				} else {
					_hideWaitMsg();
					timerOut = true;
					storeDims.remove(selectedDims[0]);
					_showErrorMsg(result[3])
				}
			}
			_checkEmptyDimView()
		},
		renameDim : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					var dimNewName = result[0];
					storeDims.getAt(elemActive).set("name", dimNewName);
					tabs.getComponent(elemActive + 1).setTitle(dimNewName);
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
		},
		deleteDim : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					var tmpDim = storeDims.getAt(elemActive);
					storeDims.remove(tmpDim);
					var dimTab = tabs.getComponent(elemActive + 1).getId();
					tabs.remove(dimTab);
					_setOpMode("gridView");
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
			_checkEmptyDimView()
		},
		deleteCube : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					if (elemActive.getDepth() == 1) {
						rootNodeCubes.removeChild(elemActive)
					}
					_setOpMode("gridView");
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
			if (!rootNodeCubes.hasChildNodes()) {
				rootNodeCubes.appendChild(new Ext.tree.TreeNode( {
					text : 'Click on the "New Cube" button to add Cube'
							.localize().concat("...")
				}))
			}
		},
		renameCube : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					elemActive.setText(result[0]);
					_setOpMode("gridView");
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
		},
		getDimInfo : function(result) {
			if (!result[2]) {
				_showDimInfoWin(result)
			} else {
				_showErrorMsg(result[3])
			}
		},
		getCubeInfo : function(result) {
			if (!result[2]) {
				_showCubeInfoWin(result)
			} else {
				_showErrorMsg(result[3])
			}
		},
		connDisconnServer : function(result) {
			if (result[0]) {
				var tmpRec = storeServDb.getAt(storeServDb
						.findBy(function(record, id) {
							return ((record.get("id") == servId) && (record
									.get("type") == "server"))
						}));
				if (tmpRec) {
					if (tmpRec.get("connected") == 1) {
						tmpRec.set("connected", 0);
						btnCombos.disconnectB.setIconClass("palo_icon_turnon");
						var tmpRecs = storeServDb.query("parent_id", servId);
						for ( var i = (tmpRecs.getCount() - 1); i >= 0; i--) {
							storeServDb.remove(tmpRecs.get(i))
						}
					} else {
						tmpRec.set("connected", 1);
						btnCombos.disconnectB.setIconClass("palo_icon_turnoff");
						_refillComboBox();
						cmbDbState++
					}
				}
			} else {
				_showErrorMsg(result[1])
			}
		}
	};
	var servId, dbName, preselectedServDb = null;
	var that = this;
	var phpPaloServer = new Palo(phpPaloServerCbHandlers);
	var _config = Jedox.wss.palo.config;
	var dataMode, currDbId;
	var panelMain, panelCombo, panelComboButtons, panelBottomLeft, panelChooseView, panelCubesBtns, panelCloseButton, panelDataViewLeft, panelDataViewRight;
	var viewLeft, dvOnEnterPlugin, viewRight, btnAccts, btnClose, btnCombos, btnChooseViews, btnCubes, lblRight, lblLeft, lblCombo, cmbDb, cmbDbState;
	var opMode;
	var win, form, formDB, tabs;
	var dVs = new Array();
	var timerOut, timer, timerOutTime = 10000;
	var conn = new Ext.data.Connection();
	var numOfRequests;
	var elemActive;
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
		} ]
	});
	var rootNodeCubes = new Ext.tree.TreeNode( {
		text : "root",
		draggable : false,
		id : "root"
	});
	var DimRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var storeDims = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	var RecordLeftView = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var _init = function() {
		opMode = "gridView";
		cmbDbState = -1;
		dataMode = 0;
		numOfRequests = 0;
		dvOnEnterPlugin = new InlineEditor(
				{
					dataIndex : "name",
					labelSelector : "div.row-modeller",
					listeners : {
						beforecomplete : function(thisEd, value, startValue) {
							if (value != startValue) {
								if (value && (value != "")) {
									if ((startValue == "New Dimension"
											.localize())
											&& (elemActive == (storeDims
													.getCount() - 1))) {
										if (Jedox.wss.palo.utils.checkPaloName(
												value, "dim")) {
											thisEd.activeRecord.set(
													thisEd.dataIndex, value);
											phpPaloServer.addDim(servId,
													dbName, value);
											_showWaitMsg("_msg: new Dim"
													.localize(), "Storing"
													.localize().concat("..."));
											_setOpMode("elemLeftSelected")
										} else {
											Ext.MessageBox
													.show( {
														title : "Information"
																.localize(),
														msg : ("Name"
																.localize())
																.concat(
																		' "',
																		value
																				.replace(
																						"<",
																						"&lt;"),
																		'" ',
																		"is not allowed"
																				.localize()),
														buttons : Ext.Msg.OK,
														icon : Ext.MessageBox.INFO
													});
											storeDims
													.remove(thisEd.activeRecord);
											elemActive = storeDims.getCount() - 1;
											viewLeft.select(elemActive);
											_setOpMode("elemLeftSelected")
										}
									} else {
										if (Jedox.wss.palo.utils.checkPaloName(
												value, "dim")) {
											phpPaloServer.renameDim(servId,
													dbName, startValue, value);
											_showWaitMsg("_msg: rename Dim"
													.localize(), "Storing"
													.localize().concat("..."))
										} else {
											Ext.MessageBox
													.show( {
														title : "Information"
																.localize(),
														msg : ("Name"
																.localize())
																.concat(
																		' "',
																		value
																				.replace(
																						"<",
																						"&lt;"),
																		'" ',
																		"is not allowed"
																				.localize()),
														buttons : Ext.Msg.OK,
														icon : Ext.MessageBox.INFO
													})
										}
									}
								} else {
									if (value == "") {
										if (startValue != "New Dimension"
												.localize()) {
											Ext.MessageBox
													.show( {
														title : "Delete Dimension"
																.localize(),
														msg : "_msg: Delete Dimension"
																.localize(),
														buttons : Ext.MessageBox.OKCANCEL,
														fn : _deleteDim,
														icon : Ext.MessageBox.QUESTION
													})
										} else {
											storeDims
													.remove(thisEd.activeRecord);
											elemActive = storeDims.getCount() - 1;
											viewLeft.select(elemActive);
											_setOpMode("elemLeftSelected")
										}
									}
								}
							} else {
								if (startValue == "New Dimension".localize()) {
									storeDims.remove(thisEd.activeRecord);
									elemActive = storeDims.getCount() - 1;
									viewLeft.select(elemActive);
									_setOpMode("elemLeftSelected")
								}
							}
							_checkEmptyDimView()
						},
						complete : function() {
							viewLeft.retainFocus()
						},
						canceledit : function(thisEd, value, startValue) {
							if (startValue == "New Dimension".localize()) {
								thisEd.activeRecord.store
										.remove(thisEd.activeRecord);
								elemActive = storeDims.getCount() - 1;
								viewLeft.select( [ elemActive ])
							}
							_checkEmptyDimView();
							viewLeft.retainFocus()
						}
					}
				});
		viewLeft = new Jedox.wss.palo.utils.OnEnterDataViewClass(
				{
					plugins : [ dvOnEnterPlugin ],
					itemSelector : "div.row-modeller",
					style : "overflow:auto",
					singleSelect : true,
					store : storeDims,
					cls : "modellerDataViewSelect",
					tpl : new Ext.XTemplate('<tpl for=".">',
							'<div class="row-modeller">',
							"<span>&#160;{name}</span>", "</div>", "</tpl>"),
					listeners : {
						click : function(dataView, index, node, evnt) {
							if ((storeDims.getCount() != 1)
									|| (storeDims.getAt(index).get("name") != "Hit RETURN to add Dimension"
											.localize().concat("..."))) {
								_singleClickLeftViewHandler(dataView, index,
										node, evnt)
							}
						},
						dblclick : function(dataView, index, node, evnt) {
							if ((storeDims.getCount() != 1)
									|| (storeDims.getAt(index).get("name") != "Hit RETURN to add Dimension"
											.localize().concat("..."))) {
								_dblClickLeftViewHandler(dataView, index, node,
										evnt)
							}
						},
						contextmenu : function(dataView, index, node, e) {
							if ((storeDims.getCount() != 1)
									|| (storeDims.getAt(index).get("name") != "Hit RETURN to add Dimension"
											.localize().concat("..."))) {
								_showMenuLeftView(dataView, index, node, e)
							}
						}
					}
				});
		viewLeft.on("onkeyenter", function() {
			var timer = setTimeout(function() {
				_newDim()
			}, 0)
		});
		storeDims.add(new DimRecord( {
			name : "Hit RETURN to add Dimension".localize().concat("...")
		}));
		panelDataViewLeft = new Ext.Panel( {
			layout : "fit",
			items : [ viewLeft ]
		});
		viewRight = new Ext.tree.TreePanel(
				{
					cls : "x-tree-noicon",
					border : false,
					autoScroll : true,
					animate : false,
					enableDD : false,
					containerScroll : true,
					rootVisible : false,
					bodyStyle : "background-color: transparent;",
					listeners : {
						click : function(node, evnt) {
							if (node.text != 'Click on the "New Cube" button to add Cube'
									.localize().concat("...")) {
								_singleClickRightViewHandler(node, evnt)
							}
						},
						contextmenu : function(node, e) {
							if ((node.getDepth() == 1)
									&& (node.text != 'Click on the "New Cube" button to add Cube'
											.localize().concat("..."))) {
								_showMenuRightView(node, e)
							}
						}
					}
				});
		viewRight.setRootNode(rootNodeCubes);
		rootNodeCubes.appendChild(new Ext.tree.TreeNode( {
			text : 'Click on the "New Cube" button to add Cube'.localize()
					.concat("...")
		}));
		panelDataViewRight = new Ext.Panel( {
			layout : "fit",
			items : [ viewRight ]
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
		panelCombo = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ cmbDb ]
		});
		btnCombos = {
			disconnectB : new Ext.Button( {
				iconCls : "palo_icon_turnoff",
				cls : "modellerImageButton",
				handler : function() {
					var tmpRec = storeServDb.getAt(storeServDb.findBy(function(
							record, id) {
						return ((record.get("id") == servId) && (record
								.get("type") == "server"))
					}));
					if (tmpRec) {
						phpPaloServer.connDisconnServer( [ servId,
								tmpRec.get("connected") ? 0 : 1 ])
					}
				}
			}),
			newB : new Ext.Button( {
				iconCls : "palo_icon_db",
				cls : "modellerImageButton",
				handler : function() {
					if (Jedox.wss.palo.workIn) {
						Jedox.wss.app.load(
								Jedox.wss.app.dynJSRegistry.paloWizard, [ {
									fn : function() {
										storeServDb.removeAll();
										phpPaloServer.getServList()
									}
								} ])
					}
				}
			})
		};
		panelComboButtons = new Ext.Panel(
				{
					border : false,
					bodyStyle : "background-color: transparent; vertical-align: middle;",
					layout : "table",
					width : 58,
					height : 40,
					autoHeight : true,
					items : [ btnCombos.disconnectB, btnCombos.newB ]
				});
		lblCombo = new Ext.form.MiscField( {
			value : "Choose Server/Database".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		lblLeft = new Ext.form.MiscField( {
			value : "Dimensions and Global Subsets".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		lblRight = new Ext.form.MiscField( {
			value : "Cubes".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		btnAccts = {
			newB : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_cl_create",
				cls : "modellerImageButton",
				handler : _newDim
			}),
			deleteB : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_cl_delete",
				cls : "modellerImageButton",
				handler : function() {
					Ext.MessageBox.show( {
						title : "Delete Dimension".localize(),
						msg : "_msg: Delete Dimension".localize(),
						buttons : Ext.MessageBox.OKCANCEL,
						fn : _deleteDim,
						icon : Ext.MessageBox.QUESTION
					})
				}
			}),
			editB : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_cl_edit",
				cls : "modellerImageButton",
				handler : _renameDim
			})
		};
		panelBottomLeft = new Ext.Panel(
				{
					border : false,
					bodyStyle : "background-color: transparent; vertical-align: middle;",
					layout : "table",
					width : 200,
					height : 40,
					autoHeight : true,
					items : [ btnAccts.newB, btnAccts.deleteB, btnAccts.editB ]
				});
		btnChooseViews = {
			metaDataB : new Ext.Button( {
				disabled : true,
				iconCls : "icon_palo_cube",
				cls : "modellerImageButton",
				handler : function() {
					_setDataMode(0);
					if (cmbDb.disabled) {
						cmbDb.enable();
						cmbDb.setValue(currDbId);
						dbName = storeServDb.getAt(
								storeServDb.find("id", currDbId)).get("name")
					}
					_loadDatabase(storeServDb.getAt(storeServDb.find("id",
							cmbDb.getValue())));
					_setOpMode("gridView")
				}
			}),
			attribsB : new Ext.Button( {
				disabled : true,
				iconCls : "icon_palo_table",
				cls : "modellerImageButton",
				handler : function() {
					_setDataMode(2);
					if (cmbDb.disabled) {
						cmbDb.enable();
						cmbDb.setValue(currDbId);
						dbName = storeServDb.getAt(
								storeServDb.find("id", currDbId)).get("name")
					}
					_loadDatabase(storeServDb.getAt(storeServDb.find("id",
							cmbDb.getValue())));
					_setOpMode("gridView")
				}
			}),
			usersB : new Ext.Button( {
				disabled : true,
				iconCls : "icon_palo_user",
				cls : "modellerImageButton",
				handler : function() {
					_setDataMode(1);
					currDbId = cmbDb.getValue();
					var tmpRec = storeServDb.getAt(storeServDb.find("id",
							currDbId));
					dbName = "System";
					cmbDb.setValue(dbName);
					cmbDb.disable();
					var tmpRec = new ServDbRecord( {
						id : "id-system",
						parent_id : tmpRec.get("parent_id"),
						type : "database",
						name : "System"
					});
					_loadDatabase(tmpRec);
					_setOpMode("gridView")
				}
			})
		};
		panelChooseView = new Ext.Panel(
				{
					border : false,
					bodyStyle : "background-color: transparent; vertical-align: middle;",
					layout : "table",
					width : 83,
					height : 40,
					autoHeight : true,
					items : [ btnChooseViews.metaDataB,
							btnChooseViews.attribsB, btnChooseViews.usersB ]
				});
		btnCubes = {
			newB : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_newcube",
				cls : "modellerImageButton",
				handler : function() {
					if (Jedox.wss.palo.workIn) {
						Jedox.wss.app.load(
								Jedox.wss.app.dynJSRegistry.cubeWizard, [ that,
										servId, dbName ])
					}
				}
			}),
			delB : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_deletecube",
				cls : "modellerImageButton",
				handler : function() {
					Ext.MessageBox.show( {
						title : "Delete Cube".localize(),
						msg : "_msg: Delete Cube".localize(),
						buttons : Ext.MessageBox.OKCANCEL,
						fn : _delCube,
						icon : Ext.MessageBox.QUESTION
					})
				}
			}),
			editB : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_fxcube",
				cls : "modellerImageButton",
				handler : function() {
					if (Jedox.wss.palo.workIn) {
						Jedox.wss.app.load(
								Jedox.wss.app.dynJSRegistry.ruleEditor, [
										servId,
										dbName,
										viewRight.getSelectionModel()
												.getSelectedNode().text ])
					}
				}
			})
		};
		panelCubesBtns = new Ext.Panel(
				{
					border : false,
					bodyStyle : "background-color: transparent; vertical-align: middle;",
					layout : "table",
					width : 150,
					height : 40,
					autoHeight : true,
					items : [ btnCubes.newB, btnCubes.delB, btnCubes.editB ]
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
		panelCloseButton = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnClose ]
		});
		panelMain = new Ext.Panel( {
			title : "Database".localize(),
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			anchor : "100% 100%",
			monitorResize : true,
			listeners : {
				resize : _resizeAll,
				activate : _resizeAll
			},
			items : [ panelDataViewLeft, lblCombo, panelCombo,
					panelComboButtons, lblLeft, lblRight, panelDataViewRight,
					panelBottomLeft, panelChooseView, panelCubesBtns,
					panelCloseButton ]
		});
		tabs = new Ext.TabPanel( {
			minTabWidth : 115,
			tabWidth : 135,
			enableTabScroll : true,
			bodyStyle : "padding: 5px 5px 0px; background-color: transparent;",
			layoutOnTabChange : true,
			items : [ panelMain ]
		});
		win = new Ext.Window(
				{
					layout : "fit",
					width : _config.panelW,
					height : _config.panelH,
					minWidth : _config.panelW,
					minHeight : _config.panelH,
					closeAction : "close",
					autoDestroy : true,
					plain : true,
					modal : true,
					cls : "default-format-window",
					title : "PALO Modeller".localize(),
					listeners : {
						close : function() {
							if (Jedox.wss.palo.workIn == Jedox.wss.palo.GRID) {
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
							}
							if (Jedox.wss.palo.workIn) {
								Jedox.wss.app
										.unload(Jedox.wss.app.dynJSRegistry.modeller)
							}
							phpPaloServer.setPreselectServDb(servId, dbName)
						}
					},
					items : [ tabs ]
				});
		if (Jedox.wss.palo.workIn == Jedox.wss.palo.GRID) {
			Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
			Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
		}
		tabs.setActiveTab(panelMain)
	};
	this.getView = function() {
		return panelMain
	};
	this.getDialogPanel = function() {
		return tabs
	};
	this.setPanelMode = function() {
		btnClose.hide()
	};
	this.resize = function() {
		_resizeAll()
	};
	this.showDialog = function() {
		win.show();
		_resizeAll()
	};
	this.refreshServerList = function() {
		storeServDb.removeAll();
		phpPaloServer.getServList()
	};
	this.refreshDbList = function() {
		var tmpRec;
		for ( var i = (storeServDb.getCount() - 1); i >= 0; i--) {
			tmpRec = storeServDb.getAt(i);
			if ((tmpRec.get("type") == "database")
					&& (tmpRec.get("parent_id") == servId)) {
				storeServDb.remove(tmpRec)
			}
		}
		phpPaloServer.getDBs(servId)
	};
	this.addNewCube = function(cubeName, dims) {
		if (rootNodeCubes.firstChild.text == 'Click on the "New Cube" button to add Cube'
				.localize().concat("...")) {
			rootNodeCubes.removeChild(rootNodeCubes.firstChild, true)
		}
		var tmpTreeNode = new Ext.tree.TreeNode( {
			text : cubeName
		});
		rootNodeCubes.appendChild(tmpTreeNode);
		for ( var i = 0; i < dims.length; i++) {
			tmpTreeNode.appendChild(new Ext.tree.TreeNode( {
				leaf : true,
				text : dims[i]
			}))
		}
	};
	var _newDim = function() {
		var newIndex = storeDims.getCount();
		if ((newIndex == 1)
				&& (storeDims.getAt(0).get("name") == "Hit RETURN to add Dimension"
						.localize().concat("..."))) {
			newIndex = 0;
			storeDims.getAt(0).set("name", "New Dimension".localize())
		} else {
			storeDims.add(new DimRecord( {
				name : "New Dimension".localize()
			}))
		}
		viewLeft.select(newIndex);
		elemActive = newIndex;
		var endIndex = storeDims.getCount() - 1;
		var newNode = viewLeft.getNode(endIndex);
		newNode.scrollIntoView(viewLeft.body, false);
		var newRec = viewLeft.getRecord(newNode);
		dvOnEnterPlugin.activeRecord = newRec;
		dvOnEnterPlugin.startEdit(newNode,
				newRec.data[dvOnEnterPlugin.dataIndex])
	};
	var _deleteDim = function(btn) {
		if ((btn == "ok") && (opMode == "elemLeftSelected")) {
			phpPaloServer.deleteDim(servId, dbName, storeDims.getAt(elemActive)
					.get("name"));
			_showWaitMsg("_msg: del Dim".localize(), "Storing".localize()
					.concat("..."))
		}
	};
	var _renameDim = function() {
		var sIndexes = viewLeft.getSelectedIndexes();
		if (sIndexes.length > 0) {
			var firstNode = viewLeft.getNode(sIndexes[0]);
			var scrollToElem = Ext.get(firstNode);
			firstNode.scrollIntoView(viewLeft.body, false);
			var firstRec = viewLeft.getRecord(firstNode);
			dvOnEnterPlugin.activeRecord = firstRec;
			dvOnEnterPlugin.startEdit(firstNode,
					firstRec.data[dvOnEnterPlugin.dataIndex])
		}
	};
	var _delCube = function(btn) {
		if ((btn == "ok") && (opMode == "elemRightSelected")) {
			phpPaloServer.deleteCube(servId, dbName, elemActive.text);
			_showWaitMsg("_msg: del Cube".localize(), "Storing".localize()
					.concat("..."))
		}
	};
	var _renameCube = function(btn, cubeNewName) {
		if ((btn == "ok") && (opMode == "elemRightSelected")) {
			if (Jedox.wss.palo.utils.checkPaloName(cubeNewName, "cube")) {
				phpPaloServer.renameCube(servId, dbName, elemActive.text,
						cubeNewName);
				_showWaitMsg("_msg: ren Cube".localize(), "Storing".localize()
						.concat("..."))
			} else {
				Ext.MessageBox.show( {
					title : "Information".localize(),
					msg : ("Name".localize()).concat(' "', cubeNewName.replace(
							"<", "&lt;"), '" ', "is not allowed".localize()),
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.INFO
				})
			}
		}
	};
	var _singleClickRightViewHandler = function(node, evnt) {
		if (node.getDepth() == 1) {
			_setOpMode("elemRightSelected");
			elemActive = node
		} else {
			if (node.getDepth() == 2) {
				_setOpMode("elemRightSelected");
				elemActive = node;
				btnCubes.delB.disable();
				btnCubes.editB.disable()
			}
		}
	};
	var _singleClickLeftViewHandler = function(dataView, index, node, evnt) {
		_setOpMode("elemLeftSelected");
		elemActive = index
	};
	var _dblClickLeftViewHandler = function(dataView, index, node, evnt) {
		var dimTab = tabs.getComponent(index + 1).getId();
		tabs.setActiveTab(dimTab)
	};
	var _cmbDbSelectionHandler = function(combo, record, index) {
		if (cmbDbState != index) {
			cmbDbState = index;
			if (record.get("type") == "database") {
				servId = record.get("parent_id");
				dbName = record.get("name");
				_loadDatabase(record);
				_setOpMode("gridView");
				_setDataMode(dataMode);
				btnCombos.disconnectB.disable();
				btnAccts.newB.enable();
				btnCubes.newB.enable()
			} else {
				if (record.get("type") == "server") {
					servId = record.get("id");
					that.clearDbForm();
					_setOpMode("gridView");
					btnCombos.disconnectB.enable();
					btnAccts.newB.disable();
					btnCubes.newB.disable();
					btnChooseViews.metaDataB.disable();
					btnChooseViews.attribsB.disable();
					btnChooseViews.usersB.disable();
					if (record.get("connected") == 1) {
						btnCombos.disconnectB.setIconClass("palo_icon_turnoff")
					} else {
						btnCombos.disconnectB.setIconClass("palo_icon_turnon")
					}
				}
			}
		}
	};
	var _setOpMode = function(modeType) {
		if (modeType == "gridView") {
			if (opMode == "elemRightSelected") {
				btnCubes.delB.disable();
				btnCubes.editB.disable();
				viewRight.getSelectionModel().clearSelections()
			} else {
				if (opMode == "elemLeftSelected") {
					btnAccts.deleteB.disable();
					btnAccts.editB.disable();
					viewLeft.clearSelections()
				}
			}
		} else {
			if (modeType == "elemLeftSelected") {
				if (opMode == "elemRightSelected") {
					btnCubes.delB.disable();
					btnCubes.editB.disable();
					viewRight.getSelectionModel().clearSelections()
				}
				btnAccts.deleteB.enable();
				btnAccts.editB.enable()
			} else {
				if (modeType == "elemRightSelected") {
					if (opMode == "elemLeftSelected") {
						btnAccts.deleteB.disable();
						btnAccts.editB.disable();
						viewLeft.clearSelections()
					}
					btnCubes.delB.enable();
					btnCubes.editB.enable()
				}
			}
		}
		opMode = modeType
	};
	var _setDataMode = function(newDataMode) {
		dataMode = newDataMode;
		btnChooseViews.metaDataB.enable();
		btnChooseViews.attribsB.enable();
		btnChooseViews.usersB.enable();
		switch (dataMode) {
		case 0:
			btnChooseViews.metaDataB.disable();
			break;
		case 2:
			btnChooseViews.attribsB.disable();
			break;
		case 1:
			btnChooseViews.usersB.disable();
			break;
		default:
			btnChooseViews.metaDataB.disable();
			btnChooseViews.attribsB.disable();
			btnChooseViews.usersB.disable()
		}
		if (dataMode == 0) {
			panelCubesBtns.show()
		} else {
			panelCubesBtns.hide()
		}
	};
	this.clearDbForm = function() {
		for ( var i = storeDims.getCount(); i > 0; i--) {
			tabs.remove(tabs.getComponent(i))
		}
		storeDims.removeAll();
		if (rootNodeCubes.childNodes) {
			for ( var i = (rootNodeCubes.childNodes.length - 1); i >= 0; i--) {
				rootNodeCubes.removeChild(rootNodeCubes.childNodes[i])
			}
		}
	};
	var _loadDatabase = function(recDb) {
		for ( var i = storeDims.getCount(); i > 0; i--) {
			tabs.remove(tabs.getComponent(i))
		}
		if (rootNodeCubes.childNodes) {
			for ( var i = (rootNodeCubes.childNodes.length - 1); i >= 0; i--) {
				rootNodeCubes.removeChild(rootNodeCubes.childNodes[i])
			}
		}
		storeDims.removeAll();
		phpPaloServer.getDims(recDb.get("parent_id"), recDb.get("name"),
				dataMode);
		phpPaloServer.getCubes(recDb.get("parent_id"), recDb.get("name"),
				dataMode)
	};
	var _refillComboBox = function() {
		var executed = false;
		var tmpRec;
		for ( var i = 0; i < storeServDb.getCount(); i++) {
			tmpRec = storeServDb.getAt(i);
			if ((tmpRec.get("type") == "server")
					&& (tmpRec.get("connected") == 1)
					&& (storeServDb.find("parent_id", tmpRec.get("id")) == -1)) {
				executed = true;
				phpPaloServer.getDBs(tmpRec.get("id"));
				numOfRequests++
			}
		}
		if (!executed && (cmbDbState == -1)) {
			var tmpIndex = 0;
			cmbDbState = -1;
			cmbDb.setValue(storeServDb.getAt(tmpIndex).get("id"));
			_cmbDbSelectionHandler(cmbDb, storeServDb.getAt(tmpIndex), tmpIndex)
		}
	};
	var _checkEmptyDimView = function() {
		if (storeDims.getCount() == 0) {
			storeDims.add(new DimRecord( {
				name : "Hit RETURN to add Dimension".localize().concat("...")
			}));
			_setOpMode("gridView")
		}
	};
	var _showMenuRightView = function(node, e) {
		node.select();
		_singleClickRightViewHandler(node, e);
		viewRight.menu = new Ext.menu.Menu(
				{
					enableScrolling : false,
					items : [
							{
								text : "Add Cube".localize(),
								iconCls : "add-general",
								listeners : {
									click : function() {
										if (Jedox.wss.palo.workIn) {
											Jedox.wss.app
													.load(
															Jedox.wss.app.dynJSRegistry.cubeWizard,
															[ that, servId,
																	dbName ])
										}
									}
								}
							},
							{
								text : "Delete Cube".localize(),
								iconCls : "delete-icon",
								listeners : {
									click : function() {
										Ext.MessageBox.show( {
											title : "Delete Cube".localize(),
											msg : "_msg: Delete Cube"
													.localize(),
											buttons : Ext.MessageBox.OKCANCEL,
											fn : _delCube,
											icon : Ext.MessageBox.QUESTION
										})
									}
								}
							},
							{
								text : "Rename Cube".localize(),
								iconCls : "rename-icon",
								listeners : {
									click : function() {
										Ext.MessageBox.show( {
											title : "Rename Cube".localize(),
											msg : "Enter new name for Cube"
													.localize()
													+ ":",
											buttons : Ext.MessageBox.OKCANCEL,
											fn : _renameCube,
											prompt : true,
											value : elemActive.text
										})
									}
								}
							},
							"-",
							{
								disabled : true,
								text : "Clear Cube".localize(),
								iconCls : "clear-icon"
							},
							{
								disabled : true,
								text : "Export Cube".localize(),
								iconCls : "export-icon"
							},
							"-",
							{
								text : "Rule Edit".localize(),
								iconCls : "rename-icon",
								listeners : {
									click : function() {
										if (Jedox.wss.palo.workIn) {
											Jedox.wss.app
													.load(
															Jedox.wss.app.dynJSRegistry.ruleEditor,
															[
																	servId,
																	dbName,
																	viewRight
																			.getSelectionModel()
																			.getSelectedNode().text ])
										}
									}
								}
							},
							"-",
							{
								text : "Cube Information".localize(),
								iconCls : "info-icon",
								listeners : {
									click : function() {
										phpPaloServer.getCubeInfo(servId,
												dbName, node.text)
									}
								}
							} ]
				});
		viewRight.menu.showAt(e.getXY());
		e.stopEvent()
	};
	var _showMenuLeftView = function(dataView, index, node, e) {
		_singleClickLeftViewHandler(dataView, index, node, e);
		viewLeft.select(index);
		dataView.menu = new Ext.menu.Menu( {
			enableScrolling : false,
			items : [
					{
						text : "Add Dimension".localize(),
						iconCls : "add-general",
						listeners : {
							click : _newDim
						}
					},
					{
						text : "Delete Dimension".localize(),
						iconCls : "delete-icon",
						listeners : {
							click : function() {
								Ext.MessageBox.show( {
									title : "Delete Dimension".localize(),
									msg : "_msg: Delete Dimension".localize(),
									buttons : Ext.MessageBox.OKCANCEL,
									fn : _deleteDim,
									icon : Ext.MessageBox.QUESTION
								})
							}
						}
					},
					{
						text : "Rename Dimension".localize(),
						iconCls : "rename-icon",
						listeners : {
							click : _renameDim
						}
					},
					{
						text : "Edit Dimension".localize(),
						iconCls : "rename-icon",
						listeners : {
							click : function() {
								_dblClickLeftViewHandler(dataView, index, node,
										e)
							}
						}
					},
					"-",
					{
						text : "Export Dimension".localize(),
						iconCls : "export-icon",
						disabled : true
					},
					"-",
					{
						text : "Dimension Information".localize(),
						iconCls : "info-icon",
						listeners : {
							click : function() {
								phpPaloServer.getDimInfo(servId, dbName,
										storeDims.getAt(index).get("name"))
							}
						}
					} ]
		});
		dataView.menu.showAt(e.getXY());
		e.stopEvent()
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
	var _showDimInfoWin = function(result) {
		var tmpWin = new Ext.Window( {
			layout : "form",
			cls : "default-format-window",
			width : 360,
			resizable : false,
			autoHeight : true,
			closeAction : "close",
			autoDestroy : true,
			plain : true,
			modal : true,
			title : "Dimension Information".localize(),
			items : [
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Identifier".localize(),
						value : result[1][0]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Name".localize(),
						value : result[1][1]
					}),
					new Ext.form.TextField(
							{
								disabled : true,
								labelSeparator : "",
								labelStyle : "width: 200px;",
								fieldLabel : "&#160;"
										+ "Number of Elements".localize(),
								value : result[1][2]
							}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Elements".localize(),
						value : result[1][3]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Number of N elem.".localize(),
						value : result[1][4]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Number of S elem.".localize(),
						value : result[1][5]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Number of C elem.".localize(),
						value : result[1][6]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Maximum Level".localize(),
						value : result[1][7]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Maximum Indent".localize(),
						value : result[1][8]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Maximum Depth".localize(),
						value : result[1][9]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Type".localize(),
						value : ((dataMode == 1) ? "system".localize()
								: "normal".localize())
					}) ],
			buttons : [ new Ext.Button( {
				text : "Close".localize(),
				ctCls : "stdButtons",
				listeners : {
					click : function() {
						tmpWin.close()
					}
				}
			}) ]
		});
		tmpWin.show()
	};
	var _showCubeInfoWin = function(result) {
		var tmpType;
		switch (dataMode) {
		case 0:
			tmpType = "normal";
			break;
		case 1:
			tmpType = "system";
			break;
		case 2:
			tmpType = "attributes";
			break;
		default:
			tmpType = "normal"
		}
		var tmpWin = new Ext.Window( {
			layout : "form",
			width : 360,
			resizable : false,
			autoHeight : true,
			closeAction : "close",
			cls : "default-format-window",
			autoDestroy : true,
			plain : true,
			modal : true,
			title : "Cube Information".localize(),
			items : [
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Identifier".localize(),
						value : result[1][0]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Name".localize(),
						value : result[1][1]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;"
								+ "Number of Dimensions".localize(),
						value : result[1][2]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Dimensions".localize(),
						value : result[1][3]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Number of Cells".localize(),
						value : result[1][4]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;"
								+ "Number of Filled Cells".localize(),
						value : result[1][5]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Filed Ratio".localize(),
						value : result[1][6]
					}),
					new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Status".localize(),
						value : ((result[1][7] == 1) ? "loaded".localize()
								: "not loaded".localize())
					}), new Ext.form.TextField( {
						disabled : true,
						labelSeparator : "",
						labelStyle : "width: 200px;",
						fieldLabel : "&#160;" + "Type".localize(),
						value : tmpType.localize()
					}) ],
			buttons : [ new Ext.Button( {
				text : "Close".localize(),
				ctCls : "stdButtons",
				listeners : {
					click : function() {
						tmpWin.close()
					}
				}
			}) ]
		});
		tmpWin.show()
	};
	this.close = function() {
		win.close()
	};
	this.setDbTab = function() {
		tabs.setActiveTab(tabs.getComponent(0))
	};
	var _resizeAll = function() {
		if (panelMain.rendered) {
			var hTop = 25;
			var wPerComboButton;
			var whButtonLeft = 30;
			var pSize = panelMain.getSize();
			var w = pSize.width;
			var h = pSize.height;
			var panelDataViewsW = (w - _config.widthMidButtons) / 2;
			var panelDataViewsH = (h - hTop * 3 - whButtonLeft) - 5;
			panelCombo.setPosition(0, hTop);
			cmbDb.setWidth(w - 63);
			panelComboButtons.setPosition(w - 55, hTop);
			panelDataViewLeft.setWidth(panelDataViewsW);
			panelDataViewLeft.setHeight(panelDataViewsH);
			panelDataViewLeft.setPosition(0, 3 * hTop);
			panelDataViewRight.setWidth(panelDataViewsW);
			panelDataViewRight.setHeight(panelDataViewsH);
			panelDataViewRight.setPosition(w - panelDataViewsW, 3 * hTop);
			lblCombo.setPosition(0, 0);
			lblLeft.setPosition(0, 5 + 2 * hTop);
			lblRight.setPosition(w - panelDataViewsW, 5 + 2 * hTop);
			panelBottomLeft.setPosition(0, h - whButtonLeft);
			if (panelChooseView.rendered) {
				panelChooseView.setPosition(panelDataViewsW
						- panelChooseView.getSize().width, h - whButtonLeft)
			}
			panelCubesBtns.setPosition(w - panelDataViewsW, h - whButtonLeft);
			if (panelCloseButton.rendered && btnClose.rendered) {
				var buttonCloseSize = panelCloseButton.getSize();
				panelCloseButton.setPosition(w
						- btnClose.getEl().getBox().width, (h - 35) + 5)
			}
		}
	};
	_init();
	phpPaloServer.getServList()
};
Jedox.wss.palo.DimensionViewer = function() {
	var InlineEditor = function(cfg, field) {
		InlineEditor.superclass.constructor.call(this, field
				|| new Ext.form.TextField( {
					allowBlank : true,
					growMin : 290,
					growMax : 290,
					grow : true,
					selectOnFocus : true
				}), cfg)
	};
	Ext.extend(InlineEditor, Ext.Editor, {
		alignment : "tl-tl",
		hideEl : false,
		cls : "x-small-editor",
		shim : false,
		completeOnEnter : true,
		cancelOnEsc : true,
		init : function(view) {
			this.view = view
		}
	});
	var phpPaloServerCbHandlers = {
		getDimElems : function(result) {
			if (!result[2]) {
				var tmpRec;
				for ( var i = 0; i < result[1].length; i++) {
					tmpRec = new RecordLeftView( {
						id : result[1][i].identifier,
						type : result[1][i].type,
						name : result[1][i].name
					});
					if (startIndexLeftView <= result[0][0]) {
						storeLeftView.add(tmpRec)
					} else {
						storeLeftView.insert(i, tmpRec)
					}
				}
				numOfElemsLeftView = result[0][3];
				numOfShownElemsLeftView = result[0][2];
				fromIdLeftView = result[0][0];
				if (startIndexLeftView < 0) {
					startIndexLeftView = 0;
					_leftViewAfterRender();
					requestedRange = new Array();
					requestedRange[0] = 0;
					requestedRange[1] = 0
				} else {
					if (startIndexLeftView <= fromIdLeftView) {
						for ( var i = (result[0][2] - 1); i >= 0; i--) {
							storeLeftView.remove(storeLeftView.getAt(i))
						}
						startIndexLeftView += result[0][2]
					} else {
						var storeLastId = storeLeftView.getCount() - 1;
						for ( var i = 0; i < result[0][2]; i++) {
							storeLeftView.remove(storeLeftView
									.getAt(storeLastId - i))
						}
						startIndexLeftView = result[0][0]
					}
				}
				_updateLeftViewDivs()
			} else {
				_showErrorMsg(result[3])
			}
			_checkEmptyLeftView()
		},
		getChildElems : function(result) {
			if (!result[2]) {
				if ((storeRightView.getCount() == 0)) {
					cacheChilds[result[0]] = result[1];
					_fillConsolidateElement(result[0])
				}
			} else {
				_showErrorMsg(result[3])
			}
		},
		addElem : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					_hideWaitMsg();
					timerOut = true;
					var t = setTimeout(function() {
						viewLeft.retainFocus()
					}, 0)
				} else {
					_hideWaitMsg();
					timerOut = true;
					var tmpElems = viewLeft.getSelectedRecords();
					if (tmpElems.length > 0) {
						storeLeftView.remove(tmpElems[0])
					}
					_showErrorMsg(result[3])
				}
			}
		},
		deleteElems : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					var tmpElems = viewLeft.getSelectedRecords();
					for ( var i = 0; i < tmpElems.length; i++) {
						if (cacheChilds[tmpElems[i].get("name")]) {
							cacheChilds[tmpElems[i].get("name")] = null
						}
						for ( var j = 0; j < result[0][0][i].length; j++) {
							if (cacheChilds[result[0][0][j]]) {
								for ( var k = 0; k < cacheChilds[result[0][0][i][j]].length; k++) {
									if (cacheChilds[result[0][0][i][j]][k].name == tmpElems[i]
											.get("name")) {
										cacheChilds[result[0][0][i][j]].splice(
												k, 1)
									}
								}
								if (cacheChilds[result[0][0][i][j]].length == 0) {
									storeLeftView.getAt(
											storeLeftView.find("name",
													result[0][0][i][j])).set(
											"type", "numeric")
								}
							}
						}
						storeLeftView.remove(tmpElems[i])
					}
					for ( var i = 0; i < result[0][1].length; i++) {
						var index = storeLeftView.find("name", result[0][1][i]);
						if (index != -1) {
							storeLeftView.getAt(index).set("type", "numeric")
						}
					}
					if (storeRightView.getCount() > 0) {
						storeRightView.removeAll()
					}
					_setOpMode("gridView");
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
			_checkEmptyLeftView()
		},
		renameElem : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					var tmpRec = storeLeftView.getAt(elemActive);
					var newElemName = result[0][0];
					if (cacheChilds[tmpRec.get("name")]) {
						cacheChilds[newElemName] = cacheChilds[tmpRec
								.get("name")];
						cacheChilds[tmpRec.get("name")] = null
					}
					for ( var j = 1; j < result[0].length; j++) {
						if (cacheChilds[result[0][j]]) {
							for ( var k = 0; k < cacheChilds[result[0][j]].length; k++) {
								if (cacheChilds[result[0][j]][k].name == tmpRec
										.get("name")) {
									cacheChilds[result[0][j]][k].name = newElemName
								}
							}
						}
					}
					tmpRec.set("name", newElemName);
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
		},
		setChildElems : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					var parentElem = storeLeftView.getAt(elemActive);
					if (storeRightView.getCount() > 0) {
						parentElem.set("type", "consolidated")
					} else {
						if (parentElem.get("type") == "consolidated") {
							parentElem.set("type", "numeric")
						}
					}
					cacheChilds[parentElem.get("name")] = new Array();
					var tmpRec;
					for ( var i = 0; i < storeRightView.getCount(); i++) {
						tmpRec = storeRightView.getAt(i);
						cacheChilds[parentElem.get("name")][cacheChilds[parentElem
								.get("name")].length] = {
							name : tmpRec.get("name"),
							type : tmpRec.get("type"),
							weight : tmpRec.get("factor")
						}
					}
					storeRightView.removeAll();
					_setOpMode("gridView");
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
		},
		setWorkingMode : function(result) {
			storeLeftView.removeAll();
			storeRightView.removeAll();
			_setOpMode("gridView");
			startIndexLeftView = -1;
			phpPaloServer.getDimElems(servId, dbName, dimName, 0,
					bufferSizeLeftView)
		},
		changeElemType : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					storeLeftView.getAt(elemActive).set("type", result[0]);
					_setOpMode("gridView");
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
		},
		moveToBeginning : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					for ( var tmpRec, i = 0, j = 0; i < storeLeftView
							.getCount(); i++) {
						if (viewLeft.isSelected(viewLeft.getNode(i))) {
							tmpRec = storeLeftView.getAt(i);
							storeLeftView.remove(tmpRec);
							storeLeftView.insert(j, tmpRec);
							j++
						}
					}
					_setOpMode("gridView");
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
		},
		moveToEnd : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					for ( var tmpRec, i = 0; i < storeLeftView.getCount(); i++) {
						if (viewLeft.isSelected(viewLeft.getNode(i))) {
							tmpRec = storeLeftView.getAt(i);
							storeLeftView.remove(tmpRec);
							storeLeftView.add(tmpRec);
							i--
						}
					}
					_setOpMode("gridView");
					_hideWaitMsg();
					timerOut = true
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
		},
		getElemsCount : function(result) {
			if (!result[2]) {
				Ext.MessageBox.show( {
					title : "Count".localize(),
					msg : "Total Number of Elements".localize() + ": "
							+ result[1][0] + "<br>"
							+ "Number of Numeric Elements".localize() + ": "
							+ result[1][2] + "<br>"
							+ "Number of String Elements".localize() + ": "
							+ result[1][3] + "<br>"
							+ "Number of Consolidated Elements".localize()
							+ ": " + result[1][1],
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.INFO
				})
			} else {
				_showErrorMsg(result[3])
			}
		},
		getSubsetList : function(result) {
			if (!result[2]) {
				storeRightView.removeAll();
				_genSubsetList(result[1])
			} else {
				_showErrorMsg(result[3])
			}
		},
		deletePaloSubset : function(result) {
			clearTimeout(timer);
			if (!result[2]) {
				storeLeftView.remove(storeLeftView.getAt(storeLeftView.find(
						"name", result[0][0])));
				_hideWaitMsg();
				timerOut = true
			} else {
				_showErrorMsg(result[3])
			}
			_checkEmptyLeftView()
		},
		renamePaloSubset : function(result) {
			clearTimeout(timer);
			if (!result[2]) {
				storeLeftView.getAt(storeLeftView.find("name", result[0][0]))
						.set("name", result[0][1]);
				_hideWaitMsg();
				timerOut = true
			} else {
				_showErrorMsg(result[3])
			}
		}
	};
	var parentModeller, servId, dbName, dimName;
	var that = this;
	var phpPaloServer = new Palo(phpPaloServerCbHandlers);
	var _config = Jedox.wss.palo.config;
	var _internalConf = {
		hTop : 25,
		wCombo : 150,
		whButtonMid : 26,
		whButtonLeft : 30,
		wCheckBox : 25
	};
	var panelMain, panelRight, panelCombo, panelLeft, panelMid, panelBottomLeft, panelbtnTreeList, panelBottomRight, panelCloseButton, panelDataButton, panelTree, panelTreeButtons, panelDataViewLeft, panelDataViewRight, viewLeft, viewRight, viewLeftDom, topLeftDiv, bottomLeftDiv, numOfElemsLeftView, numOfShownElemsLeftView, fromIdLeftView, startIndexLeftView, requestedRange, bufferSizeLeftView, btnMids, btnAccts, btnYesNo, btnDatabase, btnClose, btnTreeControls, lblRight, cmbElemType;
	var treeMode, treeRoot, treeMain, treeLevel, treeMaxLevelReached, treeMaxLevelRequested, btnTreeList;
	var opMode;
	var dataMode;
	var elemActive, elemActiveAtRight;
	var syncCounter;
	var expandMoreLevels, expandAllLevels;
	var hasOnlyLeaves, viewType, dvOnEnterPlugin;
	var timerOut, timer, timerOutTime = 10000;
	var cacheChilds = new Array();
	var dataCmbElemType = [ [ 1, "Elements".localize() ],
			[ 2, "Attributes".localize() ], [ 3, "Subsets".localize() ] ];
	var storeElemType;
	var RecordLeftView = new Ext.data.Record.create( [ {
		name : "id"
	}, {
		name : "type"
	}, {
		name : "name"
	} ]);
	var storeLeftView = new Ext.data.SimpleStore( {
		fields : [ {
			name : "id"
		}, {
			name : "type"
		}, {
			name : "name"
		} ]
	});
	var RecordRightView = new Ext.data.Record.create( [ {
		name : "name"
	}, {
		name : "type"
	}, {
		name : "factor"
	} ]);
	var storeRightView = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		}, {
			name : "type"
		}, {
			name : "factor"
		} ]
	});
	this.init = function(servIdNumber, databaseName, recDim, dimensionType) {
		servId = servIdNumber;
		dbName = databaseName;
		treeMode = false;
		opMode = "gridView";
		dimName = recDim.get("name");
		treeLevel = 0;
		syncCounter = 0;
		treeMaxLevelReached = 0;
		startIndexLeftView = -1;
		bufferSizeLeftView = _config.numberOfElements * 5;
		dataMode = (dimensionType) ? dimensionType : 0;
		viewType = 1;
		expandAllLevels = false;
		hasOnlyLeaves = false;
		storeElemType = new Ext.data.SimpleStore( {
			fields : [ {
				name : "number_val"
			}, {
				name : "str_val"
			} ]
		});
		storeElemType.loadData(dataCmbElemType);
		dvOnEnterPlugin = new InlineEditor(
				{
					dataIndex : "name",
					labelSelector : "div.row-modeller",
					listeners : {
						beforecomplete : function(thisEd, value, startValue) {
							var elemName = ((viewType == 1) ? "New Element"
									.localize() : "New Attribute".localize());
							if (value != startValue) {
								if (value && (value != "")) {
									if (elemActive == (storeLeftView.getCount() - 1)
											&& (elemName == startValue)) {
										if (Jedox.wss.palo.utils.checkPaloName(
												value, "elem")) {
											thisEd.activeRecord.set(
													thisEd.dataIndex, value);
											elemActive = storeLeftView
													.getCount() - 1;
											phpPaloServer.addElem(servId,
													dbName, dimName, "N",
													value, "", 1, false);
											_showWaitMsg("_msg: new Elem"
													.localize(), "Storing"
													.localize().concat("..."))
										} else {
											Ext.MessageBox
													.show( {
														title : "Information"
																.localize(),
														msg : ("Name"
																.localize())
																.concat(
																		' "',
																		value
																				.replace(
																						"<",
																						"&lt;"),
																		'" ',
																		"is not allowed"
																				.localize()),
														buttons : Ext.Msg.OK,
														icon : Ext.MessageBox.INFO
													});
											storeLeftView
													.remove(thisEd.activeRecord);
											elemActive = storeLeftView
													.getCount() - 1;
											viewLeft.select(elemActive)
										}
										_setOpMode("elemSelected")
									} else {
										if (opMode == "elemSelected") {
											if (Jedox.wss.palo.utils
													.checkPaloName(value,
															"elem")) {
												if (viewLeft
														.getSelectedRecords()[0]
														.get("type") == "subset") {
													phpPaloServer
															.renamePaloSubset( [
																	servId,
																	dbName,
																	startValue,
																	value ]);
													_showWaitMsg(
															"_msg: rename Subset"
																	.localize(),
															"Storing"
																	.localize()
																	.concat(
																			"..."))
												} else {
													phpPaloServer.renameElem(
															servId, dbName,
															dimName,
															startValue, value);
													_showWaitMsg(
															"_msg: rename Elem"
																	.localize(),
															"Storing"
																	.localize()
																	.concat(
																			"..."))
												}
											} else {
												Ext.MessageBox
														.show( {
															title : "Information"
																	.localize(),
															msg : ("Name"
																	.localize())
																	.concat(
																			' "',
																			value
																					.replace(
																							"<",
																							"&lt;"),
																			'" ',
																			"is not allowed"
																					.localize()),
															buttons : Ext.Msg.OK,
															icon : Ext.MessageBox.INFO
														})
											}
										}
									}
								} else {
									if (value == "") {
										if (startValue != elemName) {
											Ext.MessageBox
													.show( {
														title : ((viewType == 1) ? "Delete Element"
																.localize()
																: "Delete Attribute"
																		.localize()),
														msg : ((viewType == 1) ? "_msg: Delete Element"
																.localize()
																: "_msg: Delete Attribute"
																		.localize()),
														buttons : Ext.MessageBox.OKCANCEL,
														fn : _deleteElem,
														icon : Ext.MessageBox.QUESTION
													})
										} else {
											storeLeftView
													.remove(thisEd.activeRecord);
											elemActive = storeLeftView
													.getCount() - 1;
											viewLeft.select(elemActive)
										}
									}
								}
							} else {
								if (startValue == elemName) {
									storeLeftView.remove(thisEd.activeRecord);
									elemActive = storeLeftView.getCount() - 1;
									viewLeft.select(elemActive);
									_setOpMode("elemSelected")
								}
							}
							_checkEmptyLeftView()
						},
						complete : function() {
							viewLeft.retainFocus()
						},
						canceledit : function(thisEd, value, startValue) {
							var elemName = ((viewType == 1) ? "New Element"
									.localize() : "New Attribute".localize());
							if (startValue == elemName) {
								thisEd.activeRecord.store
										.remove(thisEd.activeRecord);
								elemActive = storeLeftView.getCount() - 1;
								viewLeft.select( [ elemActive ])
							}
							_checkEmptyLeftView();
							viewLeft.retainFocus()
						}
					}
				});
		viewLeft = new Jedox.wss.palo.utils.OnEnterDataViewClass(
				{
					plugins : [ dvOnEnterPlugin ],
					itemSelector : "div.row-modeller",
					style : "overflow:auto",
					multiSelect : true,
					store : storeLeftView,
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
							"</tpl>",
							"<tpl if=\"type == 'subset'\">",
							'<img class="palo_selelem_subset" src="../lib/ext/resources/images/default/s.gif"/ width="16" height="16">',
							"</tpl>", "<span>&#160;{name}</span>", "</div>",
							"</tpl>"),
					listeners : {
						dblclick : function(dataView, index, node, e) {
							if ((storeLeftView.getCount() != 1)
									|| (storeLeftView.getAt(0).get("name") != "Hit RETURN to add Elements"
											.localize().concat("..."))) {
								_dblClickLeftViewHandler(dataView, index, node,
										e)
							}
						},
						click : function(dataView, index, node, e) {
							if ((storeLeftView.getCount() != 1)
									|| (storeLeftView.getAt(0).get("name") != "Hit RETURN to add Elements"
											.localize().concat("..."))) {
								_singleClickLeftViewHandler(dataView, index,
										node, e)
							}
						},
						contextmenu : function(dataView, index, node, e) {
							if ((storeLeftView.getCount() != 1)
									|| (storeLeftView.getAt(0).get("name") != "Hit RETURN to add Elements"
											.localize().concat("..."))) {
								_showMenuLeftView(dataView, index, node, e)
							}
						}
					}
				});
		viewLeft.on("onkeyenter", function() {
			var timer = setTimeout(function() {
				_newElem()
			}, 0)
		});
		storeLeftView.add(new RecordLeftView( {
			id : -1,
			type : "",
			name : "Hit RETURN to add Elements".localize().concat("...")
		}));
		panelDataViewLeft = new Ext.Panel( {
			layout : "fit",
			items : [ viewLeft ]
		});
		var tplRightView = new Ext.XTemplate(
				'<tpl for=".">',
				'<div class="row-modeller">',
				'<tpl if="this.getColor(factor) == 1">',
				'<div style="width:80%; float:left;">&#160;{name}</div>',
				'<div style="width:19%; margin-left:80%; text-align:right;"><tpl if="type != \'undefined\'">{factor}</tpl>&#160;</div>',
				"</tpl>",
				'<tpl if="this.getColor(factor) == 0">',
				'<div style="width:80%; float:left; color:red;">&#160;{name}</div>',
				'<div style="width:19%; margin-left:80%; text-align:right; color:red;">{factor}&#160;</div>',
				"</tpl>",
				'<tpl if="this.getColor(factor) == 2">',
				'<div style="width:80%; float:left; color:blue;">&#160;{name}</div>',
				'<div style="width:19%; margin-left:80%; text-align:right; color:blue;">{factor}&#160;</div>',
				"</tpl>", "</div>", "</tpl>", {
					getColor : function(val) {
						if (val < 0) {
							return 0
						} else {
							if (val == 1) {
								return 1
							} else {
								return 2
							}
						}
					}
				});
		viewRight = new Ext.DataView(
				{
					itemSelector : "div.row-modeller",
					style : "overflow:auto",
					multiSelect : true,
					store : storeRightView,
					cls : "modellerDataViewSelect",
					tpl : tplRightView,
					listeners : {
						dblclick : function(dataView, index, node, evnt) {
							if ((storeRightView.getCount() != 1)
									|| (storeRightView.getAt(0).get("name") != "Add Element from list of Elements"
											.localize().concat("..."))) {
								_dblClickRightViewHandler(dataView, index,
										node, evnt)
							}
						},
						click : function(dataView, index, node, evnt) {
							if ((storeRightView.getCount() != 1)
									|| (storeRightView.getAt(0).get("name") != "Add Element from list of Elements"
											.localize().concat("..."))) {
								_singleClickRightViewHandler(dataView, index,
										node, evnt)
							}
						},
						contextmenu : function(dataView, index, node, e) {
							if ((storeRightView.getCount() != 1)
									|| (storeRightView.getAt(0).get("name") != "Add Element from list of Elements"
											.localize().concat("..."))) {
								_showMenuRightView(dataView, index, node, e)
							}
						}
					}
				});
		panelDataViewRight = new Ext.Panel( {
			layout : "fit",
			items : [ viewRight ]
		});
		if (dataMode == 1) {
			panelDataViewRight.hide()
		}
		cmbElemType = new Ext.form.ComboBox( {
			store : storeElemType,
			bodyStyle : "background-color: transparent;",
			typeAhead : true,
			selectOnFocus : true,
			hideLabel : true,
			editable : false,
			forceSelection : true,
			triggerAction : "all",
			mode : "local",
			listeners : {
				select : function(combo, record, index) {
					var newViewType;
					viewType = record.get("number_val");
					switch (viewType) {
					case 2:
						newViewType = "attributes";
						viewLeft.multiSelect = true;
						break;
					case 3:
						newViewType = "subsets";
						viewLeft.multiSelect = false;
						viewLeft.singleSelect = true;
						break;
					default:
						newViewType = "elements";
						viewLeft.multiSelect = true
					}
					phpPaloServer.setWorkingMode(servId, dbName, dimName,
							newViewType)
				}
			},
			valueField : "number_val",
			displayField : "str_val"
		});
		cmbElemType.setValue("Elements".localize());
		panelCombo = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			items : [ cmbElemType ]
		});
		if (dataMode == 1) {
			panelCombo.hide()
		}
		btnMids = {
			up : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_up",
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				handler : _upBtnHandler
			}),
			right : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_right",
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				handler : _rightBtnHandler
			}),
			left : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_left",
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				handler : _leftBtnHandler
			}),
			down : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_down",
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				handler : _downBtnHandler
			})
		};
		lblRight = new Ext.form.MiscField( {
			value : "Consolidated Elements".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		if (dataMode == 1) {
			lblRight.hide()
		}
		panelMid = new Ext.Panel(
				{
					border : false,
					bodyStyle : "background-color: transparent; vertical-align: middle;",
					layout : "form",
					width : 31,
					autoHeight : true,
					items : [ btnMids.up, btnMids.right, btnMids.left,
							btnMids.down ]
				});
		btnAccts = {
			newB : new Ext.Button( {
				iconCls : "palo_icon_cl_create",
				cls : "modellerImageButton",
				handler : function() {
					if (viewType != 3) {
						_newElem()
					} else {
						if (Jedox.wss.palo.workIn == Jedox.wss.palo.GRID) {
							Jedox.wss.app.load(
									Jedox.wss.app.dynJSRegistry.subsetEditor,
									[ {
										mode : 2,
										load_data : [ servId, dbName, dimName,
												"", 2 ],
										fnc : _reloadListOfDimsHandler
									} ])
						} else {
							Jedox.wss.palo.openSubsetEditor( {
								mode : 2,
								load_data : [ servId, dbName, dimName, "", 2 ],
								fnc : _reloadListOfDimsHandler
							})
						}
					}
				}
			}),
			deleteB : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_cl_delete",
				cls : "modellerImageButton",
				handler : function() {
					Ext.MessageBox.show( {
						title : ((viewType == 1) ? "Delete Element".localize()
								: "Delete Attribute".localize()),
						msg : ((viewType == 1) ? "_msg: Delete Element"
								.localize() : "_msg: Delete Attribute"
								.localize()),
						buttons : Ext.MessageBox.OKCANCEL,
						fn : _deleteElem,
						icon : Ext.MessageBox.QUESTION
					})
				}
			}),
			renameB : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_cl_edit",
				cls : "modellerImageButton",
				handler : _renameElem
			}),
			consolidateB : new Ext.Button( {
				disabled : true,
				iconCls : "palo_icon_C",
				cls : "modellerImageButton",
				handler : _consolidateElem
			})
		};
		if (dataMode == 1) {
			btnAccts.consolidateB.hide()
		}
		panelBottomLeft = new Ext.Panel(
				{
					border : false,
					bodyStyle : "background-color: transparent; vertical-align: middle;",
					layout : "table",
					width : 200,
					height : 40,
					autoHeight : true,
					items : [ btnAccts.newB, {
						border : false,
						bodyStyle : "background-color: transparent;",
						html : " "
					}, btnAccts.deleteB, {
						border : false,
						bodyStyle : "background-color: transparent;",
						html : " "
					}, btnAccts.renameB, {
						border : false,
						bodyStyle : "background-color: transparent;",
						html : " "
					}, btnAccts.consolidateB ]
				});
		btnTreeList = new Ext.Button(
				{
					iconCls : "palo_icon_showHRflat",
					cls : "modellerImageButton",
					style : "vertical-align:bottom;",
					handler : function() {
						treeMode = (treeMode) ? false : true;
						if (treeMode) {
							treeRoot.reload()
						} else {
							for ( var i = (treeRoot.childNodes.length - 1); i >= 0; i--) {
								treeRoot.childNodes[i].remove()
							}
						}
						_hideShowPanels(((treeMode) ? "tree" : "normal"))
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
		if (dataMode == 1) {
			panelbtnTreeList.hide()
		}
		btnYesNo = {
			applyB : new Ext.Button( {
				iconCls : "palo_icon_OK",
				cls : "modellerImageButton",
				handler : _modifyElement
			}),
			cancelB : new Ext.Button( {
				iconCls : "palo_icon_cancel",
				cls : "modellerImageButton",
				handler : function() {
					_setOpMode("gridView");
					storeRightView.removeAll()
				}
			})
		};
		panelBottomRight = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "table",
			width : 200,
			height : 40,
			autoHeight : true,
			items : [ btnYesNo.applyB, {
				border : false,
				bodyStyle : "background-color: transparent;",
				html : " "
			}, btnYesNo.cancelB ]
		});
		panelBottomRight.hide();
		btnDatabase = new Ext.Button( {
			text : "<< Database".localize(),
			ctCls : "stdButtons",
			listeners : {
				click : function() {
					parentModeller.setDbTab()
				}
			}
		});
		btnClose = new Ext.Button( {
			text : "Close".localize(),
			ctCls : "stdButtons",
			listeners : {
				click : function() {
					parentModeller.close()
				}
			}
		});
		panelDataButton = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnDatabase ]
		});
		panelCloseButton = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnClose ]
		});
		treeMain = new Ext.tree.TreePanel(
				{
					cls : "x-tree-noicon",
					border : false,
					autoScroll : true,
					animate : false,
					enableDD : false,
					containerScroll : true,
					rootVisible : false,
					listeners : {
						contextmenu : function(node, e) {
							e.stopEvent()
						}
					},
					loader : new Ext.tree.TreeLoader(
							{
								dataUrl : phpPaloServer.dispatcher.serverUrl
										+ "&wam="
										+ ((Jedox.wss.palo.workIn == Jedox.wss.palo.GRID) ? Jedox.wss.app.appModeS
												: "") + "&c="
										+ phpPaloServer.className
										+ "&m=getTreeNodes&servId=" + servId
										+ "&dbName=" + dbName + "&dimName="
										+ dimName,
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
		panelTree = new Ext.Panel( {
			bodyStyle : "background-color: transparent;",
			layout : "fit",
			items : [ treeMain ]
		});
		panelTree.hide();
		btnTreeControls = {
			plusB : new Ext.Button( {
				iconCls : "palo_icon_plus",
				cls : "modellerImageButton",
				style : "vertical-align:bottom;",
				handler : function() {
					treeLevel++;
					_expandTreeToLevel(treeRoot)
				}
			}),
			minusB : new Ext.Button( {
				iconCls : "palo_icon_minus",
				cls : "modellerImageButton",
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
						iconCls : "palo_icon_expandall",
						cls : "modellerImageButton",
						style : "vertical-align:bottom;",
						handler : function() {
							expandAllLevels = true;
							treeMain.expandAll();
							treeLevel = (treeMaxLevelReached >= 0) ? treeMaxLevelReached
									: 0
						}
					}),
			collapseAllB : new Ext.Button( {
				iconCls : "palo_icon_colapseall",
				cls : "modellerImageButton",
				style : "vertical-align:bottom;",
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
		panelTreeButtons = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "table",
			items : [ btnTreeControls.plusB, btnTreeControls.minusB,
					btnTreeControls.expandAllB, btnTreeControls.collapseAllB,
					btnTreeControls.oneB, btnTreeControls.twoB,
					btnTreeControls.threeB, btnTreeControls.fourB,
					btnTreeControls.fiveB ]
		});
		panelTreeButtons.hide();
		panelMain = new Ext.Panel(
				{
					title : recDim.get("name"),
					border : false,
					bodyStyle : "background-color: transparent;",
					layout : "absolute",
					anchor : "100% 100%",
					monitorResize : true,
					listeners : {
						resize : _resizeAll,
						activate : function() {
							if ((storeLeftView.getCount() == 1)
									&& (storeLeftView.getAt(0).get("name") == "Hit RETURN to add Elements"
											.localize().concat("..."))) {
								storeLeftView.removeAll();
								phpPaloServer.getDimElems(servId, dbName,
										dimName, 0, bufferSizeLeftView)
							}
							_resizeAll()
						}
					},
					items : [ panelDataViewLeft, panelCombo, lblRight,
							panelDataViewRight, panelMid, panelBottomLeft,
							panelbtnTreeList, panelBottomRight,
							panelDataButton, panelCloseButton, panelTree,
							panelTreeButtons ]
				});
		if (Jedox.wss.palo.workIn != Jedox.wss.palo.GRID) {
			btnClose.hide()
		}
	};
	this.getView = function() {
		return panelMain
	};
	this.bloomIn = function(modeller) {
		parentModeller = modeller;
		var w = parentModeller.getView().getSize().width;
		var h = parentModeller.getView().getSize().height;
		var panelDataViewsW = (w - _config.widthMidButtons) / 2;
		var panelDataViewsH = (h - _internalConf.hTop - _internalConf.whButtonLeft) - 5;
		cmbElemType.setWidth(_internalConf.wCombo);
		panelCombo.setPosition(0, 0);
		viewLeft.setSize(panelDataViewsW, panelDataViewsH);
		panelDataViewLeft.setPosition(0, _internalConf.hTop);
		viewRight.setSize(panelDataViewsW, panelDataViewsH);
		panelDataViewRight.setPosition(w - panelDataViewsW, _internalConf.hTop);
		lblRight.setPosition(w - panelDataViewsW, 5);
		panelMid.setPosition(panelDataViewsW + 6, panelDataViewsH / 2
				- _internalConf.whButtonMid * 2 + _internalConf.hTop);
		panelBottomLeft.setPosition(0, (h - 35) + 5);
		panelbtnTreeList.setPosition(panelDataViewsW
				- _internalConf.whButtonMid, 0);
		panelBottomRight.setPosition(w - panelDataViewsW, (h - 35) + 5);
		panelCloseButton.setPosition(w - (48), (h - 35) + 5);
		panelDataButton.setPosition(w - (48) - (89) - 5, (h - 35) + 5)
	};
	var _hideShowPanels = function(mode) {
		if (mode == "tree") {
			panelCombo.hide();
			panelDataViewLeft.hide();
			lblRight.hide();
			panelDataViewRight.hide();
			panelMid.hide();
			panelTree.show();
			panelTreeButtons.show();
			btnAccts.newB.disable();
			btnAccts.deleteB.disable();
			btnAccts.renameB.disable();
			btnAccts.consolidateB.disable();
			btnTreeList.setIconClass("palo_icon_showHR")
		} else {
			if (mode == "normal") {
				panelCombo.show();
				panelDataViewLeft.show();
				lblRight.show();
				panelDataViewRight.show();
				panelMid.show();
				panelTree.hide();
				panelTreeButtons.hide();
				btnAccts.newB.enable();
				_setOpMode("gridView");
				viewLeft.clearSelections();
				btnTreeList.setIconClass("palo_icon_showHRflat")
			}
		}
	};
	var _upBtnHandler = function() {
		if (opMode == "elemSelected") {
			var tmpArr = [];
			var tmpSelectedIds = viewLeft.getSelectedIndexes();
			for ( var tmpElem, i = 1; i < storeLeftView.getCount(); i++) {
				if (viewLeft.isSelected(viewLeft.getNode(i))) {
					if (!viewLeft.isSelected(viewLeft.getNode(i - 1))) {
						tmpElem = storeLeftView.getAt(i);
						storeLeftView.remove(tmpElem);
						storeLeftView.insert(i - 1, tmpElem);
						tmpArr[tmpArr.length] = tmpElem.get("name");
						for ( var j = 0; j < tmpSelectedIds.length; j++) {
							if (tmpSelectedIds[j] == i) {
								tmpSelectedIds[j] = i - 1;
								break
							}
						}
						viewLeft.select(tmpSelectedIds)
					}
				}
			}
			if (tmpArr.length > 0) {
				phpPaloServer.moveElemUp(servId, dbName, dimName, tmpArr)
			}
		} else {
			if ((opMode == "elemEdit") && (elemActiveAtRight > 0)) {
				var tmpElem;
				var tmpSelectedIds = viewRight.getSelectedIndexes();
				for ( var i = 1; i < storeRightView.getCount(); i++) {
					if (viewRight.isSelected(viewRight.getNode(i))) {
						if (!viewRight.isSelected(viewRight.getNode(i - 1))) {
							tmpElem = storeRightView.getAt(i);
							storeRightView.remove(tmpElem);
							storeRightView.insert(i - 1, tmpElem);
							for ( var j = 0; j < tmpSelectedIds.length; j++) {
								if (tmpSelectedIds[j] == i) {
									tmpSelectedIds[j] = i - 1;
									break
								}
							}
							viewRight.select(tmpSelectedIds)
						}
					}
				}
			}
		}
	};
	var _rightBtnHandler = function() {
		if (opMode == "elemEdit") {
			var tmpIndexes = viewLeft.getSelectedIndexes();
			for ( var i = 0; i < tmpIndexes.length; i++) {
				_dblClickLeftViewHandler(viewLeft, tmpIndexes[i], viewLeft
						.getNode(tmpIndexes[i]), null)
			}
		}
	};
	var _leftBtnHandler = function() {
		if ((elemActiveAtRight != -1) && (opMode == "elemEdit")) {
			var tmpElems = viewRight.getSelectedRecords();
			for ( var i = 0; i < tmpElems.length; i++) {
				storeRightView.remove(tmpElems[i])
			}
			elemActiveAtRight = -1;
			btnMids.up.disable();
			btnMids.down.disable();
			btnMids.left.disable();
			_checkEmptyRightView()
		}
	};
	var _downBtnHandler = function() {
		if (opMode == "elemSelected") {
			var tmpArr = [];
			var tmpSelectedIds = viewLeft.getSelectedIndexes();
			for ( var tmpElem, i = (storeLeftView.getCount() - 2); i >= 0; i--) {
				if (viewLeft.isSelected(viewLeft.getNode(i))) {
					if (!viewLeft.isSelected(viewLeft.getNode(i + 1))) {
						tmpElem = storeLeftView.getAt(i);
						storeLeftView.remove(tmpElem);
						storeLeftView.insert(i + 1, tmpElem);
						tmpArr[tmpArr.length] = tmpElem.get("name");
						for ( var j = 0; j < tmpSelectedIds.length; j++) {
							if (tmpSelectedIds[j] == i) {
								tmpSelectedIds[j] = i + 1;
								break
							}
						}
						viewLeft.select(tmpSelectedIds)
					}
				}
			}
			if (tmpArr.length > 0) {
				phpPaloServer.moveElemDown(servId, dbName, dimName, tmpArr)
			}
		} else {
			if ((elemActiveAtRight != -1) && (opMode == "elemEdit")
					&& (elemActiveAtRight < (storeRightView.getCount() - 1))) {
				var tmpSelectedIds = viewRight.getSelectedIndexes();
				for ( var tmpElem, i = (storeRightView.getCount() - 2); i >= 0; i--) {
					if (viewRight.isSelected(viewRight.getNode(i))) {
						if (!viewRight.isSelected(viewRight.getNode(i + 1))) {
							tmpElem = storeRightView.getAt(i);
							storeRightView.remove(tmpElem);
							storeRightView.insert(i + 1, tmpElem);
							for ( var j = 0; j < tmpSelectedIds.length; j++) {
								if (tmpSelectedIds[j] == i) {
									tmpSelectedIds[j] = i + 1;
									break
								}
							}
							viewRight.select(tmpSelectedIds)
						}
					}
				}
			}
		}
	};
	var _newElem = function() {
		var elemName = ((viewType == 1) ? "New Element".localize()
				: "New Attribute".localize());
		var newIndex = storeLeftView.getCount();
		if ((newIndex == 1)
				&& (storeLeftView.getAt(0).get("name") == "Hit RETURN to add Elements"
						.localize().concat("..."))) {
			newIndex = 0;
			storeLeftView.getAt(0).set("name", elemName);
			storeLeftView.getAt(0).set("type", "numeric")
		} else {
			storeLeftView.add(new RecordLeftView( {
				name : elemName,
				type : "numeric",
				identifier : -1
			}))
		}
		viewLeft.select(newIndex);
		elemActive = newIndex;
		var endIndex = storeLeftView.getCount() - 1;
		var newNode = viewLeft.getNode(endIndex);
		newNode.scrollIntoView(viewLeft.body, false);
		var newRec = viewLeft.getRecord(newNode);
		dvOnEnterPlugin.activeRecord = newRec;
		dvOnEnterPlugin.startEdit(newNode,
				newRec.data[dvOnEnterPlugin.dataIndex])
	};
	var _deleteElem = function(btn) {
		if ((btn == "ok") && (opMode == "elemSelected")) {
			var tmpElems = viewLeft.getSelectedRecords();
			if (tmpElems[0].get("type") == "subset") {
				phpPaloServer.deletePaloSubset( [ servId, dbName,
						tmpElems[0].get("name") ]);
				_showWaitMsg("_msg: delete Subset".localize(), "Storing"
						.localize().concat("..."))
			} else {
				var tmpArr = new Array();
				for ( var i = 0; i < tmpElems.length; i++) {
					tmpArr[i] = tmpElems[i].get("name")
				}
				phpPaloServer.deleteElems(servId, dbName, dimName, tmpArr);
				_showWaitMsg("_msg: del Elem".localize(), "Storing".localize()
						.concat("..."))
			}
		}
	};
	var _consolidateElem = function() {
		if (opMode == "elemSelected") {
			_dblClickLeftViewHandler(viewLeft, elemActive, viewLeft
					.getNode(elemActive), null)
		}
	};
	var _renameElem = function(btn, elemNewName) {
		var sIndexes = viewLeft.getSelectedIndexes();
		if (sIndexes.length > 0) {
			var firstNode = viewLeft.getNode(sIndexes[0]);
			var scrollToElem = Ext.get(firstNode);
			firstNode.scrollIntoView(viewLeft.body, false);
			var firstRec = viewLeft.getRecord(firstNode);
			dvOnEnterPlugin.activeRecord = firstRec;
			dvOnEnterPlugin.startEdit(firstNode,
					firstRec.data[dvOnEnterPlugin.dataIndex])
		}
	};
	var _changeConsolidationFactor = function(btn, factor) {
		if (opMode == "elemEdit" && btn == "ok" && factor != ""
				&& _isNumeric(factor)) {
			storeRightView.getAt(elemActiveAtRight).set("factor", factor)
		}
	};
	var _singleClickLeftViewHandler = function(dataView, index, node, evnt) {
		if (elemActiveAtRight != -1) {
			elemActiveAtRight = -1;
			btnMids.up.disable();
			btnMids.down.disable();
			btnMids.left.disable()
		}
		if ((opMode == "gridView") || (opMode == "elemSelected")) {
			storeRightView.removeAll();
			if (opMode == "gridView") {
				_setOpMode("elemSelected")
			}
			elemActive = index;
			var tmpType = storeLeftView.getAt(elemActive).get("type");
			if (tmpType == "consolidated") {
				if (cacheChilds[storeLeftView.getAt(elemActive).get("name")]) {
					_fillConsolidateElement(storeLeftView.getAt(elemActive)
							.get("name"))
				} else {
					phpPaloServer.getChildElems(servId, dbName, dimName,
							storeLeftView.getAt(elemActive).get("name"))
				}
			} else {
				if (tmpType == "subset") {
					phpPaloServer.getSubsetList( {
						serv_id : servId,
						db_name : dbName,
						dim_name : dimName,
						subset_type : 2,
						subset_name : storeLeftView.getAt(elemActive).get(
								"name")
					})
				}
			}
		} else {
			if (opMode == "elemEdit") {
				if (elemActive != index) {
					btnMids.right.enable()
				} else {
					btnMids.right.disable()
				}
			}
		}
	};
	var _dblClickLeftViewHandler = function(dataView, index, node, evnt) {
		if (((opMode == "elemSelected") || (opMode == "gridView"))
				&& (dataMode != 1) && (viewType == 1)) {
			_setOpMode("elemEdit");
			elemActive = index;
			node.style.fontWeight = "bold";
			_checkEmptyRightView()
		} else {
			if (opMode == "elemEdit") {
				if (elemActive != index) {
					if ((storeRightView.getCount() == 1)
							&& (storeRightView.getAt(0).get("name") == "Add Element from list of Elements"
									.localize().concat("..."))) {
						storeRightView.remove(storeRightView.getAt(0))
					}
					var rowElem = storeLeftView.getAt(index);
					if (storeRightView.find("name", rowElem.data.name) == -1) {
						storeRightView.add(new RecordRightView( {
							name : storeLeftView.getAt(index).get("name"),
							type : storeLeftView.getAt(index).get("type"),
							factor : 1
						}))
					}
				}
			}
		}
	};
	var _singleClickRightViewHandler = function(dataView, index, node, evnt) {
		if (opMode == "elemEdit") {
			elemActiveAtRight = index;
			btnMids.up.enable();
			btnMids.down.enable();
			btnMids.left.enable();
			btnMids.right.disable()
		}
	};
	var _dblClickRightViewHandler = function(dataView, index, node, evnt) {
		elemActiveAtRight = -1;
		if (opMode == "elemEdit") {
			var parentElem = storeRightView.getAt(index);
			storeRightView.remove(parentElem);
			btnMids.up.disable();
			btnMids.down.disable();
			btnMids.left.disable()
		}
		_checkEmptyRightView()
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
		return
	};
	var _fillConsolidateElement = function(elemName) {
		var tmpRec;
		for ( var i = 0; i < cacheChilds[elemName].length; i++) {
			tmpRec = new RecordRightView( {
				name : cacheChilds[elemName][i].name,
				type : cacheChilds[elemName][i].type,
				factor : cacheChilds[elemName][i].weight
			});
			storeRightView.add(tmpRec)
		}
	};
	var _modifyElement = function() {
		if ((storeRightView.getCount() != 1)
				|| (storeRightView.getAt(0).get("name") != "Add Element from list of Elements"
						.localize().concat("..."))) {
			var parentElem = storeLeftView.getAt(elemActive);
			var tmpRec, tmpArr = new Array();
			for ( var i = 0; i < storeRightView.getCount(); i++) {
				tmpRec = storeRightView.getAt(i);
				tmpArr[tmpArr.length] = {
					name : tmpRec.get("name"),
					type : tmpRec.get("type"),
					factor : tmpRec.get("factor")
				}
			}
			phpPaloServer.setChildElems(servId, dbName, dimName, parentElem
					.get("name"), parentElem.get("type"), tmpArr);
			_showWaitMsg("_msg: cons Elem".localize(), "Storing".localize()
					.concat("..."))
		} else {
			_setOpMode("gridView");
			storeRightView.removeAll()
		}
	};
	var _setOpMode = function(modeType) {
		if (modeType == "elemEdit") {
			panelBottomRight.show();
			btnMids.up.disable();
			btnMids.down.disable();
			btnClose.disable();
			btnDatabase.disable();
			btnAccts.newB.disable();
			btnAccts.deleteB.disable();
			btnAccts.renameB.disable();
			btnAccts.consolidateB.disable();
			btnTreeList.disable();
			cmbElemType.disable()
		} else {
			if (modeType == "gridView") {
				if (opMode == "elemEdit") {
					var nodeActive = viewLeft.getNode(elemActive);
					nodeActive.style.fontWeight = "";
					viewLeft.clearSelections()
				}
				panelBottomRight.hide();
				btnClose.enable();
				btnDatabase.enable();
				btnAccts.newB.enable();
				btnAccts.deleteB.disable();
				btnAccts.renameB.disable();
				btnAccts.consolidateB.disable();
				btnMids.up.disable();
				btnMids.down.disable();
				btnMids.left.disable();
				btnMids.right.disable();
				cmbElemType.enable();
				if (viewType == 3) {
					btnTreeList.disable()
				} else {
					btnTreeList.enable()
				}
			} else {
				if (modeType == "elemSelected") {
					if (viewType != 3) {
						btnAccts.deleteB.enable();
						btnAccts.renameB.enable();
						btnAccts.consolidateB.enable();
						btnMids.up.enable();
						btnMids.down.enable()
					}
				}
			}
		}
		opMode = modeType
	};
	var _showMsg = function(msgText) {
		Ext.MessageBox.show( {
			msg : msgText,
			width : 300,
			buttons : Ext.MessageBox.OK,
			icon : Ext.MessageBox.QUESTION
		})
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
				msg : "_msg: Timer".localize(),
				buttons : Ext.Msg.OK,
				icon : Ext.MessageBox.ERROR
			})
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
	var _showMenuRightView = function(dataView, index, node, e) {
		if (opMode == "elemEdit") {
			_singleClickRightViewHandler(dataView, index, node, e);
			viewRight.select(index);
			elemActiveAtRight = index;
			dataView.menu = new Ext.menu.Menu(
					{
						enableScrolling : false,
						items : [
								{
									disabled : ((viewRight.getSelectionCount() > 1)),
									text : "Consolidation Factor".localize(),
									listeners : {
										click : function() {
											Ext.MessageBox
													.show( {
														title : "Change Factor"
																.localize(),
														msg : "Enter new consolidation factor"
																.localize()
																+ ":",
														buttons : Ext.MessageBox.OKCANCEL,
														fn : _changeConsolidationFactor,
														prompt : true,
														value : storeRightView
																.getAt(
																		elemActiveAtRight)
																.get("factor")
													})
										}
									}
								},
								{
									text : "Remove from source elements"
											.localize(),
									listeners : {
										click : _leftBtnHandler
									}
								},
								"-",
								{
									text : "Move Element to the Beginning"
											.localize(),
									listeners : {
										click : function() {
											for ( var tmpRec, i = 0, j = 0; i < storeRightView
													.getCount(); i++) {
												if (viewRight
														.isSelected(viewRight
																.getNode(i))) {
													tmpRec = storeRightView
															.getAt(i);
													storeRightView
															.remove(tmpRec);
													storeRightView.insert(j,
															tmpRec);
													j++
												}
											}
											_setOpMode("elemEdit");
											btnMids.left.disable()
										}
									}
								},
								{
									text : "Move Element to the End".localize(),
									listeners : {
										click : function() {
											for ( var tmpRec, i = 0; i < storeRightView
													.getCount(); i++) {
												if (viewRight
														.isSelected(viewRight
																.getNode(i))) {
													var tmpRec = storeRightView
															.getAt(i);
													storeRightView
															.remove(tmpRec);
													storeRightView.add(tmpRec);
													i--
												}
											}
											_setOpMode("elemEdit");
											btnMids.left.disable()
										}
									}
								} ]
					});
			dataView.menu.showAt(e.getXY())
		}
		e.stopEvent()
	};
	var _showMenuLeftView = function(dataView, index, node, e) {
		if (opMode != "elemEdit") {
			_singleClickLeftViewHandler(dataView, index, node, e);
			viewLeft.select(index);
			if (storeLeftView.getAt(index).get("type") != "subset") {
				dataView.menu = new Ext.menu.Menu(
						{
							enableScrolling : false,
							items : [
									{
										text : "Add".localize(),
										iconCls : "add-general",
										listeners : {
											click : function() {
												Ext.MessageBox
														.prompt(
																"New Element"
																		.localize(),
																"Enter name for new Element"
																		.localize()
																		+ ":",
																_newElem)
											}
										}
									},
									{
										text : "Delete".localize(),
										iconCls : "delete-icon",
										listeners : {
											click : function() {
												Ext.MessageBox
														.show( {
															title : "Delete Element"
																	.localize(),
															msg : "_msg: Delete Element"
																	.localize(),
															buttons : Ext.MessageBox.OKCANCEL,
															fn : _deleteElem,
															icon : Ext.MessageBox.QUESTION
														})
											}
										}
									},
									{
										text : "Rename".localize(),
										iconCls : "rename-icon",
										listeners : {
											click : _renameElem
										}
									},
									{
										text : "Consolidate".localize(),
										listeners : {
											click : _consolidateElem
										}
									},
									"-",
									{
										text : "Numeric".localize(),
										checked : (storeLeftView.getAt(
												elemActive).get("type") == "numeric"),
										group : "itemType",
										checkHandler : function(item, isChecked) {
											if (isChecked
													&& (storeLeftView.getAt(
															elemActive).get(
															"type") != "consolidated")) {
												Ext.MessageBox
														.show( {
															title : "Change Element Type"
																	.localize(),
															msg : "_msg: Change Element Type"
																	.localize(),
															buttons : Ext.MessageBox.OKCANCEL,
															fn : function(btn) {
																if (btn == "ok") {
																	phpPaloServer
																			.changeElemType(
																					servId,
																					dbName,
																					dimName,
																					storeLeftView
																							.getAt(
																									elemActive)
																							.get(
																									"name"),
																					"numeric");
																	_showWaitMsg(
																			"_msg: change type Elem"
																					.localize(),
																			"Storing"
																					.localize()
																					.concat(
																							"..."))
																}
															}
														})
											}
										}
									},
									{
										text : "String".localize(),
										checked : (storeLeftView.getAt(
												elemActive).get("type") == "string"),
										group : "itemType",
										checkHandler : function(item, isChecked) {
											if (isChecked
													&& (storeLeftView.getAt(
															elemActive).get(
															"type") != "consolidated")) {
												Ext.MessageBox
														.show( {
															title : "Change Element Type"
																	.localize(),
															msg : "_msg: Change Element Type"
																	.localize(),
															buttons : Ext.MessageBox.OKCANCEL,
															fn : function(btn) {
																if (btn == "ok") {
																	phpPaloServer
																			.changeElemType(
																					servId,
																					dbName,
																					dimName,
																					storeLeftView
																							.getAt(
																									elemActive)
																							.get(
																									"name"),
																					"string");
																	_showWaitMsg(
																			"_msg: change type Elem"
																					.localize(),
																			"Storing"
																					.localize()
																					.concat(
																							"..."))
																}
															}
														})
											}
										}
									},
									"-",
									{
										disabled : true,
										text : "Copy Elements".localize(),
										iconCls : "copy-icon"
									},
									{
										disabled : true,
										text : "Paste Elements".localize(),
										iconCls : "paste-icon"
									},
									{
										text : "Select All".localize(),
										listeners : {
											click : function() {
												var tmpArr = new Array();
												for ( var i = 0; i < storeLeftView
														.getCount(); i++) {
													tmpArr[i] = i
												}
												viewLeft.select(tmpArr)
											}
										}
									},
									"-",
									{
										text : "Move Element to the Beginning"
												.localize(),
										listeners : {
											click : function() {
												var tmpArr = [];
												for ( var i = 0; i < storeLeftView
														.getCount(); i++) {
													if (viewLeft
															.isSelected(viewLeft
																	.getNode(i))) {
														tmpArr[tmpArr.length] = storeLeftView
																.getAt(i).get(
																		"name")
													}
												}
												phpPaloServer.moveToBeginning(
														servId, dbName,
														dimName, tmpArr);
												_showWaitMsg(
														"_msg: moving Element"
																.localize(),
														"Storing".localize()
																.concat("..."))
											}
										}
									},
									{
										text : "Move Element to the End"
												.localize(),
										listeners : {
											click : function() {
												var tmpArr = [];
												for ( var i = 0; i < storeLeftView
														.getCount(); i++) {
													if (viewLeft
															.isSelected(viewLeft
																	.getNode(i))) {
														tmpArr[tmpArr.length] = storeLeftView
																.getAt(i).get(
																		"name")
													}
												}
												phpPaloServer
														.moveToEnd(servId,
																dbName,
																dimName, tmpArr);
												_showWaitMsg(
														"_msg: moving Element"
																.localize(),
														"Storing".localize()
																.concat("..."))
											}
										}
									},
									"-",
									{
										text : "Count".localize(),
										listeners : {
											click : function() {
												phpPaloServer
														.getElemsCount(servId,
																dbName, dimName)
											}
										}
									},
									{
										text : "Search for Element".localize(),
										listeners : {
											click : function() {
												Ext.MessageBox
														.show( {
															title : "Search Element"
																	.localize(),
															msg : "_msg: Search Element"
																	.localize()
																	+ ":",
															buttons : {
																cancel : "Cancel"
																		.localize(),
																ok : "Search"
																		.localize()
															},
															fn : _searchElement,
															prompt : true
														})
											}
										}
									} ]
						})
			} else {
				dataView.menu = new Ext.menu.Menu(
						{
							enableScrolling : false,
							items : [
									{
										text : "Add Subset".localize(),
										iconCls : "add-general",
										listeners : {
											click : function() {
												if (Jedox.wss.palo.workIn == Jedox.wss.palo.GRID) {
													Jedox.wss.app
															.load(
																	Jedox.wss.app.dynJSRegistry.subsetEditor,
																	[ {
																		mode : 2,
																		load_data : [
																				servId,
																				dbName,
																				dimName,
																				"",
																				2 ],
																		fnc : _reloadListOfDimsHandler
																	} ])
												} else {
													Jedox.wss.palo
															.openSubsetEditor( {
																mode : 2,
																load_data : [
																		servId,
																		dbName,
																		dimName,
																		"", 2 ],
																fnc : _reloadListOfDimsHandler
															})
												}
											}
										}
									},
									{
										text : "Delete Subset".localize(),
										iconCls : "delete-icon",
										listeners : {
											click : function() {
												Ext.MessageBox
														.show( {
															title : "Delete Subset"
																	.localize(),
															msg : "_msg: Delete Subset"
																	.localize(),
															buttons : Ext.MessageBox.OKCANCEL,
															fn : _deleteElem,
															icon : Ext.MessageBox.QUESTION
														})
											}
										}
									}, {
										text : "Rename Subset".localize(),
										iconCls : "rename-icon",
										listeners : {
											click : _renameElem
										}
									} ]
						})
			}
			dataView.menu.showAt(e.getXY())
		}
		e.stopEvent()
	};
	var _isNumeric = function(strString) {
		var strValidChars = "0123456789.-";
		var strChar;
		var blnResult = true;
		if (strString.length == 0) {
			return false
		}
		for (i = 0; i < strString.length && blnResult == true; i++) {
			strChar = strString.charAt(i);
			if (strValidChars.indexOf(strChar) == -1) {
				blnResult = false
			}
		}
		return blnResult
	};
	var _leftViewAfterRender = function() {
		if (viewLeft.rendered) {
			viewLeftDom = viewLeft.getEl().dom;
			viewLeftDom.onscroll = function(scroll) {
				_leftViewOnScroll(scroll)
			};
			Ext.DomHelper.insertFirst(viewLeftDom.id, {
				tag : "div",
				id : "div-" + dimName + "-leftView-Top"
			});
			Ext.DomHelper.append(viewLeftDom.id, {
				tag : "div",
				id : "div-" + dimName + "-leftView-Bottom"
			});
			topLeftDiv = Ext.get("div-" + dimName + "-leftView-Top");
			bottomLeftDiv = Ext.get("div-" + dimName + "-leftView-Bottom")
		}
	};
	var _updateLeftViewDivs = function() {
		var allSize = _config.elementSize * numOfElemsLeftView;
		var renderedSize = storeLeftView.getCount() * _config.elementSize;
		var topH = _config.elementSize * startIndexLeftView;
		var bottomH = allSize - topH - renderedSize;
		topLeftDiv.applyStyles("height: " + topH + "px;");
		bottomLeftDiv.applyStyles("height: " + bottomH + "px;")
	};
	var _leftViewOnScroll = function(scroll) {
		var topSpace = (viewLeftDom.scrollTop / viewLeftDom.scrollHeight)
				* _config.elementSize * numOfElemsLeftView;
		if ((topSpace > (_config.elementSize * startIndexLeftView + (storeLeftView
				.getCount() * _config.elementSize) / 2))
				&& ((startIndexLeftView + storeLeftView.getCount()) < numOfElemsLeftView)) {
			var diff = topSpace
					- (_config.elementSize * startIndexLeftView + (storeLeftView
							.getCount() * _config.elementSize) / 2);
			var requestNumOfElems = Math.round(diff / _config.elementSize);
			var fromId = startIndexLeftView + storeLeftView.getCount();
			var toId = fromId + requestNumOfElems;
			if ((fromId > requestedRange[0]) && (fromId > requestedRange[1])) {
				requestedRange[0] = fromId;
				requestedRange[1] = toId - 1;
				phpPaloServer
						.getDimElems(servId, dbName, dimName, fromId, toId)
			}
		} else {
			if ((topSpace < (_config.elementSize * startIndexLeftView + (storeLeftView
					.getCount() * _config.elementSize) / 2))
					&& (startIndexLeftView > 0)) {
				var diff = (_config.elementSize * startIndexLeftView + (storeLeftView
						.getCount() * _config.elementSize) / 2)
						- topSpace;
				var requestNumOfElems = Math.round(diff / _config.elementSize);
				var fromId = startIndexLeftView - requestNumOfElems - 1;
				fromId = (fromId < 0) ? 0 : fromId;
				var toId = startIndexLeftView;
				if ((toId < requestedRange[0]) && (toId < requestedRange[1])) {
					requestedRange[0] = fromId + 1;
					requestedRange[1] = toId;
					phpPaloServer.getDimElems(servId, dbName, dimName, fromId,
							toId)
				}
			}
		}
	};
	var _searchElement = function(btn, text) {
		if (btn == "ok") {
			var tmpIndex = storeLeftView.find("name", text);
			if (tmpIndex == -1) {
				Ext.MessageBox.alert("", "Element".localize() + " " + text
						+ " " + "was not find in dimension".localize() + " "
						+ dimName + ".")
			} else {
				viewLeft.select(tmpIndex);
				viewLeft.getNode(tmpIndex).scrollIntoView()
			}
		}
	};
	var _genSubsetList = function(subsetArr) {
		for ( var i = 0; i < subsetArr.length; i++) {
			if (subsetArr[i].l) {
				var newRowRecord = new RecordRightView( {
					name : subsetArr[i].n,
					type : "undefined",
					factor : 1
				});
				storeRightView.add(newRowRecord);
				_genSubsetList(subsetArr[i].l)
			} else {
				var newRowRecord = new RecordRightView( {
					name : subsetArr[i],
					type : "undefined",
					factor : 1
				});
				storeRightView.add(newRowRecord)
			}
		}
	};
	var _reloadListOfDimsHandler = function() {
		phpPaloServer.setWorkingMode(servId, dbName, dimName,
				((viewType == 3) ? "subsets" : ((viewType == 2) ? "attributes"
						: "elements")))
	};
	var _checkEmptyLeftView = function() {
		if (storeLeftView.getCount() == 0) {
			storeLeftView.add(new RecordLeftView( {
				id : -1,
				type : "",
				name : "Hit RETURN to add Elements".localize().concat("...")
			}));
			_setOpMode("gridView")
		}
	};
	var _checkEmptyRightView = function() {
		if (storeRightView.getCount() == 0) {
			storeRightView.add(new RecordRightView( {
				name : "Add Element from list of Elements".localize().concat(
						"..."),
				type : "",
				factor : ""
			}))
		}
	};
	var _resizeAll = function() {
		if (panelMain.rendered) {
			panelMain.getEl().unselectable();
			var pSize = panelMain.getSize();
			var w = pSize.width;
			var h = pSize.height;
			var panelDataViewsW = (w - _config.widthMidButtons) / 2;
			var panelDataViewsH = (h - _internalConf.hTop - _internalConf.whButtonLeft) - 5;
			cmbElemType.setWidth(_internalConf.wCombo);
			panelCombo.setPosition(0, 0);
			panelbtnTreeList.setPosition(panelDataViewsW
					- _internalConf.whButtonMid, 0);
			panelDataViewLeft.setWidth(panelDataViewsW);
			panelDataViewLeft.setHeight(panelDataViewsH);
			panelDataViewLeft.setPosition(0, _internalConf.hTop);
			panelDataViewRight.setWidth(panelDataViewsW);
			panelDataViewRight.setHeight(panelDataViewsH);
			panelDataViewRight.setPosition(w - panelDataViewsW,
					_internalConf.hTop);
			lblRight.setPosition(w - panelDataViewsW, 5);
			panelMid.setPosition(panelDataViewsW + 6, panelDataViewsH / 2
					- _internalConf.whButtonMid * 2 + _internalConf.hTop);
			panelBottomLeft.setPosition(0, (h - 35) + 5);
			panelBottomRight.setPosition(w - panelDataViewsW, (h - 35) + 5);
			var clsBtnW;
			if (panelCloseButton.rendered && btnClose.rendered) {
				clsBtnW = btnClose.getEl().getBox().width;
				panelCloseButton.setPosition(w - clsBtnW, (h - 35) + 5)
			} else {
				clsBtnW = 0
			}
			if (panelDataButton.rendered && btnDatabase.rendered) {
				panelDataButton.setPosition(w - clsBtnW
						- btnDatabase.getEl().getBox().width - 5, (h - 35) + 5)
			}
			panelTree.setWidth(w);
			panelTree.setHeight(panelDataViewsH);
			panelTree.setPosition(0, _internalConf.hTop)
		}
	}
};