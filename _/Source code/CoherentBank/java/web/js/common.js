if ('data' != Ext.BLANK_IMAGE_URL.substr(0, 4))
	Ext.BLANK_IMAGE_URL = '../ext/resources/images/default/s.gif';

Ext.apply(Ext.form.VTypes, {
		password: function(val, field) {
			if (field.initialPassField) {
				var pwd = Ext.getCmp(field.initialPassField);
				return (!pwd || val == pwd.getValue());
			}
			return true;
		},
		passwordText: 'Passwords do not match!'
	});	

var currencyList = [
    ['USD', 'USD', 'U.S. Dollar'],
	['EUR', 'EUR', 'Euro'],
	['GBP', 'GBP', 'Great Britain Pound'],
	['AUD', 'AUD', 'Australian Dollar'],
	['CAD', 'CAD', 'Canadian Dollar'],
	['CHF', 'CHF', 'Swiss Franc'],
	['JPY', 'JPY', 'Japanese Yen']
];