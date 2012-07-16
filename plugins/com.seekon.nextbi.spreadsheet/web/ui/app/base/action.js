Jedox.wss.action.copy = function(copyToClp) {
	var env = Jedox.wss.app.environment;
	if (env.inputMode == Jedox.wss.grid.GridMode.READY) {
		var viewMode = Jedox.wss.grid.viewMode;
		switch (env.viewMode) {
		case viewMode.DESIGNER:
			env.defaultSelection.copy(copyToClp);
			break;
		case viewMode.USER:
			if (!env.selectedCellCoords.length) {
				return
			}
			var highlightedCell = document
					.getElementById(Jedox.wss.app.activePane.getDomId().concat(
							"_cursorField"));
			highlightedCell.style.border = "2px solid #F5B800";
			var cursorValue = Jedox.wss.app.activeBook.getCellValue(
					env.selectedCellCoords[0], env.selectedCellCoords[1]);
			if (cursorValue == undefined) {
				cursorValue = ""
			}
			Jedox.wss.app.clipboard = {
				id : null,
				op : Jedox.wss.grid.gridOperation.COPY,
				value : cursorValue
			};
			var _highlightBackToNormalCell = function() {
				highlightedCell.style.border = "2px solid #7EADD9"
			};
			setTimeout(_highlightBackToNormalCell, 300);
			if (copyToClp) {
				Jedox.wss.general.setSysClipboard(cursorValue)
			}
		}
	} else {
		var selText = Jedox.util.getSelected(env.inputField);
		if (selText != null) {
			Jedox.wss.app.clipboard = {
				id : null,
				op : Jedox.wss.grid.gridOperation.COPY,
				value : selText
			}
		}
	}
	Jedox.wss.action.togglePaste(true)
};
Jedox.wss.action.cut = function(copyToClp) {
	var env = Jedox.wss.app.environment;
	if (env.inputMode == Jedox.wss.grid.GridMode.READY) {
		var viewMode = Jedox.wss.grid.viewMode;
		switch (env.viewMode) {
		case viewMode.DESIGNER:
			env.defaultSelection.cut(copyToClp);
			break;
		case viewMode.USER:
			var selCellCoords = env.selectedCellCoords;
			if (selCellCoords.length == 0) {
				return
			}
			var activeBook = Jedox.wss.app.activeBook, cursorFld = env.cursorField, cursorValue = activeBook
					.getCellValue(selCellCoords[0], selCellCoords[1]);
			if (cursorValue == undefined) {
				cursorValue = ""
			}
			Jedox.wss.app.clipboard = {
				id : null,
				op : Jedox.wss.grid.gridOperation.CUT,
				value : cursorValue
			};
			if (copyToClp) {
				Jedox.wss.general.setSysClipboard(cursorValue)
			}
			Jedox.util.setText(cursorFld, "");
			activeBook.clrRange( [ selCellCoords[0], selCellCoords[1],
					selCellCoords[0], selCellCoords[1] ])
		}
	} else {
		var selText = Jedox.util.getSelected(env.inputField);
		if (selText != null) {
			Jedox.wss.app.clipboard = {
				id : null,
				op : Jedox.wss.grid.gridOperation.CUT,
				value : selText
			}
		}
	}
	Jedox.wss.action.togglePaste(true)
};
Jedox.wss.action.paste = function(pasteWhat) {
	var env = Jedox.wss.app.environment;
	if (env.inputMode == Jedox.wss.grid.GridMode.READY) {
		var viewMode = Jedox.wss.grid.viewMode, chkPasteWhat = function(pW) {
			var contType = Jedox.wss.range.ContentType, defCType = contType.ALL_PASTE;
			if (!pW) {
				return defCType
			}
			for ( var cT in contType) {
				if (contType[cT] == pW) {
					return pW
				}
			}
			return defCType
		};
		switch (env.viewMode) {
		case viewMode.DESIGNER:
			env.defaultSelection.paste(chkPasteWhat(pasteWhat));
			break;
		case viewMode.USER:
			var clipboard = Jedox.wss.app.clipboard, clpVal;
			if (clipboard == null) {
				clpVal = Jedox.wss.general.getSysClipboard()
			} else {
				clpVal = clipboard.value
			}
			var selCellCoords = env.selectedCellCoords;
			if (selCellCoords.length == 0) {
				return
			}
			var activeBook = Jedox.wss.app.activeBook, cursorFld = env.cursorField;
			activeBook.setCellValue(selCellCoords[0], selCellCoords[1], clpVal)
		}
	}
};
Jedox.wss.action.togglePaste = function(enabled) {
	if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.USER) {
		return
	}
	var act = enabled ? "enable" : "disable", menubar = Jedox.wss.app.menubar, toolbar = Jedox.wss.app.toolbar, tbarLayouts = Jedox.wss.app.toolbarLayouts;
	switch (Jedox.wss.app.toolbarLayout) {
	case tbarLayouts.TOOLBAR:
		menubar.paste[act]();
		menubar.pasteSpec[act]();
		break;
	case tbarLayouts.RIBBON:
		toolbar.pasteSpec[act]();
		break
	}
	toolbar.paste[act]()
};
Jedox.wss.action.closeWindow = function() {
	if (Jedox.wss.app.environment
			&& Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.USER
			&& Jedox.wss.app.activeBook.isClone()) {
		Jedox.wss.backend.ha.removeCloneWorksheet()
	}
	window.onbeforeunload = function() {
	};
	window.close()
};
Jedox.wss.action.refreshWindow = function() {
	window.onbeforeunload = function() {
	};
	window.location.href = window.location.href
};
Jedox.wss.action.exportToPDF = function() {
	window
			.open(
					"cc/export.php/".concat(
							escape(Jedox.wss.workspace.activeWin.title),
							".pdf?format=pdf&wam=", Jedox.wss.app.appModeS),
					"exp2pdf",
					"directories=no,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=no")
};
Jedox.wss.action.exportToHTML = function() {
	window
			.open(
					"cc/export.php/".concat(
							escape(Jedox.wss.workspace.activeWin.title),
							".html?format=html&wam=", Jedox.wss.app.appModeS),
					"exp2html",
					"directories=no,menubar=yes,toolbar=yes,location=no,status=yes,resizable=yes,scrollbars=yes")
};
Jedox.wss.action.exportToXLSX = function() {
	var frameID = "tmpExportIFrame";
	var frame = Ext.get("tmpExportIFrame");
	if (!frame) {
		frame = document.createElement("iframe");
		frame.id = frameID;
		frame.name = frameID;
		frame.className = "x-hidden";
		if (Ext.isIE) {
			frame.src = Ext.SSL_SECURE_URL
		}
		document.body.appendChild(frame);
		if (Ext.isIE) {
			document.frames[frameID].name = frameID
		}
	} else {
		frame = frame.dom
	}
	frame.contentWindow.location.href = "cc/export.php/".concat(
			escape(Jedox.wss.workspace.activeWin.title),
			".xlsx?format=xlsx&wam=", Jedox.wss.app.appModeS)
};
Jedox.wss.action.insDelRowCol = function(action, type) {
	var activeBook = Jedox.wss.app.activeBook, env = Jedox.wss.app.environment, selStartCoord = env.lastRangeStartCoord, selEndCoord = env.lastRangeEndCoord, handler;
	if (action == "ins") {
		handler = (type == "col") ? activeBook.insertCol : activeBook.insertRow
	} else {
		handler = (type == "col") ? activeBook.deleteCol : activeBook.deleteRow
	}
	if (selStartCoord[0] == selEndCoord[0]
			&& selStartCoord[0] == selEndCoord[0]) {
		var mrgInfo = activeBook.getMergeInfo(selStartCoord[0],
				selStartCoord[1]);
		if (mrgInfo && mrgInfo[0]) {
			selEndCoord = [ selStartCoord[0] + mrgInfo[1] - 1,
					selStartCoord[1] + mrgInfo[2] - 1 ]
		}
	}
	var pos = (type == "col") ? selStartCoord[0] : selStartCoord[1], cnt = (type == "col") ? selEndCoord[0]
			- selStartCoord[0] + 1
			: selEndCoord[1] - selStartCoord[1] + 1;
	handler.call(activeBook, pos, cnt);
	env.copySelection.removeAll();
	Jedox.wss.app.clipboard = null;
	Jedox.wss.action.togglePaste(false)
};
Jedox.wss.action.resizeRowCol = function(type, size) {
	var env = Jedox.wss.app.environment, selStartCoord = env.lastRangeStartCoord, selEndCoord = env.lastRangeEndCoord;
	if (typeof size == "number" && size >= 0) {
		Jedox.wss.app.activeBook.resizeColRow(type, [ [ selStartCoord[type],
				selEndCoord[type] ] ], size)
	} else {
		Jedox.wss.app.activeBook.autofitColRow(type, [ [ selStartCoord[type],
				selEndCoord[type] ] ])
	}
};
Jedox.wss.action.switchTheme = function(btn, state) {
	if (!state) {
		return
	}
	Ext.util.CSS.swapStyleSheet("theme", "../lib/ext/resources/css/x".concat(
			btn.id, ".css"));
	function uncheck(item, index, length) {
		if (item.id != btn.id) {
			item.setChecked(false)
		}
	}
	Jedox.wss.app[Jedox.wss.app.menubar ? "menubar" : "toolbar"].themeMenu.items
			.each(uncheck)
};
Jedox.wss.action.mergeCells = function(unmerge) {
	var defSelActRng = Jedox.wss.app.environment.defaultSelection
			.getActiveRange(), defSelCoords = defSelActRng.getCorners();
	Jedox.wss.app.activeBook.merge( [ defSelCoords[0].getX(),
			defSelCoords[0].getY(), defSelCoords[1].getX(),
			defSelCoords[1].getY() ], unmerge)
};
Jedox.wss.action.clear = function(type) {
	var defSelActRng = Jedox.wss.app.environment.defaultSelection
			.getActiveRange(), defSelCoords = defSelActRng.getCorners();
	Jedox.wss.app.activeBook.clrRange( [ defSelCoords[0].getX(),
			defSelCoords[0].getY(), defSelCoords[1].getX(),
			defSelCoords[1].getY() ], type)
};
Jedox.wss.action.newWorkbook = function() {
	try {
		for ( var triggers = Jedox.wss.events.triggers.newWorkbook_before, i = triggers.length - 1; i >= 0; i--) {
			triggers[i][0]["newWorkbook_before"].call(parent, triggers[i][1])
		}
		Jedox.wss.book.create();
		for ( var triggers = Jedox.wss.events.triggers.newWorkbook_after, i = triggers.length - 1, wbMeta = Jedox.wss.workspace
				.getMetaByWinId(Jedox.wss.app.activeBook.getWinId()); i >= 0; i--) {
			triggers[i][0]["newWorkbook_after"].call(parent, triggers[i][1],
					wbMeta.name)
		}
	} catch (e) {
		Jedox.wss.general.showMsg("Application Error".localize(), e.message
				.localize(), Ext.MessageBox.ERROR)
	}
};
Jedox.wss.action.adjustToACL = function() {
	if (Jedox.wss.app.appMode == Jedox.wss.grid.viewMode.USER) {
		return
	}
	var perms = Jedox.wss.grid.permission, perm = perms.PERM_NONE, menubar = Jedox.wss.app.menubar, toolbar = Jedox.wss.app.toolbar, tbarLayouts = Jedox.wss.app.toolbarLayouts;
	if (Jedox.wss.app.activeBook != undefined) {
		var metaData = Jedox.wss.workspace
				.getMetaByWinId(Jedox.wss.app.activeBook.getWinId());
		perm = metaData.ghn == null ? perms.PERM_DELETE : metaData.ghn.p
	}
	var act = perm >= perms.PERM_WRITE ? "enable" : "disable";
	switch (Jedox.wss.app.toolbarLayout) {
	case tbarLayouts.TOOLBAR:
		if (menubar.saveItem) {
			menubar.saveItem[act]()
		}
		menubar.saveAsItem[act]();
		break;
	case tbarLayouts.RIBBON:
		toolbar.saveAsItem[act]();
		break
	}
	toolbar.saveItem[act]()
};