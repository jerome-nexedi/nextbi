Jedox.wss.grid.Sheet = function(cb, book, uid) {
  this._inited = false;
  this._book = book;
  this._uid = uid;
  this._id = book._sheetCnt++;
  this._domId = book._domId.concat("S", this._id);
  this._contentEl = document.createElement("div");
  this._winObj = book._winObj;
  this._viewMode = Jedox.wss.app.appMode;
  this._app = Jedox.wss.app;
  this._jwgrid = Jedox.wss.grid;
  this._jwstyle = Jedox.wss.style;
  this._conn = Jedox.wss.backend.conn;
  this._cellSizeIncr = this._jwgrid.defCellSizeIncr;
  this._setText = Jedox.util.setText;
  this._n2l = Jedox.wss.app.numberToLetter;
  this._gmode_edit = book._gmode_edit;
  this._defMaxCoords = [this._jwgrid.defMaxCoords[0], this._jwgrid.defMaxCoords[1]];
  this._conn.setCcmd('[["gdcr",2],["gcr",0,1,'.concat(this._defMaxCoords[0] - 1, '],["gcr",1,1,', this._defMaxCoords[1] - 1, '],["gurn"],["wget","",[],["e_id","crosshairs","panes"],{"e_type":"panescnf"}]]'));
  this._conn.sendBatch([this, this._init_post, cb])
};
Jedox.wss.grid.Sheet.prototype = {
  _SCROLL_HORIZ: 0,
  _SCROLL_VERT: 1,
  _init_post: function(res, cb) {
    var that = this;
    if (res[0][0] !== true || res[1][0] !== true || res[2][0] !== true || res[3][0] !== true || res[4][0] !== true) {
      return
    }
    this._defColRowDims = res[0][1],
    this._colRowDims = [this._colWidths = new Jedox.gen.SparseVector(this._defMaxCoords[0], this._defColRowDims[0]), this._rowHeights = new Jedox.gen.SparseVector(this._defMaxCoords[1], this._defColRowDims[1])];
    var incr = this._cellSizeIncr[0];
    for (var w, col_ws = res[1][1], len = col_ws.length, i = 0; i < len; i += 2) {
      this._colWidths.setElemAt(col_ws[i] + 1, (w = col_ws[i + 1]) >= incr ? w: 0)
    }
    incr = this._cellSizeIncr[1];
    for (var h, row_hs = res[2][1], len = row_hs.length, i = 0; i < len; i += 2) {
      this._rowHeights.setElemAt(row_hs[i] + 1, (h = row_hs[i + 1]) >= incr ? h: 0)
    }
    this._farthestUsedCell = res[3][1];
    if (res[4][1].length) {
      this._paneConfUid = res[4][1][0].e_id;
      this._crosshairs = res[4][1][0].crosshairs;
      this._paneConf = res[4][1][0].panes
    } else {
      this._paneConfUid = null;
      this._crosshairs = [0, 0];
      this._paneConf = [{
        range: [1, 1, this._defMaxCoords[0], this._defMaxCoords[1]],
        hscroll: true,
        vscroll: true,
        pos: [1, 1]
      }]
    }
    this._genContentEl();
    if (this._gmode_edit) {
      this._colHdrsOC0 = document.getElementById(this._domId + "_colHdrsOC0");
      this._colHdrsIC0 = document.getElementById(this._domId + "_colHdrsIC0");
      this._rowHdrsOC0 = document.getElementById(this._domId + "_rowHdrsOC0");
      this._rowHdrsIC0 = document.getElementById(this._domId + "_rowHdrsIC0");
      this._colHdrsOC0.scrollLeft = 0;
      this._rowHdrsOC0.scrollTop = 0;
      this._hdrsICs0 = [this._colHdrsIC0, this._rowHdrsIC0];
      this._hdrsICs1 = [];
      this._hdrClss = ["gridColHdr", "gridRowHdr"];
      var p0Coords = this._paneConf[0].coords;
      if (this._crosshairs[0]) {
        this._colHdrsOC0.style.width = "".concat(p0Coords.w, "px");
        this._colHdrsOC1 = document.getElementById(this._domId + "_colHdrsOC1");
        this._colHdrsIC1 = document.getElementById(this._domId + "_colHdrsIC1");
        this._hdrsICs1[0] = this._colHdrsIC1;
        this._colHdrsOC1.style.borderLeft = "1px solid #000000";
        this._colHdrsOC1.scrollLeft = 0
      }
      if (this._crosshairs[1]) {
        this._rowHdrsOC0.style.height = "".concat(p0Coords.h, "px");
        this._rowHdrsOC1 = document.getElementById(this._domId + "_rowHdrsOC1");
        this._rowHdrsIC1 = document.getElementById(this._domId + "_rowHdrsIC1");
        this._hdrsICs1[1] = this._rowHdrsIC1;
        this._rowHdrsOC1.style.borderTop = "1px solid #000000";
        this._rowHdrsOC1.scrollTop = 0
      }
      this._colHdrs0 = [];
      this._rowHdrs0 = [];
      this._hdrArrs0 = [this._colHdrs0, this._rowHdrs0];
      this._activeHdrs_o0 = [{},
      {}];
      this._activeHdrs_a0 = [[], []];
      this._activeHdrsAll0 = [false, false];
      for (var ch = this._colHdrs0,
      hdrs = this._colHdrsIC0.getElementsByTagName("div"), i = this._numCols0 - 1; i >= 0; --i) {
        ch[i] = hdrs[i]
      }
      for (var rh = this._rowHdrs0,
      hdrs = this._rowHdrsIC0.getElementsByTagName("div"), i = this._numRows0 - 1; i >= 0; --i) {
        rh[i] = hdrs[i]
      }
      this._hdrArrs1 = [];
      this._activeHdrs_o1 = [{},
      {}];
      this._activeHdrs_a1 = [[], []];
      this._activeHdrsAll1 = [false, false];
      if (this._crosshairs[0]) {
        this._colHdrs1 = [];
        this._hdrArrs1[0] = this._colHdrs1;
        for (var ch = this._colHdrs1,
        hdrs = this._colHdrsIC1.getElementsByTagName("div"), i = this._numCols1 - 1; i >= 0; --i) {
          ch[i] = hdrs[i]
        }
      }
      if (this._crosshairs[1]) {
        this._rowHdrs1 = [];
        this._hdrArrs1[1] = this._rowHdrs1;
        for (var rh = this._rowHdrs1,
        hdrs = this._rowHdrsIC1.getElementsByTagName("div"), i = this._numRows1 - 1; i >= 0; --i) {
          rh[i] = hdrs[i]
        }
      }
      document.getElementById(this._domId + "_gridSelectAll").onmousedown = function(ev) {
        that._aPane._defaultSelection.markAllCells(ev)
      };
      this._colHdrsIC0.onmousemove = Jedox.wss.mouse.checkForResize;
      this._colHdrsIC0.onmousedown = function(ev) {
        that._aPane._defaultSelection.selectOrResize(ev);
        document.body.onselectstart = Jedox.util.ignoreSelection
      };
      this._colHdrsIC0.ondblclick = Jedox.wss.mouse.autoSizeCell;
      this._rowHdrsIC0.onmousemove = Jedox.wss.mouse.checkForResize;
      this._rowHdrsIC0.onmousedown = function(ev) {
        that._aPane._defaultSelection.selectOrResize(ev);
        document.body.onselectstart = Jedox.util.ignoreSelection
      };
      this._rowHdrsIC0.ondblclick = Jedox.wss.mouse.autoSizeCell;
      document.body.onmouseup = function() {
        document.body.onselectstart = Jedox.util.unignoreSelection
      }
    } else {
      this._activeHdrs_o0 = [[], []];
      this._activeHdrs_a0 = [[], []];
      this._activeHdrs_o1 = [[], []];
      this._activeHdrs_a1 = [[], []]
    }
    this._paneOCs = [];
    this._panes = [];
    for (var i = this._numPanes - 1; i >= 0; --i) {
      this["_paneOC".concat(i)] = this._paneOCs[i] = document.getElementById(this._domId.concat("P", i, "_OC"));
      this._panes[i] = new Jedox.wss.grid.Pane(i ? 0 : cb, this, i, this._paneOCs[i])
    }
    this._book._recalcSlider(0);
    this._book._recalcSlider(1);
    this._inited = true
  },
  _genContentEl: function() {
    if (this._gmode_edit) {
      var hdrsWidth = 41,
      hdrsHeight = 19,
      html = ['<div id="', this._domId, '_gridSelectAll" class="gridSelectAll"></div>'];
      var decr = this._cellSizeIncr[0];
      if (this._crosshairs[0]) {
        html.push('<div id="', this._domId, '_colHdrsOC0" class="gridColHdrsOC" style="left:', hdrsWidth, 'px;"><div id="', this._domId, '_colHdrsIC0" class="gridColHdrsIC">');
        for (var w, cols = this._crosshairs[0], c = this._paneConf[0].range[0], l = 0, i = 0; i < cols; ++i, ++c, l += w) {
          w = this._colWidths.getElemAt(c);
          html.push('<div class="gridColHdr" style="left:', l, "px;", (w ? "width:".concat(w - decr, "px;") : "display:none;"), '">', this._n2l[c], "</div>")
        }
        html.push("</div></div>");
        this._numCols0 = cols;
        var colPaneId = 1,
        colsOffset = l
      } else {
        var colPaneId = 0,
        colsOffset = 0
      }
      var colsSize = this._book._sheetsOCWidth - hdrsWidth - colsOffset;
      html.push('<div id="', this._domId, "_colHdrsOC", colPaneId, '" class="gridColHdrsOC" style="left:', colsOffset + hdrsWidth, "px;width:", colsSize, 'px;"><div id="', this._domId, "_colHdrsIC", colPaneId, '" class="gridColHdrsIC">');
      for (var w, xtra_c = 2,
      c_first = this._paneConf[colPaneId].range[0], c = c_first, l = 0; l <= colsSize || xtra_c--; ++c, l += w) {
        w = this._colWidths.getElemAt(c);
        html.push('<div class="gridColHdr" style="left:', l, "px;", (w ? "width:".concat(w - decr, "px;") : "display:none;"), '">', this._n2l[c], "</div>")
      }
      html.push("</div></div>");
      this["_numCols".concat(colPaneId)] = c - c_first;
      decr = this._cellSizeIncr[1];
      if (this._crosshairs[1]) {
        html.push('<div id="', this._domId, '_rowHdrsOC0" class="gridRowHdrsOC" style="top:', hdrsHeight, 'px;"><div id="', this._domId, '_rowHdrsIC0" class="gridRowHdrsIC">');
        for (var h, rows = this._crosshairs[1], r = this._paneConf[0].range[1], t = 0, i = 0; i < rows; ++i, ++r, t += h) {
          h = this._rowHeights.getElemAt(r);
          html.push('<div class="gridRowHdr" style="top:', t, "px;", (h ? "height:".concat(h - decr, "px;") : "display:none;"), '">', r, "</div>")
        }
        html.push("</div></div>");
        this._numRows0 = rows;
        var rowPaneId = 1,
        rowsOffset = t
      } else {
        var rowPaneId = 0,
        rowsOffset = 0
      }
      var rowsSize = this._book._sheetsOCHeight - hdrsHeight - rowsOffset;
      html.push('<div id="', this._domId, "_rowHdrsOC", rowPaneId, '" class="gridRowHdrsOC" style="top:', rowsOffset + hdrsHeight, "px;height:", rowsSize, 'px;"><div id="', this._domId, "_rowHdrsIC", rowPaneId, '" class="gridRowHdrsIC">');
      for (var h, xtra_r = 2,
      r_first = this._paneConf[rowPaneId + colPaneId].range[1], r = r_first, t = 0; t <= rowsSize || xtra_r--; ++r, t += h) {
        h = this._rowHeights.getElemAt(r);
        html.push('<div class="gridRowHdr" style="top:', t, "px;", (h ? "height:".concat(h - decr, "px;") : "display:none;"), '">', r, "</div>")
      }
      html.push("</div></div>");
      this["_numRows".concat(rowPaneId)] = r - r_first
    } else {
      var hdrsWidth = 0,
      hdrsHeight = 0,
      html = [];
      if (this._crosshairs[0]) {
        for (var cols = this._crosshairs[0], colsOffset = 0, c = this._paneConf[0].range[0], i = 0; i < cols; ++i, colsOffset += this._colWidths.getElemAt(c++)) {}
        this._numCols0 = cols;
        var colPaneId = 1
      } else {
        var colPaneId = 0,
        colsOffset = 0
      }
      var colsSize = this._book._sheetsOCWidth - hdrsWidth - colsOffset;
      for (var xtra_c = 2,
      c_first = this._paneConf[colPaneId].range[0], c = c_first, l = 0; l <= colsSize || xtra_c--; l += this._colWidths.getElemAt(c++)) {}
      this["_numCols".concat(colPaneId)] = c - c_first;
      if (this._crosshairs[1]) {
        for (var rows = this._crosshairs[1], rowsOffset = 0, r = this._paneConf[0].range[1], i = 0; i < rows; ++i, rowsOffset += this._rowHeights.getElemAt(r++)) {}
        this._numRows0 = rows;
        var rowPaneId = 1
      } else {
        var rowPaneId = 0,
        rowsOffset = 0
      }
      var rowsSize = this._book._sheetsOCHeight - hdrsHeight - rowsOffset;
      for (var xtra_r = 2,
      r_first = this._paneConf[rowPaneId + colPaneId].range[1], r = r_first, t = 0; t <= rowsSize || xtra_r--; t += this._rowHeights.getElemAt(r++)) {}
      this["_numRows".concat(rowPaneId)] = r - r_first
    }
    if (this._crosshairs[0] && this._crosshairs[1]) {
      this._paneConf[0].coords = {
        l: hdrsWidth,
        t: hdrsHeight,
        w: colsOffset,
        h: rowsOffset,
        x: 0,
        y: 0
      };
      this._paneConf[1].coords = {
        l: colsOffset + hdrsWidth,
        t: hdrsHeight,
        w: colsSize,
        h: rowsOffset,
        x: 1,
        y: 0
      };
      this._paneConf[2].coords = {
        l: hdrsWidth,
        t: rowsOffset + hdrsHeight,
        w: colsOffset,
        h: rowsSize,
        x: 0,
        y: 1
      };
      this._paneConf[3].coords = {
        l: colsOffset + hdrsWidth,
        t: rowsOffset + hdrsHeight,
        w: colsSize,
        h: rowsSize,
        x: 1,
        y: 1
      };
      this._paneConf[1].customCSS = "border-left:1px solid #000000;";
      this._paneConf[2].customCSS = "border-top:1px solid #000000;";
      this._paneConf[3].customCSS = "border-top:1px solid #000000;border-left:1px solid #000000;"
    } else {
      if (this._crosshairs[0]) {
        this._paneConf[0].coords = {
          l: hdrsWidth,
          t: hdrsHeight,
          w: colsOffset,
          h: rowsSize,
          x: 0,
          y: 0
        };
        this._paneConf[1].coords = {
          l: colsOffset + hdrsWidth,
          t: hdrsHeight,
          w: colsSize,
          h: rowsSize,
          x: 1,
          y: 0
        };
        this._paneConf[1].customCSS = "border-left:1px solid #000000;"
      } else {
        if (this._crosshairs[1]) {
          this._paneConf[0].coords = {
            l: hdrsWidth,
            t: hdrsHeight,
            w: colsSize,
            h: rowsOffset,
            x: 0,
            y: 0
          };
          this._paneConf[1].coords = {
            l: hdrsWidth,
            t: rowsOffset + hdrsHeight,
            w: colsSize,
            h: rowsSize,
            x: 0,
            y: 1
          };
          this._paneConf[1].customCSS = "border-top:1px solid #000000;"
        } else {
          this._paneConf[0].coords = {
            l: hdrsWidth,
            t: hdrsHeight,
            w: colsSize,
            h: rowsSize,
            x: 0,
            y: 0
          }
        }
      }
    }
    this._numPanes = this._paneConf.length;
    for (var pane, i = this._numPanes - 1; i >= 0; --i) {
      pane = this._paneConf[i];
      html.push('<div id="', this._domId, "P", i, '_OC" class="paneOC" style="left:', pane.coords.l, "px;top:", pane.coords.t, "px;width:", pane.coords.w, "px;height:", pane.coords.h, "px;", pane.customCSS, '"></div>')
    }
    this._contentEl.id = this._domId.concat("_OC");
    this._contentEl.className = "sheetOC";
    this._contentEl.innerHTML = html.join("");
    this._book._sheetsOC.appendChild(this._contentEl)
  },
  _fit: function() {
    this._contentEl.style.width = "".concat(this._book._sheetsOCWidth, "px");
    this._contentEl.style.height = "".concat(this._book._sheetsOCHeight, "px");
    if (this._crosshairs[0] && this._crosshairs[1]) {
      var colsSizeId = 1,
      rowsSizeId = 2,
      fits = {
        1 : 1,
        2 : 2,
        3 : 3
      }
    } else {
      if (this._crosshairs[0]) {
        var colsSizeId = 1,
        rowsSizeId = 0,
        fits = {
          0 : 2,
          1 : 3
        }
      } else {
        if (this._crosshairs[1]) {
          var colsSizeId = 0,
          rowsSizeId = 1,
          fits = {
            0 : 1,
            1 : 3
          }
        } else {
          var colsSizeId = 0,
          rowsSizeId = 0,
          fits = {
            0 : 3
          }
        }
      }
    }
    var colsSize = this._book._sheetsOCWidth - this._paneConf[colsSizeId].coords.l,
    rowsSize = this._book._sheetsOCHeight - this._paneConf[rowsSizeId].coords.t,
    colsSizePx = "".concat(colsSize, "px"),
    rowsSizePx = "".concat(rowsSize, "px");
    if (this._gmode_edit) {
      this["_colHdrsOC".concat(colsSizeId ? "1": "0")].style.width = colsSizePx;
      this["_rowHdrsOC".concat(rowsSizeId ? "1": "0")].style.height = rowsSizePx
    }
    for (id in fits) {
      if (fits[id] & 1) {
        this._paneConf[id].coords.w = colsSize,
        this._paneOCs[id].style.width = colsSizePx
      }
      if (fits[id] & 2) {
        this._paneConf[id].coords.h = rowsSize,
        this._paneOCs[id].style.height = rowsSizePx
      }
    }
    this._reconstruct()
  },
  _reconstruct: function(cb) {
    var cIdx = this._crosshairs[0] ? 1 : 0,
    rIdx = this._crosshairs[1] ? 1 : 0;
    if (this._gmode_edit) {
      var colHdrs = this["_colHdrs".concat(cIdx)],
      rowHdrs = this["_rowHdrs".concat(rIdx)],
      cOC = this["_colHdrsOC".concat(cIdx)],
      rOC = this["_rowHdrsOC".concat(rIdx)],
      lastC = colHdrs[this["_numCols".concat(cIdx)] - 1],
      lastR = rowHdrs[this["_numRows".concat(rIdx)] - 1],
      lastCol = this.getCoordByHdr(0, lastC),
      lastRow = this.getCoordByHdr(1, lastR)
    } else {
      var cPane = this._panes[cIdx],
      rPane = rIdx ? this._panes[rIdx + cIdx] : this._panes[0],
      cOC = cPane._oc,
      rOC = rPane._oc,
      lastC = cPane._firstRowCells[cPane._numCols - 1],
      lastR = rPane._tableRows[rPane._numRows - 1],
      lastCol = cPane._currTopCell[0] + cPane._numCols - 1,
      lastRow = rPane._currTopCell[1] + rPane._numRows - 1
    }
    var hEdge = cOC.scrollLeft + cOC.offsetWidth,
    vEdge = rOC.scrollTop + rOC.offsetHeight,
    cEdge = parseInt(lastC.style.left) + this._colWidths.getElemAt(lastCol),
    rEdge = parseInt(lastR.style.top) + this._rowHeights.getElemAt(lastRow),
    prcs = [{},
    {},
    {},
    {}],
    next = lastCol + 1;
    hEdge += this._colWidths.getElemAt(next) + this._colWidths.getElemAt(++next);
    vEdge += this._rowHeights.getElemAt(next = lastRow + 1) + this._rowHeights.getElemAt(++next);
    if (cEdge < hEdge) {
      var c = lastCol,
      diff;
      if (this._gmode_edit) {
        var docFrag = document.createDocumentFragment(),
        decr = this._cellSizeIncr[0],
        hdr,
        w;
        do {
          hdr = lastC.cloneNode(true);
          this._setText(hdr, this._n2l[++c]);
          hdr.style.left = "".concat(cEdge, "px");
          w = this._colWidths.getElemAt(c);
          if (w) {
            hdr.style.width = "".concat(w - decr, "px");
            if (hdr.style.display) {
              hdr.style.display = ""
            }
          } else {
            if (!hdr.style.display) {
              hdr.style.display = "none"
            }
          }
          docFrag.appendChild(hdr);
          colHdrs.push(hdr);
          cEdge += w
        } while ( cEdge < hEdge );
        cOC.firstChild.appendChild(docFrag)
      } else {
        do {
          cEdge += this._colWidths.getElemAt(++c)
        } while ( cEdge < hEdge )
      }
      this["_numCols".concat(cIdx)] += (diff = c - lastCol);
      prcs[cIdx].c = diff;
      if (rIdx) {
        prcs[rIdx | cIdx << 1].c = diff
      }
    }
    if (rEdge < vEdge) {
      var r = lastRow,
      diff;
      if (this._gmode_edit) {
        var docFrag = document.createDocumentFragment(),
        decr = this._cellSizeIncr[1],
        hdr,
        h;
        do {
          hdr = lastR.cloneNode(true);
          this._setText(hdr, ++r);
          hdr.style.top = "".concat(rEdge, "px");
          h = this._rowHeights.getElemAt(r);
          if (h) {
            hdr.style.height = "".concat(h - decr, "px");
            if (hdr.style.display) {
              hdr.style.display = ""
            }
          } else {
            if (!hdr.style.display) {
              hdr.style.display = "none"
            }
          }
          docFrag.appendChild(hdr);
          rowHdrs.push(hdr);
          rEdge += h
        } while ( rEdge < vEdge );
        rOC.firstChild.appendChild(docFrag)
      } else {
        do {
          rEdge += this._rowHeights.getElemAt(++r)
        } while ( rEdge < vEdge )
      }
      this["_numRows".concat(rIdx)] += (diff = r - lastRow);
      if (cIdx) {
        prcs[rIdx <<= 1].r = diff;
        prcs[cIdx | rIdx].r = diff
      } else {
        prcs[rIdx].r = diff
      }
    }
    for (var prc = prcs[0], i = 0; i < this._numPanes; prc = prcs[++i]) {
      if (prc.c || prc.r) {
        this._panes[i]._reconstruct(cb, prc.c, prc.r)
      } else {
        this._panes[i]._fit(cb)
      }
    }
  },
  isClone: function() {
    return this._uid in this._book._sheetsO2C
  },
  getUid: function() {
    return this._uid in this._book._sheetsO2C ? this._book._sheetsO2C[this._uid] : this._uid
  },
  activateHdr: function(type, idx, cls) {
    var notfnd = true,
    clss = this._hdrClss[type].concat(" ", cls);
    for (var size, sprop = type ? "height": "width", crd = this._colRowDims[type], csi = this._cellSizeIncr[type], hdrsIC = this._hdrsICs0[type], hdrs = this._activeHdrs_a0[type], i = hdrs.length - 1, hdr = hdrs[i]; i >= 0; hdr = hdrs[--i]) {
      if (hdr[0] != idx) {
        hdrsIC.removeChild(hdr[1])
      } else {
        notfnd = false;
        var actHdr = hdr[1];
        if (actHdr.className != clss) {
          actHdr.className = clss
        }
        if (actHdr.style[sprop] != (size = "".concat(crd.getElemAt(idx) - csi, "px"))) {
          actHdr.style[sprop] = size
        }
      }
    }
    if (notfnd) {
      var actHdr = this._hdrArrs0[type][idx - this._aPane._currTopCell[type]].cloneNode(true);
      actHdr.id = "ah".concat(type, "_", idx);
      actHdr.className = clss;
      this._hdrsICs0[type].appendChild(actHdr)
    }
    this._activeHdrs_a0[type] = [[idx, actHdr]];
    this._activeHdrs_o0[type] = {};
    this._activeHdrs_o0[type][idx] = 0;
    return true
  },
  activateHdrRng: function(type, rng, cls) {
    var hdrs_a = [],
    hdrs_o = {},
    clss = this._hdrClss[type].concat(" ", cls);
    for (var size, sprop = type ? "height": "width", csi = this._cellSizeIncr[type], crd = this._colRowDims[type], hdrsIC = this._hdrsICs0[type], hdrs = this._activeHdrs_a0[type], i = hdrs.length - 1, hdr = hdrs[i]; i >= 0; hdr = hdrs[--i]) {
      if (rng[0] <= hdr[0] && hdr[0] <= rng[1]) {
        hdrs_a.push(hdr);
        hdrs_o[hdr[0]] = hdrs_a.length - 1;
        if (hdr[1].className != clss) {
          hdr[1].className = clss
        }
        if (hdr[1].style[sprop] != (size = "".concat(crd.getElemAt(hdr[0]) - csi, "px"))) {
          hdr[1].style[sprop] = size
        }
      } else {
        hdrsIC.removeChild(hdr[1])
      }
    }
    for (var actHdr, hdrArrs = this._hdrArrs0[type], ctct = this._aPane._currTopCell[type], hdrIC = this._hdrsICs0[type], i = rng[0]; i <= rng[1]; ++i) {
      if (! (i in hdrs_o)) {
        if (i - ctct >= this._aPane._tableSize[type]) {
          break
        } else {
          if (i - ctct < 0) {
            continue
          }
        }
        actHdr = hdrArrs[i - ctct].cloneNode(true);
        actHdr.id = "ah".concat(type, "_", i);
        actHdr.className = clss;
        hdrIC.appendChild(actHdr);
        hdrs_a.push([i, actHdr]);
        hdrs_o[i] = hdrs_a.length - 1
      }
    }
    this._activeHdrs_a0[type] = hdrs_a;
    this._activeHdrs_o0[type] = hdrs_o;
    return true
  },
  activateHdrAll: function(type, cls) {
    if (this._activeHdrsAll0[type] == cls) {
      return true
    }
    for (var hdrsIC = this._hdrsICs0[type], hdrs = this._activeHdrs_a0[type], i = hdrs.length - 1; i >= 0; --i) {
      hdrsIC.removeChild(hdrs[i][1])
    }
    if (this._activeHdrs_a0[type].length > 0) {
      this._activeHdrs_a0[type] = [],
      this._activeHdrs_o0[type] = {}
    }
    for (var hdrs = this._hdrArrs0[type], i = hdrs.length - 1; i >= 0; --i) {
      hdrs[i].className = cls
    }
    this._activeHdrsAll0[type] = cls;
    return true
  },
  getCoordByHdr: function(type, obj) {
    if ((type | 1) != 1) {
      return undefined
    }
    for (var hdrs = this._hdrArrs0[type], ahdrs_o = this._activeHdrs_o0[type], ahdrs_a = this._activeHdrs_a0[type], i = this._aPane._tableSize[type] - 1, ctci = this._aPane._currTopCell[type] + i; i >= 0; --i, --ctci) {
      if (hdrs[i] == obj) {
        return ctci
      } else {
        if (ctci in ahdrs_o && ahdrs_a[ahdrs_o[ctci]][1] == obj) {
          return ctci
        }
      }
    }
  },
  getHdrByCoord: function(type, coord) {
    if ((type | 1) != 1) {
      return undefined
    }
    return this._hdrArrs0[type][coord - this._aPane._currTopCell[type]]
  },
  insertCol: function(col, num) {
    this._conn.cmd([this._book, this._book.exec], ["ic"], [[col, num]], this._aPane.getRealGridRange(), true, false)
  },
  insertRow: function(row, num) {
    this._conn.cmd([this._book, this._book.exec], ["ir"], [[row, num]], this._aPane.getRealGridRange(), false, true)
  },
  deleteCol: function(col, num) {
    this._conn.cmd([this._book, this._book.exec], ["dc"], [[col, num]], this._aPane.getRealGridRange(), true, false)
  },
  deleteRow: function(row, num) {
    this._conn.cmd([this._book, this._book.exec], ["dr"], [[row, num]], this._aPane.getRealGridRange(), false, true)
  },
  setColRowSize: function(type, chngsets) {
    if ((type | 1) != 1 || !(chngsets instanceof Array)) {
      return false
    }
    var sizes, range, size, sizepx, len, visible, oldvals, csi = this._cellSizeIncr[type],
    crs = this._colRowDims[type],
    def = crs.getDef(),
    tbl_size = [this._numCols0, this._numRows0],
    ctc = this._aPane._currTopCell,
    tbl_beg = ctc[type],
    tbl_end = tbl_beg + tbl_size[type] - 1,
    i;
    for (i = chngsets.length - 1; i >= 0; --i) {
      if ((len = (sizes = chngsets[i].slice()).length - 2) < 1) {
        continue
      }
      range = [sizes.shift(), sizes.shift()];
      if (len != 1 && range[1] - range[0] != len - 1) {
        continue
      }
      if (len != 1) {
        size = undefined
      } else {
        if ((size = sizes[0]) == -1) {
          size = def
        } else {
          if (size < csi) {
            size = 0
          }
        }
      }
      if (visible = (range[0] > tbl_end || range[1] < tbl_beg) ? false: (oldvals = {},
      true)) {
        for (var min = range[0] > tbl_beg ? range[0] : tbl_beg, c = range[1] < tbl_end ? range[1] : tbl_end; c >= min; --c) {
          oldvals[c] = crs.getElemAt(c)
        }
      }
      if (range[0] == 1 && range[1] == this._defMaxCoords[type] && len == 1 && size != this._defColRowDims[type]) {
        this._defColRowDims[type] = size;
        crs.reInit(size);
        this._aPane._defcpp[type] = (type == this._SCROLL_HORIZ ? this._aPane._ocWidth: this._aPane._ocHeight) / size;
        this._aPane._defcppi[type] = this._aPane._defcpp[type] | 0;
        sizepx = "".concat(size - csi, "px")
      } else {
        if (len == 1) {
          for (var min = range[0], c = range[1]; c >= min; --c) {
            if (crs.getElemAt(c) != size) {
              crs.setElemAt(c, size)
            }
          }
          sizepx = "".concat(size - csi, "px")
        } else {
          for (var c = range[1], j = len - 1; j >= 0; --j, --c) {
            if ((size = sizes[j]) == -1) {
              size = def
            } else {
              if (size < csi) {
                size = 0
              }
            }
            if (crs.getElemAt(c) != size) {
              crs.setElemAt(c, size)
            }
          }
        }
      }
      if (!visible) {
        continue
      }
      for (var oldsize, st, tr_s, newpos, diff, move_sum = 0,
      th = this._numRows0,
      tw = this._numCols0,
      tm = this._aPane._tableMat,
      ch = this._aPane._colHdrs,
      cs = this._aPane._colSeps,
      rh = this._aPane._rowHdrs,
      fr = this._aPane._firstRowCells,
      tr = this._aPane._tableRows,
      ahdrs_o = this._activeHdrs_o0[type], ahdrs_a = this._activeHdrs_a0[type], curr = range[0] > tbl_beg ? range[0] - tbl_beg: 0, max = range[1] < tbl_end ? range[1] - tbl_beg: tbl_end - tbl_beg, curr_abs = tbl_beg + curr, c = curr_abs - range[0]; curr <= max;) {
        if (len > 1) {
          if ((size = sizes[c++]) == -1) {
            size = def
          } else {
            if (size < csi) {
              size = 0
            }
          }
          sizepx = "".concat(size - csi, "px")
        }
        if (type == this._SCROLL_HORIZ) {
          if ((oldsize = oldvals[curr_abs]) != size) {
            if (this._gmode_edit) {
              if (size) {
                st = ch[curr].style;
                st.width = sizepx;
                if (oldsize == 0) {
                  st.display = ""
                }
              } else {
                if (oldsize) {
                  ch[curr].style.display = "none"
                }
              }
              if (curr_abs in ahdrs_o) {
                if (size) {
                  st = ahdrs_a[ahdrs_o[curr_abs]][1].style;
                  st.width = sizepx;
                  if (oldsize == 0) {
                    st.display = ""
                  }
                } else {
                  if (oldsize) {
                    ahdrs_a[ahdrs_o[curr_abs]][1].style.display = "none"
                  }
                }
              }
            }
            diff = size - oldsize;
            for (var mi, mrow, cell, cspx, row = ctc[1], j = 0; j < th; ++j, ++row) {
              if ((mi = this._aPane._storage.getPart(curr, j, "m")) !== undefined) {
                if (mi[0] === true) {
                  cspx = "".concat(parseInt(tm[j][curr].style.width) + diff, "px")
                } else {
                  cspx = sizepx;
                  if ((mrow = mi[1] + 1) == row && (cell = this._aPane.getCellByCoords(mi[2] + 1, mrow)) !== undefined) {
                    cell.style.width = "".concat(parseInt(cell.style.width) + diff, "px")
                  }
                }
              } else {
                cspx = sizepx
              }
              if (size) {
                st = tm[j][curr].style;
                st.width = cspx;
                if (oldsize == 0) {
                  st.display = ""
                }
              } else {
                if (oldsize) {
                  tm[j][curr].style.display = "none"
                }
              }
            }
            move_sum += diff
          }++curr_abs;
          if (curr++==max || !move_sum) {
            continue
          }
          newpos = "".concat(parseInt(fr[curr].style.left) + move_sum, "px");
          if (this._gmode_edit) {
            ch[curr].style.left = newpos;
            if (curr_abs in ahdrs_o) {
              ahdrs_a[ahdrs_o[curr_abs]][1].style.left = newpos
            }
            cs[curr].style.left = newpos
          }
          for (var j = 0; j < th; tm[j][curr].style.left = newpos, ++j) {}
        } else {
          if ((oldsize = oldvals[curr_abs]) != size) {
            if (size) {
              if (this._gmode_edit) {
                st = rh[curr].style;
                st.height = sizepx;
                if (oldsize == 0) {
                  st.display = ""
                }
                if (curr_abs in ahdrs_o) {
                  st = ahdrs_a[ahdrs_o[curr_abs]][1].style;
                  st.height = sizepx;
                  if (oldsize == 0) {
                    st.display = ""
                  }
                }
              }
              st = tr[curr].style;
              st.height = sizepx;
              if (oldsize == 0) {
                st.display = ""
              }
            } else {
              if (oldsize) {
                if (this._gmode_edit) {
                  rh[curr].style.display = "none";
                  if (curr_abs in ahdrs_o) {
                    ahdrs_a[ahdrs_o[curr_abs]][1].style.display = "none"
                  }
                }
                tr[curr].style.display = "none"
              }
            }
            diff = size - oldsize;
            for (var mi, mcol, cell, col = ctc[0], j = 0; j < tw; ++j, ++col) {
              if ((mi = this._aPane._storage.getPart(j, curr, "m")) !== undefined) {
                if (mi[0] === true) {
                  tm[curr][j].style.height = "".concat(parseInt(tm[curr][j].style.height) + diff, "px")
                } else {
                  if ((mcol = mi[2] + 1) == col && (cell = this._aPane.getCellByCoords(mcol, mi[1] + 1)) !== undefined) {
                    cell.style.height = "".concat(parseInt(cell.style.height) + diff, "px")
                  }
                }
              }
            }
            move_sum += diff
          }++curr_abs;
          if (curr++==max || !move_sum) {
            continue
          }
          newpos = "".concat(parseInt((tr_s = tr[curr].style).top) + move_sum, "px");
          if (this._gmode_edit) {
            rh[curr].style.top = newpos;
            if (curr_abs in ahdrs_o) {
              ahdrs_a[ahdrs_o[curr_abs]][1].style.top = newpos
            }
          }
          tr_s.top = newpos
        }
      }
      if (!move_sum) {
        continue
      }
      for (var ahdr, ahdr_s, ahdrs_a = this._activeHdrs_a0[type], curr = -1; (ahdr = ahdrs_a[++curr]) != undefined;) {
        if (ahdr[0] <= range[1]) {
          continue
        }
        if (type == this._SCROLL_HORIZ) { (ahdr_s = ahdr[1].style).left = "".concat(parseInt(ahdr_s.left) + move_sum, "px")
        } else { (ahdr_s = ahdr[1].style).top = "".concat(parseInt(ahdr_s.top) + move_sum, "px")
        }
      }
      if (range[1] >= tbl_end) {
        continue
      }
      for (var tr_s, newpos, curr = range[1] - tbl_beg + 1, max = tbl_size[type]; curr < max; ++curr) {
        if (type == this._SCROLL_HORIZ) {
          newpos = "".concat(parseInt(fr[curr].style.left) + move_sum, "px");
          if (this._gmode_edit) {
            ch[curr].style.left = newpos,
            cs[curr].style.left = newpos
          }
          for (var j = 0; j < th; tm[j][curr].style.left = newpos, ++j) {}
        } else {
          newpos = "".concat(parseInt((tr_s = tr[curr].style).top) + move_sum, "px");
          if (this._gmode_edit) {
            rh[curr].style.top = newpos
          }
          tr_s.top = newpos
        }
      }
    }
    this._reconstruct();
    if (this._gmode_edit) {
      this._aPane._defaultSelection.draw(),
      this._aPane._formulaSelection.draw(),
      this._aPane._copySelection.draw()
    }
    var cr_size, pos = this._aPane._lastDestCell[type] + 1,
    sum = crs.getElemAt(pos),
    oc_size = type == this._SCROLL_HORIZ ? this._aPane._ocWidth: this._aPane._ocHeight;
    for (var max = oc_size; sum < max; sum += crs.getElemAt(++pos)) {}
    this._aPane._cppi[type] = pos - this._aPane._lastDestCell[type] - 1;
    this._aPane._cpp[type] = this._aPane._cppi[type] + ((cr_size = crs.getElemAt(pos)) - sum + oc_size) / cr_size;
    this._book._recalcSlider(type)
  },
  resizeColRow: function(type, ranges, newsize) {
    if ((type | 1) != 1) {
      return false
    }
    if (newsize < this._cellSizeIncr[type]) {
      newsize = 0
    }
    var range, chngsets = [],
    max = ranges.length,
    i = 0;
    for (; i < max; ++i) {
      range = ranges[i],
      chngsets.push([range[0], range[1], newsize])
    }
    this.setColRowSize(type, chngsets);
    if (ranges.length == 1 && ranges[0][0] == 1 && ranges[0][1] == this._defMaxCoords[type]) {
      this._conn.cmd(this._conn.dummy_cb, ["sdcr"], [type, newsize])
    } else {
      this._conn.cmd(this._conn.dummy_cb, ["scr", type], chngsets)
    }
  },
  autofitColRow: function(type, ranges) {
    this._conn.cmd([this._book, this._book.exec], ["afit", type], ranges, this._aPane.getRealGridRange(), !type, !!type, 0)
  },
  adjustRowHeights: function(row1, row2, contentHeight) {
    var ranges = [],
    range = undefined,
    rowHeight;
    for (var rh = this._rowHeights,
    row = row1; row <= row2; ++row) {
      rowHeight = rh.getElemAt(row);
      if (contentHeight == rowHeight) {
        continue
      }
      if (contentHeight > rowHeight) {
        if (range === undefined) {
          range = [row, row];
          continue
        }
        if (row == range[1] + 1) {
          range[1] = row;
          continue
        }
        ranges.push(range);
        range = [row, row]
      }
    }
    if (range !== undefined) {
      ranges.push(range)
    }
    if (ranges.length) {
      this.resizeColRow(1, ranges, contentHeight)
    }
  },
  newDims: function(type, dims_new) {
    var svec_old = this._colRowDims[type],
    dims_old = svec_old.getSparseArray(),
    idx_max = svec_old.getLen(),
    idx,
    idx_old = idx_max,
    idx_new = idx_max,
    val,
    val_old = 0,
    val_new = 0,
    next_new = true,
    next_old = true,
    cond = 2,
    sets = [],
    set;
    for (var i_old = 0,
    i_new = 0,
    end_old = dims_old.length,
    end_new = dims_new.length; cond;) {
      if (next_new) {
        if (i_new < end_new) {
          idx_new = dims_new[i_new++] + 1;
          val_new = dims_new[i_new++]
        } else {
          idx_new = idx_max;
          val_new = 0; --cond
        }
        next_new = false
      }
      if (next_old) {
        if (i_old < end_old) {
          idx_old = dims_old[i_old++];
          val_old = dims_old[i_old++]
        } else {
          idx_old = idx_max;
          val_old = 0; --cond
        }
        next_old = false
      }
      if (idx_new < idx_old) {
        next_new = true;
        idx = idx_new;
        val = val_new
      } else {
        if (idx_new > idx_old) {
          next_old = true;
          idx = idx_old;
          val = -1
        } else {
          next_new = true;
          next_old = true;
          if (val_new == val_old) {
            continue
          } else {
            idx = idx_new;
            val = val_new
          }
        }
      }
      if (!set) {
        set = [idx, idx, val]
      } else {
        if (idx == set[1] + 1) {++set[1];
          if (set.length > 3 || val != set[2]) {
            set.push(val)
          }
        } else {
          sets.push(set);
          set = [idx, idx, val]
        }
      }
    }
    if (set) {
      sets.push(set)
    }
    this.setColRowSize(type, sets)
  },
  recalc: function(cb) {
    this._aPane.recalc(cb)
  }
};