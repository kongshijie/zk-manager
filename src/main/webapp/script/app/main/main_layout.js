/**
 * @author liudejian
 * @description主页面
 * @date 2010-08-26
 */
Ext.namespace('com.dj');
var centerFrame = null;
Ext
		.onReady(function() {
			new Ext.Viewport(
					{
						layout : 'border',
						layoutConfig : {
							animate : true
						},
						items : [
								{
									region : 'north',
									layout : 'fit',
									height : 50,
									border : true,
									frame : false,
									bodyStyle : " background-image:url('./script/lib/extjs3/resources/images/bg_config.jpg');padding:10px 5px 0;text-align: center;border: 20pt;cursor: pointer;",
									html : ' <marquee behavior="scroll" onclick="alert(\'if you any question please contact beipiao83@163.com!\')" style="font-weight: bold; font-size: 30px;font-family:宋体;font-color:#000000;">Zookeeper 管 理 系 统</marquee> '
								},
								{
									region : 'center',
									layout : 'fit',
									id : 'srcCenter',
									height : 700,
									border : false,
									frame : false,
									autoScroll : true,
									items : [ new Ext.TabPanel(
											{
												activeTab : 0,
												frame : true,
												autoScroll : true,
												defaults : {
													autoHeight : true,
													autoWidth : true
												},
												items : [
														{
															title : '服务状态',
															items : [ new Ext.ux.ManagedIFrame.Panel(
																	{
																		id : "monitorFrame",
																		loadMask : {
																			msg : '正在加载...'
																		},
																		deferredRender : false,
																		frameConfig : {
																			name : 'monitorForm'
																		},
																		height : 700,
																		border : false,
																		frame : false,
																		defaultSrc : Urls.monitorUrl
																	}) ]
														},{
															title : '节点树',
															items : [ new Ext.ux.ManagedIFrame.Panel(
																	{
																		id : "srcFrame",
																		loadMask : {
																			msg : '正在加载...'
																		},
																		deferredRender : false,
																		frameConfig : {
																			name : 'chatperadminForm'
																		},
																		height : 700,
																		autoScroll : true,
																		border : false,
																		frame : false,
																		defaultSrc : Urls.zooUrl
																	}) ]
														},
														{
															title : '连接者信息',
															items : [ new Ext.ux.ManagedIFrame.Panel(
																	{
																		id : "connectorFrame",
																		loadMask : {
																			msg : '正在加载...'
																		},
																		deferredRender : false,
																		frameConfig : {
																			name : 'connectorConfigFrame'
																		},
																		height : 700,
																		border : false,
																		frame : false,
																		defaultSrc : Urls.connectorUrl
																	}) ]
														},
														{
															title : 'Zoo服务端信息',
															items : [ new Ext.ux.ManagedIFrame.Panel(
																	{
																		id : "commandFrame",
																		loadMask : {
																			msg : '正在加载...'
																		},
																		deferredRender : false,
																		frameConfig : {
																			name : 'commandConfiAppForm'
																		},
																		height : 700,
																		border : false,
																		frame : false,
																		defaultSrc : Urls.commandUrl
																	}) ]
														},
														{
															title : 'WatcherByNode',
															items : [ new Ext.ux.ManagedIFrame.Panel(
																	{
																		id : "watcherFrame",
																		loadMask : {
																			msg : '正在加载...'
																		},
																		deferredRender : false,
																		frameConfig : {
																			name : 'watcherList'
																		},
																		height : 700,
																		border : false,
																		frame : false,
																		defaultSrc : Urls.watcherUrl
																	}) ]
														},
														{
															title : 'WatcherByClient',
															items : [ new Ext.ux.ManagedIFrame.Panel(
																	{
																		id : "watcherByClientFrame",
																		loadMask : {
																			msg : '正在加载...'
																		},
																		deferredRender : false,
																		frameConfig : {
																			name : 'watcherByClientList'
																		},
																		height : 700,
																		border : false,
																		frame : false,
																		defaultSrc : Urls.watcherclientUrl
																	}) ]
														},
														{
															title : '已使用应用',
															items : [ new Ext.ux.ManagedIFrame.Panel(
																	{
																		id : "useFrame",
																		loadMask : {
																			msg : '正在加载...'
																		},
																		deferredRender : false,
																		frameConfig : {
																			name : 'usedConfiAppForm'
																		},
																		height : 700,
																		border : false,
																		frame : false,
																		defaultSrc : Urls.usedappUrl
																	}) ]
														}

												]
											}) ],
									defaults : {
										autoScroll : true

									}
								} ]
					});

		});