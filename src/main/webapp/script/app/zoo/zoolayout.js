/**
 * @author DJ
 * @description主页面
 * @date 2010-08-26
 */
Ext.onReady(function() {

	Ext.QuickTips.init();
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
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
			items : [ {
				region : 'west',
				layout : 'fit',
				title : '导航栏',
				collapsible : true,
				collapseMode : 'mini',
				margins : '0 0 0 0',
				autoScroll : true,
				split : true,
				width : 400,
				border : false,
				items : control.left
			}, {
				// title : '角色对应权限',
				region : 'center',
				layout : 'fit',
				autoScroll : true,
				split : true,
				width : 600,
				border : false,
				items : control.center
			} ]
		} ]
	});

});