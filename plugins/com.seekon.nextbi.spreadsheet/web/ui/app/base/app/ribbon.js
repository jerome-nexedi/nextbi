Jedox.wss.app.initRibbon = function() {
	var ribbonButtonTemplate = new Ext.Template(
			'<table cellspacing="0" class="x-btn {3}"><tbody class="{4}">',
			'<tr><td class="x-btn-tl"><i>&#160;</i></td><td class="x-btn-tc"></td><td class="x-btn-tr"><i>&#160;</i></td></tr>',
			'<tr><td class="x-btn-ml"><i>&#160;</i></td><td class="x-btn-mc">',
			'<em class="{5}" unselectable="on"><button class="x-btn-text {2}" type="{1}"><div style="width: 32px;"></div></button>',
			"<div>{0}</div>",
			'</em></td><td class="x-btn-mr"><i>&#160;</i></td></tr>',
			'<tr><td class="x-btn-bl"><i>&#160;</i></td><td class="x-btn-bc"></td><td class="x-btn-br"><i>&#160;</i></td></tr>',
			"</tbody></table>");
	var ribbon = Jedox.wss.app.toolbar = {};
	var rngClearType = Jedox.wss.range.ContentType;
	var home = new Ext.Panel(
			{
				title : "Home".localize(),
				tbar : new Ext.Toolbar(
						{
							listeners : {
								overflowchange : function(e, r) {
									var tbbg = this.items.items;
									for ( var it = 0; it < tbbg.length; it++) {
										var tbgitems = tbbg[it].items.items;
										for ( var icl = 0; icl < tbgitems.length; icl++) {
											var currClass = tbgitems[icl].iconCls;
											if (currClass) {
												var newClass = (!tbbg[it]
														.isVisible()) ? currClass
														.replace(/32/i, "16")
														: currClass.replace(
																/16/i, "32");
												tbgitems[icl]
														.setIconClass(newClass)
											}
										}
									}
								}
							},
							items : [
									{
										xtype : "buttongroup",
										columns : 3,
										title : "File".localize(),
										height : 90,
										items : [
												new Ext.Button(
														{
															text : "New<br>document"
																	.localize(),
															scale : "large",
															rowspan : 3,
															ctCls : "ribbonButtons",
															iconCls : "new-icon32",
															iconAlign : "top",
															width : 40,
															tooltip : "Create new document"
																	.localize(),
															handler : Jedox.wss.action.newWorkbook,
															template : ribbonButtonTemplate
														}),
												{
													text : "Open".localize(),
													iconCls : "open-icon",
													tooltip : "Open document"
															.localize(),
													handler : function() {
														Jedox.wss.app
																.load(
																		Jedox.wss.app.dynJSRegistry.open,
																		[ "open" ])
													}
												},
												{
													xtype : "splitbutton",
													text : "Recent".localize(),
													id : "openRecentMenu",
													iconCls : "open-recent-icon",
													menu : {
														items : [],
														listeners : {
															beforeshow : function() {
																if (this.loaded) {
																	return true
																}
																var jwgnrl = Jedox.wss.general;
																Jedox.wss.backend.conn
																		.rpc(
																				[
																						jwgnrl,
																						jwgnrl.showRecent,
																						this,
																						Ext
																								.get("openRecentMenu"),
																						"bl" ],
																				"common",
																				"fetchRecent",
																				[
																						"files",
																						"spreadsheet" ]);
																return false
															},
															hide : function() {
																this.loaded = false
															}
														}
													}
												},
												(ribbon.saveItem = new Ext.Toolbar.Button(
														{
															text : "Save"
																	.localize(),
															iconCls : "save-icon",
															tooltip : "Save document"
																	.localize(),
															handler : Jedox.wss.book.save
														})),
												(ribbon.saveAsItem = new Ext.Toolbar.Button(
														{
															text : "Save as"
																	.localize(),
															iconCls : "saveas-icon",
															tooltip : "Save As document"
																	.localize(),
															handler : function() {
																Jedox.wss.app
																		.load(
																				Jedox.wss.app.dynJSRegistry.open,
																				[ "save" ])
															}
														})),
												{
													text : "Import".localize(),
													iconCls : "import-icon",
													handler : function() {
														Jedox.wss.app
																.load(Jedox.wss.app.dynJSRegistry.importFile)
													}
												},
												{
													xtype : "splitbutton",
													text : "Export".localize(),
													iconCls : "export-icon",
													menu : {
														items : [
																{
																	text : "XLSX"
																			.localize(),
																	iconCls : "icon_file_xlsx",
																	handler : Jedox.wss.action.exportToXLSX
																},
																{
																	text : "PDF"
																			.localize(),
																	iconCls : "icon_file_pdf",
																	handler : Jedox.wss.action.exportToPDF,
																	disabled : !Jedox.wss.app.fopper
																},
																{
																	text : "HTML"
																			.localize(),
																	iconCls : "icon_file_html",
																	handler : Jedox.wss.action.exportToHTML
																} ]
													}
												} ]
									},
									{
										xtype : "buttongroup",
										columns : 3,
										title : "Operation".localize(),
										bodyStyle : "padding: 10px 2px 10px 2px",
										height : 90,
										hidden : true,
										items : [
												(ribbon.undoItem = new Ext.Button(
														{
															text : "Undo"
																	.localize(),
															scale : "large",
															rowspan : 3,
															ctCls : "ribbonButtons",
															iconCls : "undo-icon32",
															iconAlign : "top",
															width : 40,
															tooltip : "Undo"
																	.localize(),
															handler : Jedox.wss.sheet.undo,
															template : ribbonButtonTemplate
														})),
												(ribbon.redoItem = new Ext.Button(
														{
															text : "Redo"
																	.localize(),
															scale : "large",
															rowspan : 3,
															ctCls : "ribbonButtons",
															iconCls : "redo-icon32",
															iconAlign : "top",
															width : 40,
															tooltip : "Redo"
																	.localize(),
															handler : Jedox.wss.sheet.redo,
															template : ribbonButtonTemplate
														})) ]
									},
									{
										xtype : "buttongroup",
										columns : 2,
										title : "Clipboard".localize(),
										height : 90,
										items : [
												(ribbon.paste = new Ext.Button(
														{
															xtype : "splitbutton",
															text : "Paste"
																	.localize(),
															scale : "large",
															rowspan : 3,
															ctCls : "ribbonButtons",
															iconCls : "paste-icon32",
															iconAlign : "top",
															width : 40,
															arrowAlign : "bottom",
															tooltip : "Paste"
																	.localize(),
															disabled : true,
															template : ribbonButtonTemplate,
															handler : Jedox.wss.action.paste,
															menu : [ (ribbon.pasteSpec = new Ext.menu.Item(
																	{
																		text : "Paste Special"
																				.localize(),
																		disabled : true,
																		handler : function() {
																			Jedox.wss.app
																					.load(
																							Jedox.wss.app.dynJSRegistry.pasteSpecial,
																							[])
																		}
																	})) ]
														})),
												{
													text : "Cut".localize(),
													iconCls : "cut-icon",
													handler : function() {
														Jedox.wss.action
																.cut(false)
													}
												},
												{
													text : "Copy".localize(),
													iconCls : "copy-icon",
													handler : function() {
														Jedox.wss.action
																.copy(false)
													}
												},
												{
													text : "Format Painter"
															.localize(),
													iconCls : "formatpainter-icon"
												} ]
									},
									{
										xtype : "buttongroup",
										columns : 6,
										title : "Font".localize(),
										bodyStyle : "padding-top:10px",
										height : 90,
										items : [
												new Ext.form.ComboBox(
														{
															id : "tbarFonts",
															displayField : "fontname",
															valueField : "fontdef",
															typeAhead : true,
															editable : true,
															mode : "local",
															triggerAction : "all",
															ctCls : "toolbar-combo",
															selectOnFocus : true,
															forceSelection : true,
															value : Jedox.wss.app.cnfDefaultFont,
															listWidth : 160,
															width : 140,
															colspan : 5,
															style : "margin-bottom: 1px",
															tpl : '<tpl for="."><div class="x-combo-list-item" style="font-family: {fontdef}; color: #15428B;">{fontname}</div></tpl>',
															store : new Ext.data.SimpleStore(
																	{
																		fields : [
																				"fontname",
																				"fontdef" ],
																		data : Jedox.wss.style.fonts
																	}),
															listeners : {
																select : {
																	fn : Jedox.wss.app.onFormatDropdownSelect,
																	scope : this
																},
																focus : {
																	fn : Jedox.wss.app.onFormatDropdownFocus,
																	scope : this
																},
																blur : {
																	fn : Jedox.wss.app.onFormatDropdownBlur,
																	scope : this
																}
															}
														}),
												new Ext.form.ComboBox(
														{
															id : "tbarFontSizes",
															displayField : "fontsize",
															valueField : "fontsize",
															typeAhead : true,
															editable : true,
															selectOnFocus : true,
															mode : "local",
															triggerAction : "all",
															forceSelection : false,
															value : Jedox.wss.app.cnfDefaultFontSize,
															listWidth : 110,
															width : 40,
															colspan : 1,
															style : "margin-bottom: 1px",
															allowBlank : false,
															tpl : '<tpl for="."><div class="x-combo-list-item" style="font-size: {fontsize}pt; color: #15428B;">{fontsize}</div></tpl>',
															store : new Ext.data.SimpleStore(
																	{
																		fields : [ "fontsize" ],
																		data : Jedox.wss.style.fontSizes
																	}),
															listeners : {
																change : {
																	fn : Jedox.wss.app.onFormatDropdownChange,
																	scope : this
																},
																select : {
																	fn : Jedox.wss.app.onFormatDropdownSelect,
																	scope : this
																},
																focus : {
																	fn : Jedox.wss.app.onFormatDropdownFocus,
																	scope : this
																},
																blur : {
																	fn : Jedox.wss.app.onFormatDropdownBlur,
																	scope : this
																},
																specialkey : {
																	fn : Jedox.wss.app.onFormatDropdownSpecKey,
																	scope : this
																}
															}
														}),
												(ribbon.bold = new Ext.Toolbar.Button(
														{
															id : "tbarBold",
															iconCls : "bold-icon",
															enableToggle : true,
															pressed : false,
															tooltip : "Bold"
																	.localize(),
															toggleHandler : Jedox.wss.app.onFormatItemToggle
														})),
												(ribbon.italic = new Ext.Toolbar.Button(
														{
															id : "tbarItalic",
															iconCls : "italic-icon",
															enableToggle : true,
															pressed : false,
															tooltip : "Italic"
																	.localize(),
															toggleHandler : Jedox.wss.app.onFormatItemToggle
														})),
												(ribbon.underline = new Ext.Toolbar.Button(
														{
															id : "tbarUnderline",
															iconCls : "underline-icon",
															enableToggle : true,
															pressed : false,
															tooltip : "Underline"
																	.localize(),
															toggleHandler : Jedox.wss.app.onFormatItemToggle
														})),
												(ribbon.border = new Ext.Toolbar.SplitButton(
														{
															id : "tbarBorder",
															iconCls : "border-icon",
															tooltip : "Bottom Border"
																	.localize(),
															handler : this.onBorderSelect,
															menu : {
																items : [
																		{
																			id : "brd-bottom-norm",
																			text : "Bottom Border"
																					.localize(),
																			iconCls : "icon-brd-bottom-norm",
																			handler : this.onBorderSelect
																		},
																		{
																			id : "brd-top-norm",
																			text : "Top Border"
																					.localize(),
																			iconCls : "icon-brd-top-norm",
																			handler : this.onBorderSelect
																		},
																		{
																			id : "brd-left-norm",
																			text : "Left Border"
																					.localize(),
																			iconCls : "icon-brd-left-norm",
																			handler : this.onBorderSelect
																		},
																		{
																			id : "brd-right-norm",
																			text : "Right Border"
																					.localize(),
																			iconCls : "icon-brd-right-norm",
																			handler : this.onBorderSelect
																		},
																		"-",
																		{
																			id : "brd-all-norm",
																			text : "All Borders"
																					.localize(),
																			iconCls : "icon-brd-all-norm",
																			handler : this.onBorderSelect
																		},
																		{
																			id : "brd-out-norm",
																			text : "Outside Borders"
																					.localize(),
																			iconCls : "icon-brd-out-norm",
																			handler : this.onBorderSelect
																		},
																		{
																			id : "brd-out-thick",
																			text : "Thick Outside Border"
																					.localize(),
																			iconCls : "icon-brd-out-thick",
																			handler : this.onBorderSelect
																		},
																		{
																			id : "brd-all-none",
																			text : "No Border"
																					.localize(),
																			iconCls : "icon-brd-all-none",
																			handler : this.onBorderSelect
																		},
																		"-",
																		{
																			id : "brd-top-bottom-norm",
																			text : "Top and Bottom Border"
																					.localize(),
																			iconCls : "icon-brd-top-bottom-norm",
																			handler : this.onBorderSelect
																		},
																		{
																			id : "brd-bottom-thick",
																			text : "Thick Bottom Border"
																					.localize(),
																			iconCls : "icon-brd-bottom-thick",
																			handler : this.onBorderSelect
																		},
																		{
																			id : "brd-top-norm-bottom-thick",
																			text : "Top and Thick Bottom Border"
																					.localize(),
																			iconCls : "icon-brd-top-norm-bottom-thick",
																			handler : this.onBorderSelect
																		},
																		"-",
																		{
																			id : "formatCellsBorders",
																			text : "More Borders"
																					.localize()
																					.concat(
																							"..."),
																			iconCls : "icon-brd-more",
																			handler : function() {
																				Jedox.wss.app
																						.load(
																								Jedox.wss.app.dynJSRegistry.formatCells,
																								[ "formatCellsBorders" ])
																			}
																		} ]
															}
														})),
												{
													xtype : "splitbutton",
													id : "tbarBgColorBtn",
													iconCls : "iconbgcolor",
													tooltip : "Fill Color"
															.localize(),
													handler : Jedox.wss.app.onColorSelect,
													menu : (ribbon.bgColor = new Ext.menu.Menu(
															{
																id : "tbarBgColor",
																colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
																cls : "wide-color-palette",
																iconCls : "no-icon",
																handler : Jedox.wss.app.onColorSelect,
																items : [
																		{
																			text : "No Color"
																					.localize(),
																			id : "bgNoColor",
																			handler : Jedox.wss.app.onColorSelect
																		},
																		new Ext.ColorPalette(
																				{
																					colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
																					handler : Jedox.wss.app.onColorSelect
																				}) ]
															}))
												},
												{
													xtype : "splitbutton",
													id : "tbarTextColorBtn",
													iconCls : "icontextcolor",
													tooltip : "Font Color"
															.localize(),
													handler : Jedox.wss.app.onColorSelect,
													menu : new Ext.menu.ColorMenu(
															{
																id : "tbarTextColor",
																iconCls : "no-icon",
																colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
																cls : "wide-color-palette",
																handler : Jedox.wss.app.onColorSelect
															})
												} ]
									},
									{
										xtype : "buttongroup",
										columns : 4,
										height : 90,
										title : "Alignment".localize(),
										id : "ribbonAlignment",
										items : [
												(ribbon.alignLeft = new Ext.Button(
														{
															id : "tbarAlignLeft",
															iconCls : "leftalign-icon32",
															text : "Left"
																	.localize()
																	.concat(
																			"..."),
															scale : "large",
															iconAlign : "top",
															width : 40,
															rowspan : 2,
															ctCls : "ribbonButtons",
															template : ribbonButtonTemplate,
															enableToggle : true,
															pressed : false,
															tooltip : "Align Text Left"
																	.localize(),
															toggleHandler : Jedox.wss.app.onFormatItemToggle,
															template : ribbonButtonTemplate
														})),
												(ribbon.alignCenter = new Ext.Button(
														{
															id : "tbarAlignCenter",
															iconCls : "centeralign-icon32",
															text : "Center"
																	.localize(),
															scale : "large",
															iconAlign : "top",
															width : 40,
															rowspan : 2,
															template : ribbonButtonTemplate,
															ctCls : "ribbonButtons",
															enableToggle : true,
															pressed : false,
															tooltip : "Align Text Center"
																	.localize(),
															toggleHandler : Jedox.wss.app.onFormatItemToggle
														})),
												(ribbon.alignRight = new Ext.Button(
														{
															id : "tbarAlignRight",
															iconCls : "rightalign-icon32",
															text : "Right"
																	.localize(),
															scale : "large",
															iconAlign : "top",
															width : 40,
															rowspan : 2,
															template : ribbonButtonTemplate,
															ctCls : "ribbonButtons",
															enableToggle : true,
															pressed : false,
															tooltip : "Align Text Right"
																	.localize(),
															toggleHandler : Jedox.wss.app.onFormatItemToggle
														})),
												{
													id : "tbarMergeCells",
													text : "Merge Cells"
															.localize(),
													iconCls : "mergecells-icon",
													tooltip : "Merge Cells"
															.localize(),
													handler : function() {
														Jedox.wss.action
																.mergeCells(false)
													}
												},
												{
													id : "tbarUnmergeCells",
													text : "Unmerge Cells"
															.localize(),
													iconCls : "unmergecells-icon",
													tooltip : "Unmerge Cells"
															.localize(),
													handler : function() {
														Jedox.wss.action
																.mergeCells(true)
													}
												} ]
									},
									{
										xtype : "buttongroup",
										columns : 4,
										title : "Cells".localize(),
										height : 90,
										items : [
												new Ext.Button(
														{
															xtype : "splitbutton",
															iconCls : "insertrow-icon32",
															text : "Insert"
																	.localize(),
															scale : "large",
															iconAlign : "top",
															width : 40,
															rowspan : 3,
															template : ribbonButtonTemplate,
															ctCls : "ribbonButtons",
															arrowAlign : "bottom",
															menu : [
																	(ribbon.delRow = new Ext.menu.Item(
																			{
																				text : "Delete Rows"
																						.localize(),
																				iconCls : "deleterow-icon",
																				handler : function() {
																					Jedox.wss.action
																							.insDelRowCol(
																									"del",
																									"row")
																				}
																			})),
																	(ribbon.delCol = new Ext.menu.Item(
																			{
																				text : "Delete Columns"
																						.localize(),
																				iconCls : "deletecolumn-icon",
																				handler : function() {
																					Jedox.wss.action
																							.insDelRowCol(
																									"del",
																									"col")
																				}
																			})),
																	"-",
																	{
																		text : "Insert Sheet"
																				.localize(),
																		iconCls : "insertsheet-icon",
																		handler : function() {
																			Jedox.wss.app.activeBook
																					.getSheetSelector()
																					.addSheet()
																		}
																	} ]
														}),
												new Ext.Button(
														{
															xtype : "splitbutton",
															iconCls : "deleterow-icon32",
															text : "Delete"
																	.localize(),
															scale : "large",
															iconAlign : "top",
															width : 40,
															rowspan : 3,
															template : ribbonButtonTemplate,
															ctCls : "ribbonButtons",
															arrowAlign : "bottom",
															menu : [
																	(ribbon.insRow = new Ext.menu.Item(
																			{
																				text : "Insert Rows"
																						.localize(),
																				iconCls : "insertrow-icon",
																				handler : function() {
																					Jedox.wss.action
																							.insDelRowCol(
																									"ins",
																									"row")
																				}
																			})),
																	(ribbon.insCol = new Ext.menu.Item(
																			{
																				text : "Insert Columns"
																						.localize(),
																				iconCls : "insertcolumn-icon",
																				handler : function() {
																					Jedox.wss.action
																							.insDelRowCol(
																									"ins",
																									"col")
																				}
																			})),
																	"-",
																	{
																		text : "Delete Sheet"
																				.localize()
																	} ]
														}),
												new Ext.Button(
														{
															xtype : "splitbutton",
															iconCls : "format-icon32",
															text : "Format"
																	.localize(),
															scale : "large",
															iconAlign : "top",
															width : 40,
															rowspan : 3,
															template : ribbonButtonTemplate,
															ctCls : "ribbonButtons",
															arrowAlign : "bottom",
															menu : {
																items : [
																		'<b class="menu-title">Cell Size</b>',
																		{
																			text : "Row Height"
																					.localize()
																					.concat(
																							"..."),
																			iconCls : "rowheight-icon",
																			handler : function() {
																				Jedox.wss.app
																						.load(
																								Jedox.wss.app.dynJSRegistry.formatColRow,
																								[
																										Jedox.wss.grid.headerType.ROW,
																										Jedox.wss.app.environment.cursorField.offsetHeight + 1 ])
																			}
																		},
																		{
																			text : "AutoFit Row Height"
																					.localize(),
																			handler : function() {
																				Jedox.wss.action
																						.resizeRowCol(Jedox.wss.grid.headerType.ROW)
																			}
																		},
																		"-",
																		{
																			text : "Column Width"
																					.localize(),
																			iconCls : "columnwidth-icon",
																			handler : function() {
																				Jedox.wss.app
																						.load(
																								Jedox.wss.app.dynJSRegistry.formatColRow,
																								[
																										Jedox.wss.grid.headerType.COLUMN,
																										Jedox.wss.app.environment.cursorField.offsetWidth + 2 ])
																			}
																		},
																		{
																			text : "AutoFit Column Width"
																					.localize(),
																			handler : function() {
																				Jedox.wss.action
																						.resizeRowCol(Jedox.wss.grid.headerType.COLUMN)
																			}
																		},
																		'<b class="menu-title">Organize Sheets</b>',
																		{
																			text : "Rename Sheet"
																					.localize()
																		},
																		{
																			text : "Move or Copy Sheet"
																					.localize()
																		},
																		"-",
																		{
																			text : "Format Cells"
																					.localize(),
																			iconCls : "formatcells-icon"
																		} ]
															}
														}),
												(ribbon.lock = new Ext.Button(
														{
															id : "tbarLock",
															text : "Lock<br>Unlock"
																	.localize(),
															iconCls : "lock-icon32",
															scale : "large",
															rowspan : 3,
															template : ribbonButtonTemplate,
															ctCls : "ribbonButtons",
															iconAlign : "top",
															width : 40,
															enableToggle : true,
															pressed : true,
															tooltip : "Item Lock/Unlock"
																	.localize(),
															toggleHandler : Jedox.wss.general.toggleCellLock
														})) ]
									},
									{
										xtype : "buttongroup",
										columns : 1,
										title : "Styles".localize(),
										height : 91,
										items : [ new Ext.Button(
												{
													xtype : "splitbutton",
													text : "Conditional<br>Formating"
															.localize(),
													scale : "large",
													rowspan : 3,
													template : ribbonButtonTemplate,
													ctCls : "ribbonButtons",
													iconCls : "conditionalformat-icon32",
													iconAlign : "top",
													width : 50,
													menu : {
														items : [
																{
																	text : "New Rule"
																			.localize()
																			.concat(
																					"..."),
																	iconCls : "newrule-icon",
																	handler : function() {
																		Jedox.wss.app
																				.load(Jedox.wss.app.dynJSRegistry.conditionalFormatting)
																	}
																},
																{
																	text : "Clear Rules"
																			.localize(),
																	iconCls : "clearrules-icon",
																	menu : {
																		items : [
																				{
																					text : "Clear Rules from Selected Cells"
																							.localize(),
																					handler : function() {
																						Jedox.wss.cndfmt
																								.remove(Jedox.wss.cndfmt.SCOPE_CURR_SEL)
																					}
																				},
																				{
																					text : "Clear Rules from Entire Sheet"
																							.localize(),
																					handler : function() {
																						Jedox.wss.cndfmt
																								.remove(Jedox.wss.cndfmt.SCOPE_CURR_WKS)
																					}
																				} ]
																	}
																},
																{
																	text : "Manage Rules"
																			.localize()
																			.concat(
																					"..."),
																	iconCls : "managerules-icon",
																	handler : function() {
																		Jedox.wss.app
																				.load(Jedox.wss.app.dynJSRegistry.manageConditionalFormatting)
																	}
																} ]
													}
												}) ]
									},
									{
										xtype : "buttongroup",
										columns : 1,
										title : "Editing".localize(),
										height : 90,
										items : [
												{
													xtype : "splitbutton",
													text : "Clear".localize(),
													iconCls : "clearall-icon",
													menu : {
														items : [
																{
																	text : "Clear All"
																			.localize(),
																	iconCls : "clearall-icon",
																	handler : function() {
																		Jedox.wss.action
																				.clear(rngClearType.ALL_CLEAR)
																	}
																},
																{
																	text : "Clear Formats"
																			.localize(),
																	iconCls : "clearformats-icon",
																	handler : function() {
																		Jedox.wss.action
																				.clear(rngClearType.STYLE
																						| rngClearType.FORMAT
																						| rngClearType.CNDFMT)
																	}
																},
																{
																	text : "Clear Contents"
																			.localize(),
																	iconCls : "clearcontents-icon",
																	handler : function() {
																		Jedox.wss.action
																				.clear(rngClearType.FORMULA)
																	}
																} ]
													}
												},
												{
													text : "Go To".localize(),
													iconCls : "goto-icon",
													tooltip : "Go To"
															.localize(),
													handler : function() {
														Jedox.wss.app
																.load(Jedox.wss.app.dynJSRegistry.goTo)
													}
												} ]
									},
									{
										xtype : "buttongroup",
										columns : 1,
										title : "Mode".localize(),
										height : 90,
										items : [
												(ribbon.hbQuickView = new Ext.Toolbar.Button(
														{
															iconCls : "quickviewpreview-icon",
															text : "Quick View"
																	.localize(),
															tooltip : "Quick View"
																	.localize(),
															enableToggle : true,
															pressed : false,
															toggleHandler : Jedox.wss.hb.run
														})),
												(ribbon.userModeView = new Ext.Toolbar.Button(
														{
															id : "tbarViewMode",
															text : "User Mode"
																	.localize(),
															iconCls : "userpreview-icon",
															tooltip : "Open User Mode"
																	.localize(),
															handler : Jedox.wss.app.openViewMode
														})) ]
									} ]
						})
			});
	var insert = new Ext.Panel(
			{
				title : "Insert".localize(),
				tbar : [
						{
							xtype : "buttongroup",
							columns : 2,
							title : "Ilustrations",
							bodyStyle : "padding:10px",
							height : 90,
							items : [ new Ext.Button( {
								text : "Picture".localize(),
								scale : "large",
								rowspan : 3,
								ctCls : "ribbonButtons",
								template : ribbonButtonTemplate,
								colspan : 2,
								width : 50,
								iconCls : "insertpicture-icon32",
								iconAlign : "top",
								handler : function() {
									Jedox.wss.dialog.openInsertPicture()
								}
							}) ]
						},
						{
							xtype : "buttongroup",
							columns : 1,
							title : "Links".localize(),
							bodyStyle : "padding:10px",
							height : 90,
							items : [ new Ext.Button(
									{
										text : "Hyperlink".localize(),
										scale : "large",
										rowspan : 3,
										ctCls : "ribbonButtons",
										template : ribbonButtonTemplate,
										width : 50,
										iconCls : "inserthl-icon32",
										iconAlign : "top",
										handler : function() {
											Jedox.wss.app
													.load(Jedox.wss.app.dynJSRegistry.openHL)
										}
									}) ]
						},
						{
							xtype : "buttongroup",
							columns : 3,
							title : "Charts".localize(),
							bodyStyle : "padding:10px",
							height : 90,
							items : [
									new Ext.Button(
											{
												text : "Chart".localize(),
												scale : "large",
												rowspan : 3,
												template : ribbonButtonTemplate,
												ctCls : "ribbonButtons",
												width : 50,
												iconCls : "insertchart-icon32",
												iconAlign : "top",
												handler : function() {
													Jedox.wss.app
															.load(
																	Jedox.wss.app.dynJSRegistry.chart,
																	[ "insert",
																			0 ])
												}
											}),
									new Ext.Button(
											{
												text : "Micro Chart".localize(),
												scale : "large",
												rowspan : 3,
												template : ribbonButtonTemplate,
												ctCls : "ribbonButtons",
												width : 50,
												iconCls : "insertmicrochart-icon32",
												iconAlign : "top",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.microChart)
												}
											}) ]
						} ]
			});
	var pageLayout = new Ext.Panel( {
		title : "Page Layout".localize(),
		tbar : [ {
			xtype : "buttongroup",
			columns : 2,
			title : "Page Setup".localize(),
			height : 90,
			items : [ new Ext.Button( {
				text : "Print<br>Preview".localize(),
				scale : "large",
				rowspan : 3,
				template : ribbonButtonTemplate,
				ctCls : "ribbonButtons",
				width : 40,
				iconCls : "printpreview-icon32",
				iconAlign : "top",
				handler : Jedox.wss.action.exportToHTML
			}), {
				text : "Page Setup".localize(),
				iconCls : "pagesetup-icon",
				handler : function() {
					Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.pageSetup)
				}
			} ]
		} ]
	});
	var formulas = new Ext.Panel(
			{
				title : "Formulas".localize(),
				tbar : [
						{
							xtype : "buttongroup",
							columns : 2,
							title : "Function".localize(),
							items : [ new Ext.Button(
									{
										text : "Insert<br>Funciton".localize(),
										scale : "large",
										rowspan : 3,
										template : ribbonButtonTemplate,
										ctCls : "ribbonButtons",
										width : 40,
										iconCls : "insertfunction-icon32",
										iconAlign : "top",
										handler : function() {
											Jedox.wss.app
													.load(Jedox.wss.app.dynJSRegistry.insertFunction)
										}
									}) ]
						},
						{
							xtype : "buttongroup",
							columns : 2,
							title : "Defined Names".localize(),
							height : 90,
							items : [ new Ext.Button(
									{
										text : "Name<br> Manager".localize(),
										scale : "large",
										width : 90,
										rowspan : 3,
										template : ribbonButtonTemplate,
										ctCls : "ribbonButtons",
										iconCls : "namedrange-icon32",
										iconAlign : "top",
										handler : function() {
											Jedox.wss.app
													.load(Jedox.wss.app.dynJSRegistry.nameManager)
										}
									}) ]
						},
						{
							xtype : "buttongroup",
							columns : 3,
							title : "Calculation".localize(),
							height : 90,
							items : [
									new Ext.Button( {
										text : "Refresh<br>Data".localize(),
										iconCls : "refresh-icon",
										scale : "large",
										rowspan : 3,
										template : ribbonButtonTemplate,
										ctCls : "ribbonButtons",
										width : 50,
										iconAlign : "top",
										handler : function() {
											Jedox.wss.app.activeSheet.recalc()
										}
									}),
									{
										xtype : "tbspacer",
										width : 10,
										rowspan : 3
									},
									{
										xtype : "checkbox",
										id : "autoRefreshDataMenu",
										boxLabel : "Auto-Refresh Data"
												.localize(),
										enableToggle : true,
										checked : false,
										handler : function() {
											this.checked ? Jedox.wss.app
													.load(Jedox.wss.app.dynJSRegistry.autoRefreshStart)
													: Jedox.wss.book
															.autoRefresh(0)
										},
										listeners : {
											beforerender : function() {
												this
														.setValue(Jedox.wss.app.activeBook._autoRefresh > 0)
											}
										}
									},
									{
										xtype : "checkbox",
										id : "autoCalcDataItem",
										boxLabel : "Auto-Calculate Data"
												.localize(),
										enableToggle : true,
										checked : false,
										handler : function() {
											Jedox.wss.general
													.autoCalc(this.checked)
										},
										listeners : {
											beforerender : function() {
												this
														.setValue(Jedox.wss.app.autoCalc)
											}
										}
									} ]
						} ]
			});
	var view = new Ext.Panel(
			{
				title : "View".localize(),
				tbar : [
						{
							xtype : "buttongroup",
							columns : 1,
							title : "Show/Hide".localize(),
							bodyStyle : "padding:0px 10px 0px 10px; ",
							height : 90,
							items : [ {
								xtype : "checkbox",
								boxLabel : "Formula Bar".localize(),
								enableToggle : true,
								checked : true,
								handler : function(btn, state) {
									Jedox.wss.app.hideShowFormulaBar(state)
								}
							}, {
								xtype : "checkbox",
								boxLabel : "Status Bar".localize(),
								enableToggle : true,
								checked : true,
								handler : function(btn, state) {
									Jedox.wss.app.statusBar.hideShow(state)
								}
							} ]
						},
						{
							xtype : "buttongroup",
							columns : 2,
							bodyStyle : "padding:0px 10px 10px 10px; ",
							title : "Window".localize(),
							height : 90,
							items : [
									new Ext.Button(
											{
												text : "Arrange <br>All"
														.localize(),
												scale : "large",
												width : 50,
												rowspan : 3,
												template : ribbonButtonTemplate,
												ctCls : "ribbonButtons",
												iconCls : "arrangeall-icon32",
												iconAlign : "top",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.arrangeWindows)
												}
											}),
									{
										text : "Hide".localize(),
										iconCls : "hide-icon",
										handler : function() {
											Jedox.wss.workspace.hideActiveWin()
										}
									},
									{
										text : "Unhide".localize(),
										iconCls : "unhide-icon",
										handler : function() {
											Jedox.wss.app
													.load(Jedox.wss.app.dynJSRegistry.unhideWindow)
										}
									},
									{
										xtype : "splitbutton",
										text : "Open documents".localize(),
										iconCls : "unhide-icon",
										menu : (ribbon.openDocs = new Ext.menu.Menu())
									} ]
						} ]
			});
	var developer = new Ext.Panel(
			{
				title : "Developer".localize(),
				tbar : [
						{
							xtype : "buttongroup",
							columns : 2,
							title : "Controls".localize(),
							height : 90,
							items : [
									new Ext.Button(
											{
												text : "Macro <br>Editor"
														.localize(),
												scale : "large",
												rowspan : 3,
												template : ribbonButtonTemplate,
												ctCls : "ribbonButtons",
												width : 40,
												iconCls : "macroeditor-icon32",
												iconAlign : "top",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.editMacro)
												}
											}),
									{
										text : "Combo Box".localize(),
										iconCls : "formcombobox-icon",
										handler : function() {
											Jedox.wss.app
													.load(
															Jedox.wss.app.dynJSRegistry.formatControl,
															[ {
																type : Jedox.wss.wsel.type.COMBO_BOX
															} ])
										}
									},
									{
										text : "Check Box".localize(),
										iconCls : "formcheckbox-icon",
										handler : function() {
											Jedox.wss.app
													.load(
															Jedox.wss.app.dynJSRegistry.formatControl,
															[ {
																type : Jedox.wss.wsel.type.CHECK_BOX
															} ])
										}
									},
									{
										text : "Button".localize(),
										iconCls : "formbutton-icon",
										handler : function() {
											Jedox.wss.app
													.load(
															Jedox.wss.app.dynJSRegistry.formatControl,
															[ {
																type : Jedox.wss.wsel.type.BUTTON
															} ])
										}
									} ]
						},
						{
							xtype : "buttongroup",
							columns : 2,
							title : "Dyna Ranges".localize(),
							height : 90,
							items : [ (ribbon.newHBVert = new Ext.Button( {
								id : "newHBVertTbar",
								text : "Vertical <br> Dyna Range".localize(),
								scale : "large",
								rowspan : 3,
								template : ribbonButtonTemplate,
								ctCls : "ribbonButtons",
								iconCls : "verdynarange-icon32",
								iconAlign : "top",
								handler : Jedox.wss.hb.addDynarange
							})), (ribbon.newHBHoriz = new Ext.Button( {
								id : "newHBHorizTbar",
								text : "Horizontal <br> Dyna Range".localize(),
								scale : "large",
								rowspan : 3,
								template : ribbonButtonTemplate,
								ctCls : "ribbonButtons",
								iconCls : "hordynarange-icon32",
								iconAlign : "top",
								handler : Jedox.wss.hb.addDynarange
							})) ]
						},
						{
							xtype : "buttongroup",
							columns : 1,
							title : "Mode".localize(),
							height : 90,
							items : [
									(ribbon.hbQuickView = new Ext.Toolbar.Button(
											{
												iconCls : "quickviewpreview-icon",
												text : "Quick View".localize(),
												tooltip : "Quick View"
														.localize(),
												enableToggle : true,
												pressed : false,
												toggleHandler : Jedox.wss.hb.run
											})),
									(ribbon.userModeView = new Ext.Toolbar.Button(
											{
												id : "tbarViewMode-d",
												text : "User Mode".localize(),
												iconCls : "userpreview-icon",
												tooltip : "Open User Mode"
														.localize(),
												handler : Jedox.wss.app.openViewMode
											})) ]
						} ]
			});
	var palo = new Ext.Panel(
			{
				title : "Palo",
				tbar : [
						{
							xtype : "buttongroup",
							columns : 2,
							title : "Create or Modify Reports".localize(),
							height : 90,
							items : [
									new Ext.Button(
											{
												text : "Paste <br>View"
														.localize(),
												scale : "large",
												rowspan : 3,
												ctCls : "ribbonButtons",
												template : ribbonButtonTemplate,
												width : 40,
												iconCls : "palopasteview-icon32",
												iconAlign : "top",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.pasteView)
												}
											}),
									{
										text : "Paste Elements".localize(),
										iconCls : "palopasteelements-icon",
										handler : function() {
											Jedox.wss.app
													.load(Jedox.wss.app.dynJSRegistry.selectElements)
										}
									},
									{
										text : "Paste Subset".localize(),
										iconCls : "palopastesubset-icon",
										handler : function() {
											Jedox.wss.app
													.load(Jedox.wss.app.dynJSRegistry.subsetEditor)
										}
									},
									{
										text : "Paste Function".localize(),
										iconCls : "palopastefunction-icon",
										handler : function() {
											Jedox.wss.app
													.load(Jedox.wss.app.dynJSRegistry.pasteDataFunctions)
										}
									} ]
						},
						{
							xtype : "buttongroup",
							columns : 2,
							title : "Control and Modify Palo".localize(),
							height : 90,
							items : [ {
								text : "Import Data".localize(),
								iconCls : "paloimportdata-icon",
								width : 120,
								handler : function() {
									Jedox.wss.app
											.load(Jedox.wss.app.dynJSRegistry.paloImport)
								}
							} ]
						}, {
							xtype : "buttongroup",
							columns : 1,
							title : "Save".localize(),
							height : 90,
							items : [ {
								text : "Save as Snapshot".localize(),
								iconCls : "palosaveassnapshoot-icon",
								disabled : true
							} ]
						} ]
			});
	var help = new Ext.Panel( {
		title : "Help".localize(),
		tbar : [ {
			xtype : "buttongroup",
			columns : 1,
			bodyStyle : "padding:0px 10px 10px 10px; ",
			height : 90,
			title : "Window",
			items : [ {
				text : "Jedox Online".localize(),
				iconCls : "hide-icon",
				handler : function() {
					window.open("http://www.jedox.com", "_blank")
				}
			} ]
		} ]
	});
	Jedox.wss.app.ribbon = new Ext.TabPanel( {
		id : "ribbon-main-panel",
		layoutOnTabChange : true,
		renderTo : "ribbon",
		items : [ home, insert, pageLayout, formulas, view, developer, palo,
				help ],
		activeTab : 0
	});
	Ext.DomQuery.selectNode("*[class*=iconbgcolor]").style.borderLeft = "solid 4px #FF0000";
	Ext.DomQuery.selectNode("*[class*=icontextcolor]").style.borderLeft = "solid 4px #FFFF00"
};