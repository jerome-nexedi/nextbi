Jedox.wss.palo.openPaloImport = function() {
	var phpPaloServerCbHandlers = {
		doImportCleanup : function(result) {
		}
	};
	var that = this, phpPaloServer = new Palo(phpPaloServerCbHandlers), _config = Jedox.wss.palo.config, servId;
	var timerOut, timerOutTime = 10000, timer;
	var win, panelMain, logoImg, btnClose, panelbtnClose, btnBack, btnFinish, panelbtnFinish, lblImportDesc, panelStart, panelMiddle, panelEnd, panelGrouped, lbl1Source, rb1Textfile, rb1ODBC, rb1Cube, rb1InternalLoop, rb21Tab, rb21Comma, rb21Semicolon, rb21Blank, rb21User_defined, txt21User_defined, validate21File, file21Upload, txt21Decimalpoint, chb21Header, chb3StepByStep, pbar3;
	var firstStep, stepPBarSize, doCleanUp;
	var _init = function() {
		doCleanUp = false;
		logoImg = new Ext.BoxComponent( {
			autoEl : {
				tag : "img",
				src : _config.imgsPath + "wizard_logo.png"
			}
		});
		validate21File = function(fieldValue) {
			var nameSize = fieldValue.length - 4;
			if ((nameSize > 0)
					&& ((fieldValue.lastIndexOf(".txt") == nameSize) || (fieldValue
							.lastIndexOf(".csv") == nameSize))) {
				return true
			} else {
				return "impPalo_msgWrongType".localize()
			}
		};
		file21Upload = new Ext.ux.FileUploadField( {
			hideLabel : true,
			emptyText : "impPalo_msgFieldBlank".localize(),
			defaultAutoCreate : {
				tag : "input",
				type : "text",
				size : "65",
				autocomplete : "off"
			},
			width : 350,
			name : "palo_import_file",
			validator : validate21File
		});
		pbar3 = new Ext.ProgressBar( {
			id : "palo_imp_pbar3",
			width : 500
		});
		btnClose = new Ext.Button( {
			text : "Close".localize(),
			ctCls : "stdButtons",
			listeners : {
				click : function() {
					win.close()
				}
			}
		});
		panelbtnClose = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnClose ]
		});
		btnBack = new Ext.Button( {
			disabled : true,
			ctCls : "stdButtons",
			text : "".concat("<< ").concat("Back".localize()),
			listeners : {
				click : _handlebtnBack
			}
		});
		panelbtnBack = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnBack ]
		});
		btnFinish = new Ext.Button( {
			text : "Next".localize().concat(" >>"),
			ctCls : "stdButtons",
			listeners : {
				click : _handlebtnFinish
			}
		});
		panelbtnFinish = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnFinish ]
		});
		txt21User_defined = new Ext.form.TextField( {
			name : "user_separator",
			disabled : true,
			allowBlank : true,
			width : 50,
			hideLabel : true
		});
		txt21Decimalpoint = new Ext.form.TextField( {
			name : "decimal_point",
			allowBlank : true,
			width : 15,
			fieldLabel : "Decimalpoint".localize(),
			value : "."
		});
		lblImportDesc = new Ext.form.MiscField( {
			value : "_msg: Palo Import 1".localize(),
			height : 22,
			bodyStyle : "background-color: transparent;",
			style : "font-weight: bold;",
			hideLabel : true
		});
		lbl1Source = new Ext.form.MiscField( {
			value : "Select Data Source".localize(),
			height : 22,
			bodyStyle : "background-color: transparent;",
			style : "font-weight: bold;",
			hideLabel : true
		});
		lbl21SourceFile = new Ext.form.MiscField( {
			value : "Select the sourcefile (*.txt, *.csv)".localize(),
			height : 22,
			bodyStyle : "background-color: transparent;",
			style : "font-weight: bold;",
			hideLabel : true
		});
		rb1Textfile = new Ext.form.Radio( {
			name : "radioImportType",
			boxLabel : "Flat Textfile (*.txt, *.csv)".localize(),
			hideLabel : true,
			checked : true
		});
		rb1ODBC = new Ext.form.Radio( {
			disabled : true,
			name : "radioImportType",
			boxLabel : "ODBC Query".localize(),
			hideLabel : true
		});
		rb1Cube = new Ext.form.Radio( {
			disabled : true,
			name : "radioImportType",
			boxLabel : "Cube".localize(),
			hideLabel : true
		});
		rb1InternalLoop = new Ext.form.Radio( {
			disabled : true,
			name : "radioImportType",
			boxLabel : "Internal Loop (increse A1 until error in B1)"
					.localize(),
			hideLabel : true
		});
		rb21Tab = new Ext.form.Radio( {
			name : "radioImportSeparateType",
			boxLabel : "Tab".localize(),
			hideLabel : true,
			inputValue : "tab",
			checked : true
		});
		rb21Comma = new Ext.form.Radio( {
			name : "radioImportSeparateType",
			boxLabel : "Comma".localize(),
			inputValue : "comma",
			hideLabel : true
		});
		rb21Semicolon = new Ext.form.Radio( {
			name : "radioImportSeparateType",
			boxLabel : "Semicolon".localize(),
			inputValue : "semi",
			hideLabel : true
		});
		rb21Blank = new Ext.form.Radio( {
			name : "radioImportSeparateType",
			boxLabel : "Blank".localize(),
			inputValue : "blank",
			hideLabel : true
		});
		rb21User_defined = new Ext.form.Radio( {
			name : "radioImportSeparateType",
			boxLabel : "User-defined".localize(),
			hideLabel : true,
			inputValue : "user",
			listeners : {
				check : function(thisRb, isChecked) {
					if (isChecked) {
						txt21User_defined.enable()
					} else {
						txt21User_defined.disable()
					}
				}
			}
		});
		chb21Header = new Ext.form.Checkbox( {
			name : "header",
			boxLabel : "Header exists".localize(),
			hideLabel : true
		});
		chb3StepByStep = new Ext.form.Checkbox( {
			hidden : true,
			boxLabel : "Step by Step".localize(),
			hideLabel : true,
			checked : true,
			listeners : {
				check : function(thisChb, isChecked) {
				}
			}
		});
		panelStart = new Ext.Panel( {
			border : false,
			layout : "form",
			bodyStyle : "background-color: transparent;",
			items : [ lbl1Source, {
				border : false,
				bodyStyle : "background-color:transparent;",
				html : "&nbsp;"
			}, rb1Textfile, rb1ODBC, rb1Cube, rb1InternalLoop ]
		});
		panelMiddle = new Ext.FormPanel( {
			border : false,
			layout : "form",
			bodyStyle : "background-color: transparent;",
			defaults : {
				anchor : "95%",
				allowBlank : false
			},
			items : [ lbl21SourceFile, file21Upload, {
				border : false,
				bodyStyle : "background-color:transparent;",
				html : "&nbsp;"
			}, new Ext.Panel( {
				border : false,
				layout : "column",
				bodyStyle : "background-color: transparent;",
				items : [ new Ext.Panel( {
					columnWidth : 0.5,
					border : false,
					layout : "form",
					bodyStyle : "background-color: transparent;",
					items : [ rb21Tab, rb21Comma, rb21Semicolon, {
						border : false,
						bodyStyle : "background-color:transparent;",
						html : "&nbsp;"
					}, txt21Decimalpoint, {
						border : false,
						bodyStyle : "background-color:transparent;",
						html : "&nbsp;"
					}, chb21Header ]
				}), new Ext.Panel( {
					columnWidth : 0.5,
					border : false,
					layout : "form",
					bodyStyle : "background-color: transparent;",
					items : [ rb21Blank, rb21User_defined, txt21User_defined ]
				}) ]
			}) ]
		});
		panelEnd = new Ext.FormPanel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			items : [ chb3StepByStep, pbar3 ]
		});
		panelGrouped = new Ext.Panel( {
			border : false,
			layout : "card",
			activeItem : 0,
			bodyStyle : "background-color: transparent;",
			defaults : {
				hideMode : "offsets"
			},
			items : [ panelStart, panelMiddle, panelEnd ]
		});
		panelMain = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			anchor : "100% 100%",
			monitorResize : true,
			listeners : {
				resize : _resizeAll
			},
			items : [ logoImg, lblImportDesc, panelGrouped, panelbtnBack,
					panelbtnFinish, panelbtnClose ]
		});
		win = new Ext.Window(
				{
					layout : "fit",
					title : "PALO Import Wizard".localize(),
					cls : "default-format-window",
					width : _config.paloWizWinW,
					height : _config.paloWizWinH,
					minWidth : _config.paloWizWinW,
					minHeight : _config.paloWizWinH,
					closeAction : "close",
					autoDestroy : true,
					plain : true,
					modal : true,
					resizable : false,
					listeners : {
						activate : _resizeAll,
						close : function() {
							if (Jedox.wss.palo.workIn) {
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
								Jedox.wss.app
										.unload(Jedox.wss.app.dynJSRegistry.paloImport)
							}
							if (Jedox.wss.palo.numOfUploadedLines) {
								delete Jedox.wss.palo.numOfUploadedLines
							}
							if (doCleanUp) {
								phpPaloServer.doImportCleanup()
							}
						}
					},
					items : [ panelMain ]
				});
		if (Jedox.wss.palo.workIn) {
			Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
			Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
		}
	};
	var _handlebtnBack = function() {
		if (panelMiddle.isVisible()) {
			lblImportDesc.setValue("_msg: Palo Import 1".localize());
			panelGrouped.layout.setActiveItem(0);
			btnBack.disable();
			btnFinish.setText("Next".localize().concat(" >>"));
			btnBack.setText("".concat("<< ").concat("Back".localize()))
		} else {
			if (Jedox.wss.palo.numOfUploadedLines) {
				if (firstStep) {
					stepPBarSize = pbar3.value;
					firstStep = false
				}
				Jedox.wss.palo.numOfUploadedLines--;
				if (Jedox.wss.palo.numOfUploadedLines) {
					pbar3.updateProgress(pbar3.value + stepPBarSize);
					Jedox.wss.app.activeBook.cb(
							"palo_handlerImportPaloDataFunc",
							[ Jedox.wss.palo.numOfUploadedLines ])
				}
				if ((Jedox.wss.palo.numOfUploadedLines == 1)
						|| (chb21Header.getValue() && (Jedox.wss.palo.numOfUploadedLines == 2))) {
					win.close()
				}
			} else {
				firstStep = true;
				doCleanUp = true;
				_do_upload("partial")
			}
		}
	};
	var _handlebtnFinish = function() {
		if (panelStart.isVisible()) {
			lblImportDesc.setValue("_msg: Palo Import 21".localize());
			panelGrouped.layout.setActiveItem(1);
			btnBack.enable()
		} else {
			if (panelMiddle.isVisible()) {
				lblImportDesc.setValue("_msg: Palo Import 3".localize());
				panelGrouped.layout.setActiveItem(2);
				btnFinish.setText("Finish".localize().concat(" >>"));
				btnBack.setText("Next".localize().concat(" >>"));
				win.setHeight(150)
			} else {
				_do_upload("full")
			}
		}
	};
	var _do_upload = function(type) {
		var frameID = "tmpImportIFrame";
		var frame = Ext.get(frameID);
		if (!frame) {
			frame = document.createElement("iframe");
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
		var form = Ext.getDom(panelMiddle.getForm().getEl());
		form.target = frameID;
		form.method = "POST";
		form.action = "cc/import.php?wam=designer&t=palo&f=textfile&o=" + type;
		form.enctype = form.encoding = "multipart/form-data";
		try {
			form.submit();
			if (type == "full") {
				Ext.MessageBox.show( {
					msg : "_msg: PaloImport Wait".localize(),
					progressText : "Importing".localize().concat("..."),
					width : 300,
					wait : true,
					waitConfig : {
						interval : 200
					}
				});
				doCleanUp = false;
				win.close()
			} else {
				Ext.MessageBox.show( {
					msg : "_msg: PaloImport Upload".localize(),
					progressText : "Uploading".localize().concat("..."),
					width : 300,
					wait : true,
					waitConfig : {
						interval : 200
					}
				})
			}
		} catch (e) {
			Jedox.wss.general.showMsg("Application Error".localize(), e.message
					.localize(), Ext.MessageBox.ERROR)
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
			var lineH = 23;
			var marginSize = 3;
			var pSize = panelMain.getSize();
			var w = pSize.width;
			var h = pSize.height;
			var logoSize = {
				width : 160,
				height : 400
			};
			panelGrouped.setPosition(logoSize.width + 2 * lineH + marginSize, 2
					* lineH + marginSize);
			logoImg.setPosition(0, 0);
			if (lblImportDesc.rendered) {
				lblImportDesc.setPosition(logoSize.width + 5 * marginSize, 5)
			}
			if (panelbtnClose.rendered && btnClose.rendered) {
				var wClose = btnClose.getEl().getBox().width;
				panelbtnClose.setPosition(w - wClose - 3, (h - 30) + 5);
				if (panelbtnFinish.rendered && btnFinish.rendered) {
					var wFinish = btnFinish.getEl().getBox().width;
					panelbtnFinish.setPosition(w - wClose - wFinish - 8,
							(h - 30) + 5);
					if (panelbtnBack.rendered && btnBack.rendered) {
						panelbtnBack.setPosition(w - wClose - wFinish
								- btnBack.getEl().getBox().width - 13,
								(h - 30) + 5)
					}
				}
			}
		}
	};
	_init();
	win.show()
};