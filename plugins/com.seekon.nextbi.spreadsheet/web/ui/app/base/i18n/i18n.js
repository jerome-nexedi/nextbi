String.prototype.localize = function(params) {
	var orig = this.toString(), trans = Jedox.wss.i18n.strings[orig];
	if (trans) {
		if (params instanceof Object) {
			for ( var p in params) {
				trans = trans.replace("{".concat(p, "}"), params[p])
			}
		}
		return trans
	}
	return orig
};