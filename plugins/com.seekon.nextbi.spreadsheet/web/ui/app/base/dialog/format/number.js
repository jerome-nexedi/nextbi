Jedox.wss.dialog.format.number = function(callback, _pre, toDisable,
		isFromOther) {
	var rawCellValue = Jedox.wss.app.environment.selectedCellValue;
	var selCoord = Jedox.wss.app.environment.selectedCellCoords;
	var env = Jedox.wss.app.environment, rngStartCoord = env.lastRangeStartCoord, rngEndCoord = env.lastRangeEndCoord;
	function formatStringInitial(fString, fValue) {
		var ftValue = Jedox.wss.format.getSample(fString, fValue);
		cellFormattingString = fString;
		cellFormattedString = ftValue;
		return ftValue
	}
	var cellFormattedString = "";
	var initNumCard = 0;
	if (_pre) {
		var preFormattingString = _pre
	} else {
		var preFormattingString = Jedox.wss.app.activeBook.getRangeFormat( [
				rngStartCoord[0], rngStartCoord[1], rngEndCoord[0],
				rngEndCoord[1] ])
	}
	if (!preFormattingString) {
		preFormattingString = [ "general", 0, 2, false, 0 ]
	}
	var initNumCard = preFormattingString[1];
	var indexMarker = initNumCard;
	var NFAttrib1 = preFormattingString[2];
	var NFAttrib2 = preFormattingString[3];
	var NFAttrib3 = preFormattingString[4];
	var cellFormattingString = preFormattingString[0];
	if (initNumCard == 11) {
		var initialCustomFmt = cellFormattingString
	} else {
		var initialCustomFmt = ""
	}
	cellFormattedString = formatStringInitial(cellFormattingString,
			rawCellValue);
	function formatString(fString, fValue) {
		var ftValue = Jedox.wss.format.getSample(fString, fValue);
		cellFormattingString = fString;
		var tmpRec = new sampleViewTmp( {
			formatted_value : ftValue
		});
		sampleViewStore.removeAll();
		sampleViewStore.insert(0, tmpRec);
		return ftValue
	}
	function formatStringInitial(fString, fValue) {
		var ftValue = Jedox.wss.format.getSample(fString, fValue);
		cellFormattingString = fString;
		cellFormattedString = ftValue;
		return ftValue
	}
	var sampleView = [ [ cellFormattedString ] ];
	var sampleViewStore = new Ext.data.SimpleStore( {
		fields : [ "formatted_value" ],
		data : sampleView
	});
	var sampleDataView = new Ext.DataView( {
		id : "number-sample",
		itemSelector : ".border-field-chooser",
		style : "overflow:auto",
		autoWidth : true,
		singleSelect : true,
		store : sampleViewStore,
		cls : "borderStyleSelect",
		tpl : new Ext.XTemplate(
				'<div><tpl for=".">{formatted_value}</tpl></div>')
	});
	var sampleViewTmp = new Ext.data.Record.create( [ {
		name : "formatted_value"
	} ]);
	var onCategorySelect = function(dView, index, node, ev) {
		indexMarker = index;
		Ext.getCmp("main_format_content").layout.setActiveItem(index);
		Ext.getCmp("num_description").layout.setActiveItem(index);
		if (index == 0) {
			cellFormattingString = "general";
			formatString(cellFormattingString, rawCellValue);
			formattingStringDesc = [ 0 ]
		} else {
			if (index == 1) {
				cellFormattingString = "0";
				var stopper = decPlacesSpinner.getValue();
				stopper++;
				for (i = 1; i < stopper; i++) {
					cellFormattingString = (i == 1) ? cellFormattingString
							.concat(".0") : cellFormattingString.concat("0")
				}
				decPlacesSpinner.setWidth(57);
				if (NFAttrib2 != true) {
					Ext.getCmp("th_separator").setValue(false)
				}
				if (NFAttrib3 > 0) {
					formatNegativeNum(NFAttrib3)
				} else {
					formatString(cellFormattingString, rawCellValue)
				}
				Ext.getCmp("negativeNumberView1")
						.select(NFAttrib3, false, true)
			} else {
				if (index == 2) {
					cellFormattingString = "#,##0";
					var stopper = decPlacesSpinner2.getValue();
					stopper++;
					for (i = 1; i < stopper; i++) {
						cellFormattingString = (i == 1) ? cellFormattingString
								.concat(".0") : cellFormattingString
								.concat("0")
					}
					decPlacesSpinner2.setWidth(57);
					if (NFAttrib2 != false
							&& currencyListStore.find("csymbol", NFAttrib2) > 0) {
						cellFormattingString = cellFormattingString.concat(" ",
								NFAttrib2);
						Ext.getCmp("currencyCombo1").setValue(
								currency_list[currencyListStore.find("csymbol",
										NFAttrib2)][1])
					}
					if (NFAttrib3 > 0) {
						formatNegativeNum(NFAttrib3)
					} else {
						formatString(cellFormattingString, rawCellValue)
					}
					Ext.getCmp("negativeNumberView2").select(NFAttrib3, false,
							true)
				} else {
					if (index == 3) {
						cellFormattingString = "#,##0";
						var stopper = decPlacesSpinner3.getValue();
						stopper++;
						for (i = 1; i < stopper; i++) {
							cellFormattingString = (i == 1) ? cellFormattingString
									.concat(".0")
									: cellFormattingString.concat("0")
						}
						if (NFAttrib2 != false
								&& currencyListStore.find("csymbol", NFAttrib2) > 0) {
							cellFormattingString = cellFormattingString.concat(
									" ", NFAttrib2);
							Ext.getCmp("currencyCombo2").setValue(
									currency_list[currencyListStore.find(
											"csymbol", NFAttrib2)][1])
						}
						decPlacesSpinner3.setWidth(57);
						formatString(cellFormattingString, rawCellValue)
					} else {
						if (index == 4) {
							cellFormattingString = "d.m.yyyy";
							formatString(cellFormattingString, rawCellValue)
						} else {
							if (index == 5) {
								cellFormattingString = "[$-F400]HH:mm:ss";
								formatString(cellFormattingString, rawCellValue)
							} else {
								if (index == 6) {
									cellFormattingString = "0";
									var stopper = decPlacesSpinner4.getValue();
									stopper++;
									for (i = 1; i < stopper; i++) {
										cellFormattingString = (i == 1) ? cellFormattingString
												.concat(".0")
												: cellFormattingString
														.concat("0")
									}
									cellFormattingString = cellFormattingString
											.concat("%");
									decPlacesSpinner4.setWidth(57);
									formatString(cellFormattingString,
											rawCellValue)
								} else {
									if (index == 7) {
										cellFormattingString = "# ?/?";
										formatString(cellFormattingString,
												rawCellValue)
									} else {
										if (index == 8) {
											cellFormattingString = "0";
											var stopper = decPlacesSpinner5
													.getValue();
											stopper++;
											for (i = 1; i < stopper; i++) {
												cellFormattingString = (i == 1) ? cellFormattingString
														.concat(".0")
														: cellFormattingString
																.concat("0")
											}
											cellFormattingString = cellFormattingString
													.concat("E+00");
											decPlacesSpinner5.setWidth(57);
											formatString(cellFormattingString,
													rawCellValue)
										} else {
											if (index == 9) {
												cellFormattingString = "@";
												formatString(
														cellFormattingString,
														rawCellValue)
											} else {
												if (index == 10) {
													cellFormattingString = "";
													formatString(
															cellFormattingString,
															rawCellValue)
												} else {
													if (index == 11) {
														cellFormattingString = "";
														formatString(
																cellFormattingString,
																rawCellValue)
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	};
	var cellNumberFormat = [ [ "general", "General".localize() ],
			[ "number", "Number".localize() ],
			[ "currency", "Currency".localize() ],
			[ "accounting", "Accounting".localize() ],
			[ "date", "Date".localize() ], [ "time", "Time".localize() ],
			[ "percentage", "Percentage".localize() ],
			[ "fraction", "Fraction".localize() ],
			[ "scientific", "Scientific".localize() ],
			[ "text", "Text".localize() ], [ "special", "Special".localize() ],
			[ "custom", "Custom".localize() ] ];
	var numberNegativeFormat = [
			[ "normal", "".concat("<span>&#160;", "-1234,10", "</span>") ],
			[
					"red",
					"".concat('<span style="color:#FF0000;">&#160;', "1234,10",
							"</span>") ],
			[ "negative", "".concat("<span>&#160;", "(1234,10)", "</span>") ],
			[
					"red_negative",
					"".concat('<span style="color:#FF0000;">&#160;',
							"(1234,10)", "</span>") ] ];
	var currency_list = [ [ "dollar", "$ US Dollar", "[$$]" ],
			[ "euro", "€ Euro", "[$€]" ], [ "pound", "£ GB Pound", "[$£]" ],
			[ "swiss", "CHF Swiss Francs", "[$CHF]" ],
			[ "japan", "Y Japanese Yen", "[$Y]" ],
			[ "turkey", "YTL Turkey Liras", "[$YTL]" ],
			[ "polska", "ZL Poland Zlotych", "[$ZL]" ],
			[ "israel", "ISH Israel, New Shekels", "[$ISH]" ],
			[ "hongkong", "HKD Hong Kong Dollar", "[$HKD]" ],
			[ "czech", "KC Czech Koruny", "[$KC]" ],
			[ "china", "CNY China Yuan", "[$CNY]" ],
			[ "russia", "P Russian Rubles", "[$RUB]" ] ];
	var currencyListStore = new Ext.data.SimpleStore( {
		fields : [ "cname", "cdesc", "csymbol" ],
		data : currency_list
	});
	var fractionType = [ [ "1", "Up to one digit(1/4)".localize(), "# ?/?" ],
			[ "2", "Up to two digits(21/35)".localize(), "# ??/??" ],
			[ "3", "Up to three digits(312/943)".localize(), "# ???/???" ],
			[ "4", "Up to halves(1/2)".localize(), "# ?/2" ],
			[ "5", "Up to quarters(2/4)".localize(), "# ?/4" ],
			[ "6", "Up to eights(4/8)".localize(), "# ?/8" ],
			[ "7", "Up to sixteenths(8/16)".localize(), "# ?/16" ],
			[ "8", "Up to tenths(3/10)".localize(), "# ?/10" ],
			[ "9", "Up to hundredths(30/100)".localize(), "# ?/100" ] ];
	var dateType = [
			[ "1", "*16.5.2009", "d.m.yyyy" ],
			[ "2", "*16. ".concat("may".localize(), " 2009"), "d. mmm yyyy" ],
			[ "3", "16.5.2009", "dd.m.yyyy" ],
			[ "4", "16.5.09", "dd.m.yy" ],
			[ "5", "16. 5. 2009", "dd. m. yyyy" ],
			[ "6", "16.05.2009", "dd.mm.yyyy" ],
			[ "7", "16. 5. 09", "dd. m. yy" ],
			[ "8", "2009-05-16", "yyyy-mm-dd" ],
			[ "9", "16. ".concat("may".localize(), " 2009"), "dd. mmmm yyyy" ],
			[
					"10",
					"Thu".localize().concat(" 16. ", "may".localize(), " 2009"),
					"dddd, d. mmmm yyyy" ] ];
	var timeType = [ [ "1", "*16:25:00", "h:mm:ss" ],
			[ "2", "16:25:00", "hh:mm:ss" ], [ "3", "16:25", "hh:mm" ] ];
	var dateTypeStore = new Ext.data.SimpleStore( {
		fields : [ "nn", "date_format", "formatting_str" ],
		data : dateType
	});
	var fractionTypeStore = new Ext.data.SimpleStore( {
		fields : [ "nn", "fraction_format", "formatting_str" ],
		data : fractionType
	});
	var timeTypeStore = new Ext.data.SimpleStore( {
		fields : [ "nn", "time_format", "formatting_str" ],
		data : timeType
	});
	var cellNumberFormatStore = new Ext.data.SimpleStore( {
		fields : [ "catname", "catlabel" ],
		data : cellNumberFormat
	});
	var numberNegativeStore = new Ext.data.SimpleStore( {
		fields : [ "num_name", "num_format" ],
		data : numberNegativeFormat
	});
	var numDescription = new Ext.Panel( {
		id : "num_description",
		layout : "card",
		autoWidth : true,
		baseCls : "x-plain",
		defaults : {
			bodyStyle : "background-color: transparent; padding-top: 10px;"
		},
		border : false,
		activeItem : initNumCard,
		region : "center",
		items : [ {
			border : false
		}, {
			html : "_catDescr: number".localize(),
			border : false
		}, {
			html : "_catDescr: currency".localize(),
			border : false
		}, {
			html : "_catDescr: accounting".localize(),
			border : false
		}, {
			html : "_catDescr: date".localize(),
			border : false
		}, {
			html : "_catDescr: time".localize(),
			border : false
		}, {
			html : "_catDescr: percentage".localize(),
			border : false
		}, {
			html : "".localize(),
			border : false
		}, {
			html : "".localize(),
			border : false
		}, {
			html : "".localize(),
			border : false
		}, {
			html : "_catDescr: special".localize(),
			border : false
		}, {
			html : "_catDescr: custom".localize(),
			border : false
		} ]
	});
	var decPlacesSpinner = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 50
		}),
		name : "dec_places",
		allowBlank : false,
		xtype : "number",
		fieldLabel : "Decimal places".localize(),
		width : 40,
		border : false,
		value : NFAttrib1,
		listeners : {
			blur : function(e) {
				var tmpVal = this.getRawValue();
				tmpVal = parseInt(tmpVal);
				this.setValue(tmpVal);
				NFAttrib1 = this.getValue();
				if (tmpVal == 0) {
					var fmtStr = "0"
				} else {
					var fmtStr = "0."
				}
				for (i = 1; i < tmpVal + 1; i++) {
					fmtStr = fmtStr.concat("0")
				}
				if (NFAttrib2) {
					fmtStr = "#,##".concat(fmtStr)
				}
				if (NFAttrib3 > 0) {
					cellFormattingString = fmtStr;
					formatNegativeNum(NFAttrib3)
				} else {
					formatString(fmtStr, rawCellValue)
				}
			}
		},
		onSpinUp : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal < 30) {
				tmpVal = tmpVal + 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			var fmtStr = "0.";
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			if (NFAttrib2) {
				fmtStr = "#,##".concat(fmtStr)
			}
			if (NFAttrib3 > 0) {
				cellFormattingString = fmtStr;
				formatNegativeNum(NFAttrib3)
			} else {
				formatString(fmtStr, rawCellValue)
			}
		},
		onSpinDown : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal > 0) {
				tmpVal = tmpVal - 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			if (tmpVal == 0) {
				var fmtStr = "0"
			} else {
				var fmtStr = "0."
			}
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			if (NFAttrib2) {
				fmtStr = "#,##".concat(fmtStr)
			}
			if (NFAttrib3 > 0) {
				cellFormattingString = fmtStr;
				formatNegativeNum(NFAttrib3)
			} else {
				formatString(fmtStr, rawCellValue)
			}
		}
	});
	var decPlacesSpinner2 = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 50
		}),
		name : "dec_places_2",
		allowBlank : false,
		fieldLabel : "Decimal places".localize(),
		width : 40,
		border : false,
		value : NFAttrib1,
		listeners : {
			blur : function(e) {
				var tmpVal = this.getRawValue();
				tmpVal = parseInt(tmpVal);
				this.setValue(tmpVal);
				NFAttrib1 = this.getValue();
				if (tmpVal == 0) {
					var fmtStr = "0"
				} else {
					var fmtStr = "0."
				}
				for (i = 1; i < tmpVal + 1; i++) {
					fmtStr = fmtStr.concat("0")
				}
				if (NFAttrib2) {
					fmtStr = "#,##".concat(fmtStr)
				}
				if (NFAttrib3 > 0) {
					cellFormattingString = fmtStr;
					formatNegativeNum(NFAttrib3)
				} else {
					formatString(fmtStr, rawCellValue)
				}
			}
		},
		onSpinUp : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (!NFAttrib2) {
				NFAttrib2 = ""
			}
			if (tmpVal < 30) {
				tmpVal = tmpVal + 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			var fmtStr = "#,##0.";
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			if (NFAttrib2 != true) {
				fmtStr = fmtStr.concat(" ", NFAttrib2)
			}
			if (NFAttrib3 > 0) {
				cellFormattingString = fmtStr;
				formatNegativeNum(NFAttrib3)
			} else {
				formatString(fmtStr, rawCellValue)
			}
		},
		onSpinDown : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (!NFAttrib2) {
				NFAttrib2 = ""
			}
			if (tmpVal > 0) {
				tmpVal = tmpVal - 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			if (tmpVal == 0) {
				var fmtStr = "#,##0"
			} else {
				var fmtStr = "#,##0."
			}
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			if (NFAttrib2 != true) {
				fmtStr = fmtStr.concat(" ", NFAttrib2)
			}
			if (NFAttrib3 > 0) {
				cellFormattingString = fmtStr;
				formatNegativeNum(NFAttrib3)
			} else {
				formatString(fmtStr, rawCellValue)
			}
		}
	});
	var decPlacesSpinner3 = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 50
		}),
		name : "dec_places_3",
		allowBlank : false,
		fieldLabel : "Decimal places".localize(),
		width : 40,
		border : false,
		value : NFAttrib1,
		listeners : {
			blur : function(e) {
				var tmpVal = this.getRawValue();
				tmpVal = parseInt(tmpVal);
				this.setValue(tmpVal);
				NFAttrib1 = this.getValue();
				if (tmpVal == 0) {
					var fmtStr = "#,##0"
				} else {
					var fmtStr = "#,##0."
				}
				for (i = 1; i < tmpVal + 1; i++) {
					fmtStr = fmtStr.concat("0")
				}
				if (NFAttrib2 != true) {
					fmtStr = fmtStr.concat(" ", NFAttrib2)
				}
				formatString(fmtStr, rawCellValue)
			}
		},
		onSpinUp : function() {
			if (!NFAttrib2) {
				NFAttrib2 = ""
			}
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal < 30) {
				tmpVal = tmpVal + 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			var fmtStr = "#,##0.";
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			if (NFAttrib2 != true) {
				fmtStr = fmtStr.concat(" ", NFAttrib2)
			}
			formatString(fmtStr, rawCellValue)
		},
		onSpinDown : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal > 0) {
				tmpVal = tmpVal - 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			if (tmpVal == 0) {
				var fmtStr = "#,##0"
			} else {
				var fmtStr = "#,##0."
			}
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			if (NFAttrib2 != true) {
				fmtStr = fmtStr.concat(" ", NFAttrib2)
			}
			formatString(fmtStr, rawCellValue)
		}
	});
	var decPlacesSpinner4 = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 50
		}),
		name : "dec_places_2",
		allowBlank : false,
		fieldLabel : "Decimal places".localize(),
		width : 40,
		border : false,
		value : NFAttrib1,
		listeners : {
			blur : function(e) {
				var tmpVal = this.getRawValue();
				tmpVal = parseInt(tmpVal);
				this.setValue(tmpVal);
				NFAttrib1 = this.getValue();
				if (tmpVal == 0) {
					var fmtStr = "0"
				} else {
					var fmtStr = "0."
				}
				for (i = 1; i < tmpVal + 1; i++) {
					fmtStr = fmtStr.concat("0")
				}
				fmtStr = fmtStr.concat("%");
				formatString(fmtStr, rawCellValue)
			}
		},
		onSpinUp : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal < 30) {
				tmpVal = tmpVal + 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			var fmtStr = "0.";
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			fmtStr = fmtStr.concat("%");
			formatString(fmtStr, rawCellValue)
		},
		onSpinDown : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal > 0) {
				tmpVal = tmpVal - 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			if (tmpVal == 0) {
				var fmtStr = "0"
			} else {
				var fmtStr = "0."
			}
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			fmtStr = fmtStr.concat("%");
			formatString(fmtStr, rawCellValue)
		}
	});
	var decPlacesSpinner5 = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 50
		}),
		name : "dec_places_2",
		allowBlank : false,
		fieldLabel : "Decimal places".localize(),
		width : 40,
		border : false,
		value : NFAttrib1,
		listeners : {
			blur : function(e) {
				var tmpVal = this.getRawValue();
				tmpVal = parseInt(tmpVal);
				this.setValue(tmpVal);
				NFAttrib1 = this.getValue();
				if (tmpVal == 0) {
					var fmtStr = "0"
				} else {
					var fmtStr = "0."
				}
				for (i = 1; i < tmpVal + 1; i++) {
					fmtStr = fmtStr.concat("0")
				}
				fmtStr = fmtStr.concat("E+00");
				formatString(fmtStr, rawCellValue)
			}
		},
		onSpinUp : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal < 30) {
				tmpVal = tmpVal + 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			var fmtStr = "0.";
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			fmtStr = fmtStr.concat("E+00");
			formatString(fmtStr, rawCellValue)
		},
		onSpinDown : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal > 0) {
				tmpVal = tmpVal - 1
			}
			this.setValue(tmpVal);
			var decPlacesNum = this.getValue();
			NFAttrib1 = this.getValue();
			if (tmpVal == 0) {
				var fmtStr = "0"
			} else {
				var fmtStr = "0."
			}
			for (i = 1; i < tmpVal + 1; i++) {
				fmtStr = fmtStr.concat("0")
			}
			fmtStr = fmtStr.concat("E+00");
			formatString(fmtStr, rawCellValue)
		}
	});
	var customFormatting = new Ext.form.TextField( {
		name : "custom_formatting",
		width : 295,
		value : initialCustomFmt,
		enableKeyEvents : true
	});
	customFormatting.on("keyup", function() {
		var tmpFMT = this.getValue();
		formatString(tmpFMT, rawCellValue)
	});
	var formatNegativeNum = function(index) {
		NFAttrib3 = index;
		var nnf = numberNegativeFormat[index][0];
		switch (nnf) {
		case "normal":
			break;
		case "red":
			cellFormattingString = cellFormattingString.concat(";[Red]",
					cellFormattingString);
			break;
		case "negative":
			cellFormattingString = cellFormattingString.concat("_);(",
					cellFormattingString, ")");
			break;
		case "red_negative":
			cellFormattingString = cellFormattingString.concat("_);[Red](",
					cellFormattingString, ")");
			break
		}
		formatString(cellFormattingString, rawCellValue)
	};
	var numberTabContainer = {
		border : false,
		frame : false,
		autoHeight : true,
		baseCls : "x-title-f",
		header : false,
		items : [
				{
					width : 162,
					bodyStyle : "margin-bottom: 5px; padding: 0px; background-color: transparent;",
					layout : "form",
					border : false,
					items : [ decPlacesSpinner ]
				},
				new Ext.form.Checkbox( {
					fieldLabel : "Active".localize(),
					name : "th_separator",
					id : "th_separator",
					checked : NFAttrib2,
					boxLabel : "Use 1000 Separator (.)".localize(),
					listeners : {
						check : {
							fn : function() {
								NFAttrib2 = this.getValue();
								if (NFAttrib3 > 0) {
									cellFormattingString = cellFormattingString
											.split(";")[0].replace(/_\)/i, "")
								}
								if (NFAttrib2) {
									cellFormattingString = "#,##"
											.concat(cellFormattingString)
								} else {
									cellFormattingString = cellFormattingString
											.replace(/#,##/i, "")
								}
								if (NFAttrib3 > 0) {
									formatNegativeNum(NFAttrib3)
								} else {
									formatString(cellFormattingString,
											rawCellValue)
								}
							}
						}
					}
				}),
				{
					html : "Negative numbers".localize().concat(":"),
					border : false,
					bodyStyle : "margin-bottom: 3px; margin-top: 9px; background-color: transparent;"
				},
				new Ext.DataView( {
					itemSelector : ".row-modeller",
					style : "overflow:auto",
					id : "negativeNumberView1",
					width : 295,
					height : 150,
					singleSelect : true,
					store : numberNegativeStore,
					cls : "listDataViewSelect",
					tpl : new Ext.XTemplate(
							'<div class="border-neg"><tpl for=".">',
							'<div class="row-modeller">', "{num_format}</div>",
							"</tpl></div>"),
					listeners : {
						click : function(dView, index, node, ev) {
							if (NFAttrib3 > 0) {
								cellFormattingString = cellFormattingString
										.split(";")[0].replace(/_\)/i, "")
							}
							formatNegativeNum(index)
						}
					}
				}) ]
	};
	var cardPanelNumber = new Ext.Panel(
			{
				id : "main_format_content",
				layout : "card",
				deferredRender : true,
				width : 325,
				bodyStyle : "background-color: transparent;",
				defaults : {
					bodyStyle : "background-color: transparent; padding: 0px;"
				},
				border : false,
				activeItem : initNumCard,
				region : "center",
				items : [
						{
							html : "General format cells have no formatting."
									.localize(),
							baseCls : "x-plain"
						},
						numberTabContainer,
						{
							id : "curency",
							border : false,
							frame : false,
							autoHeight : true,
							header : false,
							baseCls : "x-title-f",
							items : [
									{
										width : 162,
										bodyStyle : "margin-bottom: 5px; padding: 0px; background-color: transparent;",
										layout : "form",
										border : false,
										items : [ decPlacesSpinner2 ]
									},
									{
										layout : "form",
										baseCls : "x-plain",
										autoWidth : true,
										items : new Ext.form.ComboBox(
												{
													fieldLabel : "Symbol"
															.localize(),
													store : currencyListStore,
													displayField : "cdesc",
													hideLabel : false,
													labelSeparator : ":",
													id : "currencyCombo1",
													editable : false,
													value : (NFAttrib2 != false && currencyListStore
															.find("csymbol",
																	NFAttrib2) > 0) ? currency_list[currencyListStore
															.find("csymbol",
																	NFAttrib2)][1]
															: this.emptyText,
													typeAhead : true,
													mode : "local",
													triggerAction : "all",
													listeners : {
														select : {
															fn : function(el,
																	e, index) {
																var tmpVal = decPlacesSpinner2
																		.getValue();
																var fmtStr = (tmpVal == 0) ? "#,##0"
																		: "#,##0.";
																for (i = 0; i < tmpVal; i++) {
																	fmtStr = fmtStr
																			.concat("0")
																}
																fmtStr = fmtStr
																		.concat(
																				" ",
																				currency_list[index][2]);
																NFAttrib2 = currency_list[index][2];
																cellFormattingString = fmtStr;
																if (NFAttrib3 > 0) {
																	formatNegativeNum(NFAttrib3)
																} else {
																	formatString(
																			cellFormattingString,
																			rawCellValue)
																}
															},
															scope : this
														}
													},
													emptyText : "None"
															.localize(),
													selectOnFocus : true,
													listWidth : 188,
													width : 173,
													maxHeight : 100
												})
									},
									{
										html : "Negative numbers".localize()
												.concat(":"),
										border : false,
										bodyStyle : "margin-bottom: 3px; margin-top: 1px; background-color: transparent;"
									},
									new Ext.DataView(
											{
												itemSelector : ".row-modeller",
												style : "overflow:auto",
												id : "negativeNumberView2",
												width : 295,
												height : 150,
												singleSelect : true,
												bodyStyle : "margin-bopttom: 0px;",
												store : numberNegativeStore,
												cls : "listDataViewSelect",
												tpl : new Ext.XTemplate(
														'<div class="border-neg"><tpl for=".">',
														'<div class="row-modeller">',
														"{num_format}</div>",
														"</tpl></div>"),
												listeners : {
													click : function(dView,
															index, node, ev) {
														if (NFAttrib3 > 0) {
															cellFormattingString = cellFormattingString
																	.split(";")[0]
																	.replace(
																			/_\)/i,
																			"")
														}
														formatNegativeNum(index)
													}
												}
											}) ]
						},
						{
							id : "accounting",
							border : false,
							frame : false,
							autoHeight : true,
							header : false,
							baseCls : "x-title-f",
							items : [
									{
										width : 162,
										bodyStyle : "margin-bottom: 5px; padding: 0px; background-color: transparent;",
										layout : "form",
										border : false,
										items : [ decPlacesSpinner3 ]
									},
									{
										layout : "form",
										baseCls : "x-plain",
										autoWidth : true,
										items : new Ext.form.ComboBox(
												{
													fieldLabel : "Symbol"
															.localize(),
													store : currencyListStore,
													displayField : "cdesc",
													id : "currencyCombo2",
													hideLabel : false,
													labelSeparator : ":",
													value : (NFAttrib2 != false && currencyListStore
															.find("csymbol",
																	NFAttrib2) > 0) ? currency_list[currencyListStore
															.find("csymbol",
																	NFAttrib2)][1]
															: this.emptyText,
													editable : false,
													typeAhead : true,
													mode : "local",
													triggerAction : "all",
													listeners : {
														select : {
															fn : function(el,
																	e, index) {
																var tmpVal = decPlacesSpinner3
																		.getValue();
																var fmtStr = (tmpVal == 0) ? "#,##0"
																		: "#,##0.";
																for (i = 0; i < tmpVal; i++) {
																	fmtStr = fmtStr
																			.concat("0")
																}
																fmtStr = fmtStr
																		.concat(
																				" ",
																				currency_list[index][2]);
																NFAttrib2 = currency_list[index][2];
																formatString(
																		fmtStr,
																		rawCellValue)
															},
															scope : this
														}
													},
													emptyText : "None"
															.localize(),
													selectOnFocus : true,
													listWidth : 188,
													width : 173,
													maxHeight : 100
												})
									} ]
						},
						{
							id : "date",
							border : false,
							frame : false,
							autoHeight : true,
							baseCls : "x-title-f",
							header : false,
							items : [
									{
										html : "Type:",
										border : false,
										bodyStyle : "margin-bottom:2px; margin-top: 5px; background-color: transparent;"
									},
									new Ext.Panel(
											{
												border : true,
												width : 295,
												autoHeight : true,
												layout : "fit",
												items : new Ext.DataView(
														{
															itemSelector : ".DVSelector",
															style : "overflow:auto",
															overClass : "x-view-over",
															width : 293,
															height : 125,
															singleSelect : true,
															border : true,
															bodyStyle : "margin-bopttom: 15px;",
															store : dateTypeStore,
															listeners : {
																click : {
																	fn : function(
																			dView,
																			index,
																			node,
																			ev) {
																		var tmpFS = dateType[index][2];
																		cellFormattingString = tmpFS;
																		formatString(
																				cellFormattingString,
																				rawCellValue)
																	},
																	scope : this
																}
															},
															cls : "listDataViewSelect",
															tpl : new Ext.XTemplate(
																	'<div class="border-list"><tpl for=".">',
																	'<div class="DVSelector">',
																	"{date_format}</div>",
																	"</tpl></div>")
														})
											}) ]
						},
						{
							id : "time",
							border : false,
							frame : false,
							autoHeight : true,
							baseCls : "x-title-f",
							header : false,
							items : [
									{
										html : "Type".localize().concat(":"),
										border : false,
										bodyStyle : "margin-bottom:2px; margin-top: 5px; background-color: transparent;"
									},
									new Ext.Panel(
											{
												border : true,
												width : 295,
												autoHeight : true,
												layout : "fit",
												items : new Ext.DataView(
														{
															itemSelector : ".DVSelector",
															style : "overflow:auto",
															overClass : "x-view-over",
															width : 293,
															height : 125,
															singleSelect : true,
															border : true,
															bodyStyle : "margin-bopttom: 15px;",
															store : timeTypeStore,
															listeners : {
																click : {
																	fn : function(
																			dView,
																			index,
																			node,
																			ev) {
																		var tmpFS = timeType[index][2];
																		cellFormattingString = tmpFS;
																		formatString(
																				cellFormattingString,
																				rawCellValue)
																	},
																	scope : this
																}
															},
															cls : "listDataViewSelect",
															tpl : new Ext.XTemplate(
																	'<div class="border-list"><tpl for=".">',
																	'<div class="DVSelector">',
																	"{time_format}</div>",
																	"</tpl></div>")
														})
											}) ]
						},
						{
							id : "percentage",
							border : false,
							items : [ {
								width : 162,
								bodyStyle : "margin-bottom: 5px; padding: 0px; background-color: transparent;",
								layout : "form",
								border : false,
								items : [ decPlacesSpinner4 ]
							} ]
						},
						{
							id : "fraction",
							border : false,
							frame : false,
							autoHeight : true,
							baseCls : "x-title-f",
							header : false,
							items : [
									{
										html : "Type".localize().concat(":"),
										border : false,
										bodyStyle : "margin-bottom:2px; margin-top: 5px; background-color: transparent;"
									},
									new Ext.Panel(
											{
												border : true,
												width : 295,
												autoHeight : true,
												layout : "fit",
												items : new Ext.DataView(
														{
															itemSelector : ".DVSelector",
															style : "overflow:auto",
															overClass : "x-view-over",
															width : 293,
															height : 125,
															singleSelect : true,
															border : true,
															listeners : {
																click : {
																	fn : function(
																			dView,
																			index,
																			node,
																			ev) {
																		var tmpFS = fractionType[index][2];
																		cellFormattingString = tmpFS;
																		formatString(
																				cellFormattingString,
																				rawCellValue)
																	},
																	scope : this
																}
															},
															bodyStyle : "margin-bopttom: 15px;",
															store : fractionTypeStore,
															cls : "listDataViewSelect",
															tpl : new Ext.XTemplate(
																	'<div class="border-list"><tpl for=".">',
																	'<div class="DVSelector">',
																	"{fraction_format}</div>",
																	"</tpl></div>")
														})
											}) ]
						},
						{
							id : "scientific",
							border : false,
							items : [ {
								width : 162,
								bodyStyle : "margin-bottom: 5px; padding: 0px; background-color: transparent;",
								layout : "form",
								border : false,
								items : [ decPlacesSpinner5 ]
							} ]
						},
						{
							id : "text",
							width : 290,
							html : "_catDescr: text".localize(),
							baseCls : "x-plain",
							border : false
						},
						{
							id : "special",
							border : false,
							frame : false,
							autoHeight : true,
							baseCls : "x-title-f",
							header : false,
							items : [
									{
										html : "Type".localize().concat(":"),
										border : false,
										bodyStyle : "margin-bottom:2px; margin-top: 5px; background-color: transparent;"
									},
									new Ext.Panel(
											{
												border : true,
												width : 295,
												autoHeight : true,
												layout : "fit",
												items : new Ext.DataView(
														{
															itemSelector : ".row-modeller",
															style : "overflow:auto",
															width : 293,
															height : 125,
															singleSelect : true,
															border : true,
															bodyStyle : "margin-bopttom: 15px;",
															cls : "listDataViewSelect",
															tpl : new Ext.XTemplate(
																	'<div class="border-list"><tpl for=".">',
																	'<div class="row-modeller">',
																	"{time_format}</div>",
																	"</tpl></div>")
														})
											}) ]
						},
						{
							id : "custom",
							border : false,
							frame : false,
							autoHeight : true,
							header : false,
							items : [
									{
										html : "Type".localize().concat(":"),
										baseCls : "x-plain",
										bodyStyle : "margin-bottom: 2px;"
									},
									customFormatting,
									new Ext.Panel(
											{
												border : true,
												width : 295,
												autoHeight : true,
												layout : "fit",
												bodyStyle : "margin-top: 2px;",
												items : new Ext.DataView(
														{
															itemSelector : ".row-modeller",
															style : "overflow:auto",
															width : 293,
															height : 155,
															singleSelect : true,
															border : true,
															cls : "listDataViewSelect",
															tpl : new Ext.XTemplate(
																	'<div class="border-list"><tpl for=".">',
																	'<div class="row-modeller">',
																	"{time_format}</div>",
																	"</tpl></div>")
														}),
												buttons : [ {
													text : "Delete".localize()
												} ]
											}) ]
						} ]
			});
	var mainField = new Ext.Panel( {
		baseCls : "x-plain",
		width : 295,
		bodyStyle : "margin-top: 7px;",
		items : [ new Ext.Panel( {
			border : false,
			frame : false,
			autoHeight : true,
			id : "sample",
			header : false,
			baseCls : "x-title-f",
			items : [ {
				width : 295,
				xtype : "fieldset",
				labelAlign : "left",
				bodyStyle : "padding-top: 0px; background-color: transparent;",
				layout : "form",
				border : true,
				autoHeight : true,
				title : "Sample".localize(),
				items : [ sampleDataView ]
			} ]
		}), cardPanelNumber ]
	});
	var categorySelector = new Ext.DataView( {
		itemSelector : ".list-selector",
		style : "overflow:auto",
		overClass : "x-view-over",
		autoWidth : true,
		singleSelect : true,
		store : cellNumberFormatStore,
		cls : "listDataViewSelect",
		tpl : new Ext.XTemplate('<div class="borderr"><tpl for=".">',
				'<div class="list-selector">',
				"<span>&#160;{catlabel}</span></div>", "</tpl></div>"),
		listeners : {
			click : {
				fn : onCategorySelect,
				scope : this
			}
		}
	});
	var numberTab = new Ext.Panel(
			{
				baseCls : "x-title-f",
				labelWidth : 100,
				labelAlign : "left",
				frame : false,
				bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
				header : false,
				monitorValid : true,
				autoHeight : true,
				autoWidth : true,
				items : [
						new Ext.Panel(
								{
									id : "numberTab",
									layout : "column",
									border : false,
									width : 437,
									bodyStyle : "background-color: transparent; padding-left: 0px;",
									items : [
											{
												columnWidth : 0.3,
												layout : "form",
												id : "fcdlg-num-cat-sel",
												labelAlign : "left",
												xtype : "fieldset",
												baseCls : "x-title-f",
												title : "Category".localize()
														.concat(":"),
												defaults : {
													autowidth : true
												},
												autoHeight : true,
												bodyStyle : "padding-top:0px;",
												border : false,
												items : [ categorySelector ]
											},
											{
												columnWidth : 0.7,
												layout : "form",
												autoHeight : true,
												bodyStyle : "margin-left: 10px; background-color: transparent; padding: 0px;",
												border : false,
												items : [ mainField ]
											} ]
								}), numDescription ],
				listeners : {
					doFormatNumber : function(callback) {
						if ((cellFormattingString != preFormattingString[0])
								|| (initNumCard != preFormattingString[1])
								|| (NFAttrib1 != preFormattingString[2])
								|| (NFAttrib2 != preFormattingString[3])
								|| (NFAttrib3 != preFormattingString[4])) {
							if (isFromOther) {
								callback( [ cellFormattingString, false ])
							} else {
								Jedox.wss.app.activeBook.setRangeFormat( [
										rngStartCoord[0], rngStartCoord[1],
										rngEndCoord[0], rngEndCoord[1] ], [
										cellFormattingString, indexMarker,
										NFAttrib1, NFAttrib2, NFAttrib3 ])
							}
						}
					}
				}
			});
	callback(numberTab);
	setTimeout(function() {
		categorySelector.select(initNumCard, false, false);
		if (initNumCard == 1 || initNumCard == 2) {
			Ext.getCmp("negativeNumberView".concat(initNumCard)).select(
					NFAttrib3, false, true)
		}
	}, 20);
	initNumCard = preFormattingString[1];
	indexMarker = initNumCard;
	NFAttrib1 = preFormattingString[2];
	NFAttrib2 = preFormattingString[3];
	NFAttrib3 = preFormattingString[4];
	cellFormattingString = preFormattingString[0]
};