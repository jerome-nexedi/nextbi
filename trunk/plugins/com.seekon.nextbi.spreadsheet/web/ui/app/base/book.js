Jedox.wss.book = new function() {
	var that = this;
	this.spawn = function(cb, uid, name, ghn) {
		var app = Jedox.wss.app, win = Jedox.wss.workspace.createWindow(name), winId = win.initialConfig._winId;
		app.books[winId] = app.activeBook = new Jedox.wss.grid.Book(cb, win,
				uid, ghn);
		Jedox.wss.action.adjustToACL()
	};
	this.create = function(cb) {
		var res = Jedox.wss.backend.ha.loadWorkbook(null);
		if (!res[0]) {
			Jedox.wss.general
					.showMsg("Application Error".localize(), "errLoadWB_intro"
							.localize().concat(" ", res[1].localize()),
							Ext.MessageBox.ERROR);
			return false
		}
		that.spawn(cb, res[1].wbid, res[1].name, null);
		return true
	};
	this.load = function(cb, wb, group, hierarchy, ext, pc) {
		Jedox.wss.general.chkState();
		if (!ext) {
			ext = {
				appmode : Jedox.wss.app.appMode
			}
		} else {
			ext.appmode = Jedox.wss.app.appMode
		}
		var workspace = Jedox.wss.workspace, res = Jedox.wss.backend.ha
				.loadWorkbook(wb, group, hierarchy, ext);
		if (!res[0]) {
			if (!res[1].search(/^follHL*/i)) {
				throw res[1]
			}
			if (!res[1].search(/^recovery*/i)) {
				Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.recover, [
						res[2], arguments ])
			} else {
				Jedox.wss.general.showMsg("Application Error".localize(),
						"errLoadWB_intro".localize().concat(" ",
								res[1].localize()), Ext.MessageBox.ERROR, [
								this, Jedox.wss.workspace.notifyCloseByMeta, {
									ghn : {
										g : group,
										h : hierarchy,
										n : wb
									},
									name : null
								} ])
			}
			return false
		}
		if (pc) {
			pc.params.unshift(res[1][pc.tag]);
			pc.func.apply(pc.scope, pc.params)
		}
		if (res[1].imp && workspace.chkWinByWbId(res[1].wbid)) {
			if (res[1].wsid) {
				Jedox.wss.sheet.select(cb, res[1].wsid, res[1].wbid)
			} else {
				if (cb instanceof Array && cb.length > 1) {
					cb[1].call(cb[0])
				}
			}
			workspace.showWinByWbId(res[1].wbid)
		} else {
			that.spawn(cb, res[1].wbid, res[1].name,
					(group == undefined || hierarchy == undefined) ? null : {
						g : group,
						h : hierarchy,
						n : wb,
						p : res[1].perm
					})
		}
		return true
	};
	this.loadRecent = function(item) {
		var ghn = item.initialConfig.ghn;
		try {
			for ( var triggers = Jedox.wss.events.triggers.openWorkbook_before, i = triggers.length - 1; i >= 0; i--) {
				triggers[i][0]["openWorkbook_before"].call(parent,
						triggers[i][1], {
							g : ghn.g,
							h : ghn.h,
							n : ghn.n
						}, ghn.nm, false)
			}
			Jedox.wss.book.load(null, ghn.n, ghn.g, ghn.h, {
				vars : true
			});
			for ( var triggers = Jedox.wss.events.triggers.openWorkbook_after, i = triggers.length - 1; i >= 0; i--) {
				triggers[i][0]["openWorkbook_after"].call(parent,
						triggers[i][1], {
							g : ghn.g,
							h : ghn.h,
							n : ghn.n
						}, ghn.nm)
			}
		} catch (e) {
			Jedox.wss.general.showMsg("Application Error".localize(), e.message
					.localize(), Ext.MessageBox.ERROR)
		}
	};
	this.save = function(cb) {
		var ghn = Jedox.wss.workspace.getMetaByWinId(Jedox.wss.app.activeBook
				.getWinId())["ghn"];
		if (ghn == null) {
			Jedox.wss.app
					.load(Jedox.wss.app.dynJSRegistry.open, [ "save", cb ]);
			return
		}
		var res = Jedox.wss.backend.ha.saveWorkbook(ghn.g, ghn.h, ghn.n);
		if (!res[0]) {
			Ext.MessageBox.show( {
				title : "Save".localize(),
				msg : res[1].localize(res[2]),
				modal : true,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.WARNING,
				fn : Ext.emptyFn,
				minWidth : 300
			})
		}
		if (cb instanceof Array && cb.length > 1) {
			cb[1].call(cb[0])
		}
	};
	this.destroy = function(id) {
		var app = Jedox.wss.app;
		if (app.activeBook == app.books[id]) {
			app.activeBook = undefined
		}
		app.books[id] = undefined;
		Jedox.wss.workspace.cleanupWinClose(id)
	};
	this.getBookByUid = function(uid) {
		for ( var books = Jedox.wss.app.books, i = books.length - 1; i >= 0; --i) {
			if (!books[i]) {
				continue
			}
			if (books[i]._wbId == uid) {
				return books[i]
			}
		}
		return undefined
	};
	this.autoRefresh = function(secs) {
		var activeBook = Jedox.wss.app.activeBook;
		if (activeBook._autoRefresh) {
			clearTimeout(activeBook._tid_autoRefresh)
		}
		activeBook._autoRefresh = secs |= 0;
		if (secs) {
			that.startRefresh(activeBook)
		}
		if (activeBook._conf == null) {
			activeBook._conf = {
				e_type : "conf",
				autoRefresh : secs
			};
			var ccmd = '[["badd","",{"e_type":"conf","autoRefresh":'.concat(
					secs, "}]]"), post = [ that, function(res) {
				if (res[0][0]) {
					activeBook._conf.e_id = res[0][1][0]
				}
			} ]
		} else {
			activeBook._conf.autoRefresh = secs;
			var e_id = activeBook._conf.e_id;
			delete activeBook._conf.e_id;
			var ccmd = '[["bupd","",{"'.concat(e_id, '":', Ext.util.JSON
					.encode(activeBook._conf), "}]]"), post = null;
			activeBook._conf.e_id = e_id
		}
		var conn = Jedox.wss.backend.conn;
		conn.setCcmd(ccmd);
		conn.sendBatch(post)
	};
	this.startRefresh = function(book, ms) {
		if (!book._autoRefresh) {
			return
		}
		if (typeof ms != "number") {
			ms = book._autoRefresh * 1000
		}
		book._tid_autoRefresh = setTimeout(function() {
			book._aSheet.recalc( [ that, that.startRefresh, book, ms ])
		}, ms)
	};
	this.recover = function(win, args) {
		win._execClose = false;
		win.close();
		this.load.apply(this, args)
	};
	this.goTo = function(ref) {
		ref = Jedox.wss.formula.parse(ref);
		if (ref.length != 1) {
			return
		}
		ref = ref[0];
		var activeBook = Jedox.wss.app.activeBook, defSel = Jedox.wss.app.environment.defaultSelection;
		if (ref.sheet.length
				&& ref.sheet != activeBook._sheetUids[activeBook._aSheetUid]) {
			return
		}
		activeBook.scrollTo(defSel ? [ defSel, defSel.jumpTo, ref.rng ] : null,
				ref.rng[0], ref.rng[1], true, false)
	}
};