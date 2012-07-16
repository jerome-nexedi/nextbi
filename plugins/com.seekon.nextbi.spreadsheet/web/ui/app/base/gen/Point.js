Jedox.wss.cls.Point = function(x, y) {
	this._x = x;
	this._y = y
};
Jedox.wss.cls.Point.prototype = {
	getX : function() {
		return this._x
	},
	getY : function() {
		return this._y
	},
	setX : function(x) {
		this._x = x
	},
	setY : function(y) {
		this._y = y
	},
	getCoords : function() {
		return [ this._x, this._y ]
	},
	toString : function() {
		return this._x + " / " + this._y
	},
	equals : function(point) {
		return this._x == point._x && this._y == point._y
	},
	equalsX : function(point) {
		return this._x == point._x
	},
	equalsY : function(point) {
		return this._y == point._y
	},
	clone : function() {
		return new Jedox.wss.cls.Point(this._x, this._y)
	}
};