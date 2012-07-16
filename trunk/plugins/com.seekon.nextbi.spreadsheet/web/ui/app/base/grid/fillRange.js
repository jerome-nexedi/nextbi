Jedox.wss.grid.fillRange = (function() {
	var FillRange = function() {
		var _test;
		this.test = true;
		function _test1() {
		}
		this.test1 = function() {
			console.log("test1")
		}
	};
	FillRange.prototype = new Jedox.wss.grid.Range(1, 1);
	FillRange.prototype.constructor = FillRange;
	return new FillRange()
})();