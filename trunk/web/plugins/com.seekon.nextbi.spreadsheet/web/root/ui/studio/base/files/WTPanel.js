document.getElementsByClassName = function(cl) {
	var retnode = [];
	var myclass = new RegExp("\\b" + cl + "\\b");
	var elem = this.getElementsByTagName("*");
	for ( var i = 0; i < elem.length; i++) {
		var classes = elem[i].className;
		if (myclass.test(classes)) {
			retnode.push(elem[i])
		}
	}
	return retnode
};
Jedox.studio.files.WTPanel = function() {
	var that = this;
	var timer;
	this.dblClickFlag = false;
	this.newNodeFlag = false;
	this.fCtxDFlag = {
		open : false,
		openNT : false,
		cut : false,
		copy : false,
		rename : false,
		remove : false,
		properties : false,
		c_paste : false,
		c_new : false,
		c_properties : false
	};
	this.pasteAction = [];
	this.hyperlinkRegistar = {};
	this.staticRegistar = {};
	this.newHyperlinkFlag = false;
	this._currentTab = false;
	this._renameFlag = false;
	this._triggerSaveAsFromWSSFlag = false;
	this._ghn = null;
	this._beforeCloseTriggerFlag = false;
	this.wbStore = new Ext.data.SimpleStore(
			{
				fields : [ {
					name : "id",
					type : "string"
				}, {
					name : "text",
					type : "string"
				}, {
					name : "leaf"
				}, {
					name : "qtip",
					type : "string"
				}, {
					name : "iconCls",
					type : "string"
				}, {
					name : "img_src",
					type : "string"
				}, {
					name : "type",
					type : "string"
				}, {
					name : "perm_n"
				} ],
				sortInfo : {
					field : "iconCls",
					direction : "ASC"
				},
				listeners : {
					update : function(store, record, operation) {
						that.rename(record.get("id"), record.get("text"))
					},
					add : function(store, records, index) {
						if (that.copyRecord || that.cutRecord) {
						} else {
							if (!that.newHyperlinkFlag) {
								setTimeout(
										function() {
											var cmp = Ext.getCmp(
													"main-view-panel")
													.getLayout().activeItem;
											if (cmp.getXType() === "dataview") {
												cmp
														.select(cmp.store
																.getCount() - 1);
												var nodeEl = cmp
														.getSelectedNodes()[0];
												var cmpEl = cmp.getEl();
												Ext.fly(nodeEl).scrollIntoView(
														cmpEl);
												cmp.fireEvent("edit", cmp)
											} else {
												cmp.getSelectionModel()
														.selectLastRow();
												cmp.fireEvent("edit", cmp)
											}
										}, 0)
							} else {
								that.newHyperlinkFlag = false
							}
						}
					}
				}
			});
	var newBtn = {
		split : true,
		text : "New".localize(),
		iconCls : "new-icon",
		handler : function() {
			this.showMenu()
		},
		tooltip : {
			title : "New".localize(),
			text : "New...".localize().concat("...")
		},
		menu : {
			cls : "view-menu",
			autoWidth : true,
			items : [ {
				text : "New Workbook".localize(),
				tooltip : {
					title : "New Workbook".localize(),
					text : "Creates new workbook".localize()
				},
				iconCls : "new-spreadsheet-icon",
				handler : that.addWB,
				scope : this
			}, {
				text : "New Pivot".localize(),
				tooltip : {
					title : "New Pivot".localize(),
					text : "Creates new pivot grid".localize()
				},
				iconCls : "new-pivot-icon",
				handler : that.addURLPlugin,
				scope : this
			}, {
				text : "New Hyperlink".localize(),
				iconCls : "w3s_hyperlink",
				handler : function() {
					that.addHyperlink()
				},
				scope : that
			}, "-", {
				text : "New Folder".localize(),
				tooltip : {
					title : "New Folder".localize(),
					text : "Creates new folder".localize()
				},
				iconCls : "new-folder-icon",
				handler : that.addFolder,
				scope : this
			} ]
		}
	};
	var removeTlbBtn = {
		text : "Remove".localize(),
		tooltip : {
			title : "Remove".localize(),
			text : "Removes node from list".localize()
		},
		id : "delete",
		iconCls : "delete-icon",
		handler : this.remove,
		scope : this
	};
	var folderUpTlbBtn = {
		text : "Up".localize(),
		tooltip : {
			title : "Up".localize(),
			text : "Up one level".localize()
		},
		iconCls : "folder-up-icon",
		handler : this.up,
		scope : this
	};
	var viewBtn = {
		split : true,
		text : "View".localize(),
		tooltip : {
			title : "View".localize(),
			text : "View".localize().concat("...")
		},
		iconCls : "view-menu-icon",
		handler : this.setView.createDelegate(this, []),
		menu : {
			id : "view-menu",
			cls : "view-menu",
			width : 110,
			items : [ {
				text : "List".localize(),
				checked : true,
				group : "rp-view",
				checkHandler : this.setView,
				scope : this,
				iconCls : "view-list-icon"
			}, {
				text : "Thumbnails".localize(),
				checked : false,
				group : "rp-view",
				checkHandler : this.setView,
				scope : this,
				iconCls : "view-thumbnails-icon"
			}, {
				text : "Details".localize(),
				checked : false,
				group : "rp-view",
				checkHandler : this.setView,
				scope : this,
				iconCls : "view-details-icon"
			} ]
		}
	};
	var splitterTlbBtn = {
		xtype : "tbseparator"
	};
	var importBtn = {
		text : "Import file".localize(),
		tooltip : {
			title : "Import file".localize(),
			text : "Import .wss file to filesystem".localize()
		},
		iconCls : "import-file",
		handler : function() {
			Jedox.studio.files.importFile(that.wbStore, that.node)
		},
		scope : this
	};
	var DataViewClass = Ext.extend(Ext.DataView, {
		focusedClass : "x-view-focused",
		focusEl : true,
		getXType : function() {
			return "dataview"
		},
		afterRender : function() {
			DataViewClass.superclass.afterRender.call(this);
			var that = this;
			if (this.singleSelect || this.multiSelect) {
				if (this.focusEl === true) {
					this.focusEl = this.el.parent().parent().createChild( {
						tag : "a",
						href : "#",
						cls : "x-view-focus",
						tabIndex : "-1"
					});
					this.focusEl.insertBefore(this.el.parent());
					this.focusEl.swallowEvent("click", true);
					this.renderedFocusEl = true
				} else {
					if (this.focusEl) {
						this.focusEl = Ext.get(this.focusEl)
					}
				}
			}
		},
		onClick : function(e) {
			var item = e.getTarget(this.itemSelector, this.el);
			if (item) {
				var index = this.indexOf(item);
				if (this.onItemClick(item, index, e) !== false) {
					this.fireEvent("click", this, index, item, e);
					this.retainFocus()
				}
			} else {
				if (this.fireEvent("containerclick", this, e) !== false) {
					this.clearSelections();
					this.retainFocus()
				}
			}
		},
		retainFocus : function() {
			if (this.focusEl) {
				this.focusEl.focus()
			}
		},
		doRetainFocus : function() {
			this.focusEl.focus()
		}
	});
	var mainListView = new DataViewClass(
			{
				style : "overflow:auto;",
				id : "main-list-view",
				store : this.wbStore,
				tpl : new Ext.XTemplate(
						'<div class="main-list-view"><tpl for=".">',
						'<div class="thumb-wrap">',
						'<div class="thumb" style="padding: 0px; text-align: left;"><div style="width: 16px; height: 16px; display: inline;"><img class="{img_src}" src="../lib/ext/resources/images/default/s.gif" width="16" height="16"></div>',
						'<span class="x-editable">&nbsp;{text}</span></div></div>',
						"</tpl></div>"),
				autoWidth : true,
				multiSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				emptyText : "",
				plugins : [ new Ext.DataView.DragSelector(),
						new Ext.DataView.LabelEditor( {
							dataIndex : "text"
						}) ],
				listeners : {
					dblclick : function(dataView, index, node, e) {
						that.onDblClick(dataView.store.getAt(index).get("id"),
								dataView.store.getAt(index).get("iconCls"))
					},
					click : function(dataView, index, node, e) {
						that.selectedIndex = index;
						that.selectedNode = node;
						that.selection = this.store.getAt(index).get("id");
						that.activeRecord = this.store.getAt(index);
						that.setInterfacePermission(this.store.getAt(index)
								.get("perm_n"));
						that.setInputMode()
					},
					contextmenu : function(dataView, index, node, e) {
						var t = e.target;
						if (t.className == "thumb") {
							dataView.clearSelections();
							that.onContainerContextMenu(e)
						} else {
							var args = {
								cmp : dataView,
								index : index,
								node : node,
								e : e
							};
							this.fireEvent("click", this, index, node, e);
							that.onContextMenu(args)
						}
						that.setInputMode()
					},
					edit : function(dataView) {
						var node = dataView.getSelectedNodes();
						var record = dataView.getRecords(node);
						var index = dataView.getSelectedIndexes();
						var target = document
								.getElementsByClassName("x-editable");
						dataView.plugins[1].activeRecord = record[0];
						dataView.plugins[1].startEdit(target[index],
								record[0].data.text)
					},
					containerclick : function(dataView, e) {
						if (dataView.getSelectionCount() == 0) {
							that.setNoSelectionInterface()
						}
						that.setInputMode()
					},
					selectionchange : function(dataView, selections) {
						if (selections.length == 0) {
							that.setNoSelectionInterface()
						} else {
							that.setHasSelectionInterface()
						}
					}
				}
			});
	var mainDetailsView = new Ext.grid.EditorGridPanel( {
		id : "main-details-view",
		colModel : new Ext.grid.ColumnModel( [ {
			id : "folder-name",
			header : "Name",
			width : 30,
			sortable : true,
			dataIndex : "text",
			renderer : this.formatTitle,
			editor : new Ext.form.TextField( {
				allowBlank : false
			})
		}, {
			header : "Size".localize(),
			width : 20,
			sortable : true,
			dataIndex : ""
		}, {
			header : "Type".localize(),
			width : 20,
			sortable : true,
			dataIndex : "type"
		}, {
			header : "Last Modified".localize(),
			width : 20,
			sortable : true,
			dataIndex : ""
		} ]),
		store : this.wbStore,
		viewConfig : {
			forceFit : true,
			sortAscText : "Sort Ascending".localize(),
			sortDescText : "Sort Descending".localize(),
			columnsText : "Columns".localize(),
			groupByText : "Group by".localize()
		},
		sm : new Ext.grid.RowSelectionModel( {}),
		autoScroll : true,
		border : false,
		listeners : {
			rowdblclick : function(gridView, index, e) {
				that.onDblClick(gridView.store.getAt(index).get("id"),
						gridView.store.getAt(index).get("iconCls"))
			},
			rowclick : function(gridView, index, e) {
				that.selection = gridView.store.getAt(index).get("id");
				that.selectedIndex = index;
				var record = gridView.store.getAt(index);
				gridView.getColumnModel().setEditable(0, false);
				that.activeRecord = record;
				that.setInterfacePermission(this.store.getAt(index).get(
						"perm_n"));
				that.setInputMode()
			},
			rowselect : function(gridView, index) {
				gridView.getSelectionModel().selectRow(index)
			},
			rowcontextmenu : function(gridView, index, e) {
				var args = {
					cmp : gridView,
					index : index,
					e : e
				};
				this.fireEvent("rowclick", this, index, e);
				e.stopEvent();
				clearTimeout(timer);
				timer = setTimeout(function() {
					that.onContextMenu(args, true)
				}, 0);
				that.setInputMode()
			},
			edit : function(gridView) {
				gridView.getColumnModel().setEditable(0, true);
				var record = gridView.getSelectionModel().getSelected();
				var index = gridView.store.indexOf(record);
				gridView.startEditing(index, 0)
			},
			contextmenu : function(e) {
				e.stopEvent();
				var gridView = this;
				timer = setTimeout(function() {
					gridView.getSelectionModel().clearSelections();
					that.onContainerContextMenu(e, true)
				}, 0);
				that.setInputMode()
			},
			validateedit : function(o) {
				o.value = that.trim(o.value);
				var _ret = that.validateName(o.value,
						that.activeRecord.data.iconCls);
				return _ret
			},
			cellcontextmenu : function(gridView, rowIndex, cellIndex, e) {
				if (cellIndex > 0) {
					gridView.getSelectionModel().clearSelections();
					clearTimeout(timer);
					timer = setTimeout(function() {
						that.onContainerContextMenu(e)
					}, 0)
				}
			},
			keydown : function(e) {
				return false
			}
		}
	});
	var mainThumbnailsView = new DataViewClass(
			{
				id : "main-thumbnails-view",
				style : "overflow:auto;",
				store : this.wbStore,
				tpl : new Ext.XTemplate(
						'<tpl for=".">',
						'<div class="thumb-wrap" id="{text}">',
						'<div class="thumb"><div class="{img_src}32"><img src="../lib/ext/resources/images/default/s.gif" title="{text}"></div></div>',
						'<span class="x-editable">{shortName}</span></div>',
						"</tpl>", '<div class="x-clear"></div>'),
				autoWidth : true,
				multiSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				emptyText : "",
				plugins : [ new Ext.DataView.DragSelector(),
						new Ext.DataView.LabelEditor( {
							dataIndex : "text"
						}) ],
				prepareData : function(data) {
					data.shortName = Ext.util.Format.ellipsis(data.text, 11);
					return data
				},
				listeners : {
					dblclick : function(dataView, index, node, e) {
						that.onDblClick(dataView.store.getAt(index).get("id"),
								dataView.store.getAt(index).get("iconCls"))
					},
					click : function(dataView, index, node, e) {
						that.selectedIndex = index;
						that.selection = dataView.store.getAt(index).get("id");
						that.activeRecord = dataView.store.getAt(index);
						that.setInputMode();
						that.setInterfacePermission(this.store.getAt(index)
								.get("perm_n"))
					},
					contextmenu : function(dataView, index, node, e) {
						var args = {
							cmp : dataView,
							index : index,
							node : node,
							e : e
						};
						this.fireEvent("click", this, index, node, e);
						that.onContextMenu(args);
						that.setInputMode()
					},
					edit : function(dataView) {
						var node = dataView.getSelectedNodes();
						var record = dataView.getRecords(node);
						var index = dataView.getSelectedIndexes();
						var target = document
								.getElementsByClassName("x-editable");
						var step = target.length / 2;
						dataView.plugins[1].activeRecord = record[0];
						dataView.plugins[1].startEdit(target[step + index[0]],
								record[0].data.text)
					},
					containerclick : function(dataView, e) {
						if (dataView.getSelectionCount() == 0) {
							that.setNoSelectionInterface()
						}
						dataView.plugins[1].cancelEdit();
						that.setInputMode()
					},
					selectionchange : function(dataView, selections) {
						if (selections.length == 0) {
							that.setNoSelectionInterface()
						} else {
							that.setHasSelectionInterface()
						}
					}
				}
			});
	this.fTlb = new Ext.Toolbar( {
		items : [ newBtn, removeTlbBtn, splitterTlbBtn, importBtn,
				splitterTlbBtn, folderUpTlbBtn, splitterTlbBtn, viewBtn ],
		disabled : true
	});
	var mainPanel = new Ext.Panel( {
		id : "main-view-panel",
		layout : "card",
		autoScroll : true,
		border : false,
		tbar : this.fTlb,
		items : [ mainListView, mainDetailsView, mainThumbnailsView ],
		activeItem : 0
	});
	this.wssPanel = new Ext.Panel( {
		border : false,
		layout : "fit"
	});
	this.hyperlinkPanel = new Ext.Panel( {
		border : false,
		layout : "card"
	});
	this.staticPanel = new Ext.Panel( {
		border : false,
		layout : "card"
	});
	this.filesWSSPanel = new Ext.Panel( {
		layout : "card",
		autoScroll : true,
		border : false,
		items : [ mainPanel, this.wssPanel, this.hyperlinkPanel,
				this.staticPanel ],
		activeItem : 0
	});
	this.filesNavigationTabPanel = new Ext.TabPanel(
			{
				id : "filesNavigationTabPanel1",
				border : false,
				bodyBorder : false,
				resizeTabs : true,
				tabWidth : 150,
				minTabWidth : 120,
				enableTabScroll : true,
				items : [ {
					id : "files-tab",
					title : "Files".localize(),
					listeners : {
						activate : function(panel) {
							if (that.filesWSSPanel.getLayout().activeItem) {
								that.filesWSSPanel.getLayout().setActiveItem(0)
							}
						}
					}
				} ],
				activeTab : 0,
				listeners : {
					beforetabchange : function(tabPanel, newTab, currentTab) {
						if (newTab && currentTab && newTab.id == currentTab.id) {
							return
						}
						var files = Jedox.studio.frames.files;
						if (files
								&& files.Jedox.wss.app.environment
								&& (files.Jedox.wss.app.environment.inputMode == files.Jedox.wss.grid.GridMode.DIALOG || files.Jedox.wss.general.switchSuspendModeAlert)) {
							if (currentTab.type == "wss") {
								if (!that._currentTab) {
									that._currentTab = currentTab
								}
								if (newTab.type == "wss") {
									Jedox.studio.app
											.showMessageERROR(
													"Warrning",
													files.Jedox.wss.general.switchSuspendModeAlert ? "wb_in_suspend_mode"
															.localize()
															: "wb_in_dlg_mode"
																	.localize());
									return false
								} else {
									return true
								}
							} else {
								if (newTab.type == "wss") {
									if (newTab == that._currentTab) {
										return true
									} else {
										setTimeout(function() {
											tabPanel
													.setActiveTab(that._currentTab.id);
											that.filesWSSPanel.getLayout()
													.setActiveItem(1);
											Jedox.studio.app
													.showMessageERROR(
															"Warrning",
															"wb_in_dlg_mode"
																	.localize())
										});
										return false
									}
								} else {
									return true
								}
							}
						} else {
							that._currentTab = false;
							if ((newTab.id == "files-tab" && Jedox.studio.app.resourcesRefreshFlag)
									&& !that._triggerSaveAsFromWSSFlag) {
								Ext.getCmp("ps-wt-tree").fireEvent("onRefresh",
										that._ghn.g, that._ghn.h, that._ghn.n);
								Jedox.studio.app.resourcesRefreshFlag = false;
								that._ghn = null
							}
						}
					},
					beforeremove : function(tabPanel, cmp) {
						var files = Jedox.studio.frames.files;
						if (files
								&& files.Jedox.wss.app.environment
								&& (files.Jedox.wss.app.environment.inputMode == files.Jedox.wss.grid.GridMode.DIALOG || files.Jedox.wss.general.switchSuspendModeAlert)) {
							Jedox.studio.app
									.showMessageERROR(
											"Warrning",
											files.Jedox.wss.general.switchSuspendModeAlert ? "wb_in_suspend_mode"
													.localize()
													: "wb_in_dlg_mode"
															.localize());
							return false
						}
					}
				}
			});
	var previewPanel = new Ext.Panel( {
		layout : "table",
		layoutConfig : {
			columns : 1
		},
		borders : false,
		bodyBorder : false,
		items : [ {
			id : "files-navigation-tab-panel",
			border : false,
			height : 26,
			width : 200,
			rowspan : 1,
			layout : "fit",
			items : that.filesNavigationTabPanel
		}, {
			id : "files-content-panel",
			border : false,
			items : that.filesWSSPanel,
			layout : "fit",
			rowspan : 2
		} ],
		listeners : {
			resize : function(panel, adjWidth, adjHeight, rawWidth, rawHeight) {
				Ext.getCmp("files-navigation-tab-panel").setWidth(adjWidth);
				Ext.getCmp("files-content-panel").setHeight(adjHeight - 26);
				Ext.getCmp("files-content-panel").setWidth(adjWidth)
			}
		}
	});
	Jedox.studio.files.WTPanel.superclass.constructor.call(this, {
		id : "wt-panel",
		title : "File Manager".localize(),
		layout : "fit",
		items : previewPanel,
		listeners : {
			render : function(p) {
				this.getEl().unselectable()
			}
		}
	})
};
Ext
		.extend(
				Jedox.studio.files.WTPanel,
				Ext.Panel,
				{
					initComponent : function() {
						var that = this;
						Jedox.studio.files.WTPanel.superclass.initComponent
								.call(this);
						this.renameHandleFnc = function(newName) {
							if (that.validateName(newName,
									that.activeRecord.data.iconCls)) {
								that.activeRecord.set("text", newName)
							}
						};
						this.refreshHandleFnc = function() {
							Ext.getCmp("ps-wt-tree").fireEvent(
									"reloadActiveHierarchy")
						};
						this.importHandleFnc = function() {
							setTimeout(function() {
								that.initWTData(that.node, that.activeGroup,
										that.activeHierarchy, that._parentPerm)
							}, 200)
						}
					},
					setView : function(m, pressed) {
						var that = this;
						if (!m) {
							var viewMenu = Ext.menu.MenuMgr.get("view-menu");
							viewMenu.render();
							var items = viewMenu.items.items;
							var b = items[0], r = items[1], h = items[2];
							if (b.checked) {
								r.setChecked(true)
							} else {
								if (r.checked) {
									h.setChecked(true)
								} else {
									if (h.checked) {
										b.setChecked(true)
									}
								}
							}
							return
						}
						if (pressed) {
							var mvp = Ext.getCmp("main-view-panel");
							switch (m.text) {
							case "List".localize():
								mvp.getLayout().setActiveItem("main-list-view");
								mvp.ownerCt.doLayout();
								break;
							case "Thumbnails".localize():
								mvp.getLayout().setActiveItem(
										"main-thumbnails-view");
								mvp.ownerCt.doLayout();
								break;
							case "Details".localize():
								mvp.getLayout().setActiveItem(
										"main-details-view");
								mvp.ownerCt.doLayout();
								break
							}
						}
						this.setInputMode();
						this.clearAllSelections()
					},
					initWTData : function(node, group, hierarchy, parentPerm) {
						var that = this;
						this.node = node;
						this.selection = null;
						if (this.activeGroup != group
								|| this.activeHierarchy != hierarchy) {
							this.copyRecords = null;
							this.cutRecords = null
						}
						this.activeGroup = group;
						this.activeHierarchy = hierarchy;
						this._parentPerm = parentPerm;
						var wssStudioHandler = {
							treeMngNode : function(result) {
								if (!result) {
									Jedox.studio.app.showMessageERROR(
											"Database read error".localize(),
											"read_data_err".localize());
									return false
								}
								var wbData = [];
								var type;
								for ( var i = 0; i < result.length; i++) {
									wbData.push( [ result[i].id,
											result[i].text, result[i].leaf,
											result[i].qtip, result[i].iconCls,
											that.img_src(result[i].iconCls),
											that.getType(result[i].iconCls),
											result[i].perm_n ])
								}
								that.wbStore.loadData(wbData);
								that.enableTlb();
								that
										.setContainerInterfacePermission(parentPerm);
								that.setUpTlbBtnState(node != "root")
							}
						};
						var wssStudioStub = new Studio(wssStudioHandler);
						wssStudioStub.treeMngNode("file", this.node, "dump", 0)
					},
					clear : function() {
						this.wbStore.removeAll()
					},
					img_src : function(iconCls) {
						return iconCls
					},
					getType : function(iconCls) {
						var _return;
						switch (iconCls) {
						case "w3s_folder":
							_return = "File Folder".localize();
							break;
						case "w3s_workbook":
							_return = "Workbook".localize();
							break;
						case "w3s_hyperlink":
							_return = "Hyperlink".localize();
							break;
						case "w3s_csv":
							_return = "Microsoft Office Excel Comma Separated Values File"
									.localize();
							break;
						case "w3s_doc":
							_return = "Microsoft Office Word 97 - 2003"
									.localize();
							break;
						case "w3s_pdf":
							_return = "Adobe Acrobat Document".localize();
							break;
						case "w3s_gif":
							_return = "GIF Image".localize();
							break;
						case "w3s_html":
							_return = "Firefox Document".localize();
							break;
						case "w3s_jpg":
							_return = "JPEG Image".localize();
							break;
						case "w3s_png":
							_return = "PNG Image".localize();
							break;
						case "w3s_pps":
							_return = "Microsoft Office PowerPoint 97 - 2003 Show"
									.localize();
							break;
						case "w3s_ppt":
							_return = "Microsoft Office PowerPoint 97 - 2003 Presentation"
									.localize();
							break;
						case "w3s_rar":
							_return = "WinZip File".localize();
							break;
						case "w3s_rtf":
							_return = "Rich Text Format".localize();
							break;
						case "w3s_txt":
							_return = "Text Document".localize();
							break;
						case "w3s_xls":
							_return = "Microsoft Office Excel 97 - 2003 Workbook"
									.localize();
							break;
						case "w3s_xlsx":
							_return = "Microsoft Office Excel Workbook"
									.localize();
							break;
						case "w3s_zip":
							_return = "WinZip File".localize();
							break;
						case "w3s_unknown":
							_return = "Unknown File format".localize();
							break;
						case "w3s_ahview":
							_return = "Palo Pivot".localize();
							break;
						default:
							_return = "Unknown File format".localize();
							break
						}
						return _return
					},
					formatTitle : function(value, p, record) {
						return String
								.format(
										'<div class="topic"><img class="{0}" src="../lib/ext/resources/images/default/s.gif" width="16" height="16" />&nbsp;&nbsp;{1}</div>',
										record.data.img_src, value)
					},
					onDblClick : function(id, iconCls) {
						switch (iconCls) {
						case "w3s_folder":
							Ext.getCmp("ps-wt-tree").fireEvent("select", id);
							break;
						case "w3s_workbook":
							this.openWB(id);
							break;
						case "w3s_hyperlink":
						case "w3s_ahview":
							this.openHyperlink(id);
							break;
						default:
							this.openStatic(id);
							break
						}
					},
					addFolder : function() {
						var that = this;
						var iconCls = "w3s_folder";
						var text = this.getNewItemName("New Folder".localize(),
								iconCls);
						var leaf = false;
						var qtip = "";
						var type = iconCls.split("_")[1];
						var img_src = this.img_src(iconCls);
						var perm_n = this._parentPerm;
						var wssStudioHandler = {
							treeMngNode : function(result) {
								if (!result) {
									Jedox.studio.app.showMessageERROR(
											"Database write error".localize(),
											"write_data_err".localize());
									return false
								}
								var newRecord = new Ext.data.Record( {
									id : result,
									text : text,
									leaf : leaf,
									qtip : qtip,
									iconCls : iconCls,
									img_src : img_src,
									perm_n : perm_n
								});
								that.wbStore.add(newRecord);
								that.newNodeFlag = true;
								that.activeRecord = newRecord;
								that.newRecord = newRecord;
								that.selection = result;
								var newNode = {
									id : result,
									text : text,
									leaf : leaf,
									qtip : qtip,
									iconCls : iconCls,
									perm_n : perm_n
								};
								Ext.getCmp("ps-wt-tree").fireEvent("addNode",
										that.node, newNode, perm_n)
							}
						};
						var wssStudioStub = new Studio(wssStudioHandler);
						wssStudioStub.treeMngNode("file", this.node, "addNode",
								leaf, type, {
									name : text,
									desc : qtip
								});
						this.setInputMode()
					},
					addWB : function() {
						var that = this;
						var iconCls = "w3s_workbook";
						var text = this.getNewItemName("New Workbook"
								.localize(), iconCls);
						var leaf = true;
						var qtip = "";
						var type = iconCls.split("_")[1];
						var img_src = this.img_src(iconCls);
						var perm_n = this._parentPerm;
						var wssStudioHandler = {
							treeMngNode : function(result) {
								if (!result) {
									Jedox.studio.app.showMessageERROR(
											"Database write error".localize(),
											"write_data_err".localize());
									return false
								}
								var newRecord = new Ext.data.Record( {
									id : result,
									text : text,
									leaf : leaf,
									qtip : qtip,
									iconCls : iconCls,
									img_src : img_src,
									perm_n : perm_n
								});
								that.wbStore.add(newRecord);
								that.newNodeFlag = true;
								that.activeRecord = that.newRecord = newRecord;
								that.selection = result;
								that.setInputMode()
							}
						};
						var wssStudioStub = new Studio(wssStudioHandler);
						wssStudioStub.treeMngNode("file", this.node, "addNode",
								leaf, type, {
									name : text,
									desc : qtip
								});
						this.setInputMode()
					},
					addHyperlink : function() {
						var that = this;
						function fn(name, desc, url, target) {
							var text = name;
							var leaf = true;
							var qtip = desc;
							var iconCls = "w3s_hyperlink";
							var type = iconCls.split("_")[1];
							var img_src = that.img_src(iconCls);
							var perm_n = that._parentPerm;
							for ( var i = 0, items = that.wbStore.data.items, count = items.length; i < count; i++) {
								var item = items[i].data;
								var newFileName = text;
								if (newFileName == item.text
										&& item.type == "Hyperlink".localize()) {
									Jedox.studio.app.showMessageERROR(
											"Hyperlink name error".localize(),
											"add_hpl_err_file_exists"
													.localize( {
														new_name : newFileName
													}));
									return false
								}
							}
							var wssStudioHandler = {
								treeMngNode : function(result) {
									if (!result) {
										Jedox.studio.app.showMessageERROR(
												"Database write error"
														.localize(),
												"write_data_err".localize());
										return false
									}
									var newRecord = new Ext.data.Record( {
										id : result,
										text : name,
										leaf : leaf,
										qtip : qtip,
										iconCls : iconCls,
										img_src : img_src,
										type : "Hyperlink".localize(),
										perm_n : perm_n
									});
									that.newHyperlinkFlag = true;
									that.wbStore.add(newRecord);
									that.newNodeFlag = true;
									that.activeRecord = newRecord;
									that.newRecord = newRecord;
									that.selection = result;
									that.setInputMode()
								}
							};
							var wssStudioStub = new Studio(wssStudioHandler);
							wssStudioStub.treeMngNode("file", that.node,
									"addNode", leaf, type, {
										name : name,
										desc : qtip,
										hyperlink : {
											type : "url",
											target : target,
											url : url
										}
									});
							return true
						}
						Jedox.studio.files.openAddHyperlink(fn)
					},
					addURLPlugin : function() {
						var that = this;
						var iconCls = "w3s_ahview";
						var text = this.getNewItemName("New Palo Pivot"
								.localize(), iconCls);
						var leaf = true;
						var qtip = "";
						var type = "urlplugin";
						var subtype = iconCls.split("_")[1];
						var img_src = that.img_src(iconCls);
						var perm_n = this._parentPerm;
						var wssStudioHandler = {
							treeMngNode : function(result) {
								if (!result) {
									Jedox.studio.app.showMessageERROR(
											"Database write error".localize(),
											"write_data_err".localize());
									return false
								}
								var newRecord = new Ext.data.Record( {
									id : result,
									text : text,
									leaf : leaf,
									qtip : qtip,
									iconCls : iconCls,
									img_src : img_src,
									type : that.getType(iconCls),
									perm_n : perm_n
								});
								that.wbStore.add(newRecord);
								that.newNodeFlag = true;
								that.activeRecord = that.newRecord = newRecord;
								that.selection = result;
								that.setInputMode()
							}
						};
						var wssStudioStub = new Studio(wssStudioHandler);
						wssStudioStub.treeMngNode("file", that.node, "addNode",
								leaf, type, {
									name : text,
									desc : qtip,
									type : type,
									subtype : subtype,
									params : {
										hideToolbar : 0,
										hideSave : 0,
										hideFilter : 0,
										hideStaticFilter : 0,
										hideHorizontalAxis : 0,
										hideVerticalAxis : 0,
										hideConnectionPicker : 1
									}
								})
					},
					getNewItemName : function(tmplName, iconCls) {
						var br = 1;
						var exist = true;
						var name = tmplName.toLowerCase();
						while (exist) {
							exist = false;
							if (br > 1) {
								name = tmplName.toLowerCase() + " (" + br + ")"
							}
							this.wbStore
									.each(
											function(record) {
												if (record.data.text
														.toLowerCase() == name
														&& (iconCls && iconCls == record.data.iconCls)) {
													br = br + 1;
													exist = true;
													return false
												}
											}, [ this ])
						}
						return br > 1 ? tmplName + " (" + br + ")" : tmplName
					},
					getNewItemCopyName : function(tmplName, iconCls) {
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
								name = ("Copy".localize() + " (" + br + ") "
										+ "of ".localize() + tmplName)
										.toLowerCase()
							}
							this.wbStore
									.each(
											function(record) {
												if (record.data.text
														.toLowerCase() == name
														&& (iconCls && iconCls == record.data.iconCls)) {
													br = br + 1;
													exist = true;
													return false
												}
											}, [ this ])
						}
						return (br == 1) ? ("Copy".localize()
								+ " of ".localize() + tmplName)
								: ((br > 0) ? ("Copy".localize() + " (" + br
										+ ") " + "of ".localize() + tmplName)
										: (tmplName))
					},
					getSelectedRecords : function() {
						var cmp = Ext.getCmp("main-view-panel").getLayout().activeItem;
						var selectedRecords;
						if (cmp.getXType() === "dataview") {
							var nodes = cmp.getSelectedNodes();
							selectedRecords = cmp.getRecords(nodes)
						} else {
							selectedRecords = cmp.getSelectionModel()
									.getSelections()
						}
						return selectedRecords
					},
					getSelectedIndex : function() {
						var cmp = Ext.getCmp("main-view-panel").getLayout().activeItem;
						var selectedIndex;
						if (cmp.getXType() === "dataview") {
							var selections = cmp.getSelectedIndexes();
							if (selections) {
								selectedIndex = selections[0]
							}
						} else {
							selectedIndex = cmp.getSelectionModel()
									.getSelections()
						}
						return selectedIndex
					},
					getRecordByName : function(name) {
						var r;
						this.wbStore.each(function(record) {
							if (record.data.text.toLowerCase() == name
									.toLowerCase()) {
								r = record;
								return false
							}
						}, [ this ]);
						return r
					},
					getRecordById : function(id) {
						var r;
						this.wbStore.each(function(record) {
							if (record.data.id == id) {
								r = record;
								return false
							}
						}, [ this ]);
						return r
					},
					remove : function() {
						this.setInputMode();
						var that = this;
						var title = "Remove Item".localize();
						var cmp = Ext.getCmp("main-view-panel").getLayout().activeItem;
						if (cmp.getXType() === "dataview") {
							var nodes = cmp.getSelectedNodes();
							var removeRecords = cmp.getRecords(nodes)
						} else {
							var removeRecords = cmp.getSelectionModel()
									.getSelections()
						}
						if (removeRecords.length) {
							if (removeRecords.length > 1) {
								var msg = "remove_items_warning_msg".localize( {
									nmb : removeRecords.length
								});
								var rnodes = [];
								for ( var i = 0; i < removeRecords.length; i++) {
									rnodes.push(removeRecords[i].data.id)
								}
								var fn = function() {
									var removeAction = [];
									for ( var i = 0; i < removeRecords.length; i++) {
										removeAction.push( [ that.node,
												"removeNode",
												removeRecords[i].data.id ])
									}
									Jedox.studio.backend.wssStudio.treeMngNode(
											"file", removeAction);
									for ( var i = 0; i < removeRecords.length; i++) {
										Ext.getCmp("ps-wt-tree").fireEvent(
												"removeNode",
												removeRecords[i].data.id)
									}
									that.initWTData(that.node,
											that.activeGroup,
											that.activeHierarchy,
											that._parentPerm);
									Jedox.studio.app.showTopMsg("",
											"Items removed successefully"
													.localize());
									Jedox.studio.app.reportsRefreshFlag = true
								};
								Jedox.studio.app.showMessageR_QUESTION_YND( {
									nodes : rnodes,
									container : true
								}, title, msg, fn)
							} else {
								var selectionID = removeRecords[0].data.id;
								var msg = "remove_item_warning_msg".localize( {
									name : removeRecords[0].data.text
								});
								var fn = function() {
									Jedox.studio.backend.wssStudio.treeMngNode(
											"file", that.node, "removeNode",
											selectionID);
									Ext.getCmp("ps-wt-tree").fireEvent(
											"removeNode", selectionID);
									that.initWTData(that.node,
											that.activeGroup,
											that.activeHierarchy,
											that._parentPerm);
									Jedox.studio.app.showTopMsg("",
											"Item removed successefully"
													.localize());
									Jedox.studio.app.reportsRefreshFlag = true
								};
								Jedox.studio.app.showMessageR_QUESTION_YND( {
									nodes : [ selectionID ],
									container : !removeRecords[0].data.leaf
								}, title, msg, fn)
							}
						} else {
							var msg = "You did't select an item. Select an Item and try again."
									.localize();
							Jedox.studio.app.showMessageERROR(title, msg)
						}
					},
					removeRecord : function(r) {
						var id = r.data.id;
						this.wbStore.remove(r);
						Ext.getCmp("ps-wt-tree").fireEvent("removeNode", id)
					},
					rename : function(id, newName) {
						Jedox.studio.backend.wssStudio.treeMngNode("file",
								this.node, "renameNode", id, newName);
						Ext.getCmp("ps-wt-tree").fireEvent("renameNode", id,
								newName)
					},
					findById : function(id) {
						var index = this.wbStore.find("id", id);
						return this.wbStore.getAt(index).get("text")
					},
					getTypeById : function(id) {
						var index = this.wbStore.find("id", id);
						return this.wbStore.getAt(index).get("iconCls")
								.replace("w3s_", "")
					},
					isURLPlugin : function(id) {
						var index = this.wbStore.find("id", id);
						return this.wbStore.getAt(index).get("iconCls") == "w3s_ahview" ? true
								: false
					},
					findByText : function(text) {
						var index = this.wbStore.find("text", text);
						return this.wbStore.getAt(index).get("id")
					},
					up : function() {
						Ext.getCmp("ps-wt-tree").fireEvent("up", this.node);
						this.setInputMode()
					},
					onContainerContextMenu : function(e, ieHack) {
						var that = this;
						this.setNoSelectionInterface();
						if (this.fTlb.disabled) {
							return
						}
						var menu = new Ext.menu.Menu(
								{
									id : "f-gc-ctx",
									enableScrolling : false,
									autoWidth : true,
									listeners : {
										hide : function(menu) {
											menu.destroy()
										}
									},
									items : [
											{
												text : "Paste".localize(),
												disabled : that.fCtxDFlag.c_paste
														|| !(this.copyRecords || this.cutRecords),
												iconCls : "paste-icon",
												handler : function() {
													that.onPasteNEW()
												},
												scope : that
											},
											"-",
											{
												text : "New".localize(),
												iconCls : "new-icon",
												disabled : that.fCtxDFlag.c_new,
												menu : {
													cls : "view-menu",
													autoWidth : true,
													items : [
															{
																text : "New Workbook"
																		.localize(),
																iconCls : "new-spreadsheet-icon",
																handler : that.addWB,
																scope : that
															},
															{
																text : "New Pivot"
																		.localize(),
																tooltip : {
																	title : "Palo Pivot"
																			.localize(),
																	text : "Creates new pivot grid"
																			.localize()
																},
																iconCls : "new-pivot-icon",
																handler : that.addURLPlugin,
																scope : that
															},
															{
																text : "New Hyperlink"
																		.localize(),
																iconCls : "w3s_hyperlink",
																handler : function() {
																	that
																			.addHyperlink()
																},
																scope : that
															},
															"-",
															{
																text : "Folder"
																		.localize(),
																tooltip : {
																	title : "New Folder"
																			.localize(),
																	text : "Creates new folder"
																			.localize()
																},
																iconCls : "new-folder-icon",
																handler : that.addFolder,
																scope : that
															} ]
												}
											},
											"-",
											{
												iconCls : "import-file",
												text : "Import file".localize(),
												disabled : that.fCtxDFlag.c_new,
												handler : function() {
													Jedox.studio.files
															.importFile(
																	that.wbStore,
																	that.node)
												},
												scope : that
											},
											"-",
											{
												text : "Properties".localize(),
												iconCls : "properties-icon",
												disabled : that.fCtxDFlag.c_properties,
												handler : function() {
													that.onProperties(
															that.node, true)
												},
												scope : that
											} ]
								});
						menu.showAt(e.getXY())
					},
					onContextMenu : function(args, ieHack) {
						var openInNewTabFlag = args.cmp.store.getAt(args.index)
								.get("iconCls") === "w3s_folder" ? true : false;
						var that = this;
						var menu = new Ext.menu.Menu(
								{
									id : "f-ctx",
									enableScrolling : false,
									listeners : {
										hide : function(menu) {
											menu.destroy()
										}
									},
									items : [
											{
												text : "Open".localize(),
												disabled : that.fCtxDFlag.open,
												iconCls : "open-folder-icon",
												handler : function() {
													that
															.onDblClick(
																	that.selection,
																	args.cmp.store
																			.getAt(
																					args.index)
																			.get(
																					"iconCls"))
												},
												scope : that
											},
											{
												text : "Open in New Window"
														.localize(),
												iconCls : "new-tab-icon",
												disabled : that.fCtxDFlag.openNT
														|| openInNewTabFlag,
												handler : function() {
													that
															.openInNewWindow(
																	{
																		g : that.activeGroup,
																		h : that.activeHierarchy,
																		n : that.selection,
																		t : args.cmp.store
																				.getAt(
																						args.index)
																				.get(
																						"img_src")
																				.replace(
																						"w3s_",
																						"")
																	},
																	args.cmp.store
																			.getAt(
																					args.index)
																			.get(
																					"text"))
												},
												scope : that
											},
											"-",
											{
												text : "Cut".localize(),
												iconCls : "cut-icon",
												disabled : that.fCtxDFlag.cut,
												handler : this.onCut,
												scope : that
											},
											{
												text : "Copy".localize(),
												iconCls : "copy-icon",
												disabled : that.fCtxDFlag.copy,
												handler : this.onCopy,
												scope : that
											},
											"-",
											{
												text : "Rename".localize(),
												iconCls : "rename-icon",
												disabled : that.fCtxDFlag.rename,
												handler : function() {
													var cmp = Ext.getCmp(
															"main-view-panel")
															.getLayout().activeItem;
													cmp.fireEvent("edit", cmp);
													that._renameFlag = true
												},
												scope : that
											},
											{
												text : "Remove".localize(),
												iconCls : "delete-icon",
												disabled : that.fCtxDFlag.remove,
												handler : function() {
													that.remove()
												},
												scope : that
											},
											"-",
											{
												text : "Mark".localize()
														.concat("..."),
												iconCls : "mark-workbook-icon",
												disabled : args.cmp.store
														.getAt(args.index).get(
																"iconCls") === "w3s_workbook" ? false
														: true,
												handler : function() {
													function cb(path) {
														that
																.addMarker( {
																	g : that.activeGroup,
																	h : that.activeHierarchy,
																	n : that.selection,
																	path : path
																			.concat(args.cmp.store
																					.getAt(
																							args.index)
																					.get(
																							"text"))
																})
													}
													Ext
															.getCmp(
																	"ps-wt-tree")
															.fireEvent(
																	"onGetPath",
																	that.node,
																	cb)
												},
												scope : this
											},
											"-",
											{
												text : "Export...".localize(),
												iconCls : "export-icon",
												disabled : that.fCtxDFlag.properties,
												handler : function() {
													Jedox.studio.files
															.exportFile(
																	{
																		g : that.activeGroup,
																		h : that.activeHierarchy,
																		n : that.selection
																	}, "file")
												},
												scope : this
											},
											"-",
											{
												text : "Properties".localize(),
												iconCls : "properties-icon",
												disabled : that.fCtxDFlag.properties,
												handler : function() {
													that.onProperties(
															that.selection,
															false)
												},
												scope : this
											} ]
								});
						if (!ieHack) {
							args.e.stopEvent()
						}
						menu.showAt(args.e.getXY());
						var cmp = Ext.getCmp("main-view-panel").getLayout().activeItem;
						if (cmp.getXType() === "dataview") {
							var nodes = cmp.getSelectedNodes();
							var selectedRecords = cmp.getRecords(nodes);
							if (selectedRecords.length == 0
									|| !this.checkIsSelected(selectedRecords,
											args.cmp.store.getAt(args.index))) {
								args.cmp.select(args.node);
								args.cmp.fireEvent("click", args.cmp,
										args.index, args.node, args.e)
							}
						} else {
							var selectedRecords = cmp.getSelectionModel()
									.getSelections();
							if (selectedRecords.length == 0
									|| !this.checkIsSelected(selectedRecords,
											args.cmp.store.getAt(args.index))) {
								args.cmp.fireEvent("rowselect", args.cmp,
										args.index)
							}
						}
						if (selectedRecords.length > 1
								&& this.checkIsSelected(selectedRecords,
										args.cmp.store.getAt(args.index))) {
							var menuItems = [ 0, 1, 6, 9, 11, 13 ];
							for ( var i = 0; i < menuItems.length; i++) {
								menu.items.items[menuItems[i]].disable()
							}
						}
					},
					onContextHide : function() {
						if (this.ctxNode) {
							this.ctxNode.ui.removeClass("x-node-ctx");
							this.ctxNode = null
						}
					},
					checkIsSelected : function(selectedRecords, clickTarget) {
						for ( var i = 0; i < selectedRecords.length; i++) {
							if (selectedRecords[i] == clickTarget) {
								return true
							}
						}
						return false
					},
					addMarker : function(markObj) {
						var key = markObj.g.concat(markObj.h, markObj.n);
						if (!(key in Jedox.studio.app.markers)) {
							Jedox.studio.app.markers[key] = markObj
						}
					},
					onCopy : function() {
						var cmp = Ext.getCmp("main-view-panel").getLayout().activeItem;
						if (cmp.getXType() === "dataview") {
							var nodes = cmp.getSelectedNodes();
							this.copyRecords = cmp.getRecords(nodes)
						} else {
							this.copyRecords = cmp.getSelectionModel()
									.getSelections()
						}
						this.onCutCopyParentNode = this.node
					},
					onCut : function() {
						var cmp = Ext.getCmp("main-view-panel").getLayout().activeItem;
						if (cmp.getXType() === "dataview") {
							var nodes = cmp.getSelectedNodes();
							this.cutRecords = cmp.getRecords(nodes)
						} else {
							this.cutRecords = cmp.getSelectionModel()
									.getSelections()
						}
						this.onCutCopyParentNode = this.node
					},
					onPaste : function() {
						var that = this;
						var ERRFlag = false;
						if (this.copyRecord) {
							var record = this.copyRecord;
							var newName = record.data.text;
							if (this.onCutCopyParentNode == this.node) {
								var newId = Jedox.studio.backend.wssStudio
										.treeMngNode("file", record.data.id,
												"copyNode", this.node);
								newName = this.getNewItemCopyName(
										record.data.text, record.data.iconCls);
								this.rename(newId, newName)
							} else {
								if (newName != this.getNewItemName(
										record.data.text, record.data.iconCls)) {
									var title = "Confirm Item Replace"
											.localize();
									var message = "replace_folder_warning_msg"
											.localize( {
												name : newName
											});
									ERRFlag = true;
									var fn = function() {
										var recordToRemove = that
												.getRecordByName(newName);
										that.removeRecord(recordToRemove);
										var newId = Jedox.studio.backend.wssStudio
												.treeMngNode(that.group,
														that.hierarchy,
														record.data.id,
														"copyNode", that.node);
										if (record.data.iconCls == "w3s_folder") {
											var newNode = {
												id : newId,
												text : newName,
												leaf : leaf,
												qtip : newName,
												iconCls : "w3s_folder"
											};
											Ext.getCmp("ps-wt-tree").fireEvent(
													"addNode", that.node,
													newNode)
										}
										var pasteRecord = new Ext.data.Record( {
											id : newId,
											text : newName,
											leaf : record.data.leaf,
											qtip : record.data.qtip,
											iconCls : record.data.iconCls,
											img_src : record.data.img_src
										});
										that.wbStore.add(pasteRecord)
									};
									Jedox.studio.app.showMessageQUESTION(title,
											message, fn)
								} else {
									var newId = Jedox.studio.backend.wssStudio
											.treeMngNode("file",
													record.data.id, "copyNode",
													this.node)
								}
							}
							if (!ERRFlag) {
								if (record.data.iconCls == "w3s_folder") {
									var newNode = {
										id : newId,
										text : newName,
										leaf : false,
										qtip : newName,
										iconCls : "w3s_folder"
									};
									Ext.getCmp("ps-wt-tree").fireEvent(
											"addNode", this.node, newNode)
								}
								var pasteRecord = new Ext.data.Record( {
									id : newId,
									text : newName,
									leaf : record.data.leaf,
									qtip : record.data.qtip,
									iconCls : record.data.iconCls,
									img_src : record.data.img_src
								});
								this.wbStore.add(pasteRecord);
								this.copyRecord = null
							}
						}
						for ( var i = 0; i < this.cutRecords.length; i++) {
							if (this.cutRecords[i]) {
								var record = this.cutRecords[i];
								var newName = record.data.text;
								if (this.onCutCopyParentNode == this.node) {
									ERRFlag = true;
									this.cutRecords = null
								} else {
									if (newName != this.getNewItemName(
											record.data.text,
											record.data.iconCls)) {
										var title = "Confirm Item Replace"
												.localize();
										var message = "replace_folder_warning_msg"
												.localize( {
													name : newName
												});
										ERRFlag = true;
										var fn = function() {
											var recordToRemove = that
													.getRecordByName(newName);
											Jedox.studio.backend.wssStudio
													.treeMngNode(
															"file",
															that.node,
															"removeNode",
															recordToRemove.data.id);
											Ext.getCmp("ps-wt-tree").fireEvent(
													"removeNode",
													recordToRemove.data.id);
											that.removeRecord(recordToRemove);
											var result = Jedox.studio.backend.wssStudio
													.treeMngNode("file",
															record.data.id,
															"moveNode",
															that.node);
											if (result === true) {
												if (record.data.iconCls == "w3s_folder") {
													Ext
															.getCmp(
																	"ps-wt-tree")
															.fireEvent(
																	"replaceNode",
																	record.data.id,
																	that.node)
												}
												var pasteRecord = new Ext.data.Record(
														{
															id : record.data.id,
															text : newName,
															leaf : record.data.leaf,
															qtip : record.data.qtip,
															iconCls : record.data.iconCls,
															img_src : record.data.img_src
														});
												that.wbStore.add(pasteRecord)
											}
										};
										Jedox.studio.app.showMessageQUESTION(
												title, message, fn)
									} else {
										var result = Jedox.studio.backend.wssStudio
												.treeMngNode("file",
														record.data.id,
														"moveNode", this.node);
										if (result === true) {
											ERRFlag = false
										}
									}
								}
								if (!ERRFlag) {
									if (record.data.iconCls == "w3s_folder") {
										Ext.getCmp("ps-wt-tree").fireEvent(
												"replaceNode", record.data.id,
												this.node)
									}
									var pasteRecord = new Ext.data.Record( {
										id : record.data.id,
										text : newName,
										leaf : record.data.leaf,
										qtip : record.data.qtip,
										iconCls : record.data.iconCls,
										img_src : record.data.img_src
									});
									this.wbStore.add(pasteRecord)
								}
							}
						}
						this.cutRecords = null;
						this.onCutCopyParentNode = null
					},
					onPasteNEW : function() {
						var that = this;
						that.pasteAction = [];
						var removeNodes = [];
						var copyNodes = [];
						var cutNodes = [];
						var copyInSameFolderFlag = false;
						var operation;
						var pasteRecords;
						if (that.cutRecords) {
							pasteRecords = that.cutRecords;
							operation = "moveNode"
						} else {
							if (that.copyRecords) {
								pasteRecords = that.copyRecords;
								operation = "copyNode"
							} else {
								return
							}
						}
						function walkThroughtRecords(index) {
							var i = index + 1 || 0;
							var BREAKflag = false;
							if (i == pasteRecords.length) {
								executePasteAction()
							}
							for (; i < pasteRecords.length; i++) {
								for ( var j = 0; j < that.wbStore.getCount(); j++) {
									if (that.wbStore.getAt(j).data.text
											.toLowerCase() == pasteRecords[i].data.text
											.toLowerCase()) {
										showQuestionDlg(pasteRecords[i],
												that.wbStore.getAt(j), i,
												walkThroughtRecords);
										BREAKflag = true;
										break
									}
								}
								if (BREAKflag) {
									break
								}
								var action = [ pasteRecords[i].data.id,
										operation, that.node ];
								that.pasteAction.push(action);
								if (i == pasteRecords.length - 1) {
									executePasteAction()
								}
							}
						}
						function showQuestionDlg(pasteRecord, removeRecord,
								index, callBackFnc) {
							switch (operation) {
							case "moveNode":
								showCutQuestionDlg(pasteRecord, removeRecord,
										index, callBackFnc);
								break;
							case "copyNode":
								showCopyQuestionDlg(pasteRecord, removeRecord,
										index, callBackFnc);
								break
							}
						}
						function showCopyQuestionDlg(copyRecord, removeRecord,
								index, callBackFnc) {
							if (copyRecord != removeRecord) {
								var title = "Confirm Item Replace".localize();
								var message = "replace_folder_warning_msg"
										.localize( {
											name : removeRecord.data.text
										});
								var fnYES = function() {
									var removeAction = [ that.node,
											"removeNode", removeRecord.data.id ];
									var copyAction = [ copyRecord.data.id,
											"copyNode", that.node ];
									that.pasteAction.push(removeAction,
											copyAction);
									callBackFnc(index)
								};
								var fnNO = function() {
									callBackFnc(index)
								};
								Jedox.studio.app.showMessageQUESTION_YN(title,
										message, fnYES, fnNO)
							} else {
								var newId = Jedox.studio.backend.wssStudio
										.treeMngNode("file",
												copyRecord.data.id, "copyNode",
												that.node);
								var newName = that.getNewItemCopyName(
										copyRecord.data.text,
										copyRecord.data.iconCls);
								that.rename(newId, newName);
								copyInSameFolderFlag = true;
								callBackFnc(index)
							}
						}
						function showCutQuestionDlg(cutRecord, removeRecord,
								index, callBackFnc) {
							if (cutRecord != removeRecord) {
								var title = "Confirm Item Replace".localize();
								var message = "replace_folder_warning_msg"
										.localize( {
											name : removeRecord.data.text
										});
								var fnYES = function() {
									var removeAction = [ that.node,
											"removeNode", removeRecord.data.id ];
									var cutAction = [ cutRecord.data.id,
											"moveNode", that.node ];
									that.pasteAction.push(removeAction,
											cutAction);
									callBackFnc(index)
								};
								var fnNO = function() {
									callBackFnc(index)
								};
								Jedox.studio.app.showMessageQUESTION_YN(title,
										message, fnYES, fnNO)
							} else {
								callBackFnc(index)
							}
						}
						function cleanClipboard() {
							that.copyRecords = null;
							that.cutRecords = null
						}
						function propagateInterface() {
							Ext.getCmp("ps-wt-tree").fireEvent("refreshOnCopy",
									that.node);
							that.initWTData(that.node, that.activeGroup,
									that.activeHierarchy, that._parentPerm)
						}
						function executePasteAction() {
							if (that.pasteAction.length > 0) {
								var result = Jedox.studio.backend.wssStudio
										.treeMngNode("file", that.pasteAction);
								propagateInterface()
							}
							if (copyInSameFolderFlag) {
								propagateInterface()
							}
							cleanClipboard()
						}
						walkThroughtRecords()
					},
					activateFilesTab : function() {
						this.filesNavigationTabPanel.setActiveTab(0);
						this.filesWSSPanel.getLayout().setActiveItem(
								"main-view-panel")
					},
					handlePasteNodes : function() {
						Jedox.studio.backend.wssStudio
								.treeMngNode("file", that.node, "removeNode",
										recordToRemove.data.id)
					},
					onRename : function() {
						var cmp = Ext.getCmp("main-view-panel").getLayout().activeItem;
						cmp.fireEvent("edit", cmp);
						this._renameFlag = true
					},
					onProperties : function(node, cFlag) {
						if (cFlag) {
							Ext.getCmp("ps-wt-tree").fireEvent(
									"onNodeProperties", node);
							return
						}
						var that = this;
						var data = Jedox.studio.backend.wssStudio
								.getNodePropertiesData("file",
										this.activeGroup, this.activeHierarchy,
										node);
						switch (data.type) {
						case "Hyperlink":
							that.onHyperlinkProperties(node, data);
							break;
						case "Urlplugin":
							that.onURLPluginProperties(node, data);
							break;
						default:
							Jedox.studio.files
									.properties("file", data,
											this.renameHandleFnc,
											this.refreshHandleFnc)
						}
					},
					onHyperlinkProperties : function(node, data) {
						var that = this;
						var fn = function(hName, hDesc, hPath, hTarget) {
							var result = Jedox.studio.backend.wssStudio
									.setHyperlinkPropertiesData("file",
											that.activeGroup,
											that.activeHierarchy, node, {
												desc : hDesc,
												target : hTarget,
												url : hPath
											});
							that.renameHandleFnc(hName)
						};
						Jedox.studio.files.properties("hyperlink", data, fn,
								that.refreshHandleFnc)
					},
					onURLPluginProperties : function(node, data) {
						var that = this;
						var fn = function(name, desc, params) {
							var result = Jedox.studio.backend.wssStudio
									.setURLPluginPropertiesData("file",
											that.activeGroup,
											that.activeHierarchy, node, desc,
											params);
							that.renameHandleFnc(name)
						};
						Jedox.studio.files.properties("urlplugin", data, fn,
								that.refreshHandleFnc)
					},
					validateName : function(value, iconCls) {
						var that = this;
						var id = this.selection;
						var oldName = this.activeRecord.data.text;
						var _return = true;
						if (oldName != value) {
							if (value.length < 64) {
								this.wbStore
										.each(
												function(record) {
													if (record.data.text
															.toLowerCase() == value
															.toLowerCase()
															&& (iconCls && iconCls == record.data.iconCls)) {
														showErrMsg(
																"Error Renaming File or Folder"
																		.localize(),
																"rename_item_error_msg"
																		.localize( {
																			old_name : oldName,
																			new_name : value
																		}));
														_return = false
													}
												}, [ this ])
							} else {
								showErrMsg("Error Renaming File or Folder"
										.localize(),
										"rename_item_long_error_msg".localize( {
											old_name : oldName,
											new_name : value
										}));
								_return = false
							}
						} else {
							_return = false
						}
						function showErrMsg(title, msg) {
							var fn = function() {
								var cmp = Ext.getCmp("main-view-panel")
										.getLayout().activeItem;
								cmp.fireEvent("edit", cmp)
							};
							Jedox.studio.app.showMessageQUESTIONERROR(title,
									msg, fn)
						}
						return _return
					},
					trim : function(s) {
						return s.replace(/^\s+|\s+$/g, "")
					},
					disableTlb : function() {
						this.fTlb.disable()
					},
					enableTlb : function() {
						if (this.fTlb.disabled) {
							this.fTlb.enable()
						}
					},
					getWSSURL : function(g, h, n) {
						var page = "/spreadsheet/root/cc/wss_preload.php?";
						var wb = n;
						var grp = g;
						var hrc = h;
						var url = page.concat("wam=designer&wb=", wb, "&grp=",
								grp, "&hrc=", hrc);
						return url
					},
					openWB : function(id, w3s_data) {
						if (!this.verifySession()) {
							return
						}
						var that = this;
						this.activateFlag = false;
						var title = w3s_data ? w3s_data.title ? w3s_data.title
								: w3s_data.name : that.findById(id);
						if (!w3s_data) {
							var w3s_data = {
								group : that.activeGroup,
								hierarchy : that.activeHierarchy,
								node : id,
								name : title,
								close_trigger : false,
								open_trigger : false,
								switch_trigger : false,
								loaded : false
							}
						}
						var tab_uid = this.getTabUID(w3s_data.group,
								w3s_data.hierarchy, w3s_data.node);
						function showWBTab() {
							if (!(NWtab)) {
								var NWtab = new Ext.Panel(
										{
											id : tab_uid,
											title : title,
											layout : "fit",
											bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
											header : false,
											closable : true,
											minimizable : true,
											border : true,
											group : w3s_data.group,
											hierarchy : w3s_data.hierarchy,
											node : w3s_data.node,
											w3s_data : w3s_data,
											type : "wss",
											listeners : {
												beforeclose : function(panel) {
													if (that.filesNavigationTabPanel
															.getActiveTab() != panel) {
														this.fireEvent(
																"activate",
																panel)
													}
													if (Jedox.studio.frames.files.Jedox.wss.workspace.activeWin) {
														Jedox.studio.frames.files.Jedox.wss.workspace
																.closeActiveWin()
													}
													return false
												},
												beforedestroy : function(panel) {
													if (this.w3s_data.close_trigger) {
														return true
													}
												},
												destroy : function(panel) {
													if (that.filesNavigationTabPanel.items.items.length == 1) {
														that.filesNavigationTabPanel
																.setActiveTab(0);
														that.filesWSSPanel
																.getLayout()
																.setActiveItem(
																		"main-view-panel")
													} else {
														switch (that.filesNavigationTabPanel
																.getActiveTab().type) {
														case "hyperlink":
															that.filesWSSPanel
																	.getLayout()
																	.setActiveItem(
																			2);
															break;
														case "static":
															that.filesWSSPanel
																	.getLayout()
																	.setActiveItem(
																			3);
															break;
														case "wss":
															break;
														default:
															that.filesNavigationTabPanel
																	.setActiveTab(0);
															that.filesWSSPanel
																	.getLayout()
																	.setActiveItem(
																			"main-view-panel");
															break
														}
													}
												},
												beforehide : function(panel) {
													if (that.filesNavigationTabPanel
															.getActiveTab() == that.filesNavigationTabPanel
															.getComponent(0)) {
														that.filesWSSPanel
																.getLayout()
																.setActiveItem(
																		"main-view-panel")
													}
												},
												activate : function(panel) {
													if (that._currentTab) {
														that.filesWSSPanel
																.getLayout()
																.setActiveItem(
																		1);
														return true
													}
													if (this.w3s_data.open_trigger) {
														that.filesWSSPanel
																.getLayout()
																.setActiveItem(
																		1);
														this.w3s_data.open_trigger = false;
														this.w3s_data.loaded = true;
														return true
													}
													if (this.w3s_data.switch_trigger) {
														that.filesWSSPanel
																.getLayout()
																.setActiveItem(
																		1);
														this.w3s_data.switch_trigger = false;
														return true
													}
													if (that.activateFlag) {
														that.filesWSSPanel
																.getLayout()
																.setActiveItem(
																		1);
														if (this.w3s_data.loaded) {
															var ghn = {
																g : this.w3s_data.group,
																h : this.w3s_data.hierarchy,
																n : this.w3s_data.node
															};
															if (ghn.g == "nn"
																	&& ghn.h == "nn") {
																ghn = null
															}
															Jedox.studio.frames.files.Jedox.wss.workspace
																	.showWinByMeta(
																			this.w3s_data.name,
																			ghn)
														} else {
															Jedox.studio.frames.files.Jedox.wss.book
																	.load(
																			null,
																			this.w3s_data.node,
																			this.w3s_data.group,
																			this.w3s_data.hierarchy);
															this.w3s_data.loaded = true
														}
													} else {
														if (Jedox.studio.frames.files) {
															that.filesWSSPanel
																	.getLayout()
																	.setActiveItem(
																			1)
														} else {
															this.w3s_data.loaded = true
														}
													}
												}
											}
										});
								that.filesNavigationTabPanel.add(NWtab)
							}
							NWtab.show();
							that.filesNavigationTabPanel.setActiveTab(NWtab)
						}
						if (!Jedox.studio.frames.files) {
							Ext.MessageBox.show( {
								title : "Palo Suite".localize(),
								msg : "wss_open_wait".localize(),
								closable : false,
								icon : "largeLoadingImage"
							});
							showWBTab();
							that.wss = {
								xtype : "iframepanel",
								id : "wssframe-designer-mode",
								border : false,
								frameStyle : {
									overflow : "hidden"
								},
								defaultSrc : that.getWSSURL(w3s_data.group,
										w3s_data.hierarchy, id),
								listeners : {
									documentloaded : function() {
										frames[(Ext.isIE) ? 0
												: frames.length - 1].Jedox.wss.events
												.registerPlugin(new Jedox.studio.plugin.WSSPluginFiles());
										Ext.MessageBox.hide()
									}
								}
							};
							that.wssPanel.add(this.wss);
							that.filesWSSPanel.getLayout().setActiveItem(1);
							Jedox.studio.frames.files = frames[(Ext.isIE) ? 0
									: frames.length - 1];
							Jedox.studio.frames.filesLoaded = true
						} else {
							this.activateFlag = true;
							showWBTab()
						}
					},
					getHyperlinkURL : function(g, h, n) {
						var url = Jedox.studio.backend.wssStudio
								.getHyperlinkURL("file", g, h, n);
						return url
					},
					getURLPluginURL : function(g, h, n) {
						var url = Jedox.studio.backend.wssStudio
								.getURLPluginURL("file", g, h, n);
						return url
					},
					getStaticURL : function(g, h, n, name) {
						return "/spreadsheet/root/cc/studio/static.php/"
								.concat(escape(name), "?t=file&g=", g, "&h=",
										h, "&n=", n)
					},
					getHyperlinkTabID : function(g, h, n) {
						var tabID = g.concat("-", h, "-", n, "-hyperlink");
						return tabID
					},
					getStaticTabID : function(g, h, n) {
						var tabID = g.concat("-", h, "-", n, "-static");
						return tabID
					},
					openHyperlink : function(id, hyperlink_data) {
						var that = this;
						if (!hyperlink_data) {
							var hyperlink_data = {
								group : that.activeGroup,
								hierarchy : that.activeHierarchy,
								node : id,
								name : that.findById(id),
								urlData : that.isURLPlugin(id) ? that
										.getURLPluginURL(that.activeGroup,
												that.activeHierarchy, id)
										: that.getHyperlinkURL(
												that.activeGroup,
												that.activeHierarchy, id),
								hyperlinkID : that.getHyperlinkTabID(
										that.activeGroup, that.activeHierarchy,
										id),
								frameID : that.getHyperlinkTabID(
										that.activeGroup, that.activeHierarchy,
										id)
										+ "-frame",
								urlPlugin : that.isURLPlugin(id)
							}
						}
						if (hyperlink_data.urlData.target == "blank") {
							that.openInNewWindow( {
								g : hyperlink_data.group,
								h : hyperlink_data.hierarchy,
								n : id,
								t : hyperlink_data.urlPlugin ? "ahview"
										: "hyperlink"
							}, hyperlink_data.name);
							return
						}
						function showHiperlinkTab(newFlag) {
							if (newFlag) {
								var NWtab = new Ext.Panel(
										{
											id : hyperlink_data.hyperlinkID,
											title : hyperlink_data.name,
											layout : "fit",
											bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
											header : false,
											closable : true,
											minimizable : true,
											border : true,
											type : "hyperlink",
											hyperlink_data : hyperlink_data,
											listeners : {
												destroy : function(panel) {
													Ext
															.getCmp(
																	that.hyperlinkRegistar[this.id])
															.destroy();
													that.hyperlinkPanel
															.remove(that.hyperlinkRegistar[this.id]);
													delete that.hyperlinkRegistar[this.id]
												},
												beforehide : function(panel) {
													if (that.filesNavigationTabPanel
															.getActiveTab() == that.filesNavigationTabPanel
															.getComponent(0)) {
														that.filesWSSPanel
																.getLayout()
																.setActiveItem(
																		"main-view-panel")
													}
												},
												activate : function(panel) {
													that.filesWSSPanel
															.getLayout()
															.setActiveItem(2);
													if (this.opened) {
														that.filesWSSPanel
																.getLayout().activeItem
																.getLayout()
																.setActiveItem(
																		that.hyperlinkRegistar[this.id])
													} else {
														this.opened = true
													}
												}
											}
										});
								that.filesNavigationTabPanel.add(NWtab)
							}
							Ext.getCmp(hyperlink_data.hyperlinkID).show();
							that.filesNavigationTabPanel.setActiveTab(Ext
									.getCmp(hyperlink_data.hyperlinkID))
						}
						if (hyperlink_data.hyperlinkID in that.hyperlinkRegistar) {
							showHiperlinkTab(false)
						} else {
							showHiperlinkTab(true);
							Ext.MessageBox
									.show( {
										title : "Palo Suite".localize(),
										msg : hyperlink_data.urlPlugin ? "wss_openpivot_wait"
												.localize()
												: "wss_openhl_wait".localize(),
										closable : false,
										icon : "largeLoadingImage"
									});
							var hyperlinkIFrame = {
								xtype : "iframepanel",
								id : hyperlink_data.frameID,
								border : false,
								defaultSrc : hyperlink_data.urlData.url,
								listeners : {
									documentloaded : function() {
										Ext.MessageBox.hide()
									}
								}
							};
							that.hyperlinkPanel.add(hyperlinkIFrame);
							that.hyperlinkRegistar[hyperlink_data.hyperlinkID] = hyperlink_data.frameID;
							that.filesWSSPanel.getLayout().setActiveItem(2);
							that.filesWSSPanel.getLayout().activeItem
									.getLayout()
									.setActiveItem(
											that.hyperlinkRegistar[hyperlink_data.hyperlinkID])
						}
					},
					getStaticReg : function(key) {
						return this.staticRegistar[key]
					},
					openStatic : function(id, static_data) {
						var that = this;
						if (!static_data) {
							var static_data = {
								group : that.activeGroup,
								hierarchy : that.activeHierarchy,
								node : id,
								name : that.findById(id),
								type : that.activeRecord.get("iconCls").split(
										"_").pop(),
								url : that.getStaticURL(that.activeGroup,
										that.activeHierarchy, id, that
												.findById(id)),
								staticID : that.getStaticTabID(
										that.activeGroup, that.activeHierarchy,
										id),
								frameID : that.getStaticTabID(that.activeGroup,
										that.activeHierarchy, id)
										+ "-frame"
							}
						}
						function showStaticTab(newFlag) {
							if (newFlag) {
								var NWtab = new Ext.Panel(
										{
											id : static_data.staticID,
											title : static_data.name,
											layout : "fit",
											bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
											header : false,
											closable : true,
											minimizable : true,
											border : true,
											opened : false,
											static_data : static_data,
											type : "static",
											listeners : {
												destroy : function(panel) {
													Ext
															.getCmp(
																	that.staticRegistar[this.id])
															.destroy();
													that.staticPanel
															.remove(that.staticRegistar[this.id]);
													delete that.staticRegistar[this.id]
												},
												beforehide : function(panel) {
													if (that.filesNavigationTabPanel
															.getActiveTab() == that.filesNavigationTabPanel
															.getComponent(0)) {
														that.filesWSSPanel
																.getLayout()
																.setActiveItem(
																		"main-view-panel")
													}
												},
												activate : function(panel) {
													that.filesWSSPanel
															.getLayout()
															.setActiveItem(3);
													if (this.opened) {
														that.filesWSSPanel
																.getLayout().activeItem
																.getLayout()
																.setActiveItem(
																		that.staticRegistar[this.id])
													} else {
														this.opened = true
													}
												}
											}
										});
								that.filesNavigationTabPanel.add(NWtab)
							}
							Ext.getCmp(static_data.staticID).show();
							that.filesNavigationTabPanel.setActiveTab(Ext
									.getCmp(static_data.staticID))
						}
						if (static_data.staticID in that.staticRegistar) {
							showStaticTab(false)
						} else {
							showStaticTab(true);
							var staticIFrame = {
								xtype : "iframepanel",
								id : static_data.frameID,
								border : false,
								defaultSrc : static_data.url,
								listeners : {
									documentloaded : function() {
									}
								}
							};
							that.staticPanel.add(staticIFrame);
							that.staticRegistar[static_data.staticID] = static_data.frameID;
							that.filesWSSPanel.getLayout().setActiveItem(3);
							that.filesWSSPanel.getLayout().activeItem
									.getLayout()
									.setActiveItem(
											that.staticRegistar[static_data.staticID])
						}
					},
					openHyperlinkURLFromWSS : function(urlData) {
						var that = this;
						if (urlData.target == "win") {
							window.open(urlData.url, "_blank");
							return
						}
						var hyperlinkID = urlData.url.concat("-hyperlink");
						var frameID = hyperlinkID + "-frame";
						var title = urlData.title;
						function showHiperlinkTab(newFlag) {
							if (newFlag) {
								var NWtab = new Ext.Panel(
										{
											id : hyperlinkID,
											title : title,
											layout : "fit",
											bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
											header : false,
											closable : true,
											minimizable : true,
											border : true,
											type : "hyperlink",
											listeners : {
												destroy : function(panel) {
													Ext
															.getCmp(
																	that.hyperlinkRegistar[this.id])
															.destroy();
													that.hyperlinkPanel
															.remove(that.hyperlinkRegistar[this.id]);
													delete that.hyperlinkRegistar[this.id]
												},
												beforehide : function(panel) {
													if (that.filesNavigationTabPanel
															.getActiveTab() == that.filesNavigationTabPanel
															.getComponent(0)) {
														that.filesWSSPanel
																.getLayout()
																.setActiveItem(
																		"main-view-panel")
													}
												},
												activate : function(panel) {
													that.filesWSSPanel
															.getLayout()
															.setActiveItem(2);
													if (this.opened) {
														that.filesWSSPanel
																.getLayout().activeItem
																.getLayout()
																.setActiveItem(
																		that.hyperlinkRegistar[this.id])
													} else {
														this.opened = true
													}
												}
											}
										});
								that.filesNavigationTabPanel.add(NWtab)
							}
							Ext.getCmp(hyperlinkID).show();
							that.filesNavigationTabPanel.setActiveTab(Ext
									.getCmp(hyperlinkID))
						}
						if (hyperlinkID in that.hyperlinkRegistar) {
							showHiperlinkTab(false)
						} else {
							showHiperlinkTab(true);
							var hyperlinkIFrame = {
								xtype : "iframepanel",
								id : frameID,
								border : false,
								defaultSrc : urlData.url,
								listeners : {
									documentloaded : function() {
									}
								}
							};
							that.hyperlinkPanel.add(hyperlinkIFrame);
							that.hyperlinkRegistar[hyperlinkID] = frameID;
							that.filesWSSPanel.getLayout().setActiveItem(2);
							that.filesWSSPanel.getLayout().activeItem
									.getLayout()
									.setActiveItem(
											that.hyperlinkRegistar[hyperlinkID])
						}
					},
					verifySession : function() {
						if (!Jedox.studio.backend.wssStudio.verifySess()) {
							var title = "Logout".localize();
							var msg = "session_expired".localize();
							var callBackFnc = function() {
								window.location.href = "/spreadsheet/root/ui/login/?r"
							};
							Jedox.studio.app.showMessageQUESTIONERROR(title,
									msg, callBackFnc);
							return false
						} else {
							return true
						}
					},
					openInNewWindow : function(ghnt, name) {
						var that = this;
						switch (ghnt.t) {
						case "workbook":
							if (!this.verifySession()) {
								return
							}
							var url = this.getWSSURL(ghnt.g, ghnt.h, ghnt.n);
							break;
						case "hyperlink":
							var url = this.getHyperlinkURL(ghnt.g, ghnt.h,
									ghnt.n).url;
							break;
						case "ahview":
							var url = this.getURLPluginURL(ghnt.g, ghnt.h,
									ghnt.n).url;
							break;
						default:
							var url = this.getStaticURL(ghnt.g, ghnt.h, ghnt.n,
									name);
							break
						}
						window.open(url, "_blank")
					},
					editWB : function(group, hierarchy, n, NOTopenFlag) {
						for ( var i = 0; i < this.wbStore.getCount(); i++) {
							if (this.wbStore.getAt(i).get("id") == n) {
								break
							}
						}
						Ext.getCmp("main-list-view").select(i);
						Ext.getCmp("main-thumbnails-view").select(i);
						Ext.getCmp("main-details-view").getSelectionModel()
								.selectRow(i);
						if (!NOTopenFlag) {
							this.openWB(n)
						}
					},
					getTabUID : function(group, hierarchy, node) {
						return group.concat("-", hierarchy, "-", node)
					},
					getDefaultW3S_data : function(ghn, name) {
						var wss = {
							group : ghn.g,
							hierarchy : ghn.h,
							node : ghn.n,
							name : name,
							close_trigger : false,
							open_trigger : false,
							switch_trigger : false,
							loaded : false
						};
						return wss
					},
					getDefaultStatic_data : function(ghnt, name) {
						var that = this;
						var static_data = {
							group : ghnt.g,
							hierarchy : ghnt.h,
							node : ghnt.n,
							name : name,
							type : ghnt.t,
							path : ghnt.p || null,
							url : that.getStaticURL(ghnt.g, ghnt.h, ghnt.n,
									name),
							staticID : that.getStaticTabID(ghnt.g, ghnt.h,
									ghnt.n),
							frameID : that.getStaticTabID(ghnt.g, ghnt.h,
									ghnt.n)
									+ "-frame"
						};
						return static_data
					},
					getDefaultHyperlink_data : function(ghnt, name) {
						var that = this;
						var hyperlink_data = {
							group : ghnt.g,
							hierarchy : ghnt.h,
							node : ghnt.n,
							name : name,
							type : ghnt.t,
							path : ghnt.p || null,
							urlData : ghnt.t == "ahview" ? that
									.getURLPluginURL(ghnt.g, ghnt.h, ghnt.n)
									: that.getHyperlinkURL(ghnt.g, ghnt.h,
											ghnt.n),
							hyperlinkID : that.getHyperlinkTabID(ghnt.g,
									ghnt.h, ghnt.n),
							frameID : that.getHyperlinkTabID(ghnt.g, ghnt.h,
									ghnt.n)
									+ "-frame",
							urlPlugin : ghnt.t == "ahview" ? true : false
						};
						return hyperlink_data
					},
					openRecent : function(ghntp, name, type) {
						var data;
						switch (type) {
						case "spreadsheet":
							var w3s_data = this.getDefaultW3S_data(ghntp, name);
							this.openWB(ghntp.n, w3s_data);
							break;
						case "static":
							var static_data = this.getDefaultStatic_data(ghntp,
									name);
							this.openStatic(ghntp.n, static_data);
							break;
						case "hyperlink":
							var hyperlink_data = this.getDefaultHyperlink_data(
									ghntp, name);
							this.openHyperlink(ghntp.n, hyperlink_data);
							break;
						default:
							break
						}
					},
					triggerOpenWB_beforeFromWSS : function(ghn, name) {
						var perm = Jedox.studio.backend.wssStudio
								.getNodePermission(ghn.g, ghn.h, ghn.n);
						if ((perm == -1)) {
							throw "follHLInvalidRng".localize()
						}
					},
					triggerOpenWBFromWSS : function(ghn, name) {
						var w3s_data = this.getDefaultW3S_data(ghn, name);
						w3s_data.open_trigger = true;
						this.openWB(ghn.n, w3s_data)
					},
					triggerCloseWBFromWSS : function(ghn, name) {
						if (this._beforeCloseTriggerFlag
								&& !this._triggerSaveAsFromWSSFlag) {
							this._beforeCloseTriggerFlag = !this._beforeCloseTriggerFlag;
							return
						}
						if (ghn) {
							var tab_uid = this.getTabUID(ghn.g, ghn.h, ghn.n)
						} else {
							var tab_uid = this.getTabUID("nn", "nn", name)
						}
						var tab = this.filesNavigationTabPanel
								.getComponent(tab_uid);
						if (tab) {
							tab.w3s_data.close_trigger = true;
							this.filesNavigationTabPanel.remove(tab)
						}
					},
					triggerSwitchWBFromWSS : function(ghn, name) {
						if (!ghn) {
							var ghn = {
								g : "nn",
								h : "nn",
								n : name
							}
						}
						var tab_uid = this.getTabUID(ghn.g, ghn.h, ghn.n);
						var tab = this.filesNavigationTabPanel
								.getComponent(tab_uid);
						if (tab
								&& tab != this.filesNavigationTabPanel
										.getActiveTab()) {
							tab.w3s_data.switch_trigger = true;
							this.filesNavigationTabPanel.activate(tab)
						}
					},
					triggerNewWBFromWSS : function(name) {
						var ghn = {
							g : "nn",
							h : "nn",
							n : name
						};
						var w3s_data = this.getDefaultW3S_data(ghn, name);
						w3s_data.open_trigger = true;
						this.openWB(ghn.n, w3s_data)
					},
					triggerSaveAsFromWSS : function(ghn, oldName, name) {
						this._triggerSaveAsFromWSSFlag = true;
						Jedox.studio.app.resourcesRefreshFlag = true;
						this._ghn = ghn;
						var tab = this.filesNavigationTabPanel.getActiveTab();
						if (tab) {
							tab.w3s_data.close_trigger = true;
							this.filesNavigationTabPanel.remove(tab)
						}
						this.triggerOpenWBFromWSS(ghn, name);
						this._triggerSaveAsFromWSSFlag = false;
						Jedox.studio.frames.files.Jedox.wss.workspace
								.showWinByMeta(name, ghn)
					},
					triggerHideWBFromWSS : function(ghn, name) {
						if (!ghn) {
							var ghn = {
								g : "nn",
								h : "nn",
								n : name
							}
						}
						var tab_uid = this.getTabUID(ghn.g, ghn.h, ghn.n);
						var tab = this.filesNavigationTabPanel
								.getComponent(tab_uid);
						tab.setTitle(tab.title + " - hidden");
						tab.disabled = true
					},
					triggerUnhideWBFromWSS : function(ghn, name) {
						if (!ghn) {
							var ghn = {
								g : "nn",
								h : "nn",
								n : name
							}
						}
						var tab_uid = this.getTabUID(ghn.g, ghn.h, ghn.n);
						var tab = this.filesNavigationTabPanel
								.getComponent(tab_uid);
						tab.setTitle(tab.title.replace(" - hidden", ""));
						tab.disabled = false
					},
					triggerImportWBFromWSS : function(name) {
						var ghn = {
							g : "nn",
							h : "nn",
							n : name
						};
						var w3s_data = this.getDefaultW3S_data(ghn, name);
						w3s_data.open_trigger = true;
						this.openWB(ghn.n, w3s_data)
					},
					triggerOpenURLFromWSS : function(url, title, target) {
						var urlData = {
							url : url,
							title : title,
							target : target
						};
						this.openHyperlinkURLFromWSS(urlData)
					},
					triggerOpenStaticFromWSS : function(ghnt, target, chkACLs) {
						var that = this;
						if (chkACLs) {
							this.triggerOpenWB_beforeFromWSS(ghnt)
						}
						var wssStudioHandler = {
							getNodeName : function(name) {
								if (!name) {
									return false
								}
								switch (target) {
								case "tab":
									if (ghnt.t == "hyperlink"
											|| ghnt.t == "ahview") {
										var hyperlink_data = that
												.getDefaultHyperlink_data(ghnt,
														name);
										that.openHyperlink(ghnt.n,
												hyperlink_data)
									} else {
										var static_data = that
												.getDefaultStatic_data(ghnt,
														name);
										that.openStatic(ghnt.n, static_data)
									}
									break;
								case "win":
									that.openInNewWindow(ghnt, name);
									break;
								case "export":
									Jedox.studio.files.exportFile( {
										g : ghnt.g,
										h : ghnt.h,
										n : ghnt.n
									}, "file");
									break
								}
							}
						};
						var wssStudioStub = new Studio(wssStudioHandler);
						wssStudioStub.getNodeName(ghnt.g, ghnt.h, ghnt.n)
					},
					clearAllSelections : function() {
						Ext.getCmp("main-list-view").clearSelections();
						Ext.getCmp("main-thumbnails-view").clearSelections();
						Ext.getCmp("main-details-view").getSelectionModel()
								.clearSelections();
						this.setNoSelectionInterface()
					},
					setNoSelectionInterface : function() {
						this.fTlb.items.items[1].disable()
					},
					setHasSelectionInterface : function() {
						this.fTlb.items.items[1].enable()
					},
					setUpTlbBtnState : function(state) {
						var tlbBtns = this.fTlb.items.items;
						state ? tlbBtns[5].enable() : tlbBtns[5].disable()
					},
					setContainerInterfacePermission : function(parentPerm) {
						var permType = Jedox.studio.access.permType;
						var tlbBtns = this.fTlb.items.items;
						!(parentPerm & permType.WRITE) ? tlbBtns[0].disable()
								: tlbBtns[0].enable();
						tlbBtns[1].disable();
						!(parentPerm & permType.WRITE) ? tlbBtns[3].disable()
								: tlbBtns[3].enable();
						this.fCtxDFlag.c_paste = this.fCtxDFlag.c_new = this.fCtxDFlag.c_properties = !(parentPerm & permType.WRITE)
					},
					setInterfacePermission : function(nodePerm) {
						var permType = Jedox.studio.access.permType;
						!(nodePerm & permType.DELETE) ? this.fTlb.items.items[1]
								.disable()
								: this.fTlb.items.items[1].enable();
						this.fCtxDFlag.openNT = !(this._parentPerm & permType.WRITE);
						this.fCtxDFlag.cut = !(nodePerm & permType.DELETE);
						this.fCtxDFlag.copy = !(nodePerm & permType.READ);
						this.fCtxDFlag.rename = !(nodePerm & permType.WRITE);
						this.fCtxDFlag.remove = !(nodePerm & permType.DELETE);
						this.fCtxDFlag.properties = !(nodePerm & permType.WRITE)
					},
					resetInterfacePermission : function() {
						this.enableTlb();
						this.fCtxDFlag.open = false;
						this.fCtxDFlag.openNT = false;
						this.fCtxDFlag.cut = false;
						this.fCtxDFlag.copy = false;
						this.fCtxDFlag.rename = false;
						this.fCtxDFlag.remove = false;
						this.fCtxDFlag.properties = false
					},
					setInputMode : function() {
						Jedox.studio.app.inputMode = Jedox.studio.app.inputMode_content_FILES
					},
					moveSelection : function(operation) {
						var that = this;
						var cmp = Ext.getCmp("main-view-panel").getLayout().activeItem;
						var storeCount = cmp.store.getCount();
						if (storeCount == 0) {
							return
						}
						var selectedIndex = that.getSelectedIndex() || 0;
						switch (operation) {
						case "UP":
							selectedIndex == 0 ? selectedIndex = storeCount - 1
									: selectedIndex -= 1;
							break;
						case "DOWN":
							selectedIndex == storeCount - 1 ? selectedIndex = 0
									: selectedIndex += 1;
							break
						}
						if (cmp.getXType() === "dataview") {
							cmp.select(selectedIndex);
							cmp.fireEvent("click", cmp, selectedIndex, cmp
									.getNode(selectedIndex))
						} else {
							cmp.fireEvent("rowclick", cmp, selectedIndex)
						}
					},
					onScroll : function(operation) {
						var that = this;
						var cmp = Ext.getCmp("main-view-panel").getLayout().activeItem;
						var storeCount = cmp.store.getCount();
						switch (operation) {
						case "UP":
							that.selectedIndex = 0;
							break;
						case "DOWN":
							that.selectedIndex = storeCount - 1;
							break
						}
						if (cmp.getXType() === "dataview") {
							cmp.select(that.selectedIndex);
							var nodeEl = cmp.getSelectedNodes()[0];
							var cmpEl = cmp.getEl();
							Ext.fly(nodeEl).scrollIntoView(cmpEl)
						} else {
							that.selectedIndex == 0 ? cmp.getSelectionModel()
									.selectFirstRow() : cmp.getSelectionModel()
									.selectLastRow()
						}
					},
					keyboardDispacher : function(myEvent) {
						var that = Ext.getCmp("wt-panel");
						var o = {
							13 : onENTER,
							27 : onESC,
							113 : onF2,
							46 : onDELETE,
							33 : onPAGEUP,
							34 : onPAGEDOWN,
							37 : onLEFT,
							38 : onUP,
							39 : onRIGHT,
							40 : onDOWN,
							67 : onCTRL_c,
							88 : onCTRL_x,
							86 : onCTRL_v,
							65 : onCTRL_a
						};
						if (myEvent.keyCode in o) {
							o[myEvent.keyCode]()
						}
						function onENTER() {
							var selectedRecords = that.getSelectedRecords();
							if (selectedRecords.length == 1) {
								that.onDblClick(selectedRecords[0].data.id,
										selectedRecords[0].data.iconCls)
							}
						}
						function onESC() {
						}
						function onF2() {
							var selectedRecords = that.getSelectedRecords();
							if (selectedRecords.length == 1) {
								if (!that.fCtxDFlag.rename) {
									that.onRename()
								} else {
									var title = "Warning".localize();
									var msg = "You have no permission for this operation"
											.localize();
									Jedox.studio.app.showMessageERROR(title,
											msg)
								}
							}
						}
						function onDELETE() {
							if (!that.fCtxDFlag.remove) {
								that.remove()
							} else {
								var title = "Warning".localize();
								var msg = "You have no permission for this operation"
										.localize();
								Jedox.studio.app.showMessageERROR(title, msg)
							}
						}
						function onPAGEUP() {
							that.onScroll("UP")
						}
						function onPAGEDOWN() {
							that.onScroll("DOWN")
						}
						function onLEFT() {
							that.moveSelection("UP")
						}
						function onUP() {
							that.moveSelection("UP")
						}
						function onRIGHT() {
							that.moveSelection("DOWN")
						}
						function onDOWN() {
							that.moveSelection("DOWN")
						}
						function onCTRL_c() {
							that.onCopy()
						}
						function onCTRL_x() {
							if (!that.fCtxDFlag.cut) {
								that.onCut()
							} else {
								var title = "Warning".localize();
								var msg = "You have no permission for this operation"
										.localize();
								Jedox.studio.app.showMessageERROR(title, msg)
							}
						}
						function onCTRL_v() {
							if (!that.fCtxDFlag.c_paste) {
								that.onPasteNEW()
							} else {
								var title = "Warning".localize();
								var msg = "You have no permission for this operation"
										.localize();
								Jedox.studio.app.showMessageERROR(title, msg)
							}
						}
						function onCTRL_a() {
						}
					}
				});