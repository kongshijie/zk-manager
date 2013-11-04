/**
 * @author dejianliu
 * @param {}
 *            config
 */
WatcherTreeGrid = function(config) {
	var config = config || {};

	var $this = this;
	var cacheCurNode = {};

	var zkHostStore = new Ext.data.SimpleStore({
		fields : [ 'hostCode', 'hostName' ],
		data : hostInfoData
	// 后台响应
	});

	var zkHostCombox = new Ext.form.ComboBox({
		store : zkHostStore,
		valueField : 'hostCode',
		displayField : 'hostName',
		id : 'zk_watcher_id',
		typeAhead : true,
		mode : 'local',
		value : hostInfoData[0][0],
		forceSelection : true,
		triggerAction : 'all',
		emptyText : '--请选择--',
		selectOnFocus : true
	});

	Ext.apply(config, {
		title : '【根据节点查看监听者】如果数据超过1000系统自动截取前1000笔数据',
		width : 500,
		height : 300,
		id : 'watcher_tree_load',
		enableDD : true,
		tbar : [ {
			xtype : 'label',
			text : 'ZK服务端:'
		}, zkHostCombox, '-', {
			text : '加载',
			xtype : 'button',
			pressed : true,
			handler : function() {
				Ext.getCmp('watcher_tree_load').getRootNode().reload();
			}
		} ],
		columns : [ {
			header : '被监听节点',
			dataIndex : 'node',
			align : 'center',
			width : 600
		}, {
			header : '服务端',
			align : 'center',
			dataIndex : 'zkHost',
			width : 150
		}, {
			header : '监听客户端',
			align : 'center',
			dataIndex : 'clientIpHost',
			width : 150
		}, {
			header : '会话时长',
			align : 'center',
			dataIndex : 'sessionTime',
			width : 200
		} ],
		
		loader : new Ext.tree.TreeLoader({
			baseAttrs : {// 设置子节点的基本属性
				cust : 'client'
			},

			dataUrl : Urls.listwatcherbynodeUrl,
			listeners : {
				'beforeload' : function(treeloader, node) {
					treeloader.baseParams = {
						id : Ext.getCmp('zk_watcher_id').getValue()
					};
			 
					treeloader.dataUrl =Urls.listwatcherbynodeUrl;
				}
			}

		})
		
		//dataUrl : Urls.listwatcherbynodeUrl

	});
	WatcherTreeGrid.superclass.constructor.call(this, config);
}

Ext.extend(WatcherTreeGrid, Ext.ux.tree.TreeGrid, {
	
	
	
});