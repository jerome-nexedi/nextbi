Jedox.studio.app.content = function() {
	var that = this;
	var welcomePanel = this.getWelcomePanel();
	Jedox.studio.app.content.superclass.constructor.call(this, {
		id : "ps-content-panel",
		region : "center",
		layout : "card",
		border : false,
		margins : "5 5 5 0",
		bodyStyle : "background-color: transparent;",
		activeItem : 0,
		items : [ welcomePanel ]
	})
};
Ext
		.extend(
				Jedox.studio.app.content,
				Ext.Panel,
				{
					addCmp : function(cmp) {
						for ( var i = 0; i < cmp.length; i++) {
							this.add(cmp[i])
						}
					},
					addBtn : function(btn) {
						for ( var i = 0; i < btn.length; i++) {
							this.items.items[0].items.items[0].add(btn[i])
						}
					},
					getWelcomePanel : function() {
						var that = this;
						var btnPanel = new Ext.Panel( {
							layout : "table",
							border : false,
							layoutConfig : {
								columns : 2
							}
						});
						var recentStore = new Ext.data.SimpleStore( {
							fields : [ {
								name : "context",
								type : "string"
							}, {
								name : "location"
							}, {
								name : "type",
								type : "string"
							}, {
								name : "path",
								type : "string"
							}, {
								name : "iconCls",
								type : "string"
							} ]
						});
						var recentListView = new Ext.DataView(
								{
									id : "recent-list-view",
									store : recentStore,
									tpl : new Ext.XTemplate(
											'<div class="recent-list-view"><tpl for=".">',
											'<div class="thumb-wrap">',
											'<div class="thumb" style="padding: 0px; text-align: left;"><img class="{iconCls}" src="../lib/ext/resources/images/default/s.gif" width="16" height="16">',
											'<span>&nbsp;{path}</span></div></div><div class="br"><br></div>',
											"</tpl></div>"),
									autoWidth : true,
									autoHeight : true,
									multiSelect : false,
									singleSelect : true,
									overClass : "x-view-over",
									itemSelector : "div.thumb-wrap",
									emptyText : "",
									tooltip : null,
									listeners : {
										click : function(dataView, index, node,
												e) {
											var data = dataView.getStore()
													.getAt(index);
											var context = data.get("context");
											var location = data.get("location");
											var g = location.group;
											var h = location.hierarchy;
											var n = location.node;
											var t = data.get("iconCls").split(
													"_").pop();
											var p = location.path;
											var vars = location["var"];
											var type = data.get("type");
											var name = (location
													&& location.path && location.path.length != 0) ? location.path
													.split("/").pop()
													: "unknown".localize();
											switch (context) {
											case "files":
												Jedox.studio.files.openRecentClb = function() {
													Ext.getCmp("wt-panel")
															.openRecent( {
																g : g,
																h : h,
																n : n,
																t : t,
																p : p
															}, name, type);
													Jedox.studio.files.openRecentClb = null
												};
												Ext.getCmp("files-tree-holder")
														.expand();
												break;
											case "reports":
												Ext.getCmp(
														"reports-tree-holder")
														.expand();
												Ext
														.getCmp(
																"reports-content-panel")
														.fireEvent("quickview",
																true);
												if (Jedox.studio.access.rules.ste_reports
														& Jedox.studio.access.permType.WRITE) {
													Ext
															.getCmp(
																	"studioReportsTree")
															.fireEvent(
																	"switchToQV")
												}
												setTimeout(function() {
													Ext
															.getCmp(
																	"reports-content-panel")
															.fireEvent(
																	"openRecent",
																	{
																		g : g,
																		h : h,
																		n : n,
																		t : t,
																		p : p,
																		v : vars
																	}, name,
																	type)
												});
												break;
											default:
												break
											}
										},
										contextmenu : function(dataView, index,
												node, e) {
											var data = dataView.getStore()
													.getAt(index);
											var context = data.get("context");
											var location = data.get("location");
											var g = location.group;
											var h = location.hierarchy;
											var n = location.node;
											var expToHF = (context == "reports" && g
													.charAt(0) == "f") ? true
													: false;
											var menu = new Ext.menu.Menu(
													{
														enableScrolling : false,
														listeners : {
															hide : function(
																	menu) {
																menu.destroy()
															}
														},
														items : [
																{
																	text : "Open"
																			.localize(),
																	iconCls : "open-folder-icon",
																	handler : function() {
																		dataView
																				.fireEvent(
																						"click",
																						dataView,
																						index,
																						node,
																						e)
																	}
																},
																{
																	text : "Explore To"
																			.localize(),
																	hidden : expToHF,
																	iconCls : "view-list-icon",
																	handler : function() {
																		if (context == "files") {
																			Ext
																					.getCmp(
																							"ps-wt-tree")
																					.fireEvent(
																							"onExplore",
																							g,
																							h,
																							n)
																		} else {
																			Ext
																					.getCmp(
																							"reports-tree-holder")
																					.expand();
																			if (Jedox.studio.access.rules.ste_reports
																					& Jedox.studio.access.permType.WRITE) {
																				Ext
																						.getCmp(
																								"studioReportsTree")
																						.fireEvent(
																								"switchToQV")
																			}
																			Ext
																					.getCmp(
																							"quickViewStudioReportsTree")
																					.fireEvent(
																							"preselect",
																							{
																								g : g,
																								h : h,
																								n : n,
																								rPath : location.path,
																								open : false
																							})
																		}
																	}
																} ]
													});
											e.stopEvent();
											menu.showAt(e.getXY())
										},
										mouseenter : function(dataView, index,
												node, e) {
											var data = dataView.getStore()
													.getAt(index);
											var context = data.get("context");
											var title = context.substr(0, 1)
													.toUpperCase()
													+ context.substr(1);
											var location = data.get("location");
											var path = (context == "reports" && location.group
													.charAt(0) == "f") ? "location unknown (opened throught hyperlink)"
													.localize()
													: location.path;
											if (!this.tooltip) {
												this.tooltip = new Ext.ToolTip(
														{
															target : this.id,
															title : title,
															layout : "fit",
															items : new Ext.form.Label(
																	{
																		text : path
																	}),
															trackMouse : true,
															tplWriteMode : "overwrite"
														})
											} else {
												this.tooltip.setTitle(title);
												this.tooltip.items.items[0]
														.setText(path)
											}
											this.tooltip.showAt(e.xy)
										},
										mouseleave : function(dataView, index,
												node, e) {
											this.tooltip.hide()
										}
									}
								});
						var recentContainer = new Ext.Panel( {
							layout : "fit",
							x : 40,
							y : 40,
							width : 700,
							height : 200,
							autoScroll : true,
							border : true,
							items : [ recentListView ]
						});
						var clearBtn = {
							text : "Clear list".localize(),
							handler : function() {
								function cb() {
									Ext.getCmp("recent-list-view").getStore()
											.removeAll()
								}
								Jedox.wss.backend.conn.rpc( [ this, cb ],
										"common", "clearRecent")
							}
						};
						var clearBtnPanel = {
							layout : "form",
							border : false,
							bodyStyle : "padding:0px",
							width : 700,
							height : 20,
							x : 40,
							y : 245,
							buttons : clearBtn
						};
						var recentPanel = new Ext.Panel( {
							layout : "absolute",
							height : 280,
							border : false,
							items : [
									{
										x : 40,
										y : 20,
										html : "<b><td>"
												+ "Recent Documents".localize()
												+ "</b>",
										baseCls : "x-plain"
									}, recentContainer, clearBtnPanel ]
						});
						var welcomePanel = new Ext.Panel( {
							layout : "form",
							autoScroll : true,
							defaults : {
								bodyStyle : "padding:20px"
							},
							items : [ btnPanel, recentPanel ],
							listeners : {
								beforeshow : function() {
									that.reloadRecent()
								}
							}
						});
						return welcomePanel
					},
					reloadRecent : function() {
						function cb(result) {
							var recentData = [];
							if (result) {
								for ( var i = result.length - 1; 0 <= i; i--) {
									recentData
											.push( [
													result[i].context,
													result[i].location,
													result[i].type,
													(result[i].location
															&& result[i].location.path && result[i].location.path != 0) ? result[i].location.path
															.split("/").pop()
															: "unknown"
																	.localize(),
													result[i].subtype ? "w3s_"
															.concat(result[i].subtype)
															: "w3s_workbook" ])
								}
								Ext.getCmp("recent-list-view").getStore()
										.loadData(recentData)
							}
						}
						Jedox.wss.backend.conn.rpc( [ this, cb ], "common",
								"fetchRecent")
					}
				});