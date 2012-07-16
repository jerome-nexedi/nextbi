Jedox.wss.mouse.MouseIsOnCell = function(XPos, YPos, Coords) {
	return (((XPos >= Coords[0][0]) && (XPos <= Coords[0][1])) && ((YPos >= Coords[1][0]) && (YPos <= Coords[1][1])))
};
Jedox.wss.mouse.updateLastCell = function(el, elCoords) {
	var env = Jedox.wss.app.environment, activeBook = Jedox.wss.app.activeBook, mergeInfo = activeBook
			.getMergeInfo(elCoords[0], elCoords[1]);
	if (mergeInfo != undefined && !mergeInfo) {
		env.lastCellCoords = [ mergeInfo[1], mergeInfo[2] ];
		env.lastCell = activeBook.getCellByCoords(mergeInfo[1], mergeInfo[2])
	} else {
		env.lastCell = el;
		env.lastCellCoords = [ elCoords[0], elCoords[1] ]
	}
};
Jedox.wss.mouse.overTracking = function(ev) {
	var activeBook = Jedox.wss.app.activeBook, elCoords;
	if (!Jedox.wss.app.loaded || activeBook == undefined) {
		return
	}
	if (document.all) {
		ev = window.event
	}
	var el = (document.all) ? ev.srcElement : ev.target;
	if (el.tagName != "DIV") {
		el = el.parentNode
	}
	if ((elCoords = activeBook.getCoordsByCell(el)) == undefined) {
		return
	}
	Jedox.wss.mouse.updateLastCell(el, elCoords);
	if (el.tagName == "DIV" && el.className == "gridCell") {
		Jedox.wss.keyboard.preventEvent(ev)
	}
};
Jedox.wss.mouse.rangeOverTracking = function(myEvent) {
	var env = Jedox.wss.app.environment;
	if (env.viewMode == Jedox.wss.grid.viewMode.USER) {
		return
	}
	if (document.all) {
		myEvent = window.event
	}
	var activeBook = Jedox.wss.app.activeBook, myElement = (document.all) ? myEvent.srcElement
			: myEvent.target, rangeStartCoords = env.defaultSelection
			.getActiveRange().getUpperLeft(), lastCellCoords = env.lastCellCoords, targetCoords = activeBook
			.getNeighByOffset(rangeStartCoords.getX(), rangeStartCoords.getY(),
					(document.all ? myEvent.offsetX : myEvent.layerX)
							+ myElement.offsetLeft + 4,
					(document.all ? myEvent.offsetY : myEvent.layerY)
							+ myElement.offsetTop + 4);
	if (lastCellCoords[0] != targetCoords[0]
			|| lastCellCoords[1] != targetCoords[1]) {
		env.lastCell = targetCoords[2];
		env.lastCellCoords = [ targetCoords[0], targetCoords[1] ]
	}
	if (myElement.tagName == "DIV" && myElement.className == "gridCell") {
		Jedox.wss.keyboard.preventEvent(myEvent)
	}
};
Jedox.wss.mouse.mouseonCellDblClick = function(ev) {
	if (document.all) {
		ev = window.event
	}
	var el = (document.all) ? ev.srcElement : ev.target;
	if (el.tagName != "DIV") {
		el = el.parentNode
	}
	var activeBook = Jedox.wss.app.activeBook, cellCoords = activeBook
			.getCoordsByCell(el);
	if (!cellCoords) {
		return true
	}
	Jedox.wss.mouse.showCursorLayer("marker_sand_clock");
	var cbRes = activeBook.cbFire(cellCoords[0], cellCoords[1], "dblclick", {
		c : cellCoords[0],
		r : cellCoords[1]
	});
	Jedox.wss.mouse.hideCursorLayer();
	if (!cbRes) {
		return false
	}
};
Jedox.wss.mouse.mouseOnCellDown = function(myEvent) {
	if (document.all) {
		myEvent = window.event
	}
	var myElement = (document.all) ? myEvent.srcElement : myEvent.target;
	if (myElement.tagName != "DIV") {
		myElement = myElement.parentNode
	}
	var env = Jedox.wss.app.environment, activeBook = Jedox.wss.app.activeBook, elCoords = activeBook
			.getCoordsByCell(myElement);
	if (elCoords == undefined) {
		return
	}
	var cbRes = activeBook.cbFire(elCoords[0], elCoords[1], "mousedown",
			myEvent);
	if (!cbRes) {
		Jedox.wss.keyboard.preventEvent(myEvent);
		return false
	}
	if (env.viewMode == Jedox.wss.grid.viewMode.USER
			&& activeBook.isCellLocked(elCoords[0], elCoords[1])) {
		Jedox.wss.keyboard.cancelInput();
		return
	}
	var value;
	if ((env.inputMode == Jedox.wss.grid.GridMode.EDIT)
			|| (env.inputMode == Jedox.wss.grid.GridMode.INPUT)) {
		value = env.inputField.value;
		if (value.length > 0 && value.substr(0, 1) == "="
				&& env.viewMode != Jedox.wss.grid.viewMode.USER) {
			Jedox.wss.app.lastInputMode = env.inputMode;
			Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.POINT);
			var elemCoords = activeBook.getCoordsByCell(myElement);
			if (env.formulaSelection.activeToken != null) {
				var point, area = env.formulaSelection.getActiveRange();
				area.set(point = new Jedox.wss.cls.Point(elemCoords[0],
						elemCoords[1]), point);
				area.formulaUpdate();
				area.draw()
			} else {
				var currFormula = Jedox.wss.app.currFormula, selection = Jedox.util
						.getSelection(Jedox.wss.app.fromFormulaField ? currFormula
								: env.inputField), selStart = selection.start, selEnd = selection.end;
				if (selStart == 0
						|| value.substr(selStart - 1, 1).match(/^[a-z0-9]$/i) != null) {
					if (env.oldValue != value) {
						Jedox.wss.keyboard.sendInput(env.inputField, 1000)
					} else {
						Jedox.wss.keyboard.cancelInput()
					}
					return Jedox.wss.mouse.mouseOnCellDown(myEvent)
				}
				env.inputField.value = currFormula.value = value.substring(0,
						selStart).concat(
						Jedox.wss.app.numberToLetter[elemCoords[0]],
						elemCoords[1], value.substring(selEnd));
				Jedox.wss.range.drawDependingCells(currFormula.value);
				Jedox.wss.keyboard.setFieldSize();
				env.lastInputValue = currFormula.value;
				for ( var tok, area, areas = env.formulaSelection.getRanges(), i = areas.length - 1; i >= 0; --i) {
					if (selStart >= (tok = (area = areas[i]).formulaToken).start
							&& selStart < tok.end) {
						env.formulaSelection.activeToken = tok;
						env.formulaSelection.setActiveRange(i);
						env.formulaSelection
								.setState(Jedox.wss.range.AreaState.NEW);
						area.repaint();
						break
					}
				}
			}
			Jedox.wss.app.environment.formulaSelection
					.registerForMouseMovement(myElement)
		} else {
			if (Jedox.wss.app.environment.viewMode != Jedox.wss.grid.viewMode.USER) {
				Jedox.wss.app.environment.formulaSelection.removeAll()
			} else {
				Jedox.wss.app.environment.inputField.style.display = "none"
			}
			if (Jedox.wss.app.environment.oldValue != value) {
				Jedox.wss.keyboard.sendInput(
						Jedox.wss.app.environment.inputField, 1000)
			} else {
				Jedox.wss.keyboard.cancelInput()
			}
			Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.READY);
			Jedox.wss.hb.setAllNormal()
		}
	}
	if (Jedox.wss.app.environment.inputMode == Jedox.wss.grid.GridMode.DIALOG
			&& Jedox.wss.app.sourceSel) {
		Jedox.wss.mouse.initSourceRange(myElement, activeBook
				.getCoordsByCell(myElement))
	}
	if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.DESIGNER
			&& Jedox.wss.app.environment.inputMode == Jedox.wss.grid.GridMode.READY) {
		var elementCoords;
		var activeBook = Jedox.wss.app.activeBook;
		if (activeBook != undefined) {
			var elementCoords = activeBook.getCoordsByCell(myElement);
			if (elementCoords == undefined) {
				return
			}
		}
		Jedox.wss.app.environment.defaultSelection.set(new Jedox.wss.cls.Point(
				elementCoords[0], elementCoords[1]), new Jedox.wss.cls.Point(
				elementCoords[0], elementCoords[1]));
		Jedox.wss.app.environment.defaultSelection
				.registerForMouseMovement(myElement);
		Jedox.wss.app.environment.defaultSelection.draw();
		Jedox.wss.app.environment.defaultSelection.getActiveRange()
				.reposBgndMask(false);
		if (myEvent.button == 2
				|| (Ext.isMac && myEvent.button == 0 && Jedox.wss.app.ctrlKeyPressed)) {
			Jedox.wss.mouse.showMainCntxMenu(myEvent)
		}
		Jedox.wss.hb.setAllNormal()
	}
	if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.USER) {
		var selectedCellCoords = activeBook.getCoordsByCell(myElement), mrgInfoSelCell = activeBook
				.getMergeInfo(selectedCellCoords[0], selectedCellCoords[1]);
		env.selectedCell = myElement;
		env.selectedCellCoords = selectedCellCoords;
		env.lastRangeStartCoord = env.lastRangeEndCoord = [
				selectedCellCoords[0], selectedCellCoords[1] ];
		var selCellValue = activeBook.getCellValue(selectedCellCoords[0],
				selectedCellCoords[1]);
		env.selectedCellValue = (selCellValue == undefined) ? "" : selCellValue;
		var actCellCoords = activeBook.getPixelsByCoords(selectedCellCoords[0],
				selectedCellCoords[1]), actCellIncCoords = activeBook
				.getPixelsByCoords(
						selectedCellCoords[0]
								+ (mrgInfoSelCell && mrgInfoSelCell[0] ? mrgInfoSelCell[1]
										: 1),
						selectedCellCoords[1]
								+ (mrgInfoSelCell && mrgInfoSelCell[0] ? mrgInfoSelCell[2]
										: 1));
		activeBook.getCursorField().draw(
				{
					ulX : actCellCoords[0],
					ulY : actCellCoords[1],
					lrX : actCellIncCoords[0],
					lrY : actCellIncCoords[1]
				},
				new Jedox.wss.cls.Point(selectedCellCoords[0],
						selectedCellCoords[1]))
	}
};
Jedox.wss.mouse.initSourceRange = function(cell, ulCoords, lrCoords) {
	if (!lrCoords) {
		lrCoords = ulCoords
	}
	var formSel = Jedox.wss.app.environment.formulaSelection;
	formSel.removeAll();
	formSel.addRange(new Jedox.wss.cls.Point(ulCoords[0], ulCoords[1]),
			new Jedox.wss.cls.Point(lrCoords[0], lrCoords[1]));
	formSel.setActiveRange(formSel.getRanges().length - 1);
	formSel.setState(Jedox.wss.range.AreaState.NEW);
	if (cell) {
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.POINT);
		formSel.registerForMouseMovement(cell)
	}
	formSel.draw()
};
Jedox.wss.mouse.mouseOnRangeDown = function(myEvent) {
	if (document.all) {
		myEvent = window.event
	}
	var env = Jedox.wss.app.environment, myElement = (document.all) ? myEvent.srcElement
			: myEvent.target, srcRngMode = env.inputMode == Jedox.wss.grid.GridMode.DIALOG
			&& Jedox.wss.app.sourceSel;
	if (!srcRngMode && myEvent.button == 2
			&& myElement.className == "rangeMask") {
		Jedox.wss.mouse.showMainCntxMenu(myEvent);
		Jedox.wss.keyboard.preventEvent(myEvent);
		return
	}
	var activeBook = Jedox.wss.app.activeBook, rangeStartCoords = env.defaultSelection
			.getActiveRange().getUpperLeft(), targetCoords = activeBook
			.getNeighByOffset(rangeStartCoords.getX(), rangeStartCoords.getY(),
					((document.all) ? myEvent.offsetX : myEvent.layerX)
							+ myElement.offsetLeft + 4,
					((document.all) ? myEvent.offsetY : myEvent.layerY)
							+ myElement.offsetTop + 4), tX = targetCoords[0], tY = targetCoords[1], targetMrgInfo = activeBook
			.getMergeInfo(tX, tY);
	if (targetMrgInfo && !targetMrgInfo[0]) {
		tX = targetMrgInfo[1], tY = targetMrgInfo[2]
	}
	if (env.inputMode == Jedox.wss.grid.GridMode.READY) {
		env.defaultSelection.set(new Jedox.wss.cls.Point(tX, tY),
				new Jedox.wss.cls.Point(tX, tY));
		if (env.viewMode == Jedox.wss.grid.viewMode.DESIGNER) {
			env.defaultSelection.registerForMouseMovement(targetCoords[2])
		}
		env.defaultSelection.draw();
		env.defaultSelection.getActiveRange().reposBgndMask(false)
	}
	if (srcRngMode) {
		Jedox.wss.mouse.initSourceRange(activeBook.getCellByCoords(
				targetCoords[0], targetCoords[1]), targetCoords)
	}
	if ((myElement.tagName == "DIV") && (myElement.className == "rangeMask")) {
		Jedox.wss.keyboard.preventEvent(myEvent)
	}
};
Jedox.wss.mouse.checkForResize = function(MyEvent) {
	var Sheet, Column, Row, RowNumeric, MyElement, elemData;
	if (document.all) {
		MyEvent = window.event
	}
	MyElement = (document.all) ? MyEvent.srcElement : MyEvent.target;
	if (Jedox.wss.app.environment.inputMode == Jedox.wss.grid.GridMode.EDIT) {
		return
	}
	function checkPosition() {
		var XOffset = 5;
		var YOffset = 4;
		var activeBook = Jedox.wss.app.activeBook;
		if (MyElement.parentNode.className == "gridColHdrsIC") {
			var resizeIndex = (MyElement.className + "").indexOf(" col_resize");
			var targetX = (document.all) ? MyEvent.offsetX : MyEvent.layerX;
			if ((targetX <= XOffset && (activeBook.getCoordByHdr(
					Jedox.wss.grid.headerType.COLUMN, MyElement) != 1))
					|| (MyElement.offsetWidth - targetX <= XOffset)) {
				Jedox.wss.app.environment.mousePosition = "colResize";
				if (resizeIndex == -1) {
					MyElement.className += " col_resize"
				}
			} else {
				Jedox.wss.app.environment.mousePosition = "colMark";
				if (resizeIndex > -1) {
					MyElement.className = MyElement.className.substring(0,
							resizeIndex)
				}
			}
		}
		if (MyElement.parentNode.className == "gridRowHdrsIC") {
			var resizeIndex = (MyElement.className + "").indexOf(" row_resize");
			var targetY = (document.all) ? MyEvent.offsetY : MyEvent.layerY;
			if ((targetY <= YOffset && (activeBook.getCoordByHdr(
					Jedox.wss.grid.headerType.ROW, MyElement) != 1))
					|| (MyElement.offsetHeight - targetY <= YOffset)) {
				Jedox.wss.app.environment.mousePosition = "rowResize";
				if (resizeIndex == -1) {
					MyElement.className += " row_resize"
				}
			} else {
				Jedox.wss.app.environment.mousePosition = "rowMark";
				if (resizeIndex > -1) {
					MyElement.className = MyElement.className.substring(0,
							resizeIndex)
				}
			}
		}
	}
	if ((Jedox.wss.app.xPos != MyEvent.clientX)
			|| (Jedox.wss.app.yPos != MyEvent.clientY)) {
		if (Jedox.wss.app.mouseButton1Down) {
			if (Jedox.wss.app.environment.headerMarkMode === "") {
				Jedox.wss.mouse.fetchGlobalMouseMove(MyEvent)
			}
		} else {
			Jedox.wss.app.xPos = MyEvent.clientX;
			Jedox.wss.app.yPos = MyEvent.clientY;
			checkPosition()
		}
	}
};
Jedox.wss.mouse.fetchMove = function(MouseEvent) {
	if (Jedox.wss.app.maybeDragging
			|| Jedox.wss.app.environment.headerResizeType == Jedox.wss.grid.headerType.COLUMN
			|| Jedox.wss.app.environment.headerResizeType == Jedox.wss.grid.headerType.ROW) {
		return
	}
	var Coords = null;
	if (Jedox.wss.app.environment.lastCell === null) {
		Jedox.wss.app.xPos = MouseEvent.clientX;
		Jedox.wss.app.yPos = MouseEvent.clientY
	} else {
		Coords = Jedox.wss.range
				.getCellCoord(Jedox.wss.app.environment.lastCell);
		if (!(Jedox.wss.mouse.MouseIsOnCell(MouseEvent.clientX,
				MouseEvent.clientY, Coords))) {
			Jedox.wss.app.xPos = MouseEvent.clientX;
			Jedox.wss.app.yPos = MouseEvent.clientY
		}
	}
};
Jedox.wss.mouse.calcGridScreenCoords = function(ev, el, offsets) {
	if (!offsets) {
		offsets = [ 0, 0 ]
	}
	var viewportPos = Jedox.wss.app.activeBook.getViewportPos(), gridScreenX = ev.clientX
			- (el.offsetLeft + (document.all ? ev.offsetX : ev.layerX)
					+ offsets[0] - viewportPos[0][0]), gridScreenY = ev.clientY
			- ((el.className == "gridCell" ? el.parentNode.offsetTop
					: el.offsetTop)
					+ (document.all ? ev.offsetY : ev.layerY) + offsets[1] - viewportPos[0][1]);
	Jedox.wss.app.environment.gridScreenCoords = [
			[ gridScreenX, gridScreenY ],
			[ gridScreenX + viewportPos[1][0] - viewportPos[0][0],
					gridScreenY + viewportPos[1][1] - viewportPos[0][1] ],
			[
					(document.all) ? document.body.clientWidth
							: window.innerWidth,
					(document.all) ? document.body.clientHeight
							: window.innerHeight ] ]
};
Jedox.wss.mouse.fetchGlobalMouseDown = function(myMouseEvent) {
	Jedox.wss.app.mouseDownObserver.notify(this, myMouseEvent);
	if (!Jedox.wss.app.loaded) {
		return
	}
	if (document.all) {
		myMouseEvent = window.event
	}
	var mouseEl = (document.all) ? myMouseEvent.srcElement
			: myMouseEvent.target, trigCls = [ "gridCell", "rangeEdge",
			"HBRangeBorder", "HBSubDataCell" ];
	if (trigCls.indexOf(mouseEl.className) >= 0) {
		Jedox.wss.mouse.calcGridScreenCoords(myMouseEvent, mouseEl)
	}
	Jedox.wss.app.mouseButton1Down = false;
	Jedox.wss.app.mouseButton2Down = false;
	if ((document.all && myMouseEvent.button == 1)
			|| (myMouseEvent.button === 0)) {
		Jedox.wss.app.mouseButton1Down = true
	} else {
		if (myMouseEvent.button == 2) {
			Jedox.wss.app.mouseButton2Down = true
		}
	}
	try {
		var ParentElementId = mouseEl.parentNode.childNodes[0].id;
		if ((typeof (ParentElementId) != "undefined")
				&& (ParentElementId.length > 5)
				&& (ParentElementId.substring(0, 6) != "column")) {
			Jedox.wss.app.maybeDragging = true
		} else {
			Jedox.wss.app.maybeDragging = false
		}
	} catch (Exception) {
		Jedox.wss.app.maybeDragging = true
	}
};
Jedox.wss.mouse.fetchGlobalMouseUp = function(myMouseEvent) {
	Jedox.wss.app.mouseUpObserver.notify(this, myMouseEvent);
	if (!Jedox.wss.app.loaded) {
		return
	}
	if (document.all) {
		myMouseEvent = window.event
	}
	var env = Jedox.wss.app.environment;
	if (env == null) {
		return
	}
	if ((document.all && myMouseEvent.button == 1)
			|| (myMouseEvent.button === 0)) {
		Jedox.wss.app.mouseButton1Down = false;
		Jedox.wss.app.mouseButton2Down = false;
		if (env.headerResizeType != Jedox.wss.grid.headerType.NONE) {
			Jedox.wss.mouse.stopHeaderResize()
		}
	}
	Jedox.wss.app.maybeDragging = false;
	Jedox.wss.app.mouseButton2Down = false;
	if (env.headerMarkMode.length > 0) {
		Jedox.wss.mouse.hideCursorLayer()
	}
	env.headerMarkMode = "";
	env.headerSelected = null;
	if (env.viewMode != Jedox.wss.grid.viewMode.USER) {
		env.defaultSelection.getActiveRange().reposBgndMask(true);
		Jedox.wss.wsel.moveSave()
	}
};
Jedox.wss.mouse.fetchGlobalMouseMove = function(MyMouseEvent) {
	Jedox.wss.app.mouseMovementObserver.notify(this, MyMouseEvent);
	if (!Jedox.wss.app.loaded) {
		return
	}
	if (Jedox.wss.app.mimicOvertracking) {
		var elCoords = Jedox.wss.mouse.getGridPos(MyMouseEvent);
		if (elCoords[0] > 0 && elCoords[1] > 0) {
			Jedox.wss.mouse.updateLastCell(elCoords[2], [ elCoords[0],
					elCoords[1] ])
		}
	}
	if (document.all) {
		MyMouseEvent = window.event
	}
	var i;
	var elem = (document.all) ? MyMouseEvent.srcElement : MyMouseEvent.target;
	var env = Jedox.wss.app.environment;
	if (env == null) {
		return
	}
	Jedox.wss.app.xPos = MyMouseEvent.clientX;
	Jedox.wss.app.yPos = MyMouseEvent.clientY;
	var hdrResType = env.headerResizeType;
	if (hdrResType == Jedox.wss.grid.headerType.COLUMN) {
		Jedox.wss.mouse.doHeaderResize(hdrResType, MyMouseEvent.clientX)
	} else {
		if (hdrResType == Jedox.wss.grid.headerType.ROW) {
			Jedox.wss.mouse.doHeaderResize(hdrResType, MyMouseEvent.clientY)
		}
	}
	if (env.headerMarkMode == "column" || env.headerMarkMode == "row") {
		env.defaultSelection.resizeByHeader(MyMouseEvent)
	}
};
Jedox.wss.mouse.appendtoScrollwheel = function() {
	if (window.addEventListener) {
		window.addEventListener("DOMMouseScroll", Jedox.wss.mouse.fetchWheel,
				false)
	}
	window.onmousewheel = document.onmousewheel = Jedox.wss.mouse.fetchWheel
};
Jedox.wss.mouse.fetchWheel = function(ev) {
	if (Jedox.wss.app.environment.inputMode != Jedox.wss.grid.GridMode.DIALOG) {
		var value = 0;
		if (!ev) {
			ev = window.event
		}
		if (ev.wheelDelta) {
			value = ev.wheelDelta;
			if (!(typeof HTMLElement != "undefined" && HTMLElement.prototype)) {
				value *= -1
			}
		} else {
			if (ev.detail) {
				value = ev.detail
			}
		}
		if (value > 0) {
			Jedox.wss.sheet.scrollYBy(1, Jedox.wss.grid.ScrollDirection.DOWN)
		} else {
			if (value < 0) {
				Jedox.wss.sheet.scrollYBy(1, Jedox.wss.grid.ScrollDirection.UP)
			}
		}
		Jedox.wss.keyboard.preventKeyEvent(ev)
	}
};
Jedox.wss.mouse.showCursorLayer = function(cls) {
	var cursorMarker = document.getElementById("CursorMarker");
	cursorMarker.style.top = "0px";
	cursorMarker.style.left = "0px";
	cursorMarker.style.width = (document.all) ? document.body.clientWidth
			: window.innerWidth + "px";
	cursorMarker.style.height = (document.all) ? document.body.clientHeight
			: window.innerHeight + "px";
	cursorMarker.className = cls;
	cursorMarker.style.display = "block"
};
Jedox.wss.mouse.hideCursorLayer = function() {
	var cursorMarker = document.getElementById("CursorMarker");
	cursorMarker.style.width = "0px";
	cursorMarker.style.height = "0px";
	cursorMarker.style.display = "none"
};
Jedox.wss.mouse.startHeaderResize = function(type, elem, coord, cursorOffset) {
	var hdrType = Jedox.wss.grid.headerType;
	var activeBook = Jedox.wss.app.activeBook;
	var gridIC = activeBook.getBookIC();
	var winID = activeBook.getDomId();
	Jedox.wss.app.environment.headerResizeType = type;
	Jedox.wss.app.environment.headerResizeCoord = coord;
	var startMarkerLeft = (type == hdrType.COLUMN) ? elem.offsetLeft : 0;
	var startMarkerTop = (type == hdrType.ROW) ? elem.offsetTop : -1;
	var stopMarkerLeft = (type == hdrType.COLUMN) ? elem.offsetLeft
			+ elem.offsetWidth : 0;
	var stopMarkerTop = (type == hdrType.ROW) ? elem.offsetTop
			+ elem.offsetHeight : -1;
	var markerHeight = ((type == hdrType.COLUMN) ? gridIC.offsetHeight : 1)
			+ "px";
	var markerWidth = ((type == hdrType.ROW) ? gridIC.offsetWidth : 1) + "px";
	var borderTopWidth = (type == hdrType.ROW) ? "1px" : "0px";
	var borderLeftWidth = (type == hdrType.COLUMN) ? "1px" : "0px";
	var startMarker = document.getElementById("".concat(winID, "_startMarker"));
	startMarker.style.left = startMarkerLeft + "px";
	startMarker.style.top = startMarkerTop + "px";
	startMarker.style.height = markerHeight;
	startMarker.style.width = markerWidth;
	startMarker.style.borderTopWidth = borderTopWidth;
	startMarker.style.borderLeftWidth = borderLeftWidth;
	startMarker.style.display = "block";
	var stopMarker = document.getElementById("".concat(winID, "_stopMarker"));
	stopMarker.style.left = stopMarkerLeft + "px";
	stopMarker.style.top = stopMarkerTop + "px";
	stopMarker.style.height = markerHeight;
	stopMarker.style.width = markerWidth;
	stopMarker.style.borderTopWidth = borderTopWidth;
	stopMarker.style.borderLeftWidth = borderLeftWidth;
	stopMarker.style.display = "block";
	this.showCursorLayer((type == hdrType.ROW) ? "marker_row_resize"
			: "marker_col_resize");
	var viewportPos = Jedox.wss.app.activeBook.getViewportPos();
	if (type == hdrType.ROW) {
		Jedox.wss.app.environment.headerResize = [ startMarkerTop,
				stopMarkerTop,
				cursorOffset - (stopMarkerTop - viewportPos[0][1]),
				viewportPos[0][1], viewportPos[1][1], stopMarkerTop ]
	} else {
		Jedox.wss.app.environment.headerResize = [ startMarkerLeft,
				stopMarkerLeft,
				cursorOffset - (stopMarkerLeft - viewportPos[0][0]),
				viewportPos[0][0], viewportPos[1][0], stopMarkerLeft ]
	}
};
Jedox.wss.mouse.doHeaderResize = function(type, cursorOffset) {
	var hdrRes = Jedox.wss.app.environment.headerResize;
	var stopMarker = document.getElementById("".concat(Jedox.wss.app.activeBook
			.getDomId(), "_stopMarker"));
	var newPos = cursorOffset - hdrRes[2] + hdrRes[3];
	if (newPos > hdrRes[4]) {
		newPos = hdrRes[1]
	} else {
		if (newPos < hdrRes[0] + 5) {
			newPos = hdrRes[0] + 5
		}
	}
	if (type == Jedox.wss.grid.headerType.COLUMN) {
		stopMarker.style.left = newPos + "px"
	} else {
		stopMarker.style.top = newPos + "px"
	}
	hdrRes[5] = newPos
};
Jedox.wss.mouse.stopHeaderResize = function() {
	var env = Jedox.wss.app.environment;
	var hdrRes = env.headerResize;
	var resizedHdr = env.headerResizeCoord;
	var winID = Jedox.wss.app.activeBook.getDomId();
	var startMarker = document.getElementById("".concat(winID, "_startMarker"));
	var stopMarker = document.getElementById("".concat(winID, "_stopMarker"));
	var hdrCoords = env.defaultSelection.getSelHeaderFromRange(
			env.headerResizeType, resizedHdr);
	if (hdrRes[1] != hdrRes[5]) {
		Jedox.wss.app.activeBook.resizeColRow(env.headerResizeType, hdrCoords,
				hdrRes[5] - hdrRes[0]);
		Jedox.wss.hb.setAllNormal(null, true)
	}
	startMarker.style.display = "none";
	stopMarker.style.display = "none";
	this.hideCursorLayer();
	env.headerResizeType = Jedox.wss.grid.headerType.NONE;
	env.headerResize = []
};
Jedox.wss.mouse.autoSizeCell = function(ev) {
	var mousePosition = Jedox.wss.app.environment.mousePosition;
	if (!(mousePosition == "colResize" || mousePosition == "rowResize")) {
		return
	}
	if (document.all) {
		ev = window.event
	}
	if (!((document.all && ev.button == 1) || (ev.button === 0))) {
		return
	}
	var myElement = (document.all) ? ev.srcElement : ev.target;
	var XOffset = 5;
	var YOffset = 5;
	var activeBook = Jedox.wss.app.activeBook;
	var headerType = Jedox.wss.grid.headerType;
	var hdrCoords = undefined;
	if (mousePosition == "colResize") {
		var selHdr = activeBook.getCoordByHdr(headerType.COLUMN, myElement);
		var targetOffsetX = (document.all) ? ev.offsetX : ev.layerX;
		if (targetOffsetX <= XOffset) {
			--selHdr
		}
		hdrCoords = Jedox.wss.app.environment.defaultSelection
				.getSelHeaderFromRange(headerType.COLUMN, selHdr)
	} else {
		if (mousePosition == "rowResize") {
			var selHdr = activeBook.getCoordByHdr(headerType.ROW, myElement);
			var targetOffsetY = (document.all) ? ev.offsetY : ev.layerY;
			if (targetOffsetY <= YOffset) {
				--selHdr
			}
			hdrCoords = Jedox.wss.app.environment.defaultSelection
					.getSelHeaderFromRange(headerType.ROW, selHdr)
		}
	}
	Jedox.wss.app.activeBook.autofitColRow(
			mousePosition == "colResize" ? 0 : 1, hdrCoords)
};
Jedox.wss.mouse.showMainCntxMenu = function(ev) {
	var defMaxCoords = Jedox.wss.grid.defMaxCoords, env = Jedox.wss.app.environment, selEndCoord = env.lastRangeEndCoord, selType = (selEndCoord[0] == defMaxCoords[0] && selEndCoord[1] == defMaxCoords[1]) ? "all"
			: (selEndCoord[0] == defMaxCoords[0]) ? "row"
					: (selEndCoord[1] == defMaxCoords[1]) ? "col" : null, insItem, delItem;
	if (selType == null) {
		insItem = new Ext.menu.Item(
				{
					text : "Insert".localize(),
					menu : {
						items : [
								{
									text : "Shift cells right".localize(),
									handler : function() {
										Jedox.wss.sheet
												.ins(Jedox.wss.sheet.insDelMode.SHIFT_HOR)
									}
								},
								{
									text : "Shift cells down".localize(),
									handler : function() {
										Jedox.wss.sheet
												.ins(Jedox.wss.sheet.insDelMode.SHIFT_VER)
									}
								},
								{
									text : "Entire row".localize(),
									iconCls : "ico_ins_row",
									handler : function() {
										Jedox.wss.action.insDelRowCol("ins",
												"row")
									}
								},
								{
									text : "Entire column".localize(),
									iconCls : "ico_ins_column",
									handler : function() {
										Jedox.wss.action.insDelRowCol("ins",
												"col")
									}
								} ]
					}
				});
		delItem = new Ext.menu.Item(
				{
					text : "Delete".localize(),
					menu : {
						items : [
								{
									text : "Shift cells left".localize(),
									handler : function() {
										Jedox.wss.sheet
												.del(Jedox.wss.sheet.insDelMode.SHIFT_HOR)
									}
								},
								{
									text : "Shift cells up".localize(),
									handler : function() {
										Jedox.wss.sheet
												.del(Jedox.wss.sheet.insDelMode.SHIFT_VER)
									}
								},
								{
									text : "Entire row".localize(),
									iconCls : "ico_del_row",
									handler : function() {
										Jedox.wss.action.insDelRowCol("del",
												"row")
									}
								},
								{
									text : "Entire column".localize(),
									iconCls : "ico_del_column",
									handler : function() {
										Jedox.wss.action.insDelRowCol("del",
												"col")
									}
								} ]
					}
				})
	} else {
		insItem = new Ext.menu.Item( {
			text : "Insert".localize(),
			handler : function() {
				Jedox.wss.action.insDelRowCol("ins", selType)
			},
			disabled : (selType == "all")
		});
		delItem = new Ext.menu.Item( {
			text : "Delete".localize(),
			handler : function() {
				Jedox.wss.action.insDelRowCol("del", selType)
			},
			disabled : (selType == "all")
		})
	}
	var clpExs = Jedox.wss.app.clipboard == null ? false : true, mainCntxMenu = new Ext.menu.Menu(
			{
				id : "chartContextMenu",
				cls : "default-format-window",
				enableScrolling : false,
				listeners : {
					hide : function(menu) {
						menu.destroy()
					}
				},
				items : [
						{
							text : "Cut".localize(),
							iconCls : "icon_cut",
							handler : function() {
								Jedox.wss.action.cut(false)
							}
						},
						{
							text : "Copy".localize(),
							iconCls : "icon_copy",
							handler : function() {
								Jedox.wss.action.copy(false)
							}
						},
						{
							text : "Paste".localize(),
							iconCls : "icon_paste",
							disabled : !clpExs,
							handler : Jedox.wss.action.paste
						},
						{
							text : "Paste Special".localize().concat("..."),
							disabled : !clpExs,
							handler : function() {
								Jedox.wss.app
										.load(
												Jedox.wss.app.dynJSRegistry.pasteSpecial,
												[])
							}
						},
						"-",
						insItem,
						delItem,
						"-",
						{
							id : "formatCellsContext",
							text : "Format Cells".localize().concat("..."),
							iconCls : "icon_edit",
							handler : function() {
								Jedox.wss.app
										.load(
												Jedox.wss.app.dynJSRegistry.formatCells,
												[])
							}
						} ].concat(Jedox.wss.hl.rangeContextMenu(ev))
			});
	if (env.selectedCellFormula.indexOf("=SPARK") == 0) {
		mainCntxMenu.insert(7, new Ext.menu.Item( {
			text : "Edit Micro Chart".localize().concat("..."),
			iconCls : "icon_insert_chart",
			handler : function() {
				Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.microChart,
						[ true ])
			}
		}))
	}
	mainCntxMenu.showAt( [ ev.clientX, ev.clientY ])
};
Jedox.wss.mouse.getGridPos = function(ev) {
	var env = Jedox.wss.app.environment, activeBook = Jedox.wss.app.activeBook, gridScreenCoords = env.gridScreenCoords, gridPosOffset = env.gridPosOffset, firstVCell = activeBook
			.getCoordsFirstVCell();
	if (ev.clientX < gridScreenCoords[0][0]
			|| ev.clientX > gridScreenCoords[1][0]
			|| ev.clientY < gridScreenCoords[0][1]
			|| ev.clientY > gridScreenCoords[1][1]) {
		return [ 0, 0 ]
	}
	return activeBook.getNeighByOffset(firstVCell[0], firstVCell[1], ev.clientX
			- gridScreenCoords[0][0] + gridPosOffset[0], ev.clientY
			- gridScreenCoords[0][1] + gridPosOffset[1])
};
Jedox.wss.mouse.mimicCellMouseEvent = function(x, y, evName) {
	var activeBook = Jedox.wss.app.activeBook, cbScrollTo = function() {
		var target = activeBook.getCellByCoords(x, y);
		if (document.dispatchEvent) {
			var oEvent = document.createEvent("MouseEvents");
			oEvent.initMouseEvent(evName, true, true, window, 1, 1, 1, 1, 1,
					false, false, false, false, 0, target);
			target.dispatchEvent(oEvent)
		} else {
			if (document.fireEvent) {
				target.fireEvent("on".concat(evName))
			}
		}
	};
	if (!activeBook.isCellVisible(x, y)) {
		if (activeBook._scrollOpPending) {
			return
		}
		activeBook.scrollTo( [ this, cbScrollTo ], x, y, true, false)
	} else {
		cbScrollTo()
	}
};
Jedox.wss.mouse.regHyperlinkHandlers = function() {
	Jedox.wss.grid.cbReg("hl", [ this, Jedox.wss.hl.follow ])
};