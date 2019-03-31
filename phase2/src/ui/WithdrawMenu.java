package ui;

import account.Withdrawable;
import atm.AtmMachine;
import atm.BankManager;
import atm.User;
import transaction.WithdrawTransaction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;


public class WithdrawMenu extends SubMenu {
    private JComboBox<Withdrawable> accountSelection;
    private JButton confirmationButton;
    private JTextField userInputAmount;
    private AtmMachine machine;

    WithdrawMenu(BankManager manager, User user) {
        super("Account Selection");

        machine = manager.getMachineList().get(0);

        accountSelection = new JComboBox<>(user.getAccountListOfType(Withdrawable.class).toArray(new Withdrawable[0]));

        userInputAmount = new JTextField(10);
        userInputAmount.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                inputCheck();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                inputCheck();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                inputCheck();
            }


        });

        confirmationButton = new JButton("Confirm Withdraw");
        confirmationButton.addActionListener(e -> {
            Withdrawable selectedAccount = (Withdrawable) accountSelection.getSelectedItem();
            String inputStr = userInputAmount.getText();

            if (selectedAccount != null || inputStr.equals("")) {
                int amount = Integer.parseInt(inputStr);
                WithdrawTransaction transaction = new WithdrawTransaction(user, machine, selectedAccount, amount);

                if (transaction.perform()) {
                    MainFrame.showInfoMessage("Withdraw Successful!\n" + transaction);
                } else {
                    MainFrame.showErrorMessage("Withdraw failed because something went wrong");
                }

                accountSelection.updateUI();
            } else {
                MainFrame.showErrorMessage("Account not selected or invalid/missing withdraw amount!");
            }

        });


        initializeLayout();
        setVisible(true);

    }

    private void inputCheck() {
        if (!userInputAmount.getText().matches("\\d*")) {
            MainFrame.showErrorMessage("Invalid input detected! Positive integers only!");
            userInputAmount.setText("");
        }
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);

        JPanel accountSelectionPanel = new JPanel(flowLayout);
        accountSelectionPanel.add(new JLabel("Withdraw From: "));
        accountSelectionPanel.add(accountSelection);

        JPanel amountPanel = new JPanel(flowLayout);
        amountPanel.add(new JLabel("Withdraw Amount: "));
        amountPanel.add(userInputAmount);

        JPanel confirmPanel = new JPanel(flowLayout);
        confirmPanel.add(confirmationButton);

        container.setLayout(new GridLayout(3, 1));
        container.add(accountSelectionPanel);
        container.add(amountPanel);
        container.add(confirmPanel);
    }
}
