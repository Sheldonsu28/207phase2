package atm;

import account.Account;
import account.ChequingAccount;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username, password;
    private AccountStorageManager accountVaults;

    User(String username, String defaultPassword) {
        password = defaultPassword;
        this.username = username;
        accountVaults = new AccountStorageManager();
    }

    public ChequingAccount getPrimaryAccount() {
        return accountVaults.getPrimaryAccount();
    }

    public void setPrimaryAccount(ChequingAccount primaryAccount) {
        accountVaults.setPrimaryAccount(primaryAccount);
    }

    public String getUserName() {
        return username;
    }

    List<Account> getAccounts() {
        return accountVaults.getAllAccounts();
    }

    boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    void changePassword(String newPassword) {
        password = newPassword;
    }

    double getNetTotal() {
        double netTotal = 0;

        for (Account account : accountVaults.getAllAccounts())
            netTotal += account.getNetBalance();

        return netTotal;
    }


    <T extends Account> void addAccount(T account) {
        accountVaults.addAccount(account);
    }

    String getAccountsSummary() {
        StringBuilder summary = new StringBuilder("Account Summary: \n");

        for (Account account : accountVaults.getAllAccounts()) {
            summary.append(String.format("ID %s   TYPE %s   BAL $%.2f\n",
                    account.getId(), account.getClass().getSimpleName(), account.getBalance()));
        }

        return summary.toString();
    }


    public <T> ArrayList<T> getAccountListOfType(Class<T> klass) {
        return accountVaults.getAccountListOfType(klass);
    }

    public String toString() {
        return username;
    }
}
