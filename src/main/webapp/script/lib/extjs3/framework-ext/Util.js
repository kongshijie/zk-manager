var Util = {
	Constant : {
		IMAGE_PATH : rootPath + '/images/'
//		RESOURCES_PATH : rootPath + 'resources/'
	},
	/**
	 * 使用非Ajax异步方式导入js文件
	 * 
	 * @param {}
	 *            path ： rootPath下的路径,以/开头
	 */
	loadJs : function(path) {
		Util.log("已导入js文件", '<script type="text/javascript" src="' + rootPath
						+ path + '"></script>')
		document.write('<script type="text/javascript" src="' + rootPath + path
				+ '"></script>');
		console.log(Ext.isReady);
	},
	/**
	 * 载入模板
	 * 
	 * @param {}
	 * @example:template.form.FormTemplate
	 */
	loadComponent : function(ns, rootPath) {
		var path = ns.replace(/\./g, "\/");
		path += '\.js';
		Util.loadJs(rootPath + path);
	},
	// 缓存数据用，可以随意使用
	cache : {},
	component : {},
	/**
	 * 替换console.log (如果你使用的是FF 3.5则直接调用console.log就可以了)
	 * 
	 * @param {}
	 *            sign
	 * @param {}
	 *            arg
	 */
	log : function(sign, arg) {
		if (sign) {
			console.log(sign + ':', arg);
		} else {
			console.log(arg);
		}
	},
	/**
	 * 产生若干不重复的Id
	 * 
	 * @param {}
	 *            names : ['a','b','c']
	 * @return {}: [a:'ext-123',b:'ext-456',c:'ext-789']
	 */
	createIdJson : function(names) {
		var obj = {};
		for (var i = 0; i < names.length; i++) {
			obj[names[i]] = Ext.id();
		}
		return obj;
	},
	idCollection : [],
	// 获得屏幕尺寸
	ViewSize : Ext.getDoc().ViewSize,
	/**
	 * 用于对次序要求严格的Ajax回调
	 * 
	 * @example
	 * Ext.apply(Util.ajaxCache, {
				readyCount : 3,
				callBack : function() {
					center.store.loadData(Util.dataCache.psrListData)
				}
			})
	 * 
	 * use this to reduce readyCount :  Util.ajaxCache.setReady();
	 * @type
	 */
	ajaxCache : {
		readyCount : 1,// 设置ajax缓冲次数
		setCount : function() {// 用这个方法往下减数
			if (this.readyCount == 1) {
				this.callBack();
			} else {
				this.countDown();
				return this.readyCount--;
			}
		},
		callBack : Ext.emptyFn,
		countDown : Ext.emptyFn
	},
	/**
	 * 初始化方法，详细说明见Util的说明
	 */
	initAutoForm : function() {
		/**
		 * 错误信息显示在输入框下方;当allowBlank为false，并且只输入空格时，提示错误信息;当时去焦点时，进行trim()
		 */
		Ext.override(Ext.form.TextField, {
					msgTarget : 'qtip',
					trim : true,
					validator : function(s) {
						if (this.trim)
							if (!this.allowBlank && s.trim() == '') {
								return false;
							}
						return true;
					},
					onBlur : function() {
						if (this.trim && typeof this.getValue() == 'string')
							this.setValue(this.getValue().trim());
						Ext.form.TextField.superclass.onBlur.call(this);
					}
				})
		/**
		 * make form.isValid(preventMark) available default false
		 */
		Ext.override(Ext.form.BasicForm, {
					isValid : function(preMark) {
						var valid = true;
						this.items.each(function(f) {
									if (!f.isValid(preMark)) {
										valid = false;
									}
								});
						return valid;
					}
				})
	},
	// 一颗红星,用于标记必选字段
	markStar : '<span style="color:red" title="必填字段">*</span>',
	markIndex : '<span style="color:blue" title="可索引字段"><img height=10 src='+ rootPath + '/images/key.gif></span>',
	/**
	 * 输入store返回适用于该store的record
	 * 
	 * @param {}
	 *            store
	 * @return {}
	 */
	createRecordByStore : function(store, json) {
		var fields = store.fields.keys
		var rec = {};
		for (var i = 0; i < fields.length; i++) {
			rec[fields[i]] = json[fields[i]] || '';
		}
		return new store.recordType(rec);
	},
	/**
	 * 把Rest返回的包含List<JsonObject>型数据的response转换成固定格式的List
	 * 
	 * @param {}
	 *            response
	 * @return []
	 */
	readListFromResponse : function(response) {
		var res = eval("(" + response.responseText + ")");
		if (res == null)
			return [];
		for (var header in res) {
			var body = res[header];
			if (body[0] == undefined) {// 如果body中正好有个属性叫0，就会出错，可以换成length
				return [body];
			} else {
				var arr = [];
				for (var j = 0; j < body.length; j++) {
					arr.push(body[j]);
				}
				return arr;
			}
			console.error('无法读取response', header, response);
			break;
		}
	},
	/*
	 * 读取如下类型数据 com.jl.demo.model.Material@9a11c0[ uuid=<null> code=<null> ]
	 * 并转成js对象 @param : ajax response @return : 对象，带有包名的对象名
	 */
	javaObjReader : function(response) {
		var a = response.responseText;
		a = a.replace(/\>\s/g, '\>,');
		a = a.replace(/\[/, '{');
		a = a.replace(/\]/, '}');
		a = a.replace(/\</g, '\"');
		a = a.replace(/\>/g, '\"');
		a = a.replace(/\=/g, '\:');
		var a_arr = a.split('@');
		var fullName = a_arr[0];
		var fn_arr = fullName.split('.');
		var key = fn_arr[fn_arr.length - 1];
		var inx1 = a_arr[1].indexOf('{');
		var value = a_arr[1].substring(inx1);
		var finalStr = key + '=' + value;
		var obj = eval("(" + finalStr + ")");
		return [obj, key, fullName];
	},
	lowerCaseModel : function(o) {
		for (var i in o) {
			var t = o[i];
			delete o[i];
			o[i.toLowerCase()] = t;
		}
		return o;
	},
	model2Store : function(model, data) {
		var fields = [model.pk];
		var attr = model.attr;
		for (var i in attr) {
			if (typeof attr[i] == 'object')
				fields.push(i);
		}
		return new Ext.data.JsonStore({
					id : model.pk,
					fields : fields,
					data : data || []
				})
	},
	formBlankField : {
		xtype : 'displayfield',
		fieldLabel : '&nbsp;',
		labelSeparator : ''
	},
	renderer : {
		enable : function(val) {
			val = "" + val;
			switch (val) {
				case '0' :
					return '禁用';
				case '1' :
					return '启用';
				default :
					return ''
			}
		}
	},
   rendererYN : {
		enable : function(val) {
			val = "" + val;
			switch (val) {
				case 'Y' :
					return '是';
				case 'N' :
					return '否';
				default :
					return ''
			}
		}
	},
	log4IE : function(values) {
		myWindow = window.open('', '', 'width=600,height=600');
		myWindow.document.body.innerHTML = Ext.encode(values);
	},
	showMsg : function(msg, title, option) {
		var option = option || {}
		if (option.type == 1)
			DelayMessage.show(title || '系统信息', msg);
	}
}