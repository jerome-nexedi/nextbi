Jedox.wss.grid.PaneCache = function(pane) {
	this._pane = pane;
	this._conn = Jedox.wss.backend.conn;
	this._json = Ext.util.JSON;
	this._xhrHdrs = this._conn.xhrHdrs;
	this._raCoef = this._READAHEAD_COEF;
	this.clear()
};
Jedox.wss.grid.PaneCache.prototype = {
	_READAHEAD_COEF : 2,
	clear : function() {
		this._map = {};
		this._rng = [ 0, 0, 0, 0 ]
	},
	get : function(x, y) {
		if (x in this._map) {
			return this._map[x][y]
		}
		return undefined
	},
	getPart : function(x, y, type) {
		if (x in this._map) {
			x = this._map[x];
			if (y in x) {
				return x[y][type]
			}
			return undefined
		}
		return undefined
	},
	has : function(x, y) {
		return x in this._map && y in this._map[x]
	},
	hasPart : function(x, y, type) {
		return x in this._map && y in this._map[x] && type in this._map[x][y]
	},
	abort : function() {
		if (this._activeXHR) {
			this._activeXHR.abort();
			delete this._activeXHR
		}
	},
	miss : function(rng) {
		return rng[0] < this._rng[0] || rng[2] > this._rng[2]
				|| rng[1] < this._rng[1] || rng[3] > this._rng[3]
	},
	load : function(cb, rng, opts, spec) {
		if (typeof opts != "object") {
			opts = {}
		}
		if (opts.sdr in rng) {
			var h_incr = this._pane._numCols * this._raCoef, v_incr = this._pane._numRows
					* this._raCoef, fuc = this._pane._sheet._farthestUsedCell;
			switch (opts.sdr) {
			case 0:
				if ((rng[0] -= h_incr) < 1) {
					rng[0] = 1
				}
				break;
			case 2:
				if ((rng[2] += h_incr) > fuc[0]) {
					rng[2] = fuc[0]
				}
				break;
			case 1:
				if ((rng[1] -= v_incr) < 1) {
					rng[1] = 1
				}
				break;
			case 3:
				if ((rng[3] += v_incr) > fuc[1]) {
					rng[3] = fuc[1]
				}
				break
			}
		}
		var that = this, xhr = new XMLHttpRequest();
		xhr.open("POST", "ub/ccmd", true);
		for ( var hdrName in this._xhrHdrs) {
			xhr.setRequestHeader(hdrName, this._xhrHdrs[hdrName])
		}
		xhr.onreadystatechange = function(ev) {
			if (xhr.readyState == 4 && xhr.status == 200) {
				that.fill.call(that, cb, that._json.decode(xhr.responseText),
						rng, opts)
			}
		};
		xhr.send('[["grar",'.concat(this._conn.Q_ALL, ",", rng[0], ",", rng[1],
				",", rng[2], ",", rng[3], opts.rfr ? ",true]]" : "]]"));
		if (spec) {
			delete this._activeXHR
		} else {
			this._activeXHR = xhr
		}
	},
	fill : function(cb, rgns, rng, opts) {
		var map = this._map = {}, grid = this._pane.getGridRng(), frn = opts.frn ? this._pane.furnishCell
				: false;
		this._rng = rng;
		rgns = rgns[0];
		for ( var rgn, i = 0; (rgn = rgns[++i]) != undefined;) {
			for ( var cell, x = rgn[0], y = rgn[1], w = rgn[2], j = 2; (cell = rgn[++j]) != undefined;) {
				if (!(x in map)) {
					map[x] = {}
				}
				map[x][y] = cell;
				if (frn && x >= grid[0] && x <= grid[2] && y >= grid[1]
						&& y <= grid[3]) {
					frn.call(this._pane, x, y, cell)
				}
				if (++x - rgn[0] >= w && w > 0) {
					++y, x = rgn[0]
				}
			}
		}
		if (cb instanceof Array && cb.length > 1) {
			cb[1].apply(cb[0], cb.slice(2))
		}
	}
};