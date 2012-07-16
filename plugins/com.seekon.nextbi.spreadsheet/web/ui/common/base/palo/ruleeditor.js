Jedox.wss.palo.ruleEditor = function(servId, dbName, cubeName) {
	var _ident;
	var _fromDlgF = false;
	if (Jedox.wss.palo.workIn == Jedox.wss.palo.GRID) {
		if (Jedox.wss.app.environment.inputMode === Jedox.wss.grid.GridMode.DIALOG) {
			_fromDlgF = true
		} else {
			Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
			Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
		}
	}
	Ext.grid.CheckColumn = function(config) {
		Ext.apply(this, config);
		if (!this.id) {
			this.id = Ext.id()
		}
		this.renderer = this.renderer.createDelegate(this)
	};
	Ext.grid.CheckColumn.prototype = {
		init : function(grid) {
			this.grid = grid;
			this.grid.on("render", function() {
				var view = this.grid.getView();
				view.mainBody.on("mousedown", this.onMouseDown, this)
			}, this)
		},
		onMouseDown : function(e, t) {
			if (t.className
					&& t.className.indexOf("x-grid3-cc-" + this.id) != -1) {
				e.stopEvent();
				var index = this.grid.getView().findRowIndex(t);
				var record = this.grid.store.getAt(index);
				record.set(this.dataIndex, !record.data[this.dataIndex]);
				_ident = gridData[index][0];
				var _tmp = gridData[index][1];
				var _tmc = gridData[index][2];
				var _act = record.data[this.dataIndex];
				_ruleObj.modifyRule( [ [ servId, dbName, cubeName ], [ {
					identifier : _ident,
					extern_id : ""
				}, {
					def : _tmp,
					comment : _tmc,
					active : _act
				} ] ])
			}
		},
		renderer : function(v, p, record) {
			p.css += " x-grid3-check-col-td";
			return '<div class="x-grid3-check-col' + (v ? "-on" : "")
					+ " x-grid3-cc-" + this.id + '"> </div>'
		}
	};
	var gridData = [];
	var ruleHandlers = {
		getRules : function(result) {
			for ( var j = 0; j < result[1].length; j++) {
				var _id = result[1][j].identifier;
				var _definition = result[1][j].definition;
				var _comment = result[1][j].comment;
				var _active = result[1][j].activated;
				var _ts = result[1][j].timestamp;
				var _date = new Date(_ts * 1000);
				var _dat = _date.toLocaleString();
				var _tt = [ _id, _definition, _comment, _dat, _active ];
				gridData.push(_tt)
			}
			gridStore.loadData(gridData)
		},
		createRule : function(result) {
			gridData = [];
			_ruleObj.getRules( [ servId, dbName, cubeName ])
		},
		deleteRule : function(result) {
			gridData = [];
			_ruleObj.getRules( [ servId, dbName, cubeName ])
		},
		modifyRule : function(result) {
			gridData = [];
			_ruleObj.getRules( [ servId, dbName, cubeName ])
		}
	};
	var _ruleObj = new Palo(ruleHandlers);
	_ruleObj.getRules( [ servId, dbName, cubeName ]);
	var gridStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "id"
		}, {
			name : "rule"
		}, {
			name : "comment"
		}, {
			name : "updated"
		}, {
			name : "active"
		} ]
	});
	gridStore.loadData(gridData);
	var _new = false;
	var _edit = false;
	var _i;
	var cbActive = new Ext.grid.CheckColumn( {
		header : "Active".localize(),
		dataIndex : "active",
		width : 45
	});
	var grid = new Ext.grid.GridPanel( {
		store : gridStore,
		width : 540,
		clicksToEdit : 1,
		plugins : cbActive,
		cm : new Ext.grid.ColumnModel( [ {
			id : "rule",
			header : "Rule".localize(),
			width : 180,
			sortable : false,
			dataIndex : "rule"
		}, {
			header : "Comment".localize(),
			width : 100,
			sortable : false,
			dataIndex : "comment"
		}, {
			header : "Updated".localize(),
			width : 160,
			sortable : false,
			dataIndex : "updated"
		}, cbActive ]),
		stripeRows : false,
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : true
		}),
		enableHdMenu : false,
		autoExpandColumn : "rule",
		height : 200,
		autoWidth : true,
		baseCls : "x-plain"
	});
	grid.getSelectionModel().on("rowselect", function(sm, rowIdx, r) {
		_i = rowIdx;
		_ident = gridData[_i][0];
		ruleArea.disable();
		commentArea.disable();
		ruleArea.setRawValue(r.data.rule);
		commentArea.setRawValue(r.data.comment);
		_new = false;
		_edit = true
	});
	var ruleArea = new Ext.form.TextArea( {
		xtype : "textarea",
		hideLabel : "true",
		name : "rule",
		height : 50,
		width : 540,
		disabled : true
	});
	var commentArea = new Ext.form.TextArea( {
		xtype : "commentarea",
		hideLabel : "true",
		name : "rule",
		height : 50,
		width : 540,
		disabled : true
	});
	var win = new Ext.Window(
			{
				title : "Rule Editor".localize(),
				cls : "default-format-window",
				closable : true,
				autoDestroy : true,
				plain : true,
				constrain : true,
				modal : true,
				resizable : false,
				animCollapse : false,
				width : 600,
				autoHeight : true,
				items : [ new Ext.Panel(
						{
							baseCls : "x-title-f",
							labelWidth : 100,
							labelAlign : "left",
							layout : "form",
							bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
							frame : false,
							header : false,
							monitorValid : true,
							autoHeight : true,
							autoWidth : true,
							items : [
									{
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										border : true,
										autoHeight : true,
										xtype : "fieldset",
										layout : "form",
										frame : false,
										title : "List of defined rules"
												.localize(),
										items : [ grid ]
									},
									{
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										border : true,
										autoHeight : true,
										xtype : "fieldset",
										layout : "form",
										frame : false,
										title : "Rule".localize(),
										items : [ {
											bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
											border : true,
											autoHeight : true,
											baseCls : "x-title-f",
											xtype : "fieldset",
											layout : "form",
											items : [ ruleArea ]
										} ]
									},
									{
										bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
										border : true,
										autoHeight : true,
										xtype : "fieldset",
										layout : "form",
										frame : false,
										title : "Comment".localize(),
										items : [ {
											bodyStyle : "color: #000000; font-size: 9pt; background-color: transparent;",
											border : true,
											autoHeight : true,
											baseCls : "x-title-f",
											xtype : "fieldset",
											layout : "form",
											items : [ commentArea ]
										} ]
									} ]
						}) ],
				listeners : {
					close : function() {
						if (Jedox.wss.palo.workIn == Jedox.wss.palo.GRID) {
							Jedox.wss.general
									.setInputMode(Jedox.wss.app.lastInputModeDlg);
							Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
						}
						if (Jedox.wss.palo.workIn) {
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.ruleEditor)
						}
					}
				},
				buttons : [
						{
							text : "New".localize().concat("..."),
							handler : function() {
								ruleArea.setRawValue("");
								commentArea.setRawValue("");
								ruleArea.enable();
								commentArea.enable();
								ruleArea.focus();
								_new = true;
								_edit = false
							}
						},
						{
							text : "Edit".localize().concat("..."),
							handler : function() {
								ruleArea.enable();
								commentArea.enable();
								ruleArea.focus();
								_new = false;
								_edit = true
							}
						},
						{
							text : "Delete".localize().concat("..."),
							handler : function() {
								_ruleObj.deleteRule( [
										[ servId, dbName, cubeName ], _ident ])
							}
						},
						{
							text : "Apply".localize(),
							handler : function() {
								ruleArea.disable();
								commentArea.disable();
								if ((_new == true) && (_edit == false)) {
									_new = false;
									_edit = false;
									var _tmp = ruleArea.getValue();
									var _tmc = commentArea.getValue();
									_ruleObj.createRule( [
											[ servId, dbName, cubeName ], {
												definition : _tmp,
												comment : _tmc,
												extern_id : "",
												activate : true
											} ])
								} else {
									if ((_new == false) && (_edit == true)) {
										_new = false;
										_edit = false;
										var _tmp = ruleArea.getValue();
										var _tmc = commentArea.getValue();
										_ruleObj.modifyRule( [
												[ servId, dbName, cubeName ],
												[ {
													identifier : _ident,
													extern_id : ""
												}, {
													def : _tmp,
													comment : _tmc,
													active : true
												} ] ])
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
	win.show(this)
};