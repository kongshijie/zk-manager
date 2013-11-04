/**
 * @description 已经使用应用信息
 * @author DJ
 */
ConnectorGrid = function(config) {
	var config = this.config = config || {};

	var zkHostStore = new Ext.data.SimpleStore({
	    fields : ['hostCode','hostName'],
	    data : hostInfoData //后台响应
	});
 
	var zkHostCombox = new Ext.form.ComboBox({
        store: zkHostStore,
        valueField: 'hostCode', 
        displayField:'hostName',
        id : 'zk_connector_id',
        typeAhead: true,
        mode: 'local',
        forceSelection: true,
        triggerAction: 'all',
        emptyText:'--请选择--',
        selectOnFocus:true
    });
 
	
	
	var clientStore = new Ext.data.SimpleStore({
		fields : [ 'clientHost', 'clientHost' ],
		data : clientHostData//后台响应
 	});

	var clientCombox = new Ext.form.ComboBox({
		store : clientStore,
		valueField : 'clientHost',
		displayField : 'clientHost',
		id : 'zk_client_host_id',
		typeAhead : true,
		mode : 'local',
 		forceSelection : true,
		triggerAction : 'all',
		emptyText : '--请选择--',
		selectOnFocus : true
	});

	
	var checkBox = new Ext.grid.CheckboxSelectionModel();
	var store = this.store = new Ext.data.JsonStore({
		id : 'connectorPath',
		fields : [ 'zooHost', 'clientHostPort', 'sent', 'recved', 'sessionId',
				'createConTimeDate', 'lastEstTimeDate','sessionDifDate' ],
		root : 'items',
		totalProperty : 'totalCount',
		proxy : new Ext.data.HttpProxy({
			url : Urls.listconnectorsUrl,
			method : "POST"
		})
	});

	Ext.apply(config, {
		autoScroll : true,
		enableColumnMove : false,
		trackMouseOver : true,
		stripeRows : true,
		loadMask : {
			msg : '加载中...'
		},
		sm : new Ext.grid.RowSelectionModel(),
		clicksToEdit : 'auto',
		viewConfig : {
			forceFit : true
		},
		tbar : [{
			 xtype : 'label',
			 text : 'ZK服务端:'
			
		},'-',zkHostCombox,{
			 xtype : 'label',
			 text : '客户端IP:'
			
		}, clientCombox,'-',{
			xtype : 'button',
			text : '查询',
			iconCls : 'Find',
			scope : this,
			pressed : true,
			handler : this.query

		}, '-',{
			
			xtype : 'button',
			text : '清空',
			iconCls : 'Textindentremove',
			scope : this,
			pressed : true,
			handler : this.clear
		}],
		store : store,
		columns : [ new Ext.grid.RowNumberer({}), checkBox, {
			header : 'ZK服务',
			align : 'center',
			width : 100,
			dataIndex : 'zooHost'

		}, {
			header : '连接客户端',
			align : 'center',
			width : 100,
			dataIndex : 'clientHostPort'
		}, {
			header : '会话ID',
			align : 'center',
			width : 150,
			dataIndex : 'sessionId'

		}, {
			header : '发送包',
			align : 'center',
			width : 50,
			dataIndex : 'sent'
		}, {
			header : '接收包',
			align : 'center',
			width : 50,
			dataIndex : 'recved'
		}, {
			header : '会话建立时间',
			align : 'center',
			width : 50,
			dataIndex : 'createConTimeDate'
		}, {
			header : '会话确认时间',
			align : 'center',
			width : 50,
			dataIndex : 'lastEstTimeDate'
		}, {
			header : '会话时长',
			align : 'center',
			width : 50,
			dataIndex : 'sessionDifDate'
		}],
		sm : checkBox
	});

	ConnectorGrid.superclass.constructor.call(this, config);
	this.initAction();
};

Ext.extend(ConnectorGrid, Ext.grid.EditorGridPanel, {
 
	initAction : function() {

	},
	clear : function() {
			Ext.getCmp("zk_connector_id").setValue("");
			Ext.getCmp("zk_client_host_id").setValue("");
			
	},
	query : function() {
		var obj = {
			host : Ext.getCmp("zk_connector_id").getValue(),
			clientIp : Ext.getCmp("zk_client_host_id").getValue()
		};

		this.store.load({
			params : obj,
			callback : function() {
			}
		});

	}
})
