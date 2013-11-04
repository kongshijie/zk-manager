/**
 * @author dejianliu
 * @param {}
 *            config
 */
WatcherClientTreeGrid = function(config) {
	var config = config || {};

	var $this = this;
	var cacheCurNode = {};

	var zkHostStore = new Ext.data.SimpleStore({
		fields : [ 'hostCode', 'hostName' ],
		data : clientInfoData
 	});

	var zkHostCombox = new Ext.form.ComboBox({
		store : zkHostStore,
		valueField : 'hostCode',
		displayField : 'hostName',
		id : 'zk_client_info_id',
		typeAhead : true,
		mode : 'local',
 		forceSelection : true,
		triggerAction : 'all',
		emptyText : '--请选择--',
		selectOnFocus : true
	});

	Ext.apply(config, {
		title : '【根据客户端查看其监听节点】',
		width : 500,
		height : 300,
		id : 'watcher_client_tree_load',
		enableDD : true,
		tbar : [ {
			xtype : 'label',
			text : '客户端:'
		}, zkHostCombox, '-', {
			text : '加载',
			iconCls : 'Find',
			xtype : 'button',
			pressed : true,
			handler : function() {
				Ext.getCmp('watcher_client_tree_load').getRootNode().reload();
			}
		} ,'-',{
			
			xtype : 'button',
			text : '清空',
			iconCls : 'Textindentremove',
			scope : this,
			pressed : true,
			handler : function() {
				Ext.getCmp('zk_client_info_id').setValue("");
			}
		}],
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
			header : '总计',
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

			dataUrl : Urls.listwatcherbyclientUrl,
			listeners : {
				'beforeload' : function(treeloader, node) {
					treeloader.baseParams = {
						id : Ext.getCmp('zk_client_info_id').getValue()
					};
					treeloader.dataUrl =Urls.listwatcherbyclientUrl;
				}
			}
		})
	});
	WatcherClientTreeGrid.superclass.constructor.call(this, config);
}

Ext.extend(WatcherClientTreeGrid, Ext.ux.tree.TreeGrid, {});