package ui;

import account.Depositable;
import atm.BankManager;
import atm.User;
import transaction.DepositTransaction;

import javax.swing.*;
import java.util.LinkedHashMap;

class ManualDepositMenu extends SubMenu {

    private final JComboBox<Depositable> accountSelection;
    private final JTextField userInputAmount;
    private final JButton submitButton;

    ManualDepositMenu(BankManager manager, User user) {
        super("Manual Deposit");

        accountSelection = new JComboBox<>(user.getAccountListOfType(Depositable.class).toArray(new Depositable[0]));

        userInputAmount = getPositiveTwoDecimalOnlyField();

        submitButton = new JButton("Submit Deposit");
        submitButton.addActionListener(e -> {
            Depositable selectedAccount = (Depositable) accountSelection.getSelectedItem();
            String amountText = userInputAmount.getText();

            if (selectedAccount != null && !amountText.equals("")) {
                DepositTransaction transaction = new DepositTransaction(
                        user, manager.getMachineList().get(0), selectedAccount, Integer.parseInt(amountText));

                if (transaction.perform()) {
                    MainFrame.showInfoMessage("Deposit Successful!\n" + transaction, "Success");
                } else {
                    MainFrame.showErrorMessage("Deposit failed! Something went wrong.");
                }

                accountSelection.updateUI();
            } else {
                MainFrame.showErrorMessage("Account not selected or invalid/missing deposit amount!");
            }
        });

        defaultRowsLayout(new LinkedHashMap<JComponent, String>() {{
            put(accountSelection, "Deposit to: ");
            put(userInputAmount, "Deposit Amount: ");
            put(submitButton, null);
        }});

        setVisible(true);
    }

}
