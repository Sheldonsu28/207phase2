package ui;

import account.ChequingAccount;
import account.CreditCardAccount;
import account.LineOfCreditAccount;
import account.SavingsAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Menu {
    WELCOME_MENU(Arrays.asList("Login as user", "Login as manager")),
    MAIN_MENU(Arrays.asList("Deposit", "Withdraw", "Transfer", "Pay Bill", "Account Info", "Open Account", "Logout")),
    ACCOUNT_INFO_MENU(Arrays.asList("Show Summary", "Show Net Total", "Show Recent Transactions", "Change password", "Back to main")),
    CONFIRM_MENU(Arrays.asList("Confirm", "Back to main")),
    ACCOUNT_SELECTION_MENU(Collections.emptyList()),
    USER_SELECTION_MENU(Collections.emptyList()),
    ACCOUNT_MENU(Arrays.asList(SavingsAccount.class.getSimpleName(), ChequingAccount.class.getSimpleName(),
            LineOfCreditAccount.class.getSimpleName(), CreditCardAccount.class.getSimpleName())),
    MANAGER_MENU(Arrays.asList("Read alerts", "Create user", "Read account creation request",
            "Cancel recent transaction", "Restock machine", "Create account", "Logout"));

    private final ArrayList<String> choices;

    Menu(List<String> choices) {
        this.choices = new ArrayList<>(choices);
    }

    public List<String> getMenuOptions() {
        return choices;
    }

}
