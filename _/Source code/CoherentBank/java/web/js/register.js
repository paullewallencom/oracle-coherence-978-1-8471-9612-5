Ext.onReady(function() {
	
	Ext.QuickTips.init();
		
	var formPanel = new Ext.FormPanel({
        labelWidth: 120,
		iconCls: 'icon-register',
        url: 'register',
        frame: true,
        title: 'Register',
        bodyStyle: 'padding: 5px 5px 0',
        autoWidth: true,
        defaults: { anchor: '0' },
        defaultType: 'textfield',
		standardSubmit: true,
        items: [{
			xtype: 'fieldset',
			title: 'Contact information',
			autoHeight: true,
			labelWidth: 110,
			anchor: '0',
			defaults: { anchor: '0' },
			defaultType: 'textfield',
			items: [{
				fieldLabel: 'Full name',
                name: 'name',
                allowBlank: false
			}, {
                fieldLabel: 'Email',
                name: 'email',
                allowBlank: false,
				vtype: 'email'
            }, {
				fieldLabel: 'Street',
				name: 'address.street'
			}, {
				fieldLabel: 'City',
				name: 'address.city'
			}, {
				fieldLabel: 'Postal code',
				name: 'address.postalCode'
			}, {
				fieldLabel: 'State',
				name: 'address.state'
			}, {
				fieldLabel: 'Country',
				name: 'address.country'
			}]
		}, {
			xtype: 'fieldset',
			title: 'Credentials',
			autoHeight: true,
			labelWidth: 110,
			anchor: '0',
			defaults: { anchor: '0' },
			defaultType: 'textfield',
			items: [{
                fieldLabel: 'Username',
                name: 'username',
                allowBlank: false
            }, {
                fieldLabel: 'Password',
				id: 'password',
                name: 'password',
				inputType: 'password',
				allowBlank: false
            }, {
                fieldLabel: 'Repeat password',
                name: 'password2',
				inputType: 'password',
				vtype: 'password',
				initialPassField: 'password',
				allowBlank: false
            }]
		}],
		
        buttons: [{
            text: 'Create',
			type: 'submit',
			handler: function () { formPanel.getForm().submit(); }
        }, {
            text: 'Clear',
			type: 'reset',
			handler: function () { formPanel.getForm().reset(); }
        }],
		
		renderTo: Ext.get('container')
    });
});