Jedox.studio.etl.ETLPanel=Ext.extend(Ext.Panel,{_isLoadedEtl:false,_timer:null,title:"Integration Manager",id:"etl-panel",listeners:{render:function(self){self.body.createChild({tag:"div",id:"etl_content_panel",cls:"etl-projects-tab-panel"})},show:function(self){if(!self._isLoadedEtl){self.setTitle("ETL Manager".localize());var wssStudio=new Studio({getUserCreds:function(result){if(result[0]){self._startLoadingETL(result[1])}}});wssStudio.getUserCreds()}}},_startLoadingETL:function(cred){var that=this;var tmp_dialog=Ext.MessageBox.show({title:"ETL".localize(),msg:"".concat("ETL is loading, please wait".localize(),"..."),closable:false,width:300,icon:"largeLoadingImage",_check_key:"etl"});var onhide_fnc=function(){clearTimeout(that._timer);Ext.MessageBox.getDialog().removeListener("hide",onhide_fnc)};Ext.MessageBox.getDialog().addListener("hide",onhide_fnc);that._timer=setTimeout(function(){Ext.MessageBox.getDialog().removeListener("hide",onhide_fnc);Ext.MessageBox.hide();Ext.MessageBox.show({title:"Error".localize(),msg:"_err: Timer".localize(),buttons:Ext.Msg.OK,icon:Ext.MessageBox.ERROR})},60000);var jsonStr=Ext.util.JSON.encode(cred);var xhr=new XMLHttpRequest();xhr.open("POST","/tc/web-etl/app/service/studiologin",true);xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");xhr.setRequestHeader("Content-length",jsonStr.length);xhr.setRequestHeader("Connection","close");xhr.onreadystatechange=function(ev){if(xhr.readyState==4){if(xhr.status==200){var result=Ext.util.JSON.decode(xhr.responseText);if(result[0]){that._loadETLFiles();that._isLoadedEtl=true}else{clearTimeout(that._timer);Ext.MessageBox.getDialog().removeListener("hide",onhide_fnc);Ext.MessageBox.hide();Ext.MessageBox.show({title:"Login problem".localize(),msg:"_info: etl: bad username".localize(),buttons:Ext.Msg.OK,icon:Ext.MessageBox.INFO})}}else{clearTimeout(that._timer);Ext.MessageBox.getDialog().removeListener("hide",onhide_fnc);Ext.MessageBox.hide();Ext.MessageBox.show({title:"Login problem".localize(),msg:"ETL Login service is not available.".localize(),buttons:Ext.Msg.OK,icon:Ext.MessageBox.INFO})}}};xhr.send(jsonStr)},_loadETLFiles:function(){var that=this,listOfFiles=[["/tc/web-etl/app/base.css","css"],["/tc/web-etl/app/dialogs.css","css"],["/tc/web-etl/app/server-tree.css","css"],["/tc/web-etl/app/overview-tab.css","css"],["/tc/web-etl/app/project-editor.css","css"],["/tc/web-etl/app/contexts-editor.css","css"],["/tc/web-etl/app/main-frame.css","css"],["/tc/web-etl/app/jobs-editor.css","css"],["/tc/web-etl/app/job-editor.css","css"],["/tc/web-etl/app/section-editor.css","css"],["/tc/web-etl/app/server-editor.css","css"],["/tc/web-etl/app/connection-editor.css","css"],["/tc/web-etl/app/transform-editor.css","css"],["/tc/web-etl/app/extract-editor.css","css"],["/tc/web-etl/app/load-editor.css","css"],["/tc/web-etl/app/tensegrity-tables.css","css"],["/tc/web-etl/app/dnd.css","css"],["/tc/web-etl/app/img/logo.css","css"],["/tc/web-etl/app/integrated.css","css"],["/tc/web-etl/app/gxt/css/gxt_ext_diff.css","css"],["/tc/web-etl/app/advanced/themes/default/theme.css","css"],["/tc/web-etl/app/app.nocache-studio.js","js"]];for(var i=0;i<listOfFiles.length;i++){that._loadFile(listOfFiles[i][0],listOfFiles[i][1])}},_loadFile:function(url,type){var newFile,docBody=document.getElementsByTagName("body")[0];if(type=="css"){newFile=document.createElement("link");newFile.type="text/css";newFile.href=url;newFile.rel="stylesheet";newFile.id=url}else{if(type=="js"){newFile=document.createElement("script");newFile.type="text/javascript";newFile.language="javascript";newFile.src=url}}newFile.onerror=function(){docBody.removeChild(newFile);console.log("Loading error: ".concat(url))};docBody.appendChild(newFile)}});