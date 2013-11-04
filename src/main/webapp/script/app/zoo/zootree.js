

/**
 * @description 数据缓存
 * @type 
 */
var nodeCache = {};

/**
 * Zoo树
 * @author dejian.liu
 * @param {} config
 */
ZooTree = function(config) {
	var config = config || {};
	var childbool = true;

	var $this = this;
	Ext.apply(config, {
		// title : '权限',
		frame : true,
		enableDD : false,
		enableDrag : false,
		rootVisible : true,
		autoScroll : true,
		autoHeight : false,
		autoWidth : true,
		border : false,
		useArrows : false,
		expanded : false,
		lines : true,
		height : 680,
		viewConfig : {
			forceFit : true
		},
		bodyStyle : 'background-color:#000fff;',
		bodyStyle : "cursor: pointer;",
		root : new Ext.tree.AsyncTreeNode({
					text : '配置管理',
					expanded : false,// 设置根节点默认是展开的
					id : zookeeper.root,
					checked : false,
					path : zookeeper.root
				}),
		// 设置异步树节点的数据加载器
		loader : new Ext.tree.TreeLoader({
					baseAttrs : {// 设置子节点的基本属性
						cust : 'client'
					},
					dataUrl : Urls.listTreeUrl,
					listeners : {
						'beforeload' : function(treeloader, node) {
							treeloader.baseParams = {
								id : node.text
							};
							var path = node.attributes.path;
							if (!path) {
								path = zookeeper.root;
							}
							treeloader.dataUrl = Urls.listTreeUrl + "?node="
									+ node.text + "&path=" + path;
						}
					}

				}),
		tbar : ['->', this.refleshBtn = new Ext.Button({
							text : '刷 新',
							iconCls : 'Arrowrefresh',
							scope : this,
							handler : function() {
								this.root.reload();
								this.getRootNode().expand(true)
							},
							minWidth : 70,
							pressed : true
						}), '-', this.closeBtn = new Ext.Button({
							text : '收 起',
							scope : this,
							iconCls : 'Sectioncollapsed',
							handler : function() {
								this.getRootNode().collapse(true);
							},
							minWidth : 70,
							pressed : true

						}), '-', this.expandBtn = new Ext.Button({
							text : '展 开',
							iconCls : 'Applicationsideexpand',
							scope : this,
							handler : function() {
								this.getRootNode().expand(true);
							},
							minWidth : 70,
							pressed : true
						})

		],
		listeners : {
			// 右键菜单
			'contextmenu' : function(node, e) {
				var nodemenu = new Ext.menu.Menu({
					items : [{
					    text : '刷新',
					    iconCls : 'Arrowrefresh',
					    handler : function() {
					        if(!node.leaf) { 
					        	node.reload();
					        }
					    }
						
					},{
					    text : '展开',
					    iconCls : 'NsExpand',
					    handler : function() {
					        if(!node.leaf) { 
					            node.expand(true);
					        }
					    }
						
					},{
					    text : '收起',
					    iconCls : 'NsCollapse',
					    handler : function() {
					        if(!node.leaf) { 
					        	node.collapse(true);
					        }
					    }
						
					},{
					    text : '复制节点',
					    iconCls : 'Pagecopy',
					    handler : function() {
 					       nodeCache.nodePath = node.attributes.path;
					       nodeCache.text = node.text;
					       Ext.Msg.show({
					            title : '系统提示',
							    msg : '节点【'+node.text+"】复制成功!(*^__^*)",
							    buttons : Ext.MessageBox.OK,
							    icon : Ext.MessageBox.INFO
					       });
					    }
					},{
					    text : '粘贴节点',
					    iconCls : 'Pagepaste',
					    handler : function() {
					        if(nodeCache.text == null || nodeCache.text.trim() == "" || nodeCache.nodePath == null || nodeCache.nodePath.trim() == "") {
					        	 Ext.Msg.show({
						            title : '系统提示',
								    msg : '没有需要粘贴的节点!o(╯□╰)o',
								    buttons : Ext.MessageBox.OK,
								    icon : Ext.MessageBox.WARNING
					            });
					        	return;
					        }
					        var reqData = {
					             	'targetNodePath' : node.attributes.path,
									'sourceNode' : nodeCache.text,
									'sourceNodePath' : nodeCache.nodePath
					        };
				 
                            Ext.Msg.confirm("系统提示","确定要复制节点【"+nodeCache.text+"】到节点【"+node.text+"】下?",function(e,text) {
                                 if (e == "yes") {
                                        Ext.Ajax.request({
												url : Urls.nodecopyUrl,
												method : "POST",
												params : reqData,
												disableCaching : true,
												success : function(response,options) {
													$this.fireEvent("nodechanage",node);
													 //清空缓存
					       							 nodeCache = {};
 												},
												failure : function() {
													Ext.MessageBox.show({
														title : '系统提示',
														msg : '添加节点失败',
														buttons : Ext.MessageBox.OK,
														icon : Ext.MessageBox.ERROR
													});
												}
											});  	
                                 }
                            });
					        
					    }
					},{
						text : "添加节点",
						iconCls : 'Applicationadd',
						handler : function() {
							var win;
							if (!win) {
								win = new Ext.Window({
									layout : 'fit',
									title : '新增加节点窗口: 父节点为【'
											+ node.attributes.path + '】',
									width : 600,
									height : 500,
 									buttonAlign : 'center',
									maximizable : true,
									plain : true,
									items : [{
										xtype : 'form',
										defaults : {
											layout : 'fit',
											labelWidth : 60,
											labelAlign : 'right',
											width : 540
 										},
										modal : true,
										frame : true,
										resizable : false,
										items : [{
													xtype : "textfield",
													name : "nodeName",
													fieldLabel : "节点名称",
													labelAlign : 'right',
 													emptyText : '请输入节点名称....',
													allowBlank : false,
													maxLengthText : '节点名称不能超过200!',
													minLengthText : '节点名称字符长度不符合要求!',
													blankText : '节点名称不能为空!',
													maxlength : 200,
													minlength : 2,
													frame : true,
													labelStyle : "font-weight:bold;"
												}, {
													xtype : 'textarea',
													fieldLabel : '节点数据',
													labelAlign : 'right',
													name : 'nodeData',
													anchor : '100% -80',
													preventScrollbars : true,
													labelStyle : "font-weight:bold;",
													height : 500
												}]

									}],
									buttons : [{
										text : '保存',
										handler : function() {
											var data = win.items;
											var form = data.items[0];
									        var nodeName = form.items.items[0].getValue();
									        var nodeData = form.items.items[1].getValue();
										    var chinaReg = /[\u4e00-\u9fa5]/;
										    
											if(nodeName == "" || nodeName.trim() == "") {
 												Ext.MessageBox.show({
														title : '系统提示',
														msg : '节点名称不能为空！',
														buttons : Ext.MessageBox.OK,
														icon : Ext.MessageBox.INFO
													});
												return;
											}
											if(chinaReg.test(nodeName)) {
 												Ext.MessageBox.show({
														title : '系统提示',
														msg : '节点名称不能为中文！',
														buttons : Ext.MessageBox.OK,
														icon : Ext.MessageBox.INFO
													});
												return;
											}
 
											Ext.Ajax.request({
												url : Urls.createUrl,
												method : "POST",
												params : {
													'node' : nodeName,
													'data' : nodeData,
													'path' : node.attributes.path
												},
												disableCaching : true,
												success : function(response,options) {
													$this.fireEvent("nodechanage",node);
													win.close();
												} 
											});
										}
									}, {
										text : '取消',
										handler : function() {
											win.close();
										}
									}]
								});
								win.show();
							}
						}

					}, {
						text : "删除节点",
						iconCls : 'Applicationdelete',
						handler : function() {
							Ext.MessageBox.confirm('确认窗口', '确定要删除节点【'
											+ node.text + '】吗?', function(e,
											text) {
										if (e == "yes") {
											Ext.Ajax.request({
												url : Urls.deleteUrl,
												method : "POST",
												params : {
													path : node.attributes.path
												},
												disableCaching : true,
												success : function(response,options) {
												         var parentNode = node.parentNode;
												         if(parentNode) {
												         	parentNode.reload();
												         } 	  
												} 
											})
										}
									});
						}
					},{
					    text : '属性',
					    iconCls : 'Applicationosx',
					    handler : function() {
					    	var nodeAttr = "当前节点名:"+node.text+"<br/>";
					    	nodeAttr += "是否子节点:"+node.leaf+"<br/>";
					    	nodeAttr += "节点全路径:"+node.attributes.path;
					       Ext.MessageBox.show({
									title : '节点属性',
									msg : nodeAttr,
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.INFO
							});
					    }
						
					}]
				})
				nodemenu.showAt(e.getXY());
			},
			'checkchange' : function(node, checked) {
				if (checked) {
					node.getUI().addClass('complete');
				} else {
					node.getUI().removeClass('complete');
				}
				var parentNode = node.parentNode;
				if (checked) {
					/**
					 * 节点为真时，此节点的子节点,判断此节点的父节点时，判断父节点的子节点是否全部为
					 * 真，如果全部为真，则此父节点为真，如果不为真则不变 全部为真
					 */
					if (childbool) {
						var childNodes = node.childNodes;
						for (var i = 0; i < childNodes.length; i++) {
							var childNode = childNodes[i];
							if (!childNode.attributes.checked) {
								childNode.ui.toggleCheck();
							}
						}
					}
					/**
					 * 此如果此节点又父节点，则判断此父节点的子节点是否全为真 如果全为真则此父节点也为真
					 */
					if (parentNode && !parentNode.attributes.checked) {
						childbool = false;
						parentNode.ui.toggleCheck();
					} else {
						childbool = true;
					}
				} else {
					/**
					 * 如果为false时，
					 */
					// if(parentNode&&parentNode.attributes.checked){
					// parentNode.attributes.checked=false;
					// parentNode.ui.toggleCheck();
					// }
					/*
					 * 父节点
					 */
					ParentState(parentNode);
					/*
					 * 子接点
					 */
					var childNodes = node.childNodes;
					for (var i = 0; i < childNodes.length; i++) {
						var childNode = childNodes[i];
						if (childNode.attributes.checked) {
							childNode.ui.toggleCheck();
						}
					}
				}
			},
			'click' : function(node, event) {				
				$this.fireEvent("nodeclick",node);
 			} 
		}

	});

	ZooTree.superclass.constructor.call(this, config);

}

Ext.extend(ZooTree, Ext.tree.TreePanel, {});

function ParentState(parentNode) {
	var brothNodes = null;
	if (parentNode != null) { // 兄弟接点
		brothNodes = parentNode.childNodes;
	} else {
		return false;
	}
	var brothflag = 0;
	for (var i = 0; i < brothNodes.length; i++) {
		var brothNode = brothNodes[i];
		if (brothNode.attributes.checked) {
			break;
		} else {
			brothflag++;
		}
	}
	if (brothflag == brothNodes.length) { // 说明兄弟节点没选种的
		if (parentNode.attributes.checked)
			parentNode.ui.toggleCheck();
		ParentState(parentNode.parentNode);
	}
}