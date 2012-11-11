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