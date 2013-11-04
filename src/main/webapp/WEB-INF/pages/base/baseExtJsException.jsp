<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">

	Ext.data.Connection.prototype.timeout = Ext.data.Connection.prototype.timeout * 60 * 60;
	Ext.Ajax.on('requestexception', function(conn, response, options) {
		if (options.maskingCmp) {
			options.maskingCmp.getEl().unmask();
		}
		if (response.status == 404) {
			Ext.Msg.alert("系统消息", "您请求的资源不存在");
		} else if (response.status == 405 || response.status == 401) {
		   
		     Ext.Msg.show({
                           title:'系统消息',
                           msg: '有的资源你没有权限访问或操作!',
                           modal : false,
                           icon : Ext.Msg.ERROR,
                           buttons: Ext.Msg.OK
                        });
		} else if (response.status == 403) {
		       Ext.Msg.show({
                           title:'系统消息',
                           msg: '请求资料受限!',
                            icon : Ext.Msg.ERROR,
                           modal : false,
                           buttons: Ext.Msg.OK
                        });
		}else if (response.status == 0) {
 			  Ext.Msg.show({
                           title:'系统消息',
                           msg: "网络断开或服务器当掉，请您联系管理员解决",
                           icon : Ext.MessageBox.ERROR,
                           modal : false,
                           buttons: Ext.Msg.OK
                        });
		} else {
 			  Ext.Msg.show({
                           title:'系统消息',
                           msg: response.responseText,
                           icon : Ext.MessageBox.ERROR,
                           modal : false,
                           buttons: Ext.Msg.OK
                        });
		}
		return;
	});
	
	Ext.Ajax.handleResponse = function(response) {
		if (response.getResponseHeader.isLogin) {
	        //发现请求超时，退出处理代码... 
	        Ext.Msg.confirm("提示信息", "session已过期,确定要刷新页面吗?", function(msg) {
	            if(msg == "yes") {
	                window.location.reload();
	            }
	        })
	    } else {
	    	this.transId = false;
	        var options = response.argument.options;
	        response.argument = options ? options.argument : null;
	        this.fireEvent("requestcomplete", this, response, options);
	        Ext.callback(options.success, options.scope, [response, options]);
	        Ext.callback(options.callback, options.scope, [options, true, response]);
	    }
    };
	
	//处理IE下的当后台方法为void,且请求为POST请求,IE下的状态码为1223且进入Ext的AJAX错误处理函数的问题。
	Ext.Ajax.handleFailure = function(response, e) {
        this.transId = false;
        var options = response.argument.options;
        response.argument = options ? options.argument : null;
        if (Ext.isIE && response.status == 1223) {
        	Ext.callback(options.success, options.scope, [response, options]);
        } else {
        	this.fireEvent("requestexception", this, response, options, e);
        	Ext.callback(options.failure, options.scope, [response, options]);
        }
        Ext.callback(options.callback, options.scope, [options, false, response]);
    };
</script>
