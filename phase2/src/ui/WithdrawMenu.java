package ui;

import account.Withdrawable;
import atm.BankManager;
import atm.User;
import transaction.WithdrawTransaction;

import javax.swing.*;
import java.util.LinkedHashMap;


class WithdrawMenu extends SubMenu {
    private final JComboBox<Withdrawable> accountSelection;
    private final JButton submitButton;
    private final JTextField userInputAmount;

    WithdrawMenu(BankManager manager, User user) {
        super("Account Selection");

        accountSelection = new JComboBox<>(user.getAccountListOfType(Withdrawable.class).toArray(new Withdrawable[0]));

        userInputAmount = getPositiveIntegerOnlyField();

        submitButton = new JButton("Submit Withdraw");
        submitButton.addActionListener(e -> {
            Withdrawable selectedAccount = (Withdrawable) accountSelection.getSelectedItem();
            String inputStr = userInputAmount.getText();

            if (selectedAccount != null || inputStr.equals("")) {
                int amount = Integer.parseInt(inputStr);
                WithdrawTransaction transaction =
                        new WithdrawTransaction(user, manager.getMachineList().get(0), selectedAccount, amount);

                if (transaction.perform()) {
                    MainFrame.showInfoMessage("Withdraw Successful!\n" + transaction, "Success");
                } else {
                    MainFrame.showErrorMessage("Withdraw failed! Something went wrong.");
                }

                accountSelection.updateUI();
            } else {
                MainFrame.showErrorMessage("Account not selected or invalid/missing withdraw amount!");
            }

        });

        defaultRowsLayout(new LinkedHashMap<JComponent, String>() {{
            put(accountSelection, "Withdraw From: ");
            put(userInputAmount, "Withdraw Amount: ");
            put(submitButton, null);
        }});

        setVisible(true);

    }
}
