Jedox.wss.app.updateUndoState=function(undoState,updState){};Jedox.wss.app.hideBar=function(barId){var bar=Ext.get(barId);bar.setVisibilityMode(Ext.Element.DISPLAY);bar.hide();Jedox.wss.workspace.resize();Jedox.wss.workspace.resizeMaxWindows()};Jedox.wss.app.showBar=function(barId){var bar=Ext.get(barId);bar.setVisibilityMode(Ext.Element.DISPLAY);bar.show();Jedox.wss.workspace.resize();Jedox.wss.workspace.resizeMaxWindows()};Jedox.wss.app.hideToolbar=function(toolbar){Jedox.wss.app.hideBar("Toolbar")};Jedox.wss.app.showToolbar=function(toolbar){Jedox.wss.app.showBar("Toolbar")};Jedox.wss.app.hideShowToolbar=function(state,toolbar){if(state){Jedox.wss.app.showToolbar(toolbar)}else{Jedox.wss.app.hideToolbar(toolbar)}};Jedox.wss.app.initMenuBar=function(){var menu=Jedox.wss.app.menubar={};Jedox.wss.app.Menu=new Ext.Toolbar({cls:"extmenubar"});var fileMenuItems=[{text:"Export".localize(),menu:{items:[{text:"PDF".localize(),iconCls:"icon_file_pdf",handler:Jedox.wss.action.exportToPDF,disabled:!Jedox.wss.app.fopper},{text:"HTML".localize(),iconCls:"icon_file_html",handler:Jedox.wss.action.exportToHTML}]}},"-",{text:"Page Setup".localize().concat("..."),disabled:true},{text:"Print Preview".localize(),handler:Jedox.wss.action.exportToHTML}],enaRestr=!Jedox.wss.app.UPRestrictMode,theme=Jedox.wss.app.theme;if(enaRestr){fileMenuItems.splice(1,0,{text:"Close".localize(),disabled:true});fileMenuItems.unshift({text:"Open".localize().concat("..."),iconCls:"icon_open_doc",handler:function(){Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.open,["open"])}})}Jedox.wss.app.Menu.add({text:"File".localize(),cls:"extmenubaritem",menu:(menu.fileMenu=new Ext.menu.Menu({id:"FileMenu",items:fileMenuItems}))},{text:"Edit".localize(),cls:"extmenubaritem",menu:new Ext.menu.Menu({id:"EditMenu",items:[{text:"Can't Undo".localize(),disabled:true},{text:"Can't Repeat".localize(),disabled:true},"-",{text:"Cut".localize(),iconCls:"icon_cut",handler:function(){Jedox.wss.action.cut(false)}},{text:"Copy".localize(),iconCls:"icon_copy",handler:function(){Jedox.wss.action.copy(false)}},{text:"Paste".localize(),iconCls:"icon_paste",handler:Jedox.wss.action.paste}]})},{text:"View".localize(),cls:"extmenubaritem",menu:new Ext.menu.Menu({id:"ViewMenu",items:[{text:"Toolbars".localize(),enableToggle:true,checked:true,checkHandler:function(btn,state){Jedox.wss.app.hideShowToolbar(state,"")}},{text:"Status Bar".localize(),enableToggle:true,checked:true,checkHandler:function(btn,state){Jedox.wss.app.statusBar.hideShow(state)}},"-",{text:"Full Screen".localize(),disabled:true}]})},{text:"Data".localize(),cls:"extmenubaritem",menu:new Ext.menu.Menu({id:"DataMenu",items:[{text:"Refresh Data".localize(),handler:function(){Jedox.wss.app.activeSheet.recalc()}},{text:"Auto-Refresh Data".localize(),id:"autoRefreshDataMenu",enableToggle:true,checked:false,checkHandler:function(){this.checked?Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.autoRefreshStart):Jedox.wss.book.autoRefresh(0)}}],listeners:{beforeshow:function(){this.items.get("autoRefreshDataMenu").setChecked(Jedox.wss.app.activeBook._autoRefresh>0,true)}}})});if(enaRestr){Jedox.wss.app.Menu.add({text:"Window".localize(),cls:"extmenubaritem",menu:(menu.windowMenu=new Ext.menu.Menu({id:"WindowMenu",items:[{text:"Arrange".localize().concat("..."),handler:function(){Jedox.wss.dialog.openArrangeWindows()}},{text:"Hide".localize(),handler:function(){Jedox.wss.workspace.hideActiveWin()}},{text:"Unhide".localize().concat("..."),handler:function(){Jedox.wss.dialog.openUnhideDialog()}},"-"]}))})}Jedox.wss.app.Menu.add({text:"Help".localize(),cls:"extmenubaritem",menu:new Ext.menu.Menu({id:"HelpMenu",items:[{text:"Worksheet-Server Help".localize(),disabled:true},"-",{text:"Jedox Online".localize(),href:"http://www.jedox.com"}]})});Jedox.wss.app.Menu.render("MenuPlaceholder")};Jedox.wss.app.initActiveToolbars=function(){Jedox.wss.app.initStandardToolbar()};Jedox.wss.app.initStandardToolbar=function(){var stdTbar=Jedox.wss.app.standardToolbar=new Ext.Toolbar({cls:"wssUserMenubar",renderTo:"wssStandardToolbar",items:[{iconCls:"iconrecalcnow",text:"Refresh".localize(),tooltip:"Refresh / Recalculate".localize(),handler:function(){Jedox.wss.app.activeSheet.recalc()}},"-",{iconCls:"icon_copy",cls:"x-btn-icon",tooltip:"Copy".localize(),handler:function(){Jedox.wss.action.copy(false)}},{iconCls:"icon_cut",cls:"x-btn-icon",tooltip:"Cut".localize(),handler:function(){Jedox.wss.action.cut(false)}},{iconCls:"icon_paste",cls:"x-btn-icon",tooltip:"Paste".localize(),handler:Jedox.wss.action.paste},"-",{iconCls:"iconprintpreview",text:"Print Preview".localize(),tooltip:"Print Preview".localize(),handler:Jedox.wss.action.exportToHTML},{iconCls:"iconprinttopdf",text:"PDF".localize(),tooltip:"Print To PDF".localize(),handler:Jedox.wss.action.exportToPDF,disabled:!Jedox.wss.app.fopper}]});if(window.opener!=null){stdTbar.add("-",{iconCls:"iconclose",text:"Close".localize(),tooltip:"Close".localize(),handler:Jedox.wss.action.closeWindow})}stdTbar.doLayout()};