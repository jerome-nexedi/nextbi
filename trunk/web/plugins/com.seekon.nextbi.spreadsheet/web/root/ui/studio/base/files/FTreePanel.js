Jedox.studio.files.FTreePanel = function() {
	var activeGroup;
	var gCmbSelectedIndex;
	var activeHierarchy = {};
	var selectedNode;
	var gCtxDFlag = {
		open : false,
		newGroup : false,
		deleteGroup : false,
		newHierarchy : false,
		properties : false
	};
	var hCtxDFlag = {
		open : false,
		deleteHierarchy : false,
		properties : false
	};
	var fCtxDFlag = {
		open : false,
		cut : false,
		copy : false,
		rename : false,
		remove : false,
		properties : false
	};
	var _groupNameList;
	var _hierarchyNameList;
	var refreshFlag = false;
	var _triggerSaveAsFromWSS = false;
	var _defaultHierarchyFlag = true;
	var _preselect_cb = false;
	var _nodeClick_cb = false;
	var gStore = new Ext.data.SimpleStore( {
		fields : [ "id", "name", "perm_g", "perm_h" ],
		listeners : {
			load : function(store, records, options) {
			},
			add : function(store, records, index) {
			},
			remove : function(store, record, index) {
			}
		}
	});
	var gCmb = new Ext.form.ComboBox( {
		store : gStore,
		displayField : "name",
		valueField : "id",
		hideLabel : true,
		readOnly : false,
		editable : false,
		typeAhead : true,
		mode : "local",
		triggerAction : "all",
		selectOnFocus : true,
		listWidth : 150,
		width : 150,
		listeners : {
			select : function(cmb, record, index) {
				if (activeGroup != record.data) {
					var wssStudioHandler = {
						treeSetGroup : function(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database read error".localize(),
										"read_data_err".localize());
								return false
							}
							activeGroup = record.data;
							fileTP.root.setText(record.data.name);
							fileTP.root.g_id = record.data.id;
							initHierarchies(activeGroup.id);
							gCmbSelectedIndex = index
						}
					};
					var wssStudioStub = new Studio(wssStudioHandler);
					wssStudioStub.treeSetGroup("file", record.data.id)
				}
			}
		}
	});
	var newGroupTlbBtn = {
		tooltip : {
			title : "New Folder Group".localize(),
			text : "Creates New Group".localize()
		},
		iconCls : "new-group-icon",
		handler : onAddGroup,
		scope : this
	};
	var editGTlbBtn = new Ext.SplitButton( {
		split : true,
		iconCls : "tlb-menu-icon",
		tooltip : {
			title : "Edit File Group".localize(),
			text : "Edit File Group...".localize()
		},
		menu : {
			listeners : {
				beforeshow : function() {
					var perm_g = activeGroup ? activeGroup.perm_g : 0;
					setContainerInterfacePermission("group", {
						perm_g : perm_g,
						perm_h : perm_g
					});
					gCtxDFlag.newGroup ? this.items.items[0].disable()
							: this.items.items[0].enable();
					gCtxDFlag.deleteGroup ? this.items.items[1].disable()
							: this.items.items[1].enable();
					gCtxDFlag.newHierarchy ? this.items.items[5].disable()
							: this.items.items[5].enable();
					gCtxDFlag.properties ? this.items.items[7].disable()
							: this.items.items[7].enable();
					perm_g ? this.items.items[3].enable() : this.items.items[3]
							.disable()
				}
			},
			autoWidth : true,
			items : [ {
				text : "New Folder Group".localize(),
				iconCls : "new-group-icon",
				disabled : gCtxDFlag.newGroup,
				handler : onAddGroup,
				scope : this
			}, {
				text : "Delete Folder Group".localize(),
				disabled : gCtxDFlag.deleteGroup,
				iconCls : "delete-icon",
				handler : function() {
					onDeleteGroup(root)
				},
				scope : this
			}, "-", {
				text : "Refresh Folder Group".localize(),
				handler : function() {
					refreshGroup()
				},
				iconCls : "refresh-icon",
				scope : this
			}, "-", {
				text : "New Root Folder".localize(),
				disabled : gCtxDFlag.newHierarchy,
				handler : onAddHierarchy,
				iconCls : "add-general",
				scope : this
			}, "-", {
				text : "Properties".localize(),
				disabled : gCtxDFlag.properties,
				iconCls : "properties-icon",
				handler : function() {
					onGroupProperties(root)
				},
				scope : this
			} ]
		}
	});
	var fTlb = new Ext.Toolbar( {
		id : "f-tree-tlb"
	});
	var root = new Ext.tree.TreeNode( {
		text : "root",
		draggable : false,
		editable : false,
		id : "root",
		iconCls : "w3s_group",
		expanded : true
	});
	var fileTP = new Ext.tree.TreePanel(
			{
				id : "ps-wt-tree",
				iconCls : "folder-icon",
				animate : false,
				border : false,
				lines : true,
				containerScroll : true,
				ddScroll : true,
				autoScroll : true,
				height : 50,
				enableDD : true,
				root : root,
				rootVisible : false,
				hidden : true,
				listeners : {
					click : function(node, e) {
						if (!_nodeClick_cb) {
							var wssStudioHandler = {
								treeSetHierarchy : function(result) {
									if (!result) {
										Jedox.studio.app.showMessageERROR(
												"Database read error"
														.localize(),
												"read_data_err".localize());
										return false
									}
									fileTP.fireEvent("click", node)
								}
							};
							if (node.id != "root") {
								if (node.attributes.type) {
									if (activeHierarchy.id != node.attributes.id) {
										var wssStudioStub = new Studio(
												wssStudioHandler);
										wssStudioStub.treeSetHierarchy("file",
												node.attributes.id);
										_nodeClick_cb = true;
										activeHierarchy.id = node.attributes.id;
										return false
									}
								} else {
									if (activeHierarchy.id != node.attributes.h_uid) {
										var wssStudioStub = new Studio(
												wssStudioHandler);
										wssStudioStub.treeSetHierarchy("file",
												node.attributes.h_uid);
										_nodeClick_cb = true;
										activeHierarchy.id = node.attributes.h_uid;
										return false
									}
								}
							}
						} else {
							_nodeClick_cb = false
						}
						node.expand()
					},
					select : function(n_uid) {
						var id = getNodeId(n_uid);
						var node = this.getNodeById(id);
						this.fireEvent("click", node)
					},
					renameNode : function(n_uid, newName) {
						var id = getNodeId(n_uid);
						var node = this.getNodeById(id);
						if (node) {
							node.setText(newName)
						}
					},
					removeNode : function(n_uid) {
						var id = getNodeId(n_uid);
						var node = this.getNodeById(id);
						if (node) {
							node.remove()
						}
					},
					removeHierarchy : function(id) {
						var node = this.getNodeById(id);
						if (node) {
							node.remove()
						}
					},
					up : function(n_uid) {
						if (n_uid == "root") {
							var parentNode = this.root;
							this.fireEvent("click", parentNode)
						} else {
							var id = getNodeId(n_uid);
							var node = this.getNodeById(id);
							if (node) {
								var parentNode = node.parentNode;
								this.fireEvent("click", parentNode)
							}
						}
					},
					addNode : function(n_uid, nodeData, perm_n) {
						var newNode = new Ext.tree.TreeNode( {
							text : nodeData.text,
							n_uid : nodeData.id,
							iconCls : nodeData.iconCls,
							h_uid : activeHierarchy.id,
							id : activeHierarchy.id + "_" + nodeData.id,
							qtip : nodeData.qtip,
							leaf : nodeData.leaf,
							perm_n : perm_n
						});
						var id = getNodeId(n_uid);
						var parent = this.getNodeById(activeHierarchy.id);
						if (n_uid != "root") {
							parent = this.getNodeById(id)
						}
						parent.appendChild(newNode)
					},
					editId : function(n_uid, newId) {
						var id = getNodeId(n_uid);
						var node = this.getNodeById(id);
						node.attributes.id = newId;
						node.id = newId
					},
					test : function(id) {
						var node = this.getNodeById(id);
						node.attributes.loader.load(node)
					},
					replaceNode : function(n_uid, newParentId) {
						var id = getNodeId(n_uid);
						var node = this.getNodeById(id);
						var newNode = {
							id : node.attributes.n_uid,
							text : node.attributes.text,
							leaf : node.attributes.leaf,
							qtip : node.attributes.qtip,
							iconCls : node.attributes.iconCls
						};
						this.fireEvent("removeNode", n_uid);
						this.fireEvent("addNode", newParentId, newNode)
					},
					contextmenu : function(node, e) {
						selectedNode = fileTP.selModel.getSelectedNode();
						node.select();
						itemClicked = false;
						if (node.id == "root") {
							onGroupCtxMnu(node, e)
						} else {
							if (node.attributes.type) {
								onHierarchyCtxMnu(node, e)
							} else {
								onFolderCtxMnu(node, e)
							}
						}
					},
					beforenodedrop : function(el) {
						var tmpParent = el.target.attributes.h_uid
								|| el.target.attributes.id;
						if (el.dropNode.attributes.h_uid === tmpParent) {
							onDDCtxMnu(el, el.rawEvent)
						}
						return false
					},
					onEditTmpl : function(group, hierarchy, n) {
						preselectOnEditTmpl(group, hierarchy, n, false)
					},
					onRefresh : function(group, hierarchy, n) {
						_triggerSaveAsFromWSS = true;
						refreshFlag = true;
						refreshAndSelectPathButNotOpen(group, hierarchy, n)
					},
					onExplore : function(group, hierarchy, n) {
						refreshAndSelectPathButNotOpen(group, hierarchy, n)
					},
					refreshOnCopy : function(n) {
						refreshOnCopy(n);
						refreshFlag = true
					},
					keyPress : function(myEvent) {
						keyboardDispacher(myEvent)
					},
					reloadActiveHierarchy : function() {
						reloadActiveHierarchy()
					},
					onGetPath : function(n_uid, cbFnc) {
						if (n_uid == "root") {
							var id = activeHierarchy.id
						} else {
							id = getNodeId(n_uid)
						}
						var node = this.getNodeById(id);
						var path = node.getPath( [ "text" ]);
						cbFnc("/".concat(path, "/"))
					},
					onNodeProperties : function(id) {
						var node = fileTP.selModel.getSelectedNode();
						if (id == "root") {
							onHierarchyProperties(node)
						} else {
							onProperties(node)
						}
					}
				}
			});
	var editor = new Ext.tree.TreeEditor(fileTP, null, {
		id : "f-tree-node-editor",
		editDelay : 0,
		cancelOnEsc : true,
		allowBlank : false,
		ignoreNoChange : true,
		completeOnEnter : true,
		beforeNodeClick : Ext.emptyFn,
		onNodeDblClick : Ext.emptyFn,
		onNodeClick : Ext.emptyFn
	});
	editor.on( {
		complete : function(editor, newName, oldName) {
			if (newName != oldName) {
				if (validateName(editor.editNode, oldName, newName)) {
					var parentId = editor.editNode.parentNode.attributes.type
							|| editor.editNode.parentNode.attributes.id;
					var id = editor.editNode.attributes.id;
					Jedox.studio.backend.wssStudio.treeMngNode("file",
							parentId, "renameNode", id, newName)
				} else {
					editor.cancelEdit()
				}
			}
		}
	});
	function initTlb() {
		if (gCmb.rendered) {
			if (_preselect_cb) {
				_preselect_cb()
			}
			if (Jedox.studio.files.openRecentClb) {
				Jedox.studio.files.openRecentClb()
			}
			return
		}
		fTlb.addField(gCmb);
		fTlb.add(editGTlbBtn);
		fTlb.doLayout();
		init()
	}
	function init() {
		function initGroups_cb(result) {
			if (result) {
				function selectGroup_cb(result) {
					if (result) {
						initHierarchies()
					} else {
						if (Jedox.studio.files.openRecentClb) {
							Jedox.studio.files.openRecentClb()
						}
					}
				}
				fileTP.show();
				if (_preselect_cb) {
					_preselect_cb()
				} else {
					selectGroup(getDefaultGroupIndex(), selectGroup_cb)
				}
			} else {
				if (Jedox.studio.files.openRecentClb) {
					Jedox.studio.files.openRecentClb()
				}
			}
		}
		initGroups(initGroups_cb)
	}
	function getDefaultGroupIndex() {
		var index = gStore.find("id", Jedox.studio.app.defaultFiles.group);
		return index != -1 ? index : 0
	}
	function getActiveGroupIndex() {
		var index = gStore.find("id", activeGroup.id);
		return index != -1 ? index : 0
	}
	function initGroups(cb) {
		var wssStudioHandler = {
			treeMngGroup : function(result) {
				_groupNameList = {};
				if (result == null) {
					Jedox.studio.app.showMessageERROR("Database read error"
							.localize(), "read_data_err".localize());
					if (cb) {
						cb(false)
					}
					return false
				}
				if (result instanceof Array) {
					return
				}
				var gData = [];
				groupExist = true;
				for ( var i in result) {
					gData.push( [ i, result[i].name, result[i].perm_g,
							result[i].perm_h ]);
					_groupNameList[result[i].name] = i
				}
				gStore.loadData(gData);
				gCmbSelectedIndex = 0;
				if (cb) {
					cb(true)
				}
			}
		};
		var wssStudioStub = new Studio(wssStudioHandler);
		wssStudioStub.treeMngGroup("listGroups", [ "file" ])
	}
	function selectGroup(index, cb) {
		var wssStudioHandler = {
			treeSetGroup : function(result) {
				if (!result) {
					Jedox.studio.app.showMessageERROR("Database read error"
							.localize(), "read_data_err".localize());
					if (cb) {
						cb(false)
					}
					return false
				}
				activeGroup = record.data;
				gCmb.setValue(record.data.id);
				fileTP.root.setText(record.data.name);
				fileTP.root.g_id = record.data.id;
				if (cb) {
					cb(true)
				}
			}
		};
		if (gStore.getCount() > 0) {
			var record = gStore.getAt(index);
			var wssStudioStub = new Studio(wssStudioHandler);
			wssStudioStub.treeSetGroup("file", record.data.id)
		} else {
			activeGroup = null;
			gCmb.disable();
			if (cb) {
				cb(false)
			}
			return false
		}
	}
	function refreshGroup() {
		function initGroups_cb(result) {
			if (result) {
				function selectGroup_cb(result) {
					if (result) {
						initHierarchies()
					}
				}
				selectGroup(getActiveGroupIndex(), selectGroup_cb)
			}
		}
		initGroups(initGroups_cb)
	}
	function initHierarchies(group, cb) {
		var that = this;
		_hierarchyNameList = {};
		var nodes = [];
		fileTP.root.eachChild(function(node) {
			nodes.push(node)
		}, [ fileTP ]);
		for ( var i = 0; i < nodes.length; i++) {
			nodes[i].remove()
		}
		if (!group) {
			group = activeGroup.id
		}
		var hData = [];
		var wssStudioHandler = {
			treeMngHierarchy : function(result) {
				if (result != null && !result) {
					Jedox.studio.app.showMessageERROR("Database read error"
							.localize(), "read_data_err".localize());
					return false
				}
				for ( var i in result) {
					fileTP.root
							.appendChild(new Ext.tree.AsyncTreeNode(
									{
										id : i,
										text : result[i].name,
										draggable : false,
										editable : false,
										iconCls : "w3s_hierarchy",
										perm_h : result[i].perm_h,
										perm_n : result[i].perm_n,
										type : "root",
										loader : new Ext.tree.TreeLoader(
												{
													dataUrl : Jedox.studio.backend.wssStudio.dispatcher.serverUrl
															+ "&c="
															+ Jedox.studio.backend.wssStudio.className
															+ "&m=treeDump",
													baseParams : {
														type : "file",
														hierarchy : i,
														filter : "folder",
														multi_h : true
													}
												})
									}));
					_hierarchyNameList[result[i].name] = i
				}
				fileTP.root
						.expand(
								false,
								false,
								function(gNode) {
									if (_triggerSaveAsFromWSS) {
										_triggerSaveAsFromWSS = false;
										return
									}
									if (cb) {
										cb();
										return
									}
									if (fileTP.root.hasChildNodes()) {
										if (_defaultHierarchyFlag) {
											var hNode = fileTP
													.getNodeById(Jedox.studio.app.defaultFiles.hierarchy)
													|| fileTP
															.getNodeById(fileTP.root.firstChild.attributes.id);
											_defaultHierarchyFlag = false
										} else {
											var hNode = fileTP
													.getNodeById(fileTP.root.firstChild.attributes.id)
										}
									} else {
										var hNode = fileTP
												.getNodeById(fileTP.root.attributes.id)
									}
									if (hNode) {
										hNode.expand(false, false, function(
												hNode) {
											fileTP.fireEvent("click", hNode)
										})
									}
								})
			}
		};
		var wssStudioStub = new Studio(wssStudioHandler);
		wssStudioStub.treeMngHierarchy("file", "listHierarchies")
	}
	function onGroupCtxMnu(node, e) {
		var that = this;
		setContainerInterfacePermission( {
			g : activeGroup.id,
			h : "meta",
			n : "group"
		});
		var groupCtxMnu = new Ext.menu.Menu( {
			id : "f-group-ctx-mnu",
			enableScrolling : false,
			listeners : {
				hide : function(menu) {
					menu.destroy()
				}
			},
			items : [ {
				text : "Open".localize(),
				disabled : gCtxDFlag.open,
				iconCls : "open-folder-icon",
				handler : function() {
					onOpen(node)
				},
				scope : that
			}, {
				text : "Delete Group".localize(),
				disabled : gCtxDFlag.deleteGroup,
				iconCls : "delete-icon",
				handler : function() {
					onDeleteGroup(node)
				},
				scope : that
			}, "-", {
				text : "New File Repository".localize(),
				disabled : gCtxDFlag.newHierarchy,
				handler : onAddHierarchy,
				iconCls : "add-general",
				scope : that
			}, "-", {
				text : "Properties".localize(),
				disabled : gCtxDFlag.properties,
				iconCls : "properties-icon",
				handler : function() {
					onGroupProperties(node)
				},
				scope : that
			} ]
		});
		groupCtxMnu.on("hide", onContextHide, groupCtxMnu);
		e.stopEvent();
		groupCtxMnu.showAt(e.getXY())
	}
	function onHierarchyCtxMnu(node, e) {
		var that = this;
		activeHierarchy.id = node.attributes.id;
		setContainerInterfacePermission("hierarchy", {
			perm_h : node.attributes.perm_h,
			perm_n : node.attributes.perm_n
		});
		var hierarchyCtxMnu = new Ext.menu.Menu( {
			id : "f-hierarchy-ctx-mnu",
			enableScrolling : false,
			listeners : {
				hide : function(menu) {
					menu.destroy()
				}
			},
			autoWidth : true,
			items : [ {
				text : "Open".localize(),
				iconCls : "open-folder-icon",
				disabled : hCtxDFlag.open,
				handler : function() {
					onOpen(node)
				},
				scope : that
			}, {
				text : "Delete Root Folder".localize(),
				disabled : hCtxDFlag.deleteHierarchy,
				iconCls : "delete-icon",
				handler : function() {
					onDeleteHierarchy(node)
				},
				scope : that
			}, "-", {
				text : "Properties".localize(),
				disabled : hCtxDFlag.properties,
				iconCls : "properties-icon",
				handler : function() {
					onHierarchyProperties(node)
				},
				scope : that
			} ]
		});
		e.stopEvent();
		hierarchyCtxMnu.showAt(e.getXY())
	}
	function onFolderCtxMnu(node, e) {
		var that = this;
		var parent = node.parentNode.attributes.type ? node.parentNode.attributes.id
				: node.parentNode.attributes.n_uid;
		setContainerInterfacePermission("node", {
			perm_n : node.attributes.perm_n
		});
		var folderCtxMnu = new Ext.menu.Menu( {
			id : "f-folder-ctx-mnu",
			enableScrolling : false,
			listeners : {
				hide : function(menu) {
					menu.destroy()
				}
			},
			items : [ {
				text : "Open".localize(),
				iconCls : "open-folder-icon",
				handler : function() {
					onOpen(node)
				},
				scope : that
			}, "-", {
				text : "Rename".localize(),
				disabled : fCtxDFlag.rename,
				iconCls : "rename-icon",
				handler : function() {
					onRenameFolder(node)
				},
				scope : that
			}, {
				text : "Delete".localize(),
				iconCls : "delete-icon",
				disabled : fCtxDFlag.remove,
				handler : function() {
					onDeleteFolder(node)
				},
				scope : that
			}, "-", {
				text : "Properties".localize(),
				disabled : fCtxDFlag.properties,
				iconCls : "properties-icon",
				handler : function() {
					onProperties(node)
				},
				scope : that
			} ],
			listeners : {
				itemclick : function(baseItem, e) {
					itemClicked = true
				}
			}
		});
		folderCtxMnu.on("hide", onContextHide, folderCtxMnu);
		e.stopEvent();
		folderCtxMnu.showAt(e.getXY())
	}
	function onDDCtxMnu(el, e) {
		var that = this;
		var ddCtxMnu = new Ext.menu.Menu(
				{
					id : "f-group-ctx-mnu",
					enableScrolling : false,
					listeners : {
						hide : function(menu) {
							menu.destroy()
						}
					},
					items : [
							{
								text : "Move".localize(),
								iconCls : "cut-icon",
								disabled : !(el.target.attributes.perm_n & Jedox.studio.access.permType.WRITE)
										|| !(el.dropNode.attributes.perm_n & Jedox.studio.access.permType.WRITE),
								handler : function() {
									onMoveNode(el)
								},
								scope : that
							},
							{
								text : "Copy".localize(),
								disabled : !(el.target.attributes.perm_n & Jedox.studio.access.permType.WRITE),
								iconCls : "copy-icon",
								handler : function() {
									onCopyNode(el)
								},
								scope : that
							} ]
				});
		ddCtxMnu.on("hide", onContextHide, ddCtxMnu);
		e.stopEvent();
		ddCtxMnu.showAt(e.getXY())
	}
	function onContextHide(ctxMnu) {
		if (ctxMnu) {
			ctxMnu.destroy();
			ctxMnu = null
		}
	}
	function getNewItemCopyName(target, tmplName) {
		var br = 0;
		var exist = true;
		var name = tmplName.toLowerCase();
		while (exist) {
			exist = false;
			if (br == 1) {
				name = ("Copy".localize() + " of ".localize() + tmplName)
						.toLowerCase()
			}
			if (br > 1) {
				name = ("Copy".localize() + " (" + br + ") " + "of ".localize() + tmplName)
						.toLowerCase()
			}
			for ( var i = 0; i < target.childNodes.length; i++) {
				if (name === target.childNodes[i].attributes.text.toLowerCase()) {
					br = br + 1;
					exist = true;
					break
				}
			}
		}
		return (br == 1) ? ("Copy".localize() + " of ".localize() + tmplName)
				: ((br > 0) ? ("Copy".localize() + " (" + br + ") "
						+ "of ".localize() + tmplName) : (tmplName))
	}
	function onCopyNode(el) {
		var parentId = el.target.attributes.id;
		var tmpParentId = el.target.attributes.type || parentId;
		if (checkNameAvailable(el.target, el.dropNode.attributes.text)) {
			copyNode()
		} else {
			var newName = getNewItemCopyName(el.target,
					el.dropNode.attributes.text);
			copyNode(newName)
		}
		function copyNode(newName) {
			var newId = Jedox.studio.backend.wssStudio.treeMngNode("file",
					el.dropNode.attributes.id, "copyNode", tmpParentId);
			if (newId) {
				if (newName) {
					Jedox.studio.backend.wssStudio.treeMngNode("file",
							el.dropNode.attributes.id, "renameNode", newId,
							newName)
				}
				refreshOnCopy(newId)
			} else {
				var title = "Copy folder".localize();
				var message = "copy_folder_error_msg".localize( {
					folder_name : el.dropNode.attributes.text
				});
				Jedox.studio.app.showMessageERROR(title, message)
			}
		}
	}
	function checkNameAvailable(target, name) {
		for ( var i = 0; i < target.childNodes.length; i++) {
			if (name === target.childNodes[i].attributes.text) {
				return false
			}
		}
		return true
	}
	function getNodeByName(target, name) {
		for ( var i = 0; i < target.childNodes.length; i++) {
			if (name === target.childNodes[i].attributes.text) {
				return target.childNodes[i]
			}
		}
	}
	function onMoveNode(el) {
		if (el.dropNode.parentNode.attributes.n_uid != el.target.attributes.n_uid) {
			if (checkNameAvailable(el.target, el.dropNode.attributes.text)) {
				moveNode()
			} else {
				var title = "Confirm Item Replece".localize();
				var message = "replace_folder_warning_msg".localize( {
					name : el.dropNode.attributes.text
				});
				var fn = function() {
					var nodeToRemove = getNodeByName(el.target,
							el.dropNode.attributes.text);
					var tmpParentId = el.target.attributes.type
							|| el.target.attributes.n_uid;
					Jedox.studio.backend.wssStudio.treeMngNode("file",
							tmpParentId, "removeNode",
							nodeToRemove.attributes.n_uid);
					fileTP.fireEvent("removeNode",
							nodeToRemove.attributes.n_uid);
					moveNode()
				};
				Jedox.studio.app.showMessageQUESTION(title, message, fn)
			}
		}
		function moveNode() {
			var tmpParentId = el.target.attributes.type
					|| el.target.attributes.n_uid;
			var result = Jedox.studio.backend.wssStudio.treeMngNode("file",
					el.dropNode.attributes.n_uid, "moveNode", tmpParentId);
			if (result === true) {
				refreshOnCopy(tmpParentId)
			} else {
				var title = "Cut folder".localize();
				var message = "cut_folder_error_msg".localize( {
					folder_name : el.dropNode.attributes.text
				});
				Jedox.studio.app.showMessageERROR(title, message)
			}
		}
	}
	function onOpen(node) {
		fileTP.fireEvent("click", node);
		onClick(node)
	}
	function getGroupData(group) {
		var gData = Jedox.studio.backend.wssStudio.getGroupData(group);
		return gData
	}
	function setGroupData(group, name, description) {
		Jedox.studio.backend.wssStudio.setGroupPropertiesData("file", group,
				name, description);
		fileTP.fireEvent("select", "root")
	}
	function onAddGroup() {
		var fn = function(gName, gDesc) {
			var wssStudioHandler = {
				treeMngGroup : function(result) {
					if (!result) {
						Jedox.studio.app.showMessageERROR(
								"Database write error".localize(),
								"write_data_err".localize());
						return false
					}
					function initGroups_cb(result) {
						if (result) {
							fileTP.show();
							gCmb.fireEvent("select", gCmb, gStore.getAt(gStore
									.getCount() - 1), (gStore.getCount() - 1));
							gCmb.setValue(gName)
						}
					}
					initGroups(initGroups_cb)
				}
			};
			var wssStudioStub = new Studio(wssStudioHandler);
			wssStudioStub.treeMngGroup("addGroup", "file", {
				name : gName,
				desc : gDesc
			})
		};
		Jedox.studio.files.openAddGroup("New", fn, _groupNameList)
	}
	function onGroupProperties(node) {
		var renameFnc = function(gName, gDesc) {
			if (gName in _groupNameList) {
				var title = "Error Renaming Group".localize();
				var msg = "rename_group_error_msg".localize( {
					new_name : gName
				});
				Jedox.studio.app.showMessageERROR(title, msg)
			} else {
				setGroupData(activeGroup.id, gName, gDesc);
				var record = gStore.getAt(gCmbSelectedIndex);
				record.set("name", gName);
				gCmb.setValue(gName);
				root.setText(gName)
			}
		};
		var refreshFnc = function() {
			function initGroups_cb(result) {
				if (result) {
					function selectGroup_cb(result) {
						if (result) {
							initHierarchies()
						}
					}
					selectGroup(getActiveGroupIndex(), selectGroup_cb)
				}
			}
			initGroups(initGroups_cb)
		};
		var data = Jedox.studio.backend.wssStudio
				.getGroupPropertiesData("file");
		Jedox.studio.files.properties("group", data, renameFnc, refreshFnc)
	}
	function onDeleteGroup(node) {
		var title = "Remove Group".localize();
		if (gStore.getCount() == 1) {
			var errMsg = "Can not delete group!".localize();
			Jedox.studio.app.showMessageERROR(title, errMsg);
			return false
		}
		var msg = "remove_group_warning_msg".localize( {
			name : node.attributes.text
		});
		var fn = function() {
			var wssStudioHandler = {
				treeMngGroup : function(result) {
					if (!result) {
						Jedox.studio.app.showMessageERROR("Delete Folder Group"
								.localize(),
								"An error occured? Can not delete Folder Group!"
										.localize());
						return false
					}
					init();
					Jedox.studio.app.showTopMsg("",
							"Group removed successefully".localize())
				}
			};
			var wssStudioStub = new Studio(wssStudioHandler);
			wssStudioStub.treeMngGroup("removeGroup", node.g_id)
		};
		Jedox.studio.app.showMessageQUESTION(title, msg, fn)
	}
	function getHierarchyData(group, hierarchy) {
		var hData = Jedox.studio.backend.wssStudio.getHierarchyData(group,
				hierarchy);
		return hData
	}
	function setHierarchyData(group, hierarchy, name, description, path) {
		var result = Jedox.studio.backend.wssStudio.setHierarchyPropertiesData(
				"file", group, hierarchy, name, description, path)
	}
	function onAddHierarchy() {
		var fn = function(hName, hDesc, hPath) {
			var wssStudioHandler = {
				treeMngHierarchy : function(result) {
					if (!result) {
						Jedox.studio.app.showMessageERROR(
								"Database write error".localize(),
								"write_data_err".localize());
						return false
					}
					initHierarchies(activeGroup.id)
				}
			};
			var wssStudioStub = new Studio(wssStudioHandler);
			wssStudioStub.treeMngHierarchy("file", "addHierarchy", "file", {
				name : hName,
				desc : hDesc,
				backend : {
					type : "local",
					location : hPath
				}
			})
		};
		Jedox.studio.files.openAddHierarchy("New", fn, _hierarchyNameList)
	}
	function onHierarchyProperties(node) {
		var renameFnc = function(hName, hDesc, hPath) {
			if (hName != node.attributes.text && hName in _hierarchyNameList) {
				var title = "Error Renaming Hierarchy".localize();
				var msg = "rename_hierarchy_error_msg".localize( {
					new_name : hName
				});
				Jedox.studio.app.showMessageERROR(title, msg)
			} else {
				setHierarchyData(activeGroup.id, node.attributes.id, hName,
						hDesc, hPath);
				initHierarchies(activeGroup.id);
				fileTP.fireEvent("select", "root")
			}
		};
		var refreshFnc = function(permission) {
			if (permission == "N") {
				initHierarchies(activeGroup.id)
			} else {
				var h_node = fileTP.getNodeById(activeHierarchy.id);
				var map = {
					N : 0,
					R : 1,
					W : 3,
					D : 7
				};
				h_node.attributes.perm_h = map[permission];
				h_node.reload();
				h_node.select()
			}
		};
		var data = Jedox.studio.backend.wssStudio.getHierarchyPropertiesData(
				"file", node.attributes.id);
		Jedox.studio.files.properties("hierarchy", data, renameFnc, refreshFnc)
	}
	function onDeleteHierarchy(node) {
		var title = "Remove Hierarchy".localize();
		var msg = "remove_hierarchy_warning_msg".localize( {
			name : node.attributes.text
		});
		var fn = function() {
			var wssStudioHandler = {
				treeMngHierarchy : function(result) {
					if (!result) {
						Jedox.studio.app.showMessageERROR("Remove Hierarchy"
								.localize(),
								"An error occured? Can not delete this hierarchy!"
										.localize());
						return false
					}
					fileTP.fireEvent("removeHierarchy", node.attributes.id);
					Jedox.studio.app.showTopMsg("",
							"Hierarchy removed successefully".localize());
					Ext.getCmp("wt-panel").clear();
					Ext.getCmp("wt-panel").disableTlb();
					Jedox.studio.app.reportsRefreshFlag = true
				}
			};
			var wssStudioStub = new Studio(wssStudioHandler);
			wssStudioStub.treeMngHierarchy("file", "removeHierarchy",
					node.attributes.id)
		};
		Jedox.studio.app.showMessageR_QUESTION_YND( {
			nodes : [ "root" ],
			container : true
		}, title, msg, fn)
	}
	function onDeleteFolder(node) {
		var title = "Remove Folder".localize();
		var msg = "remove_folder_warning_msg".localize( {
			name : node.attributes.text
		});
		var fn = function() {
			var parentId = node.parentNode.attributes.id;
			var tmpParentId = node.parentNode.attributes.type || parentId;
			var wssStudioHandler = {
				treeMngNode : function(result) {
					if (!result) {
						Jedox.studio.app.showMessageERROR("Remove Folder"
								.localize(),
								"An error occured? Can not delete this item!"
										.localize());
						return false
					}
					fileTP.fireEvent("removeNode", node.attributes.n_uid);
					Jedox.studio.app.showTopMsg("",
							"Folder removed successefully".localize());
					fileTP.fireEvent("select", parentId);
					Jedox.studio.app.reportsRefreshFlag = true
				}
			};
			var wssStudioStub = new Studio(wssStudioHandler);
			wssStudioStub.treeMngNode("file", tmpParentId, "removeNode",
					node.attributes.id)
		};
		Jedox.studio.app.showMessageR_QUESTION_YND( {
			nodes : [ node.attributes.n_uid ],
			container : true
		}, title, msg, fn)
	}
	function onRenameFolder(node) {
		editor.triggerEdit(node)
	}
	function onProperties(node) {
		function renameHandleFnc(newName) {
			if (validateName(node, node.attributes.text, newName)) {
				var parentId = node.parentNode.attributes.type
						|| node.parentNode.attributes.id;
				var id = node.attributes.id;
				Jedox.studio.backend.wssStudio.treeMngNode("file", parentId,
						"renameNode", id, newName);
				node.setText(newName)
			}
		}
		var refreshFnc = function() {
			reloadActiveHierarchy()
		};
		var data = Jedox.studio.backend.wssStudio.getNodePropertiesData("file",
				activeGroup.id, activeHierarchy.id, node.attributes.n_uid);
		Jedox.studio.files
				.properties("file", data, renameHandleFnc, refreshFnc)
	}
	function reloadActiveHierarchy() {
		var h_node = fileTP.getNodeById(activeHierarchy.id);
		h_node.reload();
		fileTP.fireEvent("select", activeHierarchy.id)
	}
	function test(parentNode, name) {
		parentNode.eachChild(function(node) {
			console.log(node.attributes.text)
		}, [ this ])
	}
	function getNodeId(n_uid) {
		if (fileTP.getNodeById(n_uid)) {
			return n_uid
		}
		return activeHierarchy.id + "_" + n_uid
	}
	function validateName(node, oldName, newName) {
		var parent = node.parentNode;
		var _return = true;
		if (oldName != newName) {
			if (newName.length < 64) {
				parent.eachChild(function(node) {
					if (node.attributes.text.toLowerCase() == newName
							.toLowerCase()) {
						_return = false
					}
				}, [ this ])
			} else {
				var title = "Error Renaming File or Folder".localize();
				var msg = "rename_item_long_error_msg".localize( {
					old_name : oldName,
					new_name : newName
				});
				var fn = function() {
					node.setText(oldName);
					editor.triggerEdit(node)
				};
				Jedox.studio.app.showMessageQUESTIONERROR(title, msg, fn);
				return false
			}
		}
		if (!_return) {
			var title = "Error Renaming Folder".localize();
			var msg = "rename_folder_error_msg".localize( {
				old_name : oldName,
				new_name : newName
			});
			var fn = function() {
				node.setText(oldName);
				editor.triggerEdit(node)
			};
			Jedox.studio.app.showMessageQUESTIONERROR(title, msg, fn)
		}
		return _return
	}
	function trim(s) {
		return s.replace(/^\s+|\s+$/g, "")
	}
	function preselectOnEditTmpl(group, hierarchy, n, NOTopenFlag) {
		_preselect_cb = function() {
			for ( var i = 0, count = gStore.getCount(); i < count; i++) {
				if (gStore.getAt(i).get("id") == group) {
					break
				}
			}
			selectGroup(
					i,
					function(result) {
						if (result) {
							initHierarchies(
									group,
									function() {
										var hNode = fileTP
												.getNodeById(hierarchy);
										hNode
												.expand(
														false,
														false,
														function(hNode) {
															var wssStudioHandler = {
																getElementPath : function(
																		result) {
																	if (!result[0]) {
																		Jedox.studio.app
																				.showMessageERROR(
																						"Database read error"
																								.localize(),
																						"read_data_err"
																								.localize());
																		return false
																	}
																	var path = "/root/"
																			+ result[1];
																	fileTP
																			.expandPath(
																					path,
																					false,
																					function(
																							bSuccess,
																							oLastNode) {
																						if (bSuccess
																								&& oLastNode) {
																							if (result.length == 2
																									&& result[1]) {
																								var id = result[1]
																										.split("/")[result[1]
																										.split("/").length - 1];
																								var node = fileTP
																										.getNodeById(id);
																								if (node) {
																									fileTP
																											.fireEvent(
																													"click",
																													node);
																									setTimeout(
																											function() {
																												Ext
																														.getCmp(
																																"wt-panel")
																														.editWB(
																																group,
																																hierarchy,
																																n,
																																NOTopenFlag)
																											},
																											500)
																								}
																							}
																						}
																					})
																}
															};
															var wssStudioStub = new Studio(
																	wssStudioHandler);
															wssStudioStub
																	.getElementPath(
																			group,
																			hierarchy,
																			n)
														})
									})
						}
					});
			_preselect_cb = false
		};
		Ext.getCmp("files-tree-holder").expand()
	}
	function refreshAndSelectPathButNotOpen(group, hierarchy, n) {
		preselectOnEditTmpl(group, hierarchy, n, true)
	}
	function refreshOnCopy(n) {
		var hNode = fileTP.getNodeById(activeHierarchy.id);
		hNode.reload(function() {
			hNode.expand(false, false, function() {
				var wssStudioHandler = {
					getElementPath : function(result) {
						if (!result[0]) {
							Jedox.studio.app.showMessageERROR(
									"Database read error".localize(),
									"read_data_err".localize());
							return false
						}
						var path = "/root/" + result[1] + "/"
								+ activeHierarchy.id + "_" + n;
						fileTP.expandPath(path, false, function(bSuccess,
								oLastNode) {
							if (bSuccess && oLastNode) {
								if (result.length == 2 && result[1]) {
									var id = activeHierarchy.id + "_" + n;
									var node = fileTP.getNodeById(id);
									fileTP.fireEvent("click", node)
								}
							}
						})
					}
				};
				var wssStudioStub = new Studio(wssStudioHandler);
				wssStudioStub.getElementPath(activeGroup.id,
						activeHierarchy.id, n)
			})
		})
	}
	function setContainerInterfacePermission(type, permObj) {
		switch (type) {
		case "group":
			setGroupInterfacePermission(permObj);
			break;
		case "hierarchy":
			setHierarchyInterfacePermission(permObj);
			break;
		case "node":
			setFolderInterfacePermission(permObj);
			break
		}
		function setGroupInterfacePermission(permObj) {
			var permType = Jedox.studio.access.permType;
			gCtxDFlag.newGroup = !(Jedox.studio.access.perm_g & permType.WRITE);
			gCtxDFlag.deleteGroup = !(permObj.perm_g & permType.DELETE);
			gCtxDFlag.properties = !(permObj.perm_g & permType.WRITE);
			gCtxDFlag.newHierarchy = !(permObj.perm_h & permType.WRITE)
		}
		function setHierarchyInterfacePermission(permObj) {
			var permType = Jedox.studio.access.permType;
			hCtxDFlag.deleteHierarchy = !(permObj.perm_h & permType.DELETE);
			hCtxDFlag.properties = !(permObj.perm_h & permType.WRITE)
		}
		function setFolderInterfacePermission(permObj) {
			var permType = Jedox.studio.access.permType;
			fCtxDFlag.rename = !(permObj.perm_n & permType.WRITE);
			fCtxDFlag.remove = !(permObj.perm_n & permType.DELETE);
			fCtxDFlag.properties = !(permObj.perm_n & permType.WRITE)
		}
	}
	function onClick(n) {
		if (n.id != "root") {
			if (n.attributes.type) {
				Ext.getCmp("wt-panel").initWTData(n.attributes.type,
						activeGroup.id, activeHierarchy.id)
			} else {
				Ext.getCmp("wt-panel").initWTData(n.attributes.n_uid,
						activeGroup.id, activeHierarchy.id)
			}
		} else {
			if (n.id == "root") {
				Ext.getCmp("wt-panel").clear();
				Ext.getCmp("wt-panel").disableTlb()
			}
		}
	}
	function keyboardDispacher(myEvent) {
		var that = Ext.getCmp("wt-panel");
		var o = {
			13 : onENTER,
			113 : onF2,
			46 : onDELETE
		};
		if (myEvent.keyCode in o) {
			o[myEvent.keyCode]()
		}
		function onENTER() {
			onClick(fileTP.selModel.getSelectedNode())
		}
		function onF2() {
			var node = fileTP.selModel.getSelectedNode();
			if ((node.attributes.perm_h || node.attributes.perm_n)
					& Jedox.studio.access.permType.WRITE) {
				onRenameFolder(node)
			} else {
				var title = "Warning".localize();
				var msg = "You have no permission for this operation"
						.localize();
				Jedox.studio.app.showMessageERROR(title, msg)
			}
		}
		function onDELETE() {
			var node = fileTP.selModel.getSelectedNode();
			if ((node.attributes.perm_h || node.attributes.perm_n)
					& Jedox.studio.access.permType.DELETE) {
				if (node.attributes.type == "root") {
					onDeleteHierarchy(node)
				} else {
					onDeleteFolder(node)
				}
			} else {
				var title = "Warning".localize();
				var msg = "You have no permission for this operation"
						.localize();
				Jedox.studio.app.showMessageERROR(title, msg)
			}
		}
	}
	fileTP
			.on(
					"click",
					function(n) {
						Ext.getCmp("wt-panel").activateFilesTab();
						var sn = fileTP.selModel.getSelectedNode() || {};
						if (n.id != sn.id && n.id != "root" || refreshFlag) {
							if (n.attributes.type) {
								Ext.getCmp("wt-panel")
										.initWTData(n.attributes.type,
												activeGroup.id,
												activeHierarchy.id,
												n.attributes.perm_n)
							} else {
								Ext.getCmp("wt-panel")
										.initWTData(n.attributes.n_uid,
												activeGroup.id,
												activeHierarchy.id,
												n.attributes.perm_n)
							}
							refreshFlag = false
						} else {
							if (n.id == "root") {
								Ext.getCmp("wt-panel").clear();
								Ext.getCmp("wt-panel").disableTlb()
							}
						}
						if (Jedox.studio.files.openRecentClb) {
							Jedox.studio.files.openRecentClb()
						}
						Jedox.studio.app.inputMode = Jedox.studio.app.inputMode_navigation_FILES
					});
	var mainPanel = new Ext.Panel( {
		id : "rt-main-panel",
		tbar : fTlb,
		border : false,
		autoScroll : true,
		items : [ fileTP ],
		listeners : {
			initTlb : function(p) {
				initTlb()
			}
		}
	});
	return mainPanel
};