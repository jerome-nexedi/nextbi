Jedox.wss.dialog.openNewName = function(newNameHandleFnc, selectedName,
		nameManagerStore) {
	var _fromDlgF = false;
	var selectedScope = "";
	var selectedRange = "";
	var _newNameHandleFncFlag = false;
	if (Jedox.wss.app.environment.inputMode === Jedox.wss.grid.GridMode.DIALOG) {
		_fromDlgF = true
	} else {
		Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
		Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
	}
	var newName = [];
	var dlgTitle = "";
	var worksheets = Jedox.wss.backend.ha.getSheets()[0];
	var scopeData = [ [ "", "Workbook".localize() ] ];
	for ( var i = 0; i < worksheets.length; i += 2) {
		scopeData.push( [ worksheets[i], worksheets[i + 1] ])
	}
	var scopeStore = new Ext.data.SimpleStore( {
		fields : [ "sheetID", "sheetName" ],
		data : scopeData
	});
	if (selectedName) {
		dlgTitle = "Edit Name".localize()
	} else {
		dlgTitle = "New Name".localize()
	}
	var nameTxf = new Ext.form.TextField( {
		fieldLabel : "Name".localize(),
		width : 200
	});
	var scopeCmb = new Ext.form.ComboBox( {
		fieldLabel : "Scope".localize(),
		width : 135,
		store : scopeStore,
		displayField : "sheetName",
		editable : false,
		mode : "local",
		triggerAction : "all",
		listeners : {
			select : function(combo, record, index) {
				selectedScope = index == 0 ? "" : combo.getValue()
			}
		}
	});
	var commentTxa = new Ext.form.TextArea( {
		fieldLabel : "Comment".localize(),
		width : 200
	});
	var refersToTxf = new Ext.form.TextField( {
		fieldLabel : "Refers To".localize(),
		value : "="
				+ Jedox.wss.app.activeBook.getSheetSelector()
						.getActiveSheetName()
				+ "!"
				+ tmpFormatRange(Jedox.wss.app.environment.defaultSelection
						.getActiveRange().getValue()),
		width : 170
	});
	var selectRangeBtn = new Ext.Toolbar.Button( {
		id : "selRangeBegin",
		iconCls : "select-range-icon",
		cls : "x-btn-icon",
		tooltip : "Select Range".localize(),
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRangeHandleFnc ],
				rng : refersToTxf.getValue()
			} ]);
			win.hide()
		}
	});
	function selRangeHandleFnc(selected) {
		win.show();
		refersToTxf.setValue(selected)
	}
	function tmpFormatRange(sr) {
		var tmp = sr.split(":");
		var formatedRange = "";
		for ( var i = 0; i < tmp.length; i++) {
			if (i > 0) {
				formatedRange += ":"
			}
			for ( var j = 0; j < tmp[i].length; j++) {
				if (j == 0
						|| (parseInt(tmp[i].charAt(j)) && !(parseInt(tmp[i]
								.charAt(j - 1))))) {
					formatedRange += "$" + tmp[i].charAt(j)
				} else {
					formatedRange += tmp[i].charAt(j)
				}
			}
		}
		return formatedRange
	}
	function openSelectRangeDialog() {
		win.hide();
		minWin.show()
	}
	function initName() {
		if (selectedName) {
			nameTxf.setValue(selectedName.get("name"));
			scopeCmb.setValue(selectedName.get("scope"));
			selectedScope = selectedName.get("scope");
			scopeCmb.disable();
			commentTxa.setValue(selectedName.get("comment"));
			refersToTxf.setValue(selectedName.get("refersTo"))
		}
	}
	function validateName() {
		var name = nameTxf.getValue();
		var refersTo = refersToTxf.getValue();
		var returnValue = false;
		var my_regexp_ALLOWED_CHARS = /^[a-zA-Z_\\][a-zA-Z0-9_\-\.]+$/;
		var my_regexp_RESERVED_NAMES = /^([a-zA-Z]{1,2}[0-9]{1,5})+$/;
		var my_regexp_RESERVED_CELL_REF = /^[rR]{1}[0-9]{1,5}([cC]{1}[0-9]{1,5})*$/;
		if (((name.length > 1 && name.length < 256 && refersTo.length > 0) && (my_regexp_ALLOWED_CHARS
				.test(name) && !my_regexp_RESERVED_NAMES.test(name)))
				&& !my_regexp_RESERVED_CELL_REF.test(name)) {
			returnValue = true;
			nameManagerStore.each(function(record) {
				if (record.data.name.toLowerCase() == name.toLowerCase()
						&& record.data.scope == scopeCmb.getValue()) {
					returnValue = record != selectedName ? false : true
				}
			}, [ this ])
		}
		return returnValue
	}
	function addNewName() {
		if (validateName()) {
			var newName = [ nameTxf.getValue(), "", refersToTxf.getValue(),
					scopeCmb.getValue(), commentTxa.getValue() ];
			newNameHandleFnc(newName);
			_newNameHandleFncFlag = true;
			win.close()
		} else {
			Ext.Msg.show( {
				title : "Warning".localize() + "...",
				msg : "newNameDlg_NameWarningMsg".localize(),
				buttons : Ext.Msg.OK,
				icon : Ext.MessageBox.WARNING
			})
		}
	}
	var mainPanel = new Ext.Panel( {
		modal : true,
		layout : "form",
		baseCls : "x-plain",
		border : false,
		items : [ nameTxf, scopeCmb, commentTxa, {
			layout : "column",
			border : false,
			baseCls : "main-panel",
			items : [ {
				layout : "form",
				border : false,
				width : 280,
				baseCls : "x-plain",
				items : refersToTxf
			}, {
				layout : "form",
				border : false,
				width : 30,
				baseCls : "x-plain",
				items : selectRangeBtn
			} ]
		} ]
	});
	var win = new Ext.Window( {
		defaults : {
			bodyStyle : "padding:10px"
		},
		title : dlgTitle,
		closable : true,
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		constrain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		cls : "default-format-window",
		layout : "fit",
		width : 350,
		height : 240,
		items : mainPanel,
		listeners : {
			close : function() {
				if (!_newNameHandleFncFlag) {
					newNameHandleFnc()
				}
				if (!_fromDlgF) {
					Jedox.wss.general
							.setInputMode(Jedox.wss.app.lastInputModeDlg);
					Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
				}
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.newName)
			}
		},
		buttons : [ {
			text : "OK".localize(),
			handler : addNewName
		}, {
			text : "Cancel".localize(),
			handler : function() {
				win.close()
			}
		} ]
	});
	win.show(this);
	scopeCmb.setValue("Workbook".localize());
	initName()
};