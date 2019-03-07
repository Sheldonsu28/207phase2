package ui;

import java.util.ArrayList;

class Menu {
    private ArrayList<String> menu = new ArrayList<String>();
    ArrayList<String> mainMenu() {
        menu.add("Deposit");
        menu.add("Withdraw");
        menu.add("Transfer");
        menu.add("Info");
        return menu;
    }
}
