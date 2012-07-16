Jedox.wss.palo.CubeWizard = function(parentModeller, serv_id, db_name) {
	var phpPaloServerCbHandlers = {
		getDims : function(result) {
			if (!result[2]) {
				var tmpRec;
				for ( var i = 0; i < result[1].length; i++) {
					tmpRec = new DimRecord( {
						name : result[1][i]
					});
					storeLeft.add(tmpRec)
				}
			} else {
				_showErrorMsg(result[3])
			}
		},
		addCube : function(result) {
			if (!timerOut) {
				clearTimeout(timer);
				if (!result[2]) {
					_hideWaitMsg();
					timerOut = true;
					Ext.MessageBox
							.show( {
								title : "Information".localize(),
								msg : "_msg: Cube added".localize(),
								buttons : Ext.Msg.OK,
								icon : Ext.MessageBox.INFO,
								fn : function(btn) {
									if (btn == "ok") {
										if (parent) {
											var dims = new Array();
											for ( var i = 0; i < storeRight
													.getCount(); i++) {
												dims[i] = storeRight.getAt(i)
														.get("name")
											}
											parent.addNewCube(txtName
													.getValue(), dims)
										}
										win.close()
									}
								}
							})
				} else {
					_hideWaitMsg();
					timerOut = true;
					_showErrorMsg(result[3])
				}
			}
		}
	};
	var that = this;
	var phpPaloServer = new Palo(phpPaloServerCbHandlers);
	var parent = parentModeller;
	var _config = Jedox.wss.palo.config;
	var servId, dbName, indexOfSelect;
	var timerOut;
	var timerOutTime = 10000;
	var timer;
	var viewLeft, viewRight, btnCancel, btnFinish, btnMids, lblLeft, lblRight, lblWizardName, lblWizardDesc, txtName;
	var win, panelMain, panelDataViewLeft, panelDataViewRight, logoImg, panelCancelButton, panelFinishButton, panelTxtName, panelMid;
	var DimRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var storeLeft = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	var storeRight = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	this.init = function(serv_id, db_name) {
		servId = serv_id;
		dbName = db_name;
		viewLeft = new Ext.DataView( {
			itemSelector : "div.row-modeller",
			style : "overflow:auto",
			multiSelect : true,
			store : storeLeft,
			cls : "modellerDataViewSelect",
			tpl : new Ext.XTemplate('<tpl for=".">',
					'<div class="row-modeller">',
					"<span>&#160;{name}</span></div>", "</tpl>"),
			listeners : {
				click : function(dataView, index, node, evnt) {
					_singleClickLeftViewHandler(dataView, index, node, evnt)
				},
				dblclick : function(dataView, index, node, evnt) {
					_dblClickLeftViewHandler(dataView, index, node, evnt)
				}
			}
		});
		viewRight = new Ext.DataView( {
			itemSelector : "div.row-modeller",
			style : "overflow:auto",
			multiSelect : true,
			store : storeRight,
			cls : "modellerDataViewSelect",
			tpl : new Ext.XTemplate('<tpl for=".">',
					'<div class="row-modeller">',
					"<span>&#160;{name}</span></div>", "</tpl>"),
			listeners : {
				click : function(dataView, index, node, evnt) {
					_singleClickRightViewHandler(dataView, index, node, evnt)
				},
				dblclick : function(dataView, index, node, evnt) {
					_dblClickRightViewHandler(dataView, index, node, evnt)
				}
			}
		});
		panelDataViewLeft = new Ext.Panel( {
			layout : "fit",
			items : [ viewLeft ]
		});
		panelDataViewRight = new Ext.Panel( {
			layout : "fit",
			items : [ viewRight ]
		});
		logoImg = new Ext.BoxComponent( {
			autoEl : {
				tag : "img",
				src : _config.imgsPath + "wizard_logo.png"
			}
		});
		btnMids = {
			up : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				iconCls : "palo_icon_up",
				style : "margin-top: 2px; margin-bottom: 2px;",
				handler : _upBtnHandler
			}),
			right : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				iconCls : "palo_icon_right",
				handler : _rightBtnHandler
			}),
			left : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				iconCls : "palo_icon_left",
				handler : _leftBtnHandler
			}),
			down : new Ext.Button( {
				disabled : true,
				cls : "modellerImageButton",
				style : "margin-top: 2px; margin-bottom: 2px;",
				iconCls : "palo_icon_down",
				handler : _downBtnHandler
			})
		};
		panelMid = new Ext.Panel(
				{
					border : false,
					bodyStyle : "background-color: transparent; vertical-align: middle;",
					layout : "form",
					width : 31,
					autoHeight : true,
					items : [ btnMids.up, btnMids.right, btnMids.left,
							btnMids.down ]
				});
		btnCancel = new Ext.Button( {
			text : "Cancel".localize(),
			ctCls : "subsetEditorBtns",
			listeners : {
				click : function() {
					if ((txtName.getValue() != "")
							|| (storeRight.getCount() > 0)) {
						Ext.MessageBox.show( {
							title : "Close Cube Wizard".localize(),
							msg : "_msg: close Cube Wizard".localize(),
							buttons : Ext.MessageBox.YESNO,
							fn : function(btn) {
								if (btn == "yes") {
									win.close()
								}
							},
							icon : Ext.MessageBox.QUESTION
						})
					} else {
						win.close()
					}
				}
			}
		});
		btnFinish = new Ext.Button( {
			text : "Finish >>".localize(),
			ctCls : "subsetEditorBtns",
			listeners : {
				click : _storeNewCube
			}
		});
		lblLeft = new Ext.form.MiscField( {
			value : "Available Dimensions".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		lblRight = new Ext.form.MiscField( {
			value : "Selected Dimensions".localize() + ":",
			height : 22,
			bodyStyle : "background-color: transparent;",
			hideLabel : true
		});
		lblWizardName = new Ext.form.MiscField(
				{
					value : "_tit: Cube Creation Wizard".localize(),
					height : 22,
					bodyStyle : "background-color: transparent;",
					style : "color:white; font-size:20; font-weight: bold; font-family: arial;",
					hideLabel : true
				});
		lblWizardDesc = new Ext.form.MiscField( {
			value : "_msg: Cube Wizard".localize(),
			height : 22,
			bodyStyle : "background-color: transparent;",
			style : "font-weight: bold;",
			hideLabel : true
		});
		txtName = new Ext.form.TextField( {
			fieldLabel : "Cube name".localize(),
			labelSeparator : ":",
			labelStyle : "font-weight:bold;",
			hideLabel : false,
			width : "95%"
		});
		panelTxtName = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoHeight : true,
			items : [ txtName ]
		});
		panelCancelButton = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnCancel ]
		});
		panelFinishButton = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnFinish ]
		});
		panelMain = new Ext.Panel( {
			id : "cw_mainPanel",
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			anchor : "100% 100%",
			monitorResize : true,
			listeners : {
				resize : _resizeAll
			},
			items : [ logoImg, panelDataViewLeft, panelDataViewRight,
					panelCancelButton, panelFinishButton, lblLeft, lblRight,
					panelTxtName, lblWizardName, lblWizardDesc, panelMid ]
		});
		win = new Ext.Window( {
			id : "cubeWizardWindow",
			layout : "fit",
			title : "PALO Cube Wizard".localize(),
			cls : "default-format-window",
			width : _config.cubeWizWinW,
			height : _config.cubeWizWinH,
			minWidth : _config.cubeWizWinW,
			minHeight : _config.cubeWizWinH,
			closeAction : "close",
			autoDestroy : true,
			plain : true,
			modal : true,
			resizable : false,
			listeners : {
				activate : _resizeAll,
				close : function() {
					Jedox.wss.app
							.unload(Jedox.wss.app.dynJSRegistry.cubeWizard)
				}
			},
			items : [ panelMain ]
		})
	};
	this.show = function() {
		phpPaloServer.getDims(servId, dbName, "data");
		win.show()
	};
	var _upBtnHandler = function() {
		if (indexOfSelect > 0) {
			var tmpElem = storeRight.getAt(indexOfSelect);
			storeRight.remove(tmpElem);
			indexOfSelect--;
			storeRight.insert(indexOfSelect, tmpElem);
			viewRight.select(indexOfSelect)
		}
	};
	var _rightBtnHandler = function() {
		var tmpIndexes = viewLeft.getSelectedIndexes();
		for ( var i = 0; i < tmpIndexes.length; i++) {
			_dblClickLeftViewHandler(viewLeft, tmpIndexes[i], viewLeft
					.getNode(tmpIndexes[i]), null)
		}
	};
	var _leftBtnHandler = function() {
		if (indexOfSelect != -1) {
			var tmpIndexes = viewRight.getSelectedIndexes();
			for ( var i = (tmpIndexes.length - 1); i >= 0; i--) {
				_dblClickRightViewHandler(viewRight, tmpIndexes[i], viewRight
						.getNode(tmpIndexes[i]), null)
			}
			indexOfSelect = -1
		}
	};
	var _downBtnHandler = function() {
		if ((indexOfSelect != -1)
				&& (indexOfSelect < (storeRight.getCount() - 1))) {
			var tmpElem = storeRight.getAt(indexOfSelect);
			storeRight.remove(tmpElem);
			indexOfSelect++;
			storeRight.insert(indexOfSelect, tmpElem);
			viewRight.select(indexOfSelect)
		}
	};
	var _singleClickLeftViewHandler = function(dataView, index, node, evnt) {
		viewRight.clearSelections();
		btnMids.up.disable();
		btnMids.down.disable();
		btnMids.left.disable();
		btnMids.right.enable()
	};
	var _dblClickLeftViewHandler = function(dataView, index, node, evnt) {
		var rowElem = storeLeft.getAt(index);
		if (storeRight.find("name", rowElem.data.name) == -1) {
			var newRowRecord = new DimRecord( {
				name : storeLeft.getAt(index).get("name")
			});
			storeRight.add(newRowRecord)
		}
	};
	var _singleClickRightViewHandler = function(dataView, index, node, evnt) {
		indexOfSelect = index;
		viewLeft.clearSelections();
		btnMids.up.enable();
		btnMids.down.enable();
		btnMids.left.enable();
		btnMids.right.disable()
	};
	var _dblClickRightViewHandler = function(dataView, index, node, evnt) {
		var parentElem = storeRight.getAt(index);
		storeRight.remove(parentElem);
		btnMids.up.disable();
		btnMids.down.disable();
		btnMids.left.disable()
	};
	var _storeNewCube = function() {
		if ((txtName.getValue() != "") && (storeRight.getCount() > 0)) {
			if (Jedox.wss.palo.utils.checkPaloName(txtName.getValue(), "cube")) {
				var dims = new Array();
				for ( var i = 0; i < storeRight.getCount(); i++) {
					dims[i] = storeRight.getAt(i).get("name")
				}
				phpPaloServer.addCube(servId, dbName, txtName.getValue(), dims);
				_showWaitMsg("_msg: new Cube".localize(), "Storing".localize()
						.concat("..."))
			} else {
				Ext.MessageBox.show( {
					title : "Information".localize(),
					msg : ("Name".localize()).concat(' "', txtName.getValue()
							.replace("<", "&lt;"), '" ', "is not allowed"
							.localize()),
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.INFO
				})
			}
		} else {
			Ext.MessageBox.show( {
				title : "Warning".localize(),
				msg : "_err: create new Cube".localize(),
				buttons : Ext.Msg.OK,
				icon : Ext.MessageBox.WARNING
			})
		}
	};
	var _showWaitMsg = function(msgText, onBarText) {
		Ext.MessageBox.show( {
			msg : msgText,
			progressText : onBarText,
			width : 300,
			wait : true,
			waitConfig : {
				interval : 200
			}
		});
		timerOut = false;
		timer = setTimeout(function() {
			if (!timerOut) {
				timerOut = true;
				_hideWaitMsg()
			}
		}, timerOutTime)
	};
	var _hideWaitMsg = function() {
		Ext.MessageBox.hide();
		if (timerOut) {
			Ext.MessageBox.show( {
				title : "Error".localize(),
				msg : "_err: Timer".localize(),
				buttons : Ext.Msg.OK,
				icon : Ext.MessageBox.ERROR
			})
		}
	};
	var _showErrorMsg = function(message) {
		Ext.MessageBox.show( {
			title : "Error".localize(),
			msg : message,
			buttons : Ext.Msg.OK,
			icon : Ext.MessageBox.ERROR
		})
	};
	var _resizeAll = function() {
		if (panelMain.rendered) {
			var hTop = 25;
			var whButtonMid = 26;
			var hBottom = 30;
			var pSize = panelMain.getSize();
			var w = pSize.width;
			var h = pSize.height;
			var logoSize = {
				width : 160,
				height : 400
			};
			var panelDataViewsW = (w - _config.widthMidButtons - logoSize.width - 5) / 2;
			var panelDataViewsH = (h - 3 * hTop - hBottom) - 3;
			panelDataViewLeft.setWidth(panelDataViewsW);
			panelDataViewLeft.setHeight(panelDataViewsH - 2 * hTop);
			panelDataViewLeft.setPosition(logoSize.width + 5, 5 * hTop);
			panelDataViewRight.setWidth(panelDataViewsW);
			panelDataViewRight.setHeight(panelDataViewsH - 2 * hTop);
			panelDataViewRight.setPosition(w - panelDataViewsW - 3, 5 * hTop);
			panelMid.setPosition(logoSize.width + panelDataViewsW + 9,
					panelDataViewsH / 2 - whButtonMid * 2 + 4 * hTop);
			logoImg.setPosition(0, 0);
			lblLeft.setPosition(logoSize.width + 5, 5 + 4 * hTop);
			lblRight.setPosition(w - panelDataViewsW - 3, 5 + 4 * hTop);
			lblWizardName.setPosition(30, 150);
			if (lblWizardDesc.rendered) {
				var tmpTM = Ext.util.TextMetrics
						.createInstance(lblWizardDesc.id);
				var widthDesc = tmpTM.getWidth("_msg: Cube Wizard".localize());
				lblWizardDesc.setPosition(logoSize.width
						+ (w - logoSize.width - widthDesc - 5) / 2, 5)
			}
			panelTxtName.setPosition(logoSize.width + 5, 2 * hTop + 5);
			panelTxtName.setWidth(panelDataViewsW);
			if (panelFinishButton.rendered && btnFinish.rendered) {
				var buttonFinishSize = panelFinishButton.getSize();
				panelFinishButton.setPosition(w
						- btnFinish.getEl().getBox().width - 3, (h - 30) + 5);
				if (panelCancelButton.rendered && btnCancel.rendered) {
					var buttonCancelSize = panelCancelButton.getSize();
					panelCancelButton.setPosition(w
							- btnCancel.getEl().getBox().width
							- btnFinish.getEl().getBox().width - 8,
							(h - 30) + 5)
				}
			}
		}
	};
	this.init(serv_id, db_name);
	this.show()
};