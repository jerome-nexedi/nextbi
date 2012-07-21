Jedox.wss.backend.conn = new function() {
	var that = this, _json = Ext.util.JSON, _ccmd = [], _cbs = [], _hasCBs = false, _batchOpen = 0, _currCmd;
	this.Q_VALUE = 1;
	this.Q_FORMULA = 2;
	this.Q_STYLE = 4;
	this.Q_FORMULA_WE = 8;
	this.Q_ATTRS = 16;
	this.Q_DIMS = 32;
	this.Q_FMT_VAL = 64;
	this.Q_FMT = 128;
	this.Q_MERGE = 256;
	this.Q_FORMULA_NF = 512;
	this.Q_LOCK = 1024;
	this.Q_ALL = 2013;
	this.xhrHdrs = {};
	this.cmdHdr = [ "cdrn", {
		cm : true
	} ];
	this.dummy_cb = [ this, function() {
	} ];
	this.ping_interval = 240000;
	this.createBatch = function() {
		++_batchOpen
	};
	this.cmd = function(cb, cmdHdr, cmdData, gridRange, diffCols, diffRows,
			diffWhat) {
		var idx = _ccmd.push(_currCmd = cmdHdr.concat(cmdData)) - 1;
		if (_batchOpen) {
			if (cb instanceof Array && cb.length > 1) {
				_cbs.push(cb);
				_hasCBs = true
			} else {
				_cbs.push(undefined)
			}
			return idx
		}
		return that.sendBatch(cb, gridRange, diffCols, diffRows, diffWhat)
	};
	this.cmdData = function(cmdIdx, cmdData) {
		if (!(cmdIdx in _ccmd)) {
			return false
		}
		_ccmd[cmdIdx] = _ccmd[cmdIdx].concat(cmdData);
		return true
	};
	this.setCcmd = function(ccmd) {
		_ccmd = ccmd;
		_cbs = [];
		_hasCBs = false;
		_batchOpen = 1
	};
	this.handleRes = function(data, cbs) {
		var el;
		if (data instanceof Array && (el = data[0]) instanceof Array) {
			if (typeof el[0] == "boolean") {
				var hdr = undefined, res = data
			} else {
				var hdr = data[0], res = (el = data[1]) instanceof Array
						&& (el = el[0]) instanceof Array
						&& typeof el[0] == "boolean" ? data[1] : undefined
			}
		} else {
			var hdr = data, res = undefined
		}
		if (res) {
			Jedox.wss.error.scan(res)
		}
		if (!(cbs instanceof Array) || !cbs.length) {
			return data
		}
		var cb_hdr = cbs.shift(), cb = cb_hdr, len;
		if (hdr !== undefined && cb instanceof Array && cb.length > 1) {
			cb[1].apply(cb[0], [ data ].concat(cb.slice(2)))
		}
		if (!res) {
			return
		}
		if ((len = res.length) && len == cbs.length) {
			for ( var i = 0; i < len; ++i) {
				if ((cb = cbs[i]) instanceof Array && cb.length > 1) {
					cb[1].apply(cb[0], [ res[i] ].concat(cb.slice(2)))
				}
			}
		}
		if (hdr === undefined && (cb = cb_hdr) instanceof Array
				&& cb.length > 1) {
			cb[1].apply(cb[0], [ data ].concat(cb.slice(2)))
		}
	};
	this.sendBatch = function(cb, gridRange, diffCols, diffRows, diffWhat) {
		if (_batchOpen > 1) {
			--_batchOpen;
			return true
		}
		_batchOpen = 0;
		_currCmd = undefined;
		if (!_ccmd.length) {
			return undefined
		}
		if (typeof _ccmd == "object") {
			_ccmd = _json.encode(_ccmd)
		}
		var dimSet = diffCols != false ? 1 : 0;
		if (diffRows != false) {
			dimSet |= 2
		}
		if (cb instanceof Array && cb.length > 1) {
			_cbs.unshift(cb);
			_hasCBs = true
		} else {
			_cbs.unshift(undefined)
		}
		if (!_hasCBs) {
			res = typeof gridRange != "object" ? that.ccmd_s(_ccmd) : that
					.rpc_s("WSS", "exec",
							[ _ccmd, gridRange, dimSet, diffWhat ]);
			_ccmd = [];
			_cbs = [];
			return that.handleRes(res)
		}
		var post = [ that, that.handleRes, _cbs ];
		if (typeof gridRange != "object") {
			that.ccmd(post, _ccmd)
		} else {
			that.rpc(post, "WSS", "exec",
					[ _ccmd, gridRange, dimSet, diffWhat ])
		}
		_ccmd = [];
		_cbs = [];
		_hasCBs = false;
		return true
	};
	this.ccmd = function(cb, ccmd) {
		if (!(cb instanceof Array) || cb.length < 2) {
			return false
		}
		if (typeof ccmd == "object") {
			ccmd = _json.encode(ccmd)
		}
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "ub/ccmd", true);
		xhr.onreadystatechange = function(ev) {
			if (xhr.readyState == 4 && xhr.status == 200) {
				cb[1].apply(cb[0], [ _json.decode(xhr.responseText) ].concat(cb
						.slice(2)))
			}
		};
		for ( var hdrName in that.xhrHdrs) {
			xhr.setRequestHeader(hdrName, that.xhrHdrs[hdrName])
		}
		xhr.send(ccmd);
		return true
	};
	this.ccmd_s = function(ccmd) {
		if (typeof ccmd == "object") {
			ccmd = _json.encode(ccmd)
		}
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "ub/ccmd", false);
		for ( var hdrName in that.xhrHdrs) {
			xhr.setRequestHeader(hdrName, that.xhrHdrs[hdrName])
		}
		xhr.send(ccmd);
		return xhr.readyState == 4 && xhr.status == 200 ? _json
				.decode(xhr.responseText) : undefined
	};
	this.rpc = function(cb, obj, method, params) {
		if (!(cb instanceof Array) || cb.length < 2) {
			return false
		}
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "cc/rpc.php", true);
		xhr.onreadystatechange = function(ev) {
			if (xhr.readyState == 4 && xhr.status == 200) {
				cb[1].apply(cb[0], [ _json.decode(xhr.responseText) ].concat(cb
						.slice(2)))
			}
		};
		for ( var hdrName in that.xhrHdrs) {
			xhr.setRequestHeader(hdrName, that.xhrHdrs[hdrName])
		}
		xhr.send(_json.encode( [ obj, method, params ]));
		return true
	};
	this.rpc_s = function(obj, method, params) {
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "cc/rpc.php", false);
		for ( var hdrName in that.xhrHdrs) {
			xhr.setRequestHeader(hdrName, that.xhrHdrs[hdrName])
		}
		xhr.send(_json.encode( [ obj, method, params ]));
		return xhr.readyState == 4 && xhr.status == 200 ? _json
				.decode(xhr.responseText) : undefined
	};
	this.ping = function() {
		that.ccmd(that.dummy_cb, that.ping_cmd);
		setTimeout(that.ping, that.ping_interval)
	}
};