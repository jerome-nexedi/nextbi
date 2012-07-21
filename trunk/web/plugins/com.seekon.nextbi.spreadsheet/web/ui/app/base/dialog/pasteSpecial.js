Jedox.wss.dialog.openPasteSpecial = function(sheetID, sheetTitle) {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var allRb = new Ext.form.Radio( {
		hideLabel : true,
		checked : true,
		boxLabel : "All".localize(),
		name : "rb-paste",
		handler : function() {
			if (this.getValue()) {
				onRBCheck(true)
			}
		}
	});
	var contentTypesRb = new Ext.form.Radio( {
		hideLabel : true,
		boxLabel : "Content Types".localize(),
		name : "rb-paste",
		handler : function() {
			if (this.getValue()) {
				onRBCheck(false)
			}
		}
	});
	var formulasChb = new Ext.form.Checkbox( {
		hideLabel : true,
		boxLabel : "Formulas".localize(),
		checked : true
	});
	var valuesChb = new Ext.form.Checkbox( {
		hideLabel : true,
		boxLabel : "Values".localize(),
		checked : true
	});
	var stylesChb = new Ext.form.Checkbox( {
		hideLabel : true,
		boxLabel : "Styles".localize(),
		checked : true
	});
	var formatsChb = new Ext.form.Checkbox( {
		hideLabel : true,
		boxLabel : "Formats".localize(),
		checked : true
	});
	var conditionalFormatsChb = new Ext.form.Checkbox( {
		hideLabel : true,
		boxLabel : "Conditional Formats".localize(),
		checked : true
	});
	var cellMetadataChb = new Ext.form.Checkbox( {
		hideLabel : true,
		boxLabel : "Cell Metadata".localize(),
		checked : true
	});
	var rbPanel = new Ext.Panel(
			{
				layout : "form",
				border : false,
				defaults : {
					bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
				},
				baseCls : "x-plain",
				x : 10,
				y : 10,
				items : [ allRb, contentTypesRb ]
			});
	var contentTypePanel = new Ext.Panel(
			{
				layout : "form",
				border : false,
				defaults : {
					bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
				},
				baseCls : "x-plain",
				x : 30,
				y : 65,
				items : [ formulasChb, valuesChb, stylesChb, formatsChb,
						conditionalFormatsChb, cellMetadataChb ]
			});
	var mainPanel = new Ext.Panel( {
		layout : "absolute",
		baseCls : "main-panel",
		border : false,
		items : [ rbPanel, contentTypePanel ]
	});
	var win = new Ext.Window( {
		id : "paste-special-dlg",
		defaults : {
			bodyStyle : "padding:10px"
		},
		cls : "default-format-window",
		title : "Paste Special".localize(),
		closable : true,
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		constrain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 200,
		height : 330,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.pasteSpecial)
			}
		},
		buttons : [ {
			text : "OK".localize(),
			handler : function() {
				var paste = getPasteValue();
				win.close();
				Jedox.wss.action.paste(paste)
			}
		}, {
			text : "Cancel".localize(),
			handler : function() {
				win.close()
			}
		} ]
	});
	win.show(this);
	onRBCheck(true);
	function onRBCheck(all) {
		if (all) {
			formulasChb.disable();
			formulasChb.setValue(all);
			valuesChb.disable();
			valuesChb.setValue(true);
			stylesChb.disable();
			stylesChb.setValue(all);
			formatsChb.disable();
			formatsChb.setValue(all);
			conditionalFormatsChb.disable();
			conditionalFormatsChb.setValue(all);
			cellMetadataChb.disable();
			cellMetadataChb.setValue(all)
		} else {
			formulasChb.enable();
			formulasChb.setValue(false);
			valuesChb.enable();
			valuesChb.setValue(true);
			stylesChb.enable();
			stylesChb.setValue(false);
			formatsChb.enable();
			formatsChb.setValue(false);
			conditionalFormatsChb.enable();
			conditionalFormatsChb.setValue(false);
			cellMetadataChb.enable();
			cellMetadataChb.setValue(false)
		}
	}
	function getPasteValue() {
		var contType = Jedox.wss.range.ContentType;
		if (allRb.getValue()) {
			var paste = contType.ALL_PASTE
		} else {
			var formulas = formulasChb.getValue() ? contType.FORMULA : 0;
			var values = valuesChb.getValue() ? contType.VALUE : 0;
			var styles = stylesChb.getValue() ? contType.STYLE : 0;
			var format = formatsChb.getValue() ? contType.FORMAT : 0;
			var conditionalFormat = conditionalFormatsChb.getValue() ? contType.CNDFMT
					: 0;
			var cellMetadata = cellMetadataChb.getValue() ? contType.ATTRS : 0;
			var paste = formulas | values | styles | format | conditionalFormat
					| cellMetadata
		}
		return paste
	}
};