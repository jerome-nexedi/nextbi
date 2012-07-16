Jedox.wss.sheet = new function() {
	var that = this, _json = Ext.util.JSON;
	this.insDelMode = {
		SHIFT_VER : 1,
		SHIFT_HOR : 2,
		ENTIRE_COL : 3,
		ENTIRE_ROW : 4
	};
	this.undo = function() {
		var env = Jedox.wss.app.environment;
		var gridMode = Jedox.wss.grid.GridMode;
		if (env.inputMode == gridMode.EDIT || env.inputMode == gridMode.INPUT) {
			env.redoValue = env.inputField.value;
			env.inputField.value = Jedox.wss.app.currFormula.value = env.undoValue;
			Jedox.wss.app.updateUndoState( [ 0, 1 ], false);
			Jedox.wss.app.lastInputField.focus()
		} else {
			Jedox.wss.app.activeBook.undo(1)
		}
	};
	this.redo = function() {
		var env = Jedox.wss.app.environment;
		var gridMode = Jedox.wss.grid.GridMode;
		if (env.inputMode == gridMode.EDIT || env.inputMode == gridMode.INPUT) {
			env.inputField.value = Jedox.wss.app.currFormula.value = env.redoValue;
			Jedox.wss.app.updateUndoState( [ 1, 0 ], false);
			Jedox.wss.app.lastInputField.focus()
		} else {
			Jedox.wss.app.activeBook.redo(1)
		}
	};
	this.freeze = function(crosshairs) {
		var jwgrid = Jedox.wss.grid, activeSheet = Jedox.wss.app.activeSheet, noCH = !(crosshairs instanceof Array);
		if (noCH && activeSheet._numPanes > 1) {
			var ccmd = '[["wdel","",["'
					.concat(activeSheet._paneConfUid, '"]]]'), menuItemTxt = "Freeze Panes"
					.localize()
		} else {
			var selCellCoords = Jedox.wss.app.environment.selectedCellCoords, leftTop = activeSheet._aPane._lastDoneDestCell;
			++leftTop[0];
			++leftTop[1];
			if (noCH) {
				if (selCellCoords[0] == 1 && selCellCoords[1] == 1) {
					return false
				}
				crosshairs = [ selCellCoords[0] - leftTop[0],
						selCellCoords[1] - leftTop[1] ]
			}
			if (crosshairs[0] == 0 && crosshairs[1] == 0) {
				return false
			}
			if (crosshairs[0] && crosshairs[1]) {
				var panes = [
						{
							range : [ leftTop[0], leftTop[1],
									selCellCoords[0] - 1, selCellCoords[1] - 1 ],
							hscroll : false,
							vscroll : false
						},
						{
							range : [ selCellCoords[0], leftTop[1],
									jwgrid.defMaxCoords[0],
									selCellCoords[1] - 1 ],
							hscroll : true,
							vscroll : false
						},
						{
							range : [ leftTop[0], selCellCoords[1],
									selCellCoords[0] - 1,
									jwgrid.defMaxCoords[1] ],
							hscroll : false,
							vscroll : true
						},
						{
							range : [ selCellCoords[0], selCellCoords[1],
									jwgrid.defMaxCoords[0],
									jwgrid.defMaxCoords[1] ],
							hscroll : true,
							vscroll : true
						} ]
			} else {
				if (crosshairs[0]) {
					var panes = [
							{
								range : [ leftTop[0], 1, selCellCoords[1] - 1,
										jwgrid.defMaxCoords[0] ],
								hscroll : false,
								vscroll : true
							},
							{
								range : [ selCellCoords[0], 1,
										jwgrid.defMaxCoords[0],
										jwgrid.defMaxCoords[1] ],
								hscroll : true,
								vscroll : true
							} ]
				} else {
					if (crosshairs[1]) {
						var panes = [
								{
									range : [ 1, leftTop[1],
											jwgrid.defMaxCoords[0],
											selCellCoords[1] - 1 ],
									hscroll : true,
									vscroll : false
								},
								{
									range : [ 1, selCellCoords[1],
											jwgrid.defMaxCoords[0],
											jwgrid.defMaxCoords[1] ],
									hscroll : true,
									vscroll : true
								} ]
					}
				}
			}
			var conf = '{"crosshairs":['.concat(crosshairs[0], ",",
					crosshairs[1], '],"panes":', _json.encode(panes)), ccmd = activeSheet._paneConfUid ? '[["wupd","",{"'
					.concat(activeSheet._paneConfUid, '":', conf, "}}]]")
					: '[["wadd","",'.concat(conf, ',"e_type":"panescnf"}]]'), menuItemTxt = "Unfreeze Panes"
					.localize()
		}
		var conn = Jedox.wss.backend.conn;
		conn.setCcmd(ccmd);
		conn.sendBatch( [ that, _freeze_post, activeSheet ])
	};
	function _freeze_post(res, sheet) {
		sheet._book._showSheet(null, sheet._uid, true)
	}
	this.select = function(cb, wsId, wbId, selSheet) {
		var book = wbId ? Jedox.wss.book.getBookByUid(wbId)
				: Jedox.wss.app.activeBook;
		if (!book) {
			return
		}
		var sheetSel = book._sheetSelector, cbShowSheet = function() {
			book._showSheet(cb, wsId)
		};
		sheetSel.enable(false);
		sheetSel.freeze(false);
		sheetSel.selectById(wsId, !selSheet);
		sheetSel.enable(true);
		if (selSheet) {
			Jedox.wss.backend.conn.cmd( [ this, cbShowSheet ], [ "osel" ], [ 2,
					wsId ])
		} else {
			cbShowSheet()
		}
	};
	this.ins = function(mode) {
		var activeBook = Jedox.wss.app.activeBook, defRanges = Jedox.wss.app.environment.defaultSelection
				.getRanges(), ranges = [];
		for ( var mi, range, i = defRanges.length - 1; i >= 0; --i) {
			range = defRanges[i].getCoords();
			if (range[0] == range[2]
					&& range[1] == range[3]
					&& (mi = activeBook._aPane.getMergeInfo(range[0], range[1]))
					&& mi[0]) {
				range[2] += mi[1] - 1, range[3] += mi[2] - 1
			}
			range.push(mode);
			ranges.unshift(range)
		}
		Jedox.wss.backend.conn.cmd( [ activeBook, activeBook.exec ],
				[ "icel" ], ranges, activeBook.getRealGridRange())
	};
	this.del = function(mode) {
		var activeBook = Jedox.wss.app.activeBook, defRanges = Jedox.wss.app.environment.defaultSelection
				.getRanges(), ranges = [];
		for ( var mi, range, i = defRanges.length - 1; i >= 0; --i) {
			range = defRanges[i].getCoords();
			if (range[0] == range[2]
					&& range[1] == range[3]
					&& (mi = activeBook._aPane.getMergeInfo(range[0], range[1]))
					&& mi[0]) {
				range[2] += mi[1] - 1, range[3] += mi[2] - 1
			}
			range.push(mode);
			ranges.unshift(range)
		}
		Jedox.wss.backend.conn.cmd( [ activeBook, activeBook.exec ],
				[ "dcel" ], ranges, activeBook.getRealGridRange())
	};
	this.getMergeState = function() {
		return Jedox.wss.app.activePane
				.getMergeState(Jedox.wss.app.environment.defaultSelection
						.getActiveRange().getCoords())
	};
	this.merge = function() {
		Jedox.wss.app.activePane
				.merge(Jedox.wss.app.environment.defaultSelection
						.getActiveRange().getCoords())
	};
	this.unMerge = function() {
		Jedox.wss.app.activePane.merge(
				Jedox.wss.app.environment.defaultSelection.getActiveRange()
						.getCoords(), true)
	}
};