package ui;

import account.Depositable;
import account.Withdrawable;
import atm.BankManager;
import atm.User;
import transaction.TransferTransaction;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.List;

class InterTransferMenu extends SubMenu {

    private final JButton submitButton;
    private JComboBox<User> toUserSelection;
    private JComboBox<Withdrawable> fromAccountSelection;
    private JComboBox<Depositable> toAccountSelection;
    private final JTextField amountField;
    private final User fromUser;
    private final BankManager manager;

    InterTransferMenu(BankManager manager, User user) {
        super("Inter-User Transfer Menu");

        fromUser = user;
        this.manager = manager;

        initializeSelections();

        amountField = getPositiveTwoDecimalOnlyField();

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            Withdrawable from = (Withdrawable) fromAccountSelection.getSelectedItem();
            Depositable to = (Depositable) toAccountSelection.getSelectedItem();
            User toUser = (User) toUserSelection.getSelectedItem();
            String amountText = amountField.getText();

            TransferTransaction transaction;

            if (from != null && to != null && toUser != null && !amountText.equals("")) {
                transaction = new TransferTransaction(
                        fromUser, from, toUser, to, Double.parseDouble(amountText));
            } else {
                MainFrame.showErrorMessage("Missing information! Unselected attribute detected!");
                return;
            }

            if (transaction.perform()) {
                MainFrame.showInfoMessage("Transfer succeeded!", "Success");
            } else {
                MainFrame.showErrorMessage("Transfer failed! Something went wrong!");
            }

            toAccountSelection.updateUI();
            fromAccountSelection.updateUI();
            toUserSelection.updateUI();
        });

        defaultRowsLayout(new LinkedHashMap<JComponent, String>() {{
            put(new JLabel("From user: " + fromUser), null);
            put(fromAccountSelection, "From account: ");
            put(toUserSelection, "To user: ");
            put(toAccountSelection, "To account: ");
            put(amountField, "Transfer Amount: ");
            put(submitButton, null);
        }});

        setBounds(100, 100, 600, 400);
        setMinimumSize(getSize());
        setVisible(true);
    }

    private void initializeSelections() {
        fromAccountSelection = new JComboBox<>(
                fromUser.getAccountListOfType(Withdrawable.class).toArray(new Withdrawable[0]));

        List<User> userList = manager.getAllUsers();
        userList.remove(fromUser);
        toUserSelection = new JComboBox<>(userList.toArray(new User[0]));

        toUserSelection.addActionListener(e -> {
            User selectedUser = (User) toUserSelection.getSelectedItem();
            toAccountSelection.removeAllItems();

            if (selectedUser != null) {
                for (Depositable account : selectedUser.getAccountListOfType(Depositable.class)) {
                    toAccountSelection.addItem(account);
                }
                toAccountSelection.setSelectedItem(selectedUser.getPrimaryAccount());
            }

        });

        User defaultUser = (User) toUserSelection.getSelectedItem();
        if (defaultUser != null) {
            toAccountSelection = new JComboBox<>(
                    defaultUser.getAccountListOfType(Depositable.class).toArray(new Depositable[0]));
        } else {
            toAccountSelection = new JComboBox<>();
        }
    }
}
