Jedox.wss.grid.CopyRange = (function() {
	return function(selection, startPoint, endPoint) {
		Jedox.wss.grid.CopyRange.parent.constructor.call(this, selection,
				startPoint, endPoint);
		var that = this;
		for ( var htmlEl, clsName = "formularRangeBorder", i = 3; i >= 0; --i) {
			htmlEl = this._edgeElems[i] = document.createElement("div");
			htmlEl.className = clsName;
			this._container.appendChild(htmlEl)
		}
	}
})();
Jedox.util.extend(Jedox.wss.grid.CopyRange, Jedox.wss.grid.Range);
clsRef = Jedox.wss.grid.CopyRange;
clsRef = null;