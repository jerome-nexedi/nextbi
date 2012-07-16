Jedox.wss.grid.copyRange = (function() {
	var CopyRange = function() {
		var _test;
		this.test = true;
		function _test1() {
		}
		this.test1 = function() {
			console.log("test1")
		}
	};
	CopyRange.prototype = new Jedox.wss.grid.Range(1, 1);
	CopyRange.prototype.constructor = CopyRange;
	return new CopyRange()
})();