package ui;

import account.Account;
import account.Withdrawable;
import atm.User;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;


public class WithdrawMenu extends SubMenu {
    private JComboBox<Withdrawable> accountSelection;

    WithdrawMenu(User user) {
        super("Account Selection");

        accountSelection = new JComboBox<>(user.getAccountListOfType(Withdrawable.class).toArray(new Withdrawable[0]));
        accountSelection.addActionListener(e -> {
            Account selectAccount = (Account) accountSelection.getSelectedItem();

            if (selectAccount != null) {

            }
        });


    }
}
