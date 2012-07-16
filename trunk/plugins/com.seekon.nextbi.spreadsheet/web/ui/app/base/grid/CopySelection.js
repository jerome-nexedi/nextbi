Jedox.wss.grid.CopySelection = (function() {
	return function(startCoords, book) {
		Jedox.wss.grid.CopySelection.parent.constructor.call(this, startCoords,
				book);
		this.setVisibility(Jedox.wss.range.DisplayStatus.HIDDEN)
	}
})();
Jedox.util.extend(Jedox.wss.grid.CopySelection, Jedox.wss.grid.Selection);
clsRef = Jedox.wss.grid.CopySelection;
clsRef.prototype._isRect = function() {
	return (this._ranges.length === 1)
};
clsRef.prototype.addRange = function(startPoint, endPoint) {
	return this._ranges.push(new Jedox.wss.grid.CopyRange(this, startPoint,
			endPoint))
};
clsRef = null;