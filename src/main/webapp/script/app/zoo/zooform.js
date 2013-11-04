
/**
 * @author dejianliu
 * @param {}
 *            config
 */
ZooForm = function(config) {
	var config = config || {};
	
	var $this = this;
	var cacheCurNode = {};
	Ext.apply(config, {
				defaults : {
					layout : 'fit'
				},
 				frame : true,
				resizable : false,
				tbar : [{
							xtype : 'label',
							text : '节点路径:'
									
						}, {
							xtype : "textfield",
							name : "nodeName",
							id : 'curNodeLabel_1',
							fieldLabel : "节点路径",
							width : 500,
							labelAlign : 'right',
							readOnly : true
						}, '-', {
							xtype : 'button',
							cls : 'x-btn-text-icon',
							text : '保存',
							iconCls : 'Databasesave',
							minWidth : 70,
							pressed : true,
							handler : function() {
								$this.save();
							}
						}],
				items : [{
							xtype : 'textarea',
							id : 'nodeData_1',
							fieldLabel : '节点数据',
							hideLabel : true,
							anchor : '100%-90',
							height : '100%',
							name : 'nodeDataAll',
							preventScrollbars : true
						}]

			});
	ZooForm.superclass.constructor.call(this, config);
}

Ext.extend(ZooForm, Ext.form.FormPanel, {
			save : function() {
				var $$this = this;
				var path = Ext.get('curNodeLabel_1').getValue();
				var nodeData = Ext.get('nodeData_1').getValue();
				Ext.Ajax.request({
							url : Urls.saveUrl,
							method : "POST",
							params : {
								'path' : path,
								'data' : nodeData
							},
							disableCaching : true,
							success : function(response, options) {
								$$this.fireEvent("saveEvent",$$this.cacheCurNode);
								Ext.MessageBox.show({
											title : '系统提示',
											msg : '操作成功',
											buttons : Ext.MessageBox.OK,
											icon : Ext.MessageBox.INFO
										});
 							}
						});
			}
		});