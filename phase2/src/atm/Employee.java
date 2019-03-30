package atm;

import account.Account;

import java.io.Serializable;

public class Employee extends User implements Serializable {
    private BankManager manager;

    /**
     * Employee have less access than manager, but it can also act like a normal user.
     * @param username Username of the account.
     * @param password Password of the account.
     */
    Employee(String username, String password, BankManager manager) {
        super(username, password);
        this.manager = manager;
    }

    /**
     * Create a new account for the user.
     * @param username      Username of the account.
     * @param accountType   Password of the account.
     * @param isPrimary     Is the account primary or not.
     * @param <T>           Any Account.
     * @return              Return if the account is created or not.
     */
    public <T extends Account> boolean createAccount(String username, Class<T> accountType, boolean isPrimary) {
        return manager.createAccount(username, accountType, isPrimary);
    }

    /**
     *
     * @param username                          Username of the account.
     * @return                                  Password of the account.
     * @throws UsernameAlreadyExistException    Throw this exception if the username already exist.
     * @throws UsernameOutOfRangeException      Throw this exception if the username is too long or too short.
     */
    public String createUser(String username) throws UsernameAlreadyExistException, UsernameOutOfRangeException {
        return manager.createUser(username);
    }

    /**
     * Cancel the last transaction made by the specific user account.
     *
     * @param targetAccount The account requires cancellation.
     * @return Return true if cancellation is successful, return false if the last transaction is not empty.
     */
    public boolean cancelLastTransaction(Account targetAccount) {
        return manager.cancelLastTransaction(targetAccount);
    }
}
