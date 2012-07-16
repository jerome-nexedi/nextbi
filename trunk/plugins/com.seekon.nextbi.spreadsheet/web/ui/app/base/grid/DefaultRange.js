Jedox.wss.grid.DefaultRange = (function() {
	return function(selection, startPoint, endPoint) {
		Jedox.wss.grid.DefaultRange.parent.constructor.call(this, selection,
				startPoint, endPoint);
		var that = this;
		for ( var htmlEl, clsName = "defaultRangeBorder", i = 3; i >= 0; --i) {
			htmlEl = this._edgeElems[i] = document.createElement("div");
			htmlEl.className = clsName;
			Jedox.util.addEvent(htmlEl, "mousedown", function(ev) {
				that._onmousedown(ev)
			}, false);
			Jedox.util.addEvent(htmlEl, "mouseup", function(ev) {
				that._onmouseup(ev)
			}, false);
			Jedox.util.addEvent(htmlEl, "mousemove", function(ev) {
				that._onmousemove(ev)
			}, false);
			Jedox.util.addEvent(htmlEl, "mouseout", function(ev) {
				that._onmouseout(ev)
			}, false);
			this._container.appendChild(htmlEl)
		}
		this._cursorField = new Jedox.wss.cls.CursorField(this._selection
				.getDomId(), this._container, this);
		this._fillRange = new Jedox.wss.grid.FillSelection(this._upperLeft,
				this._selection.getBook());
		htmlEl = this._cornerElems[0] = document.createElement("div");
		htmlEl.className = "rangeEdge";
		this._addCursor(htmlEl, 0);
		Jedox.util
				.addEvent(
						htmlEl,
						"mousedown",
						function(ev) {
							var env = Jedox.wss.app.environment, gridMode = Jedox.wss.grid.GridMode;
							if ((env.inputMode == Jedox.wss.grid.GridMode.INPUT || env.inputMode == Jedox.wss.grid.GridMode.EDIT)
									&& Jedox.wss.keyboard.sendInput(
											env.inputField, 13, false)) {
								Jedox.wss.keyboard.cancelInput(true);
								try {
									Jedox.wss.app.currFormula.blur()
								} catch (e) {
								}
							}
							var viewportPos = Jedox.wss.app.activeBook
									.getViewportPos();
							that._fillRange
									.getActiveRange()
									.activate(
											{
												ul : that._upperLeft.clone(),
												lr : that._lowerRight.clone(),
												ulPx : that._ulCorner.clone(),
												lrPx : that._lrCorner.clone(),
												mdLRCornerPx : new Jedox.wss.cls.Point(
														that._cornerElems[0].offsetLeft
																+ that._cornerElems[0].offsetWidth
																+ env.gridScreenCoords[0][0]
																- viewportPos[0][0],
														that._cornerElems[0].offsetTop
																+ that._cornerElems[0].offsetHeight
																+ env.gridScreenCoords[0][1]
																- viewportPos[0][1])
											})
						}, false);
		Jedox.util.addEvent(htmlEl, "mouseover", function(ev) {
			that._mouseOnEdge(ev)
		}, false);
		Jedox.util.addEvent(htmlEl, "mouseout", function(ev) {
			that._mouseOffEdge(ev)
		}, false);
		this._container.appendChild(htmlEl);
		this._bgndElem = document.createElement("div");
		this._bgndElem.className = "rangeBackground";
		this._bgndElem.onmousemove = Jedox.wss.mouse.rangeOverTracking;
		this._bgndElem.onmousedown = Jedox.wss.mouse.mouseOnRangeDown;
		this._bgndMask = document.createElement("div");
		this._bgndMask.className = "rangeMask";
		var vportSize = this._selection.getBook().getViewportSize();
		this._bgndMask.style.cssText = "".concat("width:", vportSize[0] * 2,
				"px;height:", vportSize[1] * 2, "px;left:0px;top:0px;");
		this._bgndElem.appendChild(this._bgndMask);
		this._container.appendChild(this._bgndElem)
	}
})();
Jedox.util.extend(Jedox.wss.grid.DefaultRange, Jedox.wss.grid.Range);
clsRef = Jedox.wss.grid.DefaultRange;
clsRef.prototype.moveTo = function(x, y) {
	x = parseInt(x, 10);
	y = parseInt(y, 10);
	var width = this._lowerRight.getX() - this._upperLeft.getX(), height = this._lowerRight
			.getY()
			- this._upperLeft.getY(), defMaxCoords = Jedox.wss.grid.defMaxCoords;
	if (x < 1 || y < 1 || x + width > defMaxCoords[0]
			|| y + height > defMaxCoords[1]) {
		return
	}
	this._upperLeft.setX(x);
	this._upperLeft.setY(y);
	this._lowerRight.setX(x + width);
	this._lowerRight.setY(y + height);
	this._environment.shared.lastRangeStartCoord = [ this._upperLeft.getX(),
			this._upperLeft.getY() ];
	this._environment.shared.lastRangeEndCoord = [ this._lowerRight.getX(),
			this._lowerRight.getY() ];
	this._activeCell.setX(x);
	this._activeCell.setY(y);
	this._selection.setActiveCell(null, x - Jedox.wss.app.firstRowNumeric + 1,
			y - Jedox.wss.app.firstColumn + 1, false);
	this._updateOffsets()
};
clsRef.prototype._updateRangeSelector = function() {
	this._selection.updateRangeSelector()
};
clsRef.prototype._mouseOnEdgeDown = function(ev) {
	if (document.all) {
		ev = window.event
	}
	var elem = document.all ? ev.srcElement : ev.target;
	switch (elem.style.cursor) {
	case "se-resize":
		this._resizeDirection = Jedox.wss.range.ResizeDirection.SOUTH_EAST;
		break;
	case "sw-resize":
		this._resizeDirection = Jedox.wss.range.ResizeDirection.SOUTH_WEST;
		break;
	case "ne-resize":
		this._resizeDirection = Jedox.wss.range.ResizeDirection.NORTH_EAST;
		break;
	case "nw-resize":
		this._resizeDirection = Jedox.wss.range.ResizeDirection.NORTH_WEST;
		break;
	default:
		this._resizeDirection = Jedox.wss.range.ResizeDirection.SOUTH_EAST
	}
	this._selection.activeToken = this.formulaToken;
	this.registerForMouseMovement(this._environment.shared.lastCell);
	this._latestChangedCell = this._lrCell;
	this.setStartCoords()
};
clsRef.prototype._legacyMouseUp = function() {
	if (this._environment.shared.inputMode == Jedox.wss.grid.GridMode.EDIT) {
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.READY)
	} else {
		if (this._environment.shared.inputMode == Jedox.wss.grid.GridMode.INPUT) {
			Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.EDIT)
		}
	}
	Jedox.wss.general.setCoords();
	this._selection.checkForUndoneMarkers()
};
clsRef.prototype._onmouseup = function(ev) {
	Jedox.wss.app.mimicOvertracking = false;
	if (this._status == Jedox.wss.range.AreaStatus.EXPANDING) {
		this.afterExpanding()
	}
	this._status = Jedox.wss.range.AreaStatus.HOVERING;
	Jedox.wss.app.mouseMovementObserver.unsubscribe(this._move);
	Jedox.wss.app.mouseUpObserver.unsubscribe(this._onmouseup);
	this._unHover(ev);
	this._resizeDirection = Jedox.wss.range.ResizeDirection.NONE;
	this._selection.setMode(Jedox.wss.range.RangeMode.NONE);
	Jedox.wss.app.mouseUpObserver.unsubscribe(this._legacyDraw);
	this._legacyMouseUp();
	this.reposBgndMask(true);
	if (this._doPaste && Jedox.wss.app.clipboard != null) {
		this._selection.paste();
		this._doPaste = false;
		this._environment.shared.copySelection.removeAll();
		Jedox.wss.app.clipboard = null;
		Jedox.wss.action.togglePaste(false)
	}
};
clsRef.prototype._onmousedown = function(ev) {
	var that = this;
	if (document.all) {
		ev = window.event
	}
	this._doCC = true;
	this._status = Jedox.wss.range.AreaStatus.DRAGGING;
	Jedox.wss.app.mimicOvertracking = (Ext.isGecko3 && ev.button == 0);
	this._setMonitorCell(this._environment.shared.lastCell);
	Jedox.wss.app.mouseMovementObserver.subscribe(this._move, this);
	Jedox.wss.app.mouseUpObserver.subscribe(this._onmouseup, this);
	this._selection.activeToken = this.formulaToken;
	this._selection.setMode(Jedox.wss.range.RangeMode.EDIT);
	this.reposBgndMask(false)
};
clsRef.prototype._move = function(ev) {
	if (document.all) {
		ev = window.event
	}
	if (this._doCC) {
		this._doCC = false;
		this._doPaste = true;
		if (ev.ctrlKey) {
			this._selection.copy()
		} else {
			this._selection.cut()
		}
	}
	this._environment.shared.autoScroll.checkAndScroll(ev,
			this._resizeOnScroll, Jedox.wss.grid.scrollType.ALL, null, this);
	this._selection.setActiveRange(this);
	if (this._status == Jedox.wss.range.AreaStatus.DRAGGING) {
		var elemCoords = this._environment.shared.lastCellCoords;
		this._selection.moveTo(elemCoords[0] + this._realCoords[0].getX()
				- this._monitorCellCoords[0], elemCoords[1]
				+ this._realCoords[0].getY() - this._monitorCellCoords[1])
	} else {
		this._resizeRange(ev)
	}
	this.draw();
	this._setMonitorCell(this._environment.shared.lastCell)
};
clsRef.prototype.hide = function() {
	this._switchVisibility(false)
};
clsRef.prototype.show = function() {
	this._switchVisibility(true)
};
clsRef.prototype._switchVisibility = function(rngVis) {
	for ( var i = 0; i < this._edgeElems.length; i++) {
		this._edgeElems[i].style.display = rngVis ? "block" : "none"
	}
	for ( var i = 0; i < this._cornerElems.length; i++) {
		this._cornerElems[i].style.display = rngVis ? "block" : "none"
	}
	if (rngVis) {
		this._cursorField.show()
	} else {
		this._cursorField.hide()
	}
	this._bgndElem.style.display = rngVis ? "block" : "none"
};
clsRef.prototype.isVisible = function() {
	return this._edgeElems[0].style.display == "block"
			&& this._cornerElems[0].style.display == "block"
};
clsRef.prototype.getOffsetsPx = function(useragent) {
	var offsets = [];
	switch (useragent) {
	case "ff":
		offsets = [ {
			left : 0,
			top : -1,
			width : 1,
			height : 0
		}, {
			left : 0,
			top : -2,
			width : 0,
			height : 0
		}, {
			left : -1,
			top : 0,
			width : 0,
			height : 1
		}, {
			left : -2,
			top : 0,
			width : 0,
			height : 0
		} ];
		break;
	case "sf":
		offsets = [ {
			left : 0,
			top : -1,
			width : 1,
			height : 0
		}, {
			left : 0,
			top : -2,
			width : 0,
			height : 0
		}, {
			left : -1,
			top : 0,
			width : 0,
			height : 1
		}, {
			left : -2,
			top : 0,
			width : 0,
			height : 0
		} ];
		break;
	case "ie":
		offsets = [ {
			left : 0,
			top : -1,
			width : 1,
			height : 0
		}, {
			left : 0,
			top : -2,
			width : 0,
			height : 0
		}, {
			left : -1,
			top : 0,
			width : 0,
			height : 1
		}, {
			left : -2,
			top : 0,
			width : 0,
			height : 0
		} ];
		break;
	default:
		offsets = [ {
			left : 0,
			top : 0,
			width : 0,
			height : 0
		}, {
			left : 0,
			top : 0,
			width : 0,
			height : 0
		}, {
			left : 0,
			top : 0,
			width : 0,
			height : 0
		}, {
			left : 0,
			top : 0,
			width : 0,
			height : 0
		} ];
		console.warn("Unkown user agent: ", useragent)
	}
	return offsets
};
clsRef.prototype.draw = function() {
	if (this._status == Jedox.wss.range.AreaStatus.EXPANDING) {
		return
	}
	var ulOffset, lrOffset, pxCoords, book = this._selection.getBook(), activeSheet = Jedox.wss.app.activeSheet, acell = this
			.getActiveCell(), actX = activeSheet._colWidths.getElemAt(acell
			.getX()), actY = activeSheet._rowHeights.getElemAt(acell.getY());
	if (!actX || !actY) {
		actX = acell.getX() + (!actX ? 1 : 0);
		actY = acell.getY() + (!actY ? 1 : 0);
		this._selection.set(new Jedox.wss.cls.Point(actX, actY),
				new Jedox.wss.cls.Point(actX, actY));
		this._selection.draw();
		return
	}
	var acellEl = book.getCellByCoords(acell.getX(), acell.getY());
	if (acellEl) {
		var acellCoords = book.getPixelsByCoords(acell.getX(), acell.getY());
		this._activeCellEl = {
			ulX : acellCoords[0],
			ulY : acellCoords[1],
			lrX : acellCoords[0] + acellEl.offsetWidth,
			lrY : acellCoords[1] + acellEl.offsetHeight
		}
	}
	ulOffset = 1;
	lrOffset = 0;
	this._realCoords = this.getCorners();
	if ((this._ulCell = book.getCellByCoords(this._realCoords[0].getX(),
			this._realCoords[0].getY())) == undefined) {
		pxCoords = book.getPixelsByCoords(this._realCoords[0].getX(),
				this._realCoords[0].getY());
		this._ulCorner.setX(pxCoords[0] - ulOffset);
		this._ulCorner.setY(pxCoords[1] - ulOffset)
	} else {
		this._ulCorner.setX(this._ulCell.offsetLeft - ulOffset);
		this._ulCorner.setY(this._ulCell.parentNode.offsetTop - ulOffset)
	}
	if ((this._lrCell = book.getCellByCoords(this._realCoords[1].getX(),
			this._realCoords[1].getY())) == undefined) {
		pxCoords = book.getPixelsByCoords(this._realCoords[1].getX() + 1,
				this._realCoords[1].getY() + 1);
		this._lrCorner.setX(pxCoords[0]);
		this._lrCorner.setY(pxCoords[1] + lrOffset)
	} else {
		this._lrCorner.setX(this._lrCell.offsetLeft + this._lrCell.offsetWidth);
		this._lrCorner.setY(this._lrCell.parentNode.offsetTop
				+ this._lrCell.offsetHeight + lrOffset)
	}
	this._drawRect();
	this._drawBgnd();
	this.drawActiveCell();
	this._setBorder();
	this._drawCorners();
	Jedox.wss.hb.setAllNormal()
};
clsRef.prototype.reposBgndMask = function(whether) {
	if (whether) {
		if (this._bgndElemParams.w > this._selection.getBook()
				.getViewportSize()[0]
				|| this._bgndElemParams.h > this._selection.getBook()
						.getViewportSize()[1]) {
			this._selection.getBook().scrollObserver.subscribe(
					this._reposBgndMask, this)
		}
	} else {
		this._selection.getBook().scrollObserver
				.unsubscribe(this._reposBgndMask)
	}
};
clsRef.prototype.destruct = function() {
	Jedox.wss.app.mouseMovementObserver.unsubscribe(this._move);
	Jedox.wss.app.mouseUpObserver.unsubscribe(this._onmouseup);
	for ( var i = 3; i >= 0; --i) {
		this._edgeElems[i].parentNode.removeChild(this._edgeElems[i]);
		this._cornerElems[i].parentNode.removeChild(this._cornerElems[i])
	}
};
clsRef.prototype._getColorNumber = function() {
	return "default"
};
clsRef.prototype._drawCorners = function() {
	this._cornerElems[0].style.left = ""
			.concat(this._lrCorner.getX() - 3, "px");
	this._cornerElems[0].style.top = "".concat(this._lrCorner.getY() - 3, "px");
	this._cornerElems[0].style.display = "block"
};
clsRef = null;