Jedox.studio.app.preferences = function(level, name) {
	var prefs_level = {
		SERVER : 0,
		GROUP : 1,
		USER : 2
	};
	var wssStudioHandler = {
		getPrefs : function(data) {
			if (!data) {
				return false
			}
			init(data)
		}
	};
	var wssStudioStub = new Studio(wssStudioHandler);
	wssStudioStub.getPrefs(level, name);
	function init(data) {
		var _nnS = "<not set>".localize();
		var _nnID = "nn";
		var _nnHFS = "&lt;not set&gt;".localize();
		var _Pdata = data;
		var _data = getInterfaceData(data);
		var General = function(general) {
			var preselectFirstResourcesHierarchy = false;
			var preselectFirstReportsHierarchy = false;
			var BRLbl = {
				html : "<br/>",
				baseCls : "x-plain"
			};
			var localizationLbl = new Ext.form.Label( {
				text : "Localization".localize(),
				cls : "preferences-title"
			});
			var environmentLbl = new Ext.form.Label( {
				text : "Environment".localize(),
				cls : "preferences-title"
			});
			var languageCmb = new Ext.form.ComboBox( {
				fieldLabel : "Language".localize(),
				width : 180,
				store : new Ext.data.SimpleStore( {
					data : [ [ _nnID, _nnHFS ],
							[ "en_US", "English".localize() ],
							[ "de_DE", "Deutsch".localize() ],
							[ "fr_FR", "Francais".localize() ] ],
					fields : [ {
						name : "id"
					}, {
						name : "desc"
					} ]
				}),
				valueField : "id",
				displayField : "desc",
				value : general.i18n,
				editable : false,
				mode : "local",
				triggerAction : "all",
				listeners : {
					select : function(cmb, record, index) {
						if (this.value == _nnID) {
							this.setValue(_nnS)
						}
					}
				}
			});
			var themeCmb = new Ext.form.ComboBox( {
				fieldLabel : "Theme".localize(),
				width : 180,
				store : new Ext.data.SimpleStore( {
					data : [ [ _nnID, _nnHFS ],
							[ "default", "Blue (default)" ],
							[ "gray", "Gray".localize() ] ],
					fields : [ {
						name : "id"
					}, {
						name : "desc"
					} ]
				}),
				valueField : "id",
				displayField : "desc",
				value : general.theme,
				editable : false,
				mode : "local",
				triggerAction : "all",
				listeners : {
					select : function(cmb, record, index) {
						if (this.value == _nnID) {
							this.setValue(_nnS)
						}
					}
				}
			});
			var languageThemePanel = new Ext.Panel(
					{
						defaults : {
							bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;",
							labelStyle : "font-size:11px;"
						},
						baseCls : "x-plain",
						layout : "form",
						labelWidth : 60,
						autoWidth : true,
						autoHeight : true,
						x : 20,
						y : 25,
						items : [ languageCmb, themeCmb ]
					});
			var environmentPanel = new Ext.Panel( {
				layout : "absolute",
				baseCls : "x-plain",
				width : 400,
				height : 75,
				items : [ environmentLbl, languageThemePanel ]
			});
			var resourcesLbl = new Ext.form.Label( {
				text : "File Manager - Default Folder".localize(),
				cls : "preferences-title",
				x : 0,
				y : 30
			});
			var resourcesGroupStore = new Ext.data.SimpleStore( {
				fields : [ "id", "name" ]
			});
			var resourcesHierarchyStore = new Ext.data.SimpleStore( {
				fields : [ "id", "name" ]
			});
			var resourcesGroupCmb = new Ext.form.ComboBox( {
				fieldLabel : "Group".localize(),
				width : 180,
				store : resourcesGroupStore,
				valueField : "id",
				displayField : "name",
				value : general.resources.group,
				editable : false,
				mode : "local",
				triggerAction : "all",
				listeners : {
					select : function(cmb, record, index) {
						if (this.value == _nnID || this.value == _nnS) {
							this.setValue(_nnS);
							resourcesHierarchyStore.removeAll();
							resourcesHierarchyCmb.setValue(_nnS)
						} else {
							Jedox.studio.backend.wssStudio.treeSetGroup(
									"prefs_file", record.data.id);
							initResourcesHierarchy()
						}
					}
				}
			});
			var resourcesHierarchyCmb = new Ext.form.ComboBox( {
				fieldLabel : "Hierarchy".localize(),
				width : 180,
				store : resourcesHierarchyStore,
				valueField : "id",
				displayField : "name",
				value : findInStore(general.resources.hierarchy,
						resourcesHierarchyStore).index == 0 ? _nnS
						: general.resources.hierarchy,
				editable : false,
				mode : "local",
				triggerAction : "all"
			});
			var resourcesCmbPanel = new Ext.Panel(
					{
						defaults : {
							bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;",
							labelStyle : "font-size:11px;"
						},
						baseCls : "x-plain",
						layout : "form",
						labelWidth : 60,
						autoWidth : true,
						autoHeight : true,
						x : 20,
						y : 55,
						items : [ resourcesGroupCmb, resourcesHierarchyCmb ]
					});
			var resourcesPanel = new Ext.Panel( {
				layout : "absolute",
				baseCls : "x-plain",
				width : 400,
				height : 105,
				items : [ resourcesLbl, resourcesCmbPanel ]
			});
			var reportsLbl = new Ext.form.Label( {
				text : "Reports".localize(),
				cls : "preferences-title",
				x : 0,
				y : 30
			});
			var reportsGroupStore = new Ext.data.SimpleStore( {
				fields : [ "id", "name" ]
			});
			var reportsGroupCmb = new Ext.form.ComboBox( {
				fieldLabel : "Group".localize(),
				width : 180,
				store : reportsGroupStore,
				valueField : "id",
				displayField : "name",
				value : general.reports.group,
				editable : false,
				mode : "local",
				triggerAction : "all",
				listeners : {
					select : function(cmb, record, index) {
						if (this.value == _nnID || this.value == _nnS) {
							this.setValue(_nnS);
							reportsHierarchyStore.removeAll();
							reportsHierarchyCmb.setValue(_nnS)
						} else {
							Jedox.studio.backend.wssStudio.treeSetGroup(
									"prefs_report", record.data.id);
							initReportsHierarchy()
						}
					}
				}
			});
			var reportsHierarchyStore = new Ext.data.SimpleStore( {
				fields : [ "id", "name" ]
			});
			var reportsHierarchyCmb = new Ext.form.ComboBox( {
				fieldLabel : "Hierarchy".localize(),
				width : 180,
				store : reportsHierarchyStore,
				valueField : "id",
				displayField : "name",
				value : findInStore(general.reports.hierarchy,
						reportsHierarchyStore).index == 0 ? _nnS
						: general.reports.hierarchy,
				editable : false,
				mode : "local",
				triggerAction : "all"
			});
			var reportsCmbPanel = new Ext.Panel(
					{
						defaults : {
							bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;",
							labelStyle : "font-size:11px;"
						},
						baseCls : "x-plain",
						layout : "form",
						labelWidth : 60,
						autoWidth : true,
						autoHeight : true,
						x : 20,
						y : 55,
						items : [ reportsGroupCmb, reportsHierarchyCmb ]
					});
			var reportsPanel = new Ext.Panel( {
				layout : "absolute",
				baseCls : "x-plain",
				width : 400,
				height : 105,
				items : [ reportsLbl, reportsCmbPanel ]
			});
			var generalTabPanel = {
				title : "Default".localize(),
				layout : "form",
				baseCls : "x-plain",
				items : [ BRLbl, environmentPanel, resourcesPanel, reportsPanel ]
			};
			var mainPanel = new Ext.TabPanel(
					{
						height : 370,
						width : 330,
						layoutOnTabChange : true,
						baseCls : "options-tab-panel",
						defaults : {
							bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
						},
						activeTab : 0,
						items : [ generalTabPanel ]
					});
			function init() {
				if (initResourcesGroup()) {
					var result = findInStore(general.resources.group,
							resourcesGroupStore);
					if (result.index == 0 && result.record.data.id == _nnID) {
						resourcesGroupCmb.setValue(_nnS);
						resourcesHierarchyCmb.setValue(_nnS)
					} else {
						resourcesGroupCmb.fireEvent("select", this,
								result.record, result.index);
						resourcesGroupCmb
								.setValue(result.record.get("id") == _nnID ? _nnS
										: result.record.get("id"))
					}
				} else {
					resourcesGroupCmb.setValue(_nnS);
					resourcesHierarchyCmb.setValue(_nnS)
				}
				if (initReportsGroup()) {
					var result = findInStore(general.reports.group,
							reportsGroupStore);
					if (result.index == 0 && result.record.data.id == _nnID) {
						reportsGroupCmb.setValue(_nnS);
						reportsHierarchyCmb.setValue(_nnS)
					} else {
						reportsGroupCmb.fireEvent("select", this,
								result.record, result.index);
						reportsGroupCmb
								.setValue(result.record.get("id") == _nnID ? _nnS
										: result.record.get("id"))
					}
				} else {
					reportsGroupCmb.setValue(_nnS);
					reportsHierarchyCmb.setValue(_nnS)
				}
			}
			function findInStore(value, store) {
				var index = store.find("id", value);
				if (index == -1) {
					index = 0
				}
				var record = store.getAt(index);
				return ( {
					record : record,
					index : index
				})
			}
			function initResourcesGroup() {
				var gData = [];
				gData.push( [ _nnID, _nnHFS ]);
				var result = Jedox.studio.backend.wssStudio.treeMngGroup(
						"listGroups", [ "file" ]);
				if (result == null) {
					return false
				}
				if (result instanceof Array) {
					return false
				}
				groupExist = true;
				for ( var i in result) {
					gData.push( [ i, result[i].name ])
				}
				resourcesGroupStore.loadData(gData);
				return true
			}
			function initResourcesHierarchy() {
				var hData = [];
				var result = Jedox.studio.backend.wssStudio.treeMngHierarchy(
						"prefs_file", "listHierarchies");
				if (!result) {
					resourcesHierarchyStore.loadData(hData);
					resourcesHierarchyCmb.setValue("");
					return false
				}
				if (result instanceof Array) {
					resourcesHierarchyCmb.setValue(_nnS);
					return false
				}
				for ( var i in result) {
					hData.push( [ i, result[i].name ])
				}
				resourcesHierarchyStore.loadData(hData);
				if (preselectFirstResourcesHierarchy) {
					resourcesHierarchyCmb.setValue(hData[0][0])
				} else {
					preselectFirstResourcesHierarchy = true;
					if (resourcesHierarchyCmb.getValue() == _nnS) {
						resourcesHierarchyCmb.setValue(hData[0][0])
					}
				}
				return true
			}
			function initReportsGroup() {
				var gData = [];
				gData.push( [ _nnID, _nnHFS ]);
				var result = Jedox.studio.backend.wssStudio.treeMngGroup(
						"listGroups", [ "report" ]);
				if (result == null) {
					return false
				}
				if (result instanceof Array) {
					return false
				}
				groupExist = true;
				for ( var i in result) {
					gData.push( [ i, result[i].name ])
				}
				reportsGroupStore.loadData(gData);
				gCmbSelectedIndex = 0;
				return true
			}
			function initReportsHierarchy() {
				var hData = [];
				var result = Jedox.studio.backend.wssStudio.treeMngHierarchy(
						"prefs_report", "listHierarchies");
				if (!result) {
					reportsHierarchyStore.loadData(hData);
					reportsHierarchyCmb.setValue("");
					return false
				}
				if (result instanceof Array) {
					reportsHierarchyCmb.setValue(_nnS);
					return false
				}
				for ( var i in result) {
					hData.push( [ i, result[i].name ])
				}
				reportsHierarchyStore.loadData(hData);
				if (preselectFirstReportsHierarchy) {
					reportsHierarchyCmb.setValue(hData[0][0])
				} else {
					preselectFirstReportsHierarchy = true;
					if (reportsHierarchyCmb.getValue() == _nnS) {
						reportsHierarchyCmb.setValue(hData[0][0])
					}
				}
				return true
			}
			init();
			function getGeneralValue() {
				return {
					i18n : languageCmb.getValue(),
					theme : themeCmb.getValue(),
					resources : {
						group : resourcesGroupCmb.getValue(),
						hierarchy : resourcesHierarchyCmb.getValue()
					},
					reports : {
						group : reportsGroupCmb.getValue(),
						hierarchy : reportsHierarchyCmb.getValue()
					}
				}
			}
			function setGeneralValue(obj) {
			}
			this.getPanel = function() {
				return mainPanel
			};
			this.getValue = function() {
				return getGeneralValue()
			};
			this.setValue = function(obj) {
				setGeneralValue(obj)
			}
		};
		var Studio = function() {
			var BRLbl = {
				html : "<br/>",
				baseCls : "x-plain"
			};
			var studioTabPanel = {
				title : "General".localize(),
				layout : "form",
				baseCls : "x-plain",
				items : [ BRLbl ]
			};
			var mainPanel = new Ext.TabPanel(
					{
						height : 400,
						width : 330,
						layoutOnTabChange : true,
						baseCls : "options-tab-panel",
						defaults : {
							bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
						},
						activeTab : 0,
						items : [ studioTabPanel ]
					});
			function getStudioValue() {
			}
			function setStudioValue(obj) {
			}
			this.getPanel = function() {
				return mainPanel
			};
			this.getValue = function() {
				return getStudioValue()
			};
			this.setValue = function(obj) {
				setStudioValue(obj)
			}
		};
		var Wss = function(wss) {
			var BRLbl = {
				html : "<br/>",
				baseCls : "x-plain"
			};
			var environmentLbl = new Ext.form.Label( {
				text : "Environment".localize(),
				cls : "preferences-title"
			});
			var toolbarCmb = new Ext.form.ComboBox( {
				fieldLabel : "Toolbar".localize(),
				width : 180,
				store : new Ext.data.SimpleStore( {
					data : [ [ _nnID, _nnHFS ],
							[ "toolbar", "Toolbar".localize() ],
							[ "ribbon", "Ribbon".localize() ] ],
					fields : [ {
						name : "id"
					}, {
						name : "desc"
					} ]
				}),
				valueField : "id",
				displayField : "desc",
				value : wss.toolbar,
				editable : false,
				mode : "local",
				triggerAction : "all",
				listeners : {
					select : function(cmb, record, index) {
						if (this.value == _nnID) {
							this.setValue(_nnS)
						}
					}
				}
			});
			var wssCmbPanel = new Ext.Panel(
					{
						defaults : {
							bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;",
							labelStyle : "font-size:11px;"
						},
						baseCls : "x-plain",
						layout : "form",
						labelWidth : 60,
						autoWidth : true,
						autoHeight : true,
						x : 20,
						y : 25,
						items : [ toolbarCmb ]
					});
			var wssPanel = new Ext.Panel( {
				layout : "absolute",
				baseCls : "x-plain",
				width : 400,
				height : 105,
				items : [ environmentLbl, wssCmbPanel ]
			});
			var wssTabPanel = {
				title : "Default".localize(),
				layout : "form",
				baseCls : "x-plain",
				items : [ BRLbl, wssPanel ]
			};
			var mainPanel = new Ext.TabPanel(
					{
						height : 370,
						width : 330,
						layoutOnTabChange : true,
						baseCls : "options-tab-panel",
						defaults : {
							bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
						},
						activeTab : 0,
						items : [ wssTabPanel ]
					});
			function getWssValue() {
				return {
					toolbar : toolbarCmb.getValue()
				}
			}
			function setWssValue(obj) {
			}
			this.getPanel = function() {
				return mainPanel
			};
			this.getValue = function() {
				return getWssValue()
			};
			this.setValue = function(obj) {
				setWssValue(obj)
			}
		};
		var PaloOnline = function(paloOnlineData) {
			var BRLbl = {
				html : "<br/>",
				baseCls : "x-plain"
			};
			var helpLbl = new Ext.form.Label( {
				text : "Help".localize(),
				cls : "preferences-title"
			});
			var noteLbl = {
				html : "Enter your online Palo access data".localize(),
				baseCls : "x-plain"
			};
			var userNameTxf = new Ext.form.TextField( {
				fieldLabel : "User Name".localize(),
				labelStyle : "font-size:11px;",
				cls : "preview-properties-panel",
				name : "username",
				value : paloOnlineData.username,
				width : 200
			});
			var passwordTxf = new Ext.form.TextField( {
				fieldLabel : "Password".localize(),
				labelStyle : "font-size:11px;",
				cls : "preview-properties-panel",
				name : "password",
				value : paloOnlineData.password,
				width : 200
			});
			var paloOnlineTabPanel = {
				title : "General".localize(),
				layout : "form",
				baseCls : "x-plain",
				items : [ BRLbl, helpLbl, BRLbl, noteLbl, BRLbl, BRLbl,
						userNameTxf, passwordTxf ]
			};
			var mainPanel = new Ext.TabPanel(
					{
						height : 370,
						width : 330,
						layoutOnTabChange : true,
						baseCls : "options-tab-panel",
						defaults : {
							bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
						},
						activeTab : 0,
						items : [ paloOnlineTabPanel ]
					});
			function getPaloOnlineValue() {
				var name = userNameTxf.getValue() || _nnID;
				var pass = passwordTxf.getValue() || _nnID;
				return {
					username : name,
					password : pass
				}
			}
			function setPaloOnlineValue(obj) {
			}
			this.getPanel = function() {
				return mainPanel
			};
			this.getValue = function() {
				return getPaloOnlineValue()
			};
			this.setValue = function(obj) {
				setPaloOnlineValue(obj)
			}
		};
		var Etl = function() {
			var BRLbl = {
				html : "<br/>",
				baseCls : "x-plain"
			};
			var etlTabPanel = {
				title : "General".localize(),
				layout : "form",
				baseCls : "x-plain",
				items : [ BRLbl ]
			};
			var mainPanel = new Ext.TabPanel(
					{
						height : 370,
						width : 330,
						layoutOnTabChange : true,
						baseCls : "options-tab-panel",
						defaults : {
							bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
						},
						activeTab : 0,
						items : [ etlTabPanel ]
					});
			function getEtlValue() {
			}
			function setEtlValue(obj) {
			}
			this.getPanel = function() {
				return mainPanel
			};
			this.getValue = function() {
				return getEtlValue()
			};
			this.setValue = function(obj) {
				setEtlValue(obj)
			}
		};
		var navigationMenuData = [ [ "General".localize(), "prefs-general" ],
				[ "WSS".localize(), "prefs-wss" ],
				[ "Palo Online".localize(), "prefs-palo-online" ] ];
		var navigationMenuStore = new Ext.data.SimpleStore( {
			fields : [ "label", "icon" ],
			data : navigationMenuData
		});
		var navigationMenuDV = new Ext.DataView(
				{
					id : "options-menu-navigation",
					store : navigationMenuStore,
					tpl : new Ext.XTemplate(
							'<div class="options-menu-navigation" style="width: 99%;"><div class="charttypes">Type</div><tpl for=".">',
							'<div class="thumb-wrap">',
							'<div class="thumb"><img class="{icon}" src="../lib/ext/resources/images/default/s.gif"/>&nbsp;&nbsp;&nbsp;{label}</div></div>',
							"</tpl></div>"),
					multiSelect : false,
					singleSelect : true,
					overClass : "x-view-over",
					itemSelector : "div.thumb-wrap",
					listeners : {
						containerclick : function(dView, e) {
							e.stopEvent();
							return false
						},
						click : function(dataView, index, node, e) {
							setNavigationView(index)
						}
					}
				});
		function getInterfaceData(data) {
			var _Mdata = {
				general : {
					i18n : _nnS,
					theme : _nnS,
					resources : {
						group : _nnS,
						hierarchy : _nnS
					},
					reports : {
						group : _nnS,
						hierarchy : _nnS
					}
				},
				wss : {
					toolbar : _nnS
				},
				mypalo : {
					username : "",
					password : ""
				}
			};
			if (data) {
				if (data.general) {
					if (data.general.l10n) {
						_Mdata.general.i18n = data.general.l10n
					}
					if (data.general.theme) {
						_Mdata.general.theme = data.general.theme
					}
				}
				if (data.studio) {
					if (data.studio["default"]) {
						if (data.studio["default"].files) {
							_Mdata.general.resources.group = data.studio["default"].files.group;
							_Mdata.general.resources.hierarchy = data.studio["default"].files.hierarchy
						}
						if (data.studio["default"].reports) {
							_Mdata.general.reports.group = data.studio["default"].reports.group;
							_Mdata.general.reports.hierarchy = data.studio["default"].reports.hierarchy
						}
					}
				}
				if (data.wss) {
					if (data.wss["interface"]) {
						_Mdata.wss.toolbar = data.wss["interface"]
					}
				}
				if (data.mypalo) {
					_Mdata.mypalo.username = data.mypalo.username;
					_Mdata.mypalo.password = data.mypalo.password
				}
			}
			return _Mdata
		}
		function setNavigationView(index) {
			mainPanel.items.items[1].layout.setActiveItem(index)
		}
		var navigationMenuPanel = {
			layout : "fit",
			width : 130,
			height : 380,
			border : true,
			items : navigationMenuDV
		};
		var general = new General(_data.general);
		var wss = new Wss(_data.wss);
		var paloOnline = new PaloOnline(_data.mypalo);
		function getOptionsData() {
			var generalData = general.getValue();
			var wssData = wss.getValue();
			var mypaloData = paloOnline.getValue();
			var nnValue = {};
			nnValue[_nnID] = true;
			nnValue[_nnS] = true;
			var _Pdata = {};
			var tmplData = [
					[ "general/l10n", generalData.i18n ],
					[ "general/theme", generalData.theme ],
					[ "studio/default/files/group",
							generalData.resources["group"] ],
					[ "studio/default/files/hierarchy",
							generalData.resources["hierarchy"] ],
					[ "studio/default/reports/group",
							generalData.reports["group"] ],
					[ "studio/default/reports/hierarchy",
							generalData.reports["hierarchy"] ],
					[ "wss/interface", wssData.toolbar ],
					[ "mypalo/username", mypaloData.username ],
					[ "mypalo/password", mypaloData.password ] ];
			function clearNodes(nodes) {
				for ( var i = 0; i < nodes.length; i++) {
					delete _Pdata[nodes[i]]
				}
			}
			function addNodes(data) {
				var path, value, obj, node;
				for ( var i = 0; i < data.length; i++) {
					if (data[i][1] in nnValue) {
						continue
					}
					path = data[i][0].split("/");
					value = data[i][1];
					obj = _Pdata;
					node = path.pop();
					for ( var j = 0; j < path.length; j++) {
						if (!obj[path[j]]) {
							obj[path[j]] = {}
						}
						obj = obj[path[j]]
					}
					obj[node] = value
				}
			}
			clearNodes( [ "general", "studio", "wss", "mypalo" ]);
			addNodes(tmplData);
			return _Pdata
		}
		var aboutBtn = {
			text : "About".localize(),
			handler : function() {
				function _show_vers(vers) {
					var ui_ver = Jedox.studio.app.uiVer;
					var msg = "<b>UI</b><br />Version: <b>".concat(ui_ver[0],
							"</b><br />Date: ", ui_ver[1],
							"<br /><br /><b>Backend</b><br />Version: <b>",
							vers.core[0], "</b><br />Date: ", vers.core[1],
							"<br />Build Date: ", vers.core[2],
							"<br /><br /><b>Palo</b><br />Server Version: <b>",
							vers.palo, "</b><br />libpalo_ng Version: <b>",
							vers.lib[0], "</b><br />PHPPalo Version: <b>",
							vers.lib[1], "</b><br />");
					var aboutPanel = {
						bodyStyle : "padding:20px",
						html : msg,
						border : false
					};
					var aboutWin = new Ext.Window( {
						id : "about-dlg",
						title : "About Palo Suite".localize(),
						closable : true,
						closeAction : "close",
						autoDestroy : true,
						plain : true,
						modal : true,
						resizable : false,
						animCollapse : false,
						layout : "fit",
						width : 270,
						height : 300,
						items : aboutPanel,
						buttonAlign : "center",
						buttons : {
							text : "OK".localize(),
							handler : function() {
								aboutWin.close()
							}
						}
					});
					aboutWin.show()
				}
				Jedox.wss.backend.conn.rpc( [ this, _show_vers ], "WSS",
						"getVersions")
			}
		};
		function isEmpty(object) {
			for ( var i in object) {
				return true
			}
			return false
		}
		var okBtn = {
			text : "OK".localize(),
			handler : function() {
				var d = getOptionsData();
				win.close();
				var theme = false;
				if (!name) {
					if (d) {
						if (d.general) {
							if (d.general.theme) {
								theme = d.general.theme
							}
						}
					}
					if (theme) {
						Ext.util.CSS.swapStyleSheet("theme",
								"../lib/ext/resources/css/xtheme-".concat(
										theme, ".css"));
						if (Jedox.studio.frames.files) {
							Jedox.studio.frames.files.Ext.util.CSS
									.swapStyleSheet("theme",
											"../lib/ext/resources/css/xtheme-"
													.concat(theme, ".css"))
						}
						if (Jedox.studio.frames.reports) {
							Jedox.studio.frames.reports.Ext.util.CSS
									.swapStyleSheet("theme",
											"../lib/ext/resources/css/xtheme-"
													.concat(theme, ".css"))
						}
					}
					var helpBtn = Ext.getCmp("ps-help-btn");
					if (d.mypalo && d.mypalo.username && d.mypalo.password) {
						Jedox.studio.app.myPalo = {
							username : d.mypalo.username,
							password : d.mypalo.password
						};
						helpBtn.enable();
						helpBtn.setTooltip("Help".localize())
					} else {
						delete Jedox.studio.app.myPalo;
						helpBtn.disable();
						helpBtn.setTooltip("nregmsg".localize())
					}
					var url = "/spreadsheet/root/cc/studio/prefs.php";
					var params = Ext.util.JSON.encode(d);
					var xhttp = new XMLHttpRequest();
					xhttp.open("POST", url, true);
					xhttp.setRequestHeader("Content-length", params.length);
					xhttp.send(params)
				} else {
					Jedox.studio.backend.wssStudio.setPrefs(level, name, d)
				}
			}
		};
		var cancelBtn = {
			text : "Cancel".localize(),
			handler : function() {
				win.close()
			}
		};
		var mainPanel = new Ext.Panel( {
			layout : "absolute",
			baseCls : "x-plain",
			border : false,
			items : [
					{
						layout : "column",
						border : false,
						baseCls : "x-plain",
						x : 0,
						y : 0,
						items : [ navigationMenuPanel ]
					},
					{
						layout : "card",
						baseCls : "x-plain",
						border : false,
						width : 448,
						height : 380,
						activeItem : 0,
						x : 140,
						y : 0,
						items : [ general.getPanel(), wss.getPanel(),
								paloOnline.getPanel() ]
					}, {
						layout : "absolute",
						width : 590,
						height : 100,
						defaults : {
							baseCls : "x-plain"
						},
						baseCls : "x-plain",
						x : 0,
						y : 382,
						items : [ {
							layout : "form",
							x : 0,
							y : 0,
							buttons : [ aboutBtn ]
						}, {
							layout : "form",
							x : 410,
							y : 0,
							buttons : [ okBtn, cancelBtn ]
						} ]
					} ]
		});
		var win = new Ext.Window( {
			id : "options-dlg",
			title : "Options".localize(),
			closable : true,
			closeAction : "close",
			autoDestroy : true,
			plain : true,
			modal : true,
			resizable : false,
			animCollapse : false,
			layout : "fit",
			width : 605,
			height : 455,
			items : [ mainPanel ],
			listeners : {
				close : function() {
					Jedox.studio.backend.wssStudio
							.removeUserPrefGroupsFromSession()
				}
			}
		});
		win.show(this);
		navigationMenuDV.select(0, false, false)
	}
};