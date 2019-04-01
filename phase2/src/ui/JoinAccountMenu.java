package ui;

import account.Account;
import atm.BankManager;
import atm.User;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.List;

public class JoinAccountMenu extends SubMenu {
    private final JComboBox<User> orgUserSelection;
    private final JComboBox<User> joinUserSelection;
    private final JComboBox<Account> accountSelection;
    private final JButton submitButton;

    JoinAccountMenu(BankManager manager) {
        super("Join");

        orgUserSelection = new JComboBox<>(manager.getAllUsers().toArray(new User[0]));
        orgUserSelection.addActionListener(e -> updateSelection(manager));

        accountSelection = new JComboBox<>();
        joinUserSelection = new JComboBox<>();

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            User jointUser = (User) joinUserSelection.getSelectedItem();
            Account account = (Account) accountSelection.getSelectedItem();

            if (jointUser != null && account != null) {

                account.addOwner(jointUser);
                jointUser.addAccount(account);

                MainFrame.showInfoMessage("Account ownership successfully expanded.", "Success");

            } else {
                MainFrame.showErrorMessage("Account/user not selected!");
            }
        });

        updateSelection(manager);

        defaultRowsLayout(new LinkedHashMap<JComponent, String>() {{
            put(orgUserSelection, "Select User: ");
            put(accountSelection, "Select account: ");
            put(joinUserSelection, "Select joint user: ");
            put(submitButton, null);
        }});

        setVisible(true);
    }

    private void updateSelection(BankManager manager) {
        User selectedUser = (User) orgUserSelection.getSelectedItem();
        accountSelection.removeAllItems();

        if (selectedUser != null) {
            for (Account account : selectedUser.getAllAccounts()) {
                accountSelection.addItem(account);
            }
        }

        accountSelection.updateUI();

        joinUserSelection.removeAll();

        List<User> allUsers = manager.getAllUsers();

        for (User user : allUsers) {
            if (user != selectedUser) {
                joinUserSelection.addItem(user);
            }
        }

        joinUserSelection.updateUI();
    }
}
