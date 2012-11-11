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