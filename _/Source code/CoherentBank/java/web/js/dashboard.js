// Create new Account dialog
function responseHandlerGenerator(parentWindow, checkField) {
    return function (form, action) {
        if (action.result) {
            if (!checkField || action.result[checkField])
                Ext.getCmp('accounts-grid').getStore().reload();
            if (action.result.error)
                Ext.Msg.alert('Failure', action.result.error);
        }
        else {
            switch (action.failureType) {
                case Ext.form.Action.CLIENT_INVALID:
                    Ext.Msg.alert('Failure', 'Invalid values');
                    break;
                case Ext.form.Action.CONNECT_FAILURE:
                    Ext.Msg.alert('Failure', 'Connection failure');
                    break;
                case Ext.form.Action.SERVER_INVALID:
                   Ext.Msg.alert('Failure', action.result.error || 'Internal Server Error');
            }
        }
        parentWindow.close();
    };
}

var dlgCreateAccount = {
	getConfig: function () {
        return {
            id: 'create-account-window',
            layout: 'fit',
            width: 360,
            height: 200,
            plain: true,
            title: 'Open account',
            listeners: {
                beforehide: { fn: function () { Ext.getBody().unmask(); }, scope: this },
                beforeshow: { fn: function () { Ext.getBody().mask(); }, scope: this }
            },
            items: {
                id: 'create-account-form',
                xtype: 'form',
                layout: 'form',
                labelWidth: 100,
                border: false,
                bodyStyle: 'padding: 5px 5px 0',
                autoWidth: true,
                defaults: { anchor: '0' },
                defaultType: 'textfield',
                items: [{
                        fieldLabel: 'Currency',
                        name: 'currency',
                        xtype: 'combo',
                        store: currencyList,
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        emptyText: '',
                        selectOnFocus: true,
                        value: 'EUR',
                        allowBlank: false
                    }, {
                        fieldLabel: 'Initial deposit',
                        name: 'initialDeposit',
                        xtype: 'numberfield'
                    }, {
                        fieldLabel: 'Description',
                        name: 'description',
                        xtype: 'textarea'
                    }],

                buttons: [{
                    text: 'Create',
                    type: 'submit',
                    handler: function () {
                        var responseHandler = responseHandlerGenerator(Ext.getCmp('create-account-window'), 'account');
                        Ext.getCmp('create-account-form').getForm().submit({
                            method: 'post',
                            url: 'account',
                            success: responseHandler,
                            failure: responseHandler
                        });
                    }
                }, {
                    text: 'Clear',
                    type: 'reset',
                    handler: function () { Ext.getCmp('create-account-form').getForm().reset(); }
                }]
            }
        };
    },
	show: function (parent) {
        var win = new Ext.Window(this.getConfig());
		win.show(parent);
	}
};
// --

// Make Payment Dialog
var dlgMakePayment = {
	getConfig: function (account) {
        return {
            id: 'make-payment-window',
            layout: 'fit',
            width: 300,
            height: 250,
            plain: true,
            title: 'Pay bill',
            listeners: {
                beforehide: { fn: function () { Ext.getBody().unmask(); }, scope: this },
                beforeshow: { fn: function () { Ext.getBody().mask(); }, scope: this }
            },
            items: {
                id: 'make-payment-form',
                xtype: 'form',
                layout: 'form',
                labelWidth: 100,
                border: false,
                bodyStyle: 'padding: 5px 5px 0',
                autoWidth: true,
                defaults: { anchor: '0' },
                defaultType: 'textfield',
                items: [{
                        fieldLabel: 'Account',
                        name: 'account',
                        value: account.description,
                        disabled: true
                    }, {
                        fieldLabel: 'Payee',
                        name: 'payee',
                        allowBlank: false
                    }, {
                        fieldLabel: 'Description',
                        name: 'description',
                        xtype: 'textarea',
                        allowBlank: false
                    }, {
                        xtype: 'panel',
                        layout: 'column',
                        border: false,
                        items: [{
                            layout: 'form',
                            columnWidth: 0.8,
                            border: false,
                            items: {
                                fieldLabel: 'Amount',
                                xtype: 'numberfield',
                                name: 'amount',
                                allowBlank: false,
                                anchor: '-2'
                            }
                        }, {
                            layout: 'form',
                            columnWidth: 0.2,
                            border: false,
                            items: {
                                id: 'make-payment-currency',
                                hideLabel: true,
                                name: 'currency',
                                xtype: 'combo',
                                anchor: '0',
                                store: currencyList,
                                typeAhead: true,
                                mode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                emptyText: '',
                                selectOnFocus: true,
                                value: account.currency
                            }
                        }]
                    }],

                buttons: [{
                    text: 'Pay',
                    type: 'submit',
                    handler: function () {
                        var responseHandler = responseHandlerGenerator(Ext.getCmp('make-payment-window'), 'account');
                        Ext.getCmp('make-payment-form').getForm().submit({
                            method: 'post',
                            url: 'account/' + account.id + '/billpayment',
                            success: responseHandler,
                            failure: responseHandler
                        });
                    }
                }, {
                    text: 'Clear',
                    type: 'reset',
                    handler: function () { Ext.getCmp('make-payment-form').getForm().reset(); }
                }]
            }
        };
    },
	show: function (parent) {
        var selModel = Ext.getCmp('accounts-grid').getSelectionModel();
        if (!selModel.hasSelection())
            return;

        var win = new Ext.Window(this.getConfig(selModel.getSelected().data));
		win.show(parent);
	}
};
// --

var dlgViewTransactions = {
	getConfig: function (account) {
        return {
            layout: 'fit',
            width: 720,
            height: 360,
            plain: true,
            iconCls: 'icon-table',
            title: 'Transactions: ' + account.description,
            listeners: {
                beforehide: { fn: function () { Ext.getBody().unmask(); }, scope: this },
                beforeshow: { fn: function () { Ext.getBody().mask(); }, scope: this }
            },
            items: {
                xtype: 'grid',
                id: 'transactions-grid',
                border: false,
                frame: false,
                autoScroll: true,
                height: '100%',
                autoWidth: true,
                store: (function () {
                    var store = new Ext.data.Store({
                        id: 'transactions',
                        restful: true,
                        storeId: 'transactionsStore',
                        proxy: new Ext.data.HttpProxy({
                            url: 'transactions/' + account.id,
                            listeners: {
                                'beforeload': {
                                    fn: function (proxy, params) {
                                        params['from'] = Ext.util.Format.date(Ext.getCmp('filter-from-date').getValue(), 'Y-m-d');
                                        params['to'] = Ext.util.Format.date(Ext.getCmp('filter-to-date').getValue(), 'Y-m-d');
                                    },
                                    scope: this
                                }
                            }
                        }),
                        listeners: {
                            load: {
                            fn: function (s, recs, opts) {
                                    Ext.getCmp('transactions-grid-total').setText('Displaying ' + recs.length + ' transaction(s).')
                                },
                                scope: this
                            }
                        },
                        reader: new Ext.data.JsonReader({
                            idProperty: 'transactionNumber',
                            root: 'transactions'
                        }, [{ name: 'id', convert: function (v) { return [v.accountId, v.transactionNumber].join(',') } },
                            { name: 'transactionNumber', mapping: 'id.transactionNumber' },
                            { name: 'time', convert: function (v) { return Ext.util.Format.date(new Date(v), 'M d, Y'); } },
                            { name: 'description' },
                            { name: 'withdrawal', convert: function (v, rec) {
                                return ('WITHDRAWAL' == rec.type.toUpperCase()) ? (Ext.util.Format.number(rec.amount.amount, '0,000.00')) : ''; }
                            },
                            { name: 'deposit', convert: function (v, rec) {
                                return ('DEPOSIT' == rec.type.toUpperCase()) ? (Ext.util.Format.number(rec.amount.amount, '0,000.00')) : ''; }
                            },
                            { name: 'balance', convert: function (v) { return Ext.util.Format.number(v.amount, '0,000.00') + ' (' + v.currency + ')'; }}
                        ])
                    });

                    return store;
                })(),
                columns: [
                    { header: "#", width: 30, sortable: true, dataIndex: 'transactionNumber'},
                    { header: "Date", width: 100, sortable: true, dataIndex: 'time'},
                    { header: "Description", width: 300, sortable: true, dataIndex: 'description' },
                    { header: "Withdrawal", width: 90, sortable: true, dataIndex: 'withdrawal', align: 'right' },
                    { header: "Deposit", width: 90, sortable: true, dataIndex: 'deposit', align: 'right' },
                    { header: "Balance", width: 120, sortable: true, dataIndex: 'balance', align: 'right' }
                ],
                bbar: [{
                        xtype: 'label',
                        html: '<strong>Filter:</strong>&nbsp;'
                    }, {
                        id: 'filter-from-date',
                        xtype: 'datefield',
                        value: new Date(new Date() - 30*86400000)
                    }, {
                        xtype: 'label',
                        html: '&nbsp;to:&nbsp;'
                    }, {
                        id: 'filter-to-date',
                        xtype: 'datefield',
                        value: new Date()
                    }, {
                        text: 'Apply',
                        xtype: 'button',
                        handler: function () { Ext.getCmp('transactions-grid').getStore().reload(); }
                    }, '->', {
                        id: 'transactions-grid-total',
                        xtype: 'label',
                        text: ''
                    }
                ],
                viewConfig: { forceFit: true },
                listeners: { render: { fn: function (grid) {
                            grid.store.load();
                        }
                    }
                }
            }
        };
	},
	show: function (parent) {
        var selModel = Ext.getCmp('accounts-grid').getSelectionModel();
        if (!selModel.hasSelection())
            return;

        var win = new Ext.Window(this.getConfig(selModel.getSelected().data));
		win.show(parent);
	}
};

var dlgEditCustomer = {
	getConfig: function () {
        return {
            id: 'edit-customer-window',
            layout: 'fit',
            width: 360,
            height: 300,
            plain: true,
            title: 'Edit Profile',
            iconCls: 'icon-user-edit',
            listeners: {
                beforehide: { fn: function () { Ext.getBody().unmask(); }, scope: this },
                beforeshow: { fn: function () { Ext.getBody().mask(); }, scope: this },
                afterrender: { fn: function (cmp) {
                    var formContainer = Ext.getCmp('edit-customer-form');
                    formContainer.getEl().mask('Loading', 'x-mask-loading');

                    var store = new Ext.data.JsonStore({
                        autoLoad: true,
                        autoDestroy: true,
                        url: 'profile',
                        method: 'get',
                        fields: ['id', 'name', 'email', 'address.street', 'address.city',
                                'address.postalCode', 'address.state', 'address.country'],
                        idProperty: 'id',
                        root: 'customer',
                        listeners: {
                            load: {
                                fn: function (s, recs, opts) {
                                    formContainer.getForm().loadRecord(store.getAt(0));
                                    formContainer.getEl().unmask();
                                },
                                scope: this
                            },
                            exception: {
                                fn: function () {
                                    Ext.Msg.alert('Failure', 'Failed to load profile');
                                    Ext.getCmp('edit-customer-window').hide();
                                }
                            }
                        }
                    });
                }, scope: this }
            },
            items: {
                id: 'edit-customer-form',
                xtype: 'form',
                layout: 'form',
                labelWidth: 100,
                border: false,
                bodyStyle: 'padding: 5px 5px 0',
                autoWidth: true,
                defaults: { anchor: '0' },
                defaultType: 'textfield',
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
                }],

                buttons: [{
                    text: 'Save',
                    type: 'submit',
                    handler: function () {
                        var responseHandler = responseHandlerGenerator(Ext.getCmp('edit-customer-window'), 'customer');
                        Ext.getCmp('edit-customer-form').getForm().submit({
                            method: 'post',
                            url: 'profile',
                            success: responseHandler,
                            failure: responseHandler
                        });
                    }
                }, {
                    text: 'Clear',
                    type: 'reset',
                    handler: function () { Ext.getCmp('edit-customer-form').getForm().reset(); }
                }]
            }
        };
    },
	show: function (parent) {
        var win = new Ext.Window(this.getConfig());
		win.show(parent);
	}
};

// Account Grid
var accountGrid = function () {
	function updateButtonState(selModel) {
		var isSelected = selModel.hasSelection();
		Ext.getCmp('view-transactions-button').setDisabled(!isSelected);
        Ext.getCmp('make-payment-button').setDisabled(!isSelected);
	}
	
	var gridSelectionModel = new Ext.grid.CheckboxSelectionModel({
			singleSelect: true,
			header: '',
			listeners: { selectionchange: { fn: updateButtonState } }
		});
		
	return new Ext.grid.GridPanel({
        iconCls: 'icon-table',
        id: 'accounts-grid',
        frame: true,
        title: 'Accounts',
        autoScroll: true,
        height: '100%',
		autoWidth: true,
        store: new Ext.data.JsonStore({
            autoDestroy: true,
            autoLoad: true,
            url: 'accounts',
            storeId: 'accountStore',
            root: 'accountList',
            idProperty: 'id',
            fields: ['id',
                'description',
                'currency',
                { name: 'balance', convert: function (v) { return Ext.util.Format.number(v.amount, '0,000.00') + ' (' + v.currency + ')'; }}
            ]
        }),
        columns: [
			gridSelectionModel,
			{ header: "Account", width: 100, sortable: true, dataIndex: 'id'},
			{ header: "Description", width: 300, sortable: true, dataIndex: 'description' },
            { header: "Currency", width: 60, sortable: true, dataIndex: 'currency' },
            { header: "Balance", width: 100, sortable: true, dataIndex: 'balance', align: 'right'}
		],
        tbar: [{
            text: 'Open Account',
            iconCls: 'icon-add',
            handler: function (obj, ev) { dlgCreateAccount.show(ev.target); }
        }, '-', {
            id: 'make-payment-button',
            text: 'Pay Bill',
            iconCls: 'icon-money',
            handler: function (obj, ev) { dlgMakePayment.show(ev.target); }
        }, '-', {
			id: 'view-transactions-button',
            text: 'View Transactions',
            iconCls: 'icon-view-detail',
            handler: function (obj, ev) { dlgViewTransactions.show(ev.target); }
        }, '->', {
			id: 'edit-customer-button',
            text: 'Edit Profile',
            iconCls: 'icon-user-edit',
            handler: function (obj, ev) { dlgEditCustomer.show(ev.target); }
        }],
        viewConfig: { forceFit: true },
		selModel: gridSelectionModel,
		listeners: { render: { fn: function (grid) {
                    updateButtonState(grid.getSelectionModel());
                }
            }
        }
	});
};
// --

Ext.onReady(function() {
	Ext.QuickTips.init();
	
	new Ext.Viewport({
		layout: 'fit', 
		items: accountGrid()
	});
});