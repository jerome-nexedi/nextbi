Jedox.wss.wsel.img = {};
Jedox.wss.wsel.img.moveTo = function(imgId, pos, offsets) {
	Jedox.wss.wsel.moveTo("".concat(Jedox.wss.app.activeBook.getDomId(),
			"_ws_element_", imgId,
			Jedox.wss.app.appMode == Jedox.wss.grid.viewMode.USER ? ""
					: "-rzwrap"), pos, offsets)
};
Jedox.wss.wsel.img.showAlert = function(title, msg) {
	Ext.Msg.alert(title.localize(), msg.localize())
};
Jedox.wss.wsel.img.loadAll = function() {
	function _load(res) {
		if (!(res instanceof Array) || res[0] !== true) {
			return
		}
		res = res[1];
		var jwwsel = Jedox.wss.wsel, activeBook = Jedox.wss.app.activeBook, winId = activeBook
				.getDomId();
		for ( var imgData, i = res.length - 1; i >= 0; --i) {
			imgData = res[i];
			var rng = jwwsel.getRngFromNLoc(imgData.n_location), tlXY = activeBook
					.getPixelsByCoords(rng[0], rng[1]);
			this.createImg(false, winId, imgData.e_id, tlXY[1]
					+ imgData.pos_offsets[1], tlXY[0] + imgData.pos_offsets[0],
					imgData.size[0], imgData.size[1])
		}
	}
	Jedox.wss.backend.conn.cmd( [ this, _load ], [ "wget" ], [ "", [],
			[ "e_id", "n_location", "pos_offsets", "size" ], {
				e_type : "img"
			} ])
};
Jedox.wss.wsel.img.createImg = function(setWbWsID, winID, imgID, elTop, elLeft,
		elWidth, elHeight, setLoc) {
	if (imgID == null) {
		Ext.MessageBox.show( {
			title : "Operation Error".localize(),
			msg : "imgDlg_genError".localize(),
			buttons : Ext.MessageBox.OK,
			icon : Ext.MessageBox.ERROR
		});
		return
	}
	var activeBook = Jedox.wss.app.activeBook;
	var env = Jedox.wss.app.environment;
	var handleElementMove = false;
	var isUserMode = (env.viewMode == Jedox.wss.grid.viewMode.USER);
	var defMaxCoords = Jedox.wss.grid.defMaxCoords;
	var elConstr = {
		up : 0,
		down : 0,
		left : 0,
		right : 0,
		vportPos : [],
		lrCornerPx : []
	};
	var imgWrapperEl = null;
	var calcConstraints = function() {
		elConstr.vportPos = activeBook.getViewportPos();
		if (elLeft < 0 || elTop < 0) {
			elConstr.left = parseInt(elConstr.vportPos[0][0]
					+ (elConstr.vportPos[1][0] - elConstr.vportPos[0][0] - elWidth)
					/ 2);
			elConstr.up = parseInt(elConstr.vportPos[0][1]
					+ (elConstr.vportPos[1][1] - elConstr.vportPos[0][1] - elHeight)
					/ 2);
			elLeft = elConstr.left;
			elTop = elConstr.up
		} else {
			elConstr.left = elLeft;
			elConstr.up = elTop
		}
		elConstr.lrCornerPx = activeBook.getPixelsByCoords(defMaxCoords[0] + 1,
				defMaxCoords[1] + 1);
		elConstr.right = elConstr.lrCornerPx[0] - elConstr.left - elWidth;
		elConstr.down = elConstr.lrCornerPx[1] - elConstr.up - elHeight
	};
	var onImgMouseDown = function(e) {
		if (e.button == 2
				|| (Ext.isMac && e.button == 0 && Jedox.wss.app.ctrlKeyPressed)
				|| (Ext.isWebKit && e.button == 1)) {
			handleElementMove = false;
			showContextMenu(e);
			e.stopEvent()
		} else {
			var bEv = e.browserEvent;
			var el = (document.all) ? bEv.srcElement : bEv.target;
			el.className = "ws_element_move";
			var newVportPos = Jedox.wss.app.activeBook.getViewportPos();
			var newLrCornerPx = activeBook.getPixelsByCoords(
					defMaxCoords[0] + 1, defMaxCoords[1] + 1);
			if (env.winStateMax) {
				var vportDiff = {
					h : newVportPos[0][0] - elConstr.vportPos[0][0],
					v : newVportPos[0][1] - elConstr.vportPos[0][1]
				};
				var lrCornerPxDiff = {
					h : newLrCornerPx[0] - elConstr.lrCornerPx[0],
					v : newLrCornerPx[1] - elConstr.lrCornerPx[1]
				};
				imgWrapper.setXConstraint(elConstr.left + vportDiff.h,
						(elConstr.right - vportDiff.h) + lrCornerPxDiff.h);
				imgWrapper.setYConstraint(elConstr.up + vportDiff.v,
						(elConstr.down - vportDiff.v) + lrCornerPxDiff.v)
			} else {
				var leftConstr = elConstr.left
						- ((bEv.clientX - (el.parentNode.offsetLeft
								+ ((document.all) ? bEv.offsetX : bEv.layerX) - newVportPos[0][0])) - env.gridScreenCoordsMax[0])
						+ newVportPos[0][0];
				imgWrapper.setXConstraint(leftConstr, newLrCornerPx[0]
						- leftConstr - elWidth);
				var topConstr = elConstr.up
						- ((bEv.clientY - (el.parentNode.offsetTop
								+ ((document.all) ? bEv.offsetY : bEv.layerY) - newVportPos[0][1])) - env.gridScreenCoordsMax[1])
						+ newVportPos[0][1];
				imgWrapper.setYConstraint(topConstr, newLrCornerPx[1]
						- topConstr - elHeight)
			}
			Jedox.wss.wsel.moveRegistry.push( [ this, onImgMouseUp, el ]);
			handleElementMove = true
		}
	};
	var onImgMouseUp = function(el) {
		if (!handleElementMove) {
			return
		}
		el.className = "ws_element";
		var newLeft = el.parentNode.offsetLeft, newTop = el.parentNode.offsetTop, elWidth = el.parentNode.offsetWidth, elHeight = el.parentNode.offsetHeight;
		if (newLeft != elLeft || newTop != elTop) {
			elLeft = newLeft;
			elTop = newTop;
			var conn = Jedox.wss.backend.conn, imgData = {};
			imgData[imgID] = Jedox.wss.wsel.getNLoc(newLeft, newTop, elWidth,
					elHeight);
			conn.cmd(conn.dummy_cb, [ "wupd" ], [ "", imgData ])
		}
		handleElementMove = false
	};
	var afterResizeImg = function(wrapper, newWidth, newHeight, e) {
		var wrapperEl = wrapper.getEl(), elLeft = wrapperEl.getLeft(true), elTop = wrapperEl
				.getTop(true);
		if (elWidth != newWidth) {
			elConstr.right += elWidth - newWidth
		}
		if (elHeight != newHeight) {
			elConstr.down += elHeight - newHeight
		}
		var conn = Jedox.wss.backend.conn, imgData = {};
		imgData[imgID] = Jedox.wss.wsel.getNLoc(elLeft, elTop, newWidth,
				newHeight);
		imgData[imgID].size = [ newWidth, newHeight ];
		conn.cmd(conn.dummy_cb, [ "wupd" ], [ "", imgData ])
	};
	var showContextMenu = function(e) {
		var contextMenu = new Ext.menu.Menu( {
			items : [ {
				text : "Edit Picture".localize().concat("..."),
				iconCls : "icon_edit",
				handler : editImg
			}, {
				text : "Delete Picture".localize(),
				iconCls : "icon_delete",
				handler : deleteImg
			} ],
			enableScrolling : false,
			listeners : {
				hide : function(menu) {
					menu.destroy()
				}
			}
		});
		var coords = e.getXY();
		contextMenu.showAt( [ coords[0], coords[1] ])
	};
	function editImg() {
		Jedox.wss.dialog.openInsertPicture( {
			id : imgID
		})
	}
	function deleteImg_cb(res) {
		if (res instanceof Array && (res = res[0]) instanceof Array
				&& res[0] === true) {
			imgWrapperEl.remove()
		} else {
			Ext.MessageBox.show( {
				title : "Operation Error".localize(),
				msg : "imgDlg_deleteError".localize(),
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.ERROR
			})
		}
	}
	function deleteImg() {
		Jedox.wss.backend.conn.cmd( [ this, deleteImg_cb ], [ "wdel" ], [ "",
				[ imgID ] ])
	}
	var currDate = new Date();
	var gridDivId = activeBook.getDomId() + "_IC";
	calcConstraints();
	Ext.DomHelper.insertFirst(Ext.DomQuery.selectNode("div[id=" + gridDivId
			+ "]"), {
		tag : "img",
		id : "".concat(winID, "_ws_element_", imgID),
		src : "cc/gen_element.php?wam=".concat(Jedox.wss.app.appModeS,
				"&t=img", "&id=", imgID, "&ts=", currDate.getTime(),
				((setWbWsID) ? "".concat("&wbid=", activeBook.getWbId(),
						"&wsid=", activeBook.getWsId()) : "")),
		width : elWidth,
		height : elHeight,
		cls : "ws_element",
		style : "".concat("z-index: 50; position: absolute; left:", elLeft,
				"px; top:", elTop, "px;")
	}, false);
	Ext.ResizableConstrained = function(el, config) {
		Ext.ResizableConstrained.superclass.constructor.call(this, el, config)
	};
	Ext.extend(Ext.ResizableConstrained, Ext.Resizable, {
		setXConstraint : function(left, right) {
			this.dd.setXConstraint(left, right)
		},
		setYConstraint : function(up, down) {
			this.dd.setYConstraint(up, down)
		}
	});
	var imgWrapper = new Ext.ResizableConstrained("".concat(winID,
			"_ws_element_", imgID), {
		wrap : !isUserMode,
		dynamic : !isUserMode,
		pinned : false,
		width : elWidth,
		height : elHeight,
		minWidth : 10,
		maxWidth : 1600,
		minHeight : 10,
		maxHeight : 1050,
		preserveRatio : false,
		transparent : false,
		handles : "all",
		draggable : !isUserMode,
		enabled : !isUserMode,
		style : "background-color: white;",
		resizeRegion : Ext.get(gridDivId).getRegion()
	});
	if (!isUserMode) {
		imgWrapper.on( {
			resize : {
				fn : afterResizeImg,
				scope : this
			}
		});
		imgWrapper.dd.maintainOffset = true;
		imgWrapper.setXConstraint(elConstr.left, elConstr.right);
		imgWrapper.setYConstraint(elConstr.up, elConstr.down);
		imgWrapperEl = imgWrapper.getEl();
		imgWrapperEl.dom.style.backgroundColor = "#FFFFFF";
		imgWrapperEl.on( {
			mousedown : {
				fn : onImgMouseDown,
				scope : this
			},
			dblclick : {
				fn : editImg,
				scope : this
			}
		})
	}
	if (!setLoc) {
		return
	}
	var conn = Jedox.wss.backend.conn, imgData = {};
	imgData[imgID] = Jedox.wss.wsel.getNLoc(elLeft, elTop, elWidth, elHeight);
	conn.cmd(conn.dummy_cb, [ "wupd" ], [ "", imgData ])
};
Jedox.wss.wsel.img.removeImg = function(imgId) {
	if (imgId) {
		try {
			var activeBook = Jedox.wss.app.activeBook;
			var winId = activeBook.getDomId(), gridIC = activeBook.getBookIC();
			if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.DESIGNER) {
				gridIC.removeChild(document.getElementById("".concat(winId,
						"_ws_element_", imgId, "-rzwrap")));
				gridIC.removeChild(document.getElementById("".concat(winId,
						"_ws_element_", imgId, "-rzwrap-rzproxy")))
			} else {
				gridIC.removeChild(document.getElementById("".concat(winId,
						"_ws_element_", imgId)))
			}
		} catch (e) {
			console.log("Unable to remove Worksheet Element (picture).")
		}
	}
};
Jedox.wss.dialog.openInsertPicture = function(editObj) {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var validateFile = function(fieldValue) {
		var nameSize = fieldValue.length - 4;
		fieldValue = fieldValue.toUpperCase();
		if ((nameSize > 0)
				&& ((fieldValue.lastIndexOf(".PNG") == nameSize)
						|| (fieldValue.lastIndexOf(".GIF") == nameSize)
						|| (fieldValue.lastIndexOf(".JPG") == nameSize) || (fieldValue
						.lastIndexOf(".JPEG") == (nameSize - 1)))) {
			return true
		} else {
			return "impImg_msgWrongType".localize()
		}
	};
	var fileUpload = new Ext.ux.FileUploadField( {
		emptyText : "Select a picture".localize(),
		fieldLabel : "_lbl: picToImport".localize(),
		defaultAutoCreate : {
			tag : "input",
			type : "text",
			size : "65",
			autocomplete : "off"
		},
		width : 420,
		name : "img_filename",
		validator : validateFile
	});
	var formPanel = new Ext.form.FormPanel( {
		layout : "form",
		border : false,
		baseCls : "x-plain",
		labelWidth : 50,
		labelAlign : "top",
		width : 450,
		defaults : {
			width : 410
		},
		defaultType : "textfield",
		buttonAlign : "right",
		items : [ fileUpload ]
	});
	var mainPanel = new Ext.Panel( {
		baseCls : "main-panel",
		border : false,
		items : formPanel
	});
	var win = new Ext.Window(
			{
				defaults : {
					bodyStyle : "padding:10px 5px 5px 15px"
				},
				title : "Insert Picture".localize(),
				closable : true,
				closeAction : "close",
				cls : "default-format-window",
				autoDestroy : true,
				plain : true,
				constrain : true,
				modal : true,
				resizable : false,
				animCollapse : false,
				layout : "fit",
				width : 450,
				height : 140,
				items : mainPanel,
				listeners : {
					close : function() {
						Jedox.wss.general
								.setInputMode(Jedox.wss.app.lastInputModeDlg);
						Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
					}
				},
				buttons : [
						{
							text : "Insert".localize(),
							handler : function() {
								if (formPanel.getForm().isValid()) {
									var frameID = "tmpImportIFrame";
									var frame = Ext.get(frameID);
									if (!frame) {
										frame = document
												.createElement("iframe");
										frame.id = frameID;
										frame.name = frameID;
										frame.className = "x-hidden";
										if (Ext.isIE) {
											frame.src = Ext.SSL_SECURE_URL
										}
										document.body.appendChild(frame);
										if (Ext.isIE) {
											document.frames[frameID].name = frameID
										}
									}
									if (editObj && editObj.id) {
										var imgWrapper = Ext.get("".concat(
												Jedox.wss.app.activeBook
														.getDomId(),
												"_ws_element_", editObj.id,
												"-rzwrap"));
										var elX = imgWrapper.dom.offsetLeft;
										var elY = imgWrapper.dom.offsetTop
									} else {
										var vportPos = Jedox.wss.app.activeBook
												.getViewportPos();
										var elX = parseInt((vportPos[1][0] - vportPos[0][0]) / 2);
										var elY = parseInt((vportPos[1][1] - vportPos[0][1]) / 2)
									}
									var form = Ext.getDom(formPanel.getForm()
											.getEl());
									form.target = frameID;
									form.method = "POST";
									form.action = "cc/import.php?wam=designer&t=img&x_pos="
											+ elX
											+ "&y_pos="
											+ elY
											+ ((editObj && editObj.id) ? "&img_id="
													.concat(editObj.id)
													: "");
									form.enctype = form.encoding = "multipart/form-data";
									try {
										form.submit();
										Ext.MessageBox.show( {
											msg : "_msg: PaloImport Wait"
													.localize(),
											progressText : "Importing"
													.localize().concat("..."),
											width : 300,
											wait : true,
											waitConfig : {
												interval : 200
											}
										});
										win.close()
									} catch (e) {
										Jedox.wss.general.showMsg(
												"Application Error".localize(),
												e.message.localize(),
												Ext.MessageBox.ERROR)
									}
								}
							}
						}, {
							text : "Close".localize(),
							handler : function() {
								win.close()
							}
						} ]
			});
	win.show()
};