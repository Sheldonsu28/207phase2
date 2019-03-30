package ui;

import atm.BankManager;
import atm.ExternalFiles;
import atm.FileHandler;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ManagerMainMenu extends MainMenu {
    private JButton toAlerts, toCreateUser, toAccountRequest, toCreateAccount, toCancelTransactions, toRestock;

    ManagerMainMenu(BankManager manager) {
        super(manager, "Manager Main Menu");

        initializeLayout(new JButton[]{toAlerts, toCreateUser, toAccountRequest, toCreateAccount,
                toCancelTransactions, toRestock});

        setVisible(true);
    }


    @Override
    void initializeButtons() {
        toAlerts = new JButton("Read Alerts");
        toCreateUser = new JButton("Create User");
        toAccountRequest = new JButton("Read Account Request");
        toCreateAccount = new JButton("Create Account");
        toCancelTransactions = new JButton("Cancel Transactions");
        toRestock = new JButton("Restock Machine");

        ActionListener listener = e -> {
            JButton source = (JButton) e.getSource();
            ManagerMainMenu.this.setVisible(false);

            if (source == toAlerts) {
                JOptionPane.showMessageDialog(this, getFormattedMessage(ExternalFiles.CASH_ALERT_FILE),
                        "Alerts", JOptionPane.INFORMATION_MESSAGE);

            } else if (source == toCreateUser) {
                new UserCreationMenu(manager);

            } else if (source == toAccountRequest) {
                JOptionPane.showMessageDialog(this,
                        getFormattedMessage(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE),
                        "Account Creation Requests", JOptionPane.INFORMATION_MESSAGE);

            } else if (source == toCreateAccount) {
                int choice = getAccountCreationMethod(new String[]{"From Request File", "Manual"});

                switch (choice) {
                    case 0:
                        new AccountRequestedCreation(manager);
                        break;

                    case 1:
                        new AccountManualCreation(manager);
                        break;

                    default:
                        throw new IllegalStateException("Unregistered account creation choice type!");
                }

            } else if (source == toCancelTransactions) {

                new CancelTransactionMenu(manager);

            } else if (source == toRestock) {
                new RestockMenu(manager);
            }

            ManagerMainMenu.this.setVisible(true);
        };

        toAlerts.addActionListener(listener);
        toCreateUser.addActionListener(listener);
        toAccountRequest.addActionListener(listener);
        toCreateAccount.addActionListener(listener);
        toCancelTransactions.addActionListener(listener);
        toRestock.addActionListener(listener);
    }

    private String getFormattedMessage(ExternalFiles file) {
        StringBuilder msg = new StringBuilder();

        for (String info : new FileHandler().readFrom(file)) {
            msg.append(info).append("\n");
        }

        return msg.toString();
    }

    private int getAccountCreationMethod(String[] options) {

        return JOptionPane.showOptionDialog(this, "Choose how to create account",
                "Account Creation Method", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
    }

}
