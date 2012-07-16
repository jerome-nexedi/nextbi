Jedox.wss.palo.openPaloWizard = function(conf) {
	var pw = new Jedox.wss.palo.PaloWizard(conf);
	pw.init();
	pw.show()
};
Jedox.wss.palo.PaloWizard = function(conf) {
	var phpPaloServerCbHandlers = {
		getServList : function(result) {
			if (result[0]) {
				storeServers.loadData(result[1]);
				var tmpRec = storeServers.getAt(0);
				cmbServers.setValue(tmpRec.get("name"));
				servId = tmpRec.get("id");
				if (tmpRec.get("connected") == 1) {
					phpPaloServer.getDBs(servId)
				} else {
					btnDisconnect.setText("Connect".localize());
					_disableDbTabs()
				}
			} else {
				_showErrorMsg(result[1])
			}
		},
		getDBs : function(result) {
			if (result[0]) {
				storeDb.removeAll();
				var tmpRec;
				for ( var i = (result[1].length - 1); i > 0; i--) {
					tmpRec = new DbRecord( {
						name : result[1][i]
					});
					storeDb.add(tmpRec)
				}
				if (storeDb.getCount() > 0) {
					cmbDb.setValue(storeDb.getAt(0).get("name"))
				} else {
					cmbDb.setValue("")
				}
			} else {
				_showErrorMsg(result[1])
			}
		},
		createNewDb : function(result) {
			if (!result[2]) {
				if (result[0]) {
					Ext.MessageBox.show( {
						title : "Information".localize(),
						msg : "_msg: Db added".localize(),
						buttons : Ext.Msg.OK,
						icon : Ext.MessageBox.INFO,
						fn : function() {
							if (startedAsPanel) {
								txtNewDbName.setValue("");
								phpPaloServer.getDBs(servId)
							} else {
								win.close()
							}
						}
					})
				} else {
					_showErrorMsg("_err: Db not added".localize())
				}
			} else {
				_showErrorMsg(result[3])
			}
		},
		deleteDb : function(result) {
			if (!result[2]) {
				if (result[0]) {
					Ext.MessageBox.show( {
						title : "Information".localize(),
						msg : "_msg: Db deleted".localize(),
						buttons : Ext.Msg.OK,
						icon : Ext.MessageBox.INFO,
						fn : function() {
							if (startedAsPanel) {
								phpPaloServer.getDBs(servId)
							} else {
								win.close()
							}
						}
					})
				} else {
					_showErrorMsg("_err: Db not deleted".localize())
				}
			} else {
				_showErrorMsg(result[3])
			}
		},
		testConnection : function(result) {
			if (result) {
				Ext.MessageBox.show( {
					title : "Information".localize(),
					msg : "_msg: Test Conn Ok".localize(),
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.INFO
				})
			} else {
				Ext.MessageBox.show( {
					title : "Error".localize(),
					msg : "_err: Test Conn Not Ok".localize(),
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.ERROR
				})
			}
		},
		createNewServer : function(result) {
			if (!result[2]) {
				if (result[0]) {
					Ext.MessageBox.show( {
						title : "Information".localize(),
						msg : "_msg: Server added".localize(),
						buttons : Ext.Msg.OK,
						icon : Ext.MessageBox.INFO,
						fn : function() {
							win.close()
						}
					})
				} else {
					_showErrorMsg("_err: Server not added".localize())
				}
			} else {
				_showErrorMsg(result[3])
			}
		},
		deleteServer : function(result) {
			if (!result[2]) {
				if (result[0]) {
					Ext.MessageBox.show( {
						title : "Information".localize(),
						msg : "_msg: Server deleted".localize(),
						buttons : Ext.Msg.OK,
						icon : Ext.MessageBox.INFO,
						fn : function() {
							win.close()
						}
					})
				} else {
					_showErrorMsg("_err: Server not deleted".localize())
				}
			} else {
				_showErrorMsg(result[3])
			}
		},
		connDisconnServer : function(result) {
			if (result[0]) {
				var tmpRec = storeServers
						.getAt(storeServers.find("id", servId));
				if (tmpRec) {
					if (tmpRec.get("connected") == 1) {
						tmpRec.set("connected", 0);
						btnDisconnect.setText("Connect".localize());
						_disableDbTabs()
					} else {
						tmpRec.set("connected", 1);
						btnDisconnect.setText("Disconnect".localize());
						phpPaloServer.getDBs(servId);
						_enableDbTabs()
					}
				}
			} else {
				_showErrorMsg(result[1])
			}
		},
		getServerData : function(result) {
			if (!result[2]) {
				editServId = result[1][0];
				txtEditNewConnName.setValue(result[1][4]);
				txtEditServerName.setValue(result[1][5]);
				txtEditPort.setValue(result[1][6]);
				txtEditUsername.setValue(result[1][7]);
				txtEditPassword.setValue("");
				txtEditPassword2.setValue("")
			} else {
				_showErrorMsg(result[3])
			}
		},
		editServer : function(result) {
			if (!result[2]) {
				if (result[0]) {
					storeServers.getAt(
							storeServers.find("name", cmbServers.getValue()))
							.set("name", result[1][4]);
					cmbServers.setValue(result[1][4]);
					Ext.MessageBox.show( {
						title : "Information".localize(),
						msg : "_msg: Server edited".localize(),
						buttons : Ext.Msg.OK,
						icon : Ext.MessageBox.INFO,
						fn : function() {
							win.close()
						}
					})
				} else {
					_showErrorMsg("_err: Server not edited".localize())
				}
			} else {
				_showErrorMsg(result[3])
			}
		}
	};
	var that = this;
	var phpPaloServer = new Palo(phpPaloServerCbHandlers);
	var _config = Jedox.wss.palo.config;
	var servId;
	var startedAsPanel = false;
	var timerOut;
	var timerOutTime = 10000;
	var timer;
	var btnCancel, btnFinish, btnDisconnect, lblWizardName, lblWizardDesc, win, panelMain, logoImg, panelCancelButton, panelFinishButton, panelbtnDisconnect, tabPanel, paneltxtNewDbName, txtNewDbName, panelcmbDb, panelCreateDb, panelDeleteDb, panelCreateServer, panelDeleteServer, cmbDb, txtNewConnName, txtServerName, txtPort, txtUsername, txtPassword, txtPassword2, chbStoreLogin, btnTestConn, panelFormNewServer, lblDeleteServer, panelEditServer, txtEditNewConnName, txtEditServerName, txtEditPort, txtEditUsername, txtEditPassword, txtEditPassword2, chbEditStoreLogin, btnEditTestConn, panelFormEditServer, editServId;
	var ServerRecord = new Ext.data.Record.create( [ {
		name : "id"
	}, {
		name : "parent_id"
	}, {
		name : "connected"
	}, {
		name : "type"
	}, {
		name : "name"
	}, {
		name : "host"
	}, {
		name : "port"
	}, {
		name : "username"
	}, {
		name : "password"
	} ]);
	var storeServers = new Ext.data.SimpleStore( {
		fields : [ {
			name : "id"
		}, {
			name : "parent_id"
		}, {
			name : "connected"
		}, {
			name : "type"
		}, {
			name : "name"
		}, {
			name : "host"
		}, {
			name : "port"
		}, {
			name : "username"
		}, {
			name : "password"
		} ]
	});
	var DbRecord = new Ext.data.Record.create( [ {
		name : "name"
	} ]);
	var storeDb = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name"
		} ]
	});
	this.init = function() {
		phpPaloServer.getServList();
		editServId = -1;
		logoImg = new Ext.BoxComponent( {
			autoEl : {
				tag : "img",
				src : _config.imgsPath + "wizard_logo.png"
			}
		});
		btnCancel = new Ext.Button( {
			text : "Cancel".localize(),
			ctCls : "stdButtons",
			listeners : {
				click : function() {
					win.close()
				}
			}
		});
		panelCancelButton = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnCancel ]
		});
		btnFinish = new Ext.Button(
				{
					text : ((startedAsPanel) ? "OK".localize() : "Finish >>"
							.localize()),
					ctCls : "stdButtons",
					listeners : {
						click : _handlebtnFinish
					}
				});
		panelFinishButton = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnFinish ]
		});
		btnDisconnect = new Ext.Button( {
			text : "Disconnect".localize(),
			ctCls : "stdButtons",
			listeners : {
				click : function() {
					var tmpRec = storeServers.getAt(storeServers.find("id",
							servId));
					if (tmpRec) {
						phpPaloServer.connDisconnServer( [ servId,
								tmpRec.get("connected") ? 0 : 1 ])
					}
				}
			}
		});
		panelbtnDisconnect = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			autoWidth : true,
			autoHeight : true,
			items : [ btnDisconnect ]
		});
		lblWizardName = new Ext.form.MiscField(
				{
					value : "_tit: Palo Wizard".localize(),
					height : 22,
					bodyStyle : "background-color: transparent;",
					style : "color:white; font-size:20; font-weight: bold; font-family: arial;",
					hideLabel : true
				});
		lblWizardDesc = new Ext.form.MiscField( {
			value : "_msg: Palo Wizard".localize(),
			height : 22,
			bodyStyle : "background-color: transparent;",
			style : "font-weight: bold;",
			hideLabel : true
		});
		cmbServers = new Ext.form.ComboBox(
				{
					store : storeServers,
					fieldLabel : "Select connection".localize(),
					bodyStyle : "background-color: transparent;",
					typeAhead : true,
					selectOnFocus : true,
					editable : false,
					forceSelection : true,
					triggerAction : "all",
					mode : "local",
					tpl : new Ext.XTemplate(
							'<tpl for=".">',
							'<div class="x-combo-list-item">',
							"<tpl if=\"type == 'database'\">",
							'<span style="cursor: default;">&#160;&#160;&#160;{name}</span>',
							"</tpl>", "<tpl if=\"type == 'server'\">",
							'<span style="cursor: default;">{name}</span>',
							"</tpl>", "</div>", "</tpl>"),
					listeners : {
						select : function(combo, record, index) {
							storeDb.removeAll();
							cmbDb.setValue("");
							servId = record.get("id");
							if (record.get("connected") == 1) {
								_enableDbTabs();
								btnDisconnect.setText("Disconnect".localize());
								phpPaloServer.getDBs(servId)
							} else {
								_disableDbTabs();
								btnDisconnect.setText("Connect".localize())
							}
							if ((tabPanel.getActiveTab().getId() == panelEditServer
									.getId())
									&& (editServId != servId)) {
								phpPaloServer.getServerData(servId)
							}
						}
					},
					valueField : "name",
					displayField : "name"
				});
		panelcmbServers = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			labelWidth : 200,
			autoHeight : true,
			items : [ cmbServers ]
		});
		txtNewDbName = new Ext.form.TextField( {
			fieldLabel : "New Database Name".localize(),
			width : 200,
			labelStyle : "width: 200px;"
		});
		paneltxtNewDbName = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoHeight : true,
			items : [ txtNewDbName ]
		});
		panelCreateDb = new Ext.Panel( {
			title : "New Database".localize(),
			layout : "absolute",
			border : false,
			bodyStyle : "background-color: transparent;",
			items : [ paneltxtNewDbName ]
		});
		cmbDb = new Ext.form.ComboBox( {
			store : storeDb,
			fieldLabel : "Select database to delete".localize(),
			labelStyle : "width: 200px;",
			bodyStyle : "background-color: transparent;",
			typeAhead : true,
			selectOnFocus : true,
			editable : false,
			forceSelection : true,
			triggerAction : "all",
			mode : "local",
			valueField : "name",
			displayField : "name"
		});
		panelcmbDb = new Ext.Panel( {
			labelWidth : 200,
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoHeight : true,
			items : [ cmbDb ]
		});
		panelDeleteDb = new Ext.Panel( {
			title : "Delete Database".localize(),
			layout : "absolute",
			border : false,
			bodyStyle : "background-color: transparent;",
			items : [ panelcmbDb ]
		});
		txtNewConnName = new Ext.form.TextField( {
			fieldLabel : "Connection Name".localize(),
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtServerName = new Ext.form.TextField( {
			fieldLabel : "Server".localize(),
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtPort = new Ext.form.TextField( {
			fieldLabel : "Port".localize(),
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtUsername = new Ext.form.TextField( {
			fieldLabel : "Username".localize(),
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtPassword = new Ext.form.TextField( {
			fieldLabel : "Password".localize(),
			inputType : "password",
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtPassword2 = new Ext.form.TextField( {
			fieldLabel : "Confirm Password".localize(),
			inputType : "password",
			width : 200,
			labelStyle : "width: 200px;"
		});
		chbStoreLogin = new Ext.form.Checkbox( {
			fieldLabel : "",
			labelSeparator : "",
			labelStyle : "width: 200px;",
			boxLabel : "Store login data".localize()
		});
		btnTestConn = new Ext.Button( {
			text : "Test Connection".localize(),
			listeners : {
				click : function() {
					if (txtPassword.getValue() == txtPassword2.getValue()) {
						phpPaloServer
								.testConnection( [ txtServerName.getValue(),
										txtPort.getValue(),
										txtUsername.getValue(),
										txtPassword.getValue() ])
					} else {
						_showErrorMsg("_err: Wrong Pass".localize())
					}
				}
			}
		});
		panelFormNewServer = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoHeight : true,
			items : [ txtNewConnName, txtServerName, txtPort, txtUsername,
					txtPassword, txtPassword2, chbStoreLogin, btnTestConn ]
		});
		panelCreateServer = new Ext.Panel( {
			title : "New Server".localize(),
			layout : "absolute",
			border : false,
			bodyStyle : "background-color: transparent;",
			items : [ panelFormNewServer ]
		});
		txtEditNewConnName = new Ext.form.TextField( {
			fieldLabel : "Connection Name".localize(),
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtEditServerName = new Ext.form.TextField( {
			fieldLabel : "Server".localize(),
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtEditPort = new Ext.form.TextField( {
			fieldLabel : "Port".localize(),
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtEditUsername = new Ext.form.TextField( {
			fieldLabel : "Username".localize(),
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtEditPassword = new Ext.form.TextField( {
			fieldLabel : "Password".localize(),
			inputType : "password",
			width : 200,
			labelStyle : "width: 200px;"
		});
		txtEditPassword2 = new Ext.form.TextField( {
			fieldLabel : "Confirm Password".localize(),
			inputType : "password",
			width : 200,
			labelStyle : "width: 200px;"
		});
		chbEditStoreLogin = new Ext.form.Checkbox( {
			fieldLabel : "",
			labelSeparator : "",
			labelStyle : "width: 200px;",
			boxLabel : "Store login data".localize()
		});
		btnEditTestConn = new Ext.Button( {
			text : "Test Connection".localize(),
			listeners : {
				click : function() {
					if (txtEditPassword.getValue() == txtEditPassword2
							.getValue()) {
						phpPaloServer.testConnection( [
								txtEditServerName.getValue(),
								txtEditPort.getValue(),
								txtEditUsername.getValue(),
								txtEditPassword.getValue() ])
					} else {
						_showErrorMsg("_err: Wrong Pass".localize())
					}
				}
			}
		});
		panelFormEditServer = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "form",
			autoHeight : true,
			items : [ txtEditNewConnName, txtEditServerName, txtEditPort,
					txtEditUsername, txtEditPassword, txtEditPassword2,
					chbEditStoreLogin, btnEditTestConn ]
		});
		panelEditServer = new Ext.Panel( {
			title : "Edit Server".localize(),
			layout : "absolute",
			border : false,
			bodyStyle : "background-color: transparent;",
			listeners : {
				activate : function() {
					if (editServId != servId) {
						phpPaloServer.getServerData(servId)
					}
				}
			},
			items : [ panelFormEditServer ]
		});
		lblDeleteServer = new Ext.form.MiscField( {
			value : "_msg: Palo Wizard Delete Server".localize(),
			height : 22,
			bodyStyle : "background-color: transparent;",
			style : "font-weight: bold;",
			hideLabel : true
		});
		panelDeleteServer = new Ext.Panel( {
			title : "Delete Server".localize(),
			layout : "absolute",
			border : false,
			bodyStyle : "background-color: transparent;",
			items : [ lblDeleteServer ]
		});
		tabPanel = new Ext.TabPanel( {
			activeTab : 0,
			bodyStyle : "background-color: transparent;",
			plain : true,
			defaults : {
				autoScroll : true
			},
			items : [ panelCreateDb, panelDeleteDb ],
			listeners : {
				tabchange : function(tabPanel, panel) {
					panel.doLayout()
				}
			}
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
			items : [ logoImg, tabPanel, panelcmbServers, panelCancelButton,
					panelFinishButton, lblWizardName, lblWizardDesc,
					panelbtnDisconnect ]
		});
		win = new Ext.Window( {
			layout : "fit",
			cls : "default-format-window",
			title : "PALO Wizard".localize(),
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
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.paloWizard)
					}
					if (conf && conf.fn) {
						conf.fn()
					}
				}
			},
			items : [ panelMain ]
		})
	};
	this.show = function() {
		win.show()
	};
	var _handlebtnFinish = function() {
		var selectedPan = tabPanel.getActiveTab();
		if (selectedPan.getId() == panelCreateDb.getId()) {
			if (Jedox.wss.palo.utils.checkPaloName(txtNewDbName.getValue(),
					"db")) {
				phpPaloServer.createNewDb( [ servId, txtNewDbName.getValue() ])
			} else {
				Ext.MessageBox.show( {
					title : "Information".localize(),
					msg : ("Name".localize()).concat(' "', txtNewDbName
							.getValue().replace("<", "&lt;"), '" ',
							"is not allowed".localize()),
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.INFO
				})
			}
		} else {
			if (selectedPan.getId() == panelCreateServer.getId()) {
				if ((txtPassword.getValue() == txtPassword2.getValue())
						&& (txtServerName.getValue() != "")) {
					if ((txtNewConnName.getValue() != "")
							&& (txtPort.getValue() != "")
							&& (txtServerName.getValue() != "")
							&& (txtUsername.getValue() != "")) {
						phpPaloServer
								.createNewServer( [ txtNewConnName.getValue(),
										txtServerName.getValue(),
										txtPort.getValue(),
										txtUsername.getValue(),
										txtPassword.getValue() ])
					}
				}
			} else {
				if (selectedPan.getId() == panelDeleteDb.getId()) {
					Ext.MessageBox.show( {
						title : "Delete Database".localize(),
						msg : "_msg: Delete Database".localize(),
						buttons : Ext.MessageBox.OKCANCEL,
						fn : function(btn) {
							if (btn == "ok") {
								phpPaloServer.deleteDb( [ servId,
										cmbDb.getValue() ])
							}
						},
						icon : Ext.MessageBox.QUESTION
					})
				} else {
					if (selectedPan.getId() == panelEditServer.getId()) {
						if ((txtEditPassword.getValue() == txtEditPassword2
								.getValue())
								&& (txtEditServerName.getValue() != "")) {
							if ((txtEditNewConnName.getValue() != "")
									&& (txtEditPort.getValue() != "")
									&& (txtEditServerName.getValue() != "")
									&& (txtEditUsername.getValue() != "")) {
								phpPaloServer.editServer( [ servId,
										txtEditNewConnName.getValue(),
										txtEditServerName.getValue(),
										txtEditPort.getValue(),
										txtEditUsername.getValue(),
										txtEditPassword.getValue() ])
							}
						}
					} else {
						if (selectedPan.getId() == panelDeleteServer.getId()) {
							Ext.MessageBox.show( {
								title : "Delete Server".localize(),
								msg : "_msg: Delete Server".localize(),
								buttons : Ext.MessageBox.OKCANCEL,
								fn : function(btn) {
									if (btn == "ok") {
										phpPaloServer.deleteServer( [ servId ])
									}
								},
								icon : Ext.MessageBox.QUESTION
							})
						}
					}
				}
			}
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
	var _showErrorMsg = function(message, props) {
		Ext.MessageBox.show( {
			title : "Error".localize(),
			msg : message.localize(props),
			buttons : Ext.Msg.OK,
			icon : Ext.MessageBox.ERROR
		})
	};
	var _disableDbTabs = function() {
		tabPanel.getComponent(0).disable();
		tabPanel.getComponent(1).disable();
		var activeTab = tabPanel.getActiveTab();
		if ((activeTab.id == tabPanel.getComponent(0).id)
				|| ((activeTab.id == tabPanel.getComponent(1).id))) {
			tabPanel.setActiveTab(tabPanel.getComponent(2))
		}
	};
	var _enableDbTabs = function() {
		tabPanel.getComponent(0).enable();
		tabPanel.getComponent(1).enable()
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
			logoImg.setPosition(0, 0);
			tabPanel.setPosition(logoSize.width + marginSize, 3 * lineH + 7
					* marginSize);
			tabPanel.setSize(w - logoSize.width - 2 * marginSize, h - 5 * lineH
					- 3 * marginSize);
			lblWizardName.setPosition(30, 150);
			panelcmbServers.setPosition(logoSize.width + marginSize, 2 * lineH
					+ 3 * marginSize);
			panelcmbServers.setSize(w - logoSize.width - 2 * marginSize,
					2 * lineH);
			if (lblWizardDesc.rendered) {
				var tmpTM = Ext.util.TextMetrics
						.createInstance(lblWizardDesc.id);
				var widthDesc = tmpTM.getWidth("_msg: Palo Wizard".localize());
				lblWizardDesc.setPosition(Math.round(logoSize.width
						+ (w - logoSize.width - widthDesc - 5) / 2), 5)
			}
			if (panelFinishButton.rendered && btnFinish.rendered) {
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
			if (btnDisconnect.rendered) {
				panelbtnDisconnect.setPosition(
						w - btnDisconnect.getEl().getBox().width - 2
								* marginSize, 2 * lineH + 3 * marginSize)
			}
			paneltxtNewDbName.setSize(w - logoSize.width - 6 * marginSize,
					lineH);
			paneltxtNewDbName.setPosition(2 * marginSize, lineH);
			panelcmbDb.setSize(w - logoSize.width - 6 * marginSize, lineH);
			panelcmbDb.setPosition(2 * marginSize, lineH);
			panelFormNewServer.setSize(w - logoSize.width - 6 * marginSize,
					lineH);
			panelFormNewServer.setPosition(2 * marginSize, lineH);
			panelFormEditServer.setSize(w - logoSize.width - 6 * marginSize,
					lineH);
			panelFormEditServer.setPosition(2 * marginSize, lineH);
			lblDeleteServer.setPosition(2 * marginSize, lineH)
		}
	};
	this.setPanelMode = function() {
		startedAsPanel = true
	};
	this.getDialogPanel = function() {
		var that = this;
		var panelM;
		var _resizeM = function() {
			if (panelM.rendered) {
				panelM.getEl().unselectable();
				var lineH = 23;
				var marginSize = 3;
				var pSize = panelM.getSize();
				var w = pSize.width;
				var h = pSize.height;
				tabPanel.setPosition(marginSize, lineH + 3 * marginSize);
				tabPanel.setSize(w - 2 * marginSize, h - 2 * lineH - 6
						* marginSize);
				panelcmbServers.setPosition(marginSize, 2 * marginSize);
				panelcmbServers.setSize(w - 2 * marginSize, lineH);
				if (panelFinishButton.rendered && btnFinish.rendered) {
					panelFinishButton.setPosition(w
							- btnFinish.getEl().getBox().width - 3,
							(h - 30) + 5)
				}
				if (panelbtnDisconnect.rendered && btnDisconnect.rendered) {
					panelbtnDisconnect.setPosition(w
							- btnDisconnect.getEl().getBox().width - 2
							* marginSize, 2 * marginSize)
				}
				paneltxtNewDbName.setSize(w - 6 * marginSize, lineH);
				paneltxtNewDbName.setPosition(2 * marginSize, lineH);
				panelcmbDb.setSize(w - 6 * marginSize, lineH);
				panelcmbDb.setPosition(2 * marginSize, lineH)
			}
		};
		var panelM = new Ext.Panel( {
			border : false,
			bodyStyle : "background-color: transparent;",
			layout : "absolute",
			anchor : "100% 100%",
			monitorResize : true,
			listeners : {
				resize : _resizeM
			},
			items : [ tabPanel, panelcmbServers, panelFinishButton,
					panelbtnDisconnect ]
		});
		var tmpT = setTimeout(function() {
			_resizeM()
		}, 0);
		return panelM
	}
};