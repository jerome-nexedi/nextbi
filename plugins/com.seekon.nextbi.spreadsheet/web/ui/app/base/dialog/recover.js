Jedox.wss.dialog.openRecoverDialog = function(asList, loadArgs) {
	var env = Jedox.wss.app.environment, listDateFormat = "d.m.Y H:i:s", radioOrig, radioRecov, grid;
	if (env != null) {
		Jedox.wss.app.lastInputModeDlg = env.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
	}
	var mainPanel = new Ext.Panel( {
		modal : true,
		layout : "form",
		baseCls : "x-plain",
		bodyStyle : "padding: 5px 5px 0",
		border : false,
		items : [
				new Ext.Panel( {
					autoHeight : true,
					baseCls : "x-plain",
					style : "margin: 10px 0px; word-wrap: break-word;",
					items : [ {
						html : "as_msg".localize(),
						baseCls : "x-plain"
					} ]
				}),
				(radioOrig = new Ext.form.Radio( {
					checked : true,
					name : "ftype",
					boxLabel : "astype_orig".localize( {
						date : Date.parseDate(asList.orig.date, listDateFormat)
								.format("date_format".localize()),
						size : Ext.util.Format.fileSize(asList.orig.size)
					}),
					hideLabel : true
				})),
				(radioRecov = new Ext.form.Radio( {
					checked : false,
					name : "ftype",
					boxLabel : "astype_recov".localize(),
					hideLabel : true,
					listeners : {
						check : function(cmp, checked) {
							if (checked) {
								var selModel = grid.getSelectionModel();
								if (!selModel.hasSelection()) {
									selModel.selectFirstRow()
								}
							}
						}
					}
				})),
				(grid = new Ext.grid.GridPanel(
						{
							id : "asgrid",
							store : new Ext.data.ArrayStore( {
								fields : [ {
									name : "idx",
									type : "string"
								}, {
									name : "date",
									type : "date",
									dateFormat : listDateFormat
								}, {
									name : "size",
									type : "int"
								} ],
								data : asList.recov
							}),
							columns : [
									{
										id : "asDate",
										header : "Date".localize(),
										width : 200,
										resizable : true,
										sortable : true,
										renderer : Ext.util.Format
												.dateRenderer("date_format"
														.localize()),
										dataIndex : "date"
									}, {
										header : "Size".localize(),
										width : 150,
										resizable : true,
										sortable : true,
										renderer : Ext.util.Format.fileSize,
										dataIndex : "size"
									} ],
							hideLabel : true,
							stripeRows : true,
							autoExpandColumn : "asDate",
							height : 220,
							width : 350,
							viewConfig : {
								forceFit : true
							},
							header : false,
							tabindex : 1,
							anchor : "100%",
							selModel : new Ext.grid.RowSelectionModel( {
								singleSelect : true,
								listeners : {
									rowselect : function() {
										if (!radioRecov.getValue()) {
											radioRecov.setValue(true)
										}
									}
								}
							})
						})) ]
	});
	var win = new Ext.Window(
			{
				id : "recoverDlg",
				title : "Open and Repair".localize(),
				closable : true,
				cls : "default-format-window",
				closeAction : "close",
				autoDestroy : true,
				plain : true,
				constrain : true,
				modal : true,
				resizable : false,
				animCollapse : false,
				layout : "fit",
				width : 450,
				height : 400,
				items : mainPanel,
				_execClose : true,
				listeners : {
					beforedestroy : function(panel) {
						mainPanel.items.each(function(f) {
							f.purgeListeners();
							Ext.destroy(f)
						});
						mainPanel.purgeListeners();
						mainPanel.destroy();
						var env = Jedox.wss.app.environment;
						if (env
								&& env.inputMode == Jedox.wss.grid.GridMode.DIALOG) {
							Jedox.wss.general
									.setInputMode(Jedox.wss.app.lastInputModeDlg);
							Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
						}
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.recover)
					},
					destroy : function(cmp) {
						if (win._execClose) {
							try {
								for ( var triggers = Jedox.wss.events.triggers.closeWorkbook_before, i = triggers.length - 1, wbMeta = asList.orig.meta; i >= 0; --i) {
									triggers[i][0]["closeWorkbook_before"]
											.call(parent, triggers[i][1],
													wbMeta.ghn, wbMeta.name)
								}
								for ( var triggers = Jedox.wss.events.triggers.closeWorkbook_after, i = triggers.length - 1, wbMeta = asList.orig.meta; i >= 0; --i) {
									triggers[i][0]["closeWorkbook_after"].call(
											parent, triggers[i][1], wbMeta.ghn,
											wbMeta.name)
								}
							} catch (e) {
								Jedox.wss.general.showMsg("Application Error"
										.localize(), e.message.localize(),
										Ext.MessageBox.ERROR)
							}
						}
					}
				},
				buttons : [
						{
							text : "OK".localize(),
							handler : function() {
								var asIdx = "";
								if (radioRecov.getValue()) {
									var selModel = grid.getSelectionModel();
									if (selModel.getCount()) {
										asIdx = selModel.getSelected().get(
												"idx")
									}
								}
								if (loadArgs[4]) {
									if (typeof loadArgs[4] == "object") {
										loadArgs[4].asid = asIdx
									} else {
										loadArgs[4] = {
											asid : asIdx
										}
									}
								} else {
									var loadArgsCopy = [];
									for ( var i = 0, largs = loadArgs, lalen = loadArgs.length; i < lalen; i++) {
										loadArgsCopy.push(largs[i])
									}
									loadArgs = loadArgsCopy;
									loadArgs.splice(4, 0, {
										asid : asIdx
									})
								}
								Jedox.wss.book.recover(win, loadArgs)
							}
						}, {
							text : "Cancel".localize(),
							handler : function() {
								win.close()
							}
						} ]
			});
	win.show(this)
};