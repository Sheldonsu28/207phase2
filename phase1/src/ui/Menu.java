package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Menu {
    WELCOME_MENU(Arrays.asList("Login as user", "Login as manager")),
    MAIN_MENU(Arrays.asList("Deposit", "Withdraw", "Transfer", "Pay Bill", "Account Info", "Create Account", "Logout")),
    ACCOUNT_INFO_MENU(Arrays.asList("Show Summary", "Show Net Total", "Show Recent Transactions", "Back to main")),
    CONFIRM_MENU(Arrays.asList("Confirm", "Back to main")),
    ACCOUNT_SELECTION_MENU(Collections.emptyList());

    private final ArrayList<String> choices;

    Menu(List<String> choices) {
        this.choices = new ArrayList<>(choices);
    }

    public List<String> getMenuOptions() {
        return choices;
    }

}
