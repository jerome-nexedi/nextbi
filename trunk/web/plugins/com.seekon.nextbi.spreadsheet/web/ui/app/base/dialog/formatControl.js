Jedox.wss.dialog.formatControl = function(conf) {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var isEdit = !!(conf.id);
	if (isEdit) {
		var _initialConf = conf
	}
	var cbState;
	var cbPreState = {
		unchecked : false,
		checked : false,
		mixed : false
	};
	if (conf.type == Jedox.wss.wsel.type.CHECK_BOX) {
		cbPreState[conf.state] = true;
		var labelNM = "CheckBox Label".localize();
		if (!isEdit) {
			cbState = "unchecked"
		} else {
			cbState = conf.state
		}
		var txtNM = "CheckBox Name".localize()
	} else {
		if (conf.type == Jedox.wss.wsel.type.BUTTON) {
			var txtNM = "Button Name".localize();
			labelNM = "Button Label".localize()
		} else {
			var txtNM = "ComboBox Name".localize()
		}
	}
	var formElement = Jedox.wss.wsel;
	var _config = {
		winW : 300,
		mainPanelLabelWidth : 150,
		impPath : "res/img/dialog/"
	};
	var FunctionTypesForm = Ext.extend(Ext.Panel, {
		border : false,
		bodyStyle : "background-color: transparent;",
		autoWidth : true,
		autoHeight : true,
		typeDisabled : false,
		functionValue : "",
		labelWidth : 150,
		comboWidth : 150,
		textWidth : 250,
		comboLabel : "Subset".localize(),
		textFieldLabel : "Formula".localize(),
		getFuncText : function() {
			return this._txtFunc.getValue()
		},
		getTypeDisabled : function() {
			return this.typeDisabled
		},
		initComponent : function() {
			that = this;
			Ext.apply(this, {
				layout : "form"
			});
			FunctionTypesForm.superclass.initComponent.call(this);
			var setFuncText = function(funcText) {
				that._txtFunc.setValue(funcText)
			};
			this._rbTypes = new Ext.form.Radio( {
				checked : !this.typeDisabled,
				name : "radioSourceForm",
				boxLabel : this.comboLabel,
				labelWidth : 150,
				hideLabel : true,
				listeners : {
					check : function(thisRb, isChecked) {
						if (isChecked) {
							that.typeDisabled = false;
							that._btnWizard.enable();
							that._txtFunc.disable()
						}
					}
				}
			});
			this._btnWizard = new Ext.Button( {
				disabled : this.typeDisabled,
				ctCls : "stdButtonsSmall",
				text : "...",
				listeners : {
					click : function() {
						Jedox.wss.app.load(
								Jedox.wss.app.dynJSRegistry.subsetEditor, [ {
									mode : 3,
									fnc : setFuncText
								} ])
					}
				}
			});
			this._rbFunc = new Ext.form.Radio( {
				checked : this.typeDisabled,
				name : "radioSourceForm",
				boxLabel : this.textFieldLabel,
				hideLabel : true,
				listeners : {
					check : function(thisRb, isChecked) {
						if (isChecked) {
							that.typeDisabled = true;
							that._btnWizard.disable();
							that._txtFunc.enable()
						}
					}
				}
			});
			this._txtFunc = new Ext.form.TextField( {
				disabled : !this.typeDisabled,
				hideLabel : true,
				width : this.textWidth,
				allowBlank : true,
				value : this.functionValue
			});
			this.add(new Ext.Panel( {
				border : false,
				layout : "column",
				bodyStyle : "background-color:transparent;",
				autoWidth : true,
				autoHeight : true,
				items : [ new Ext.Panel( {
					labelWidth : this.labelWidth,
					border : false,
					layout : "form",
					bodyStyle : "background-color:transparent;",
					width : 150,
					autoHeight : true,
					items : [ this._rbTypes ]
				}), {
					border : false,
					bodyStyle : "background-color:transparent;",
					html : "&nbsp;&nbsp;&nbsp;"
				}, new Ext.Panel( {
					border : false,
					layout : "form",
					bodyStyle : "background-color:transparent;",
					autoWidth : true,
					autoHeight : true,
					items : [ this._btnWizard ]
				}) ]
			}));
			this.add(this._rbFunc);
			this.add(this._txtFunc)
		}
	});
	var TargetForm = Ext
			.extend(
					Ext.Panel,
					{
						border : false,
						bodyStyle : "background-color: transparent;",
						autoWidth : true,
						autoHeight : true,
						element_name : "ComboBoxN",
						iconImgPath : "",
						rangeEnabled : false,
						varEnabled : false,
						varValue : "",
						rangeValue : "",
						nameTextWidth : 150,
						rangeTextWidth : 210,
						rbRangeLabel : "Cell/Range".localize(),
						rbNameLabel : "Named Range".localize().concat(":"),
						rbVarLabel : "Variable".localize(),
						initComponent : function() {
							var that = this;
							Ext.apply(this, {
								layout : "form"
							});
							TargetForm.superclass.initComponent.call(this);
							this._rbRange = new Ext.form.Radio( {
								checked : this.rangeEnabled,
								name : "radioTargetForm",
								boxLabel : this.rbRangeLabel,
								hideLabel : true,
								listeners : {
									check : function(thisRb, isChecked) {
										if (isChecked) {
											that.rangeEnabled = true;
											that._txtRange.enable();
											that._btnTargetSelect.enable();
											that._fldName.disable();
											that._fldVar.disable()
										}
									}
								}
							});
							this._txtRange = new Ext.form.TextField( {
								disabled : !this.rangeEnabled,
								hideLabel : true,
								width : this.rangeTextWidth,
								allowBlank : true,
								value : this.rangeValue
							});
							this._btnTargetSelect = new Ext.Toolbar.Button(
									{
										disabled : !this.rangeEnabled,
										iconCls : "select-range-icon",
										cls : "x-btn-icon",
										tooltip : "Select Cell".localize(),
										listeners : {
											click : function() {
												if (that.winToHide) {
													that.winToHide.hide()
												}
												Jedox.wss.app
														.load(
																Jedox.wss.app.dynJSRegistry.selectRange,
																[ {
																	fnc : [
																			this,
																			function(
																					tmpStr) {
																				that._txtRange
																						.setValue(tmpStr);
																				if (that.winToHide) {
																					that.winToHide
																							.show()
																				}
																			} ],
																	format : "{Sheet}!{$Range}",
																	omitInitSheet : true,
																	rng : that._txtRange
																			.getValue()
																} ])
											}
										}
									});
							this._rbName = new Ext.form.Radio(
									{
										checked : !(this.rangeEnabled || this.varEnabled),
										name : "radioTargetForm",
										boxLabel : this.rbNameLabel,
										hideLabel : true,
										listeners : {
											check : function(thisRb, isChecked) {
												if (isChecked) {
													that.rangeEnabled = false;
													that.varEnabled = false;
													that._txtRange.disable();
													that._btnTargetSelect
															.disable();
													that._fldName.enable();
													that._fldVar.disable()
												}
											}
										}
									});
							this._rbVar = new Ext.form.Radio( {
								checked : this.varEnabled,
								name : "radioTargetForm",
								boxLabel : this.rbVarLabel,
								hideLabel : true,
								listeners : {
									check : function(thisRb, isChecked) {
										if (isChecked) {
											that.rangeEnabled = false;
											that._fldVar.enable();
											that._txtRange.disable();
											that._btnTargetSelect.disable();
											that._fldName.disable()
										}
									}
								}
							});
							this._fldName = new Ext.form.MiscField(
									{
										disabled : (this.rangeEnabled || this.varEnabled),
										style : "padding:3px 3px 3px 0px;",
										value : this.element_name,
										hideLabel : true,
										labelSeparator : "",
										listeners : {
											disable : function(thisMf) {
												Ext.DomHelper
														.applyStyles(
																thisMf.el.dom.parentNode.parentNode.childNodes[0],
																"color:gray;opacity:0.6;")
											},
											enable : function(thisMf) {
												Ext.DomHelper
														.applyStyles(
																thisMf.el.dom.parentNode.parentNode.childNodes[0],
																"color:black;opacity:1;")
											}
										}
									});
							this._fldVar = new Ext.form.TextField( {
								disabled : !this.varEnabled,
								value : this.varValue,
								hideLabel : true,
								labelSeparator : "",
								listeners : {}
							});
							this
									.add(
											this._rbRange,
											new Ext.Panel(
													{
														border : false,
														layout : "column",
														bodyStyle : "background-color:transparent;",
														autoWidth : true,
														autoHeight : true,
														items : [
																new Ext.Panel(
																		{
																			labelWidth : that.labelWidth,
																			border : false,
																			layout : "form",
																			bodyStyle : "background-color:transparent;",
																			autoWidth : true,
																			autoHeight : true,
																			items : [ this._txtRange ]
																		}),
																{
																	border : false,
																	bodyStyle : "background-color:transparent;",
																	html : "&nbsp;&nbsp;&nbsp;"
																},
																new Ext.Panel(
																		{
																			labelWidth : that.labelWidth,
																			border : false,
																			layout : "form",
																			bodyStyle : "background-color:transparent;",
																			autoWidth : true,
																			autoHeight : true,
																			items : [ this._btnTargetSelect ]
																		}) ]
													}),
											new Ext.Panel(
													{
														border : false,
														layout : "column",
														bodyStyle : "background-color:transparent;",
														autoWidth : true,
														autoHeight : true,
														items : [
																new Ext.Panel(
																		{
																			labelWidth : that.labelWidth,
																			border : false,
																			layout : "form",
																			bodyStyle : "background-color:transparent;",
																			autoWidth : true,
																			autoHeight : true,
																			items : [ this._rbName ]
																		}),
																{
																	border : false,
																	bodyStyle : "background-color:transparent;",
																	html : "&nbsp;&nbsp;&nbsp;"
																},
																new Ext.Panel(
																		{
																			labelWidth : that.labelWidth,
																			border : false,
																			layout : "form",
																			bodyStyle : "background-color:transparent;",
																			autoWidth : true,
																			autoHeight : true,
																			items : [ this._fldName ]
																		}) ]
													}),
											new Ext.Panel(
													{
														border : false,
														layout : "column",
														bodyStyle : "background-color:transparent;",
														autoWidth : true,
														autoHeight : true,
														items : [
																new Ext.Panel(
																		{
																			labelWidth : that.labelWidth,
																			border : false,
																			layout : "form",
																			bodyStyle : "background-color:transparent;",
																			autoWidth : true,
																			autoHeight : true,
																			items : [ this._rbVar ]
																		}),
																{
																	border : false,
																	bodyStyle : "background-color:transparent;",
																	html : "&nbsp;&nbsp;&nbsp;"
																},
																new Ext.Panel(
																		{
																			labelWidth : that.labelWidth,
																			border : false,
																			layout : "form",
																			bodyStyle : "background-color:transparent;",
																			autoWidth : true,
																			autoHeight : true,
																			items : [ this._fldVar ]
																		}) ]
													}))
						},
						setWindowToHide : function(winToH) {
							this.winToHide = winToH
						},
						setName : function(nameValue) {
							this._fldName.setValue(nameValue)
						},
						getRangeValues : function() {
							var retData = {};
							retData.name = ((this._rbName.checked) ? this._fldName
									.getValue()
									: "");
							retData.range = ((this._rbRange.checked) ? this._txtRange
									.getValue()
									: "");
							retData.variable = ((this._rbVar.checked) ? this._fldVar
									.getValue()
									: "");
							return retData
						}
					});
	if (conf) {
		var panFnc = new FunctionTypesForm( {
			typeDisabled : (!!conf.formulaEnabled),
			functionValue : conf.src,
			labelWidth : 75,
			comboWidth : 170,
			textWidth : 440
		});
		var panTar = new TargetForm(
				{
					element_name : conf.name,
					rangeEnabled : ((conf.trange == "" || !conf.trange) ? false
							: true),
					rangeValue : ((conf.trange == "" || !conf.trange) ? ""
							: conf.trange),
					varEnabled : ((conf.tvar == "" || !conf.tvar) ? false
							: true),
					varValue : ((conf.tvar == "" || !conf.tvar) ? ""
							: conf.tvar),
					iconImgPath : _config.impPath
				})
	} else {
		var panFnc = new FunctionTypesForm( {});
		var panTar = new TargetForm( {
			element_name : formElement.type.COMBO_BOX.concat(formElement
					.countEl(formElement.type.COMBO_BOX) + 1),
			iconImgPath : _config.impPath
		})
	}
	var txNMVal;
	var txLabel;
	if (conf.type == Jedox.wss.wsel.type.COMBO_BOX) {
		txNMVal = ((conf.name) ? conf.name : formElement.type.COMBO_BOX
				.concat(formElement.countEl(formElement.type.COMBO_BOX) + 1))
	} else {
		if (conf.type == Jedox.wss.wsel.type.BUTTON) {
			txNMVal = ((conf.name) ? conf.name : formElement.type.BUTTON
					.concat(formElement.countEl(formElement.type.BUTTON) + 1));
			txLabel = ((conf.label) ? conf.label : txNMVal)
		} else {
			txNMVal = ((conf.name) ? conf.name
					: formElement.type.CHECK_BOX.concat(formElement
							.countEl(formElement.type.CHECK_BOX) + 1));
			txLabel = ((conf.label) ? conf.label : txNMVal);
			if (!isEdit) {
				cbPreState.unchecked = true
			}
		}
	}
	panTar.setName(txNMVal);
	var txtName = new Ext.form.TextField( {
		value : txNMVal,
		width : 250,
		labelSeparator : "",
		fieldLabel : txtNM + ":",
		allowBlank : false,
		enableKeyEvents : true,
		listeners : {
			keyup : function(thisTf, e) {
				panTar.setName(thisTf.getValue())
			}
		}
	});
	var labelName = new Ext.form.TextField( {
		value : txLabel,
		width : 250,
		labelSeparator : ":",
		fieldLabel : labelNM,
		allowBlank : true,
		enableKeyEvents : false
	});
	var cbPanel = new Ext.Panel( {
		border : false,
		bodyStyle : "background-color:transparent;",
		collapsible : false,
		autoWidth : true,
		autoHeight : true,
		items : [ new Ext.form.Radio( {
			checked : cbPreState.unchecked,
			name : "radioSourceForm",
			boxLabel : "Unchecked".localize(),
			hideLabel : true,
			listeners : {
				check : function(thisRb, isChecked) {
					if (isChecked) {
						cbState = "unchecked"
					}
				}
			}
		}), new Ext.form.Radio( {
			checked : cbPreState.checked,
			name : "radioSourceForm",
			boxLabel : "Checked".localize(),
			hideLabel : true,
			listeners : {
				check : function(thisRb, isChecked) {
					if (isChecked) {
						cbState = "checked"
					}
				}
			}
		}) ]
	});
	var fsSource = new Ext.form.FieldSet( {
		title : "Source".localize(),
		collapsible : false,
		autoWidth : true,
		autoHeight : true,
		items : [ panFnc ]
	});
	var cbSource = new Ext.form.FieldSet( {
		title : "Value".localize(),
		collapsible : false,
		autoWidth : true,
		autoHeight : true,
		items : [ cbPanel ]
	});
	var fsTarget = new Ext.form.FieldSet( {
		title : "Target".localize(),
		collapsible : false,
		autoWidth : true,
		autoHeight : true,
		items : [ panTar ]
	});
	var contentPanel;
	if (conf.type == Jedox.wss.wsel.type.COMBO_BOX) {
		contentPanel = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color:transparent;",
			layout : "form",
			autoWidth : true,
			autoHeight : true,
			style : "padding:2px;",
			labelWidth : _config.mainPanelLabelWidth,
			items : [ txtName, fsSource, fsTarget ]
		})
	} else {
		if (conf.type == Jedox.wss.wsel.type.BUTTON) {
			contentPanel = new Ext.Panel( {
				border : false,
				bodyStyle : "background-color:transparent;",
				layout : "form",
				autoWidth : true,
				autoHeight : true,
				style : "padding:2px;",
				labelWidth : _config.mainPanelLabelWidth,
				items : [ txtName, labelName ]
			})
		} else {
			contentPanel = new Ext.Panel( {
				border : false,
				bodyStyle : "background-color:transparent;",
				layout : "form",
				autoWidth : true,
				autoHeight : true,
				style : "padding:2px;",
				labelWidth : _config.mainPanelLabelWidth,
				items : [ txtName, labelName, cbSource, fsTarget ]
			})
		}
	}
	var tabs = new Ext.TabPanel( {
		region : "center",
		xtype : "tabpanel",
		layoutOnTabChange : true,
		activeTab : 0,
		ctCls : "tb-no-bg",
		autoHeight : true,
		baseCls : "x-plain",
		bodyStyle : "background-color: transparent; padding: 5px;",
		defaults : {
			autoScroll : false
		},
		items : [ {
			id : "control",
			title : "Control".localize(),
			baseCls : "x-plain",
			items : [ contentPanel ]
		} ],
		listeners : {
			tabchange : function(el, e) {
			}
		}
	});
	var win = new Ext.Window(
			{
				title : "Format Control".localize(),
				closable : true,
				autoDestroy : true,
				id : "formatControlDialog",
				cls : "default-format-window",
				plain : true,
				constrain : true,
				modal : true,
				resizable : false,
				animCollapse : false,
				width : 500,
				height : 430,
				items : [ tabs ],
				listeners : {
					close : function() {
						Jedox.wss.general
								.setInputMode(Jedox.wss.app.lastInputModeDlg);
						Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.formatControl)
					}
				},
				buttons : [
						{
							text : "OK".localize(),
							handler : function() {
								var retData = _handleSave();
								if (retData[0]) {
									win.close()
								} else {
									Ext.MessageBox
											.show( {
												title : ((retData[2]._type) && (retData[2]._type == "info")) ? "Information"
														.localize()
														: "Error".localize(),
												msg : retData[1]
														.localize(retData[2]),
												buttons : Ext.Msg.OK,
												icon : ((retData[2]._type) && (retData[2]._type == "info")) ? Ext.MessageBox.INFO
														: Ext.MessageBox.ERROR
											})
								}
							}
						}, {
							text : "Cancel".localize(),
							handler : function() {
								win.close()
							}
						} ]
			});
	win.show(this);
	panTar.setWindowToHide(win);
	var _handleSave = function() {
		if (panFnc.getFuncText() == "") {
			return [ false, "WSS_FormComboBox_empty_source", {
				_type : "info"
			} ]
		}
		if (conf.type != Jedox.wss.wsel.type.BUTTON) {
			var tarData = panTar.getRangeValues();
			if ((tarData.range == "") && (tarData.name == "")
					&& (tarData.variable == "")) {
				return [ false, "WSS_FormComboBox_empty_target", {
					_type : "info"
				} ]
			}
		}
		if (conf.type == Jedox.wss.wsel.type.COMBO_BOX) {
			var fType = Jedox.wss.wsel.type.COMBO_BOX;
			conf = {
				type : Jedox.wss.wsel.type.COMBO_BOX,
				name : txtName.getValue(),
				src : panFnc.getFuncText(),
				formulaEnabled : panFnc.getTypeDisabled(),
				trange : tarData.range,
				tnamedrange : tarData.name,
				tvar : tarData.variable
			}
		} else {
			if (conf.type == Jedox.wss.wsel.type.BUTTON) {
				var fType = Jedox.wss.wsel.type.BUTTON;
				conf = {
					type : Jedox.wss.wsel.type.BUTTON,
					name : txtName.getValue(),
					label : labelName.getValue()
				}
			} else {
				var fType = Jedox.wss.wsel.type.CHECK_BOX;
				conf = {
					type : Jedox.wss.wsel.type.CHECK_BOX,
					name : txtName.getValue(),
					label : labelName.getValue(),
					state : cbState,
					trange : tarData.range,
					tnamedrange : tarData.name,
					tvar : tarData.variable
				}
			}
		}
		if (isEdit) {
			conf.id = _initialConf.id;
			return Jedox.wss.wsel.update(conf)
		} else {
			return Jedox.wss.wsel.add(conf)
		}
	}
};