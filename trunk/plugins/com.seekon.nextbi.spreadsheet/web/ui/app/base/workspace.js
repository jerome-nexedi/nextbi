Jedox.wss.workspace = new function() {
	var that = this, _workspace = document.getElementById("workspace"), _heightDecrement = document
			.getElementById("barsContainer").offsetHeight
			+ document.getElementById("statusBarContainer").offsetHeight, _panel = null, _windows = [], _openWinNum = 0, _minimizedWins = [];
	this.activeWin = null;
	this.windowsArrangement = "tiled";
	var GridWindow = Ext.extend(Ext.Window, {
		initComponent : function() {
			GridWindow.superclass.initComponent.call(this)
		},
		minimize : function() {
			if (!this._minimized) {
				this._isMinimizing = true;
				if (this.id == Jedox.wss.workspace.activeWin.id) {
					var arrWins = Jedox.wss.workspace.getVisibleWinsList();
					if (arrWins.length > 1) {
						Jedox.wss.workspace.activeWin = arrWins[1]
					}
				}
				this._restoreData = {
					w : ((this.maximized) ? this.restoreSize.width : this
							.getSize().width),
					x : ((this.maximized) ? this.restorePos[0] : this.x),
					y : ((this.maximized) ? this.restorePos[1] : this.y)
				};
				var titleSize = Ext.util.TextMetrics.measure(this.header.id,
						this.title);
				if (titleSize.width > 70) {
					var headerId = this.header.id;
					var _getNewName = function(inName, inWidth) {
						if (Ext.util.TextMetrics.measure(headerId, inName
								+ "...").width > inWidth) {
							return _getNewName(inName.substring(0,
									inName.length - 1), inWidth)
						} else {
							return inName + "..."
						}
					};
					this._restoreData.title = this.title;
					this.setTitle(_getNewName(this.title.substring(0,
							this.title.length - 1), 70))
				}
				this.restore();
				this._minimized = true;
				this.toBack();
				this.collapse();
				this.tools.restore.show();
				this.tools.maximize.show();
				this.tools.minimize.hide();
				this.setWidth(150);
				var _wsH = _panel.getInnerHeight();
				var i = 0;
				for (; i < _minimizedWins.length; i++) {
					if (!_minimizedWins[i]) {
						_minimizedWins[i] = true;
						this.setPosition(2 * (i + 1) + (150 * i), _wsH
								- this.header.getSize().height - 2);
						this._restoreData.position = i;
						break
					}
				}
				if (i == _minimizedWins.length) {
					_minimizedWins[i] = true;
					this.setPosition(2 * (i + 1) + (150 * i), _wsH
							- this.header.getSize().height - 2);
					this._restoreData.position = i
				}
				delete this._isMinimizing
			}
		},
		restore : function() {
			if (this._minimized) {
				this._isMinimizing = true;
				this._minimized = false;
				this.setWidth(this._restoreData.w);
				this.setPosition(this._restoreData.x, this._restoreData.y);
				if (this._restoreData.title) {
					this.setTitle(this._restoreData.title)
				}
				delete this._isMinimizing;
				this.expand();
				this.tools.minimize.show();
				this.tools.restore.hide();
				_minimizedWins[this._restoreData.position] = false;
				delete this._restoreData
			} else {
				GridWindow.superclass.restore.call(this)
			}
		},
		maximize : function() {
			GridWindow.superclass.maximize.call(this);
			if (this._minimized) {
				this._minimized = false;
				if (this.maximized) {
					this.restoreSize.width = this._restoreData.w;
					this.restorePos = [ this._restoreData.x,
							this._restoreData.y ];
					if (this._restoreData.title) {
						this.setTitle(this._restoreData.title)
					}
				}
				this.tools.minimize.show();
				_minimizedWins[this._restoreData.position] = false;
				delete this._restoreData
			}
		},
		show : function(animateTarget, callback, scope) {
			GridWindow.superclass.show.call(this, animateTarget, callback,
					scope);
			if (this._minimized) {
				this.collapse()
			}
		}
	});
	var _hasMinimizedWins = function() {
		for ( var i = 0; i < _minimizedWins.length; i++) {
			if (_minimizedWins[i]) {
				return true
			}
		}
		return false
	};
	var _getHeight = function() {
		return document.getElementById("barsContainer").offsetHeight
				+ document.getElementById("statusBarContainer").offsetHeight
	};
	this.resizeMaxWindows = function() {
		for ( var i = 0; i < _windows.length; i++) {
			if ((_windows[i]) && (_windows[i][1]) && (_windows[i][1].maximized)) {
				_windows[i][1]
						.setHeight((window.innerWidth ? window.innerHeight
								: document.documentElement.clientHeight)
								- _getHeight())
			}
		}
	};
	this.resize = function() {
		var newheight = (window.innerWidth ? window.innerHeight
				: document.documentElement.clientHeight)
				- _getHeight();
		_workspace.style.height = "".concat(newheight, "px");
		_panel.setHeight(newheight);
		if (_hasMinimizedWins()) {
			for ( var tmpWin, i = 0; i < _windows.length; i++) {
				if ((_windows[i]) && (_windows[i][1])) {
					tmpWin = _windows[i][1];
					if (tmpWin._minimized) {
						tmpWin.setPosition(tmpWin.getPosition()[0], newheight
								- tmpWin.header.getSize().height - 2)
					}
				}
			}
		}
	};
	this.createWindow = function(title) {
		var enaRestr = !Jedox.wss.app.UPRestrictMode;
		return new GridWindow( {
			width : 900,
			height : 300,
			minWidth : 350,
			minHeight : 150,
			maximizable : true,
			minimizable : enaRestr,
			bodyBorder : false,
			border : true,
			header : false,
			footer : false,
			plain : true,
			shadow : false,
			hideBorders : true,
			title : title,
			onEsc : Ext.emptyFn,
			contentEl : document.createElement("div"),
			closable : enaRestr,
			_minimized : false,
			_winId : _windows.length
		})
	};
	this.displayWindow = function(win, uid, ghn) {
		var winId = win.initialConfig._winId, title = win.initialConfig.title;
		if (winId != _windows.length) {
			return false
		}
		var winEntry = [ title, win, undefined, uid, ghn ];
		_windows.push(winEntry);
		_panel.add(win);
		win.render(_panel.body);
		win.show();
		win.maximize();
		that.activeWin = win;
		winEntry[2] = _addWinMenuLnk(winId, title);
		return true
	};
	this.showWin = function(winId) {
		var win = _windows[winId][1];
		win.toFront();
		that.activeWin = win;
		if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.DESIGNER) {
			that.chkHBCtl()
		}
	};
	this.showWinByWbId = function(wbId) {
		for ( var win = _windows, i = win.length - 1; i >= 0; --i) {
			if (win[i] != undefined && win[i][3] == wbId) {
				that.showWin(i);
				break
			}
		}
	};
	this.showWinByMeta = function(wbName, wbGhn) {
		for ( var win = _windows, name = wbName, ghn = wbGhn, i = win.length - 1; i >= 0; --i) {
			if (win[i] != undefined
					&& (ghn != undefined ? win[i][4] != null
							&& win[i][4]["g"] == ghn.g
							&& win[i][4]["h"] == ghn.h
							&& win[i][4]["n"] == ghn.n : win[i][4] == null)
					&& win[i][0] == name) {
				that.showWin(i);
				return true
			}
		}
		Jedox.wss.general.showMsg("Application Error".localize(),
				"noWBtoSwitch".localize(), Ext.MessageBox.ERROR);
		return false
	};
	this.chkWinByWbId = function(wbId) {
		for ( var win = _windows, i = win.length - 1; i >= 0; --i) {
			if (win[i] != undefined && win[i][3] == wbId) {
				return true
			}
		}
		return false
	};
	this.cleanupWinClose = function(winId) {
		_delWinMenuLnk(winId);
		if (that.activeWin == _windows[winId][1]) {
			that.activeWin = undefined
		}
		_windows[winId] = undefined;
		if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.DESIGNER) {
			Jedox.wss.app.currFormula.value = "";
			Jedox.wss.app.currCoord.value = "";
			that.chkHBCtl()
		}
	};
	_addWinMenuLnk = function(winId, winName) {
		if (Jedox.wss.app.UPRestrictMode) {
			return
		}
		if (winName.length > 30) {
			winName = winName.substring(0, 14).concat("...",
					winName.substring(winName.length - 9))
		}
		return (Jedox.wss.app.menubar ? Jedox.wss.app.menubar.windowMenu
				: Jedox.wss.app.toolbar.openDocs).add( {
			id : "".concat("menu-win-", ++_openWinNum),
			text : "".concat(_openWinNum, ". ", winName),
			href : "javascript: Jedox.wss.workspace.showWin(".concat(winId,
					");")
		})
	};
	this.chkHBCtl = function() {
		var activeSheet = Jedox.wss.app.activeSheet;
		if (activeSheet == undefined || !activeSheet.isClone()) {
			Jedox.wss.hb.syncCntrl(false);
			Jedox.wss.hb.enaDisHBAdd("enable")
		} else {
			Jedox.wss.hb.syncCntrl(true);
			Jedox.wss.hb.enaDisHBAdd("disable")
		}
	};
	_delWinMenuLnk = function(winId) {
		(Jedox.wss.app.menubar ? Jedox.wss.app.menubar.windowMenu
				: Jedox.wss.app.toolbar.openDocs).remove(_windows[winId][2]);
		var openwn = 0;
		for ( var win, itemTxt, len = _windows.length, id = 0; id < len; ++id) {
			win = _windows[id];
			if (win == undefined || id == winId) {
				continue
			}
			itemTxt = win[2].text;
			win[2].setText("".concat(++openwn, itemTxt.substring(itemTxt
					.indexOf(". "))))
		}
		_openWinNum = openwn
	};
	this.updateMeta = function(winId, winName, ghn) {
		_windows[winId][0] = winName;
		_windows[winId][2].setText(_windows[winId][2].getId().substring(9)
				.concat(". ", winName));
		_windows[winId][4] = ghn
	};
	_panel = new Ext.Panel( {
		renderTo : _workspace,
		autoWidth : true,
		bodyBorder : false,
		border : false,
		bodyStyle : "background-color: transparent;",
		elements : "body"
	});
	this.getVisibleWinsList = function() {
		if ((_windows.length > 0) && (that.activeWin)) {
			var winList = [ that.activeWin ];
			var activeWinId = that.activeWin.id;
			for ( var i = 0; i < _windows.length; ++i) {
				if ((_windows[i]) && (_windows[i][1])
						&& (_windows[i][1].id != activeWinId)
						&& (_windows[i][1].isVisible())
						&& (!_windows[i][1]._minimized)) {
					winList[winList.length] = _windows[i][1]
				}
			}
			return winList
		}
		return []
	};
	this.hideActiveWin = function() {
		if (that.getVisibleWinsList().length > 1) {
			try {
				var metaData = this.getMetaByWinId(Jedox.wss.app.activeBook
						.getWinId());
				for ( var triggers = Jedox.wss.events.triggers.hideWorkbook_before, i = triggers.length - 1, wbMeta = metaData; i >= 0; i--) {
					triggers[i][0]["hideWorkbook_before"].call(parent,
							triggers[i][1], wbMeta.ghn, wbMeta.name)
				}
				that.activeWin.hide();
				Jedox.wss.action.adjustToACL();
				for ( var triggers = Jedox.wss.events.triggers.hideWorkbook_after, i = triggers.length - 1, wbMeta = metaData; i >= 0; i--) {
					triggers[i][0]["hideWorkbook_after"].call(parent,
							triggers[i][1], wbMeta.ghn, wbMeta.name)
				}
			} catch (e) {
				Jedox.wss.general.showMsg("Application Error".localize(),
						e.message.localize(), Ext.MessageBox.ERROR)
			}
		}
	};
	this.getHiddenWinsList = function() {
		var winList = [];
		if ((_windows.length > 0) && (that.activeWin)) {
			for ( var i = 0; i < _windows.length; i++) {
				if ((_windows[i]) && (_windows[i][1])
						&& (!_windows[i][1].isVisible())) {
					winList[winList.length] = _windows[i][1]
				}
			}
		}
		return winList
	};
	this.hasMinimized = function() {
		return _hasMinimizedWins()
	};
	this.getMetaByWinId = function(winId) {
		return {
			name : _windows[winId][0],
			ghn : _windows[winId][4]
		}
	};
	this.closeActiveWin = function() {
		Jedox.wss.general.chkState();
		var actWin = this.activeWin;
		actWin.close();
		return actWin != this.activeWin
	};
	this.closeWinByMeta = function(wbName, wbGhn) {
		for ( var win = _windows, name = wbName, ghn = wbGhn, i = win.length - 1; i >= 0; --i) {
			if (win[i] != undefined
					&& (ghn != undefined ? win[i][4] != null
							&& win[i][4]["g"] == ghn.g
							&& win[i][4]["h"] == ghn.h
							&& win[i][4]["n"] == ghn.n : win[i][4] == null)
					&& win[i][0] == name) {
				if (that.activeWin == win[i][1]) {
					Jedox.wss.general.chkState()
				}
				win[i][1].close();
				var clsDone = win[i] == undefined;
				if (!clsDone) {
					this.showWinByMeta(wbName, wbGhn)
				}
				return clsDone
			}
		}
		Jedox.wss.general.showMsg("Application Error".localize(), "noWBtoClose"
				.localize(), Ext.MessageBox.ERROR);
		return false
	};
	this.notifyCloseByMeta = function(wbMeta) {
		try {
			for ( var triggers = Jedox.wss.events.triggers.closeWorkbook_after, i = triggers.length - 1; i >= 0; --i) {
				triggers[i][0]["closeWorkbook_after"].call(parent,
						triggers[i][1], wbMeta.ghn, wbMeta.name)
			}
		} catch (e) {
			Jedox.wss.general.showMsg("Application Error".localize(), e.message
					.localize(), Ext.MessageBox.ERROR)
		}
	};
	this.resize()
};