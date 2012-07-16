Jedox.wss.wsel = {
	type : {
		COMBO_BOX : "ComboBox",
		CHECK_BOX : "CheckBox",
		BUTTON : "Button"
	},
	contIdSrc : "activeSheet",
	moveRegistry : [],
	add : function(conf) {
		try {
			var el = new Jedox.wss.wsel[conf.type](conf)
		} catch (e) {
			return [ false ].concat(e)
		}
		return [ true ]
	},
	update : function(conf) {
		var elem = Ext.getCmp(Jedox.wss.app[this.contIdSrc]._domId.concat("_",
				conf.type, "_", conf.id));
		if (elem) {
			try {
				elem.wsel.update(conf)
			} catch (e) {
				return [ false ].concat(e)
			}
		} else {
			return [ false, "formel_no_el", {} ]
		}
		return [ true ]
	},
	loadAll : function() {
		function _load(res) {
			if (!(res instanceof Array) || res[0] !== true) {
				return
			}
			function setUpdObj(updObj, formEl) {
				if (!formEl.n_target_ref) {
					updObj[formEl.e_id] = {
						n_target_ref : "="
								.concat(formEl.data.trange.length ? formEl.data.trange
										: formEl.data.tnamedrange)
					};
					var loc = formEl.n_location.split(",", 2);
					if (loc.length > 1) {
						updObj[formEl.e_id]["n_location"] = loc[0]
					}
				}
				if (formEl.data.tvar == undefined) {
					formEl.data.tvar = "";
					if (updObj[formEl.e_id]) {
						updObj[formEl.e_id]["data"] = formEl.data
					} else {
						updObj[formEl.e_id] = {
							data : formEl.data
						}
					}
				}
			}
			function fixDims(type, conf) {
				var minDims = Jedox.wss.wsel[type]["minDims"];
				if (conf.width < minDims.w) {
					conf.width = minDims.w
				}
				if (conf.height < minDims.h) {
					conf.height = minDims.h
				}
			}
			res = res[1];
			for ( var formEl, rng, tlXY, lrXY, offsets, updObj = {}, i = res.length - 1, activeBook = Jedox.wss.app.activeBook, windowId = activeBook
					.getWinId(), conf; i >= 0; i--) {
				formEl = res[i];
				conf = {
					id : formEl.e_id,
					type : formEl.formel_type,
					name : formEl.formel_name,
					winId : windowId
				};
				switch (formEl.formel_type) {
				case this.type.COMBO_BOX:
					conf.src = formEl.n_refers_to;
					conf.treeVal = formEl.n_get_val;
					conf.targetVal = formEl.n_target_val != undefined ? (formEl.n_target_val instanceof Array ? formEl.n_target_val[0]
							: formEl.n_target_val)
							: null;
					conf.trange = formEl.data.trange;
					conf.tnamedrange = formEl.data.tnamedrange;
					conf.selval = formEl.data.selval;
					conf.selpath = formEl.data.selpath;
					conf.formulaEnabled = formEl.n_use_locale;
					setUpdObj(updObj, formEl);
					conf.tvar = formEl.data.tvar;
					break;
				case this.type.CHECK_BOX:
					conf.targetVal = formEl.n_target_val != undefined ? formEl.n_target_val
							: null;
					conf.trange = formEl.data.trange;
					conf.tnamedrange = formEl.data.tnamedrange;
					conf.state = formEl.data.selval;
					conf.label = formEl.data.label;
					setUpdObj(updObj, formEl);
					conf.tvar = formEl.data.tvar;
					break;
				case this.type.BUTTON:
					conf.label = formEl.data.label;
					break
				}
				conf.macros = formEl.macros ? formEl.macros : {};
				rng = this.getRngFromNLoc(formEl.n_location);
				tlXY = activeBook.getPixelsByCoords(rng[0], rng[1]);
				lrXY = activeBook.getPixelsByCoords(rng[2], rng[3]);
				offsets = formEl.pos_offsets;
				conf.top = tlXY[1] + offsets[1];
				conf.left = tlXY[0] + offsets[0];
				conf.height = lrXY[1] + offsets[3] - (tlXY[1] + offsets[1]);
				conf.width = lrXY[0] + offsets[2] - (tlXY[0] + offsets[0]);
				fixDims(formEl.formel_type, conf);
				this.add(conf)
			}
			for ( var updItem in updObj) {
				Jedox.wss.backend.conn.cmd(null, [ "wupd" ], [ "", updObj ]);
				break
			}
		}
		Jedox.wss.backend.conn.cmd( [ this, _load ], [ "wget" ], [
				"",
				[],
				[ "e_id", "formel_type", "formel_name", "n_refers_to",
						"n_get_val", "n_target_ref", "n_target_val",
						"n_location", "n_use_locale", "pos_offsets", "macros",
						"data" ], {
					e_type : "formel"
				} ])
	},
	unloadAll : function() {
		var res = Jedox.wss.backend.conn.cmd(0, [ "wget" ], [ "", [],
				[ "e_id", "formel_type", "formel_name" ], {
					e_type : "formel"
				} ])[0];
		if (!res[0]) {
			return
		}
		for ( var elems = res[1], i = elems.length - 1, elem; i >= 0; i--) {
			elem = Ext.getCmp(elems[i].formel_type.concat("_", elems[i].e_id));
			if (elem) {
				elem.wsel.unload()
			}
		}
	},
	refreshAll : function(data) {
		for ( var elems = data, i = elems.length - 1, elem; i >= 0; i--) {
			elem = Ext.getCmp(Jedox.wss.app[this.contIdSrc]._domId.concat("_",
					elems[i].type, "_", elems[i].id));
			if (elem) {
				try {
					elem.wsel.refresh(elems[i].val)
				} catch (e) {
				}
			}
		}
	},
	updateTarget : function(data) {
		for ( var elems = data, i = elems.length - 1, elem; i >= 0; i--) {
			elem = Ext.getCmp(Jedox.wss.app[this.contIdSrc]._domId.concat("_",
					elems[i].formel_type, "_", elems[i].e_id));
			if (elem) {
				try {
					elem.wsel.updateTarget(elems[i])
				} catch (e) {
				}
			}
		}
	},
	formelMoveTo : function(wsel) {
		var elem = Ext.getCmp(Jedox.wss.app[this.contIdSrc]._domId.concat("_",
				wsel.formel_type, "_", wsel.id));
		if (elem) {
			try {
				elem.wsel.move(wsel)
			} catch (e) {
			}
		}
	},
	countEl : function(elType) {
		return Jedox.wss.backend.conn.cmd(0, [ "wget" ], [ "", [], [ "e_id" ],
				{
					e_type : "formel",
					formel_type : elType
				} ])[0][1].length
	},
	getNLoc : function(x, y, w, h) {
		var n2l = Jedox.wss.app.numberToLetter, activeBook = Jedox.wss.app.activeBook, tlCellCords = activeBook
				.getNeighByOffset(1, 1, x, y), tlOffsetXY = activeBook
				.getPixelsByCoords(tlCellCords[0], tlCellCords[1]), lrCellCoords = activeBook
				.getNeighByOffset(1, 1, x + w, y + h), lrOffsetXY = activeBook
				.getPixelsByCoords(lrCellCoords[0], lrCellCoords[1]);
		return {
			pos_offsets : [ x - tlOffsetXY[0], y - tlOffsetXY[1],
					x + w - lrOffsetXY[0], y + h - lrOffsetXY[1] ],
			n_location : "=$".concat(n2l[tlCellCords[0]], "$", tlCellCords[1],
					":$", n2l[lrCellCoords[0]], "$", lrCellCoords[1])
		}
	},
	getTLSize : function(pos, offsets) {
		var activeBook = Jedox.wss.app.activeBook, offsetsAdjusted = false;
		var tlXY = activeBook.getPixelsByCoords(pos[0], pos[1]), brXY = activeBook
				.getPixelsByCoords(pos[2], pos[3]);
		var cellDims = activeBook.getCellDims(pos[0], pos[1]);
		if (offsets[0] < 0 || offsets[0] > cellDims[0]) {
			offsetsAdjusted = true;
			offsets[0] = 0
		}
		if (offsets[1] < 0 || offsets[1] > cellDims[1]) {
			offsetsAdjusted = true;
			offsets[1] = 0
		}
		cellDims = activeBook.getCellDims(pos[2], pos[3]);
		if (offsets[2] < 0 || offsets[2] > cellDims[0]) {
			offsetsAdjusted = true;
			offsets[0] = cellDims[0]
		}
		if (offsets[3] < 0 || offsets[3] > cellDims[1]) {
			offsetsAdjusted = true;
			offsets[1] = cellDims[1]
		}
		var w = brXY[0] + offsets[2] - (tlXY[0] + offsets[0]), h = brXY[1]
				+ offsets[3] - (tlXY[1] + offsets[1]);
		return {
			tl : [ tlXY[0] + offsets[0], tlXY[1] + offsets[1] ],
			size : [ w, h ],
			_error : offsetsAdjusted
		}
	},
	getRngFromNLoc : function(n_loc) {
		var refs = Jedox.wss.formula.parse(n_loc);
		if (!refs.length) {
			return undefined
		}
		var rng = refs[0].rng;
		if (refs.length > 1 && n_loc.indexOf(":") == -1) {
			rng[2] = refs[1].rng[2];
			rng[3] = refs[1].rng[3]
		}
		return rng
	},
	moveTo : function(wrapId, pos, offsets) {
		var tlOffsetXY = Jedox.wss.app.activeBook.getPixelsByCoords(pos[0],
				pos[1]), newX = tlOffsetXY[0] + offsets[0], newY = tlOffsetXY[1]
				+ offsets[1], wrapElem = Ext.get(wrapId);
		wrapElem.dom.style.left = "".concat(newX, "px");
		wrapElem.dom.style.top = "".concat(newY, "px")
	},
	moveSave : function() {
		for ( var moveReg = this.moveRegistry, i = moveReg.length - 1; i >= 0; i--) {
			moveReg[i][1].call(moveReg[i][0], moveReg[i][2])
		}
		this.moveRegistry = []
	},
	assignMacro : function(data) {
		var elem = Ext.getCmp(Jedox.wss.app[this.contIdSrc]._domId.concat("_",
				data.type, "_", data.id));
		if (elem) {
			try {
				elem.wsel.assignMacro(data.macros)
			} catch (e) {
				return [ false ].concat(e)
			}
		} else {
			return [ false, "formel_no_el", {} ]
		}
		return [ true ]
	}
};