/**
 * @author DJ
 * @description 已使用应用
 * @date 2010-08-26
 */
Ext.namespace('com.dj')
Ext.onReady(function() {
			var control = new Control();

		    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

 		    var tools = [{
		        id:'gear',
		        handler: function(){
		            Ext.Msg.alert('系统提示', '工具设置');
		        }
		    },{
		        id:'close',
		        handler: function(e, target, panel){
		            panel.ownerCt.remove(panel, true);
		        }
		    }];
		    
			  var viewport = new Ext.Viewport({
			        layout:'border',
			        items:[{
			            xtype:'portal',
			            region:'center',
 			            margins:'10 5 5 0',
			            id : 'portal_center',
			            items:[{
			                columnWidth:.9,
			                style:'padding:10px 0 10px 10px',
			                items:[{
			                    title: '服务状态',
			                    layout:'fit',
			                    id : 'server_status',
			                    tools: tools,
			                    height : 400,
 			                    items : control.serverStatus
			                }]
			            }
			            /*,{
			                columnWidth:.2,
			                style:'padding:10px 0 10px 10px',
			                items:[{
			                    title: '监控',
			                    height : 400,
			                    tools: tools,
			                    html: "6666666666"
			                }]
			            }
			            */
			            ]
			            
			            /*
			             * Uncomment this block to test handling of the drop event. You could use this
			             * to save portlet position state for example. The event arg e is the custom 
			             * event defined in Ext.ux.Portal.DropZone.
			             */
			           /* ,listeners: {
			                'drop': function(e){
			                    Ext.Msg.alert('Portlet Dropped', e.panel.title + '<br />Column: ' + 
			                        e.columnIndex + '<br />Position: ' + e.position);
			                }
			            }*/
			        }]
			    });
			  
  

		});