Jedox.wss.cls.MagicDot = function(startCoords, id, parentElement) {
	var that = this;
	var _htmlElement = document.createElement("div");
	var _htmlLines = [];
	var _parent = parentElement;
	var _scope = this;
	var _initialMovement;
	var _offsetX;
	var _offsetY;
	var _startCoords;
	var _environment = _parent.getEnvironment();
	var _book = _parent.getBook();
	function _reverseDirection(direction) {
		var newDirection;
		switch (direction) {
		case Jedox.wss.grid.ScrollDirection.RIGHT:
			newDirection = Jedox.wss.grid.ScrollDirection.LEFT;
			break;
		case Jedox.wss.grid.ScrollDirection.LEFT:
			newDirection = Jedox.wss.grid.ScrollDirection.RIGHT;
			break;
		case Jedox.wss.grid.ScrollDirection.DOWN:
			newDirection = Jedox.wss.grid.ScrollDirection.UP;
			break;
		case Jedox.wss.grid.ScrollDirection.UP:
			newDirection = Jedox.wss.grid.ScrollDirection.DOWN;
			break
		}
		return newDirection
	}
	this.modifyRange = function(ev) {
		if (document.all) {
			ev = window.event
		}
		var targetCell = document.all ? ev.srcElement : ev.target;
		var cell = _parent.getLastChangedCell();
		var neighbourCells = _getNeighbourCells(cell);
		var monitorArea = _getMonitorArea(neighbourCells);
		var gridScreenCoords = _environment.shared.gridScreenCoords;
		var scrollDir = Jedox.wss.grid.ScrollDirection;
		var directionX = scrollDir.NONE;
		var directionY = scrollDir.NONE;
		var direction = scrollDir.NONE;
		var changeDirection = false;
		var focusCell;
		console.log(monitorArea);
		if (ev.clientX > monitorArea[1].getX()) {
			directionX = scrollDir.RIGHT
		} else {
			if (ev.clientX < monitorArea[0].getX()) {
				directionX = scrollDir.LEFT
			}
		}
		if (ev.clientY > monitorArea[1].getY()) {
			directionY = scrollDir.DOWN
		} else {
			if (ev.clientY < monitorArea[0].getY()) {
				directionY = scrollDir.UP
			}
		}
		if (directionX == scrollDir.NONE && directionY == scrollDir.NONE) {
			return
		}
		direction = (ev.clientX - _offsetX > ev.clientY - _offsetY) ? directionX
				: directionY;
		var corners = _parent.getCorners();
		switch (direction) {
		case scrollDir.RIGHT:
			focusCell = neighbourCells.E;
			break;
		case scrollDir.LEFT:
			focusCell = neighbourCells.W;
			break;
		case scrollDir.DOWN:
			focusCell = neighbourCells.S;
			break;
		case scrollDir.UP:
			focusCell = neighbourCells.X;
			break
		}
		if (_initialMovement == scrollDir.NONE) {
			_initialMovement = direction
		}
		var focusCellCoords = _book.getCoordsByCell(focusCell);
		if ((ev.clientX - _offsetX) > (ev.clientY - _offsetY)) {
			var endPoint = new Jedox.wss.cls.Point(focusCellCoords[0],
					_startCoords[1].getY());
			_parent.hideElement(0);
			_parent.hideElement(1);
			_parent.hideElement(2);
			_parent.showElement(3)
		} else {
			if ((ev.clientY - _offsetY) > (ev.clientX - _offsetX)) {
				var endPoint = new Jedox.wss.cls.Point(_startCoords[1].getX(),
						focusCellCoords[1]);
				_parent.hideElement(0);
				_parent.hideElement(2);
				_parent.hideElement(3);
				_parent.showElement(1)
			}
		}
		var startPoint = new Jedox.wss.cls.Point(_startCoords[0].getX(),
				_startCoords[0].getY());
		_environment.shared.defaultSelection.set(startPoint, endPoint);
		_parent.setLastChangedCell(focusCell);
		_scope.draw()
	};
	this.expand = function() {
		var corners = _parent.getCornersPx();
		_htmlElement.style.width = corners[1].getX() - corners[0].getX() + "px";
		_htmlElement.style.height = corners[1].getY() - corners[0].getY()
				+ "px"
	};
	this.setBorders = function(upperLeft, lowerRight) {
		_upperLeft = upperLeft.clone();
		_lowerRight = lowerRight.clone()
	};
	this.setSizes = function(x, y, width, height) {
		var edge = _parent.getHtmlEdges()[0];
		var viewportPos = Jedox.wss.app.activeBook.getViewportPos();
		_initialMovement = Jedox.wss.grid.ScrollDirection.NONE;
		_startCoords = _parent.getParent().getStartCoords();
		_offsetX = edge.offsetLeft + edge.offsetWidth - viewportPos[0][0];
		_offsetY = edge.offsetTop + edge.offsetHeight - viewportPos[0][1];
		Jedox.wss.app.mouseUpObserver.subscribe(_scope.hide)
	};
	this.draw = function() {
		var rangeLines = _parent.getHtmlElements();
		for ( var i = 0; i < 4; i++) {
			if (i == 0 || i == 1) {
				_htmlLines[i].style.width = _htmlElement.offsetWidth + 3 + "px";
				_htmlLines[i].style.height = "3px"
			} else {
				_htmlLines[i].style.width = "3px";
				_htmlLines[i].style.height = _htmlElement.offsetHeight + 3
						+ "px"
			}
			switch (i) {
			case 0:
				_htmlLines[i].style.top = Jedox.util.offsetTop(_htmlElement)
						+ "px";
				_htmlLines[i].style.left = Jedox.util.offsetLeft(_htmlElement)
						+ "px";
				break;
			case 1:
				_htmlLines[i].style.top = Jedox.util.offsetTop(_htmlElement)
						+ _htmlElement.offsetHeight + "px";
				_htmlLines[i].style.left = Jedox.util.offsetLeft(_htmlElement)
						+ "px";
				break;
			case 2:
				_htmlLines[i].style.top = Jedox.util.offsetTop(_htmlElement)
						+ "px";
				_htmlLines[i].style.left = Jedox.util.offsetLeft(_htmlElement)
						+ "px";
				break;
			case 3:
				_htmlLines[i].style.top = Jedox.util.offsetTop(_htmlElement)
						+ "px";
				_htmlLines[i].style.left = Jedox.util.offsetLeft(_htmlElement)
						+ _htmlElement.offsetWidth + "px";
				break
			}
			_htmlLines[i].style.display = "block"
		}
	};
	this.hide = function() {
		for ( var i = 0; i < 4; i++) {
			_htmlLines[i].style.display = "none"
		}
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.READY);
		_initialMovement = Jedox.wss.grid.ScrollDirection.NONE;
		_parent.setVisibility(Jedox.wss.range.DisplayStatus.VISIBLE);
		_parent.draw()
	};
	this.set = function(upperLeft, lowerRight) {
		_upperLeft = upperLeft;
		_lowerRight = lowerRight
	};
	function _init() {
		var container = _parent.getContainer();
		for ( var i = 0; i < 4; i++) {
			_htmlLines.push(document.createElement("div"));
			_htmlLines[i].className = "modifyingBorder";
			container.appendChild(_htmlLines[i])
		}
		_htmlElement.style.position = "absolute";
		container.appendChild(_htmlElement)
	}
	this.getType = function() {
		return Jedox.wss.range.AreaStatus.FORMULA
	};
	_init()
};