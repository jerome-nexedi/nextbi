Jedox.wss.dialog.Browse=function(OSHflag,closeWinFnc,preselectMode){var that=this;var title;var btn;var typeCmb;var hyperlink={removeTlbBtn:false,newFolderTlbBtn:false,fileTypesCmb:false};that.closeFnc=closeWinFnc;that.activeHierarchy={};that._isHyperlink=false;that._preselectMode=preselectMode?true:false;that._selectAfterDump_cb=false;that._nodeClick_cb=false;that.gStore=new Ext.data.SimpleStore({fields:["id","name"]});that.fStore=new Ext.data.SimpleStore({fields:[{name:"id",type:"string"},{name:"text",type:"string"},{name:"leaf"},{name:"qtip",type:"string"},{name:"iconCls",type:"string"},{name:"img_src",type:"string"}],sortInfo:{field:"iconCls",direction:"ASC"}});var folderTypeData=[["My Workbook Documents".localize(),"my_workbook_documents"]];var folderTypeNavigationStore=new Ext.data.SimpleStore({fields:["label","icon"],data:folderTypeData});var fileTypes=[["0","Work Sheet Files".localize()],["1","All Files".localize()]];var fileTypesStore=new Ext.data.SimpleStore({fields:["code","option"],data:fileTypes});var saveTypes=[["0","Work Sheet Files (*.wss)".localize()]];var saveTypesStore=new Ext.data.SimpleStore({fields:["code","option"],data:saveTypes});var fileNameData=[[""]];var fileNameStore=new Ext.data.SimpleStore({fields:["fileName"],data:fileNameData});var pathTxf=new Ext.form.TextField({fieldLabel:"Path".localize(),labelStyle:"font-size:11px;",cls:"preview-properties-panel",allowBlank:false,width:200});that.gCmb=new Ext.form.ComboBox({store:that.gStore,displayField:"name",valueField:"id",fieldLabel:"Look in".localize(),readOnly:false,editable:false,lazyRender:true,typeAhead:true,mode:"local",triggerAction:"all",selectOnFocus:true,width:330,listeners:{select:function(cmb,record,index){if(that.activeGroup!=record.data){var wssStudioHandler={treeSetGroup:function(result){if(!result){that.showMessageERROR("Database read error".localize(),"read_data_err".localize());return false}that.activeGroup=record.data;that.hierarchyTP.root.setText(record.data.name);that.initHierarchies(that.activeGroup.id)}};var wssStudioStub=new Studio(wssStudioHandler);wssStudioStub.treeSetGroup("wss",record.data.id)}}}});var root=new Ext.tree.TreeNode({text:"root",draggable:false,editable:false,id:"root",iconCls:"w3s_group",expanded:true});that.hierarchyTP=new Ext.tree.TreePanel({border:false,lines:true,heigth:100,containerScroll:true,ddScroll:true,autoScroll:true,collapseFirst:false,root:root,collapseFirst:true,listeners:{click:function(node,e){if(!that._nodeClick_cb){var wssStudioHandler={treeSetHierarchy:function(result){if(!result){that.showMessageERROR("Database read error".localize(),"read_data_err".localize());return false}that.hierarchyTP.fireEvent("click",node)}};if(node.id!="root"){if(node.attributes.type){if(that.activeHierarchy.id!=node.attributes.id){var wssStudioStub=new Studio(wssStudioHandler);wssStudioStub.treeSetHierarchy("wss",node.attributes.id);_nodeClick_cb=true;that.activeHierarchy.id=node.attributes.id;return false}}else{if(that.activeHierarchy.id!=node.attributes.h_uid){var wssStudioStub=new Studio(wssStudioHandler);wssStudioStub.treeSetHierarchy("wss",node.attributes.h_uid);_nodeClick_cb=true;that.activeHierarchy.id=node.attributes.h_uid;return false}}}}else{that._nodeClick_cb=false}node.expand()},select:function(id){var node=this.getNodeById(id);this.fireEvent("click",node)},up:function(sn){if(sn){var parentNode=sn.parentNode;if(parentNode){this.fireEvent("click",parentNode)}}}}});that.hierarchyTP.on("click",function(n){var sn=that.hierarchyTP.getSelectionModel().getSelectedNode()||{};if(n.id!=sn.id&&n.id!="root"){if(n.attributes.type){initFilesData(n.attributes.type)}else{initFilesData(n.attributes.n_uid)}}else{if(n.id=="root"){clear()}}});var mainListView=new Ext.DataView({id:"main-list-view",store:that.fStore,tpl:new Ext.XTemplate('<div class="main-list-view"><tpl for=".">','<div class="thumb-wrap">','<div class="thumb" style="padding: 0px; text-align: left;">','<div style="width: 16px; height: 16px; display: inline;">','<img class="{img_src}" src="../lib/ext/resources/images/default/s.gif" width="16" height="16"></div>','<span class="x-editable">&nbsp;{text}</span></div></div>',"</tpl></div>"),autoWidth:true,multiSelect:false,autoScroll:true,singleSelect:true,overClass:"x-view-over",itemSelector:"div.thumb-wrap",emptyText:"",plugins:[new Ext.DataView.DragSelector()],listeners:{dblclick:function(dataView,index,node,e){onDblClick(dataView.store.getAt(index).get("id"),dataView.store.getAt(index).get("iconCls"))},click:function(dataView,index,node,e){var value="";if(that.fStore.getAt(index).get("iconCls")!="w3s_folder"){value=that.fStore.getAt(index).get("text");that.selectionId=that.fStore.getAt(index).get("id");if(!isClicked(value)){fileNameData.unshift(new Array());fileNameData[0].push(value);fileNameStore.loadData(fileNameData)}}that.fileNameCmb.setValue(value)}}});var mainDetailsView=new Ext.grid.GridPanel({id:"main-details-view",colModel:new Ext.grid.ColumnModel([{id:"folder-name",header:"Name",width:30,sortable:true,dataIndex:"text",renderer:formatTitle,editor:new Ext.form.TextField({allowBlank:false})},{header:"Size",width:20,sortable:true,dataIndex:""},{header:"Type",width:20,sortable:true,dataIndex:""},{header:"Last Modified",width:20,sortable:true,dataIndex:""}]),store:that.fStore,viewConfig:{forceFit:true},sm:new Ext.grid.RowSelectionModel({singleSelect:true}),border:false,iconCls:"icon-grid",listeners:{rowdblclick:function(gridView,index,e){onDblClick(gridView.store.getAt(index).get("id"),gridView.store.getAt(index).get("iconCls"))},rowclick:function(gridView,index,e){var value="";if(that.fStore.getAt(index).get("iconCls")!="w3s_folder"){value=that.fStore.getAt(index).get("text");that.selectionId=that.fStore.getAt(index).get("id");if(!isClicked(value)){fileNameData.unshift(new Array());fileNameData[0].push(value);fileNameStore.loadData(fileNameData)}}that.fileNameCmb.setValue(value)}}});var mainThumbnailsView=new Ext.DataView({id:"main-thumbnails-view",store:that.fStore,tpl:new Ext.XTemplate('<tpl for=".">','<div class="thumb-wrap" id="{text}">','<div class="thumb"><div class="{img_src}32"><img src="../lib/ext/resources/images/default/s.gif" title="{text}"></div></div>','<span class="x-editable">{shortName}</span></div>',"</tpl>",'<div class="x-clear"></div>'),autoWidth:true,multiSelect:false,singleSelect:true,overClass:"x-view-over",itemSelector:"div.thumb-wrap",emptyText:"",plugins:[new Ext.DataView.DragSelector()],prepareData:function(data){data.shortName=Ext.util.Format.ellipsis(data.text,11);return data},listeners:{dblclick:function(dataView,index,node,e){onDblClick(dataView.store.getAt(index).get("id"),dataView.store.getAt(index).get("iconCls"))},click:function(dataView,index,node,e){var value="";if(that.fStore.getAt(index).get("iconCls")!="w3s_folder"){value=that.fStore.getAt(index).get("text");that.selectionId=that.fStore.getAt(index).get("id");if(!isClicked(value)){fileNameData.unshift(new Array());fileNameData[0].push(value);fileNameStore.loadData(fileNameData)}}that.fileNameCmb.setValue(value)}}});that.fileNameCmb=new Ext.form.ComboBox({store:fileNameStore,displayField:"fileName",fieldLabel:"File name".localize(),typeAhead:true,mode:"local",triggerAction:"all",width:400});var fileTypesCmb=new Ext.form.ComboBox({fieldLabel:"Files of type".localize(),width:400,store:fileTypesStore,displayField:"option",valueField:"code",typeAhead:true,editable:false,mode:"local",triggerAction:"all",selectOnFocus:true,listeners:{select:function(cmb,record,index){if(cmb.getValue()==0){that.fStore.filterBy(filterFunc)}else{that.fStore.clearFilter(false)}}}});var saveTypesCmb=new Ext.form.ComboBox({fieldLabel:"Save as type".localize(),width:400,store:saveTypesStore,displayField:"option",valueField:"code",typeAhead:true,editable:false,mode:"local",triggerAction:"all",selectOnFocus:true});function isClicked(value){var flag=false;for(var i=0;i<fileNameData.length;i++){if(value===fileNameData[i][0]){flag=true;break}}return flag}function onContainerClick(dView,e){e.stopEvent();return false}function setView(m,pressed){if(!m){var viewMenu=Ext.menu.MenuMgr.get("view-menu");viewMenu.render();var items=viewMenu.items.items;var b=items[0],r=items[1],h=items[2];if(b.checked){r.setChecked(true)}else{if(r.checked){h.setChecked(true)}else{if(h.checked){b.setChecked(true)}}}return}if(pressed){var mvp=Ext.getCmp("main-view-panel");switch(m.text){case"List".localize():mvp.layout.setActiveItem("main-list-view");mvp.ownerCt.doLayout();break;case"Thumbnails".localize():mvp.layout.setActiveItem("main-thumbnails-view");mvp.ownerCt.doLayout();break;case"Details".localize():mvp.layout.setActiveItem("main-details-view");mvp.ownerCt.doLayout();break}}}function initInterface(){if(OSHflag==="open"){title="Open".localize();typeCmb=fileTypesCmb}else{if(OSHflag==="hyperlink"){typeCmb={html:"",baseCls:"x-plain"};hyperlink.removeTlbBtn=hyperlink.newFolderTlbBtn=true;that._isHyperlink=true}else{title="Save As".localize();typeCmb=saveTypesCmb}}}initInterface();function init(){function initGroups_cb(result){if(result){function selectGroup_cb(result){if(result){that.initHierarchies()}}that.selectGroup(getDefaultGroupIndex(),selectGroup_cb)}}that.initGroups(initGroups_cb)}function getDefaultGroupIndex(){var index=that.gStore.find("id",Jedox.wss.app.defaultFiles.group);return index!=-1?index:0}function initFilesData(node){var wssStudioHandler={treeMngNode:function(result){if(!result){that.showMessageERROR("Database read error".localize(),"read_data_err".localize());return false}var fData=[];for(var i=0;i<result.length;i++){fData.push([result[i].id,result[i].text,result[i].leaf,result[i].qtip,result[i].iconCls,img_src(result[i].iconCls)])}that.fStore.loadData(fData);filterStore();clearFileNames();if(that._selectAfterDump_cb){that._selectAfterDump_cb();_selectAfterDump_cb=false}}};var wssStudioStub=new Studio(wssStudioHandler);wssStudioStub.treeMngNode("wss",node,"dump",0)}function filterStore(){if(fileTypesCmb.getValue()==0){that.fStore.filterBy(filterFunc)}}function clearFileNames(){if(OSHflag==="save"||OSHflag==="saveqp"){return false}fileNameData=[[""]];fileNameStore.removeAll();that.fileNameCmb.setValue("")}function img_src(iconCls){return iconCls}function onDblClick(id,iconCls){if(iconCls==="w3s_folder"){propagateSelection(id)}else{if(!that._isHyperlink){that.openWB(id)}}}function propagateSelection(n_uid){var id=that.hierarchyTP.getNodeById(n_uid)?n_uid:that.activeHierarchy.id+"_"+n_uid;that.hierarchyTP.fireEvent("select",id)}function formatTitle(value,p,record){return String.format('<div class="topic"><img src="{0}"/>&nbsp;&nbsp;{1}</div>',record.data.img_src,value)}function filterFunc(record,id){return record.get("iconCls")==="w3s_workbook"||record.get("iconCls")==="w3s_folder"?true:false}function clearFilter(record,id){return record.get("iconCls")!=="w3s_workbook"?true:false}function onUp(){var sn=that.hierarchyTP.selModel.getSelectedNode();that.hierarchyTP.fireEvent("up",sn)}function clear(){that.fStore.removeAll();filterStore();clearFileNames()}function addFolder(){}function remove(){var parentId=that.getParentId();Jedox.wss.backend.studio.treeMngNode("file",parentId,"removeNode",that.selectionId);initFilesData(parentId)}Jedox.wss.dialog.Browse.superclass.constructor.call(this,{id:"open-save-dlg-browse-panel",layout:"absolute",baseCls:"x-plain",border:false,items:[{layout:"column",border:false,baseCls:"x-plain",width:600,height:40,x:0,y:0,items:[{layout:"form",border:false,baseCls:"top-left-bottom-panel",width:460,items:that.gCmb},{border:false,baseCls:"toolbar-panel",width:130,height:30,tbar:[{tooltip:{title:"Up".localize(),text:"Go Up one level".localize()},iconCls:"folder-up-icon",handler:onUp,scope:this},"-",{tooltip:{title:"Delete".localize(),text:"Delete".localize().concat("...")},iconCls:"delete-icon",handler:remove,hidden:hyperlink.removeTlbBtn,scope:this},{tooltip:{title:"New Folder".localize(),text:"Adds New folder to the list".localize()},iconCls:"new-folder-icon",hidden:hyperlink.newFolderTlbBtn,scope:this,disabled:true},{split:true,tooltip:{title:"View".localize(),text:"View".localize().concat("...")},iconCls:"view-menu-icon",handler:setView.createDelegate(this,[]),menu:{id:"view-menu",cls:"view-menu",width:110,items:[{text:"List".localize(),checked:true,group:"rp-view",checkHandler:setView,scope:this,iconCls:"view-list-icon"},{text:"Thumbnails".localize(),checked:false,group:"rp-view",checkHandler:setView,scope:this,iconCls:"view-thumbnails-icon"},{text:"Details".localize(),checked:false,group:"rp-view",checkHandler:setView,scope:this,iconCls:"view-details-icon"}]}}]}]},{baseCls:"left-panel",layout:"fit",border:false,width:200,height:220,x:5,y:40,items:that.hierarchyTP},{id:"main-view-panel",layout:"card",autoScroll:true,defaults:{bodyStyle:"padding:0px"},width:370,height:220,x:210,y:40,items:[mainListView,mainThumbnailsView,mainDetailsView],activeItem:0},{layout:"form",border:false,baseCls:"top-left-bottom-panel",width:600,height:100,x:0,y:260,items:[that.fileNameCmb,typeCmb]}]});if(!that._preselectMode){init()}fileTypesCmb.setValue(that._isHyperlink?"1":"0");saveTypesCmb.setValue("0")};Ext.extend(Jedox.wss.dialog.Browse,Ext.Panel,{initComponent:function(){var that=this;Jedox.wss.dialog.Browse.superclass.initComponent.call(this);this.onOpen=function(){var name=that.fileNameCmb.getValue();var id=that.getIdByName(name);that.openWB(id)};this.onSave=function(cb){var parentId=that.getParentId();var group=that.gCmb.getValue();var hierarchy=that.activeHierarchy.id;if(!parentId){return false}var fileName=that.trim(that.fileNameCmb.getValue());var s=fileName.toLowerCase().indexOf(".wss");if(s>0&&s===fileName.length-4){fileName=that.trim(fileName.slice(0,length-4))}if(that.validateName(fileName)){that.closeFnc();var oldMeta=Jedox.wss.workspace.getMetaByWinId(Jedox.wss.app.activeBook.getWinId());try{for(var triggers=Jedox.wss.events.triggers.saveAsWorkbook_before,i=triggers.length-1,wbMeta=oldMeta;i>=0;i--){triggers[i][0]["saveAsWorkbook_before"].call(parent,triggers[i][1],wbMeta.ghn,wbMeta.name)}}catch(e){Jedox.wss.general.showMsg("Application Error".localize(),e.message.localize(),Ext.MessageBox.ERROR);return}var saveRes=Jedox.wss.backend.ha.saveWorkbook(group,hierarchy,fileName,parentId);if(!saveRes[0]){Ext.MessageBox.show({title:"Save As".localize(),msg:saveRes[1].localize(saveRes[2]),modal:true,buttons:Ext.MessageBox.OK,icon:Ext.MessageBox.WARNING,fn:Ext.emptyFn,minWidth:300})}else{var activeBook=Jedox.wss.app.activeBook,newWbMeta={g:saveRes[1].group,h:saveRes[1].hierarchy,n:saveRes[1].node,p:saveRes[1].perm};activeBook.setWinCaption(fileName);Jedox.wss.workspace.updateMeta(activeBook.getWinId(),fileName,newWbMeta);activeBook.setWbId(saveRes[1].wbid);Jedox.wss.action.adjustToACL();try{for(var triggers=Jedox.wss.events.triggers.saveAsWorkbook_after,i=triggers.length-1,wbMeta=newWbMeta;i>=0;i--){triggers[i][0]["saveAsWorkbook_after"].call(parent,triggers[i][1],{g:wbMeta.g,h:wbMeta.h,n:wbMeta.n},oldMeta.name,fileName)}}catch(e){Jedox.wss.general.showMsg("Application Error",e.message,Ext.MessageBox.ERROR)}if(cb instanceof Array&&cb.length>1){cb[1].call(cb[0])}}}else{Ext.MessageBox.show({title:"Save As".localize(),msg:"save_as_err_msg".localize({fileName:fileName}),modal:true,buttons:Ext.MessageBox.OK,icon:Ext.MessageBox.WARNING,fn:Ext.emptyFn,minWidth:300})}};this.onInsertHyperlink=function(){var g=that.activeGroup.id;var h=that.activeHierarchy.id;var n=that.selectionId;if(g&&h&&n){var result=Jedox.wss.backend.studio.getNodeHyperlinkPropertiesData(g,h,n);if(result){return result}else{return false}}else{var title="Insert Hyperlink".localize();var message="_hl_no_selected_file".localize();that.showMessageALERT(title,message)}};this.preselectPath=function(ghn){that.initGroups(function(result){if(result){for(var i=0,count=that.gStore.getCount();i<count;i++){if(that.gStore.getAt(i).get("id")==ghn.g){break}}that.selectGroup(i,function(result){if(result){that.initHierarchies(ghn.g,function(){var hNode=that.hierarchyTP.getNodeById(ghn.h);if(hNode){hNode.expand(false,false,function(hNode){var wssStudioHandler={getElementPath:function(result){if(!result[0]){that.showMessageERROR("Hyperlink Error".localize(),"_hl_missing_target_node".localize());return false}var path="/root/"+result[1];that.hierarchyTP.expandPath(path,false,function(bSuccess,oLastNode){if(bSuccess&&oLastNode){if(result.length==2&&result[1]){var id=result[1].split("/")[result[1].split("/").length-1];var node=that.hierarchyTP.getNodeById(id);if(node){that._selectAfterDump_cb=function(){that.selectNode(ghn.n)};that.hierarchyTP.fireEvent("click",node)}}}})}};var wssStudioStub=new Studio(wssStudioHandler);wssStudioStub.getElementPath(ghn.g,ghn.h,ghn.n)})}})}})}})}},showMessageERROR:function(title,message){Ext.Msg.show({title:title,msg:message,buttons:Ext.Msg.OK,animEl:"elId",icon:Ext.MessageBox.ERROR})},showMessageALERT:function(title,message){Ext.Msg.show({title:title,msg:message,buttons:Ext.Msg.OK,animEl:"elId",icon:Ext.MessageBox.WARNING})},getRelativePath:function(id){var parent=this.hierarchyTP.getNodeById(this.activeHierarchy.id+"_"+this.getParentId()).getPath("text");var fileName=this.getFileName(this.fileNameCmb.getValue());return parent.concat("/",fileName).slice(1)},getFileName:function(name){var extension;var iconCls;for(var i=0;i<this.fStore.getCount();i++){if(this.fStore.getAt(i).data.text===name){iconCls=this.fStore.getAt(i).data.iconCls;break}}switch(iconCls){case"w3s_workbook":extension=".wss";break;case"w3s_jpg":extension=".jpg";break;case"w3s_png":extension=".png";break;case"w3s_gif":extension=".gif";break;case"w3s_txt":extension=".txt";break;case"w3s_rtf":extension=".rtf";break;case"w3s_pdf":extension=".pdf";break;case"w3s_zip":extension=".zip";break;case"w3s_rar":extension=".rar";break;case"w3s_doc":extension=".doc";break;case"w3s_csv":extension=".csv";break;case"w3s_pps":extension=".pps";break;case"w3s_ppt":extension=".ppt";break;case"w3s_xls":extension=".xls";break;case"w3s_xlsx":extension=".xlsx";break;case"w3s_hyperlink":extension=".hyperlink";break;case"w3s_html":extension=".html";break;default:extension=".unknown";break}return name.concat(extension)},openWB:function(id){var that=this;var group=this.gCmb.getValue();var hierarchy=this.activeHierarchy.id;var name=this.fileNameCmb.getValue();try{that.closeFnc();for(var triggers=Jedox.wss.events.triggers.openWorkbook_before,i=triggers.length-1;i>=0;i--){triggers[i][0]["openWorkbook_before"].call(parent,triggers[i][1],{g:group,h:hierarchy,n:id},name,false)}Jedox.wss.book.load(null,id,group,hierarchy,{vars:true});for(var triggers=Jedox.wss.events.triggers.openWorkbook_after,i=triggers.length-1;i>=0;i--){triggers[i][0]["openWorkbook_after"].call(parent,triggers[i][1],{g:group,h:hierarchy,n:id},name)}}catch(e){Jedox.wss.general.showMsg("Application Error".localize(),e.message.localize(),Ext.MessageBox.ERROR)}},getParentId:function(){var sn=this.hierarchyTP.selModel.getSelectedNode();if(sn){if(sn.attributes.type){return sn.attributes.type}else{if(sn.id==="root"){return null}else{return sn.attributes.n_uid}}}},getIdByName:function(name){for(var i=0;i<this.fStore.getCount();i++){if(name===this.fStore.getAt(i).get("text")){return this.fStore.getAt(i).get("id")}}},trim:function(s){return s.replace(/^\s+|\s+$/g,"")},validateName:function(name){var that=this;function validateInput(){return true}if(!validateInput()){return false}name=that.trim(name).toLowerCase();for(var i=0;i<that.fStore.getCount();i++){if(name===that.fStore.getAt(i).get("text").toLowerCase()&&that.fStore.getAt(i).get("iconCls")=="w3s_workbook"){return false}}return true},initGroups:function(cb){var that=this;var wssStudioHandler={treeMngGroup:function(result){if(!result){that.showMessageERROR("Database read error".localize(),"read_data_err".localize());if(cb){cb(false)}return false}var gData=[];for(var i in result){gData.push([i,result[i].name])}that.gStore.loadData(gData);if(cb){cb(true)}}};var wssStudioStub=new Studio(wssStudioHandler);wssStudioStub.treeMngGroup("listGroups",["file"])},selectGroup:function(index,cb){var that=this;var wssStudioHandler={treeSetGroup:function(result){if(!result){that.showMessageERROR("Database read error".localize(),"read_data_err".localize());if(cb){cb(false)}return false}that.activeGroup=record.data;that.gCmb.setValue(record.data.id);that.hierarchyTP.root.setText(record.data.name);that.hierarchyTP.root.g_id=record.data.id;if(cb){cb(true)}}};if(that.gStore.getCount()>0){var record=that.gStore.getAt(index);var wssStudioStub=new Studio(wssStudioHandler);wssStudioStub.treeSetGroup("wss",record.data.id)}else{that.activeGroup=null;that.gCmb.disable();if(cb){cb(false)}return false}},initHierarchies:function(group,cb){var that=this;var nodes=[];this.hierarchyTP.root.eachChild(function(node){nodes.push(node)},[this.hierarchyTP]);for(var i=0;i<nodes.length;i++){nodes[i].remove()}if(!group){group=this.activeGroup.id}var hData=[];var wssStudioHandler={treeMngHierarchy:function(result){if(result!=null&&!result){that.showMessageERROR("Database read error".localize(),"read_data_err".localize());return false}for(var i in result){that.hierarchyTP.root.appendChild(new Ext.tree.AsyncTreeNode({id:i,text:result[i].name,draggable:false,editable:false,iconCls:"w3s_hierarchy",type:"root",loader:new Ext.tree.TreeLoader({dataUrl:Jedox.wss.backend.studio.dispatcher.serverUrl.concat("&wam=",Jedox.wss.app.appModeS,"&c=",Jedox.wss.backend.studio.className,"&m=treeDump"),baseParams:{type:"wss",hierarchy:i,filter:"folder",multi_h:true}})}))}that.hierarchyTP.root.expand(false,false,function(gNode){if(cb){cb();return}if(that.hierarchyTP.root.hasChildNodes()){var hNode;if(that.hierarchyTP.root.hasChildNodes()){hNode=that.hierarchyTP.getNodeById(Jedox.wss.app.defaultFiles.hierarchy)||that.hierarchyTP.getNodeById(that.hierarchyTP.root.firstChild.attributes.id)}else{hNode=that.hierarchyTP.getNodeById(that.hierarchyTP.root.attributes.id)}if(hNode){hNode.expand(false,false,function(hNode){that.hierarchyTP.fireEvent("click",hNode)})}}})}};var wssStudioStub=new Studio(wssStudioHandler);wssStudioStub.treeMngHierarchy("wss","listHierarchies")},selectNode:function(n){for(var i=0;i<this.fStore.getCount();i++){if(this.fStore.getAt(i).get("id")==n){break}}Ext.getCmp("main-list-view").select(i);Ext.getCmp("main-thumbnails-view").select(i);Ext.getCmp("main-details-view").getSelectionModel().selectRow(i);this.selectionId=this.fStore.getAt(i).get("id");this.fileNameCmb.setValue(this.fStore.getAt(i).get("text"))}});