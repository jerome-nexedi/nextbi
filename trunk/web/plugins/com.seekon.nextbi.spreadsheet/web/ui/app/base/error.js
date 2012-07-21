Jedox.wss.error = new function() {
	var that = this;
	var _types = {
		INFO : 0,
		WARN : 1,
		ERR : 2
	};
	var _flags = {
		SILENT : 1,
		FATAL : 2
	};
	var _origins = {
		NA : 0,
		UI : 1,
		UI_B : 2,
		CORE : 3
	};
	var _origins_desc = [ "General", "Application", "Application Backend",
			"Server" ];
	var _db = {
		1 : {
			name : "GenericException",
			type : _types.ERR,
			origin : _origins.NA
		},
		100 : {
			name : "RuntimeException",
			type : _types.ERR,
			origin : _origins.UI_B
		},
		101 : {
			name : "ParsingFailedException",
			type : _types.ERR,
			origin : _origins.UI_B
		},
		102 : {
			name : "NotImplementedException",
			type : _types.ERR,
			origin : _origins.UI_B
		},
		103 : {
			name : "InvalidSessionException",
			type : _types.ERR,
			flag : _flags.FATAL,
			origin : _origins.UI_B
		},
		1000 : {
			name : "InsufficientRightsException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1100 : {
			name : "EventHandlerAbortException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1200 : {
			name : "ClipboardInvalidIndexException",
			type : _types.WARN,
			origin : _origins.CORE
		},
		1300 : {
			name : "InvalidNameException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1400 : {
			name : "InterpreterRuntimeError",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1500 : {
			name : "InvalidFormulaException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1600 : {
			name : "CyclicDependencyException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1700 : {
			name : "ArrayException",
			type : _types.WARN,
			origin : _origins.CORE
		},
		1800 : {
			name : "NoWorkbookSelectedException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1801 : {
			name : "NoWorksheetSelectedException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1802 : {
			name : "NoApplicationSelectedException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1900 : {
			name : "LoadApplicationException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1901 : {
			name : "LoadException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		1902 : {
			name : "SaveException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		2000 : {
			name : "ConditionalFormatException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		2100 : {
			name : "NamedFormulaException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		2101 : {
			name : "NamedFormulaDoesNotExistException",
			type : _types.WARN,
			origin : _origins.CORE
		},
		2200 : {
			name : "CopyWorksheetException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		2300 : {
			name : "RangeException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		2400 : {
			name : "MergedCellException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		2500 : {
			name : "AuthenticationException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		2600 : {
			name : "CellDimensionException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		2700 : {
			name : "SessionException",
			type : _types.ERR,
			flag : _flags.FATAL,
			origin : _origins.CORE
		},
		2800 : {
			name : "InvalidGroupException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		2900 : {
			name : "InvalidUserException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		3000 : {
			name : "TranslationTableException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		3100 : {
			name : "WorksheetCopyException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		3200 : {
			name : "WorkbookCloneException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		3300 : {
			name : "ExtensionRegistryException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		3400 : {
			name : "ExtensionCallerException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		3500 : {
			name : "CyclicArrayException",
			type : _types.WARN,
			origin : _origins.CORE
		},
		3600 : {
			name : "WorksheetElementException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		3700 : {
			name : "FormatException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		3800 : {
			name : "StyleManagerException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		3900 : {
			name : "BoundedPointException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		4000 : {
			name : "FilterRegistryException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		4100 : {
			name : "ContainerWrapperException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		4200 : {
			name : "SelectionToLargeException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		4300 : {
			name : "ExtensionFailureException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		4400 : {
			name : "PaloConnectorException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		4500 : {
			name : "FatalException",
			type : _types.ERR,
			flag : _flags.FATAL,
			origin : _origins.CORE
		},
		4600 : {
			name : "ArrayFormulaOnMergedCellException",
			type : _types.ERR,
			origin : _origins.CORE
		},
		4700 : {
			name : "ValueConversionException",
			type : _types.ERR,
			origin : _origins.CORE
		}
	};
	function _relogin() {
		window.parent.onbeforeunload = function() {
		};
		window.parent.location.href = "/ui/login/?r"
	}
	this.show = function(code, msg, params) {
		var i18n_err = Jedox.wss.i18n.errors;
		if (!(code in _db) || !(code in i18n_err)) {
			code = 1
		}
		var err = _db[code];
		if (err.flag & _flags.SILENT) {
			return
		}
		var icons = [ Ext.MessageBox.INFO, Ext.MessageBox.WARNING,
				Ext.MessageBox.ERROR ], mb_conf = {
			title : _origins_desc[err.origin].concat(" Error").localize()
					.concat(" ", code),
			icon : icons[err.type],
			buttons : Ext.MessageBox.OK
		}, desc = i18n_err[code];
		if (typeof params == "object" && params.length) {
			for ( var p in params) {
				desc = desc.replace("{".concat(p, "}"), params[p])
			}
		}
		if (typeof msg == "string" && msg.length > 0) {
			desc = desc.concat("<br/><br/><br/>", "Error Data".localize(),
					":<br/>", msg, "<br/>")
		} else {
			desc = desc.concat("<br/>")
		}
		if (err.flag & _flags.FATAL) {
			desc = desc.concat("<br/><b>",
					"This is a fatal error, re-login will be required!"
							.localize(), "</b><br/>");
			mb_conf.fn = _relogin
		}
		mb_conf.msg = desc;
		Ext.MessageBox.show(mb_conf)
	};
	this.scan = function(res) {
		var el;
		for ( var i in res) {
			if ((el = res[i])[0] === false) {
				that.show(el[1], el[2])
			}
		}
	}
};