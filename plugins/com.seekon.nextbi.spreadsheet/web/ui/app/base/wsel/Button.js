Jedox.wss.wsel.Button = (function() {
	var type = "Button", defDims = {
		w : 70,
		h : 35
	};
	return function(conf) {
		Jedox.wss.wsel.Button.parent.constructor.call(this, conf);
		var activeBook = Jedox.wss.app.activeBook, conn = Jedox.wss.backend.conn, env = Jedox.wss.app.environment;
		if (!this.conf.id) {
			var isNew = true;
			if (this.conf == undefined) {
				this.conf = {
					type : type,
					name : "ButtonN",
					label : "ButtonN"
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
			if (this.conf.width == undefined) {
				this.conf.width = defDims.w
			}
			if (this.conf.height == undefined) {
				this.conf.height = defDims.h
			}
			if (!this.conf.macros) {
				this.conf.macros = {}
			}
			this.conf.winId = activeBook.getWinId();
			if (conn.cmd(0, [ "wget" ], [ "", [], [ "e_id" ], {
				e_type : "formel",
				formel_name : this.conf.name
			} ])[0][1].length > 0) {
				throw [ "formel_exists", {
					name : conf.name
				} ]
			}
			var res = conn.cmd(0, [ "wadd" ], [ "", {
				e_type : "formel",
				n_refers_to : "",
				formel_name : this.conf.name,
				formel_type : this.conf.type,
				macros : this.conf.macros,
				data : {
					label : this.conf.label
				}
			} ])[0];
			if (res[0] && res[1].length > 0) {
				this.conf.id = res[1][0]
			} else {
				throw [ "formel_add_wsel_err", {} ]
			}
		} else {
			var isNew = false
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
		this.elem = new Ext.Button( {
			id : this.gContId.concat("_Button_", this.conf.id),
			renderTo : formelContId,
			text : this.conf.label,
			listeners : {
				click : {
					fn : this.onClick,
					scope : this
				}
			}
		});
		if (this.conf.width != defDims.w) {
			this.setCmpSize(this.conf.width)
		}
		this.conf.width = this.elem.getEl().dom.offsetWidth;
		this.conf.height = this.elem.getEl().dom.offsetHeight;
		if (isNew) {
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
			this.formelWrapper = new Ext.ResizableConstrained(formelContId, {
				wrap : !this.isUserMode,
				dynamic : !this.isUserMode,
				pinned : false,
				width : this.conf.width,
				height : this.conf.height,
				minWidth : this.conf.width,
				maxWidth : 700,
				minHeight : this.conf.height,
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
Jedox.util.extend(Jedox.wss.wsel.Button, Jedox.wss.wsel.FormBase);
Jedox.wss.wsel.Button.events = [ {
	name : "click",
	funcname : "Click"
} ];
Jedox.wss.wsel.Button.minDims = {
	w : 20,
	h : 35
};
clsRef = Jedox.wss.wsel.Button;
clsRef.prototype.onClick = function(comp, e) {
	if (this.isCmpMoved(comp) || !this.conf.macros.click) {
		return
	}
	var jwmacro = Jedox.wss.macro, conn = Jedox.wss.backend.conn, activeBook = Jedox.wss.app.activeBook;
	conn.createBatch();
	conn.cmd( [ jwmacro, jwmacro.ui_exec ], [ "em" ], [
			"php",
			"em.php",
			"em",
			[ this.conf.macros.click ],
			activeBook._gmode_edit ? activeBook._aPane._defaultSelection
					.getActiveRange().getValue() : "A1" ]);
	conn.sendBatch( [ activeBook, activeBook.exec ], activeBook
			.getRealGridRange())
};
clsRef.prototype.remove = function() {
	this.elem.destroy();
	this.formelWrapperEl.remove();
	var conn = Jedox.wss.backend.conn;
	conn.cmd(conn.dummy_cb, [ "wdel" ], [ "", [ this.conf.id ] ])
};
clsRef.prototype.update = function(editConf) {
	var conn = Jedox.wss.backend.conn, wsel = {}, updObj = {}, batchStarted = updWsel = updData = false;
	function startBatch() {
		if (!batchStarted) {
			conn.createBatch();
			batchStarted = true
		}
	}
	if (editConf.name != this.conf.name) {
		if (conn.cmd(0, [ "wget" ], [ "", [], [ "e_id" ], {
			e_type : "formel",
			formel_name : editConf.name
		} ])[0][1].length) {
			throw [ "formel_exists", {
				name : editConf.name
			} ]
		} else {
			this.conf.name = editConf.name;
			wsel.formel_name = this.conf.name
		}
	}
	if (editConf.label != this.conf.label) {
		startBatch();
		this.elem.setText(editConf.label);
		var elDom = this.elem.getEl().dom;
		this.formelWrapper.resizeTo(elDom.offsetWidth, elDom.offsetHeight);
		this.conf.width = elDom.offsetWidth;
		this.conf.height = elDom.offsetHeight;
		this.formelWrapper.minWidth = this.conf.width;
		this.formelWrapper.minHeight = this.conf.height;
		this.conf.label = editConf.label;
		updData = true
	}
	for ( var fld in wsel) {
		updWsel = true;
		break
	}
	if (updWsel || updData) {
		if (updData) {
			wsel.data = {
				label : this.conf.label
			}
		}
		updObj[this.conf.id] = wsel;
		startBatch();
		conn.cmd(0, [ "wupd" ], [ "", updObj ])
	}
	if (batchStarted) {
		conn.sendBatch()
	}
};
clsRef.prototype.setCmpSize = function(w, h) {
	this.elem.getEl().dom.style.width = "".concat(w, "px")
};
clsRef = null;