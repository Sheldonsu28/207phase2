package atm;

import account.Account;

import java.io.Serializable;
import java.util.*;

public class BankManager implements Observer, Serializable {
    private AtmTime commonTime;
    private List<AtmMachine> machineList;
    private UserDatabase userDatabase;
    private RandomPasswordGenerator passwordGenerator;

    private boolean hasInitialized;

    BankManager() {
        machineList = new ArrayList<>();
        userDatabase = new UserDatabase();
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

    AtmMachine addMachine() {
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

    private <T extends Account> T generateDefaultAccount(Class<T> accountType) {
        T account = null;

        try {
            account = accountType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.out.println("Dont pass abstract!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("Constructor access error!");
        }

        return account;
    }

    <T extends Account> boolean createAccount(String username, Class<T> accountType) {
        checkState();

        T defaultAccount = generateDefaultAccount(accountType);

        if (defaultAccount != null) {
            userDatabase.getUser(username).addAccount(accountType, defaultAccount);
            return true;
        }

        return false;
    }

    /**
     * Create a new user with given username and register it in the database
     *
     * @param username The username of the user
     * @return The default password for this user
     */
    String createUser(String username) {
        checkState();

        String password = passwordGenerator.generatePassword();
        userDatabase.registerNewUser(username, password);

        return password;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
