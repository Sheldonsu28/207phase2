package atm;

import account.Account;


import java.io.Serializable;

public class Employee extends User implements Serializable {
    private BankManager thisManager;

    /**
     * Employee have less access than manager, but it can also act like a normal user.
     * @param username Username of the account.
     * @param password Password of the account.
     * @param dataBase The database of the users.
     * @param standardTime ATM time.
     */
    Employee(String username, String password, UserDatabase dataBase, AtmTime standardTime,BankManager manager) {
        super(username, password);
        this.thisManager = manager;
    }

    /**
     * Create a new account for the user.
     * @param Username      Username of the account.
     * @param accountType   Password of the account.
     * @param isPrimary     Is the account primary or not.
     * @param <T>           Any Account.
     * @return              Return if the account is created or not.
     */
    public <T extends Account> boolean createAccount(String Username, Class<T> accountType, boolean isPrimary) {
        return thisManager.createAccount(Username,accountType, isPrimary);
    }

    /**
     *
     * @param username                          Username of the account.
     * @return                                  Password of the account.
     * @throws UsernameAlreadyExistException    Throw this exception if the username already exist.
     * @throws UsernameOutOfRangeException      Throw this exception if the username is too long or too short.
     */
    public String createUser(String username) throws UsernameAlreadyExistException, UsernameOutOfRangeException {
        return thisManager.createUser(username);
    }

    /**
     * Cancel the last transaction made by the specific user account.
     *
     * @param targetAccount The account requires cancellation.
     * @return Return true if cancellation is successful, return false if the last transaction is not empty.
     */
    public boolean cancelLastTransaction(Account targetAccount) {
        return thisManager.cancelLastTransaction(targetAccount);
    }
}
