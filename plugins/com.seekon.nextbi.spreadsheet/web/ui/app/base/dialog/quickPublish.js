Jedox.wss.dialog.quickPublish = function() {
	if (!Jedox.wss.workspace
			.getMetaByWinId(Jedox.wss.app.activeBook.getWinId()).ghn) {
		Ext.Msg.show( {
			title : "Quick Publish".localize(),
			msg : "_QP_unsaved_warning".localize(),
			buttons : Ext.Msg.YESNO,
			fn : function(btn) {
				if (btn == "yes") {
					Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.open,
							[ "saveqp" ])
				}
			},
			animEl : "elId",
			icon : Ext.MessageBox.WARNING
		});
		return
	}
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var studio = Jedox.wss.backend.studio;
	var groupData = [];
	var groupStore;
	var _groups;
	var hierarchyData = [];
	var _hierarchies;
	var hierarchyStore;
	var _i;
	var selectedNode;
	var group = {
		index : "",
		group : "",
		name : ""
	};
	var hierarchy = {
		index : "",
		hr : "",
		name : ""
	};
	var pNode;
	var loadGroups = function() {
		groupData = [];
		_groups = studio.treeMngGroup("listGroups", [ "report" ]);
		for ( var i in _groups) {
			groupData.push( [ i, _groups[i].name ])
		}
	};
	var loadHierarchies = function() {
		hierarchyData = [];
		_hierarchies = studio.treeMngHierarchy("wss_report", "listHierarchies");
		if (!_hierarchies) {
			_i = false
		} else {
			_i = true;
			if (Ext.getCmp("studioReportsTree")) {
			}
			for ( var i in _hierarchies) {
				hierarchyData.push( [ i, _hierarchies[i].name ])
			}
		}
	};
	groupStore = new Ext.data.SimpleStore( {
		fields : [ "group", "name" ],
		data : groupData
	});
	hierarchyStore = new Ext.data.SimpleStore( {
		fields : [ "hierarchy", "name" ],
		data : hierarchyData
	});
	loadGroups();
	group.name = groupData[0][1];
	group.index = 0;
	group.group = groupData[0][0];
	studio.treeSetGroup("wss_report", group.group);
	loadHierarchies();
	hierarchy.index = 0;
	hierarchy.name = hierarchyData[0][1];
	hierarchy.hr = hierarchyData[0][0];
	studio.treeSetHierarchy("wss_report", hierarchy.hr);
	groupStore.loadData(groupData);
	hierarchyStore.loadData(hierarchyData);
	var docTitle = Jedox.wss.app.activeBook._winObj.title;
	var groupCombo = new Ext.form.ComboBox( {
		id : "groupComboTB",
		store : groupStore,
		displayField : "name",
		hideLabel : true,
		readOnly : false,
		editable : false,
		lazyRender : true,
		typeAhead : true,
		mode : "local",
		triggerAction : "all",
		value : groupData[0][1],
		selectOnFocus : true,
		listWidth : 145,
		width : 145,
		listeners : {
			select : function(cmb, rec, index) {
				group.index = index;
				studio.treeSetGroup("wss_report", groupData[index][0]);
				loadHierarchies();
				group.group = groupData[index][0];
				if (_i) {
					hierarchy.hr = hierarchyData[0][0];
					studio.treeSetHierarchy("wss_report", hierarchyData[0][0]);
					hierarchyStore.loadData(hierarchyData);
					hierarchyCombo.setValue(hierarchyData[0][1]);
					group.group = groupData[index][0];
					treeLoader.baseParams.hierarchy = hierarchy.hr;
					tree.root.reload();
					tree.root.setText(hierarchyData[0][1]);
					hierarchy.name = hierarchyData[0][1];
					hierarchyCombo.enable();
					Ext.getCmp("publishBtn").enable()
				} else {
					hierarchyCombo.setValue("");
					hierarchyCombo.disable();
					treeLoader.baseParams.hierarchy = "";
					tree.root.setText("");
					tree.root.reload();
					Ext.getCmp("publishBtn").disable()
				}
			}
		}
	});
	var hierarchyCombo = new Ext.form.ComboBox( {
		store : hierarchyStore,
		id : "hierarchyComboTB",
		displayField : "name",
		hideLabel : true,
		disabled : false,
		editable : false,
		readOnly : false,
		typeAhead : true,
		lazyRender : true,
		lazyInit : true,
		mode : "local",
		triggerAction : "all",
		value : hierarchyData[0][1],
		selectOnFocus : true,
		listWidth : 145,
		width : 145,
		listeners : {
			select : function(cmb, rec, index) {
				if (!hierarchyData[index]) {
					index = 0
				}
				hierarchy.hr = hierarchyData[index][0];
				hierarchy.index = index;
				hierarchy.name = hierarchyData[index][1];
				studio.treeSetHierarchy("wss_report", hierarchyData[index][0]);
				treeLoader.baseParams.hierarchy = hierarchy.hr;
				tree.root.reload();
				tree.root.setText(hierarchyData[index][1])
			}
		}
	});
	var Tree = Ext.tree;
	var treeLoader = new Tree.TreeLoader(
			{
				dataUrl : studio.dispatcher.serverUrl.concat("&wam=",
						Jedox.wss.app.appModeS, "&c=", studio.className,
						"&m=treeDump"),
				baseParams : {
					type : "wss_report",
					hierarchy : hierarchy.hr,
					filter : "rfolder"
				},
				preloadChildren : true
			});
	var tree = new Tree.TreePanel( {
		id : "studioReportsTree",
		autowidth : true,
		hidden : false,
		animate : false,
		rootVisible : true,
		autoScroll : true,
		containerScroll : true,
		ddScroll : true,
		border : true,
		loader : treeLoader,
		enableDD : false,
		height : 260,
		listeners : {
			click : function(node, e) {
				pNode = node.attributes.id
			}
		},
		tbar : [ {
			xtype : "tbtext",
			text : "Group".localize().concat(":")
		}, groupCombo, {
			xtype : "tbseparator"
		}, {
			xtype : "tbtext",
			text : "Hierarchy".localize().concat(":")
		}, hierarchyCombo ]
	});
	var initRootText;
	initRootText = (hierarchyData.length == 0) ? ""
			: hierarchyData[hierarchy.index][1];
	var root = new Tree.AsyncTreeNode( {
		text : initRootText,
		draggable : false,
		id : "root",
		expanded : true,
		iconCls : "w3s_hierarchy",
		editable : false,
		loader : treeLoader
	});
	tree.setRootNode(root);
	var rName = new Ext.form.TextField( {
		id : "rName",
		value : docTitle,
		width : 299,
		fieldLabel : "Report name".localize(),
		validator : function(vl) {
			if (vl.replace(/^\s+|\s+$/g, "") == "") {
				Ext.getCmp("publishBtn").disable();
				return "Report must have name".localize()
			} else {
				Ext.getCmp("publishBtn").enable();
				return true
			}
		}
	});
	var win = new Ext.Window(
			{
				title : "Quick Publish".localize(),
				closable : true,
				id : "quick-publish",
				autoDestroy : true,
				cls : "default-format-window",
				plain : true,
				constrain : true,
				modal : true,
				resizable : false,
				enableHdMenu : false,
				animCollapse : false,
				width : 440,
				height : 440,
				layout : "fit",
				bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
				listeners : {
					close : function() {
						Jedox.wss.general
								.setInputMode(Jedox.wss.app.lastInputModeDlg);
						Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.quickPublish);
						studio.removeFromSessionCurrGH("wss_report")
					}
				},
				items : [ new Ext.Panel( {
					baseCls : "x-plain",
					layout : "form",
					autoHeight : true,
					items : [ {
						html : "_QP_directions".localize(),
						baseCls : "x-plain",
						bodyStyle : "margin-bottom: 10px;"
					}, tree, {
						bodyStyle : "padding-top: 10px;",
						items : rName,
						layout : "form",
						baseCls : "x-plain"
					} ]
				}) ],
				buttons : [ {
					text : "Publish".localize(),
					id : "publishBtn",
					handler : publish
				}, {
					text : "Cancel".localize(),
					handler : function() {
						win.close()
					}
				} ]
			});
	function trim(s) {
		return s.replace(/^\s+|\s+$/g, "")
	}
	function validateName(nname, children, selected) {
		var n = 0;
		var is = true;
		nname = trim(nname);
		var name = nname;
		while (is) {
			is = false;
			if (n > 0) {
				name = nname + " (" + (n) + ")"
			}
			for ( var i = 0; i < children.length; i++) {
				if ((trim(children[i].text.toLowerCase()) == name.toLowerCase())) {
					n++;
					is = true
				}
			}
		}
		return name
	}
	function publish() {
		if (!pNode) {
			Ext.Msg.show( {
				title : "Quick Publish".localize(),
				msg : "_QP_noSelection".localize(),
				buttons : Ext.Msg.OK,
				animEl : "elId",
				icon : Ext.MessageBox.WARNING
			});
			return
		}
		var content = studio.treeMngNode("wss_report", pNode, "dump", 0);
		docTitle = rName.getValue();
		var noDouble = false;
		for ( var q = 0; q < content.length; q++) {
			if ((content[q].text == docTitle) && content[q].leaf) {
				noDouble = true
			}
		}
		if (noDouble) {
			Ext.Msg.show( {
				title : "Quick Publish".localize(),
				msg : "_QP_double_warning".localize( {
					rName : docTitle
				}),
				buttons : Ext.Msg.OK,
				fn : function(btn) {
					var nnm = validateName(docTitle, content, true);
					rName.setValue(nnm);
					docTitle = nnm;
					rName.focus(true, 10)
				},
				animEl : "elId",
				icon : Ext.MessageBox.WARNING
			})
		} else {
			var src = Jedox.wss.workspace
					.getMetaByWinId(Jedox.wss.app.activeBook.getWinId()).ghn;
			var pb = studio.treeMngNode("wss_report", pNode, "addNode", true,
					"template", {
						name : docTitle,
						ref : {
							group : src.g,
							hierarchy : src.h,
							node : src.n,
							type : "workbook"
						}
					});
			if (pb) {
				Ext.Msg.show( {
					title : "Quick Publish".localize(),
					msg : "_QP_success".localize(),
					buttons : Ext.Msg.OK,
					animEl : "elId",
					icon : Ext.MessageBox.INFO
				});
				win.close()
			} else {
				Ext.Msg.show( {
					title : "Quick Publish".localize(),
					msg : "_QP_error".localize(),
					buttons : Ext.Msg.OK,
					animEl : "elId",
					icon : Ext.MessageBox.ERROR
				})
			}
		}
	}
	win.show(this);
	tree.root.reload()
};