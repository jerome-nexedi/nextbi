Jedox.wss.palo.PasteDataFunctions=function(pv){var phpPaloServerCbHandlers={getServList:function(result){if(result[0]){storeServDb.loadData(result[1]);if(result[2]){preselectedServDb=result[2]}_refillcmbDb()}else{_showErrorMsg(result[1])}},getDBs:function(result){if(result[0]){var tmpIndex=storeServDb.find("id",result[1][0]);var tmpRec;for(var j=(result[1].length-1);j>0;j--){tmpRec=new ServDbRecord({id:"id-"+result[1][0]+"-"+j,parent_id:result[1][0],type:"database",name:result[1][j]});storeServDb.insert(tmpIndex+1,tmpRec)}}else{_showErrorMsg(result[1],result[2])}numOfRequests-=(numOfRequests==0)?0:1;if(preselectedServDb&&(numOfRequests==0)&&(cmbDbState==-1)){cmbDbState=storeServDb.findBy(function(rec,id){return(rec.get("name")==preselectedServDb[1]&&rec.get("parent_id")==preselectedServDb[0]&&rec.get("type")=="database")})}if((numOfRequests==0)&&(cmbDbState==-1)){cmbDbState=storeServDb.find("type","database")}if(cmbDbState!=-1){tmpIndex=cmbDbState;cmbDbState=-1;cmbDb.setValue(storeServDb.getAt(tmpIndex).get("id"));_cmbDbSelectionHandler(cmbDb,storeServDb.getAt(tmpIndex),tmpIndex)}},getCubeNames:function(result){if(!result[2]){storeCubes.removeAll();var tmpRec;for(var i=0;i<result[1].length;i++){tmpRec=new CubeRecord({name:result[1][i]});storeCubes.add(tmpRec)}if(result[1].length>0){listCubes.select(0)}}else{_showErrorMsg(result[3])}}};var _config=Jedox.wss.palo.config;var that=this;var phpPaloServer=new Palo(phpPaloServerCbHandlers);var servId,dbName,preselectedServDb=null;var win,listCubes;var panelMain,panelbtnPaste,panelbtnClose,panelcmbDb,panelcmbPaloFuncs,panelchbAttrCubes,panelchbGuessArgs;var lblcmbDb,cmbDb,cmbPaloFuncs,chbAttrCubes,chbGuessArgs,btnPaste,btnClose;var selectedElem,cmbPaloFuncsState,cmbDbState,dataMode,numOfRequests;var ServDbRecord=new Ext.data.Record.create([{name:"id"},{name:"parent_id"},{name:"connected"},{name:"type"},{name:"name"},{name:"host"},{name:"port"},{name:"username"},{name:"password"}]);var storeServDb=new Ext.data.SimpleStore({fields:[{name:"id"},{name:"parent_id"},{name:"connected"},{name:"type"},{name:"name"},{name:"host"},{name:"port"},{name:"username"},{name:"password"}]});var CubeRecord=new Ext.data.Record.create([{name:"name"}]);var storeCubes=new Ext.data.SimpleStore({fields:[{name:"name"}]});var storePaloFuncs=new Ext.data.SimpleStore({fields:[{name:"name"}]});this.init=function(){selectedElem=null;cmbPaloFuncsState=0;cmbDbState=-1;dataMode=0;numOfRequests=0;var paloFuncList=[["PALO.DATA"],["PALO.DATAC"],["PALO.DATAV"]];storePaloFuncs.loadData(paloFuncList);phpPaloServer.getServList();lbllistCubes=new Ext.form.MiscField({value:"Choose Cube".localize()+":",height:22,bodyStyle:"background-color: transparent;",hideLabel:true});listCubes=new Ext.DataView({store:storeCubes,itemSelector:"div.row-modeller",style:"overflow:auto",singleSelect:true,cls:"modellerDataViewSelect",tpl:new Ext.XTemplate('<tpl for=".">','<div class="row-modeller">',"<span>&#160;{name}</span>","</div>","</tpl>")});panellistCubes=new Ext.Panel({layout:"fit",items:[listCubes]});btnPaste=new Ext.Button({text:"Paste".localize(),ctCls:"subsetEditorBtns",listeners:{click:function(){if(listCubes.getSelectionCount()>0){var cube=listCubes.getSelectedRecords()[0].get("name");if(Jedox.wss.app.activeBook){var env=Jedox.wss.app.environment;var activeBook=Jedox.wss.app.activeBook;var upperLeftCoords=env.defaultSelection.getActiveRange().getUpperLeft();var lowerRighCoords=env.defaultSelection.getActiveRange().getLowerRight();var settings=[upperLeftCoords.getX(),upperLeftCoords.getY(),lowerRighCoords.getX(),lowerRighCoords.getY(),servId,dbName,chbGuessArgs.getValue()];activeBook.cb("palo_handlerPasteDataFunctions",[[settings,cube]])}else{var settings=[5,5,10,10,servId,dbName,chbGuessArgs.getValue()];phpPaloServer.handlerPasteDataFunctions([settings,cube])}}win.close()}}});panelbtnPaste=new Ext.Panel({border:false,bodyStyle:"background-color: transparent;",autoWidth:true,autoHeight:true,items:[btnPaste]});btnClose=new Ext.Button({text:"Close".localize(),ctCls:"subsetEditorBtns",listeners:{click:function(){win.close()}}});panelbtnClose=new Ext.Panel({border:false,bodyStyle:"background-color: transparent;",autoWidth:true,autoHeight:true,items:[btnClose]});lblcmbDb=new Ext.form.MiscField({value:"Choose Server/Database".localize()+":",height:22,bodyStyle:"background-color: transparent;",hideLabel:true});cmbDb=new Ext.form.ComboBox({store:storeServDb,bodyStyle:"background-color: transparent;",typeAhead:true,selectOnFocus:true,hideLabel:true,editable:false,forceSelection:true,triggerAction:"all",mode:"local",tpl:new Ext.XTemplate('<tpl for=".">','<div class="x-combo-list-item">',"<tpl if=\"type == 'database'\">",'<span style="cursor: default;">&#160;&#160;&#160;{name}</span>',"</tpl>","<tpl if=\"type == 'server'\">",'<span style="cursor: default;">{name}</span>',"</tpl>","</div>","</tpl>"),listeners:{select:_cmbDbSelectionHandler},valueField:"id",displayField:"name"});panelcmbDb=new Ext.Panel({border:false,bodyStyle:"background-color: transparent;",layout:"form",autoWidth:true,autoHeight:true,items:[cmbDb]});cmbPaloFuncs=new Ext.form.ComboBox({disabled:true,store:storePaloFuncs,bodyStyle:"background-color: transparent;",typeAhead:true,selectOnFocus:true,hideLabel:true,editable:false,forceSelection:true,triggerAction:"all",mode:"local",valueField:"name",displayField:"name"});panelcmbPaloFuncs=new Ext.Panel({border:false,bodyStyle:"background-color: transparent;",layout:"form",autoWidth:true,autoHeight:true,items:[cmbPaloFuncs]});cmbPaloFuncs.setValue(storePaloFuncs.getAt(cmbPaloFuncsState).get("name"));chbAttrCubes=new Ext.form.Checkbox({hideLabel:true,boxLabel:"Attribute Cubes".localize(),listeners:{check:function(){var tmpType=storeServDb.getAt(cmbDbState).get("type");if(chbAttrCubes.getValue()){dataMode=2;if(tmpType=="database"){phpPaloServer.getCubeNames(servId,dbName,dataMode)}}else{dataMode=0;if(tmpType=="database"){phpPaloServer.getCubeNames(servId,dbName,dataMode)}}}}});panelchbAttrCubes=new Ext.Panel({border:false,bodyStyle:"background-color: transparent;",layout:"form",autoWidth:true,autoHeight:true,items:[chbAttrCubes]});chbGuessArgs=new Ext.form.Checkbox({hideLabel:true,boxLabel:"Guess Arguments".localize()});panelchbGuessArgs=new Ext.Panel({border:false,bodyStyle:"background-color: transparent;",layout:"form",autoWidth:true,autoHeight:true,items:[chbGuessArgs]});panelMain=new Ext.Panel({id:"pdf_mainPanel",border:false,bodyStyle:"background-color: transparent;",layout:"absolute",anchor:"100% 100%",monitorResize:true,listeners:{resize:_resizeAll},items:[panellistCubes,lbllistCubes,panelbtnPaste,panelbtnClose,panelcmbDb,lblcmbDb,panelcmbPaloFuncs,panelchbAttrCubes,panelchbGuessArgs]});win=new Ext.Window({id:"pastedatafunctionsWizardWindow",layout:"fit",cls:"default-format-window",title:"Paste Data Functions".localize(),width:_config.pdfWinW,height:_config.pdfWinH,minWidth:_config.pdfWinW,minHeight:_config.pdfWinH,closeAction:"close",autoDestroy:true,plain:true,modal:true,resizable:true,listeners:{activate:_resizeAll,close:function(){Jedox.wss.app.unload(Jedox.wss.app.dynJSRegistry.pasteDataFunctions);phpPaloServer.setPreselectServDb(servId,dbName)}},items:[panelMain]})};this.show=function(){if(win){win.show()}};var _cmbDbSelectionHandler=function(combo,record,index){if(cmbDbState!=index){cmbDbState=index;if(record.get("type")=="database"){servId=record.get("parent_id");dbName=record.get("name");phpPaloServer.getCubeNames(servId,dbName,dataMode)}else{storeCubes.removeAll()}}};var _refillcmbDb=function(){var executed=false;for(var i=0;i<storeServDb.getCount();i++){if(storeServDb.getAt(i).get("connected")==1){executed=true;phpPaloServer.getDBs(storeServDb.getAt(i).get("id"));numOfRequests++}}if(!executed&&(cmbDbState==-1)){var tmpIndex=0;cmbDbState=-1;cmbDb.setValue(storeServDb.getAt(tmpIndex).get("id"));_cmbDbSelectionHandler(cmbDb,storeServDb.getAt(tmpIndex),tmpIndex)}};var _showErrorMsg=function(message,props){Ext.MessageBox.show({title:"Error".localize(),msg:message.localize(props),buttons:Ext.Msg.OK,icon:Ext.MessageBox.ERROR})};var _resizeAll=function(){if(panelMain.rendered){var lineH=23;var marginSize=3;var w=panelMain.getSize().width;var h=panelMain.getSize().height;var listW=w-2*marginSize;var listH=h-7*marginSize-5*lineH;panellistCubes.setSize(listW,listH);panellistCubes.setPosition(marginSize,3*marginSize+3*lineH);cmbDb.setWidth(listW);panelcmbDb.setPosition(marginSize,marginSize+lineH);lblcmbDb.setPosition(marginSize,3*marginSize);lbllistCubes.setPosition(marginSize,5*marginSize+2*lineH);panelcmbPaloFuncs.setPosition(marginSize,5*marginSize+3*lineH+listH);if(chbAttrCubes.rendered&&chbGuessArgs.rendered){var widthShow=Ext.util.TextMetrics.measure(chbAttrCubes.id,"Attribute Cubes".localize());panelchbAttrCubes.setPosition(w-widthShow.width-35,4*marginSize+2*lineH);var widthShow2=Ext.util.TextMetrics.measure(chbGuessArgs.id,"Guess Arguments".localize());panelchbGuessArgs.setPosition(w-widthShow2.width-35,5*marginSize+3*lineH+listH+2)}if(panelbtnClose.rendered&&panelbtnPaste.rendered&&btnClose.rendered&&btnPaste.rendered){panelbtnClose.setPosition(w-btnClose.getEl().getBox().width-marginSize,h-lineH-marginSize);panelbtnPaste.setPosition(w-btnClose.getEl().getBox().width-btnPaste.getEl().getBox().width-2*marginSize,h-lineH-marginSize)}}};this.init();this.show()};