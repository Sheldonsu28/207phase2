package ui;

import java.util.ArrayList;

class Menu {
    ArrayList<String> welcome() {
        ArrayList<String> menu = new ArrayList<String>();
        menu.add("Sign in");
        menu.add("Reset password");
        menu.add("Create a account");
        return menu;
    }

    ArrayList<String> main() {
        ArrayList<String> menu = new ArrayList<String>();
        menu.add("Deposit");
        menu.add("Withdraw");
        menu.add("Transfer");
        menu.add("Bills");
        menu.add("Account(s) Info");
        return menu;
    }

    ArrayList<String> getAccountInfo() {
        ArrayList<String> menu = new ArrayList<String>();
        menu.add("Summary of all account balances");
        menu.add("Net total");
        menu.add("Most recent transaction");
        menu.add("Date of creation of account(s)");
        menu.add("Create accounts");
        return menu;
    }

    ArrayList<String> confirmMenu() {
        ArrayList<String> menu = new ArrayList<String>();
        menu.add("Confirm");
        return menu;
    }
}
