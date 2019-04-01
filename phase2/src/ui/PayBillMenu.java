package ui;

import account.BillingAccount;
import account.Withdrawable;
import atm.BankManager;
import atm.User;
import transaction.PayBillTransaction;

import javax.swing.*;
import java.util.LinkedHashMap;

class PayBillMenu extends SubMenu {

    private final JComboBox<BillingAccount> payeeSelection;
    private final JComboBox<Withdrawable> fromAccountSelection;
    private final JTextField amountField;
    private final JButton submitButton;

    PayBillMenu(BankManager manager, User user) {
        super("Pay Bill Menu");

        payeeSelection = new JComboBox<>(manager.getPayeeList().toArray(new BillingAccount[0]));
        fromAccountSelection = new JComboBox<>(
                user.getAccountListOfType(Withdrawable.class).toArray(new Withdrawable[0]));
        amountField = getPositiveTwoDecimalOnlyField();

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String amountText = amountField.getText();
            Withdrawable fromAccount = (Withdrawable) fromAccountSelection.getSelectedItem();
            BillingAccount payee = (BillingAccount) payeeSelection.getSelectedItem();
            PayBillTransaction transaction;

            if (fromAccount != null && payee != null && !amountText.equals("")) {
                transaction = new PayBillTransaction(user, fromAccount, payee, Double.parseDouble(amountText));
            } else {
                MainFrame.showErrorMessage("Account not selected or invalid/missing amount!");
                return;
            }

            if (transaction.perform()) {
                MainFrame.showInfoMessage("Bill Payment successful!\n" + transaction, "Success");
            } else {
                MainFrame.showErrorMessage("Deposit failed! Something went wrong.");
            }

            fromAccountSelection.updateUI();
            payeeSelection.updateUI();
        });

        defaultRowsLayout(new LinkedHashMap<JComponent, String>() {{
            put(fromAccountSelection, "From Account: ");
            put(payeeSelection, "To Payee: ");
            put(amountField, "Payment Amount: ");
            put(submitButton, null);
        }});

        setVisible(true);
    }
}
