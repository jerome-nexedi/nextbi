Jedox.wss.dialog.funcArgs = function(fn) {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var _fnCat = fn[0];
	var _fnName = fn[1];
	var _fnCall = fn[2];
	var _fnDesc = fn[3];
	var _fnParams = fn[4];
	if (!_fnDesc) {
		_fnDesc = "_error: fnc_desc".localize()
	}
	var _activeParam;
	var _maxSequences;
	var _args = _fnParams.length;
	var position = Jedox.wss.app.environment.defaultSelection.getActiveRange()
			.getActiveCell();
	var rawNR = Jedox.wss.backend.conn.cmd(0, [ "nlst" ], [ [ position._x,
			position._y ] ]);
	var namedRangesList = [];
	for ( var z = 0; z < rawNR[0][1][0].length; z++) {
		namedRangesList.push(rawNR[0][1][0][z].name)
	}
	function isInArray(value, array) {
		var isIn = false;
		for ( var c = 0; c < array.length; c++) {
			if (value == array[c]) {
				isIn = true
			}
		}
		return isIn
	}
	var selRangeBtn = Ext.extend(Ext.Button, {
		iconCls : "select-range-icon",
		cls : "x-btn-icon",
		tooltip : "Select Range".localize(),
		initComponent : function() {
			var that = this;
			Ext.apply(this, {});
			selRangeBtn.superclass.initComponent.call(this)
		}
	});
	var valField = Ext.extend(Ext.form.TextField, {
		width : 175,
		hideLabel : true,
		enableKeyEvents : true,
		initComponent : function() {
			var that = this;
			Ext.apply(this, {});
			selRangeBtn.superclass.initComponent.call(this)
		}
	});
	function selRange(selected) {
		win.show();
		setTimeout(function() {
			var f = Ext.getCmp("valFld_seq_".concat(_activeParam));
			f.setValue(selected);
			_activeParam = ""
		}, 1)
	}
	function selRangeB(selected) {
		win.show();
		setTimeout(function() {
			var f = Ext.getCmp("valFld_".concat(_activeParam));
			f.setValue(selected);
			_activeParam = ""
		}, 1)
	}
	var argsHolder = new Ext.Panel( {
		layout : "form",
		baseCls : "x-plain",
		items : []
	});
	var argDescData = {};
	var sargDescData = {};
	var argTypeData = {};
	argsHolder.removeAll(false);
	function addNewSeq(seq, hp) {
		allSeqs++;
		var _seqArgs = seq.m.length;
		for ( var j = 0; j < _seqArgs; j++) {
			lastSeqIndex = lastSeqIndex + 1;
			sargDescData[j + lastSeqIndex] = seq.m[j].d;
			var sargPanel = new Ext.Panel(
					{
						id : "argPan_seq_".concat(lastSeqIndex),
						layout : "column",
						baseCls : "x-plain",
						bodyStyle : "margin-top: 5px;",
						border : false,
						frame : false,
						items : [
								{
									columnWidth : 0.3,
									autoHeight : true,
									baseCls : "x-plain",
									border : false,
									style : "text-align: right; margin-right: 10px;",
									frame : false,
									items : [ {
										html : seq.m[j].n.concat(allSeqs),
										baseCls : "x-plain"
									} ]
								},
								{
									columnWidth : 0.43,
									xtype : "fieldset",
									autoHeight : true,
									baseCls : "x-plain",
									border : false,
									frame : false,
									items : [ new valField(
											{
												id : "valFld_seq_"
														.concat(lastSeqIndex),
												listeners : {
													focus : function() {
														var nr = this.id;
														nr = nr.split("_");
														nr = nr[2];
														win
																.remove("actAttribDesc");
														var dsp = {
															html : sargDescData[nr],
															baseCls : "x-plain",
															height : 70,
															autoScroll : "auto",
															id : "actAttribDesc",
															bodyStyle : "margin-left: 0px; margin-top: 5px;"
														};
														win.add(dsp);
														win.doLayout();
														if (nr == lastSeqIndex
																&& allSeqs < _maxSequences) {
															addNewSeq(rawSeq,
																	lastSeqIndex)
														}
													},
													keypress : function(el, e) {
														var nr = this.id;
														nr = nr.split("_");
														nr = nr[2];
														var kp = e.getKey();
														if (kp == 9) {
															nr = parseInt(nr);
															if (Ext
																	.getCmp("valFld_seq_"
																			.concat(nr + 1))) {
																Ext
																		.getCmp(
																				"valFld_seq_"
																						.concat(nr + 1))
																		.fireEvent(
																				"focus");
																Ext
																		.getCmp(
																				"valFld_seq_"
																						.concat(nr + 1))
																		.focus(
																				true,
																				1)
															}
														}
													},
													keyup : function() {
														var nr = this.id;
														nr = nr.split("_");
														nr = nr[2];
														Ext
																.getCmp(
																		"valArg_val_"
																				.concat(nr))
																.removeAll();
														var dsp = {
															html : "= "
																	.concat(this
																			.getValue()),
															baseCls : "x-plain"
														};
														Ext
																.getCmp(
																		"valArg_val_"
																				.concat(nr))
																.add(dsp);
														Ext
																.getCmp(
																		"valArg_val_"
																				.concat(nr))
																.doLayout()
													}
												}
											}) ]
								},
								{
									columnWidth : 0.07,
									xtype : "fieldset",
									autoHeight : true,
									baseCls : "x-plain",
									border : false,
									frame : false,
									items : [ new selRangeBtn(
											{
												id : "valBtn_seq_"
														.concat(lastSeqIndex),
												handler : function() {
													var nr = this.id;
													nr = nr.split("_");
													nr = nr[2];
													Jedox.wss.app
															.load(
																	Jedox.wss.app.dynJSRegistry.selectRange,
																	[ {
																		fnc : [
																				this,
																				selRange ],
																		format : "{Sheet}!{Range}",
																		rng : Ext
																				.getCmp(
																						"valFld_seq_"
																								.concat(nr))
																				.getValue(),
																		omitInitSheet : true
																	} ]);
													_activeParam = nr;
													win.hide()
												}
											}) ]
								},
								{
									columnWidth : 0.2,
									autoHeight : true,
									baseCls : "x-plain",
									border : false,
									id : "valArg_val_".concat(lastSeqIndex),
									style : "text-align: left; margin-left: 10px;",
									frame : false,
									items : [ {
										html : "= ".concat(seq.m[j].t),
										baseCls : "x-plain"
									} ]
								} ]
					});
			argsHolder.add(sargPanel);
			argsHolder.doLayout()
		}
	}
	var allSeqs = 0;
	var lastSeqIndex = 0;
	function parseSequence(seq) {
		_maxSequences = seq.c;
		var _seqArgs = seq.m.length;
		allSeqs++;
		for ( var j = 0; j < _seqArgs; j++) {
			lastSeqIndex = j;
			sargDescData[j] = seq.m[j].d;
			var sargPanel = new Ext.Panel(
					{
						id : "argPan_seq_".concat(j),
						layout : "column",
						baseCls : "x-plain",
						bodyStyle : "margin-top: 5px;",
						border : false,
						frame : false,
						items : [
								{
									columnWidth : 0.3,
									autoHeight : true,
									baseCls : "x-plain",
									border : false,
									style : "text-align: right; margin-right: 10px;",
									frame : false,
									items : [ {
										html : seq.m[j].n.concat(allSeqs),
										baseCls : "x-plain"
									} ]
								},
								{
									columnWidth : 0.43,
									xtype : "fieldset",
									autoHeight : true,
									baseCls : "x-plain",
									border : false,
									frame : false,
									items : [ new valField(
											{
												id : "valFld_seq_".concat(j),
												listeners : {
													focus : function() {
														var nr = this.id;
														nr = nr.split("_");
														nr = nr[2];
														win
																.remove("actAttribDesc");
														var dsp = {
															html : sargDescData[nr],
															baseCls : "x-plain",
															height : 70,
															autoScroll : "auto",
															id : "actAttribDesc",
															bodyStyle : "margin-left: 0px; margin-top: 5px;"
														};
														win.add(dsp);
														win.doLayout();
														if (nr == lastSeqIndex
																&& allSeqs < _maxSequences) {
															addNewSeq(rawSeq,
																	lastSeqIndex)
														}
													},
													keypress : function(el, e) {
														var nr = this.id;
														nr = nr.split("_");
														nr = nr[2];
														var kp = e.getKey();
														if (kp == 9) {
															nr = parseInt(nr);
															if (Ext
																	.getCmp("valFld_seq_"
																			.concat(nr + 1))) {
																Ext
																		.getCmp(
																				"valFld_seq_"
																						.concat(nr + 1))
																		.fireEvent(
																				"focus");
																Ext
																		.getCmp(
																				"valFld_seq_"
																						.concat(nr + 1))
																		.focus(
																				true,
																				1)
															}
														}
													},
													keyup : function() {
														var nr = this.id;
														nr = nr.split("_");
														nr = nr[2];
														Ext
																.getCmp(
																		"valArg_val_"
																				.concat(nr))
																.removeAll();
														var dsp = {
															html : "= "
																	.concat(this
																			.getValue()),
															baseCls : "x-plain"
														};
														Ext
																.getCmp(
																		"valArg_val_"
																				.concat(nr))
																.add(dsp);
														Ext
																.getCmp(
																		"valArg_val_"
																				.concat(nr))
																.doLayout()
													}
												}
											}) ]
								},
								{
									columnWidth : 0.07,
									xtype : "fieldset",
									autoHeight : true,
									baseCls : "x-plain",
									border : false,
									frame : false,
									items : [ new selRangeBtn(
											{
												id : "valBtn_seq_".concat(j),
												handler : function() {
													var nr = this.id;
													nr = nr.split("_");
													nr = nr[2];
													Jedox.wss.app
															.load(
																	Jedox.wss.app.dynJSRegistry.selectRange,
																	[ {
																		fnc : [
																				this,
																				selRange ],
																		format : "{Sheet}!{Range}",
																		rng : Ext
																				.getCmp(
																						"valFld_seq_"
																								.concat(nr))
																				.getValue(),
																		omitInitSheet : true
																	} ]);
													_activeParam = nr;
													win.hide()
												}
											}) ]
								},
								{
									columnWidth : 0.2,
									autoHeight : true,
									baseCls : "x-plain",
									id : "valArg_val_".concat(j),
									border : false,
									style : "text-align: left; margin-left: 10px;",
									frame : false,
									items : [ {
										html : "= ".concat(seq.m[j].t),
										baseCls : "x-plain"
									} ]
								} ]
					});
			argsHolder.add(sargPanel)
		}
	}
	for ( var i = 0; i < _args; i++) {
		var rawSeq;
		if (_fnParams[i].t == "sequence") {
			rawSeq = _fnParams[i];
			parseSequence(_fnParams[i]);
			break
		}
		argDescData[i] = _fnParams[i].d;
		argTypeData[i] = _fnParams[i].t;
		var argPanel = new Ext.Panel(
				{
					id : "argPan_".concat(i),
					layout : "column",
					baseCls : "x-plain",
					bodyStyle : "margin-top: 5px;",
					border : false,
					frame : false,
					items : [
							{
								columnWidth : 0.3,
								autoHeight : true,
								baseCls : "x-plain",
								border : false,
								style : "text-align: right; margin-right: 10px;",
								frame : false,
								items : [ {
									html : _fnParams[i].n,
									baseCls : "x-plain"
								} ]
							},
							{
								columnWidth : 0.43,
								xtype : "fieldset",
								autoHeight : true,
								baseCls : "x-plain",
								border : false,
								frame : false,
								items : [ new valField(
										{
											id : "valFld_".concat(i),
											listeners : {
												focus : function() {
													var nr = this.id;
													nr = nr.split("_");
													nr = nr[1];
													win.remove("actAttribDesc");
													var dsp = {
														html : argDescData[nr],
														baseCls : "x-plain",
														id : "actAttribDesc",
														height : 70,
														autoScroll : "auto",
														bodyStyle : "margin-left: 0px; margin-top: 5px;"
													};
													win.add(dsp);
													win.doLayout()
												},
												keypress : function(el, e) {
													var nr = this.id;
													nr = nr.split("_");
													nr = nr[1];
													var kp = e.getKey();
													nr = parseInt(nr);
													if (kp == 9) {
														if (Ext
																.getCmp("valFld_"
																		.concat(nr + 1))) {
															Ext
																	.getCmp(
																			"valFld_"
																					.concat(nr + 1))
																	.fireEvent(
																			"focus");
															Ext
																	.getCmp(
																			"valFld_"
																					.concat(nr + 1))
																	.focus(
																			true,
																			1)
														} else {
															if (Ext
																	.getCmp("valFld_seq_0")) {
																Ext
																		.getCmp(
																				"valFld_seq_0")
																		.fireEvent(
																				"focus");
																Ext
																		.getCmp(
																				"valFld_seq_0")
																		.focus(
																				true,
																				1)
															}
														}
													}
												},
												keyup : function() {
													var nr = this.id;
													nr = nr.split("_");
													nr = nr[1];
													Ext
															.getCmp(
																	"val_val_"
																			.concat(nr))
															.removeAll();
													var dsp = {
														html : "= ".concat(this
																.getValue()),
														baseCls : "x-plain"
													};
													Ext
															.getCmp(
																	"val_val_"
																			.concat(nr))
															.add(dsp);
													Ext
															.getCmp(
																	"val_val_"
																			.concat(nr))
															.doLayout()
												},
												blur : function() {
													var nr = this.id;
													nr = nr.split("_");
													nr = nr[1];
													if (argTypeData[nr] == "text") {
														var vale = setQuotes(this
																.getValue());
														Ext
																.getCmp(
																		"val_val_"
																				.concat(nr))
																.removeAll();
														var dsp = {
															html : "= "
																	.concat(vale),
															baseCls : "x-plain"
														};
														Ext
																.getCmp(
																		"val_val_"
																				.concat(nr))
																.add(dsp);
														Ext
																.getCmp(
																		"val_val_"
																				.concat(nr))
																.doLayout()
													}
												}
											}
										}) ]
							},
							{
								columnWidth : 0.07,
								xtype : "fieldset",
								autoHeight : true,
								baseCls : "x-plain",
								border : false,
								frame : false,
								items : [ new selRangeBtn(
										{
											id : "valBtn_".concat(i),
											handler : function() {
												var nr = this.id;
												nr = nr.split("_");
												nr = nr[1];
												Jedox.wss.app
														.load(
																Jedox.wss.app.dynJSRegistry.selectRange,
																[ {
																	fnc : [
																			this,
																			selRangeB ],
																	format : "{Sheet}!{Range}",
																	rng : Ext
																			.getCmp(
																					"valFld_"
																							.concat(nr))
																			.getValue(),
																	omitInitSheet : true
																} ]);
												_activeParam = nr;
												win.hide()
											}
										}) ]
							}, {
								columnWidth : 0.2,
								autoHeight : true,
								baseCls : "x-plain",
								border : false,
								id : "val_val_".concat(i),
								style : "text-align: left; margin-left: 10px;",
								frame : false,
								items : [ {
									html : "= ".concat(_fnParams[i].t),
									baseCls : "x-plain"
								} ]
							} ]
				});
		argsHolder.add(argPanel)
	}
	if (_args < 1) {
		argsHolder.add( {
			html : "fnc_no_params".localize(),
			baseCls : "x-plain"
		})
	}
	argsHolder.doLayout();
	function setQuotes(value) {
		var refs = Jedox.wss.formula.parse(value);
		return refs.sgn.replace(/@\d+/g, "") == value.replace(/\$/g, "")
				|| isInArray(value, namedRangesList) ? value : '"'.concat(
				value, '"')
	}
	function setFunction() {
		var tmpArr = [];
		for ( var w = 0; w < _args; w++) {
			if (Ext.getCmp("valFld_".concat(w))) {
				if (argTypeData[w] == undefined || argTypeData[w] == "text") {
					var val = setQuotes(Ext.getCmp("valFld_".concat(w))
							.getValue())
				} else {
					var val = Ext.getCmp("valFld_".concat(w)).getValue()
				}
				tmpArr.push(val)
			}
		}
		for ( var z = 0; z < allSeqs; z++) {
			if (argTypeData[z] == undefined || argTypeData[z] == "text") {
				var val = setQuotes(Ext.getCmp("valFld_seq_".concat(z))
						.getValue())
			} else {
				var val = Ext.getCmp("valFld_seq_".concat(z)).getValue()
			}
			tmpArr.push(val)
		}
		var num = tmpArr.length;
		for ( var r = num; r > -1; r--) {
			if ((tmpArr[r] == undefined) || (tmpArr[r] == "") || (!tmpArr[r])) {
				tmpArr.splice(r, 1)
			} else {
				r = -1
			}
		}
		var func = "".concat(_fnName, "(");
		var fDelimiter = Jedox.wss.i18n.separators[2];
		for ( var t = 0; t < tmpArr.length; t++) {
			if (t == 0) {
				func = func.concat(tmpArr[t])
			} else {
				func = func.concat(fDelimiter, tmpArr[t])
			}
		}
		func = func.concat(")");
		var env = Jedox.wss.app.environment, GridMode = Jedox.wss.grid.GridMode, inputField = env.inputField;
		Jedox.wss.general.setInputMode(GridMode.EDIT);
		Jedox.wss.app.lastInputMode = GridMode.READY;
		Jedox.wss.general.showInputField(null, false, true);
		inputField.value = (inputField.value.length == 0
				|| inputField.value.charAt(0) != "=" ? "=" : inputField.value)
				.concat(func);
		Jedox.wss.keyboard.setFieldSize();
		Jedox.wss.general.focusInputField();
		win.close()
	}
	var win = new Ext.Window(
			{
				title : "Function Arguments".localize(),
				closable : true,
				id : "function-args",
				autoDestroy : true,
				plain : true,
				constrain : true,
				cls : "default-format-window",
				modal : true,
				resizable : false,
				enableHdMenu : false,
				animCollapse : false,
				width : 500,
				height : 400,
				layout : "form",
				bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
				listeners : {
					close : function() {
					},
					destroy : function() {
					}
				},
				items : [
						new Ext.Panel(
								{
									border : false,
									frame : false,
									autoHeight : true,
									id : "sample",
									header : false,
									height : 200,
									baseCls : "x-title-f",
									items : [ {
										autoWidth : true,
										xtype : "fieldset",
										bodyStyle : "padding-top: 0px; background-color: transparent;",
										layout : "form",
										border : true,
										height : 200,
										autoScroll : true,
										title : _fnName,
										items : [ argsHolder ]
									} ]
								}), {
							html : _fnDesc,
							baseCls : "x-plain"
						}, {
							html : "",
							height : 70,
							autoScroll : "auto",
							baseCls : "x-plain",
							id : "actAttribDesc",
							bodyStyle : "margin-left: 0px; margin-top: 5px;"
						} ],
				buttons : [
						{
							text : "OK".localize(),
							handler : setFunction
						},
						{
							text : "Cancel".localize(),
							handler : function() {
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
								Jedox.wss.app
										.unload(Jedox.wss.app.dynJSRegistry.formatControl);
								win.close()
							}
						} ]
			});
	win.show(this)
};