Jedox.gen.SparseVector = function(len, def) {
	this._len = len;
	this._def = def;
	this._els = {};
	this._bit = {}
};
Jedox.gen.SparseVector.prototype = {
	getLen : function() {
		return this._len
	},
	getDef : function() {
		return this._def
	},
	setElemAt : function(i, val) {
		if (i < 1 || i > this._len) {
			return
		}
		var nod, len = this._len, els = this._els, bit = this._bit;
		if (i in els) {
			if (val == this._def) {
				val -= els[i];
				delete els[i]
			} else {
				val -= els[i], els[i] += val
			}
			while (i <= len) {
				bit[i][0] += val, i += i & -i
			}
		} else {
			if (val != this._def) {
				els[i] = val;
				while (i <= len) {
					if (i in bit) {
						(nod = bit[i])[0] += val
					} else {
						nod = bit[i] = [ val, 0 ]
					}
					++nod[1], i += i & -i
				}
			}
		}
		return this
	},
	getElemAt : function(i) {
		if (i < 1 || i > this._len) {
			return
		}
		return i in this._els ? this._els[i] : this._def
	},
	reInit : function(def) {
		this._els = {};
		this._bit = {};
		if (typeof def == "number") {
			this._def = def
		}
		return this
	},
	getSumUpTo : function(i, from) {
		if (i < 1 || i > this._len + 1
				|| (typeof from == "number" && from >= i)) {
			return
		}
		var nod, idx = --i, ndc = 0, nds = 0, bit = this._bit;
		while (i > 0) {
			if (i in bit) {
				nds += (nod = bit[i])[0], ndc += nod[1]
			}
			i -= i & -i
		}
		return nds + (idx - ndc) * this._def
				- (typeof from == "number" ? this.getSumUpTo(from) : 0)
	},
	getIdxByOffset : function(i, offset) {
		if (i < 1 || i > this._len) {
			return
		}
		for ( var s = 0; s <= offset; s += this.getElemAt(i++)) {
		}
		return i - 1
	},
	getSparseArray : function() {
		var array = [];
		for ( var idx in this._els) {
			array.push(idx | 0, this._els[idx])
		}
		return array
	}
};