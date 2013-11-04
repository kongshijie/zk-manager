/**
 * @author DJ
 * @description主页面
 * @date 2010-08-26
 */
Ext.onReady(function() {
			Ext.QuickTips.init();
			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
			new Ext.Viewport({
						layout : 'border',
						layoutConfig : {
							animate : true
						},
						items : [{
									region : 'center',
									layout : 'border',
									autoScroll : true,
									split : true,
									height : 500,
									border : true,
                                    split : true,
									items : [{
												region : 'west',
												layout : 'fit',
												title : '所 有 角 色',
												collapsible : true,
												autoScroll : true,
												split : true,
												width : 300,
												border : true,
												html :'west'
												//items : control.left
											}, {
												title : '角色对应权限',
												region : 'center',
												layout : 'fit',
												autoScroll : true,
												split : true,
												width : 600,
												border : true,
												html : 'center'
											}

									]
								}]
					});

		});