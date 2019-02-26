package atm;

import account.Account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private List<Account> accounts;
    private String username, password;

    User(String username, String defaultPassword) {
        accounts = new ArrayList<>();
        password = defaultPassword;
        this.username = username;
    }

    List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    // TODO maybe test required
    double getNetTotal() {
        double netTotal = 0;

        for (Account account : accounts)
            netTotal += account.getNetBalance();

        return netTotal;
    }

    void changePassword(String newPassword) {
        password = newPassword;
    }

    //  TODO test required
    String getAccountSummary() {
        StringBuilder summary = new StringBuilder("Account Summary: \n");

        for (Account account : accounts) {
            summary.append(String.format("ID %s   TYPE %s   BAL $%.2f\n",
                    account.getId(), account.getClass().getSimpleName(), account.getBalance()));
        }

        return summary.toString();
    }
}
