Jedox.wss.app.initMenuBar = function() {
	var menu = Jedox.wss.app.menubar = {}, rngClearType = Jedox.wss.range.ContentType;
	Jedox.wss.app.Menu = new Ext.Toolbar( {
		cls : "extmenubar"
	});
	Jedox.wss.app.Menu
			.add(
					{
						text : "File".localize(),
						cls : "extmenubaritem",
						menu : (menu.fileMenu = new Ext.menu.Menu(
								{
									id : "FileMenu",
									cls : "default-format-window",
									items : [
											{
												text : "New".localize().concat(
														"..."),
												iconCls : "icon_new_doc",
												handler : Jedox.wss.action.newWorkbook
											},
											{
												text : "Open".localize()
														.concat("..."),
												iconCls : "icon_open_doc",
												handler : function() {
													Jedox.wss.app
															.load(
																	Jedox.wss.app.dynJSRegistry.open,
																	[ "open" ])
												}
											},
											{
												text : "Open Recent".localize(),
												id : "openRecentMenu",
												menu : {
													items : [],
													cls : "default-format-window",
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
																					this.parentMenu.activeItem
																							.getEl(),
																					"tr",
																					this.parentMenu ],
																			"common",
																			"fetchRecent",
																			[
																					"files",
																					"spreadsheet" ]);
															return false
														}
													}
												}
											},
											{
												text : "Import".localize()
														.concat("..."),
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.importFile)
												}
											},
											{
												text : "Export".localize(),
												menu : {
													cls : "default-format-window",
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
											},
											{
												text : "Quick Publish"
														.localize(),
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.quickPublish)
												}
											},
											{
												text : "Close".localize(),
												handler : function() {
													if (Jedox.wss.workspace.activeWin != null) {
														Jedox.wss.workspace.activeWin
																.close()
													}
												}
											},
											"-",
											(menu.saveItem = new Ext.menu.Item(
													{
														text : "Save"
																.localize(),
														handler : Jedox.wss.book.save,
														iconCls : "icon_save_doc"
													})),
											(menu.saveAsItem = new Ext.menu.Item(
													{
														text : "Save as"
																.localize()
																.concat("..."),
														handler : function() {
															Jedox.wss.app
																	.load(
																			Jedox.wss.app.dynJSRegistry.open,
																			[ "save" ])
														}
													})),
											"-",
											{
												text : "Page Setup".localize()
														.concat("..."),
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.pageSetup)
												}
											},
											{
												text : "Print Area".localize(),
												disabled : true
											},
											{
												text : "Print Preview"
														.localize(),
												handler : Jedox.wss.action.exportToHTML
											} ],
									listeners : {
										hide : function() {
											this.items.get("openRecentMenu").menu.loaded = false
										}
									}
								}))
					},
					{
						text : "Edit".localize(),
						cls : "extmenubaritem",
						menu : new Ext.menu.Menu(
								{
									id : "EditMenu",
									cls : "default-format-window",
									items : [
											(menu.undoItem = new Ext.menu.Item(
													{
														text : "Undo"
																.localize(),
														iconCls : "icon_undo",
														handler : Jedox.wss.sheet.undo,
														hidden : true
													})),
											(menu.redoItem = new Ext.menu.Item(
													{
														text : "Redo"
																.localize(),
														iconCls : "icon_redo",
														handler : Jedox.wss.sheet.redo,
														hidden : true
													})),
											{
												text : "Cut".localize(),
												iconCls : "icon_cut",
												handler : function() {
													Jedox.wss.action.cut(false)
												}
											},
											{
												text : "Copy".localize(),
												iconCls : "icon_copy",
												handler : function() {
													Jedox.wss.action
															.copy(false)
												}
											},
											(menu.paste = new Ext.menu.Item(
													{
														text : "Paste"
																.localize(),
														iconCls : "icon_paste",
														disabled : true,
														handler : Jedox.wss.action.paste
													})),
											(menu.pasteSpec = new Ext.menu.Item(
													{
														text : "Paste Special"
																.localize()
																.concat("..."),
														disabled : true,
														handler : function() {
															Jedox.wss.app
																	.load(
																			Jedox.wss.app.dynJSRegistry.pasteSpecial,
																			[])
														}
													})),
											"-",
											{
												text : "Clear".localize(),
												iconCls : "icon_clear_all",
												menu : {
													cls : "default-format-window",
													items : [
															{
																text : "Clear All"
																		.localize(),
																iconCls : "icon_clear_all",
																handler : function() {
																	Jedox.wss.action
																			.clear(rngClearType.ALL_CLEAR)
																}
															},
															{
																text : "Clear Formats"
																		.localize(),
																iconCls : "icon_clear_formats",
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
																iconCls : "icon_clear_content",
																handler : function() {
																	Jedox.wss.action
																			.clear(rngClearType.FORMULA)
																}
															} ]
												}
											},
											(menu.delRow = new Ext.menu.Item(
													{
														text : "Delete Rows"
																.localize(),
														iconCls : "icon_del_row",
														handler : function() {
															Jedox.wss.action
																	.insDelRowCol(
																			"del",
																			"row")
														}
													})),
											(menu.delCol = new Ext.menu.Item( {
												text : "Delete Columns"
														.localize(),
												iconCls : "icon_del_col",
												handler : function() {
													Jedox.wss.action
															.insDelRowCol(
																	"del",
																	"col")
												}
											})),
											"-",
											{
												text : "Go To".localize()
														.concat("..."),
												iconCls : "icon_goto",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.goTo)
												}
											} ]
								})
					},
					{
						text : "View".localize(),
						cls : "extmenubaritem",
						menu : new Ext.menu.Menu( {
							id : "ViewMenu",
							cls : "default-format-window",
							items : [
									{
										text : "Toolbars".localize(),
										enableToggle : true,
										checked : true,
										checkHandler : function(btn, state) {
											Jedox.wss.app.hideShowToolbar(
													state, "")
										}
									},
									{
										text : "Formula Bar".localize(),
										enableToggle : true,
										checked : true,
										checkHandler : function(btn, state) {
											Jedox.wss.app
													.hideShowFormulaBar(state)
										}
									},
									{
										text : "Status Bar".localize(),
										enableToggle : true,
										checked : true,
										checkHandler : function(btn, state) {
											Jedox.wss.app.statusBar
													.hideShow(state)
										}
									},
									"-",
									(menu.hbQuickView = new Ext.menu.CheckItem(
											{
												text : "Quick View".localize(),
												enableToggle : true,
												checked : false,
												checkHandler : Jedox.wss.hb.run
											})),
									(menu.userModeView = new Ext.menu.Item( {
										text : "User Mode".localize(),
										handler : Jedox.wss.app.openViewMode
									})) ]
						})
					},
					{
						text : "Insert".localize(),
						cls : "extmenubaritem",
						menu : new Ext.menu.Menu(
								{
									id : "InsertMenu",
									cls : "default-format-window",
									items : [
											(menu.insRow = new Ext.menu.Item( {
												text : "Rows".localize(),
												iconCls : "icon_ins_row",
												handler : function() {
													Jedox.wss.action
															.insDelRowCol(
																	"ins",
																	"row")
												}
											})),
											(menu.insCol = new Ext.menu.Item( {
												text : "Columns".localize(),
												iconCls : "icon_ins_col",
												handler : function() {
													Jedox.wss.action
															.insDelRowCol(
																	"ins",
																	"col")
												}
											})),
											{
												text : "Worksheet".localize(),
												iconCls : "icon_ins_sheet",
												handler : function() {
													Jedox.wss.app.activeBook
															.getSheetSelector()
															.addSheet()
												}
											},
											{
												text : "Chart".localize()
														.concat("..."),
												handler : function() {
													Jedox.wss.app
															.load(
																	Jedox.wss.app.dynJSRegistry.chart,
																	[ "insert",
																			0 ])
												},
												iconCls : "icon_insert_chart"
											},
											{
												text : "Micro Chart".localize()
														.concat("..."),
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.microChart)
												},
												iconCls : "icon_insert_chart"
											},
											"-",
											{
												text : "Function".localize()
														.concat("..."),
												iconCls : "icon_ins_func",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.insertFunction)
												}
											},
											{
												text : "Name".localize(),
												iconCls : "icon_name_mgr",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.nameManager)
												}
											},
											"-",
											{
												text : "Picture".localize(),
												iconCls : "icon_ins_picture",
												handler : function() {
													Jedox.wss.dialog
															.openInsertPicture()
												}
											},
											{
												text : "Hyperlink".localize()
														.concat("..."),
												iconCls : "icon_insert_hyperlink",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.openHL)
												}
											} ]
								})
					},
					{
						text : "Format".localize(),
						cls : "extmenubaritem",
						menu : new Ext.menu.Menu(
								{
									id : "FormatMenu",
									cls : "default-format-window",
									items : [
											{
												text : "Cells".localize()
														.concat("..."),
												iconCls : "icon_edit",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.formatCells)
												}
											},
											"-",
											{
												text : "Row Height".localize()
														.concat("..."),
												iconCls : "icon_row_height",
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
												text : "Autofit Row Height"
														.localize(),
												handler : function() {
													Jedox.wss.action
															.resizeRowCol(Jedox.wss.grid.headerType.ROW)
												}
											},
											"-",
											{
												text : "Column Width"
														.localize().concat(
																"..."),
												iconCls : "icon_col_width",
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
												text : "Autofit Column Width"
														.localize(),
												handler : function() {
													Jedox.wss.action
															.resizeRowCol(Jedox.wss.grid.headerType.COLUMN)
												}
											},
											"-",
											{
												text : "Merge Cells".localize(),
												iconCls : "icon_merge_cells",
												handler : function() {
													Jedox.wss.action
															.mergeCells(false)
												}
											},
											{
												text : "Unmerge Cells"
														.localize(),
												iconCls : "icon_unmerge_cells",
												handler : function() {
													Jedox.wss.action
															.mergeCells(true)
												}
											},
											"-",
											{
												text : "Conditional Formatting"
														.localize(),
												iconCls : "icon_conditional_fmt",
												menu : {
													cls : "default-format-window",
													items : [
															{
																text : "New Rule"
																		.localize()
																		.concat(
																				"..."),
																iconCls : "icon_cnd_new",
																handler : function() {
																	Jedox.wss.app
																			.load(Jedox.wss.app.dynJSRegistry.conditionalFormatting)
																}
															},
															{
																text : "Clear Rules"
																		.localize(),
																iconCls : "icon_cnd_clear",
																menu : {
																	cls : "default-format-window",
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
																iconCls : "icon_cnd_manage",
																handler : function() {
																	Jedox.wss.app
																			.load(Jedox.wss.app.dynJSRegistry.manageConditionalFormatting)
																}
															} ]
												}
											} ]
								})
					},
					/*{
						text : "Palo".localize(),
						cls : "extmenubaritem",
						menu : new Ext.menu.Menu(
								{
									id : "PaloMenu",
									cls : "default-format-window",
									items : [
											{
												text : "Paste View".localize()
														.concat("..."),
												iconCls : "icon_palo_pasteview",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.pasteView)
												}
											},
											"-",
											{
												text : "Paste Elements"
														.localize().concat(
																"..."),
												iconCls : "icon_palo_pasteelements",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.selectElements)
												}
											},
											{
												text : "Paste Subset"
														.localize().concat(
																"..."),
												iconCls : "icon_palo_pastesubset",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.subsetEditor)
												}
											},
											{
												text : "Paste Data Function"
														.localize().concat(
																"..."),
												iconCls : "icon_palo_pastedatafn",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.pasteDataFunctions)
												}
											},
											"-",
											{
												text : "Data Import".localize()
														.concat("..."),
												iconCls : "icon_palo_dataimport",
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.paloImport)
												}
											},
											{
												text : "Save as Snapshot"
														.localize().concat(
																"..."),
												iconCls : "icon_palo_saveassnap",
												disabled : true
											} ]
								})
					},*/
					{
						text : "Data".localize(),
						cls : "extmenubaritem",
						menu : new Ext.menu.Menu(
								{
									id : "DataMenu",
									cls : "default-format-window",
									items : [
											{
												text : "Refresh Data"
														.localize(),
												handler : function() {
													Jedox.wss.app.activeSheet
															.recalc()
												}
											},
											{
												text : "Auto-Refresh Data"
														.localize(),
												id : "autoRefreshDataMenu",
												enableToggle : true,
												checked : false,
												checkHandler : function() {
													this.checked ? Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.autoRefreshStart)
															: Jedox.wss.book
																	.autoRefresh(0)
												}
											},
											"-",
											{
												text : "Auto-Calculate Data"
														.localize(),
												id : "autoCalcDataItem",
												enableToggle : true,
												checked : false,
												checkHandler : function() {
													Jedox.wss.general
															.autoCalc(this.checked)
												}
											} ],
									listeners : {
										beforeshow : function() {
											var aBook = Jedox.wss.app.activeBook;
											this.items
													.get("autoRefreshDataMenu")
													.setChecked(
															aBook
																	&& aBook._autoRefresh > 0,
															true);
											this.items
													.get("autoCalcDataItem")
													.setChecked(
															Jedox.wss.app.autoCalc,
															true)
										}
									}
								})
					},
					{
						text : "Tools".localize(),
						cls : "extmenubaritem",
						menu : new Ext.menu.Menu(
								{
									id : "ToolsMenu",
									cls : "default-format-window",
									items : [
											{
												text : "DynaRanges".localize(),
												iconCls : "icon_dynaranges",
												menu : {
													cls : "default-format-window",
													items : [
															(menu.newHBVert = new Ext.menu.Item(
																	{
																		id : "newHBVertMenubar",
																		text : "Vertical DynaRange"
																				.localize(),
																		iconCls : "icon_vert_dynarange",
																		handler : Jedox.wss.hb.addDynarange
																	})),
															(menu.newHBHoriz = new Ext.menu.Item(
																	{
																		id : "newHBHorizMenubar",
																		text : "Horizontal DynaRange"
																				.localize(),
																		iconCls : "icon_hor_dynarange",
																		handler : Jedox.wss.hb.addDynarange
																	})) ]
												}
											},
											"-",
											{
												text : "Form Elements"
														.localize(),
												iconCls : "icon_form_elems",
												menu : {
													cls : "default-format-window",
													items : [
															{
																text : "ComboBox"
																		.localize()
																		.concat(
																				"..."),
																iconCls : "icon_form_combo",
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
																text : "CheckBox"
																		.localize()
																		.concat(
																				"..."),
																iconCls : "icon_form_checkbox",
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
																text : "Button"
																		.localize()
																		.concat(
																				"..."),
																iconCls : "icon_form_button",
																handler : function() {
																	Jedox.wss.app
																			.load(
																					Jedox.wss.app.dynJSRegistry.formatControl,
																					[ {
																						type : Jedox.wss.wsel.type.BUTTON
																					} ])
																}
															} ]
												}
											},
											{
												text : "Macros".localize(),
												iconCls : "icon_macro",
												menu : {
													cls : "default-format-window",
													items : [ {
														text : "Macro Editor"
																.localize()
																.concat("..."),
														iconCls : "icon_macro_editor",
														handler : function() {
															Jedox.wss.app
																	.load(Jedox.wss.app.dynJSRegistry.editMacro)
														}
													} ]
												}
											},
											"-",
											{
												text : "Options".localize()
														.concat("..."),
												disabled : true
											} ]
								})
					},
					{
						text : "Window".localize(),
						cls : "extmenubaritem",
						menu : (menu.windowMenu = new Ext.menu.Menu(
								{
									id : "WindowMenu",
									cls : "default-format-window",
									items : [
											{
												text : "Arrange".localize()
														.concat("..."),
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.arrangeWindows)
												}
											},
											{
												text : "Hide".localize(),
												handler : function() {
													Jedox.wss.workspace
															.hideActiveWin()
												}
											},
											{
												text : "Unhide".localize()
														.concat("..."),
												handler : function() {
													Jedox.wss.app
															.load(Jedox.wss.app.dynJSRegistry.unhideWindow)
												}
											},
											"-",
											{
												text : "Freeze Panes"
														.localize(),
												disabled : true,
												menu : {
													cls : "default-format-window",
													items : [
															{
																text : "Freeze Panes"
																		.localize(),
																id : "FreezePane",
																handler : function() {
																	Jedox.wss.sheet
																			.freeze()
																}
															},
															{
																text : "Freeze Top Row"
																		.localize(),
																handler : function() {
																	Jedox.wss.sheet
																			.freeze( [
																					0,
																					1 ])
																}
															},
															{
																text : "Freeze First Column"
																		.localize(),
																handler : function() {
																	Jedox.wss.sheet
																			.freeze( [
																					1,
																					0 ])
																}
															} ],
													listeners : {
														beforeshow : function() {
															this.items
																	.get(
																			"FreezePane")
																	.setText(
																			Jedox.wss.app.activeSheet._numPanes > 1 ? "Unfreeze Panes"
																					.localize()
																					: "Freeze Panes"
																							.localize())
														}
													}
												}
											}, "-" ]
								}))
					}, {
						text : "Help".localize(),
						cls : "extmenubaritem",
						menu : new Ext.menu.Menu( {
							id : "HelpMenu",
							cls : "default-format-window",
							items : [
									{
										text : "Worksheet-Server Help"
												.localize(),
										disabled : true
									},
									"-",
									{
										text : "Jedox Online".localize(),
										handler : function() {
											window.open("http://www.jedox.com",
													"_blank")
										}
									} ]
						})
					});
	Jedox.wss.app.Menu.render("MenuPlaceholder")
};