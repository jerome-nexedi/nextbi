Jedox.wss.dialog.chart.editChartDialog = function(mode, chartID) {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var _charProps;
	var chartTypesSizeDefs = Jedox.wss.wsel.chart.type_sizeDefs, chartMinSizeFactor = Jedox.wss.wsel.chart.min_sizeFactor, chartsMinWidthHeightRatio = Jedox.wss.wsel.chart.min_whRatio, widthHeightRatio = Jedox.wss.wsel.chart.whRatio;
	function getWHRatio() {
		var selChartType = _chartProps.ChartType.General.Type;
		var whRatio = widthHeightRatio[chartTypesSizeDefs[selChartType]];
		return whRatio
	}
	var _reg = {
		1 : {
			HorizontalAxisScale : true
		},
		2 : {
			Legend : true,
			Title : true,
			Axes : true
		},
		3 : {
			Legend : true,
			HorizontalAxisName : true,
			VericalAxis : true
		},
		4 : {}
	};
	var _chartTypesInterfaceDate = {
		xlColumnClustered : 1,
		xlColumnStacked : 1,
		xlColumnStacked100 : 1,
		xl3DColumnClustered : 1,
		xl3DColumnStacked : 1,
		xl3DColumnStacked100 : 1,
		xlCylinderColClustered : 1,
		xlCylinderColStacked : 1,
		xlCylinderColStacked100 : 1,
		xlLine : 1,
		xlLineStacked : 1,
		xlLineStacked100 : 1,
		xlLineRotated : 5,
		xlLineMarkers : 1,
		xlLineMarkersStacked : 1,
		xlLineMarkersStacked100 : 1,
		xlLineMarkersRotated : 5,
		xl3DLine : 1,
		xlPie : 2,
		xl3DPie : 2,
		xlPieExploded : 2,
		xl3DPieExploded : 2,
		xlBarClustered : 5,
		xlBarStacked : 5,
		xlBarStacked100 : 5,
		xl3DBarClustered : 5,
		xl3DBarStacked : 5,
		xl3DBarStacked100 : 5,
		xlCylinderBarClustered : 5,
		xlCylinderBarStacked : 5,
		xlCylinderBarStacked100 : 5,
		xlArea : 1,
		xlAreaStacked : 1,
		xlAreaStacked100 : 1,
		xl3DArea : 1,
		xl3DAreaStacked : 1,
		xl3DAreaStacked100 : 1,
		xlXYScatter : 4,
		xlXYScatterSmooth : 4,
		xlXYScatterSmoothNoMarkers : 4,
		xlXYScatterLines : 4,
		xlXYScatterLinesNoMarkers : 4,
		xlStockHLC : 4,
		xlStockOHLC : 4,
		xlDoughnut : 2,
		xlDoughnutExploded : 2,
		xlBubble : 4,
		xlBubble3DEffect : 4,
		xlRadar : 4,
		xlRadarMarkers : 4,
		xlRadarFilled : 4,
		xlMeterOdoFull : 3,
		xlMeterOdoFull100 : 3,
		xlMeterOdoHalf : 3,
		xlMeterOdoHalf100 : 3,
		xlMeterAngularWide : 3,
		xlMeterLineHorizontal : 3,
		xlMeterLineVertical : 3
	};
	var Fill = function(type, obj) {
		var typesData = {
			chartArea : {
				fillLbl : "Fill".localize(),
				borderLbl : "Border Color".localize(),
				rbFillGroup : "chart-area-fill",
				rbBorderGroup : "chart-area-border",
				defaultColor : "#FFFFFF"
			},
			plotArea : {
				fillLbl : "Fill".localize(),
				borderLbl : "Border Color".localize(),
				rbFillGroup : "plot-area-fill",
				rbBorderGroup : "plot-area-border",
				defaultColor : "#FFFFFF"
			},
			title : {
				fillLbl : "Fill".localize(),
				borderLbl : "Border Color".localize(),
				rbFillGroup : "title-fill",
				rbBorderGroup : "title-border"
			},
			legend : {
				fillLbl : "Fill".localize(),
				borderLbl : "Border Color".localize(),
				rbFillGroup : "legend-fill",
				rbBorderGroup : "legend-border",
				defaultColor : "#FFFFFF"
			}
		};
		var _fillColor = typesData[type].defaultColor;
		var _obj;
		var _flags = {
			preselect : false,
			setValueCall : false
		};
		var _noFillCONST = "0xFFFFFFFF";
		var fillLbl = new Ext.form.Label( {
			text : typesData[type].fillLbl.concat(":"),
			cls : "edit-chart-title"
		});
		var BRLbl = {
			html : "<br/>",
			baseCls : "x-plain"
		};
		var noFillRb = new Ext.form.Radio( {
			boxLabel : "No fill".localize(),
			hideLabel : true,
			checked : false,
			name : typesData[type].rbFillGroup,
			width : 100,
			handler : function() {
				if (this.getValue()) {
					switchSolid(solidFillColorSwitchPanel, 0)
				}
			},
			inputValue : 1
		});
		var solidFillRb = new Ext.form.Radio( {
			boxLabel : "Solid fill".localize(),
			hideLabel : true,
			checked : false,
			name : typesData[type].rbFillGroup,
			width : 100,
			handler : function() {
				if (this.getValue()) {
					switchSolid(solidFillColorSwitchPanel, 1)
				}
			},
			inputValue : 1
		});
		var automaticFillRb = new Ext.form.Radio( {
			boxLabel : "Automatic".localize(),
			hideLabel : true,
			checked : false,
			name : typesData[type].rbFillGroup,
			width : 100,
			handler : function() {
				if (this.getValue()) {
					switchSolid(solidFillColorSwitchPanel, 0)
				}
			},
			inputValue : 1
		});
		var fillColorLbl = new Ext.form.Label( {
			text : "Color".localize().concat(":")
		});
		var solidFillBtn = new Ext.Toolbar.SplitButton(
				{
					minWidth : 100,
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										_fillColor = "#" + colorStr;
										setColorButton(solidFillBtn,
												_fillColor, false)
									},
									beforeshow : function(thisMenu) {
										thisMenu.palette.select(_fillColor)
									}
								}
							})
				});
		var solidFillColorPanel = new Ext.Panel(
				{
					layout : "column",
					border : false,
					defaults : {
						bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
					},
					baseCls : "x-plain",
					width : 400,
					height : 100,
					items : [ {
						border : false,
						width : 50,
						items : fillColorLbl
					}, {
						border : false,
						width : 200,
						height : 50,
						items : solidFillBtn
					} ]
				});
		var solidFillColorSwitchPanel = new Ext.Panel( {
			layout : "card",
			border : false,
			baseCls : "x-plain",
			activeItem : 1,
			items : [ {
				html : "",
				baseCls : "x-plain"
			}, solidFillColorPanel ]
		});
		var fillPanel = new Ext.Panel( {
			title : "Fill".localize(),
			layout : "form",
			baseCls : "x-plain",
			items : [ BRLbl, fillLbl, BRLbl, noFillRb, solidFillRb,
					automaticFillRb, solidFillColorSwitchPanel ],
			listeners : {
				render : function(panel) {
					setTimeout(function() {
						if (_flags.setValueCall && !_flags.preselect) {
							setFillValue(_obj)
						}
					})
				}
			}
		});
		function switchSolid(panel, index) {
			solidFillColorSwitchPanel.getLayout().setActiveItem(index)
		}
		function setColorButton(colorBtn, color, setWidth) {
			var tmpElem = Ext.DomQuery.selectNode("*[id=" + colorBtn.btnEl.id
					+ "]");
			tmpElem.style.background = color;
			tmpElem.style.width = "80px";
			tmpElem.style.height = "15px"
		}
		function getFillValue() {
			if (fillPanel.rendered) {
				return {
					BackColor : noFillRb.getValue() ? _noFillCONST
							: solidFillRb.getValue() ? _fillColor.replace("#",
									"0x") : typesData[type].defaultColor
									.replace("#", "0x")
				}
			}
			return _obj
		}
		function setFillValue(obj) {
			_flags.setValueCall = true;
			if (fillPanel.rendered) {
				_flags.preselect = true;
				switch (obj.BackColor) {
				case "Transparent":
					noFillRb.setValue(true);
					break;
				case "automatic":
					automaticFillRb.setValue(true);
					break;
				default:
					_fillColor = obj.BackColor.replace("0x", "#");
					solidFillRb.setValue(true);
					setColorButton(solidFillBtn, _fillColor, false);
					solidFillBtn.fireEvent("select", _fillColor, obj.Color);
					break
				}
			} else {
				_obj = obj
			}
		}
		this.getPanel = function() {
			return fillPanel
		};
		this.getValue = function() {
			return getFillValue()
		};
		this.setValue = function(obj) {
			setFillValue(obj)
		}
	};
	var Border = function(type, color) {
		var typesData = {
			chartArea : {
				fillLbl : "Fill".localize(),
				borderLbl : "Border Color".localize(),
				rbFillGroup : "chart-area-fill",
				rbBorderGroup : "chart-area-border",
				defaultColor : "#FFFFFF"
			},
			plotArea : {
				fillLbl : "Fill".localize(),
				borderLbl : "Border Color".localize(),
				rbFillGroup : "plot-area-fill",
				rbBorderGroup : "plot-area-border",
				defaultColor : "#868686"
			},
			title : {
				fillLbl : "Fill".localize(),
				borderLbl : "Border Color".localize(),
				rbFillGroup : "title-fill",
				rbBorderGroup : "title-border",
				defaultColor : "#FFFFFF"
			},
			legend : {
				fillLbl : "Fill".localize(),
				borderLbl : "Border Color".localize(),
				rbFillGroup : "legend-fill",
				rbBorderGroup : "legend-border",
				defaultColor : "#FFFFFF"
			}
		};
		var _borderColor = typesData[type].defaultColor;
		var _obj;
		var _flags = {
			preselect : false,
			setValueCall : false
		};
		var _noBorderCONST = "0xFFFFFFFF";
		var borderColorLbl = new Ext.form.Label( {
			text : "Border Color".localize().concat(":"),
			cls : "edit-chart-title"
		});
		var BRLbl = {
			html : "<br/>",
			baseCls : "x-plain"
		};
		var noLineRb = new Ext.form.Radio( {
			boxLabel : "No line".localize(),
			hideLabel : true,
			checked : false,
			name : typesData[type].rbBorderGroup,
			width : 100,
			handler : function() {
				if (this.getValue()) {
					switchSolid(solidLineColorSwitchPanel, 0)
				}
			},
			inputValue : 1
		});
		var solidLineRb = new Ext.form.Radio( {
			boxLabel : "Solid line".localize(),
			hideLabel : true,
			checked : false,
			name : typesData[type].rbBorderGroup,
			width : 100,
			handler : function() {
				if (this.getValue()) {
					switchSolid(solidLineColorSwitchPanel, 1)
				}
			},
			inputValue : 1
		});
		var automaticLineRb = new Ext.form.Radio( {
			boxLabel : "Automatic".localize(),
			hideLabel : true,
			checked : false,
			name : typesData[type].rbBorderGroup,
			width : 100,
			handler : function() {
				if (this.getValue()) {
					switchSolid(solidLineColorSwitchPanel, 0)
				}
			},
			inputValue : 1
		});
		var lineColorLbl = new Ext.form.Label( {
			text : "Color".localize().concat(":")
		});
		var solidLineBtn = new Ext.Toolbar.SplitButton(
				{
					minWidth : 100,
					menu : new Ext.menu.ColorMenu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								listeners : {
									select : function(colorP, colorStr) {
										_borderColor = "#" + colorStr;
										setColorButton(solidLineBtn,
												_borderColor, false)
									},
									beforeshow : function(thisMenu) {
										if (_borderColor in thisMenu.palette.colors) {
											thisMenu.palette
													.select(_borderColor)
										}
									}
								}
							})
				});
		var solidLineColorPanel = new Ext.Panel(
				{
					layout : "column",
					border : false,
					defaults : {
						bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
					},
					baseCls : "x-plain",
					width : 400,
					height : 100,
					items : [ {
						border : false,
						width : 50,
						items : lineColorLbl
					}, {
						border : false,
						width : 200,
						height : 50,
						items : solidLineBtn
					} ]
				});
		var solidLineColorSwitchPanel = new Ext.Panel( {
			layout : "card",
			border : false,
			baseCls : "x-plain",
			activeItem : 1,
			items : [ {
				html : "",
				baseCls : "x-plain"
			}, solidLineColorPanel ]
		});
		var borderPanel = new Ext.Panel( {
			title : "Border Color".localize(),
			layout : "form",
			baseCls : "x-plain",
			items : [ BRLbl, borderColorLbl, BRLbl, noLineRb, solidLineRb,
					automaticLineRb, solidLineColorSwitchPanel ],
			listeners : {
				render : function(panel) {
					setTimeout(function() {
						if (_flags.setValueCall && !_flags.preselect) {
							setBorderValue(_obj)
						}
					})
				}
			}
		});
		function switchSolid(panel, index) {
			solidLineColorSwitchPanel.getLayout().setActiveItem(index)
		}
		function setColorButton(colorBtn, color, setWidth) {
			var tmpElem = Ext.DomQuery.selectNode("*[id=" + colorBtn.btnEl.id
					+ "]");
			tmpElem.style.background = color;
			tmpElem.style.width = "80px";
			tmpElem.style.height = "15px"
		}
		function getBorderValue() {
			if (borderPanel.rendered) {
				return {
					Color : noLineRb.getValue() ? _noBorderCONST
							: solidLineRb.getValue() ? _borderColor.replace(
									"#", "0x")
									: automaticLineRb.getValue() ? typesData[type].defaultColor
											.replace("#", "0x")
											: false
				}
			}
			return _obj
		}
		function setBorderValue(obj) {
			_flags.setValueCall = true;
			if (borderPanel.rendered) {
				_flags.preselect = true;
				switch (obj.Color) {
				case "Transparent":
					noLineRb.setValue(true);
					break;
				case "automatic":
					automaticLineRb.setValue(true);
					break;
				default:
					_borderColor = obj.Color.replace("0x", "#");
					solidLineRb.setValue(true);
					setColorButton(solidLineBtn, _borderColor, false);
					solidLineBtn.fireEvent("select", _borderColor, obj.Color);
					break
				}
			} else {
				_obj = obj
			}
		}
		this.getPanel = function() {
			return borderPanel
		};
		this.getValue = function() {
			return getBorderValue()
		};
		this.setValue = function(obj) {
			setBorderValue(obj)
		}
	};
	var SeriesOptions = function(pointsFlag) {
		var _pointsFlag = pointsFlag;
		var _paletteData;
		var _seriesData;
		var _renderColor = {
			color : "",
			border : ""
		};
		var _handsColor;
		var seriesOptionsLbl = new Ext.form.Label( {
			text : "Series Options".localize().concat(":"),
			cls : "edit-chart-title"
		});
		var BRLbl = {
			html : "<br/>",
			baseCls : "x-plain"
		};
		var paletteCmb = new Ext.form.ComboBox( {
			fieldLabel : "Palette".localize(),
			store : new Ext.data.SimpleStore( {
				data : [ [ "Custom".localize(), "custom" ],
						[ "Office".localize(), "office" ],
						[ "Apex".localize(), "apex" ],
						[ "Aspect".localize(), "aspect" ] ],
				fields : [ "itemName", "itemValue" ]
			}),
			displayField : "itemName",
			valueField : "itemValue",
			mode : "local",
			triggerAction : "all",
			value : "custom",
			listWidth : 120,
			width : 120,
			editable : false,
			allowBlank : false,
			selectOnFocus : false,
			listeners : {
				select : function() {
					setPalette(this.getValue())
				}
			}
		});
		var seriesStore = new Ext.data.SimpleStore( {
			fields : [ {
				name : "name"
			}, {
				name : "color"
			}, {
				name : "border"
			} ],
			listeners : {
				update : function(store, record, operation) {
					var index = this.indexOf(record);
					_seriesData[index][1] = record.get("color");
					_seriesData[index][2] = record.get("border");
					paletteCmb.setValue("custom")
				}
			}
		});
		Ext.ux.MySplitButton = Ext.extend(Ext.Toolbar.SplitButton, {
			setValue : function(value) {
				if (this.btnEl) {
					var tmpElem = Ext.DomQuery.selectNode("*[id="
							+ this.btnEl.id + "]");
					tmpElem.style.background = value;
					tmpElem.style.width = "80px";
					tmpElem.style.height = "15px"
				} else {
					if (this.type == "color") {
						_renderColor.color = value
					} else {
						_renderColor.border = value
					}
				}
			},
			getValue : function() {
				var tmpElem = Ext.DomQuery.selectNode("*[id=" + this.btnEl.id
						+ "]");
				if (tmpElem.style.background.search("transparent") != -1) {
					return "none"
				}
				var color = tmpElem.style.backgroundColor;
				return color.charAt(0) == "#" ? color : rgbToHex(color)
			},
			isValid : function() {
				return true
			}
		});
		var colorEditor = new Ext.ux.MySplitButton(
				{
					minWidth : 100,
					type : "color",
					menu : new Ext.menu.Menu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								items : [ new Ext.ColorPalette(
										{
											colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
											allowReselect : true,
											listeners : {
												select : function(colorP,
														colorStr) {
													var fillColor = "#"
															+ colorStr;
													colorEditor
															.setValue(fillColor);
													colorEditor.menu.hide()
												}
											}
										}) ],
								listeners : {
									beforeshow : function() {
										var color = colorEditor.getValue()
												.replace("#", "").toUpperCase();
										if (this.items.items[0].colors
												.join(",").indexOf(color) > 0) {
											this.items.items[0].select(color)
										}
									}
								}
							}),
					listeners : {
						render : function(btn) {
							if (this.btnEl) {
								var tmpElem = Ext.DomQuery.selectNode("*[id="
										+ this.btnEl.id + "]");
								tmpElem.style.background = _renderColor.color;
								tmpElem.style.width = "80px";
								tmpElem.style.height = "15px"
							}
						}
					}
				});
		var borderEditor = new Ext.ux.MySplitButton(
				{
					minWidth : 100,
					type : "border",
					menu : new Ext.menu.Menu(
							{
								colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
								cls : "wide-color-palette",
								iconCls : "no-icon",
								handler : Jedox.wss.app.onColorSelect,
								items : [
										{
											text : "No Color".localize(),
											id : "bgNoColor",
											handler : onNoColorSelect
										},
										new Ext.ColorPalette(
												{
													colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
													allowReselect : true,
													listeners : {
														select : function(
																colorP,
																colorStr) {
															var fillColor = "#"
																	+ colorStr;
															borderEditor
																	.setValue(fillColor);
															borderEditor.menu
																	.hide()
														}
													}
												}) ],
								listeners : {
									beforeshow : function() {
										var color = borderEditor.getValue()
												.replace("#", "").toUpperCase();
										if (this.items.items[1].colors
												.join(",").indexOf(color) > 0) {
											this.items.items[1].select(color)
										}
									}
								}
							}),
					listeners : {
						render : function(btn) {
							if (this.btnEl) {
								var tmpElem = Ext.DomQuery.selectNode("*[id="
										+ this.btnEl.id + "]");
								tmpElem.style.background = _renderColor.border;
								tmpElem.style.width = "80px";
								tmpElem.style.height = "15px"
							}
						}
					}
				});
		var editor = new Ext.ux.grid.RowEditor( {
			saveText : "Update".localize()
		});
		var columns = [
				{
					header : "Name".localize(),
					dataIndex : "name",
					width : 150,
					sortable : true
				},
				{
					header : "Color".localize(),
					dataIndex : "color",
					width : 120,
					sortable : false,
					editor : colorEditor,
					renderer : function(c, f, g) {
						var id = Ext.id();
						return '<div id="'
								+ id
								+ '" style="position: relative; background:'
								+ g.data.color
								+ ';"><div style="display: block; width:50px; margin-left: 20px;"> &nbsp </div></div>'
					}
				},
				{
					header : "Border".localize(),
					dataIndex : "border",
					width : 120,
					sortable : false,
					editor : borderEditor,
					renderer : function(c, f, g) {
						var id = Ext.id();
						var cellValue = g.data.border == "none" ? "No border"
								.localize() : "&nbsp";
						return '<div id="'
								+ id
								+ '" style="position: relative; background:'
								+ g.data.border
								+ ';"><div style="display: block; width:50px; margin-left: 20px;">'
								+ cellValue + "</div></div>"
					}
				} ];
		var grid = new Ext.grid.GridPanel( {
			store : seriesStore,
			width : 400,
			margins : "0 5 5 5",
			plugins : [ editor ],
			columns : columns
		});
		var gridPanel = new Ext.Panel( {
			border : false,
			layout : "fit",
			width : 425,
			height : 240,
			items : [ grid ]
		});
		function rgbToHex(colorStr) {
			var str = colorStr.replace(/rgb\(|\)/g, "").split(",");
			str[0] = parseInt(str[0], 10).toString(16).toLowerCase();
			str[1] = parseInt(str[1], 10).toString(16).toLowerCase();
			str[2] = parseInt(str[2], 10).toString(16).toLowerCase();
			str[0] = (str[0].length == 1) ? "0" + str[0] : str[0];
			str[1] = (str[1].length == 1) ? "0" + str[1] : str[1];
			str[2] = (str[2].length == 1) ? "0" + str[2] : str[2];
			return ("#" + str.join(""))
		}
		function onNoColorSelect() {
			borderEditor.setValue("none")
		}
		var seriesOptionsPanel = {
			title : "General".localize(),
			layout : "form",
			labelWidth : 60,
			baseCls : "x-plain",
			items : [ BRLbl, seriesOptionsLbl, BRLbl, paletteCmb, BRLbl,
					gridPanel ]
		};
		function setPalette(pKey) {
			if (pKey == "custom") {
				return
			}
			var palette = _paletteData[pKey];
			for ( var i = 0; i < _seriesData.length; i++) {
				_seriesData[i][1] = palette[i % 32];
				_seriesData[i][2] = "none"
			}
			seriesStore.loadData(_seriesData)
		}
		function getSeriesOptionsValue(chartTypeIndex) {
			var seriesOptionsProps;
			switch (chartTypeIndex) {
			case 3:
				if (_pointsFlag) {
					seriesOptionsProps = {
						SeriesCollection : _seriesData
					}
				} else {
					_seriesData.unshift(_handsColor)
				}
			default:
				seriesOptionsProps = {
					SeriesCollection : _seriesData
				};
				break
			}
			return seriesOptionsProps
		}
		function setSeriesOptionsValue(obj, chartTypeIndex) {
			_paletteData = obj.Palette;
			_seriesData = obj.SeriesCollection;
			switch (chartTypeIndex) {
			case 3:
				if (_pointsFlag) {
					_seriesData = _seriesData[0]
				} else {
					_handsColor = _seriesData.shift()
				}
				break;
			default:
				break
			}
			seriesStore.loadData(_seriesData)
		}
		function setSeriesOptionsInterface(chartType) {
			var noSeriesBorderChartTypes = {
				xlLine : 1,
				xlLineStacked : 1,
				xlLineStacked100 : 1,
				xlLineRotated : 5,
				xlLineMarkers : 1,
				xlLineMarkersStacked : 1,
				xlLineMarkersStacked100 : 1,
				xlLineMarkersStacked100 : 1,
				xlLineMarkersRotated : 5,
				xl3DLine : 1,
				xlLineMarkersRotated : 5,
				xlPie : 2,
				xl3DPie : 2,
				xlPieExploded : 2,
				xl3DPieExploded : 2,
				xlDoughnut : 2,
				xlDoughnutExploded : 2,
				xlMeterOdoFull : 3,
				xlMeterOdoFull100 : 3,
				xlMeterOdoHalf : 3,
				xlMeterOdoHalf100 : 3,
				xlMeterAngularWide : 3,
				xlMeterLineHorizontal : 3,
				xlMeterLineVertical : 3
			};
			if (chartType in noSeriesBorderChartTypes) {
				grid.getColumnModel().setConfig(columns.slice(0, 2), true)
			}
		}
		this.getPanel = function() {
			return seriesOptionsPanel
		};
		this.getValue = function(chartTypeIndex) {
			return getSeriesOptionsValue(chartTypeIndex)
		};
		this.setValue = function(obj, chartTypeIndex) {
			setSeriesOptionsValue(obj, chartTypeIndex)
		};
		this.setLabel = function(label) {
			seriesOptionsLbl.setText(label)
		};
		this.setSeriesInterface = function(chartType) {
			setSeriesOptionsInterface(chartType)
		}
	};
	var Font = function(type) {
		var type = type;
		var _obj;
		var _obj_fontPanel = {};
		var _flags = {
			preselect : false,
			setValueCall : false
		};
		var _default = {
			Type : "Arial",
			Size : 10,
			Color : "0x000000",
			Weight : false,
			Style : "regular"
		};
		var fontOptionsLbl = new Ext.form.Label( {
			text : "Font Options".localize().concat(":"),
			cls : "edit-chart-title"
		});
		var BRLbl = {
			html : "<br/>",
			baseCls : "x-plain"
		};
		var TitleTxf = function(label) {
			return new Ext.form.TextField( {
				fieldLabel : label,
				value : "Title",
				width : 180
			})
		};
		var fontOptionsPanel = new Ext.Panel( {
			title : "Font".localize(),
			layout : "form",
			baseCls : "x-plain",
			items : [ BRLbl, fontOptionsLbl, BRLbl ],
			listeners : {
				render : function(panel) {
					setTimeout(function() {
						if (_flags.setValueCall && !_flags.preselect) {
							setFontValue(_obj)
						}
						if (_fontLoaded) {
							Jedox.wss.dialog.format.font(callBackFnc,
									_obj_fontPanel, {
										effects : true,
										style : type === "title" ? false : true
									}, true)
						} else {
							Jedox.wss.app.load(
									Jedox.wss.app.dynJSRegistry.formatFont,
									[ callBackFnc, _obj_fontPanel, {
										effects : true,
										style : type === "title" ? false : true
									}, true ]);
							_fontLoaded = true
						}
					})
				}
			}
		});
		var callBackFnc = function(fontPan) {
			fontOptionsPanel.add(fontPan);
			fontOptionsPanel.doLayout()
		};
		function convert(obj) {
			var fontProps = {};
			fontProps.Type = obj.fontFamily.split(",")[0] || _default.Type;
			fontProps.Size = eval(obj.fontSize.replace("pt", ""))
					|| _default.Size;
			fontProps.Color = obj.color.replace("#", "0x") || _defautl.Color;
			if (type == "title") {
				fontProps.Style = obj.fontStyle || _default.Style;
				fontProps.Weight = obj.fontWeight || _default.Weight
			}
			return fontProps
		}
		function hexToDecCONVERTER(hex) {
			function getDec(hex) {
				var value;
				if (hex == "A") {
					value = 10
				} else {
					if (hex == "B") {
						value = 11
					} else {
						if (hex == "C") {
							value = 12
						} else {
							if (hex == "D") {
								value = 13
							} else {
								if (hex == "E") {
									value = 14
								} else {
									if (hex == "F") {
										value = 15
									} else {
										value = eval(hex)
									}
								}
							}
						}
					}
				}
				return value
			}
			hex = hex.replace("0x", "").toUpperCase();
			var a = getDec(hex.substring(0, 1));
			var b = getDec(hex.substring(1, 2));
			var c = getDec(hex.substring(2, 3));
			var d = getDec(hex.substring(3, 4));
			var e = getDec(hex.substring(4, 5));
			var f = getDec(hex.substring(5, 6));
			var x = (a * 16) + b;
			var y = (c * 16) + d;
			var z = (e * 16) + f;
			return "rgb(".concat(x, ",", y, ",", z, ")")
		}
		function getFontValue() {
			function getFont(val) {
				_obj_fontPanel = val
			}
			if (_flags.preselect) {
				fontOptionsPanel.items.items[fontOptionsPanel.items.items.length - 1]
						.fireEvent("doFontSelect", getFont);
				return convert(_obj_fontPanel)
			} else {
				return _obj
			}
		}
		function setFontValue(obj) {
			_flags.setValueCall = true;
			if (fontOptionsPanel.rendered) {
				_flags.preselect = true;
				_obj_fontPanel.fontFamily = obj.Type;
				_obj_fontPanel.color = hexToDecCONVERTER(obj.Color);
				_obj_fontPanel.fontSize = obj.Size + "pt";
				_obj_fontPanel.fontStyle = obj.Style;
				_obj_fontPanel.fontWeight = obj.Weight;
				_obj_fontPanel.textDecoration = obj.Decoration
			} else {
				_obj = obj
			}
		}
		this.getPanel = function() {
			return fontOptionsPanel
		};
		this.getValue = function() {
			return getFontValue()
		};
		this.setValue = function(obj) {
			setFontValue(obj)
		}
	};
	var TitleOptions = function(label) {
		var typesData = {
			title : {
				titleOptionsLbl : "Title Options".localize(),
				titleTxf : "Title".localize(),
				titleChb : "Title".localize()
			},
			legend : {
				titleOptionsLbl : "Legend Options".localize(),
				titleTxf : "Legend".localize(),
				titleChb : "Legend".localize()
			}
		};
		var titleOptionsLbl = new Ext.form.Label( {
			text : typesData[label].titleOptionsLbl.localize().concat(":"),
			cls : "edit-chart-title"
		});
		var BRLbl = {
			html : "<br/>",
			baseCls : "x-plain"
		};
		var titleTxf = new Ext.form.TextField( {
			fieldLabel : typesData[label].titleTxf,
			value : "Title",
			width : 180
		});
		var titleChb = new Ext.form.Checkbox( {
			fieldLabel : typesData[label].titleChb,
			checked : true
		});
		var titleOptionsPanel = {
			title : "General".localize(),
			layout : "form",
			baseCls : "x-plain",
			items : [ BRLbl, titleOptionsLbl, BRLbl, titleTxf, titleChb ]
		};
		function getTitleOptionsValue() {
			if (label === "legend") {
				return {
					HasLegend : titleChb.getValue()
				}
			} else {
				return {
					Name : titleTxf.getValue(),
					HasTitle : titleChb.getValue()
				}
			}
		}
		function setTitleOptionsValue(obj) {
			titleTxf.setValue(obj.Name);
			titleChb.setValue(obj.HasTitle || obj.HasLegend)
		}
		this.getPanel = function() {
			return titleOptionsPanel
		};
		this.getValue = function() {
			return getTitleOptionsValue()
		};
		this.setValue = function(obj) {
			setTitleOptionsValue(obj)
		}
	};
	var LegendOptions = function() {
		var legendOptionsLbl = new Ext.form.Label( {
			text : "Legend Options".localize().concat(":"),
			cls : "edit-chart-title"
		});
		var BRLbl = {
			html : "<br/>",
			baseCls : "x-plain"
		};
		var legendChb = new Ext.form.Checkbox( {
			fieldLabel : "Legend".localize()
		});
		var legendOptionsPanel = {
			title : "General".localize(),
			layout : "form",
			baseCls : "x-plain",
			items : [ BRLbl, legendOptionsLbl, BRLbl, legendChb ]
		};
		function getLegendOptionsValue() {
			return {
				HasLegend : legendChb.getValue()
			}
		}
		function setLegendOptionsValue(obj) {
			legendChb.setValue(obj.HasLegend)
		}
		this.getPanel = function() {
			return legendOptionsPanel
		};
		this.getValue = function() {
			return getLegendOptionsValue()
		};
		this.setValue = function(obj) {
			setLegendOptionsValue(obj)
		}
	};
	var AxisOptions = function(type) {
		var _type = type;
		var _obj;
		var _flags = {
			hideName : false,
			hideOptions : false,
			setValue : false
		};
		var _showPropsInterfaceflag;
		var _chartTypeIndex;
		var typesData = {
			horizontal : {
				axisOptionsLbl : "Horizontal Axis".localize(),
				axisTitleTxf : "Horizontal axis".localize(),
				title : "Horizontal Axis".localize()
			},
			vertical : {
				axisOptionsLbl : "Vertical Axis".localize(),
				axisTitleTxf : "Vertical axis".localize(),
				title : "Vertical Axis".localize()
			},
			scale : {
				axisOptionsLbl : "Scale".localize(),
				axisTitleTxf : "Scale".localize(),
				title : "Scale".localize()
			}
		};
		var axisOptionsLbl = new Ext.form.Label( {
			text : typesData[type].axisOptionsLbl.concat(":"),
			cls : "edit-chart-title"
		});
		var BRLbl = {
			html : "<br/>",
			baseCls : "x-plain"
		};
		var RadioLbl = function(text) {
			return new Ext.form.Label( {
				text : text
			})
		};
		var AutoRB = function(name, txf) {
			return new Ext.form.Radio( {
				boxLabel : "Auto".localize(),
				hideLabel : true,
				name : name,
				width : 60,
				handler : function() {
					if (this.getValue()) {
						changeStatus(txf, false)
					}
				},
				inputValue : 1
			})
		};
		var FixedRB = function(name, txf) {
			return new Ext.form.Radio( {
				boxLabel : "Fixed".localize(),
				hideLabel : true,
				name : name,
				width : 60,
				handler : function() {
					if (this.getValue()) {
						changeStatus(txf, true)
					}
				},
				inputValue : 1
			})
		};
		var FixedTxf = function() {
			return new Ext.form.TextField( {
				hideLabel : true,
				disabled : true,
				width : 80
			})
		};
		var TitleTxf = function(label) {
			return new Ext.form.TextField( {
				fieldLabel : label,
				value : "",
				width : 220
			})
		};
		var axisTitleTxf = new TitleTxf(typesData[type].axisTitleTxf);
		var minimumLbl = new RadioLbl("Minimum".localize().concat(":"));
		var minimumFixedTxf = new FixedTxf();
		var minimumAutoRb = new AutoRB("axis-options-minimum", minimumFixedTxf);
		var minimumFixedRb = new FixedRB("axis-options-minimum",
				minimumFixedTxf);
		var maximumLbl = new RadioLbl("Maximum".localize().concat(":"));
		var maximumFixedTxf = new FixedTxf();
		var maximumAutoRb = new AutoRB("axis-options-maximum", maximumFixedTxf);
		var maximumFixedRb = new FixedRB("axis-options-maximum",
				maximumFixedTxf);
		var majorUnitLbl = new RadioLbl("Major Unit".localize().concat(":"));
		var majorUnitFixedTxf = new FixedTxf();
		var majorUnitAutoRb = new AutoRB("axis-options-major-unit",
				majorUnitFixedTxf);
		var majorUnitFixedRb = new FixedRB("axis-options-major-unit",
				majorUnitFixedTxf);
		var minorUnitLbl = new RadioLbl("Minor Unit".localize().concat(":"));
		var minorUnitFixedTxf = new FixedTxf();
		var minorUnitAutoRb = new AutoRB("axis-options-minor-unit",
				minorUnitFixedTxf);
		var minorUnitFixedRb = new FixedRB("axis-options-minor-unit",
				minorUnitFixedTxf);
		var ColumnPanel = function(cmp1, cmp2, cmp3, cmp4) {
			return {
				layout : "column",
				border : false,
				width : 350,
				baseCls : "x-plain",
				defaults : {
					border : false,
					height : 25,
					baseCls : "x-plain"
				},
				items : [ {
					columnWidth : 0.3,
					items : [ cmp1 ]
				}, {
					columnWidth : 0.2,
					items : [ cmp2 ]
				}, {
					columnWidth : 0.2,
					items : [ cmp3 ]
				}, {
					columnWidth : 0.3,
					items : [ cmp4 ]
				} ]
			}
		};
		var namePanelContainer = new Ext.Panel( {
			layout : "card",
			baseCls : "x-plain",
			activeItem : 1,
			items : [ {
				html : " ",
				baseCls : "x-plain"
			}, {
				layout : "form",
				baseCls : "x-plain",
				items : axisTitleTxf
			} ]
		});
		var optionsPanel = {
			layout : "form",
			baseCls : "x-plain",
			items : [
					new ColumnPanel(minimumLbl, minimumAutoRb, minimumFixedRb,
							minimumFixedTxf),
					new ColumnPanel(maximumLbl, maximumAutoRb, maximumFixedRb,
							maximumFixedTxf),
					new ColumnPanel(majorUnitLbl, majorUnitAutoRb,
							majorUnitFixedRb, majorUnitFixedTxf),
					new ColumnPanel(minorUnitLbl, minorUnitAutoRb,
							minorUnitFixedRb, minorUnitFixedTxf) ]
		};
		var optionsPanelContainer = new Ext.Panel( {
			layout : "card",
			baseCls : "x-plain",
			activeItem : 1,
			items : [ {
				html : " ",
				baseCls : "x-plain"
			}, optionsPanel ]
		});
		var axisPanel = new Ext.Panel( {
			title : typesData[type].title,
			layout : "form",
			baseCls : "x-plain",
			items : [ BRLbl, axisOptionsLbl, BRLbl, namePanelContainer, BRLbl,
					optionsPanelContainer ],
			listeners : {
				render : function(panel) {
					setTimeout(function() {
						if (_flags.hideName) {
							hideName()
						}
						if (_flags.hideOptions) {
							hideOptions()
						}
						if (_flags.setValue) {
							setAxisOptionsValue(_obj, _chartTypeIndex)
						}
					})
				}
			}
		});
		function changeStatus(txf, status) {
			status ? txf.enable() : txf.disable()
		}
		function getAxisOptionsValue() {
			if (axisPanel.rendered) {
				return {
					Name : _flags.hideName ? null : axisTitleTxf.getValue(),
					MinimumScaleIsAuto : _flags.hideOptions ? null
							: minimumAutoRb.getValue(),
					MaximumScaleIsAuto : _flags.hideOptions ? null
							: maximumAutoRb.getValue(),
					MinimumScale : _flags.hideOptions ? null : minimumFixedTxf
							.getValue(),
					MaximumScale : _flags.hideOptions ? null : maximumFixedTxf
							.getValue(),
					MajorUnitIsAuto : _flags.hideOptions ? null
							: majorUnitAutoRb.getValue(),
					MinorUnitIsAuto : _flags.hideOptions ? null
							: minorUnitAutoRb.getValue(),
					MajorUnit : _flags.hideOptions ? null : majorUnitFixedTxf
							.getValue(),
					MinorUnit : _flags.hideOptions ? null : minorUnitFixedTxf
							.getValue()
				}
			}
			return _obj
		}
		function setAxisOptionsValue(obj, chartTypeIndex) {
			if (axisPanel.rendered) {
				axisTitleTxf.setValue(obj.Name);
				if (obj.MinimumScaleIsAuto) {
					minimumAutoRb.setValue(obj.MinimumScaleIsAuto)
				} else {
					minimumFixedRb.setValue(!obj.MinimumScaleIsAuto);
					minimumFixedTxf.setValue(obj.MinimumScale)
				}
				if (obj.MaximumScaleIsAuto) {
					maximumAutoRb.setValue(obj.MaximumScaleIsAuto)
				} else {
					maximumFixedRb.setValue(!obj.MaximumScaleIsAuto);
					maximumFixedTxf.setValue(obj.MaximumScale)
				}
				if (obj.MajorUnitIsAuto) {
					majorUnitAutoRb.setValue(obj.MajorUnitIsAuto)
				} else {
					majorUnitFixedRb.setValue(!obj.MajorUnitIsAuto);
					majorUnitFixedTxf.setValue(obj.MajorUnit)
				}
				if (obj.MinorUnitIsAuto) {
					minorUnitAutoRb.setValue(obj.MinorUnitIsAuto)
				} else {
					minorUnitFixedRb.setValue(!obj.MinorUnitIsAuto);
					minorUnitFixedTxf.setValue(obj.MinorUnit)
				}
			} else {
				_flags.setValue = true;
				_obj = obj;
				_chartTypeIndex = chartTypeIndex
			}
		}
		function setDefault() {
			minimumAutoRb.setValue(true);
			maximumAutoRb.setValue(true);
			majorUnitAutoRb.setValue(true);
			minorUnitAutoRb.setValue(true)
		}
		function hideName() {
			if (axisPanel.rendered) {
				namePanelContainer.getLayout().setActiveItem(0)
			} else {
				_flags.hideName = true
			}
		}
		function hideOptions() {
			if (axisPanel.rendered) {
				optionsPanelContainer.getLayout().setActiveItem(0)
			} else {
				_flags.hideOptions = true
			}
		}
		this.getPanel = function() {
			return axisPanel
		};
		this.getValue = function() {
			return getAxisOptionsValue()
		};
		this.setValue = function(obj, chartTypeIndex) {
			setAxisOptionsValue(obj, chartTypeIndex)
		};
		this.hideAxisName = function() {
			hideName()
		};
		this.hideAxisScale = function() {
			hideOptions()
		};
		this.setLabel = function(label) {
			axisOptionsLbl.setText(label);
			axisPanel.setTitle(label)
		}
	};
	var ChartArea = function() {
		var fillPanel = new Fill("chartArea");
		var borderPanel = new Border("chartArea");
		var chartArea = new Ext.TabPanel(
				{
					height : 370,
					width : 345,
					layoutOnTabChange : true,
					baseCls : "edit-chart-tab-panel",
					defaults : {
						bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
					},
					activeTab : 0,
					items : [ fillPanel.getPanel(), borderPanel.getPanel() ]
				});
		function getChartAreaValue() {
			var chartAreaProps = {
				Fill : fillPanel.getValue(),
				Border : borderPanel.getValue()
			};
			return chartAreaProps
		}
		function setChartAreaValue(obj) {
			fillPanel.setValue(obj.Fill);
			borderPanel.setValue(obj.Border)
		}
		this.getPanel = function() {
			return chartArea
		};
		this.getValue = function() {
			return getChartAreaValue()
		};
		this.setValue = function(obj) {
			setChartAreaValue(obj)
		}
	};
	var PlotArea = function() {
		var fillPanel = new Fill("plotArea");
		var borderPanel = new Border("plotArea");
		var plotArea = new Ext.TabPanel(
				{
					height : 370,
					width : 345,
					layoutOnTabChange : true,
					baseCls : "edit-chart-tab-panel",
					defaults : {
						bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
					},
					activeTab : 0,
					items : [ fillPanel.getPanel(), borderPanel.getPanel() ]
				});
		function getPlotAreaValue() {
			var plotAreaProps = {
				Fill : fillPanel.getValue(),
				Border : borderPanel.getValue()
			};
			return plotAreaProps
		}
		function setPlotAreaValue(obj) {
			fillPanel.setValue(obj.Fill);
			borderPanel.setValue(obj.Border)
		}
		this.getPanel = function() {
			return plotArea
		};
		this.getValue = function() {
			return getPlotAreaValue()
		};
		this.setValue = function(obj) {
			setPlotAreaValue(obj)
		}
	};
	var Title = function() {
		var titleOptions = new TitleOptions("title");
		var font = new Font("title");
		var title = new Ext.TabPanel(
				{
					height : 370,
					width : 345,
					layoutOnTabChange : true,
					baseCls : "edit-chart-tab-panel",
					defaults : {
						bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
					},
					activeTab : 0,
					items : [ titleOptions.getPanel(), font.getPanel() ]
				});
		function getTitleValue(chartTypeIndex) {
			var titleProps;
			switch (chartTypeIndex) {
			case 2:
			case 3:
				titleProps = null;
				break;
			default:
				titleProps = {
					General : titleOptions.getValue(),
					Font : font.getValue()
				};
				break
			}
			return titleProps
		}
		function setTitleValue(obj) {
			titleOptions.setValue(obj.General);
			font.setValue(obj.Font)
		}
		this.getPanel = function() {
			return title
		};
		this.getValue = function(chartTypeIndex) {
			return getTitleValue(chartTypeIndex)
		};
		this.setValue = function(obj) {
			setTitleValue(obj)
		}
	};
	var Series = function(pointsFlag) {
		var seriesOptions = new SeriesOptions(pointsFlag);
		var series = new Ext.TabPanel(
				{
					height : 370,
					width : 345,
					layoutOnTabChange : true,
					baseCls : "edit-chart-tab-panel",
					defaults : {
						bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
					},
					activeTab : 0,
					items : [ seriesOptions.getPanel() ]
				});
		function getSeriesValue(chartTypeIndex) {
			var seriesProps = {
				General : seriesOptions.getValue(chartTypeIndex)
			};
			return seriesProps
		}
		function setSeriesValue(obj, chartTypeIndex) {
			seriesOptions.setValue(obj.General, chartTypeIndex)
		}
		this.getPanel = function() {
			return series
		};
		this.getValue = function(chartTypeIndex) {
			return getSeriesValue(chartTypeIndex)
		};
		this.setValue = function(obj, chartTypeIndex) {
			setSeriesValue(obj, chartTypeIndex)
		};
		this.getSeriesOptions = function() {
			return seriesOptions
		}
	};
	var Legend = function() {
		var legendOptions = new LegendOptions();
		var font = new Font("legend");
		var fillPanel = new Fill("legend");
		var borderPanel = new Border("legend");
		var legend = new Ext.TabPanel(
				{
					height : 370,
					width : 345,
					layoutOnTabChange : true,
					baseCls : "edit-chart-tab-panel",
					defaults : {
						bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
					},
					activeTab : 0,
					items : [ legendOptions.getPanel(), font.getPanel(),
							fillPanel.getPanel(), borderPanel.getPanel() ]
				});
		function getLegendValue(chartTypeIndex) {
			var legendProps;
			switch (chartTypeIndex) {
			case 2:
			case 3:
				legendProps = null;
				break;
			default:
				legendProps = {
					General : legendOptions.getValue(),
					Font : font.getValue(),
					Fill : fillPanel.getValue(),
					Border : borderPanel.getValue()
				};
				break
			}
			return legendProps
		}
		function setLegendValue(obj) {
			legendOptions.setValue(obj.General);
			fillPanel.setValue(obj.Fill);
			borderPanel.setValue(obj.Border);
			font.setValue(obj.Font)
		}
		this.getPanel = function() {
			return legend
		};
		this.getValue = function(chartTypeIndex) {
			return getLegendValue(chartTypeIndex)
		};
		this.setValue = function(obj) {
			setLegendValue(obj)
		}
	};
	var Axes = function() {
		var horizontalAxisOptions = new AxisOptions("horizontal");
		var verticalAxisOptions = new AxisOptions("vertical");
		var font = new Font("axis");
		var _cmp;
		var _no = -1;
		var _operationStack = [];
		var axes = new Ext.TabPanel(
				{
					height : 370,
					width : 345,
					layoutOnTabChange : true,
					baseCls : "edit-chart-tab-panel",
					defaults : {
						bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
					},
					activeTab : 0,
					items : [ horizontalAxisOptions.getPanel(),
							verticalAxisOptions.getPanel(), font.getPanel() ]
				});
		function getAxesValue(chartTypeIndex) {
			var axesProps;
			switch (chartTypeIndex) {
			case 2:
				axesProps = null;
				break;
			case 5:
				axesProps = {
					HorizontalAxis : verticalAxisOptions.getValue(),
					VerticalAxis : horizontalAxisOptions.getValue(),
					Font : font.getValue()
				};
				break;
			default:
				axesProps = {
					HorizontalAxis : horizontalAxisOptions.getValue(),
					VerticalAxis : verticalAxisOptions.getValue(),
					Font : font.getValue()
				};
				break
			}
			return axesProps
		}
		function setAxesValue(obj, chartTypeIndex) {
			switch (chartTypeIndex) {
			case 5:
				horizontalAxisOptions
						.setValue(obj.VerticalAxis, chartTypeIndex);
				verticalAxisOptions
						.setValue(obj.HorizontalAxis, chartTypeIndex);
				break;
			default:
				horizontalAxisOptions.setValue(obj.HorizontalAxis,
						chartTypeIndex);
				verticalAxisOptions.setValue(obj.VerticalAxis, chartTypeIndex);
				break
			}
			font.setValue(obj.Font)
		}
		this.getPanel = function() {
			return axes
		};
		this.getValue = function(chartTypeIndex) {
			return getAxesValue(chartTypeIndex)
		};
		this.setValue = function(obj, chartTypeIndex) {
			setAxesValue(obj, chartTypeIndex)
		};
		this.getHorizontalAxis = function() {
			return horizontalAxisOptions
		};
		this.getVerticalAxis = function() {
			return verticalAxisOptions
		};
		this.removeVerticalAxis = function() {
			axes.remove(1)
		}
	};
	var _fontLoaded = false;
	var typeData = [ [ "Chart Area".localize(), "chart_area_icon", 0 ],
			[ "Plot Area".localize(), "plot_area_icon", 1 ],
			[ "Points".localize(), "points_options_icon", 2 ],
			[ "Series".localize(), "series_options_icon", 3 ],
			[ "Title".localize(), "title_icon", 4 ],
			[ "Legend".localize(), "legend_icon", 5 ],
			[ "Axes".localize(), "axis_icon", 6 ] ];
	var typeNavigationStore = new Ext.data.SimpleStore( {
		fields : [ "label", "icon", "pindex" ],
		data : typeData
	});
	var typeNavigationView = new Ext.DataView(
			{
				id : "chart-type-navigation",
				store : typeNavigationStore,
				tpl : new Ext.XTemplate(
						'<div class="chart-type-navigation" style="width: 99%;"><div class="charttypes">Type</div><tpl for=".">',
						'<div class="thumb-wrap">',
						'<div class="thumb"><img class="{icon}" src="../lib/ext/resources/images/default/s.gif" width="29" height="27" />&nbsp;&nbsp;&nbsp;{label}</div></div>',
						"</tpl></div>"),
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				listeners : {
					containerclick : function(dView, e) {
						e.stopEvent();
						return false
					},
					click : function(dataView, index, node, e) {
						setNavigationView(this.store.getAt(index).get("pindex"));
						if (index != 0) {
							Jedox.wss.app.currentDialogControl = null;
							Jedox.wss.app.currentDialogControlItemsCnt = 0
						}
					}
				}
			});
	function setNavigationView(index) {
		mainPanel.items.items[1].layout.setActiveItem(index)
	}
	var typeNavigationPanel = {
		layout : "fit",
		width : 130,
		height : 380,
		border : true,
		items : typeNavigationView
	};
	var chartArea = new ChartArea();
	var plotArea = new PlotArea();
	var points = new Series(true);
	var series = new Series(false);
	var title = new Title();
	var legend = new Legend();
	var axes = new Axes();
	var mainPanel = new Ext.Panel( {
		layout : "absolute",
		baseCls : "x-plain",
		border : false,
		items : [
				{
					layout : "column",
					border : false,
					baseCls : "x-plain",
					x : 0,
					y : 0,
					items : [ typeNavigationPanel ]
				},
				{
					layout : "card",
					baseCls : "x-plain",
					border : false,
					width : 450,
					height : 380,
					activeItem : 0,
					x : 140,
					y : 0,
					items : [ chartArea.getPanel(), plotArea.getPanel(),
							points.getPanel(), series.getPanel(),
							title.getPanel(), legend.getPanel(),
							axes.getPanel() ]
				} ]
	});
	var okBtn = {
		text : "OK".localize(),
		handler : function() {
			doEditChart();
			win.close()
		}
	};
	var cancelBtn = {
		text : "Cancel".localize(),
		handler : function() {
			win.close()
		}
	};
	function switchSolid(panel, index) {
		panel.layout.setActiveItem(index)
	}
	function setNavigationInterface(indexToRmv) {
		var newTypeData = [];
		for ( var i = 0, j = 0; i < typeData.length; i++) {
			if (i in indexToRmv) {
				continue
			}
			newTypeData[j++] = typeData[i]
		}
		typeNavigationStore.loadData(newTypeData);
		typeNavigationView.select(0, false, false)
	}
	function _setInterface(chartType) {
		var remove = _chartTypesInterfaceDate[chartType];
		switch (remove) {
		case 1:
			setNavigationInterface( {
				2 : true
			});
			series.getSeriesOptions().setSeriesInterface(chartType);
			axes.getHorizontalAxis().hideAxisScale();
			break;
		case 2:
			setNavigationInterface( {
				2 : true,
				4 : true,
				5 : true,
				6 : true
			});
			typeNavigationView.getStore().getAt(2).set("label",
					"Points".localize());
			series.getSeriesOptions().setLabel("Points options".localize());
			series.getSeriesOptions().setSeriesInterface(chartType);
			break;
		case 3:
			setNavigationInterface( {
				4 : true,
				5 : true
			});
			typeNavigationView.getStore().getAt(3).set("label",
					"Zones".localize());
			typeNavigationView.getStore().getAt(4).set("label",
					"Scales".localize());
			points.getSeriesOptions().setLabel("Points options".localize());
			points.getSeriesOptions().setSeriesInterface(chartType);
			series.getSeriesOptions().setLabel("Zones options".localize());
			series.getSeriesOptions().setSeriesInterface(chartType);
			axes.getHorizontalAxis().setLabel("Scale".localize());
			axes.getHorizontalAxis().hideAxisName();
			axes.removeVerticalAxis();
			break;
		case 5:
			setNavigationInterface( {
				2 : true
			});
			series.getSeriesOptions().setSeriesInterface(chartType);
			axes.getVerticalAxis().hideAxisScale();
			break
		}
	}
	function _setValue(chartProps, chartType) {
		var chartTypeIndex = _chartTypesInterfaceDate[chartType];
		switch (chartTypeIndex) {
		case 1:
			chartArea.setValue(chartProps.ChartArea);
			plotArea.setValue(chartProps.PlotArea);
			series.setValue(chartProps.Series, chartTypeIndex);
			title.setValue(chartProps.Title);
			legend.setValue(chartProps.Legend);
			axes.setValue(chartProps.Axes, chartTypeIndex);
			break;
		case 2:
			chartArea.setValue(chartProps.ChartArea);
			plotArea.setValue(chartProps.PlotArea);
			series.setValue(chartProps.Series, chartTypeIndex);
			break;
		case 3:
			chartArea.setValue(chartProps.ChartArea);
			plotArea.setValue(chartProps.PlotArea);
			points.setValue(chartProps.Series, chartTypeIndex);
			series.setValue(chartProps.Series, chartTypeIndex);
			axes.setValue(chartProps.Axes, chartTypeIndex);
			break;
		case 4:
			chartArea.setValue(chartProps.ChartArea);
			plotArea.setValue(chartProps.PlotArea);
			series.setValue(chartProps.Series, chartTypeIndex);
			title.setValue(chartProps.Title);
			legend.setValue(chartProps.Legend);
			axes.setValue(chartProps.Axes, chartTypeIndex);
			break;
		case 5:
			chartArea.setValue(chartProps.ChartArea);
			plotArea.setValue(chartProps.PlotArea);
			series.setValue(chartProps.Series, chartTypeIndex);
			title.setValue(chartProps.Title);
			legend.setValue(chartProps.Legend);
			axes.setValue(chartProps.Axes, chartTypeIndex);
			break
		}
	}
	function fillChartDialog(chartID) {
		var chartProps;
		if ((chartProps = Jedox.wss.backend.ha.wsel("get_chart_properties",
				chartID)) == false) {
			Ext.MessageBox
					.show( {
						title : "Operation Error".localize(),
						msg : "chartDlg_EditError".localize(),
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR,
						fn : function() {
							Jedox.wss.general
									.setInputMode(Jedox.wss.app.lastInputModeDlg);
							Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.editChart)
						}
					});
			win.destroy();
			return
		}
		_setInterface(chartProps.ChartType.General.Type);
		_setValue(chartProps, chartProps.ChartType.General.Type);
		console.log(chartProps);
		_chartProps = chartProps;
		win.show();
		typeNavigationView.select(0, false, false)
	}
	var doEditChart = function() {
		var chartProps = collectProperties();
		var winID = Jedox.wss.app.activeBook.getDomId();
		if (chartProps != false) {
			chartProps.id = chartID;
			var chartElem = Ext.get("".concat(winID, "_ws_element_", chartID));
			chartProps.size = [ chartElem.getWidth(), chartElem.getHeight() ];
			if (Jedox.wss.backend.ha.wsel("update_chart", chartProps)) {
				var currDate = new Date();
				document.getElementById("".concat(winID, "_ws_element_",
						chartID)).src = "cc/gen_element.php?wam=".concat(
						Jedox.wss.app.appModeS, "&id=", chartID, "&ts=",
						currDate.getTime())
			}
		}
	};
	function collectProperties() {
		var chartTypeIndex = _chartTypesInterfaceDate[_chartProps.ChartType.General.Type];
		var whRatio = getWHRatio();
		var vportPos = Jedox.wss.app.activeBook.getViewportPos();
		var elWidth = whRatio[0] * 100;
		var elHeight = whRatio[1] * 100;
		var elTop = parseInt(vportPos[0][1]
				+ (vportPos[1][1] - vportPos[0][1] - elHeight) / 2);
		var elLeft = parseInt(vportPos[0][0]
				+ (vportPos[1][0] - vportPos[0][0] - elWidth) / 2);
		var nLoc = Jedox.wss.wsel.getNLoc(elLeft, elTop, elWidth, elHeight);
		var chartProps = {
			props : {
				ChartType : _chartProps.ChartType,
				ChartArea : chartArea.getValue(),
				PlotArea : plotArea.getValue(),
				SourceData : _chartProps.SourceData,
				Series : series.getValue(chartTypeIndex),
				Title : title.getValue(chartTypeIndex),
				Legend : legend.getValue(chartTypeIndex),
				Axes : axes.getValue(chartTypeIndex),
				General : {
					WH_ratio : whRatio,
					Layout : _chartProps.General.Layout,
					Style : _chartProps.General.Style,
					ActiveSheet : Jedox.wss.app.activeSheetName
				}
			},
			n_location : nLoc.n_location,
			pos_offsets : nLoc.pos_offsets,
			size : [ elWidth, elHeight ],
			operation : "format"
		};
		if (!chartProps.props.ChartType.General.Type.indexOf("xl") == 0) {
			Ext.MessageBox.show( {
				title : "Input Error".localize(),
				msg : "chartDlg_invalidChartType".localize(),
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.WARNING
			});
			return false
		}
		if (chartProps.props.SourceData.General.Source.length == 0) {
			Ext.MessageBox.show( {
				title : "Input Error".localize(),
				msg : "chartDlg_rangeEmpty".localize(),
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.WARNING,
				fn : function() {
					Ext.getCmp("dataRange").focus()
				}
			});
			return false
		}
		console.log(chartProps);
		return chartProps
	}
	var win = new Ext.Window( {
		id : "edit-chart-dlg",
		title : "Format Chart Properties".localize(),
		closable : true,
		closeAction : "close",
		cls : "default-format-window",
		autoDestroy : true,
		plain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 605,
		height : 455,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.editChart)
			}
		},
		buttons : [ okBtn, cancelBtn ]
	});
	if (mode == "edit") {
		fillChartDialog(chartID)
	}
};