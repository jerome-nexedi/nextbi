Jedox.wss.general = new function() {
	var that = this;
	this.startUp = function() {
		if (Ext.isGecko) {
			Jedox.wss.app.browser = "ff"
		} else {
			if (Ext.isSafari) {
				Jedox.wss.app.browser = "sf"
			} else {
				if (Ext.isIE) {
					Jedox.wss.app.browser = "ie"
				}
			}
		}
		Jedox.wss.app.UPRestrictMode = Jedox.wss.app.UPRestrictModeEnabled
				&& Jedox.wss.app.appMode == Jedox.wss.grid.viewMode.USER
				&& Jedox.wss.app.standalone;
		Ext.applyIf(Jedox.wss.i18n.strings, Jedox.wss.tmp.i18n_strings);
		delete Jedox.wss.tmp.i18n_strings;
		Jedox.wss.app.dynJSAutoload();
		window.onresize = Jedox.wss.workspace.resize;
		document.onkeydown = Jedox.wss.keyboard.checkGlobalInput;
		document.onkeyup = Jedox.wss.keyboard.fetchGlobalKeyUp;
		document.onmouseup = Jedox.wss.mouse.fetchGlobalMouseUp;
		document.onmousedown = Jedox.wss.mouse.fetchGlobalMouseDown;
		document.onmousemove = Jedox.wss.mouse.fetchGlobalMouseMove;
		Jedox.wss.app.currFormula = document.getElementById("currFormula");
		Jedox.wss.app.currCoord = document.getElementById("currCoord");
		Jedox.wss.backend.ha(WSS);
		Jedox.wss.backend.studio = new Studio();
		if (!Jedox.wss.app.serverAvailable) {
			alert("srvNotRespond".localize());
			return
		}
		Jedox.wss.app.mouseMovementObserver = new Jedox.gen.Observer();
		Jedox.wss.app.mouseUpObserver = new Jedox.gen.Observer();
		Jedox.wss.app.mouseDownObserver = new Jedox.gen.Observer();
		Ext.QuickTips.init();
		var tbarLayouts = Jedox.wss.app.toolbarLayouts;
		switch (Jedox.wss.app.toolbarLayout) {
		case tbarLayouts.TOOLBAR:
			Jedox.wss.app.initMenuBar();
			Jedox.wss.app.initActiveToolbars();
			break;
		case tbarLayouts.RIBBON:
			Jedox.wss.app.initRibbon();
			break
		}
		Jedox.wss.app.initStatusBar();
		Jedox.wss.palo.utils.registerHandlers();
		Jedox.wss.hb.regECHandlers();
		Jedox.wss.mouse.regHyperlinkHandlers();
		Jedox.wss.app.screenPosition = [ 0, 0 ];
		var isUserMode = Jedox.wss.app.appMode == Jedox.wss.grid.viewMode.USER;
		var wbList = Jedox.wss.backend.ha.getLoadedBooks(), currWbId = Jedox.wss.backend.ha
				.getCurrWbId(), upRestr = Jedox.wss.app.UPRestrictMode;
		Jedox.wss.app.workbookList = wbList;
		if (opener != null && isUserMode) {
			try {
				opener.Jedox.wss.general.switchSuspendMode(true);
				Jedox.wss.app.userPreview = true
			} catch (e) {
			}
		}
		function spawn(idx) {
			if (idx < 0) {
				return that.startUp_post(currWbId)
			}
			var wbMeta = wbList[idx][2].length ? Ext.util.JSON
					.decode(wbList[idx][2]) : null;
			if ((wbMeta && wbMeta.hidden)
					|| (upRestr && wbList[idx][0] != currWbId)) {
				spawn(idx - 1);
				return
			}
			var ghn = wbMeta != null ? {
				g : wbMeta.group,
				h : wbMeta.hierarchy,
				n : wbMeta.node,
				p : wbMeta.perm,
				v : wbMeta.hidden === true
			} : null;
			Jedox.wss.backend.ha.selectBook(wbList[idx][0], !isUserMode,
					!(upRestr && Jedox.wss.app.userPreview));
			Jedox.wss.book.spawn( [ this, function() {
				spawn(idx - 1)
			} ], wbList[idx][0], wbMeta != null ? wbMeta.name : wbList[idx][1],
					ghn)
		}
		spawn(wbList.length - 1)
	};
	this.startUp_post = function(currWbId) {
		var wbList = Jedox.wss.app.workbookList;
		if (currWbId != null && currWbId.length && currWbId != wbList[0][0]) {
			Jedox.wss.workspace.showWinByWbId(currWbId)
		}
		Jedox.wss.action.adjustToACL();
		var activeBook = Jedox.wss.app.activeBook;
		if (activeBook && activeBook._autoRefresh) {
			Jedox.wss.book.startRefresh(activeBook)
		}
		if (!wbList.length && !currWbId) {
			var preload = Jedox.wss.backend.ha.getPreload();
			if (preload && !preload[1]) {
				if (!preload[2].search(/^recovery*/i)) {
					Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.recover, [
							preload[3], [ null ].concat(preload[0]) ])
				} else {
					Jedox.wss.general.showMsg("Application Error".localize(),
							"errLoadWB_intro".localize().concat(" ",
									preload[2].localize()),
							Ext.MessageBox.ERROR, [ this,
									Jedox.wss.workspace.notifyCloseByMeta, {
										ghn : {
											g : preload[0][1],
											h : preload[0][2],
											n : preload[0][0]
										},
										name : null
									} ])
				}
			}
		}
		Jedox.wss.app.loaded = true
	};
	this.setCurrentCoord = function() {
		var env = Jedox.wss.app.environment, selCellCoords = env.selectedCellCoords, value = env.defaultSelection
				.getValue();
		Jedox.wss.app.currCoord.value = env.selectedRowName
				+ env.selectedAbsColumnName;
		env.inputField.value = value[0];
		Jedox.wss.app.currFormula.value = (value[1] != "") ? value[1] : that
				.filterHLTags(selCellCoords[0], selCellCoords[1], value[0],
						false)
	};
	this.setCoords = function() {
		Jedox.wss.app.currCoord.value = Jedox.wss.app.numberToLetter[Jedox.wss.app.environment.selectedCellCoords[0]]
				+ Jedox.wss.app.environment.selectedCellCoords[1]
	};
	this.setInputMode = function(inputMode) {
		var env = Jedox.wss.app.environment, activeBook = Jedox.wss.app.activeBook, sheetSel = activeBook ? activeBook
				.getSheetSelector()
				: undefined, modeDesc = "";
		function removeMarkRng() {
			if (env.copySelection != null) {
				env.copySelection.removeAll();
				Jedox.wss.app.clipboard = null;
				Jedox.wss.action.togglePaste(false)
			}
		}
		function saveInitVal() {
			Jedox.wss.app.updateUndoState( [ 0, 0 ]);
			env.undoValue = env.inputField.value
		}
		switch (inputMode) {
		case Jedox.wss.grid.GridMode.EDIT:
			if (sheetSel) {
				sheetSel.enable(false)
			}
			removeMarkRng();
			saveInitVal();
			modeDesc = "Edit";
			break;
		case Jedox.wss.grid.GridMode.INPUT:
			if (sheetSel) {
				sheetSel.enable(false)
			}
			removeMarkRng();
			saveInitVal();
			modeDesc = "Enter";
			break;
		case Jedox.wss.grid.GridMode.POINT:
			modeDesc = "Point";
			break;
		default:
			if (sheetSel) {
				sheetSel.enable(true)
			}
			Jedox.wss.app.updateUndoState();
			modeDesc = "Ready"
		}
		if (env != null) {
			env.inputMode = inputMode
		}
		Jedox.wss.app.statusBar.setInputMode(modeDesc)
	};
	this.mouseDownOnFormFld = function(event) {
		var env = Jedox.wss.app.environment;
		var range = env.defaultSelection;
		var gridMode = Jedox.wss.grid.GridMode;
		function cbMain() {
			Jedox.wss.app.fromFormulaField = true;
			Jedox.wss.app.lastInputField = Jedox.wss.app.currFormula;
			env.formulaSelection.activeToken = null;
			Jedox.wss.app.currFormula.focus()
		}
		function cbInputEdit() {
			Jedox.wss.keyboard.setFieldSize();
			cbMain()
		}
		if (env.inputMode == gridMode.INPUT) {
			that.setInputMode(gridMode.EDIT)
		} else {
			if (env.inputMode != gridMode.EDIT) {
				var value = range.getValue();
				env.inputField.value = value[1];
				that.setInputMode(gridMode.EDIT);
				Jedox.wss.keyboard.setFieldContent();
				that.showInputField( [ this, cbInputEdit ], false, true);
				return
			}
		}
		cbMain()
	};
	this.focusOnFormFld = function(event) {
		Jedox.wss.keyboard.setFieldSize()
	};
	this.toggleCellLock = function() {
		var env = Jedox.wss.app.environment;
		var startCoord = env.lastRangeStartCoord;
		var endCoord = env.lastRangeEndCoord;
		var selCoord = env.selectedCellCoords;
		var activeBook = Jedox.wss.app.activeBook;
		if (Jedox.wss.app.performItemToggle) {
			activeBook.setRangeLock( [ startCoord[0], startCoord[1],
					endCoord[0], endCoord[1] ], !activeBook.isCellLocked(
					selCoord[0], selCoord[1]))
		}
	};
	this.focusInputField = function() {
		if (Jedox.wss.app.fromFormulaField) {
			Jedox.wss.app.currFormula.focus()
		} else {
			var inputField = Jedox.wss.app.environment.inputField, fieldLen = inputField.value.length;
			inputField[Ext.isSafari ? "select" : "focus"]();
			if (document.all) {
				var selRng = document.selection.createRange();
				selRng.move("character", fieldLen);
				selRng.select()
			} else {
				inputField.setSelectionRange(fieldLen, fieldLen)
			}
		}
	};
	this.isRngSingleCell = function() {
		var env = Jedox.wss.app.environment;
		var rngStartCoords = env.lastRangeStartCoord;
		var rngEndCoords = env.lastRangeEndCoord;
		return (rngStartCoords[0] == rngEndCoords[0] && rngStartCoords[1] == rngEndCoords[1])
	};
	this.updateInputFieldPosition = function() {
		var env = Jedox.wss.app.environment;
		var inputField = env.inputField;
		var cursorField = env.cursorField;
		if (this.isRngSingleCell()) {
			inputField.style.left = cursorField.offsetLeft + "px";
			inputField.style.top = cursorField.offsetTop + "px"
		} else {
			inputField.style.left = cursorField.offsetLeft
					+ (document.all ? 3 : 0) + "px";
			inputField.style.top = cursorField.offsetTop
					+ (document.all ? 3 : 0) + "px"
		}
		inputField.style.height = cursorField.offsetHeight + "px"
	};
	this.showInputField = function(cb, moveToFirstChar, directly, fetchFormula) {
		var env = Jedox.wss.app.environment, activeBook = Jedox.wss.app.activeBook, vMode = Jedox.wss.grid.viewMode, inputField = env.inputField, selectedCell = env.selectedCell, selectedCellValue = env.selectedCellValue, selectedCellCoords = env.selectedCellCoords, extraXOffset = (Jedox.wss.app.isIE) ? 1
				: 0, extraYOffset = (Jedox.wss.app.isIE) ? 1 : 0, myPosition, oldValue = null, calculatedCellContent = false;
		fetchFormula = (fetchFormula == undefined) ? true : fetchFormula;
		inputField.style.visibility = "hidden";
		function delArrForm(formVal) {
			if (formVal.charAt(0) == "{"
					&& formVal.charAt(formVal.length - 1) == "}") {
				oldValue = formVal;
				formVal = formVal.substring(1, formVal.length - 1)
			}
			return formVal
		}
		var cbScrollTo = function() {
			env.editingDirectly = directly;
			if (env.viewMode == vMode.USER) {
				inputField.style.top = selectedCell.parentNode.offsetTop - 1
						+ "px";
				inputField.style.left = selectedCell.offsetLeft - 1 + "px";
				inputField.style.width = selectedCell.offsetWidth - 3 + "px";
				inputField.style.height = selectedCell.offsetHeight - 4 + "px"
			} else {
				that.updateInputFieldPosition();
				inputField.style.height = selectedCell.offsetHeight
						- (that.isRngSingleCell() ? 4 : 5) + "px";
				inputField.style.borderStyle = "none"
			}
			var selFormulaSet = (env.selectedCellFormula != "null" && env.selectedCellFormula.length > 0);
			if (fetchFormula && env.viewMode == vMode.DESIGNER
					&& !selFormulaSet) {
				var rngFormulaVal = env.defaultSelection.getValue()[1];
				if (rngFormulaVal != env.selectedCellFormula) {
					env.selectedCellFormula = rngFormulaVal;
					selFormulaSet = true
				}
			}
			inputField.value = env.viewMode == vMode.DESIGNER && selFormulaSet ? delArrForm(env.selectedCellFormula)
					: (selectedCellValue.length ? that.filterHLTags(
							selectedCellCoords[0], selectedCellCoords[1],
							selectedCellValue, false) : " ");
			Jedox.wss.style.cellTransfer(inputField);
			var c = selectedCellCoords[0], r = selectedCellCoords[1];
			if (inputField.style.textAlign == ""
					&& (activeBook.getCellValue(c, r) == undefined || activeBook
							.getCellFormula(c, r) != undefined)) {
				inputField.style.textAlign = "left"
			}
			inputField.style.zIndex = "39";
			inputField.style.display = "block";
			inputField.style.visibility = "visible";
			if (moveToFirstChar) {
				if (document.all) {
					var selRng = document.selection.createRange();
					selRng.move("character", 0);
					selRng.select()
				} else {
					inputField.setSelectionRange(0, 0)
				}
			}
			if (env.viewMode != vMode.USER) {
				Jedox.wss.keyboard.setFieldContent()
			} else {
				Jedox.wss.keyboard.setFieldSize()
			}
			env.oldValue = (oldValue == null) ? inputField.value : oldValue;
			env.lastInputValue = inputField.value;
			if (selectedCellValue.length == 0 && !selFormulaSet) {
				inputField.value = "";
				if (env.viewMode == vMode.DESIGNER) {
					Jedox.wss.app.currFormula.value = ""
				}
			}
			that.focusInputField();
			if (cb instanceof Array && cb.length > 1) {
				cb[1].call(cb[0])
			}
		};
		if (env.viewMode == vMode.DESIGNER) {
			if (activeBook._scrollOpPending) {
				return
			}
			env.cursorField.style.visibility = "hidden";
			activeBook.scrollTo( [ this, cbScrollTo ], selectedCellCoords[0],
					selectedCellCoords[1], false, false)
		} else {
			cbScrollTo()
		}
	};
	this.addFileMenuEntry = function(entryText) {
		var fixedItemsCount = (Jedox.wss.app.appMode == Jedox.wss.grid.viewMode.USER) ? 8
				: 13;
		var itemName = (entryText.length > 30) ? (entryText.substring(0, 14)
				+ "..." + entryText.substring(entryText.length - 9))
				: entryText;
		var menuPosition = Jedox.wss.app.menubar.fileMenu.items.length
				- fixedItemsCount + 1;
		Jedox.wss.app.menubar.fileMenu.add( {
			text : menuPosition + ". " + itemName + ".wss",
			href : "javascript: Jedox.wss.book.load(null,'" + entryText + "');"
		})
	};
	this.createWorksheetElements = function() {
		function _load(res) {
			if (!(res instanceof Array) || res[0] !== true) {
				return
			}
			res = res[1];
			var jwdchart = Jedox.wss.dialog.chart, winId = Jedox.wss.app.activeBook
					.getDomId();
			for ( var chartData, i = res.length - 1; i >= 0; --i) {
				jwdchart.createChart(false, winId, (chartData = res[i]).e_id,
						chartData.n_location, chartData.pos_offsets,
						chartData.size, chartData.subtype)
			}
		}
		Jedox.wss.backend.conn.cmd( [ this, _load ], [ "wget" ], [ "", [],
				[ "e_id", "size", "subtype", "n_location", "pos_offsets" ], {
					e_type : "chart"
				} ])
	};
	this.refreshWorksheetElements = function(wsElements) {
		var winId = Jedox.wss.app.activeBook.getDomId();
		var numOfElems = wsElements.length;
		for ( var currDate = new Date(), i = 0; i < numOfElems; i++) {
			document.getElementById("".concat(winId, "_ws_element_",
					wsElements[i])).src = "cc/gen_element.php?wam=".concat(
					Jedox.wss.app.appModeS, "&id=", wsElements[i], "&ts=",
					currDate.getTime())
		}
	};
	this.getSysClipboard = function() {
		var pasteFld = document.getElementById("_paste_field_");
		return (pasteFld == null) ? null : document
				.getElementById("_paste_field_").value
	};
	this.setSysClipboard = function(value) {
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		that.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
		if (!document.getElementById("_copy_field_")) {
			_cinput = document.createElement("textarea");
			_cinput.setAttribute("id", "_copy_field_");
			_cinput.setAttribute("name", "_copy_field_");
			_cinput
					.setAttribute(
							"style",
							"position: float; width: 1px; height: 1px; user-select: text; -moz-user-select: text; -webkit-user-select: text; z-index: 999;");
			_cinput.setAttribute("value", "1");
			document.getElementById("mainBody").appendChild(_cinput)
		}
		if (Jedox.wss.app.browser == "ie") {
			var copyfld = document.getElementById("_copy_field_");
			copyfld.value = value;
			copyfld.select();
			document.selection.createRange()
		} else {
			var copyfld = document.getElementById("_copy_field_");
			copyfld.value = value;
			copyfld.select();
			copyfld.selectionStart = 0;
			copyfld.selectionEnd = 65535
		}
	};
	this.filterHLTags = function(x, y, val, addTags) {
		if (Jedox.wss.app.activeBook == undefined) {
			return val
		}
		var valType = Jedox.wss.app.activeBook.getCellType(x, y), hlTag = Jedox.wss.hl.hlTag;
		if (addTags) {
			if (valType == "h" && val.search(/^=HYPERLINK\(/)) {
				return hlTag.begin.concat(val, hlTag.end)
			}
		} else {
			if (valType == undefined || valType == "h") {
				var bStrs = [ hlTag.oldBegin, hlTag.begin ];
				for ( var bStr in bStrs) {
					if (val.indexOf(bStrs[bStr]) == 0) {
						return val.replace(bStrs[bStr], "").replace(hlTag.end,
								"")
					}
				}
			}
		}
		return val
	};
	this.switchSuspendMode = function(status) {
		if (status) {
			if (!that.switchSuspendModeAlert) {
				that.switchSuspendModeAlert = new Ext.Window(
						{
							title : "Suspend Mode".localize(),
							id : "ext-el-mask-suspend-win",
							cls : "default-format-window",
							closable : false,
							autoDestroy : true,
							plain : true,
							draggable : false,
							constrain : true,
							modal : true,
							resizable : false,
							animCollapse : false,
							width : 400,
							autoHeight : true,
							layout : "fit",
							items : [ new Ext.Panel(
									{
										bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
										border : false,
										frame : false,
										autoHeight : true,
										layout : "fit",
										items : [ {
											html : "suspModeMsg".localize(),
											baseCls : "x-plain"
										} ]
									}) ]
						});
				Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
				that.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
				that.switchSuspendModeAlert.show(this)
			}
		} else {
			if (that.switchSuspendModeAlert) {
				var ha = Jedox.wss.backend.ha, activeBook = Jedox.wss.app.activeBook;
				ha.selectBook(activeBook.getWbId());
				ha.selectSheet(activeBook.getWsId(), activeBook.isClone());
				that.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				activeBook.recalc();
				that.switchSuspendModeAlert.destroy();
				that.switchSuspendModeAlert = null
			}
		}
	};
	this.appUnload = function() {
		if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.USER) {
			try {
				opener.Jedox.wss.general.switchSuspendMode(false)
			} catch (e) {
			}
		}
		document.getElementById("mainBody").style.display = "none"
	};
	this.showMsg = function(title, msg, dlgIcon, cb) {
		var env = Jedox.wss.app.environment;
		function resetInput() {
			if (env && env.inputMode == Jedox.wss.grid.GridMode.DIALOG) {
				that.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
			}
			if (cb instanceof Array && cb.length > 1) {
				cb[1].apply(cb[0], cb.slice(2))
			}
		}
		if (env && env.inputMode != Jedox.wss.grid.GridMode.DIALOG) {
			Jedox.wss.app.lastInputModeDlg = env.inputMode;
			that.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
		}
		Ext.MessageBox.show( {
			title : title,
			msg : msg,
			icon : dlgIcon,
			modal : true,
			buttons : Ext.MessageBox.OK,
			fn : resetInput
		})
	};
	this.refreshCursorField = function() {
		var env = Jedox.wss.app.environment, activeBook = Jedox.wss.app.activeBook, cursorField = env.cursorField, selCellCoords = env.selectedCellCoords, cursorValue = typeof selCellCoords[0] != "number"
				|| typeof selCellCoords[1] != "number" ? undefined : activeBook
				.getCellValue(selCellCoords[0], selCellCoords[1]), redrawCursorField;
		try {
			redrawCursorField = !cursorValue.search(/^<img src="/)
		} catch (e) {
			redrawCursorField = false
		}
		if (env.viewMode == Jedox.wss.grid.viewMode.USER
				&& cursorField.style.display != "none"
				&& activeBook.isCellLocked(selCellCoords[0], selCellCoords[1])) {
			cursorField.style.display = "none";
			return
		}
		if (redrawCursorField) {
			env.defaultSelection.getActiveRange().drawActiveCell()
		} else {
			Jedox.util.setText(cursorField, cursorValue == undefined ? ""
					: that.filterHLTags(selCellCoords[0], selCellCoords[1],
							cursorValue, false));
			Jedox.wss.style.cellTransfer(cursorField)
		}
	};
	this.chkState = function() {
		var env = Jedox.wss.app.environment;
		if (env && env.inputMode.inputMode == Jedox.wss.grid.GridMode.DIALOG) {
			throw {
				key : "wb_in_dlg_mode",
				params : {}
			}
		}
		if (that.switchSuspendModeAlert) {
			throw {
				key : "wb_in_suspend_mode",
				params : {}
			}
		}
	};
	this.chkHiddenColRow = function(isRow, pos, amount, isInc) {
		var dim = isRow ? Jedox.wss.app.activeSheet._rowHeights
				: Jedox.wss.app.activeSheet._colWidths, maxCoords = Jedox.wss.grid.defMaxCoords[isRow ? 1
				: 0], dirIdx = isInc ? 1 : -1, foundUnhidden = false;
		for ( var i = pos + amount * dirIdx; isInc ? i <= maxCoords : i >= 0; i += dirIdx) {
			if (dim.getElemAt(i) > 0) {
				foundUnhidden = true;
				break
			}
		}
		return foundUnhidden ? (isInc ? pos + (i - pos) : pos - (pos - i))
				: pos
	};
	this.showRecent = function(res, menu, alignEl, alignPos, parentMenu) {
		menu.removeAll();
		for ( var item, name, i = res.length - 1, loadRecent = Jedox.wss.book.loadRecent; i >= 0; --i) {
			item = res[i].location;
			name = item.path.substr(item.path.lastIndexOf("/") + 1);
			menu.addMenuItem( {
				text : name,
				iconCls : "w3s_workbook",
				ghn : {
					g : item.group,
					h : item.hierarchy,
					n : item.node,
					nm : name
				},
				qtip : item.path,
				listeners : {
					afterrender : function() {
						Ext.QuickTips.register( {
							target : this.getEl(),
							text : this.initialConfig.qtip,
							showDelay : 500
						})
					}
				},
				handler : function(item) {
					loadRecent(item)
				}
			})
		}
		menu.loaded = true;
		menu.show(alignEl, alignPos, parentMenu)
	};
	this.autoCalc = function(state) {
		var conn = Jedox.wss.backend.conn;
		conn.cmd(conn.dummy_cb, [ "sac" ], [ state ? 1 : 0 ]);
		Jedox.wss.app.autoCalc = state
	}
};
Jedox.wss.format.getSample = function(format, val) {
	var numericSeps = Jedox.wss.i18n.separators, l10nBool = Jedox.wss.i18n.bool, reThouSep = new RegExp(
			"\\".concat(numericSeps[1]), "g"), num = numericSeps[0] != "." ? val
			.replace(reThouSep, "").replace(numericSeps[0], ".")
			: val.replace(reThouSep, ""), res;
	if (val === "" || val === null || val === undefined) {
		val = ""
	} else {
		if (!isNaN(num)) {
			val = num * 1
		} else {
			if (val.toUpperCase() == l10nBool[true]) {
				val = true
			} else {
				if (val.toUpperCase() == l10nBool[false]) {
					val = false
				}
			}
		}
	}
	res = Jedox.wss.backend.conn.cmd(0, [ "gfs" ], [ format, val ])[0];
	return typeof res == "object" && res[0] == true ? res[1] : ""
};