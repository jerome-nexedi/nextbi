Jedox.wss.wsel.FormBase = (function() {
	return function(conf) {
		Jedox.wss.wsel.FormBase.parent.constructor.call(this, conf)
	}
})();
Jedox.util.extend(Jedox.wss.wsel.FormBase, Jedox.wss.wsel.Base);
Jedox.wss.wsel.FormBase.trgTypes = {
	RNG : "trange",
	NRNG : "tnamedrange",
	VAR : "tvar"
};
clsRef = Jedox.wss.wsel.FormBase;
clsRef.prototype.updateDependencies = function() {
	var activeBook = Jedox.wss.app.activeBook, conn = Jedox.wss.backend.conn, env = Jedox.wss.app.environment;
	if (this.conf.trange.length > 0) {
		var destRng = Jedox.wss.formula.parse(this.conf.trange)[0];
		if (destRng.sheet.length
				&& destRng.sheet != activeBook._sheetSelector
						.getActiveSheetName()) {
			var actSheetId = Jedox.wss.app.activeSheet._uid, tSheetId = activeBook._sheetSelector
					.getIdByName(destRng.sheet);
			conn.cmd(0, [ "osel" ], [ 2, tSheetId ])
		}
		conn.cmd(0, [ "cdrn" ], [ {
			cm : true
		}, destRng.rng.concat( {
			v : this.conf.selval
		}) ]);
		if (tSheetId) {
			conn.cmd(0, [ "osel" ], [ 2, actSheetId ])
		}
	}
	if (this.conf.tnamedrange.length > 0) {
		var actCellCoords = (env.viewMode == Jedox.wss.grid.viewMode.USER) ? [
				1, 1 ] : env.selectedCellCoords;
		conn
				.cmd(
						0,
						[ "nupd" ],
						[ [
								[ this.conf.tnamedrange,
										Jedox.wss.app.activeSheet.getUid() ],
								actCellCoords[0],
								actCellCoords[1],
								{
									refers_to : "="
											.concat(typeof this.conf.selval == "string" ? Ext.util.JSON
													.encode(this.conf.selval)
													: this.conf.selval)
								} ] ])
	}
	if (this.conf.tvar.length > 0) {
		conn.cmd(null, [ "svar" ], [ this.conf.tvar, this.conf.selval ])
	}
};
clsRef.prototype._getMacroData = function() {
	return {
		id : this.conf.id,
		type : this.conf.type,
		name : this.conf.name,
		macros : this.conf.macros
	}
};
clsRef.prototype.showContextMenu = function(e) {
	var that = this, contextMenu = new Ext.menu.Menu( {
		id : "formelContextMenu",
		enableScrolling : false,
		listeners : {
			hide : function(menu) {
				menu.destroy()
			}
		},
		items : [
				{
					text : "formel_edit".localize( {
						type : this.conf.type
					}).concat("..."),
					iconCls : "icon_edit",
					handler : function() {
						that.edit.call(that)
					}
				},
				{
					text : "formel_delete".localize( {
						type : this.conf.type
					}),
					iconCls : "icon_delete",
					handler : function() {
						that.remove.call(that)
					}
				},
				"-",
				{
					text : "Assign Macro".localize().concat("..."),
					iconCls : "icon_macro",
					handler : function() {
						Jedox.wss.app.load(
								Jedox.wss.app.dynJSRegistry.insertMacro,
								[ that._getMacroData.call(that) ])
					}
				} ]
	});
	var coords = e.getXY();
	contextMenu.showAt( [ coords[0], coords[1] ])
};
clsRef.prototype.edit = function() {
	Jedox.wss.app
			.load(Jedox.wss.app.dynJSRegistry.formatControl, [ this.conf ])
};
clsRef.prototype.remove = function() {
	var conn = Jedox.wss.backend.conn, activeBook = Jedox.wss.app.activeBook;
	this.elem.destroy();
	this.formelWrapperEl.remove();
	if (this.conf.tnamedrange.length > 0) {
		var ngetRes = conn.cmd(0, [ "nget" ], [ [ 1, 1, this.conf.tnamedrange,
				Jedox.wss.app.activeSheet.getUid() ] ])[0]
	}
	conn.createBatch();
	if (this.conf.tnamedrange.length > 0 && ngetRes[0]) {
		conn.cmd(0, [ "ndel" ], [ ngetRes[1][0].uuid ])
	}
	conn.cmd(0, [ "wdel" ], [ "", [ this.conf.id ] ]);
	conn.sendBatch( [ activeBook, activeBook.exec ], activeBook
			.getRealGridRange())
};
clsRef.prototype.unload = function() {
	var env = Jedox.wss.app.environment, viewMode = Jedox.wss.grid.viewMode, gridIC = Jedox.wss.app.activeBook
			.getBookIC();
	try {
		if (env.viewMode == viewMode.DESIGNER
				|| env.viewMode == viewMode.PREVIEW) {
			this.elem.destroy();
			gridIC.removeChild(document.getElementById(this.gContId.concat(
					"_formel_cont_", this.conf.id, "-rzwrap")));
			gridIC.removeChild(document.getElementById(this.gContId.concat(
					"_formel_cont_", this.conf.id, "-rzwrap-rzproxy")))
		} else {
			gridIC.removeChild(document.getElementById(this.gContId.concat(
					"_formel_cont_", this.conf.id)))
		}
	} catch (e) {
	}
};
clsRef.prototype.assignMacro = function(macros) {
	var updObj = {};
	updObj[this.conf.id] = {
		macros : macros
	};
	if (!Jedox.wss.backend.conn.cmd(0, [ "wupd" ], [ "", updObj ])) {
		throw [ "formel_assign_macro_err", {} ]
	}
	this.conf.macros = macros
};
clsRef.prototype.isCmpMoved = function(comp) {
	if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.USER) {
		return false
	}
	var targetEl = comp.getEl().dom, targetId = this.gContId.concat(
			"_formel_cont_", this.conf.id, "-rzwrap");
	while (targetEl.id != targetId) {
		targetEl = targetEl.parentNode
	}
	return targetEl.offsetTop != this.conf.top
			|| targetEl.offsetLeft != this.conf.left
};
clsRef.prototype.move = function(data) {
	var tlOffsetXY = Jedox.wss.app.activeBook.getPixelsByCoords(data.pos[0],
			data.pos[1]), newX = tlOffsetXY[0] + data.offsets[0], newY = tlOffsetXY[1]
			+ data.offsets[1], style = this.formelWrapperEl.dom.style;
	style.left = "".concat(newX, "px");
	style.top = "".concat(newY, "px");
	this.conf.left = newX;
	this.conf.top = newY
};
clsRef.prototype.getTargetType = function() {
	return this.conf.trange.length ? "trange"
			: (this.conf.tnamedrange.length ? "tnamedrange" : "tvar")
};
clsRef = null;