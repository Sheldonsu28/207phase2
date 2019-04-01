package ui;

import atm.BankManager;
import atm.ExternalFiles;
import atm.FileHandler;

import javax.swing.*;
import java.awt.event.ActionListener;

class ManagerMainMenu extends MainMenu {
    private JButton toAlerts, toCreateUser, toAccountRequest, toCreateAccount, toCancelTransactions, toRestock,
            toJoinAccount;

    ManagerMainMenu(BankManager manager, LoginType loginType) {
        super(manager, "Manager Main Menu");

        initializeLayout(new JButton[]{toAlerts, toCreateUser, toAccountRequest, toCreateAccount,
                toCancelTransactions, toRestock, toJoinAccount});

        if (loginType == LoginType.EMPLOYEE) {
            toCreateAccount.setEnabled(false);
            toCreateUser.setEnabled(false);
        } else if (loginType != LoginType.MANAGER) {
            throw new IllegalArgumentException("Can not access manager menu if you do not have manager access!");
        }

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
        toJoinAccount = new JButton("Join Accounts");

        ActionListener listener = e -> {
            JButton source = (JButton) e.getSource();
            ManagerMainMenu.this.setVisible(false);

            if (source == toAlerts) {
                MainFrame.showInfoMessage(getFormattedMessage(ExternalFiles.CASH_ALERT_FILE), "Alerts");

            } else if (source == toCreateUser) {
                new UserCreationMenu(manager);

            } else if (source == toAccountRequest) {
                MainFrame.showInfoMessage(getFormattedMessage(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE),
                        "Account Creation Requests");

            } else if (source == toCreateAccount) {
                int choice = getBranchChoice("Choose creation method", "Creation Method",
                        new String[]{"From Request File", "Manual"});

                switch (choice) {
                    case 0:
                        new AccountRequestedCreation(manager);
                        break;

                    case 1:
                        new AccountManualCreation(manager);
                        break;
                }

            } else if (source == toCancelTransactions) {
                new CancelTransactionMenu(manager);

            } else if (source == toRestock) {
                new RestockMenu(manager);

            } else if (source == toJoinAccount) {
                new JoinAccountMenu(manager);

            }

            ManagerMainMenu.this.setVisible(true);
        };

        toAlerts.addActionListener(listener);
        toCreateUser.addActionListener(listener);
        toAccountRequest.addActionListener(listener);
        toCreateAccount.addActionListener(listener);
        toCancelTransactions.addActionListener(listener);
        toRestock.addActionListener(listener);
        toJoinAccount.addActionListener(listener);
    }

    private String getFormattedMessage(ExternalFiles file) {
        StringBuilder msg = new StringBuilder();

        for (String info : new FileHandler().readFrom(file)) {
            msg.append(info).append("\n");
        }

        return msg.toString();
    }

}
