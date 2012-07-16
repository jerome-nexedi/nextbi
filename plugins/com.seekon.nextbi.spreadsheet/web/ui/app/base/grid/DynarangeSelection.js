Jedox.wss.grid.DynarangeSelection = (function() {
	return function(book, props) {
		var startCoords = new Jedox.wss.cls.Point(props.src[0], props.src[1]);
		Jedox.wss.grid.DynarangeSelection.parent.constructor.call(this,
				startCoords, book);
		this.dynarange = true;
		this._endCoords = new Jedox.wss.cls.Point(props.src[2], props.src[3]);
		this._ranges = [ new Jedox.wss.grid.DynarangeRange(this,
				this._startCoords, this._endCoords, props) ];
		this._firstRange = this._ranges[0];
		this._activeRange = 0
	}
})();
Jedox.util.extend(Jedox.wss.grid.DynarangeSelection, Jedox.wss.grid.Selection);
clsRef = Jedox.wss.grid.DynarangeSelection;
clsRef.prototype._isRect = function() {
	return (this._ranges.length === 1)
};
clsRef.prototype.setMode = function(mode) {
	this._mode = mode;
	if (this._mode == Jedox.wss.range.RangeMode.EDIT) {
		Jedox.wss.hb.setAllNormal(this.getActiveRange().getId());
		this._environment.shared.defaultSelection.hide();
		this._selectionChanged = true;
		this.checkForUndoneMarkers()
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
clsRef.prototype.redraw = function() {
	var actRng = this.getActiveRange();
	actRng.draw(false);
	actRng.setNormalMode()
};
clsRef.prototype.remove = function(perm) {
	this.getActiveRange().remove(perm)
};
clsRef.prototype.move = function(pos) {
	this.getActiveRange().move(pos)
};
clsRef = null;