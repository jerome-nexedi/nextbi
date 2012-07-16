Jedox.wss.cls.AutoScroll = function() {
	var that = this;
	var _hook = null;
	this.scrollElem = false;
	var _defScrollSteps = [ [ 25, 250 ], [ 50, 150 ], [ 75, 50 ], [ 100, 25 ] ];
	function _calcScrollSpeed(scrollArea, position) {
		var scrollPerc = position * 100 / scrollArea;
		for ( var i = 0; i < _defScrollSteps.length; ++i) {
			if (scrollPerc <= _defScrollSteps[i][0]) {
				return _defScrollSteps[i][1]
			}
		}
	}
	function _stopScroll(ev) {
		clearTimeout(that.scrollElem);
		var scrollType = Jedox.wss.grid.scrollType;
		var activeBook = Jedox.wss.app.activeBook;
		activeBook.stopScrollGrid(scrollType.HORIZ);
		activeBook.stopScrollGrid(scrollType.VERT);
		Jedox.wss.app.mouseUpObserver.unsubscribe(_stopScroll);
		if (_hook != null) {
			activeBook.scrollObserver.unsubscribe(_hook)
		}
	}
	this.checkAndScroll = function(ev, hook, direction, innerOffset, scope) {
		_hook = hook;
		var gridScreenCoords = Jedox.wss.app.environment.gridScreenCoords, scrollType = Jedox.wss.grid.scrollType, horScrollDir = Jedox.wss.grid.horScrollDir, vertScrollDir = Jedox.wss.grid.vertScrollDir, activeBook = Jedox.wss.app.activeBook, scrollAction = 0;
		var cbStartScrollGrid = function() {
			if (direction == scrollType.ALL || direction == scrollType.VERT) {
				if (ev.clientY >= gridScreenCoords[0][1]
						&& ev.clientY <= gridScreenCoords[1][1]) {
					activeBook.stopScrollGrid(scrollType.VERT)
				} else {
					Jedox.wss.app.mouseUpObserver.subscribe(_stopScroll, scope);
					activeBook.scrollObserver.subscribe(hook, scope);
					if (ev.clientY > gridScreenCoords[1][1]) {
						activeBook.startScrollGrid(undefined, scrollType.VERT,
								vertScrollDir.DOWN, _calcScrollSpeed(
										gridScreenCoords[2][1]
												- gridScreenCoords[1][1],
										ev.clientY - gridScreenCoords[1][1]))
					} else {
						activeBook.startScrollGrid(undefined, scrollType.VERT,
								vertScrollDir.UP, _calcScrollSpeed(
										gridScreenCoords[0][1],
										gridScreenCoords[0][1] - ev.clientY))
					}
				}
			}
		};
		if (direction == scrollType.ALL || direction == scrollType.HORIZ) {
			if (innerOffset == undefined) {
				if (ev.clientX < gridScreenCoords[0][0]) {
					scrollAction--
				} else {
					if (ev.clientX > gridScreenCoords[1][0]) {
						scrollAction++
					}
				}
			} else {
				if (ev.clientX >= gridScreenCoords[0][0]
						&& ev.clientX <= gridScreenCoords[0][0] + innerOffset) {
					scrollAction--
				} else {
					if (ev.clientX >= gridScreenCoords[1][0] - innerOffset
							&& ev.clientX <= gridScreenCoords[1][0]) {
						scrollAction++
					}
				}
			}
			if (scrollAction == 0) {
				activeBook.stopScrollGrid(scrollType.HORIZ)
			} else {
				Jedox.wss.app.mouseUpObserver.subscribe(_stopScroll, scope);
				if (hook != null) {
					activeBook.scrollObserver.subscribe(hook, scope)
				}
				if (scrollAction > 0) {
					activeBook.startScrollGrid( [ this, cbStartScrollGrid ],
							scrollType.HORIZ, horScrollDir.RIGHT,
							(innerOffset == undefined) ? _calcScrollSpeed(
									gridScreenCoords[2][0]
											- gridScreenCoords[1][0],
									ev.clientX - gridScreenCoords[1][0])
									: _defScrollSteps[1][1])
				} else {
					activeBook.startScrollGrid( [ this, cbStartScrollGrid ],
							scrollType.HORIZ, horScrollDir.LEFT,
							_calcScrollSpeed(gridScreenCoords[0][0],
									gridScreenCoords[0][0] - ev.clientX))
				}
				return
			}
		}
		cbStartScrollGrid()
	}
};