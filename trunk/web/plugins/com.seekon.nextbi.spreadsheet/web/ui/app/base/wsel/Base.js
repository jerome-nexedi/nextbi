Jedox.wss.wsel.Base = (function() {
	return function(conf) {
		var that = this;
		this.conf = conf;
		this.elConstr = {
			up : 0,
			down : 0,
			left : 0,
			right : 0,
			vportPos : [],
			lrCornerPx : []
		};
		this.handleElementMove = false;
		this.isUserMode = (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.USER);
		this.gContId = Jedox.wss.app.activeSheet._domId
	}
})();
Jedox.wss.wsel.Base.prototype = {
	calcConstraints : function() {
		var activeBook = Jedox.wss.app.activeBook, defMaxCoords = Jedox.wss.grid.defMaxCoords;
		this.elConstr.vportPos = activeBook.getViewportPos();
		if (this.conf.left < 0 || this.conf.top < 0) {
			this.elConstr.left = parseInt(this.elConstr.vportPos[0][0]
					+ (this.elConstr.vportPos[1][0]
							- this.elConstr.vportPos[0][0] - this.conf.width)
					/ 2);
			this.elConstr.up = parseInt(this.elConstr.vportPos[0][1]
					+ (this.elConstr.vportPos[1][1]
							- this.elConstr.vportPos[0][1] - this.conf.height)
					/ 2);
			this.conf.left = this.elConstr.left;
			this.conf.top = this.elConstr.up
		} else {
			this.elConstr.left = this.conf.left;
			this.elConstr.up = this.conf.top
		}
		this.elConstr.lrCornerPx = activeBook.getPixelsByCoords(
				defMaxCoords[0] + 1, defMaxCoords[1] + 1);
		this.elConstr.right = this.elConstr.lrCornerPx[0] - this.elConstr.left
				- this.conf.width;
		this.elConstr.down = this.elConstr.lrCornerPx[1] - this.elConstr.up
				- this.conf.height
	},
	beforeShowCtxMenu : function() {
	},
	onFormelMouseDown : function(e) {
		var activeBook = Jedox.wss.app.activeBook, defMaxCoords = Jedox.wss.grid.defMaxCoords, env = Jedox.wss.app.environment;
		if (activeBook != this.conf.winId) {
			Jedox.wss.workspace.showWin(this.conf.winId)
		}
		if (e.button == 2
				|| (Ext.isMac && e.button == 0 && Jedox.wss.app.ctrlKeyPressed)
				|| (Ext.isWebKit && e.button == 1)) {
			this.beforeShowCtxMenu();
			this.handleElementMove = false;
			this.showContextMenu(e);
			e.stopEvent()
		} else {
			Jedox.wss.app.mouseMovementObserver.subscribe(
					this.onFormelMouseMove, this);
			var bEv = e.browserEvent;
			var el = (document.all) ? bEv.srcElement : bEv.target;
			var newVportPos = activeBook.getViewportPos();
			var newLrCornerPx = activeBook.getPixelsByCoords(
					defMaxCoords[0] + 1, defMaxCoords[1] + 1);
			if (env.winStateMax) {
				var vportDiff = {
					h : newVportPos[0][0] - this.elConstr.vportPos[0][0],
					v : newVportPos[0][1] - this.elConstr.vportPos[0][1]
				};
				var lrCornerPxDiff = {
					h : newLrCornerPx[0] - this.elConstr.lrCornerPx[0],
					v : newLrCornerPx[1] - this.elConstr.lrCornerPx[1]
				};
				this.formelWrapper.setXConstraint(this.elConstr.left
						+ vportDiff.h, (this.elConstr.right - vportDiff.h)
						+ lrCornerPxDiff.h);
				this.formelWrapper.setYConstraint(this.elConstr.up
						+ vportDiff.v, (this.elConstr.down - vportDiff.v)
						+ lrCornerPxDiff.v)
			} else {
				var leftConstr = this.elConstr.left
						- ((bEv.clientX - (el.parentNode.offsetLeft
								+ ((document.all) ? bEv.offsetX : bEv.layerX) - newVportPos[0][0])) - env.gridScreenCoordsMax[0])
						+ newVportPos[0][0];
				this.formelWrapper.setXConstraint(leftConstr, newLrCornerPx[0]
						- leftConstr - this.conf.width);
				var topConstr = this.elConstr.up
						- ((bEv.clientY - (el.parentNode.offsetTop
								+ ((document.all) ? bEv.offsetY : bEv.layerY) - newVportPos[0][1])) - env.gridScreenCoordsMax[1])
						+ newVportPos[0][1];
				this.formelWrapper.setYConstraint(topConstr, newLrCornerPx[1]
						- topConstr - this.conf.height)
			}
			Jedox.wss.wsel.moveRegistry
					.push( [ this, this.onFormelMouseUp, el ]);
			this.handleElementMove = true
		}
	},
	onFormelMouseUp : function(el) {
		Jedox.wss.app.mouseMovementObserver.unsubscribe(this.onFormelMouseMove);
		if (!this.handleElementMove) {
			return
		}
		var targetId = this.gContId.concat("_formel_cont_", this.conf.id,
				"-rzwrap");
		while (el.id != targetId) {
			el = el.parentNode
		}
		var newLeft = el.offsetLeft, newTop = el.offsetTop, elWidth = el.offsetWidth, elHeight = el.offsetHeight, conf = this.conf, elem = this.elem;
		if (newLeft != this.conf.left || newTop != this.conf.top) {
			elem.disable();
			try {
				elem.enable();
				elem.collapse()
			} catch (e) {
			}
			conf.left = newLeft;
			conf.top = newTop;
			var nLoc = Jedox.wss.wsel.getNLoc(newLeft, newTop, elWidth,
					elHeight), formelData = {};
			if (conf.trange && conf.trange.length) {
				nLoc.n_location = nLoc.n_location.concat(",", conf.trange)
			}
			formelData[conf.id] = nLoc;
			var conn = Jedox.wss.backend.conn;
			conn.cmd(conn.dummy_cb, [ "wupd" ], [ "", formelData ])
		}
		this.handleElementMove = false
	},
	onFormelMouseMove : function() {
		try {
			this.elem.collapse()
		} catch (e) {
		}
		Jedox.wss.app.mouseMovementObserver.unsubscribe(this.onFormelMouseMove)
	},
	afterFormelResize : function(wrapper, newWidth, newHeight, e) {
		var wrapperEl = wrapper.getEl(), elLeft = wrapperEl.getLeft(true), elTop = wrapperEl
				.getTop(true);
		if (this.conf.width != newWidth) {
			this.elConstr.right += this.conf.width - newWidth
		}
		if (this.conf.height != newHeight) {
			this.elConstr.down += this.conf.height - newHeight
		}
		this.setCmpSize(newWidth, newHeight);
		wrapper.dynamic = !this.isUserMode;
		var formelData = {};
		formelData[this.conf.id] = Jedox.wss.wsel.getNLoc(elLeft, elTop,
				newWidth, newHeight);
		var conn = Jedox.wss.backend.conn;
		conn.cmd(conn.dummy_cb, [ "wupd" ], [ "", formelData ])
	},
	beforeFormelResize : function(wrapper, e) {
		wrapper.dynamic = false
	},
	refresh : function(data) {
		return
	}
};