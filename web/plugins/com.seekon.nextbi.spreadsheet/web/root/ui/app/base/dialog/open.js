Jedox.wss.dialog.openOpenDialog=function(OSflag,cb){if(Jedox.wss.app.environment!=null){Jedox.wss.app.lastInputModeDlg=Jedox.wss.app.environment.inputMode;Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.DIALOG)}var title;var btn;var closeWinFnc=function(){win.close();if(OSflag==="saveqp"){Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.quickPublish)}};var browsePanel=new Jedox.wss.dialog.Browse(OSflag,closeWinFnc);var openBtn={text:"Open".localize(),handler:function(){browsePanel.onOpen()}};var saveBtn={text:"Save".localize(),handler:function(){browsePanel.onSave(cb)}};var cancelBtn={text:"Cancel".localize(),handler:function(){win.close()}};function initInterface(){if(OSflag==="open"){title="Open".localize();btn=openBtn}else{title="Save As".localize();btn=saveBtn}}initInterface();var win=new Ext.Window({id:"open-dlg",title:title,closable:true,closeAction:"close",autoDestroy:true,plain:true,constrain:true,cls:"default-format-window",modal:true,resizable:false,animCollapse:false,layout:"fit",width:600,height:400,items:browsePanel,listeners:{close:function(){Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);Jedox.wss.app.lastInputMode=Jedox.wss.grid.GridMode.READY;Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.open);Jedox.wss.backend.studio.removeFromSessionCurrGH("wss")}},buttons:[btn,cancelBtn]});win.show(this)};