Jedox.studio.app.modules.users = Jedox.studio.users.init = function() {
	return {
		navigation : {
			id : "ug-tree-holder",
			title : "User Manager".localize(),
			autoScroll : true,
			collapsed : true,
			items : [ Jedox.studio.users.UGTreePanel() ],
			listeners : {
				expand : function() {
					this.body.dom.style.display = "block";
					var that = Ext.getCmp("ps-navigation-panel");
					var ugTreePanel = this.items.items[0];
					ugTreePanel.fireEvent("refreshToolbarCombo");
					if (!that.isSelected(ugTreePanel)) {
						ugTreePanel.root.firstChild.select()
					}
					that.setPanelActive("ug-panel");
					Jedox.studio.app.inputMode = Jedox.studio.app.inputMode_studio_DEFAULT
				},
				collapse : function() {
					if (Ext.getCmp("gridRowEditorUsers").isVisible()) {
						Ext.getCmp("gridRowEditorUsers").stopEditing(false)
					}
					this.body.dom.style.display = "none";
					var that = Ext.getCmp("ps-navigation-panel");
					that.timer = setTimeout(function() {
						that.expandedFlag = false;
						that.setWelcomePanelActive()
					}, 0)
				}
			}
		},
		content : new Jedox.studio.users.UGPanel(),
		btn : new Ext.Panel(
				{
					layout : "absolute",
					bodyStyle : "background-color: transparent;",
					width : 370,
					height : 180,
					border : false,
					defaults : {
						bodyStyle : "background-color: transparent;",
						baseCls : "x-plain"
					},
					items : [
							new Ext.ux.ImageButton(
									{
										autoEl : {
											tag : "img",
											src : "../lib/ext/resources/images/default/s.gif"
										},
										width : 104,
										height : 144,
										x : 20,
										y : 10,
										cls : "w_users",
										imgNormal : "../lib/ext/resources/images/default/s.gif",
										imgOver : "../lib/ext/resources/images/default/s.gif",
										imgClicked : "../lib/ext/resources/images/default/s.gif",
										actionFn : function() {
											Ext.getCmp("ug-tree-holder")
													.expand()
										}
									}),
							{
								x : 150,
								y : 114,
								html : "<b>"
										+ "Users and Groups".localize()
										+ "</b><br>"
										+ "Users and Groups administration"
												.localize(),
								baseCls : "x-plain"
							} ]
				}),
		order : 3
	}
};
Jedox.studio.users.UGTreePanel = function() {
	var _int = false;
	var connToolbar = new Ext.Toolbar( {
		id : "connToolbar",
		items : [ {
			xtype : "tbtext",
			text : "Connection_users".localize().concat(":")
		} ]
	});
	var connData = [];
	var connStore = new Ext.data.SimpleStore( {
		fields : [ "desc", "name" ],
		data : connData
	});
	var _internalConn = "<".concat("internal_conn".localize(), ">");
	var _internalConnHTM = "&lt;".concat("internal_conn".localize(), "&gt;");
	var initUsers = false;
	var initGroups = false;
	var initRoles = false;
	var connCombo = new Ext.form.ComboBox( {
		id : "conn_combo",
		store : connStore,
		displayField : "desc",
		readOnly : true,
		editable : false,
		lazyRender : true,
		typeAhead : true,
		mode : "local",
		triggerAction : "all",
		value : "",
		selectOnFocus : true,
		listWidth : 200,
		width : 200,
		listeners : {
			select : function(cmb, rec, index) {
				var res = Jedox.studio.backend.wssStudio
						.setPaloConnData(connData[index][1]);
				if (res !== true) {
					Jedox.studio.app.showMessageERROR("Connection error"
							.localize(), res.localize( {
						conn_name : connData[index][1]
					}));
					this.setValue(cmb.startValue);
					return false
				}
				if (this.value == _internalConnHTM) {
					this.setValue(_internalConn);
					_int = Jedox.studio.users.internalConnection = true
				} else {
					_int = Jedox.studio.users.internalConnection = false
				}
				initUsers = false;
				initGroups = false;
				initRoles = false;
				cmb.startValue = this.value;
				var ai = Ext.getCmp("ug-grid-panel").layout.activeItem.id
						.split("-")[1];
				switch (ai) {
				case "user":
					if (!initUsers) {
						Ext.getCmp("ug-panel").refreshUserList()
					}
					initUsers = true;
					break;
				case "group":
					if (!initGroups) {
						Ext.getCmp("ug-panel").refreshGroupList()
					}
					initGroups = true;
					break;
				case "role":
					if (!initRoles) {
						Ext.getCmp("ug-panel").refreshRoleList()
					}
					initRoles = true;
					break
				}
			}
		}
	});
	connToolbar.addField(connCombo);
	var usersAndGroupsTP = new Ext.tree.TreePanel( {
		id : "ps-ug-tree",
		rootVisible : false,
		border : false,
		lines : true,
		autoScroll : true,
		root : new Ext.tree.TreeNode("Users and Groups"),
		collapseFirst : false,
		tbar : connToolbar,
		listeners : {
			refreshToolbarCombo : function() {
				connData = [];
				var rawConnData = Jedox.studio.backend.wssStudio
						.getAllPaloConnections(false, true);
				connData.push( [ _internalConnHTM, null ]);
				for (i = 0; i < (rawConnData.connections.length); i++) {
					connData.push( [ rawConnData.connections[i][0],
							rawConnData.connections[i][0] ])
				}
				connStore.loadData(connData);
				var _isValid = connStore.find("name", rawConnData.currConn);
				if (_isValid > 0) {
					connCombo.setValue(rawConnData.currConn)
				} else {
					if (!_int) {
						connCombo.fireEvent("select", connCombo, null, 0)
					}
					connCombo.setValue(_internalConn);
					_int = Jedox.studio.users.internalConnection = true
				}
			}
		}
	});
	initUserAndGroupsTP();
	function initUserAndGroupsTP() {
		var userN = new Ext.tree.TreeNode( {
			id : "ug-user-node",
			text : "Users".localize(),
			iconCls : "user-icon",
			expanded : true
		});
		var groupN = new Ext.tree.TreeNode( {
			id : "ug-group-node",
			text : "Groups".localize(),
			iconCls : "group-icon",
			expanded : true
		});
		var roleN = new Ext.tree.TreeNode( {
			id : "ug-role-node",
			text : "Roles".localize(),
			iconCls : "role-icon",
			expanded : true
		});
		usersAndGroupsTP.root.appendChild( [ userN, groupN, roleN ])
	}
	usersAndGroupsTP.on("click", function(n) {
		var sn = this.selModel.selNode || {};
		if (n.id != sn.id) {
			var title = n.id.split("-")[1];
			Ext.getCmp("ug-grid-panel").layout.setActiveItem(n.id + "-panel");
			Ext.getCmp("ug-main-panel").setTitle(
					("All " + title.charAt(0).toUpperCase()
							+ title.slice(-(title.length - 1)) + "s")
							.localize());
			if (title === "user") {
				Ext.getCmp("ug-panel").openSelectedUser();
				if (initUsers) {
					return
				}
				Ext.getCmp("ug-panel").refreshUserList();
				initUsers = true
			} else {
				if (title === "group") {
					Ext.getCmp("ug-panel").openSelectedGroup();
					if (initGroups) {
						return
					}
					Ext.getCmp("ug-panel").refreshGroupList();
					initGroups = true
				} else {
					if (title === "role") {
						Ext.getCmp("ug-panel").openSelectedRole();
						if (initRoles) {
							return
						}
						Ext.getCmp("ug-panel").refreshRoleList();
						initRoles = true
					}
				}
			}
		}
	});
	return usersAndGroupsTP
};
Jedox.studio.users.UGPanel = function() {
	var that = this;
	this.previewFlag = 0;
	this.userStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "userName"
		}, {
			name : "fullName",
			type : "string"
		}, {
			name : "description"
		}, {
			name : "accountStatus"
		} ]
	});
	this.groupStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "groupName"
		}, {
			name : "description"
		}, {
			name : "accountStatus"
		} ]
	});
	this.roleStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "roleName"
		}, {
			name : "roleDesc"
		}, {
			name : "roleStatus"
		} ]
	});
	var user = new Jedox.studio.users.User();
	var group = new Jedox.studio.users.Group();
	var role = new Jedox.studio.users.Role();
	var addUserTlbBtn = {
		text : "Add User".localize(),
		tooltip : {
			title : "Add User".localize(),
			text : "Creates new user".localize()
		},
		id : "add",
		iconCls : "add-user-icon",
		handler : this.addUser,
		scope : this
	};
	var removeUserTlbBtn = {
		text : "Remove User".localize(),
		tooltip : {
			title : "Remove User".localize(),
			text : "Removes the user from users list".localize()
		},
		id : "delete",
		iconCls : "delete-user-icon",
		handler : this.removeUser,
		scope : this
	};
	var addGroupTlbBtn = {
		text : "Add Group".localize(),
		tooltip : {
			title : "Add Group".localize(),
			text : "Creates new group".localize()
		},
		id : "addGroup",
		iconCls : "add-group-icon",
		handler : this.addGroup,
		scope : this
	};
	var removeGroupTlbBtn = {
		text : "Remove Group".localize(),
		tooltip : {
			title : "Remove Group".localize(),
			text : "Removes the group from groups list".localize()
		},
		id : "deleteGroup",
		iconCls : "delete-group-icon",
		handler : this.removeGroup,
		scope : this
	};
	var addRoleTlbBtn = {
		text : "Add Role".localize(),
		tooltip : {
			title : "Add Role".localize(),
			text : "Creates new role".localize()
		},
		id : "addRole",
		iconCls : "add-role-icon",
		handler : this.addRole,
		scope : this
	};
	var removeRoleTlbBtn = {
		text : "Remove Role".localize(),
		tooltip : {
			title : "Remove Role".localize(),
			text : "Removes the role from role list".localize()
		},
		id : "deleteRole",
		iconCls : "delete-role-icon",
		handler : this.removeRole,
		scope : this
	};
	var layoutTlbBtn = {
		split : true,
		text : "Layout".localize(),
		tooltip : {
			title : "Layout".localize(),
			text : "Show, move or hide".localize().concat("...")
		},
		iconCls : "preview-bottom-icon",
		handler : this.movePreview.createDelegate(this, []),
		menu : {
			id : "reading-menu",
			cls : "layout-menu",
			width : 100,
			items : [ {
				text : "Bottom".localize(),
				checked : true,
				group : "rp-group",
				checkHandler : this.movePreview,
				scope : this,
				iconCls : "preview-bottom-icon"
			}, {
				text : "Right".localize(),
				checked : false,
				group : "rp-group",
				checkHandler : this.movePreview,
				scope : this,
				iconCls : "preview-right-icon"
			}, {
				text : "Hide".localize(),
				checked : false,
				group : "rp-group",
				checkHandler : this.movePreview,
				scope : this,
				iconCls : "preview-hide-icon"
			} ]
		}
	};
	var splitTlbBtn = "-";
	var viewInNewTabTlbBtn = {
		text : "View in New Tab".localize(),
		iconCls : "new-tab-icon",
		tooltip : {
			title : "View in New Tab".localize(),
			text : "Open this panel in new tab".localize().concat("...")
		},
		handler : this.openTab,
		scope : this
	};
	var viewInNewTabGridTlbBtn = {
		text : "View in New Tab".localize(),
		iconCls : "new-tab-icon",
		tooltip : {
			title : "View in New Tab".localize(),
			text : "Open this panel in new tab".localize().concat("...")
		},
		handler : this.openTabs,
		scope : this
	};
	var selectAllTlbBtn = {
		text : "Select All".localize(),
		tooltip : {
			title : "Select All".localize(),
			text : "Select All from the list".localize().concat("...")
		},
		handler : this.selectAll,
		scope : this
	};
	var clearSelectionTlbBtn = {
		text : "Clear Selection".localize(),
		tooltip : {
			title : "Clear Selection".localize(),
			text : "Clear all selection".localize().concat("...")
		},
		handler : this.clearSelections,
		scope : this
	};
	this.userGrid = new Ext.grid.GridPanel( {
		id : "ug-user-node-panel",
		border : false,
		tbar : [ addUserTlbBtn, removeUserTlbBtn, splitTlbBtn, selectAllTlbBtn,
				clearSelectionTlbBtn, viewInNewTabGridTlbBtn, splitTlbBtn,
				layoutTlbBtn ],
		colModel : new Ext.grid.ColumnModel( [ {
			header : "User name".localize(),
			width : 50,
			sortable : true,
			dataIndex : "userName"
		}, {
			header : "Full name".localize(),
			width : 100,
			sortable : true,
			dataIndex : "fullName"
		}, {
			header : "Description".localize(),
			width : 200,
			dataIndex : "description"
		} ]),
		store : this.userStore,
		autoScroll : true,
		viewConfig : {
			forceFit : true
		},
		sm : new Ext.grid.RowSelectionModel( {}),
		listeners : {
			rowdblclick : function(gridView, index, e) {
				that.openTab()
			},
			rowclick : function(gridView, index, e) {
				if (gridView.getSelectionModel().getSelections().length > 1) {
					that.changeUserSelections(gridView, index)
				}
			},
			rowselect : function(userName) {
				that.setPreview(1);
				user.initUser(userName)
			}
		}
	});
	this.userGrid
			.getSelectionModel()
			.on(
					"rowselect",
					function(sm, index, record) {
						if (this.userGrid.getSelectionModel().getSelections().length > 1) {
							that.selections = this.userGrid.getSelectionModel()
									.getSelections();
							that.setPreview(0)
						} else {
							this.userGrid.fireEvent("rowselect",
									record.data.userName)
						}
					}, this, {
						buffer : 250
					});
	this.groupGrid = new Ext.grid.GridPanel( {
		id : "ug-group-node-panel",
		border : false,
		tbar : [ addGroupTlbBtn, removeGroupTlbBtn, splitTlbBtn,
				selectAllTlbBtn, clearSelectionTlbBtn, viewInNewTabGridTlbBtn,
				splitTlbBtn, layoutTlbBtn ],
		colModel : new Ext.grid.ColumnModel( [ {
			header : "Group name".localize(),
			width : 100,
			sortable : true,
			dataIndex : "groupName"
		}, {
			header : "Description".localize(),
			width : 200
		} ]),
		store : this.groupStore,
		autoScroll : true,
		viewConfig : {
			forceFit : true,
			enableRowBody : true,
			showPreview : false
		},
		sm : new Ext.grid.RowSelectionModel( {}),
		listeners : {
			rowdblclick : function(gridView, index, e) {
				that.openTab()
			},
			rowclick : function(gridView, index, e) {
				if (gridView.getSelectionModel().getSelections().length > 1) {
					that.changeGroupSelections(gridView, index)
				}
			},
			rowselect : function(groupName) {
				that.setPreview(2);
				group.initGroup(groupName)
			}
		}
	});
	this.groupGrid
			.getSelectionModel()
			.on(
					"rowselect",
					function(sm, index, record) {
						if (this.groupGrid.getSelectionModel().getSelections().length > 1) {
							that.selections = this.groupGrid
									.getSelectionModel().getSelections();
							that.setPreview(0)
						} else {
							that.setPreview(1);
							this.groupGrid.fireEvent("rowselect",
									record.data.groupName)
						}
					}, this, {
						buffer : 250
					});
	this.roleGrid = new Ext.grid.GridPanel( {
		id : "ug-role-node-panel",
		border : false,
		tbar : [ addRoleTlbBtn, removeRoleTlbBtn, splitTlbBtn, selectAllTlbBtn,
				clearSelectionTlbBtn, viewInNewTabGridTlbBtn, splitTlbBtn,
				layoutTlbBtn ],
		colModel : new Ext.grid.ColumnModel( [ {
			header : "Role Name".localize(),
			width : 100,
			sortable : true,
			dataIndex : "roleName"
		}, {
			header : "Description".localize(),
			width : 250,
			sortable : true,
			dataIndex : "roleDesc"
		} ]),
		store : this.roleStore,
		autoScroll : true,
		viewConfig : {
			forceFit : true,
			enableRowBody : true,
			showPreview : false
		},
		sm : new Ext.grid.RowSelectionModel( {}),
		listeners : {
			rowdblclick : function(roleView, index, e) {
				that.openTab()
			},
			rowclick : function(gridView, index, e) {
				if (gridView.getSelectionModel().getSelections().length > 1) {
					that.changeRoleSelections(gridView, index)
				}
			},
			rowselect : function(roleName) {
				that.setPreview(3);
				role.initRole(roleName)
			}
		}
	});
	this.roleGrid
			.getSelectionModel()
			.on(
					"rowselect",
					function(sm, index, record) {
						if (this.roleGrid.getSelectionModel().getSelections().length > 1) {
							that.selections = this.roleGrid.getSelectionModel()
									.getSelections();
							that.setPreview(0)
						} else {
							that.setPreview(3);
							this.roleGrid.fireEvent("rowselect",
									record.data.roleName)
						}
					}, this, {
						buffer : 250
					});
	this.preview = new Ext.Panel( {
		id : "ug-preview-panel",
		border : false,
		layout : "card",
		tbar : [ viewInNewTabTlbBtn ],
		items : [ {
			id : "start",
			html : "<br><br><br>",
			baseCls : "x-plain"
		}, user, group, role ],
		activeItem : 0
	});
	this.gridPanel = new Ext.Panel( {
		id : "ug-grid-panel",
		layout : "card",
		items : [ this.userGrid, this.groupGrid, this.roleGrid ],
		activeItem : 0,
		height : 300,
		region : "center"
	});
	var bootomPreview = new Ext.Panel( {
		id : "ug-bottom-preview-panel",
		layout : "fit",
		items : [ this.preview ],
		height : 300,
		region : "south"
	});
	var rightPreview = new Ext.Panel( {
		id : "ug-right-preview-panel",
		layout : "fit",
		region : "east",
		width : 400,
		hidden : true
	});
	var mainPanel = new Ext.Panel( {
		id : "ug-main-panel",
		title : "All Users".localize(),
		layout : "border",
		split : true,
		defaults : {
			split : true
		},
		items : [ this.gridPanel, bootomPreview, rightPreview ]
	});
	Jedox.studio.users.UGPanel.superclass.constructor
			.call(
					this,
					{
						id : "ug-panel",
						bodyStyle : "border-left:none; border-right:none; border-top:1; border-bottom:none;",
						resizeTabs : true,
						tabWidth : 150,
						minTabWidth : 120,
						enableTabScroll : true,
						items : [ mainPanel ],
						activeItem : 0
					})
};
Ext
		.extend(
				Jedox.studio.users.UGPanel,
				Ext.TabPanel,
				{
					movePreview : function(m, pressed) {
						if (!m) {
							var readMenu = Ext.menu.MenuMgr.get("reading-menu");
							readMenu.render();
							var items = readMenu.items.items;
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
							var preview = this.preview;
							var right = Ext.getCmp("ug-right-preview-panel");
							var bot = Ext.getCmp("ug-bottom-preview-panel");
							var uBtn = this.userGrid.getTopToolbar().items
									.get(7);
							var gBtn = this.groupGrid.getTopToolbar().items
									.get(7);
							switch (m.text) {
							case "Bottom".localize():
								right.hide();
								bot.add(preview);
								bot.show();
								bot.ownerCt.doLayout();
								uBtn.setIconClass("preview-bottom-icon");
								gBtn.setIconClass("preview-bottom-icon");
								break;
							case "Right".localize():
								bot.hide();
								right.add(preview);
								right.show();
								right.ownerCt.doLayout();
								uBtn.setIconClass("preview-right-icon");
								gBtn.setIconClass("preview-right-icon");
								break;
							case "Hide".localize():
								right.hide();
								bot.hide();
								this.doLayout();
								uBtn.setIconClass("preview-hide-icon");
								gBtn.setIconClass("preview-hide-icon");
								break
							}
						}
					},
					openTab : function() {
						switch (this.previewFlag) {
						case 1:
							this.openUserTab();
							break;
						case 2:
							this.openGroupTab();
							break;
						case 3:
							this.openRoleTab();
							break
						}
					},
					openTabs : function() {
						switch (this.getGridPanelActiveItem()) {
						case 1:
							var records = this.userGrid.getSelectionModel()
									.getSelections();
							for ( var i = 0; i < records.length; i++) {
								this.openUserTab(records[i])
							}
							break;
						case 2:
							var records = this.groupGrid.getSelectionModel()
									.getSelections();
							for ( var i = 0; i < records.length; i++) {
								this.openGroupTab(records[i])
							}
							break;
						case 3:
							var records = this.roleGrid.getSelectionModel()
									.getSelections();
							for ( var i = 0; i < records.length; i++) {
								this.openRoleTab(records[i])
							}
							break
						}
					},
					openUserTab : function(record) {
						var record = (record && record.data) ? record
								: this.userGrid.getSelectionModel()
										.getSelected();
						var user = record.data.userName;
						if (!(NWtab)) {
							var NWtab = new Ext.Panel(
									{
										id : user,
										title : user,
										layout : "fit",
										bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
										header : false,
										items : new Jedox.studio.users.User(
												"update"),
										closable : true,
										border : true
									});
							this.add(NWtab)
						}
						this.setActiveTab(NWtab);
						NWtab.getComponent(0).initUser(user)
					},
					openGroupTab : function(record) {
						var record = (record && record.data) ? record
								: this.groupGrid.getSelectionModel()
										.getSelected();
						var group = record.data.groupName;
						if (!(NWtab)) {
							var NWtab = new Ext.Panel(
									{
										id : group,
										title : group,
										layout : "fit",
										bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
										header : false,
										items : new Jedox.studio.users.Group(
												"update"),
										closable : true,
										border : true
									});
							this.add(NWtab)
						}
						this.setActiveTab(NWtab);
						NWtab.getComponent(0).initGroup(group)
					},
					openRoleTab : function(record) {
						var record = (record && record.data) ? record
								: this.roleGrid.getSelectionModel()
										.getSelected();
						var role = record.data.roleName;
						if (!(NWtab)) {
							var NWtab = new Ext.Panel(
									{
										id : role,
										title : role,
										layout : "fit",
										bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
										header : false,
										items : new Jedox.studio.users.Role(
												"update"),
										closable : true,
										border : true
									});
							this.add(NWtab)
						}
						this.setActiveTab(NWtab);
						NWtab.getComponent(0).initRole(role)
					},
					openSelectedUser : function() {
						this.initIterface();
						var record = this.userGrid.getSelectionModel()
								.getSelections();
						if (record.length === 1) {
							this.userGrid.fireEvent("rowselect",
									record[0].data.userName)
						} else {
							this.preview.layout.setActiveItem(0);
							this.preview.getTopToolbar().items.get(0).disable()
						}
					},
					initIterface : function() {
						this.closeAllTabs();
						this.setSelections()
					},
					openSelectedGroup : function() {
						this.closeAllTabs();
						var record = this.groupGrid.getSelectionModel()
								.getSelections();
						if (record.length === 1) {
							this.groupGrid.fireEvent("rowselect",
									record[0].data.groupName)
						} else {
							this.preview.layout.setActiveItem(0);
							this.preview.getTopToolbar().items.get(0).disable()
						}
					},
					openSelectedRole : function() {
						this.closeAllTabs();
						var record = this.roleGrid.getSelectionModel()
								.getSelections();
						if (record.length === 1) {
							this.roleGrid.fireEvent("rowselect",
									record[0].data.roleName)
						} else {
							this.preview.layout.setActiveItem(0);
							this.preview.getTopToolbar().items.get(0).disable()
						}
					},
					setPreview : function(index) {
						if (index === 0) {
							this.preview.getTopToolbar().items.get(0).disable()
						} else {
							this.preview.getTopToolbar().items.get(0).enable()
						}
						this.preview.layout.setActiveItem(index);
						this.preview.doLayout();
						this.previewFlag = index
					},
					formatData : function(i_data, order) {
						var o_data = [];
						for (key in i_data) {
							var record = [];
							record.push(key);
							for ( var i = 0, count = order.length; i < count; i++) {
								if (order[i] in i_data[key]) {
									record.push(i_data[key][order[i]])
								} else {
									record.push("unknown")
								}
							}
							o_data.push(record)
						}
						return o_data
					},
					initUserListData : function() {
						var that = this;
						var db = "System";
						var cube = "#_USER_USER_PROPERTIES";
						var order = [ "#_USER_" ];
						var props = [];
						var cords = {
							"#_USER_PROPERTIES_" : props
						};
						for ( var i = 0, items = this.userStore.fields.items, count = items.length; i < count; i++) {
							props.push(items[i]["name"])
						}
						props.shift();
						function cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database error".localize(),
										"Can not read data".localize());
								return
							}
							var userData = that.formatData(result, props);
							that.userStore.loadData(userData)
						}
						Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
								"paloGet", [ db, cube, order, cords ])
					},
					refreshUserList : function() {
						this.initUserListData();
						this.setPreview(0)
					},
					initGroupListData : function() {
						var that = this;
						var db = "System";
						var cube = "#_GROUP_GROUP_PROPERTIES";
						var order = [ "#_GROUP_" ];
						var props = [];
						var cords = {
							"#_GROUP_PROPERTIES_" : props
						};
						for ( var i = 0, items = this.groupStore.fields.items, count = items.length; i < count; i++) {
							props.push(items[i]["name"])
						}
						props.shift();
						function cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database read error".localize(),
										"Can not read data".localize());
								return
							}
							var groupData = that.formatData(result, props);
							that.groupStore.loadData(groupData)
						}
						Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
								"paloGet", [ db, cube, order, cords ])
					},
					refreshGroupList : function() {
						this.initGroupListData();
						this.setPreview(0)
					},
					initRoleListData : function() {
						var that = this;
						var db = "System";
						var cube = "#_ROLE_ROLE_PROPERTIES";
						var order = [ "#_ROLE_" ];
						var props = [ "name", "description", "status" ];
						var cords = {
							"#_ROLE_PROPERTIES_" : props
						};
						for ( var i = 0, items = this.roleStore.fields.items, count = items.length; i < count; i++) {
							props.push(items[i]["name"])
						}
						props.shift();
						function cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database read error".localize(),
										"Can not read data".localize());
								return
							}
							var roleData = that.formatData(result, props);
							that.roleStore.loadData(roleData)
						}
						Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
								"paloGet", [ db, cube, order, cords ])
					},
					refreshRoleList : function() {
						this.initRoleListData();
						this.setPreview(0)
					},
					addUser : function() {
						if (!NUtab) {
							var NUtab = new Ext.Panel(
									{
										id : "ug-add-user-panel",
										title : "Add User".localize(),
										layout : "fit",
										bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
										tabTip : "Add New User".localize(),
										items : new Jedox.studio.users.User(
												"add"),
										closable : true,
										border : true
									});
							this.add(NUtab)
						}
						this.setActiveTab(NUtab)
					},
					addGroup : function() {
						if (!NGtab) {
							var NGtab = new Ext.Panel(
									{
										id : "ug-add-group-panel",
										layout : "fit",
										bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
										title : "Add Group".localize(),
										tabTip : "Add New Group".localize(),
										items : new Jedox.studio.users.Group(
												"add"),
										closable : true,
										border : true
									});
							this.add(NGtab)
						}
						this.setActiveTab(NGtab)
					},
					addRole : function() {
						if (!NRtab) {
							var NRtab = new Ext.Panel(
									{
										id : "ug-add-role-panel",
										layout : "fit",
										bodyStyle : "border-left:1; border-right:1; border-top:none; border-bottom:1;",
										title : "Add Role".localize(),
										tabTip : "Add New Role".localize(),
										items : new Jedox.studio.users.Role(
												"add"),
										closable : true,
										border : true
									});
							this.add(NRtab)
						}
						this.setActiveTab(NRtab)
					},
					removeUser : function() {
						var that = this;
						var record = this.userGrid.getSelectionModel()
								.getSelected();
						var user = record.data.userName;
						var title = "Remove User".localize();
						var msg = "You are about to remove <b>"
								+ user
								+ "</b> user. <br>Are you sure you want do this?";
						var fn = function() {
							var db = "System";
							var dim = "#_USER_";
							var cords = [ user ];
							function cb(result) {
								that.userStore.remove(record);
								that.setPreview(0);
								Jedox.studio.app
										.showTopMsg("",
												"User removed successefully"
														.localize())
							}
							Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
									"paloRemove", [ db, dim, cords ])
						};
						Jedox.studio.app.showMessageQUESTION(title, msg, fn)
					},
					removeGroup : function() {
						var that = this;
						var record = this.groupGrid.getSelectionModel()
								.getSelected();
						var group = record.data.groupName;
						var title = "Remove Group".localize();
						var msg = "You are about to remove <b>"
								+ group
								+ "</b> group. <br>Are you sure you want do this?";
						var fn = function() {
							var db = "System";
							var dim = "#_GROUP_";
							var cords = [ group ];
							function cb(result) {
								that.groupStore.remove(record);
								that.setPreview(0);
								Jedox.studio.app.showTopMsg("",
										"Group removed successefully"
												.localize())
							}
							Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
									"paloRemove", [ db, dim, cords ])
						};
						Jedox.studio.app.showMessageQUESTION(title, msg, fn)
					},
					removeRole : function() {
						var that = this;
						var record = this.roleGrid.getSelectionModel()
								.getSelected();
						var role = record.data.roleName;
						var title = "Remove Role".localize();
						var msg = "You are about to remove <b>"
								+ role
								+ "</b> role. <br>Are you sure you want do this?";
						var fn = function() {
							var db = "System";
							var dim = "#_ROLE_";
							var cords = [ role ];
							function cb(result) {
								that.roleStore.remove(record);
								that.setPreview(0);
								Jedox.studio.app
										.showTopMsg("",
												"Role removed successefully"
														.localize())
							}
							Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
									"paloRemove", [ db, dim, cords ])
						};
						Jedox.studio.app.showMessageQUESTION(title, msg, fn)
					},
					openAllUsers : function() {
						this.beginUpdate();
						this.userGrid.store.each(this.openUserTab, this);
						this.endUpdate()
					},
					openAllGroups : function() {
						this.groupGrid.store.each(this.openGroupTab, this)
					},
					getGridPanelActiveItem : function() {
						if (this.gridPanel.layout.activeItem.id === "ug-user-node-panel") {
							return 1
						} else {
							if (this.gridPanel.layout.activeItem.id === "ug-group-node-panel") {
								return 2
							} else {
								return 3
							}
						}
					},
					selectAll : function() {
						switch (this.getGridPanelActiveItem()) {
						case 1:
							this.userGrid.getSelectionModel().selectAll();
							break;
						case 2:
							this.groupGrid.getSelectionModel().selectAll();
							break;
						case 3:
							this.roleGrid.getSelectionModel().selectAll();
							break
						}
					},
					setSelections : function() {
						switch (this.getGridPanelActiveItem()) {
						case 1:
							this.selections = this.userGrid.getSelectionModel()
									.getSelections();
							break;
						case 2:
							this.selections = this.groupGrid
									.getSelectionModel().getSelections();
							break;
						case 3:
							this.selections = this.roleGrid.getSelectionModel()
									.getSelections();
							break
						}
					},
					changeUserSelections : function(grid, index) {
						var record = grid.getStore().getAt(index);
						if (this.selections) {
							for ( var i = 0; i < this.selections.length; i++) {
								if (this.selections[i].data.userName === record.data.userName) {
									this.clearSelections();
									this.selections = null;
									grid.getSelectionModel().selectRow(index);
									break
								}
							}
						}
					},
					changeGroupSelections : function(grid, index) {
						var record = grid.getStore().getAt(index);
						if (this.selections) {
							for ( var i = 0; i < this.selections.length; i++) {
								if (this.selections[i].data.groupName === record.data.groupName) {
									this.clearSelections();
									this.selections = null;
									grid.getSelectionModel().selectRow(index);
									break
								}
							}
						}
					},
					changeRoleSelections : function(grid, index) {
						var record = grid.getStore().getAt(index);
						if (this.selections) {
							for ( var i = 0; i < this.selections.length; i++) {
								if (this.selections[i].data.roleName === record.data.roleName) {
									this.clearSelections();
									this.selections = null;
									grid.getSelectionModel().selectRow(index);
									break
								}
							}
						}
					},
					clearSelections : function() {
						switch (this.getGridPanelActiveItem()) {
						case 1:
							this.userGrid.getSelectionModel().clearSelections();
							break;
						case 2:
							this.groupGrid.getSelectionModel()
									.clearSelections();
							break;
						case 3:
							this.roleGrid.getSelectionModel().clearSelections();
							break
						}
						this.setPreview(0)
					},
					closeAllTabs : function() {
						var tabsNmb = this.items.length;
						for ( var i = 1; i < tabsNmb; i++) {
							this.remove(this.getComponent(1))
						}
					},
					isError : function(result) {
						if (result[0] === "!" && result.length === 2) {
							return true
						}
						return false
					}
				});
Jedox.studio.users.User = function(addFlag) {
	var that = this;
	this.addFlag = addFlag;
	this.userName = "";
	var prefs_level = {
		SERVER : 0,
		GROUP : 1,
		USER : 2
	};
	this.availableGroupsStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "groupName"
		} ]
	});
	this.userGroupsStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "groupName"
		} ]
	});
	this.generalTabLbl = {
		html : "General informations about user".localize() + "...<br><br><br>",
		baseCls : "x-plain",
		bodyStyle : "font-size:11;"
	};
	this.userNameTxf = new Ext.form.TextField( {
		fieldLabel : "User Name".localize(),
		labelStyle : "font-size:11px;",
		cls : "preview-properties-panel",
		name : "userName",
		allowBlank : false,
		width : 200
	});
	this.fullNameTxf = new Ext.form.TextField( {
		fieldLabel : "Full Name".localize(),
		labelStyle : "font-size:11px;",
		cls : "preview-properties-panel",
		name : "fullName",
		width : 200
	});
	this.descriptionTxa = new Ext.form.TextArea( {
		fieldLabel : "Description".localize(),
		labelStyle : "font-size:11px;",
		cls : "preview-properties-panel",
		name : "description",
		width : 200
	});
	this.splitLine = {
		html : "<br><br>",
		baseCls : "split-line",
		width : 315
	};
	this.passwordTxf = new Ext.form.TextField( {
		fieldLabel : "Password".localize(),
		labelStyle : "font-size:11px;",
		cls : "preview-properties-panel",
		name : "password",
		width : 200,
		baseCls : "x-plain"
	});
	this.optionsBtn = new Ext.Button( {
		text : "Options".localize(),
		hidden : addFlag == "add",
		handler : function() {
			Jedox.studio.app.preferences(prefs_level.USER, that.userNameTxf
					.getValue())
		}
	});
	var optionsBtnContainer = {
		layout : "form",
		baseCls : "x-plain",
		width : 315,
		buttonAlign : "right",
		buttons : this.optionsBtn
	};
	this.accountStatusChb = new Ext.form.Checkbox( {
		labelStyle : "font-size:11px;",
		hidden : true
	});
	this.memberOfTabLbl = {
		html : "Choose the group you want to join user".localize()
				+ "<br><br><br>",
		baseCls : "x-plain",
		bodyStyle : "font-size:11;"
	}, this.memeberOfIS = {
		xtype : "itemselector",
		name : "itemselector",
		fieldLabel : "ItemSelector",
		cls : "preview-properties-panel",
		hideLabel : true,
		fromStore : this.availableGroupsStore,
		dataFields : [ "groupName" ],
		toStore : this.userGroupsStore,
		drawUpIcon : false,
		drawDownIcon : false,
		drawTopIcon : false,
		drawBotIcon : false,
		msWidth : 150,
		msHeight : 200,
		displayField : "groupName",
		imagePath : "../../../lib/ext/extensions/multiselect",
		toLegend : "Member Of".localize(),
		fromLegend : "Available Groups".localize()
	};
	this.tabPanel = new Ext.TabPanel(
			{
				activeTab : 0,
				width : 400,
				height : 400,
				bodyStyle : "border-left:none; border-right:none; border-top:1; border-bottom:1;",
				enableTabScroll : true,
				plain : true,
				defaults : {
					autoScroll : true,
					bodyStyle : "padding:10px",
					layout : "form"
				},
				items : [
						{
							title : "General".localize(),
							items : [ this.generalTabLbl, this.userNameTxf,
									this.fullNameTxf, this.descriptionTxa,
									this.splitLine, this.passwordTxf,
									this.passwordTxf, this.splitLine,
									optionsBtnContainer, this.accountStatusChb,
									{
										html : "<br><br>",
										baseCls : "x-plain"
									} ]
						}, {
							title : "Member Of".localize(),
							items : [ this.memberOfTabLbl, this.memeberOfIS ]
						} ]
			});
	Jedox.studio.users.User.superclass.constructor.call(this, {
		labelAlign : "right",
		border : false,
		bodyStyle : "padding:5px",
		cls : "preview-properties-panel",
		layout : "fit",
		items : [ this.tabPanel ],
		listeners : {
			show : function() {
			}
		},
		buttons : [ {
			text : "Save".localize(),
			handler : this.onSave,
			scope : this
		}, {
			text : "Cancel".localize(),
			handler : this.onCancel,
			scope : this
		} ]
	});
	if (this.addFlag === "add") {
		this.initMemberOf()
	}
};
Ext
		.extend(
				Jedox.studio.users.User,
				Ext.Panel,
				{
					initUser : function(userName) {
						var that = this;
						this.userName = userName;
						var db = "System";
						var cube = "#_USER_USER_PROPERTIES";
						var order = [ "#_USER_" ];
						var props = [ "userName", "fullName", "description",
								"password", "accountStatus" ];
						var cords = {
							"#_USER_" : [ userName ],
							"#_USER_PROPERTIES_" : props
						};
						props.shift();
						function cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database error".localize(),
										"Can not read data".localize());
								return
							}
							that.userNameTxf.setValue(that
									.getPropsValue(userName));
							that.fullNameTxf
									.setValue(that
											.getPropsValue(result[userName]["fullName"]));
							that.descriptionTxa
									.setValue(that
											.getPropsValue(result[userName]["description"]));
							that.passwordTxf
									.setValue(that
											.getPropsValue(result[userName]["password"]));
							that.accountStatusChb
									.setValue(that
											.getPropsValue(result[userName]["accountStatus"]));
							if (that.userName == "admin"
									&& Jedox.studio.users.internalConnection) {
								that.passwordTxf.disable()
							} else {
								that.passwordTxf.enable()
							}
							that.initMemberOf()
						}
						Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
								"paloGet", [ db, cube, order, cords ])
					},
					getPropsValue : function(value) {
						if (value != undefined) {
							return value
						}
						return ""
					},
					initMemberOf : function() {
						var that = this;
						var db = "System";
						var cube = "#_USER_GROUP";
						var order = [ "#_USER_" ];
						var cords = {
							"#_USER_" : [ that.userName ]
						};
						function cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database error".localize(),
										"Can not read data".localize());
								return
							}
							var availableGroupsData = [];
							var userGroupsData = [];
							for (user in result) {
								for (group in result[user]) {
									if (result[user][group] == "1") {
										userGroupsData.push( [ group ])
									} else {
										availableGroupsData.push( [ group ])
									}
								}
							}
							that.availableGroupsStore
									.loadData(availableGroupsData);
							that.userGroupsStore.loadData(userGroupsData)
						}
						Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
								"paloGet", [ db, cube, order, cords ])
					},
					addUser : function(addF) {
						var that = this;
						if (this.validateForm()) {
							var db = "System";
							var cube = "#_USER_USER_PROPERTIES";
							var data = {};
							var order = [ "#_USER_" ];
							var add = addF;
							var userName = addF ? this.userNameTxf.getValue()
									: this.userName;
							var fullName = this.fullNameTxf.getValue();
							var description = this.descriptionTxa.getValue();
							var password = this.passwordTxf.getValue();
							var status = this.accountStatusChb.getValue() ? "1"
									: "0";
							data[userName] = {
								fullName : fullName,
								description : description,
								password : password,
								accountStatus : status
							};
							function cb(result) {
								if (!result) {
									Jedox.studio.app.showMessageERROR(
											"Database error".localize(),
											"Can not write data".localize());
									return
								}
								var ug_cube = "#_USER_GROUP";
								var ug_data = {};
								var ug_order = [ "#_USER_" ];
								ug_data[userName] = that.getUserGroups();
								function ug_cb(result) {
									if (!result) {
										Jedox.studio.app
												.showMessageERROR(
														"Database error"
																.localize(),
														"Can not write data"
																.localize());
										return
									}
									if (!addF
											&& userName != that.userNameTxf
													.getValue()) {
										var r_db = "System";
										var r_dim = "#_USER_";
										var r_data = {};
										var newUserName = that.userNameTxf
												.getValue();
										r_data[userName] = newUserName;
										function r_cb(result) {
											if (!result) {
												Jedox.studio.app
														.showMessageERROR(
																"Database error"
																		.localize(),
																"Can not write data"
																		.localize());
												return
											}
											that.onClose()
										}
										Jedox.wss.backend.conn.rpc( [ this,
												r_cb ], "common", "paloRename",
												[ r_db, r_dim, r_data ])
									} else {
										that.onClose()
									}
								}
								Jedox.wss.backend.conn.rpc( [ this, ug_cb ],
										"common", "paloSet", [ db, ug_cube,
												ug_data, ug_order, add ])
							}
							Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
									"paloSet", [ db, cube, data, order, add ])
						}
					},
					getUserGroups : function() {
						var data = {};
						for ( var i = 0, agNmbr = this.memeberOfIS.fromStore
								.getCount(); i < agNmbr; i++) {
							data[this.memeberOfIS.fromStore.getAt(i).get(
									"groupName")] = ""
						}
						for ( var i = 0, ugNmbr = this.memeberOfIS.toStore
								.getCount(); i < ugNmbr; i++) {
							data[this.memeberOfIS.toStore.getAt(i).get(
									"groupName")] = "1"
						}
						return data
					},
					isError : function(result) {
						if (result[0] === "!" && result.length === 2) {
							return true
						}
						return false
					},
					validateForm : function() {
						var that = this;
						var _return = true;
						function userName() {
							var msg = "Sign in name must start with a-z/A-Z character and must contain more than two character"
									.localize()
									+ "...";
							var my_regexp = /^[a-zA-Z][a-zA-Z0-9_\-@\.]+$/;
							var value = that.userNameTxf.getValue();
							if (!my_regexp.test(value)) {
								that.userNameTxf.markInvalid(msg);
								_return = false
							}
						}
						function fullName() {
							var msg = "Full name must start with a-z/A-Z character"
									.localize()
									+ "...";
							var my_regexp = /^[a-zA-Z]+[\ ]*[a-zA-Z0-9\ ]*$/;
							var value = that.fullNameTxf.getValue();
							if (!my_regexp.test(value)) {
								that.fullNameTxf.markInvalid(msg);
								_return = false
							}
						}
						function password() {
							var msg = "Password must start with a-z/A-Z character and must contain 6 char min"
									.localize()
									+ "...";
							var my_regexp = /^[a-zA-Z]([a-zA-Z0-9#_-]{4,})$/;
							var value = that.passwordTxf.getValue();
							if (!my_regexp.test(value)) {
								that.passwordTxf.markInvalid(msg);
								_return = false
							}
						}
						userName();
						fullName();
						password();
						return _return
					},
					onSave : function() {
						if (this.addFlag === "add") {
							this.addUser(true)
						} else {
							this.addUser(false)
						}
					},
					onCancel : function() {
						if (this.addFlag) {
							this.ownerCt.ownerCt.refreshUserList();
							this.ownerCt.ownerCt.remove(this.ownerCt)
						} else {
							this.initUser(this.userName)
						}
					},
					onClose : function() {
						if (this.addFlag) {
							this.ownerCt.ownerCt.refreshUserList();
							this.ownerCt.ownerCt.remove(this.ownerCt);
							if (this.addFlag == "add") {
								Jedox.studio.app.showTopMsg("",
										"User added successefully".localize())
							} else {
								Jedox.studio.app
										.showTopMsg("",
												"User updated successefully"
														.localize())
							}
						} else {
							this.ownerCt.ownerCt.ownerCt.ownerCt
									.refreshUserList();
							Jedox.studio.app.showTopMsg("",
									"User updated successefully".localize())
						}
					}
				});
Jedox.studio.users.Group = function(addFlag) {
	var that = this;
	this.addFlag = addFlag;
	this.groupName = "";
	var prefs_level = {
		SERVER : 0,
		GROUP : 1,
		USER : 2
	};
	this.availableUsersStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "userName"
		} ]
	});
	this.groupUsersStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "userName"
		} ]
	});
	this.availableRolesStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "roleName"
		} ]
	});
	this.groupRolesStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "roleName"
		} ]
	});
	this.generalTabLbl = {
		html : "Enter the General informations about Group".localize()
				+ "...<br><br><br>",
		baseCls : "x-plain",
		bodyStyle : "font-size:11;"
	};
	this.groupNameTxf = new Ext.form.TextField( {
		fieldLabel : "Group Name".localize(),
		labelStyle : "font-size:11px;",
		cls : "preview-properties-panel",
		allowBlank : false,
		width : 200
	});
	this.descriptionTxa = new Ext.form.TextArea( {
		fieldLabel : "Description".localize(),
		labelStyle : "font-size:11px;",
		cls : "preview-properties-panel",
		name : "description",
		width : 200
	});
	this.splitLine = {
		html : "<br><br>",
		baseCls : "split-line",
		width : 315
	};
	this.optionsBtn = new Ext.Button( {
		text : "Options".localize(),
		hidden : addFlag == "add",
		handler : function() {
			Jedox.studio.app.preferences(prefs_level.GROUP, that.groupNameTxf
					.getValue())
		}
	});
	var optionsBtnContainer = {
		layout : "form",
		baseCls : "x-plain",
		width : 315,
		buttonAlign : "right",
		buttons : this.optionsBtn
	};
	this.accountStatusChb = new Ext.form.Checkbox( {
		labelStyle : "font-size:11px;",
		hidden : true
	});
	this.memberOfTabLbl = {
		html : "Choose the users you want to asign to this group".localize()
				+ "<br><br><br>",
		baseCls : "x-plain",
		bodyStyle : "font-size:11;"
	};
	this.memberOfRTabLbl = {
		html : "Choose the roles you want to asign to this group".localize()
				+ "<br><br><br>",
		baseCls : "x-plain",
		bodyStyle : "font-size:11;"
	};
	this.memeberOfIS = {
		xtype : "itemselector",
		name : "itemselector",
		fieldLabel : "ItemSelector",
		cls : "preview-properties-panel",
		hideLabel : true,
		fromStore : this.availableUsersStore,
		dataFields : [ "userName" ],
		toStore : this.groupUsersStore,
		drawUpIcon : false,
		drawDownIcon : false,
		drawTopIcon : false,
		drawBotIcon : false,
		msWidth : 150,
		msHeight : 200,
		displayField : "userName",
		imagePath : "../../../lib/ext/extensions/multiselect",
		toLegend : "Member Of".localize(),
		fromLegend : "Available Users".localize()
	};
	this.memeberOfRIS = {
		xtype : "itemselector",
		name : "itemselector",
		fieldLabel : "ItemSelector",
		cls : "preview-properties-panel",
		hideLabel : true,
		fromStore : this.availableRolesStore,
		dataFields : [ "roleName" ],
		toStore : this.groupRolesStore,
		drawUpIcon : false,
		drawDownIcon : false,
		drawTopIcon : false,
		drawBotIcon : false,
		msWidth : 150,
		msHeight : 200,
		displayField : "roleName",
		imagePath : "../../../lib/ext/extensions/multiselect",
		toLegend : "Member Of".localize(),
		fromLegend : "Available Roles".localize()
	};
	this.tabPanel = new Ext.TabPanel(
			{
				activeTab : 0,
				width : 400,
				height : 350,
				bodyStyle : "border-left:none; border-right:none; border-top:1; border-bottom:1;",
				enableTabScroll : true,
				plain : true,
				defaults : {
					autoScroll : true,
					bodyStyle : "padding:10px",
					layout : "form"
				},
				items : [
						{
							title : "General".localize(),
							items : [ this.generalTabLbl, this.groupNameTxf,
									this.descriptionTxa, this.splitLine,
									optionsBtnContainer, this.accountStatusChb ]
						}, {
							title : "Member Of".localize(),
							items : [ this.memberOfTabLbl, this.memeberOfIS ]
						}, {
							title : "Member Of Role".localize(),
							items : [ this.memberOfRTabLbl, this.memeberOfRIS ]
						} ]
			});
	Jedox.studio.users.Group.superclass.constructor.call(this, {
		labelAlign : "right",
		border : false,
		bodyStyle : "padding:5px",
		cls : "preview-properties-panel",
		layout : "fit",
		items : [ this.tabPanel ],
		buttons : [ {
			text : "Save".localize(),
			handler : this.onSave,
			scope : this
		}, {
			text : "Cancel".localize(),
			handler : this.onCancel,
			scope : this
		} ]
	});
	if (this.addFlag === "add") {
		this.initMemberOf()
	}
};
Ext
		.extend(
				Jedox.studio.users.Group,
				Ext.Panel,
				{
					initGroup : function(groupName) {
						var that = this;
						this.groupName = groupName;
						var db = "System";
						var cube = "#_GROUP_GROUP_PROPERTIES";
						var order = [ "#_GROUP_" ];
						var props = [ "groupName", "description",
								"accountStatus" ];
						var cords = {
							"#_GROUP_" : [ groupName ],
							"#_GROUP_PROPERTIES_" : props
						};
						props.shift();
						function cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database error".localize(),
										"Can not read data".localize());
								return
							}
							that.groupNameTxf.setValue(that
									.getPropsValue(groupName));
							that.descriptionTxa
									.setValue(that
											.getPropsValue(result[groupName]["description"]));
							that.accountStatusChb
									.setValue(that
											.getPropsValue(result[groupName]["accountStatus"]));
							that.initMemberOf()
						}
						Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
								"paloGet", [ db, cube, order, cords ])
					},
					getPropsValue : function(value) {
						if (value != undefined) {
							return value
						}
						return ""
					},
					initMemberOf : function() {
						var that = this;
						var db = "System";
						var ug_cube = "#_USER_GROUP";
						var ug_order = [ "#_GROUP_" ];
						var ug_cords = {
							"#_GROUP_" : [ that.groupName ]
						};
						function ug_cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database error".localize(),
										"Can not read data".localize());
								return
							}
							var availableUsersData = [];
							var groupUsersData = [];
							for (group in result) {
								for (user in result[group]) {
									if (result[group][user] == "1") {
										groupUsersData.push( [ user ])
									} else {
										availableUsersData.push( [ user ])
									}
								}
							}
							that.availableUsersStore
									.loadData(availableUsersData);
							that.groupUsersStore.loadData(groupUsersData)
						}
						Jedox.wss.backend.conn.rpc( [ this, ug_cb ], "common",
								"paloGet", [ db, ug_cube, ug_order, ug_cords ]);
						var gr_cube = "#_GROUP_ROLE";
						var gr_order = [ "#_GROUP_" ];
						var gr_cords = {
							"#_GROUP_" : [ that.groupName ]
						};
						function gr_cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database error".localize(),
										"Can not read data".localize());
								return
							}
							var availableRolesData = [];
							var groupRolesData = [];
							for (group in result) {
								for (role in result[group]) {
									if (result[group][role] == "1") {
										groupRolesData.push( [ role ])
									} else {
										availableRolesData.push( [ role ])
									}
								}
							}
							that.availableRolesStore
									.loadData(availableRolesData);
							that.groupRolesStore.loadData(groupRolesData)
						}
						Jedox.wss.backend.conn.rpc( [ this, gr_cb ], "common",
								"paloGet", [ db, gr_cube, gr_order, gr_cords ])
					},
					addGroup : function(addF) {
						var that = this;
						if (this.validateForm()) {
							var db = "System";
							var gg_cube = "#_GROUP_GROUP_PROPERTIES";
							var gg_data = {};
							var gg_order = [ "#_GROUP_" ];
							var add = addF;
							var groupName = addF ? this.groupNameTxf.getValue()
									: this.groupName;
							var description = this.descriptionTxa.getValue();
							var status = this.accountStatusChb.getValue() ? "1"
									: "0";
							gg_data[groupName] = {
								description : description,
								accountStatus : status
							};
							function gg_cb(result) {
								if (!result) {
									Jedox.studio.app.showMessageERROR(
											"Database error".localize(),
											"Can not write data".localize());
									return
								}
								var ug_cube = "#_USER_GROUP";
								var ug_data = {};
								var ug_order = [ "#_GROUP_" ];
								ug_data[groupName] = that.getGroupUsers();
								function ug_cb(result) {
									if (!result) {
										Jedox.studio.app
												.showMessageERROR(
														"Database error"
																.localize(),
														"Can not write data"
																.localize());
										return
									}
									var gr_cube = "#_GROUP_ROLE";
									var gr_data = {};
									var gr_order = [ "#_GROUP_" ];
									gr_data[groupName] = that.getGroupRoles();
									function gr_cb(result) {
										if (!result) {
											Jedox.studio.app
													.showMessageERROR(
															"Database error"
																	.localize(),
															"Can not write data"
																	.localize());
											return
										}
										if (!addF
												&& groupName != that.groupNameTxf
														.getValue()) {
											var r_db = "System";
											var r_dim = "#_GROUP_";
											var r_data = {};
											var newGroupName = that.groupNameTxf
													.getValue();
											r_data[groupName] = newGroupName;
											function r_cb(result) {
												if (!result) {
													Jedox.studio.app
															.showMessageERROR(
																	"Database error"
																			.localize(),
																	"Can not write data"
																			.localize());
													return
												}
												that.onClose()
											}
											Jedox.wss.backend.conn.rpc( [ this,
													r_cb ], "common",
													"paloRename", [ r_db,
															r_dim, r_data ])
										} else {
											that.onClose()
										}
									}
									Jedox.wss.backend.conn.rpc(
											[ this, gr_cb ], "common",
											"paloSet", [ db, gr_cube, gr_data,
													gr_order, add ])
								}
								Jedox.wss.backend.conn.rpc( [ this, ug_cb ],
										"common", "paloSet", [ db, ug_cube,
												ug_data, ug_order, add ])
							}
							Jedox.wss.backend.conn.rpc( [ this, gg_cb ],
									"common", "paloSet", [ db, gg_cube,
											gg_data, gg_order, add ])
						}
					},
					getGroupUsers : function() {
						var data = {};
						for ( var i = 0, auNmbr = this.memeberOfIS.fromStore
								.getCount(); i < auNmbr; i++) {
							data[this.memeberOfIS.fromStore.getAt(i).get(
									"userName")] = ""
						}
						for ( var i = 0, guNmbr = this.memeberOfIS.toStore
								.getCount(); i < guNmbr; i++) {
							data[this.memeberOfIS.toStore.getAt(i).get(
									"userName")] = "1"
						}
						return data
					},
					getGroupRoles : function() {
						var data = {};
						for ( var i = 0, auNmbr = this.memeberOfRIS.fromStore
								.getCount(); i < auNmbr; i++) {
							data[this.memeberOfRIS.fromStore.getAt(i).get(
									"roleName")] = ""
						}
						for ( var i = 0, guNmbr = this.memeberOfRIS.toStore
								.getCount(); i < guNmbr; i++) {
							data[this.memeberOfRIS.toStore.getAt(i).get(
									"roleName")] = "1"
						}
						return data
					},
					isError : function(result) {
						if (result[0] === "!" && result.length === 2) {
							return true
						}
						return false
					},
					validateForm : function() {
						var that = this;
						var _return = true;
						function groupName() {
							var msg = "Group name must start with a-z/A-Z character and must contain at least two character"
									.localize();
							var my_regexp = /^[a-zA-Z][a-zA-Z0-9_\-@\.]+$/;
							var value = that.groupNameTxf.getValue();
							if (!my_regexp.test(value)) {
								that.groupNameTxf.markInvalid(msg);
								_return = false
							}
						}
						groupName();
						return _return
					},
					onSave : function() {
						if (this.addFlag === "add") {
							this.addGroup(true)
						} else {
							this.addGroup(false)
						}
					},
					onCancel : function() {
						if (this.addFlag) {
							this.ownerCt.ownerCt.refreshGroupList();
							this.ownerCt.ownerCt.remove(this.ownerCt)
						} else {
							this.initGroup(this.groupName)
						}
					},
					onClose : function() {
						if (this.addFlag) {
							this.ownerCt.ownerCt.refreshGroupList();
							this.ownerCt.ownerCt.remove(this.ownerCt);
							if (this.addFlag == "add") {
								Jedox.studio.app.showTopMsg("",
										"Group added successefully".localize())
							} else {
								Jedox.studio.app.showTopMsg("",
										"Group updated successefully"
												.localize())
							}
						} else {
							this.ownerCt.ownerCt.ownerCt.ownerCt
									.refreshGroupList();
							Jedox.studio.app.showTopMsg("",
									"Group updated successefully".localize())
						}
					}
				});
Jedox.studio.users.Role = function(addFlag) {
	this.addFlag = addFlag;
	this.roleName = "";
	var that = this;
	this.permKey = {
		D : "Full Access".localize(),
		W : "Writable".localize(),
		R : "Read Only".localize(),
		N : "No Access".localize(),
		S : "Splash".localize()
	};
	this.permData = [ [ "S", "Splash".localize() ],
			[ "D", "Full Access".localize() ], [ "W", "Writable".localize() ],
			[ "R", "Read Only".localize() ], [ "N", "No Access".localize() ] ];
	this.permStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "perm"
		}, {
			name : "permD"
		} ]
	});
	this.permStore.loadData(this.permData);
	this.rightObjReg = {
		user : {
			desc : "robj_desc_user".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		password : {
			desc : "robj_desc_password".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		group : {
			desc : "robj_desc_group".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		database : {
			desc : "robj_desc_database".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		cube : {
			desc : "robj_desc_cube".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		dimension : {
			desc : "robj_desc_dimension".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		"dimension element" : {
			desc : "robj_desc_dimElements".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		"cell data" : {
			desc : "robj_desc_cellData".localize(),
			defPerm : "S",
			allowedPerms : [ "S", "D", "W", "R", "N" ]
		},
		rights : {
			desc : "robj_desc_rights".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		"system operations" : {
			desc : "robj_desc_sysOperations".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		"event processor" : {
			desc : "robj_desc_eventProc".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		"sub-set view" : {
			desc : "robj_desc_subsetView".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		"user info" : {
			desc : "robj_desc_userInfo".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		rule : {
			desc : "robj_desc_rule".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		ste_reports : {
			desc : "robj_desc_report".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		ste_files : {
			desc : "robj_desc_file".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		ste_palo : {
			desc : "robj_desc_olap".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		ste_users : {
			desc : "robj_desc_userManager".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		ste_etl : {
			desc : "robj_desc_etl".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		},
		ste_conns : {
			desc : "robj_desc_connection".localize(),
			defPerm : "D",
			allowedPerms : [ "D", "W", "R", "N" ]
		}
	};
	this.generalTabLbl = {
		html : "Enter the General informations about Role".localize()
				+ "...<br><br><br>",
		baseCls : "x-plain"
	};
	this.availableGroupStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "groupName"
		} ]
	});
	this.roleGroupStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "groupName"
		} ]
	});
	this.roleNameTxf = new Ext.form.TextField( {
		fieldLabel : "Role Name".localize(),
		cls : "preview-properties-panel",
		allowBlank : false,
		width : 200
	});
	this.descriptionTxa = new Ext.form.TextArea( {
		fieldLabel : "Description".localize(),
		cls : "preview-properties-panel",
		name : "description",
		width : 200
	});
	this.statusCB = new Ext.form.Checkbox( {
		checked : true,
		hidden : true
	});
	this.memberOfTabLbl = {
		html : "Choose the groups you want to asign to this role".localize()
				+ "<br><br><br>",
		baseCls : "x-plain"
	};
	this.memeberOfIS = {
		xtype : "itemselector",
		name : "itemselector",
		fieldLabel : "ItemSelector",
		cls : "preview-properties-panel",
		hideLabel : true,
		fromStore : this.availableGroupStore,
		dataFields : [ "groupName" ],
		toStore : this.roleGroupStore,
		drawUpIcon : false,
		drawDownIcon : false,
		drawTopIcon : false,
		drawBotIcon : false,
		msWidth : 150,
		msHeight : 200,
		displayField : "groupName",
		imagePath : "../../../lib/ext/extensions/multiselect",
		toLegend : "Role Members".localize(),
		fromLegend : "Available Groups".localize()
	};
	this.rightsGridStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "rightObj"
		}, {
			name : "rightDesc"
		}, {
			name : "permission"
		}, {
			name : "permH"
		} ],
		listeners : {
			update : function(st, rec, op) {
				rec.set("permH", that.newPerm);
				that.newPerm = false
			}
		}
	});
	this.editor = new Ext.ux.grid.RowEditor( {
		saveText : "Update".localize(),
		id : "gridRowEditorUsers",
		style : {
			"z-index" : 7000,
			"font-size" : "11px"
		},
		listeners : {
			afteredit : function(el, changed) {
			},
			beforeedit : function(e, f, m, t) {
			}
		}
	});
	Ext.override(Ext.grid.GridView, {
		getEditorParent : function() {
			return document.body
		}
	});
	this.rowIndex = 0;
	this.newPerm = false;
	this.assignedRightsGrid = new Ext.grid.EditorGridPanel( {
		store : this.rightsGridStore,
		autoWidth : true,
		autoHeight : true,
		id : "assignedRightsGrid",
		ctCls : "tplMap-Grid",
		border : true,
		frame : true,
		plugins : [ this.editor ],
		stripeRows : false,
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : false,
			moveEditorOnEnter : false
		}),
		enableHdMenu : false,
		viewConfig : {
			forceFit : true
		},
		baseCls : "x-plain",
		columns : [ {
			id : "rightObject",
			header : "Object name".localize(),
			width : 180,
			sortable : false,
			dataIndex : "rightObj"
		}, {
			id : "rightDescription",
			header : "Object description".localize(),
			width : 180,
			sortable : false,
			dataIndex : "rightDesc"
		}, {
			id : "objPerm",
			header : "Object permission".localize(),
			width : 220,
			sortable : false,
			dataIndex : "permission",
			editor : new Ext.form.ComboBox( {
				typeAhead : false,
				triggerAction : "all",
				id : "permComboEditor",
				lazyRender : true,
				editable : false,
				store : this.permStore,
				mode : "local",
				displayField : "permD",
				listeners : {
					select : function(el, rec, index) {
						that.newPerm = rec.data.perm
					}
				}
			})
		} ],
		listeners : {
			rowclick : function(grid, index, e) {
				that.rowIndex = index;
				var robj = grid.getSelectionModel().getSelected().data;
				var aPerms = that.rightObjReg[robj.rightObj].allowedPerms;
				that.permData = [];
				for ( var i = 0; i < aPerms.length; i++) {
					that.permData.push( [ aPerms[i], that.permKey[aPerms[i]] ])
				}
				that.permStore.loadData(that.permData)
			}
		}
	});
	this.tabPanel = new Ext.TabPanel(
			{
				activeTab : 0,
				width : 400,
				height : 350,
				bodyStyle : "border-left:none; border-right:none; border-top:1; border-bottom:1;",
				enableTabScroll : true,
				plain : true,
				defaults : {
					autoScroll : true,
					bodyStyle : "padding:10px",
					layout : "form"
				},
				items : [
						{
							title : "General".localize(),
							items : [ this.generalTabLbl, this.roleNameTxf,
									this.descriptionTxa, this.statusCB ]
						}, {
							title : "Role Members".localize(),
							items : [ this.memberOfTabLbl, this.memeberOfIS ]
						}, {
							title : "Assigned rights".localize(),
							items : [ this.assignedRightsGrid ]
						} ]
			});
	Jedox.studio.users.Role.superclass.constructor.call(this, {
		labelAlign : "right",
		border : false,
		bodyStyle : "padding:5px",
		cls : "preview-properties-panel",
		layout : "fit",
		items : [ this.tabPanel ],
		buttons : [ {
			text : "Save".localize(),
			handler : this.onSave,
			scope : this
		}, {
			text : "Cancel".localize(),
			handler : this.onCancel,
			scope : this
		} ]
	});
	if (this.addFlag === "add") {
		this.roleName = "";
		this.initMemberOf()
	}
};
Ext
		.extend(
				Jedox.studio.users.Role,
				Ext.Panel,
				{
					initRole : function(roleName) {
						var that = this;
						this.roleName = roleName;
						var db = "System";
						var cube = "#_ROLE_ROLE_PROPERTIES";
						var order = [ "#_ROLE_" ];
						var props = [ "name", "description", "status" ];
						var cords = {
							"#_ROLE_" : [ roleName ],
							"#_ROLE_PROPERTIES_" : props
						};
						props.shift();
						function cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database error".localize(),
										"Can not read data".localize());
								return
							}
							that.roleNameTxf.setValue(that
									.getPropsValue(roleName));
							that.descriptionTxa
									.setValue(that
											.getPropsValue(result[roleName]["description"]));
							that.statusCB.setValue(that
									.getPropsValue(result[roleName]["status"]));
							that.initMemberOf()
						}
						Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
								"paloGet", [ db, cube, order, cords ])
					},
					getPropsValue : function(value) {
						if (value != undefined) {
							return value
						}
						return ""
					},
					initMemberOf : function() {
						var that = this;
						var db = "System";
						var rg_cube = "#_GROUP_ROLE";
						var rg_order = [ "#_ROLE_" ];
						var rg_cords = {
							"#_ROLE_" : [ that.roleName ]
						};
						function rg_cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database error".localize(),
										"Can not read data".localize());
								return
							}
							var availableGroupsData = [];
							var roleGroupData = [];
							for (role in result) {
								for (group in result[role]) {
									if (result[role][group] == "1") {
										roleGroupData.push( [ group ])
									} else {
										availableGroupsData.push( [ group ])
									}
								}
							}
							that.availableGroupStore
									.loadData(availableGroupsData);
							that.roleGroupStore.loadData(roleGroupData)
						}
						Jedox.wss.backend.conn.rpc( [ this, rg_cb ], "common",
								"paloGet", [ db, rg_cube, rg_order, rg_cords ]);
						var rr_cube = "#_ROLE_RIGHT_OBJECT";
						var rr_order = [ "#_ROLE_" ];
						var rr_cords = {
							"#_ROLE_" : [ that.roleName ]
						};
						function rr_cb(result) {
							if (!result) {
								Jedox.studio.app.showMessageERROR(
										"Database error".localize(),
										"Can not read data".localize());
								return
							}
							var robjData = [];
							for (role in result) {
								for (r_obj in result[role]) {
									robjData
											.push((result[role][r_obj] != "") ? [
													r_obj,
													that.rightObjReg[r_obj].desc,
													that.permKey[result[role][r_obj]],
													result[role][r_obj] ]
													: [
															r_obj,
															that.rightObjReg[r_obj].desc,
															that.permKey[that.rightObjReg[r_obj].defPerm],
															that.rightObjReg[r_obj].defPerm ])
								}
							}
							that.rightsGridStore.loadData(robjData)
						}
						Jedox.wss.backend.conn.rpc( [ this, rr_cb ], "common",
								"paloGet", [ db, rr_cube, rr_order, rr_cords ])
					},
					addRole : function(addF) {
						var that = this;
						if (this.validateForm()) {
							var db = "System";
							var rr_cube = "#_ROLE_ROLE_PROPERTIES";
							var rr_data = {};
							var rr_order = [ "#_ROLE_" ];
							var add = addF;
							var roleName = addF ? this.roleNameTxf.getValue()
									: this.roleName;
							var roleDesc = this.descriptionTxa.getValue();
							var roleStatus = this.statusCB.getValue() ? "1"
									: "0";
							rr_data[roleName] = {
								description : roleDesc,
								status : roleStatus
							};
							function rr_cb(result) {
								if (!result) {
									Jedox.studio.app.showMessageERROR(
											"Database error".localize(),
											"Can not write data".localize());
									return
								}
								var gr_cube = "#_GROUP_ROLE";
								var gr_data = {};
								var gr_order = [ "#_ROLE_" ];
								gr_data[roleName] = that.getRoleGroups();
								function gr_cb(result) {
									if (!result) {
										Jedox.studio.app
												.showMessageERROR(
														"Database error"
																.localize(),
														"Can not write data"
																.localize());
										return
									}
									var ro_cube = "#_ROLE_RIGHT_OBJECT";
									var ro_data = {};
									var ro_order = [ "#_ROLE_" ];
									ro_data[roleName] = that.getRoleRights();
									function ro_cb(result) {
										if (!result) {
											Jedox.studio.app
													.showMessageERROR(
															"Database error"
																	.localize(),
															"Can not write data"
																	.localize());
											return
										}
										if (!addF
												&& roleName != that.roleNameTxf
														.getValue()) {
											var r_db = "System";
											var r_dim = "#_ROLE_";
											var r_data = {};
											var newRoleName = that.roleNameTxf
													.getValue();
											r_data[roleName] = newRoleName;
											function r_cb(result) {
												if (!result) {
													Jedox.studio.app
															.showMessageERROR(
																	"Database error"
																			.localize(),
																	"Can not write data"
																			.localize());
													return
												}
												that.onClose()
											}
											Jedox.wss.backend.conn.rpc( [ this,
													r_cb ], "common",
													"paloRename", [ r_db,
															r_dim, r_data ])
										} else {
											that.onClose()
										}
									}
									Jedox.wss.backend.conn.rpc(
											[ this, ro_cb ], "common",
											"paloSet", [ db, ro_cube, ro_data,
													ro_order, add ])
								}
								Jedox.wss.backend.conn.rpc( [ this, gr_cb ],
										"common", "paloSet", [ db, gr_cube,
												gr_data, gr_order, add ])
							}
							Jedox.wss.backend.conn.rpc( [ this, rr_cb ],
									"common", "paloSet", [ db, rr_cube,
											rr_data, rr_order, add ])
						}
					},
					getRoleGroups : function() {
						var data = {};
						for ( var i = 0, auNmbr = this.memeberOfIS.fromStore
								.getCount(); i < auNmbr; i++) {
							data[this.memeberOfIS.fromStore.getAt(i).get(
									"groupName")] = ""
						}
						for ( var i = 0, guNmbr = this.memeberOfIS.toStore
								.getCount(); i < guNmbr; i++) {
							data[this.memeberOfIS.toStore.getAt(i).get(
									"groupName")] = "1"
						}
						return data
					},
					getRoleRights : function() {
						var data = this.rightsGridStore;
						var cnt = data.getCount();
						var rights = {};
						for ( var i = 0; i < cnt; i++) {
							var el = data.getAt(i);
							rights[el.get("rightObj")] = el.get("permH")
						}
						return rights
					},
					isError : function(result) {
						if (result[0] === "!" && result.length === 2) {
							return true
						}
						return false
					},
					validateForm : function() {
						var that = this;
						var _return = true;
						function roleName() {
							var msg = "Role name must start with a-z/A-Z character and must contain at least two character"
									.localize();
							var my_regexp = /^[a-zA-Z][a-zA-Z0-9_\-@\.]+$/;
							var value = that.roleNameTxf.getValue();
							if (!my_regexp.test(value)) {
								that.roleNameTxf.markInvalid(msg);
								_return = false
							}
						}
						roleName();
						return _return
					},
					onSave : function() {
						if (this.addFlag === "add") {
							this.addRole(true)
						} else {
							this.addRole(false)
						}
					},
					onCancel : function() {
						if (this.addFlag) {
							this.ownerCt.ownerCt.refreshRoleList();
							this.ownerCt.ownerCt.remove(this.ownerCt)
						} else {
							this.initRole(this.roleName)
						}
					},
					onClose : function() {
						if (this.addFlag) {
							this.ownerCt.ownerCt.refreshRoleList();
							this.ownerCt.ownerCt.remove(this.ownerCt);
							if (this.addFlag == "add") {
								Jedox.studio.app.showTopMsg("",
										"Role added successefully".localize())
							} else {
								Jedox.studio.app
										.showTopMsg("",
												"Role updated successefully"
														.localize())
							}
						} else {
							this.ownerCt.ownerCt.ownerCt.ownerCt
									.refreshRoleList();
							Jedox.studio.app.showTopMsg("",
									"Role updated successefully".localize())
						}
					}
				});