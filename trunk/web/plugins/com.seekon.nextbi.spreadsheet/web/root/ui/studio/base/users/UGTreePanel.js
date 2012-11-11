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