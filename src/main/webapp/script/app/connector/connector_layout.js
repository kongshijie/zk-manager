/**
 * @author DJ
 * @description 已使用应用
 * @date 2010-08-26
 */
Ext.namespace('com.dj')
Ext.onReady(function() {
			var control = new Control();

			new Ext.Viewport({
						layout : 'border',
						layoutConfig : {
							animate : true
						},

						items : [{
									region : 'center',
									layout : 'fit',
									autoScroll : true,
									split : true,
									height : 800,
									border : true,
									items : control.center
								}]
					});

		});