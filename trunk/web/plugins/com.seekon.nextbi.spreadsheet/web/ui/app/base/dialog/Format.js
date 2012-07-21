Jedox.wss.dialog.formatCells = function(item, ev, addFn, preFmt) {
	var _fromDlgF = false;
	var _fontToDisable = false;
	if (Jedox.wss.app.environment.inputMode === Jedox.wss.grid.GridMode.DIALOG) {
		_fromDlgF = true
	} else {
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
	}
	var selCoord = Jedox.wss.app.environment.selectedCellCoords;
	var env = Jedox.wss.app.environment, rngStartCoord = env.lastRangeStartCoord, rngEndCoord = env.lastRangeEndCoord;
	var formatCellPreConf = Jedox.wss.app.activeBook.getRangeStyle(
			[ rngStartCoord[0], rngStartCoord[1], rngEndCoord[0],
					rngEndCoord[1] ], {
				backgroundColor : "",
				fontWeight : "",
				fontFamily : "",
				textDecoration : "",
				fontSize : "",
				fontStyle : "",
				color : "",
				textAlign : "",
				verticalAlign : "",
				textIndent : "",
				whiteSpace : "",
				direction : "",
				backgroundImage : ""
			});
	var _preFont = {
		fontWeight : formatCellPreConf.fontWeight,
		fontFamily : formatCellPreConf.fontFamily,
		textDecoration : formatCellPreConf.textDecoration,
		fontSize : formatCellPreConf.fontSize,
		fontStyle : formatCellPreConf.fontStyle,
		color : formatCellPreConf.color
	};
	var _preAlignment = {
		textAlign : formatCellPreConf.textAlign,
		verticalAlign : formatCellPreConf.verticalAlign,
		textIndent : formatCellPreConf.textIndent,
		whiteSpace : formatCellPreConf.whiteSpace,
		direction : formatCellPreConf.direction
	};
	var _preFill = {
		backgroundColor : formatCellPreConf.backgroundColor,
		backgroundImage : formatCellPreConf.backgroundImage
	};
	var initStateCF = {};
	if (item == "fromConditionalFormatting") {
		_preFont = {
			fontWeight : preFmt[1].fontWeight,
			fontFamily : preFmt[1].fontFamily,
			textDecoration : preFmt[1].textDecoration,
			fontSize : preFmt[1].fontSize,
			fontStyle : preFmt[1].fontStyle,
			color : preFmt[1].color
		};
		initStateCF.preFont = _preFont;
		_preAlignment = {
			textAlign : preFmt[1].textAlign,
			verticalAlign : preFmt[1].verticalAlign,
			textIndent : preFmt[1].textIndent,
			whiteSpace : preFmt[1].whiteSpace,
			direction : preFmt[1].direction
		};
		initStateCF.preAlignment = _preAlignment;
		_preFill = {
			backgroundColor : preFmt[1].backgroundColor,
			backgroundImage : preFmt[1].backgroundImage
		};
		initStateCF.preFill = _preFill;
		var _preBorders = preFmt[2];
		initStateCF.preBorders = _preBorders;
		var _preFormat = preFmt[0];
		var _preProtection = preFmt[3]
	} else {
		if (item == "fromPageSetup") {
			_preFont = {
				fontWeight : preFmt.fontWeight,
				fontFamily : preFmt.fontFamily,
				textDecoration : preFmt.textDecoration,
				fontSize : preFmt.fontSize,
				fontStyle : preFmt.fontStyle,
				color : preFmt.color
			};
			initStateCF.preFont = _preFont
		}
	}
	var fontHolder = new Ext.Panel( {
		id : "font",
		title : "Font".localize(),
		items : [ {} ]
	});
	var numberHolder = new Ext.Panel( {
		id : "number",
		title : "Number".localize(),
		items : [ {} ]
	});
	var alignmentHolder = new Ext.Panel( {
		id : "alignment",
		title : "Alignment".localize(),
		items : [ {} ]
	});
	var borderHolder = new Ext.Panel( {
		id : "border",
		title : "Border".localize(),
		items : [ {} ]
	});
	var fillHolder = new Ext.Panel( {
		id : "fill",
		title : "Fill".localize(),
		items : [ {} ]
	});
	var protectionHolder = new Ext.Panel( {
		id : "protection",
		title : "Protection".localize(),
		items : [ {} ]
	});
	var fontPann;
	var numberPann;
	var alignmentPann;
	var borderPann;
	var fillPann;
	var protectionPann;
	var isFromOther = false;
	var components = {
		number : false,
		alignment : false,
		font : false,
		border : false,
		fill : false,
		protection : false
	};
	var tabs = new Ext.TabPanel(
			{
				region : "center",
				xtype : "tabpanel",
				margins : "3 3 3 0",
				layoutOnTabChange : true,
				activeTab : 0,
				baseCls : "x-plain",
				defaults : {
					autoScroll : false,
					bodyStyle : "background-color: transparent;"
				},
				items : [ numberHolder, alignmentHolder, fontHolder,
						borderHolder, fillHolder, protectionHolder ],
				listeners : {
					tabchange : function(el, e) {
						if (!components[e.id]) {
							switch (e.id) {
							case "font":
								var fcb = function(fontPan) {
									fontPann = fontPan;
									fontHolder.removeAll();
									fontHolder.add(fontPan);
									fontHolder.doLayout()
								};
								Jedox.wss.app.load(
										Jedox.wss.app.dynJSRegistry.formatFont,
										[ fcb, _preFont, _fontToDisable ]);
								break;
							case "number":
								var ncb = function(numberPan) {
									numberPann = numberPan;
									numberHolder.removeAll();
									numberHolder.add(numberPan);
									numberHolder.doLayout()
								};
								if (item == "fromConditionalFormatting") {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.formatNumber,
													[ ncb, _preFormat, false,
															isFromOther ])
								} else {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.formatNumber,
													[ ncb, false, false,
															isFromOther ])
								}
								break;
							case "alignment":
								var acb = function(alignmentPan) {
									alignmentPann = alignmentPan;
									alignmentHolder.removeAll();
									alignmentHolder.add(alignmentPan);
									alignmentHolder.doLayout()
								};
								Jedox.wss.app
										.load(
												Jedox.wss.app.dynJSRegistry.formatAlignment,
												[ acb, _preAlignment, false ]);
								break;
							case "border":
								var bcb = function(borderPan) {
									borderPann = borderPan;
									borderHolder.removeAll();
									borderHolder.add(borderPan);
									borderHolder.doLayout()
								};
								if (item == "fromConditionalFormatting") {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.formatBorder,
													[ bcb, _preBorders, false,
															isFromOther ])
								} else {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.formatBorder,
													[ bcb, false, false,
															isFromOther ])
								}
								break;
							case "fill":
								var flcb = function(fillPan) {
									fillPann = fillPan;
									fillHolder.removeAll();
									fillHolder.add(fillPan);
									fillHolder.doLayout()
								};
								if (item == "fromConditionalFormatting") {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.formatFill,
													[ flcb, _preFill, false,
															true ])
								} else {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.formatFill,
													[ flcb, _preFill, false,
															false ])
								}
								break;
							case "protection":
								var pcb = function(protectionPan) {
									protectionPann = protectionPan;
									protectionHolder.removeAll();
									protectionHolder.add(protectionPan);
									protectionHolder.doLayout()
								};
								if (item == "fromConditionalFormatting") {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.formatProtection,
													[ pcb, _preProtection, true ])
								} else {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.formatProtection,
													[ pcb, false, false ])
								}
								break
							}
							components[e.id] = true
						}
					}
				}
			});
	var win = new Ext.Window(
			{
				title : "Format Cells".localize(),
				closable : true,
				autoDestroy : true,
				plain : true,
				constrain : true,
				cls : "default-format-window",
				modal : true,
				resizable : false,
				animCollapse : false,
				width : 475,
				height : 500,
				layout : "fit",
				items : [ tabs ],
				listeners : {
					beforedestroy : function() {
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.formatCells)
					},
					close : function() {
						if (!_fromDlgF) {
							Jedox.wss.general
									.setInputMode(Jedox.wss.app.lastInputModeDlg);
							Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
						} else {
						}
						if (components.alignment) {
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.formatAlignment)
						}
						if (components.border) {
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.formatBorder)
						}
						if (components.fill) {
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.formatFill)
						}
						if (components.font) {
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.formatFont)
						}
						if (components.number) {
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.formatNumber)
						}
						if (components.protection) {
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.formatProtection)
						}
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.formatCells);
						Jedox.wss.general.refreshCursorField()
					}
				},
				buttons : [
						{
							text : "OK".localize(),
							handler : function() {
								var fontFormat = {};
								var alignFormat = {};
								var fillFormat = {};
								var borderFormat = {};
								var numberFormat;
								var protectionFormat = null;
								function getFont(val) {
									fontFormat = val
								}
								function getAlignment(val) {
									alignFormat = val
								}
								function getFill(val) {
									fillFormat = val
								}
								function getBorder(val) {
									borderFormat = val
								}
								function getNumber(val) {
									numberFormat = val
								}
								function getProtection(val) {
									protectionFormat = val
								}
								var bgColorT = "";
								var formatToSet = {};
								if (components.font) {
									fontPann.fireEvent("doFontSelect", getFont);
									for ( var fnt in fontFormat) {
										formatToSet[fnt] = fontFormat[fnt]
									}
								}
								if (components.alignment) {
									alignmentPann.fireEvent(
											"doSelectAlignment", getAlignment);
									for ( var aln in alignFormat) {
										formatToSet[aln] = alignFormat[aln]
									}
								}
								if (components.fill) {
									fillPann.fireEvent("formatFill", getFill);
									for ( var fil in fillFormat) {
										formatToSet[fil] = fillFormat[fil]
									}
									if (formatToSet.backgroundColor == "#-1-1-1") {
										formatToSet.backgroundColor = "";
										bgColorT = "#-1-1-1"
									}
								}
								if (item == "fromConditionalFormatting") {
									if (!components.font) {
										for ( var fnt in initStateCF.preFont) {
											formatToSet[fnt] = initStateCF.preFont[fnt]
										}
									}
									if (!components.fill) {
										for ( var fill in initStateCF.preFill) {
											formatToSet[fill] = initStateCF.preFill[fill]
										}
									}
									if (!components.alignment) {
										for ( var al in initStateCF.preAlignment) {
											formatToSet[al] = initStateCF.preAlignment[al]
										}
										if (formatToSet.backgroundColor == "#-1-1-1") {
											formatToSet.backgroundColor = "";
											bgColorT = "#-1-1-1"
										}
									}
									var cellFormattingString;
									if (components.border) {
										borderPann.fireEvent("doFormatBorders",
												getBorder);
										for ( var brd in borderFormat) {
											formatToSet[brd] = borderFormat[brd]
										}
									} else {
										for ( var brd in initStateCF.preBorders) {
											formatToSet[brd] = initStateCF.preBorders[brd]
										}
									}
									if (components.number) {
										numberPann.fireEvent("doFormatNumber",
												getNumber);
										cellFormattingString = numberFormat
									}
									if (components.protection) {
										protectionPann.fireEvent("doLock",
												getProtection)
									}
									var FCtoCF = [ formatToSet,
											cellFormattingString,
											protectionFormat ];
									addFn(FCtoCF);
									win.close()
								} else {
									if (item == "fromPageSetup") {
										var FCtoPS = {
											fontFamily : fontFormat.fontFamily,
											textDecoration : fontFormat.textDecoration,
											fontWeight : fontFormat.fontWeight,
											fontSize : fontFormat.fontSize,
											fontStyle : fontFormat.fontStyle,
											color : fontFormat.color
										};
										win.close();
										addFn(FCtoPS)
									} else {
										if (components.number) {
											numberPann
													.fireEvent("doFormatNumber")
										}
										if (components.border) {
											borderPann
													.fireEvent("doFormatBorders")
										}
										if (components.protection) {
											protectionPann.fireEvent("doLock")
										}
										for ( var e in formatToSet) {
											if (!formatToSet[e]
													|| formatToSet[e] == ""
													|| formatToSet[e] == formatCellPreConf[e]) {
												delete formatToSet[e]
											}
										}
										if (bgColorT == "#-1-1-1") {
											formatToSet.backgroundColor = ""
										}
										Jedox.wss.style.set(formatToSet);
										win.close()
									}
								}
							}
						},
						{
							text : "Cancel".localize(),
							handler : function() {
								win.close();
								if (item == "fromPageSetup") {
									return false
								}
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
								Jedox.wss.general.refreshCursorField();
								Jedox.wss.app
										.unload(Jedox.wss.app.dynJSRegistry.formatCells)
							}
						} ]
			});
	if (item == "formatCellsBorders") {
		tabs.setActiveTab("border")
	}
	if (item == "fromConditionalFormatting") {
		tabs.remove("alignment");
		isFromOther = true;
		_fontToDisable = {
			effects : true,
			size : true,
			type : true
		}
	}
	if (item == "fromPageSetup") {
		tabs.remove("protection");
		tabs.remove("number");
		tabs.remove("alignment");
		tabs.remove("border");
		tabs.remove("fill")
	}
	win.show(this)
};