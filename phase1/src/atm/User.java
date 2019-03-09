package atm;

import account.Account;
import account.ChequingAccount;

import java.util.ArrayList;

public class User {
    private String username, password;
    private AccountStorageManager accountVaults;

    User(String username, String defaultPassword) {
        password = defaultPassword;
        this.username = username;
        accountVaults = new AccountStorageManager(this);
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

    public ArrayList<Account> getAllAccounts() {
        return accountVaults.getAllAccounts();
    }

    boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public void changePassword(String newPassword) {
        password = newPassword;
    }

    public double getNetTotal() {
        double netTotal = 0;

        for (Account account : accountVaults.getAllAccounts())
            netTotal += account.getNetBalance();

        return netTotal;
    }


    <T extends Account> void addAccount(T account) {
        accountVaults.addAccount(account);
    }

    public String getAccountsSummary() {
        StringBuilder summary = new StringBuilder("Account Summary: \n");

        for (Account account : accountVaults.getAllAccounts()) {
            summary.append(String.format("ID %s\tTYPE %s\tDATE OF CREATION %s\tBAL $%.2f\tNET BAL %.2f\n",
                    account.getId(), account.getClass().getSimpleName(), account.getTimeCreated(),
                    account.getBalance(), account.getNetBalance()));
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
