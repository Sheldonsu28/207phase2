package atm;

import account.Account;
import account.BillingAccount;
import account.ChequingAccount;
import transaction.Transaction;

import java.io.Serializable;
import java.util.*;

/**
 * The manager class represent a manger, responsible for managing accounts and initialize time for the ATMs.
 *
 * @author zhaojuna
 * @version 1.0
 */
public class BankManager implements Serializable {
    private AtmTime commonTime;
    private List<AtmMachine> machineList;
    private AccountFactory accountFactory;
    private UserDatabase userDatabase;
    private List<BillingAccount> payeeList;
    private TreeMap<String, User> employeeList;
    private boolean hasLoggedin;
    private String username, password;
    private PasswordManager passwordManager;

    private boolean hasInitialized;

    public BankManager() {
        machineList = new ArrayList<>();
        userDatabase = new UserDatabase();
        accountFactory = new AccountFactory();
        employeeList = new TreeMap<>();
        hasInitialized = false;
        hasLoggedin = false;
        username = "admin";
        password = "CS207fun";
        passwordManager = new PasswordManager(12, 24, "[0-9]|[a-z]|[A-Z]");

        addMachine();
    }

    public void initialize(Date initialDate) {
        commonTime = new AtmTime(initialDate);

        try {
            payeeList = accountFactory.getPayeesFromFile(commonTime);
        } catch (IllegalFileFormatException e) {
            System.out.println(e.getMessage());
        }

        hasInitialized = true;
    }

    public List<BillingAccount> getPayeeList() {
        return payeeList;
    }

    /**
     * Get the Atm's time
     *
     * @return The time of this ATM.
     */
    public AtmTime getCommonTime() {
        return commonTime;
    }

    public boolean hasInitialized() {
        return hasInitialized;
    }

    public boolean isValidPassword(String password) {
        return passwordManager.isValidPassword(password);
    }

    /**
     * Takes the user's username and log the user in.
     *
     * @param username Username of the user.
     * @param password Password of the user.
     * @throws WrongPasswordException Throws this exception if the password is incorrect.
     * @throws UserNotExistException  Throws this exception if the username does not exist.
     */
    public void login(String username, String password) throws WrongPasswordException, UserNotExistException {
        if (username.equals(this.username)) {
            if (password.equals(this.password))
                hasLoggedin = true;
            else
                throw new WrongPasswordException(username);
        } else {
            throw new UserNotExistException(username);
        }

    }

    /**
     * Check if the manager has logged in ot not.
     *
     * @return Log in states of the manager.
     */

    public boolean hasLoggedin() {
        return hasLoggedin;
    }

    public void logout() {
        hasLoggedin = false;
    }

    public List<User> getAllUsers() {
        return userDatabase.getUserList();
    }

    public boolean hasUser(String username) {
        return userDatabase.hasUser(username);
    }

    /**
     * Private method that checks if the manager account is in a correct state.
     *
     * @param needLoginState True if manager need to log in.
     */
    private void checkState(boolean needLoginState) {
        if (!hasInitialized)
            throw new IllegalStateException("Manager not yet initialized!");

        if (needLoginState && !hasLoggedin)
            throw new IllegalStateException("This manager is not logged in yet!");
    }

    public List<AtmMachine> getMachineList() {
        return Collections.unmodifiableList(machineList);
    }

    public void restockMachine(TreeMap<Integer, Integer> stock) {
        try {
            machineList.get(0).increaseStock(stock);
        } catch (InvalidCashTypeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add a new ATM Machine with stock to the manager.
     *
     * @return The new ATM machine that has been created.
     */
    private AtmMachine addMachine() {
        TreeMap<Integer, Integer> initialStock = new TreeMap<>();
        initialStock.put(5, 100);
        initialStock.put(10, 100);
        initialStock.put(20, 100);
        initialStock.put(50, 100);

        AtmMachine machine = new AtmMachine(commonTime, initialStock, new StepCashDistributor());
        machineList.add(machine);
        return machine;
    }

    /**
     * Create new account to the correspond user.
     *
     * @param username    The user that account need to be added to.
     * @param accountType The type of the account.
     * @param isPrimary   if the account is the primary account of the user or not.
     * @param <T>         Any account type.
     * @return Return the account created.
     */
    public <T extends Account> boolean createAccount(String username, Class<T> accountType, boolean isPrimary) {
        checkState(true);

        return accountFactory.generateDefaultAccount(
                new ArrayList<>(Collections.singletonList(userDatabase.getUser(username))),
                accountType, commonTime, isPrimary);
    }

    /**
     * Create joint account to the correspond user.
     *
     * @param usernames   The usernames of the owners of the joint accounts
     * @param accountType The type of the account.
     * @param isPrimary   if the account is the primary account of the user or not.
     * @param <T>         Any account type.
     * @return Return the account created.
     */
    public <T extends Account> boolean createJointAccount(
            List<String> usernames, Class<T> accountType, boolean isPrimary) {
        checkState(true);

        List<User> owners = new ArrayList<>();
        for (String username : usernames) {
            owners.add(userDatabase.getUser(username));
        }

        return accountFactory.generateDefaultAccount(owners, accountType, commonTime, isPrimary);
    }


    /**
     * Create a new user with given username and register it in the database
     *
     * @param username The username of the user
     * @return The default password for this user
     */
    public String createUser(String username) throws UsernameAlreadyExistException, UsernameOutOfRangeException {
        String password = userCreationValidation(username);
        User newUser = userDatabase.registerNewUser(username, password);

        accountFactory.generateDefaultAccount(Collections.singletonList(newUser),
                ChequingAccount.class, commonTime, true);

        return password;
    }

    public String createEmployee(String username) throws UsernameAlreadyExistException, UsernameOutOfRangeException {
        String password = createUser(username);

        employeeList.put(username, userDatabase.getUser(username));

        return password;
    }

    private String userCreationValidation(String username) throws UsernameOutOfRangeException {
        checkState(true);

        if (username.length() < User.MIN_NAME_LENGTH || username.length() > User.MAX_NAME_LENGTH)
            throw new UsernameOutOfRangeException();

        return passwordManager.generateRandomPassword();
    }


    /**
     * Try to log user into the system, throws error if the user failed to log in.
     *
     * @param username User's username.
     * @param password User's password.
     * @return The user account correspond to the username.
     */
    public User validateUserLogin(String username, String password)
            throws WrongPasswordException, UserNotExistException {
        checkState(false);

        return userDatabase.loginUser(username, password);
    }

    public void validateEmployeeLogin(String username, String password)
            throws WrongPasswordException, UserNotExistException {
        checkState(false);

        if (employeeList.containsKey(username)) {
            if (!employeeList.get(username).verifyPassword(password)) {
                throw new WrongPasswordException(username);
            }
        } else {
            throw new UserNotExistException(username);
        }

    }

    /**
     * Cancel the last transaction made by the specific user account.
     *
     * @param targetAccount The account requires cancellation.
     * @return Return true if cancellation is successful, return false if the last transaction is not empty.
     */
    public boolean cancelLastTransaction(Account targetAccount) {
        checkState(true);

        Transaction transaction = targetAccount.getLastTransaction();

        if (transaction != null && transaction.isCancellable()) {
            transaction.cancel();
            return true;
        }

        return false;
    }

    /**
     * This method is responsible for cancelling multiple recent transactions.
     *
     * @param targetAccount User account that the cancellation wil be perform on.
     * @param amount        Numbers of transaction you want to cancel.
     * @return Whether the cancellation is performed or not.
     */
    public boolean cancelTransactions(Account targetAccount, int amount) {
        checkState(true);

        List<Transaction> transactions = targetAccount.getTransactions();
        int total = transactions.size();

        if (total == 0) {
            return true;
        } else if (transactions.size() < amount) {
            amount = transactions.size();
        }

        for (int index = total - 1; index > total - 1 - amount; index--) {
            Transaction transaction = transactions.get(index);

            if (transaction.isCancellable()) {
                if (!transaction.isCancelled()) {
                    transaction.cancel();
                    continue;
                }
            }

            return false;
        }

        return true;
    }

}
