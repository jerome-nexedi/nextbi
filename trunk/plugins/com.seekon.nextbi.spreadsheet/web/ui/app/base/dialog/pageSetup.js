Jedox.wss.dialog.openPageSetup = function() {
	Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG);
	var _selectedData;
	var phpWSSServer = new Palo();
	var _preselctionChbValues = {
		horizontallyChb : false,
		horizontallyChb : false,
		gridLinesChb : false
	};
	var portraitRb = new Ext.form.Radio( {
		boxLabel : "Portrait".localize(),
		hideLabel : true,
		checked : true,
		name : "rb-orientation",
		width : 100,
		inputValue : 1
	});
	var landscapeRb = new Ext.form.Radio( {
		boxLabel : "Landscape".localize(),
		hideLabel : true,
		name : "rb-orientation",
		width : 100,
		handler : centerPic,
		inputValue : 1
	});
	var adjustToRb = new Ext.form.Radio( {
		hideLabel : true,
		checked : true,
		name : "rb-scaling",
		width : 20,
		inputValue : 1
	});
	var adjustToSpn = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 400
		}),
		name : "adjust_to",
		allowBlank : false,
		hideLabel : true,
		width : 60,
		border : false,
		value : 100,
		onSpinUp : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal < 400) {
				tmpVal = tmpVal + 5
			}
			this.setValue(tmpVal)
		},
		onSpinDown : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal > 10) {
				tmpVal = tmpVal - 5
			}
			this.setValue(tmpVal)
		}
	});
	var fitToRb = new Ext.form.Radio( {
		hideLabel : true,
		name : "rb-scaling",
		width : 20,
		inputValue : 1
	});
	var pageWideSpn = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 1,
			maxValue : 400
		}),
		name : "adjust_to",
		allowBlank : false,
		hideLabel : true,
		width : 60,
		border : false,
		value : 1,
		onSpinUp : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal < 400) {
				tmpVal = tmpVal + 5
			}
			this.setValue(tmpVal)
		},
		onSpinDown : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal > 10) {
				tmpVal = tmpVal - 5
			}
			this.setValue(tmpVal)
		}
	});
	var pageTallSpn = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 1,
			maxValue : 400
		}),
		name : "adjust_to",
		allowBlank : false,
		hideLabel : true,
		width : 60,
		border : false,
		value : 1,
		onSpinUp : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal < 400) {
				tmpVal = tmpVal + 5
			}
			this.setValue(tmpVal)
		},
		onSpinDown : function() {
			var tmpVal = this.getRawValue();
			tmpVal = parseInt(tmpVal);
			if (tmpVal > 10) {
				tmpVal = tmpVal - 5
			}
			this.setValue(tmpVal)
		}
	});
	var paperSizeCmb = new Ext.form.ComboBox( {
		fieldLabel : "Paper size".localize(),
		width : 300,
		store : new Ext.data.SimpleStore( {
			data : [ [ 0, "A4" ], [ 1, "Letter".localize() ] ],
			fields : [ {
				name : "paperSizeID"
			}, {
				name : "paperSizeName"
			} ]
		}),
		displayField : "paperSizeName",
		value : "A4",
		editable : false,
		mode : "local",
		triggerAction : "all"
	});
	var firstPageNumberTxf = new Ext.form.TextField( {
		fieldLabel : "First page".localize(),
		name : "userName",
		value : "Auto",
		allowBlank : false,
		width : 100
	});
	var topMarginSpn = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 100
		}),
		name : "top",
		allowBlank : false,
		fieldLabel : "Top".localize(),
		width : 60,
		border : false,
		value : 0.75,
		onSpinUp : function() {
			changePic(1);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal < 99.75) {
				tmpVal = (tmpVal + 0.25).toFixed(2)
			} else {
				tmpVal = 100
			}
			this.setValue(tmpVal)
		},
		onSpinDown : function() {
			changePic(1);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal > 0.55) {
				tmpVal = (tmpVal - 0.25).toFixed(2)
			} else {
				tmpVal = 0.3
			}
			this.setValue(tmpVal)
		}
	});
	var headerMarginSpn = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 100
		}),
		name : "header",
		allowBlank : false,
		fieldLabel : "Header".localize(),
		width : 60,
		border : false,
		value : 0.3,
		onSpinUp : function() {
			changePic(2);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal < 99.75) {
				tmpVal = (tmpVal + 0.25).toFixed(2)
			} else {
				tmpVal = 100
			}
			this.setValue(tmpVal)
		},
		onSpinDown : function() {
			changePic(2);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal > 0.45) {
				tmpVal = (tmpVal - 0.25).toFixed(2)
			} else {
				tmpVal = 0.2
			}
			this.setValue(tmpVal)
		}
	});
	var leftMarginSpn = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 100
		}),
		name : "left",
		allowBlank : false,
		fieldLabel : "Left".localize(),
		width : 60,
		border : false,
		value : 0.7,
		onSpinUp : function() {
			changePic(3);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal < 99.75) {
				tmpVal = (tmpVal + 0.25).toFixed(2)
			} else {
				tmpVal = 100
			}
			this.setValue(tmpVal)
		},
		onSpinDown : function() {
			changePic(3);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal > 0.5) {
				tmpVal = (tmpVal - 0.25).toFixed(2)
			} else {
				tmpVal = 0.25
			}
			this.setValue(tmpVal)
		}
	});
	var rightMarginSpn = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 100
		}),
		name : "right",
		allowBlank : false,
		fieldLabel : "Right".localize(),
		width : 60,
		border : false,
		value : 0.7,
		onSpinUp : function() {
			changePic(4);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal < 99.75) {
				tmpVal = (tmpVal + 0.25).toFixed(2)
			} else {
				tmpVal = 100
			}
			this.setValue(tmpVal)
		},
		onSpinDown : function() {
			changePic(4);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal > 0.5) {
				tmpVal = (tmpVal - 0.25).toFixed(2)
			} else {
				tmpVal = 0.25
			}
			this.setValue(tmpVal)
		}
	});
	var bottomMarginSpn = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 100
		}),
		name : "bottom",
		allowBlank : false,
		fieldLabel : "Bottom".localize(),
		width : 60,
		border : false,
		value : 0.75,
		onSpinUp : function() {
			changePic(5);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal < 99.75) {
				tmpVal = (tmpVal + 0.25).toFixed(2)
			} else {
				tmpVal = 100
			}
			this.setValue(tmpVal)
		},
		onSpinDown : function() {
			changePic(5);
			pageMarginsDV.select(2, false, false);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal > 0.55) {
				tmpVal = (tmpVal - 0.25).toFixed(2)
			} else {
				tmpVal = 0.3
			}
			this.setValue(tmpVal)
		}
	});
	var footerMarginSpn = new Ext.ux.form.Spinner( {
		strategy : new Ext.ux.form.Spinner.NumberStrategy( {
			xtype : "number",
			minValue : 0,
			maxValue : 100
		}),
		name : "footer",
		allowBlank : false,
		fieldLabel : "Footer".localize(),
		width : 60,
		border : false,
		value : 0.3,
		onSpinUp : function() {
			changePic(6);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal < 99.75) {
				tmpVal = (tmpVal + 0.25).toFixed(2)
			} else {
				tmpVal = 100
			}
			this.setValue(tmpVal)
		},
		onSpinDown : function() {
			changePic(6);
			var tmpVal = this.getRawValue();
			tmpVal = parseFloat(tmpVal);
			if (tmpVal > 0.45) {
				tmpVal = (tmpVal - 0.25).toFixed(2)
			} else {
				tmpVal = 0.2
			}
			this.setValue(tmpVal)
		}
	});
	var horizontallyChb = new Ext.form.Checkbox( {
		boxLabel : "Horizontally".localize(),
		hideLabel : true,
		handler : centerPic,
		inputValue : 8
	});
	var verticallyChb = new Ext.form.Checkbox( {
		boxLabel : "Vertically".localize(),
		hideLabel : true,
		handler : centerPic,
		inputValue : 8
	});
	function centerPic() {
		var record = pageMardinsStore.getAt(0);
		var hor = horizontallyChb.checked;
		var ver = verticallyChb.checked;
		var ls = landscapeRb.checked;
		if (ls) {
			pageMarginsDV.tpl = pageMarginsTemplateH;
			if (ver && hor) {
				_selectedData = centerHorVerMardinsLSData
			} else {
				if (hor) {
					_selectedData = centerHorMardinsLSData
				} else {
					if (ver) {
						_selectedData = centerVerMardinsLSData
					} else {
						_selectedData = pageMardinsLSData
					}
				}
			}
		} else {
			pageMarginsDV.tpl = pageMarginsTemplateV;
			if (ver && hor) {
				_selectedData = centerHorVerMardinsData
			} else {
				if (hor) {
					_selectedData = centerHorMardinsData
				} else {
					if (ver) {
						_selectedData = centerVerMardinsData
					} else {
						_selectedData = pageMardinsData
					}
				}
			}
		}
		changePic(0);
		pageMarginsDV.refresh()
	}
	function changePic(selectedIndex) {
		var record = pageMardinsStore.getAt(0);
		record.set("pic", _selectedData[selectedIndex][1])
	}
	var pageMardinsData = [ [ "start", "page_margins" ],
			[ "top", "page_margins_top" ], [ "header", "page_margins_header" ],
			[ "left", "page_margins_left" ], [ "rignt", "page_margins_right" ],
			[ "bottom", "page_margins_bottom" ],
			[ "footer", "page_margins_footer" ] ];
	_selectedData = pageMardinsData;
	var centerHorMardinsData = [
			[ "center_horizontally", "center_horizontally" ],
			[ "center_horizontally_top", "center_horizontally_top" ],
			[ "center_horizontally_header", "center_horizontally_header" ],
			[ "center_horizontally_left", "center_horizontally_left" ],
			[ "center_horizontally_right", "center_horizontally_right" ],
			[ "center_horizontally_bottom", "center_horizontally_bottom" ],
			[ "center_horizontally_footer", "center_horizontally_footer" ] ];
	var centerVerMardinsData = [ [ "center_vertically", "center_vertically" ],
			[ "center_vertically_top", "center_vertically_top" ],
			[ "center_vertically_header", "center_vertically_header" ],
			[ "center_vertically_left", "center_vertically_left" ],
			[ "center_vertically_right", "center_vertically_right" ],
			[ "center_vertically_bottom", "center_vertically_bottom" ],
			[ "center_vertically_footer", "center_vertically_footer" ] ];
	var centerHorVerMardinsData = [ [ "center_hor_ver", "center_hor_ver" ],
			[ "center_hor_ver_header", "center_hor_ver_header" ],
			[ "center_hor_ver_top", "center_hor_ver_top" ],
			[ "center_hor_ver_left", "center_hor_ver_left" ],
			[ "center_hor_ver_right", "center_hor_ver_right" ],
			[ "center_hor_ver_bottom", "center_hor_ver_bottom" ],
			[ "center_hor_ver_footer", "center_hor_ver_footer" ] ];
	var pageMardinsLSData = [ [ "start", "page_margins_ls" ],
			[ "top_ls", "page_margins_top_ls" ],
			[ "header_ls", "page_margins_header_ls" ],
			[ "left_ls", "page_margins_left_ls" ],
			[ "rignt_ls", "page_margins_right_ls" ],
			[ "bottom_ls", "page_margins_bottom_ls" ],
			[ "footer_ls", "page_margins_footer_ls" ] ];
	var centerHorMardinsLSData = [
			[ "center_horizontally_ls", "center_horizontally_ls" ],
			[ "center_horizontally_top_ls", "center_horizontally_top_ls" ],
			[ "center_horizontally_header_ls", "center_horizontally_header_ls" ],
			[ "center_horizontally_left_ls", "center_horizontally_left_ls" ],
			[ "center_horizontally_right_ls", "center_horizontally_right_ls" ],
			[ "center_horizontally_bottom_ls", "center_horizontally_bottom_ls" ],
			[ "center_horizontally_footer_ls", "center_horizontally_footer_ls" ] ];
	var centerVerMardinsLSData = [
			[ "center_vertically_ls", "center_vertically_ls" ],
			[ "center_vertically_top_ls", "center_vertically_top_ls" ],
			[ "center_vertically_header_ls", "center_vertically_header_ls" ],
			[ "center_vertically_left_ls", "center_vertically_left_ls" ],
			[ "center_vertically_right_ls", "center_vertically_right_ls" ],
			[ "center_vertically_bottom_ls", "center_vertically_bottom_ls" ],
			[ "center_vertically_footer_ls", "center_vertically_footer_ls" ] ];
	var centerHorVerMardinsLSData = [
			[ "center_hor_ver_ls", "center_hor_ver_ls" ],
			[ "center_hor_ver_top_ls", "center_hor_ver_top_ls" ],
			[ "center_hor_ver_header_ls", "center_hor_ver_header_ls" ],
			[ "center_hor_ver_left_ls", "center_hor_ver_left_ls" ],
			[ "center_hor_ver_right_ls", "center_hor_ver_right_ls" ],
			[ "center_hor_ver_bottom_ls", "center_hor_ver_bottom_ls" ],
			[ "center_hor_ver_footer_ls", "center_hor_ver_footer_ls" ] ];
	var br = 20;
	var pageMardinsStore = new Ext.data.SimpleStore( {
		fields : [ "marginName", "pic" ],
		data : [ [ "start", "page_margins" ] ]
	});
	var pageMarginsTemplateV = new Ext.XTemplate(
			'<div><tpl for=".">',
			'<div class="thumb-wrap">',
			'<div class="thumb" style="padding: 5px; width:145px; height:145px;  text-align: center; display: table-cell; vertical-align:middle;">',
			'<img class="{pic}" src="../lib/ext/resources/images/default/s.gif" ',
			'width="108" height="142"', " /></div></div>", "</tpl></div>");
	var pageMarginsTemplateH = new Ext.XTemplate(
			'<div><tpl for=".">',
			'<div class="thumb-wrap">',
			'<div class="thumb" style="padding: 5px; width:145px; height:145px;  text-align: center; display: table-cell; vertical-align:middle;">',
			'<img class="{pic}" src="../lib/ext/resources/images/default/s.gif" ',
			'width="142" height="108"', " /></div></div>", "</tpl></div>");
	var pageMarginsDV = new Ext.DataView(
			{
				id : "page-margins-dv",
				store : pageMardinsStore,
				tpl : pageMarginsTemplateV,
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				emptyText : "No images to display".localize(),
				listeners : {
					afterrender : function() {
						horizontallyChb
								.setValue(_preselctionChbValues.horizontallyChb);
						verticallyChb
								.setValue(_preselctionChbValues.verticallyChb);
						centerPic()
					}
				}
			});
	var pageMarginPanel = {
		layout : "column",
		border : false,
		items : [ {
			layout : "absolute",
			border : false,
			columnWidth : 0.3,
			height : 255,
			items : [ {
				layout : "form",
				labelAlign : "top",
				border : false,
				width : 65,
				x : 65,
				y : 100,
				items : leftMarginSpn
			} ]
		}, {
			layout : "absolute",
			border : false,
			columnWidth : 0.4,
			height : 255,
			items : [ {
				layout : "form",
				labelAlign : "top",
				border : false,
				width : 65,
				x : 62,
				y : 0,
				items : topMarginSpn
			}, {
				layout : "fit",
				height : 155,
				width : 155,
				x : 15,
				y : 50,
				items : pageMarginsDV
			}, {
				layout : "form",
				labelAlign : "top",
				border : false,
				width : 65,
				x : 62,
				y : 210,
				items : bottomMarginSpn
			} ]
		}, {
			layout : "absolute",
			border : false,
			columnWidth : 0.3,
			height : 255,
			items : [ {
				layout : "form",
				labelAlign : "top",
				border : false,
				width : 65,
				x : 5,
				y : 0,
				items : headerMarginSpn
			}, {
				layout : "form",
				labelAlign : "top",
				border : false,
				width : 65,
				x : 5,
				y : 100,
				items : rightMarginSpn
			}, {
				layout : "form",
				labelAlign : "top",
				border : false,
				width : 65,
				x : 5,
				y : 210,
				items : footerMarginSpn
			} ]
		} ]
	};
	var centerOnPageFs = new Ext.form.FieldSet( {
		title : "Orientation".localize(),
		layout : "form",
		cls : "custom-title-fieldset",
		bodyStyle : "padding-top:10px;",
		autoHeight : true,
		items : [ horizontallyChb, verticallyChb ]
	});
	var headerStore = new Ext.data.SimpleStore( {
		fields : [ "leftStyle", "leftValue", "leftPreview", "centerStyle",
				"centerValue", "centerPreview", "rightStyle", "rightValue",
				"rightPreview" ],
		data : [ [ "", "", "", "", "", "", "", "", "" ] ]
	});
	var footerStore = new Ext.data.SimpleStore( {
		fields : [ "leftStyle", "leftValue", "leftPreview", "centerStyle",
				"centerValue", "centerPreview", "rightStyle", "rightValue",
				"rightPreview" ],
		data : [ [ "", "", "", "", "", "", "", "", "" ] ]
	});
	var headerDV = new Ext.DataView(
			{
				id : "header-dv",
				store : headerStore,
				tpl : new Ext.XTemplate(
						'<div class="header"><tpl for=".">',
						'<div class="thumb-wrap">',
						'<div class="thumb" style="padding-left: 2px; text-align: left;"><table>',
						'<tr><td width=145 height=50 align=left valign=top style="{leftStyle}">{leftPreview}</td><td width=145 height=50 align=center valign=top style="{centerStyle}">{centerPreview}</td><td width=140 height=50 align=right valign=top style="{rightStyle}">{rightPreview}</td></tr>',
						"</table></div></div>", "</tpl></div>"),
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				emptyText : "No images to display".localize()
			});
	var headerCmb = new Ext.form.ComboBox( {
		fieldLabel : "Header".localize(),
		width : 450,
		store : new Ext.data.SimpleStore(
				{
					data : [
							[ "", "", "(none)".localize() ],
							[ "font-size:8pt;", "Page &[Page]",
									"Page".localize() + " 1" ],
							[
									"font-size:8pt;",
									"Page &[Page] of &[Pages]",
									"Page".localize() + " 1 " + "of".localize()
											+ " ?" ],
							[ "font-size:8pt;", "&[Tab]",
									"Sheet".localize() + " 1" ],
							[ "font-size:8pt;", "&[File]",
									"Book".localize() + " 1" ] ],
					fields : [ {
						name : "preDefHStyle"
					}, {
						name : "preDefHValue"
					}, {
						name : "preDefHPreview"
					} ]
				}),
		value : "(none)",
		displayField : "preDefHPreview",
		editable : false,
		mode : "local",
		triggerAction : "all",
		listeners : {
			select : function(cmb, tmpRec, index) {
				var record = headerStore.getAt(0);
				setCmbSelection(record, tmpRec, "preDefH")
			}
		}
	});
	var footerDV = new Ext.DataView(
			{
				id : "footer-dv",
				store : footerStore,
				tpl : new Ext.XTemplate(
						'<div class="footer"><tpl for=".">',
						'<div class="thumb-wrap">',
						'<div class="thumb" style="padding-bottom: 2px; text-align: left;"><table>',
						'<tr><td width=145 height=50 align=left valign=bottom style="{leftStyle}">{leftPreview}</td><td width=145 height=50 align=center valign=bottom style="{centerStyle}">{centerPreview}</td><td width=140 height=50 align=right valign=bottom style="{rightStyle}">{rightPreview}</td></tr>',
						"</table></div></div>", "</tpl></div>"),
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				emptyText : "No images to display".localize()
			});
	var footerCmb = new Ext.form.ComboBox( {
		fieldLabel : "Footer".localize(),
		width : 450,
		store : new Ext.data.SimpleStore(
				{
					data : [
							[ "", "", "(none)".localize() ],
							[ "font-size:8pt;", "Page &[Page]",
									"Page".localize() + " 1" ],
							[
									"font-size:8pt;",
									"Page &[Page] of &[Pages]",
									"Page".localize() + " 1 " + "of".localize()
											+ " ?" ],
							[ "font-size:8pt;", "&[Tab]",
									"Sheet".localize() + " 1" ],
							[ "font-size:8pt;", "&[File]",
									"Book".localize() + " 1" ] ],
					fields : [ {
						name : "preDefFStyle"
					}, {
						name : "preDefFValue"
					}, {
						name : "preDefFPreview"
					} ]
				}),
		value : "(none)",
		displayField : "preDefFPreview",
		editable : false,
		mode : "local",
		triggerAction : "all",
		listeners : {
			select : function(cmb, tmpRec, index) {
				var record = footerStore.getAt(0);
				setCmbSelection(record, tmpRec, "preDefF")
			}
		}
	});
	function setCmbSelection(record, tmpRec, s) {
		var preview = tmpRec.get(s + "Preview") === "(none)" ? "" : tmpRec
				.get(s + "Preview");
		record.set("leftStyle", "");
		record.set("leftValue", "");
		record.set("leftPreview", "");
		record.set("centerStyle", tmpRec.get(s + "Style"));
		record.set("centerValue", tmpRec.get(s + "Value"));
		record.set("centerPreview", preview);
		record.set("rightStyle", "");
		record.set("rightValue", "");
		record.set("rightPreview", "")
	}
	var headerFooterPanel = {
		layout : "form",
		labelAlign : "top",
		border : false,
		defaults : {
			border : false
		},
		items : [
				{
					layout : "fit",
					height : 60,
					width : 455,
					items : headerDV
				},
				headerCmb,
				{
					layout : "form",
					buttons : [
							{
								text : "Custom Header".localize().concat("..."),
								handler : function() {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.customHeaderFooter,
													[
															"Header".localize(),
															customHandleFunction,
															headerStore
																	.getAt(0) ])
								}
							},
							{
								text : "Custom Footer".localize().concat("..."),
								handler : function() {
									Jedox.wss.app
											.load(
													Jedox.wss.app.dynJSRegistry.customHeaderFooter,
													[
															"Footer".localize(),
															customHandleFunction,
															footerStore
																	.getAt(0) ])
								}
							} ]
				}, footerCmb, {
					layout : "fit",
					height : 62,
					width : 450,
					items : footerDV
				} ]
	};
	var customHandleFunction = function(tmpRec, hORf) {
		var record;
		if (hORf === "Header") {
			record = headerStore.getAt(0)
		} else {
			record = footerStore.getAt(0)
		}
		record.set("leftStyle", tmpRec.get("leftStyle"));
		record.set("leftValue", tmpRec.get("leftValue"));
		record.set("leftPreview", tmpRec.get("leftPreview"));
		record.set("centerStyle", tmpRec.get("centerStyle"));
		record.set("centerValue", tmpRec.get("centerValue"));
		record.set("centerPreview", tmpRec.get("centerPreview"));
		record.set("rightStyle", tmpRec.get("rightStyle"));
		record.set("rightValue", tmpRec.get("rightValue"));
		record.set("rightPreview", tmpRec.get("rightPreview"))
	};
	function getPreview(value) {
		var date = new Date();
		var today = date.getDate() + "/" + date.getMonth() + 1 + "/"
				+ date.getFullYear();
		var time = date.getHours() + ":" + date.getMinutes() + ":"
				+ date.getSeconds();
		var v = [ "&[Page]", "&[Pages]", "&[Date]", "&[Time]", "&[Tab]",
				"&[File]" ];
		var p = [ "1", "5", today, time, "Sheet 1", "Book 1" ];
		var preview = value;
		for ( var i = 0; i < v.length; i++) {
			preview = preview.replace(v[i], p[i])
		}
		return preview
	}
	var refersToTxf = new Ext.form.TextField( {
		fieldLabel : "Print area".localize(),
		width : 315
	});
	var selectRangeBtn = new Ext.Button( {
		id : "selRangeBegin",
		iconCls : "select-range-icon",
		cls : "x-btn-icon",
		tooltip : "Select Range".localize(),
		handler : function() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.selectRange, [ {
				fnc : [ this, selRangeHandleFnc ],
				format : "{$Range}",
				rng : refersToTxf.getValue()
			} ]);
			win.hide()
		}
	});
	function selRangeHandleFnc(selected) {
		win.show();
		refersToTxf.setValue(selected)
	}
	var gridLinesChb = new Ext.form.Checkbox( {
		boxLabel : "Gridlines".localize(),
		hideLabel : true,
		handler : centerPic
	});
	var cellErrorsCmb = new Ext.form.ComboBox( {
		fieldLabel : "Cell Errors As".localize(),
		width : 190,
		store : new Ext.data.SimpleStore( {
			data : [ [ 0, "displayed" ], [ 1, "blank" ], [ 2, "--" ],
					[ 3, "#N/A" ] ],
			fields : [ {
				name : "cellErrorID"
			}, {
				name : "cellErrorName"
			} ]
		}),
		value : "displayed",
		displayField : "cellErrorName",
		editable : false,
		mode : "local",
		triggerAction : "all"
	});
	var downThenOverRb = new Ext.form.Radio( {
		hideLabel : true,
		boxLabel : "Down, then over".localize(),
		checked : true,
		name : "rb-page-order",
		listeners : {
			check : function(chb, checked) {
				if (checked) {
					setPageOrderPic(0)
				}
			}
		},
		inputValue : 0
	});
	var overThenDownRb = new Ext.form.Radio( {
		hideLabel : true,
		boxLabel : "Over, then down".localize(),
		name : "rb-page-order",
		listeners : {
			check : function(chb, checked) {
				if (checked) {
					setPageOrderPic(1)
				}
			}
		},
		inputValue : 1
	});
	var pageOrderData = [ [ "down_over", "down_over" ],
			[ "over_down", "over_down" ] ];
	var pageOrderStore = new Ext.data.SimpleStore( {
		fields : [ "title", "pic" ],
		data : [ [ "down_over", "down_over" ] ]
	});
	function setPageOrderPic(selectedIndex) {
		var record = pageOrderStore.getAt(0);
		record.set("pic", pageOrderData[selectedIndex][1])
	}
	var pageOrderDV = new Ext.DataView(
			{
				id : "page-order-dv",
				store : pageOrderStore,
				tpl : new Ext.XTemplate(
						'<div class="folder-navigation"><tpl for=".">',
						'<div class="thumb-wrap">',
						'<div class="thumb" style="padding: 5px; text-align: center; "><img class="{pic}" src="../lib/ext/resources/images/default/s.gif" width="59" height="49" /></div></div>',
						"</tpl></div>"),
				multiSelect : false,
				singleSelect : true,
				overClass : "x-view-over",
				itemSelector : "div.thumb-wrap",
				emptyText : "No images to display".localize()
			});
	var printAreaPanel = {
		layout : "column",
		border : false,
		baseCls : "x-plain",
		bodyStyle : "padding-top:10px; padding-bottom:10px",
		items : [ {
			layout : "form",
			border : false,
			width : 425,
			baseCls : "main-panel",
			items : refersToTxf
		}, {
			layout : "form",
			border : false,
			width : 30,
			baseCls : "x-plain",
			items : selectRangeBtn
		} ]
	};
	var printFs = new Ext.form.FieldSet(
			{
				title : "Print".localize(),
				cls : "custom-title-fieldset",
				bodyStyle : "padding-top:10px;",
				autoHeight : true,
				items : {
					layout : "column",
					defaults : {
						border : false
					},
					baseCls : "main-panel",
					items : [
							{
								layout : "form",
								width : 150,
								items : gridLinesChb,
								listeners : {
									afterrender : function() {
										gridLinesChb
												.setValue(_preselctionChbValues.gridLinesChb)
									}
								}
							}, {
								layout : "form",
								width : 300,
								items : cellErrorsCmb
							} ]
				}
			});
	var pageOrderFs = new Ext.form.FieldSet( {
		title : "Page order".localize(),
		cls : "custom-title-fieldset",
		bodyStyle : "padding-top:10px;",
		autoHeight : true,
		items : {
			layout : "column",
			defaults : {
				border : false
			},
			baseCls : "main-panel",
			items : [ {
				layout : "form",
				width : 150,
				height : 60,
				items : [ downThenOverRb, overThenDownRb ]
			}, {
				layout : "fit",
				width : 150,
				height : 60,
				items : pageOrderDV
			} ]
		}
	});
	var orientationFs = new Ext.form.FieldSet(
			{
				title : "Orientation".localize(),
				cls : "custom-title-fieldset",
				bodyStyle : "padding-top:10px;",
				autoHeight : true,
				items : {
					layout : "column",
					baseCls : "x-plain",
					columns : 2,
					items : [
							{
								html : '<img class="ico_portrait" src="../lib/ext/resources/images/default/s.gif" align="center" width="36" height="36" />',
								baseCls : "x-plain",
								width : 50
							},
							new Ext.Panel( {
								border : false,
								layout : "form",
								bodyStyle : "background-color:transparent;",
								autoWidth : true,
								autoHeight : true,
								items : [ portraitRb ]
							}),
							{
								html : '<img class="ico_landscape" align="center" src="../lib/ext/resources/images/default/s.gif" width="36" height="36" />',
								baseCls : "x-plain",
								width : 50
							}, new Ext.Panel( {
								border : false,
								layout : "form",
								bodyStyle : "background-color:transparent;",
								autoWidth : true,
								autoHeight : true,
								items : [ landscapeRb ]
							}) ]
				}
			});
	var scalingFs = new Ext.form.FieldSet( {
		title : "Scaling".localize(),
		cls : "custom-title-fieldset",
		bodyStyle : "padding-top:10px;",
		autoHeight : true,
		items : [ {
			layout : "column",
			border : false,
			height : 28,
			items : [ new Ext.Panel( {
				border : false,
				layout : "form",
				bodyStyle : "background-color:transparent;",
				autoWidth : true,
				autoHeight : true,
				items : [ adjustToRb ]
			}), {
				html : "Adjust to".localize() + ":",
				baseCls : "x-plain",
				width : 80,
				style : "padding-top:4px;"
			}, {
				border : false,
				items : adjustToSpn
			}, {
				html : "&nbsp&nbsp% normal size",
				baseCls : "x-plain"
			} ]
		}, {
			layout : "column",
			columns : 4,
			border : false,
			items : [ new Ext.Panel( {
				border : false,
				layout : "form",
				bodyStyle : "background-color:transparent;",
				autoWidth : true,
				autoHeight : true,
				items : [ fitToRb ]
			}), {
				html : "Fit to".localize() + ":",
				baseCls : "x-plain",
				width : 80,
				style : "padding-top:4px;"
			}, {
				border : false,
				items : pageWideSpn
			}, {
				html : "&nbsp&nbsp page(s) wide by",
				baseCls : "x-plain",
				width : 120
			}, {
				border : false,
				items : pageTallSpn
			}, {
				html : "&nbsp&nbsp tall",
				baseCls : "ja"
			} ]
		} ]
	});
	var printOptionsFs = new Ext.form.FieldSet( {
		cls : "custom-title-fieldset",
		bodyStyle : "padding-top:10px;",
		autoHeight : true,
		items : [ paperSizeCmb, firstPageNumberTxf ]
	});
	var pageSetupTbs = new Ext.TabPanel(
			{
				region : "center",
				height : 385,
				layoutOnTabChange : true,
				baseCls : "x-plain",
				activeTab : 0,
				defaults : {
					bodyStyle : "background-color: transparent; padding:10px 5px 5px 10px;"
				},
				items : [ {
					id : "page",
					title : "Page".localize(),
					baseCls : "x-plain",
					items : [ orientationFs, scalingFs, printOptionsFs ]
				}, {
					id : "margins",
					title : "Margins".localize(),
					baseCls : "x-plain",
					items : [ pageMarginPanel, centerOnPageFs ]
				}, {
					id : "header-footer",
					title : "Header/Footer".localize(),
					baseCls : "x-plain",
					items : [ headerFooterPanel ]
				}, {
					id : "sheet",
					title : "Sheet".localize(),
					baseCls : "x-plain",
					items : [ printAreaPanel, printFs, pageOrderFs ]
				} ]
			});
	var mainPanel = new Ext.Panel( {
		modal : true,
		layout : "form",
		baseCls : "main-panel",
		border : false,
		items : [ pageSetupTbs, {
			layout : "form",
			border : false,
			height : 30
		} ]
	});
	var win = new Ext.Window( {
		id : "page-setup-dlg",
		defaults : {
			bodyStyle : "padding:2px"
		},
		title : "Page Setup".localize(),
		closable : true,
		closeAction : "close",
		autoDestroy : true,
		plain : true,
		cls : "default-format-window",
		constrain : true,
		modal : true,
		resizable : false,
		animCollapse : false,
		layout : "fit",
		width : 500,
		height : 500,
		items : mainPanel,
		listeners : {
			close : function() {
				Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
				Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY;
				Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.pageSetup)
			}
		},
		buttons : [ {
			text : "OK".localize(),
			handler : function() {
				phpWSSServer.storePageSetup(_genSavingObj());
				win.close()
			}
		}, {
			text : "Cancel".localize(),
			handler : function() {
				win.close()
			}
		} ]
	});
	var _genSavingObj = function() {
		var tmpObj;
		var ps = {};
		ps.page = {};
		ps.margins = {};
		ps.hf = {};
		ps.sheet = {};
		tmpObj = ps.page;
		tmpObj.portrait = portraitRb.getValue();
		tmpObj.adjust = {
			enabled : adjustToRb.getValue(),
			size : adjustToSpn.getValue()
		};
		tmpObj.fit = {
			enabled : fitToRb.getValue(),
			wide : pageWideSpn.getValue(),
			tall : pageTallSpn.getValue()
		};
		tmpObj.paper_size = paperSizeCmb.getValue();
		tmpObj.first_page = firstPageNumberTxf.getValue();
		tmpObj = ps.margins;
		tmpObj.top = topMarginSpn.getValue();
		tmpObj.left = leftMarginSpn.getValue();
		tmpObj.bottom = bottomMarginSpn.getValue();
		tmpObj.right = rightMarginSpn.getValue();
		tmpObj.header = headerMarginSpn.getValue();
		tmpObj.footer = footerMarginSpn.getValue();
		tmpObj.horiz = horizontallyChb.getValue();
		tmpObj.vert = verticallyChb.getValue();
		tmpObj = ps.hf;
		var tmpRec = headerStore.getAt(0);
		tmpObj.header = {};
		tmpObj.header.left_style = tmpRec.get("leftStyle");
		tmpObj.header.left_value = tmpRec.get("leftValue");
		tmpObj.header.center_style = tmpRec.get("centerStyle");
		tmpObj.header.center_value = tmpRec.get("centerValue");
		tmpObj.header.right_style = tmpRec.get("rightStyle");
		tmpObj.header.right_value = tmpRec.get("rightValue");
		tmpRec = footerStore.getAt(0);
		tmpObj.footer = {};
		tmpObj.footer.left_style = tmpRec.get("leftStyle");
		tmpObj.footer.left_value = tmpRec.get("leftValue");
		tmpObj.footer.center_style = tmpRec.get("centerStyle");
		tmpObj.footer.center_value = tmpRec.get("centerValue");
		tmpObj.footer.right_style = tmpRec.get("rightStyle");
		tmpObj.footer.right_value = tmpRec.get("rightValue");
		tmpObj = ps.sheet;
		var tmpRngStr = refersToTxf.getValue(), refs = Jedox.wss.formula
				.parse(tmpRngStr);
		tmpObj.print_area = refs.length ? refs[0].rng : "";
		tmpObj.print_area_string = tmpRngStr;
		tmpObj.gridlines = gridLinesChb.getValue();
		tmpObj.cell_errors = cellErrorsCmb.getValue();
		tmpObj.page_order = downThenOverRb.getValue();
		return ps
	};
	var initPageSetupDlg = function() {
		var result = phpWSSServer.getPageSetup();
		if (result[0]) {
			if (result[1]) {
				var ps = result[1];
				portraitRb.setValue(ps.page.portrait);
				landscapeRb.setValue(!ps.page.portrait);
				adjustToRb.setValue(ps.page.adjust.enabled);
				adjustToSpn.setValue(ps.page.adjust.size);
				fitToRb.setValue(ps.page.fit.enabled);
				pageWideSpn.setValue(ps.page.fit.wide);
				pageTallSpn.setValue(ps.page.fit.tall);
				paperSizeCmb.setValue(ps.page.paper_size);
				firstPageNumberTxf.setValue(ps.page.first_page);
				var tmpHRec = new Ext.data.Record( {
					leftStyle : ps.hf.header.left_style,
					leftValue : ps.hf.header.left_value,
					leftPreview : getPreview(ps.hf.header.left_value),
					centerStyle : ps.hf.header.center_style,
					centerValue : ps.hf.header.center_value,
					centerPreview : getPreview(ps.hf.header.center_value),
					rightStyle : ps.hf.header.right_style,
					rightValue : ps.hf.header.right_value,
					rightPreview : getPreview(ps.hf.header.right_value)
				});
				var tmpFRec = new Ext.data.Record( {
					leftStyle : ps.hf.footer.left_style,
					leftValue : ps.hf.footer.left_value,
					leftPreview : getPreview(ps.hf.footer.left_value),
					centerStyle : ps.hf.footer.center_style,
					centerValue : ps.hf.footer.center_value,
					centerPreview : getPreview(ps.hf.footer.center_value),
					rightStyle : ps.hf.footer.right_style,
					rightValue : ps.hf.footer.right_value,
					rightPreview : getPreview(ps.hf.footer.right_value)
				});
				customHandleFunction(tmpHRec, "Header");
				customHandleFunction(tmpFRec, "Footer");
				topMarginSpn.setValue(ps.margins.top);
				headerMarginSpn.setValue(ps.margins.header);
				leftMarginSpn.setValue(ps.margins.left);
				rightMarginSpn.setValue(ps.margins.right);
				bottomMarginSpn.setValue(ps.margins.bottom);
				footerMarginSpn.setValue(ps.margins.footer);
				_preselctionChbValues.horizontallyChb = ps.margins.horiz;
				_preselctionChbValues.verticallyChb = ps.margins.vert;
				refersToTxf.setValue(ps.sheet.print_area_string);
				_preselctionChbValues.gridLinesChb = ps.sheet.gridlines;
				cellErrorsCmb.setValue(ps.sheet.cell_errors);
				downThenOverRb.setValue(ps.sheet.page_order);
				overThenDownRb.setValue(!ps.sheet.page_order);
				setPageOrderPic(ps.sheet.page_order ? 0 : 1)
			} else {
			}
		} else {
			console.log(result)
		}
	};
	initPageSetupDlg();
	win.show(this)
};