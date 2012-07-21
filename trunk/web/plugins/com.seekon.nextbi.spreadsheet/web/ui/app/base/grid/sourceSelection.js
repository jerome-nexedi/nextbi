Jedox.wss.grid.sourceSelection = (function() {
	var SourceSelection = function() {
		var _test;
		this.test = true;
		function _test1() {
		}
		this.test1 = function() {
			console.log("test1")
		}
	};
	SourceSelection.prototype = new Jedox.wss.grid.Selection();
	SourceSelection.prototype.constructor = SourceSelection;
	return new SourceSelection()
})();