package atm;

import account.Account;
import account.ChequingAccount;
import transaction.Transaction;

import java.io.Serializable;
import java.util.*;

public class BankManager implements Serializable {
    private AtmTime commonTime;
    private List<AtmMachine> machineList;
    private AccountFactory accountFactory;
    private UserDatabase userDatabase;
    private boolean hasLoggedin;
    private String username, password;
    private RandomPasswordGenerator passwordGenerator;

    private boolean hasInitialized;

    public BankManager() {
        machineList = new ArrayList<>();
        userDatabase = new UserDatabase();
        accountFactory = new AccountFactory();
        hasInitialized = false;
        hasLoggedin = false;
        username = "admin";
        password = "CS207fun";
        passwordGenerator = new RandomPasswordGenerator(12, 24,
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()");

        addMachine();
    }

    public void initialize(Date initialDate) {
        commonTime = new AtmTime(initialDate);

        hasInitialized = true;
    }

    public AtmTime getCommonTime() {
        return commonTime;
    }

    public boolean hasInitialized() {
        return hasInitialized;
    }

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

    public boolean hasLoggedin() {
        return hasLoggedin;
    }

    public void logout() {
        hasLoggedin = false;
    }

    private void checkState(boolean needLoginState) {
        if (!hasInitialized)
            throw new IllegalStateException("Manager not yet initialized!");

        if (needLoginState && !hasLoggedin)
            throw new IllegalStateException("This manager is not loggedin yet!");
    }

    public List<AtmMachine> getMachineList() {
        return Collections.unmodifiableList(machineList);
    }

    private AtmMachine addMachine() {
        TreeMap<Integer, Integer> initialStock = new TreeMap<>();
        initialStock.put(5, 500);
        initialStock.put(10, 500);
        initialStock.put(20, 500);
        initialStock.put(50, 500);

        AtmMachine machine = new AtmMachine(commonTime, initialStock, new StepCashDistributor());
        machineList.add(machine);
        return machine;
    }


    public <T extends Account> boolean createAccount(User user, Class<T> accountType, boolean isPrimary) {
        checkState(true);

        return accountFactory.generateDefaultAccount(user, accountType, commonTime, isPrimary);
    }

    /**
     * Create a new user with given username and register it in the database
     *
     * @param username The username of the user
     * @return The default password for this user
     */
    public String createUser(String username) throws UsernameAlreadyExistException {
        checkState(true);

        String password = passwordGenerator.generatePassword();
        User newUser = userDatabase.registerNewUser(username, password);

        accountFactory.generateDefaultAccount(newUser, ChequingAccount.class, commonTime, true);

        return password;
    }

    public User validateUserLogin(String username, String password) {
        checkState(false);

        User user = null;

        try {
            user = userDatabase.loginUser(username, password);
        } catch (WrongPasswordException | UserNotExistException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    public boolean cancelLastTransaction(Account targetAccount) {
        checkState(true);

        Transaction transaction = targetAccount.getLastTransaction();

        if (transaction != null && transaction.isCancellable()) {
            transaction.cancel();
            return true;
        }

        return false;
    }

}
