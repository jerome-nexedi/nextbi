Jedox.wss.grid.DefaultSelection = (function() {
	return function(startCoords, book) {
		Jedox.wss.grid.DefaultSelection.parent.constructor.call(this,
				startCoords, book);
		this._ranges = [ new Jedox.wss.grid.DefaultRange(this,
				this._startCoords, this._startCoords) ];
		this._firstRange = this._ranges[0];
		this._activeRange = 0;
		this._ranges[this._activeRange].activate();
		this._setLegacyVars()
	}
})();
Jedox.util.extend(Jedox.wss.grid.DefaultSelection, Jedox.wss.grid.Selection);
clsRef = Jedox.wss.grid.DefaultSelection;
clsRef.prototype._isRect = function() {
	return (this._ranges.length === 1)
};
clsRef.prototype.addRange = function(startPoint, endPoint) {
	return this._ranges.push(new Jedox.wss.grid.DefaultRange(this, startPoint,
			endPoint))
};
clsRef.prototype.expandToCell = function(cell) {
	this._selectionChanged = true;
	this._ranges[this._activeRange].expandToCell(cell)
};
clsRef.prototype.moveTo = function(x, y, mode) {
	this._selectionChanged = true;
	this._ranges[this._activeRange].moveTo(x, y);
	this.checkForUndoneMarkers()
};
clsRef.prototype.expand = function(amount, direction, defExpand) {
	this._selectionChanged = true;
	this._ranges[this._activeRange].expand(amount, direction, defExpand)
};
clsRef.prototype.show = function() {
	for ( var i = this._ranges.length - 1; i >= 0; i--) {
		if (this._ranges[i].isVisible()) {
			return
		}
		this._ranges[i].show()
	}
	this._selectionChanged = true;
	this.checkForUndoneMarkers()
};
clsRef.prototype.jumpTo = function(rng) {
	this.set(new Jedox.wss.cls.Point(rng[0], rng[1]), new Jedox.wss.cls.Point(
			rng[2], rng[3]));
	this.draw()
};
clsRef = null;