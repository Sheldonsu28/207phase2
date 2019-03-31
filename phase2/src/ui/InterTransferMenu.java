package ui;

import atm.BankManager;
import atm.User;

import javax.swing.*;

public class InterTransferMenu extends SubMenu {

    private JButton submitButton;

    InterTransferMenu(BankManager manager, User user) {
        super("Inter-User Transfer Menu");


        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {

        });
    }
}
