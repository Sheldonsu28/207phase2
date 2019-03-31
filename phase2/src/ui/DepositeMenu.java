package ui;

import account.Depositable;
import atm.BankManager;
import atm.IllegalFileFormatException;
import atm.User;
import transaction.DepositTransaction;
import javax.swing.*;
import java.awt.*;

public class DepositeMenu extends SubMenu {
    private JComboBox<Depositable> accountSelection;
    private JButton confirmationButton;
    private DepositTransaction transaction;

    DepositeMenu(User user, BankManager manager) {
        super("Account Selection");


        accountSelection = new JComboBox<>(user.getAccountListOfType(Depositable.class).toArray(new Depositable[0]));


        confirmationButton = new JButton("Confirm Withdraw");
        confirmationButton.addActionListener(e -> {
            Depositable selectedAccount = (Depositable) accountSelection.getSelectedItem();

            if (selectedAccount != null) {

                try {
                    this.transaction = new DepositTransaction(user, manager.getMachineList().get(0), selectedAccount);
                } catch (IllegalFileFormatException exception) {
                    JOptionPane.showMessageDialog(this,
                            "The deposit format is wrong", "Deposit Failed",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (transaction.perform()) {
                JOptionPane.showMessageDialog(this,
                        "Deposit Performed!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Deposit action is not perform because something went wrong", "Deposit Failed",
                        JOptionPane.INFORMATION_MESSAGE);
            }


        });


        initializeLayout();
        setVisible(true);

    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);


        JPanel accountSelectionPanel = new JPanel(flowLayout);
        accountSelectionPanel.add(new JLabel("Select Account: "));
        accountSelectionPanel.add(accountSelection);


        JPanel confirmPanel = new JPanel(flowLayout);
        confirmPanel.add(confirmationButton);

        JPanel infoPanel = new JPanel(new GridLayout(12, 1));
        infoPanel.add(accountSelectionPanel);
        infoPanel.add(confirmPanel);

        Box box = Box.createHorizontalBox();
        box.add(accountSelectionPanel);
        box.add(Box.createHorizontalStrut(10));
        box.add(infoPanel);

        container.add(box);
    }
}
