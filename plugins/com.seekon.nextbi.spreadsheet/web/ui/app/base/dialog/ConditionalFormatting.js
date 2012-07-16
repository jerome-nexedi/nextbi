Jedox.wss.dialog.openConditionalFormatting = function(from, addParams, toEdit) {
	var initActiveItem = 0;
	var winw = 650;
	var rules = [];
	var _tmpRule = {
		sit : true
	};
	var _style;
	var _applies_to;
	var _pre = toEdit;
	var _wf;
	var _type;
	var _operator;
	var _operands;
	var _sit;
	var _format;
	var _protection;
	var _style;
	var _appliesTo;
	var uniFButton = Ext.extend(Ext.Button, {
		iconCls : "select-range-icon",
		cls : "x-btn-icon",
		tooltip : "Select Range".localize(),
		initComponent : function() {
			var that = this;
			Ext.apply(this, {});
			uniFButton.superclass.initComponent.call(this)
		}
	});
	if (!_pre) {
		_pre = {
			type : "",
			operator : "",
			operands : [ "", "" ],
			sit : true,
			format : "",
			style : "",
			borders : {},
			lock : true
		}
	}
	if (!_pre.operands) {
		_pre.operands = ""
	}
	_tmpRule = _pre;
	_style = _pre.style;
	_applies_to = _pre.applies_to;
	if (_pre.id) {
		var _id = _pre.id
	}
	_type = _pre.type;
	_operator = _pre.operator;
	_operands = _pre.operands;
	_sit = _pre.sit;
	_format = _pre.format;
	_style = _pre.style;
	_borders = _pre.borders;
	_protection = _pre.lock;
	var _fromDlgF = false;
	if (Jedox.wss.app.environment.inputMode === Jedox.wss.grid.GridMode.DIALOG) {
		_fromDlgF = true
	} else {
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
	}
	var cndfmt = Jedox.wss.cndfmt;
	var CTCSelected;
	var colorPalette = new Ext.ColorPalette(
			{
				colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
				cls : "wide-color-palette"
			});
	var mainMenuData = [
			[ "Format only cells that contain".localize().concat("...") ],
			[ "Format only top or bottom ranked values".localize() ],
			[ "Format only values that are above or below average".localize() ],
			[ "Format only unique or duplicate values".localize() ],
			[ "Use a formula to determine which cells to format".localize() ] ];
	var mainMenuStore = new Ext.data.SimpleStore( {
		fields : [ "type" ],
		data : mainMenuData
	});
	function selRange(selected) {
		win.show();
		setTimeout(function() {
			var f = Ext.getCmp(_wf);
			f.setValue(selected);
			_wf = ""
		}, 1)
	}
	var _ai_main;
	var _ai_topbot;
	var _ab_bel;
	var _unique;
	function chooseMenu(dView, index, node, ev) {
		Ext.getCmp("main_display").layout.setActiveItem(index);
		index = index + 1;
		_ai_main = index;
		if (index == 1) {
			win.setWidth(650);
			_type = "cell_value";
			_tmpRule.type = _type;
			if (!_operator) {
				_operator = "between";
				_tmpRule.operator = _operator
			}
		} else {
			win.setWidth(500)
		}
		if (index == 0) {
			win.setHeight(460);
			previewPanel.hide()
		} else {
			win.setHeight(420);
			previewPanel.show()
		}
		if (index == 2) {
			if (!_ai_topbot) {
				_ai_topbot = "top"
			}
		}
		if (index == 3) {
			if (!_ab_bel) {
				_ab_bel = ">"
			}
		}
		if (index == 4) {
			if (!_unique) {
				_unique = "duplicate_value"
			}
		}
		if (index == 5) {
			_tmpRule = {
				type : "formula_true"
			}
		}
	}
	var _ai_text;
	var _ai_cell_val;
	function setCTCCard(combo, record, index) {
		Ext.getCmp("CTC_card").layout.setActiveItem(index);
		if ((index == 0) || (index == 1)) {
			win.setWidth(650);
			CTCRight.setWidth(460);
			CTCCellValue.setWidth(460)
		} else {
			win.setWidth(500);
			CTCRight.setWidth(300)
		}
		CTCSelected = CTCComboData[index][1];
		_type = CTCSelected;
		_tmpRule.type = _type;
		if (_type == "text") {
			if (!_ai_text) {
				_ai_text = "contained"
			}
			_tmpRule.operator = _ai_text
		}
		if (_type == "cell_value") {
			if (!_ai_cell_val) {
				_ai_cell_val = "between"
			}
			_tmpRule.operator = _ai_cell_val
		}
		if ((_type == "blanks") || (_type == "no_blanks")
				|| (_type == "errors") || (_type == "no_errors")) {
			_tmpRule = {
				type : _type
			}
		}
	}
	var _config = {
		colorButtonW : "120px",
		labelWidth : 60,
		textBoxW : 180,
		comboBoxW : 200,
		rowH : 25,
		margingSize : 2,
		minColor : "#FF6633",
		maxColor : "#33FF66",
		midColor : "#6633FF"
	};
	var state = {
		colorMin : _config.minColor,
		colorMax : _config.maxColor,
		colorMid : _config.midColor
	};
	function _setupColorButton(colorBtn, color, setWidth) {
		var tmpElem = Ext.DomQuery
				.selectNode("*[id=" + colorBtn.btnEl.id + "]");
		tmpElem.style.background = color;
		tmpElem.style.width = "100px"
	}
	var topBottomComboData = [ [ "Top".localize(), "top" ],
			[ "Bottom".localize(), "bottom" ] ];
	var topBottomComboStore = new Ext.data.SimpleStore( {
		fields : [ "rank", "type" ],
		data : topBottomComboData
	});
	var aboveBelowData = [ [ "above".localize(), ">" ],
			[ "below".localize(), "<" ], [ "equal or above".localize(), ">=" ],
			[ "equal or below".localize(), "<=" ],
			[ "1 std dev above".localize(), "std_dev_above_1" ],
			[ "1 std dev below".localize(), "std_dev_below_1" ],
			[ "2 std dev above".localize(), "std_dev_above_2" ],
			[ "2 std dev below".localize(), "std_dev_below_2" ],
			[ "3 std dev above".localize(), "std_dev_above_3" ],
			[ "3 std dev below".localize(), "std_dev_below_3" ] ];
	var aboveBelowStore = new Ext.data.SimpleStore( {
		fields : [ "type", "operator" ],
		data : aboveBelowData
	});
	var duplicateComboData = [ [ "duplicate".localize(), "duplicate_value" ],
			[ "unique".localize(), "unique_value" ] ];
	var duplicateComboStore = new Ext.data.SimpleStore( {
		fields : [ "type", "rule_type" ],
		data : duplicateComboData
	});
	var CTCComboData = [ [ "Cell Value".localize(), "cell_value" ],
			[ "Specific Text".localize(), "text" ],
			[ "Dates Occurring".localize(), false ],
			[ "Blanks".localize(), "blanks" ],
			[ "No Blanks".localize(), "no_blanks" ],
			[ "Errors".localize(), "errors" ],
			[ "No errors".localize(), "no_errors" ] ];
	var CTCComboStore = new Ext.data.SimpleStore( {
		fields : [ "with", "value" ],
		data : CTCComboData
	});
	var CTCComboDataCellValue = [ [ "between".localize(), "between" ],
			[ "not between".localize(), "not_between" ],
			[ "equal to".localize(), "=" ],
			[ "not equal to".localize(), "<>" ],
			[ "greater than".localize(), ">" ],
			[ "less than".localize(), "<" ],
			[ "greater than or equal to".localize(), ">=" ],
			[ "less than or equal to".localize(), "<=" ] ];
	var CTCComboStoreCellValue = new Ext.data.SimpleStore( {
		fields : [ "desc", "operator" ],
		data : CTCComboDataCellValue
	});
	var CTCComboDataSpecificText = [ [ "containing".localize(), "contained" ],
			[ "not containing".localize(), "not_contained" ],
			[ "beginning with".localize(), "begins_with" ],
			[ "ending with".localize(), "ends_with" ] ];
	var CTCComboStoreSpecificText = new Ext.data.SimpleStore( {
		fields : [ "desc", "operator" ],
		data : CTCComboDataSpecificText
	});
	var CTCComboDataDatesOccurring = [ [ "Yesterday".localize() ],
			[ "Today".localize() ], [ "Tomorrow".localize() ],
			[ "In the last 7 days".localize() ], [ "Last week".localize() ],
			[ "This week".localize() ], [ "Next week".localize() ],
			[ "Last month".localize() ], [ "This month".localize() ],
			[ "Next month".localize() ] ];
	var CTCComboStoreDatesOccurring = new Ext.data.SimpleStore( {
		fields : [ "desc" ],
		data : CTCComboDataDatesOccurring
	});
	var dummyList = [ [ '<div style="line-height: 52px;">No Format Set</div>' ] ];
	var dummyStore = new Ext.data.SimpleStore( {
		fields : [ "dummy" ],
		data : dummyList
	});
	var tmpDummy = new Ext.data.Record.create( [ {
		name : "dummy"
	} ]);
	var formatPreview = new Ext.DataView(
			{
				id : "CF_format_preview",
				itemSelector : ".border-field-chooser",
				style : "overflow:auto",
				autoWidth : true,
				singleSelect : true,
				store : dummyStore,
				cls : "borderStyleSelect",
				tpl : new Ext.XTemplate(
						'<div class="format-sample-preview" id="CF_F_PRE_HOLD"><tpl for=".">',
						'<div style="display: table-cell; vertical-align: middle;">',
						'<div style="text-align: center; overflow: hidden;">',
						"{dummy}</div></div>", "</tpl></div>")
			});
	var mainMenu = new Ext.DataView(
			{
				id : "main-cond-menu",
				store : mainMenuStore,
				tpl : new Ext.XTemplate(
						'<div style="background-color: #FFFFFF; border:1px solid #B5B8C8;"><tpl for=".">',
						'<div class="thumb-wrap">{type}<br /></div>',
						"</tpl></div>"),
				autoHeight : true,
				border : true,
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				emptyText : "Menu error, please contact support",
				listeners : {
					click : {
						fn : chooseMenu,
						scope : this
					}
				}
			});
	var fromFCDLG;
	var _edited_from_MCF = false;
	var openFCDLG = function(fmDesc, isEdit) {
		fromFCDLG = fmDesc;
		_edited_from_MCF = true;
		if (!fromFCDLG[0].top) {
			fromFCDLG[0].top = {
				width : "",
				type : "none",
				color : ""
			}
		}
		if (!fromFCDLG[0].bottom) {
			fromFCDLG[0].bottom = {
				width : "",
				type : "none",
				color : ""
			}
		}
		if (!fromFCDLG[0].left) {
			fromFCDLG[0].left = {
				width : "",
				type : "none",
				color : ""
			}
		}
		if (!fromFCDLG[0].right) {
			fromFCDLG[0].right = {
				width : "",
				type : "none",
				color : ""
			}
		}
		for ( var i in fromFCDLG[0].top) {
			if (!fromFCDLG[0].top[i]) {
				fromFCDLG[0].top[i] = ""
			}
		}
		for ( var i in fromFCDLG[0].bottom) {
			if (!fromFCDLG[0].bottom[i]) {
				fromFCDLG[0].bottom[i] = ""
			}
		}
		for ( var i in fromFCDLG[0].left) {
			if (!fromFCDLG[0].left[i]) {
				fromFCDLG[0].left[i] = ""
			}
		}
		for ( var i in fromFCDLG[0].right) {
			if (!fromFCDLG[0].right[i]) {
				fromFCDLG[0].right[i] = ""
			}
		}
		_borders.top = fromFCDLG[0].top;
		_borders.bottom = fromFCDLG[0].bottom;
		_borders.left = fromFCDLG[0].left;
		_borders.right = fromFCDLG[0].right;
		var tmpRec = new tmpDummy( {
			dummy : '<div style="font-style: '.concat(fromFCDLG[0].fontStyle,
					"; background:", fromFCDLG[0].backgroundColor,
					"; text-decoration:", fromFCDLG[0].textDecoration,
					"; font-weight: ", fromFCDLG[0].fontWeight, "; color:",
					fromFCDLG[0].color, "; background-image:",
					fromFCDLG[0].backgroundImage, ";border-top:",
					fromFCDLG[0].top.width, " ", fromFCDLG[0].top.type, " ",
					fromFCDLG[0].top.color, ";", ";border-bottom:",
					fromFCDLG[0].bottom.width, " ", fromFCDLG[0].bottom.type,
					" ", fromFCDLG[0].bottom.color, ";", ";border-left:",
					fromFCDLG[0].left.width, " ", fromFCDLG[0].left.type, " ",
					fromFCDLG[0].left.color, ";", ";border-right:",
					fromFCDLG[0].right.width, " ", fromFCDLG[0].right.type,
					" ", fromFCDLG[0].right.color,
					'; line-height: 52px;">AaBbCcZz</div>')
		});
		dummyStore.removeAll();
		dummyStore.insert(0, tmpRec);
		formatPreview.refresh();
		_style = fromFCDLG[0];
		_format = fromFCDLG[1];
		_protection = fromFCDLG[2];
		win.show();
		win.focus()
	};
	var previewPanel = new Ext.Panel(
			{
				id : "preview_panel",
				layout : "column",
				baseCls : "x-plain",
				hidden : true,
				border : false,
				frame : false,
				items : [
						{
							width : 120,
							layout : "form",
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ {
								html : "Preview".localize().concat(":"),
								bodyStyle : "font-weight: bold; margin-top: 20px;",
								baseCls : "x-plain"
							} ]
						},
						{
							width : 280,
							layout : "form",
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ formatPreview ]
						},
						{
							width : 100,
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ new Ext.Button(
									{
										disabled : false,
										id : "fromConditionalFormatting",
										hidden : false,
										style : "margin-top: 20px;margin-right: 10px;",
										enableToggle : false,
										minWidth : 100,
										text : "Format".localize()
												.concat("..."),
										listeners : {
											click : {
												fn : function() {
													Jedox.wss.app
															.load(
																	Jedox.wss.app.dynJSRegistry.formatCells,
																	[
																			"fromConditionalFormatting",
																			0,
																			openFCDLG,
																			[
																					_format,
																					_style,
																					_borders,
																					_protection ] ])
												},
												scope : this
											}
										}
									}) ]
						} ]
			});
	var CTCCombo = new Ext.form.ComboBox( {
		store : CTCComboStore,
		displayField : "with",
		hideLabel : true,
		editable : false,
		typeAhead : true,
		mode : "local",
		triggerAction : "all",
		value : CTCComboData[0][0],
		selectOnFocus : true,
		listWidth : 115,
		width : 100,
		listeners : {
			select : {
				fn : setCTCCard,
				scope : this
			}
		}
	});
	var CTCComboCellValue = new Ext.form.ComboBox( {
		store : CTCComboStoreCellValue,
		displayField : "desc",
		hideLabel : true,
		editable : false,
		typeAhead : true,
		mode : "local",
		triggerAction : "all",
		value : CTCComboDataCellValue[0][0],
		selectOnFocus : true,
		listWidth : 125,
		width : 110,
		listeners : {
			select : {
				fn : function(combo, record, index) {
					Ext.getCmp("CTC_cell_value_card").layout
							.setActiveItem(index);
					if ((index == 0) || (index == 1)) {
						win.setWidth(650)
					} else {
					}
					_operator = CTCComboDataCellValue[index][1];
					_ai_cell_val = CTCComboDataCellValue[index][1];
					_tmpRule.operator = _operator
				},
				scope : this
			}
		}
	});
	var CTCComboSpecificText = new Ext.form.ComboBox( {
		store : CTCComboStoreSpecificText,
		displayField : "desc",
		hideLabel : true,
		editable : false,
		typeAhead : true,
		mode : "local",
		triggerAction : "all",
		value : CTCComboDataSpecificText[0][0],
		selectOnFocus : true,
		listWidth : 100,
		width : 100,
		listeners : {
			select : {
				fn : function(combo, record, index) {
					_operator = CTCComboDataSpecificText[index][1];
					_tmpRule.operator = _operator;
					_ai_text = CTCComboDataSpecificText[index][1]
				},
				scope : this
			}
		}
	});
	var CTCComboDatesOccurring = new Ext.form.ComboBox( {
		store : CTCComboStoreDatesOccurring,
		displayField : "desc",
		hideLabel : true,
		editable : false,
		typeAhead : true,
		mode : "local",
		triggerAction : "all",
		value : CTCComboDataDatesOccurring[0],
		selectOnFocus : true,
		listWidth : 120,
		width : 120,
		listeners : {
			select : {
				fn : function() {
				},
				scope : this
			}
		}
	});
	var CTCComboDatesOccHolder = new Ext.Panel( {
		baseCls : "x-plain",
		widh : 120,
		items : CTCComboDatesOccurring
	});
	var fromField1 = new Ext.form.TextField( {
		name : "fromfield1",
		id : "fromfield1",
		width : 110,
		value : (_pre.operands[0]) ? _pre.operands[0].replace(/=/i, "") : "",
		hideLabel : true,
		enableKeyEvents : true
	});
	var fromField1Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("fromfield1").getValue()
			} ]);
			_wf = "fromfield1";
			win.hide()
		}
	});
	var fromField1Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 110,
			baseCls : "x-plain",
			items : fromField1
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : fromField1Button
		} ]
	};
	var fromField2 = new Ext.form.TextField( {
		name : "fromfield2",
		id : "fromfield2",
		width : 110,
		value : (_pre.operands[1]) ? _pre.operands[1].replace(/=/i, "") : "",
		hideLabel : true,
		enableKeyEvents : true
	});
	var fromField2Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("fromfield2").getValue()
			} ]);
			_wf = "fromfield2";
			win.hide()
		}
	});
	var fromField2Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 110,
			baseCls : "x-plain",
			items : fromField2
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : fromField2Button
		} ]
	};
	var fromField3 = new Ext.form.TextField( {
		name : "fromfield3",
		id : "fromfield3",
		width : 110,
		value : (_pre.operands[0]) ? _pre.operands[0].replace(/=/i, "") : "",
		hideLabel : true,
		enableKeyEvents : true
	});
	var fromField3Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("fromfield3").getValue()
			} ]);
			_wf = "fromfield3";
			win.hide()
		}
	});
	var fromField3Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 110,
			baseCls : "x-plain",
			items : fromField3
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : fromField3Button
		} ]
	};
	var fromField4 = new Ext.form.TextField( {
		name : "fromfield4",
		id : "fromfield4",
		width : 110,
		value : (_pre.operands[1]) ? _pre.operands[1].replace(/=/i, "") : "",
		hideLabel : true,
		enableKeyEvents : true
	});
	var fromField4Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("fromfield4").getValue()
			} ]);
			_wf = "fromfield4";
			win.hide()
		}
	});
	var fromField4Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 110,
			baseCls : "x-plain",
			items : fromField4
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : fromField4Button
		} ]
	};
	function isArray(obj) {
		if (obj.constructor.toString().indexOf("Array") == -1) {
			return false
		} else {
			return true
		}
	}
	var tempOperands;
	if (isArray(_pre.operands)) {
		tempOperands = _pre.operands[0]
	} else {
		tempOperands = _pre.operands
	}
	var formField5 = new Ext.form.TextField( {
		name : "formfield5",
		id : "formfield5",
		width : 180,
		hideLabel : true,
		value : tempOperands.replace(/=/i, ""),
		enableKeyEvents : true
	});
	var formField5Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("formfield5").getValue()
			} ]);
			_wf = "formfield5";
			win.hide()
		}
	});
	var formField5Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 180,
			baseCls : "x-plain",
			items : formField5
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : formField5Button
		} ]
	};
	var formField6 = new Ext.form.TextField( {
		name : "formfield6",
		id : "formfield6",
		width : 180,
		hideLabel : true,
		value : tempOperands.replace(/=/i, ""),
		enableKeyEvents : true
	});
	var formField6Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("formfield6").getValue()
			} ]);
			_wf = "formfield6";
			win.hide()
		}
	});
	var formField6Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 180,
			baseCls : "x-plain",
			items : formField6
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : formField6Button
		} ]
	};
	var formField7 = new Ext.form.TextField( {
		name : "formfield7",
		id : "formfield7",
		width : 180,
		value : tempOperands.replace(/=/i, ""),
		hideLabel : true,
		enableKeyEvents : true
	});
	var formField7Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("formfield7").getValue()
			} ]);
			_wf = "formfield7";
			win.hide()
		}
	});
	var formField7Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 180,
			baseCls : "x-plain",
			items : formField7
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : formField7Button
		} ]
	};
	var formField8 = new Ext.form.TextField( {
		name : "formfield8",
		id : "formfield8",
		width : 180,
		value : tempOperands.replace(/=/i, ""),
		hideLabel : true,
		enableKeyEvents : true
	});
	var formField8Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("formfield8").getValue()
			} ]);
			_wf = "formfield8";
			win.hide()
		}
	});
	var formField8Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 180,
			baseCls : "x-plain",
			items : formField8
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : formField8Button
		} ]
	};
	var formField9 = new Ext.form.TextField( {
		name : "formfield9",
		id : "formfield9",
		width : 180,
		value : tempOperands.replace(/=/i, ""),
		hideLabel : true,
		enableKeyEvents : true
	});
	var formField9Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("formfield9").getValue()
			} ]);
			_wf = "formfield9";
			win.hide()
		}
	});
	var formField9Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 180,
			baseCls : "x-plain",
			items : formField9
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : formField9Button
		} ]
	};
	var formField10 = new Ext.form.TextField( {
		name : "formfield10",
		id : "formfield10",
		width : 180,
		value : tempOperands.replace(/=/i, ""),
		hideLabel : true,
		enableKeyEvents : true
	});
	var formField10Button = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "{Sheet}!{Range}",
				rng : Ext.getCmp("formfield10").getValue()
			} ]);
			_wf = "formfield10";
			win.hide()
		}
	});
	var formField10Container = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 180,
			baseCls : "x-plain",
			items : formField10
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : formField10Button
		} ]
	};
	var specificTextfield = new Ext.form.TextField( {
		name : "specificTextfield",
		width : 180,
		value : _pre.operands[0],
		hideLabel : true,
		enableKeyEvents : true
	});
	var CTCCellValueCard = new Ext.Panel(
			{
				id : "CTC_cell_value_card",
				layout : "card",
				deferredRender : true,
				autoWidth : true,
				baseCls : "x-plain",
				border : false,
				activeItem : 0,
				items : [
						new Ext.Panel(
								{
									id : "ctc_tertiar_container",
									layout : "column",
									baseCls : "x-plain",
									border : false,
									frame : false,
									autoWidth : true,
									items : [
											{
												width : 160,
												layout : "form",
												xtype : "fieldset",
												bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
												autoHeight : true,
												border : false,
												frame : false,
												items : [ fromField1Container ]
											},
											{
												width : 55,
												layout : "form",
												xtype : "fieldset",
												bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
												autoHeight : true,
												border : false,
												frame : false,
												items : [ {
													html : "and".localize(),
													baseCls : "x-plain",
													bodyStyle : "padding-left: 5px; padding-right: 5px;"
												} ]
											},
											{
												width : 160,
												layout : "form",
												xtype : "fieldset",
												bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
												autoHeight : true,
												border : false,
												frame : false,
												items : [ fromField2Container ]
											} ]
								}),
						new Ext.Panel(
								{
									id : "ctc_tertiar_container2",
									layout : "column",
									baseCls : "x-plain",
									border : false,
									frame : false,
									autoWidth : true,
									items : [
											{
												width : 160,
												layout : "form",
												xtype : "fieldset",
												bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
												autoHeight : true,
												border : false,
												frame : false,
												items : [ fromField3Container ]
											},
											{
												width : 55,
												layout : "form",
												xtype : "fieldset",
												bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
												autoHeight : true,
												border : false,
												frame : false,
												items : [ {
													html : "and".localize(),
													baseCls : "x-plain",
													bodyStyle : "padding-left: 5px; padding-right: 5px;"
												} ]
											},
											{
												width : 160,
												layout : "form",
												xtype : "fieldset",
												bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
												autoHeight : true,
												border : false,
												frame : false,
												items : [ fromField4Container ]
											} ]
								}),
						new Ext.Panel(
								{
									id : "ctc_tertiar_container3",
									layout : "column",
									baseCls : "x-plain",
									border : false,
									frame : false,
									autoWidth : true,
									items : [ {
										width : 230,
										layout : "form",
										xtype : "fieldset",
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										autoHeight : true,
										border : false,
										frame : false,
										items : [ formField5Container ]
									} ]
								}),
						new Ext.Panel(
								{
									id : "ctc_tertiar_container6",
									layout : "column",
									baseCls : "x-plain",
									border : false,
									frame : false,
									autoWidth : true,
									items : [ {
										width : 230,
										layout : "form",
										xtype : "fieldset",
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										autoHeight : true,
										border : false,
										frame : false,
										items : [ formField6Container ]
									} ]
								}),
						new Ext.Panel(
								{
									id : "ctc_tertiar_container7",
									layout : "column",
									baseCls : "x-plain",
									border : false,
									frame : false,
									autoWidth : true,
									items : [ {
										width : 230,
										layout : "form",
										xtype : "fieldset",
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										autoHeight : true,
										border : false,
										frame : false,
										items : [ formField7Container ]
									} ]
								}),
						new Ext.Panel(
								{
									id : "ctc_tertiar_container8",
									layout : "column",
									baseCls : "x-plain",
									border : false,
									frame : false,
									autoWidth : true,
									items : [ {
										width : 230,
										layout : "form",
										xtype : "fieldset",
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										autoHeight : true,
										border : false,
										frame : false,
										items : [ formField8Container ]
									} ]
								}),
						new Ext.Panel(
								{
									id : "ctc_tertiar_container9",
									layout : "column",
									baseCls : "x-plain",
									border : false,
									frame : false,
									autoWidth : true,
									items : [ {
										width : 230,
										layout : "form",
										xtype : "fieldset",
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										autoHeight : true,
										border : false,
										frame : false,
										items : [ formField9Container ]
									} ]
								}),
						new Ext.Panel(
								{
									id : "ctc_tertiar_container10",
									layout : "column",
									baseCls : "x-plain",
									border : false,
									frame : false,
									autoWidth : true,
									items : [ {
										width : 230,
										layout : "form",
										xtype : "fieldset",
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										autoHeight : true,
										border : false,
										frame : false,
										items : [ formField10Container ]
									} ]
								}) ]
			});
	var CTCCellValue = new Ext.Panel(
			{
				id : "ctc_secondary_container",
				layout : "column",
				autoHeight : true,
				baseCls : "x-plain",
				border : false,
				frame : false,
				items : [
						{
							width : 150,
							layout : "form",
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ CTCComboCellValue ]
						},
						{
							width : 350,
							layout : "form",
							id : "ctc_nest",
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ CTCCellValueCard ]
						} ]
			});
	var CTCSpecificText = new Ext.Panel(
			{
				id : "ctc_specific_text",
				layout : "column",
				baseCls : "x-plain",
				border : false,
				frame : false,
				items : [
						{
							width : 140,
							layout : "form",
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ CTCComboSpecificText ]
						},
						{
							width : 300,
							layout : "form",
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ specificTextfield ]
						} ]
			});
	var CTCCard = new Ext.Panel( {
		id : "CTC_card",
		layout : "card",
		deferredRender : true,
		autoWidth : true,
		baseCls : "x-plain",
		border : false,
		activeItem : 0,
		items : [ CTCCellValue, CTCSpecificText, CTCComboDatesOccHolder, {
			baseCls : "x-plain"
		}, {
			baseCls : "x-plain"
		}, {
			baseCls : "x-plain"
		}, {
			baseCls : "x-plain"
		} ]
	});
	var CTCRight = new Ext.Panel( {
		width : 470,
		id : "ctc_right",
		baseCls : "xplain",
		items : CTCCard
	});
	var CTCMainContainer = new Ext.Panel(
			{
				id : "ctc_main_container",
				layout : "column",
				autoHeight : true,
				baseCls : "x-plain",
				border : false,
				frame : false,
				items : [
						{
							width : 140,
							layout : "form",
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ CTCCombo ]
						},
						{
							id : "ctc_main_right",
							layout : "form",
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ CTCRight ]
						} ]
			});
	var cellsThatContain = new Ext.Panel( {
		border : true,
		id : "cells_that_contain",
		frame : true,
		baseCls : "main-panel-open",
		autoHeight : true,
		autoWidth : true,
		layout : "form",
		items : [ {
			html : "Format only cells with".localize().concat(":"),
			baseCls : "x-plain",
			bodyStyle : "margin-bottom: 10px;"
		}, CTCMainContainer ]
	});
	var topBottomCombo = new Ext.form.ComboBox( {
		store : topBottomComboStore,
		displayField : "rank",
		id : "top_bottom_combo",
		hideLabel : true,
		editable : false,
		typeAhead : false,
		mode : "local",
		triggerAction : "all",
		value : topBottomComboData[0][0],
		selectOnFocus : true,
		listWidth : 100,
		width : 100,
		listeners : {
			select : {
				fn : function(combo, record, index) {
					_ai_topbot = topBottomComboData[index][1]
				},
				scope : this
			}
		}
	});
	var topBottomValue = new Ext.form.TextField( {
		name : "top_bottom_value",
		width : 100,
		hideLabel : true,
		enableKeyEvents : true,
		value : 10
	});
	var topBotPerc;
	var topBottomCB = new Ext.form.Checkbox( {
		fieldLabel : "perc_of_sel_rng",
		hideLabel : true,
		name : "perc_of_sel_rng",
		boxLabel : "% of the selected range".localize(),
		listeners : {
			check : {
				fn : function() {
					topBotPerc = this.getValue()
				}
			}
		}
	});
	var topBottomHolder = new Ext.Panel(
			{
				id : "top_bottom_holder",
				layout : "column",
				baseCls : "x-plain",
				border : false,
				frame : false,
				items : [
						{
							columnWidth : 0.3,
							layout : "form",
							xtype : "fieldset",
							width : 100,
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ topBottomCombo ]
						},
						{
							columnWidth : 0.3,
							layout : "form",
							width : 130,
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ topBottomValue ]
						},
						{
							columnWidth : 0.4,
							layout : "form",
							width : 200,
							xtype : "fieldset",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ topBottomCB ]
						} ]
			});
	var topBottomRanked = new Ext.Panel( {
		border : true,
		id : "top_bottom_ranked",
		frame : true,
		baseCls : "main-panel-open",
		autoHeight : true,
		autoWidth : true,
		layout : "form",
		items : [ {
			html : "Format values that rank in the".localize().concat(":"),
			baseCls : "x-plain",
			bodyStyle : "margin-bottom: 10px;"
		}, topBottomHolder ]
	});
	var aboveBelowCombo = new Ext.form.ComboBox( {
		store : aboveBelowStore,
		displayField : "type",
		id : "above_below_combo",
		hideLabel : true,
		editable : false,
		typeAhead : false,
		mode : "local",
		triggerAction : "all",
		value : aboveBelowData[0][0],
		selectOnFocus : true,
		listWidth : 120,
		width : 120,
		listeners : {
			select : {
				fn : function(combo, record, index) {
					_ab_bel = aboveBelowData[index][1]
				},
				scope : this
			}
		}
	});
	var aboveBelowHolder = new Ext.Panel(
			{
				id : "above_below_holder",
				layout : "column",
				baseCls : "x-plain",
				border : false,
				frame : false,
				items : [
						{
							columnWidth : 0.4,
							layout : "form",
							xtype : "fieldset",
							width : 170,
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ aboveBelowCombo ]
						},
						{
							columnWidth : 0.6,
							layout : "form",
							xtype : "fieldset",
							width : 250,
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ {
								html : "the average for the selected range"
										.localize(),
								baseCls : "x-plain"
							} ]
						} ]
			});
	var aboveBelowAverage = new Ext.Panel( {
		border : true,
		id : "above_below_average",
		frame : true,
		baseCls : "main-panel-open",
		autoHeight : true,
		autoWidth : true,
		layout : "form",
		items : [ {
			html : "Format values that are".localize().concat(":"),
			baseCls : "x-plain",
			bodyStyle : "margin-bottom: 10px;"
		}, aboveBelowHolder ]
	});
	var duplicateCombo = new Ext.form.ComboBox( {
		store : duplicateComboStore,
		displayField : "type",
		id : "duplicate_combo",
		hideLabel : true,
		editable : false,
		typeAhead : false,
		mode : "local",
		triggerAction : "all",
		value : duplicateComboData[0][0],
		selectOnFocus : true,
		listWidth : 120,
		width : 120,
		listeners : {
			select : {
				fn : function(combo, record, index) {
					_unique = duplicateComboData[index][1]
				},
				scope : this
			}
		}
	});
	var duplicateHolder = new Ext.Panel(
			{
				id : "duplicate_holder",
				layout : "column",
				baseCls : "x-plain",
				border : false,
				frame : false,
				items : [
						{
							columnWidth : 0.4,
							layout : "form",
							xtype : "fieldset",
							width : 170,
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ duplicateCombo ]
						},
						{
							columnWidth : 0.6,
							layout : "form",
							xtype : "fieldset",
							width : 250,
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ {
								html : "values in the selected range"
										.localize(),
								baseCls : "x-plain"
							} ]
						} ]
			});
	var uniqueDuplicate = new Ext.Panel( {
		border : true,
		id : "unique_duplicate",
		frame : true,
		baseCls : "main-panel-open",
		autoHeight : true,
		autoWidth : true,
		layout : "form",
		items : [ {
			html : "Format all".localize().concat(":"),
			baseCls : "x-plain",
			bodyStyle : "margin-bottom: 10px;"
		}, duplicateHolder ]
	});
	var useFormulaField = new Ext.form.TextField( {
		name : "formula_value",
		id : "formula_value",
		width : 410,
		value : tempOperands,
		hideLabel : true,
		enableKeyEvents : true
	});
	var formulaFieldButton = new uniFButton( {
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRange ],
				format : "={Sheet}!{Range}",
				rng : Ext.getCmp("formula_value").getValue()
			} ]);
			_wf = "formula_value";
			win.hide()
		}
	});
	var formulaContainer = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		items : [ {
			layout : "form",
			border : false,
			width : 410,
			baseCls : "x-plain",
			items : useFormulaField
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : formulaFieldButton
		} ]
	};
	var useFormula = new Ext.Panel( {
		border : true,
		id : "use_formula",
		frame : true,
		baseCls : "main-panel-open",
		autoHeight : true,
		autoWidth : true,
		layout : "form",
		items : [
				{
					html : "Format values where this formula is true"
							.localize().concat(":"),
					baseCls : "x-plain",
					bodyStyle : "margin-bottom: 10px;"
				}, formulaContainer ]
	});
	var mainDisplay = new Ext.Panel( {
		id : "main_display",
		layout : "card",
		deferredRender : true,
		autoWidth : true,
		baseCls : "x-plain",
		border : false,
		activeItem : initActiveItem,
		items : [ cellsThatContain, topBottomRanked, aboveBelowAverage,
				uniqueDuplicate, useFormula ]
	});
	var _title;
	if (from == "editFromConditionalManage") {
		_title = "Edit Formatting Rule".localize()
	} else {
		_title = "New Formatting Rule".localize()
	}
	var win = new Ext.Window(
			{
				title : _title,
				closable : true,
				autoDestroy : true,
				id : "conditional_formatting",
				cls : "default-format-window",
				plain : true,
				constrain : true,
				modal : true,
				resizable : false,
				animCollapse : false,
				width : winw,
				height : 420,
				items : [ new Ext.Panel(
						{
							baseCls : "x-title-f",
							labelWidth : 100,
							labelAlign : "left",
							bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
							frame : false,
							header : false,
							monitorValid : true,
							autoHeight : true,
							autoWidth : true,
							items : [
									{
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										border : true,
										autoHeight : true,
										xtype : "fieldset",
										layout : "form",
										frame : false,
										title : "Select a Rule Type".localize()
												.concat(":"),
										items : [ mainMenu ]
									},
									{
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										border : true,
										autoHeight : true,
										xtype : "fieldset",
										layout : "form",
										frame : false,
										title : "Edit the Rule Description"
												.localize().concat(":"),
										items : [ mainDisplay ]
									}, previewPanel ]
						}) ],
				listeners : {
					close : function() {
						if (!_fromDlgF) {
							Jedox.wss.general
									.setInputMode(Jedox.wss.app.lastInputModeDlg);
							Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
						}
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.newName)
					},
					show : function() {
						setTimeout(function() {
							previewPanel.doLayout()
						}, 1)
					}
				},
				buttons : [
						{
							text : "OK".localize(),
							handler : function() {
								function setCondFormatting() {
									if (!fromFCDLG[1]) {
										fromFCDLG[1] = [ false ]
									}
									switch (_ai_main) {
									case 1:
										switch (_tmpRule.type) {
										case "cell_value":
											switch (_tmpRule.operator) {
											case "between":
												var _op1 = "="
														.concat(fromField1
																.getValue());
												var _op2 = "="
														.concat(fromField2
																.getValue());
												if ((!_op1) || (!_op2)) {
													alert("The value you entered is not a valid number, date, time or string")
												} else {
													_tmpRule.operands = [ _op1,
															_op2 ];
													_tmpRule.style = fromFCDLG[0];
													_tmpRule.format = fromFCDLG[1][0]
												}
												break;
											case "not_between":
												var _op3 = "="
														.concat(fromField3
																.getValue());
												var _op4 = "="
														.concat(fromField4
																.getValue());
												if ((!_op3) || (!_op4)) {
													alert("The value you entered is not a valid number, date, time or string")
												} else {
													_tmpRule.operands = [ _op3,
															_op4 ];
													_tmpRule.style = fromFCDLG[0];
													_tmpRule.format = fromFCDLG[1][0]
												}
												break;
											case "=":
												var _op5 = "="
														.concat(formField5
																.getValue());
												if (!_op5) {
													alert("The value you entered is not a valid number, date, time or string")
												} else {
													_tmpRule.operands = [ _op5 ];
													_tmpRule.style = fromFCDLG[0];
													_tmpRule.format = fromFCDLG[1][0]
												}
												break;
											case "<>":
												var _op6 = "="
														.concat(formField6
																.getValue());
												if (!_op6) {
													alert("The value you entered is not a valid number, date, time or string")
												} else {
													_tmpRule.operands = [ _op6 ];
													_tmpRule.style = fromFCDLG[0];
													_tmpRule.format = fromFCDLG[1][0]
												}
												break;
											case ">":
												var _op7 = "="
														.concat(formField7
																.getValue());
												if (!_op7) {
													alert("The value you entered is not a valid number, date, time or string")
												} else {
													_tmpRule.operands = [ _op7 ];
													_tmpRule.style = fromFCDLG[0];
													_tmpRule.format = fromFCDLG[1][0]
												}
												break;
											case "<":
												var _op8 = "="
														.concat(formField8
																.getValue());
												if (!_op8) {
													alert("The value you entered is not a valid number, date, time or string")
												} else {
													_tmpRule.operands = [ _op8 ];
													_tmpRule.style = fromFCDLG[0];
													_tmpRule.format = fromFCDLG[1][0]
												}
												break;
											case ">=":
												var _op9 = "="
														.concat(formField9
																.getValue());
												if (!_op9) {
													alert("The value you entered is not a valid number, date, time or string")
												} else {
													_tmpRule.operands = [ _op9 ];
													_tmpRule.style = fromFCDLG[0];
													_tmpRule.format = fromFCDLG[1][0]
												}
												break;
											case "<=":
												var _op10 = "="
														.concat(formField10
																.getValue());
												if (!_op10) {
													alert("The value you entered is not a valid number, date, time or string")
												} else {
													_tmpRule.operands = [ _op10 ];
													_tmpRule.style = fromFCDLG[0];
													_tmpRule.format = fromFCDLG[1][0]
												}
												break
											}
											break;
										case "text":
											var _op = specificTextfield
													.getValue();
											if (!_op) {
												alert("The value you entered is not a valid number, date, time or string")
											} else {
												_tmpRule.operands = [ _op ];
												_tmpRule.style = fromFCDLG[0];
												_tmpRule.format = fromFCDLG[1][0]
											}
											break;
										case "blanks":
											_tmpRule = {
												type : _tmpRule.type,
												style : fromFCDLG[0],
												format : fromFCDLG[1][0]
											};
											break;
										case "no_blanks":
											_tmpRule = {
												type : _tmpRule.type,
												style : fromFCDLG[0],
												format : fromFCDLG[1][0]
											};
											break;
										case "errors":
											_tmpRule = {
												type : _tmpRule.type,
												style : fromFCDLG[0],
												format : fromFCDLG[1][0]
											};
											break;
										case "no_blanks":
											_tmpRule = {
												type : _tmpRule.type,
												style : fromFCDLG[0],
												format : fromFCDLG[1][0]
											};
											break
										}
										break;
									case 2:
										var topBotN = topBottomValue.getValue();
										if (topBotPerc) {
											_tmpRule = {
												type : _ai_topbot
														.concat("_percent"),
												operator : topBotN
											}
										} else {
											_tmpRule = {
												type : _ai_topbot,
												operator : topBotN
											}
										}
										_tmpRule.style = fromFCDLG[0];
										_tmpRule.format = fromFCDLG[1][0];
										break;
									case 3:
										_tmpRule = {
											type : "average_value",
											operator : _ab_bel
										};
										_tmpRule.style = fromFCDLG[0];
										_tmpRule.format = fromFCDLG[1][0];
										break;
									case 4:
										_tmpRule = {
											type : _unique
										};
										_tmpRule.style = fromFCDLG[0];
										_tmpRule.format = fromFCDLG[1][0];
										break;
									case 5:
										var tmpOperand = useFormulaField
												.getValue();
										tmpOperand = [ tmpOperand ];
										_tmpRule = {
											type : "formula_true",
											operands : tmpOperand,
											operator : null,
											style : fromFCDLG[0],
											sit : true,
											format : null
										};
										_tmpRule.style = fromFCDLG[0];
										_tmpRule.format = fromFCDLG[1][0];
										break
									}
									_tmpRule.borders = {};
									_tmpRule.borders.top = fromFCDLG[0].top;
									_tmpRule.borders.bottom = fromFCDLG[0].bottom;
									_tmpRule.borders.left = fromFCDLG[0].left;
									_tmpRule.borders.right = fromFCDLG[0].right;
									_tmpRule.lock = (fromFCDLG[2] == null) ? _pre.lock
											: fromFCDLG[2];
									if (_tmpRule.style) {
										delete _tmpRule.style.top;
										delete _tmpRule.style.bottom;
										delete _tmpRule.style.left;
										delete _tmpRule.style.right
									}
									rules = [ _tmpRule ]
								}
								if (!fromFCDLG) {
									fromFCDLG = [ null, null, null ]
								}
								if ((from == "fromConditionalManage")
										|| (from == "editFromConditionalManage")) {
									if ((from == "editFromConditionalManage")
											&& (!_edited_from_MCF)) {
										fromFCDLG[0] = _style;
										fromFCDLG[1] = _pre.format;
										fromFCDLG[2] = _pre.protection
									}
									setCondFormatting();
									rules[0].applies_to = Jedox.wss.app.environment.defaultSelection
											.getFormula(true);
									var tArr = rules[0].style;
									for ( var w in tArr) {
										if (!tArr[w]) {
											delete tArr[w]
										}
										switch (w) {
										case "backgroundImage":
											if (tArr[w] == "none") {
												delete tArr[w]
											}
											break
										}
									}
									if (from == "fromConditionalManage") {
										var _isEdit = false
									} else {
										if (from == "editFromConditionalManage") {
											var _isEdit = true;
											rules[0].applies_to = _applies_to;
											rules[0].id = _id
										}
									}
									rules[0].borders = _borders;
									var _output = rules;
									addParams(_output, _isEdit);
									win.destroy()
								} else {
									setCondFormatting();
									var tArr = rules[0].style;
									for ( var w in tArr) {
										if (!tArr[w]) {
											delete tArr[w]
										}
										switch (w) {
										case "backgroundImage":
											if (tArr[w] == "none") {
												delete tArr[w]
											}
											break
										}
									}
									cndfmt.set(rules);
									win.destroy();
									Jedox.wss.general
											.setInputMode(Jedox.wss.app.lastInputModeDlg);
									Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
									Jedox.wss.general.refreshCursorField()
								}
							}
						},
						{
							text : "Cancel".localize(),
							handler : function() {
								if ((from == "fromConditionalManage")
										|| (from == "editFromConditionalManage")) {
									addParams(false, false)
								}
								win.destroy();
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
								Jedox.wss.general.refreshCursorField()
							}
						} ]
			});
	win.show(this);
	if (toEdit) {
		switch (_pre.type) {
		case "cell_value":
			win.setWidth(650);
			_ai_main = 1;
			mainDisplay.layout.setActiveItem(0);
			mainMenu.select(0, false, false);
			chooseMenu(mainMenu, 0, "", "");
			switch (_pre.operator) {
			case "=":
				CTCComboCellValue.setValue("equal to");
				CTCCellValueCard.layout.setActiveItem(2);
				break;
			case "<>":
				CTCComboCellValue.setValue("not equal to");
				CTCCellValueCard.layout.setActiveItem(3);
				break;
			case "<=":
				CTCComboCellValue.setValue("less than or equal to");
				CTCCellValueCard.layout.setActiveItem(7);
				break;
			case ">=":
				CTCComboCellValue.setValue("greater than or equal to");
				CTCCellValueCard.layout.setActiveItem(6);
				break;
			case "<":
				CTCComboCellValue.setValue("less than");
				CTCCellValueCard.layout.setActiveItem(5);
				break;
			case ">":
				CTCComboCellValue.setValue("greater than");
				CTCCellValueCard.layout.setActiveItem(4);
				break;
			case "between":
				CTCComboCellValue.setValue("between");
				CTCCellValueCard.layout.setActiveItem(0);
				break;
			case "not_between":
				CTCComboCellValue.setValue("not between");
				CTCCellValueCard.layout.setActiveItem(1);
				break;
			default:
			}
			break;
		case "bottom_percent":
			win.setWidth(500);
			_ai_main = 2;
			mainDisplay.layout.setActiveItem(1);
			mainMenu.select(1, false, false);
			chooseMenu(mainMenu, 1, "", "");
			topBottomCombo.setValue("Bottom");
			topBottomValue.setValue(_pre.operator);
			topBottomCB.setValue(true);
			break;
		case "average_value":
			_ai_main = 3;
			mainDisplay.layout.setActiveItem(2);
			mainMenu.select(2, false, false);
			chooseMenu(mainMenu, 2, "", "");
			switch (_pre.operator) {
			case ">":
				aboveBelowCombo.setValue("above");
				break;
			case "<":
				aboveBelowCombo.setValue("below");
				break;
			case ">=":
				aboveBelowCombo.setValue("equal or above");
				break;
			case "<=":
				aboveBelowCombo.setValue("equal or below");
				break;
			case "std_dev_above_1":
				aboveBelowCombo.setValue("1 std dev above");
				break;
			case "std_dev_above_2":
				aboveBelowCombo.setValue("2 std dev above");
				break;
			case "std_dev_above_3":
				aboveBelowCombo.setValue("3 std dev above");
				break;
			case "std_dev_above_4":
				aboveBelowCombo.setValue("4 std dev above");
				break;
			case "std_dev_below_1":
				aboveBelowCombo.setValue("1 std dev below");
				break;
			case "std_dev_below_2":
				aboveBelowCombo.setValue("2 std dev below");
				break;
			case "std_dev_below_3":
				aboveBelowCombo.setValue("3 std dev below");
				break;
			case "std_dev_below_4":
				aboveBelowCombo.setValue("4 std dev below");
				break
			}
			break;
		case "no_blanks":
			_ai_main = 1;
			mainDisplay.layout.setActiveItem(0);
			mainMenu.select(0, false, false);
			chooseMenu(mainMenu, 0, "", "");
			CTCCard.layout.setActiveItem(4);
			CTCCombo.setValue("No Blanks");
			break;
		case "blanks":
			_ai_main = 1;
			mainDisplay.layout.setActiveItem(0);
			mainMenu.select(0, false, false);
			chooseMenu(mainMenu, 0, "", "");
			CTCCard.layout.setActiveItem(3);
			CTCCombo.setValue("Blanks");
			break;
		case "no_errors":
			_ai_main = 1;
			mainDisplay.layout.setActiveItem(0);
			mainMenu.select(0, false, false);
			chooseMenu(mainMenu, 0, "", "");
			CTCCard.layout.setActiveItem(6);
			CTCCombo.setValue("No errors");
			break;
		case "errors":
			_ai_main = 1;
			mainDisplay.layout.setActiveItem(0);
			mainMenu.select(0, false, false);
			chooseMenu(mainMenu, 0, "", "");
			CTCCard.layout.setActiveItem(5);
			CTCCombo.setValue("Errors");
			break;
		case "formula_true":
			_ai_main = 5;
			mainDisplay.layout.setActiveItem(4);
			mainMenu.select(4, false, false);
			chooseMenu(mainMenu, 4, "", "");
			break;
		case "text":
			_ai_main = 1;
			mainDisplay.layout.setActiveItem(0);
			mainMenu.select(0, false, false);
			chooseMenu(mainMenu, 0, "", "");
			CTCCard.layout.setActiveItem(1);
			CTCCombo.setValue("Specific Text");
			switch (_pre.operator) {
			case "contained":
				CTCComboSpecificText.setValue("containing");
				break;
			case "not_contained":
				CTCComboSpecificText.setValue("not containing");
				break;
			case "begins_with":
				CTCComboSpecificText.setValue("beginning with");
				break;
			case "ends_with":
				CTCComboSpecificText.setValue("ending with");
				break
			}
			break;
		case "top":
			_ai_main = 2;
			win.setWidth(500);
			mainDisplay.layout.setActiveItem(1);
			mainMenu.select(1, false, false);
			chooseMenu(mainMenu, 1, "", "");
			topBottomCombo.setValue("Top");
			topBottomValue.setValue(_pre.operator);
			topBottomCB.setValue(false);
			break;
		case "top_percent":
			_ai_main = 2;
			win.setWidth(500);
			mainDisplay.layout.setActiveItem(1);
			mainMenu.select(1, false, false);
			chooseMenu(mainMenu, 1, "", "");
			topBottomCombo.setValue("Top");
			topBottomValue.setValue(_pre.operator);
			topBottomCB.setValue(true);
			break;
		case "bottom":
			_ai_main = 2;
			win.setWidth(500);
			mainDisplay.layout.setActiveItem(1);
			mainMenu.select(1, false, false);
			chooseMenu(mainMenu, 1, "", "");
			topBottomCombo.setValue("Bottom");
			topBottomValue.setValue(_pre.operator);
			topBottomCB.setValue(false);
			break;
		case "duplicate_value":
			_ai_main = 4;
			mainDisplay.layout.setActiveItem(3);
			mainMenu.select(3, false, false);
			chooseMenu(mainMenu, 3, "", "");
			duplicateCombo.setValue("duplicate");
			break;
		case "unique_value":
			_ai_main = 4;
			mainDisplay.layout.setActiveItem(3);
			mainMenu.select(3, false, false);
			chooseMenu(mainMenu, 3, "", "");
			duplicateCombo.setValue("unique");
			break;
		default:
			mainDisplay.layout.setActiveItem(0)
		}
		var tmpRec = new tmpDummy( {
			dummy : '<div style="font-style: '.concat(_style.fontStyle,
					"; background:", _style.backgroundColor,
					"; text-decoration:", _style.textDecoration,
					"; font-weight: ", _style.fontWeight, "; color:",
					_style.color, "; background-image:",
					_style.backgroundImage, ";border-top:", _borders.top.width,
					" ", _borders.top.type, " ", _borders.top.color, ";",
					";border-bottom:", _borders.bottom.width, " ",
					_borders.bottom.type, " ", _borders.bottom.color, ";",
					";border-left:", _borders.left.width, " ",
					_borders.left.type, " ", _borders.left.color, ";",
					";border-right:", _borders.right.width, " ",
					_borders.right.type, " ", _borders.right.color,
					'; line-height: 52px;">AaBbCcZz</div>')
		});
		dummyStore.removeAll();
		dummyStore.insert(0, tmpRec);
		setTimeout(function() {
			formatPreview.refresh()
		}, 1)
	} else {
		mainMenu.select(0, false, false);
		chooseMenu(mainMenu, 0, "", "")
	}
	previewPanel.show();
	if (!_ai_main) {
		_ai_main = 0
	}
};