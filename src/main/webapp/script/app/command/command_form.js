
/**
 * @author dejianliu
 * @param {}
 *            config
 */
CommandForm = function(config) {
	var config = config || {};

	var $this = this;
	var cacheCurNode = {};
	
 
	
	var zkHostStore = new Ext.data.SimpleStore({
	    fields : ['hostCode','hostName'],
	    data : hostInfoData //后台响应
	});
 
	var zkHostCombox = new Ext.form.ComboBox({
        store: zkHostStore,
        valueField: 'hostCode', 
        displayField:'hostName',
        id : 'zk_host_box_id',
        typeAhead: true,
        mode: 'local',
        value : hostInfoData[0][0],
        forceSelection: true,
        triggerAction: 'all',
        emptyText:'请选择',
        selectOnFocus:true
    });
 
	
	var commandStore = new Ext.data.SimpleStore({
	    fields : ['commandCode','commandName'],
	    data : commandData  //后台响应
	});
 
	var commandCombox = new Ext.form.ComboBox({
        store: commandStore,
        valueField: 'commandCode', 
        displayField:'commandName',
        id : 'command_box_id',
        typeAhead: true,
        mode: 'local',
        value : commandData[0][0],
        forceSelection: true,
        triggerAction: 'all',
        emptyText:'请选择',
        selectOnFocus:true
    });
	
	
	
	Ext.apply(config, {
		defaults : {
			layout : 'fit'
		},
		frame : true,
		resizable : false,
		tbar : [ {
			xtype : 'label',
			text : '主机:'
		},zkHostCombox,'-', {
			xtype : 'label',
			text : '命令:'
		}, commandCombox, '-', {
			xtype : 'button',
			cls : 'x-btn-text-icon',
			text : '执行',
			iconCls : 'Databasesave',
			minWidth : 70,
			pressed : true,
			handler : function() {
				$this.execute();
			}
		} ],
		items : [{
			xtype : 'textarea',
			id : 'redData_1',
			fieldLabel : '节点数据',
			hideLabel : true,
			anchor : '100%-90',
			height : '100%',
			name : 'redDataAll',
			preventScrollbars : true
		} ]

	});
	CommandForm.superclass.constructor.call(this, config);
};

Ext.extend(CommandForm, Ext.form.FormPanel, {
	execute : function() {
		var $$this = this;
		var host = Ext.getCmp('zk_host_box_id').getValue();
		var command = Ext.getCmp('command_box_id').getValue();
 
 		Ext.Ajax.request({
			url : Urls.commandExecuteUrl,
			method : "POST",
			params : {
				'host' : host,
				'command' : command
			},
			disableCaching : true,
			success : function(response, options) {
 				 var res = Ext.decode(response.responseText);
 				 Ext.getCmp('redData_1').setValue(res.message); 
			}
		});
	}
});