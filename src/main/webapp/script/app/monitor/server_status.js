/**
 * @description 已经使用应用信息
 * @author DJ
 */
ServerStatus = function(config) {
	var config = this.config = config || {};

 	var checkBox = new Ext.grid.CheckboxSelectionModel();
	var store = this.store = new Ext.data.JsonStore({
		id : 'hostPort',
		fields : [ 'hostPort','serverVersion', 'sent', 'received', 'connections', 'mode',
				'nodeCount', 'status'],
		root : 'items',
		totalProperty : 'totalCount',
		proxy : new Ext.data.HttpProxy({
			url : Urls.monitorServerStatusUrl,
			method : "GET"
		}),
		autoLoad :true
	});

	Ext.apply(config, {
		id : 'serverStatudGrid',
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
			xtype : 'button',
			text : '刷新',
			iconCls : 'Find',
			scope : this,
			pressed : true,
			handler : this.query

		},'-',{
			xtype : 'button',
			text : '监视',
 			scope : this,
			pressed : true,
			handler : this.monitor

		}],
		store : store,
		columns : [ new Ext.grid.RowNumberer({}), checkBox,{
			header : 'ZK服务',
			align : 'center',
			width : 70,
			dataIndex : 'hostPort'
		}, {
			header : '服务版本',
			align : 'center',
			width : 200,
			dataIndex : 'serverVersion'

		}, {
			header : '连接数',
			align : 'center',
			width : 50,
			dataIndex : 'connections'
		}, {
			header : '节点数',
			align : 'center',
			width : 50,
			dataIndex : 'nodeCount'

		}, {
			header : 'MODE',
			align : 'center',
			width : 50,
			dataIndex : 'mode'
		}, {
			header : '状态',
			align : 'center',
			width : 50,
			dataIndex : 'status'
		}, {
			header : '发送包',
			align : 'center',
			width : 50,
			dataIndex : 'sent'
		}, {
			header : '已接收包',
			align : 'center',
			width : 50,
			dataIndex : 'received'
		}
		/*, {
			header : '操作',
			align : 'center',
			width : 50,
			dataIndex : 'hostPort',
			renderer : function(value, meta, rec, rowIdx, colIdx, ds) {
				var divId = "div" + value;
				var str = "<div   id=" + divId
						+ "><input type='button' value='监视' onclick='"
						+ 'Ext.getCmp(\"serverStatudGrid\").monitor("' + value + '")'
						+ "'/>";
			 
				str += "</div>";
				return str;
			}
		}*/
		],
		sm : checkBox
	});

	ServerStatus.superclass.constructor.call(this, config);
	this.initAction();
};

Ext.extend(ServerStatus, Ext.grid.EditorGridPanel, {
 
	initAction : function() {
		
	},
    monitor : function() {
    	if (!this.monitorWin) {
			this.monitorWin = new MonitorWin({id : 'monitroWin'});
		}
		this.monitorWin.show();
    },
	query : function() {
		this.store.load({});
	}
});
