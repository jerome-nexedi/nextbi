Jedox.wss.grid.FormulaSelection = (function() {
	return function(startCoords, book) {
		Jedox.wss.grid.FormulaSelection.parent.constructor.call(this,
				startCoords, book);
		this.setVisibility(Jedox.wss.range.DisplayStatus.HIDDEN)
	}
})();
Jedox.util.extend(Jedox.wss.grid.FormulaSelection, Jedox.wss.grid.Selection);
clsRef = Jedox.wss.grid.FormulaSelection;
clsRef.prototype.addRange = function(startPoint, endPoint, isPassive) {
	return this._ranges.push(new Jedox.wss.grid.FormulaRange(this, startPoint,
			endPoint, isPassive))
};
clsRef.prototype.moveTo = function(x, y, mode) {
	this._ranges[this._activeRange].moveTo(x, y);
	this.checkForUndoneMarkers()
};
clsRef.prototype.expand = function(amount, direction) {
	this._ranges[this._activeRange].expand(amount, direction)
};
clsRef.prototype._refreshElement = function(scope, range) {
	range.formulaUpdate();
	range.draw()
};
clsRef = null;