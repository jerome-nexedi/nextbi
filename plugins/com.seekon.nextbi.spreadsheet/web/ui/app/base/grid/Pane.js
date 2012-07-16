Jedox.wss.grid.Pane = function(cb, sheet, id, contentEl) {
	var that = this;
	this._inited = false;
	this._sheet = sheet;
	this._book = sheet._book;
	this._id = id;
	this._domId = sheet._domId.concat("P", id);
	this._contentEl = contentEl;
	this._conf = sheet._paneConf[id];
	this._winObj = sheet._winObj;
	this.scrollObserver = this._book.scrollObserver;
	this._viewMode = Jedox.wss.app.appMode;
	this._app = Jedox.wss.app;
	this._jwgrid = Jedox.wss.grid;
	this._jwstyle = Jedox.wss.style;
	this._jutil = Jedox.util;
	this._conn = Jedox.wss.backend.conn;
	this._ha = Jedox.wss.backend.ha;
	this._srl = this._jutil.serializer;
	this._n2l = this._app.numberToLetter;
	this._setText = this._jutil.setText;
	this._gmode_edit = sheet._gmode_edit;
	this._colWidths = sheet._colWidths;
	this._rowHeights = sheet._rowHeights;
	this._env = new Jedox.wss.cls.Environment();
	Jedox.wss.app.environment = this._env.shared;
	this._env.shared.viewMode = this._viewMode;
	var winPos = this._winObj.getPosition(), gridOffset = this._jwgrid.maxGridOffset;
	this._env.shared.gridScreenCoordsMax = [ winPos[0] + gridOffset[0],
			winPos[1] + gridOffset[1] ];
	this._app.activePane = this._book._aPane = sheet._aPane = this;
	var x = this._conf.coords.x, y = this._conf.coords.y;
	if (this._gmode_edit) {
		this._colHdrsOC = sheet["_colHdrsOC".concat(x)];
		this._colHdrsIC = sheet["_colHdrsIC".concat(x)];
		this._colHdrs = sheet["_colHdrs".concat(x)];
		this._rowHdrsOC = sheet["_rowHdrsOC".concat(y)];
		this._rowHdrsIC = sheet["_rowHdrsIC".concat(y)];
		this._rowHdrs = sheet["_rowHdrs".concat(y)]
	}
	this._numCols = sheet["_numCols".concat(x)];
	this._numRows = sheet["_numRows".concat(y)];
	this._tableSize = [ this._numCols, this._numRows ];
	this._maxCoords = [ this._conf.range[2], this._conf.range[3] ];
	this._tableMat = [];
	this._tableRows = [];
	this._colSeps = [];
	this._cppi = [];
	this._cpp = [];
	this._defcppi = [];
	this._defcpp = [];
	this._farthestSeenCell = [];
	this._loadTableCoef = this._jwgrid.defLoadTableCoef;
	this._currTopCell = [ 1, 1 ];
	this._oldCTC = [ 1, 1 ];
	this._lastDestCell = [ 0, 0 ];
	this._lastDoneDestCell = [ 0, 0 ];
	this._cellSizeIncr = this._jwgrid.defCellSizeIncr;
	this._ocWidth = this._conf.coords.w;
	this._ocHeight = this._conf.coords.h;
	this._storage = new Jedox.wss.grid.PaneStorage(this);
	this._cache = new Jedox.wss.grid.PaneCache(this);
	this._numericSeps = Jedox.wss.i18n.separators;
	this._reThouSep = new RegExp("\\".concat(this._numericSeps[1]), "g");
	this._l10nBool = Jedox.wss.i18n.bool;
	this._rePaloData = new RegExp("^=PALO.DATAC?\\((.*)\\)$");
	this._genContentEl();
	this._oc = contentEl;
	this._ic = document.getElementById(this._domId + "_IC");
	this._oc.scrollLeft = 0;
	this._oc.scrollTop = 0;
	var mouse = Jedox.wss.mouse;
	if (this._gmode_edit) {
		this._ic.onmouseover = mouse.overTracking
	} else {
		this._ic.ondblclick = mouse.mouseonCellDblClick
	}
	this._ic.onmousedown = mouse.mouseOnCellDown;
	for ( var row, tr = this._tableRows, tm = this._tableMat, tw = this._numCols, rows = document
			.getElementsByName(this._domId + "_gridRow"), i = this._numRows - 1; i >= 0; --i) {
		row = rows[i];
		tr[i] = row;
		tm[i] = [];
		for ( var tmi = tm[i], cells = row.getElementsByTagName("div"), j = tw - 1; j >= 0; --j) {
			tmi[j] = cells[j]
		}
	}
	this._firstRowCells = this._tableMat[0];
	this._env.shared.inputField = document.getElementById(this._domId
			+ "_inputField");
	if (this._gmode_edit) {
		for ( var cs = this._colSeps, seps = document
				.getElementsByName(this._domId + "_colSep"), i = this._numCols - 1; i >= 0; --i) {
			cs[i] = seps[i]
		}
		this._env.shared.defaultSelection = this._defaultSelection = new Jedox.wss.grid.DefaultSelection(
				new Jedox.wss.cls.Point(1, 1), this);
		this._env.shared.formulaSelection = this._formulaSelection = new Jedox.wss.grid.FormulaSelection(
				new Jedox.wss.cls.Point(1, 1), this);
		this._env.shared.copySelection = this._copySelection = new Jedox.wss.grid.CopySelection(
				new Jedox.wss.cls.Point(1, 1), this);
		this._env.shared.autoScroll = new Jedox.wss.cls.AutoScroll();
		this._ic.onselectstart = this._jutil.ignoreSelection;
		this._env.shared.inputField.onkeyup = Jedox.wss.keyboard.setFieldContent;
		this._env.shared.inputField.onselectstart = this._jutil.unignoreSelection;
		this._env.shared.inputField.onmousedown = function() {
			if (Jedox.wss.app.fromFormulaField) {
				that._env.shared.inputMode = that._jwgrid.GridMode.EDIT
			}
			that._formulaSelection.activeToken = null
		}
	} else {
		this._cursorField = new Jedox.wss.cls.CursorField(this._domId,
				this._ic, null);
		this._env.shared.inputField.onkeyup = Jedox.wss.keyboard.setFieldSize
	}
	this._env.shared.inputField.onfocus = function() {
		Jedox.wss.app.lastInputField = that._env.shared.inputField
	};
	this._fit();
	if (this._sheet._farthestUsedCell[0] > this._defcppi[0] + 2) {
		this._farthestSeenCell[0] = this._sheet._farthestUsedCell[0]
	} else {
		this._farthestSeenCell[0] = this._defcppi[0] + 2
	}
	if (this._sheet._farthestUsedCell[1] > this._defcppi[1] + 2) {
		this._farthestSeenCell[1] = this._sheet._farthestUsedCell[1]
	} else {
		this._farthestSeenCell[1] = this._defcppi[1] + 2
	}
	if (this._currTopCell[0] > this._sheet._farthestUsedCell[0]
			|| this._currTopCell[1] > this._sheet._farthestUsedCell[1]) {
		this._load_wsels(cb)
	} else {
		this._cache.load( [ this, this._load_wsels, cb ], this.getGridRng(), {
			frn : true
		})
	}
};
Jedox.wss.grid.Pane.prototype = {
	_SCROLL_BACK : -1,
	_SCROLL_FWD : 1,
	_load_wsels : function(cb) {
		this._conn.createBatch();
		Jedox.wss.general.createWorksheetElements();
		Jedox.wss.hb.loadAll();
		Jedox.wss.wsel.loadAll();
		Jedox.wss.wsel.img.loadAll();
		this._conn.sendBatch( [ this, this._init_post, cb ])
	},
	_init_post : function(res, cb) {
		if (this._gmode_edit) {
			this._defaultSelection.selectFirstCell();
			Jedox.wss.hb.syncCntrl(false);
			Jedox.wss.hb.enaDisHBAdd("enable");
			Jedox.wss.general.refreshCursorField()
		}
		this._book._sheetSelector.enable(true);
		this._inited = true;
		if (cb instanceof Array && cb.length > 1) {
			cb[1].apply(cb[0], cb.slice(2))
		}
	},
	_genContentEl : function() {
		var html = [ '<div id="', this._domId, '_IC" class="paneIC">' ];
		var cws = this._colWidths, rhs = this._rowHeights, wdecr = this._cellSizeIncr[0], hdecr = this._cellSizeIncr[1];
		if (this._gmode_edit) {
			for ( var c = this._conf.range[0], l = 0, i = this._numCols - 1; i >= 0; l += cws
					.getElemAt(c++), --i) {
				html.push('<div id="', this._domId, '_colSep" name="',
						this._domId,
						'_colSep" class="gridColSep" style="left:', l,
						'px;"></div>')
			}
		}
		for ( var t = 0, r = this._conf.range[1], rh = rhs.getElemAt(r), i = this._numRows - 1; i >= 0; t += rh, rh = rhs
				.getElemAt(++r), --i) {
			html.push('<div class="gridRow" id="', this._domId,
					'_gridRow" name="', this._domId, '_gridRow" style="top:',
					t, "px;", (rh ? "height:".concat(rh - hdecr, "px;")
							: "display:none;"), '">');
			for ( var l = 0, c = this._conf.range[0], cw = cws.getElemAt(c), j = this._numCols - 1; j >= 0; l += cw, cw = cws
					.getElemAt(++c), --j) {
				html.push('<div class="gridCell" style="left:', l, "px;",
						(cw ? "width:".concat(cw - wdecr, "px;")
								: "display:none;"), '"></div>')
			}
			html.push("</div>")
		}
		html.push('<textarea class="inputField default-format" id="',
				this._domId, '_inputField" name="', this._domId,
				'_inputField"></textarea><div class="HeaderMarker" id="',
				this._domId,
				'_startMarker"></div><div class="HeaderMarker" id="',
				this._domId, '_stopMarker"></div></div>');
		this._contentEl.innerHTML = html.join("")
	},
	_fit : function(cb) {
		this._env.shared.winStateMax = this._winObj.maximized;
		this._ocWidth = this._conf.coords.w;
		this._ocHeight = this._conf.coords.h;
		var pos = this._lastDestCell[0] + 1, sum = this._colWidths
				.getElemAt(pos), els;
		for ( var max = this._ocWidth, cw = this._colWidths; sum <= max; sum += cw
				.getElemAt(++pos)) {
		}
		this._cppi[0] = pos - this._lastDestCell[0] - 1;
		this._cpp[0] = this._cppi[0]
				+ ((els = this._colWidths.getElemAt(pos)) - sum + this._ocWidth)
				/ els;
		this._defcpp[0] = this._ocWidth / this._sheet._defColRowDims[0];
		this._defcppi[0] = parseInt(this._defcpp[0]);
		pos = this._lastDestCell[1] + 1, sum = this._rowHeights.getElemAt(pos);
		for ( var max = this._ocHeight, rh = this._rowHeights; sum <= max; sum += rh
				.getElemAt(++pos)) {
		}
		this._cppi[1] = pos - this._lastDestCell[1] - 1;
		this._cpp[1] = this._cppi[1]
				+ ((els = this._rowHeights.getElemAt(pos)) - sum + this._ocHeight)
				/ els;
		this._defcpp[1] = this._ocHeight / this._sheet._defColRowDims[1];
		this._defcppi[1] = parseInt(this._defcpp[1]);
		if (!this._inited) {
			return
		}
		var newDefFUC, lDCcppi;
		if (this._farthestSeenCell[0] < (newDefFUC = this._defcppi[0] + 2)) {
			this._farthestSeenCell[0] = newDefFUC
		} else {
			if ((lDCcppi = this._lastDestCell[0] + this._cppi[0]) >= this._sheet._farthestUsedCell[0]
					&& lDCcppi >= this._defcppi[0] + 2) {
				this._farthestSeenCell[0] = lDCcppi
			}
		}
		this._book._recalcSlider(0);
		if (this._farthestSeenCell[1] < (newDefFUC = this._defcppi[1] + 2)) {
			this._farthestSeenCell[1] = newDefFUC
		} else {
			if ((lDCcppi = this._lastDestCell[1] + this._cppi[1]) >= this._sheet._farthestUsedCell[1]
					&& lDCcppi >= this._defcppi[1] + 2) {
				this._farthestSeenCell[1] = lDCcppi
			}
		}
		this._book._recalcSlider(1);
		if (cb instanceof Array && cb.length > 1) {
			cb[1].apply(cb[0], cb.slice(2))
		}
	},
	_reconstruct : function(cb, cDiff, rDiff, inScroll) {
		if (cDiff > 0) {
			var lastCol = this._currTopCell[0] + this._numCols - 1, dfCells = document
					.createDocumentFragment(), decr = this._cellSizeIncr[0], lastCell = this._firstRowCells[this._numCols - 1], lastStoCell = this._srl
					.enc(this._storage._tbl[0][this._numCols - 1]), cEdge = parseInt(lastCell.style.left)
					+ this._colWidths.getElemAt(lastCol), c = lastCol, i = cDiff, cell, w, wpx, lpx, disp;
			if (this._gmode_edit) {
				var dfSeps = document.createDocumentFragment(), lastSep = this._colSeps[this._numCols - 1], sep;
				if (inScroll) {
					var dfHdrs = document.createDocumentFragment(), lastHdr = this._colHdrs[this._numCols - 1], hdr
				}
			} else {
				if (inScroll) {
					inScroll = false
				}
			}
			do {
				cell = lastCell.cloneNode(true);
				cell.style.left = lpx = "".concat(cEdge, "px");
				w = this._colWidths.getElemAt(++c);
				disp = false;
				if (w) {
					cell.style.width = wpx = "".concat(w - decr, "px");
					if (cell.style.display) {
						cell.style.display = disp = ""
					}
				} else {
					if (!cell.style.display) {
						cell.style.display = disp = "none"
					}
				}
				dfCells.appendChild(cell);
				if (this._gmode_edit) {
					sep = lastSep.cloneNode(true);
					sep.style.left = lpx;
					dfSeps.appendChild(sep);
					this._colSeps.push(sep);
					if (inScroll) {
						hdr = lastHdr.cloneNode(true);
						this._setText(hdr, this._n2l[c]);
						hdr.style.left = lpx;
						if (w) {
							hdr.style.width = wpx
						}
						if (disp !== false) {
							hdr.style.display = disp
						}
						dfHdrs.appendChild(hdr);
						this._colHdrs.push(hdr)
					}
				}
				cEdge += w
			} while (--i);
			if (this._gmode_edit) {
				this._ic.appendChild(dfSeps)
			}
			var cells, matRow, stoRow, j;
			i = this._numRows - 1;
			do {
				cells = dfCells.cloneNode(true);
				matRow = this._tableMat[i];
				stoRow = this._storage._tbl[i];
				j = 0;
				do {
					matRow.push(cells.childNodes[j]);
					stoRow.push(this._srl.dec(lastStoCell))
				} while (++j < cDiff);
				this._tableRows[i].appendChild(cells)
			} while (--i >= 0);
			this._tableSize[0] = (this._numCols += cDiff);
			if (inScroll) {
				this._colHdrsIC.appendChild(dfHdrs);
				this._sheet["_numCols".concat(this._conf.coords.x)] = this._numCols
			}
			this._cache.load(null, this._jutil.reg2rng( [ lastCol + 1,
					this._currTopCell[1] ], cDiff, this._numRows), {
				frn : true,
				rfr : true
			}, true)
		}
		if (rDiff > 0) {
			var lastRow = this._currTopCell[1] + this._numRows - 1, dfTblRows = document
					.createDocumentFragment(), decr = this._cellSizeIncr[1], lastTblRow = this._tableRows[this._numRows - 1], lastStoRow = this._srl
					.enc(this._storage._tbl[this._numRows - 1]), rEdge = parseInt(lastTblRow.style.top)
					+ this._rowHeights.getElemAt(lastRow), numCols = this._numCols, r = lastRow, i = rDiff, tblRow, h, firstCell, matRow, off, j, hpx, tpx, disp;
			if (this._gmode_edit) {
				if (inScroll) {
					var dfHdrs = document.createDocumentFragment(), lastHdr = this._rowHdrs[this._numRows - 1], hdr
				}
			} else {
				if (inScroll) {
					inScroll = false
				}
			}
			do {
				tblRow = lastTblRow.cloneNode(true);
				tblRow.style.top = tpx = "".concat(rEdge, "px");
				h = this._rowHeights.getElemAt(++r);
				disp = false;
				if (h) {
					tblRow.style.height = hpx = "".concat(h - decr, "px");
					if (tblRow.style.display) {
						tblRow.style.display = disp = ""
					}
				} else {
					if (!tblRow.style.display) {
						tblRow.style.display = disp = "none"
					}
				}
				dfTblRows.appendChild(tblRow);
				this._tableRows.push(tblRow);
				firstCell = this._tableMat[this._numRows - 1][0];
				off = -1;
				while (firstCell != lastTblRow.childNodes[++off]) {
				}
				matRow = [];
				j = off;
				do {
					matRow.push(tblRow.childNodes[j])
				} while (++j < numCols);
				j = 0;
				do {
					matRow.push(tblRow.childNodes[j])
				} while (++j < off);
				this._tableMat.push(matRow);
				this._storage._tbl.push(this._srl.dec(lastStoRow));
				if (inScroll) {
					hdr = lastHdr.cloneNode(true);
					this._setText(hdr, r);
					hdr.style.top = tpx;
					if (h) {
						hdr.style.height = hpx
					}
					if (disp !== false) {
						hdr.style.display = disp
					}
					dfHdrs.appendChild(hdr);
					this._rowHdrs.push(hdr)
				}
				rEdge += h
			} while (--i);
			this._ic.appendChild(dfTblRows);
			this._tableSize[1] = (this._numRows += rDiff);
			if (inScroll) {
				this._rowHdrsIC.appendChild(dfHdrs);
				this._sheet["_numRows".concat(this._conf.coords.y)] = this._numRows
			}
			this._cache.load(null, this._jutil.reg2rng( [ this._currTopCell[0],
					lastRow + 1 ], this._numCols, rDiff), {
				frn : true,
				rfr : true
			}, true)
		}
		this._fit(cb)
	},
	_loadTable : function(doHoriz, doVert, fillCells, doClean) {
		if (doVert) {
			for ( var hdr, ndh, tr, ntpx, rhd = this._rowHdrs, rhs = this._rowHeights, trs = this._tableRows, rhi = this._cellSizeIncr[1], ctc = this._currTopCell[1], i = this._numRows - 1, ctci = ctc
					+ i, nt = rhs.getSumUpTo(ctci), rh = rhs.getElemAt(ctci); i >= 0; --i, nt -= (rh = rhs
					.getElemAt(--ctci))) {
				ntpx = "".concat(nt, "px");
				tr = trs[i];
				if (rh) {
					if (tr.style.display != "") {
						ndh = ";height:".concat(rh - rhi, "px;display:;")
					} else {
						if ((rh -= rhi) != parseInt(tr.style.height)) {
							ndh = ";height:".concat(rh, "px;")
						} else {
							ndh = ""
						}
					}
				} else {
					ndh = tr.style.display != "none" ? ";display:none;" : ""
				}
				if (ndh.length) {
					tr.style.cssText = "top:".concat(ntpx, ndh)
				} else {
					tr.style.top = ntpx
				}
				if (this._gmode_edit) {
					hdr = rhd[i];
					if (ndh.length) {
						hdr.style.cssText = "top:".concat(ntpx, ndh)
					} else {
						hdr.style.top = ntpx
					}
					this._setText(hdr, ctci)
				}
			}
		}
		if (doHoriz || fillCells) {
			for ( var hdr, nlpx, ndw, st, ch = this._colHdrs, cs = this._colSeps, ntl = this._n2l, cws = this._colWidths, rhs = this._rowHeights, cwi = this._cellSizeIncr[0], rhi = this._cellSizeIncr[1], frc = this._firstRowCells, tm = this._tableMat, th = this._numRows, decSep = this._numericSeps[0], l10nBool = this._l10nBool, ctc = this._currTopCell, j = this._numCols - 1, ctcj = ctc[0]
					+ j, nl = cws.getSumUpTo(ctcj), cw = cws.getElemAt(ctcj), dta = this._jwstyle.defTextAlign; j >= 0; --j, nl -= (cw = cws
					.getElemAt(--ctcj))) {
				nlpx = "".concat(nl, "px");
				if (cw) {
					st = frc[j].style;
					if (st.display != "") {
						ndw = true, cw = ";width:".concat(cw - cwi,
								"px;display:;")
					} else {
						ndw = (cw -= cwi) != parseInt(st.width), cw = ";width:"
								.concat(cw, "px;")
					}
				} else {
					ndw = frc[j].style.display != "none", cw = ";display:none;"
				}
				for ( var newc, oldc, cell, i = th - 1, ctci = ctc[1] + i; i >= 0; --i) {
					newc = this._cache.get(ctcj, ctci--);
					oldc = this._storage.get(j, i);
					cell = tm[i][j];
					if (newc != undefined && "s" in newc) {
						cell.style.cssText = "left:".concat(nlpx, cw, newc.s);
						if (cell.style.borderTopWidth == "2px") {
							cell.style.width = "".concat(cws.getElemAt(ctcj)
									- cwi - 1, "px")
						}
						if (cell.style.borderLeftWidth == "2px") {
							cell.style.height = "".concat(rhs
									.getElemAt(ctci + 1)
									- rhi - 1, "px")
						}
					} else {
						if (ndw || (oldc != undefined && "s" in oldc)
								|| doClean) {
							cell.style.cssText = "left:".concat(nlpx, cw)
						} else {
							if (doHoriz) {
								cell.style.left = nlpx
							}
						}
					}
					if (newc != undefined && "m" in newc && newc.m[0] == true) {
						if (cell.className.indexOf("mergedCell") == -1) {
							cell.className += " mergedCell"
						}
						for ( var sum = 0, max = newc.m[2], k = 0; k < max; sum += cws
								.getElemAt(ctcj + k++)) {
						}
						cell.style.width = "".concat(sum - cwi, "px");
						for ( var sum = 0, max = newc.m[1], k = 0; k < max; sum += rhs
								.getElemAt(ctci + ++k)) {
						}
						cell.style.height = "".concat(sum - rhi, "px")
					} else {
						if (cell.className.indexOf("mergedCell") != -1) {
							cell.className = "gridCell";
							cell.style.width = "".concat(cws.getElemAt(ctcj)
									- cwi, "px");
							cell.style.height = "".concat(rhs
									.getElemAt(ctci + 1)
									- rhi, "px")
						}
					}
					if (newc != undefined && "m" in newc && newc.m[0] == false) {
						cell.style.visibility = "hidden"
					} else {
						if (cell.style.visibility == "hidden") {
							cell.style.visibility = ""
						}
					}
					if (newc != undefined && "v" in newc) {
						switch (typeof newc.v) {
						case "number":
							newc.v = decSep == "." ? "" + newc.v
									: ("" + newc.v).replace(".", decSep);
							break;
						case "boolean":
							newc.v = l10nBool[newc.v];
							break
						}
						if (newc.t == "h") {
							cell.innerHTML = newc.v
						} else {
							this._setText(cell, newc.l || newc.v)
						}
						if ("t" in newc && cell.style.textAlign == ""
								&& dta[newc.t] != "") {
							cell.style.textAlign = dta[newc.t]
						}
					} else {
						if ((oldc != undefined && "v" in oldc) || doClean) {
							this._setText(cell, "")
						}
					}
					if (!this._gmode_edit) {
						if (newc != undefined && "k" in newc
								&& newc.k === false) {
							cell.className = "gridCell gridCellUnlocked"
						} else {
							if ((oldc != undefined && "k" in oldc && oldc.k === false)
									|| doClean) {
								cell.className = "gridCell"
							}
						}
					}
					this._storage.set(j, i, newc)
				}
				if (doHoriz && this._gmode_edit) {
					hdr = ch[j];
					if (ndw) {
						hdr.style.cssText = "left:".concat(nlpx, cw)
					} else {
						hdr.style.left = nlpx
					}
					this._setText(hdr, ntl[ctcj]);
					cs[j].style.left = nlpx
				}
			}
		}
	},
	_calcScroll : function(params, amount) {
		var type = params.type;
		if (params.dir == this._SCROLL_BACK) {
			var fincoord = this._currTopCell[type] - amount;
			if (fincoord < 1) {
				amount += fincoord - 1;
				fincoord = 1
			}
			var newcoord = this._currTopCell[type], oldcoord = this._currTopCell[type]
					+ this._tableSize[type], s_amount = -amount
		} else {
			if (params.dir == this._SCROLL_FWD) {
				var fincoord = this._currTopCell[type] + this._tableSize[type]
						- 1 + amount;
				if (fincoord > this._maxCoords[type]) {
					amount -= fincoord - this._maxCoords[type]
				} else {
					if (fincoord > this._farthestSeenCell[type] + 1) {
						amount -= fincoord - this._farthestSeenCell[type] - 1
					}
				}
				fincoord = this._currTopCell[type] + this._tableSize[type];
				var newcoord = fincoord - 1, oldcoord = this._currTopCell[type] - 1, s_amount = amount
			} else {
				return false
			}
		}
		if (amount > 0) {
			params.amount = amount;
			params.s_amount = s_amount;
			params.fincoord = fincoord;
			params.newcoord = newcoord;
			params.oldcoord = oldcoord
		}
		return true
	},
	_scrollTableX : function(dir, amount, newcoord, oldcoord) {
		var newwidth, oldwidth, newleft, hdr, sep, wdiff, newx, cw = this._colWidths, rh = this._rowHeights, tw = this._numCols, th = this._numRows, csi = this._cellSizeIncr[0], rhi = this._cellSizeIncr[1], frc = this._firstRowCells, ch = this._colHdrs, cs = this._colSeps, ntl = this._n2l, decSep = this._numericSeps[0], l10nBool = this._l10nBool, tm = this._tableMat, st = this._storage._tbl, dta = this._jwstyle.defTextAlign, ctc = this._currTopCell[1];
		for ( var i = amount; i > 0; --i) {
			if (dir == this._SCROLL_BACK) {
				--newcoord;
				--oldcoord;
				newx = 0;
				oldwidth = cw.getElemAt(oldcoord);
				newwidth = cw.getElemAt(newcoord);
				newleft = "".concat(parseInt(frc[0].style.left) - newwidth,
						"px");
				if (this._gmode_edit) {
					ch.unshift(hdr = ch.pop()), cs.unshift(sep = cs.pop())
				}
			} else {
				++newcoord;
				++oldcoord;
				newx = tw - 1;
				oldwidth = cw.getElemAt(oldcoord);
				newwidth = cw.getElemAt(newcoord);
				newleft = "".concat(parseInt(frc[tw - 1].style.left)
						+ cw.getElemAt(newcoord - 1), "px");
				if (this._gmode_edit) {
					ch.push(hdr = ch.shift()), cs.push(sep = cs.shift())
				}
			}
			if (newwidth != oldwidth) {
				wdiff = true;
				if (newwidth) {
					newwidth = ";width:".concat(newwidth - csi, "px;",
							(oldwidth ? "" : "display:;"))
				} else {
					newwidth = ";display:none;"
				}
			} else {
				if (newwidth) {
					wdiff = false, newwidth = ";width:".concat(newwidth - csi,
							"px;")
				} else {
					wdiff = false, newwidth = ";display:none;"
				}
			}
			if (this._gmode_edit) {
				if (wdiff) {
					hdr.style.cssText = "left:".concat(newleft, newwidth)
				} else {
					hdr.style.left = newleft
				}
				this._setText(hdr, ntl[newcoord]);
				sep.style.left = newleft
			}
			for ( var row, srow, cell, newc, oldc, j = th - 1, ctcj = ctc + j; j >= 0; --j, --ctcj) {
				row = tm[j];
				srow = st[j];
				if (dir == this._SCROLL_BACK) {
					row.unshift(cell = row.pop());
					srow.unshift(oldc = srow.pop())
				} else {
					row.push(cell = row.shift());
					srow.push(oldc = srow.shift())
				}
				newc = this._cache.get(newcoord, ctcj);
				if (newc != undefined && "s" in newc) {
					cell.style.cssText = "left:".concat(newleft, newwidth,
							newc.s);
					if (cell.style.borderTopWidth == "2px") {
						cell.style.width = "".concat(cw.getElemAt(newcoord)
								- csi - 1, "px")
					}
					if (cell.style.borderLeftWidth == "2px") {
						cell.style.height = "".concat(rh.getElemAt(ctcj) - rhi
								- 1, "px")
					}
				} else {
					if (wdiff || (oldc != undefined && "s" in oldc)) {
						cell.style.cssText = "left:".concat(newleft, newwidth)
					} else {
						cell.style.left = newleft
					}
				}
				if (newc != undefined && "m" in newc && newc.m[0] == true) {
					if (cell.className.indexOf("mergedCell") == -1) {
						cell.className += " mergedCell"
					}
					for ( var sum = 0, max = newc.m[2], k = 0; k < max; sum += cw
							.getElemAt(newcoord + k++)) {
					}
					cell.style.width = "".concat(sum - csi, "px");
					for ( var sum = 0, max = newc.m[1], k = 0; k < max; sum += rh
							.getElemAt(ctcj + k++)) {
					}
					cell.style.height = "".concat(sum - rhi, "px")
				} else {
					if (cell.className.indexOf("mergedCell") != -1) {
						cell.className = "gridCell";
						cell.style.width = "".concat(cw.getElemAt(newcoord)
								- csi, "px");
						cell.style.height = "".concat(rh.getElemAt(ctcj) - rhi,
								"px")
					}
				}
				if (newc != undefined && "m" in newc && newc.m[0] == false) {
					cell.style.visibility = "hidden"
				} else {
					if (cell.style.visibility == "hidden") {
						cell.style.visibility = ""
					}
				}
				if (newc != undefined && "v" in newc) {
					switch (typeof newc.v) {
					case "number":
						newc.v = decSep == "." ? "" + newc.v : ("" + newc.v)
								.replace(".", decSep);
						break;
					case "boolean":
						newc.v = l10nBool[newc.v];
						break
					}
					if (newc.t == "h") {
						cell.innerHTML = newc.v
					} else {
						this._setText(cell, newc.l || newc.v)
					}
					if ("t" in newc && cell.style.textAlign == ""
							&& dta[newc.t] != "") {
						cell.style.textAlign = dta[newc.t]
					}
				} else {
					if (oldc != undefined && "v" in oldc) {
						this._setText(cell, "")
					}
				}
				if (!this._gmode_edit) {
					if (newc != undefined && "k" in newc && newc.k === false) {
						cell.className = "gridCell gridCellUnlocked"
					} else {
						if (oldc != undefined && "k" in oldc
								&& oldc.k === false) {
							cell.className = "gridCell"
						}
					}
				}
				srow[newx] = newc
			}
		}
	},
	_scrollTableY : function(dir, amount, newcoord, oldcoord) {
		var newheight, oldheight, newtop, row, srow, tblrow, hdr, ndh, rh = this._rowHeights, cw = this._colWidths, csi = this._cellSizeIncr[1], cwi = this._cellSizeIncr[0], tr = this._tableRows, tm = this._tableMat, st = this._storage._tbl, rhd = this._rowHdrs, th = this._numRows, tw = this._numCols, l10nBool = this._l10nBool, decSep = this._numericSeps[0], dta = this._jwstyle.defTextAlign, ctc = this._currTopCell[0];
		for ( var i = amount; i > 0; --i) {
			if (dir == this._SCROLL_BACK) {
				--newcoord;
				--oldcoord;
				oldheight = rh.getElemAt(oldcoord);
				newheight = rh.getElemAt(newcoord);
				newtop = parseInt(tr[0].style.top) - newheight;
				tm.unshift(row = tm.pop());
				st.unshift(srow = st.pop());
				tr.unshift(tblrow = tr.pop());
				if (this._gmode_edit) {
					rhd.unshift(hdr = rhd.pop())
				}
			} else {
				++newcoord;
				++oldcoord;
				oldheight = rh.getElemAt(oldcoord);
				newheight = rh.getElemAt(newcoord);
				newtop = parseInt(tr[th - 1].style.top)
						+ rh.getElemAt(newcoord - 1);
				tm.push(row = tm.shift());
				st.push(srow = st.shift());
				tr.push(tblrow = tr.shift());
				if (this._gmode_edit) {
					rhd.push(hdr = rhd.shift())
				}
			}
			if (newheight != oldheight) {
				ndh = true;
				if (newheight) {
					var csstxt = "top:".concat(newtop, "px;height:", newheight
							- csi, "px;", (oldheight ? "" : "display:;"))
				} else {
					var csstxt = "top:".concat(newtop, "px;display:none;")
				}
				tblrow.style.cssText = csstxt
			} else {
				ndh = false, tblrow.style.top = newtop = ""
						.concat(newtop, "px")
			}
			if (this._gmode_edit) {
				if (ndh) {
					hdr.style.cssText = csstxt
				} else {
					hdr.style.top = newtop
				}
				this._setText(hdr, newcoord)
			}
			for ( var cell, newc, oldc, j = tw - 1, ctcj = ctc + j; j >= 0; --j, --ctcj) {
				cell = row[j];
				newc = this._cache.get(ctcj, newcoord);
				oldc = srow[j];
				if (newc != undefined && "s" in newc) {
					cell.style.cssText = "left:".concat(cell.style.left,
							";width:", cell.style.width, ";display:",
							cell.style.display, ";", newc.s);
					if (cell.style.borderTopWidth == "2px") {
						cell.style.width = "".concat(cw.getElemAt(ctcj) - cwi
								- 1, "px")
					}
					if (cell.style.borderLeftWidth == "2px") {
						cell.style.height = "".concat(rh.getElemAt(newcoord)
								- csi - 1, "px")
					}
				} else {
					if (oldc != undefined && "s" in oldc) {
						cell.style.cssText = "left:".concat(cell.style.left,
								";width:", cell.style.width, ";display:",
								cell.style.display, ";")
					}
				}
				if (newc != undefined && "m" in newc && newc.m[0] == true) {
					if (cell.className.indexOf("mergedCell") == -1) {
						cell.className += " mergedCell"
					}
					for ( var sum = 0, max = newc.m[2], k = 0; k < max; sum += cw
							.getElemAt(ctcj + k++)) {
					}
					cell.style.width = "".concat(sum - cwi, "px");
					for ( var sum = 0, max = newc.m[1], k = 0; k < max; sum += rh
							.getElemAt(newcoord + k++)) {
					}
					cell.style.height = "".concat(sum - csi, "px")
				} else {
					if (cell.className.indexOf("mergedCell") != -1) {
						cell.className = "gridCell";
						cell.style.width = "".concat(cw.getElemAt(ctcj) - cwi,
								"px");
						cell.style.height = "".concat(rh.getElemAt(newcoord)
								- csi, "px")
					}
				}
				if (newc != undefined && "m" in newc && newc.m[0] == false) {
					cell.style.visibility = "hidden"
				} else {
					if (cell.style.visibility == "hidden") {
						cell.style.visibility = ""
					}
				}
				if (newc != undefined && "v" in newc) {
					switch (typeof newc.v) {
					case "number":
						newc.v = decSep == "." ? "" + newc.v : ("" + newc.v)
								.replace(".", decSep);
						break;
					case "boolean":
						newc.v = l10nBool[newc.v];
						break
					}
					if (newc.t == "h") {
						cell.innerHTML = newc.v
					} else {
						this._setText(cell, newc.l || newc.v)
					}
					if ("t" in newc && cell.style.textAlign == ""
							&& dta[newc.t] != "") {
						cell.style.textAlign = dta[newc.t]
					}
				} else {
					if (oldc != undefined && "v" in oldc) {
						this._setText(cell, "")
					}
				}
				if (!this._gmode_edit) {
					if (newc != undefined && "k" in newc && newc.k === false) {
						cell.className = "gridCell gridCellUnlocked"
					} else {
						if (oldc != undefined && "k" in oldc
								&& oldc.k === false) {
							cell.className = "gridCell"
						}
					}
				}
				srow[j] = newc
			}
		}
	},
	_scrollGridX : function(offset, destCellAbs, cb) {
		if (destCellAbs == undefined) {
			destCellAbs = Math.round(offset / this._book._sliderTicks[0])
		}
		if (destCellAbs == this._lastDestCell[0]) {
			return
		}
		this._lastDestCell[0] = destCellAbs;
		var dir = destCellAbs < this._lastDoneDestCell[0] ? this._SCROLL_BACK
				: this._SCROLL_FWD, destCellRel = destCellAbs
				- (this._currTopCell[0] - 1);
		var colw, pos = destCellAbs + 1, sum = this._colWidths.getElemAt(pos);
		for ( var max = this._ocWidth, cw = this._colWidths; sum < max; sum += cw
				.getElemAt(++pos)) {
		}
		var cppi = pos - destCellAbs - 1, cpp = cppi
				+ ((colw = this._colWidths.getElemAt(pos)) - sum + this._ocWidth)
				/ colw;
		if (destCellAbs + cppi > this._farthestSeenCell[0]
				&& (this._farthestSeenCell[0] = destCellAbs + cppi) > this._maxCoords[0]) {
			this._farthestSeenCell[0] = this._maxCoords[0]
		}
		if (cppi + 3 > this._numCols) {
			this._reconstruct(null, cppi + 3 - this._numCols, 0, true)
		}
		var scrollParams = {
			type : 0,
			dir : dir,
			r_dir : dir,
			amount : 0
		}, gridParams = {
			dca : destCellAbs,
			cppi : cppi,
			cpp : cppi
		};
		if (Math.abs(destCellRel) + 1 >= this._numCols * this._loadTableCoef) {
			var ctc = destCellAbs;
			destCellRel = 1;
			var gridEdge = ctc + this._numCols - 1;
			if (ctc < 1) {
				destCellRel -= -ctc + 1;
				ctc = 1
			} else {
				if (gridEdge > this._farthestSeenCell[0]) {
					if (gridEdge > this._maxCoords[0]) {
						var corr = gridEdge - this._maxCoords[0]
					} else {
						var corr = gridEdge - this._farthestSeenCell[0] - 1
					}
					destCellRel += corr;
					ctc -= corr
				}
			}
			gridParams.ctc = ctc;
			gridParams.dcr = destCellRel;
			var loadRng = this._jutil.reg2rng( [ ctc, this._currTopCell[1] ],
					this._numCols, this._numRows);
			this._jutil.rngCap(loadRng, this._sheet._farthestUsedCell);
			if (ctc > this._sheet._farthestUsedCell[0]
					|| this._currTopCell[1] > this._sheet._farthestUsedCell[1]
					|| !this._cache.miss(loadRng)) {
				return this._scrollGridX_do(true, gridParams, scrollParams, cb)
			}
			this._cache.abort();
			clearTimeout(this._book._tid_scrollGrid);
			var that = this;
			this._book._tid_scrollGrid = setTimeout(function() {
				that._cache.load( [ that, that._scrollGridX_do, true,
						gridParams, scrollParams, cb ], loadRng, {
					sdr : 1 + dir
				})
			}, 200);
			this._book._scrollPending = true;
			return
		}
		if (destCellRel >= 0) {
			if (destCellRel + cppi < this._numCols) {
				var rremaining = this._numCols - destCellRel - cppi;
				if (rremaining < 1) {
					this._calcScroll(scrollParams, 1 - rremaining);
					destCellRel -= scrollParams.amount
				} else {
					var lmissing = 1 - destCellRel;
					if (lmissing > 0 && this._currTopCell[0] - lmissing > 0) {
						scrollParams.dir = this._SCROLL_BACK;
						this._calcScroll(scrollParams, lmissing);
						destCellRel += scrollParams.amount
					}
				}
			} else {
				this._calcScroll(scrollParams, destCellRel + cppi
						- this._numCols + 2);
				destCellRel -= scrollParams.amount
			}
		} else {
			this._calcScroll(scrollParams, -destCellRel + 1);
			destCellRel += scrollParams.amount
		}
		gridParams.dcr = destCellRel;
		var loadRng = this._jutil.reg2rng( [ scrollParams.fincoord,
				this._currTopCell[1] ], scrollParams.amount, this._numRows);
		this._jutil.rngCap(loadRng, this._sheet._farthestUsedCell);
		if (!scrollParams.amount
				|| scrollParams.fincoord > this._sheet._farthestUsedCell[0]
				|| this._currTopCell[1] > this._sheet._farthestUsedCell[1]
				|| !this._cache.miss(loadRng)) {
			return this._scrollGridX_do(false, gridParams, scrollParams, cb)
		}
		this._cache.abort();
		clearTimeout(this._book._tid_scrollGrid);
		var that = this;
		this._book._tid_scrollGrid = setTimeout(function() {
			that._cache.load( [ that, that._scrollGridX_do, false, gridParams,
					scrollParams, cb ], loadRng, {
				sdr : 1 + dir
			})
		}, 200);
		this._book._scrollPending = true
	},
	_scrollGridX_do : function(mode, gridParams, scrollParams, cb) {
		this._lastDoneDestCell[0] = gridParams.dca;
		this._cppi[0] = gridParams.cppi;
		this._cpp[0] = gridParams.cpp;
		if (mode) {
			this._oldCTC[0] = this._currTopCell[0];
			this._currTopCell[0] = gridParams.ctc;
			this._loadTable(true, false, true, false);
			var newleft = this._colWidths.getSumUpTo(this._currTopCell[0]
					+ gridParams.dcr)
		} else {
			if (scrollParams.amount) {
				this._scrollTableX(scrollParams.dir, scrollParams.amount,
						scrollParams.newcoord, scrollParams.oldcoord),
						this._currTopCell[0] += scrollParams.s_amount
			}
			var newleft = parseInt(this._firstRowCells[gridParams.dcr].style.left)
		}
		this._oc.scrollLeft = newleft;
		if (this._gmode_edit) {
			this._colHdrsOC.scrollLeft = newleft
		}
		if (this._book._doCheckSlider) {
			this._book._checkSlider(0)
		}
		this._book._scrollPending = false;
		this._book.scrollObserver.notify(this, this, 0, scrollParams.r_dir,
				this._lastDestCell[0] + 1, this._lastDestCell[0]
						+ this._cppi[0], this._book._currScrollSpeeds[0]);
		if (cb instanceof Array && cb.length > 1) {
			cb[1].apply(cb[0], cb.slice(2))
		}
	},
	_scrollGridY : function(offset, destCellAbs, cb) {
		if (destCellAbs == undefined) {
			destCellAbs = parseInt(offset / this._book._sliderTicks[1])
		}
		if (destCellAbs == this._lastDestCell[1]) {
			return
		}
		this._lastDestCell[1] = destCellAbs;
		var dir = destCellAbs < this._lastDoneDestCell[1] ? this._SCROLL_BACK
				: this._SCROLL_FWD, destCellRel = destCellAbs
				- (this._currTopCell[1] - 1);
		var rowh, pos = destCellAbs + 1, sum = this._rowHeights.getElemAt(pos);
		for ( var max = this._ocHeight, rh = this._rowHeights; sum < max; sum += rh
				.getElemAt(++pos)) {
		}
		var cppi = pos - destCellAbs - 1;
		cpp = cppi
				+ ((rowh = this._rowHeights.getElemAt(pos)) - sum + this._ocHeight)
				/ rowh;
		if (destCellAbs + cppi > this._farthestSeenCell[1]
				&& (this._farthestSeenCell[1] = destCellAbs + cppi) > this._maxCoords[1]) {
			this._farthestSeenCell[1] = this._maxCoords[1]
		}
		if (cppi + 3 > this._numRows) {
			this._reconstruct(null, 0, cppi + 3 - this._numRows, true)
		}
		var scrollParams = {
			type : 1,
			dir : dir,
			r_dir : dir,
			amount : 0
		}, gridParams = {
			dca : destCellAbs,
			cppi : cppi,
			cpp : cppi
		};
		if (Math.abs(destCellRel) + 1 >= this._numRows * this._loadTableCoef) {
			var ctc = destCellAbs;
			destCellRel = 1;
			var gridEdge = ctc + this._numRows - 1;
			if (ctc < 1) {
				destCellRel -= -ctc + 1;
				ctc = 1
			} else {
				if (gridEdge > this._farthestSeenCell[1]) {
					if (gridEdge > this._maxCoords[1]) {
						var corr = gridEdge - this._maxCoords[1]
					} else {
						var corr = gridEdge - this._farthestSeenCell[1] - 1
					}
					destCellRel += corr;
					ctc -= corr
				}
			}
			gridParams.ctc = ctc;
			gridParams.dcr = destCellRel;
			var loadRng = this._jutil.reg2rng( [ this._currTopCell[0], ctc ],
					this._numCols, this._numRows);
			this._jutil.rngCap(loadRng, this._sheet._farthestUsedCell);
			if (ctc > this._sheet._farthestUsedCell[1]
					|| this._currTopCell[0] > this._sheet._farthestUsedCell[0]
					|| !this._cache.miss(loadRng)) {
				return this._scrollGridY_do(true, gridParams, scrollParams, cb)
			}
			this._cache.abort();
			clearTimeout(this._book._tid_scrollGrid);
			var that = this;
			this._book._tid_scrollGrid = setTimeout(function() {
				that._cache.load( [ that, that._scrollGridY_do, true,
						gridParams, scrollParams, cb ], loadRng, {
					sdr : 2 + dir
				})
			}, 200);
			this._book._scrollPending = true;
			return
		}
		if (destCellRel >= 0) {
			if (destCellRel + cppi < this._numRows) {
				var dremaining = this._numRows - destCellRel - cppi;
				if (dremaining < 1) {
					this._calcScroll(scrollParams, 1 - dremaining);
					destCellRel -= scrollParams.amount
				} else {
					var umissing = 1 - destCellRel;
					if (umissing > 0 && this._currTopCell[1] - umissing > 0) {
						scrollParams.dir = this._SCROLL_BACK;
						this._calcScroll(scrollParams, umissing);
						destCellRel += scrollParams.amount
					}
				}
			} else {
				this._calcScroll(scrollParams, destCellRel + cppi
						- this._numRows + 2);
				destCellRel -= scrollParams.amount
			}
		} else {
			this._calcScroll(scrollParams, -destCellRel + 1);
			destCellRel += scrollParams.amount
		}
		gridParams.dcr = destCellRel;
		var loadRng = this._jutil.reg2rng( [ this._currTopCell[0],
				scrollParams.fincoord ], this._numCols, scrollParams.amount);
		this._jutil.rngCap(loadRng, this._sheet._farthestUsedCell);
		if (!scrollParams.amount
				|| scrollParams.fincoord > this._sheet._farthestUsedCell[1]
				|| this._currTopCell[0] > this._sheet._farthestUsedCell[0]
				|| !this._cache.miss(loadRng)) {
			return this._scrollGridY_do(false, gridParams, scrollParams, cb)
		}
		this._cache.abort();
		clearTimeout(this._book._tid_scrollGrid);
		var that = this;
		this._book._tid_scrollGrid = setTimeout(function() {
			that._cache.load( [ that, that._scrollGridY_do, false, gridParams,
					scrollParams, cb ], loadRng, {
				sdr : 2 + dir
			})
		}, 200);
		this._book._scrollPending = true
	},
	_scrollGridY_do : function(mode, gridParams, scrollParams, cb) {
		this._lastDoneDestCell[1] = gridParams.dca;
		this._cppi[1] = gridParams.cppi;
		this._cpp[1] = gridParams.cpp;
		if (mode) {
			this._oldCTC[1] = this._currTopCell[1];
			this._currTopCell[1] = gridParams.ctc;
			this._loadTable(false, true, true, false);
			var newtop = this._rowHeights.getSumUpTo(this._currTopCell[1]
					+ gridParams.dcr)
		} else {
			if (scrollParams.amount) {
				this._scrollTableY(scrollParams.dir, scrollParams.amount,
						scrollParams.newcoord, scrollParams.oldcoord),
						this._currTopCell[1] += scrollParams.s_amount
			}
			var newtop = parseInt(this._tableRows[gridParams.dcr].style.top)
		}
		this._oc.scrollTop = newtop;
		if (this._gmode_edit) {
			this._rowHdrsOC.scrollTop = newtop
		}
		if (this._book._doCheckSlider) {
			this._book._checkSlider(1)
		}
		this._book._scrollPending = false;
		this._book.scrollObserver.notify(this, this, 1, scrollParams.r_dir,
				this._lastDestCell[1] + 1, this._lastDestCell[1]
						+ this._cppi[1], this._book._currScrollSpeeds[1]);
		if (cb instanceof Array && cb.length > 1) {
			cb[1].apply(cb[0], cb.slice(2))
		}
	},
	getCoordsByCell : function(obj) {
		for ( var tmi, ldc = this._lastDestCell, ctc = this._currTopCell, cppi = this._cppi, tm = this._tableMat, i = ldc[1]
				- ctc[1] + 1, uly = i + cppi[1]; i <= uly; ++i) {
			if (i in tm) {
				tmi = tm[i]
			} else {
				break
			}
			for ( var j = ldc[0] - ctc[0] + 1, ulx = j + cppi[0]; j <= ulx; ++j) {
				if (tmi[j] == obj) {
					return [ ctc[0] + j, ctc[1] + i ]
				}
			}
		}
		return undefined
	},
	getCellByPos : function(x, y) {
		if (y in this._tableMat) {
			return this._tableMat[y][x]
		}
		return undefined
	},
	getCellByCoords : function(col, row) {
		col -= this._currTopCell[0];
		row -= this._currTopCell[1];
		if (row in this._tableMat) {
			return this._tableMat[row][col]
		}
		return undefined
	},
	getPixelsByCoords : function(col, row) {
		return [ this._colWidths.getSumUpTo(col),
				this._rowHeights.getSumUpTo(row) ]
	},
	furnishCell : function(col, row, obj, comb) {
		var x = col - this._currTopCell[0], y = row - this._currTopCell[1], cell = this
				.getCellByPos(x, y), stor = this._storage, decSep;
		if (cell == undefined) {
			return false
		}
		var s = "s" in obj ? obj.s : (comb ? undefined : ""), s_old = stor
				.getPart(x, y, "s"), cws = this._colWidths, rhs = this._rowHeights, cwi = this._cellSizeIncr[0], rhi = this._cellSizeIncr[1];
		if (s != undefined && (s != "" || s_old != undefined)) {
			cell.style.cssText = "left:".concat(cell.style.left, ";width:",
					cell.style.width, ";height:", cell.style.height,
					";visibility:", cell.style.visibility, ";display:",
					cell.style.display, ";text-align:", cell.style.textAlign,
					";", s);
			if (cell.style.borderTopWidth == "2px") {
				cell.style.width = ""
						.concat(cws.getElemAt(col) - cwi - 1, "px")
			}
			if (cell.style.borderLeftWidth == "2px") {
				cell.style.height = "".concat(rhs.getElemAt(row) - rhi - 1,
						"px")
			}
		}
		if ("v" in obj) {
			switch (typeof obj.v) {
			case "number":
				obj.v = (decSep = this._numericSeps[0]) == "." ? "" + obj.v
						: ("" + obj.v).replace(".", decSep);
				break;
			case "boolean":
				obj.v = this._l10nBool[obj.v];
				break
			}
		}
		if ("l" in obj) {
			var v = obj.o != "" ? obj.l : ("v" in obj ? obj.v : stor.getPart(x,
					y, "v"))
		} else {
			if ("v" in obj) {
				var v = stor.hasPart(x, y, "l") ? undefined : obj.v
			} else {
				var v = comb ? undefined : ""
			}
		}
		if (v != undefined) {
			var t = obj.t, t_old = stor.getPart(x, y, "t");
			if (t == "h" || (!t && t_old == "h")) {
				cell.innerHTML = v
			} else {
				this._setText(cell, v)
			}
			if (t
					&& ((s == undefined && (s_old == undefined || s_old
							.indexOf("text-align:") == -1)) || (s != undefined && s
							.indexOf("text-align:") == -1))
					&& ((t_old == undefined && this._jwstyle.defTextAlign[t] != "") || (t_old != undefined && t != t_old))) {
				cell.style.textAlign = this._jwstyle.defTextAlign[t]
			}
		}
		if ("m" in obj) {
			if (obj.m[0] == true) {
				if (cell.className.indexOf("mergedCell") == -1) {
					cell.className += " mergedCell"
				}
				for ( var sum = 0, max = obj.m[2], i = 0; i < max; sum += cws
						.getElemAt(col + i++)) {
				}
				cell.style.width = "".concat(sum - cwi, "px");
				for ( var sum = 0, max = obj.m[1], i = 0; i < max; sum += rhs
						.getElemAt(row + i++)) {
				}
				cell.style.height = "".concat(sum - rhi, "px")
			} else {
				if (cell.className.indexOf("mergedCell") != -1) {
					cell.className = "gridCell";
					cell.style.width = ""
							.concat(cws.getElemAt(col) - cwi, "px");
					cell.style.height = "".concat(rhs.getElemAt(row) - rhi,
							"px")
				}
			}
		} else {
			if (!comb && cell.className.indexOf("mergedCell") != -1) {
				cell.className = "gridCell";
				cell.style.width = "".concat(cws.getElemAt(col) - cwi, "px");
				cell.style.height = "".concat(rhs.getElemAt(row) - rhi, "px")
			}
		}
		if ("m" in obj) {
			if (obj.m[0] == false) {
				if (cell.style.visibility != "hidden") {
					cell.style.visibility = "hidden"
				}
			} else {
				if (cell.style.visibility == "hidden") {
					cell.style.visibility = ""
				}
			}
		} else {
			if (!comb && cell.style.visibility == "hidden") {
				cell.style.visibility = ""
			}
		}
		if (!this._gmode_edit && "k" in obj
				&& obj.k !== stor.getPart(x, y, "k")) {
			cell.className = obj.k ? "gridCell" : "gridCell gridCellUnlocked"
		}
		this._storage.set(x, y, obj, comb)
	},
	getNeighByOffset : function(col, row, offx, offy) {
		col = this._colWidths.getIdxByOffset(col, offx);
		row = this._rowHeights.getIdxByOffset(row, offy);
		return [ col, row, this.getCellByCoords(col, row) ]
	},
	isCellVisible : function(col, row) {
		return col >= this._lastDestCell[0] + 1
				&& col <= this._lastDestCell[0] + this._cppi[0]
				&& row >= this._lastDestCell[1] + 1
				&& row <= this._lastDestCell[1] + this._cppi[1]
	},
	isCellLocked : function(col, row) {
		return this._storage.fetch(col - this._currTopCell[0], row
				- this._currTopCell[1], "k") !== false
	},
	getCellDims : function(col, row) {
		return [ this._colWidths.getElemAt(col),
				this._rowHeights.getElemAt(row) ]
	},
	getCell : function(col, row) {
		return this._storage.fetch(col - this._currTopCell[0], row
				- this._currTopCell[1])
	},
	getCellValue : function(col, row) {
		col -= this._currTopCell[0];
		row -= this._currTopCell[1];
		if (col < 0 || col >= this._numCols || row < 0 || row >= this._numRows) {
			return undefined
		}
		return this._storage.hasPart(col, row, "l") ? this._storage.getPart(
				col, row, "l") : this._storage.getPart(col, row, "v")
	},
	getCellFVal : function(col, row) {
		return this._storage.fetch(col - this._currTopCell[0], row
				- this._currTopCell[1], "l")
	},
	getCellUVal : function(col, row) {
		return this._storage.fetch(col - this._currTopCell[0], row
				- this._currTopCell[1], "v")
	},
	getCellFormula : function(col, row) {
		return this._storage.fetch(col - this._currTopCell[0], row
				- this._currTopCell[1], "f")
	},
	getCellNFs : function(col, row) {
		return this._storage.fetch(col - this._currTopCell[0], row
				- this._currTopCell[1], "n")
	},
	getCellType : function(col, row) {
		return this._storage.fetch(col - this._currTopCell[0], row
				- this._currTopCell[1], "t")
	},
	getCellStyle : function(col, row) {
		return this._storage.fetch(col - this._currTopCell[0], row
				- this._currTopCell[1], "s")
	},
	setCellValue : function(col, row, val) {
		if (typeof val != "string") {
			return false
		}
		var cb = [ this._book, this._book.exec ], x = col
				- this._currTopCell[0], y = row - this._currTopCell[1];
		if (val[0] == "=") {
			this._conn.setCcmd( [ [ "cdrn", {
				cm : true
			}, [ col, row, col, row, {
				v : val
			} ] ] ])
		} else {
			var n = this._numericSeps[0] != "." ? val.replace(this._reThouSep,
					"").replace(this._numericSeps[0], ".") : val.replace(
					this._reThouSep, ""), quo = {
				n : "",
				s : '"'
			}, match, f, t, v, cell_old;
			if (n === "" || n === null || n === undefined) {
				v = "", t = "e"
			} else {
				if (!isNaN(n)) {
					v = n * 1, t = "n"
				} else {
					if (val.toUpperCase() == this._l10nBool[true]) {
						v = true, t = "b", val = this._l10nBool[true]
					} else {
						if (val.toUpperCase() == this._l10nBool[false]) {
							v = false, t = "b", val = this._l10nBool[false]
						} else {
							t = (v = val)[0] == "<" ? "h" : "s"
						}
					}
				}
			}
			if (t in quo && (f = this._storage.fetch(x, y, "f")) != undefined
					&& (match = f.match(this._rePaloData)) !== null) {
				this._conn.setCcmd( [ [
						"cdrn",
						{
							cm : true
						},
						[
								col,
								row,
								col,
								row,
								{
									v : "=PALO.SETDATA("
											.concat(quo[t], v, quo[t],
													this._numericSeps[2],
													this._l10nBool[false],
													this._numericSeps[2],
													match[1], ")")
								} ], [ col, row, col, row, {
							v : f
						} ] ] ])
			} else {
				cell_old = (cell_old = this._storage.fetch(x, y)) !== undefined ? {
					v : cell_old.v,
					f : cell_old.f,
					t : cell_old.t
				}
						: {};
				this.furnishCell(col, row, {
					v : val,
					t : t,
					_pend : true
				}, true), this._conn.setCcmd( [ [ "cdrn", {
					cm : true
				}, [ col, row, col, row, {
					v : v
				} ] ] ]);
				cb.push( [ this, this.setCellValue_post, col, row, cell_old ])
			}
		}
		this._conn.sendBatch(cb, this.getRealGridRange(), true, true,
				this._conn.Q_VALUE | this._conn.Q_STYLE
						| this._conn.Q_FORMULA_WE | this._conn.Q_ATTRS
						| this._conn.Q_FMT_VAL | this._conn.Q_FMT
						| this._conn.Q_FORMULA_NF | this._conn.Q_LOCK)
	},
	setCellValue_post : function(res, col, row, cell_old) {
		var x = col - this._currTopCell[0], y = row - this._currTopCell[1];
		if (this._storage.hasPart(x, y, "_pend")) {
			this.furnishCell(col, row, cell_old, true)
		}
	},
	setRangeValue : function(range, vals) {
		if (typeof vals != "object" || !("length" in vals)) {
			return false
		}
		var cmd_range = [ range[0], range[1], range[2], range[3] ], cmd_post = [], seps = this._numericSeps, l10nBool = this._l10nBool, reTS = this._reThouSep, rePD = this._rePaloData, quo = {
			n : "",
			s : '"'
		}, val, match, f, v, t, n;
		for ( var len = vals.length, col = range[0], row = range[1], i = 0; i < len; ++i) {
			val = vals[i];
			if (val[0] == "=") {
				cmd_range.push( {
					v : val
				})
			} else {
				n = seps[0] != "." ? val.replace(reTS, "")
						.replace(seps[0], ".") : val.replace(reTS, "");
				if (n === "" || n === null || n === undefined) {
					v = "", t = "e"
				} else {
					if (!isNaN(n)) {
						v = n * 1, t = "n"
					} else {
						if (val.toUpperCase() == l10nBool[true]) {
							v = true, t = "b"
						} else {
							if (val.toUpperCase() == l10nBool[false]) {
								v = false, t = "b"
							} else {
								v = val, t = "s"
							}
						}
					}
				}
				if (t in quo
						&& (f = this._storage.fetch(col - this._currTopCell[0],
								row - this._currTopCell[1], "f")) != undefined
						&& (match = f.match(rePD)) !== null) {
					cmd_range.push( {
						v : "=PALO.SETDATA(".concat(quo[t], v, quo[t], seps[2],
								l10nBool[false], seps[2], match[1], ")")
					});
					cmd_post.push( [ col, row, col, row, {
						v : f
					} ])
				} else {
					cmd_range.push( {
						v : v
					})
				}
			}
			if (++col > range[2]) {
				++row, col = range[0]
			}
		}
		this._conn.setCcmd( [ [ "cdrn", {
			cm : true
		}, cmd_range ] ].concat(cmd_post.length ? [ [ "cdrn", {
			cm : true
		} ].concat(cmd_post) ] : []));
		this._conn.sendBatch( [ this._book, this._book.exec ], this
				.getRealGridRange(), true, true, this._conn.Q_VALUE
				| this._conn.Q_STYLE | this._conn.Q_FORMULA_WE
				| this._conn.Q_ATTRS | this._conn.Q_FMT_VAL | this._conn.Q_FMT
				| this._conn.Q_FORMULA_NF | this._conn.Q_LOCK)
	},
	setArrayFormula : function(range, val) {
		range.push(val);
		this._conn.cmd( [ this._book, this._book.exec ], [ "saf" ], [ range ],
				this.getRealGridRange(), true, true, this._conn.Q_VALUE
						| this._conn.Q_STYLE | this._conn.Q_FORMULA_WE
						| this._conn.Q_FMT_VAL | this._conn.Q_FMT
						| this._conn.Q_FORMULA_NF)
	},
	merge : function(range, isUnMerge) {
		var mi;
		if (range[0] == range[2]
				&& range[1] == range[3]
				&& (mi = this._storage.fetch(range[0] - this._currTopCell[0],
						range[1] - this._currTopCell[1], "m")) !== undefined
				&& mi[0] === true) {
			range[2] += mi[2] - 1, range[3] += mi[1] - 1
		}
		if (range[2] > this._sheet._farthestUsedCell[0]) {
			this._sheet._farthestUsedCell[0] = range[2]
		}
		if (range[3] > this._sheet._farthestUsedCell[1]) {
			this._sheet._farthestUsedCell[1] = range[3]
		}
		this._conn.cmd( [ this._book, this._book.exec,
				[ this, this._merge_post, range, isUnMerge ] ],
				[ isUnMerge ? "umrg" : "mrg" ], [ range ], this
						.getRealGridRange(), true, true, this._conn.Q_VALUE
						| this._conn.Q_STYLE | this._conn.Q_FORMULA_WE
						| this._conn.Q_FMT_VAL | this._conn.Q_MERGE
						| this._conn.Q_FORMULA_NF)
	},
	_merge_post : function(res, range, isUnMerge) {
		this._defaultSelection.set(new Jedox.wss.cls.Point(range[0], range[1]),
				isUnMerge ? new Jedox.wss.cls.Point(range[2], range[3])
						: new Jedox.wss.cls.Point(range[0], range[1]));
		this._defaultSelection.draw();
		if (isUnMerge) {
			this._defaultSelection.checkForUndoneMarkers(true);
			Jedox.wss.general.setCurrentCoord()
		}
	},
	getMergeInfo : function(col, row) {
		var mi = this._storage.fetch(col - this._currTopCell[0], row
				- this._currTopCell[1], "m");
		if (typeof mi != "object") {
			return undefined
		}
		return mi[0] == true ? [ true, mi[2], mi[1] ] : [ false, mi[2] + 1,
				mi[1] + 1 ]
	},
	getMergeState : function(range) {
		var x1 = range[0] - this._currTopCell[0], y1 = range[1]
				- this._currTopCell[1], x2 = range[2] - this._currTopCell[0], y2 = range[3]
				- this._currTopCell[1];
		if (x1 < 0 || x1 >= this._numCols || y1 < 0 || y1 >= this._numRows
				|| x2 < 0 || x2 >= this._numCols || y2 < 0
				|| y2 >= this._numRows) {
			return undefined
		}
		var mi = this._storage.fetch(x1, y1, "m"), c_mi;
		for ( var y = y2; y >= y1; --y) {
			for ( var x = x2; x >= x1; --x) {
				if (x == x1 && y == y1) {
					continue
				}
				c_mi = this._storage.fetch(x, y, "m");
				if (mi) {
					if (!c_mi || c_mi[0]) {
						return undefined
					}
					if (mi[0]) {
						if (c_mi[1] != range[1] - 1 || c_mi[2] != range[0] - 1) {
							return undefined
						}
					} else {
						if (mi[1] != c_mi[1] || mi[2] != c_mi[2]) {
							return undefined
						}
					}
				} else {
					if (c_mi) {
						return undefined
					}
				}
			}
		}
		return mi ? true : false
	},
	setCellStyle : function(col, row, style) {
		if (col > this._sheet._farthestUsedCell[0]) {
			this._sheet._farthestUsedCell[0] = col
		}
		if (row > this._sheet._farthestUsedCell[1]) {
			this._sheet._farthestUsedCell[1] = row
		}
		col -= this._currTopCell[0], row -= this._currTopCell[1];
		var cstyle = this.getCellByPos(col, row);
		if (cstyle == undefined) {
			return
		}
		cstyle = cstyle.style;
		for ( var attr in style) {
			cstyle[attr] = style[attr]
		}
		this._storage.setPart(col, row, style, "s")
	},
	setRangeStyle : function(range, style) {
		if (range[2] > this._sheet._farthestUsedCell[0]) {
			this._sheet._farthestUsedCell[0] = range[2]
		}
		if (range[3] > this._sheet._farthestUsedCell[1]) {
			this._sheet._farthestUsedCell[1] = range[3]
		}
		var x1 = range[0] - this._currTopCell[0], y1 = range[1]
				- this._currTopCell[1], x2 = range[2] - this._currTopCell[0], y2 = range[3]
				- this._currTopCell[1];
		if (x1 >= this._numCols || x2 < 0 || y1 >= this._numRows || y2 < 0) {
			return
		}
		if (x1 < 0) {
			x1 = 0
		} else {
			if (x1 >= this._numCols) {
				x1 = this._numCols - 1
			}
		}
		if (y1 < 0) {
			y1 = 0
		} else {
			if (y1 >= this._numRows) {
				y1 = this._numRows - 1
			}
		}
		if (x2 < 0) {
			x2 = 0
		} else {
			if (x2 >= this._numCols) {
				x2 = this._numCols - 1
			}
		}
		if (y2 < 0) {
			y2 = 0
		} else {
			if (y2 >= this._numRows) {
				y2 = this._numRows - 1
			}
		}
		var cstyle;
		for ( var y = y2; y >= y1; --y) {
			for ( var x = x2; x >= x1; --x) {
				cstyle = this._tableMat[y][x].style;
				for ( var attr in style) {
					cstyle[attr] = style[attr]
				}
				this._storage.setPart(x, y, style, "s")
			}
		}
	},
	getRangeStyle : function(range, attrs) {
		var x1 = range[0] - this._currTopCell[0], y1 = range[1]
				- this._currTopCell[1], x2 = range[2] - this._currTopCell[0], y2 = range[3]
				- this._currTopCell[1];
		if (x1 < 0 || x1 >= this._numCols || y1 < 0 || y1 >= this._numRows
				|| x2 < 0 || x2 >= this._numCols || y2 < 0
				|| y2 >= this._numRows) {
			return {}
		}
		var cstyle = this._tableMat[y1][x1].style;
		for ( var attr in attrs) {
			attrs[attr] = cstyle[attr]
		}
		for ( var tm = this._tableMat, y = y1; y <= y2; ++y) {
			for ( var x = x1; x <= x2; ++x) {
				cstyle = tm[y][x].style;
				for ( var attr in attrs) {
					if (cstyle[attr] != attrs[attr]) {
						delete attrs[attr]
					}
				}
			}
		}
		return attrs
	},
	setRangeFormat : function(range, fmt) {
		var mi;
		if (range[0] == range[2] && range[1] == range[3]
				&& (mi = this.getMergeInfo(range[0], range[1])) && mi[0]) {
			range[2] += mi[1] - 1, range[3] += mi[2] - 1
		}
		this._conn.cmd( [ this._book, this._book.exec ], this._conn.cmdHdr, [ [
				range[0], range[1], range[2], range[3], {
					o : fmt.shift(),
					a : {
						o : fmt
					}
				} ] ], this.getRealGridRange(), false, false,
				this._conn.Q_STYLE | this._conn.Q_ATTRS | this._conn.Q_FMT_VAL
						| this._conn.Q_FMT)
	},
	getRangeFormat : function(range) {
		var x1 = range[0] - this._currTopCell[0], y1 = range[1]
				- this._currTopCell[1], x2 = range[2] - this._currTopCell[0], y2 = range[3]
				- this._currTopCell[1];
		if (x1 < 0 || x1 >= this._numCols || y1 < 0 || y1 >= this._numRows
				|| x2 < 0 || x2 >= this._numCols || y2 < 0
				|| y2 >= this._numRows) {
			return undefined
		}
		var fmt = [ this._storage.getPart(x1, y1, "o") ].concat(this._storage
				.getAttr(x1, y1, "o")), fmt_len = fmt.length;
		for ( var y = y2; y >= y1; --y) {
			for ( var c_fmt, x = x2; x >= x1; --x) {
				c_fmt = [ this._storage.getPart(x, y, "o") ]
						.concat(this._storage.getAttr(x, y, "o"));
				if (c_fmt.length != fmt_len) {
					return undefined
				}
				for ( var i = fmt_len - 1; i >= 0; --i) {
					if (c_fmt[i] != fmt[i]) {
						return undefined
					}
				}
			}
		}
		return fmt[0] != undefined ? fmt : undefined
	},
	setRangeLock : function(range, lock) {
		lock = lock ? true : false;
		var x1 = range[0] - this._currTopCell[0], y1 = range[1]
				- this._currTopCell[1], x2 = range[2] - this._currTopCell[0], y2 = range[3]
				- this._currTopCell[1];
		if (x1 >= this._numCols || x2 < 0 || y1 >= this._numRows || y2 < 0) {
			return
		}
		if (x1 < 0) {
			x1 = 0
		} else {
			if (x1 >= this._numCols) {
				x1 = this._numCols - 1
			}
		}
		if (y1 < 0) {
			y1 = 0
		} else {
			if (y1 >= this._numRows) {
				y1 = this._numRows - 1
			}
		}
		if (x2 < 0) {
			x2 = 0
		} else {
			if (x2 >= this._numCols) {
				x2 = this._numCols - 1
			}
		}
		if (y2 < 0) {
			y2 = 0
		} else {
			if (y2 >= this._numRows) {
				y2 = this._numRows - 1
			}
		}
		this._storage.setRangePart(x1, y1, x2, y2, lock, "k");
		this._conn.cmd(this._conn.dummy_cb, [ "lock" ], [ range, lock ])
	},
	getCoordsFirstVCell : function() {
		return [ this._lastDestCell[0] + 1, this._lastDestCell[1] + 1 ]
	},
	getCoordsLastVCell : function() {
		return [ this._lastDestCell[0] + this._cppi[0],
				this._lastDestCell[1] + this._cppi[1] ]
	},
	getVisibleRange : function(type) {
		return [ this._lastDestCell[type] + 1,
				this._lastDestCell[type] + this._cppi[type] ]
	},
	getGridRng : function() {
		var ctc = this._currTopCell;
		return ctc.concat(ctc[0] + this._numCols - 1, ctc[1] + this._numRows
				- 1)
	},
	getRealGridRange : function() {
		return [
				[ this._currTopCell[0], this._currTopCell[1] ],
				[ this._currTopCell[0] + this._numCols - 1,
						this._currTopCell[1] + this._numRows - 1 ] ]
	},
	getViewportSize : function() {
		return [ this._ocWidth, this._ocHeight ]
	},
	getViewportPos : function() {
		return [
				[ this._oc.scrollLeft, this._oc.scrollTop ],
				[ this._oc.scrollLeft + this._ocWidth,
						this._oc.scrollTop + this._ocHeight ] ]
	},
	getFarthestUsedCell : function() {
		return [ this._sheet._farthestUsedCell[0],
				this._sheet._farthestUsedCell[1] ]
	},
	pasteRange : function(cb, range, clip_id, paste_what) {
		if (typeof paste_what != "number") {
			paste_what = Jedox.wss.range.ContentType.ALL_PASTE
		}
		range.unshift(clip_id);
		range.push(paste_what);
		this._conn.createBatch();
		this._conn.cmd(null, [ "ptrn" ], [ range ]);
		this._conn.cmd(null, [ "adcr" ], [ 1, [ range[2], range[4] ] ]);
		this._conn.sendBatch( [ this._book, this._book.exec, cb ], this
				.getRealGridRange())
	},
	clrRange : function(range, clr_what) {
		if (typeof clr_what != "number") {
			clr_what = 1
		}
		range.unshift(clr_what);
		this._conn.cmd( [ this._book, this._book.exec ], [ "clr" ], [ range ],
				this.getRealGridRange())
	},
	cbFire : function(col, row, attr_name, params) {
		col -= this._currTopCell[0];
		row -= this._currTopCell[1];
		if (col < 0 || col >= this._numCols || row < 0 || row >= this._numRows) {
			return false
		}
		var args = this._storage.getAttr(col, row, attr_name), cb;
		if (args == undefined
				|| (cb = this._jwgrid.cbGet(args[0])) == undefined) {
			return true
		}
		return cb[1].apply(cb[0], [ params ].concat(args.slice(1)))
	},
	recalc : function(cb) {
		this._conn.rpc( [ this._book, this._book.exec,
				[ this, this.recalc_post, cb ] ], "WSS", "recalc", [
				this.getGridRng(), Jedox.wss.app.autoCalc ])
	},
	recalc_post : function(res, cb) {
		if (cb instanceof Array && cb.length > 1) {
			cb[1].apply(cb[0], cb.slice(2))
		}
	},
	getColHdrsIC : function() {
		return this._colHdrsIC
	},
	getRowHdrsIC : function() {
		return this._rowHdrsIC
	},
	getBookIC : function() {
		return this._ic
	},
	getEnvironment : function() {
		return this._env
	},
	getDomId : function() {
		return this._domId
	},
	getCursorField : function() {
		return this._cursorField
	},
	insertCol : function(col, num) {
		return this._sheet.insertCol(col, num)
	},
	insertRow : function(row, num) {
		return this._sheet.insertRow(row, num)
	},
	deleteCol : function(col, num) {
		return this._sheet.deleteCol(col, num)
	},
	deleteRow : function(row, num) {
		return this._sheet.deleteRow(row, num)
	},
	activateHdr : function(type, idx, cls) {
		return this._sheet.activateHdr(type, idx, cls)
	},
	activateHdrRng : function(type, rng, cls) {
		return this._sheet.activateHdrRng(type, rng, cls)
	},
	activateHdrAll : function(type, cls) {
		return this._sheet.activateHdrAll(type, cls)
	},
	getCoordByHdr : function(type, obj) {
		return this._sheet.getCoordByHdr(type, obj)
	},
	getHdrByCoord : function(type, coord) {
		return this._sheet.getHdrByCoord(type, coord)
	},
	scrollTo : function(cb, col, row, minscroll, force) {
		this._book.scrollTo(cb, col, row, minscroll, force)
	}
};