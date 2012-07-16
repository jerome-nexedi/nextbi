Jedox.wss.dialog.chart.MicroChart = function(inIsEditT) {
	var phpMCSHandler = {
		getStateFromFunc : function(result) {
			for ( var prop in result) {
				state[prop] = result[prop]
			}
			_setValuesFromState();
			Ext.MessageBox.hide()
		}
	};
	var phpMicroChartStreamer = new MicroChartStreamer(phpMCSHandler);
	var _config = {
		imgPath : "res/img/dialog/",
		winW : 500,
		winH : 370,
		winWhiskerH : 230,
		winPieH : 200,
		colorButtonW : "100px",
		labelWidth : 60,
		textBoxW : 180,
		comboBoxW : 200,
		rowH : 28,
		margingSize : 2,
		posColor : "#000000",
		negColor : "#000000",
		firstColor : "#000000",
		lastColor : "#000000",
		minColor : "#000000",
		maxColor : "#000000",
		winColor : "#000000",
		loseColor : "#000000",
		tieColor : "#000000",
		pieColor : "#000000"
	};
	var _getMChartScaling = function() {
		return [ [ "0..max".localize() ], [ "min..max".localize() ],
				[ "user defined".localize() ] ]
	};
	var that = this;
	var win;
	var btnColorPos, btnColorNeg, btnColorFirst, btnColorLast, btnColorMin, btnColorMax, btnSoruceSelect, btnTargetSelect, btnScaleMinSelect, btnScaleMaxSelect, btnColorWin, btnColorTie, btnColorLose, btnColorPie;
	var chkFirst, chkLast, chkMinMax, cmbScaling, txtSource, txtTarget, txtMin, txtMax;
	var state, isEdit;
	var navigation = [ [ "Bar".localize(), "mchart-bar" ],
			[ "Line".localize(), "mchart-line" ],
			[ "Dots".localize(), "mchart-dots" ],
			[ "Doted Line".localize(), "mchart-dotted" ],
			[ "Whisker".localize(), "mchart-whisker" ],
			[ "Pie".localize(), "mchart-pie" ] ];
	var navigationStore = new Ext.data.SimpleStore( {
		fields : [ "description", "image" ],
		data : navigation
	});
	var TypeRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var ScalingRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var storeScaling = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	this.init = function(inIsEdit) {
		Ext.QuickTips.init();
		isEdit = inIsEdit;
		storeScaling.loadData(_getMChartScaling());
		state = {
			type : navigation[0][0],
			type_index : 0,
			scaling : storeScaling.getAt(0).get("name"),
			source : "",
			target : "",
			scaleMin : "",
			scaleMax : "",
			colorPos : _config.posColor,
			colorNeg : _config.negColor,
			colorFirst : _config.firstColor,
			colorLast : _config.lastColor,
			colorMin : _config.minColor,
			colorMax : _config.maxColor,
			colorWin : _config.winColor,
			colorLose : _config.loseColor,
			colorTie : _config.tieColor,
			colorPie : _config.pieColor,
			showFirst : false,
			showLast : false,
			showMinMax : false
		};
		chkFirst = new Ext.form.Checkbox( {
			checked : state.showFirst,
			hideLabel : true,
			id : "chkFirst"
		});
		chkLast = new Ext.form.Checkbox( {
			checked : state.showFirst,
			hideLabel : true,
			id : "chkLast"
		});
		chkMinMax = new Ext.form.Checkbox( {
			checked : state.showFirst,
			hideLabel : true,
			id : "chkMinMax"
		});
		btnColorPos = new Ext.Toolbar.SplitButton(
				{
					id : "posButton",
					style : "margin-left:auto;margin-right:auto;",
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorPos = "#" + colorStr;
										_setupColorButton(btnColorPos,
												state.colorPos, false)
									},
									beforeshow : function(thisMenu) {
									}
								}
							})
				});
		btnColorNeg = new Ext.Toolbar.SplitButton(
				{
					id : "negButton",
					style : "margin-left:auto;margin-right:auto;",
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorNeg = "#" + colorStr;
										_setupColorButton(btnColorNeg,
												state.colorNeg, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette.select(state.colorNeg)
									}
								}
							})
				});
		btnColorFirst = new Ext.Toolbar.SplitButton(
				{
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorFirst = "#" + colorStr;
										_setupColorButton(btnColorFirst,
												state.colorFirst, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette
												.select(state.colorFirst)
									}
								}
							})
				});
		btnColorLast = new Ext.Toolbar.SplitButton(
				{
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorLast = "#" + colorStr;
										_setupColorButton(btnColorLast,
												state.colorLast, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette
												.select(state.colorLast)
									}
								}
							})
				});
		btnColorMin = new Ext.Toolbar.SplitButton(
				{
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorMin = "#" + colorStr;
										_setupColorButton(btnColorMin,
												state.colorMin, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette.select(state.colorMin)
									}
								}
							})
				});
		btnColorMax = new Ext.Toolbar.SplitButton(
				{
					minWidth : 50,
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorMax = "#" + colorStr;
										_setupColorButton(btnColorMax,
												state.colorMax, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette.select(state.colorMax)
									}
								}
							})
				});
		btnColorWin = new Ext.Toolbar.SplitButton(
				{
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorWin = "#" + colorStr;
										_setupColorButton(btnColorWin,
												state.colorWin, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette.select(state.colorWin)
									}
								}
							})
				});
		btnColorLose = new Ext.Toolbar.SplitButton(
				{
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorLose = "#" + colorStr;
										_setupColorButton(btnColorLose,
												state.colorLose, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette
												.select(state.colorLose)
									}
								}
							})
				});
		btnColorTie = new Ext.Toolbar.SplitButton(
				{
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorTie = "#" + colorStr;
										_setupColorButton(btnColorTie,
												state.colorTie, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette.select(state.colorTie)
									}
								}
							})
				});
		btnColorPie = new Ext.Toolbar.SplitButton(
				{
					id : "btnColorPie",
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										state.colorPie = "#" + colorStr;
										_setupColorButton(btnColorPie,
												state.colorPie, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette.select(state.colorPie)
									}
								}
							})
				});
		btnSoruceSelect = new Ext.Toolbar.Button( {
			iconCls : "select-range-icon",
			cls : "x-btn-icon",
			tooltip : "Select Range".localize(),
			listeners : {
				click : function() {
					win.hide();
					Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange,
							[ {
								fnc : [ this, function(tmpStr) {
									txtSource.setValue(tmpStr);
									win.show()
								} ],
								format : "{Range}",
								rng : txtSource.getValue()
							} ])
				}
			}
		});
		btnTargetSelect = new Ext.Toolbar.Button( {
			disabled : true,
			iconCls : "select-range-icon",
			cls : "x-btn-icon",
			tooltip : "Select Cell".localize(),
			listeners : {
				click : function() {
					win.hide();
					Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange,
							[ {
								fnc : [ this, function(tmpStr) {
									txtTarget.setValue(tmpStr);
									win.show()
								} ],
								singleCell : true,
								format : "{Range}",
								rng : txtTarget.getValue()
							} ])
				}
			}
		});
		btnScaleMinSelect = new Ext.Toolbar.Button( {
			disabled : true,
			iconCls : "select-range-icon",
			cls : "x-btn-icon",
			tooltip : "Select Cell".localize(),
			listeners : {
				click : function() {
					win.hide();
					Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange,
							[ {
								fnc : [ this, function(tmpStr) {
									txtMin.setValue(tmpStr);
									win.show()
								} ],
								singleCell : true,
								format : "{Range}",
								rng : txtMin.getValue()
							} ])
				}
			}
		});
		btnScaleMaxSelect = new Ext.Toolbar.Button( {
			disabled : true,
			iconCls : "select-range-icon",
			cls : "x-btn-icon",
			tooltip : "Select Cell".localize(),
			listeners : {
				click : function() {
					win.hide();
					Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange,
							[ {
								fnc : [ this, function(tmpStr) {
									txtMax.setValue(tmpStr);
									win.show()
								} ],
								singleCell : true,
								format : "{Range}",
								rng : txtMax.getValue()
							} ])
				}
			}
		});
		cmbScaling = new Ext.form.ComboBox( {
			store : storeScaling,
			id : "cmbScaling",
			width : 155,
			bodyStyle : "background-color:transparent;",
			typeAhead : true,
			selectOnFocus : true,
			fieldLabel : "Scaling".localize(),
			editable : false,
			forceSelection : true,
			triggerAction : "all",
			mode : "local",
			valueField : "name",
			displayField : "name",
			listeners : {
				select : _handleScaleChange
			}
		});
		cmbScaling.setValue(state.scaling);
		txtSource = new Ext.form.TextField( {
			value : state.source,
			fieldLabel : "Source".localize(),
			autoWidth : true,
			id : "txtSource"
		});
		txtTarget = new Ext.form.TextField( {
			disabled : true,
			value : state.target,
			fieldLabel : "Target".localize(),
			autoWidth : true,
			id : "txtTarget"
		});
		txtMin = new Ext.form.TextField( {
			disabled : true,
			value : state.scaleMin,
			fieldLabel : "Min".localize(),
			id : "txtMin"
		});
		txtMax = new Ext.form.TextField( {
			disabled : true,
			value : state.scaleMax,
			fieldLabel : "Max".localize(),
			id : "txtMax"
		});
		var primaryColorPanel = new Ext.Panel( {
			baseCls : "x-plain",
			id : "primaryColorPanel",
			border : false,
			layout : "form",
			items : [ new Ext.Panel( {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				bodyStyle : "margin-bottom: 5px;",
				items : [ {
					html : "Pos. Values".localize(),
					baseCls : "x-plain",
					columnWidth : 0.3
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.2,
					items : btnColorPos
				}, {
					html : "Neg. Values".localize(),
					baseCls : "x-plain",
					columnWidth : 0.3
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.2,
					items : btnColorNeg
				} ]
			}), {
				baseCls : "x-plain",
				bodyStyle : "margin-top: 5px;",
				html : '<div style="border-top: 1px solid #B5B8C8;"></div>'
			}, {
				html : "Values".localize(),
				baseCls : "x-plain",
				bodyStyle : "text-align: right; margin-top: 5px;"
			}, new Ext.Panel( {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				items : [ {
					html : "First".localize(),
					baseCls : "x-plain",
					columnWidth : 0.35
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.55,
					items : btnColorFirst
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.1,
					items : chkFirst
				} ]
			}), new Ext.Panel( {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				items : [ {
					html : "Last".localize(),
					baseCls : "x-plain",
					columnWidth : 0.35
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.55,
					items : btnColorLast
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.1,
					items : chkLast
				} ]
			}), new Ext.Panel( {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				items : [ {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.9,
					items : [ new Ext.Panel( {
						layout : "column",
						border : false,
						baseCls : "x-plain",
						items : [ {
							html : "Min".localize(),
							baseCls : "x-plain",
							columnWidth : 0.39
						}, {
							layout : "form",
							border : false,
							baseCls : "x-plain",
							columnWidth : 0.61,
							items : btnColorMin
						} ]
					}), new Ext.Panel( {
						layout : "column",
						border : false,
						baseCls : "x-plain",
						bodyStyle : "padding-top: 7px;",
						items : [ {
							html : "Max".localize(),
							baseCls : "x-plain",
							columnWidth : 0.39
						}, {
							layout : "form",
							border : false,
							baseCls : "x-plain",
							columnWidth : 0.61,
							items : btnColorMax
						} ]
					}) ]
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					bodyStyle : "padding-top: 14px;",
					columnWidth : 0.1,
					items : chkMinMax
				} ]
			}) ]
		});
		var secondaryColorPanel = new Ext.Panel( {
			baseCls : "x-plain",
			border : false,
			id : "secondaryColorPanel",
			layout : "form",
			items : [ new Ext.Panel( {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				items : [ {
					html : "Win".localize(),
					baseCls : "x-plain",
					columnWidth : 0.35
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.65,
					items : btnColorWin
				} ]
			}), new Ext.Panel( {
				layout : "column",
				border : false,
				bodyStyle : "padding-top: 7px;",
				baseCls : "x-plain",
				items : [ {
					html : "Tie".localize(),
					baseCls : "x-plain",
					columnWidth : 0.35
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.65,
					items : btnColorTie
				} ]
			}), new Ext.Panel( {
				layout : "column",
				border : false,
				bodyStyle : "padding-top: 7px;",
				baseCls : "x-plain",
				items : [ {
					html : "Lose".localize(),
					baseCls : "x-plain",
					columnWidth : 0.35
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					columnWidth : 0.65,
					items : btnColorLose
				} ]
			}) ]
		});
		var scalingPanel = new Ext.Panel( {
			border : "false",
			baseCls : "x-plain",
			id : "scalingPanel",
			layout : "form",
			items : [ cmbScaling, new Ext.Panel( {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				items : [ {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					items : txtMin
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					items : btnScaleMinSelect
				} ]
			}), new Ext.Panel( {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				items : [ {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					items : txtMax
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					items : btnScaleMaxSelect
				} ]
			}) ]
		});
		var mainPanel = new Ext.Panel( {
			layout : "form",
			bodyStyle : "padding-left: 10px; background-color:transparent;",
			border : false,
			items : [ new Ext.Panel( {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				items : [ {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					items : txtSource
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					items : btnSoruceSelect
				} ]
			}), new Ext.Panel( {
				layout : "column",
				border : false,
				baseCls : "x-plain",
				items : [ {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					items : txtTarget
				}, {
					layout : "form",
					border : false,
					baseCls : "x-plain",
					items : btnTargetSelect
				} ]
			}), new Ext.Panel( {
				layout : "form",
				xtype : "fieldset",
				autoHeight : true,
				baseCls : "x-plain",
				border : false,
				frame : false,
				items : [ {
					border : true,
					autoHeight : true,
					xtype : "fieldset",
					layout : "card",
					id : "generalColorCt",
					frame : false,
					title : "Color".localize(),
					activeItem : 0,
					layoutOnCardChange : true,
					items : [ primaryColorPanel, secondaryColorPanel, {
						width : 100,
						baseCls : "x-plain",
						items : btnColorPie
					} ]
				} ]
			}), scalingPanel ]
		});
		mainPanel.doLayout();
		var navigationView = new Ext.DataView(
				{
					id : "hyperlink-navigation",
					store : navigationStore,
					tpl : new Ext.XTemplate(
							'<div class="hyperlink-navigation"><tpl for=".">',
							'<div class="thumb-wrap">',
							'<div class="thumb" style="padding: 5px; text-align: left; font-size: 11px;"><img class="{image}" src="../lib/ext/resources/images/default/s.gif" width="55" height="20"/>',
							'<div style="display:inline; position: absolute; left: 100px;">{description}</div></div></div></tpl>',
							"</div>"),
					autoHeight : true,
					multiSelect : false,
					singleSelect : true,
					overClass : "x-view-over",
					itemSelector : "div.thumb-wrap",
					emptyText : "No images to display".localize(),
					listeners : {
						click : function(cmp, index, node, e) {
							var gcct = Ext.getCmp("generalColorCt").layout;
							state.type_index = index;
							state.type = navigation[index][0];
							if (index >= 0 && index <= 3) {
								gcct.setActiveItem(0);
								scalingPanel.show()
							} else {
								if (index == 4) {
									gcct.setActiveItem(1);
									scalingPanel.hide()
								} else {
									if (index == 5) {
										gcct.setActiveItem(2);
										scalingPanel.hide()
									}
								}
							}
							setTimeout(function() {
								_resizeAll();
								mainPanel.doLayout()
							}, 10)
						}
					}
				});
		win = new Ext.Window(
				{
					hidden : true,
					layout : "fit",
					title : "Micro Chart".localize(),
					width : 500,
					height : 440,
					bodyStyle : "padding: 10px;",
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
									.unload(Jedox.wss.app.dynJSRegistry.microChart)
						}
					},
					items : [ new Ext.Panel( {
						id : "microChartWinCtPanel",
						layout : "column",
						baseCls : "x-plain",
						border : false,
						frame : false,
						items : [ {
							columnWidth : 0.35,
							layout : "form",
							xtype : "fieldset",
							autoHeight : true,
							baseCls : "x-plain",
							border : false,
							frame : false,
							items : [ {
								border : true,
								autoHeight : true,
								xtype : "fieldset",
								layout : "form",
								frame : false,
								title : "Micro Chart Type".localize(),
								items : [ navigationView ]
							} ]
						}, {
							columnWidth : 0.65,
							layout : "form",
							xtype : "fieldset",
							autoHeight : true,
							autoScroll : false,
							border : false,
							frame : false,
							items : [ mainPanel ]
						} ]
					}) ],
					buttons : [
							{
								text : "OK".localize(),
								listeners : {
									click : function() {
										var tmpVal;
										state.source = txtSource.getValue();
										state.target = txtTarget.getValue();
										state.target_cell = [ 1, 1 ];
										state.scaling = cmbScaling.getValue();
										state.scaling_index = storeScaling
												.find("name", cmbScaling
														.getValue());
										state.scaleMin = txtMin.getValue();
										state.scaleMax = txtMax.getValue();
										state.showFirst = chkFirst.getValue();
										state.showLast = chkLast.getValue();
										state.showMinMax = chkMinMax.getValue();
										var activeBook = Jedox.wss.app.activeBook;
										var upperLeftCoords = Jedox.wss.app.environment.defaultSelection
												.getActiveRange()
												.getUpperLeft();
										state.upCellX = upperLeftCoords.getX();
										state.upCellY = upperLeftCoords.getY();
										activeBook.cb("mc_getFuncFromState",
												[ state ]);
										win.close()
									}
								}
							}, {
								text : "Cancel".localize(),
								listeners : {
									click : function() {
										win.close()
									}
								}
							} ]
				})
	};
	this.show = function() {
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
		win.show();
		var upperLeftCoords = Jedox.wss.app.environment.defaultSelection
				.getActiveRange().getUpperLeft();
		var tmpVal = Jedox.wss.app.activeBook.getCellFormula(upperLeftCoords
				.getX(), upperLeftCoords.getY());
		if (isEdit && tmpVal) {
			phpMicroChartStreamer.getStateFromFunc(tmpVal,
					Jedox.wss.i18n.separators[2]);
			Ext.MessageBox.show( {
				title : "Please wait".localize(),
				id : "obtDataMsg",
				msg : "<b><br>" + "Obtaining data!".localize() + "</b>",
				closable : false,
				icon : "largeLoadingImage"
			})
		}
		Ext.getCmp("hyperlink-navigation").select(0)
	};
	var _setupColorButton = function(colorBtn, color, setWidth) {
		var tmpElem = Ext.DomQuery
				.selectNode("*[id=" + colorBtn.btnEl.id + "]");
		if (color && color != "") {
			tmpElem.style.background = color
		} else {
			tmpElem.style.background = "#000000"
		}
		tmpElem.style.width = (colorBtn.id == "posButton" || colorBtn.id == "negButton") ? "20px"
				: "100px";
		tmpElem.style.height = "15px"
	};
	function _changeLayoutByType(index) {
		Ext.getCmp("hyperlink-navigation").fireEvent("click", this, index);
		Ext.getCmp("hyperlink-navigation").select(index)
	}
	var _handleScaleChange = function(combo, record, index) {
		if (index == 2) {
			Ext.getCmp("txtMin").enable();
			Ext.getCmp("txtMax").enable();
			btnScaleMinSelect.enable();
			btnScaleMaxSelect.enable()
		} else {
			Ext.getCmp("txtMin").disable();
			Ext.getCmp("txtMax").disable();
			btnScaleMinSelect.disable();
			btnScaleMaxSelect.disable()
		}
	};
	var _setValuesFromState = function() {
		if (state) {
			_changeLayoutByType(state.type_index);
			Ext.getCmp("txtSource").setValue(state.source);
			Ext.getCmp("txtTarget").setValue(state.target);
			if (state.type_index <= 3) {
				Ext.getCmp("cmbScaling").setValue(
						storeScaling.getAt(state.scaling_index).get("name"));
				_handleScaleChange(storeScaling, storeScaling
						.getAt(state.scaling_index), state.scaling_index);
				Ext.getCmp("txtMin").setValue(state.scaleMin);
				Ext.getCmp("txtMax").setValue(state.scaleMax);
				_setupColorButton(btnColorPos, state.colorPos);
				_setupColorButton(btnColorNeg, state.colorNeg);
				_setupColorButton(btnColorFirst, state.colorFirst);
				_setupColorButton(btnColorLast, state.colorLast);
				_setupColorButton(btnColorMin, state.colorMin);
				_setupColorButton(btnColorMax, state.colorMax);
				Ext.getCmp("chkFirst").setValue(state.showFirst);
				Ext.getCmp("chkLast").setValue(state.showLast);
				Ext.getCmp("chkMinMax").setValue(state.showMinMax)
			} else {
				if (state.type_index == 4) {
					_setupColorButton(btnColorWin, state.colorWin);
					_setupColorButton(btnColorTie, state.colorTie);
					_setupColorButton(btnColorLose, state.colorLose)
				} else {
					if (state.type_index == 5) {
						_setupColorButton(btnColorPie, state.colorPie)
					}
				}
			}
		}
	};
	var _resizeAll = function() {
		setTimeout(function() {
			if (state.type_index == 5) {
				_setupColorButton(btnColorPie, state.colorPie, true)
			} else {
				if (state.type_index == 4) {
					_setupColorButton(btnColorWin, state.colorWin, true);
					_setupColorButton(btnColorTie, state.colorTie, true);
					_setupColorButton(btnColorLose, state.colorLose, true)
				} else {
					_setupColorButton(btnColorPos, state.colorPos, true);
					_setupColorButton(btnColorNeg, state.colorNeg, true);
					_setupColorButton(btnColorFirst, state.colorFirst, true);
					_setupColorButton(btnColorLast, state.colorLast, true);
					_setupColorButton(btnColorMin, state.colorMin, true);
					_setupColorButton(btnColorMax, state.colorMax, true)
				}
			}
		}, 1)
	};
	this.init(inIsEditT);
	this.show()
};