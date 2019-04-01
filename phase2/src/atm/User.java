package atm;

import account.Account;
import account.ChequingAccount;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * The user class represent a user, responsible for storing accounts of the user and the interaction between the user
 * and the accounts.
 */
public class User implements Serializable {
    private final String username;
    private final AccountStorageManager accountVaults;
    private String password;
    static final int MAX_NAME_LENGTH = 15;
    static final int MIN_NAME_LENGTH = 5;

    /**
     * Initial a user account with username and password.
     *
     * @param username        The username of the user account.
     * @param defaultPassword The default password of the user account.
     */
    User(String username, String defaultPassword) {
        password = defaultPassword;
        this.username = username;
        accountVaults = new AccountStorageManager(this);
    }

    /**
     * Get the primary cheque account from the Account Storage manager.
     *
     * @return The primary account of this user.
     */
    public ChequingAccount getPrimaryAccount() {
        return accountVaults.getPrimaryAccount();
    }

    /**
     * Set the primary account for this user.
     *
     * @param primaryAccount The chequeing account that will be set as the primary account.
     */
    void setPrimaryAccount(ChequingAccount primaryAccount) {
        accountVaults.setPrimaryAccount(primaryAccount);
    }

    /**
     * Get the user name for the current user account.
     *
     * @return The user name for the current user account.
     */
    public String getUserName() {
        return username;
    }

    /**
     * Return a array list that contains all the bank accounts belong to this user account.
     *
     * @return An array list of all bank accounts belong to this user.
     */
    public ArrayList<Account> getAllAccounts() {
        return accountVaults.getAllAccounts();
    }

    /**
     * Verify if the inout string equals to the password of this user account. Return true if the input is equal to the
     * password, else return false.
     *
     * @param password Password for the user account.
     * @return True if the input is equal to the password, else return false.
     */
    boolean verifyPassword(String password) {
        return !this.password.equals(password);
    }

    /**
     * Change the password of the user account.
     *
     * @param newPassword New password.
     */
    public void changePassword(String newPassword) {
        password = newPassword;
    }

    /**
     * Get total balance within in the user account by adding up all the balance stored in different bank accounts
     * belong to the user.
     *
     * @return The net total balance in the user account.
     */
    public double getNetTotal() {
        double netTotal = 0;

        for (Account account : accountVaults.getAllAccounts())
            netTotal += account.getNetBalance();

        return netTotal;
    }

    /**
     * Add a bank account to the user.
     *
     * @param account Any bank account.
     * @param <T>     Any classes that extends Account.
     */
    public <T extends Account> void addAccount(T account) {
        accountVaults.addAccount(account);
    }

    /**
     * Save a add account request to a file named accReq.txt.
     *
     * @param klass Bank account that want to be add to the user account.
     */
    public void requestAccountCreation(Class<? extends Account> klass) {
        if (klass.isInterface() || Modifier.isAbstract(klass.getModifiers()))
            throw new IllegalArgumentException("Can not request abstract or interface account class creation!");

        (new FileHandler()).saveTo(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE,
                String.format("%s %s", username, klass.getName()));
    }

    /**
     * Get the account summary, the summary include username, type of accounts, balance in each account, and net balance.
     *
     * @return Summary of the user account.
     */
    public String getAccountsSummary() {
        StringBuilder summary = new StringBuilder("Account Summary: \n");

        for (Account account : accountVaults.getAllAccounts()) {
            summary.append(String.format("ID %s\tTYPE %s\tDATE OF CREATION %s\tBAL $%.2f\tNET BAL %.2f\n",
                    account.getId(), account.getClass().getSimpleName(), account.getTimeCreated(),
                    account.getBalance(), account.getNetBalance()));
        }

        return summary.toString();
    }

    /**
     * Get the list of account of the type designated by the input class in the user account.
     *
     * @param klass Type of class to be filtered.
     * @param <T>   The generic type of the target class.
     * @return An ArrayList contain all the class that satisfy the requirement in the user account
     */
    public <T> ArrayList<T> getAccountListOfType(Class<T> klass) {
        return accountVaults.getAccountListOfType(klass);
    }

    /**
     * The String representation of the User account(username).
     *
     * @return The username of the user account.
     */
    public String toString() {
        return username;
    }
}
