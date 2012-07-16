Jedox.wss.app.initActiveToolbars = function() {
	Jedox.wss.app.toolbar = {};
	Jedox.wss.app.initStandardToolbar();
	Jedox.wss.app.initFormatToolbar();
	Jedox.wss.app.initDynarangeToolbar()
};
Jedox.wss.app.initStandardToolbar = function() {
	var tbar = Jedox.wss.app.toolbar;
	Jedox.wss.app.standardToolbar = new Ext.Toolbar( {
		cls : "exttoolbar",
		renderTo : "wssStandardToolbar",
		items : [
				{
					iconCls : "icon_new_doc",
					tooltip : "Create new document".localize(),
					handler : Jedox.wss.action.newWorkbook
				},
				{
					iconCls : "icon_open_doc",
					cls : "x-btn-icon",
					tooltip : "Open document".localize(),
					handler : function() {
						Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.open,
								[ "open" ])
					}
				}, (tbar.saveItem = new Ext.Toolbar.Button( {
					iconCls : "icon_save_doc",
					cls : "x-btn-icon",
					tooltip : "Save document".localize(),
					handler : Jedox.wss.book.save
				})), (tbar.undoItem = new Ext.Toolbar.Button( {
					id : "menuIconUndo",
					iconCls : "icon_undo",
					cls : "x-btn-icon",
					tooltip : "Undo".localize(),
					handler : Jedox.wss.sheet.undo,
					hidden : true
				})), (tbar.redoItem = new Ext.Toolbar.Button( {
					id : "menuIconRedo",
					iconCls : "icon_redo",
					cls : "x-btn-icon",
					tooltip : "Redo".localize(),
					handler : Jedox.wss.sheet.redo,
					hidden : true
				})), "-", {
					iconCls : "icon_copy",
					cls : "x-btn-icon",
					tooltip : "Copy".localize(),
					handler : function() {
						Jedox.wss.action.copy(false)
					}
				}, {
					iconCls : "icon_cut",
					cls : "x-btn-icon",
					tooltip : "Cut".localize(),
					handler : function() {
						Jedox.wss.action.cut(false)
					}
				}, (tbar.paste = new Ext.Toolbar.Button( {
					iconCls : "icon_paste",
					cls : "x-btn-icon",
					tooltip : "Paste".localize(),
					disabled : true,
					handler : Jedox.wss.action.paste
				})), "  " ]
	})
};
Jedox.wss.app.initFormatToolbar = function() {
	var tbar = Jedox.wss.app.toolbar;
	Jedox.wss.app.formatToolbar = new Ext.Toolbar(
			{
				cls : "exttoolbar",
				renderTo : "wssFormatToolbar",
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
									width : 120,
									tpl : '<tpl for="."><div class="x-combo-list-item" style="font-family: {fontdef}; color: #15428B;">{fontname}</div></tpl>',
									store : new Ext.data.SimpleStore( {
										fields : [ "fontname", "fontdef" ],
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
						" ",
						(tbar.fontSizes = new Ext.form.ComboBox(
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
									allowBlank : false,
									applyTo : tbar.fontSizes,
									tpl : '<tpl for="."><div class="x-combo-list-item" style="font-size: {fontsize}pt; color: #15428B;">{fontsize}</div></tpl>',
									store : new Ext.data.SimpleStore( {
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
								})),
						" ",
						(tbar.bold = new Ext.Toolbar.Button( {
							id : "tbarBold",
							iconCls : "icon_font_bold",
							cls : "x-btn-icon",
							enableToggle : true,
							pressed : false,
							toggleHandler : Jedox.wss.app.onFormatItemToggle,
							tooltip : "Bold".localize()
						})),
						(tbar.italic = new Ext.Toolbar.Button( {
							id : "tbarItalic",
							iconCls : "icon_font_italic",
							cls : "x-btn-icon",
							enableToggle : true,
							pressed : false,
							toggleHandler : Jedox.wss.app.onFormatItemToggle,
							tooltip : "Italic".localize()
						})),
						(tbar.underline = new Ext.Toolbar.Button( {
							id : "tbarUnderline",
							iconCls : "icon_font_underline",
							cls : "x-btn-icon",
							enableToggle : true,
							pressed : false,
							toggleHandler : Jedox.wss.app.onFormatItemToggle,
							tooltip : "Underline".localize()
						})),
						"-",
						(tbar.alignLeft = new Ext.Toolbar.Button( {
							id : "tbarAlignLeft",
							iconCls : "icon_align_left",
							cls : "x-btn-icon",
							enableToggle : true,
							pressed : false,
							toggleHandler : Jedox.wss.app.onFormatItemToggle,
							tooltip : "Align Text Left".localize()
						})),
						(tbar.alignCenter = new Ext.Toolbar.Button( {
							id : "tbarAlignCenter",
							iconCls : "icon_align_center",
							cls : "x-btn-icon",
							enableToggle : true,
							pressed : false,
							toggleHandler : Jedox.wss.app.onFormatItemToggle,
							tooltip : "Center text".localize()
						})),
						(tbar.alignRight = new Ext.Toolbar.Button( {
							id : "tbarAlignRight",
							iconCls : "icon_align_right",
							cls : "x-btn-icon",
							enableToggle : true,
							pressed : false,
							toggleHandler : Jedox.wss.app.onFormatItemToggle,
							tooltip : "Align Text Right".localize()
						})),
						"-",
						(tbar.border = new Ext.Toolbar.SplitButton(
								{
									id : "tbarBorder",
									iconCls : "icon-brd-bottom-norm",
									tooltip : "Bottom Border".localize(),
									handler : this.onBorderSelect,
									menu : {
										cls : "default-format-window",
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
															.localize().concat(
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
						new Ext.Toolbar.Button( {
							id : "tbarMergeCells",
							iconCls : "icon_merge_cells",
							cls : "x-btn-icon",
							handler : function() {
								Jedox.wss.action.mergeCells(false)
							},
							tooltip : "Merge Cells".localize()
						}),
						new Ext.Toolbar.Button( {
							id : "tbarUnmergeCells",
							iconCls : "icon_unmerge_cells",
							cls : "x-btn-icon",
							handler : function() {
								Jedox.wss.action.mergeCells(true)
							},
							tooltip : "Unmerge Cells".localize()
						}),
						"-",
						new Ext.Toolbar.SplitButton(
								{
									id : "tbarBgColorBtn",
									iconCls : "iconbgcolor",
									tooltip : "Fill Color".localize(),
									handler : Jedox.wss.app.onColorSelect,
									menu : (tbar.bgColor = new Ext.menu.Menu(
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
								}),
						new Ext.Toolbar.SplitButton(
								{
									id : "tbarTextColorBtn",
									iconCls : "icontextcolor",
									tooltip : "Font Color".localize(),
									handler : Jedox.wss.app.onColorSelect,
									menu : new Ext.menu.ColorMenu(
											{
												id : "tbarTextColor",
												iconCls : "no-icon",
												colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
												cls : "wide-color-palette",
												handler : Jedox.wss.app.onColorSelect
											})
								}),
						"-",
						new Ext.Toolbar.Button( {
							id : "menuIconChart",
							iconCls : "icon_insert_chart",
							cls : "x-btn-icon",
							tooltip : "Insert Chart".localize(),
							handler : function() {
								Jedox.wss.app.load(
										Jedox.wss.app.dynJSRegistry.chart, [
												"insert", 0 ])
							}
						}), "-", (tbar.lock = new Ext.Toolbar.Button( {
							id : "tbarLock",
							iconCls : "icon_lock",
							cls : "x-btn-icon",
							enableToggle : true,
							pressed : true,
							toggleHandler : Jedox.wss.general.toggleCellLock,
							tooltip : "Item Lock/Unlock".localize()
						})), "-" ]
			});
	Ext.DomQuery.selectNode("*[class*=iconbgcolor]").style.borderLeft = "solid 4px #FF0000";
	Ext.DomQuery.selectNode("*[class*=icontextcolor]").style.borderLeft = "solid 4px #FFFF00"
};
Jedox.wss.app.initDynarangeToolbar = function() {
	var tbar = Jedox.wss.app.toolbar;
	Jedox.wss.app.hbToolbar = new Ext.Toolbar( {
		cls : "exttoolbar",
		renderTo : "wssDynarangeToolbar",
		items : [ (tbar.newHBVert = new Ext.Toolbar.Button( {
			id : "newHBVertTbar",
			iconCls : "icon_vert_dynarange",
			cls : "x-btn-icon",
			tooltip : "Vertical Dynarange".localize(),
			handler : Jedox.wss.hb.addDynarange
		})), (tbar.newHBHoriz = new Ext.Toolbar.Button( {
			id : "newHBHorizTbar",
			iconCls : "icon_hor_dynarange",
			cls : "x-btn-icon",
			tooltip : "Horizontal Dynarange".localize(),
			handler : Jedox.wss.hb.addDynarange
		})), (tbar.hbQuickView = new Ext.Toolbar.Button( {
			id : "tbarHBQuickView",
			iconCls : "icon_designer_preview",
			cls : "x-btn-icon",
			tooltip : "Designer Preview".localize(),
			enableToggle : true,
			pressed : false,
			toggleHandler : Jedox.wss.hb.run
		})), "-", (tbar.userModeView = new Ext.Toolbar.Button( {
			id : "tbarViewMode",
			iconCls : "icon_user_mode",
			cls : "x-btn-icon",
			tooltip : "Open User Mode".localize(),
			handler : Jedox.wss.app.openViewMode
		})) ]
	})
};