Jedox.wss.hb = new function() {
	var that = this;
	this.HBMode = {
		DESIGNER : 0,
		QUICKVIEW : 1,
		USER : 2
	};
	this.addDynarange = function(item, ev) {
		function genHBName() {
			var env = Jedox.wss.app.environment, cnt = that.getHBCount() + 1;
			while (env.dynaranges["DynaRange".concat(cnt)] != undefined
					&& cnt < 1000) {
				cnt++
			}
			return "DynaRange".concat(cnt)
		}
		var dir = item.getId().indexOf("newHBVert") == 0 ? 0 : 1, hbdata = {
			id : genHBName(),
			dir : dir,
			drill : true,
			level : 2,
			border : "1px solid #000000",
			indent : dir == 0
		};
		Jedox.wss.palo.utils.openSubsetEditor( {
			mode : 1,
			dynarange : hbdata
		})
	};
	this.delDynarange = function(hbid) {
		var env = Jedox.wss.app.environment, hbs = env.dynaranges;
		hbs[hbid].remove(true);
		delete hbs[hbid];
		env.defaultSelection.show();
		if (!that.getHBCount()) {
			that.enaDisHBQuickView("disable")
		}
	};
	this.setAllNormal = function(exceptId, redraw) {
		var env = Jedox.wss.app.environment, activeBook = Jedox.wss.app.activeBook;
		if (env.HBMode != that.HBMode.DESIGNER || activeBook == undefined
				|| activeBook.isClone()) {
			return
		}
		var hbs = env.dynaranges;
		for ( var hbid in hbs) {
			if (hbid == exceptId) {
				continue
			}
			if (redraw) {
				hbs[hbid].redraw()
			} else {
				hbs[hbid].show()
			}
		}
	};
	this.hideAll = function() {
		var env = Jedox.wss.app.environment;
		if (env.HBMode != that.HBMode.QUICKVIEW) {
			return
		}
		var hbs = env.dynaranges;
		for ( var hbid in hbs) {
			hbs[hbid].hide()
		}
	};
	this.loadAll = function() {
		function _load(res) {
			if (!(res instanceof Array) || res[0] !== true) {
				return
			}
			res = res[1];
			var jwgrid = Jedox.wss.grid, activeBook = Jedox.wss.app.activeBook;
			for ( var elem, hbdata, i = res.length - 1; i >= 0; i--) {
				elem = res[i];
				hbdata = elem.hbdata;
				hbdata.wselid = elem.e_id;
				hbdata.id = elem.id;
				hbdata._gendata = elem._gendata;
				hbs[hbdata.id] = new jwgrid.DynarangeSelection(activeBook,
						hbdata);
				if (activeBook.isClone()) {
					hbs[hbdata.id].hide();
					that.syncCntrl(true);
					that.enaDisHBAdd("disable")
				} else {
					hbs[hbdata.id].show();
					that.syncCntrl(false);
					that.enaDisHBAdd("enable")
				}
			}
		}
		var env = Jedox.wss.app.environment, viewMode = Jedox.wss.grid.viewMode, hbs = (env.dynaranges = {});
		switch (env.viewMode) {
		case viewMode.DESIGNER:
			Jedox.wss.backend.conn.cmd( [ that, _load ], [ "wget" ], [ "", [],
					[ "e_id", "id", "hbdata", "_gendata" ], {
						e_type : "hb"
					} ]);
			break;
		case viewMode.USER:
			env.HBMode = that.HBMode.USER;
			break
		}
	};
	this.unloadAll = function() {
		var hbs = Jedox.wss.app.environment.dynaranges;
		for ( var hbid in hbs) {
			hbs[hbid].remove(false)
		}
		hbs = {}
	};
	this.move = function(id, pos) {
		if (Jedox.wss.app.environment.HBMode != that.HBMode.DESIGNER) {
			return
		}
		var hbs = Jedox.wss.app.environment.dynaranges;
		for ( var hbid in hbs) {
			if (hbs[hbid].getProps().wselid == id) {
				hbs[hbid].move(pos);
				hbs[hbid].redraw();
				break
			}
		}
	};
	this.subsetDlgCB = function(hbdata) {
		var env = Jedox.wss.app.environment;
		if (env.dynaranges[hbdata.id] != undefined) {
			env.dynaranges[hbdata.id].setProps(hbdata);
			return
		}
		env.defaultSelection.hide();
		var ulCoord = env.lastRangeStartCoord, lrCoord = env.lastRangeEndCoord;
		hbdata.src = [ ulCoord[0], ulCoord[1], lrCoord[0], lrCoord[1] ];
		env.dynaranges[hbdata.id] = new Jedox.wss.grid.DynarangeSelection(
				Jedox.wss.app.activeBook, hbdata);
		that.enaDisHBQuickView("enable")
	};
	this.propDlgCB = function(hbdata) {
		Jedox.wss.app.environment.dynaranges[hbdata.id].setProps(hbdata)
	};
	this.getHBCount = function() {
		var cnt = 0;
		for ( var fld in Jedox.wss.app.environment.dynaranges) {
			++cnt
		}
		return cnt
	};
	this.enaDisHBAdd = function(cmd) {
		if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.USER) {
			return
		}
		var menubar = Jedox.wss.app.menubar, toolbar = Jedox.wss.app.toolbar;
		if (menubar) {
			menubar.newHBVert[cmd]();
			menubar.newHBHoriz[cmd]();
			menubar.userModeView[cmd]()
		}
		toolbar.newHBVert[cmd]();
		toolbar.newHBHoriz[cmd]();
		toolbar.userModeView[cmd]();
		that.enaDisHBQuickView(that.getHBCount() ? "enable" : "disable")
	};
	this.enaDisHBQuickView = function(cmd) {
		var menubar = Jedox.wss.app.menubar;
		if (menubar) {
			menubar.hbQuickView[cmd]()
		}
		Jedox.wss.app.toolbar.hbQuickView[cmd]()
	};
	this.syncCntrl = function(press) {
		if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.USER) {
			return
		}
		Jedox.wss.app.performItemToggle = false;
		var menubar = Jedox.wss.app.menubar;
		if (menubar) {
			menubar.hbQuickView.setChecked(press, true)
		}
		Jedox.wss.app.toolbar.hbQuickView.toggle(press);
		Jedox.wss.app.performItemToggle = true
	};
	this.run = function(btn, state) {
		if (!Jedox.wss.app.performItemToggle || that.getHBCount() <= 0) {
			return
		}
		var env = Jedox.wss.app.environment, activeBook = Jedox.wss.app.activeBook, HBMode = env.HBMode;
		if (state) {
			try {
				env.HBMode = that.HBMode.QUICKVIEW;
				var hbs = env.dynaranges, hasHbs = false;
				for ( var hbid in hbs) {
					hasHbs = true;
					break
				}
				if (hasHbs) {
					var clnRes = Jedox.wss.backend.ha.addCloneWorksheet();
					if (clnRes !== false) {
						activeBook.setClone(clnRes)
					}
					that.hideAll();
					activeBook.cb("hb_run", [ HBMode == that.HBMode.USER ? null
							: 3 ],
							[ true, false, Jedox.wss.backend.conn.Q_ALL ])
				}
				that.syncCntrl(true);
				that.enaDisHBAdd("disable");
				Jedox.wss.app.statusBar.setWorkingMode("QuickView".localize())
			} catch (e) {
				env.HBMode = HBMode
			}
		} else {
			try {
				env.HBMode = that.HBMode.DESIGNER;
				var sheetSelector = activeBook.getSheetSelector();
				sheetSelector.action = sheetSelector.actionTypes.UNCLONED;
				activeBook._actOnSheetSel();
				that.setAllNormal();
				that.syncCntrl(false);
				that.enaDisHBAdd("enable");
				Jedox.wss.app.statusBar.setWorkingMode("Designer".localize())
			} catch (e) {
				env.HBMode = HBMode
			}
		}
	};
	this.regECHandlers = function() {
		Jedox.wss.grid.cbReg("hb_ec", [ that, that.expandCollapse ])
	};
	this.expandCollapse = function(eventData, hbId, idxPath) {
		var activeBook = Jedox.wss.app.activeBook;
		activeBook.cb("hb_ec", [ hbId, [ eventData.c, eventData.r ], idxPath ],
				[ true, false, Jedox.wss.backend.conn.Q_ALL ]);
		return false
	}
};