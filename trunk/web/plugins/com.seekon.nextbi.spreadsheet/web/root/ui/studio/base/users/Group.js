Jedox.studio.users.Group=function(addFlag){var that=this;this.addFlag=addFlag;this.groupName="";var prefs_level={SERVER:0,GROUP:1,USER:2};this.availableUsersStore=new Ext.data.SimpleStore({fields:[{name:"userName"}]});this.groupUsersStore=new Ext.data.SimpleStore({fields:[{name:"userName"}]});this.availableRolesStore=new Ext.data.SimpleStore({fields:[{name:"roleName"}]});this.groupRolesStore=new Ext.data.SimpleStore({fields:[{name:"roleName"}]});this.generalTabLbl={html:"Enter the General informations about Group".localize()+"...<br><br><br>",baseCls:"x-plain",bodyStyle:"font-size:11;"};this.groupNameTxf=new Ext.form.TextField({fieldLabel:"Group Name".localize(),labelStyle:"font-size:11px;",cls:"preview-properties-panel",allowBlank:false,width:200});this.descriptionTxa=new Ext.form.TextArea({fieldLabel:"Description".localize(),labelStyle:"font-size:11px;",cls:"preview-properties-panel",name:"description",width:200});this.splitLine={html:"<br><br>",baseCls:"split-line",width:315};this.optionsBtn=new Ext.Button({text:"Options".localize(),hidden:addFlag=="add",handler:function(){Jedox.studio.app.preferences(prefs_level.GROUP,that.groupNameTxf.getValue())}});var optionsBtnContainer={layout:"form",baseCls:"x-plain",width:315,buttonAlign:"right",buttons:this.optionsBtn};this.accountStatusChb=new Ext.form.Checkbox({labelStyle:"font-size:11px;",hidden:true});this.memberOfTabLbl={html:"Choose the users you want to asign to this group".localize()+"<br><br><br>",baseCls:"x-plain",bodyStyle:"font-size:11;"};this.memberOfRTabLbl={html:"Choose the roles you want to asign to this group".localize()+"<br><br><br>",baseCls:"x-plain",bodyStyle:"font-size:11;"};this.memeberOfIS={xtype:"itemselector",name:"itemselector",fieldLabel:"ItemSelector",cls:"preview-properties-panel",hideLabel:true,fromStore:this.availableUsersStore,dataFields:["userName"],toStore:this.groupUsersStore,drawUpIcon:false,drawDownIcon:false,drawTopIcon:false,drawBotIcon:false,msWidth:150,msHeight:200,displayField:"userName",imagePath:"../../../lib/ext/extensions/multiselect",toLegend:"Member Of".localize(),fromLegend:"Available Users".localize()};this.memeberOfRIS={xtype:"itemselector",name:"itemselector",fieldLabel:"ItemSelector",cls:"preview-properties-panel",hideLabel:true,fromStore:this.availableRolesStore,dataFields:["roleName"],toStore:this.groupRolesStore,drawUpIcon:false,drawDownIcon:false,drawTopIcon:false,drawBotIcon:false,msWidth:150,msHeight:200,displayField:"roleName",imagePath:"../../../lib/ext/extensions/multiselect",toLegend:"Member Of".localize(),fromLegend:"Available Roles".localize()};this.tabPanel=new Ext.TabPanel({activeTab:0,width:400,height:350,bodyStyle:"border-left:none; border-right:none; border-top:1; border-bottom:1;",enableTabScroll:true,plain:true,defaults:{autoScroll:true,bodyStyle:"padding:10px",layout:"form"},items:[{title:"General".localize(),items:[this.generalTabLbl,this.groupNameTxf,this.descriptionTxa,this.splitLine,optionsBtnContainer,this.accountStatusChb]},{title:"Member Of".localize(),items:[this.memberOfTabLbl,this.memeberOfIS]},{title:"Member Of Role".localize(),items:[this.memberOfRTabLbl,this.memeberOfRIS]}]});Jedox.studio.users.Group.superclass.constructor.call(this,{labelAlign:"right",border:false,bodyStyle:"padding:5px",cls:"preview-properties-panel",layout:"fit",items:[this.tabPanel],buttons:[{text:"Save".localize(),handler:this.onSave,scope:this},{text:"Cancel".localize(),handler:this.onCancel,scope:this}]});if(this.addFlag==="add"){this.initMemberOf()}};Ext.extend(Jedox.studio.users.Group,Ext.Panel,{initGroup:function(groupName){var that=this;this.groupName=groupName;var db="System";var cube="#_GROUP_GROUP_PROPERTIES";var order=["#_GROUP_"];var props=["groupName","description","accountStatus"];var cords={"#_GROUP_":[groupName],"#_GROUP_PROPERTIES_":props};props.shift();function cb(result){if(!result){Jedox.studio.app.showMessageERROR("Database error".localize(),"Can not read data".localize());return}that.groupNameTxf.setValue(that.getPropsValue(groupName));that.descriptionTxa.setValue(that.getPropsValue(result[groupName]["description"]));that.accountStatusChb.setValue(that.getPropsValue(result[groupName]["accountStatus"]));that.initMemberOf()}Jedox.wss.backend.conn.rpc([this,cb],"common","paloGet",[db,cube,order,cords])},getPropsValue:function(value){if(value!=undefined){return value}return""},initMemberOf:function(){var that=this;var db="System";var ug_cube="#_USER_GROUP";var ug_order=["#_GROUP_"];var ug_cords={"#_GROUP_":[that.groupName]};function ug_cb(result){if(!result){Jedox.studio.app.showMessageERROR("Database error".localize(),"Can not read data".localize());return}var availableUsersData=[];var groupUsersData=[];for(group in result){for(user in result[group]){if(result[group][user]=="1"){groupUsersData.push([user])}else{availableUsersData.push([user])}}}that.availableUsersStore.loadData(availableUsersData);that.groupUsersStore.loadData(groupUsersData)}Jedox.wss.backend.conn.rpc([this,ug_cb],"common","paloGet",[db,ug_cube,ug_order,ug_cords]);var gr_cube="#_GROUP_ROLE";var gr_order=["#_GROUP_"];var gr_cords={"#_GROUP_":[that.groupName]};function gr_cb(result){if(!result){Jedox.studio.app.showMessageERROR("Database error".localize(),"Can not read data".localize());return}var availableRolesData=[];var groupRolesData=[];for(group in result){for(role in result[group]){if(result[group][role]=="1"){groupRolesData.push([role])}else{availableRolesData.push([role])}}}that.availableRolesStore.loadData(availableRolesData);that.groupRolesStore.loadData(groupRolesData)}Jedox.wss.backend.conn.rpc([this,gr_cb],"common","paloGet",[db,gr_cube,gr_order,gr_cords])},addGroup:function(addF){var that=this;if(this.validateForm()){var db="System";var gg_cube="#_GROUP_GROUP_PROPERTIES";var gg_data={};var gg_order=["#_GROUP_"];var add=addF;var groupName=addF?this.groupNameTxf.getValue():this.groupName;var description=this.descriptionTxa.getValue();var status=this.accountStatusChb.getValue()?"1":"0";gg_data[groupName]={description:description,accountStatus:status};function gg_cb(result){if(!result){Jedox.studio.app.showMessageERROR("Database error".localize(),"Can not write data".localize());return}var ug_cube="#_USER_GROUP";var ug_data={};var ug_order=["#_GROUP_"];ug_data[groupName]=that.getGroupUsers();function ug_cb(result){if(!result){Jedox.studio.app.showMessageERROR("Database error".localize(),"Can not write data".localize());return}var gr_cube="#_GROUP_ROLE";var gr_data={};var gr_order=["#_GROUP_"];gr_data[groupName]=that.getGroupRoles();function gr_cb(result){if(!result){Jedox.studio.app.showMessageERROR("Database error".localize(),"Can not write data".localize());return}if(!addF&&groupName!=that.groupNameTxf.getValue()){var r_db="System";var r_dim="#_GROUP_";var r_data={};var newGroupName=that.groupNameTxf.getValue();r_data[groupName]=newGroupName;function r_cb(result){if(!result){Jedox.studio.app.showMessageERROR("Database error".localize(),"Can not write data".localize());return}that.onClose()}Jedox.wss.backend.conn.rpc([this,r_cb],"common","paloRename",[r_db,r_dim,r_data])}else{that.onClose()}}Jedox.wss.backend.conn.rpc([this,gr_cb],"common","paloSet",[db,gr_cube,gr_data,gr_order,add])}Jedox.wss.backend.conn.rpc([this,ug_cb],"common","paloSet",[db,ug_cube,ug_data,ug_order,add])}Jedox.wss.backend.conn.rpc([this,gg_cb],"common","paloSet",[db,gg_cube,gg_data,gg_order,add])}},getGroupUsers:function(){var data={};for(var i=0,auNmbr=this.memeberOfIS.fromStore.getCount();i<auNmbr;i++){data[this.memeberOfIS.fromStore.getAt(i).get("userName")]=""}for(var i=0,guNmbr=this.memeberOfIS.toStore.getCount();i<guNmbr;i++){data[this.memeberOfIS.toStore.getAt(i).get("userName")]="1"}return data},getGroupRoles:function(){var data={};for(var i=0,auNmbr=this.memeberOfRIS.fromStore.getCount();i<auNmbr;i++){data[this.memeberOfRIS.fromStore.getAt(i).get("roleName")]=""}for(var i=0,guNmbr=this.memeberOfRIS.toStore.getCount();i<guNmbr;i++){data[this.memeberOfRIS.toStore.getAt(i).get("roleName")]="1"}return data},isError:function(result){if(result[0]==="!"&&result.length===2){return true}return false},validateForm:function(){var that=this;var _return=true;function groupName(){var msg="Group name must start with a-z/A-Z character and must contain at least two character".localize();var my_regexp=/^[a-zA-Z][a-zA-Z0-9_\-@\.]+$/;var value=that.groupNameTxf.getValue();if(!my_regexp.test(value)){that.groupNameTxf.markInvalid(msg);_return=false}}groupName();return _return},onSave:function(){if(this.addFlag==="add"){this.addGroup(true)}else{this.addGroup(false)}},onCancel:function(){if(this.addFlag){this.ownerCt.ownerCt.refreshGroupList();this.ownerCt.ownerCt.remove(this.ownerCt)}else{this.initGroup(this.groupName)}},onClose:function(){if(this.addFlag){this.ownerCt.ownerCt.refreshGroupList();this.ownerCt.ownerCt.remove(this.ownerCt);if(this.addFlag=="add"){Jedox.studio.app.showTopMsg("","Group added successefully".localize())}else{Jedox.studio.app.showTopMsg("","Group updated successefully".localize())}}else{this.ownerCt.ownerCt.ownerCt.ownerCt.refreshGroupList();Jedox.studio.app.showTopMsg("","Group updated successefully".localize())}}});