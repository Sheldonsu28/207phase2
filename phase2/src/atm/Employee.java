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

    Employee(String username, String password, UserDatabase dataBase, AtmTime standardTime) {
        super(username, password);
        this.Users = dataBase;
        this.commonTime = standardTime;
        this.accountFactory = new AccountFactory();
        passwordManager = new PasswordManager(12, 24, "[0-9]|[a-z]|[A-Z]");
    }

    public < T extends Account> boolean createAccount(String Username, Class<T> accountType,boolean isPrimary ){
        if (Username.equals(super.getUserName())) {
            System.out.println("You can not add accounts to yourself.");
            return false;
        }
        return accountFactory.generateDefaultAccount(Users.getUser(Username), accountType, commonTime, isPrimary);
    }

    public String createUser(String username) throws UsernameAlreadyExistException, UsernameOutOfRangeException {

        if (username.length() < User.MIN_NAME_LENGTH || username.length() > User.MAX_NAME_LENGTH)
            throw new UsernameOutOfRangeException();

        String password = passwordManager.generateRandomPassword();
        User newUser = Users.registerNewUser(username, password);

        accountFactory.generateDefaultAccount(newUser, ChequingAccount.class, commonTime, true);

        return password;
    }

    public boolean cancelLastTransaction(Account targetAccount) {
        Transaction transaction = targetAccount.getLastTransaction();

        if (transaction != null && transaction.isCancellable()) {
            transaction.cancel();
            return true;
        }

        return false;
    }
}
