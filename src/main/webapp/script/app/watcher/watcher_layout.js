/**
 * @author DJ
 * @description zookeeper命令行
 * @date 2010-08-26
 */
Ext.namespace('com.dj');
Ext.onReady(function() {
	var control = new Control();

	new Ext.Viewport({
		layout : 'border',
		layoutConfig : {
			animate : true
		},
		items : [ {
			region : 'center',
			layout : 'border',
			autoScroll : true,
			split : true,
			height : 800,
			border : false,
			frame : false,
			items : [{
 				region : 'center',
				layout : 'fit',
				autoScroll : true,
				split : true,
				width : 600,
				border : false,
				items : control.center
			}
			
			]
		} ]
	});
});