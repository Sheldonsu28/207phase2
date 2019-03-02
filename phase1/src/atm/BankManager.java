package atm;

import account.Account;

import java.io.Serializable;
import java.util.*;

public class BankManager implements Observer, Serializable {
    private AtmTime commonTime;
    private List<AtmMachine> machineList;
    private AccountFactory accountFactory;
    private UserDatabase userDatabase;
    private RandomPasswordGenerator passwordGenerator;

    private boolean hasInitialized;

    BankManager() {
        machineList = new ArrayList<>();
        userDatabase = new UserDatabase();
        accountFactory = new AccountFactory();
        hasInitialized = false;
        passwordGenerator = new RandomPasswordGenerator(12, 24,
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()");
    }

    public void initialize(Date initialDate) {
        commonTime = new AtmTime(initialDate);

        //  TODO more initialization

        hasInitialized = true;
    }

    private void checkState() {
        if (!hasInitialized)
            throw new IllegalStateException("Manager not yet initialized!");
    }


    public AtmMachine addMachine() {
        checkState();

        TreeMap<Integer, Integer> initialStock = new TreeMap<>();
        initialStock.put(5, 500);
        initialStock.put(10, 500);
        initialStock.put(20, 500);
        initialStock.put(50, 500);

        AtmMachine machine = new AtmMachine(this, initialStock);
        machineList.add(machine);
        return machine;
    }

    public List<AtmMachine> getMachineList() {
        return Collections.unmodifiableList(machineList);
    }

    public <T extends Account> boolean createAccount(User user, Class<T> accountType) {
        checkState();

        return accountFactory.generateDefaultAccount(user, accountType, commonTime.getCurrentTime());
    }

    /**
     * Create a new user with given username and register it in the database
     *
     * @param username The username of the user
     * @return The default password for this user
     */
    public String createUser(String username) {
        checkState();

        String password = passwordGenerator.generatePassword();
        userDatabase.registerNewUser(username, password);

        return password;
    }

    public User validateLogin(String username, String password) {
        User user = null;

        try {
            user = userDatabase.loginUser(username, password);
        } catch (WrongPasswordException | UserNotExistException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
