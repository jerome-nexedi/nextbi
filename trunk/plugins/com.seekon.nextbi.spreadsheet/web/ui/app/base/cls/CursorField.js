Jedox.wss.cls.CursorField = function(domID, container, areaPres) {
	var _cursorFld;
	var _env = Jedox.wss.app.environment;
	this.getCursorField = function() {
		return _cursorFld
	};
	this.draw = function(activeCell, activeCellCoords) {
		var viewMode = Jedox.wss.grid.viewMode, offsets = (_env.viewMode == viewMode.DESIGNER) ? {
			t : 1,
			l : 0,
			w : -6,
			h : -6
		}
				: {
					t : -1,
					l : -1,
					w : -5,
					h : -5
				}, activeBook = Jedox.wss.app.activeBook, selCelFVal = (activeBook == undefined) ? _env.selectedCellValue
				: activeBook.getCellValue(activeCellCoords.getX(),
						activeCellCoords.getY()), defDraw = true;
		if (selCelFVal == undefined) {
			selCelFVal = ""
		}
		if (activeBook == undefined) {
			if (selCelFVal.indexOf("<") == 0) {
				defDraw = false
			}
		} else {
			var cellType = activeBook.getCellType(activeCellCoords.getX(),
					activeCellCoords.getY());
			defDraw = (cellType == undefined || cellType == "s"
					|| cellType == "n" || cellType == "b" || cellType == "e")
		}
		_cursorFld.style.left = activeCell.ulX + offsets.l + "px";
		_cursorFld.style.top = activeCell.ulY + offsets.t + "px";
		_cursorFld.style.width = activeCell.lrX - activeCell.ulX + offsets.w
				+ "px";
		_cursorFld.style.height = activeCell.lrY - activeCell.ulY + offsets.h
				+ "px";
		if (defDraw) {
			Jedox.util.setText(_cursorFld, selCelFVal)
		} else {
			while (_cursorFld.childNodes.length >= 1) {
				_cursorFld.removeChild(_cursorFld.firstChild)
			}
			_cursorFld
					.appendChild(_env.selectedCell.firstChild.cloneNode(true))
		}
		Jedox.wss.style.cellTransfer(_cursorFld);
		_cursorFld.style.display = "block"
	};
	this.hide = function() {
		_cursorFld.style.display = "none"
	};
	this.show = function() {
		_cursorFld.style.display = "block"
	};
	function _init() {
		var viewMode = Jedox.wss.grid.viewMode;
		_cursorFld = document.createElement("div");
		_cursorFld.id = "".concat(domID, "_cursorField");
		_cursorFld.ondblclick = function() {
			Jedox.wss.mouse.showCursorLayer("marker_sand_clock");
			var selCellCoords = _env.selectedCellCoords;
			var cbRes = Jedox.wss.app.activeBook.cbFire(selCellCoords[0],
					selCellCoords[1], "dblclick", {
						c : selCellCoords[0],
						r : selCellCoords[1]
					});
			Jedox.wss.mouse.hideCursorLayer();
			if (!cbRes) {
				return false
			}
			Jedox.wss.general
					.setInputMode(_cursorFld.innerHTML != "" ? Jedox.wss.grid.GridMode.EDIT
							: Jedox.wss.grid.GridMode.INPUT);
			Jedox.wss.general.showInputField(null, false, true)
		};
		if (_env.viewMode == viewMode.DESIGNER) {
			_cursorFld.onmousedown = function(ev) {
				if (document.all) {
					ev = window.event
				}
				var selCellCoords = _env.selectedCellCoords;
				if (!Jedox.wss.app.activeBook.cbFire(selCellCoords[0],
						selCellCoords[1], "mousedown", ev)) {
					return false
				}
				if (ev.button == 2
						|| (Ext.isMac && ev.button == 0 && Jedox.wss.app.ctrlKeyPressed)) {
					Jedox.wss.mouse.showMainCntxMenu(ev)
				} else {
					if (_env.inputMode == Jedox.wss.grid.GridMode.DIALOG
							&& Jedox.wss.app.sourceSel) {
						Jedox.wss.mouse.initSourceRange(
								Jedox.wss.app.activeBook.getCellByCoords(
										selCellCoords[0], selCellCoords[1]),
								selCellCoords)
					} else {
						var singleCell = areaPres._selection.isSingleCell();
						areaPres._selection.set(new Jedox.wss.cls.Point(
								selCellCoords[0], selCellCoords[1]),
								new Jedox.wss.cls.Point(selCellCoords[0],
										selCellCoords[1]));
						if (!singleCell) {
							areaPres._selection.draw()
						}
						areaPres.registerForMouseMovement(_env.selectedCell)
					}
				}
			};
			_cursorFld.onmouseover = function() {
				_env.lastCell = _env.selectedCell;
				_env.lastCellCoords = _env.selectedCellCoords
			};
			_cursorFld.className = "activeCellElement cursorField default-format"
		} else {
			if (_env.viewMode == viewMode.USER) {
				_cursorFld.className = "cursorFieldUM default-format"
			}
		}
		_cursorFld.style.display = "none";
		container.appendChild(_cursorFld);
		_env.cursorField = _cursorFld
	}
	_init()
};