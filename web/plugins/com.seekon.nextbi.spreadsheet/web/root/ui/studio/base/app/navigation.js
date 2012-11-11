Jedox.studio.app.navigation = function() {
	this._isReport = false;
	this.expandedFlag = false;
	var navigation = new Ext.Panel( {
		layout : "accordion",
		id : "studio-accordion",
		bodyBorder : false,
		bodyStyle : "background-color: transparent;",
		cls : "studio-navigation-accordion"
	});
	Jedox.studio.app.navigation.superclass.constructor
			.call(
					this,
					{
						id : "ps-navigation-panel",
						title : "Admin mode".localize(),
						collapsible : true,
						region : "west",
						split : true,
						width : 275,
						margins : "5 0 5 5",
						cmargins : "5 5 5 5",
						border : false,
						layout : "fit",
						collapseMode : "mini",
						items : [ navigation ],
						listeners : {
							collapse : function(p) {
								Ext.DomQuery
										.selectNode("*[id*=ps-navigation-panel]").style.display = "none"
							}
						},
						bbar : [
								{
									iconCls : "home-icon",
									tooltip : "Home Page".localize(),
									handler : function() {
										if (Ext.getCmp("studio-accordion").layout.activeItem) {
											Ext.getCmp("studio-accordion").layout.activeItem
													.collapse()
										}
									}
								},
								"-",
								{
									text : "Options".localize(),
									iconCls : "user-preferences-icon",
									tooltip : "Options".localize(),
									disabled : false,
									handler : function() {
										var prefs_level = {
											SERVER : 0,
											GROUP : 1,
											USER : 2
										};
										Jedox.studio.app.preferences(
												prefs_level.USER, null)
									}
								},
								{
									id : "ps-help-btn",
									text : "Help".localize(),
									iconCls : "help-icon",
									tooltip : Jedox.studio.app.myPalo
											&& Jedox.studio.app.myPalo.username
											&& Jedox.studio.app.myPalo.password ? "Help"
											.localize()
											: "nregmsg".localize(),
									disabled : !(Jedox.studio.app.myPalo
											&& Jedox.studio.app.myPalo.username && Jedox.studio.app.myPalo.password),
									handler : function() {
										var myPaloWin = window.open("",
												"_blank"), myPaloDoc = myPaloWin.document;
										myPaloDoc.open();
										myPaloDoc
												.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml">');
										myPaloDoc
												.write('<head><title>Palo Web</title></head><body><form id="mypalo" action="https://www.jedox.com/login.php?lang='
														.concat(
																Jedox.studio.i18n.L10n,
																'" method="post">'));
										myPaloDoc
												.write('<input type="hidden" name="username" value="'
														.concat(
																Jedox.studio.app.myPalo.username,
																'" />'));
										myPaloDoc
												.write('<input type="hidden" name="password" value="'
														.concat(
																Jedox.studio.app.myPalo.password,
																'" />'));
										myPaloDoc
												.write("</form></body></html>");
										myPaloDoc.close();
										myPaloDoc.getElementById("mypalo")
												.submit()
									}
								},
								"-",
								{
									iconCls : "logout-icon",
									text : "Logout".localize(),
									handler : function() {
										Ext.Msg
												.show( {
													title : "Logout".localize(),
													msg : "logout_prompt"
															.localize(),
													buttons : Ext.Msg.OKCANCEL,
													closable : false,
													icon : Ext.MessageBox.QUESTION,
													fn : function(btn) {
														if (btn != "ok") {
															return
														}
														window.onbeforeunload = function() {
														};
														window.location.href = "/spreadsheet/root/ui/login/?r"
													}
												})
									},
									scope : this
								} ]
					})
};
Ext.extend(Jedox.studio.app.navigation, Ext.Panel, {
	addCmp : function(cmp) {
		for ( var i = 0; i < cmp.length; i++) {
			this.items.items[0].add(cmp[i])
		}
	},
	setWelcomePanelActive : function() {
		if (!this.expandedFlag) {
			Ext.getCmp("ps-content-panel").layout.setActiveItem(0)
		}
	},
	setPanelActive : function(n) {
		clearTimeout(this.timer);
		this.expandedFlag = true;
		Ext.getCmp("ps-content-panel").layout.setActiveItem(n)
	},
	isSelected : function(treePanel) {
		var br = treePanel.root.childNodes.length;
		for ( var i = 0; i < br; i++) {
			if (treePanel.root.childNodes[i].isSelected()) {
				return true
			}
		}
		return false
	}
});