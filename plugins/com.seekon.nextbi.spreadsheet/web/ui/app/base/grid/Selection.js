Jedox.wss.grid.Selection = (function() {
	return function(startCoords, book) {
		this._ranges = [];
		this._mode = Jedox.wss.range.RangeMode.NONE;
		this._activeRange = 0;
		this._scope = this;
		this._startCoords = startCoords;
		this._book = book;
		this._container = ((book == null) ? null : this._book.getBookIC());
		this._environment = ((book == null) ? null : this._book
				.getEnvironment());
		this._headerState = {
			colActive : false,
			colSelected : false,
			rowActive : false,
			rowSelected : false
		};
		this.lastParseRes = [];
		this.activeToken = null
	}
})();
Jedox.wss.grid.Selection.prototype = {
	_isRect : function() {
		return false
	},
	_setLegacyVars : function() {
		var activeCell = this._ranges[this._activeRange].getActiveCell();
		if (this._environment.shared.headerMarkMode == "") {
			this._environment.shared.headerStyle = 0;
			this._environment.shared.lastMarkMode = 0
		}
		this.setActiveCell(null, activeCell.getX()
				- Jedox.wss.app.firstRowNumeric + 1, activeCell.getY()
				- Jedox.wss.app.firstColumn + 1, false)
	},
	getActiveCellElement : function() {
		return this._ranges[this._activeRange].getCursorField()
	},
	getRanges : function() {
		return this._ranges
	},
	getRange : function(idx) {
		return this._ranges[idx]
	},
	getValue : function() {
		var cellValue, cellFormula;
		var activeCell = this._ranges[this._activeRange].getActiveCell();
		try {
			cellValue = this._book.getCellUVal(activeCell.getX(), activeCell
					.getY());
			if (cellValue == undefined) {
				cellValue = ""
			}
		} catch (e) {
			cellValue = ""
		}
		try {
			cellFormula = this._book.getCellFormula(activeCell.getX(),
					activeCell.getY());
			if (cellFormula == undefined) {
				cellFormula = ""
			}
		} catch (e) {
			cellFormula = ""
		}
		return [ cellValue, cellFormula ]
	},
	activeCellIsVisible : function() {
		var activeCell = this._ranges[this._activeRange].getActiveCell();
		return this._book.isCellVisible(activeCell.getX(), activeCell.getY())
	},
	setMode : function(mode) {
		this._mode = mode
	},
	getMode : function() {
		return this._mode
	},
	isSingleCell : function() {
		return (this._ranges[this._activeRange].getUpperLeft()
				.equals(this._ranges[this._activeRange].getLowerRight()))
	},
	isSingleColumn : function() {
		return (this._ranges[this._activeRange].getUpperLeft()
				.equalsY(this._ranges[this._activeRange].getLowerRight()))
	},
	isSingleRow : function() {
		return (this._ranges[this._activeRange].getUpperLeft()
				.equalsX(this._ranges[this._activeRange].getLowerRight()))
	},
	getCell : function(numericX, numericY) {
		return this._book.getCellByCoords(numericX, numericY)
	},
	setActiveCell : function(CellElement, x, y, saveLastCell) {
		var currentFormula = Jedox.wss.app.currFormula;
		var inputField = this._environment.shared.inputField;
		this._environment.shared.selectedCell = this.getCell(x, y);
		this._environment.shared.selectedCellCoords = [ x, y ];
		try {
			this._environment.shared.selectedCellValue = this._book
					.getCellUVal(x, y);
			if (this._environment.shared.selectedCellValue == undefined) {
				this._environment.shared.selectedCellValue = ""
			}
			this._environment.shared.selectedCellFormula = this._book
					.getCellFormula(x, y);
			if (this._environment.shared.selectedCellFormula == undefined) {
				this._environment.shared.selectedCellFormula = ""
			}
		} catch (Exception) {
			this._environment.shared.selectedCellValue = "";
			this._environment.shared.selectedCellFormula = ""
		}
		this._environment.shared.selectedRowNumericName = x;
		this._environment.shared.selectedColumnName = y;
		this._environment.shared.selectedAbsRowNameNumeric = Jedox.wss.app.firstRowNumeric
				+ x - 1;
		this._environment.shared.selectedAbsColumnName = Jedox.wss.app.firstColumn
				+ y - 1;
		this._environment.shared.selectedRowName = Jedox.wss.app.numberToLetter[x];
		this._environment.shared.selectedRow = this._book.getColHdrsIC().childNodes[x - 1];
		this._environment.shared.selectedColumn = this._book.getRowHdrsIC().childNodes[y - 1];
		var value = this.getValue();
		Jedox.wss.app.currCoord.value = this._environment.shared.selectedRowName
				+ this._environment.shared.selectedAbsColumnName;
		this._environment.shared.inputField.value = value[0];
		Jedox.wss.app.currFormula.value = (value[1] != "") ? value[1]
				: Jedox.wss.general.filterHLTags(x, y, value[0], false);
		try {
			if (this._environment.shared.cursorField != null) {
				Jedox.wss.style
						.cellTransfer(this._environment.shared.cursorField)
			}
			Jedox.wss.style.syncBar()
		} catch (e) {
		}
	},
	setRefActiveCell : function(cell) {
		this._ranges[this._activeRange].setRefActiveCell(cell)
	},
	getRefActiveCell : function() {
		return this._ranges[this._activeRange].getRefActiveCell()
	},
	scrollToActiveCell : function(previousActiveCell) {
		var tmp = this._ranges[this._activeRange].getActiveCell();
		var offsets = [];
		offsets[0] = tmp.getX() - previousActiveCell.getX();
		offsets[1] = tmp.getY() - previousActiveCell.getY();
		if ((!((tmp.getX() >= Jedox.wss.app.firstRowNumeric)))
				|| (((tmp.getX() == (Jedox.wss.app.firstRowNumeric + 1)) && (offsets[0] < 0)))) {
			if (offsets[0] > 0) {
				Jedox.wss.sheet.scrollXBy(Math.abs(offsets[0]),
						Jedox.wss.grid.ScrollDirection.RIGHT)
			} else {
				if (offsets[0] < 0) {
					Jedox.wss.sheet.scrollXBy(Math.abs(offsets[0]),
							Jedox.wss.grid.ScrollDirection.LEFT)
				}
			}
		}
		if ((!((tmp.getY() >= Jedox.wss.app.firstColumn)))
				|| (((tmp.getY() == (Jedox.wss.app.firstColumn + 1)) && (offsets[1] < 0)))) {
			if (offsets[1] > 0) {
				Jedox.wss.sheet.scrollYBy(Math.abs(offsets[1]),
						Jedox.wss.grid.ScrollDirection.DOWN)
			} else {
				if (offsets[1] < 0) {
					Jedox.wss.sheet.scrollYBy(Math.abs(offsets[1]),
							Jedox.wss.grid.ScrollDirection.UP)
				}
			}
		}
	},
	_activeCellIsBevoreBoundary : function() {
		var activeCell = this._ranges[this._activeRange].getActiveCell();
		return ((activeCell.getX() == (Jedox.wss.app.firstRowNumeric + 1)) || (activeCell
				.getY() == (Jedox.wss.app.firstColumn + 1)))
	},
	_iterate : function(mode) {
		var newRangeSelected = false;
		var direction;
		var offsets = [];
		var visibleBefore = this._scope.activeCellIsVisible();
		var previousActiveCell = this._ranges[this._activeRange]
				.getActiveCell().clone();
		if (!this._isRect()) {
			switch (mode) {
			case Jedox.wss.grid.IterationMode.NEXT:
			case Jedox.wss.grid.IterationMode.NEXTY:
				if (this._ranges[this._activeRange].isLast()) {
					this._ranges[this._activeRange].deactivate();
					if (this._activeRange == this._ranges.length - 1) {
						this._activeRange = 0
					} else {
						this._activeRange++
					}
					this._ranges[this._activeRange].activate(direction);
					newRangeSelected = true;
					console.warn("previous range")
				}
				break;
			case Jedox.wss.grid.IterationMode.PREV:
			case Jedox.wss.grid.IterationMode.PREVY:
				if (this._ranges[this._activeRange].isFirst()) {
					this._ranges[this._activeRange].deactivate();
					if (this._activeRange === 0) {
						this._activeRange = this._ranges.length
					} else {
						this._activeRange--
					}
					this._ranges[this._activeRange].activate(direction);
					newRangeSelected = true;
					consol.warn("previous range")
				}
				break
			}
		}
		if (!newRangeSelected) {
			switch (mode) {
			case Jedox.wss.grid.IterationMode.NEXT:
				this._ranges[this._activeRange].next();
				break;
			case Jedox.wss.grid.IterationMode.PREV:
				this._ranges[this._activeRange].prev();
				break;
			case Jedox.wss.grid.IterationMode.NEXTY:
				this._ranges[this._activeRange].nextY();
				break;
			case Jedox.wss.grid.IterationMode.PREVY:
				this._ranges[this._activeRange].prevY();
				break
			}
		}
		var activeBook = this._book, activeCell = this._ranges[this._activeRange]
				.getActiveCell(), that = this, cbScrollTo = function() {
			that._setLegacyVars();
			that.draw()
		};
		if (visibleBefore) {
			if (!this._book.isCellVisible(activeCell.getX(), activeCell.getY())
					&& !activeBook._scrollOpPending) {
				this._book.scrollTo( [ this, cbScrollTo ], activeCell.getX(),
						activeCell.getY(), true, false)
			}
		} else {
			if (activeBook._scrollOpPending) {
				return
			}
			this._book.scrollTo( [ this, cbScrollTo ], activeCell.getX(),
					activeCell.getY(), false, false)
		}
	},
	setState : function(state) {
		if (state == Jedox.wss.range.AreaState.NEW) {
			for ( var i = this._ranges.length - 1; i >= 0; --i) {
				if (this._ranges[i].getState() == Jedox.wss.range.AreaState.NEW) {
					this._ranges[i].setState(Jedox.wss.range.AreaState.NORMAL);
					this._ranges[i].repaint()
				}
			}
		}
		if (this._activeRange in this._ranges) {
			this._ranges[this._activeRange].setState(state)
		}
	},
	expandToCell : function(cell) {
		this._ranges[this._activeRange].expandToCell(cell)
	},
	focusActiveCell : function() {
		var tmp = this._ranges[this._activeRange].getActiveCell();
		var gridUpperLeft = new Jedox.wss.cls.Point(
				Jedox.wss.app.firstRowNumeric, Jedox.wss.app.firstColumn);
		if ((gridUpperLeft.getX()
				+ this._ranges[this._activeRange].getOffsets()[0] + 1 - tmp
				.getX()) > 0) {
			Jedox.wss.sheet.scrollXBy(Math.abs(gridUpperLeft.getX()
					- tmp.getX() + 1), Jedox.wss.grid.ScrollDirection.LEFT)
		}
		if ((gridUpperLeft.getY()
				+ this._ranges[this._activeRange].getOffsets()[0] + 1 - tmp
				.getY()) > 0) {
			Jedox.wss.sheet.scrollYBy(Math.abs(gridUpperLeft.getY()
					- tmp.getY() + 1), Jedox.wss.grid.ScrollDirection.UP)
		}
		if ((gridUpperLeft.getX()
				+ this._ranges[this._activeRange].getOffsets()[1] + 1 - tmp
				.getX()) < 0) {
			Jedox.wss.sheet.scrollXBy(Math.abs(gridUpperLeft.getX()
					- tmp.getX() + 1), Jedox.wss.grid.ScrollDirection.RIGHT)
		}
		if ((gridUpperLeft.getY()
				+ this._ranges[this._activeRange].getOffsets()[1] + 1 - tmp
				.getY()) < 0) {
			Jedox.wss.sheet.scrollYBy(Math.abs(gridUpperLeft.getY()
					- tmp.getY() + 1), Jedox.wss.grid.ScrollDirection.DOWN)
		}
	},
	next : function() {
		this._iterate(Jedox.wss.grid.IterationMode.NEXT)
	},
	prev : function() {
		this._iterate(Jedox.wss.grid.IterationMode.PREV)
	},
	nextY : function() {
		this._iterate(Jedox.wss.grid.IterationMode.NEXTY)
	},
	prevY : function() {
		this._iterate(Jedox.wss.grid.IterationMode.PREVY)
	},
	toString : function() {
		var s = "";
		s += "Mode: " + this._mode;
		s += " Ranges: ";
		for ( var i = 0; i < this._ranges.length; i++) {
			s += this._ranges[i].toString()
		}
		return s
	},
	getFormula : function(makeAbs) {
		var i = 0, rngs = this._ranges, len = rngs.length, f = "=";
		for (; i < len; f = f.concat(rngs[i++].getValue(makeAbs))) {
		}
		return f
	},
	moveTo : function(x, y, mode) {
		this._ranges[this._activeRange].moveTo(x, y);
		this.checkForUndoneMarkers()
	},
	expandIfActive : function(amount, direction) {
		var status = this._ranges[this._activeRange].getStatus();
		if (status == Jedox.wss.range.AreaStatus.RESIZING) {
			this.expand(amount, direction)
		}
	},
	expand : function(amount, direction) {
		this._ranges[this._activeRange].expand(amount, direction)
	},
	set : function(startPoint, endPoint) {
		if (this._isRect()) {
			if (!this._scope.activeCellIsVisible()) {
			}
			var range = this._ranges[this._activeRange], headerMarkMode = this._environment.shared.headerMarkMode, chkHiddenColRow = Jedox.wss.general.chkHiddenColRow;
			this._selectionChanged = true;
			startPoint.setX(chkHiddenColRow(false, startPoint.getX(), 0, true));
			startPoint.setY(chkHiddenColRow(true, startPoint.getY(), 0, true));
			endPoint.setX(chkHiddenColRow(false, endPoint.getX(), 0, false));
			endPoint.setY(chkHiddenColRow(true, endPoint.getY(), 0, false));
			range.set(startPoint, endPoint);
			if ((startPoint.getX() > endPoint.getX())
					|| (startPoint.getY() > endPoint.getY())) {
				this._ranges[this._activeRange].toggleCoords()
			}
			var isSingleCell = this.isSingleCell(), defMaxCoords = Jedox.wss.grid.defMaxCoords, idclNS = Jedox.wss.app[Jedox.wss.app.menubar ? "menubar"
					: "toolbar"];
			if (!isSingleCell && endPoint.getX() == defMaxCoords[0]
					&& endPoint.getY() == defMaxCoords[1]) {
				range.setActiveCell(new Jedox.wss.cls.Point(this._book
						.getCoordsFirstVCell()[0], this._book
						.getCoordsFirstVCell()[1]));
				idclNS.insRow.disable();
				idclNS.insCol.disable();
				idclNS.delRow.disable();
				idclNS.delCol.disable()
			} else {
				if (!isSingleCell && endPoint.getX() == defMaxCoords[0]) {
					range.setActiveCell(new Jedox.wss.cls.Point(
							chkHiddenColRow(false, this._book
									.getCoordsFirstVCell()[0], 0, true),
							startPoint.getY()));
					idclNS.insCol.disable();
					idclNS.delCol.disable()
				} else {
					if (!isSingleCell && endPoint.getY() == defMaxCoords[1]) {
						range.setActiveCell(new Jedox.wss.cls.Point(startPoint
								.getX(), chkHiddenColRow(true, this._book
								.getCoordsFirstVCell()[1], 0, true)));
						idclNS.insRow.disable();
						idclNS.delRow.disable()
					} else {
						range.setActiveCell(startPoint);
						idclNS.insRow.enable();
						idclNS.insCol.enable();
						idclNS.delRow.enable();
						idclNS.delCol.enable()
					}
				}
			}
			if (!((headerMarkMode == "column" && startPoint.getX() != endPoint
					.getX()) || (headerMarkMode == "row" && startPoint.getY() != endPoint
					.getY()))) {
				this._setLegacyVars()
			}
		}
	},
	fillContent : function(amount, direction, mode) {
		if (!this._isRect()) {
		}
	},
	getActiveRange : function() {
		return this._ranges[this._activeRange]
	},
	getState : function() {
		return this._ranges[this._activeRange].getState()
	},
	getState : function() {
		return _state
	},
	moveCells : function(amount, direction, mode) {
		if (!this._isRect()) {
		}
	},
	removeAll : function() {
		for ( var i = this._ranges.length - 1; i >= 0; this.removeRange(i--)) {
		}
		this.lastParseRes = []
	},
	addRange : function(startPoint, endPoint) {
		return this._ranges.push(new Jedox.wss.grid.Range(this, startPoint,
				endPoint))
	},
	setActiveRange : function(rangeId) {
		if (typeof rangeId == "object") {
			var idx = this._ranges.indexOf(rangeId);
			if (idx >= 0) {
				this._activeRange = idx
			}
			return
		}
		if ((rangeId >= 0) && (rangeId < this._ranges.length)) {
			this._activeRange = rangeId
		} else {
			throw new Error("Invalid range id: ", rangeId)
		}
	},
	registerForMouseMovement : function(element) {
		if (this._activeRange in this._ranges) {
			this._ranges[this._activeRange].registerForMouseMovement(element)
		}
	},
	setVisibility : function(visibility) {
		for ( var i = 0; i < this._ranges.length; i++) {
			this._scope.setActiveRange(i);
			this._ranges[this._activeRange].setVisibility(visibility)
		}
	},
	draw : function() {
		for ( var i = 0; i < this._ranges.length; i++) {
			this._scope.setActiveRange(i);
			this._ranges[this._activeRange].draw()
		}
	},
	removeRange : function(id) {
		if (this._ranges[id]) {
			this._ranges[id].destruct();
			this._ranges.splice(id, 1);
			return true
		} else {
			console.warn("invalid range id: ", id);
			return false
		}
	},
	_clearHeaderMarkers : function(clearAll, type) {
		if ((clearAll || type == Jedox.wss.grid.headerType.COLUMN)
				&& (this.dynarange || this._headerState.colActive || this._headerState.colSelected)) {
			this._book.activateHdrAll(Jedox.wss.grid.headerType.COLUMN,
					"gridColHdr");
			this._headerState.colActive = false;
			this._headerState.colSelected = false
		}
		if ((clearAll || type == Jedox.wss.grid.headerType.ROW)
				&& (this.dynarange || this._headerState.rowActive || this._headerState.rowSelected)) {
			this._book.activateHdrAll(Jedox.wss.grid.headerType.ROW,
					"gridRowHdr");
			this._headerState.rowActive = false;
			this._headerState.rowSelected = false
		}
	},
	checkForUndoneMarkers : function(force) {
		if (!this._selectionChanged && !(force != undefined && force)) {
			return
		}
		if (this.isSingleCell()) {
			var dimensions = this.getActiveRange().getCorners(), mergeInfo = this._book
					.getMergeInfo(dimensions[0].getX(), dimensions[0].getY());
			if (mergeInfo != undefined && mergeInfo) {
				this.drawMarkers( [
						dimensions[0],
						new Jedox.wss.cls.Point(dimensions[0].getX()
								+ mergeInfo[1] - 1, dimensions[0].getY()
								+ mergeInfo[2] - 1) ])
			} else {
				this._clearHeaderMarkers(true, null);
				this._book.activateHdr(Jedox.wss.grid.headerType.COLUMN,
						dimensions[0].getX(), "col_active");
				this._book.activateHdr(Jedox.wss.grid.headerType.ROW,
						dimensions[0].getY(), "row_active")
			}
		} else {
			if (this.dynarange) {
				this._clearHeaderMarkers(true, null)
			}
			this.drawMarkers()
		}
		this._selectionChanged = false
	},
	drawMarkers : function(rng) {
		var dimensions = (rng != undefined) ? rng : this.getActiveRange()
				.getCorners();
		if (dimensions[0].getX() == 1
				&& dimensions[1].getX() == Jedox.wss.grid.defMaxCoords[0]) {
			this._book
					.activateHdrAll(
							Jedox.wss.grid.headerType.COLUMN,
							"gridColHdr "
									+ ((this._environment.shared.headerStyle == 1) ? "col_selected"
											: "col_active"));
			this._headerState.colActive = true
		} else {
			this._book
					.activateHdrRng(
							Jedox.wss.grid.headerType.COLUMN,
							[ dimensions[0].getX(), dimensions[1].getX() ],
							(!this.dynarange && (this._environment.shared.headerStyle == 1
									|| this._environment.shared.headerStyle == 3 || (dimensions[0]
									.getY() == 1 && dimensions[1].getY() == Jedox.wss.grid.defMaxCoords[1]))) ? "col_selected"
									: "col_active")
		}
		if (dimensions[0].getY() == 1
				&& dimensions[1].getY() == Jedox.wss.grid.defMaxCoords[1]) {
			this._book
					.activateHdrAll(
							Jedox.wss.grid.headerType.ROW,
							"gridRowHdr "
									+ ((this._environment.shared.headerStyle == 1) ? "row_selected"
											: "row_active"));
			this._headerState.rowActive = true
		} else {
			this._book
					.activateHdrRng(
							Jedox.wss.grid.headerType.ROW,
							[ dimensions[0].getY(), dimensions[1].getY() ],
							(!this.dynarange && (this._environment.shared.headerStyle == 1
									|| this._environment.shared.headerStyle == 2 || (dimensions[0]
									.getX() == 1 && dimensions[1].getX() == Jedox.wss.grid.defMaxCoords[0]))) ? "row_selected"
									: "row_active")
		}
	},
	updateRangeSelector : function() {
		if ((this._environment.shared.lastRangeStartCoord[0] == this._environment.shared.lastRangeEndCoord[0])
				&& (this._environment.shared.lastRangeStartCoord[1] == this._environment.shared.lastRangeEndCoord[1])) {
			Jedox.wss.app.currCoord.value = Jedox.wss.app.numberToLetter[this._environment.shared.selectedCellCoords[0]]
					+ this._environment.shared.selectedCellCoords[1]
		} else {
			Jedox.wss.app.currCoord.value = ((this._environment.shared.lastRangeEndCoord[1] - this._environment.shared.lastRangeStartCoord[1]) + 1)
					+ "R x "
					+ ((this._environment.shared.lastRangeEndCoord[0] - this._environment.shared.lastRangeStartCoord[0]) + 1)
					+ "C"
		}
	},
	emptyCellContent : function() {
		var rngClearType = Jedox.wss.range.ContentType, rng;
		this._environment.shared.inputField.value = "";
		this._environment.shared.selectedCellValue = "";
		Jedox.wss.app.currFormula.value = "";
		this._environment.shared.cursorField.innerHTML = "";
		if (this.isSingleCell()) {
			this._clearHeaderMarkers(true, null);
			var selCoords = this._environment.shared.selectedCellCoords;
			this._book.activateHdr(Jedox.wss.grid.headerType.COLUMN,
					selCoords[0], "col_active");
			this._book.activateHdr(Jedox.wss.grid.headerType.ROW, selCoords[1],
					"row_active");
			rng = [ selCoords[0], selCoords[1], selCoords[0], selCoords[1] ]
		} else {
			var rngCorners = this.getActiveRange().getCorners();
			rng = [ rngCorners[0].getX(), rngCorners[0].getY(),
					rngCorners[1].getX(), rngCorners[1].getY() ]
		}
		this._book.clrRange(rng, rngClearType.FORMULA | rngClearType.ATTRS)
	},
	selectOrResize : function(myEvent) {
		var myElement, elemData, col, row;
		var env = this._environment.shared;
		var XOffset = 5;
		var YOffset = 5;
		if (document.all) {
			myEvent = window.event
		}
		var isRightClick = myEvent.button == 2
				|| (Ext.isMac && myEvent.button == 0 && Jedox.wss.app.ctrlKeyPressed);
		if (isRightClick) {
			var defMaxCoords = Jedox.wss.grid.defMaxCoords, rngCorners = this
					.getActiveRange().getCorners();
			if ((rngCorners[0].getX() == 1
					&& rngCorners[1].getX() == defMaxCoords[0] && rngCorners[0]
					.getY() != rngCorners[1].getY())
					|| (rngCorners[0].getY() == 1
							&& rngCorners[1].getY() == defMaxCoords[1] && rngCorners[0]
							.getX() != rngCorners[1].getX())) {
				Jedox.wss.mouse.showMainCntxMenu(myEvent);
				return
			}
		}
		if (myEvent.detail == 2) {
			return
		}
		myElement = (document.all) ? myEvent.srcElement : myEvent.target;
		col = (myElement.parentNode.className == "gridColHdrsIC") ? this._book
				.getCoordByHdr(Jedox.wss.grid.headerType.COLUMN, myElement) : 0;
		row = (myElement.parentNode.className == "gridRowHdrsIC") ? this._book
				.getCoordByHdr(Jedox.wss.grid.headerType.ROW, myElement) : 0;
		if (Jedox.wss.app.mouseButton1Down) {
			Jedox.wss.mouse.fetchGlobalMouseMove(myEvent)
		} else {
			if (env.mousePosition == "colResize" && !isRightClick) {
				var targetOffsetX = (document.all) ? myEvent.offsetX
						: myEvent.layerX;
				if (targetOffsetX <= XOffset) {
					myElement = this._book.getHdrByCoord(
							Jedox.wss.grid.headerType.COLUMN, --col)
				}
				Jedox.wss.mouse.startHeaderResize(
						Jedox.wss.grid.headerType.COLUMN, myElement, col,
						myEvent.clientX)
			} else {
				if (env.mousePosition == "rowResize" && !isRightClick) {
					var targetOffsetY = (document.all) ? myEvent.offsetY
							: myEvent.layerY;
					if (targetOffsetY <= YOffset) {
						myElement = this._book.getHdrByCoord(
								Jedox.wss.grid.headerType.ROW, --row)
					}
					Jedox.wss.mouse.startHeaderResize(
							Jedox.wss.grid.headerType.ROW, myElement, row,
							myEvent.clientY)
				} else {
					Jedox.wss.mouse
							.showCursorLayer((env.mousePosition == "rowMark") ? "marker_multirow_select"
									: "marker_multicol_select");
					Jedox.wss.mouse.calcGridScreenCoords(myEvent, myElement);
					if (env.mousePosition == "rowMark"
							|| (env.mousePosition == "rowResize" && isRightClick)) {
						var visCell = this._book.getCoordsFirstVCell();
						var visRange = this._book
								.getVisibleRange(Jedox.wss.grid.headerType.ROW);
						++visRange[1];
						var visRangeDim = new Object();
						visRangeDim[visRange[0] - 1] = env.gridScreenCoords[0][1];
						for ( var i = visRange[0]; i <= visRange[1]; ++i) {
							visRangeDim[i] = visRangeDim[i - 1]
									+ this._book.getCellDims(visCell[0], i)[1]
						}
						env.headerSelected = {
							coordBegin : row,
							coordEnd : row,
							screenBegin : visRangeDim[row - 1],
							screenEnd : visRangeDim[row],
							coordCurr : row,
							coordInit : row,
							visRange : visRange,
							visOposRangeBegin : visCell[0],
							visRangeDim : visRangeDim
						};
						env.headerMarkMode = "row";
						env.headerStyle = env.lastMarkMode = 2;
						this._clearHeaderMarkers(false,
								Jedox.wss.grid.headerType.ROW);
						this.set(new Jedox.wss.cls.Point(
								Jedox.wss.app.firstRowNumeric, row),
								new Jedox.wss.cls.Point(
										Jedox.wss.grid.defMaxCoords[0], row));
						this.draw();
						this.getActiveRange().reposBgndMask(false)
					} else {
						if (env.mousePosition == "colMark"
								|| (env.mousePosition == "colResize" && isRightClick)) {
							var visCell = this._book.getCoordsFirstVCell();
							var visRange = this._book
									.getVisibleRange(Jedox.wss.grid.headerType.COLUMN);
							++visRange[1];
							var visRangeDim = new Object();
							visRangeDim[visRange[0] - 1] = env.gridScreenCoords[0][0];
							for ( var i = visRange[0]; i <= visRange[1]; ++i) {
								visRangeDim[i] = visRangeDim[i - 1]
										+ this._book.getCellDims(i, visCell[1])[0]
							}
							env.headerSelected = {
								coordBegin : col,
								coordEnd : col,
								screenBegin : visRangeDim[col - 1],
								screenEnd : visRangeDim[col],
								coordCurr : col,
								coordInit : col,
								visRange : visRange,
								visOposRangeBegin : visCell[1],
								visRangeDim : visRangeDim
							};
							env.headerMarkMode = "column";
							env.headerStyle = env.lastMarkMode = 3;
							this._clearHeaderMarkers(false,
									Jedox.wss.grid.headerType.COLUMN);
							this.set(new Jedox.wss.cls.Point(col,
									Jedox.wss.app.firstColumn),
									new Jedox.wss.cls.Point(col,
											Jedox.wss.grid.defMaxCoords[1]));
							this.draw();
							this.getActiveRange().reposBgndMask(false)
						}
					}
				}
			}
		}
		if (isRightClick) {
			Jedox.wss.mouse.showMainCntxMenu(myEvent)
		}
	},
	_refreshElement : function(scope, range) {
		range.draw()
	},
	_hdrMarkOnScroll : function(book, type, direction, firstVisCoord,
			lastVisCoord, scrollTimeout) {
		var scope = this;
		var env = Jedox.wss.app.environment;
		var headerSelected = env.headerSelected;
		var scrollType = Jedox.wss.grid.scrollType;
		var scrollDir = Jedox.wss.grid.ScrollDirection;
		var defMaxCoords = Jedox.wss.grid.defMaxCoords;
		var selection = env.defaultSelection;
		var range = selection.getActiveRange();
		var rangeCoords = range.getCorners();
		var _hdrResizeLeft = function() {
			var nmcDelta = Math.abs(headerSelected.coordCurr - firstVisCoord
					- ((firstVisCoord == 1) ? 0 : 1));
			headerSelected.coordCurr -= nmcDelta;
			headerSelected.screenBegin = headerSelected.visRangeDim[headerSelected.coordCurr - 1];
			headerSelected.screenEnd = headerSelected.visRangeDim[headerSelected.coordCurr];
			if (headerSelected.coordCurr > rangeCoords[0].getX()) {
				selection.expand(nmcDelta * -1, scrollDir.RIGHT);
				headerSelected.coordEnd = headerSelected.coordCurr
			} else {
				selection.expand(nmcDelta, scrollDir.LEFT);
				headerSelected.coordBegin = headerSelected.coordCurr;
				headerSelected.coordEnd = headerSelected.coordInit
			}
			scope._refreshElement(scope, range)
		};
		var _hdrResizeRight = function() {
			var nmcDelta = Math.abs(lastVisCoord - headerSelected.coordCurr
					- ((lastVisCoord == defMaxCoords[0]) ? 0 : 1));
			headerSelected.coordCurr += nmcDelta;
			headerSelected.screenBegin = headerSelected.visRangeDim[headerSelected.coordCurr - 1];
			headerSelected.screenEnd = headerSelected.visRangeDim[headerSelected.coordCurr];
			if (headerSelected.coordCurr < rangeCoords[1].getX()) {
				selection.expand(nmcDelta * -1, scrollDir.LEFT);
				headerSelected.coordBegin = headerSelected.coordCurr
			} else {
				selection.expand(nmcDelta, scrollDir.RIGHT);
				headerSelected.coordEnd = headerSelected.coordCurr;
				headerSelected.coordBegin = headerSelected.coordInit
			}
			scope._refreshElement(scope, range)
		};
		var _hdrResizeDown = function() {
			var nmcDelta = Math.abs(lastVisCoord - headerSelected.coordCurr
					- ((lastVisCoord == defMaxCoords[1]) ? 0 : 1));
			headerSelected.coordCurr += nmcDelta;
			headerSelected.screenBegin = headerSelected.visRangeDim[headerSelected.coordCurr - 1];
			headerSelected.screenEnd = headerSelected.visRangeDim[headerSelected.coordCurr];
			if (headerSelected.coordCurr < rangeCoords[1].getY()) {
				selection.expand(nmcDelta * -1, scrollDir.UP);
				headerSelected.coordBegin = headerSelected.coordCurr
			} else {
				selection.expand(nmcDelta, scrollDir.DOWN);
				headerSelected.coordEnd = headerSelected.coordCurr;
				headerSelected.coordBegin = headerSelected.coordInit
			}
			scope._refreshElement(scope, range)
		};
		var _hdrResizeUp = function() {
			var nmcDelta = Math.abs(headerSelected.coordCurr - firstVisCoord
					- ((firstVisCoord == 1) ? 0 : 1));
			headerSelected.coordCurr -= nmcDelta;
			headerSelected.screenBegin = headerSelected.visRangeDim[headerSelected.coordCurr - 1];
			headerSelected.screenEnd = headerSelected.visRangeDim[headerSelected.coordCurr];
			if (headerSelected.coordCurr > rangeCoords[0].getY()) {
				selection.expand(nmcDelta * -1, scrollDir.DOWN);
				headerSelected.coordEnd = headerSelected.coordCurr
			} else {
				selection.expand(nmcDelta, scrollDir.UP);
				headerSelected.coordBegin = headerSelected.coordCurr;
				headerSelected.coordEnd = headerSelected.coordInit
			}
			scope._refreshElement(scope, range)
		};
		var _incVisRange = function(book, dim) {
			if (dim == 0) {
				var delItemDim = book.getCellDims(headerSelected.visRange[0],
						headerSelected.visOposRangeBegin)[dim]
			} else {
				var delItemDim = book.getCellDims(
						headerSelected.visOposRangeBegin,
						headerSelected.visRange[0])[dim]
			}
			for ( var i = headerSelected.visRange[0] + 1; i <= headerSelected.visRange[1]; ++i) {
				headerSelected.visRangeDim[i] -= delItemDim
			}
			delete headerSelected.visRangeDim[headerSelected.visRange[0]];
			++headerSelected.visRange[0];
			++headerSelected.visRange[1];
			headerSelected.visRangeDim[headerSelected.visRange[1]] = headerSelected.visRangeDim[headerSelected.visRange[1] - 1]
					+ book.getCellDims(headerSelected.visRange[1],
							headerSelected.visOposRangeBegin)[dim]
		};
		var _decVisRange = function(book, dim) {
			if (dim == 0) {
				var insItemDim = book.getCellDims(
						headerSelected.visRange[0] - 1,
						headerSelected.visOposRangeBegin)[dim]
			} else {
				var insItemDim = book.getCellDims(
						headerSelected.visOposRangeBegin,
						headerSelected.visRange[0] - 1)[dim]
			}
			for ( var i = headerSelected.visRange[0]; i < headerSelected.visRange[1]; ++i) {
				headerSelected.visRangeDim[i] += insItemDim
			}
			delete headerSelected.visRangeDim[headerSelected.visRange[1]];
			--headerSelected.visRange[0];
			--headerSelected.visRange[1];
			headerSelected.visRangeDim[headerSelected.visRange[0]] = env.gridScreenCoords[0][dim]
					+ insItemDim
		};
		if (type == scrollType.HORIZ) {
			if (direction == Jedox.wss.grid.horScrollDir.RIGHT) {
				_incVisRange(book, 0);
				if (headerSelected.coordCurr < lastVisCoord - 1) {
					_hdrResizeRight()
				}
				if (lastVisCoord == defMaxCoords[0]) {
					env.autoScroll.scrollElem = setTimeout(_hdrResizeRight,
							scrollTimeout)
				}
			} else {
				_decVisRange(book, 0);
				if (headerSelected.coordCurr > firstVisCoord + 1) {
					_hdrResizeLeft()
				}
				if (firstVisCoord == 1) {
					env.autoScroll.scrollElem = setTimeout(_hdrResizeLeft,
							scrollTimeout)
				}
			}
		} else {
			if (direction == Jedox.wss.grid.vertScrollDir.DOWN) {
				_incVisRange(book, 1);
				if (headerSelected.coordCurr < lastVisCoord - 1) {
					_hdrResizeDown()
				}
				if (lastVisCoord == defMaxCoords[1]) {
					env.autoScroll.scrollElem = setTimeout(_hdrResizeDown,
							scrollTimeout)
				}
			} else {
				_decVisRange(book, 1);
				if (headerSelected.coordCurr > firstVisCoord + 1) {
					_hdrResizeUp()
				}
				if (firstVisCoord == 1) {
					env.autoScroll.scrollElem = setTimeout(_hdrResizeUp,
							scrollTimeout)
				}
			}
		}
	},
	resizeByHeader : function(ev) {
		var env = this._environment.shared;
		var gridScreenCoords = env.gridScreenCoords;
		var headerSelected = env.headerSelected;
		var grid = Jedox.wss.grid;
		var selection = this;
		var activeBook = this._book;
		var range = this.getActiveRange();
		function incCoord(pos, maxPos) {
			do {
				if (headerSelected.coordCurr < maxPos) {
					++headerSelected.coordCurr
				} else {
					return
				}
			} while (pos > headerSelected.visRangeDim[headerSelected.coordCurr]);
			if (headerSelected.coordCurr > headerSelected.coordEnd) {
				headerSelected.coordEnd = headerSelected.coordCurr;
				headerSelected.coordBegin = headerSelected.coordInit
			} else {
				headerSelected.coordBegin = headerSelected.coordCurr
			}
			headerSelected.screenBegin = headerSelected.visRangeDim[headerSelected.coordCurr - 1];
			headerSelected.screenEnd = headerSelected.visRangeDim[headerSelected.coordCurr]
		}
		function decCoord(pos) {
			do {
				if (headerSelected.coordCurr > 1) {
					--headerSelected.coordCurr
				} else {
					return
				}
			} while (pos < headerSelected.visRangeDim[headerSelected.coordCurr - 1]);
			if (headerSelected.coordCurr < headerSelected.coordBegin) {
				headerSelected.coordBegin = headerSelected.coordCurr;
				headerSelected.coordEnd = headerSelected.coordInit
			} else {
				headerSelected.coordEnd = headerSelected.coordCurr
			}
			headerSelected.screenBegin = headerSelected.visRangeDim[headerSelected.coordCurr - 1];
			headerSelected.screenEnd = headerSelected.visRangeDim[headerSelected.coordCurr]
		}
		if (env.headerMarkMode == "column") {
			env.autoScroll.checkAndScroll(ev, this._hdrMarkOnScroll,
					grid.scrollType.HORIZ, null, this);
			if (Jedox.wss.app.xPos < headerSelected.screenBegin
					&& Jedox.wss.app.xPos > gridScreenCoords[0][0]) {
				decCoord(Jedox.wss.app.xPos)
			} else {
				if (Jedox.wss.app.xPos > headerSelected.screenEnd
						&& Jedox.wss.app.xPos < gridScreenCoords[1][0]) {
					incCoord(Jedox.wss.app.xPos, grid.defMaxCoords[0])
				} else {
					return
				}
			}
			this.set(new Jedox.wss.cls.Point(headerSelected.coordBegin,
					Jedox.wss.app.firstColumn), new Jedox.wss.cls.Point(
					headerSelected.coordEnd, grid.defMaxCoords[1]));
			range.setActiveCell(new Jedox.wss.cls.Point(
					headerSelected.coordInit, range.getActiveCell().getY()));
			this.draw();
			this.updateRangeSelector();
			env.headerStyle = 3;
			return
		} else {
			if (env.headerMarkMode == "row") {
				env.autoScroll.checkAndScroll(ev, this._hdrMarkOnScroll,
						grid.scrollType.VERT, null, this);
				if (Jedox.wss.app.yPos < headerSelected.screenBegin
						&& Jedox.wss.app.yPos > gridScreenCoords[0][1]) {
					decCoord(Jedox.wss.app.yPos)
				} else {
					if (Jedox.wss.app.yPos > headerSelected.screenEnd
							&& Jedox.wss.app.yPos < gridScreenCoords[1][1]) {
						incCoord(Jedox.wss.app.yPos, grid.defMaxCoords[1])
					} else {
						return
					}
				}
				this.set(new Jedox.wss.cls.Point(Jedox.wss.app.firstRowNumeric,
						headerSelected.coordBegin), new Jedox.wss.cls.Point(
						grid.defMaxCoords[0], headerSelected.coordEnd));
				range.setActiveCell(new Jedox.wss.cls.Point(range
						.getActiveCell().getX(), headerSelected.coordInit));
				this.draw();
				this.updateRangeSelector();
				env.headerStyle = 2
			}
		}
	},
	getSelHeaderFromRange : function(type, resizedHdr) {
		var headerType = Jedox.wss.grid.headerType;
		var defRngCoords = this.getActiveRange().getCorners();
		if (type == headerType.COLUMN && defRngCoords[0].getY() == 1
				&& defRngCoords[1].getY() == Jedox.wss.grid.defMaxCoords[1]
				&& resizedHdr >= defRngCoords[0].getX()
				&& resizedHdr <= defRngCoords[1].getX()) {
			return [ [ defRngCoords[0].getX(), defRngCoords[1].getX() ] ]
		} else {
			if (type == headerType.ROW && defRngCoords[0].getX() == 1
					&& defRngCoords[1].getX() == Jedox.wss.grid.defMaxCoords[0]
					&& resizedHdr >= defRngCoords[0].getY()
					&& resizedHdr <= defRngCoords[1].getY()) {
				return [ [ defRngCoords[0].getY(), defRngCoords[1].getY() ] ]
			} else {
				return [ [ resizedHdr, resizedHdr ] ]
			}
		}
	},
	markAllCells : function() {
		if (this._environment.shared.inputMode == Jedox.wss.grid.GridMode.READY) {
			var defMaxCoords = Jedox.wss.grid.defMaxCoords;
			this._environment.shared.headerMarkMode = "all";
			this._environment.shared.lastMarkMode = 1;
			this._environment.shared.headerStyle = 1;
			this.set(new Jedox.wss.cls.Point(1, 1), new Jedox.wss.cls.Point(
					defMaxCoords[0], defMaxCoords[1]));
			this.draw()
		}
	},
	_positionRng : function(x, y) {
		var that = this, cbScrollTo = function() {
			that.set(new Jedox.wss.cls.Point(x, y), new Jedox.wss.cls.Point(x,
					y));
			that.draw()
		};
		if (this._book._scrollOpPending) {
			return
		}
		this._book.scrollTo( [ this, cbScrollTo ], x, y, false, false)
	},
	selectFirstCell : function() {
		this._positionRng(1, 1)
	},
	selectFirstVisCell : function() {
		var fvcCoords = this._book.getCoordsFirstVCell();
		this._positionRng(fvcCoords[0], fvcCoords[1])
	},
	_copyAndCutImpl : function(op, copyToClp) {
		var env = this._environment.shared, startCoords = env.lastRangeStartCoord, endCoords = env.lastRangeEndCoord, endCoordsCopy = [
				endCoords[0], endCoords[1] ], startPnt = new Jedox.wss.cls.Point(
				startCoords[0], startCoords[1]), endPnt = new Jedox.wss.cls.Point(
				endCoords[0], endCoords[1]), copySel = Jedox.wss.app.copySelection;
		if (copySel != null) {
			copySel.removeAll()
		}
		copySel = env.copySelection;
		copySel.addRange(startPnt);
		copySel.setActiveRange(copySel.getRanges().length - 1);
		copySel.setState(Jedox.wss.range.AreaState.NEW);
		copySel.set(startPnt, endPnt);
		copySel.draw();
		Jedox.wss.app.copySelection = copySel;
		if (startPnt.equals(endPnt)) {
			var mergeInfoStart = this._book.getMergeInfo(startPnt.getX(),
					startPnt.getY());
			if (mergeInfoStart) {
				endCoordsCopy = [ startPnt.getX() + mergeInfoStart[1] - 1,
						startPnt.getY() + mergeInfoStart[2] - 1 ]
			}
		}
		Jedox.wss.app.clipboard = {
			id : Jedox.wss.backend.conn
					.cmd(0, [ op == Jedox.wss.grid.gridOperation.COPY ? "cprn"
							: "ctrn" ], [ [ startCoords[0], startCoords[1],
							endCoordsCopy[0], endCoordsCopy[1] ] ])[0][1][0],
			op : op,
			value : null
		};
		if (copyToClp) {
			var clpVal = "", isFirst = true;
			for ( var i = startCoords[1]; i <= endCoords[1]; i++, isFirst = true, clpVal = clpVal
					.concat("\r\n")) {
				for ( var j = startCoords[0]; j <= endCoords[0]; j++) {
					cellVal = this._book.getCellValue(j, i);
					if (isFirst) {
						isFirst = false
					} else {
						clpVal = clpVal.concat("\t")
					}
					clpVal = clpVal.concat((cellVal == undefined) ? ""
							: cellVal)
				}
			}
			Jedox.wss.general.setSysClipboard(clpVal)
		}
	},
	copy : function(copyToClp) {
		this._copyAndCutImpl(Jedox.wss.grid.gridOperation.COPY, copyToClp)
	},
	cut : function(copyToClp) {
		this._copyAndCutImpl(Jedox.wss.grid.gridOperation.CUT, copyToClp)
	},
	paste : function(pasteWhat) {
		var clipboard = Jedox.wss.app.clipboard;
		if (clipboard == null) {
			var clpVal = Jedox.wss.general.getSysClipboard();
			if (clpVal == null) {
				return
			}
			var rows = clpVal.split("\n"), maxRowLen = 0;
			for ( var i = rows.length - 1; i >= 0; i--) {
				rows[i] = rows[i].split("\t");
				maxRowLen = rows[i].length
			}
			if (rows[rows.length - 1] == "") {
				rows.splice(rows.length - 1, 1)
			}
			for ( var i = rows.length - 1; i >= 0; i--) {
				for ( var j = 0, cnt = maxRowLen - rows[i].length; j < cnt; j++) {
					rows[i].push("")
				}
			}
			clipboard = {
				id : null,
				op : Jedox.wss.grid.gridOperation.CUT,
				value : rows,
				markRngDim : {
					width : maxRowLen,
					height : rows.length
				}
			}
		}
		var env = this._environment.shared, markRng, markRngDim;
		var startCoords = env.lastRangeStartCoord;
		var endCoords = env.lastRangeEndCoord;
		if (clipboard.id == null) {
			markRngDim = (clipboard.markRngDim == undefined) ? {
				width : 1,
				height : 1
			} : clipboard.markRngDim
		} else {
			markRng = Jedox.wss.app.copySelection;
			var markRngCoords = markRng.getActiveRange().getCorners();
			if (markRngCoords[0].equals(markRngCoords[1])) {
				var mergeInfoStart = this._book.getMergeInfo(markRngCoords[0]
						.getX(), markRngCoords[0].getY());
				if (mergeInfoStart) {
					markRngCoords[1] = new Jedox.wss.cls.Point(markRngCoords[0]
							.getX()
							+ mergeInfoStart[1] - 1, markRngCoords[0].getY()
							+ mergeInfoStart[2] - 1)
				}
			}
			markRngDim = {
				width : markRngCoords[1].getX() - markRngCoords[0].getX() + 1,
				height : markRngCoords[1].getY() - markRngCoords[0].getY() + 1
			}
		}
		var defRngDim = {
			width : endCoords[0] - startCoords[0] + 1,
			height : endCoords[1] - startCoords[1] + 1
		};
		if (clipboard.op == Jedox.wss.grid.gridOperation.CUT
				|| (defRngDim.width < markRngDim.width || defRngDim.width
						% markRngDim.width != 0)
				|| (defRngDim.height < markRngDim.height || defRngDim.height
						% markRngDim.height != 0)) {
			this.set(new Jedox.wss.cls.Point(startCoords[0], startCoords[1]),
					new Jedox.wss.cls.Point(startCoords[0] + markRngDim.width
							- 1, startCoords[1] + markRngDim.height - 1));
			this.draw()
		}
		startCoords = env.lastRangeStartCoord;
		endCoords = env.lastRangeEndCoord;
		if (clipboard.id == null) {
			if (clipboard.markRngDim == undefined) {
				this._book.setCellValue(startCoords[0], startCoords[1],
						clipboard.value)
			} else {
				var vals = [];
				for ( var len = clipboard.value.length - 1, i = -1; i < len; vals = vals
						.concat(clipboard.value[++i])) {
				}
				this._book.setRangeValue( [ startCoords[0], startCoords[1],
						endCoords[0], endCoords[1] ], vals)
			}
		} else {
			this._book.pasteRange( [ this, this.draw ], [ startCoords[0],
					startCoords[1], endCoords[0], endCoords[1] ], clipboard.id,
					pasteWhat)
		}
		var cursorValue = this._book.getCellValue(startCoords[0],
				startCoords[1]);
		env.cursorField.innerHTML = (cursorValue == undefined) ? ""
				: cursorValue;
		Jedox.wss.style.cellTransfer(env.cursorField);
		if (clipboard.id != null
				&& clipboard.op == Jedox.wss.grid.gridOperation.CUT) {
			markRng.removeAll();
			Jedox.wss.app.clipboard = null;
			Jedox.wss.action.togglePaste(false)
		}
	},
	hide : function() {
		for ( var i = this._ranges.length - 1; i >= 0; i--) {
			this._ranges[i].hide()
		}
	},
	show : function() {
		for ( var i = this._ranges.length - 1; i >= 0; i--) {
			this._ranges[i].show()
		}
	},
	setCursor : function(el, cur) {
		this._ranges[this._activeRange].setCursor(el, cur)
	},
	getBook : function() {
		return this._book
	},
	getContainer : function() {
		return this._container
	},
	getEnvironment : function() {
		return this._environment
	},
	getDomId : function() {
		return this._book.getDomId()
	}
};