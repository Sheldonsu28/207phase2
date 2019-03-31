package ui;

import account.Account;
import atm.BankManager;
import atm.User;
import transaction.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CancelTransactionMenu extends SubMenu {
    private JComboBox<User> userSelection;
    private JComboBox<Account> accountSelection;
    private JTextArea transactionLog;
    private JComboBox<Integer> numOfCancellation;
    private JButton submitButton;

    CancelTransactionMenu(BankManager manager) {
        super("Transaction Cancellation");

        userSelection = new JComboBox<>(manager.getAllUsers().toArray(new User[0]));
        userSelection.addActionListener(e -> {
            User selectedUser = (User) userSelection.getSelectedItem();
            accountSelection.removeAllItems();

            if (selectedUser != null) {
                for (Account account : selectedUser.getAllAccounts()) {
                    accountSelection.addItem(account);
                }
            }

            updateTransactions();
        });

        User defaultUser = (User) userSelection.getSelectedItem();
        if (defaultUser != null) {
            accountSelection = new JComboBox<>(defaultUser.getAllAccounts().toArray(new Account[0]));
        } else {
            accountSelection = new JComboBox<>();
        }

        accountSelection.addActionListener(e -> {
            updateTransactions();
        });

        transactionLog = new JTextArea();
        transactionLog.setEditable(false);
        transactionLog.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        transactionLog.setFont(new Font("Serif", Font.PLAIN, 18));

        numOfCancellation = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            Account selectedAccount = (Account) accountSelection.getSelectedItem();

            if (selectedAccount != null) {
                if (manager.cancelTransactions(selectedAccount, (int) numOfCancellation.getSelectedItem())) {
                    JOptionPane.showMessageDialog(this,
                            "All cancellation successfully performed!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Some cancellation failed " +
                                    "(transaction already cancelled / transaction not cancellable)",
                            "Partial Failure", JOptionPane.WARNING_MESSAGE);
                }

                CancelTransactionMenu.this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No account selected!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        updateTransactions();
        initializeLayout();

        setVisible(true);
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);

        JPanel transactionPanel = new JPanel(flowLayout);
        transactionPanel.add(transactionLog);
//        JScrollPane scrollPane = new JScrollPane(transactionLog,
//                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JPanel userSelectionPanel = new JPanel(flowLayout);
        userSelectionPanel.add(new JLabel("Select User: "));
        userSelectionPanel.add(userSelection);

        JPanel accountSelectionPanel = new JPanel(flowLayout);
        accountSelectionPanel.add(new JLabel("Select Account: "));
        accountSelectionPanel.add(accountSelection);

        JPanel numOfCancellationPanel = new JPanel(flowLayout);
        numOfCancellationPanel.add(new JLabel("Number of cancellation: "));
        numOfCancellationPanel.add(numOfCancellation);

        JPanel submitPanel = new JPanel(flowLayout);
        submitPanel.add(submitButton);

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(userSelectionPanel);
        infoPanel.add(accountSelectionPanel);
        infoPanel.add(numOfCancellationPanel);
        infoPanel.add(submitPanel);

        Box box = Box.createHorizontalBox();
        box.add(transactionPanel);
        box.add(Box.createHorizontalStrut(10));
        box.add(infoPanel);

        container.add(box);
    }

    private void updateTransactions() {
        Account selectedAccount = (Account) accountSelection.getSelectedItem();

        if (selectedAccount != null) {
            List<Transaction> transactions = selectedAccount.getTransactions();

            if (transactions.size() == 0) {
                transactionLog.setText("No recent transaction");
                return;
            }

            StringBuilder log = new StringBuilder();

            for (Transaction transaction : transactions) {
                log.append(transaction).append("\n");
            }

            transactionLog.setText(log.toString());
        } else {
            transactionLog.setText("No account selected");
        }
    }
}
