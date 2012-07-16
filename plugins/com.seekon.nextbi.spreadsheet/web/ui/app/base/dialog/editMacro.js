Jedox.wss.dialog.openEditMacro = function(obj, callBackFnc) {
	Jedox.wss.macro.list( [ this, initModulesTree_post, obj, callBackFnc ],
			Jedox.wss.macro.listFmt.TREE);
	function initModulesTree_post(treeData, obj, callBackFnc) {
		if (treeData.length == 0) {
			Jedox.wss.macro
					.add( [ this, loadModulesTree_post, obj, callBackFnc ],
							"Module1")
		} else {
			if (obj) {
				if (obj.operation == "new") {
					Jedox.wss.macro.load( [ this, addMacro_inner_post,
							treeData, obj, callBackFnc ], treeData[0].uid)
				} else {
					var module = obj.macro.split(".")[0];
					for ( var i = 0; i < treeData.length; i++) {
						if (module == treeData[i].text) {
							Jedox.wss.macro.load( [ this, openDialog_post,
									treeData, obj, callBackFnc ],
									treeData[i].uid);
							break
						}
					}
				}
			} else {
				Jedox.wss.macro.load( [ this, openDialog_post, treeData, obj,
						callBackFnc ], treeData[0].uid)
			}
		}
		function addMacro_inner_post(src, treeData, obj, callBackFnc) {
			var macro = "function ".concat(obj.macro, " ()\n{\n\n}\n");
			var moduleSrc = src.length === 1 ? macro : src
					.concat("\n\n", macro);
			Jedox.wss.macro.save( [ this, loadModulesTree_post, {
				operation : "edit",
				macro : treeData[0].id.concat(".", obj.macro)
			}, callBackFnc ], treeData[0].uid, moduleSrc)
		}
	}
	function loadModulesTree_post(moduleID, obj, callBackFnc) {
		Jedox.wss.macro.list( [ this, initModulesTree_post, obj, callBackFnc ],
				Jedox.wss.macro.listFmt.TREE)
	}
	function openDialog_post(src, treeData, obj, callBackFnc) {
		var that = this;
		var _fromDlgF = false;
		if (Jedox.wss.app.environment.inputMode === Jedox.wss.grid.GridMode.DIALOG) {
			_fromDlgF = true
		} else {
			Jedox.wss.app.lastInputModeDlg = Jedox.wss.app.environment.inputMode;
			Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)
		}
		var treeData = treeData;
		var obj = obj;
		var _module;
		var _clipboard;
		var toolbarPanel = {
			border : false,
			baseCls : "x-plain",
			x : 5,
			y : 5,
			width : 400,
			height : 30,
			tbar : [ {
				tooltip : {
					title : "New Module".localize(),
					text : "Add New Module".localize()
				},
				iconCls : "new-macro-mdl-icon",
				handler : onAddModule,
				scope : this
			}, {
				tooltip : {
					title : "Delete Module".localize(),
					text : "Delete Module".localize().concat("...")
				},
				iconCls : "delete-macro-mdl-icon",
				handler : deleteModule,
				scope : this
			}, {
				tooltip : {
					title : "Rename Module".localize(),
					text : "Rename Module".localize().concat("...")
				},
				iconCls : "rename-macro-mdl-icon",
				handler : function() {
					onRenameModule()
				},
				scope : this
			}, {
				tooltip : {
					title : "Save".localize(),
					text : "Save".localize().concat("...")
				},
				iconCls : "save-macro-mdl-icon",
				handler : onApply,
				scope : this
			}, "-", {
				tooltip : {
					title : "Undo".localize(),
					text : "Undo".localize().concat("...")
				},
				iconCls : "undo-macro-icon",
				handler : function() {
					editor.undo()
				},
				scope : this
			}, {
				tooltip : {
					title : "Redo".localize(),
					text : "Redo".localize().concat("...")
				},
				iconCls : "redo-macro-icon",
				handler : function() {
					editor.redo()
				},
				scope : this
			}, "-", {
				tooltip : {
					title : "Cut".localize(),
					text : "Cut".localize().concat("...")
				},
				iconCls : "cut-macro-icon",
				handler : onCut,
				scope : this
			}, {
				tooltip : {
					title : "Copy".localize(),
					text : "Copy".localize().concat("...")
				},
				iconCls : "copy-macro-icon",
				handler : onCopy,
				scope : this
			}, {
				tooltip : {
					title : "Paste".localize(),
					text : "Paste".localize().concat("...")
				},
				iconCls : "paste-macro-icon",
				handler : onPaste,
				scope : this
			}, "-", {
				tooltip : {
					title : "Find".localize(),
					text : "Find".localize().concat("...")
				},
				iconCls : "find-in-module-icon",
				handler : openFindDialog,
				scope : this
			} ]
		};
		var root = new Ext.tree.TreeNode( {
			text : "Modules Repository".localize(),
			draggable : false,
			editable : false,
			id : "root",
			expanded : true
		});
		loadMacroTreeData(treeData);
		var macrosTP = new Ext.tree.TreePanel( {
			id : "macros-tree",
			animate : false,
			lines : true,
			containerScroll : true,
			ddScroll : true,
			autoScroll : true,
			collapseFirst : false,
			root : root,
			rootVisible : false,
			collapseFirst : true,
			listeners : {
				load : function(node) {
				},
				contextmenu : function(node, e) {
					if (!node.leaf) {
						onModuleCtxMnu(node, e)
					}
				}
			}
		});
		macrosTP.on("click", function(n) {
			n.expand();
			var sn = macrosTP.selModel.getSelectedNode() || {};
			if (n.id != sn.id && n.id != "root") {
				if (n.attributes.leaf) {
					selectMacroInText(n)
				} else {
					loadModule(n.attributes.uid);
					_module = n.attributes
				}
			}
		});
		var moduleEditor = new Ext.tree.TreeEditor(macrosTP, null, {
			id : "module-tree-node-editor",
			editDelay : 0,
			cancelOnEsc : true,
			allowBlank : false,
			ignoreNoChange : true,
			completeOnEnter : true,
			beforeNodeClick : Ext.emptyFn,
			onNodeDblClick : Ext.emptyFn,
			onNodeClick : Ext.emptyFn
		});
		moduleEditor.on( {
			complete : function(moduleEditor, newName, oldName) {
				if (newName != oldName) {
					if (validateName(moduleEditor.editNode, oldName, newName)) {
						var id = moduleEditor.editNode.attributes.id;
						Jedox.wss.macro.rename(
								[ this, afterRename_inner_post ],
								moduleEditor.editNode.attributes.uid, newName)
					} else {
						moduleEditor.cancelEdit()
					}
				}
				function afterRename_inner_post(result) {
					if (!result) {
						showMsgERROR("Error Renaming Module",
								"rename_module_error_msg")
					}
				}
			}
		});
		var descriptionTxa = new Ext.form.TextArea( {
			id : "macro-txa",
			hideLabel : true,
			width : 460,
			height : 420,
			value : src
		});
		var editor;
		var mainPanel = new Ext.Panel( {
			layout : "absolute",
			baseCls : "x-plain",
			border : false,
			items : [ toolbarPanel, {
				layout : "fit",
				border : false,
				baseCls : "x-plain",
				x : 5,
				y : 30,
				width : 230,
				height : 418,
				items : [ macrosTP ]
			}, {
				layout : "fit",
				x : 240,
				y : 30,
				items : [ descriptionTxa ]
			} ]
		});
		var win = new Ext.Window(
				{
					id : "edit-macro-dlg",
					title : "Macro Editor".localize(),
					closable : true,
					maximizable : true,
					closeAction : "close",
					autoDestroy : true,
					cls : "default-format-window",
					plain : true,
					constrain : true,
					modal : true,
					animCollapse : false,
					layout : "fit",
					width : 750,
					height : 490,
					minWidth : 500,
					minHeight : 400,
					items : mainPanel,
					listeners : {
						close : function() {
							if (!_fromDlgF) {
								Jedox.wss.general
										.setInputMode(Jedox.wss.app.lastInputModeDlg);
								Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
							}
							Jedox.wss.macro.exprt( [ this, console.log ]);
							Jedox.wss.app
									.unload(Jedox.wss.app.dynJSRegistry.editMacro);
							showInsertDialog()
						},
						resize : function(window, width, height) {
							if (mainPanel.rendered) {
								mainPanel.items.items[1].setHeight(height - 73);
								var parentEl = descriptionTxa.getEl().parent();
								parentEl.dom.childNodes[1].childNodes[0].style.width = (width - 295)
										+ "px";
								parentEl.dom.childNodes[1].childNodes[0].style.height = (height - 75)
										+ "px";
								mainPanel.doLayout()
							} else {
								this.setSize(750, 490)
							}
						}
					}
				});
		win.show(this);
		setTimeout(function() {
			editor = CodeMirror.fromTextArea("macro-txa", {
				stylesheet : "../common/res/css/cm/phpcolors.css",
				path : "../common/base/cm/",
				parserfile : [ "tokenizephp.js", "parsephp.js" ],
				textWrapping : false,
				lineNumbers : true,
				content : document.getElementById("macro-txa").value
			});
			setTimeout(function() {
				initDlg(treeData)
			}, 200)
		});
		function initDlg(treeData) {
			if (obj) {
				if (obj.operation == "new") {
					setTimeout(function() {
						preselect(obj.macro)
					}, 200)
				} else {
					setTimeout(function() {
						preselect(obj.macro)
					}, 200)
				}
			} else {
				setTimeout(function() {
					preselect()
				}, 200)
			}
		}
		function loadMacroTreeData(treeData) {
			root.appendChild(treeData)
		}
		function preselect(macro) {
			if (macro) {
				var path = "/root/".concat(macro.split(".")[0], "/", macro);
				macrosTP.expandPath(path);
				setTimeout(function() {
					macrosTP.fireEvent("click", macrosTP.getNodeById(macro))
				})
			} else {
				macrosTP.fireEvent("click", macrosTP.getRootNode()
						.hasChildNodes() ? macrosTP.getRootNode().firstChild
						: null)
			}
		}
		function loadModule(module) {
			Jedox.wss.macro.load( [ this, loadTxtArea_inner_post ], module);
			function loadTxtArea_inner_post(src) {
				editor.setCode(src)
			}
		}
		function onModuleCtxMnu(node, e) {
			var that = this;
			var moduleCtxMnu = new Ext.menu.Menu( {
				id : "edit-macro-module-ctx-mnu",
				enableScrolling : false,
				items : [ {
					text : "Rename Module".localize(),
					handler : function() {
						onRenameModule(node)
					},
					scope : that
				}, "-", {
					text : "Delete Module".localize(),
					handler : function() {
						deleteModule(node)
					},
					scope : that
				} ]
			});
			moduleCtxMnu.on("hide", onContextHide, moduleCtxMnu);
			e.stopEvent();
			moduleCtxMnu.showAt(e.getXY())
		}
		function onContextHide(ctxMnu) {
			if (ctxMnu) {
				ctxMnu.destroy();
				ctxMnu = null
			}
		}
		function onRenameModule(node) {
			if (!node) {
				node = macrosTP.getSelectionModel().getSelectedNode()
			}
			if (!node.leaf) {
				moduleEditor.triggerEdit(node)
			}
		}
		function onAddModule() {
			var _that = this;
			var moduleName = getNewModuleName("Module".localize());
			Jedox.wss.macro
					.add( [ _that, refreshModule_inner_post, moduleName ],
							moduleName);
			function refreshModule_inner_post(result, moduleName) {
				var nodes = [];
				macrosTP.root.eachChild(function(node) {
					nodes.push(node)
				}, [ macrosTP ]);
				for ( var i = 0; i < nodes.length; i++) {
					nodes[i].remove()
				}
				Jedox.wss.macro.list( [ _that, reloadMacroTreeData_inner_post,
						moduleName ], Jedox.wss.macro.listFmt.TREE)
			}
			function reloadMacroTreeData_inner_post(treeData, moduleName) {
				root.appendChild(treeData);
				macrosTP.root.expand();
				var node = macrosTP.getNodeById(moduleName);
				macrosTP.fireEvent("click", macrosTP.getNodeById(moduleName));
				moduleEditor.triggerEdit(node)
			}
		}
		function getNewModuleName(tmplName) {
			var br = 1;
			var exist = true;
			var name = tmplName.toLowerCase();
			while (exist) {
				exist = false;
				if (br >= 1) {
					name = tmplName.toLowerCase() + br
				}
				macrosTP.root.eachChild(function(node) {
					if (node.attributes.text.toLowerCase() == name) {
						br = br + 1;
						exist = true;
						return false
					}
				}, [ this ])
			}
			return tmplName + br
		}
		function validateName() {
		}
		function checkNameAvailable(target, name) {
			for ( var i = 0; i < target.childNodes.length; i++) {
				if (name === target.childNodes[i].attributes.text) {
					return false
				}
			}
			return true
		}
		function validateName(node, oldName, newName) {
			var parent = node.parentNode;
			var _return = true;
			if (oldName != newName) {
				if (newName.length < 64) {
					parent.eachChild(function(node) {
						if (node.attributes.text.toLowerCase() == newName
								.toLowerCase()) {
							_return = false
						}
					}, [ this ])
				} else {
					var title = "Error Renaming Module".localize();
					var msg = "rename_module_error_msg".localize( {
						old_name : oldName,
						new_name : newName
					});
					var fn = function() {
						node.setText(oldName);
						editor.triggerEdit(node)
					};
					showMsgQUESTIONERROR(title, msg, fn);
					return false
				}
			}
			if (!_return) {
				var title = "Error Renaming Module".localize();
				var msg = "rename_module_error_msg".localize( {
					old_name : oldName,
					new_name : newName
				});
				var fn = function() {
					node.setText(oldName);
					moduleEditor.triggerEdit(node)
				};
				showMsgQUESTIONERROR(title, msg, fn)
			}
			return _return
		}
		function trim(s) {
			return s.replace(/^\s+|\s+$/g, "")
		}
		function onApply() {
			if (_module) {
				var moduleName = _module.id;
				var moduleID = _module.uid;
				var src = editor.getCode();
				Jedox.wss.macro.save( [ this, refreshModule_inner_post,
						moduleName ], moduleID, src)
			} else {
				showMsgERROR("Error".localize(),
						"edit_macro_no_module_selected_err".localize())
			}
			function refreshModule_inner_post(result, moduleName) {
				var nodes = [];
				macrosTP.root.eachChild(function(node) {
					nodes.push(node)
				}, [ macrosTP ]);
				for ( var i = 0; i < nodes.length; i++) {
					nodes[i].remove()
				}
				Jedox.wss.macro.list( [ this, reloadMacroTreeData_inner_post,
						moduleName ], Jedox.wss.macro.listFmt.TREE)
			}
			function reloadMacroTreeData_inner_post(treeData, moduleName) {
				root.appendChild(treeData);
				macrosTP.root.expand();
				var node = macrosTP.getNodeById(moduleName);
				if (node) {
					macrosTP.fireEvent("click", node)
				}
			}
		}
		function onOK() {
			if (_module) {
				var moduleName = _module.id;
				var moduleID = _module.uid;
				var src = editor.getCode();
				Jedox.wss.macro.save( [ this, closeDlg_inner_post ], moduleID,
						src)
			} else {
				showMsgERROR("Error".localize(),
						"edit_macro_no_module_selected_err".localize())
			}
			function closeDlg_inner_post(result, moduleName) {
				win.close()
			}
		}
		function onCut() {
			_clipboard = editor.selection();
			editor.replaceSelection("")
		}
		function onCopy() {
			_clipboard = editor.selection()
		}
		function onPaste() {
			if (_clipboard) {
				editor.insertIntoLine(editor.cursorPosition(true).line, editor
						.cursorPosition(true).character, _clipboard);
				_clipboard = ""
			}
		}
		function showMsgERROR(title, message) {
			Ext.Msg.show( {
				title : title,
				msg : message,
				buttons : Ext.Msg.OK,
				animEl : "elId",
				icon : Ext.MessageBox.ERROR
			})
		}
		function showMsgQUESTIONERROR(title, message, fn) {
			Ext.Msg.show( {
				title : title,
				msg : message,
				buttons : Ext.Msg.OK,
				fn : function(btn) {
					if (btn == "ok") {
						fn()
					}
				},
				animEl : "elId",
				closable : false,
				icon : Ext.MessageBox.ERROR
			})
		}
		function deleteModule() {
			if (_module) {
				Jedox.wss.macro.del( [ this, refreshModule_inner_post ],
						_module.uid)
			} else {
				showMsgERROR("Error".localize(),
						"edit_macro_no_module_selected_err".localize())
			}
			function refreshModule_inner_post(result) {
				var nodes = [];
				macrosTP.root.eachChild(function(node) {
					nodes.push(node)
				}, [ macrosTP ]);
				for ( var i = 0; i < nodes.length; i++) {
					nodes[i].remove()
				}
				Jedox.wss.macro.list( [ this, reloadMacroTreeData_inner_post ],
						Jedox.wss.macro.listFmt.TREE)
			}
			function reloadMacroTreeData_inner_post(treeData) {
				root.appendChild(treeData);
				macrosTP.root.expand();
				editor.setCode("");
				_module = null
			}
		}
		function showInsertDialog() {
			if (obj && callBackFnc) {
				callBackFnc()
			}
		}
		function selectMacroInText(node) {
			if (node.parentNode.attributes != _module) {
				Jedox.wss.macro.load( [ this, loadTxtArea_inner_post,
						node.attributes.text ], node.parentNode.attributes.uid);
				_module = node.parentNode.attributes
			} else {
				var pattern = "function ".concat(node.attributes.text);
				var o = editor.getSearchCursor(pattern, false);
				if (o.findNext()) {
					o.select()
				}
			}
			function loadTxtArea_inner_post(src, name) {
				editor.setCode(src);
				var pattern = "function ".concat(name);
				var o = editor.getSearchCursor(pattern, false);
				if (o.findNext()) {
					o.select()
				}
			}
		}
		function find(pattern) {
			var o = editor.getSearchCursor(pattern, true);
			if (o.findNext()) {
				o.select()
			}
		}
		function openFindDialog() {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.find, [
					editor.selection(), find ])
		}
	}
};