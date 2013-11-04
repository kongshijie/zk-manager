var win = null;

Ext.Image = Ext.extend(Ext.Component, {
			initComponent : function() {

			},
			onRender : function(ct, position) {
				var a = document.createElement('a');
				a.id = this.id;
				a.href = "javascript:void(0)";
				var el = document.createElement('img');
				el.src = this.src + '?' + Math.random();
				el.style = "position:absolute; left:155px; top:62px;width:78;height:24;cursor:pointer;";
				a.appendChild(el);
				this.el = Ext.get(ct.dom.appendChild(a));
				if (this.autoRefresh)
					this.el.on('click', this.onClick, this);
			},
			onClick : function(e) {
				this.el.first().dom.src = this.src + '?' + Math.random();
			}
		})
  Ext.reg('ximg', Ext.Image);

/**
 * @description 登录窗口
 * @param {}
 *            config
 * @author liudejian
 */
LoginWin = function(config) {
	var config = config || {};
	this.userName = new Ext.form.TextField({
				name : "userName",
				fieldLabel : "用户名",
				id : 'login_user_name',
				style : "font-weight:bold;",
				allowBlank : false,
				blankText : "用户名不能为空！",
				scope : this,
				listeners : {
					'specialkey' : function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							Ext.getCmp("login_id").handler();
						}
					}
				}
			});

	this.userPwd = new Ext.form.TextField({
				name : "userPwd",
				fieldLabel : "密		码",
				allowBlank : false,
				id : 'login_user_pwd',
				style : "font-weight:bold;",
				blankText : "密码不能为空！",
				inputType : 'password',
				scope : this,
				listeners : {
					'specialkey' : function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							Ext.getCmp("login_id").handler();
						}
					}
				}
			});

	this.verifyCode = new Ext.form.TextField({
				name : "verifyCode",
				fieldLabel : '验证码',
				allowBlank : false,
				id : 'verify_code_1',
				style : "font-weight:bold;",
				blankText : "验证码不能为空！",
				scope : this,
				width : 80,
				listeners : {
					'specialkey' : function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							Ext.getCmp("login_id").handler();
						}
					}
				}

			});

 
   this.codeImage = new Ext.Image({
		id : 'codeImageId',
		autoRefresh:true,
		src : Urls.imageUrl
	});
	

	Ext.apply(config, {
				id : 'form1',
				layout : 'form',
				title : "登录窗口",
				closable : false,
				frame : true,
				bodyStyle : 'padding:10px',
				buttonsAlgin : 'center',
				height : 150,
				width : 250,
				labelAlign : 'left',
				labelWidth : 50,
				modal : true,
				resizable : true,
				items : [this.userName, this.userPwd, this.verifyCode,
						this.codeImage],
				bbar : ['->', {
							text : '登录',
							id : "login_id",
							scope : this,
							handler : this.login,
							minWidth : 70,
							pressed : true
						}, '-', {
							text : '重置',
							handler : function() {
								this.userName.setValue("");
								this.userPwd.setValue("");
								this.verifyCode.setValue("");
							},
							scope : this,
							minWidth : 70,
							pressed : true,
							style : 'margin-left:5px;'
						}, '->']
			});
	LoginWin.superclass.constructor.call(this, config);

}

Ext.extend(LoginWin, Ext.Window, {
			checkBeforeSave : function() {
				return true;
			},
		 
			login : function() {
				Ext.Ajax.request({
							maskingCmp : this,
							url : Urls.loginUrl,
							method : "POST",
							params : {
								'userName' : Ext.getCmp("login_user_name")
										.getValue(),
								'userPwd' : Ext.getCmp("login_user_pwd")
										.getValue(),
								'verifyCode' : Ext.getCmp("verify_code_1")
										.getValue()
							},
							success : function(response, options) {
								Ext.getBody().unmask();
								var data = response.responseText;
								var res = eval("(" + data + ")");
								if (res != "") {
									if (res.msg == "ok") {
										window.location.href = Urls.mainUrl;
									} else {
										Ext.MessageBox.show({
													title : '系统提示',
													msg : '用户名或密码或者验证码错误!',
													buttons : Ext.MessageBox.OK,
													icon : Ext.MessageBox.ERROR
												});
									}
								}
							},
							failure : function(response, options) {
								Ext.getBody().unmask();
							},
							scope : this
						});
			}
		});

Ext.onReady(function() {
			var win = new LoginWin({});
			win.show();
		});
