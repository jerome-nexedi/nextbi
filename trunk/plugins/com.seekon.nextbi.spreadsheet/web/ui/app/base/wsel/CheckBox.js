Jedox.wss.wsel.CheckBox = (function() {
	var type = "CheckBox", defDims = {
		w : 150,
		h : 22
	};
	return function(conf) {
		Jedox.wss.wsel.CheckBox.parent.constructor.call(this, conf);
		this.states = {
			unchecked : {
				g : false,
				c : false
			},
			checked : {
				g : true,
				c : true
			},
			mixed : {
				g : "#N/A",
				c : null
			}
		};
		var activeBook = Jedox.wss.app.activeBook, conn = Jedox.wss.backend.conn, env = Jedox.wss.app.environment, actCellCoords = env.selectedCellCoords, formulaParser = Jedox.wss.formula, trgTypes = Jedox.wss.wsel.FormBase.trgTypes, trgType = this
				.getTargetType(), that = this;
		if (this.states[this.conf.state]) {
			this.conf.selval = this.states[this.conf.state].g
		} else {
			throw [ "checkbox_inv_state", {} ]
		}
		if (!this.conf.id) {
			if (this.conf == undefined) {
				this.conf = {
					type : type,
					label : "CheckBoxN",
					trange : "",
					tnamedrange : "",
					tvar : "ComboBox",
					state : "unchecked"
				}
			}
			if (this.conf.name == undefined) {
				this.conf.name = this.conf.type.concat(Jedox.wss.wsel
						.countEl(this.conf.type) + 1)
			}
			if (this.conf.top == undefined) {
				this.conf.top = env.selectedCell.parentNode.offsetTop
			}
			if (this.conf.left == undefined) {
				this.conf.left = env.selectedCell.offsetLeft
			}
			if (this.conf.height == undefined) {
				this.conf.height = defDims.h
			}
			if (this.conf.tvar == undefined) {
				this.conf.tvar = ""
			}
			if (!this.conf.macros) {
				this.conf.macros = {}
			}
			this.conf.winId = activeBook.getWinId();
			if (this.conf.trange.length > 0) {
				if (!this.conf.trange.search(/^=/)) {
					this.conf.trange = this.conf.trange.substr(1)
				}
				var destRng = formulaParser.parse(this.conf.trange);
				if (!destRng.length) {
					throw [ "formel_inv_target", {} ]
				}
				destRng = destRng[0];
				if (destRng.sheet.length
						&& !activeBook._sheetSelector
								.getIdByName(destRng.sheet)) {
					throw [ "formel_inv_target_sheet", {
						name : destRng.sheet
					} ]
				}
			}
			conn.createBatch();
			conn.cmd(0, [ "wget" ], [ "", [], [ "e_id" ], {
				e_type : "formel",
				formel_name : this.conf.name
			} ]);
			if (this.conf.tnamedrange.length > 0) {
				conn.cmd(0, [ "nexs" ], [ [ this.conf.tnamedrange,
						Jedox.wss.app.activeSheet.getUid() ] ])
			}
			var chkRes = conn.sendBatch();
			if (chkRes[0][1].length > 0) {
				throw [ "formel_exists", {
					name : conf.name
				} ]
			}
			if (this.conf.tnamedrange.length > 0 && chkRes[1][1][0]) {
				throw [ "formel_nrange_exists", {
					name : conf.tnamedrange
				} ]
			}
			var res = conn.cmd(0, [ "wadd" ], [
					"",
					{
						e_type : "formel",
						n_target_ref : "=".concat(trgType == trgTypes.VAR ? "@"
								: "", this.conf[trgType]),
						formel_name : this.conf.name,
						formel_type : this.conf.type,
						macros : this.conf.macros,
						data : {
							trange : this.conf.trange,
							tnamedrange : this.conf.tnamedrange,
							tvar : this.conf.tvar,
							selval : this.conf.state,
							label : this.conf.label
						}
					} ])[0];
			if (res[0] && res[1].length > 0) {
				this.conf.id = res[1][0]
			} else {
				throw [ "formel_add_wsel_err", {} ]
			}
			if (this.conf.trange.length > 0) {
				conn.createBatch();
				if (destRng.sheet.length
						&& destRng.sheet != activeBook._sheetSelector
								.getActiveSheetName()) {
					var actSheetId = Jedox.wss.app.activeSheet._uid, tSheetId = activeBook._sheetSelector
							.getIdByName(destRng.sheet);
					conn.cmd(null, [ "osel" ], [ 2, tSheetId ])
				}
				conn.cmd(null, [ "cdrn" ], [ {
					cm : true
				}, destRng.rng.concat( {
					v : Ext.decode(this.conf.selval)
				}) ]);
				if (tSheetId) {
					conn.cmd(null, [ "osel" ], [ 2, actSheetId ])
				}
				conn.sendBatch( [ activeBook, activeBook.exec ], activeBook
						.getRealGridRange())
			}
			if (this.conf.tnamedrange.length > 0) {
				conn
						.cmd(
								[ activeBook, activeBook.exec ],
								[ "nadd" ],
								[ [
										actCellCoords[0],
										actCellCoords[1],
										{
											name : this.conf.tnamedrange,
											refers_to : "="
													.concat(typeof this.conf.selval == "string" ? Ext.util.JSON
															.encode(this.conf.selval)
															: this.conf.selval),
											scope : activeBook
													.getSheetSelector()
													.getActiveSheetName(),
											comment : "Managed by form element "
													.concat(this.conf.name)
										} ] ], activeBook.getRealGridRange(),
								false, false)
			}
			if (this.conf.tvar.length > 0) {
				conn
						.cmd(null, [ "svar" ], [ this.conf.tvar,
								this.conf.selval ])
			}
		} else {
			if (this.conf.targetVal != null) {
				this.preselect(this.conf.targetVal, false)
			}
		}
		var currDate = new Date(), gridDivId = activeBook.getDomId() + "_IC", formelContId = this.gContId
				.concat("_formel_cont_", this.conf.id);
		this.calcConstraints();
		Ext.DomHelper.append(Ext.DomQuery.selectNode("div[id=" + gridDivId
				+ "]"), {
			tag : "div",
			id : formelContId,
			width : this.conf.width,
			height : this.conf.height,
			cls : "ws_element",
			style : "z-index: 50; position: absolute; left:".concat(
					this.conf.left, "px; top:", this.conf.top, "px;")
		}, false);
		this.elem = new Ext.form.Checkbox( {
			id : this.gContId.concat("_CheckBox_", this.conf.id),
			renderTo : formelContId,
			fieldLabel : "",
			labelSeparator : "",
			boxLabel : this.conf.label,
			checked : this.states[this.conf.state].c,
			width : this.conf.width ? this.conf.width : "auto",
			listeners : {
				check : {
					fn : this.onCheck,
					scope : this
				}
			}
		});
		if (this.conf.width == undefined) {
			this.conf.width = this.elem.wrap.dom.offsetWidth;
			var posConf = Jedox.wss.wsel.getNLoc(this.conf.left, this.conf.top,
					this.conf.width, this.conf.height);
			var updObj = {};
			updObj[this.conf.id] = {
				n_location : posConf.n_location,
				pos_offsets : posConf.pos_offsets
			};
			conn.cmd(conn.dummy_cb, [ "wupd" ], [ "", updObj ])
		}
		if (!this.isUserMode) {
			Ext.ResizableConstrained = function(el, config) {
				Ext.ResizableConstrained.superclass.constructor.call(this, el,
						config)
			};
			Ext.extend(Ext.ResizableConstrained, Ext.Resizable, {
				setXConstraint : function(left, right) {
					this.dd.setXConstraint(left, right)
				},
				setYConstraint : function(up, down) {
					this.dd.setYConstraint(up, down)
				}
			});
			var minDims = Jedox.wss.wsel.ComboBox.minDims;
			this.formelWrapper = new Ext.ResizableConstrained(formelContId, {
				wrap : !this.isUserMode,
				dynamic : !this.isUserMode,
				pinned : false,
				width : this.conf.width,
				height : this.conf.height,
				minWidth : minDims.w,
				maxWidth : 700,
				minHeight : minDims.h,
				maxHeight : 700,
				preserveRatio : false,
				transparent : true,
				handles : !this.isUserMode ? "e w" : "nw",
				draggable : !this.isUserMode,
				enabled : !this.isUserMode,
				style : "background-color: white;",
				resizeRegion : Ext.get(gridDivId).getRegion()
			});
			this.formelWrapper.on( {
				resize : {
					fn : this.afterFormelResize,
					scope : this
				},
				beforeresize : {
					fn : this.beforeFormelResize,
					scope : this
				}
			});
			this.formelWrapper.dd.maintainOffset = true;
			this.formelWrapper.setXConstraint(this.elConstr.left,
					this.elConstr.right);
			this.formelWrapper.setYConstraint(this.elConstr.up,
					this.elConstr.down);
			this.formelWrapperEl = this.formelWrapper.getEl();
			this.formelWrapperEl.dom.style.backgroundColor = "#FFFFFF";
			this.formelWrapperEl.on( {
				mousedown : {
					fn : this.onFormelMouseDown,
					scope : this
				}
			})
		}
		this.elem.wsel = this
	}
})();
Jedox.util.extend(Jedox.wss.wsel.CheckBox, Jedox.wss.wsel.FormBase);
Jedox.wss.wsel.CheckBox.events = [ {
	name : "check",
	funcname : "Check"
} ];
Jedox.wss.wsel.CheckBox.minDims = {
	w : 40,
	h : 22
};
clsRef = Jedox.wss.wsel.CheckBox;
clsRef.prototype.updateWSElData = function() {
	var conn = Jedox.wss.backend.conn, updObj = {};
	updObj[this.conf.id] = {
		data : {
			trange : this.conf.trange,
			tnamedrange : this.conf.tnamedrange,
			tvar : this.conf.tvar,
			selval : this.conf.state,
			label : this.conf.label
		}
	};
	conn.cmd(conn.dummy_cb, [ "wupd" ], [ "", updObj ])
};
clsRef.prototype._resState = function(val) {
	for ( var state in this.states) {
		if (this.states[state].c == val) {
			return state
		}
	}
	return false
};
clsRef.prototype.onCheck = function(comp, checked) {
	if (this.isCmpMoved(comp)) {
		this.elem.suspendEvents();
		this.elem.setValue(!checked);
		this.elem.resumeEvents();
		return
	}
	var selState = this._resState(checked), conn = Jedox.wss.backend.conn, activeBook = Jedox.wss.app.activeBook;
	if (!selState || selState == this.conf.state) {
		return
	}
	this.conf.state = selState;
	this.conf.selval = this.states[selState].g;
	conn.createBatch();
	this.updateDependencies();
	this.updateWSElData();
	if (this.conf.macros.check) {
		var jwmacro = Jedox.wss.macro;
		conn.cmd( [ jwmacro, jwmacro.ui_exec ], [ "em" ], [
				"php",
				"em.php",
				"em",
				[ this.conf.macros.check, this.states[selState].c ],
				activeBook._gmode_edit ? activeBook._aPane._defaultSelection
						.getActiveRange().getValue() : "A1" ])
	}
	conn.sendBatch( [ activeBook, activeBook.exec ], activeBook
			.getRealGridRange())
};
clsRef.prototype.update = function(editConf) {
	var conn = Jedox.wss.backend.conn, activeBook = Jedox.wss.app.activeBook, actCellCoords = Jedox.wss.app.environment.selectedCellCoords, wsel = {}, updObj = {}, chkNameAndRng = chkNRng = false, batchStarted = updWsel = updData = updDep = false;
	if (editConf.tvar == undefined) {
		editConf.tvar = ""
	}
	function startBatch() {
		if (!batchStarted) {
			conn.createBatch();
			batchStarted = true
		}
	}
	if (editConf.trange.length && editConf.trange != this.conf.trange) {
		if (!editConf.trange.search(/^=/)) {
			editConf.trange = editConf.trange.substr(1)
		}
		var destRng = Jedox.wss.formula.parse(editConf.trange);
		if (!destRng.length) {
			throw [ "formel_inv_target", {} ]
		}
		destRng = destRng[0];
		if (destRng.sheet.length
				&& !activeBook._sheetSelector.getIdByName(destRng.sheet)) {
			throw [ "formel_inv_target_sheet", {
				name : destRng.sheet
			} ]
		}
	}
	if (editConf.name != this.conf.name || editConf.trange != this.conf.trange) {
		startBatch();
		conn.cmd(0, [ "wget" ], [ "", [], [ "e_id" ], {
			e_type : "formel",
			formel_name : editConf.name
		} ]);
		chkNameAndRng = true
	}
	if (editConf.tnamedrange.length
			&& editConf.tnamedrange != this.conf.tnamedrange) {
		startBatch();
		conn.cmd(0, [ "nexs" ], [ [ editConf.tnamedrange,
				Jedox.wss.app.activeSheet.getUid() ] ]);
		chkNRng = true
	}
	if (chkNameAndRng || chkNRng) {
		var chkRes = conn.sendBatch();
		if (chkNameAndRng && chkRes[0][1].length
				&& chkRes[0][1][0]["e_id"] != this.conf.id) {
			throw [ "formel_exists", {
				name : editConf.name
			} ]
		}
		batchStarted = false
	}
	if (editConf.name != this.conf.name) {
		this.conf.name = editConf.name;
		wsel.formel_name = this.conf.name
	}
	if (editConf.label != this.conf.label) {
		startBatch();
		this.elem.wrap.child(".x-form-cb-label").update(editConf.label);
		this.formelWrapper.resizeTo(editConf.label.length * 20 + 50,
				this.conf.height);
		this.conf.width = this.elem.wrap.dom.offsetWidth;
		this.formelWrapper.resizeTo(this.conf.width, this.conf.height);
		this.conf.label = editConf.label;
		updData = true
	}
	if (editConf.state != this.conf.state) {
		this.conf.state = editConf.state;
		this.conf.selval = this.states[this.conf.state].g;
		this.elem.suspendEvents();
		this.elem.setValue(this.states[this.conf.state].c);
		this.elem.resumeEvents();
		updData = updDep = true;
		if (this.conf.macros.check) {
			startBatch();
			var jwmacro = Jedox.wss.macro;
			conn
					.cmd(
							[ jwmacro, jwmacro.ui_exec ],
							[ "em" ],
							[
									"php",
									"em.php",
									"em",
									[ this.conf.macros.check,
											this.states[this.conf.state].c ],
									activeBook._gmode_edit ? activeBook._aPane._defaultSelection
											.getActiveRange().getValue()
											: "A1" ])
		}
	}
	if (editConf.trange != this.conf.trange) {
		if (editConf.trange.length) {
			wsel.n_target_ref = "=".concat(editConf.trange)
		}
		this.conf.trange = editConf.trange;
		updData = updDep = true
	}
	if (editConf.tnamedrange != this.conf.tnamedrange) {
		this.conf.tnamedrange = editConf.tnamedrange;
		if (this.conf.tnamedrange.length) {
			if (!chkRes[chkNameAndRng ? 1 : 0][1][0]) {
				startBatch();
				conn.cmd(null, [ "nadd" ], [ [
						actCellCoords[0],
						actCellCoords[1],
						{
							name : this.conf.tnamedrange,
							refers_to : "=TRUE",
							scope : activeBook.getSheetSelector()
									.getActiveSheetName(),
							comment : "Managed by form element "
									.concat(this.conf.name)
						} ] ])
			}
			wsel.n_target_ref = "=".concat(this.conf.tnamedrange)
		}
		updData = updDep = true
	}
	if (editConf.tvar != this.conf.tvar) {
		if (editConf.tvar.length) {
			wsel.n_target_ref = "=@".concat(editConf.tvar)
		}
		this.conf.tvar = editConf.tvar;
		updData = updDep = true
	}
	for ( var fld in wsel) {
		updWsel = true;
		break
	}
	if (updWsel || updData) {
		if (updData) {
			wsel.data = {
				trange : this.conf.trange,
				tnamedrange : this.conf.tnamedrange,
				tvar : this.conf.tvar,
				selval : this.conf.state,
				label : this.conf.label
			}
		}
		updObj[this.conf.id] = wsel;
		startBatch();
		conn.cmd(null, [ "wupd" ], [ "", updObj ])
	}
	if (updDep) {
		startBatch();
		this.updateDependencies()
	}
	if (batchStarted) {
		conn.sendBatch( [ activeBook, activeBook.exec ], activeBook
				.getRealGridRange())
	}
};
clsRef.prototype.setCmpSize = function(w, h) {
	this.elem.setSize(w, h)
};
clsRef.prototype.preselect = function(tVal, updEl) {
	var selState = this._resState(tVal instanceof Array ? tVal[0] : tVal);
	if (!selState || selState == this.conf.state) {
		return false
	}
	this.conf.state = selState;
	this.conf.selval = this.states[selState].g;
	if (updEl) {
		this.elem.setValue(this.states[selState].c)
	}
	this.updateWSElData();
	return true
};
clsRef.prototype.updateTarget = function(data) {
	var trgType = this.getTargetType(), selState = this
			._resState(data.n_target_val instanceof Array ? data.n_target_val[0]
					: data.n_target_val), tRefIdx = data.n_target_ref
			.search(/^=/) + 1, updData = false;
	if (!data.n_target_ref.search(/^=@/)) {
		tRefIdx++
	}
	data.n_target_ref = data.n_target_ref.substr(tRefIdx);
	if (this.conf[trgType] != data.n_target_ref) {
		this.conf[trgType] = data.n_target_ref;
		updData = true
	}
	if (!this.preselect(data.n_target_val, true) && updData) {
		this.updateWSElData()
	}
};
clsRef = null;