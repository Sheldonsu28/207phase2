package ui;

import atm.BankManager;

import javax.swing.*;

public class UserSelectionMenu extends SubMenu {
    UserSelectionMenu(BankManager manager, String title, JDialog parent) {
        super(manager, title, parent);
    }
}
