Ext.tree.TreeNodeProvider=function(cfg){this.data=[];Ext.apply(this,cfg);if(!this.getNodes||(typeof this.getNodes!="function")){throw'!this.getNodes || typeof this.getNodes != "function"'}this.setData=function(t_data){this.data=t_data};this.getData=function(){return this.data}};