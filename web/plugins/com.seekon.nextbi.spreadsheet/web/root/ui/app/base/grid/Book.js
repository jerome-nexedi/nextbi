Jedox.wss.grid.Book = function(cb, winObj, wbId, wbGHN) {
  var that = this;
  this._inited = false;
  this._winObj = winObj;
  this._winId = winObj.initialConfig._winId;
  this._wbId = wbId;
  this._domId = "B".concat(this._winId);
  this._contentEl = winObj.initialConfig.contentEl;
  this.scrollObserver = new Jedox.gen.Observer();
  this._viewMode = Jedox.wss.app.appMode;
  this._app = Jedox.wss.app;
  this._jwgrid = Jedox.wss.grid;
  this._jwstyle = Jedox.wss.style;
  this._json = Ext.util.JSON;
  this._conn = Jedox.wss.backend.conn;
  this._ha = Jedox.wss.backend.ha;
  this._gmode_edit = this._viewMode == this._jwgrid.viewMode.DESIGNER;
  this._defScrollSpeed = this._jwgrid.defDefScrollSpeed;
  this._currScrollSpeeds = [0, 0];
  this._tid_scrollGrid = 0;
  this._tid_startScrollGrid = [0, 0];
  this._tid_scrollReach = 0;
  this._scrollPending = false;
  this._scrollOpPending = false;
  this._doCheckSlider = false;
  this._scrollDirectionMap = [];
  var scrollDir = this._jwgrid.ScrollDirection;
  this._scrollDirectionMap[scrollDir.UP] = [this._SCROLL_VERT, this._SCROLL_BACK];
  this._scrollDirectionMap[scrollDir.DOWN] = [this._SCROLL_VERT, this._SCROLL_FWD];
  this._scrollDirectionMap[scrollDir.LEFT] = [this._SCROLL_HORIZ, this._SCROLL_BACK];
  this._scrollDirectionMap[scrollDir.RIGHT] = [this._SCROLL_HORIZ, this._SCROLL_FWD];
  this._genContentEl();
  Jedox.wss.workspace.displayWindow(winObj, wbId, wbGHN);
  winObj.on("activate",
  function() {
    that._selectGrid()
  });
  winObj.on("bodyresize",
  function() {
    that._fit()
  });
  winObj.on("bodyresize",
  function() {
    that._sheetSelector._refresh()
  });
  winObj.on("beforeclose",
  function() {
    return that._beforeUnloadGrid()
  });
  winObj.on("close",
  function() {
    that._unloadGrid()
  });
  winObj.on("restore",
  function() {
    that._restoreGrid()
  });
  this._horSB = document.getElementById(this._domId.concat("_gridHorSB"));
  this._horSB_bg = document.getElementById(this._domId.concat("_gridHorSB_bg"));
  this._vertSB = document.getElementById(this._domId.concat("_gridVertSB"));
  this._vertSB_bg = document.getElementById(this._domId.concat("_gridVertSB_bg"));
  this._sliders = [YAHOO.widget.Slider.getHorizSlider(this._domId + "_gridHorSB_bg", this._domId + "_gridHorSB_slider", 0, 1, 1), YAHOO.widget.Slider.getVertSlider(this._domId + "_gridVertSB_bg", this._domId + "_gridVertSB_slider", 0, 1, 1)];
  this._sliderFills = [[document.getElementById(this._domId + "_gridHorSB_lfill"), document.getElementById(this._domId + "_gridHorSB_rfill")], [document.getElementById(this._domId + "_gridVertSB_ufill"), document.getElementById(this._domId + "_gridVertSB_dfill")]];
  this._sliderCenter = [document.getElementById(this._domId + "_gridHorSB_cntr"), document.getElementById(this._domId + "_gridVertSB_cntr")];
  this._sliderSize = [0, 0];
  this._sliderTicks = [0, 0];
  this._scrollbarBGOffset = [this._jwgrid.defScrollbarBGOffset[0], this._jwgrid.defScrollbarBGOffset[1]];
  this._sliderBorderElemsSize = [this._jwgrid.defSliderBorderElemsSize[0], this._jwgrid.defSliderBorderElemsSize[1]];
  this._sliderCenterElemSize = [this._jwgrid.defSliderCenterElemSize[0], this._jwgrid.defSliderCenterElemSize[1]];
  this._flattenedCenterElem = [false, false];
  this._sliderThumbSize = [0, 0];
  this._scrollSpace = [0, 0];
  this._scrollSpaceCorr = [0, 0];
  this._sliders[0].subscribe("change",
  function(offset, destCellAbs, cb) {
    that._aPane._scrollGridX(offset, destCellAbs, cb)
  });
  this._sliders[0].subscribe("slideEnd",
  function() {
    that._horSB_slideEnd()
  });
  this._sliders[0].scrollGrid = function(offset, destCellAbs, cb) {
    that._aPane._scrollGridX(offset, destCellAbs, cb)
  };
  this._sliders[0].setConstraint = function(from, to, ticksize) {
    this.thumb.setXConstraint(from, to, ticksize)
  };
  this._sliders[0].onMouseDown = function(ev) {
    that._sliderX_omd(ev)
  };
  this._sliders[0].onMouseUp = function() {
    that._clr_tid_SR()
  };
  this._sliders[0].animate = false;
  this._sliders[0].dragOnly = false;
  this._sliders[0].enableKeys = false;
  this._sliderFills[0].setSize = function(elem, size) {
    this[elem].style.width = size + "px"
  };
  this._sliderCenter[0].setSize = function(size) {
    this.style.width = size + "px"
  };
  this._sliders[1].subscribe("change",
  function(offset, destCellAbs, cb) {
    that._aPane._scrollGridY(offset, destCellAbs, cb)
  });
  this._sliders[1].subscribe("slideEnd",
  function() {
    that._vertSB_slideEnd()
  });
  this._sliders[1].scrollGrid = function(offset, destCellAbs, cb) {
    that._aPane._scrollGridY(offset, destCellAbs, cb)
  };
  this._sliders[1].setConstraint = function(from, to, ticksize) {
    this.thumb.setYConstraint(from, to, ticksize)
  };
  this._sliders[1].onMouseDown = function(ev) {
    that._sliderY_omd(ev)
  };
  this._sliders[1].onMouseUp = function() {
    that._clr_tid_SR()
  };
  this._sliders[1].animate = false;
  this._sliders[1].dragOnly = false;
  this._sliders[1].enableKeys = false;
  this._sliderFills[1].setSize = function(elem, size) {
    this[elem].style.height = size + "px"
  };
  this._sliderCenter[1].setSize = function(size) {
    this.style.height = size + "px"
  };
  var horSB_left = document.getElementById(this._domId + "_gridHorSB_left");
  horSB_left.onmousedown = function() {
    that._horSB_left_omd()
  };
  horSB_left.onmouseup = function() {
    that._horSB_stop()
  };
  horSB_left.onmouseout = function() {
    that._horSB_stop()
  };
  var horSB_right = document.getElementById(this._domId + "_gridHorSB_right");
  horSB_right.onmousedown = function() {
    that._horSB_right_omd()
  };
  horSB_right.onmouseup = function() {
    that._horSB_stop()
  };
  horSB_right.onmouseout = function() {
    that._horSB_stop()
  };
  var vertSB_up = document.getElementById(this._domId + "_gridVertSB_up");
  vertSB_up.onmousedown = function() {
    that._vertSB_up_omd()
  };
  vertSB_up.onmouseup = function() {
    that._vertSB_stop()
  };
  vertSB_up.onmouseout = function() {
    that._vertSB_stop()
  };
  var vertSB_down = document.getElementById(this._domId + "_gridVertSB_down");
  vertSB_down.onmousedown = function() {
    that._vertSB_down_omd()
  };
  vertSB_down.onmouseup = function() {
    that._vertSB_stop()
  };
  vertSB_down.onmouseout = function() {
    that._vertSB_stop()
  };
  this._sheetsOC = document.getElementById(this._domId.concat("_sheets"));
  this._sheetCnt = 0;
  this._sheets = {};
  this._sheetSelector = new Jedox.wss.cls.SheetSelector(this._domId, this);
  this._fit();
  this._conn.setCcmd('[["olst",2],["olst",3],["ocurr",2],["bget","",[],[],{"e_type":"conf"}]]');
  this._conn.sendBatch([this, this._init_post, cb])
};
Jedox.wss.grid.Book.prototype = {
  _SCROLL_HORIZ: 0,
  _SCROLL_VERT: 1,
  _SCROLL_BACK: -1,
  _SCROLL_FWD: 1,
  _FLAG_RENEW: 1,
  _FLAG_RECON: 2,
  _init_post: function(res, cb) {
    if (res[0][0] !== true || res[1][0] !== true || res[2][0] !== true || res[3][0] !== true) {
      return
    }
    this._sheetUids = res[0][1];
    this._sheetsC2O = res[1][1];
    this._sheetsO2C = Jedox.util.objFlip(this._sheetsC2O);
    var aSheetUid = res[2][1];
    this._aSheetUid = aSheetUid in this._sheetsC2O ? this._sheetsC2O[aSheetUid] : aSheetUid;
    this._sheetSelector.readWorksheets();
    if (res[3][1].length) {
      this._conf = res[3][1][0];
      this._autoRefresh = "autoRefresh" in this._conf ? this._conf.autoRefresh: 0
    } else {
      this._conf = null;
      this._autoRefresh = 0
    }
    this._showSheet(cb, this._aSheetUid);
    this._inited = true
  },
  _genContentEl: function() {
    var html = ['<div id="', this._domId, '_gridVertSB" class="gridVertSB">', '<img id="', this._domId, '_gridVertSB_up" class="gridVertSB_up" src="../lib/ext/resources/images/default/s.gif" width="17" height="17" />', '<div id="', this._domId, '_gridVertSB_bg" class="gridVertSB_bg">', '<div id="', this._domId, '_gridVertSB_slider" class="gridVertSB_slider">', '<img class="gridScrollImg_el_top" width="17" height="4" src="../lib/ext/resources/images/default/s.gif" />', '<img class="gridScrollImg_el_vfill" width="17" height="1" id="', this._domId, '_gridVertSB_ufill" src="../lib/ext/resources/images/default/s.gif" />', '<img class="gridScrollImg_el_vcenter" width="17" height="8" id="', this._domId, '_gridVertSB_cntr" src="../lib/ext/resources/images/default/s.gif" />', '<img class="gridScrollImg_el_vfill" width="17" height="1" id="', this._domId, '_gridVertSB_dfill" src="../lib/ext/resources/images/default/s.gif" />', '<img class="gridScrollImg_el_bottom" width="17" height="4" src="../lib/ext/resources/images/default/s.gif" />', "</div>", "</div>", '<img id="', this._domId, '_gridVertSB_down" class="gridVertSB_down" src="../lib/ext/resources/images/default/s.gif" width="17" height="17" />', "</div>", '<div id="', this._domId, '_sheets" class="sheetsOC"> </div>', '<div id="gridBRSpacer"> </div>', '<div class="sheetSelectorOC"', (Jedox.wss.app.UPRestrictMode && Jedox.wss.app.userPreview ? ' style="visibility: hidden;"': ""), ' id="', this._domId, '_sheetSelectorOC">', '<div class="sheetSelectorTB" id="', this._domId, '_sheetSelectorTB"></div>', '<div class="sheetSelectorIC" id="', this._domId, '_sheetSelectorIC"></div>', "</div>", '<div id="', this._domId, '_gridHorSB" class="gridHorSB">', '<img id="', this._domId, '_gridHorSB_left" class="gridHorSB_left" src="../lib/ext/resources/images/default/s.gif" width="17" height="17" />', '<div id="', this._domId, '_gridHorSB_bg" class="gridHorSB_bg">', '<div id="', this._domId, '_gridHorSB_slider" class="gridHorSB_slider">', '<img class="gridScrollImg_el_left" width="4" height="17" src="../lib/ext/resources/images/default/s.gif" />', '<img class="gridScrollImg_el_hfill" height="17" width="1" id="', this._domId, '_gridHorSB_lfill" src="../lib/ext/resources/images/default/s.gif" />', '<img class="gridScrollImg_el_hcenter" height="17" width="8" id="', this._domId, '_gridHorSB_cntr" src="../lib/ext/resources/images/default/s.gif" />', '<img class="gridScrollImg_el_hfill" height="17" width="1" id="', this._domId, '_gridHorSB_rfill" src="../lib/ext/resources/images/default/s.gif" />', '<img class="gridScrollImg_el_right" width="4" height="17" src="../lib/ext/resources/images/default/s.gif" />', "</div>", "</div>", '<img id="', this._domId, '_gridHorSB_right" class="gridHorSB_right" src="../lib/ext/resources/images/default/s.gif" width="17" height="17" />', "</div>"];
    this._contentEl.id = this._domId.concat("_OC");
    this._contentEl.innerHTML = html.join("")
  },
  _fit: function() {
    if (this._winObj._isMinimizing) {
      return
    }
    this._sheetsOCWidth = this._winObj.getInnerWidth() - 17;
    this._sheetsOCHeight = this._winObj.getInnerHeight() - 17;
    var horSB_width = this._sheetsOCWidth / 2;
    this._sliderSize[0] = horSB_width - this._scrollbarBGOffset[0];
    this._sliderSize[1] = this._sheetsOCHeight - this._scrollbarBGOffset[1];
    this._sheetsOC.style.width = "".concat(this._sheetsOCWidth, "px");
    this._sheetSelector.setWidth(this._sheetsOCWidth - horSB_width);
    this._horSB.style.width = "".concat(horSB_width, "px");
    this._horSB_bg.style.width = "".concat(this._sliderSize[0], "px");
    this._sheetsOC.style.height = this._vertSB.style.height = "".concat(this._sheetsOCHeight, "px");
    this._vertSB_bg.style.height = "".concat(this._sliderSize[1], "px");
    for (var sheetUid in this._sheets) {
      this._sheets[sheetUid]._fit()
    }
  },
  _showSheet: function(cb, uid, flags) {
    if (! (uid in this._sheetUids)) {
      return false
    }
    if (this._aSheet) {
      this._aSheet._contentEl.style.visibility = "hidden"
    }
    if (flags & this._FLAG_RENEW && uid in this._sheets) {
      this._sheetsOC.removeChild(this._sheets[uid]._contentEl);
      delete this._sheets[uid]
    }
    this._aSheetUid = uid;
    if (uid in this._sheets) {
      this._app.activeSheet = this._aSheet = this._sheets[uid];
      this._app.activePane = this._aPane = this._aSheet._aPane;
      this._app.environment = this._aPane._env.shared;
      this._aSheet._contentEl.style.visibility = "";
      this._sheetSelector.enable(true);
      this._aSheet.recalc(flags & this._FLAG_RECON ? [this._aSheet, this._aSheet._reconstruct, cb] : cb)
    } else {
      this._app.activeSheet = this._aSheet = this._sheets[uid] = new Jedox.wss.grid.Sheet(cb, this, uid)
    }
    return true
  },
  setClone: function(cloneUid) {
    this._sheetsO2C[this._aSheetUid] = cloneUid;
    this._sheetsC2O[cloneUid] = this._aSheetUid
  },
  _actOnSheetSel: function(cb) {
    switch (this._sheetSelector.action) {
    case this._sheetSelector.actionTypes.SELECTED:
      var sheetUid = this._sheetSelector.getActiveSheetId(),
      res;
      if (! (sheetUid in this._sheetUids)) {
        break
      }
      if (sheetUid in this._sheetsO2C) {
        this._ha.selectSheet(this._sheetsO2C[sheetUid], true, this._gmode_edit)
      } else {
        if ((res = this._ha.selectSheet(sheetUid, false, this._gmode_edit, null, cb instanceof Array && cb.length > 1 ? true: false))[1]) {
          this._sheetsO2C[sheetUid] = res[2],
          this._sheetsC2O[res[2]] = sheetUid
        }
      }
      this._showSheet(cb, sheetUid, this._FLAG_RECON);
      Jedox.wss.workspace.chkHBCtl();
      break;
    case this._sheetSelector.actionTypes.ADDED:
      var res = this._ha.addSheet(this._sheetSelector.actionData);
      if (res.errcode) {
        break
      }
      this._sheetSelector.freeze(false);
      this._sheetSelector.insertEntry(res.name, res.wsid, this._sheetSelector.actionData);
      this._sheetSelector.freeze(true);
      this._sheetUids[res.wsid] = res.name;
      this._showSheet(cb, res.wsid);
      break;
    case this._sheetSelector.actionTypes.REMOVED:
      var sheetUid = this._sheetSelector.getActiveSheetId();
      if (sheetUid in this._sheetsO2C) {
        this._ha.selectSheet(this._sheetsO2C[sheetUid], true, this._gmode_edit)
      } else {
        this._ha.selectSheet(sheetUid, false, this._gmode_edit)
      }
      this._showSheet(cb, sheetUid, this._FLAG_RECON);
      var removedSheetUid = this._sheetSelector.actionData;
      this._sheetsOC.removeChild(this._sheets[removedSheetUid]._contentEl);
      delete this._sheetUids[removedSheetUid];
      delete this._sheets[removedSheetUid];
      this._conn.cmd(this._conn.dummy_cb, ["odel", 2], [removedSheetUid]);
      break;
    case this._sheetSelector.actionTypes.COPIED:
      var data = this._sheetSelector.actionData,
      sameWb = typeof data.wbId != "string" || !data.wbId.length,
      res = this._ha.copySheet(data.wsId, data.nextSheetId, data.wbId);
      if (res.errcode) {
        break
      }
      if (sameWb) {
        this._sheetSelector.freeze(false);
        this._sheetSelector.insertEntry(res.name, res.wsid, data.nextSheetId);
        this._sheetSelector.freeze(true);
        this._sheetUids[res.wsid] = res.name;
        this._showSheet(cb, res.wsid)
      } else {
        this._sheetSelector.enable(true);
        Jedox.wss.general.setInputMode(this._app.lastInputModeDlg);
        this._app.lastInputMode = this._jwgrid.GridMode.READY;
        Jedox.wss.workspace.showWinByWbId(data.wbId);
        var destWb = this._app.activeBook;
        destWb._sheetUids[res.wsid] = res.name;
        destWb._sheetSelector.insertEntry(res.name, res.wsid, data.nextSheetId)
      }
      break;
    case this._sheetSelector.actionTypes.MOVED:
      var data = this._sheetSelector.actionData,
      sameWb = typeof data.wbId != "string" || !data.wbId.length,
      res = this._ha.moveSheet(data.wsId, data.nextSheetId, data.wbId);
      if (res.errcode) {
        break
      }
      if (sameWb) {
        this._sheetSelector.freeze(false);
        this._sheetSelector.insertEntry(res.name, res.wsid, data.nextSheetId);
        this._sheetSelector.freeze(true);
        this._sheetSelector.enable(true);
        if (cb instanceof Array && cb.length > 1) {
          cb[1].apply(cb[0], cb.slice(2))
        }
      } else {
        this._sheetsOC.removeChild(this._sheets[data.wsId]._contentEl);
        delete this._sheetUids[data.wsId];
        delete this._sheets[data.wsId];
        this._sheetSelector.enable(true);
        Jedox.wss.general.setInputMode(this._app.lastInputModeDlg);
        this._app.lastInputMode = this._jwgrid.GridMode.READY;
        Jedox.wss.workspace.showWinByWbId(data.wbId);
        var destWb = this._app.activeBook;
        destWb._sheetUids[res.wsid] = res.name;
        destWb._sheetSelector.insertEntry(res.name, res.wsid, data.nextSheetId)
      }
      break;
    case this._sheetSelector.actionTypes.UNCLONED:
      Jedox.wss.wsel.unloadAll();
      if (this._ha.removeCloneWorksheet() === false) {
        break
      }
      delete this._sheetsC2O[this._sheetsO2C[this._aSheetUid]];
      delete this._sheetsO2C[this._aSheetUid];
      this._ha.selectSheet(this._aSheetUid, false, this._gmode_edit);
      this._showSheet(cb, this._aSheetUid, this._FLAG_RENEW);
      break
    }
    this._sheetSelector.action = this._sheetSelector.actionTypes.NONE
  },
  _selectGrid: function() {
    if (this._app.activeBook == this) {
      return
    }
    var workspace = Jedox.wss.workspace,
    metaData = workspace.getMetaByWinId(this._winId);
    try {
      for (var triggers = Jedox.wss.events.triggers.switchWorkbook_before,
      i = triggers.length - 1,
      wbMeta = metaData; i >= 0; i--) {
        triggers[i][0]["switchWorkbook_before"].call(parent, triggers[i][1], wbMeta.ghn, wbMeta.name)
      }
    } catch(e) {
      Jedox.wss.general.showMsg("Application Error".localize(), e.message.localize(), Ext.MessageBox.ERROR)
    }
    this._app.activeBook = this;
    this._app.activeSheet = this._aSheet;
    this._app.activePane = this._aPane;
    workspace.activeWin = this._winObj;
    this._app.environment = this._aPane._env.shared;
    if (this._gmode_edit) {
      workspace.chkHBCtl()
    }
    this._app.updateUndoState();
    if (!this._sheetSelector.getLoadMark(true)) {
      this._ha.selectBook(this._wbId);
      var sheetUid = this._sheetSelector.getActiveSheetId();
      if (sheetUid in this._sheetsO2C) {
        this._ha.selectSheet(this._sheetsO2C[sheetUid], true, this._gmode_edit)
      } else {
        this._ha.selectSheet(sheetUid, false, this._gmode_edit)
      }
      this._aPane.recalc()
    }
    Jedox.wss.action.adjustToACL();
    try {
      for (var triggers = Jedox.wss.events.triggers.switchWorkbook_after,
      i = triggers.length - 1,
      wbMeta = metaData; i >= 0; i--) {
        triggers[i][0]["switchWorkbook_after"].call(parent, triggers[i][1], wbMeta.ghn, wbMeta.name)
      }
    } catch(e) {
      Jedox.wss.general.showMsg("Application Error".localize(), e.message.localize(), Ext.MessageBox.ERROR)
    }
  },
  _beforeUnloadGrid: function() {
    function dywtscDlg(bnsRes) {
      function wbClose() {
        var activeBook = Jedox.wss.app.activeBook;
        activeBook._doClose = true;
        activeBook._winObj.close()
      }
      Ext.MessageBox.buttonText = {
        ok: "OK".localize(),
        cancel: "Cancel".localize(),
        yes: "Yes".localize(),
        no: "No".localize()
      };
      if (bnsRes[0][0] && bnsRes[0][1]) {
        Ext.MessageBox.show({
          title: "File not saved".localize(),
          msg: "autosave_msg".localize(),
          icon: Ext.MessageBox.WARNING,
          modal: true,
          buttons: Ext.MessageBox.YESNOCANCEL,
          fn: function(btn) {
            switch (btn) {
            case "yes":
              Jedox.wss.book.save([this, wbClose]);
              break;
            case "no":
              wbClose();
              break;
            case "cancel":
              break
            }
          }
        })
      } else {
        wbClose()
      }
    }
    var wbMeta = Jedox.wss.workspace.getMetaByWinId(this._winId);
    try {
      for (var triggers = Jedox.wss.events.triggers.closeWorkbook_before,
      i = triggers.length - 1; i >= 0; --i) {
        triggers[i][0]["closeWorkbook_before"].call(parent, triggers[i][1], wbMeta.ghn, wbMeta.name)
      }
      if (this._app.environment.viewMode != Jedox.wss.grid.viewMode.USER && this._doClose !== true && (wbMeta.ghn == null || wbMeta.ghn.p >= Jedox.wss.grid.permission.PERM_WRITE)) {
        this._conn.cmd([this, dywtscDlg], ["bns"], []);
        return false
      }
      Jedox.wss.wsel.unloadAll()
    } catch(e) {
      Jedox.wss.general.showMsg("Application Error".localize(), e.message.localize(), Ext.MessageBox.ERROR);
      return false
    }
    return true
  },
  _unloadGrid: function() {
    var metaData = Jedox.wss.workspace.getMetaByWinId(this._winId);
    this._ha.removeBook(this._wbId);
    Jedox.wss.book.destroy(this._winId);
    Jedox.wss.action.adjustToACL();
    try {
      for (var triggers = Jedox.wss.events.triggers.closeWorkbook_after,
      i = triggers.length - 1,
      wbMeta = metaData; i >= 0; --i) {
        triggers[i][0]["closeWorkbook_after"].call(parent, triggers[i][1], wbMeta.ghn, wbMeta.name)
      }
    } catch(e) {
      Jedox.wss.general.showMsg("Application Error".localize(), e.message.localize(), Ext.MessageBox.ERROR)
    }
  },
  _restoreGrid: function() {
    this._aPane._env.shared.winStateMax = this._winObj.maximized
  },
  _recalcSlider: function(type, no_repos) {
    if ((type | 1) != 1) {
      return false
    }
    var paneOCSize = parseInt(type == this._SCROLL_HORIZ ? this._aPane._contentEl.style.width: this._aPane._contentEl.style.height);
    this._sliderThumbSize[type] = parseInt(paneOCSize / (paneOCSize + (this._aPane._farthestSeenCell[type] - this._aPane._cpp[type]) * this._aSheet._defColRowDims[type]) * this._sliderSize[type]);
    if (this._sliderThumbSize[type] < this._sliderBorderElemsSize[type]) {
      this._sliderThumbSize[type] = this._sliderBorderElemsSize[type]
    }
    this._scrollSpace[type] = this._sliderSize[type] - this._sliderThumbSize[type];
    this._sliderTicks[type] = this._scrollSpace[type] / (this._aPane._farthestSeenCell[type] - this._aPane._defcppi[type]);
    this._scrollSpaceCorr[type] = 0;
    var ticksi = Math.round(this._sliderTicks[type]);
    if (ticksi > 0) {
      var mod = this._scrollSpace[type] % ticksi;
      if (mod > ticksi / 2) {
        this._scrollSpaceCorr[type] = ticksi - mod
      } else {
        this._scrollSpaceCorr[type] = -mod
      }
    } else {
      ticksi = 1
    }
    this._sliders[type].setConstraint(0, this._scrollSpace[type] + this._scrollSpaceCorr[type], ticksi);
    var thumbexcfill = this._sliderBorderElemsSize[type] + this._sliderCenterElemSize[type];
    if (this._sliderThumbSize[type] < thumbexcfill) {
      if (this._flattenedCenterElem[type] == false) {
        this._sliderCenter[type].setSize(0);
        this._flattenedCenterElem[type] = true
      }
      thumbexcfill = this._sliderBorderElemsSize[type]
    } else {
      if (this._flattenedCenterElem[type] == true) {
        this._sliderCenter[type].setSize(this._sliderCenterElemSize[type]);
        this._flattenedCenterElem[type] = false
      }
    }
    var fillsize = this._sliderThumbSize[type] - thumbexcfill - this._scrollSpaceCorr[type];
    if (fillsize > 0) {
      var fillhalf = Math.round(fillsize / 2);
      this._sliderFills[type].setSize(1, fillhalf);
      this._sliderFills[type].setSize(0, fillsize - fillhalf)
    } else {
      this._sliderFills[type].setSize(1, 0);
      this._sliderFills[type].setSize(0, 0)
    }
    if (no_repos) {
      return
    }
    this._sliders[type].setValue(this._aPane._lastDestCell[type] * this._sliderTicks[type], true, true, true)
  },
  startScrollGrid: function(cb, type, dir, speed) {
    if (arguments[4] !== true) {
      if ((type | 1) != 1 || this._scrollOpPending) {
        return false
      }
      if (speed == undefined || speed == NaN || speed <= 0) {
        speed = this._defScrollSpeed
      }
      if (speed == this._currScrollSpeeds[type]) {
        return true
      }
      if (this._currScrollSpeeds[type]) {
        clearTimeout(this._tid_startScrollGrid[type])
      }
      this._currScrollSpeeds[type] = speed;
      this._scrollOpPending = true
    }
    if (dir == this._SCROLL_BACK) {
      if (this._aPane._lastDestCell[type] > 0) {
        this._doCheckSlider = true;
        return this._sliders[type].scrollGrid(0, this._aPane._lastDestCell[type] - 1, [this, this.startScrollGrid_post, cb, type, dir, speed])
      }
    } else {
      if (this._scrollSpace[type] + this._scrollSpaceCorr[type] - this._sliders[type].getValue() < this._sliderTicks[type]) {
        if (this._gmode_edit) {
          if (this._aPane._farthestSeenCell[type] < this._aPane._maxCoords[type]) {
            this._aPane._farthestSeenCell[type]++;
            return this._sliders[type].scrollGrid(0, this._aPane._lastDestCell[type] + 1, [this, this.startScrollGrid_post, cb, type, dir, speed, true])
          } else {
            if (this._aPane._lastDestCell[type] + this._aPane._cppi[type] < this._aPane._maxCoords[type]) {
              return this._sliders[type].scrollGrid(0, this._aPane._lastDestCell[type] + 1, [this, this.startScrollGrid_post, cb, type, dir, speed])
            }
          }
        } else {
          if (this._aPane._lastDestCell[type] + this._aPane._cppi[type] < this._aPane._farthestSeenCell[type]) {
            return this._sliders[type].scrollGrid(0, this._aPane._lastDestCell[type] + 1, [this, this.startScrollGrid_post, cb, type, dir, speed])
          }
        }
      } else {
        return this._sliders[type].scrollGrid(0, this._aPane._lastDestCell[type] + 1, [this, this.startScrollGrid_post, cb, type, dir, speed])
      }
    }
    this._currScrollSpeeds[type] = 0;
    this._scrollOpPending = false;
    return true
  },
  startScrollGrid_post: function(cb, type, dir, speed, mode) {
    if (mode) {
      this._recalcSlider(type, true),
      this._sliders[type].setValue(this._scrollSpace[type] + this._scrollSpaceCorr[type], true, true, true)
    } else {
      this._sliders[type].setValue(this._aPane._lastDestCell[type] * this._sliderTicks[type], true, true, true)
    }
    if (this._currScrollSpeeds[type]) {
      var that = this;
      this._tid_startScrollGrid[type] = setTimeout(function() {
        that.startScrollGrid(cb, type, dir, speed, true)
      },
      speed)
    }
    if (cb instanceof Array && cb.length > 1) {
      cb[1].apply(cb[0], cb.slice(2))
    }
    this._scrollOpPending = false
  },
  stopScrollGrid: function(type) {
    this._currScrollSpeeds[type] = 0
  },
  scrollTo: function(cb, col, row, minscroll, force) {
    if (this._scrollOpPending) {
      return false
    }--col; --row;
    var doHoriz = false,
    doVert = false;
    if (col < this._aPane._lastDestCell[0]) {
      doHoriz = true
    } else {
      if (col > this._aPane._lastDestCell[0] + this._aPane._cppi[0] - 1) {
        doHoriz = true;
        if (minscroll == true) {
          col -= this._aPane._cppi[0] - 1
        }
      } else {
        if (force) {
          doHoriz = true
        }
      }
    }
    if (row < this._aPane._lastDestCell[1]) {
      doVert = true
    } else {
      if (row > this._aPane._lastDestCell[1] + this._aPane._cppi[1] - 1) {
        doVert = true;
        if (minscroll == true) {
          row -= this._aPane._cppi[1] - 1
        }
      } else {
        if (force) {
          doVert = true
        }
      }
    }
    this._scrollOpPending = true;
    if (doHoriz) {
      if (col > this._aPane._maxCoords[0] - this._aPane._cppi[0]) {
        col = this._aPane._maxCoords[0] - this._aPane._cppi[0]
      }
      if (col + this._aPane._cppi[0] > this._aPane._farthestSeenCell[0]) {
        this._aPane._farthestSeenCell[0] = col + this._aPane._cppi[0]
      }
      this._doCheckSlider = true;
      this._aPane._scrollGridX(undefined, col, doVert ? [this, this._scrollTo_vert, cb, row] : [this, this._scrollTo_post, cb, false])
    } else {
      if (doVert) {
        this._scrollTo_vert(cb, row)
      } else {
        if (cb instanceof Array && cb.length > 1) {
          this._scrollTo_post(cb, false)
        }
      }
    }
  },
  _scrollTo_vert: function(cb, row) {
    if (row > this._aPane._maxCoords[1] - this._aPane._cppi[1]) {
      row = this._aPane._maxCoords[1] - this._aPane._cppi[1]
    }
    var atEnd = false;
    if (row + this._aPane._cppi[1] >= this._aPane._farthestSeenCell[1]) {
      this._aPane._farthestSeenCell[1] = row + this._aPane._cppi[1],
      atEnd = true
    }
    this._doCheckSlider = true;
    this._aPane._scrollGridY(undefined, row, [this, this._scrollTo_post, cb, atEnd])
  },
  _scrollTo_post: function(cb, atEnd) {
    if (atEnd) {
      this._sliders[1].setValue(this._scrollSpace[1], true, true, true)
    }
    if (cb instanceof Array && cb.length > 1) {
      cb[1].apply(cb[0], cb.slice(2))
    }
    this._scrollOpPending = false
  },
  _checkSlider: function(type) {
    if (this._aPane._farthestSeenCell[type] > this._aSheet._farthestUsedCell[type] && this._aPane._lastDestCell[type] + this._aPane._cppi[type] < this._aPane._farthestSeenCell[type]) {
      if (this._aPane._lastDestCell[type] + this._aPane._cppi[type] > this._aSheet._farthestUsedCell[type]) {
        this._aPane._farthestSeenCell[type] = this._aPane._lastDestCell[type] + this._aPane._cppi[type]
      } else {
        this._aPane._farthestSeenCell[type] = this._aSheet._farthestUsedCell[type]
      }
      if (this._aPane._farthestSeenCell[type] < this._aPane._defcppi[type] + 2) {
        this._aPane._farthestSeenCell[type] = this._aPane._defcppi[type] + 2
      }
    }
    this._recalcSlider(type);
    this._doCheckSlider = false;
    return true
  },
  _scrollReach: function(type, mousePos) {
    if ((type | 1) != 1 || this._scrollOpPending) {
      return false
    }
    var dir = 0;
    if (type == this._SCROLL_HORIZ) {
      if (mousePos < this._sliders[0].thumb.lastPageX) {
        dir = this._SCROLL_BACK
      } else {
        if (mousePos > this._sliders[0].thumb.lastPageX + this._sliders[0].thumb._domRef.offsetWidth) {
          dir = this._SCROLL_FWD
        }
      }
    } else {
      if (mousePos < this._sliders[1].thumb.lastPageY) {
        dir = this._SCROLL_BACK
      } else {
        if (mousePos > this._sliders[1].thumb.lastPageY + this._sliders[1].thumb._domRef.offsetHeight) {
          dir = this._SCROLL_FWD
        }
      }
    }
    if (dir == this._SCROLL_BACK) {
      var interimDestCell = this._aPane._lastDoneDestCell[type] - this._aPane._cppi[type];
      if (interimDestCell < 0) {
        interimDestCell = 0
      }
      this._scrollOpPending = true;
      this._doCheckSlider = true;
      this._sliders[type].scrollGrid(0, interimDestCell, [this, this.scrollReach_post, type, mousePos, interimDestCell])
    } else {
      if (dir == this._SCROLL_FWD) {
        var interimDestCell = this._aPane._lastDoneDestCell[type] + this._aPane._cppi[type];
        if (interimDestCell + this._aPane._cppi[type] > this._aPane._farthestSeenCell[type]) {
          if (this._gmode_edit) {
            this._aPane._farthestSeenCell[type] = interimDestCell + this._aPane._cppi[type];
            if (this._aPane._farthestSeenCell[type] > this._aPane._maxCoords[type]) {
              this._aPane._farthestSeenCell[type] = this._aPane._maxCoords[type]
            }
            this._recalcSlider(type)
          } else {
            interimDestCell = this._aPane._farthestSeenCell[type] - this._aPane._cppi[type]
          }
        }
        this._scrollOpPending = true;
        this._doCheckSlider = true;
        this._sliders[type].scrollGrid(0, interimDestCell, [this, this.scrollReach_post, type, mousePos, interimDestCell])
      } else {
        this._scrollOpPending = false,
        this._checkSlider(type)
      }
    }
  },
  scrollReach_post: function(type, mousePos, interimDestCell) {
    this._sliders[type].setValue(interimDestCell * this._sliderTicks[type], true, true, true);
    if (!this._scrollOpPending) {
      return
    }
    var that = this;
    this._tid_scrollReach = setTimeout(function() {
      that._scrollReach(type, mousePos)
    },
    50);
    this._scrollOpPending = false
  },
  exec: function(res, cb) {
    if (typeof res != "object" || !res.length) {
      return false
    }
    cmds = res[0];
    for (var cmd, cmdid, c = -1; (cmd = cmds[++c]) != undefined;) {
      switch (cmdid = cmd.shift()) {
      case "crgn":
        this._aPane._cache.clear();
        for (var rgn, frn = this._aPane.furnishCell,
        comb = cmd[0].cm, i = 0; (rgn = cmd[++i]) != undefined;) {
          for (var cell, x = rgn[0], y = rgn[1], w = rgn[2], j = 2; (cell = rgn[++j]) != undefined;) {
            frn.call(this._aPane, x, y, cell, comb);
            if (++x - rgn[0] >= w && w > 0) {++y,
              x = rgn[0]
            }
          }
        }
        break;
      case "ccr":
        this._aSheet.setColRowSize(cmd.shift(), cmd);
        break;
      case "wmv":
        if (cmd.length) {
          for (var wsel, i = cmd.length - 1; i >= 0; --i) {
            wsel = cmd[i];
            switch (wsel.type) {
            case "img":
              Jedox.wss.wsel.img.moveTo(wsel.id, wsel.pos, wsel.offsets);
              break;
            case "formel":
              Jedox.wss.wsel.formelMoveTo(wsel);
              break;
            case "chart":
              Jedox.wss.wsel.chart.reMovize(wsel.id, wsel.pos, wsel.offsets);
              break;
            case "hb":
              Jedox.wss.hb.move(wsel.id, wsel.pos);
              break
            }
          }
        }
        break;
      case "rw":
        if (cmd.length) {
          Jedox.wss.general.refreshWorksheetElements(cmd)
        }
        break;
      case "rf":
        if (cmd.length) {
          Jedox.wss.wsel.refreshAll(cmd)
        }
        break;
      case "wtrd":
        if (cmd.length) {
          Jedox.wss.wsel.updateTarget(cmd)
        }
        break;
      case "curn":
        this._aSheet._farthestUsedCell[0] = cmd[0];
        if (cmd[0] > this._aPane._farthestSeenCell[0]) {
          this._aPane._farthestSeenCell[0] = cmd[0],
          this._recalcSlider(0)
        }
        this._aSheet._farthestUsedCell[1] = cmd[1];
        if (cmd[1] > this._aPane._farthestSeenCell[1]) {
          this._aPane._farthestSeenCell[1] = cmd[1],
          this._recalcSlider(1)
        }
        break;
      case "ncr":
        this._aSheet.newDims(0, cmd[0]);
        this._aSheet.newDims(1, cmd[1]);
        break
      }
    }
    Jedox.wss.general.refreshCursorField();
    if (this._gmode_edit) {
      Jedox.wss.general.setCurrentCoord()
    }
    if (cb instanceof Array && cb.length > 1) {
      cb[1].apply(cb[0], [res[1]].concat(cb.slice(2)))
    }
  },
  cb: function(handle, params, diff_params) {
    if (diff_params === undefined) {
      diff_params = []
    }
    var rgr = (typeof diff_params == "object" && diff_params !== null) ? this._aPane.getRealGridRange() : (diff_params = [], null);
    var dimSet = diff_params[0] != false ? 1 : 0;
    if (diff_params[1] != false) {
      dimSet |= 2
    }
    this._conn.rpc([this._conn, this._conn.handleRes, [[this, this.exec]]], "WSS", "cb", [handle, params, rgr, dimSet, diff_params[2]])
  },
  _undo_redo: function(op, steps) {
    this._conn.createBatch();
    this._conn.cmd(null, [op], [steps]);
    this._conn.cmd(null, ["gust"], []);
    this._conn.sendBatch([this, this.exec], this._aPane.getRealGridRange())
  },
  undo: function(steps) {
    this._undo_redo("undo", steps)
  },
  redo: function(steps) {
    this._undo_redo("redo", steps)
  },
  _clr_tid_SR: function() {
    clearTimeout(this._tid_scrollReach);
    this._scrollOpPending = false
  },
  _sliderX_omd: function(ev) {
    this._scrollReach(0, ev.clientX)
  },
  _sliderY_omd: function(ev) {
    this._scrollReach(1, ev.clientY)
  },
  _horSB_left_omd: function() {
    this.startScrollGrid(undefined, 0, -1)
  },
  _horSB_right_omd: function() {
    this.startScrollGrid(undefined, 0, 1)
  },
  _horSB_stop: function() {
    this.stopScrollGrid(0)
  },
  _vertSB_up_omd: function() {
    this.startScrollGrid(undefined, 1, -1)
  },
  _vertSB_down_omd: function() {
    this.startScrollGrid(undefined, 1, 1)
  },
  _vertSB_stop: function() {
    this.stopScrollGrid(1)
  },
  _horSB_slideEnd: function() {
    this._scrollPending ? this._doCheckSlider = true: this._checkSlider(0)
  },
  _vertSB_slideEnd: function() {
    this._scrollPending ? this._doCheckSlider = true: this._checkSlider(1)
  },
  getEnvironment: function() {
    return this._aPane._env
  },
  getDomId: function() {
    return this._aPane._domId
  },
  getWinId: function() {
    return this._winId
  },
  getScrollDirMap: function() {
    return this._scrollDirectionMap
  },
  setWinCaption: function(capt) {
    this._winObj.setTitle(capt)
  },
  setWbId: function(id) {
    this._wbId = id
  },
  getWbId: function() {
    return this._wbId
  },
  getWsId: function() {
    return this._sheetSelector.getActiveSheetId()
  },
  getSheetSelector: function() {
    return this._sheetSelector
  },
  getCursorField: function() {
    return this._aPane._cursorField
  },
  isClone: function() {
    return this._aSheet.isClone()
  },
  getCoordByHdr: function(type, obj) {
    return this._aSheet.getCoordByHdr(type, obj)
  },
  getHdrByCoord: function(type, coord) {
    return this._aSheet.getHdrByCoord(type, coord)
  },
  activateHdr: function(type, idx, cls) {
    return this._aSheet.activateHdr(type, idx, cls)
  },
  activateHdrRng: function(type, rng, cls) {
    return this._aSheet.activateHdrRng(type, rng, cls)
  },
  activateHdrAll: function(type, cls) {
    return this._aSheet.activateHdrAll(type, cls)
  },
  insertCol: function(col, num) {
    return this._aSheet.insertCol(col, num)
  },
  insertRow: function(row, num) {
    return this._aSheet.insertRow(row, num)
  },
  deleteCol: function(col, num) {
    return this._aSheet.deleteCol(col, num)
  },
  deleteRow: function(row, num) {
    return this._aSheet.deleteRow(row, num)
  },
  resizeColRow: function(type, ranges, newsize) {
    return this._aSheet.resizeColRow(type, ranges, newsize)
  },
  autofitColRow: function(type, ranges) {
    return this._aSheet.autofitColRow(type, ranges)
  },
  getCoordsByCell: function(obj) {
    return this._aPane.getCoordsByCell(obj)
  },
  getCellByCoords: function(col, row) {
    return this._aPane.getCellByCoords(col, row)
  },
  getPixelsByCoords: function(col, row) {
    return this._aPane.getPixelsByCoords(col, row)
  },
  furnishCell: function(col, row, obj, comb) {
    return this._aPane.furnishCell(col, row, obj, comb)
  },
  getNeighByOffset: function(col, row, offx, offy) {
    return this._aPane.getNeighByOffset(col, row, offx, offy)
  },
  isCellVisible: function(col, row) {
    return this._aPane.isCellVisible(col, row)
  },
  isCellLocked: function(col, row) {
    return this._aPane.isCellLocked(col, row)
  },
  getCellDims: function(col, row) {
    return this._aPane.getCellDims(col, row)
  },
  getCellValue: function(col, row) {
    return this._aPane.getCellValue(col, row)
  },
  getCellFVal: function(col, row) {
    return this._aPane.getCellFVal(col, row)
  },
  getCellUVal: function(col, row) {
    return this._aPane.getCellUVal(col, row)
  },
  getCellFormula: function(col, row) {
    return this._aPane.getCellFormula(col, row)
  },
  getCellType: function(col, row) {
    return this._aPane.getCellType(col, row)
  },
  getCellStyle: function(col, row) {
    return this._aPane.getCellStyle(col, row)
  },
  setCellValue: function(col, row, val) {
    return this._aPane.setCellValue(col, row, val)
  },
  setRangeValue: function(range, vals) {
    return this._aPane.setRangeValue(range, vals)
  },
  setArrayFormula: function(range, val) {
    return this._aPane.setArrayFormula(range, val)
  },
  merge: function(range, isUnMerge) {
    return this._aPane.merge(range, isUnMerge)
  },
  getMergeInfo: function(col, row) {
    return this._aPane.getMergeInfo(col, row)
  },
  setCellStyle: function(col, row, style) {
    return this._aPane.setCellStyle(col, row, style)
  },
  setRangeStyle: function(range, style) {
    return this._aPane.setRangeStyle(range, style)
  },
  getRangeStyle: function(range, attrs) {
    return this._aPane.getRangeStyle(range, attrs)
  },
  setRangeFormat: function(range, fmt) {
    return this._aPane.setRangeFormat(range, fmt)
  },
  getRangeFormat: function(range) {
    return this._aPane.getRangeFormat(range)
  },
  setRangeLock: function(range, lock) {
    return this._aPane.setRangeLock(range, lock)
  },
  getCoordsFirstVCell: function() {
    return this._aPane.getCoordsFirstVCell()
  },
  getCoordsLastVCell: function() {
    return this._aPane.getCoordsLastVCell()
  },
  getVisibleRange: function(type) {
    return this._aPane.getVisibleRange(type)
  },
  getRealGridRange: function() {
    return this._aPane.getRealGridRange()
  },
  getViewportSize: function() {
    return this._aPane.getViewportSize()
  },
  getViewportPos: function() {
    return this._aPane.getViewportPos()
  },
  getFarthestUsedCell: function() {
    return this._aPane.getFarthestUsedCell()
  },
  pasteRange: function(cb, range, clip_id, paste_what) {
    return this._aPane.pasteRange(cb, range, clip_id, paste_what)
  },
  clrRange: function(range, clr_what) {
    return this._aPane.clrRange(range, clr_what)
  },
  cbFire: function(col, row, attr_name, params) {
    return this._aPane.cbFire(col, row, attr_name, params)
  },
  recalc: function() {
    return this._aPane.recalc()
  },
  getColHdrsIC: function() {
    return this._aPane._colHdrsIC
  },
  getRowHdrsIC: function() {
    return this._aPane._rowHdrsIC
  },
  getBookIC: function() {
    return this._aPane._ic
  }
};