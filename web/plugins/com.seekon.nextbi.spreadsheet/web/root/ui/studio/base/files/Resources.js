Jedox.studio.files.Resources = function(mFlag, itemsFlag) {
	var that = this;
	var _nodes;
	var _mode = mFlag;
	var _itemsFlag = itemsFlag;
	var _dependentsStore;
	var _resourcesData = {};
	var _isDirty = false;
	var resourcesStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "path",
			type : "string"
		}, {
			name : "group",
			type : "string"
		}, {
			name : "hierarchy",
			type : "string"
		}, {
			name : "node",
			type : "string"
		} ]
	});
	var resourcesGridPanel = new Ext.grid.GridPanel( {
		colModel : new Ext.grid.ColumnModel( [ {
			header : "Path".localize(),
			width : 340,
			sortable : true,
			dataIndex : "path"
		} ]),
		store : resourcesStore,
		viewConfig : {
			forceFit : true
		},
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : false
		})
	});
	var dependentsItemStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "path",
			type : "string"
		} ]
	});
	var dependentsItemGridPanel = new Ext.grid.GridPanel( {
		colModel : new Ext.grid.ColumnModel( [ {
			header : "Path".localize(),
			width : 340,
			sortable : true,
			dataIndex : "path"
		} ]),
		store : dependentsItemStore,
		viewConfig : {
			forceFit : true
		},
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : false
		})
	});
	var dependentsItemsStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "name",
			type : "string"
		}, {
			name : "path",
			type : "string"
		} ]
	});
	var dependentsItemsGridPanel = new Ext.grid.GridPanel( {
		colModel : new Ext.grid.ColumnModel( [ {
			header : "Spreadsheet".localize(),
			width : 170,
			sortable : true,
			dataIndex : "name"
		}, {
			header : "Path".localize(),
			width : 150,
			sortable : true,
			dataIndex : "path"
		} ]),
		autoScroll : true,
		store : dependentsItemsStore,
		viewConfig : {
			forceFit : true
		},
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : false
		})
	});
	var mainPanel = new Ext.Panel( {
		layout : "fit",
		width : 365,
		height : 350,
		bodyStyle : "background-color: transparent;",
		border : false,
		buttons : [ {
			text : "Add".localize(),
			handler : function() {
				Jedox.studio.files.insertMarkers(addMarkers)
			}
		}, {
			text : "Remove".localize(),
			handler : function() {
				removeMarkers()
			}
		} ]
	});
	function addMarkers(records) {
		var rCount = resourcesStore.getCount();
		for ( var i = 0, mCount = records.length; i < mCount; i++) {
			var equals = false;
			var data = records[i].data;
			for ( var j = 0; j < rCount; j++) {
				if (resourcesStore.getAt(j).data.node == data.node
						&& resourcesStore.getAt(j).data.hierarchy == data.hierarchy
						&& resourcesStore.getAt(j).data.group == data.group) {
					equals = true;
					break
				}
			}
			if (!equals) {
				resourcesStore.add(new Ext.data.Record(data));
				_isDirty = true
			}
		}
	}
	function removeMarkers() {
		var records = resourcesGridPanel.getSelectionModel().getSelections();
		for ( var i = 0, count = records.length; i < count; i++) {
			resourcesStore.remove(records[i]);
			_isDirty = true
		}
	}
	function initData(nodes) {
		_nodes = nodes;
		var wssStudioHandler = {
			getResources : function(result) {
				resourcesStore.loadData(result);
				for ( var i = 0, count = result.length, g, h, n; i < count; i++) {
					g = result[i][1];
					h = result[i][2];
					n = result[i][3];
					_resourcesData[g.concat(h, n)] = [ g, h, n ]
				}
			},
			getDependents : function(result) {
				_dependentsStore.loadData(result)
			}
		};
		var wssStudioStub = new Studio(wssStudioHandler);
		if (_mode == "resources") {
			wssStudioStub.getResources(nodes)
		} else {
			wssStudioStub.getDependents(nodes)
		}
	}
	function setConfig() {
		if (_mode == "resources") {
			mainPanel.add(resourcesGridPanel)
		} else {
			if (_itemsFlag) {
				_dependentsStore = dependentsItemsStore;
				mainPanel.add(dependentsItemsGridPanel)
			} else {
				_dependentsStore = dependentsItemStore;
				mainPanel.add(dependentsItemGridPanel)
			}
		}
	}
	this.setResources = function() {
		if (_isDirty) {
			_isDirty = false;
			var resourcesData = {};
			for ( var i = 0, count = resourcesStore.getCount(), g, h, n; i < count; i++) {
				g = resourcesStore.getAt(i).get("group");
				h = resourcesStore.getAt(i).get("hierarchy");
				n = resourcesStore.getAt(i).get("node");
				resourcesData[g.concat(h, n)] = [ g, h, n ]
			}
			Jedox.studio.backend.wssStudio.setResources(_nodes[0],
					_resourcesData, resourcesData)
		}
	};
	this.hideBtns = function() {
		mainPanel.buttons[0].hide();
		mainPanel.buttons[1].hide()
	}, this.getPanel = function(nodes) {
		setConfig();
		initData(nodes);
		return mainPanel
	}
};
Jedox.studio.files.insertMarkers = function(callBackFnc) {
	var markersStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "path",
			type : "string"
		}, {
			name : "group",
			type : "string"
		}, {
			name : "hierarchy",
			type : "string"
		}, {
			name : "node",
			type : "string"
		} ]
	});
	var markersGP = new Ext.grid.GridPanel( {
		id : "markers-list",
		colModel : new Ext.grid.ColumnModel( [ {
			header : "Path".localize(),
			width : 150,
			sortable : true,
			dataIndex : "path"
		} ]),
		store : markersStore,
		viewConfig : {
			forceFit : true
		},
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : false
		})
	});
	var markersPanel = new Ext.Panel( {
		layout : "fit",
		bodyStyle : "background-color: transparent;",
		width : 365,
		height : 380,
		border : false,
		items : markersGP
	});
	var win = new Ext.Window( {
		title : "Add From Markers".localize(),
		closable : true,
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		bodyBorder : false,
		constrain : true,
		modal : true,
		border : false,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 500,
		height : 300,
		items : [ markersPanel ],
		buttons : [ {
			text : "Insert".localize(),
			handler : function() {
				doInsertMarkers()
			}
		}, {
			text : "Close".localize(),
			handler : function() {
				win.close()
			}
		} ]
	});
	function initMarkersData() {
		var markersData = [];
		var markers = Jedox.studio.app.markers;
		for ( var i in markers) {
			markersData.push( [ markers[i].path, markers[i].g, markers[i].h,
					markers[i].n ])
		}
		markersStore.loadData(markersData)
	}
	function doInsertMarkers() {
		var selectedRecords = markersGP.getSelectionModel().getSelections();
		callBackFnc(selectedRecords)
	}
	initMarkersData();
	win.show()
};