
var cacheMonitorData = {};
cacheMonitorData.isStart = false;
cacheMonitorData.count = 0;
MonitorWin = function(config) {
	var config = config || {};
	Ext.QuickTips.init();
 
	var timeInterval = new Ext.form.TextField({
		 id:'timeInterval',
		 name : 'timeInterval',
		 value : 3,
		 fieldLabel : '检查间隔(s)'
	});
	
	var failUrl = new Ext.form.TextField({
		 id : 'failUrl',
		 name : 'failUrl',
		 fieldLabel : '失败调用URL',
		 width : 200
	});
	
	var cosoleText = new Ext.form.TextArea( {
		 fieldLabel: "console",
         id: "consoleShow",
         labelSepartor: ":",
         hideLabel : true,
         labelWidth: 60,
         height : 350,
         width: 500
	});
	
 
	var startBtn = new Ext.Button({
				text : '开始',
				id :'startBtn',
 				disabled : false,
				scope : this,
				handler : this.doStart
			});
	var stopBtn = new Ext.Button({
		text : '停止',
		id : 'stopBtn',
 		disabled : false,
		scope : this,
		handler : this.doStop
	});
	
	Ext.apply(config, {
		title : '监视窗口',
		width : 500,
		height : 450,
		plain : true,
		modal : true,
		layout : 'form',
		id : 'monitorWin',
		defaultType : 'textfield',
 		closeAction : 'hide',
   		shim : false,
		tbar : [startBtn,'-',stopBtn],
		items : [ {
			xtype : 'form',
			baseCls : 'x-plain',
			style : 'padding : 5px',
			items : [timeInterval,failUrl,cosoleText]
		} ]
	});
MonitorWin.superclass.constructor.call(this, config);
};

Ext.extend(MonitorWin, Ext.Window, {

	initAction : function() {
		
	},
	doMonitor : function() {
		Ext.Ajax.request({
			url : Urls.monitorallUrl,
			method : "POST",
			params : {
				timeInterval : Ext.getCmp('timeInterval').getValue(),
				failUrl : Ext.getCmp('failUrl').getValue()
			},
			disableCaching : true,
			success : function(response, options) {
				 var res = Ext.decode(response.responseText);

				 if(cacheMonitorData.count >= 3) {
					 cacheMonitorData.count = 0;
					 Ext.getCmp('consoleShow').setValue(res.message); 
				 } else {
					 Ext.getCmp('consoleShow').setValue(Ext.getCmp('consoleShow').getValue()+res.message); 
				 }
				 cacheMonitorData.count++;
			} 
		});

		
	},
	doStart : function() {
		var t = Ext.getCmp('timeInterval').getValue();
		if(cacheMonitorData.isStart) {
			return;
		}
		if(t == '' || isNaN(t)) {
			alert("时间间隔不合法!");
			return;
		};
		cacheMonitorData.isStart = true;
		var timer1 = window.setInterval("Ext.getCmp(\"monitorWin\").doMonitor();", t*1000);
		cacheMonitorData.time = timer1;
		Ext.getCmp("startBtn").setDisabled(true);
	},
	doStop : function() {
		window.clearInterval(cacheMonitorData.time);
		cacheMonitorData.isStart = false;
		Ext.getCmp("startBtn").setDisabled(false);
	}
 
});
