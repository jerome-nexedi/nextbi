Jedox.wss.app.onFormatDropdownSelect = function(item, record, index) {
	var styleType = Jedox.wss.app.getToolbarItemID(item.getId());
	if (styleType != null) {
		Jedox.wss.style.setFromBar(styleType, item.getValue())
	}
	Ext.get("tbarFontSizes").blur();
	Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputMode);
	Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
};
Jedox.wss.app.onFormatDropdownFocus = function(item) {
	Jedox.wss.app.lastInputMode = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.CNTRL)
};
Jedox.wss.app.onFormatDropdownBlur = function(item) {
	var oldFontSize = Jedox.wss.app.environment.selectedCell.style.fontSize;
	if ((oldFontSize != "") && (oldFontSize.indexOf("pt") != -1)) {
		oldFontSize = oldFontSize.substr(0, oldFontSize.indexOf("pt"))
	} else {
		oldFontSize = Jedox.wss.app.cnfDefaultFontSize
	}
	if (oldFontSize != item.getRawValue()) {
		var styleType = Jedox.wss.app.getToolbarItemID(item.getId());
		if (styleType != null) {
			Jedox.wss.style.setFromBar(styleType, item.getRawValue())
		}
	}
	Ext.get("tbarFontSizes").blur();
	Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputMode);
	Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
};
Jedox.wss.app.onFormatDropdownChange = function(item, newValue, oldValue) {
	var styleType = Jedox.wss.app.getToolbarItemID(item.getId());
	if (styleType != null) {
		Jedox.wss.style.setFromBar(styleType, newValue)
	}
	Ext.get("tbarFontSizes").blur();
	Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputMode);
	Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
};
Jedox.wss.app.onFormatDropdownSpecKey = function(item, e) {
	var key = Jedox.wss.app.lastKeyPressed;
	if (key == e.ENTER || key == e.TAB) {
		var oldFontSize = Jedox.wss.app.environment.selectedCell.style.fontSize;
		if ((oldFontSize != "") && (oldFontSize.indexOf("pt") != -1)) {
			oldFontSize = oldFontSize.substr(0, oldFontSize.indexOf("pt"))
		} else {
			oldFontSize = Jedox.wss.app.cnfDefaultFontSize
		}
		if (oldFontSize != item.getRawValue()) {
			var styleType = Jedox.wss.app.getToolbarItemID(item.getId());
			if (styleType != null) {
				Jedox.wss.style.setFromBar(styleType, item.getRawValue())
			}
		}
		Ext.get("tbarFontSizes").blur();
		Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputMode);
		Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
	}
};
Jedox.wss.app.getToolbarItemID = function(itemName) {
	var toolbarItems = {
		tbarBold : 1,
		tbarItalic : 2,
		tbarUnderline : 3,
		tbarAlignLeft : 4,
		tbarAlignCenter : 5,
		tbarAlignRight : 6,
		tbarFontSizes : 7,
		tbarFonts : 8,
		tbarBgColor : 9,
		tbarBgColorBtn : 9,
		tbarTextColor : 10,
		tbarTextColorBtn : 10
	};
	return toolbarItems[itemName]
};
Jedox.wss.app.onFormatItemToggle = function(item, pressed) {
	var styleType = Jedox.wss.app.getToolbarItemID(item.getId());
	if (styleType != null && Jedox.wss.app.performItemToggle) {
		Jedox.wss.style.setFromBar(styleType, pressed)
	}
};
Jedox.wss.app.onColorSelect = function(item, color) {
	var bgColor = Jedox.wss.app.toolbar.bgColor;
	if (bgColor) {
		bgColor.hide()
	}
	var styleType = Jedox.wss.app.getToolbarItemID(item.getId());
	if (!styleType) {
		styleType = Jedox.wss.app.getToolbarItemID(item.ownerCt.getId())
	}
	if (item.id == "bgNoColor") {
		color = "transparent";
		Jedox.wss.app.cnfDefaultBgColor = color;
		Ext.DomQuery.selectNode("*[class*=iconbgcolor]").style.borderLeft = "solid 4px "
				+ color;
		Jedox.wss.style.setFromBar(9, color)
	} else {
		if (typeof color == "string") {
			if (styleType == Jedox.wss.app.getToolbarItemID("tbarBgColor")) {
				Jedox.wss.app.cnfDefaultBgColor = color;
				Ext.DomQuery.selectNode("*[class*=iconbgcolor]").style.borderLeft = "solid 4px #"
						+ color
			} else {
				Jedox.wss.app.cnfDefaultTextColor = color;
				Ext.DomQuery.selectNode("*[class*=icontextcolor]").style.borderLeft = "solid 4px #"
						+ color
			}
		} else {
			color = (styleType == Jedox.wss.app.getToolbarItemID("tbarBgColor")) ? Jedox.wss.app.cnfDefaultBgColor
					: Jedox.wss.app.cnfDefaultTextColor
		}
	}
	if (styleType != null) {
		Jedox.wss.style.setFromBar(styleType, color)
	}
};
Jedox.wss.app.onBorderSelect = function(item, ev) {
	var env = Jedox.wss.app.environment, rngStartCoord = env.lastRangeStartCoord, rngEndCoord = env.lastRangeEndCoord, btnBorders = Jedox.wss.app.toolbar.border, itemID = (item
			.getId() == btnBorders.getId()) ? Jedox.wss.app.cnfDefaultBorder
			: item.getId(), brdConf;
	switch (itemID) {
	case "brd-bottom-norm":
		brdConf = {
			bottom : {
				width : "1px",
				type : "solid",
				color : "#000000"
			}
		};
		break;
	case "brd-top-norm":
		brdConf = {
			top : {
				width : "1px",
				type : "solid",
				color : "#000000"
			}
		};
		break;
	case "brd-left-norm":
		brdConf = {
			left : {
				width : "1px",
				type : "solid",
				color : "#000000"
			}
		};
		break;
	case "brd-right-norm":
		brdConf = {
			right : {
				width : "1px",
				type : "solid",
				color : "#000000"
			}
		};
		break;
	case "brd-all-none":
		brdConf = {
			all : {
				type : "none"
			}
		};
		break;
	case "brd-all-norm":
		brdConf = {
			all : {
				width : "1px",
				type : "solid",
				color : "#000000"
			}
		};
		break;
	case "brd-out-norm":
		brdConf = {
			out : {
				width : "1px",
				type : "solid",
				color : "#000000"
			}
		};
		break;
	case "brd-out-thick":
		brdConf = {
			out : {
				width : "2px",
				type : "solid",
				color : "#000000"
			}
		};
		break;
	case "brd-bottom-thick":
		brdConf = {
			bottom : {
				width : "2px",
				type : "solid",
				color : "#000000"
			}
		};
		break;
	case "brd-top-bottom-norm":
		brdConf = {
			top : {
				width : "1px",
				type : "solid",
				color : "#000000"
			},
			bottom : {
				width : "1px",
				type : "solid",
				color : "#000000"
			}
		};
		break;
	case "brd-top-norm-bottom-thick":
		brdConf = {
			top : {
				width : "1px",
				type : "solid",
				color : "#000000"
			},
			bottom : {
				width : "2px",
				type : "solid",
				color : "#000000"
			}
		}
	}
	Jedox.wss.style.setBorder(brdConf);
	btnBorders.setIconClass("icon-".concat(itemID));
	btnBorders.getEl().child(btnBorders.buttonSelector).dom.qtip = Ext
			.getCmp(itemID).text;
	Jedox.wss.app.cnfDefaultBorder = itemID
};
Jedox.wss.app.openViewMode = function() {
	window
			.open(
					"ui/app/view.php?wam=designer",
					"winUserMode",
					"directories=no,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=no")
};
Jedox.wss.app.updateUndoState = function(undoState, updState) {
	var env = Jedox.wss.app.environment;
	if (env == null) {
		return
	}
	if (undoState == undefined) {
		undoState = env.undoState
	} else {
		if (updState == undefined || updState) {
			env.undoState = undoState
		}
	}
	if (true) {
		this.toolbar.undoItem.enable();
		if (this.menubar) {
			this.menubar.undoItem.setText("Undo".localize());
			this.menubar.undoItem.enable()
		}
	} else {
		this.toolbar.undoItem.disable();
		if (this.menubar) {
			this.menubar.undoItem.setText("Can't Undo".localize());
			this.menubar.undoItem.disable()
		}
	}
	if (true) {
		this.toolbar.redoItem.enable();
		if (this.menubar) {
			this.menubar.redoItem.setText("Redo".localize());
			this.menubar.redoItem.enable()
		}
	} else {
		this.toolbar.redoItem.disable();
		if (this.menubar) {
			this.menubar.redoItem.setText("Can't Redo".localize());
			this.menubar.redoItem.disable()
		}
	}
};
Jedox.wss.app.hideBar = function(barId) {
	var bar = Ext.get(barId);
	bar.setVisibilityMode(Ext.Element.DISPLAY);
	bar.hide();
	Jedox.wss.workspace.resize();
	Jedox.wss.workspace.resizeMaxWindows()
};
Jedox.wss.app.showBar = function(barId) {
	var bar = Ext.get(barId);
	bar.setVisibilityMode(Ext.Element.DISPLAY);
	bar.show();
	Jedox.wss.workspace.resize();
	Jedox.wss.workspace.resizeMaxWindows()
};
Jedox.wss.app.hideToolbar = function(toolbar) {
	Jedox.wss.app.hideBar("Toolbar")
};
Jedox.wss.app.showToolbar = function(toolbar) {
	Jedox.wss.app.showBar("Toolbar")
};
Jedox.wss.app.hideShowToolbar = function(state, toolbar) {
	if (state) {
		Jedox.wss.app.showToolbar(toolbar)
	} else {
		Jedox.wss.app.hideToolbar(toolbar)
	}
};
Jedox.wss.app.hideShowFormulaBar = function(state) {
	if (state) {
		Jedox.wss.app.showBar("formulaBar")
	} else {
		Jedox.wss.app.hideBar("formulaBar")
	}
};