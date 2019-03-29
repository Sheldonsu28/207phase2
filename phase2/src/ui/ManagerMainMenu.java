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
                JOptionPane.showMessageDialog(this,
                        new FileHandler().readFrom(ExternalFiles.CASH_ALERT_FILE), "Alerts",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (source == toCreateUser) {
                new UserCreationMenu(manager, this);
            } else if (source == toAccountRequest) {

            } else if (source == toCreateAccount) {

            } else if (source == toCancelTransactions) {

            } else if (source == toRestock) {

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

}
