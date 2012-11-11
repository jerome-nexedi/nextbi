Jedox.studio.files.ReferrersList = function(itemsFlag) {
	var that = this;
	var reportListData = [ [ "rGrp1/rHrh1/New folder/New workbook rprt" ],
			[ "rGrp1/rHrh1/New folder/New workbook rprt1" ],
			[ "rGrp1/rHrh1/New folder/New workbook rprt2" ] ];
	var _itemsFlag = itemsFlag;
	var _referrersStore;
	var referrersItemStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "referrer",
			type : "string"
		} ]
	});
	var referrersItemListGP = new Ext.grid.GridPanel( {
		id : "referrers-item-list",
		colModel : new Ext.grid.ColumnModel( [ {
			header : "Referrers".localize(),
			width : 340,
			sortable : true,
			dataIndex : "referrer"
		} ]),
		store : referrersItemStore,
		viewConfig : {
			forceFit : true
		},
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : true
		})
	});
	var referrersItemsStore = new Ext.data.SimpleStore( {
		fields : [ {
			name : "subNode",
			type : "string"
		}, {
			name : "referrer",
			type : "string"
		} ]
	});
	var referrersItemsListGP = new Ext.grid.GridPanel( {
		id : "referrers-items-list",
		colModel : new Ext.grid.ColumnModel( [ {
			header : "Sub Nodes".localize(),
			width : 150,
			sortable : true,
			dataIndex : "subNode"
		}, {
			header : "Referrers".localize(),
			width : 150,
			sortable : true,
			dataIndex : "referrer"
		} ]),
		store : referrersItemsStore,
		viewConfig : {
			forceFit : true
		},
		sm : new Ext.grid.RowSelectionModel( {
			singleSelect : true
		})
	});
	var mainPanel = new Ext.Panel( {
		layout : "fit",
		width : 365,
		height : 380,
		border : false
	});
	function initData(nodes) {
		var that = this;
		var wssStudioHandler = {
			getReferrers : function(result) {
				_referrersStore.loadData(result)
			}
		};
		var wssStudioStub = new Studio(wssStudioHandler);
		wssStudioStub.getReferrers(nodes)
	}
	function setConfig() {
		if (_itemsFlag) {
			_referrersStore = referrersItemsStore;
			mainPanel.add(referrersItemsListGP)
		} else {
			_referrersStore = referrersItemStore;
			mainPanel.add(referrersItemListGP)
		}
	}
	this.getReferrersList = function(nodes) {
		setConfig();
		initData(nodes);
		return mainPanel
	}
};