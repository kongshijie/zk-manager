StaticTextField = function(config) {
	var config = config || {};
	Ext.apply(config, {
				readOnly : true,
				disabled : true,
				cls : 'staticfield'
			});
	StaticTextField.superclass.constructor.call(this, config);
}
StaticTextField = Ext.extend(StaticTextField, Ext.form.TextField, {});

Ext.reg('statictextfield', StaticTextField);
