Jedox.wss.wsel.chart = {
	type_sizeDefs : {
		xlColumnClustered : "defHor",
		xlColumnStacked : "defHor",
		xlColumnStacked100 : "defHor",
		xl3DColumnClustered : "defHor",
		xl3DColumnStacked : "defHor",
		xl3DColumnStacked100 : "defHor",
		xlCylinderColClustered : "defHor",
		xlCylinderColStacked : "defHor",
		xlCylinderColStacked100 : "defHor",
		xlLine : "defHor",
		xlLineStacked : "defHor",
		xlLineStacked100 : "defHor",
		xlLineRotated : "defVer",
		xlLineMarkers : "defHor",
		xlLineMarkersStacked : "defHor",
		xlLineMarkersStacked100 : "defHor",
		xlLineMarkersRotated : "defVer",
		xl3DLine : "defHor",
		xlPie : "defHor",
		xl3DPie : "defHor",
		xlPieExploded : "defHor",
		xl3DPieExploded : "defHor",
		xlBarClustered : "defHor",
		xlBarStacked : "defHor",
		xlBarStacked100 : "defHor",
		xl3DBarClustered : "defHor",
		xl3DBarStacked : "defHor",
		xl3DBarStacked100 : "defHor",
		xlCylinderBarClustered : "defHor",
		xlCylinderBarStacked : "defHor",
		xlCylinderBarStacked100 : "defHor",
		xlArea : "defHor",
		xlAreaStacked : "defHor",
		xlAreaStacked100 : "defHor",
		xl3DArea : "defHor",
		xl3DAreaStacked : "defHor",
		xl3DAreaStacked100 : "defHor",
		xlXYScatter : "defHor",
		xlXYScatterSmooth : "defHor",
		xlXYScatterSmoothNoMarkers : "defHor",
		xlXYScatterLines : "defHor",
		xlXYScatterLinesNoMarkers : "defHor",
		xlStockHLC : "defHor",
		xlStockOHLC : "defHor",
		xlDoughnut : "defHor",
		xlDoughnutExploded : "defHor",
		xlBubble : "defHor",
		xlBubble3DEffect : "defHor",
		xlRadar : "defHor",
		xlRadarMarkers : "defHor",
		xlRadarFilled : "defHor",
		xlMeterOdoFull : "equal",
		xlMeterOdoFull100 : "equal",
		xlMeterOdoHalf : "odoHalf",
		xlMeterOdoHalf100 : "odoHalf",
		xlMeterAngularWide : "angulWide",
		xlMeterLineHorizontal : "lineHor",
		xlMeterLineVertical : "lineVer"
	},
	min_sizeFactor : 100,
	min_whRatio : {
		defHor : [ 2.5, 1 ],
		defVer : [ 1, 2.5 ],
		equal : [ 1.5, 1.5 ],
		odoHalf : [ 1.5, 1 ],
		angulWide : [ 1.5, 0.7 ],
		lineHor : [ 2, 0.7 ],
		lineVer : [ 0.8, 2 ]
	},
	max_width : 700,
	max_height : 700,
	whRatio : {
		defHor : [ 4, 3 ],
		defVer : [ 3, 4 ],
		equal : [ 2, 2 ],
		odoHalf : [ 2, 1.5 ],
		angulWide : [ 2.5, 0.9 ],
		lineHor : [ 3, 0.8 ],
		lineVer : [ 1, 3 ]
	}
};
Jedox.wss.wsel.chart.reMovize = function(chartId, pos, offsets) {
	var activeBook = Jedox.wss.app.activeBook;
	var winId = activeBook.getDomId();
	var tlSize = Jedox.wss.wsel.getTLSize(pos, offsets);
	var w = tlSize.size[0], h = tlSize.size[1];
	var wrapElem = Ext
			.get(""
					.concat(
							winId,
							"_ws_element_",
							chartId,
							(document.all || Jedox.wss.app.appMode != Jedox.wss.grid.viewMode.USER) ? "-rzwrap"
									: ""));
	wrapElem.dom.style.left = "".concat(tlSize.tl[0], "px");
	wrapElem.dom.style.top = "".concat(tlSize.tl[1], "px");
	var whRatio = Jedox.wss.wsel.chart.min_whRatio[Jedox.wss.wsel.chart.type_sizeDefs[Ext
			.get("".concat(winId, "_ws_element_", chartId)).elemSubtype]];
	w = (whRatio[0] * Jedox.wss.wsel.chart.min_sizeFactor > w) ? whRatio[0]
			* Jedox.wss.wsel.chart.min_sizeFactor
			: ((Jedox.wss.wsel.chart.max_width < w) ? Jedox.wss.wsel.chart.max_width
					: w);
	h = (whRatio[1] * Jedox.wss.wsel.chart.min_sizeFactor > h) ? whRatio[1]
			* Jedox.wss.wsel.chart.min_sizeFactor
			: ((Jedox.wss.wsel.chart.max_height < h) ? Jedox.wss.wsel.chart.max_height
					: h);
	if (tlSize._error || (w != tlSize.size[0]) || (h != tlSize.size[1])) {
		Ext.Msg.show( {
			title : "Incorect Chart Size".localize(),
			msg : "".concat("infoChart_wrongSize".localize(), "."),
			buttons : Ext.Msg.OK,
			icon : Ext.MessageBox.INFO
		});
		tlSize._error = true
	}
	if ((wrapElem.dom.offsetWidth != w) || (wrapElem.dom.offsetHeight != h)
			|| (pos[0] == pos[2]) || (pos[1] == pos[3])) {
		if (tlSize._error) {
			var chartData = Jedox.wss.wsel.getNLoc(tlSize.tl[0], tlSize.tl[1],
					w, h);
			var res = Jedox.wss.backend.ha.wsel("resize_chart", {
				id : chartId,
				n_location : chartData.n_location,
				pos_offsets : chartData.pos_offsets,
				size : [ w, h ]
			})
		}
		Ext.get("".concat(winId, "_ws_element_", chartId)).setSize(w, h);
		wrapElem.setSize(w, h);
		var currDate = new Date();
		document.getElementById("".concat(winId, "_ws_element_", chartId)).src = "cc/gen_element.php?wam="
				.concat(Jedox.wss.app.appModeS, "&id=", chartId, "&w=", w,
						"&h=", h, "&ts=", currDate.getTime())
	}
};
Jedox.wss.dialog.chart.createChart = function(setWbWsID, winID, chartID,
		n_location, pos_offsets, size, chartType) {
	if (chartID == null) {
		Ext.MessageBox.show( {
			title : "Operation Error".localize(),
			msg : "chartDlg_genError".localize(),
			buttons : Ext.MessageBox.OK,
			icon : Ext.MessageBox.ERROR
		});
		return
	}
	var chartTypesSizeDefs = Jedox.wss.wsel.chart.type_sizeDefs, chartMinSizeFactor = Jedox.wss.wsel.chart.min_sizeFactor, chartsMinWidthHeightRatio = Jedox.wss.wsel.chart.min_whRatio;
	var activeBook = Jedox.wss.app.activeBook, env = Jedox.wss.app.environment;
	var handleElementMove = false, isUserMode = (env.viewMode == Jedox.wss.grid.viewMode.USER), defMaxCoords = Jedox.wss.grid.defMaxCoords, elConstr = {
		up : 0,
		down : 0,
		left : 0,
		right : 0,
		vportPos : [],
		lrCornerPx : []
	}, chartWrapperEl = null;
	var tlSize = Jedox.wss.wsel.getTLSize(Jedox.wss.wsel
			.getRngFromNLoc(n_location), pos_offsets);
	var elWidth = tlSize.size[0], elHeight = tlSize.size[1], elLeft = tlSize.tl[0], elTop = tlSize.tl[1];
	var whRatio = Jedox.wss.wsel.chart.min_whRatio[Jedox.wss.wsel.chart.type_sizeDefs[chartType]];
	elWidth = (whRatio[0] * Jedox.wss.wsel.chart.min_sizeFactor > elWidth) ? whRatio[0]
			* Jedox.wss.wsel.chart.min_sizeFactor
			: ((Jedox.wss.wsel.chart.max_width < elWidth) ? Jedox.wss.wsel.chart.max_width
					: elWidth);
	elHeight = (whRatio[1] * Jedox.wss.wsel.chart.min_sizeFactor > elHeight) ? whRatio[1]
			* Jedox.wss.wsel.chart.min_sizeFactor
			: ((Jedox.wss.wsel.chart.max_height < elHeight) ? Jedox.wss.wsel.chart.max_height
					: elHeight);
	if (tlSize._error || (elWidth != tlSize.size[0])
			|| (elHeight != tlSize.size[1])) {
		Ext.Msg.show( {
			title : "Incorect Chart Size".localize(),
			msg : "".concat("infoChart_wrongSize2".localize(), "."),
			buttons : Ext.Msg.OK,
			icon : Ext.MessageBox.INFO
		});
		var chartData = Jedox.wss.wsel.getNLoc(tlSize.tl[0], tlSize.tl[1],
				elWidth, elHeight);
		var res = Jedox.wss.backend.ha.wsel("resize_chart", {
			id : chartID,
			n_location : chartData.n_location,
			pos_offsets : chartData.pos_offsets,
			size : [ elWidth, elHeight ]
		})
	}
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
	var onChartMouseDown = function(e) {
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
				chartWrapper.setXConstraint(elConstr.left + vportDiff.h,
						(elConstr.right - vportDiff.h) + lrCornerPxDiff.h);
				chartWrapper.setYConstraint(elConstr.up + vportDiff.v,
						(elConstr.down - vportDiff.v) + lrCornerPxDiff.v)
			} else {
				var leftConstr = elConstr.left
						- ((bEv.clientX - (el.parentNode.offsetLeft
								+ ((document.all) ? bEv.offsetX : bEv.layerX) - newVportPos[0][0])) - env.gridScreenCoordsMax[0])
						+ newVportPos[0][0];
				chartWrapper.setXConstraint(leftConstr, newLrCornerPx[0]
						- leftConstr - elWidth);
				var topConstr = elConstr.up
						- ((bEv.clientY - (el.parentNode.offsetTop
								+ ((document.all) ? bEv.offsetY : bEv.layerY) - newVportPos[0][1])) - env.gridScreenCoordsMax[1])
						+ newVportPos[0][1];
				chartWrapper.setYConstraint(topConstr, newLrCornerPx[1]
						- topConstr - elHeight)
			}
			Jedox.wss.wsel.moveRegistry.push( [ this, onChartMouseUp, el ]);
			handleElementMove = true
		}
	};
	var onChartMouseUp = function(el) {
		if (handleElementMove) {
			el.className = "ws_element";
			var newLeft = el.parentNode.offsetLeft, newTop = el.parentNode.offsetTop, elWidth = el.parentNode.offsetWidth, elHeight = el.parentNode.offsetHeight;
			if ((newLeft != elLeft) || (newTop != elTop)) {
				setTimeout(function() {
					elLeft = newLeft;
					elTop = newTop;
					var chartData = {};
					chartData[chartID] = Jedox.wss.wsel.getNLoc(newLeft,
							newTop, elWidth, elHeight);
					Jedox.wss.backend.conn
							.cmd(0, [ "wupd" ], [ "", chartData ])
				}, 0)
			}
			handleElementMove = false
		}
	};
	var beforeResizeChart = function(wrapper, e) {
		var resizeChild = wrapper.getResizeChild();
		var whRatio = chartsMinWidthHeightRatio[chartTypesSizeDefs[resizeChild.elemSubtype]];
		wrapper.minWidth = whRatio[0] * chartMinSizeFactor;
		wrapper.minHeight = whRatio[1] * chartMinSizeFactor
	};
	var afterResizeChart = function(wrapper, newWidth, newHeight, e) {
		var wrapperEl = wrapper.getEl();
		elLeft = wrapperEl.getLeft(true);
		elTop = wrapperEl.getTop(true);
		if (elWidth != newWidth) {
			elConstr.right += elWidth - newWidth
		}
		if (elHeight != newHeight) {
			elConstr.down += elHeight - newHeight
		}
		var chartData = Jedox.wss.wsel.getNLoc(elLeft, elTop, newWidth,
				newHeight);
		var res = Jedox.wss.backend.ha.wsel("resize_chart", {
			id : chartID,
			n_location : chartData.n_location,
			pos_offsets : chartData.pos_offsets,
			size : [ newWidth, newHeight ]
		});
		var currDate = new Date();
		document.getElementById("".concat(winID, "_ws_element_", chartID)).src = "cc/gen_element.php?wam="
				.concat(Jedox.wss.app.appModeS, "&id=", chartID, "&w=",
						newWidth, "&h=", newHeight, "&ts=", currDate.getTime())
	};
	var showContextMenu = function(e) {
		var contextMenu = new Ext.menu.Menu( {
			id : "chartContextMenu",
			cls : "default-format-window",
			enableScrolling : false,
			listeners : {
				hide : function(menu) {
					menu.destroy()
				}
			},
			items : [ {
				text : "Change Chart Type".localize().concat("..."),
				iconCls : "icon_insert_chart",
				handler : editChartType
			}, {
				text : "Select Source Data".localize().concat("..."),
				iconCls : "chart_source_data",
				handler : editSourceData
			}, {
				text : "Format Chart Properties".localize().concat("..."),
				iconCls : "icon_edit",
				handler : editChart
			}, {
				text : "Delete Chart".localize(),
				iconCls : "icon_delete",
				handler : deleteChart
			} ]
		});
		var coords = e.getXY();
		contextMenu.showAt( [ coords[0], coords[1] ])
	};
	function editChart() {
		Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.editChart, [ "edit",
				chartID ])
	}
	function editChartType() {
		Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.editChartType, [ "edit",
				chartID ])
	}
	function editSourceData() {
		Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.editSourceData, [
				"edit", chartID ])
	}
	function deleteChart() {
		if (Jedox.wss.backend.ha.wsel("delete_element", chartID)) {
			chartWrapperEl.remove()
		} else {
			Ext.MessageBox.show( {
				title : "Operation Error".localize(),
				msg : "chartDlg_deleteError".localize(),
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.ERROR
			})
		}
	}
	var currDate = new Date();
	var gridDivId = activeBook.getDomId() + "_IC";
	calcConstraints();
	Ext.DomHelper.insertFirst(Ext.DomQuery.selectNode("div[id=" + gridDivId
			+ "]"), {
		tag : "img",
		id : "".concat(winID, "_ws_element_", chartID),
		src : "cc/gen_element.php?wam=".concat(Jedox.wss.app.appModeS, "&id=",
				chartID, "&w=", elWidth, "&h=", elHeight, "&ts=", currDate
						.getTime(), ((setWbWsID) ? "".concat("&wbid=",
						activeBook.getWbId(), "&wsid=", activeBook.getWsId())
						: "")),
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
	var chartWrapper = new Ext.ResizableConstrained("".concat(winID,
			"_ws_element_", chartID), {
		wrap : document.all || !isUserMode,
		dynamic : !isUserMode,
		pinned : false,
		width : elWidth,
		height : elHeight,
		minWidth : 250,
		maxWidth : Jedox.wss.wsel.chart.max_width,
		minHeight : 100,
		maxHeight : Jedox.wss.wsel.chart.max_height,
		preserveRatio : false,
		transparent : false,
		handles : (document.all && isUserMode) ? "none" : "all",
		draggable : !isUserMode,
		enabled : !isUserMode,
		style : "background-color: white;",
		resizeRegion : Ext.get(gridDivId).getRegion()
	});
	Ext.get("".concat(winID, "_ws_element_", chartID)).elemSubtype = chartType;
	if (!isUserMode) {
		chartWrapper.on( {
			resize : {
				fn : afterResizeChart,
				scope : this
			},
			beforeresize : {
				fn : beforeResizeChart,
				scope : this
			}
		});
		chartWrapper.dd.maintainOffset = true;
		chartWrapper.setXConstraint(elConstr.left, elConstr.right);
		chartWrapper.setYConstraint(elConstr.up, elConstr.down);
		chartWrapperEl = chartWrapper.getEl();
		chartWrapperEl.dom.style.backgroundColor = "#FFFFFF";
		chartWrapperEl.on( {
			mousedown : {
				fn : onChartMouseDown,
				scope : this
			},
			dblclick : {
				fn : editChart,
				scope : this
			}
		})
	}
};