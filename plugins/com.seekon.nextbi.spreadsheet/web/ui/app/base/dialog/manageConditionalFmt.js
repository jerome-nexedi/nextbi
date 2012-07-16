Jedox.wss.dialog.manageConditionalFormatting = function() {
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
	function isArray(obj) {
		if (obj.constructor.toString().indexOf("Array") == -1) {
			return false
		} else {
			return true
		}
	}
	var fromCFDLG;
	var _applies_to;
	var _to_apply = [];
	var _to_delete = [];
	var _idd;
	var _selRow;
	var _selected;
	var _sel;
	var _currSel = false;
	var currWSId = Jedox.wss.app.activeBook.getSheetSelector()
			.getActiveSheetId();
	var _toEdit = false;
	var openCFDLG = function(fmDesc, toEdit) {
		win.show();
		fromCFDLG = fmDesc;
		if (!fromCFDLG) {
			return
		}
		if (toEdit) {
			fromCFDLG[0].id = _idd;
			fromCFDLG[0].applies_to = _applies_to;
			_toEdit = toEdit;
			var _double = false;
			for ( var q = 0; q < _to_apply.length; q++) {
				if (fromCFDLG[0].id == _to_apply[q].id) {
					_double = true;
					_to_apply[q] = fromCFDLG[0]
				}
			}
			if (!_double) {
				_to_apply.push(fromCFDLG[0])
			}
			gridData.splice(_selIdx, 1, parseRawRule(fromCFDLG, true))
		} else {
			_to_apply.push(fromCFDLG[0]);
			gridData.push(parseRawRule(fromCFDLG, true))
		}
		gridStore.loadData(gridData);
		grid.reconfigure(gridStore, gridCM);
		gridStore.loadData(gridData);
		Ext.getCmp("manage-cnd-apply-button").enable();
		Ext.getCmp("manage-cf-close-button").setText("Cancel")
	};
	Ext.grid.CheckColumn = function(config) {
		Ext.apply(this, config);
		if (!this.id) {
			this.id = Ext.id()
		}
		this.renderer = this.renderer.createDelegate(this)
	};
	Ext.grid.CheckColumn.prototype = {
		init : function(grid) {
			this.grid = grid;
			this.grid.on("render", function() {
				var view = this.grid.getView();
				view.mainBody.on("mousedown", this.onMouseDown, this)
			}, this)
		},
		onMouseDown : function(e, t) {
			if (t.className
					&& t.className.indexOf("x-grid3-cc-" + this.id) != -1) {
				e.stopEvent();
				var index = this.grid.getView().findRowIndex(t);
				var record = this.grid.store.getAt(index);
				record.set(this.dataIndex, !record.data[this.dataIndex]);
				_ident = gridData[index][0];
				var _tmp = gridData[index][1];
				var _tmc = gridData[index][2];
				var _act = record.data[this.dataIndex];
				_to_apply.push(rawRules[index]);
				Ext.getCmp("manage-cnd-apply-button").enable();
				Ext.getCmp("manage-cf-close-button").setText("Cancel")
			}
		},
		renderer : function(v, p, record) {
			p.css += " x-grid3-check-col-td";
			return '<div class="x-grid3-check-col' + (v ? "-on" : "")
					+ " x-grid3-cc-" + this.id + '"> </div>'
		}
	};
	var cndfmt = Jedox.wss.cndfmt;
	var worksheets = Jedox.wss.backend.ha.getSheets()[0];
	var putWSinStore = function() {
		for (i = 1; worksheets[i] != null; i = i + 2) {
			var j = i - 1;
			if (worksheets[j] != currWSId) {
				selectionData.push( [ worksheets[i], worksheets[j] ])
			}
		}
	};
	var helperData = [ [ "duplicate".localize(), "duplicate_value" ],
			[ "unique".localize(), "unique_value" ],
			[ "Cell Value".localize(), "cell_value" ],
			[ "Specific Text".localize(), "text" ],
			[ "Dates Occurring".localize(), "dates_occ" ],
			[ "Blanks".localize(), "blanks" ],
			[ "Use a formula".localize(), "formula_true" ],
			[ "No Blanks".localize(), "no_blanks" ],
			[ "Errors".localize(), "errors" ],
			[ "No errors".localize(), "no_errors" ],
			[ "between".localize(), "between" ],
			[ "not between".localize(), "not_between" ],
			[ "equal to".localize(), "=" ],
			[ "not equal to".localize(), "<>" ],
			[ "grater than".localize(), ">" ], [ "less than".localize(), "<" ],
			[ "greater than or equal to".localize(), ">=" ],
			[ "less than or equal to".localize(), "<=" ],
			[ "containing".localize(), "contained" ],
			[ "not containing".localize(), "not_contained" ],
			[ "beginning with".localize(), "begins_with" ],
			[ "ending with".localize(), "ends_with" ],
			[ "Top".localize(), "top" ], [ "Bottom".localize(), "bottom" ],
			[ "Top".localize(), "top_percent" ],
			[ "Bottom".localize(), "bottom_percent" ],
			[ "duplicate".localize(), "duplicate_value" ],
			[ "unique".localize(), "unique_value" ],
			[ "Average Value".localize(), "average_value" ] ];
	var helperDataAverage = [ [ "above".localize(), ">" ],
			[ "below".localize(), "<" ], [ "equal or above".localize(), ">=" ],
			[ "equal or below".localize(), "<=" ],
			[ "1 std dev above".localize(), "std_dev_above_1" ],
			[ "1 std dev below".localize(), "std_dev_below_1" ],
			[ "2 std dev above".localize(), "std_dev_above_2" ],
			[ "2 std dev below".localize(), "std_dev_below_2" ],
			[ "3 std dev above".localize(), "std_dev_above_3" ],
			[ "3 std dev below".localize(), "std_dev_below_3" ] ];
	var selectionData = [ [ "Current Selection".localize(), "SCOPE_CURR_SEL" ],
			[ "This Worksheet".localize(), "SCOPE_CURR_WKS" ] ];
	putWSinStore();
	var selectionStore = new Ext.data.SimpleStore( {
		fields : [ "rules_for", "id" ],
		data : selectionData
	});
	var gridData = [];
	var rawRules = cndfmt.get(cndfmt.SCOPE_CURR_SEL);
	var parseRawRule = function(_RR, _one) {
		for ( var i = 0; i < _RR.length; i++) {
			var _o = _RR[i];
			var _desc;
			for ( var j = 0; j < helperData.length; j++) {
				if (_o.type == helperData[j][1]) {
					_desc = helperData[j][0]
				}
			}
			if (_o.type == "average_value") {
				for ( var e = 0; e < helperDataAverage.length; e++) {
					if (_o.operator == helperDataAverage[e][1]) {
						_desc = _desc.concat(" ", helperDataAverage[e][0])
					}
				}
			} else {
				for ( var e = 0; e < helperData.length; e++) {
					if (_o.operator == helperData[e][1]) {
						_desc = _desc.concat(" ", helperData[e][0])
					}
				}
			}
			if (_o.operands) {
				if (_o.operands.length == 1) {
					_desc = _desc.concat(" ", _o.operands[0].replace(/=/i, ""))
				} else {
					if (isArray(_o.operands)) {
						for ( var o = 0; o < _o.operands.length; o++) {
							if (o == 0) {
								_desc = _desc.concat(" ", _o.operands[o]
										.replace(/=/i, ""))
							} else {
								_desc = _desc.concat(" and ", _o.operands[o]
										.replace(/=/i, ""))
							}
						}
					} else {
						_desc = _desc
								.concat(" ", _o.operands.replace(/=/i, ""))
					}
				}
				if (!(_o.operands instanceof Array)) {
					_o.operands = [ _o.operands ]
				}
			}
			if (!_o.style) {
				_o.style = {}
			}
			if (!_o.borders) {
				_o.borders = {}
			}
			if (!_o.borders.top) {
				_o.borders.top = {
					width : "",
					type : "",
					color : ""
				}
			}
			if (!_o.borders.bottom) {
				_o.borders.bottom = {
					width : "",
					type : "",
					color : ""
				}
			}
			if (!_o.borders.left) {
				_o.borders.left = {
					width : "",
					type : "",
					color : ""
				}
			}
			if (!_o.borders.right) {
				_o.borders.right = {
					width : "",
					type : "",
					color : ""
				}
			}
			var _sample = '<div style="font-style: '.concat(_o.style.fontStyle,
					"; background:", _o.style.backgroundColor,
					"; text-decoration:", _o.style.textDecoration,
					"; font-weight: ", _o.style.fontWeight, "; color:",
					_o.style.color, "; background-image:",
					_o.style.backgroundImage, ";border-top:",
					_o.borders.top.width, " ", _o.borders.top.type, " ",
					_o.borders.top.color, ";", ";border-bottom:",
					_o.borders.bottom.width, " ", _o.borders.bottom.type, " ",
					_o.borders.bottom.color, ";", ";border-left:",
					_o.borders.left.width, " ", _o.borders.left.type, " ",
					_o.borders.left.color, ";", ";border-right:",
					_o.borders.right.width, " ", _o.borders.right.type, " ",
					_o.borders.right.color,
					'; line-height: 18px; text-align: center;">AaBbCcZz</div>');
			if (!_desc) {
				_desc = "undefined"
			}
			var id = _o.id;
			var _a = [ _desc, _sample, _o.applies_to, true, i, id, _o ];
			if (_one) {
				return _a
			} else {
				gridData.push(_a)
			}
		}
	};
	parseRawRule(rawRules);
	var gridStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "rule"
		}, {
			name : "format"
		}, {
			name : "applies"
		}, {
			name : "stop"
		}, {
			name : "id"
		} ]
	});
	gridStore.loadData(gridData);
	function changeSelection(combo, record, index) {
		gridData = [];
		gridStore.removeAll();
		if (index == 1) {
			rawRules = cndfmt.get(cndfmt.SCOPE_CURR_WKS);
			_currSel = true
		} else {
			if (index == 0) {
				rawRules = cndfmt.get(cndfmt.SCOPE_CURR_SEL);
				_currSel = false
			} else {
				rawRules = cndfmt.get(selectionData[index][1])
			}
		}
		parseRawRule(rawRules);
		gridStore.loadData(gridData);
		grid.reconfigure(gridStore, gridCM)
	}
	var selection = new Ext.form.ComboBox( {
		store : selectionStore,
		displayField : "rules_for",
		hideLabel : true,
		typeAhead : true,
		mode : "local",
		value : selectionData[0][0],
		triggerAction : "all",
		width : 200,
		listeners : {
			select : {
				fn : changeSelection,
				scope : this
			}
		}
	});
	var newRuleBtn = new Ext.Button( {
		minWidth : 100,
		cls : "x-btn-text-icon",
		iconCls : "cnd_fmt_new",
		text : "New Rule".localize().concat("..."),
		listeners : {
			click : {
				fn : function() {
					Jedox.wss.app.load(
							Jedox.wss.app.dynJSRegistry.conditionalFormatting,
							[ "fromConditionalManage", openCFDLG ]);
					win.hide()
				},
				scope : this
			}
		}
	});
	var editRuleBtn = new Ext.Button( {
		minWidth : 100,
		cls : "x-btn-text-icon",
		iconCls : "cnd_fmt_edit",
		text : "Edit Rule".localize().concat("..."),
		listeners : {
			click : {
				fn : function() {
					_selected = gridData[_selIdx][6].id;
					_applies_to = gridData[_selIdx][6].applies_to;
					_sel = gridData[_selIdx][4];
					_idd = gridData[_selIdx][5];
					Jedox.wss.app.load(
							Jedox.wss.app.dynJSRegistry.conditionalFormatting,
							[ "editFromConditionalManage", openCFDLG,
									gridData[_selIdx][6] ]);
					win.hide()
				},
				scope : this
			}
		}
	});
	var _smt_to_delete = false;
	var deleRuleBtn = new Ext.Button( {
		minWidth : 100,
		cls : "x-btn-text-icon",
		iconCls : "cnd_fmt_delete",
		text : "Delete Rule".localize(),
		listeners : {
			click : {
				fn : function() {
					_to_delete.push(_idd);
					_smt_to_delete = true;
					gridData.splice(_selIdx, 1);
					Ext.getCmp("manage-cnd-apply-button").enable();
					Ext.getCmp("manage-cf-close-button").setText("Cancel");
					gridStore.loadData(gridData);
					grid.reconfigure(gridStore, gridCM)
				},
				scope : this
			}
		}
	});
	var upBtn = new Ext.Button( {
		cls : "x-btn-icon",
		iconCls : "cnd_fmt_up"
	});
	var downBtn = new Ext.Button( {
		cls : "x-btn-icon",
		iconCls : "cnd_fmt_down"
	});
	var gridCM = new Ext.grid.ColumnModel(
			[
					{
						id : "rule",
						header : "Rule (applied in order shown)".localize(),
						width : 180,
						sortable : false,
						dataIndex : "rule"
					},
					{
						header : "Format".localize(),
						width : 120,
						sortable : false,
						dataIndex : "format"
					},
					{
						id : "applies",
						header : "Applies to".localize(),
						width : 100,
						sortable : false,
						dataIndex : "applies",
						editor : new Ext.form.TriggerField(
								{
									id : "triggerEditorF",
									listeners : {
										change : function(e, newValue, oldValue) {
											var index = _selIdx;
											var record = grid.store
													.getAt(index);
											_ident = gridData[index][0];
											var _tmp = gridData[index][1];
											var _tmc = gridData[index][2];
											var _act = record.data[this.dataIndex];
											rawRules[index].applies_to = newValue;
											var _double = false;
											for ( var q = 0; q < _to_apply.length; q++) {
												if (rawRules[index].id == _to_apply[q].id) {
													_double = true;
													_to_apply[q].applies_to = rawRules[index].applies_to
												}
											}
											if (!_double) {
												_to_apply.push(rawRules[index])
											}
											Ext.getCmp(
													"manage-cnd-apply-button")
													.enable();
											Ext
													.getCmp(
															"manage-cf-close-button")
													.setText("Cancel")
										}
									},
									onTriggerClick : function() {
										var stil = grid.getSelectionModel()
												.getSelected().data.tf;
										var tData = grid.getSelectionModel()
												.getSelected().data;
										function selRange(selected) {
											win.show();
											setTimeout(
													function() {
														var index = _gid;
														var record = grid.store
																.getAt(index);
														_ident = gridData[index][0];
														var _tmp = gridData[index][1];
														var _tmc = gridData[index][2];
														rawRules[index].applies_to = "="
																.concat(selected);
														var _double = false;
														for ( var q = 0; q < _to_apply.length; q++) {
															if (rawRules[index].id == _to_apply[q].id) {
																_double = true;
																_to_apply[q].applies_to = rawRules[index].applies_to
															}
														}
														if (!_double) {
															_to_apply
																	.push(rawRules[index])
														}
														record
																.set(
																		"applies",
																		"="
																				.concat(selected));
														Ext
																.getCmp(
																		"manage-cnd-apply-button")
																.enable();
														Ext
																.getCmp(
																		"manage-cf-close-button")
																.setText(
																		"Cancel")
													}, 1)
										}
										win.hide();
										Jedox.wss.app
												.load(
														Jedox.wss.app.dynJSRegistry.selectRange,
														[ {
															fnc : [ this,
																	selRange ],
															rng : Ext
																	.getCmp(
																			"triggerEditorF")
																	.getValue(),
															format : "{Sheet}!{Range}",
															omitInitSheet : true
														} ]);
										var _gid = grid.getSelectionModel()
												.getSelected().data.id
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
									}
								})
					} ]);
	var _selIdx;
	var grid = new Ext.grid.EditorGridPanel( {
		store : gridStore,
		clicksToEdit : 1,
		cm : gridCM,
		stripeRows : false,
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : true
		}),
		viewConfig : {
			forceFit : true
		},
		enableHdMenu : false,
		autoExpandColumn : "applies",
		height : 200,
		autoWidth : true,
		baseCls : "x-plain",
		listeners : {
			rowclick : function(gr, index, e) {
				_selRow = index
			},
			rowdblclick : function(grid, rowIndex, e) {
				_selected = gridData[rowIndex][6].id;
				_applies_to = gridData[rowIndex][6].applies_to;
				_sel = gridData[rowIndex][4];
				_idd = gridData[rowIndex][5];
				Jedox.wss.app.load(
						Jedox.wss.app.dynJSRegistry.conditionalFormatting, [
								"editFromConditionalManage", openCFDLG,
								gridData[rowIndex][6] ]);
				win.hide()
			}
		}
	});
	grid.getSelectionModel().on("rowselect", function(sm, rowIdx, r) {
		_selected = gridData[rowIdx].id;
		_sel = gridData[rowIdx][4];
		_selIdx = rowIdx;
		_idd = gridData[rowIdx][5]
	});
	var mainApplyButton = {
		text : "Apply".localize(),
		disabled : true,
		id : "manage-cnd-apply-button",
		handler : function() {
			if (_smt_to_delete) {
				cndfmt.remove(cndfmt.SCOPE_RULE_IDS, _to_delete)
			}
			if (_to_apply.length) {
				cndfmt.set(_to_apply)
			}
			_smt_to_delete = false;
			_to_apply = [];
			_to_delete = [];
			Ext.getCmp("manage-cnd-apply-button").disable();
			Ext.getCmp("manage-cf-close-button").setText("Close")
		}
	};
	var win = new Ext.Window(
			{
				title : "Conditional Formatting Rules Manager".localize(),
				closable : true,
				autoDestroy : true,
				plain : true,
				constrain : true,
				modal : true,
				cls : "default-format-window",
				resizable : false,
				animCollapse : false,
				width : 600,
				height : 350,
				items : [ new Ext.Panel(
						{
							baseCls : "x-title-f",
							labelWidth : 100,
							labelAlign : "left",
							layout : "form",
							bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
							frame : false,
							header : false,
							monitorValid : true,
							autoHeight : true,
							autoWidth : true,
							items : [
									{
										layout : "column",
										border : false,
										baseCls : "x-plain",
										items : [
												{
													layout : "form",
													border : false,
													width : 160,
													baseCls : "x-plain",
													items : [ {
														html : "Show formatting rules for"
																.localize()
																.concat(":"),
														baseCls : "x-plain",
														bodyStyle : "padding-top: 3px;"
													} ]
												}, {
													layout : "form",
													border : false,
													autoWidth : true,
													baseCls : "x-plain",
													items : selection
												} ]
									},
									{
										layout : "column",
										border : false,
										bodyStyle : "padding-top: 3px; margin-bottom: 5px;",
										baseCls : "x-plain",
										items : [ {
											layout : "form",
											border : false,
											width : 105,
											baseCls : "x-plain",
											items : [ newRuleBtn ]
										}, {
											layout : "form",
											border : false,
											width : 105,
											baseCls : "x-plain",
											items : [ editRuleBtn ]
										}, {
											layout : "form",
											border : false,
											width : 105,
											baseCls : "x-plain",
											items : [ deleRuleBtn ]
										} ]
									}, grid ]
						}) ],
				listeners : {
					close : function() {
						Jedox.wss.general
								.setInputMode(Jedox.wss.app.lastInputModeDlg);
						Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.manageConditionalFormatting)
					}
				},
				buttons : [
						{
							text : "OK".localize(),
							handler : function() {
								if (_smt_to_delete) {
									cndfmt.remove(cndfmt.SCOPE_RULE_IDS,
											_to_delete)
								}
								if (_to_apply.length) {
									cndfmt.set(_to_apply)
								}
								win.destroy();
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
							}
						},
						{
							text : "Close".localize(),
							id : "manage-cf-close-button",
							handler : function() {
								win.destroy();
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
							}
						}, mainApplyButton ]
			});
	win.show(this)
};