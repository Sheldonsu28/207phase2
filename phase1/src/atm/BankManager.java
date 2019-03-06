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
    }

    public void initialize(Date initialDate) {
        commonTime = new AtmTime(initialDate);

        //  TODO more initialization

        hasInitialized = true;
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

    private void checkState() {
        if (!hasInitialized)
            throw new IllegalStateException("Manager not yet initialized!");

        if (!hasLoggedin)
            throw new IllegalStateException("This manager is not loggedin yet!");
    }

    public List<AtmMachine> getMachineList() {
        return Collections.unmodifiableList(machineList);
    }

    public AtmMachine addMachine() {
        checkState();

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
        checkState();

        return accountFactory.generateDefaultAccount(user, accountType, commonTime, isPrimary);
    }

    /**
     * Create a new user with given username and register it in the database
     *
     * @param username The username of the user
     * @return The default password for this user
     */
    public String createUser(String username) throws UsernameAlreadyExistException {
        checkState();

        String password = passwordGenerator.generatePassword();
        User newUser = userDatabase.registerNewUser(username, password);

        accountFactory.generateDefaultAccount(newUser, ChequingAccount.class, commonTime, true);

        return password;
    }

    public User validateUserLogin(String username, String password) {
        checkState();

        User user = null;

        try {
            user = userDatabase.loginUser(username, password);
        } catch (WrongPasswordException | UserNotExistException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    public boolean cancelLastTransaction(Account targetAccount) {
        checkState();

        Transaction transaction = targetAccount.getLastTransaction();

        if (transaction != null && transaction.isCancellable()) {
            transaction.cancel();
            return true;
        }

        return false;
    }

}
