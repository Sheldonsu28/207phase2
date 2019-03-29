package ui;

import atm.BankManager;

import javax.swing.*;

public class ManagerMainMenu extends ChoiceMenu {
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

        toAlerts.addActionListener(e -> {

        });

        toCreateUser.addActionListener(e -> {

        });

        toAccountRequest.addActionListener(e -> {

        });

        toCreateAccount.addActionListener(e -> {

        });

        toCancelTransactions.addActionListener(e -> {

        });

        toRestock.addActionListener(e -> {

        });
    }
}
