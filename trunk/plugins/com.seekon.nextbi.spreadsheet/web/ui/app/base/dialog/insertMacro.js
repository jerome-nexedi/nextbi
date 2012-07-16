Jedox.wss.dialog.openInsertMacro = function(obj) {
	Jedox.wss.macro.list( [ this, openDialog_post, obj ],
			Jedox.wss.macro.listFmt.FLAT);
	function openDialog_post(listData, obj) {
		console.log(listData);
		console.log(obj);
		var that = this;
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
		var obj = obj;
		var listData = listData;
		var macroNameLbl = new Ext.form.Label( {
			text : "Macro name".localize().concat(":")
		});
		var macroNameTxf = new Ext.form.TextField( {
			hideLabel : true,
			width : 290,
			listeners : {
				focus : function() {
					if (functionListGrid.getSelectionModel().getCount() != 0) {
						this.setValue("");
						functionListGrid.getSelectionModel().clearSelections();
						btnPanel.layout.setActiveItem(0)
					}
				},
				blur : function() {
					if (!this.getValue()) {
						this.setValue(getDefaultMacroName())
					} else {
						var rowIndex = functionListGrid.store.find("name", this
								.getValue());
						if (rowIndex != -1) {
							functionListGrid.fireEvent("rowclick",
									functionListGrid, rowIndex);
							functionListGrid.getSelectionModel().selectRow(
									rowIndex)
						}
					}
				}
			}
		});
		var functionListData = [];
		for ( var i = 0; i < listData.length; i++) {
			functionListData.push( [ listData[i] ])
		}
		var functionListStore = new Ext.data.SimpleStore( {
			fields : [ {
				name : "name"
			} ],
			data : functionListData
		});
		var functionListGrid = new Ext.grid.GridPanel( {
			store : functionListStore,
			columns : [ {
				id : "company",
				header : "Function",
				width : 270,
				sortable : true,
				dataIndex : "name"
			} ],
			sm : new Ext.grid.RowSelectionModel( {
				singleSelect : true
			}),
			autoscroll : true,
			stripeRows : true,
			height : 200,
			width : 290,
			listeners : {
				rowclick : function(grid, rowIndex, e) {
					macroNameTxf.setValue(this.store.getAt(rowIndex)
							.get("name"));
					btnPanel.layout.setActiveItem(1)
				}
			}
		});
		var okBtn = {
			text : "OK".localize(),
			handler : function() {
				onOK();
				win.close()
			}
		};
		var cancelBtn = {
			text : "Cancel".localize(),
			handler : function() {
				win.close()
			}
		};
		var newBtn = {
			text : "New".localize(),
			handler : function() {
				onNew()
			}
		};
		var BRLbl = {
			html : "<br/>",
			baseCls : "x-plain"
		};
		var editBtn = {
			text : "Edit".localize(),
			handler : function() {
				onEdit()
			}
		};
		var btnPanel = new Ext.Panel( {
			layout : "card",
			baseCls : "x-plain",
			activeItem : 0,
			items : [ {
				layout : "form",
				border : false,
				buttons : [ newBtn ]
			}, {
				layout : "form",
				border : false,
				buttons : [ editBtn ]
			} ]
		});
		var mainPanel = new Ext.Panel( {
			layout : "absolute",
			baseCls : "x-plain",
			border : false,
			items : [ {
				layout : "form",
				border : false,
				baseCls : "x-plain",
				x : 5,
				y : 10,
				width : 292,
				height : 250,
				items : [ macroNameLbl, macroNameTxf, functionListGrid ]
			}, {
				layout : "form",
				baseCls : "x-plain",
				x : 295,
				y : 18,
				height : 50,
				items : [ btnPanel ]
			} ]
		});
		function initDlg() {
			console.log("initDlg");
			preselectMacro()
		}
		function getDefaultMacroName() {
			return obj.name.concat("_",
					Jedox.wss.wsel[obj.type].events[0].funcname)
		}
		function preselectMacro() {
			var macro = obj.macros[Jedox.wss.wsel[obj.type].events[0].name];
			if (macro) {
				setTimeout(function() {
					functionListGrid.fireEvent("rowclick", functionListGrid,
							functionListGrid.store.find("name", macro));
					functionListGrid.getSelectionModel().selectRow(
							functionListStore.find("name", macro))
				}, 50)
			} else {
				macroNameTxf.setValue(getDefaultMacroName())
			}
		}
		function onNew() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.editMacro, [ {
				operation : "new",
				macro : macroNameTxf.getValue()
			}, refresh ])
		}
		function onEdit() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.editMacro, [ {
				operation : "edit",
				macro : macroNameTxf.getValue()
			}, refresh ])
		}
		function onOK() {
			var macro = macroNameTxf.getValue();
			var key = Jedox.wss.wsel[obj.type].events[0].name;
			var macros = {};
			macros[key] = macro;
			Jedox.wss.wsel.assignMacro( {
				id : obj.id,
				type : obj.type,
				name : obj.name,
				macros : macros
			})
		}
		function refresh() {
			Jedox.wss.macro.list( [ that, showDialog_post ],
					Jedox.wss.macro.listFmt.FLAT)
		}
		function showDialog_post(listData) {
			console.log(listData);
			var refreshListData = [];
			for ( var i = 0; i < listData.length; i++) {
				refreshListData.push( [ listData[i] ])
			}
			setTimeout(function() {
				functionListStore.loadData(refreshListData);
				initDlg()
			}, 300)
		}
		var win = new Ext.Window(
				{
					id : "insert-macro-dlg",
					title : "Assign macro".localize(),
					closable : true,
					closeAction : "close",
					autoDestroy : true,
					plain : true,
					constrain : true,
					cls : "default-format-window",
					modal : true,
					resizable : false,
					animCollapse : false,
					layout : "fit",
					width : 400,
					height : 335,
					items : mainPanel,
					listeners : {
						close : function() {
							Jedox.wss.general
									.setInputMode(Jedox.wss.app.lastInputModeDlg);
							Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.insertMacro)
						}
					},
					buttons : [ okBtn, cancelBtn ]
				});
		win.show(this);
		initDlg()
	}
};