Jedox.wss.dialog.openImportDialog = function() {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var onBeforeDestroyDialog = function(panel) {
		importForm.items.each(function(f) {
			f.purgeListeners();
			Ext.destroy(f)
		});
		importForm.purgeListeners();
		importForm.destroy();
		Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
		Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
		Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.importFile)
	};
	var validateFile = function(fieldValue) {
		if ((fieldValue.length > 5)
				&& (fieldValue.lastIndexOf(".xlsx") == fieldValue.length - 5)) {
			return true
		} else {
			return "impDlg_msgWrongType".localize()
		}
	};
	var doUploadFile = function() {
		var frameID = "tmpUploadIFrame";
		var frame = Ext.get("tmpUploadIFrame");
		if (!frame) {
			frame = document.createElement("iframe");
			frame.id = frameID;
			frame.name = frameID;
			frame.className = "x-hidden";
			if (Ext.isIE) {
				frame.src = Ext.SSL_SECURE_URL
			}
			document.body.appendChild(frame);
			if (Ext.isIE) {
				document.frames[frameID].name = frameID
			}
		}
		var form = Ext.getDom(importForm.getForm().getEl());
		form.target = frameID;
		form.method = "POST";
		form.action = "cc/upload.php?wam=designer";
		form.enctype = form.encoding = "multipart/form-data";
		var fileName = fileUpload.value.split(".")[0];
		try {
			for ( var triggers = Jedox.wss.events.triggers.importWorkbook_before, i = triggers.length - 1, name = fileName; i >= 0; i--) {
				triggers[i][0]["importWorkbook_before"].call(parent,
						triggers[i][1], name)
			}
			form.submit();
			dialogWindow.close()
		} catch (e) {
			Jedox.wss.general.showMsg("Application Error".localize(), e.message
					.localize(), Ext.MessageBox.ERROR);
			return false
		}
	};
	var fileUpload = new Ext.ux.FileUploadField( {
		fieldLabel : "impDlg_fieldLbl".localize(),
		emptyText : "impDlg_msgFieldBlank".localize(),
		defaultAutoCreate : {
			tag : "input",
			type : "text",
			size : "65",
			autocomplete : "off",
			accept : "*.xlsx"
		},
		width : 420,
		id : "filepath",
		name : "workbook_file",
		validator : validateFile
	});
	var importForm = new Ext.FormPanel( {
		baseCls : "x-plain",
		labelWidth : 50,
		labelAlign : "top",
		frame : true,
		bodyStyle : "padding:5px 5px 0",
		width : 450,
		defaults : {
			width : 410
		},
		defaultType : "textfield",
		buttonAlign : "right",
		header : false,
		headerAsText : false,
		monitorValid : true,
		title : null,
		items : [ fileUpload, new Ext.form.Hidden( {
			name : "MAX_FILE_SIZE",
			value : "30000"
		}) ]
	});
	var dialogWindow = new Ext.Window(
			{
				id : "importDlg",
				title : "Import".localize(),
				cls : "wssdialog default-format-window",
				layout : "fit",
				width : 450,
				height : 160,
				closeable : true,
				closeAction : "close",
				autoDestroy : true,
				plain : true,
				constrain : true,
				modal : true,
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
							items : [ importForm ]
						}) ],
				listeners : {
					close : function() {
						Jedox.wss.general
								.setInputMode(Jedox.wss.app.lastInputModeDlg);
						Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
						Jedox.wss.app
								.unload(Jedox.wss.app.dynJSRegistry.importFile)
					}
				},
				buttons : [ {
					text : "Upload".localize(),
					handler : doUploadFile,
					formBind : true,
					tabIndex : 2
				}, {
					text : "Close".localize(),
					handler : function() {
						dialogWindow.close()
					}
				} ]
			});
	dialogWindow.on("beforedestroy", onBeforeDestroyDialog);
	dialogWindow.on("show", function() {
		var f = Ext.get("filepath");
		f.focus.defer(100, f)
	});
	dialogWindow.show(this);
	Jedox.wss.dialog.importDlg = dialogWindow
};
Jedox.wss.dialog.processImport = function(res, fileName) {
	var importOk = false;
	if (eval(res)[0][0]) {
		importOk = Jedox.wss.book.load(null, fileName)
	} else {
		Jedox.wss.general.showMsg("Import Error".localize(), "imp_err_msg"
				.localize( {
					name : fileName
				}), Ext.MessageBox.ERROR)
	}
	try {
		fileName = fileName.split(".")[0];
		for ( var triggers = Jedox.wss.events.triggers.importWorkbook_after, i = triggers.length - 1, name = fileName; i >= 0; i--) {
			triggers[i][0]["importWorkbook_after"].call(parent, triggers[i][1],
					name, importOk)
		}
	} catch (e) {
		Jedox.wss.general.showMsg("Application Error".localize(), e.message
				.localize(), Ext.MessageBox.ERROR)
	}
};