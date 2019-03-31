package ui;

import account.Depositable;
import atm.*;
import transaction.DepositTransaction;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

public class ExternalDepositMenu extends SubMenu {
    private JComboBox<Depositable> accountSelection;
    private JTextArea depositInfoText;
    private JButton submitButton;

    ExternalDepositMenu(BankManager manager, User user) {
        super("Account Selection");

        accountSelection = new JComboBox<>(user.getAccountListOfType(Depositable.class).toArray(new Depositable[0]));

        depositInfoText = new JTextArea();
        depositInfoText.setEditable(false);
        depositInfoText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        depositInfoText.setLineWrap(false);
        depositInfoText.setFont(new Font("Serif", Font.PLAIN, 18));
        depositInfoText.setText(getDepositFileText());

        submitButton = new JButton("Deposit From File");
        submitButton.addActionListener(e -> {
            Depositable selectedAccount = (Depositable) accountSelection.getSelectedItem();
            DepositTransaction transaction;

            if (selectedAccount != null) {
                try {
                    transaction = new DepositTransaction(user, manager.getMachineList().get(0), selectedAccount);
                } catch (IllegalFileFormatException ex) {
                    MainFrame.showErrorMessage(ex.getMessage());
                    return;
                }
            } else {
                MainFrame.showErrorMessage("Deposit destination account not selected!");
                return;
            }

            if (transaction.perform()) {
                MainFrame.showInfoMessage("Deposit successful!\n" + transaction, "Success");
            } else {
                MainFrame.showErrorMessage("Deposit failed because something went wrong");
            }

            accountSelection.updateUI();
        });

        defaultRowsLayout(new LinkedHashMap<JComponent, String>() {{
            put(accountSelection, "Select Account: ");
            put(depositInfoText, null);
            put(submitButton, null);
        }});

        setVisible(true);
    }

    private String getDepositFileText() {
        StringBuilder text = new StringBuilder();

        for (String info : new FileHandler().readFrom(ExternalFiles.DEPOSIT_FILE)) {
            text.append(info).append("\n");
        }

        if (text.toString().equals(""))
            return "No deposit info detected in deposit file!";

        return text.toString();
    }
}