Jedox.wss.dialog.openInsertFunctionDialog = function() {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var onBeforeDestroyDialog = function(panel) {
		insFuncForm.items.each(function(f) {
			f.purgeListeners();
			Ext.destroy(f)
		});
		insFuncForm.purgeListeners();
		insFuncForm.destroy();
		if (Jedox.wss.app.environment.inputMode == Jedox.wss.grid.GridMode.DIALOG) {
			Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
			Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
		}
		Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.insertFunction)
	};
	var onBeforeShowDialog = function(panel) {
		getFunctionsList(Ext.getCmp("category").getValue)
	};
	var onCategorySelect = function(combo, record, index) {
		getFunctionsList(combo.getValue())
	};
	var onRowSelect = function(selModel, rowIndex, record) {
		setFunctionDescriptions(record)
	};
	var getFunctionsList = function(category) {
		var functionsGrid = Ext.getCmp("funcgrid");
		var gridSelModel = functionsGrid.getSelectionModel();
		if (category != "ALL") {
			functionsGrid.store.filter("catid", category)
		} else {
			functionsGrid.store.clearFilter();
			functionsGrid.store.sort("funcname")
		}
		gridSelModel.selectFirstRow();
		if (gridSelModel.getCount() > 0) {
			setFunctionDescriptions(gridSelModel.getSelected())
		}
	};
	var activeFnc;
	var setFunctionDescriptions = function(record) {
		var i;
		var formulaText = record.get("funcsyntax");
		activeFnc = record.get("funcname");
		Jedox.wss.app.selectedFormula = formulaText;
		var defPan = Ext.getCmp("funcdef");
		defPan.removeAll();
		defPan.add( {
			html : formulaText,
			baseCls : "x-plain"
		});
		defPan.doLayout();
		var dscPan = Ext.getCmp("funcdesc");
		dscPan.removeAll();
		dscPan.add( {
			html : record.get("funcdesc"),
			baseCls : "x-plain"
		});
		dscPan.doLayout()
	};
	var doInsertFunction = function() {
		var ffn = functions.lookup_func[activeFnc];
		var ff = functions.funcs[ffn];
		dialogWindow.close();
		Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.funcArgs, [ ff ])
	};
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/cc/fn_desc/".concat(Jedox.wss.i18n.L10n, ".json"), false);
	xhr.send(null);
	var functions = Ext.util.JSON.decode(xhr.responseText);
	functions.cats.unshift( [ "ALL", "All".localize() ]);
	var insFuncForm = new Ext.FormPanel(
			{
				baseCls : "x-plain",
				labelWidth : 110,
				labelAlign : "left",
				frame : true,
				bodyStyle : "padding:5px 5px 0",
				width : 400,
				defaultType : "textfield",
				buttonAlign : "right",
				header : false,
				monitorValid : true,
				items : [
						{
							xtype : "combo",
							id : "category",
							fieldLabel : "Select a category".localize(),
							store : new Ext.data.SimpleStore( {
								fields : [ "catid", "catname" ],
								data : functions.cats
							}),
							displayField : "catname",
							valueField : "catid",
							mode : "local",
							triggerAction : "all",
							listWidth : 260,
							width : 230,
							editable : false,
							allowBlank : false,
							selectOnFocus : false,
							tabIndex : 1,
							anchor : "100%",
							listeners : {
								render : function() {
									if (this.store.getTotalCount() > 0) {
										this.setValue(this.store.getAt(1).get(
												"catid"))
									}
								},
								select : {
									fn : onCategorySelect,
									scope : this
								}
							}
						},
						new Ext.form.MiscField( {
							fieldLabel : "Select a function".localize(),
							id : "funcLabel"
						}),
						new Ext.grid.GridPanel( {
							id : "funcgrid",
							cls : "insfuncgrid",
							hideLabel : true,
							store : new Ext.data.SimpleStore( {
								fields : [ "catid", "funcname", "funcsyntax",
										"funcdesc" ],
								data : functions.funcs
							}),
							columns : [ {
								id : "functionname",
								header : "Functions".localize(),
								fixed : true,
								width : 350,
								resizable : false,
								sortable : true,
								dataIndex : "funcname"
							} ],
							stripeRows : true,
							autoExpandColumn : "functionname",
							height : 150,
							width : 350,
							viewConfig : {
								forceFit : true
							},
							header : false,
							tabindex : 2,
							anchor : "100%",
							selModel : new Ext.grid.RowSelectionModel( {
								singleSelect : true,
								listeners : {
									rowselect : {
										fn : onRowSelect,
										scope : this
									}
								}
							})
						}),
						new Ext.Panel(
								{
									id : "funcdef",
									autoHeight : true,
									autoScroll : "auto",
									baseCls : "x-plain",
									style : "font-weight: bold; margin: 5px 0px; word-wrap: break-word;",
									items : [ {
										html : "",
										baseCls : "x-plain"
									} ]
								}), new Ext.Panel( {
							id : "funcdesc",
							autoScroll : "auto",
							baseCls : "x-plain",
							items : [ {
								html : "",
								baseCls : "x-plain"
							} ]
						}) ]
			});
	var dialogWindow = new Ext.Window( {
		id : "insFuncDlg",
		title : "Insert Function".localize(),
		cls : "wssdialog",
		layout : "fit",
		width : 400,
		height : 400,
		minWidth : 300,
		minHeight : 300,
		closeable : true,
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		constrain : true,
		modal : true,
		items : [ insFuncForm ],
		listeners : {
			beforedestroy : {
				fn : onBeforeDestroyDialog,
				scope : this
			},
			beforeshow : {
				fn : onBeforeShowDialog,
				scope : this
			}
		},
		buttons : [ {
			text : "OK".localize(),
			handler : doInsertFunction,
			tabIndex : 3
		}, {
			text : "Cancel".localize(),
			tabIndex : 4,
			handler : function() {
				dialogWindow.close()
			}
		} ]
	});
	dialogWindow.show(this);
	getFunctionsList(Ext.getCmp("category").getValue())
};