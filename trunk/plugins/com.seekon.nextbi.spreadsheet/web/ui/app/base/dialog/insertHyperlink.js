Jedox.wss.dialog.openHL = function() {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	Array.prototype.findIndex = function(value) {
		var ctr = "";
		for ( var i = 0; i < this.length; i++) {
			if (this[i][0] == value) {
				return i
			}
		}
		return ctr
	};
	var env = Jedox.wss.app.environment, rngStartCoord = env.lastRangeStartCoord, rngEndCoord = env.lastRangeEndCoord;
	var _pre = Jedox.wss.hl.get( [ rngStartCoord[0], rngStartCoord[1],
			rngEndCoord[0], rngEndCoord[1] ]);
	var openDlg = new Jedox.wss.dialog.Browse("hyperlink", false, _pre ? true
			: false);
	var _activeMode = 0;
	var _urlSel;
	var hldata = {
		dyn : false,
		link : {},
		text : {},
		tip : {},
		trans : []
	};
	var cellRefChildren = [];
	var definedNames = [];
	var definedNamesStore = [];
	var isNR = false;
	var selCoord = Jedox.wss.app.environment.selectedCellCoords;
	var initCellValue = Jedox.wss.general.filterHLTags(selCoord[0],
			selCoord[1], Jedox.wss.app.environment.selectedCellValue, false);
	var _txtSrc = "string";
	var _tipSrc = "string";
	var position = Jedox.wss.app.environment.defaultSelection.getActiveRange()
			.getActiveCell();
	var tmpResponse = Jedox.wss.backend.conn.cmd(0, [ "nlst" ], [ [
			position._x, position._y ] ]);
	var names = tmpResponse[0][1][0];
	for ( var i = 0; i < names.length; i++) {
		var tmpProp = {
			text : names[i].name,
			leaf : true,
			id : "N_".concat(i),
			scope : names[i].scope
		};
		definedNamesStore.push(names[i].scope);
		definedNames.push(tmpProp)
	}
	var worksheets = Jedox.wss.backend.ha.getSheets()[0];
	for (i = 1; worksheets[i] != null; i = i + 2) {
		var tmpProp = {
			text : worksheets[i],
			leaf : true
		};
		if (i == 1) {
			var firstNode = worksheets[i]
		}
		cellRefChildren.push(tmpProp)
	}
	var towhat = "towb";
	function validateTransfer() {
		for ( var t = 0; t < gridData.length; t++) {
			if (gridData[t][5] == "cval" || gridData[t][5] == "clist") {
				gridData.splice(t, 1)
			}
		}
		gridStore.loadData(gridData)
	}
	var onCategorySelect = function(dView, index, node, ev) {
		Ext.getCmp("main_field").layout.setActiveItem(index);
		tabs.setActiveTab("target");
		typeStoreFrom.loadData(typeData);
		typeStoreTo.loadData(typeData);
		Ext.getCmp("transfer").enable();
		_activeMode = index;
		if (index == 3) {
			if (eMail.getValue() != "") {
				_urlSel = "mailto:".concat(eMail.getValue())
			}
			towhat = "tomail";
			isNR = false;
			Ext.getCmp("transfer").disable();
			gridData = [];
			gridStore.loadData(gridData)
		} else {
			if (index == 2) {
				_urlSel = newDocName.getValue();
				towhat = "tourl";
				isNR = false
			} else {
				if (index == 1) {
					if (!_pre) {
						_urlSel = firstNode.concat("!A1")
					}
					towhat = "tosel";
					typeStoreTo.removeAt(4);
					typeStoreTo.removeAt(3);
					validateTransfer()
				} else {
					if (index == 0) {
						win.setHeight(580);
						towhat = "towb";
						isNR = false;
						typeStoreTo.removeAt(4);
						typeStoreTo.removeAt(3);
						validateTransfer()
					} else {
						if (index == 4) {
							win.setHeight(580);
							towhat = "tocell";
							isNR = false
						}
					}
				}
			}
		}
		gridData = []
	};
	var refSelData = [
			[ "text", "Text".localize(), "Plain text" ],
			[ "cref", "Cell Reference".localize(),
					"Reference to a cell, or formula" ] ];
	var refSelStore = new Ext.data.SimpleStore( {
		fields : [ "ref", "desc", "ttip" ],
		data : refSelData
	});
	var refSelCombo = new Ext.form.ComboBox( {
		typeAhead : true,
		triggerAction : "all",
		lazyRender : true,
		editable : false,
		value : "Text",
		width : 120,
		tabIndex : 100,
		hideLabel : true,
		store : refSelStore,
		mode : "local",
		displayField : "desc",
		listeners : {
			select : function(el, rec, index) {
				if (index == 0) {
					_txtSrc = "string"
				} else {
					_txtSrc = "ref"
				}
			}
		}
	});
	var refSelComboTip = new Ext.form.ComboBox( {
		typeAhead : true,
		triggerAction : "all",
		lazyRender : true,
		editable : false,
		value : "Text",
		width : 120,
		tabIndex : 102,
		hideLabel : true,
		store : refSelStore,
		mode : "local",
		displayField : "desc",
		listeners : {
			select : function(el, rec, index) {
				if (index == 0) {
					_tipSrc = "string"
				} else {
					_tipSrc = "ref"
				}
			}
		}
	});
	var cellReferenceField = new Ext.form.TextField( {
		name : "cell_reference",
		width : 415,
		fieldLabel : "Text to display".localize(),
		hideLabel : true,
		value : "A1",
		enableKeyEvents : true
	});
	var cellReferenceField2 = new Ext.form.TextField( {
		name : "cell_reference2",
		width : 415,
		value : "A1",
		hideLabel : true,
		enableKeyEvents : true
	});
	var cellReferenceField3 = new Ext.form.TextField( {
		name : "cell_reference3",
		width : 385,
		value : "A1",
		hideLabel : false,
		labelStyle : "width: 210px; margin-top:10px;",
		fieldLabel : "_cell_sheet_reference".localize(),
		style : "margin-top: 10px;",
		enableKeyEvents : true
	});
	cellReferenceField.on("keyup", function() {
		var tmpName = cellReferenceField.getValue();
		var activeNode = tree.getSelectionModel().getSelectedNode().text;
		_urlSel = ((activeNode) ? activeNode : firstNode).concat("!", tmpName)
	});
	var eMail = new Ext.form.TextField( {
		name : "e_mail",
		width : 415,
		fieldLabel : "E-mail address".localize(),
		hideLabel : true,
		vtype : "email",
		enableKeyEvents : true
	});
	eMail.on("keyup", function() {
		var tmpName = eMail.getValue();
		tmpName = "mailto:".concat(tmpName);
		if (mailSubject.getValue() != "") {
			_urlSel = tmpName.concat("?subject=", mailSubject.getValue())
		} else {
			_urlSel = tmpName
		}
	});
	var mailSubject = new Ext.form.TextField( {
		name : "mail_subject",
		width : 415,
		fieldLabel : "Subject".localize(),
		hideLabel : true,
		enableKeyEvents : true
	});
	mailSubject.on("keyup", function() {
		var tmpName = mailSubject.getValue();
		tmpName = "?subject=".concat(tmpName);
		if ((mailSubject.getValue() != "") && (eMail.getValue() != "")) {
			_urlSel = "mailto:".concat(eMail.getValue(), "?subject=",
					mailSubject.getValue())
		}
	});
	var newDocName = new Ext.form.TextField( {
		name : "document_name",
		width : 415,
		fieldLabel : "Subject".localize(),
		hideLabel : true,
		enableKeyEvents : true
	});
	newDocName.on("keyup", function() {
		var tmpName = newDocName.getValue();
		_urlSel = tmpName
	});
	var editDocLater = new Ext.form.Radio( {
		name : "edit_doc",
		boxLabel : "Edit the new document later".localize(),
		hideLabel : true
	});
	var editDocNow = new Ext.form.Radio( {
		name : "edit_doc",
		boxLabel : "Edit the new document now".localize(),
		hideLabel : true
	});
	var treeNodes = new Ext.tree.AsyncTreeNode( {
		children : [ {
			text : "Cell Reference".localize(),
			expanded : true,
			id : "cellref",
			children : cellRefChildren
		}, {
			text : "Defined Names".localize(),
			expanded : true,
			id : "defname",
			children : definedNames
		} ]
	});
	SampleTreePanel = Ext.extend(Ext.tree.TreePanel, {
		width : 415,
		height : 123,
		loader : new Ext.tree.TreeLoader(),
		rootVisible : false,
		border : false,
		lines : true,
		autoScroll : true,
		singleExpand : false,
		useArrows : true,
		initComponent : function() {
			Ext.apply(this, {
				root : treeNodes
			});
			SampleTreePanel.superclass.initComponent.apply(this, arguments)
		}
	});
	Ext.reg("tree_panel", SampleTreePanel);
	var _tmpName;
	var _tmp;
	var tree = new SampleTreePanel();
	var _actNRScope;
	tree.on("click", function(node, e) {
		if ((node.text == "Cell Reference".localize())
				|| (node.text == "Defined Names".localize())) {
			cellReferenceField.disable();
			isNR = false
		} else {
			if (node.id.charAt(0) == "N") {
				_actNRScope = node.attributes.scope;
				cellReferenceField.disable();
				textToDisplay.setRawValue(node.text);
				isNR = true;
				_urlSel = node.text;
				_tmp = node.id.replace(/N_/i, "");
				_tmp = parseInt(_tmp);
				if (definedNamesStore[_tmp] != "Workbook") {
					_tmpName = definedNamesStore[_tmp].concat("!", node.text)
				}
			} else {
				cellReferenceField.enable();
				firstNode = node.text;
				_urlSel = (node.text)
						.concat("!", cellReferenceField.getValue());
				isNR = false
			}
		}
	}, this);
	var webaddress = new Ext.form.TextField( {
		name : "web_address",
		width : 415,
		fieldLabel : "Web Address".localize(),
		hideLabel : true,
		enableKeyEvents : true
	});
	var mainFields = new Ext.Panel(
			{
				id : "main_field",
				layout : "card",
				deferredRender : true,
				baseCls : "x-plain",
				bodyStyle : "padding-left: 10px;",
				defaults : {
					bodyStyle : "padding-top: 0px;"
				},
				border : false,
				activeItem : 0,
				items : [
						{
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; #margin-top: 10px;",
							border : false,
							id : "open-dlgss",
							layout : "form",
							autoHeight : true,
							width : 605,
							frame : false,
							items : [
									{
										border : true,
										id : "open-dlg",
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; #margin-top: 0px;",
										height : 310,
										width : 600,
										layout : "fit",
										frame : false,
										items : [ openDlg ]
									}, cellReferenceField3 ]
						},
						{
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; #margin-top: 0px;",
							border : true,
							autoHeight : true,
							autoWidth : true,
							xtype : "fieldset",
							id : "thisDoc",
							layout : "form",
							frame : false,
							items : [
									{
										html : "Select a place in this document"
												.localize().concat(":"),
										baseCls : "x-plain"
									},
									tree,
									{
										html : "Type the cell reference"
												.localize().concat(":"),
										baseCls : "x-plain",
										bodyStyle : "margin-top: 10px;"
									}, cellReferenceField ]
						},
						{
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; #margin-top: 10px;",
							border : true,
							autoHeight : true,
							autowidth : true,
							xtype : "fieldset",
							layout : "form",
							id : "webPage",
							frame : false,
							items : [ {
								html : "Address".localize().concat(":"),
								baseCls : "x-plain"
							}, webaddress ]
						},
						{
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; #margin-top: 10px;",
							border : true,
							autoHeight : true,
							autowidth : true,
							xtype : "fieldset",
							id : "eMail",
							layout : "form",
							frame : false,
							items : [ {
								html : "E-mail address".localize().concat(":"),
								baseCls : "x-plain"
							}, eMail, {
								html : "Subject".localize().concat(":"),
								baseCls : "x-plain"
							}, mailSubject ]
						},
						{
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; #margin-top: 10px;",
							border : true,
							autoHeight : true,
							autowidth : true,
							xtype : "fieldset",
							layout : "form",
							id : "fromCell",
							frame : false,
							items : [
									{
										html : "Type the cell reference"
												.localize().concat(":"),
										baseCls : "x-plain"
									}, cellReferenceField2 ]
						} ]
			});
	var navigation = [ [ "Existing File".localize(), "hl_file_web" ],
			[ "Place in This Document".localize(), "hl_place_this" ],
			[ "Web page".localize(), "hl_file_web" ],
			[ "E-mail Address".localize(), "hl_email" ] ];
	var navigationStore = new Ext.data.SimpleStore( {
		fields : [ "description", "image" ],
		data : navigation
	});
	var navigationView = new Ext.DataView(
			{
				id : "hyperlink-navigation",
				store : navigationStore,
				tpl : new Ext.XTemplate(
						'<div class="hyperlink-navigation"><tpl for=".">',
						'<div class="thumb-wrap">',
						'<div class="thumb" style="padding: 5px; text-align: center; font-size: 11px;"><img class="{image}" width="18" height="18" src="../lib/ext/resources/images/default/s.gif" /><br />{description}</div></div>',
						'</tpl><div style="margin: 0px; padding: 0px; font-size: 1px;"><img class="hl_nav_sep" width="134" height="1" src="../lib/ext/resources/images/default/s.gif" /></div>',
						'<div class="thumb-wrap">',
						'<div class="thumb" style="padding: 5px; text-align: center; font-size: 11px;"><img class="hl_place_this" width="18" height="18" src="../lib/ext/resources/images/default/s.gif" /><br />',
						"From Cell".localize(), "</div></div>", "</div>"),
				autoHeight : true,
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				emptyText : "No images to display".localize(),
				listeners : {
					click : {
						fn : onCategorySelect,
						scope : this
					}
				}
			});
	var textToDisplay = new Ext.form.TextField( {
		name : "text_to_display",
		width : 370,
		tabIndex : 101,
		value : initCellValue,
		hideLabel : true,
		blankText : "This field is required".localize(),
		fieldLabel : "Text to display".localize()
	});
	var screenTip = new Ext.form.TextField( {
		name : "screen_tip",
		width : 370,
		tabIndex : 103,
		hideLabel : true,
		fieldLabel : "Screen tip".localize()
	});
	var textToDisplayHld = new Ext.Panel( {
		id : "txt_fld_holder",
		layout : "column",
		baseCls : "x-plain",
		bodyStyle : "margin-top: 5px;",
		border : false,
		frame : false,
		items : [ {
			columnWidth : 0.17,
			autoHeight : true,
			baseCls : "x-plain",
			border : false,
			style : "text-align: right; margin-right: 10px;",
			frame : false,
			items : [ {
				html : "Text to display".localize().concat(":"),
				baseCls : "x-plain"
			} ]
		}, {
			columnWidth : 0.21,
			xtype : "fieldset",
			autoHeight : true,
			baseCls : "x-plain",
			border : false,
			frame : false,
			items : [ refSelCombo ]
		}, {
			columnWidth : 0.62,
			layout : "form",
			xtype : "fieldset",
			autoHeight : true,
			baseCls : "x-plain",
			border : false,
			frame : false,
			items : [ textToDisplay ]
		} ]
	});
	var screenTipHld = new Ext.Panel( {
		id : "tip_fld_holder",
		layout : "column",
		baseCls : "x-plain",
		bodyStyle : "margin-top: 5px;",
		border : false,
		frame : false,
		items : [ {
			columnWidth : 0.17,
			autoHeight : true,
			baseCls : "x-plain",
			border : false,
			frame : false,
			style : "text-align: right; margin-right: 10px;",
			items : [ {
				html : "Screen tip".localize().concat(":"),
				baseCls : "x-plain"
			} ]
		}, {
			columnWidth : 0.21,
			xtype : "fieldset",
			autoHeight : true,
			baseCls : "x-plain",
			border : false,
			frame : false,
			items : [ refSelComboTip ]
		}, {
			columnWidth : 0.62,
			layout : "form",
			xtype : "fieldset",
			autoHeight : true,
			baseCls : "x-plain",
			border : false,
			frame : false,
			items : [ screenTip ]
		} ]
	});
	var staticFields = new Ext.Panel(
			{
				id : "static_fields",
				bodyStyle : "padding-left: 10px; color: #000000; font-size: 9pt; background-color: transparent; margin-bottom: 5px;",
				border : false,
				frame : false,
				autoHeight : true,
				autoWidth : true,
				layout : "form",
				items : [ textToDisplayHld, screenTipHld ]
			});
	var gridData = [];
	var gridStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "seleFrom"
		}, {
			name : "trFrom"
		}, {
			name : "te"
		}, {
			name : "seleTo"
		}, {
			name : "trTo"
		}, {
			name : "tf"
		} ]
	});
	gridStore.loadData(gridData);
	var typeData = [ [ "range", "Selection".localize() ],
			[ "nrange", "Named Range".localize() ],
			[ "var", "Variable".localize() ],
			[ "cval", "Constant Value".localize() ],
			[ "clist", "Constant List".localize() ] ];
	var rowEditorTriggerClss = {
		range : "hl-triggerFld-shSel",
		nrange : "hl-triggerFld-namedRange",
		"var" : "hl-triggerFld-atVar",
		clist : "hl-triggerFld-cList",
		cval : "hl-triggerFld-cVal"
	};
	var typeStoreFrom = new Ext.data.SimpleStore( {
		fields : [ "type", "name" ]
	});
	var typeStoreTo = new Ext.data.SimpleStore( {
		fields : [ "type", "name" ]
	});
	typeStoreFrom.loadData(typeData);
	typeStoreTo.loadData(typeData);
	var _activeRow = 0;
	var _activeColumn = 0;
	var editor = new Ext.ux.grid.RowEditor( {
		saveText : "Update",
		id : "gridRowEditor",
		listeners : {
			afteredit : function(el, changed) {
				var ndat = grid.getSelectionModel().getSelected().data;
				var tmpArray = [ ndat.seleFrom, ndat.trFrom, ndat.te,
						ndat.seleTo, ndat.trTo, ndat.tf ];
				gridData[_activeRow] = tmpArray;
				gridStore.loadData(gridData)
			}
		}
	});
	var rColEdit = false;
	function displayTrigger(trigger, display) {
		var trg = Ext.getCmp(trigger);
		var w = trg.wrap.getWidth();
		if (!display) {
			trg.trigger.dom.style.display = "none";
			trg.el.setWidth(w - trg.trigger.getWidth())
		} else {
			trg.trigger.dom.style.display = "";
			trg.el.setWidth(w - 17)
		}
	}
	var grid = new Ext.grid.GridPanel(
			{
				store : gridStore,
				width : 600,
				height : 350,
				hideHeaders : true,
				ctCls : "insertHyperlink-Grid",
				id : "hlTransferGrid",
				clicksToEdit : 1,
				autoExpandColumn : "seleFrom",
				plugins : [ editor ],
				viewConfig : {
					forceFit : true
				},
				listeners : {
					rowclick : function(th, index, ev) {
					},
					cellclick : function(grid, rowIndex, columnIndex, e) {
						_activeRow = rowIndex;
						_activeColumn = columnIndex
					}
				},
				columns : [
						{
							id : "seleFrom",
							header : "Transfer From".localize(),
							dataIndex : "seleFrom",
							width : 100,
							sortable : false,
							editor : new Ext.form.ComboBox(
									{
										editable : false,
										triggerAction : "all",
										store : typeStoreFrom,
										mode : "local",
										displayField : "name",
										lazyRender : true,
										listClass : "x-combo-list-small",
										listeners : {
											select : function(el, rec, index) {
												grid.getSelectionModel()
														.getSelected().data.te = typeData[index][0];
												var rEditor = Ext
														.getCmp("triggerEditorFld");
												var stil = rowEditorTriggerClss[typeData[index][0]];
												rEditor.trigger.dom.innerHTML = '<img src="/ui/lib/ext/resources/images/default/s.gif" class="x-form-trigger '
														.concat(
																stil,
																'"><img src="/ui/lib/ext/resources/images/default/s.gif" class="x-form-trigger ',
																stil, '">');
												if (typeData[index][0] == "cval") {
													displayTrigger(
															"triggerEditorFld",
															false)
												} else {
													displayTrigger(
															"triggerEditorFld",
															true)
												}
											}
										}
									})
						},
						{
							header : "Key".localize(),
							id : "trFrom",
							dataIndex : "trFrom",
							width : 140,
							sortable : false,
							editor : new Ext.form.TriggerField(
									{
										triggerClass : "",
										id : "triggerEditorFld",
										onTriggerClick : function() {
											var stil = grid.getSelectionModel()
													.getSelected().data.te;
											var tData = grid
													.getSelectionModel()
													.getSelected().data;
											switch (stil) {
											case "range":
												Jedox.wss.app
														.load(
																Jedox.wss.app.dynJSRegistry.selectRange,
																[ {
																	fnc : [
																			this,
																			selSetRange ],
																	format : "{Sheet}!{Range}",
																	rng : Ext
																			.getCmp(
																					"triggerEditorFld")
																			.getValue()
																} ]);
												_wf = "";
												win.hide();
												break;
											case "clist":
												var initArray = tData.trFrom;
												initArray = initArray.replace(
														/ /g, "");
												initArray = initArray
														.split(",");
												var clWin = new Ext.Window(
														{
															title : "Constants List Selection"
																	.localize(),
															closable : true,
															id : "constListSelWin",
															autoDestroy : true,
															plain : true,
															cls : "default-format-window",
															constrain : true,
															modal : true,
															resizable : false,
															animCollapse : false,
															width : 450,
															height : 350,
															layout : "fit",
															listeners : {
																close : function() {
																	Jedox.wss.general
																			.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
																}
															},
															items : [ new ConstListEditor(
																	{
																		id : "constListEditor",
																		style : "margin: 10px;",
																		baseCls : "x-plain",
																		list : initArray
																	}) ],
															buttons : [
																	{
																		text : "OK"
																				.localize(),
																		handler : function() {
																			var lista = Ext
																					.getCmp(
																							"constListEditor")
																					.getList();
																			lista = lista
																					.join(",");
																			Ext
																					.getCmp(
																							"triggerEditorFld")
																					.setValue(
																							lista);
																			Ext
																					.getCmp(
																							"constListSelWin")
																					.destroy()
																		}
																	},
																	{
																		text : "Cancel"
																				.localize(),
																		handler : function() {
																			Ext
																					.getCmp(
																							"constListSelWin")
																					.destroy()
																		}
																	} ]
														});
												clWin.show(this);
												break;
											case "var":
												var preselection = tData.trFrom;
												Jedox.wss.app
														.load(
																Jedox.wss.app.dynJSRegistry.hlVarList,
																[ selSetVar,
																		preselection ]);
												break;
											case "nrange":
												var presele = tData.trFrom;
												presele = presele.split("!");
												if (presele.length > 1) {
													presele = presele[1]
												} else {
													presele = presele[0]
												}
												Jedox.wss.app
														.load(
																Jedox.wss.app.dynJSRegistry.nameManager,
																[ genNRGrid,
																		presele ]);
												break
											}
										},
										triggerConfig : {
											tag : "span",
											cls : "hl-triggerFld-shSel",
											cn : [
													{
														tag : "img",
														src : Ext.BLANK_IMAGE_URL,
														cls : "x-form-trigger "
																+ this.triggerClass
													},
													{
														tag : "img",
														src : Ext.BLANK_IMAGE_URL,
														cls : "x-form-trigger hl-triggerFld-shSel"
													} ]
										},
										listeners : {
											beforeshow : function(ed) {
												var stil = rowEditorTriggerClss[grid
														.getSelectionModel()
														.getSelected().data.te];
												if (this.trigger) {
													this.trigger.dom.innerHTML = '<img src="/ui/lib/ext/resources/images/default/s.gif" class="x-form-trigger '
															.concat(
																	stil,
																	'"><img src="/ui/lib/ext/resources/images/default/s.gif" class="x-form-trigger ',
																	stil, '">')
												}
												this.triggerClass = stil;
												this.triggerConfig = {
													tag : "span",
													cls : stil,
													cn : [
															{
																tag : "img",
																src : Ext.BLANK_IMAGE_URL,
																cls : "x-form-trigger "
																		+ this.triggerClass
															},
															{
																tag : "img",
																src : Ext.BLANK_IMAGE_URL,
																cls : "x-form-trigger "
																		.concat(stil)
															} ]
												}
											},
											show : function(ed) {
												var st = grid
														.getSelectionModel()
														.getSelected().data.te;
												setTimeout(
														function() {
															if (st == "cval") {
																displayTrigger(
																		"triggerEditorFld",
																		false)
															} else {
																displayTrigger(
																		"triggerEditorFld",
																		true)
															}
														}, 1)
											}
										}
									})
						},
						{
							id : "seleTo",
							header : "Transfer to".localize(),
							dataIndex : "seleTo",
							width : 100,
							sortable : false,
							editor : new Ext.form.ComboBox(
									{
										editable : false,
										triggerAction : "all",
										store : typeStoreTo,
										mode : "local",
										displayField : "name",
										lazyRender : true,
										listClass : "x-combo-list-small",
										listeners : {
											select : function(el, rec, index) {
												grid.getSelectionModel()
														.getSelected().data.tf = typeData[index][0];
												var rEditor = Ext
														.getCmp("triggerEditorTo");
												var stil = rowEditorTriggerClss[typeData[index][0]];
												rEditor.trigger.dom.innerHTML = '<img src="/ui/lib/ext/resources/images/default/s.gif" class="x-form-trigger '
														.concat(
																stil,
																'"><img src="/ui/lib/ext/resources/images/default/s.gif" class="x-form-trigger ',
																stil, '">');
												if (typeData[index][0] == "cval") {
													displayTrigger(
															"triggerEditorTo",
															false)
												} else {
													if ((_activeMode == 0)
															&& (typeData[index][0] == "range" || typeData[index][0] == "nrange")) {
														displayTrigger(
																"triggerEditorTo",
																false)
													} else {
														displayTrigger(
																"triggerEditorTo",
																true)
													}
												}
											}
										}
									})
						},
						{
							header : "Value".localize(),
							id : "trTo",
							dataIndex : "trTo",
							width : 140,
							sortable : false,
							editor : new Ext.form.TriggerField(
									{
										id : "triggerEditorTo",
										onTriggerClick : function() {
											var stil = grid.getSelectionModel()
													.getSelected().data.tf;
											var tData = grid
													.getSelectionModel()
													.getSelected().data;
											switch (stil) {
											case "range":
												if (_activeMode == 0) {
													return
												}
												rColEdit = true;
												Jedox.wss.app
														.load(
																Jedox.wss.app.dynJSRegistry.selectRange,
																[ {
																	fnc : [
																			this,
																			selSetRange ],
																	format : "{Sheet}!{Range}",
																	rng : Ext
																			.getCmp(
																					"triggerEditorTo")
																			.getValue()
																} ]);
												_wf = "";
												win.hide();
												break;
											case "nrange":
												if (_activeMode == 0) {
													return
												}
												var presele = tData.trTo;
												presele = presele.split("!");
												if (presele.length > 1) {
													presele = presele[1]
												} else {
													presele = presele[0]
												}
												rColEdit = true;
												Jedox.wss.app
														.load(
																Jedox.wss.app.dynJSRegistry.nameManager,
																[ genNRGrid,
																		presele ]);
												break;
											case "clist":
												var initArray = tData.trFrom;
												initArray = initArray.replace(
														/ /g, "");
												initArray = initArray
														.split(",");
												var clWin = new Ext.Window(
														{
															title : "Constants List Selection"
																	.localize(),
															closable : true,
															id : "constListSelWin",
															autoDestroy : true,
															plain : true,
															constrain : true,
															cls : "default-format-window",
															modal : true,
															resizable : false,
															animCollapse : false,
															width : 450,
															height : 350,
															layout : "fit",
															listeners : {
																close : function() {
																	Jedox.wss.general
																			.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
																}
															},
															items : [ new ConstListEditor(
																	{
																		id : "constListEditor",
																		style : "margin: 10px;",
																		baseCls : "x-plain",
																		list : initArray
																	}) ],
															buttons : [
																	{
																		text : "OK"
																				.localize(),
																		handler : function() {
																			var lista = Ext
																					.getCmp(
																							"constListEditor")
																					.getList();
																			lista = lista
																					.join(",");
																			Ext
																					.getCmp(
																							"triggerEditorTo")
																					.setValue(
																							lista);
																			Ext
																					.getCmp(
																							"constListSelWin")
																					.destroy()
																		}
																	},
																	{
																		text : "Cancel"
																				.localize(),
																		handler : function() {
																			Ext
																					.getCmp(
																							"constListSelWin")
																					.destroy()
																		}
																	} ]
														});
												clWin.show(this);
												break;
											case "var":
												var preselection = tData.trFrom;
												rColEdit = true;
												Jedox.wss.app
														.load(
																Jedox.wss.app.dynJSRegistry.hlVarList,
																[ selSetVar,
																		preselection ]);
												break
											}
										},
										triggerClass : "hl-triggerFld-shSel",
										triggerConfig : {
											tag : "span",
											cls : "hl-triggerFld-shSel",
											cn : [
													{
														tag : "img",
														src : Ext.BLANK_IMAGE_URL,
														cls : "x-form-trigger "
																+ this.triggerClass
													},
													{
														tag : "img",
														src : Ext.BLANK_IMAGE_URL,
														cls : "x-form-trigger hl-triggerFld-shSel"
													} ]
										},
										listeners : {
											beforeshow : function(ed) {
												var stil = rowEditorTriggerClss[grid
														.getSelectionModel()
														.getSelected().data.tf];
												if (this.trigger) {
													this.trigger.dom.innerHTML = '<img src="/ui/lib/ext/resources/images/default/s.gif" class="x-form-trigger '
															.concat(
																	stil,
																	'"><img src="/ui/lib/ext/resources/images/default/s.gif" class="x-form-trigger ',
																	stil, '">')
												}
												this.triggerClass = stil;
												this.triggerConfig = {
													tag : "span",
													cls : stil,
													cn : [
															{
																tag : "img",
																src : Ext.BLANK_IMAGE_URL,
																cls : "x-form-trigger "
																		+ this.triggerClass
															},
															{
																tag : "img",
																src : Ext.BLANK_IMAGE_URL,
																cls : "x-form-trigger "
																		.concat(stil)
															} ]
												}
											},
											show : function(ed) {
												var st = grid
														.getSelectionModel()
														.getSelected().data.tf;
												setTimeout(
														function() {
															if ((_activeMode == 0)
																	&& (st == "range" || st == "nrange")) {
																displayTrigger(
																		"triggerEditorTo",
																		false)
															} else {
																displayTrigger(
																		"triggerEditorTo",
																		true)
															}
														}, 1)
											}
										}
									})
						} ]
			});
	function genNRGrid(gpan, _pre) {
		var nGrid = gpan;
		var nrWin = new Ext.Window( {
			title : "Named Range Selection".localize(),
			closable : true,
			id : "nrangeListSelWin",
			autoDestroy : true,
			plain : true,
			cls : "default-format-window",
			constrain : true,
			modal : true,
			resizable : false,
			animCollapse : false,
			width : 500,
			height : 400,
			layout : "fit",
			items : [ nGrid ],
			buttons : [
					{
						text : "OK".localize(),
						handler : function() {
							var hp = nGrid.getSelectionModel().getSelected();
							var fld = (rColEdit) ? "triggerEditorTo"
									: "triggerEditorFld";
							if (hp.data.scope != "Workbook") {
								Ext.getCmp(fld).setValue(
										(hp.data.scope).concat("!",
												hp.data.name))
							} else {
								Ext.getCmp(fld).setValue(hp.data.name)
							}
							Ext.getCmp("nrangeListSelWin").destroy()
						}
					}, {
						text : "Cancel".localize(),
						handler : function() {
							Ext.getCmp("nrangeListSelWin").destroy()
						}
					} ]
		});
		nrWin.show(this);
		if (_pre && _pre != "") {
			var store = nGrid.getStore();
			var ind = store.find("name", _pre);
			setTimeout(function() {
				nGrid.getSelectionModel().selectRow(ind)
			}, 1)
		}
	}
	function selSetRange(selected) {
		win.show();
		setTimeout(function() {
			if (rColEdit) {
				Ext.getCmp("triggerEditorTo").setValue(selected)
			} else {
				Ext.getCmp("triggerEditorFld").setValue(selected)
			}
			rColEdit = false
		}, 1)
	}
	function selSetVar(selVar) {
		setTimeout(function() {
			if (rColEdit) {
				Ext.getCmp("triggerEditorTo").setValue(selVar)
			} else {
				Ext.getCmp("triggerEditorFld").setValue(selVar)
			}
			rColEdit = false
		}, 1)
	}
	var addBtn = new Ext.Button(
			{
				id : "addButton",
				disabled : false,
				style : "display: inline;",
				ctCls : "stdButtons",
				text : "Add".localize(),
				handler : function(b, e) {
					gridData.push( [ "Selection", "", "range", "Selection", "",
							"range" ]);
					gridStore.loadData(gridData)
				}
			});
	var deleBtn = new Ext.Button( {
		id : "deleButton",
		disabled : false,
		ctCls : "stdButtons",
		style : "display: inline; margin-left: 5px;",
		text : "Delete".localize(),
		handler : function(b, e) {
			gridData.splice(_activeRow, 1);
			gridStore.loadData(gridData)
		}
	});
	var addDeleBtn = new Ext.Panel(
			{
				id : "tr_buttons",
				baseCls : "x-plain",
				bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; padding-top: 0px;",
				border : false,
				frame : false,
				items : [ addBtn, deleBtn ]
			});
	var gridTitleHolder = new Ext.Panel(
			{
				id : "gridTitleHolder",
				layout : "column",
				baseCls : "x-plain",
				bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; margin-top: 10px; margin-bottom: 5px;",
				border : false,
				frame : false,
				items : [ {
					columnWidth : 0.48,
					layout : "form",
					autoHeight : true,
					baseCls : "x-plain",
					border : false,
					frame : false,
					items : [ {
						baseCls : "x-plain",
						html : "Transfer From (Key)"
					} ]
				}, {
					columnWidth : 0.52,
					layout : "form",
					autoHeight : true,
					baseCls : "x-plain",
					border : false,
					frame : false,
					items : [ {
						baseCls : "x-plain",
						html : "Transfer To (Key)"
					} ]
				} ]
			});
	var gridHolder = new Ext.Panel( {
		id : "grid_holder",
		baseCls : "x-plain",
		border : false,
		frame : false,
		autoHeight : true,
		width : 600,
		items : [ gridTitleHolder, grid, addDeleBtn ]
	});
	var tabs = new Ext.TabPanel( {
		region : "center",
		xtype : "tabpanel",
		layoutOnTabChange : true,
		activeTab : 0,
		ctCls : "tb-no-bg",
		height : 450,
		baseCls : "x-plain",
		bodyStyle : "background-color: transparent; padding: 5px;",
		defaults : {
			autoScroll : false
		},
		items : [ {
			id : "target",
			title : "Target".localize(),
			baseCls : "x-plain",
			items : [ staticFields, mainFields ]
		}, {
			id : "transfer",
			title : "Transfer".localize(),
			baseCls : "x-plain",
			items : [ gridHolder ]
		} ],
		listeners : {
			tabchange : function(el, e) {
				if (e.id == "transfer") {
					gridStore.loadData(gridData)
				}
			}
		}
	});
	var win = new Ext.Window(
			{
				title : "Insert Hyperlink".localize(),
				closable : true,
				autoDestroy : true,
				id : "insert-hyperlink-dialog",
				plain : true,
				constrain : true,
				modal : true,
				cls : "default-format-window",
				resizable : false,
				animCollapse : false,
				width : 830,
				height : 580,
				autoHeight : true,
				items : [ new Ext.Panel(
						{
							baseCls : "x-title-f",
							labelWidth : 100,
							labelAlign : "left",
							bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
							frame : false,
							header : false,
							monitorValid : true,
							autoScroll : false,
							autoHeight : true,
							autoWidth : true,
							items : [ new Ext.Panel(
									{
										id : "hyperlink-window",
										layout : "column",
										baseCls : "x-plain",
										border : false,
										frame : false,
										items : [
												{
													columnWidth : 0.2,
													layout : "form",
													xtype : "fieldset",
													bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; padding-top: 10px;",
													autoHeight : true,
													baseCls : "x-plain",
													border : false,
													frame : false,
													items : [ {
														bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
														border : true,
														autoHeight : true,
														xtype : "fieldset",
														layout : "form",
														frame : false,
														title : "Link to"
																.localize()
																.concat(":"),
														items : [ navigationView ]
													} ]
												},
												{
													columnWidth : 0.8,
													layout : "form",
													xtype : "fieldset",
													bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; padding-left: 10px;",
													autoHeight : true,
													autoScroll : false,
													border : false,
													frame : false,
													items : [ tabs ]
												} ]
									}) ]
						}) ],
				listeners : {
					close : function() {
						Jedox.wss.general
								.setInputMode(Jedox.wss.app.lastInputModeDlg);
						Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.openHL)
					}
				},
				buttons : [
						{
							text : "OK".localize(),
							handler : function() {
								if (Ext.getCmp("gridRowEditor").isVisible()) {
									Ext.getCmp("gridRowEditor").stopEditing(
											false)
								}
								if (isNR) {
									var named_range = _urlSel;
									_urlSel = ""
								} else {
									var named_range = ""
								}
								var screenTipVal = screenTip.getValue();
								var textVal = textToDisplay.getValue();
								if (!textVal || textVal == "") {
									Ext.Msg.show( {
										title : "Hyperlink Error",
										msg : "_error: empty hl name"
												.localize(),
										buttons : Ext.Msg.OK,
										fn : function() {
											textToDisplay.focus()
										},
										animEl : "elId",
										width : 320,
										icon : Ext.MessageBox.ERROR
									});
									return false
								}
								initCellValue = textToDisplay.getValue();
								switch (towhat) {
								case "towb":
									var ghn = openDlg.onInsertHyperlink();
									if (!ghn) {
										return
									}
									var lType;
									var lPath;
									var lSh = null;
									var cRef = cellReferenceField3.getValue();
									var lRng;
									cRef = cRef.split("!");
									if (cRef[1]) {
										lSh = cRef[0];
										cRef = cRef[1]
									} else {
										cRef = cRef[0]
									}
									if (isNR) {
										lRng = _urlSel;
										_urlSel = ""
									} else {
										lRng = cRef
									}
									hldata.link = {
										type : "obj",
										value : {
											type : "local",
											target : {
												path : ghn.p,
												sheet : lSh,
												range : lRng,
												ghnt : {
													g : ghn.g,
													h : ghn.h,
													n : ghn.n,
													t : ghn.t
												}
											}
										}
									};
									if (hldata.link.value.target.ghnt.t != "workbook") {
										delete hldata.link.value.target.range;
										delete hldata.link.value.target.sheet
									}
									break;
								case "tourl":
									var trg = webaddress.getValue();
									hldata.link = {
										type : "obj",
										value : {
											type : "url",
											target : trg
										}
									};
									break;
								case "tomail":
									var trg = _urlSel;
									hldata.link = {
										type : "obj",
										value : {
											type : "url",
											target : trg
										}
									};
									break;
								case "tosel":
									var ts = _urlSel.split("!");
									if (isNR) {
										if (_actNRScope != "Workbook") {
											var _ash = _actNRScope
										} else {
											var _ash = null
										}
										hldata.link = {
											type : "obj",
											value : {
												type : "local",
												target : {
													path : "SELF",
													sheet : _ash,
													range : named_range
												}
											}
										}
									} else {
										hldata.link = {
											type : "obj",
											value : {
												type : "local",
												target : {
													path : "SELF",
													sheet : ts[0],
													range : ts[1]
												}
											}
										}
									}
									hldata.link.value.target.ghnt = null;
									break;
								case "tocell":
									var cRef = cellReferenceField2.getValue();
									hldata.link = {
										type : "ref",
										value : cRef
									};
									break;
								default:
									break
								}
								hldata.text = {
									type : _txtSrc,
									value : textVal
								};
								hldata.tip = {
									type : _tipSrc,
									value : screenTipVal
								};
								hldata.trans = [];
								for ( var w = 0; w < gridData.length; w++) {
									var tobj = {
										src : {
											type : gridData[w][2],
											value : gridData[w][1]
										},
										dst : {
											type : gridData[w][5],
											value : gridData[w][4]
										}
									};
									if (tobj.src.value != ""
											&& tobj.dst.value != "") {
										hldata.trans.push(tobj)
									} else {
										Ext.Msg
												.show( {
													title : "Hyperlink Error",
													msg : "One or more entries in Transfer tab are empty. Please check and edit values, or delete entry.",
													buttons : Ext.Msg.OK,
													fn : function() {
													},
													animEl : "elId",
													width : 320,
													icon : Ext.MessageBox.ERROR
												});
										return false
									}
								}
								Jedox.wss.hl.set( [ rngStartCoord[0],
										rngStartCoord[1], rngEndCoord[0],
										rngEndCoord[1] ], hldata);
								win.close()
							}
						},
						{
							text : "Cancel".localize(),
							handler : function() {
								if (Ext.getCmp("gridRowEditor").isVisible()) {
									Ext.getCmp("gridRowEditor").stopEditing(
											false)
								}
								win.close()
							}
						} ]
			});
	win.show(this);
	setTimeout(function() {
		textToDisplay.focus()
	}, 50);
	if (_pre) {
		hldata = _pre;
		screenTip.setValue(_pre.tip.value);
		textToDisplay.setValue(_pre.text.value);
		if (_pre.tip.type == "ref") {
			refSelComboTip.setValue("Cell Reference".localize());
			_tipSrc = "ref"
		} else {
			refSelComboTip.setValue("Text".localize());
			_tipSrc = "string"
		}
		if (_pre.text.type == "ref") {
			refSelCombo.setValue("Cell Reference".localize());
			_txtSrc = "ref"
		} else {
			refSelCombo.setValue("Text".localize());
			_txtSrc = "string"
		}
		switch (_pre.link.type) {
		case "ref":
			navigationView.fireEvent("click", this, 4);
			navigationView.select(4);
			cellReferenceField2.setValue(_pre.link.value);
			towhat = "tocell";
			break;
		case "obj":
			if (_pre.link.value.type == "local") {
				var wd = _pre.link.value.target;
				typeStoreTo.removeAt(4);
				typeStoreTo.removeAt(3);
				if (wd.ghnt == null) {
					navigationView.select(1);
					navigationView.fireEvent("click", this, 1);
					towhat = "tosel";
					cellReferenceField.setValue(wd.range);
					_urlSel = (wd.sheet) ? wd.sheet.concat(wd.range) : wd.range;
					var ch = tree.getNodeById("cellref").findChild("text",
							wd.sheet);
					if (!ch) {
						ch = tree.getNodeById("defname").findChild("text",
								wd.range)
					}
					if (ch) {
						tree.selectPath(ch.getPath())
					} else {
						Ext.Msg.show( {
							title : "Hyperlink Error".localize(),
							msg : "_hl_missing_target_sheet_nr".localize(),
							buttons : Ext.Msg.OK,
							fn : function(btn) {
							},
							animEl : "elId",
							icon : Ext.MessageBox.ERROR
						})
					}
				} else {
					towhat = "towb";
					var preghn = _pre.link.value.target.ghnt;
					navigationView.select(0);
					var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : "Please wait..."
					});
					myMask.show();
					win.disable();
					openDlg.preselectPath( {
						g : preghn.g,
						h : preghn.h,
						n : preghn.n
					});
					myMask.hide();
					win.enable();
					if (_pre.link.value.target.sheet) {
						cellReferenceField3
								.setValue(_pre.link.value.target.sheet.concat(
										"!", _pre.link.value.target.range))
					} else {
						cellReferenceField3
								.setValue(_pre.link.value.target.range)
					}
				}
			} else {
				if (_pre.link.value.type == "url") {
					var e = _pre.link.value.target.indexOf("@", 0);
					if (e < 0) {
						navigationView.fireEvent("click", this, 2);
						navigationView.select(2);
						towhat = "tourl";
						webaddress.setValue(_pre.link.value.target)
					} else {
						var rawmail = _pre.link.value.target.split(/[:?=]/);
						eMail.setValue(rawmail[1]);
						mailSubject.setValue(rawmail[3]);
						_urlSel = _pre.link.value.target;
						navigationView.fireEvent("click", this, 3);
						navigationView.select(3);
						towhat = "tomail";
						Ext.getCmp("transfer").disable()
					}
				}
			}
			break
		}
		if (_pre.trans.length > 0) {
			gridData = [];
			var _types = {
				range : "Selection".localize(),
				nrange : "Named Range".localize(),
				"var" : "Variable".localize(),
				cval : "Constant Value".localize(),
				clist : "Constant List".localize()
			};
			for ( var e = 0; e < _pre.trans.length; e++) {
				gridData.push( [ _types[_pre.trans[e].src.type],
						_pre.trans[e].src.value, _pre.trans[e].src.type,
						_types[_pre.trans[e].dst.type],
						_pre.trans[e].dst.value, _pre.trans[e].dst.type ]);
				gridStore.loadData(gridData)
			}
		}
	} else {
		navigationView.select(0)
	}
	typeStoreTo.removeAt(4);
	typeStoreTo.removeAt(3)
};