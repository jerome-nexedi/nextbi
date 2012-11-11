Jedox.studio.files.properties = function(type, data, renameHandleFnc,
		refreshHandleFnc) {
	Jedox.studio.app.lastInputMode = Jedox.studio.app.inputMode;
	Jedox.studio.app.inputMode = Jedox.studio.app.inputMode_studio_DEFAULT;
	var referrersDataTypeFlag = false;
	var resourcesDataTypeFlag = false;
	var _urlPluginParamsChangedFlag = false;
	var _urlParamsTab;
	var _resourcesObj;
	var _securityObj;
	var _listDateFormat = "d.m.Y H:i:s";
	var iconImg = {
		html : "Image".localize(),
		baseCls : "x-plain"
	};
	var nameTxf = new Ext.form.TextField( {
		hideLabel : true,
		allowBlank : false,
		width : 260
	});
	var descriptionLbl = new Ext.form.Label( {
		text : "Description".localize().concat(":"),
		hidden : true
	});
	var descriptionTxa = new Ext.form.TextArea( {
		hideLabel : true,
		hidden : true,
		width : 260
	});
	var splitLine = {
		html : "<br><br>",
		baseCls : "split-line",
		width : 350
	};
	var typeLbl = new Ext.form.Label( {
		text : "Type".localize().concat(":")
	});
	var typeVal = new Ext.form.Label( {
		text : ""
	});
	var locationLbl = new Ext.form.Label( {
		text : "Location".localize().concat(":")
	});
	var locationVal = new Ext.form.Label( {
		text : ""
	});
	var urlLbl = new Ext.form.Label( {
		text : "URL target".localize().concat(":"),
		hidden : true
	});
	var urlTxf = new Ext.form.TextField( {
		hideLabel : true,
		hidden : true,
		allowBlank : false,
		width : 180
	});
	var urlPanel = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		width : 300,
		height : 25,
		hidden : true,
		items : [
				{
					layout : "form",
					border : false,
					baseCls : "top-left-bottom-panel",
					width : 183,
					items : urlTxf
				},
				{
					border : false,
					baseCls : "toolbar-panel",
					width : 100,
					height : 25,
					tbar : [
							{
								tooltip : {
									title : "Select".localize(),
									text : "Select URL".localize()
								},
								text : "Select URL".localize(),
								handler : function() {
									urlTxf.focus(true)
								},
								scope : this
							},
							{
								tooltip : {
									title : "Mail".localize(),
									text : "Send link to mail".localize()
											.concat("...")
								},
								iconCls : "mail-link-icon",
								handler : function() {
									var email = "Enter destination e-mail"
											.localize().concat("...");
									var subject = "Link to".localize().concat(
											"...");
									var body_message = urlTxf.getValue();
									var mailto_link = "mailto:" + email
											+ "?subject=" + subject + "&body="
											+ body_message;
									document.location.href = mailto_link
								},
								scope : this
							} ]
				} ]
	};
	var locationTxf = new Ext.form.TextField( {
		hideLabel : true,
		allowBlank : false,
		width : 260
	});
	var locationCmp = locationVal;
	var sizeLbl = new Ext.form.Label( {
		text : "Size".localize().concat(":"),
		hidden : true
	});
	var sizeVal = new Ext.form.Label( {
		text : "",
		hidden : true
	});
	var sizeOnDiskLbl = new Ext.form.Label( {
		text : "Size on disk".localize().concat(":"),
		hidden : true
	});
	var sizeOnDiskVal = new Ext.form.Label( {
		text : "",
		hidden : true
	});
	var createdLbl = new Ext.form.Label( {
		text : "Created".localize().concat(":"),
		hidden : true
	});
	var createdVal = new Ext.form.Label( {
		text : "dd/mm/yyyy",
		hidden : true
	});
	var modifiedLbl = new Ext.form.Label( {
		text : "Modified".localize().concat(":"),
		hidden : true
	});
	var modifiedVal = new Ext.form.Label( {
		text : "dd/mm/yyyy",
		hidden : true
	});
	var accessedLbl = new Ext.form.Label( {
		text : "Accessed".localize().concat(":"),
		hidden : true
	});
	var accessedVal = new Ext.form.Label( {
		text : "dd/mm/yyyy",
		hidden : true
	});
	initIcon(data);
	var generalPanel = new Ext.Panel( {
		layout : "table",
		bodyStyle : "background-color: transparent;",
		border : false,
		defaults : {
			bodyStyle : "background-color: transparent;",
			bodyStyle : "padding:5px",
			baseCls : "x-plain"
		},
		layoutConfig : {
			columns : 3
		},
		items : [ {
			rowspan : 1,
			items : iconImg
		}, {
			colspan : 2,
			items : nameTxf
		}, {
			rowspan : 1,
			items : descriptionLbl
		}, {
			colspan : 2,
			items : descriptionTxa
		}, {
			colspan : 3,
			bodyStyle : "padding:2px",
			items : splitLine
		}, {
			rowspan : 1,
			items : typeLbl
		}, {
			colspan : 2,
			items : typeVal
		}, {
			rowspan : 1,
			items : locationLbl
		}, {
			colspan : 2,
			items : locationCmp
		}, {
			rowspan : 1,
			items : urlLbl
		}, {
			colspan : 2,
			items : urlPanel
		}, {
			rowspan : 1,
			items : sizeLbl
		}, {
			colspan : 2,
			items : sizeVal
		}, {
			rowspan : 1,
			items : sizeOnDiskLbl
		}, {
			colspan : 2,
			items : sizeOnDiskVal
		}, {
			colspan : 3,
			bodyStyle : "padding:2px",
			items : splitLine
		}, {
			rowspan : 1,
			items : createdLbl
		}, {
			colspan : 2,
			items : createdVal
		}, {
			rowspan : 1,
			items : modifiedLbl
		}, {
			colspan : 2,
			items : modifiedVal
		}, {
			rowspan : 1,
			items : accessedLbl
		}, {
			colspan : 2,
			items : accessedVal
		} ]
	});
	var urlLbl = new Ext.form.Label( {
		text : "URL target".localize().concat(":")
	});
	var urlTxa = new Ext.form.TextArea( {
		hideLabel : true,
		width : 340,
		height : 120,
		columns : 20,
		rows : 20,
		preventScrollbars : true
	});
	var noteLbl = {
		html : "Note: Copy into clipboard is possible with <b>ctrl + c</b> keyboard shortcut"
				.localize(),
		baseCls : "x-plain"
	};
	var credentialsChb = new Ext.form.Checkbox( {
		boxLabel : "Include credentials".localize(),
		hideLabel : true,
		checked : false,
		handler : function() {
			if (this.getValue()) {
				currentUserRb.enable();
				currentUserRb.setValue(true);
				otherUserRb.enable();
				otherUserRb.setValue(false)
			} else {
				currentUserRb.setValue(false);
				otherUserRb.setValue(false);
				currentUserRb.disable();
				otherUserRb.disable();
				urlTxa.setValue(Jedox.studio.backend.wssStudio.getExternURI( {
					studio : {
						"default" : {
							reports : {
								group : data.group,
								hierarchy : data.hierarchy,
								node : data.node,
								path : data.rPath,
								"var" : getVars()
							}
						}
					}
				}, {
					flag : 0
				}));
				urlTxa.focus(true)
			}
		}
	});
	var currentUserRb = new Ext.form.Radio( {
		boxLabel : "Current user".localize(),
		hideLabel : true,
		checked : false,
		name : "credentials",
		width : 100,
		handler : function() {
			if (this.getValue()) {
				getWSSURL(1)
			}
		},
		inputValue : 1
	});
	var otherUserRb = new Ext.form.Radio(
			{
				boxLabel : "Other user".localize(),
				hideLabel : true,
				checked : false,
				name : "credentials",
				width : 100,
				handler : function() {
					if (this.getValue()) {
						credentialsPanel.items.items[credentialsPanel.items.items.length - 2]
								.show();
						credentialsPanel.items.items[credentialsPanel.items.items.length - 1]
								.show()
					} else {
						credentialsPanel.items.items[credentialsPanel.items.items.length - 2]
								.hide();
						credentialsPanel.items.items[credentialsPanel.items.items.length - 1]
								.hide()
					}
				},
				inputValue : 1
			});
	var userTxf = new Ext.form.TextField( {
		fieldLabel : "User name".localize(),
		width : 157
	});
	var passTxf = new Ext.form.TextField( {
		fieldLabel : "Password".localize(),
		width : 157
	});
	var credentialsPanel = new Ext.Panel( {
		layout : "absolute",
		width : 400,
		height : 380,
		baseCls : "x-plain",
		defaults : {
			baseCls : "x-plain"
		},
		items : [
				{
					layout : "form",
					x : 10,
					y : 10,
					items : [ urlLbl ]
				},
				{
					layout : "form",
					x : 10,
					y : 25,
					width : 370,
					items : [ urlTxa ]
				},
				{
					layout : "form",
					x : 10,
					y : 150,
					width : 370,
					items : [ noteLbl ]
				},
				{
					layout : "form",
					x : 180,
					y : 170,
					buttons : [
							{
								text : "Select URL".localize(),
								handler : function() {
									urlTxa.focus(true)
								}
							},
							{
								text : "Send to mail".localize(),
								handler : function() {
									var email = "Enter destination e-mail"
											.localize().concat("...");
									var subject = "Link to".localize().concat(
											"...");
									var body_message = urlTxa.getValue();
									var mailto_link = "mailto:" + email
											+ "?subject=" + subject + "&body="
											+ body_message;
									document.location.href = mailto_link
								}
							} ]
				}, {
					layout : "form",
					x : 10,
					y : 200,
					width : 170,
					items : [ credentialsChb ]
				}, {
					layout : "form",
					x : 35,
					y : 225,
					items : [ currentUserRb, otherUserRb ]
				}, {
					layout : "form",
					labelWidth : 60,
					x : 65,
					y : 275,
					hidden : true,
					items : [ userTxf, passTxf ]
				}, {
					layout : "form",
					labelWidth : 60,
					x : 204,
					y : 330,
					hidden : true,
					buttons : [ {
						text : "Update URL".localize(),
						handler : function() {
							getWSSURL(2)
						}
					} ]
				} ]
	});
	var linkPanel = new Ext.Panel( {
		layout : "form",
		baseCls : "x-plain",
		items : [ credentialsPanel ],
		listeners : {
			render : function(panel) {
				currentUserRb.disable();
				otherUserRb.disable()
			}
		}
	});
	function getVars() {
		var vars = [];
		var varsData = data.var_VARNAME;
		if (varsData) {
			if (varsData.nodes) {
				for ( var i in varsData.nodes) {
					vars.push( {
						name : i,
						type : "nodes",
						val : varsData.nodes[i]
					})
				}
			}
			if (varsData.vars) {
				for ( var j in varsData.vars) {
					vars.push( {
						name : j,
						type : "vars",
						val : varsData.vars[j]
					})
				}
			}
		}
		console.log(vars);
		return vars
	}
	function URLPluginParamsTab() {
		var urlPluginDesc = new Ext.form.Label( {
			text : "urlPluginPropsLbl".localize()
		});
		var BRLbl = {
			html : "<br/>",
			baseCls : "x-plain"
		};
		var URLPluginChb = function(name, checked) {
			return new Ext.form.Checkbox( {
				boxLabel : name.localize(),
				checked : checked == 1 ? true : false,
				handler : function() {
					_urlPluginParamsChangedFlag = true
				}
			})
		};
		var hideToolbar = new URLPluginChb("Hide toolbar".localize(),
				data.urlPluginParams.hideToolbar);
		var hideSave = new URLPluginChb("Hide save".localize(),
				data.urlPluginParams.hideSave);
		var hideFilter = new URLPluginChb("Hide filter".localize(),
				data.urlPluginParams.hideFilter);
		var hideStaticFilter = new URLPluginChb(
				"Hide static filter".localize(),
				data.urlPluginParams.hideStaticFilter);
		var hideAxes = new URLPluginChb("Hide axes".localize(),
				data.urlPluginParams.hideHorizontalAxis
						|| data.urlPluginParams.hideVerticalAxis);
		var hideConnectionPicker = new URLPluginChb("Hide connection picker"
				.localize(), data.urlPluginParams.hideConnectionPicker);
		var urlPluginPanel = new Ext.Panel( {
			layout : "form",
			baseCls : "x-plain",
			labelWidth : 30,
			items : [ urlPluginDesc, BRLbl, hideToolbar, hideSave, hideFilter,
					hideStaticFilter, hideAxes, hideConnectionPicker ]
		});
		this.getPanel = function() {
			return urlPluginPanel
		};
		this.getURLPluginParams = function() {
			return {
				hideToolbar : hideToolbar.getValue() ? 1 : 0,
				hideSave : hideSave.getValue() ? 1 : 0,
				hideFilter : hideFilter.getValue() ? 1 : 0,
				hideStaticFilter : hideStaticFilter.getValue() ? 1 : 0,
				hideHorizontalAxis : hideAxes.getValue() ? 1 : 0,
				hideVerticalAxis : hideAxes.getValue() ? 1 : 0,
				hideConnectionPicker : hideConnectionPicker.getValue() ? 1 : 0
			}
		}
	}
	var propertiesTbs = new Ext.TabPanel(
			{
				region : "center",
				height : 430,
				layoutOnTabChange : true,
				border : false,
				bodyStyle : "background-color: transparent;",
				activeTab : 0,
				defaults : {
					bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
				},
				items : [ {
					title : "General".localize(),
					layout : "fit",
					baseCls : "x-plain",
					items : [ generalPanel ]
				} ]
			});
	var mainPanel = new Ext.Panel( {
		modal : true,
		layout : "form",
		bodyStyle : "background-color: transparent;",
		border : false,
		items : [ propertiesTbs ]
	});
	function initIcon(data) {
		switch (data.type) {
		case "Workbook":
			iconImg.html = '<div class="workbook-icon32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			referrersDataTypeFlag = true;
			resourcesDataTypeFlag = true;
			break;
		case "Folder":
			iconImg.html = '<div class="folder-icon32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			referrersDataTypeFlag = true;
			break;
		case "Rfolder":
			iconImg.html = '<div class="folder-icon32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			break;
		case "Template":
			iconImg.html = '<div class="workbook-icon32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			break;
		case "File group":
			iconImg.html = '<div class="w3s_group32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			showDecriptionCmp();
			break;
		case "File repository":
			iconImg.html = '<div class="w3s_hierarchy32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			referrersDataTypeFlag = true;
			showDecriptionCmp();
			break;
		case "Report repository":
			iconImg.html = '<div class="w3s_hierarchy32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			showDecriptionCmp();
			break;
		case "Report group":
			iconImg.html = '<div class="w3s_group32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			showDecriptionCmp();
			break;
		case "Urlplugin":
			iconImg.html = '<div class="new-pivot-icon32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			referrersDataTypeFlag = true;
			showDecriptionCmp();
			break;
		case "Rurlplugin":
			iconImg.html = '<div class="new-pivot-icon32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			break;
		case "Hyperlink":
			iconImg.html = '<div class="w3s_rhyperlink32"><img src="../lib/ext/resources/images/default/s.gif" width="32px" height="32px"/></div>';
			referrersDataTypeFlag = true;
			showDecriptionCmp();
			break
		}
		function showDecriptionCmp() {
			descriptionTxa.hidden = false;
			descriptionLbl.hidden = false
		}
		function showURLTxf() {
			urlLbl.hidden = false;
			urlTxf.hidden = false;
			urlPanel.hidden = false
		}
		if (type == "hierarchy") {
			locationCmp = locationTxf;
			locationCmp.setValue(data.sysPath)
		} else {
			if (type == "hyperlink") {
				locationCmp = locationTxf;
				locationCmp.setValue(data.url.url)
			} else {
				locationVal.text = data.relPath
			}
		}
	}
	function init(data) {
		nameTxf.setValue(data.name);
		descriptionTxa.setValue(data.description);
		typeVal.text = data.type.localize();
		if (data.props) {
			var props = data.props;
			sizeLbl.setVisible(true);
			sizeVal.setVisible(true);
			sizeVal.text = Ext.util.Format.fileSize(props.size).concat(" (",
					props.size, " bytes)");
			if (data.props.blksize != -1) {
				sizeOnDiskLbl.setVisible(true);
				sizeOnDiskVal.setVisible(true);
				sizeOnDiskVal.text = props.blksize
			}
			createdLbl.setVisible(true);
			createdVal.setVisible(true);
			createdVal.text = Date.parseDate(props.ctime, _listDateFormat)
					.format("date_format".localize());
			modifiedLbl.setVisible(true);
			modifiedVal.setVisible(true);
			modifiedVal.text = Date.parseDate(props.mtime, _listDateFormat)
					.format("date_format".localize());
			accessedLbl.setVisible(true);
			accessedVal.setVisible(true);
			accessedVal.text = Date.parseDate(props.atime, _listDateFormat)
					.format("date_format".localize())
		}
		if (type === "qreport") {
			nameTxf.disable();
			if (data.type == "Template") {
				propertiesTbs.add( {
					title : "Link".localize(),
					baseCls : "x-plain",
					items : [ linkPanel ]
				});
				urlTxa.setValue(Jedox.studio.backend.wssStudio.getExternURI( {
					studio : {
						"default" : {
							reports : {
								group : data.group,
								hierarchy : data.hierarchy,
								node : data.node,
								path : data.rPath,
								"var" : getVars()
							}
						}
					}
				}, {
					flag : 0
				}))
			}
		}
		if (type === "urlplugin") {
			_urlParamsTab = new URLPluginParamsTab();
			propertiesTbs.add( {
				title : "Options".localize(),
				baseCls : "x-plain",
				items : [ _urlParamsTab.getPanel() ]
			})
		}
		if (referrersDataTypeFlag) {
			var itemsFlag = true;
			if (data.type in {
				Workbook : true,
				Hyperlink : true,
				Urlplugin : true
			}) {
				itemsFlag = false
			}
			var referrers = new Jedox.studio.files.ReferrersList(itemsFlag);
			var node = data.type == "File repository" ? "root" : data.node;
			var referrersPanel = referrers.getReferrersList( [ node ]);
			propertiesTbs.add( {
				title : "Referrers".localize(),
				baseCls : "x-plain",
				items : [ referrersPanel ]
			})
		}
		if (resourcesDataTypeFlag) {
			_resourcesObj = new Jedox.studio.files.Resources("resources", false);
			var resourcesPanel = _resourcesObj.getPanel( [ data.node ]);
			propertiesTbs.add( {
				title : "Resources".localize(),
				baseCls : "x-plain",
				items : [ resourcesPanel ]
			})
		}
		if (Jedox.studio.access.rules.rights
				& Jedox.studio.access.permType.DELETE && type != "qreport") {
			_securityObj = new Jedox.studio.dialog.Security(true);
			_securityObj.setData(type, data);
			propertiesTbs.add( {
				title : "Security".localize(),
				baseCls : "x-plain",
				items : [ _securityObj.getPanel() ]
			})
		}
	}
	function trim(s) {
		return s.replace(/^\s+|\s+$/g, "")
	}
	function getWSSURL(flag) {
		var wssStudioHandler = {
			getExternURI : function(result) {
				urlTxa.setValue(result);
				urlTxa.focus(true)
			}
		};
		var wssStudioStub = new Studio(wssStudioHandler);
		if (credentialsChb.getValue()) {
			if (currentUserRb.getValue()) {
				var credentials = {
					flag : flag
				}
			} else {
				var credentials = {
					flag : flag,
					user : userTxf.getValue(),
					pass : passTxf.getValue()
				}
			}
		}
		var params = {
			studio : {
				"default" : {
					reports : {
						group : data.group,
						hierarchy : data.hierarchy,
						node : data.node,
						path : data.rPath,
						"var" : getVars()
					}
				}
			}
		};
		wssStudioStub.getExternURI(params, credentials)
	}
	function onSetPermission() {
		if (_securityObj) {
			_securityObj.savePermission()
		}
	}
	function onRename() {
		if (!nameTxf.getValue()) {
			var msg = "Name must start with a-z/A-Z character and must contain more than two character"
					.localize()
					+ "...";
			nameTxf.markInvalid(msg)
		} else {
			if (data.name != nameTxf.getValue()
					|| (descriptionTxa && descriptionTxa.getValue() != data.description)
					|| (locationTxf.getValue() && data.sysPath != locationTxf
							.getValue()) || _urlPluginParamsChangedFlag) {
				switch (type) {
				case "group":
					renameHandleFnc(trim(nameTxf.getValue()),
							trim(descriptionTxa.getValue()));
					break;
				case "hierarchy":
					renameHandleFnc(trim(nameTxf.getValue()),
							trim(descriptionTxa.getValue()), trim(locationCmp
									.getValue()));
					data.sysPath = trim(locationCmp.getValue());
					break;
				case "rhierarchy":
					renameHandleFnc(trim(nameTxf.getValue()),
							trim(descriptionTxa.getValue()));
					break;
				case "hyperlink":
					renameHandleFnc(trim(nameTxf.getValue()),
							trim(descriptionTxa.getValue()), locationCmp
									.getValue(), data.url.target);
					break;
				case "urlplugin":
					renameHandleFnc(trim(nameTxf.getValue()),
							trim(descriptionTxa.getValue()), _urlParamsTab
									.getURLPluginParams());
					break;
				default:
					renameHandleFnc(trim(nameTxf.getValue()));
					break
				}
				data.name = trim(nameTxf.getValue())
			}
		}
	}
	function onRefresh() {
		if (refreshHandleFnc && _securityObj) {
			refreshHandleFnc(_securityObj.getPermission())
		}
	}
	function onSetResources() {
		if (_resourcesObj) {
			_resourcesObj.setResources()
		}
	}
	function onSetFileProps() {
	}
	function onApply() {
		onSetPermission();
		onRename();
		onSetResources();
		if (_securityObj && _securityObj.getRefreshFlag()
				&& _securityObj.getPermission() == "N") {
			onRefresh();
			win.close()
		}
	}
	function onCancel() {
		if (_securityObj && _securityObj.getRefreshFlag()) {
			onRefresh()
		}
		win.close()
	}
	function onSave() {
		onSetPermission();
		onRename();
		onSetResources();
		if (_securityObj && _securityObj.getRefreshFlag()) {
			onRefresh()
		}
		win.close()
	}
	var win = new Ext.Window( {
		id : "studio-files-properties-dlg",
		title : "Properties".localize(),
		closable : true,
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		constrain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 400,
		height : 500,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.studio.app.inputMode = Jedox.studio.app.lastInputMode;
				if (_securityObj && _securityObj.getRefreshFlag()) {
					onRefresh()
				}
			}
		},
		buttons : [ {
			text : "OK".localize(),
			handler : function() {
				onSave()
			}
		}, {
			text : "Cancel".localize(),
			handler : function() {
				onCancel()
			}
		}, {
			text : "Apply".localize(),
			handler : function() {
				onApply()
			}
		} ]
	});
	init(data);
	win.show(this)
};