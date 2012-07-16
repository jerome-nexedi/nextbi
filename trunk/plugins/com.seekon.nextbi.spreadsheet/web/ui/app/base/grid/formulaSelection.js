Jedox.wss.grid.formulaSelection = (function() {
	var FormulaSelection = function() {
		var _test;
		this.test = true;
		function _test1() {
		}
		this.joinToGrid = function(book) {
			this._book = book;
			this._container = book.getBookIC();
			this._environment = book.getEnvironment();
			return this
		};
		this._isRect = function() {
			return (this._ranges.length === 1)
		};
		this.setVisibility(Jedox.wss.range.DisplayStatus.HIDDEN)
	};
	FormulaSelection.prototype = new Jedox.wss.grid.Selection();
	FormulaSelection.prototype.constructor = FormulaSelection;
	return new FormulaSelection(new Jedox.wss.cls.Point(1, 1),
			Jedox.wss.range.RangeType.FORMULA, null)
})();