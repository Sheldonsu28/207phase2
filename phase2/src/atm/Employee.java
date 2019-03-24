package atm;

import account.Account;
import account.ChequingAccount;
import transaction.Transaction;

import java.io.Serializable;

public class Employee extends User implements Serializable {
    private AccountFactory accountFactory;
    private UserDatabase Users;
    private AtmTime commonTime;
    private PasswordManager passwordManager;

    /**
     * Employee have less access than manager, but it can also act like a normal user.
     * @param username Username of the account.
     * @param password Password of the account.
     * @param dataBase The database of the users.
     * @param standardTime ATM time.
     */
    Employee(String username, String password, UserDatabase dataBase, AtmTime standardTime) {
        super(username, password);
        this.Users = dataBase;
        this.commonTime = standardTime;
        this.accountFactory = new AccountFactory();
        passwordManager = new PasswordManager(12, 24, "[0-9]|[a-z]|[A-Z]");
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
        if (Username.equals(super.getUserName())) {
            System.out.println("You can not add accounts to yourself.");
            return false;
        }
        return accountFactory.generateDefaultAccount(Users.getUser(Username), accountType, commonTime, isPrimary);
    }

    /**
     *
     * @param username                          Username of the account.
     * @return                                  Password of the account.
     * @throws UsernameAlreadyExistException    Throw this exception if the username already exist.
     * @throws UsernameOutOfRangeException      Throw this exception if the username is too long or too short.
     */
    public String createUser(String username) throws UsernameAlreadyExistException, UsernameOutOfRangeException {

        if (username.length() < User.MIN_NAME_LENGTH || username.length() > User.MAX_NAME_LENGTH)
            throw new UsernameOutOfRangeException();

        String password = passwordManager.generateRandomPassword();
        User newUser = Users.registerNewUser(username, password);

        accountFactory.generateDefaultAccount(newUser, ChequingAccount.class, commonTime, true);

        return password;
    }

    /**
     * Cancel the last transaction made by the specific user account.
     *
     * @param targetAccount The account requires cancellation.
     * @return Return true if cancellation is successful, return false if the last transaction is not empty.
     */
    public boolean cancelLastTransaction(Account targetAccount) {
        Transaction transaction = targetAccount.getLastTransaction();

        if (transaction != null && transaction.isCancellable()) {
            transaction.cancel();
            return true;
        }

        return false;
    }
}
