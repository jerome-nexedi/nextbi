Jedox.wss.grid.FillSelection = (function() {
	return function(startCoords, book) {
		Jedox.wss.grid.FillSelection.parent.constructor.call(this, startCoords,
				book);
		this._ranges = [ new Jedox.wss.grid.FillRange(this, startCoords,
				startCoords) ];
		this._firstRange = this._ranges[0];
		this._activeRange = 0
	}
})();
Jedox.util.extend(Jedox.wss.grid.FillSelection, Jedox.wss.grid.Selection);
clsRef = Jedox.wss.grid.FillSelection;
clsRef = null;