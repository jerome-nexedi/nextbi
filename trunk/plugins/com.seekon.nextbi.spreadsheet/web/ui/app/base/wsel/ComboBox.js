Jedox.wss.wsel.ComboBox = (function() {
	var type = "ComboBox", defDims = {
		w : 150,
		h : 22
	};
	return function(conf) {
		Jedox.wss.wsel.ComboBox.parent.constructor.call(this, conf);
		var activeBook = Jedox.wss.app.activeBook, conn = Jedox.wss.backend.conn, env = Jedox.wss.app.environment, actCellCoords = env.selectedCellCoords, formulaParser = Jedox.wss.formula, trgTypes = Jedox.wss.wsel.FormBase.trgTypes, trgType = this
				.getTargetType(), that = this;
		this.preselectType = {
			LOAD : 0,
			REFR : 1,
			UTRG : 2
		};
		if (!this.conf.id) {
			if (this.conf == undefined) {
				this.conf = {
					type : type,
					src : '=PALO.SUBSET("localhost/Demo","Years",1,,,,,,,PALO.SORT(0,0,,0,,0,1))',
					trange : "",
					tnamedrange : "",
					tvar : "ComboBox"
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
			var posConf = Jedox.wss.wsel.getNLoc(this.conf.left, this.conf.top,
					this.conf.width, this.conf.height);
			var res = conn.cmd(0, [ "wadd" ], [
					"",
					{
						e_type : "formel",
						n_refers_to : this.conf.src,
						n_target_ref : "=".concat(trgType == trgTypes.VAR ? "@"
								: "", this.conf[trgType]),
						n_location : posConf.n_location,
						pos_offsets : posConf.pos_offsets,
						n_use_locale : this.conf.formulaEnabled,
						formel_name : this.conf.name,
						formel_type : this.conf.type,
						macros : this.conf.macros
					} ])[0];
			if (res[0] && res[1].length > 0) {
				this.conf.id = res[1][0]
			} else {
				throw [ "formel_add_wsel_err", {} ]
			}
			var wselData = conn.cmd(0, [ "wget" ], [ "", [],
					[ "n_refers_to", "n_get_val" ], {
						e_id : this.conf.id
					} ])[0][1][0];
			this.conf.src = wselData.n_refers_to;
			this.conf.treeVal = Jedox.util.cols2Tree(
					wselData.n_get_val instanceof Array ? wselData.n_get_val
							: [ wselData.n_get_val ], true);
			this.conf.selval = this.conf.treeVal[0]["val"],
					this.conf.selpath = "/root/".concat(this.conf.selval);
			var updObj = {};
			updObj[this.conf.id] = {
				data : {
					trange : this.conf.trange,
					tnamedrange : this.conf.tnamedrange,
					tvar : this.conf.tvar,
					selval : this.conf.selval,
					selpath : this.conf.selpath
				}
			};
			conn.createBatch();
			conn.cmd(null, [ "wupd" ], [ "", updObj ]);
			if (this.conf.trange.length > 0) {
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
					v : this.conf.selval
				}) ]);
				if (tSheetId) {
					conn.cmd(null, [ "osel" ], [ 2, actSheetId ])
				}
			}
			if (this.conf.tnamedrange.length > 0) {
				conn
						.cmd(
								null,
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
										} ] ])
			}
			if (this.conf.tvar.length > 0) {
				conn
						.cmd(null, [ "svar" ], [ this.conf.tvar,
								this.conf.selval ])
			}
			conn.sendBatch( [ activeBook, activeBook.exec ], activeBook
					.getRealGridRange())
		} else {
			this.conf.treeVal = Jedox.util.cols2Tree(
					this.conf.treeVal instanceof Array ? this.conf.treeVal
							: [ this.conf.treeVal ], true)
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
		this.elem = new Ext.form.ComboBox(
				{
					id : this.gContId.concat("_ComboBox_", this.conf.id),
					renderTo : formelContId,
					disabled : false,
					store : new Ext.data.SimpleStore( {
						fields : [],
						data : [ [] ]
					}),
					hideLabel : true,
					editable : false,
					triggerAction : "all",
					mode : "local",
					shadow : false,
					maxHeight : 200,
					tpl : '<tpl for="."><div style="height:200px"><div class="cmbTreeList" id="'
							.concat(this.gContId, "_cmbTreeList_",
									this.conf.id, '"></div></div></tpl>'),
					selectedClass : "",
					onSelect : Ext.emptyFn,
					value : this.conf.targetVal == null ? this.conf.selval
							: this.conf.targetVal,
					width : this.conf.width,
					allowBlank : true,
					selectOnFocus : false,
					resizable : false,
					validateOnBlur : false,
					preventMark : true,
					listeners : {
						expand : {
							fn : this.onExpandComboBox,
							scope : this
						}
					}
				});
		var treeNodeProvider = new Ext.tree.TreeNodeProvider( {
			getNodes : function() {
				return this.data
			}
		});
		this.myTreeLoader = new Ext.tree.MyTreeLoader( {
			treeNodeProvider : treeNodeProvider
		});
		this.myTreeLoader.updateTreeNodeProvider(this.conf.treeVal);
		this.cmbTreeList = new Ext.tree.TreePanel( {
			id : this.gContId.concat("_cmbTreePanel_", this.conf.id),
			border : false,
			cls : "x-tree-noicon",
			autoScroll : true,
			animate : true,
			enableDD : false,
			containerScroll : true,
			loader : this.myTreeLoader,
			rootVisible : false,
			root : new Ext.tree.AsyncTreeNode( {
				text : "[All Elements]".localize(),
				draggable : false,
				val : "root"
			}),
			listeners : {
				click : {
					fn : this.onSelComboBox,
					scope : this
				}
			},
			initConf : this.conf
		});
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
		this.elem.wsel = this;
		if (this.conf.targetVal != null) {
			this.preselect(this.conf.targetVal, this.preselectType.LOAD)
		}
	}
})();
Jedox.util.extend(Jedox.wss.wsel.ComboBox, Jedox.wss.wsel.FormBase);
Jedox.wss.wsel.ComboBox.events = [ {
	name : "select",
	funcname : "Select"
} ];
Jedox.wss.wsel.ComboBox.minDims = {
	w : 80,
	h : 22
};
clsRef = Jedox.wss.wsel.ComboBox;
clsRef.prototype.updateWSElData = function() {
	var conn = Jedox.wss.backend.conn, updObj = {};
	updObj[this.conf.id] = {
		data : {
			trange : this.conf.trange,
			tnamedrange : this.conf.tnamedrange,
			tvar : this.conf.tvar,
			selval : this.conf.selval,
			selpath : this.conf.selpath
		}
	};
	conn.cmd(conn.dummy_cb, [ "wupd" ], [ "", updObj ])
};
clsRef.prototype.beforeShowCtxMenu = function() {
	this.elem.collapse()
};
clsRef.prototype.onSelComboBox = function(node, e) {
	this.elem.setValue(node.text);
	this.elem.collapse();
	if (node.getPath("val") == this.conf.selpath) {
		return
	}
	this.conf.selval = node.attributes.val;
	this.conf.selpath = node.getPath("val");
	var conn = Jedox.wss.backend.conn, activeBook = Jedox.wss.app.activeBook;
	conn.createBatch();
	this.updateDependencies();
	this.updateWSElData();
	if (this.conf.macros.select) {
		var jwmacro = Jedox.wss.macro;
		conn.cmd( [ jwmacro, jwmacro.ui_exec ], [ "em" ], [
				"php",
				"em.php",
				"em",
				[ this.conf.macros.select, this.conf.selval ],
				activeBook._gmode_edit ? activeBook._aPane._defaultSelection
						.getActiveRange().getValue() : "A1" ])
	}
	conn.sendBatch( [ activeBook, activeBook.exec ], activeBook
			.getRealGridRange())
};
clsRef.prototype.onExpandComboBox = function(combo) {
	if (!this.cmbTreeList.rendered) {
		this.cmbTreeList.render(this.gContId.concat("_cmbTreeList_",
				this.conf.id));
		this.cmbTreeList.getRootNode().reload();
		var that = this;
		setTimeout(function() {
			that.cmbTreeList.selectPath(that.conf.selpath, "val")
		}, 0)
	}
};
clsRef.prototype.refresh = function(data) {
	this.myTreeLoader.updateTreeNodeProvider(Jedox.util.cols2Tree(data, true));
	this.preselect(data[0], this.preselectType.REFR)
};
clsRef.prototype.getSelPath = function(data, selVal, path) {
	function getPath(data, selVal, path) {
		if (data.val == selVal) {
			return path.concat("/", selVal)
		}
		if (data.leaf) {
			return ""
		} else {
			path = path.concat("/", data.val);
			var subpath = "";
			for ( var child in data.children) {
				subpath = getPath(data.children[child], selVal, path);
				if (subpath.length) {
					return subpath
				}
			}
			return subpath
		}
	}
	for ( var i = 0, dataLen = data.length, path; i < dataLen; i++) {
		path = getPath(data[i], selVal, "");
		if (path.length) {
			return path
		}
	}
	return ""
};
clsRef.prototype.preselect = function(selVal, type) {
	var data = this.cmbTreeList.loader.getTreeNodeProvider().getData(), pSelType = this.preselectType;
	if (!data.length) {
		return
	}
	if (!this.cmbTreeList.rendered) {
		switch (type) {
		case pSelType.LOAD:
			if (selVal == null || this.conf.selval == selVal) {
				return
			}
			var selPath = this.getSelPath(data, selVal);
			if (selPath.length) {
				this.conf.selval = selVal;
				this.conf.selpath = "/root".concat(selPath)
			} else {
				this.conf.selval = data[0].val;
				this.conf.selpath = "/root/".concat(data[0].val)
			}
			break;
		case pSelType.REFR:
			var selPath = this.getSelPath(data, this.conf.selval);
			if (selPath.length) {
				return
			} else {
				this.conf.selval = data[0].val;
				this.conf.selpath = "/root/".concat(data[0].val)
			}
			break;
		case pSelType.UTRG:
			if (selVal == null || this.conf.selval == selVal) {
				return
			}
			var selPath = this.getSelPath(data, selVal);
			if (selPath.length) {
				this.conf.selval = selVal;
				this.conf.selpath = "/root".concat(selPath)
			} else {
				return
			}
			break
		}
		var conn = Jedox.wss.backend.conn, activeBook = Jedox.wss.app.activeBook;
		conn.createBatch();
		if (type <= this.preselectType.REFR) {
			this.updateDependencies()
		}
		this.updateWSElData();
		conn.sendBatch( [ activeBook, activeBook.exec ], activeBook
				.getRealGridRange());
		this.elem.setValue(this.conf.selval)
	} else {
		if (type == this.preselectType.REFR) {
			var selNode = this.cmbTreeList.getSelectionModel()
					.getSelectedNode(), selPath = selNode == null ? "abc"
					: selNode.getPath("val"), that = this, cbSelectPathHandler = function(
					bSuccess, oSelNode, tree, iterator) {
				if (!bSuccess) {
					tree.selectPath("/root/".concat(data[0].val), "val");
					var selNode = tree.getSelectionModel().getSelectedNode();
					tree.fireEvent("click", selNode, null)
				}
			}, cbSelectPath = function(bSuccess, oSelNode) {
				cbSelectPathHandler(bSuccess, oSelNode, that.cmbTreeList)
			};
			this.cmbTreeList.getRootNode().reload();
			this.cmbTreeList.selectPath(selPath, "val", cbSelectPath)
		} else {
			var selPath = this.getSelPath(data, selVal), that = this;
			if (selPath.length) {
				selPath = "/root".concat(selPath)
			} else {
				return
			}
			var cbSelectPathHandler = function(bSuccess, oSelNode, wsel) {
				if (bSuccess) {
					wsel.conf.selval = selVal;
					wsel.conf.selpath = selPath;
					wsel.elem.setValue(selVal);
					wsel.updateWSElData()
				}
			}, cbSelectPath = function(bSuccess, oSelNode) {
				cbSelectPathHandler(bSuccess, oSelNode, that)
			};
			this.cmbTreeList.selectPath(selPath, "val", cbSelectPath)
		}
	}
};
clsRef.prototype.update = function(editConf) {
	var conn = Jedox.wss.backend.conn, activeBook = Jedox.wss.app.activeBook, actCellCoords = Jedox.wss.app.environment.selectedCellCoords, wsel = {}, updObj = {}, chkNameAndRng = chkNRng = false, batchStarted = updWsel = updData = updDep = updSrc = false;
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
	if (editConf.formulaEnabled != this.conf.formulaEnabled) {
		this.conf.formulaEnabled = editConf.formulaEnabled;
		wsel.n_use_locale = this.conf.formulaEnabled
	}
	if (editConf.name != this.conf.name) {
		this.conf.name = editConf.name;
		wsel.formel_name = this.conf.name
	}
	if (editConf.src != this.conf.src) {
		this.conf.src = editConf.src;
		wsel.n_refers_to = this.conf.src;
		updSrc = true
	}
	if (editConf.trange != this.conf.trange) {
		if (editConf.trange.length) {
			wsel.n_target_ref = "=".concat(editConf.trange)
		}
		this.conf.trange = editConf.trange;
		updData = updDep = true
	}
	if (editConf.tnamedrange != this.conf.tnamedrange) {
		if (editConf.tnamedrange.length) {
			wsel.n_target_ref = "=".concat(editConf.tnamedrange)
		}
		this.conf.tnamedrange = editConf.tnamedrange;
		if (this.conf.tnamedrange.length
				&& !chkRes[chkNameAndRng ? 1 : 0][1][0]) {
			startBatch();
			conn.cmd(0, [ "nadd" ], [ [ actCellCoords[0], actCellCoords[1], {
				name : this.conf.tnamedrange,
				refers_to : "=TRUE",
				scope : activeBook.getSheetSelector().getActiveSheetName(),
				comment : "Managed by form element ".concat(this.conf.name)
			} ] ])
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
	if (updSrc) {
		updObj[this.conf.id] = wsel;
		startBatch();
		conn.cmd(0, [ "wupd" ], [ "", updObj ]);
		conn.cmd(0, [ "wget" ], [ "", [], [ "n_get_val", "n_refers_to" ], {
			e_id : this.conf.id
		} ]);
		var updRes = conn.sendBatch(), nVal = updRes[updRes.length - 1][1][0].n_get_val;
		batchStarted = false;
		this.conf.src = updRes[updRes.length - 1][1][0].n_refers_to;
		this.refresh(Ext.isArray(nVal) ? nVal : [ nVal ])
	} else {
		if (updWsel || updData) {
			if (updData) {
				wsel.data = {
					trange : this.conf.trange,
					tnamedrange : this.conf.tnamedrange,
					tvar : this.conf.tvar,
					selval : this.conf.selval,
					selpath : this.conf.selpath
				}
			}
			updObj[this.conf.id] = wsel;
			startBatch();
			conn.cmd(null, [ "wupd" ], [ "", updObj ]);
			if (updDep) {
				this.updateDependencies()
			}
		}
	}
	if (batchStarted) {
		conn.sendBatch( [ activeBook, activeBook.exec ], activeBook
				.getRealGridRange())
	}
};
clsRef.prototype.setCmpSize = function(w, h) {
	this.elem.setSize(w, h)
};
clsRef.prototype.updateTarget = function(data) {
	var trgType = this.getTargetType(), updVal = data.n_target_val instanceof Array ? data.n_target_val[0]
			: data.n_target_val, tRefIdx = data.n_target_ref.search(/^=/) + 1, updData = false;
	if (!data.n_target_ref.search(/^=@/)) {
		tRefIdx++
	}
	data.n_target_ref = data.n_target_ref.substr(tRefIdx);
	if (this.conf[trgType] != data.n_target_ref) {
		this.conf[trgType] = data.n_target_ref;
		updData = true
	}
	if (this.conf.selval != updVal) {
		this.preselect(updVal, this.preselectType.UTRG)
	} else {
		if (updData) {
			this.updateWSElData()
		}
	}
};
clsRef = null;