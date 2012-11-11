Jedox.studio.app.modules.files = Jedox.studio.files.init = function() {
	return {
		navigation : new Ext.Panel(
				{
					title : "File Manager".localize(),
					id : "files-tree-holder",
					items : Jedox.studio.files.FTreePanel(),
					collapsed : true,
					listeners : {
						expand : function(p) {
							this.body.dom.style.display = "block";
							var that = Ext.getCmp("ps-navigation-panel");
							that.setPanelActive("wt-panel");
							Ext.getCmp("rt-main-panel").fireEvent("initTlb");
							Jedox.studio.app.inputMode = Jedox.studio.app.inputMode_studio_DEFAULT
						},
						collapse : function(p) {
							this.body.dom.style.display = "none";
							var that = Ext.getCmp("ps-navigation-panel");
							that.timer = setTimeout(function() {
								that.expandedFlag = false;
								that.setWelcomePanelActive()
							}, 0)
						},
						resize : function(panel, adjWidth, adjHeight, rawWidth,
								rawHeight) {
							Ext.getCmp("ps-wt-tree").setHeight(adjHeight - 50);
							Ext.getCmp("ps-wt-tree").doLayout()
						}
					}
				}),
		content : new Jedox.studio.files.WTPanel(),
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
										cls : "w_resources",
										imgNormal : "../lib/ext/resources/images/default/s.gif",
										imgOver : "../lib/ext/resources/images/default/s.gif",
										imgClicked : "../lib/ext/resources/images/default/s.gif",
										actionFn : function() {
											Ext.getCmp("files-tree-holder")
													.expand()
										}
									}),
							{
								x : 150,
								y : 114,
								html : "<b>" + "File Manager".localize()
										+ "</b><br>"
										+ "Files and folders".localize(),
								baseCls : "x-plain"
							} ]
				}),
		order : 1
	}
};