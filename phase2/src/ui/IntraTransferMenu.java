package ui;

import account.Depositable;
import account.Withdrawable;
import atm.User;
import transaction.TransferTransaction;

import javax.swing.*;
import java.util.LinkedHashMap;

class IntraTransferMenu extends SubMenu {

    private final JComboBox<Withdrawable> fromAccountSelection;
    private final JComboBox<Depositable> toAccountSelection;
    private final JTextField amountField;
    private final JButton submitButton;
    private final User user;

    IntraTransferMenu(User user) {
        super("Intra-User Transfer Menu");

        this.user = user;

        fromAccountSelection = new JComboBox<>(
                user.getAccountListOfType(Withdrawable.class).toArray(new Withdrawable[0]));

        fromAccountSelection.addActionListener(e -> updateSelection());

        toAccountSelection = new JComboBox<>();

        updateSelection();

        amountField = getPositiveTwoDecimalOnlyField();

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            Withdrawable from = (Withdrawable) fromAccountSelection.getSelectedItem();
            Depositable to = (Depositable) toAccountSelection.getSelectedItem();
            TransferTransaction transaction;
            String amountText = amountField.getText();

            if (from != null && to != null && !amountText.equals("")) {
                transaction = new TransferTransaction(user, from, to, Double.parseDouble(amountText));
            } else {
                MainFrame.showErrorMessage("Missing information! Unselected attribute detected!");
                return;
            }

            if (transaction.perform()) {
                MainFrame.showInfoMessage("Transfer succeeded!\n" + transaction, "Success");
            } else {
                MainFrame.showErrorMessage("Transfer failed! Something went wrong!");
            }

            toAccountSelection.updateUI();
            fromAccountSelection.updateUI();
        });

        defaultRowsLayout(new LinkedHashMap<JComponent, String>() {{
            put(fromAccountSelection, "From Account: ");
            put(toAccountSelection, "To Account: ");
            put(amountField, "Transfer Amount: ");
            put(submitButton, null);
        }});

        setBounds(100, 100, 600, 400);
        setMinimumSize(getSize());
        setVisible(true);
    }

    private void updateSelection() {
        Withdrawable fromAccount = (Withdrawable) fromAccountSelection.getSelectedItem();
        toAccountSelection.removeAllItems();

        for (Depositable account : user.getAccountListOfType(Depositable.class)) {
            if (account != fromAccount) {
                toAccountSelection.addItem(account);
            }
        }

        toAccountSelection.updateUI();
    }
}
