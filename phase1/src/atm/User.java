package atm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class User {
    private List<Account> accounts;

    User() {
        accounts = new ArrayList<>();
    }

    List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    int getNetTotal() {
        int netTotal = 0;

        for (Account account : accounts)
            netTotal += account.getNetBalance();

        return netTotal;
    }
}
