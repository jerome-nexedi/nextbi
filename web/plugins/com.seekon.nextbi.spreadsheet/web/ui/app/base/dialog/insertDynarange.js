Jedox.wss.dialog.openDynarangeEditor = function(conf) {
	var insertHb = new Jedox.wss.dialog.Dynarange();
	insertHb.init(conf);
	insertHb.show()
};
Jedox.wss.dialog.Dynarange = function() {
	var _config = {
		imgPath : "res/img/dialog/",
		winW : 580,
		rbWidth : 100,
		btmFsHeight : 180,
		sampleFieldSetH : 130,
		samplePanelW : 70,
		samplePanelH : 50,
		mainLabelW : 60,
		txtNameFieldW : 200,
		borderColorButtonW : 80,
		displayLabelW : 180,
		displayFieldW : 70,
		columnWidth : 12.57,
		borderColor : "#000000"
	};
	var SoruceForm = Ext
			.extend(
					Ext.Panel,
					{
						border : false,
						bodyStyle : "background-color: transparent;",
						autoWidth : true,
						autoHeight : true,
						rbWidth : 100,
						labelWidth : 75,
						textWidth : 250,
						comboLabel : "Subset".localize(),
						textFieldLabel : "Formula".localize(),
						typeDisabled : false,
						functionValue : "",
						genData : null,
						previewValue : "",
						dynarangeConf : null,
						getFuncText : function() {
							return this._txtFunc.getValue()
						},
						getTypeDisabled : function() {
							return this.typeDisabled
						},
						getGenData : function() {
							return (this.typeDisabled) ? [ [ -1, "", "FN" ] ]
									: this.genData
						},
						getPreviewValue : function() {
							return (this.typeDisabled) ? "" : this.previewValue
						},
						initComponent : function() {
							that = this;
							Ext.apply(this, {
								layout : "form"
							});
							SoruceForm.superclass.initComponent.call(this);
							var setFuncText = function(result) {
								that._txtFunc.setValue(result.ss_func);
								that.genData = result._gendata;
								that.previewValue = result._preview_val
							};
							this._rbTypes = new Ext.form.Radio( {
								checked : !this.typeDisabled,
								name : "radioSourceForm",
								boxLabel : this.comboLabel,
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
							this._btnWizard = new Ext.Button(
									{
										disabled : this.typeDisabled,
										ctCls : "stdButtonsSmall",
										text : "...",
										listeners : {
											click : function() {
												Jedox.wss.app
														.load(
																Jedox.wss.app.dynJSRegistry.subsetEditor,
																[ {
																	mode : 4,
																	fnc : setFuncText,
																	dynarange : ((that.dynarangeConf._gendata[0][0] == -1) ? null
																			: that.dynarangeConf)
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
							this
									.add(new Ext.Panel(
											{
												border : false,
												layout : "column",
												bodyStyle : "background-color:transparent;",
												autoWidth : true,
												autoHeight : true,
												items : [
														new Ext.Panel(
																{
																	labelWidth : this.labelWidth,
																	border : false,
																	layout : "form",
																	bodyStyle : "background-color:transparent;",
																	width : this.rbWidth,
																	autoHeight : true,
																	items : [ this._rbFunc ]
																}),
														new Ext.Panel(
																{
																	labelWidth : this.labelWidth,
																	border : false,
																	layout : "form",
																	bodyStyle : "background-color:transparent;",
																	width : this.rbWidth,
																	autoHeight : true,
																	items : [ this._rbTypes ]
																}),
														{
															border : false,
															bodyStyle : "background-color:transparent;",
															html : "&nbsp;&nbsp;&nbsp;"
														},
														new Ext.Panel(
																{
																	border : false,
																	layout : "form",
																	bodyStyle : "background-color:transparent;",
																	autoWidth : true,
																	autoHeight : true,
																	items : [ this._btnWizard ]
																}) ]
											}));
							this.add(this._txtFunc)
						}
					});
	var that = this;
	var win;
	var panelMain, panelDrillDownLevel, panelColWidth, panelcmbDrillDownLevel, panelSample, panelSrc, fsDisplay, fsBorder, fsSample, fsDirection, fsSource;
	var lblBorderColor, txtHbName, txtFixedColWidth, chbShowAlias, cmbDrillDownLevel, cmbBorderType, cmbBorderWidth, chbDrillDownLevel, chbIndentText, chbColWidth, btnBorderColor, btnOk, btnClose, rbRight, rbDown, rbFixedWidth, rbAutoWidth;
	var hbConf, borderColor;
	var DrillDownLevelRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var storeDrillDownLevel = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	var storeBorderType = new Ext.data.SimpleStore( {
		fields : [ {
			name : "id"
		}, {
			name : "name"
		}, {
			name : "style"
		} ]
	});
	var storeBorderWidth = new Ext.data.SimpleStore( {
		fields : [ {
			name : "id"
		}, {
			name : "name"
		}, {
			name : "width"
		} ]
	});
	var _getDrillDownLevelList = function() {
		return [ [ "1" ], [ "2" ], [ "3" ], [ "4" ], [ "5" ], [ "6" ], [ "7" ],
				[ "8" ], [ "9" ], [ "10" ] ]
	};
	var _getBorderTypeList = function() {
		return [ [ 0, "None".localize(), "none" ],
				[ 1, "Solid".localize(), "solid" ],
				[ 2, "Dotted".localize(), "dotted" ],
				[ 3, "Dashed".localize(), "dashed" ] ]
	};
	var _getBorderWidthList = function() {
		return [ [ 0, "1", "1" ], [ 1, "2", "2" ] ]
	};
	this.init = function(inHbConf) {
		var initHbConf = {
			dir : 0,
			drill : true,
			level : 2,
			border : "1px solid #000000",
			indent : false,
			name : "_name: UnnamedHb".localize()
		};
		hbConf = {};
		if (inHbConf) {
			hbConf = inHbConf;
			if (!inHbConf.cwidth) {
				delete initHbConf.cwidth
			}
		}
		for ( var prop in initHbConf) {
			if (!hbConf[prop]) {
				hbConf[prop] = initHbConf[prop]
			}
		}
		borderColor = _getBorderInfo(hbConf.border)[2];
		storeDrillDownLevel.loadData(_getDrillDownLevelList());
		storeBorderType.loadData(_getBorderTypeList());
		storeBorderWidth.loadData(_getBorderWidthList());
		lblBorderColor = new Ext.form.MiscField( {
			value : "Color".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		txtHbName = new Ext.form.TextField( {
			disabled : true,
			value : hbConf.id,
			width : _config.txtNameFieldW,
			fieldLabel : "Name".localize()
		});
		txtFixedColWidth = new Ext.form.TextField( {
			disabled : !(hbConf.cwidth)
					|| (!!(hbConf.cwidth) && (hbConf.cwidth == "auto")),
			value : ((hbConf.cwidth && hbConf.cwidth != "auto") ? hbConf.cwidth
					: _config.columnWidth),
			hideLabel : true,
			width : _config.displayFieldW + 5
		});
		btnBorderColor = new Ext.Toolbar.SplitButton(
				{
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										borderColor = "#" + colorStr;
										var tmpElem = Ext.DomQuery
												.selectNode("*[id="
														+ btnBorderColor.btnEl.id
														+ "]");
										tmpElem.style.background = borderColor;
										_updateBorderSample()
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette.select(borderColor)
									}
								}
							})
				});
		btnOk = new Ext.Button( {
			text : "OK".localize(),
			listeners : {
				click : function() {
					hbConf.alias = chbShowAlias.getValue();
					hbConf.drill = chbDrillDownLevel.getValue();
					hbConf.level = cmbDrillDownLevel.getValue();
					hbConf.indent = chbIndentText.getValue();
					hbConf.dir = ((rbRight.getValue()) ? 1 : 0);
					hbConf.border = storeBorderWidth.getAt(
							storeBorderWidth.find("id", cmbBorderWidth
									.getValue())).get("width")
							+ "px "
							+ storeBorderType.getAt(
									storeBorderType.find("id", cmbBorderType
											.getValue())).get("style")
							+ " "
							+ borderColor;
					if (chbColWidth.getValue()) {
						hbConf.cwidth = rbAutoWidth.getValue() ? "auto"
								: txtFixedColWidth.getValue()
					} else {
						if (hbConf.cwidth) {
							delete hbConf.cwidth
						}
					}
					hbConf.ss_func = panelSrc.getFuncText();
					hbConf._gendata = panelSrc.getGenData();
					hbConf._preview_val = panelSrc.getPreviewValue();
					Jedox.wss.hb.propDlgCB(hbConf);
					win.close()
				}
			}
		});
		btnClose = new Ext.Button( {
			text : "Close".localize(),
			listeners : {
				click : function() {
					win.close()
				}
			}
		});
		rbDown = new Ext.form.Radio( {
			name : "hbDirection",
			boxLabel : ("Vertical".localize())
					.concat("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"),
			hideLabel : true,
			checked : !(hbConf.dir)
		});
		rbRight = new Ext.form.Radio( {
			name : "hbDirection",
			boxLabel : "Horizontal".localize(),
			hideLabel : true,
			checked : (hbConf.dir)
		});
		rbAutoWidth = new Ext.form.Radio( {
			disabled : !(hbConf.cwidth),
			name : "drColWidth",
			boxLabel : "auto".localize(),
			hideLabel : true,
			checked : !!(hbConf.cwidth) && (hbConf.cwidth == "auto")
		});
		rbFixedWidth = new Ext.form.Radio( {
			disabled : !(hbConf.cwidth),
			name : "drColWidth",
			boxLabel : ("fixed".localize()).concat("&nbsp;&nbsp;&nbsp;"),
			hideLabel : true,
			checked : !(hbConf.cwidth) || (hbConf.cwidth != "auto"),
			listeners : {
				check : function(thisrB, checked) {
					if (checked) {
						txtFixedColWidth.enable()
					} else {
						txtFixedColWidth.disable()
					}
				}
			}
		});
		chbDrillDownLevel = new Ext.form.Checkbox( {
			checked : hbConf.drill,
			width : _config.displayLabelW,
			hideLabel : true,
			boxLabel : "Drill down, begin at level".localize()
					+ ":&nbsp;&nbsp;&nbsp;",
			listeners : {
				check : function() {
					if (chbDrillDownLevel.getValue()) {
						cmbDrillDownLevel.enable()
					} else {
						cmbDrillDownLevel.disable()
					}
				}
			}
		});
		chbIndentText = new Ext.form.Checkbox( {
			checked : hbConf.indent,
			hideLabel : true,
			boxLabel : "Indent Text".localize()
		});
		chbColWidth = new Ext.form.Checkbox( {
			checked : !!(hbConf.cwidth),
			width : _config.displayLabelW,
			hideLabel : true,
			boxLabel : "Set column width".localize() + ":",
			listeners : {
				check : function(thisChb, checked) {
					if (checked) {
						rbAutoWidth.enable();
						rbFixedWidth.enable();
						if (rbFixedWidth.getValue()) {
							txtFixedColWidth.enable()
						}
					} else {
						rbAutoWidth.disable();
						rbFixedWidth.disable();
						txtFixedColWidth.disable()
					}
				}
			}
		});
		cmbBorderType = new Ext.form.ComboBox(
				{
					store : storeBorderType,
					width : 100,
					bodyStyle : "background-color:transparent;padding-left:30px;",
					hideLabel : true,
					typeAhead : true,
					selectOnFocus : true,
					editable : false,
					forceSelection : true,
					triggerAction : "all",
					mode : "local",
					valueField : "id",
					displayField : "name",
					tpl : new Ext.XTemplate(
							'<tpl for=".">',
							'<div class="x-combo-list-item" style="cursor:pointer;line-height:18px;">',
							'<div style="margin-top:8px; border-top: 1px {style} rgb(0, 0, 0); width:20px; float:left;"></div>',
							'<div style="width:69%; margin-left:30%; text-align:left;">{name}</div>',
							"</div>", "</tpl>"),
					listeners : {
						select : function(thisCombo, record, index) {
							Ext.get(cmbBorderType.id + "_styleDiv").setStyle(
									"border-top",
									"1px " + record.get("style")
											+ " rgb(0, 0, 0)");
							_updateBorderSample()
						},
						render : function() {
							this.fieldDiv = Ext.DomHelper
									.insertFirst(
											this.el.up("div.x-form-field-wrap"),
											{
												tag : "div",
												id : cmbBorderType.id
														+ "_inputFieldDiv",
												style : "position:absolute;background-color:transparent;height:18px"
											});
							var tmpBorderStyle = _getBorderInfo(hbConf.border)[1];
							this.styleDiv = Ext.DomHelper
									.insertFirst(
											this.fieldDiv,
											{
												tag : "div",
												id : cmbBorderType.id
														+ "_styleDiv",
												style : "margin-top:10px;border-top:1px "
														+ tmpBorderStyle
														+ " rgb(0, 0, 0);width:20px;margin-left:4px;"
											});
							Ext.get(cmbBorderType.getItemId()).setStyle(
									"padding-left", "30px")
						}
					}
				});
		cmbBorderWidth = new Ext.form.ComboBox(
				{
					store : storeBorderWidth,
					width : 100,
					bodyStyle : "background-color:transparent;padding-left:30px;",
					hideLabel : true,
					typeAhead : true,
					selectOnFocus : true,
					editable : false,
					forceSelection : true,
					triggerAction : "all",
					mode : "local",
					valueField : "id",
					displayField : "name",
					tpl : new Ext.XTemplate(
							'<tpl for=".">',
							'<div class="x-combo-list-item" style="cursor:pointer;line-height:18px;">',
							'<div style="margin-top:8px; border-top: {width}px solid rgb(0, 0, 0); width:20px; float:left;"></div>',
							'<div style="width:69%; margin-left:30%; text-align:left;">{name}</div>',
							"</div>", "</tpl>"),
					listeners : {
						select : function(thisCombo, record, index) {
							Ext.get(cmbBorderWidth.id + "_styleDiv").setStyle(
									"border-top",
									record.get("width")
											+ "px solid rgb(0, 0, 0)");
							_updateBorderSample()
						},
						render : function() {
							this.fieldDiv = Ext.DomHelper
									.insertFirst(
											this.el.up("div.x-form-field-wrap"),
											{
												tag : "div",
												id : cmbBorderWidth.id
														+ "_inputFieldDiv",
												style : "position:absolute;background-color:transparent;height:18px"
											});
							var tmpBorderWidth = _getBorderInfo(hbConf.border)[0];
							tmpBorderWidth = tmpBorderWidth.substring(0,
									tmpBorderWidth.length - 2);
							this.styleDiv = Ext.DomHelper
									.insertFirst(
											this.fieldDiv,
											{
												tag : "div",
												id : cmbBorderWidth.id
														+ "_styleDiv",
												style : "margin-top:10px;border-top:"
														+ tmpBorderWidth
														+ "px solid rgb(0, 0, 0);width:20px;margin-left:4px;"
											});
							Ext.get(cmbBorderWidth.getItemId()).setStyle(
									"padding-left", "30px")
						}
					}
				});
		chbShowAlias = new Ext.form.Checkbox( {
			checked : hbConf.alias,
			hideLabel : true,
			boxLabel : "Show Alias".localize()
		});
		cmbDrillDownLevel = new Ext.form.ComboBox( {
			disabled : !(hbConf.drill),
			store : storeDrillDownLevel,
			width : _config.displayFieldW,
			bodyStyle : "background-color:transparent;",
			hideLabel : true,
			typeAhead : true,
			selectOnFocus : true,
			editable : false,
			forceSelection : true,
			triggerAction : "all",
			mode : "local",
			valueField : "name",
			displayField : "name"
		});
		panelcmbDrillDownLevel = new Ext.Panel( {
			border : false,
			layout : "form",
			bodyStyle : "background-color:transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ cmbDrillDownLevel ]
		});
		panelDrillDownLevel = new Ext.Panel( {
			border : false,
			layout : "column",
			bodyStyle : "background-color:transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ new Ext.Panel( {
				border : false,
				layout : "form",
				bodyStyle : "background-color:transparent;",
				autoWidth : true,
				autoHeight : true,
				items : [ chbDrillDownLevel ]
			}), panelcmbDrillDownLevel ]
		});
		panelColWidth = new Ext.Panel( {
			border : false,
			layout : "column",
			bodyStyle : "background-color:transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ {
				html : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
				baseCls : "x-plain"
			}, new Ext.Panel( {
				border : false,
				layout : "column",
				bodyStyle : "background-color:transparent;",
				autoWidth : true,
				autoHeight : true,
				items : [ rbFixedWidth, txtFixedColWidth ]
			}), {
				html : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
				baseCls : "x-plain"
			}, rbAutoWidth ]
		});
		panelSample = new Ext.Panel( {
			border : false,
			style : "margin:auto;margin-top:20px;",
			width : _config.samplePanelW,
			height : _config.samplePanelH
		});
		panelDirection = new Ext.Panel( {
			border : false,
			layout : "column",
			bodyStyle : "background-color:transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ new Ext.Panel( {
				border : false,
				layout : "form",
				bodyStyle : "background-color:transparent;",
				width : _config.rbWidth,
				autoHeight : true,
				items : [ rbDown ]
			}), new Ext.Panel( {
				border : false,
				layout : "form",
				bodyStyle : "background-color:transparent;",
				width : _config.rbWidth,
				autoHeight : true,
				items : [ rbRight ]
			}) ]
		});
		panelSrc = new SoruceForm( {
			genData : hbConf._gendata,
			previewValue : hbConf._preview_val,
			functionValue : hbConf.ss_func,
			dynarangeConf : hbConf,
			typeDisabled : (hbConf._gendata[0][0] == -1),
			rbWidth : _config.rbWidth,
			textWidth : _config.winW - 50
		});
		fsSource = new Ext.form.FieldSet( {
			title : "Source".localize(),
			collapsible : false,
			autoWidth : true,
			autoHeight : true,
			items : [ panelSrc ]
		});
		fsDirection = new Ext.form.FieldSet( {
			autoHeight : true,
			title : "Direction".localize(),
			items : [ panelDirection ]
		});
		fsDisplay = new Ext.form.FieldSet( {
			height : _config.btmFsHeight,
			width : _config.winW / 2 - 18,
			title : "Display".localize(),
			items : [ chbShowAlias, panelDrillDownLevel, chbIndentText,
					chbColWidth, panelColWidth ]
		});
		fsSample = new Ext.form.FieldSet( {
			title : "Sample".localize(),
			height : 130,
			width : _config.winW / 4,
			style : "margin-left:20px;",
			bodyStyle : "text-align:center;",
			items : [ panelSample ]
		});
		fsBorder = new Ext.form.FieldSet( {
			height : _config.btmFsHeight,
			width : _config.winW / 2 - 18,
			layout : "column",
			title : "Border".localize(),
			items : [ new Ext.Panel( {
				border : false,
				layout : "form",
				bodyStyle : "background-color:transparent;",
				autoWidth : true,
				autoHeight : true,
				items : [ {
					html : "Color".localize() + ":",
					baseCls : "x-plain"
				}, btnBorderColor, {
					html : "Type".localize() + ":",
					baseCls : "x-plain"
				}, cmbBorderType, {
					html : "Width".localize() + ":",
					baseCls : "x-plain"
				}, cmbBorderWidth ]
			}), fsSample ]
		});
		panelMain = new Ext.Panel( {
			autoHeight : true,
			labelWidth : _config.mainLabelW,
			layout : "form",
			bodyStyle : "padding: 5px 5px 0px; background-color: transparent;",
			items : [ txtHbName, fsSource, fsDirection, new Ext.Panel( {
				border : false,
				layout : "column",
				bodyStyle : "background-color:transparent;",
				autoWidth : true,
				autoHeight : true,
				items : [ new Ext.Panel( {
					border : false,
					layout : "form",
					bodyStyle : "background-color:transparent;",
					autoWidth : true,
					autoHeight : true,
					items : [ fsDisplay ]
				}), {
					html : "&nbsp;&nbsp;",
					baseCls : "x-plain"
				}, new Ext.Panel( {
					border : false,
					layout : "form",
					bodyStyle : "background-color:transparent;",
					autoWidth : true,
					autoHeight : true,
					items : [ fsBorder ]
				}) ]
			}) ]
		});
		win = new Ext.Window(
				{
					hidden : true,
					layout : "fit",
					title : "_tit: hb Properties".localize(),
					width : _config.winW,
					minWidth : _config.winW,
					autoHeight : true,
					closeAction : "close",
					cls : "default-format-window",
					autoDestroy : true,
					plain : true,
					modal : true,
					resizable : false,
					listeners : {
						activate : _resizeAll,
						close : function() {
							Jedox.wss.general
									.setInputMode(Jedox.wss.app.lastInputModeDlg);
							Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.insertDynarange)
						}
					},
					items : [ panelMain ],
					buttons : [ btnOk, btnClose ]
				});
		cmbDrillDownLevel.setValue(hbConf.level);
		var tmpBorderValues = _getBorderInfo(hbConf.border);
		tmpBorderValues[0] = tmpBorderValues[0].substring(0,
				tmpBorderValues[0].length - 2);
		cmbBorderType.setValue(storeBorderType.getAt(
				storeBorderType.find("style", tmpBorderValues[1])).get("id"));
		cmbBorderWidth.setValue(storeBorderWidth.getAt(
				storeBorderWidth.find("width", tmpBorderValues[0])).get("id"))
	};
	this.show = function() {
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
		win.show();
		_updateBorderSample()
	};
	var _getBorderInfo = function(borderStyle) {
		return borderStyle.split(" ")
	};
	var _updateBorderSample = function() {
		var tmpBorder = storeBorderWidth.getAt(
				storeBorderWidth.find("id", cmbBorderWidth.getValue())).get(
				"width")
				+ "px "
				+ storeBorderType.getAt(
						storeBorderType.find("id", cmbBorderType.getValue()))
						.get("style") + " " + borderColor;
		Ext.get(panelSample.id).setStyle("border", tmpBorder)
	};
	var _resizeAll = function() {
		if (panelMain.rendered) {
			var tmpElem = Ext.DomQuery.selectNode("*[id="
					+ btnBorderColor.btnEl.id + "]");
			tmpElem.style.background = borderColor;
			tmpElem.style.width = _config.borderColorButtonW + "px"
		}
	}
};