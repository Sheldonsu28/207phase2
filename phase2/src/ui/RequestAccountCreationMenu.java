package ui;

import account.Account;
import account.ChequingAccount;
import atm.ExternalFiles;
import atm.FileHandler;
import atm.User;

import javax.swing.*;
import java.awt.*;

class RequestAccountCreationMenu extends SubMenu {
    private final JComboBox<Class<Account>> accountSelection;
    private final JButton request;
    private final JCheckBox setPrimary;

    @SuppressWarnings("unchecked")
    RequestAccountCreationMenu(User user) {
        super("Open Account");
        request = new JButton("Request");
        setPrimary = new JCheckBox("Set Primary");
        setPrimary.setEnabled(false);

        accountSelection = new JComboBox<>(Account.OWNABLE_ACCOUNT_TYPES);

        accountSelection.addActionListener(e -> {
            Class selectedAccount = (Class) accountSelection.getSelectedItem();
            setPrimary.setSelected(false);

            if (selectedAccount == ChequingAccount.class) {
                setPrimary.setEnabled(true);
            } else {
                setPrimary.setEnabled(false);
            }
        });

        request.addActionListener(e -> {
            boolean isPrimary = setPrimary.isSelected();
            Class<Account> accountType = (Class<Account>) accountSelection.getSelectedItem();

            if (accountType != null) {
                new FileHandler().saveTo(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE,
                        String.format("%s %s %s", user.getUserName(), accountType.getSimpleName(), isPrimary));

                MainFrame.showInfoMessage("Your request has been sent to the manager!",
                        "Request Accepted");
            } else {
                MainFrame.showErrorMessage("Account not selected!");
            }
        });
        initializeLayout();
        setVisible(true);
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);

        JPanel accountRequestPanel = new JPanel(flowLayout);
        accountRequestPanel.add(new JLabel("Select Account: "));
        accountRequestPanel.add(accountSelection);
        accountRequestPanel.add(setPrimary);
        accountRequestPanel.add(request);

        container.add(accountRequestPanel);
    }
}
