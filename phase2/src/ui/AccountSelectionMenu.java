package ui;

import atm.BankManager;

import javax.swing.*;

public class AccountSelectionMenu extends SubMenu {
    AccountSelectionMenu(BankManager manager, String title, JDialog parent) {
        super(manager, title, parent);
    }
}
