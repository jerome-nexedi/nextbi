Jedox.wss.grid.DynarangeSubSelection = (function() {
	return function(startCoords, endCoords, book, parent, parentStartPoint,
			parentEndPoint) {
		Jedox.wss.grid.DynarangeSubSelection.parent.constructor.call(this,
				startCoords, book);
		this._endCoords = endCoords;
		this._parent = parent;
		var that = this;
		this._ranges = [ new Jedox.wss.grid.DynarangeSubRange(parent, that,
				startCoords, endCoords, parentStartPoint, parentEndPoint) ];
		this._firstRange = this._ranges[0];
		this._activeRange = 0
	}
})();
Jedox.util.extend(Jedox.wss.grid.DynarangeSubSelection,
		Jedox.wss.grid.Selection);
clsRef = Jedox.wss.grid.DynarangeSubSelection;
clsRef.prototype._isRect = function() {
	return (this._ranges.length === 1)
};
clsRef.prototype.setMode = function(mode) {
	this._mode = mode;
	if (this._mode == Jedox.wss.range.RangeMode.EDIT) {
		Jedox.wss.hb.setAllNormal(this.getActiveRange().getId())
	}
	this.getActiveRange().switchMode(mode)
};
clsRef.prototype.getProps = function() {
	return this.getActiveRange().getProps()
};
clsRef.prototype.setProps = function(props) {
	this.getActiveRange().setProps(props)
};
clsRef.prototype.hide = function() {
	this.setMode(Jedox.wss.range.RangeMode.HIDDEN)
};
clsRef.prototype.show = function() {
	this.setMode(Jedox.wss.range.RangeMode.NONE)
};
clsRef.prototype.remove = function() {
	this.getActiveRange().remove()
};
clsRef = null;