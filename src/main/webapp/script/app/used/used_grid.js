/**
 * @description 已经使用应用信息
 * @author DJ
 */
UsedGrid = function(config) {
	var config = this.config = config || {};

	var checkBox = new Ext.grid.CheckboxSelectionModel();
	var store = this.store = new Ext.data.JsonStore({
		id : 'appPath',
		fields : [ 'appName', 'configPath', 'appPath', 'processId', 'serverIp',
				'createTimeDate', 'startConf', 'systemConf' ],
		root : 'items',
		totalProperty : 'totalCount',
		proxy : new Ext.data.HttpProxy({
			url : Urls.usedQueryUrl,
			method : "GET"
		})
	})

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
			 text : '应用服务IP:'
			
		}, {
			xtype : 'textfield',
			name : 'serverIp',
			id : 'serverIp',
			scope : this
		},'-',{
			 xtype : 'label',
			 text : '配置文件:'
			
		}, {
			xtype : 'textfield',
			name : 'configPath',
			id : 'configPath',
			scope : this
		}, '-',{
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
			header : '应用名称',
			align : 'center',
			width : 50,
			dataIndex : 'appName'

		}, {
			header : '配置文件',
			align : 'center',
			width : 100,
			dataIndex : 'configPath'
		}, {
			header : '进程ID',
			align : 'center',
			width : 50,
			dataIndex : 'processId'

		}, {
			header : '服务IP',
			align : 'center',
			width : 50,
			dataIndex : 'serverIp'
		}, {
			header : '应用路径',
			align : 'center',
			width : 200,
			dataIndex : 'appPath'
		}, {
			header : '创建时间',
			align : 'center',
			width : 50,
			dataIndex : 'createTimeDate'
		}, {
			header : '启动配置',
			align : 'left',
			dataIndex : 'startConf'
		}, {
			header : '系统配置',
			align : 'left',
			dataIndex : 'systemConf'
		} ],
		sm : checkBox
	});

	UsedGrid.superclass.constructor.call(this, config);
	this.initAction();
};

Ext.extend(UsedGrid, Ext.grid.EditorGridPanel, {
	listeners : {
		'rowdblclick' : function(obj, rowIndex, e) {
			var store = obj.getStore();
			var record = store.getAt(rowIndex);
			var config = record.data;
			var usedWin;
			if (!usedWin) {
				usedWin = new Ext.Window({
					title : '应用详细信息:',
					height : 550,
					width : 730,
					layout : 'form',
					items : [ {
						xtype : 'form',
						defaults : {
							width : 700
						},
						modal : true,
						frame : true,
						resizable : false,
						items : [ {
							layout : "column",
							border : false,
							frame : true,
							defaults : {
								labelAlign : 'left',
								labelWidth : 60
							},
							items : [ {
								columnWidth : .4,
								layout : 'form',
								items : [ {
									xtype : "textfield",
									fieldLabel : "应用名称",
									width : 200,
									style : "font-weight:bold;",
									readOnly : true,
									value : config.appName
								} ]

							}, {
								columnWidth : .6,
								layout : 'form',
								items : [ {
									xtype : "textfield",
									name : "notifyTypes",
									fieldLabel : "配置路径",
									style : "font-weight:bold;",
									readOnly : true,
									width : 350,
									value : config.configPath
								} ]

							} ]
						}, {
							layout : "column",
							border : false,
							frame : true,
							defaults : {
								labelAlign : 'left',
								labelWidth : 60,
								width : 350
							},
							items : [ {
								columnWidth : .4,
								layout : 'form',
								items : [ {
									xtype : "textfield",
									fieldLabel : "服务器IP",
									style : "font-weight:bold;",
									readOnly : true,
									width : 200,
									value : config.serverIp

								} ]

							}, {
								columnWidth : .6,
								layout : 'form',
								items : [ {
									xtype : "textfield",
									name : "alarmStatus",
									fieldLabel : "进程ID",
									style : "font-weight:bold;",
									readOnly : true,
									width : 350,
									value : config.processId
								} ]

							} ]

						}, {
							layout : "column",
							border : false,
							frame : true,
							defaults : {
								labelAlign : 'left',
								labelWidth : 60,
								width : 350
							},
							items : [ {
								columnWidth : .4,
								layout : 'form',
								items : [ {
									xtype : "textfield",
									name : "alarmIp",
									fieldLabel : "创建时间",
									style : "font-weight:bold;",
									readOnly : true,
									width : 200,
									value : config.createTimeDate
								} ]

							}, {
								columnWidth : .6,
								layout : 'form',
								items : [ {
									xtype : "textfield",
									name : "alarmSource",
									fieldLabel : "应用路径",
									style : "font-weight:bold;",
									readOnly : true,
									width : 350,
									value : config.appPath
								} ]

							} ]

						}, {
							xtype : "textarea",
							grow : true,
 							fieldLabel : "启动配置",
 							labelStyle : 'width:60px;',
							style : "margin-left:-38px;",
							readOnly : true,
							cols : 10,
							//maxLength : 100,
							minLength : 5,
							height : 100,
							width : 640,
							autoScroll : true,
							value : config.startConf
						}, {
							xtype : "textarea",
							grow : true,
							labelStyle : 'width:60px;',
							style : "margin-left:-38px;",
 							name : "notifyContent",
							fieldLabel : "系统配置",
							readOnly : true,
							cols : 10,
							autoScroll : true,
							//maxLength : 100,
							minLength : 5,
							height : 240,
							width : 640,
							value : config.systemConf
						} ]
					} ]
				});
			}

			usedWin.show();
		}

	},
	initAction : function() {

	},
	clear : function() {
			Ext.get("serverIp").dom.value="";
			Ext.get("configPath").dom.value="";
	},
	query : function() {
		var obj = {
			serverIp : Ext.get("serverIp").getValue(),
			configPath : Ext.get("configPath").getValue()
		};

		this.store.load({
			params : obj,
			callback : function() {
			}
		});

	}
})
