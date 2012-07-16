Jedox.wss.dialog.format.border = function(callback, _pre, toDisable,
		isFromOther) {
	var _config = {
		winW : 300,
		winH : 350,
		winWhiskerH : 210,
		winPieH : 180,
		colorButtonW : "50px",
		labelWidth : 60,
		textBoxW : 180,
		comboBoxW : 200,
		rowH : 25,
		margingSize : 2,
		posColor : "#000000",
		brdColor : "#000000"
	};
	var state = {
		colorPos : _config.posColor,
		colorBrd : _config.brdColor
	};
	function _setupColorButton2(colorBtn, color, setWidth) {
		if (color == null) {
			color = "000000"
		}
		var tmpElem = Ext.DomQuery
				.selectNode("*[id=" + colorBtn.btnEl.id + "]");
		if (tmpElem == null) {
			borderColorButton.style.background = color
		} else {
			tmpElem.style.background = color
		}
		tmpElem.style.width = "110px";
		tmpElem.style.height = "15px"
	}
	var borderColorStyle = "000000";
	var borderStyleList = [
			[ "none", '<div style="line-height: 10px;">None</div>' ],
			[
					"solid",
					'<div style="line-height: 2px; border-top: 1px solid '
							.concat(
									"#",
									borderColorStyle,
									';">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>') ],
			[
					"double",
					'<div style="line-height: 2px;border-top: 2px double '
							.concat(
									"#",
									borderColorStyle,
									';">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>') ],
			[
					"dotted",
					'<div style="line-height: 2px;border-top: 1px dotted '
							.concat(
									"#",
									borderColorStyle,
									';">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>') ],
			[
					"dashed",
					'<div style="line-height: 2px;border-top: 1px dashed '
							.concat(
									"#",
									borderColorStyle,
									';">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>') ] ];
	var borderStyleListStore = new Ext.data.SimpleStore( {
		fields : [ "style", "description" ],
		data : borderStyleList
	});
	var borderField = [
			[
					false,
					"top",
					'<div style="position:absolute; top:0px; left:0px; width: 146px; height: 20px;"></div>' ],
			[
					false,
					"left",
					'<div style="position:absolute; top:0px; left:0px; width: 20px; height: 94px; "></div>' ],
			[
					false,
					"right",
					'<div style="position:absolute; top:0px; left:126px; width: 20px; height: 94px;"></div>' ],
			[
					false,
					"bottom",
					'<div style="position:absolute; top:74px; left:0px; width: 146px; height: 20px"></div>' ] ];
	var borderField2x2 = [
			[
					false,
					"top",
					'<div style="position:absolute; top:0px; left:0px; width: 146px; height: 20px;"></div>' ],
			[
					false,
					"left",
					'<div style="position:absolute; top:0px; left:0px; width: 20px; height: 94px; "></div>' ],
			[
					false,
					"right",
					'<div style="position:absolute; top:0px; left:126px; width: 20px; height: 94px;"></div>' ],
			[
					false,
					"bottom",
					'<div style="position:absolute; top:74px; left:0px; width: 146px; height: 20px"></div>' ],
			[
					false,
					"middle-hor",
					'<div style="position:absolute; top:38px; left:0px; width: 146px; height: 20px;"></div>' ],
			[
					false,
					"middle-vert",
					'<div style="position:absolute; top:0px; left:64px; width: 20px; height: 94px; "></div>' ] ];
	var borderFieldCol = [
			[
					false,
					"top",
					'<div style="position:absolute; top:0px; left:0px; width: 146px; height: 20px;"></div>' ],
			[
					false,
					"left",
					'<div style="position:absolute; top:0px; left:0px; width: 20px; height: 94px; "></div>' ],
			[
					false,
					"right",
					'<div style="position:absolute; top:0px; left:126px; width: 20px; height: 94px;"></div>' ],
			[
					false,
					"bottom",
					'<div style="position:absolute; top:74px; left:0px; width: 146px; height: 20px"></div>' ],
			[
					false,
					"middle-hor",
					'<div style="position:absolute; top:38px; left:0px; width: 146px; height: 20px;"></div>' ] ];
	var borderFieldRow = [
			[
					false,
					"top",
					'<div style="position:absolute; top:0px; left:0px; width: 146px; height: 20px;"></div>' ],
			[
					false,
					"left",
					'<div style="position:absolute; top:0px; left:0px; width: 20px; height: 94px; "></div>' ],
			[
					false,
					"right",
					'<div style="position:absolute; top:0px; left:126px; width: 20px; height: 94px;"></div>' ],
			[
					false,
					"bottom",
					'<div style="position:absolute; top:74px; left:0px; width: 146px; height: 20px"></div>' ],
			[
					false,
					"middle-vert",
					'<div style="position:absolute; top:0px; left:64px; width: 20px; height: 94px; "></div>' ] ];
	var startCell = Jedox.wss.app.environment.lastRangeStartCoord;
	var endCell = Jedox.wss.app.environment.lastRangeEndCoord;
	var xCoordSC = startCell[0];
	var yCoordSC = startCell[1];
	var xCoordEC = endCell[0];
	var yCoordEC = endCell[1];
	var borderFieldVar = "borderField";
	var borderBgStyle = "border-choose-field";
	var isDisabledMidHor = false;
	var isDisabledMidVer = false;
	var isDisabledInner = false;
	if ((xCoordEC - xCoordSC == 0) && (yCoordEC - yCoordSC == 0)) {
		borderFieldVar = borderField;
		borderBgStyle = "border-choose-field";
		isDisabledMidHor = true;
		isDisabledMidVer = true;
		isDisabledInner = true
	} else {
		if ((xCoordEC - xCoordSC == 0) && (yCoordEC - yCoordSC > 0)) {
			borderFieldVar = borderFieldCol;
			borderBgStyle = "border-choose-field-col";
			isDisabledMidVer = true;
			isDisabledMidHor = false
		} else {
			if ((xCoordEC - xCoordSC > 0) && (yCoordEC - yCoordSC == 0)) {
				borderFieldVar = borderFieldRow;
				borderBgStyle = "border-choose-field-row";
				isDisabledMidHor = true;
				isDisabledMidVer = false
			} else {
				if ((xCoordEC - xCoordSC > 0) && (yCoordEC - yCoordSC > 0)) {
					borderFieldVar = borderField2x2;
					borderBgStyle = "border-choose-field-2x2";
					isDisabledMidHor = false;
					isDisabledMidVer = false
				}
			}
		}
	}
	var borderFieldStore = new Ext.data.SimpleStore( {
		fields : [ "trigger", "desc", "div" ],
		data : borderFieldVar
	});
	var topBorderProp = false;
	var bottomBorderProp = false;
	var leftBorderProp = false;
	var rightBorderProp = false;
	var midHorBorderProp = false;
	var midVerBorderProp = false;
	var cellBorderStyle = "solid";
	var borderThickness = "1px";
	var borderStyleSelector = function(dView, index, node, ev) {
		if (index == 0) {
			cellBorderStyle = "none";
			borderThickness = "0px"
		} else {
			if (index == 1) {
				cellBorderStyle = "solid";
				borderThickness = "1px"
			} else {
				if (index == 2) {
					cellBorderStyle = "solid";
					borderThickness = "2px"
				} else {
					if (index == 3) {
						cellBorderStyle = "dotted";
						borderThickness = "1px"
					} else {
						if (index == 4) {
							cellBorderStyle = "dashed";
							borderThickness = "1px"
						}
					}
				}
			}
		}
	};
	var BorderTrueProp = new Ext.data.Record.create( [ {
		name : "trigger"
	}, {
		name : "desc"
	}, {
		name : "div"
	} ]);
	if (!isFromOther) {
		var preselectedBorders = Jedox.wss.style.getBorder()
	} else {
		var preselectedBorders = _pre
	}
	if (!preselectedBorders.top) {
		preselectedBorders.top = {
			width : "",
			type : "",
			color : ""
		}
	}
	if (!preselectedBorders.bottom) {
		preselectedBorders.bottom = {
			width : "",
			type : "",
			color : ""
		}
	}
	if (!preselectedBorders.left) {
		preselectedBorders.left = {
			width : "",
			type : "",
			color : ""
		}
	}
	if (!preselectedBorders.right) {
		preselectedBorders.right = {
			width : "",
			type : "",
			color : ""
		}
	}
	if (!preselectedBorders.ins_vert) {
		preselectedBorders.ins_vert = {
			width : "",
			type : "",
			color : ""
		}
	}
	if (!preselectedBorders.ins_horiz) {
		preselectedBorders.ins_horiz = {
			width : "",
			type : "",
			color : ""
		}
	}
	var borders = {
		top : {
			dim : preselectedBorders.top.width,
			style : preselectedBorders.top.type,
			color : preselectedBorders.top.color
		},
		bottom : {
			dim : preselectedBorders.bottom.width,
			style : preselectedBorders.bottom.type,
			color : preselectedBorders.bottom.color
		},
		left : {
			dim : preselectedBorders.left.width,
			style : preselectedBorders.left.type,
			color : preselectedBorders.left.color
		},
		right : {
			dim : preselectedBorders.right.width,
			style : preselectedBorders.right.type,
			color : preselectedBorders.right.color
		},
		midVer : {
			dim : preselectedBorders.ins_vert.width,
			style : preselectedBorders.ins_vert.type,
			color : preselectedBorders.ins_vert.color
		},
		midHor : {
			dim : preselectedBorders.ins_horiz.width,
			style : preselectedBorders.ins_horiz.type,
			color : preselectedBorders.ins_horiz.color
		}
	};
	var topBorderHandler = function() {
		if (!topBorderProp) {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "top",
						div : '<div style="position:absolute; top:0px; left:0px; width: 146px; height: 20px;"><div style="margin-top: 9px; margin-left: 9px; margin-right: 9px; border-top: '
								.concat(borderThickness, " ", cellBorderStyle,
										" #", borderColorStyle,
										';"></div></div>')
					});
			borderTopButton.toggle(true, true);
			borderFieldStore.insert(0, tmpRec);
			topBorderProp = true;
			borders.top.dim = borderThickness;
			borders.top.style = cellBorderStyle;
			borders.top.color = borderColorStyle
		} else {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "top",
						div : '<div style="position:absolute; top:0px; left:0px; width: 146px; height: 20px;"></div>'
					});
			borderTopButton.toggle(false, true);
			borderFieldStore.insert(0, tmpRec);
			topBorderProp = false;
			borders.top.dim = "0px";
			borders.top.style = "none";
			borders.top.color = ""
		}
		deleRec = borderFieldStore.getAt(1);
		borderFieldStore.remove(deleRec);
		borderDataView.refresh()
	};
	var midHorBorderHandler = function() {
		if (!midHorBorderProp) {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "mid-hor",
						div : '<div style="position:absolute; top:38px; left:0px; width: 146px; height: 20px;"><div style="margin-top: 9px; margin-left: 9px; margin-right: 9px; border-top: '
								.concat(borderThickness, " ", cellBorderStyle,
										" #", borderColorStyle,
										';"></div></div>')
					});
			borderMidHorButton.toggle(true, true);
			borderFieldStore.insert(4, tmpRec);
			midHorBorderProp = true;
			borders.midHor.dim = borderThickness;
			borders.midHor.style = cellBorderStyle;
			borders.midHor.color = borderColorStyle
		} else {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "mid-hor",
						div : '<div style="position:absolute; top:38px; left:0px; width: 146px; height: 20px;"></div>'
					});
			borderMidHorButton.toggle(false, true);
			borderFieldStore.insert(4, tmpRec);
			midHorBorderProp = false;
			borders.midHor.dim = "0px";
			borders.midHor.style = "none";
			borders.midHor.color = ""
		}
		deleRec = borderFieldStore.getAt(5);
		borderFieldStore.remove(deleRec);
		borderDataView.refresh()
	};
	var midVerBorderHandler = function() {
		idVertPos = borderFieldStore.getCount();
		if (!midVerBorderProp) {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "mid-ver",
						div : '<div style="position:absolute; top:0px; left:64px; width: 20px; height: 94px;"><div style="margin-top: 9px; margin-left: 9px; border-left: '
								.concat(borderThickness, " ", cellBorderStyle,
										" #", borderColorStyle,
										'; height: 76px;"></div></div>')
					});
			borderMidVerButton.toggle(true, true);
			borderFieldStore.insert(idVertPos - 1, tmpRec);
			midVerBorderProp = true;
			borders.midVer.dim = borderThickness;
			borders.midVer.style = cellBorderStyle;
			borders.midVer.color = borderColorStyle
		} else {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "mid-ver",
						div : '<div style="position:absolute; top:0px; left:64px; width: 20px; height: 94px;"></div>'
					});
			borderMidVerButton.toggle(false, true);
			borderFieldStore.insert(idVertPos - 1, tmpRec);
			midVerBorderProp = false;
			borders.midVer.dim = "0px";
			borders.midVer.style = "none";
			borders.midVer.color = ""
		}
		deleRec = borderFieldStore.getAt(idVertPos);
		borderFieldStore.remove(deleRec);
		borderDataView.refresh()
	};
	var bottomBorderHandler = function() {
		if (!bottomBorderProp) {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "top",
						div : '<div style="position:absolute; top:74px; left:0px; width: 146px; height: 20px"><div style="margin-top: 11px; margin-left: 9px; margin-right: 9px; border-top: '
								.concat(borderThickness, " ", cellBorderStyle,
										" #", borderColorStyle,
										';"></div></div>')
					});
			borderBottomButton.toggle(true, true);
			borderFieldStore.insert(3, tmpRec);
			bottomBorderProp = true;
			borders.bottom.dim = borderThickness;
			borders.bottom.style = cellBorderStyle;
			borders.bottom.color = borderColorStyle
		} else {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "top",
						div : '<div style="position:absolute; top:74px; left:0px; width: 146px; height: 20px"></div>'
					});
			borderBottomButton.toggle(false, true);
			borderFieldStore.insert(3, tmpRec);
			bottomBorderProp = false;
			borders.bottom.dim = "0px";
			borders.bottom.style = "none";
			borders.bottom.color = ""
		}
		deleRec = borderFieldStore.getAt(4);
		borderFieldStore.remove(deleRec);
		borderDataView.refresh()
	};
	var leftBorderHandler = function() {
		if (!leftBorderProp) {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "top",
						div : '<div style="position:absolute; top:0px; left:0px; width: 20px; height: 94px; "><div style="margin-top: 9px; margin-left: 9px; border-left: '
								.concat(borderThickness, " ", cellBorderStyle,
										" #", borderColorStyle,
										'; height: 76px;"></div></div>')
					});
			borderLeftButton.toggle(true, true);
			borderFieldStore.insert(1, tmpRec);
			leftBorderProp = true;
			borders.left.dim = borderThickness;
			borders.left.style = cellBorderStyle;
			borders.left.color = borderColorStyle
		} else {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "top",
						div : '<div style="position:absolute; top:0px; left:0px; width: 20px; height: 94px; "></div>'
					});
			borderLeftButton.toggle(false, true);
			borderFieldStore.insert(1, tmpRec);
			leftBorderProp = false;
			borders.left.dim = "0px";
			borders.left.style = "none";
			borders.left.color = ""
		}
		deleRec = borderFieldStore.getAt(2);
		borderFieldStore.remove(deleRec);
		borderDataView.refresh()
	};
	var rightBorderHandler = function() {
		if (!rightBorderProp) {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "top",
						div : '<div style="position:absolute; top:0px; left:126px; width: 20px; height: 94px;"><div style="margin-top: 9px; margin-left: 11px; border-left: '
								.concat(borderThickness, " ", cellBorderStyle,
										" #", borderColorStyle,
										'; height: 76px;"></div></div>')
					});
			borderRightButton.toggle(true, true);
			borderFieldStore.insert(2, tmpRec);
			rightBorderProp = true;
			borders.right.dim = borderThickness;
			borders.right.style = cellBorderStyle;
			borders.right.color = borderColorStyle
		} else {
			tmpRec = new BorderTrueProp(
					{
						trigger : true,
						desc : "top",
						div : '<div style="position:absolute; top:0px; left:126px; width: 20px; height: 94px;"></div>'
					});
			borderRightButton.toggle(false, true);
			borderFieldStore.insert(2, tmpRec);
			rightBorderProp = false;
			borders.right.dim = "0px";
			borders.right.style = "none";
			borders.right.color = ""
		}
		deleRec = borderFieldStore.getAt(3);
		borderFieldStore.remove(deleRec);
		borderDataView.refresh()
	};
	var allBorderHandler = function(dView, index, node, ev) {
		if (borderBgStyle == "border-choose-field-2x2") {
			if (index == 0) {
				topBorderHandler()
			} else {
				if (index == 1) {
					leftBorderHandler()
				} else {
					if (index == 2) {
						rightBorderHandler()
					} else {
						if (index == 3) {
							bottomBorderHandler()
						} else {
							if (index == 4) {
								midHorBorderHandler()
							} else {
								if (index == 5) {
									midVerBorderHandler()
								}
							}
						}
					}
				}
			}
		} else {
			if (borderBgStyle == "border-choose-field-col") {
				if (index == 0) {
					topBorderHandler()
				} else {
					if (index == 1) {
						leftBorderHandler()
					} else {
						if (index == 2) {
							rightBorderHandler()
						} else {
							if (index == 3) {
								bottomBorderHandler()
							} else {
								if (index == 4) {
									midHorBorderHandler()
								}
							}
						}
					}
				}
			} else {
				if (borderBgStyle == "border-choose-field-row") {
					if (index == 0) {
						topBorderHandler()
					} else {
						if (index == 1) {
							leftBorderHandler()
						} else {
							if (index == 2) {
								rightBorderHandler()
							} else {
								if (index == 3) {
									bottomBorderHandler()
								} else {
									if (index == 4) {
										midVerBorderHandler()
									}
								}
							}
						}
					}
				} else {
					if (borderBgStyle == "border-choose-field") {
						if (index == 0) {
							topBorderHandler()
						} else {
							if (index == 1) {
								leftBorderHandler()
							} else {
								if (index == 2) {
									rightBorderHandler()
								} else {
									if (index == 3) {
										bottomBorderHandler()
									}
								}
							}
						}
					}
				}
			}
		}
	};
	var colorSelectView = new Ext.DataView( {
		itemSelector : ".border-chooser",
		style : "overflow:auto",
		width : 130,
		height : 140,
		singleSelect : true,
		store : borderStyleListStore,
		cls : "borderStyleSelect",
		listeners : {
			click : {
				fn : borderStyleSelector,
				scope : this
			}
		},
		tpl : new Ext.XTemplate('<div class="border-style"><tpl for=".">',
				'<div class="border-chooser">', "{description}</div>",
				"</tpl></div>")
	});
	var borderDataView = new Ext.DataView( {
		id : "borderDataView",
		itemSelector : ".border-field-chooser",
		style : "overflow:auto",
		width : 150,
		height : 100,
		singleSelect : false,
		store : borderFieldStore,
		cls : "borderStyleSelect",
		listeners : {
			click : {
				fn : allBorderHandler,
				scope : this
			}
		},
		tpl : new Ext.XTemplate('<div class="'.concat(borderBgStyle,
				'"><tpl for=".">'), '<div class="border-field-chooser">',
				"{div}</div>", "</tpl></div>")
	});
	var borderTopButton = new Ext.Button( {
		iconCls : "border-top",
		enableToggle : true,
		toggleHandler : topBorderHandler,
		cls : "x-btn-icon",
		style : "margin-bottom: 15px;"
	});
	var borderMidHorButton = new Ext.Button( {
		iconCls : "border-middle-h",
		enableToggle : true,
		disabled : isDisabledMidHor,
		toggleHandler : midHorBorderHandler,
		cls : "x-btn-icon",
		style : "margin-bottom: 15px;"
	});
	var borderBottomButton = new Ext.Button( {
		iconCls : "border-bottom",
		enableToggle : true,
		toggleHandler : bottomBorderHandler,
		cls : "x-btn-icon"
	});
	var borderLeftButton = new Ext.Button( {
		iconCls : "border-left",
		enableToggle : true,
		toggleHandler : leftBorderHandler,
		cls : "x-btn-icon",
		style : "margin-bottom: 15px;"
	});
	var borderRightButton = new Ext.Button( {
		iconCls : "border-right",
		enableToggle : true,
		toggleHandler : rightBorderHandler,
		cls : "x-btn-icon",
		style : "margin-bottom: 15px;"
	});
	var borderMidVerButton = new Ext.Button( {
		iconCls : "border-middle-v",
		enableToggle : true,
		disabled : isDisabledMidVer,
		toggleHandler : midVerBorderHandler,
		cls : "x-btn-icon",
		style : "margin-bottom: 15px;"
	});
	var borderColorButton = new Ext.Toolbar.SplitButton(
			{
				minWidth : 130,
				menu : new Ext.menu.ColorMenu(
						{
							colors : Jedox.wss.style.colorPalettes[Jedox.wss.app.activeColorPalette],
							cls : "wide-color-palette",
							listeners : {
								select : function(colorP, colorStr) {
									state.colorBrd = "#" + colorStr;
									_setupColorButton2(borderColorButton,
											state.colorBrd, false);
									borderColorStyle = colorStr;
									var borderStyleTmpList = new Ext.data.Record.create(
											[ {
												name : "style"
											}, {
												name : "description"
											} ]);
									tmpRec0 = new borderStyleTmpList(
											{
												style : "none",
												description : '<div style="line-height: 10px;">None</div>'
											});
									tmpRec1 = new borderStyleTmpList(
											{
												style : "solid",
												description : '<div style="border-top: 1px solid '
														.concat(
																"#",
																borderColorStyle,
																';"></div>')
											});
									tmpRec2 = new borderStyleTmpList(
											{
												style : "double",
												description : '<div style="border-top: 2px double '
														.concat(
																"#",
																borderColorStyle,
																';"></div>')
											});
									tmpRec3 = new borderStyleTmpList(
											{
												style : "dotted",
												description : '<div style="border-top: 1px dotted '
														.concat(
																"#",
																borderColorStyle,
																';"></div>')
											});
									tmpRec4 = new borderStyleTmpList(
											{
												style : "dashed",
												description : '<div style="border-top: 1px dashed '
														.concat(
																"#",
																borderColorStyle,
																';"></div>')
											});
									borderStyleListStore.removeAll();
									borderStyleListStore.insert(0, tmpRec0);
									borderStyleListStore.insert(1, tmpRec1);
									borderStyleListStore.insert(2, tmpRec2);
									borderStyleListStore.insert(3, tmpRec3);
									borderStyleListStore.insert(4, tmpRec4);
									colorSelectView.refresh()
								},
								beforeshow : function(thisMenu) {
									thisMenu.palette.select(state.colorBrd)
								},
								show : function(thisMenu) {
									thisMenu.palette.select(state.colorBrd)
								}
							}
						})
			});
	var rendered = false;
	var borderTab = new Ext.Panel(
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
				listeners : {
					doFormatBorders : function(getF) {
						var format = genBordersConf();
						if (isFromOther) {
							getF(format)
						} else {
							Jedox.wss.style.setBorder(format)
						}
					},
					afterlayout : function(th, l) {
						if (!rendered) {
							setTimeout(function() {
								rendered = true;
								for ( var y in borders) {
									if ((borders[y].style != "")
											&& (borders[y].style != "none")) {
										borderThickness = borders[y].dim;
										cellBorderStyle = borders[y].style;
										borderColorStyle = borders[y].color;
										borderColorStyle = borderColorStyle
												.replace(/#/i, "");
										switch (y) {
										case "top":
											topBorderHandler();
											break;
										case "bottom":
											bottomBorderHandler();
											break;
										case "left":
											leftBorderHandler();
											break;
										case "right":
											rightBorderHandler();
											break;
										case "midHor":
											midHorBorderHandler();
											break;
										case "midVer":
											midVerBorderHandler();
											break
										}
									}
								}
								borderDataView.refresh()
							}, 300)
						}
					}
				},
				items : [
						new Ext.Panel(
								{
									id : "borderTab",
									layout : "column",
									baseCls : "x-plain",
									border : false,
									frame : false,
									items : [
											{
												columnWidth : 0.35,
												layout : "form",
												xtype : "fieldset",
												bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
												autoHeight : true,
												baseCls : "x-plain",
												border : false,
												frame : false,
												items : [ {
													bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
													border : true,
													autoHeight : true,
													xtype : "fieldset",
													layout : "form",
													frame : false,
													title : "Line".localize(),
													items : [ {
														html : "Style:",
														baseCls : "x-plain"
													}, colorSelectView, {
														html : "Color:",
														baseCls : "x-plain"
													}, borderColorButton ]
												} ]
											},
											{
												columnWidth : 0.65,
												layout : "form",
												bodyStyle : "padding-left: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
												autoHeight : true,
												baseCls : "x-plain",
												border : false,
												items : [
														{
															bodyStyle : "padding: 0px; color: #000000; font-size: 9pt; background-color: transparent;",
															border : true,
															autoHeight : true,
															xtype : "fieldset",
															layout : "form",
															frame : false,
															title : "Presets"
																	.localize(),
															items : [ new Ext.Panel(
																	{
																		id : "presets-border",
																		layout : "column",
																		bodyStyle : "padding-left: 45px; color: #000000; font-size: 9pt; background-color: transparent;",
																		baseCls : "x-plain",
																		border : false,
																		frame : false,
																		items : [
																				{
																					layout : "form",
																					xtype : "fieldset",
																					autoHeight : true,
																					border : false,
																					frame : false,
																					bodyStyle : "width: 54px; text-align: center; margin: 2px;",
																					items : [
																							new Ext.Button(
																									{
																										disabled : false,
																										scale : "large",
																										iconCls : "borders-none",
																										style : "margin-left: 6px;",
																										handler : function() {
																											topBorderProp = true;
																											bottomBorderProp = true;
																											leftBorderProp = true;
																											rightBorderProp = true;
																											if (borderBgStyle == "border-choose-field-2x2") {
																												midHorBorderProp = true;
																												midVerBorderProp = true;
																												midHorBorderHandler();
																												midVerBorderHandler()
																											} else {
																												if (borderBgStyle == "border-choose-field-col") {
																													midHorBorderProp = true;
																													midHorBorderHandler()
																												} else {
																													if (borderBgStyle == "border-choose-field-row") {
																														midVerBorderProp = true;
																														midVerBorderHandler()
																													}
																												}
																											}
																											topBorderHandler();
																											bottomBorderHandler();
																											leftBorderHandler();
																											rightBorderHandler()
																										}
																									}),
																							{
																								html : "None",
																								baseCls : "x-plain",
																								bodyStyle : "text-align: center; margin-top: 5px;"
																							} ]
																				},
																				{
																					bodyStyle : "width: 54px; text-align: center; margin: 2px;",
																					layout : "form",
																					xtype : "fieldset",
																					autoHeight : true,
																					border : false,
																					frame : false,
																					items : [
																							new Ext.Button(
																									{
																										disabled : false,
																										scale : "large",
																										iconCls : "borders-outline",
																										style : "margin-left: 6px;",
																										handler : function() {
																											topBorderProp = false;
																											bottomBorderProp = false;
																											leftBorderProp = false;
																											rightBorderProp = false;
																											topBorderHandler();
																											leftBorderHandler();
																											rightBorderHandler();
																											bottomBorderHandler()
																										}
																									}),
																							{
																								html : "Outline",
																								baseCls : "x-plain",
																								bodyStyle : "text-align: center; margin-top: 5px;"
																							} ]
																				},
																				{
																					bodyStyle : "width: 54px; text-align: center; margin: 2px;",
																					layout : "form",
																					xtype : "fieldset",
																					autoHeight : true,
																					border : false,
																					frame : false,
																					items : [
																							new Ext.Button(
																									{
																										disabled : false,
																										scale : "large",
																										iconCls : "borders-inside",
																										style : "margin-left: 6px;",
																										handler : function() {
																											if (borderBgStyle == "border-choose-field-2x2") {
																												midVerBorderProp = false;
																												midHorBorderProp = false;
																												midVerBorderHandler();
																												midHorBorderHandler()
																											} else {
																												if (borderBgStyle == "border-choose-field-col") {
																													midHorBorderProp = false;
																													midHorBorderHandler()
																												} else {
																													if (borderBgStyle == "border-choose-field-row") {
																														midVerBorderProp = false;
																														midVerBorderHandler()
																													}
																												}
																											}
																										}
																									}),
																							{
																								html : "Inside",
																								baseCls : "x-plain",
																								bodyStyle : "text-align: center; margin-top: 5px;"
																							} ]
																				} ]
																	}) ]
														},
														{
															bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
															border : true,
															autoHeight : true,
															xtype : "fieldset",
															layout : "form",
															frame : false,
															title : "Border"
																	.localize(),
															items : [ new Ext.Panel(
																	{
																		id : "border-detailed",
																		layout : "column",
																		baseCls : "x-plain",
																		border : false,
																		frame : false,
																		items : [
																				{
																					columnWidth : 0.15,
																					layout : "form",
																					xtype : "fieldset",
																					bodyStyle : "padding-left: 0px; color: #000000; font-size: 9pt; background-color: transparent;",
																					autoHeight : true,
																					border : false,
																					frame : false,
																					items : [
																							borderTopButton,
																							borderMidHorButton,
																							borderBottomButton ]
																				},
																				{
																					columnWidth : 0.8,
																					layout : "form",
																					xtype : "fieldset",
																					bodyStyle : "padding-left: 0px; color: #000000; font-size: 9pt; background-color: transparent;",
																					autoHeight : true,
																					border : false,
																					frame : false,
																					items : [
																							borderDataView,
																							new Ext.Panel(
																									{
																										id : "border-detailed-buttons-down",
																										layout : "column",
																										baseCls : "x-plain",
																										border : false,
																										frame : false,
																										items : [
																												{
																													columnWidth : 0.33,
																													layout : "form",
																													xtype : "fieldset",
																													bodyStyle : "padding-left: 0px; color: #000000; font-size: 9pt; background-color: transparent;",
																													autoHeight : true,
																													border : false,
																													frame : false,
																													items : [ borderLeftButton ]
																												},
																												{
																													columnWidth : 0.33,
																													layout : "form",
																													xtype : "fieldset",
																													bodyStyle : "padding-left: 0px; color: #000000; font-size: 9pt; background-color: transparent;",
																													autoHeight : true,
																													border : false,
																													frame : false,
																													items : [ borderMidVerButton ]
																												},
																												{
																													columnWidth : 0.33,
																													layout : "form",
																													xtype : "fieldset",
																													bodyStyle : "padding-left: 0px; color: #000000; font-size: 9pt; background-color: transparent;",
																													autoHeight : true,
																													border : false,
																													frame : false,
																													items : [ borderRightButton ]
																												} ]
																									}) ]
																				} ]
																	}) ]
														} ]
											} ]
								}),
						{
							html : "The selected border style can be applied by clicking the presets, preview diagram or the buttons above."
									.localize(),
							baseCls : "x-plain"
						} ]
			});
	function genBordersConf() {
		var brdConf = {};
		brdConf.top = (topBorderProp) ? {
			width : borders.top.dim,
			type : borders.top.style,
			color : "#".concat(borders.top.color)
		} : "";
		brdConf.bottom = (bottomBorderProp) ? {
			width : borders.bottom.dim,
			type : borders.bottom.style,
			color : "#".concat(borders.bottom.color)
		} : "";
		brdConf.left = (leftBorderProp) ? {
			width : borders.left.dim,
			type : borders.left.style,
			color : "#".concat(borders.left.color)
		} : "";
		brdConf.right = (rightBorderProp) ? {
			width : borders.right.dim,
			type : borders.right.style,
			color : "#".concat(borders.right.color)
		} : "";
		brdConf.ins_vert = (midVerBorderProp) ? {
			width : borders.midVer.dim,
			type : borders.midVer.style,
			color : "#".concat(borders.midVer.color)
		} : "";
		brdConf.ins_horiz = (midHorBorderProp) ? {
			width : borders.midHor.dim,
			type : borders.midHor.style,
			color : "#".concat(borders.midHor.color)
		} : "";
		return brdConf
	}
	callback(borderTab);
	setTimeout(function() {
		_setupColorButton2(borderColorButton, state.colorBrd, false)
	}, 20)
};