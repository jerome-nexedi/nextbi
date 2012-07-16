Jedox.wss.grid.DynarangeSubRange = (function() {
	return function(parent, selection, startPoint, endPoint, parentStartPoint,
			parentEndPoint) {
		Jedox.wss.grid.DynarangeSubRange.parent.constructor.call(this,
				selection, startPoint, endPoint);
		this._parent = parent;
		this._parentStartPoint = parentStartPoint.clone();
		this._parentEndPoint = parentEndPoint.clone();
		this._cntrlElems = {};
		this._mode = Jedox.wss.range.RangeMode.EDIT;
		this.parentOffset = {
			top : this._upperLeft.getY() - parentStartPoint.getY(),
			left : this._upperLeft.getX() - parentStartPoint.getX()
		};
		var that = this;
		for ( var htmlEl, clsName = "HBSubRangeBorder", i = 3; i >= 0; --i) {
			htmlEl = this._edgeElems[i] = document.createElement("div");
			htmlEl.className = clsName;
			Jedox.util.addEvent(htmlEl, "mousedown", function(ev) {
				if (parent.chkRightClick.call(parent, ev)) {
					that._onmousedown(ev)
				}
			}, false);
			Jedox.util.addEvent(htmlEl, "mouseup", function(ev) {
				that._onmouseup(ev);
				parent.save(false)
			}, false);
			Jedox.util.addEvent(htmlEl, "mousemove", function(ev) {
				that._onmousemove(ev)
			}, false);
			Jedox.util.addEvent(htmlEl, "mouseout", function(ev) {
				that._onmouseout(ev)
			}, false);
			this._container.appendChild(htmlEl)
		}
		this._cntrlElems.list = document.createElement("div");
		this._cntrlElems.list.className = "HBSubList";
		Jedox.util.addEvent(this._cntrlElems.list, "mousedown", function(ev) {
			if (parent.chkRightClick.call(parent, ev)) {
				that._openListDlg(ev)
			}
		}, false);
		Jedox.util.addEvent(this._cntrlElems.list, "mouseover", function(ev) {
			that._listBtnMouseOver(ev)
		}, false);
		this._container.appendChild(this._cntrlElems.list);
		this._dataCell = document.createElement("div");
		this._dataCell.className = "HBSubDataCell";
		Jedox.util.addEvent(this._dataCell, "mousedown", function(ev) {
			if (parent.chkRightClick.call(parent, ev)) {
				that._dataCellMouseDown(ev)
			}
		}, false);
		Jedox.util.addEvent(this._dataCell, "mouseover", function(ev) {
			that._dataCellMouseOver(ev)
		}, false);
		this._container.appendChild(this._dataCell);
		this.draw()
	}
})();
Jedox.util.extend(Jedox.wss.grid.DynarangeSubRange, Jedox.wss.grid.Range);
clsRef = Jedox.wss.grid.DynarangeSubRange;
clsRef.prototype.getOffsetsPx = function(useragent) {
	var offsets = [];
	switch (useragent) {
	case "ff":
		offsets = [ {
			left : -5,
			top : -6,
			width : 6,
			height : 0,
			listTop : 2,
			listLeftWhenRight : 0,
			listLeftWhenLeft : 1
		}, {
			left : -5,
			top : 0,
			width : 6,
			height : 0
		}, {
			left : -5,
			top : 0,
			width : 0,
			height : 0
		}, {
			left : 1,
			top : -6,
			width : 0,
			height : 12
		} ];
		break;
	case "sf":
		offsets = [ {
			left : -5,
			top : -6,
			width : 6,
			height : 0,
			listTop : 2,
			listLeftWhenRight : 0,
			listLeftWhenLeft : 1
		}, {
			left : -5,
			top : 0,
			width : 6,
			height : 0
		}, {
			left : -5,
			top : 0,
			width : 0,
			height : 0
		}, {
			left : 1,
			top : -6,
			width : 0,
			height : 12
		} ];
		break;
	case "ie":
		offsets = [ {
			left : -5,
			top : -6,
			width : 6,
			height : 0,
			listTop : 2,
			listLeftWhenRight : 0,
			listLeftWhenLeft : 1
		}, {
			left : -5,
			top : 0,
			width : 6,
			height : 0
		}, {
			left : -5,
			top : 0,
			width : 0,
			height : 0
		}, {
			left : 1,
			top : -6,
			width : 0,
			height : 12
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
	var ulOffset = 1, lrOffset = 1, pxCoords, book = this._selection.getBook();
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
	this._drawRect(6);
	this._setBorder();
	this._drawControls();
	this._drawDataCell()
};
clsRef.prototype._drawRect = function(brdSize) {
	var offsets = this.getOffsetsPx(Jedox.wss.app.browser);
	if (brdSize == undefined) {
		brdSize = 5
	}
	this._edgeElems[0].style.left = "".concat(this._ulCorner.getX()
			+ offsets[0].left, "px");
	this._edgeElems[0].style.top = "".concat(this._ulCorner.getY()
			+ offsets[0].top, "px");
	this._edgeElems[0].style.width = "".concat(this._lrCorner.getX()
			- this._ulCorner.getX() + offsets[0].width, "px");
	this._edgeElems[0].style.height = "".concat(brdSize + offsets[0].height,
			"px");
	this._edgeElems[0].style.display = "block";
	this._edgeElems[1].style.left = "".concat(this._ulCorner.getX()
			+ offsets[1].left, "px");
	this._edgeElems[1].style.top = "".concat(this._lrCorner.getY()
			+ offsets[1].top, "px");
	this._edgeElems[1].style.width = "".concat(this._lrCorner.getX()
			- this._ulCorner.getX() + offsets[1].width, "px");
	this._edgeElems[1].style.height = "".concat(brdSize + offsets[1].height,
			"px");
	this._edgeElems[1].style.display = "block";
	this._edgeElems[2].style.left = "".concat(this._ulCorner.getX()
			+ offsets[2].left, "px");
	this._edgeElems[2].style.top = "".concat(this._ulCorner.getY()
			+ offsets[2].top, "px");
	this._edgeElems[2].style.width = ""
			.concat(brdSize + offsets[2].width, "px");
	this._edgeElems[2].style.height = "".concat(this._lrCorner.getY()
			- this._ulCorner.getY() + offsets[2].height, "px");
	this._edgeElems[2].style.display = "block";
	this._edgeElems[3].style.left = "".concat(this._lrCorner.getX()
			+ offsets[3].left, "px");
	this._edgeElems[3].style.top = "".concat(this._ulCorner.getY()
			+ offsets[3].top, "px");
	this._edgeElems[3].style.width = ""
			.concat(brdSize + offsets[3].width, "px");
	this._edgeElems[3].style.height = "".concat(this._lrCorner.getY()
			- this._ulCorner.getY() + offsets[3].height, "px");
	this._edgeElems[3].style.display = "block"
};
clsRef.prototype.updateParentCoords = function(startPoint, endPoint) {
	this._parentStartPoint = startPoint.clone();
	this._parentEndPoint = endPoint.clone();
	if (this._parentStartPoint.getX() + this.parentOffset.left > this._parentEndPoint
			.getX()) {
		this._upperLeft.setX(this._parentEndPoint.getX());
		this.parentOffset.left = this._parentEndPoint.getX()
				- this._parentStartPoint.getX()
	} else {
		this._upperLeft.setX(this._parentStartPoint.getX()
				+ this.parentOffset.left)
	}
	if (this._parentStartPoint.getY() + this.parentOffset.top > this._parentEndPoint
			.getY()) {
		this._upperLeft.setY(this._parentEndPoint.getY());
		this.parentOffset.top = this._parentEndPoint.getY()
				- this._parentStartPoint.getY()
	} else {
		this._upperLeft.setY(this._parentStartPoint.getY()
				+ this.parentOffset.top)
	}
	this._lowerRight = this._upperLeft.clone();
	this.draw()
};
clsRef.prototype._drawControls = function() {
	var offsets = this.getOffsetsPx(Jedox.wss.app.browser), cntrlStyle = this._cntrlElems.list.style, cntrlWidth = 19;
	if (this._parentStartPoint.getX() == this._parentEndPoint.getX()) {
		cntrlStyle.left = "".concat(this._lrCorner.getX() - cntrlWidth
				+ offsets[0].listLeftWhenRight, "px")
	} else {
		if (this._parentEndPoint.getX() == this._lowerRight.getX()) {
			cntrlStyle.left = "".concat(this._ulCorner.getX() - cntrlWidth
					+ offsets[0].listLeftWhenLeft, "px")
		} else {
			cntrlStyle.left = "".concat(this._lrCorner.getX()
					+ offsets[0].listLeftWhenRight, "px")
		}
	}
	cntrlStyle.top = ""
			.concat(this._ulCorner.getY() + offsets[0].listTop, "px");
	cntrlStyle.display = "block"
};
clsRef.prototype._drawDataCell = function() {
	var viewMode = Jedox.wss.grid.viewMode, offsets = (Jedox.wss.app.environment.viewMode == viewMode.DESIGNER) ? {
		t : 0,
		l : 0,
		w : -2,
		h : -4
	}
			: {
				t : -1,
				l : -1,
				w : -7,
				h : -6
			}, book = this._selection.getBook(), refCell = book
			.getCellByCoords(this._upperLeft.getX(), this._upperLeft.getY());
	if (!refCell) {
		var refCellUL = book.getPixelsByCoords(this._upperLeft.getX(),
				this._upperLeft.getY()), refCellWH = book.getCellDims(
				this._upperLeft.getX(), this._upperLeft.getY())
	}
	this._dataCell.style.left = (refCell ? refCell.offsetLeft : refCellUL[0])
			+ offsets.l + "px";
	this._dataCell.style.top = (refCell ? refCell.parentNode.offsetTop
			: refCellUL[1])
			+ offsets.t + "px";
	this._dataCell.style.width = (refCell ? refCell.offsetWidth
			: refCellWH[0] + 1)
			+ offsets.w + "px";
	this._dataCell.style.height = (refCell ? refCell.offsetHeight
			: refCellWH[1] + 1)
			+ offsets.h + "px";
	var dataCellContent = "{".concat(this._parent._props._gendata[0][2], "}");
	Jedox.util.setText(this._dataCell, dataCellContent);
	if (refCell) {
		Jedox.wss.style.cellTransfer(this._dataCell, refCell)
	}
	this._dataCell.style.fontSize = "8pt";
	this._dataCell.style.textAlign = "center";
	if (this._dataCell.style.backgroundColor == "") {
		this._dataCell.style.backgroundColor = "#ffffff"
	}
	this._dataCell.style.display = "block"
};
clsRef.prototype.switchMode = function(mode) {
};
clsRef.prototype.setNormalMode = function() {
	for ( var i = 3, el = this._edgeElems[i]; i >= 0; el = this._edgeElems[--i]) {
		el.style.zIndex = "37";
		el.style.display = "none"
	}
	this._dataCell.style.zIndex = "39";
	this._cntrlElems.list.style.zIndex = "40";
	this._cntrlElems.list.style.display = "none";
	this._dataCell.style.display = "block"
};
clsRef.prototype.setEditMode = function() {
	for ( var i = 3, el = this._edgeElems[i]; i >= 0; el = this._edgeElems[--i]) {
		el.style.zIndex = "41";
		el.style.display = "block"
	}
	this._dataCell.style.zIndex = "43";
	this._cntrlElems.list.style.zIndex = "44";
	this._cntrlElems.list.style.display = "block"
};
clsRef.prototype.setHiddenMode = function() {
	for ( var i = 3, el = this._edgeElems[i]; i >= 0; el = this._edgeElems[--i]) {
		el.style.display = "none"
	}
	this._cntrlElems.list.style.display = "none";
	this._dataCell.style.display = "none"
};
clsRef.prototype._getColorNumber = function() {
	return "hbsub"
};
clsRef.prototype.getPosInParent = function() {
	return [ this.parentOffset.left, this.parentOffset.top ]
};
clsRef.prototype._openListDlg = function(ev) {
	var props = this._parent.getProps();
	if (props._gendata[0][0] == -1) {
		Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.insertDynarange,
				[ props ])
	} else {
		Jedox.wss.palo.utils.openSubsetEditor( {
			mode : 1,
			dynarange : props
		})
	}
	if (document.all) {
		window.event.returnValue = false;
		window.event.cancelBubble = true
	} else {
		ev.preventDefault();
		ev.stopPropagation()
	}
};
clsRef.prototype.setMode = function(mode) {
	this._mode = mode
};
clsRef.prototype._dataCellMouseDown = function(ev) {
	var env = this._environment.shared, gridMode = Jedox.wss.grid.GridMode, value = env.inputField.value;
	if ((env.inputMode == gridMode.EDIT || env.inputMode == gridMode.INPUT)
			&& (value.length > 0) && (value.substr(0, 1) == "=")) {
		Jedox.wss.mouse.mimicCellMouseEvent(this._upperLeft.getX(),
				this._upperLeft.getY(), "mousedown");
		return
	}
	var rngMode = Jedox.wss.range.RangeMode;
	if (this._mode == rngMode.NONE) {
		this._parent._selection.setMode(rngMode.EDIT)
	}
};
clsRef.prototype._dataCellMouseOver = function() {
	var env = this._environment.shared, x = this._upperLeft.getX(), y = this._upperLeft
			.getY();
	env.lastCell = Jedox.wss.app.activeBook.getCellByCoords(x, y);
	env.lastCellCoords = [ x, y ]
};
clsRef.prototype._onmousedown = function(ev) {
	var that = this;
	if (document.all) {
		ev = window.event
	}
	this._status = Jedox.wss.range.AreaStatus.DRAGGING;
	Jedox.wss.app.mimicOvertracking = (Ext.isGecko3 && ev.button == 0);
	this._setMonitorCell(this._environment.shared.lastCell,
			(document.all) ? ev.srcElement : ev.target);
	Jedox.wss.app.mouseMovementObserver.subscribe(this._move, this);
	Jedox.wss.app.mouseUpObserver.subscribe(this._onmouseup, this);
	this._selection.activeToken = this.formulaToken
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
	this._legacyMouseUp();
	this._parent.save(false)
};
clsRef.prototype._move = function(ev) {
	if (document.all) {
		ev = window.event
	}
	this._environment.shared.autoScroll.checkAndScroll(ev,
			this._resizeOnScroll, Jedox.wss.grid.scrollType.ALL, null, this);
	this._selection.setActiveRange(this);
	if (this._status == Jedox.wss.range.AreaStatus.DRAGGING) {
		var elemCoords = this._environment.shared.lastCellCoords, newX = elemCoords[0]
				+ this._realCoords[0].getX() - this._monitorCellCoords[0], newY = elemCoords[1]
				+ this._realCoords[0].getY() - this._monitorCellCoords[1];
		if (newX >= this._parentStartPoint.getX()
				&& newX <= this._parentEndPoint.getX()
				&& newY >= this._parentStartPoint.getY()
				&& newY <= this._parentEndPoint.getY()) {
			this._selection.moveTo(newX, newY);
			this.parentOffset.left = newX - this._parentStartPoint.getX();
			this.parentOffset.top = newY - this._parentStartPoint.getY()
		}
	}
	this.draw();
	this._setMonitorCell(this._environment.shared.lastCell)
};
clsRef.prototype._listBtnMouseOver = function(ev) {
	var coords = Jedox.wss.mouse.getGridPos((document.all) ? window.event : ev);
	Jedox.wss.mouse.mimicCellMouseEvent(coords[0], coords[1], "mouseover")
};
clsRef.prototype.remove = function() {
	for ( var i = 3; i >= 0; --i) {
		this._container.removeChild(this._edgeElems[i])
	}
	this._container.removeChild(this._cntrlElems.list);
	this._container.removeChild(this._dataCell)
};
clsRef = null;