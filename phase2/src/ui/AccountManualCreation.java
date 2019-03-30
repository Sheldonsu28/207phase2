package ui;

import account.Account;
import account.ChequingAccount;
import atm.BankManager;
import atm.User;

import javax.swing.*;
import java.awt.*;

public class AccountManualCreation extends SubMenu {
    private JComboBox<User> userSelection;
    private JComboBox<Class<Account>> accountTypeSelection;
    private JCheckBox primaryCheck;
    private JButton submitButton;

    @SuppressWarnings("unchecked")
    AccountManualCreation(BankManager manager) {
        super("Account Manual Creation");

        userSelection = getUserSelectionBox(manager.getAllUsers());

        accountTypeSelection = new JComboBox<>(Account.OWNABLE_ACCOUNT_TYPES);
        accountTypeSelection.addActionListener(e -> {
            Class selected = (Class) accountTypeSelection.getSelectedItem();

            primaryCheck.setSelected(false);

            if (selected == ChequingAccount.class) {
                primaryCheck.setEnabled(true);
            } else {
                primaryCheck.setEnabled(false);
            }
        });

        primaryCheck = new JCheckBox("isPrimary");

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            User owner = (User) userSelection.getSelectedItem();
            Class<Account> accountType = (Class<Account>) accountTypeSelection.getSelectedItem();
            boolean isPrimary = primaryCheck.isSelected();

            if (owner != null && accountType != null) {

                manager.createAccount(owner.getUserName(), accountType, isPrimary);

                JOptionPane.showMessageDialog(this, "Account successfully created!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                AccountManualCreation.this.dispose();

            } else {
                JOptionPane.showMessageDialog(this, "You have unselected item!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        initializeLayout();

        setBounds(100, 100, 600, 300);
        setVisible(true);
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(5);
        flowLayout.setHgap(10);

        JPanel userSelectionPanel = new JPanel(flowLayout);
        userSelectionPanel.add(new JLabel("Owner: "));
        userSelectionPanel.add(userSelection);

        JPanel accountTypeSelectionPanel = new JPanel(flowLayout);
        accountTypeSelectionPanel.add(new JLabel("Account type: "));
        accountTypeSelectionPanel.add(accountTypeSelection);
        accountTypeSelectionPanel.add(primaryCheck);

        JPanel infoPanel = new JPanel(flowLayout);
        infoPanel.add(userSelectionPanel);
        infoPanel.add(accountTypeSelectionPanel);

        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.add(submitButton);

        Box box = Box.createVerticalBox();
        box.add(infoPanel);
        box.add(Box.createVerticalStrut(10));
        box.add(submitPanel);

        container.add(box);
    }


}
