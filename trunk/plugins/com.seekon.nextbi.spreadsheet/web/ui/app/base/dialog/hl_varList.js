Jedox.wss.dialog.hlVarList = function(getVar, preselection) {
	var rawData = Jedox.wss.backend.conn.cmd(0, [ "gvls" ]);
	var vGridData = [];
	if (rawData[0][0]) {
		var vars = rawData[0][1];
		for ( var q = 0; q < vars.length; q++) {
			vGridData.push( [ vars[q] ])
		}
		if (vars.length < 1) {
			vGridData = []
		}
	}
	var vGridStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "variable"
		} ]
	});
	vGridStore.loadData(vGridData);
	var _selectedVar;
	var vGrid = new Ext.grid.GridPanel( {
		store : vGridStore,
		columns : [ {
			id : "vars",
			header : "Variable name",
			width : 200,
			sortable : false,
			dataIndex : "variable"
		} ],
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : true
		}),
		width : 215,
		height : 210,
		frame : true,
		autoExpandColumn : "vars",
		baseCls : "x-plain",
		listeners : {
			rowclick : function(gr, index, e) {
				_selectedVar = vGridData[index][0]
			}
		}
	});
	var vwin = new Ext.Window(
			{
				title : "Variable list".localize(),
				closable : true,
				autoDestroy : true,
				plain : true,
				constrain : true,
				modal : true,
				resizable : false,
				enableHdMenu : false,
				animCollapse : false,
				width : 250,
				height : 300,
				layout : "form",
				items : [ new Ext.Panel(
						{
							id : "grid_varis",
							bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
							border : false,
							frame : false,
							autoHeight : true,
							layout : "form",
							items : [ vGrid ]
						}) ],
				listeners : {
					close : function() {
						Jedox.wss.general
								.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
					}
				},
				buttons : [ {
					text : "OK".localize(),
					handler : function() {
						vwin.close();
						getVar(_selectedVar)
					}
				}, {
					text : "Cancel".localize(),
					handler : function() {
						vwin.close()
					}
				} ]
			});
	vwin.show(this);
	if (preselection && preselection != "") {
		for ( var q = 0; q < vGridData.length; q++) {
			if (vGridData[q][0] == preselection) {
				setTimeout(function() {
					vGrid.getSelectionModel().selectRow(q)
				}, 1);
				break
			}
		}
	}
};