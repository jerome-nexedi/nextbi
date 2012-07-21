Jedox.wss.dialog.format.alignment = function(callback, _pre, toDisable) {
	var alignHorizontal = [ [ "none", "General".localize() ],
			[ "left", "Left (Indent)".localize() ],
			[ "center", "Center".localize() ], [ "right", "Right".localize() ],
			[ "fill", "Fill".localize() ], [ "justify", "Justify".localize() ],
			[ "center-across", "Center across section".localize() ],
			[ "distributed", "Distributed (Indent)".localize() ] ];
	var alignVertical = [ [ "top", "Top".localize() ],
			[ "center", "Center".localize() ],
			[ "bottom", "Bottom".localize() ],
			[ "justify", "Justify".localize() ],
			[ "distributed", "Distributed".localize() ] ];
	var textDirection = [ [ "context", "Context".localize() ],
			[ "ltr", "Left-to-Right".localize() ],
			[ "rtl", "Right-to-Left".localize() ] ];
	var textDirectionStore = new Ext.data.SimpleStore( {
		fields : [ "direction", "description" ],
		data : textDirection
	});
	var alignVerticalStore = new Ext.data.SimpleStore( {
		fields : [ "format", "formatType" ],
		data : alignVertical
	});
	var alignHorizontalStore = new Ext.data.SimpleStore( {
		fields : [ "format", "formatType" ],
		data : alignHorizontal
	});
	var horAlignGridDesc = "General";
	var verAlignGridDesc = "Top";
	var textDirectionVarDesc = "Context";
	var textDirectionVar;
	var horAlignGrid;
	var horAlignGrid;
	var horAlignGrid;
	var textWrap;
	var verAlignGrid;
	var isSetWrap = false;
	if (textWrap == "normal") {
		isSetWrap = true
	} else {
		if (textWrap == "nowrap") {
			isSetWrap = false
		}
	}
	var format = {};
	if (!_pre) {
		_pre = {}
	} else {
		format = _pre
	}
	if (_pre.whiteSpace == null) {
		_pre.whiteSpace = "nowrap"
	}
	var textWrap = _pre.whiteSpace;
	var isSetWrap = false;
	if (textWrap == "normal") {
		isSetWrap = true
	} else {
		if (textWrap == "nowrap") {
			isSetWrap = false
		}
	}
	if (_pre.textAlign == null) {
		_pre.textAlign = ""
	}
	var horAlignGrid = _pre.textAlign;
	if (_pre.verticalAlign == null) {
		_pre.verticalAlign = ""
	}
	var verAlignGrid = _pre.verticalAlign;
	if (_pre.textIndent == null) {
		_pre.textIndent = 0
	}
	if (_pre.textIndent == "") {
		_pre.textIndent = 0
	}
	var indentGrid = _pre.textIndent;
	if (indentGrid != 0) {
		indentGrid = indentGrid.replace(/em/i, "")
	}
	for ( var HAC = 0; HAC < 8; HAC++) {
		if (alignHorizontal[HAC][0] == horAlignGrid) {
			horAlignGridDesc = alignHorizontal[HAC][1]
		}
	}
	for ( var HAC = 0; HAC < 5; HAC++) {
		if (alignVertical[HAC][0] == verAlignGrid) {
			verAlignGridDesc = alignVertical[HAC][1]
		}
	}
	for ( var HAC = 0; HAC < 3; HAC++) {
		if (textDirection[HAC][0] == textDirectionVar) {
			textDirectionVarDesc = textDirection[HAC][1]
		}
	}
	function setHorizontalAlignment(combo, record, index) {
		horAlignGrid = alignHorizontal[index][0]
	}
	function setTextDirection(combo, record, index) {
		textDirectionVar = textDirection[index][0]
	}
	function setWrap() {
		if (wrapTextCB.getValue()) {
			textWrap = "normal"
		} else {
			textWrap = "nowrap"
		}
	}
	function setVerticalAlignment(combo, record, index) {
		verAlignGrid = alignVertical[index][0]
	}
	function setTextIndent(indentSpinner, newValue, oldValue) {
		indentGrid = newValue;
		indentGrid = "".concat(indentGrid, "em")
	}
	var wrapTextCB = new Ext.form.Checkbox( {
		name : "wrapText",
		boxLabel : "Wrap text".localize(),
		handler : setWrap,
		checked : isSetWrap
	});
	var indentSpinner = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 250
		}),
		name : "text_indent",
		allowBlank : false,
		xtype : "number",
		width : 60,
		hideLabel : true,
		border : false,
		value : indentGrid,
		listeners : {
			change : {
				fn : setTextIndent,
				scope : this
			}
		}
	});
	var cellsMerged = Jedox.wss.sheet.getMergeState();
	cellsMerged = (cellsMerged == undefined) ? null : cellsMerged;
	var newMerge = cellsMerged;
	var formTriCB = new Ext.ux.form.TriCheckbox( {
		name : "tri-check1",
		xtype : "tri-checkbox",
		id : "tribox",
		fieldLabel : "Merge".localize(),
		hideLabel : true,
		value : cellsMerged,
		listeners : {
			check : function(cmp, state) {
				newMerge = state
			}
		}
	});
	var mergeCells = function() {
		if (newMerge != cellsMerged) {
			switch (newMerge) {
			case true:
				Jedox.wss.sheet.merge();
				break;
			case false:
				Jedox.wss.sheet.unMerge();
				break;
			case null:
				if (!cellsMerged) {
					Jedox.wss.sheet.merge()
				}
				break
			}
		}
	};
	var triCBHolder = new Ext.Panel(
			{
				id : "triCB-holder",
				layout : "column",
				baseCls : "x-plain",
				border : false,
				autoWidth : true,
				frame : false,
				items : [
						{
							autoWidth : true,
							layout : "form",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; text-align: left;",
							autoHeight : true,
							border : false,
							frame : false,
							items : formTriCB
						},
						{
							width : 130,
							layout : "form",
							bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent; padding-left: 5px; line-height: 18px;",
							autoHeight : true,
							border : false,
							frame : false,
							items : [ {
								html : "Merge cells".localize(),
								baseCls : "x-plain"
							} ]
						} ]
			});
	var alignTab = new Ext.Panel(
			{
				layout : "column",
				baseCls : "x-plain",
				listeners : {
					doSelectAlignment : function(callback) {
						if (_pre.textIndent == indentSpinner.getValue()) {
							var ind = ""
						} else {
							var ind = indentSpinner.getValue().concat("em")
						}
						format = {
							textAlign : horAlignGrid,
							verticalAlign : verAlignGrid,
							textIndent : ind,
							whiteSpace : textWrap,
							direction : textDirectionVar
						};
						mergeCells();
						callback(format)
					}
				},
				items : [ {
					columnWidth : 0.99,
					baseCls : "x-plain",
					items : [ new Ext.Panel(
							{
								id : "alignTab",
								bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
								border : false,
								frame : false,
								autoHeight : true,
								autoWidth : true,
								baseCls : "x-title-f",
								header : false,
								items : [
										{
											bodyStyle : "padding: 0px; color: #000000; font-size: 9pt; background-color: transparent;",
											border : true,
											autoHeight : true,
											xtype : "fieldset",
											layout : "form",
											frame : false,
											title : "Text alignment".localize(),
											items : [ new Ext.Panel(
													{
														layout : "column",
														baseCls : "x-plain",
														items : [
																{
																	columnWidth : 0.6,
																	baseCls : "x-plain",
																	items : [
																			{
																				html : "Horizontal"
																						.localize()
																						.concat(
																								":"),
																				baseCls : "x-plain"
																			},
																			{
																				layout : "form",
																				baseCls : "x-plain",
																				autoWidth : true,
																				items : new Ext.form.ComboBox(
																						{
																							store : alignHorizontalStore,
																							displayField : "formatType",
																							hideLabel : true,
																							editable : false,
																							typeAhead : true,
																							mode : "local",
																							triggerAction : "all",
																							value : horAlignGridDesc,
																							selectOnFocus : true,
																							listWidth : 150,
																							width : 150,
																							listeners : {
																								select : {
																									fn : setHorizontalAlignment,
																									scope : this
																								}
																							}
																						})
																			},
																			{
																				baseCls : "x-plain",
																				html : "Vertical"
																						.localize()
																						.concat(
																								":"),
																				bodyStyle : "padding-top: 2px"
																			},
																			{
																				layout : "form",
																				baseCls : "x-plain",
																				autoWidth : true,
																				items : new Ext.form.ComboBox(
																						{
																							store : alignVerticalStore,
																							displayField : "formatType",
																							hideLabel : true,
																							editable : false,
																							typeAhead : true,
																							mode : "local",
																							triggerAction : "all",
																							value : verAlignGridDesc,
																							selectOnFocus : true,
																							listWidth : 150,
																							width : 150,
																							listeners : {
																								select : {
																									fn : setVerticalAlignment,
																									scope : this
																								}
																							}
																						})
																			} ]
																},
																{
																	columnWidth : 0.3,
																	baseCls : "x-plain",
																	items : [
																			{
																				html : "Indent"
																						.localize()
																						.concat(
																								":"),
																				baseCls : "x-plain",
																				bodyStyle : "margin-top: 22px;"
																			},
																			new Ext.Panel(
																					{
																						layout : "form",
																						baseCls : "x-plain",
																						width : 60,
																						items : [ indentSpinner ]
																					}) ]
																} ]
													}) ]
										},
										{
											bodyStyle : "padding: 0px; color: #000000; font-size: 9pt; background-color: transparent;",
											border : true,
											autoHeight : true,
											xtype : "fieldset",
											layout : "form",
											frame : false,
											title : "Text control".localize(),
											items : [ new Ext.Panel( {
												layout : "form",
												baseCls : "x-plain",
												items : [ {
													baseCls : "x-plain",
													items : [ wrapTextCB,
															triCBHolder ]
												} ]
											}) ]
										},
										{
											bodyStyle : "padding: 0px; color: #000000; font-size: 9pt; background-color: transparent;",
											border : true,
											autoHeight : true,
											xtype : "fieldset",
											layout : "form",
											frame : false,
											title : "Right-to-Left".localize(),
											items : [ new Ext.Panel(
													{
														layout : "column",
														baseCls : "x-plain",
														items : [ {
															columnWidth : 0.4,
															baseCls : "x-plain",
															items : [
																	{
																		html : "Text direction"
																				.localize()
																				.concat(
																						":"),
																		baseCls : "x-plain"
																	},
																	new Ext.form.ComboBox(
																			{
																				store : textDirectionStore,
																				displayField : "description",
																				hideLabel : true,
																				editable : false,
																				typeAhead : true,
																				mode : "local",
																				triggerAction : "all",
																				value : textDirectionVarDesc,
																				selectOnFocus : true,
																				listWidth : 100,
																				width : 100,
																				listeners : {
																					select : {
																						fn : setTextDirection,
																						scope : this
																					}
																				}
																			}) ]
														} ]
													}) ]
										} ]
							}) ]
				} ]
			});
	callback(alignTab)
};