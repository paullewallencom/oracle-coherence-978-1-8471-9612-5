Ext.onReady(function() {
	Ext.QuickTips.init();

    var submitOnEnterKey = function(field, event) {
            if (Ext.EventObject.ENTER == event.getKey())
                formPanel.getForm().submit();
            };

	var formPanel = new Ext.FormPanel({
        labelWidth: 75,
		iconCls: 'icon-login',
        url: 'login',
        frame: true,
        title: 'Login',
        bodyStyle: 'padding: 5px 5px 0',
        autoWidth: true,
        defaults: { anchor: '0' },
        defaultType: 'textfield',
		standardSubmit: true,
        items: [{
                fieldLabel: 'Username',
                name: 'username',
                allowBlank: false,
                listeners: { specialkey : submitOnEnterKey }
            }, {
                fieldLabel: 'Password',
                name: 'password',
				inputType: 'password',
                listeners: { specialkey : submitOnEnterKey }
            }],

        buttons: [{
            text: 'Login',
			type: 'submit',
			handler: function () { formPanel.getForm().submit() }
        }, {
            text: 'Clear',
			type: 'reset',
			handler: function () { formPanel.getForm().reset() }
        }],
		renderTo: Ext.get('form')
    });
});