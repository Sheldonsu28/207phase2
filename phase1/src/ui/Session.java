package ui;

import account.*;
import atm.*;
import transaction.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Session {
    private Scanner response = new Scanner(System.in);
    private AtmMachine atm;
    private BankManager bankManager;
    private State state;
    private Console console = new Console();
    private Menu menu = new Menu();
    private User currUser;

    public Session(AtmMachine atm, BankManager m)
    {
        this.atm = atm;
        if (m == null) {
            bankManager = new BankManager();
            state = State.INITIALIZE_STATE;
        } else {
            state = State.WELCOME_STATE;
        }
    }

    /** Perform the Session Use Case
     */
    public void performSession()
    {

        while (state != State.FINAL_STATE) {
            int choice;
            switch (state) {
                case INITIALIZE_STATE:
                    if (initialize(bankManager)) state = State.WELCOME_STATE;
                    break;

                case WELCOME_STATE:
                    currUser = welcome(bankManager);
                    if (currUser != null) state = State.MAIN_STATE;
                    break;

                case SIGN_IN_STATE:
                    String[] userInfo = signIn().split(",");
                    //what if incorrect info
                    currUser = bankManager.validateUserLogin(userInfo[0], userInfo[1]);
                    if (currUser != null) state = State.MAIN_STATE;
                    break;

                case MAIN_STATE:
                    choice = console.displayMenu(null, menu.main());
                    if (choice > 0 && choice < 4) {
                        Transaction currentTransaction = createTransaction(currUser, atm, choice);
                        if (currentTransaction != null) state = State.PERFORM_TRANSACTION_STATE;
                    } else if (choice == 4) {
                        state = State.ACCOUNT_INFO_STATE;
                    }
                    break;

                case ACCOUNT_INFO_STATE:
                    getAccountInfo(currUser);

                case PERFORM_TRANSACTION_STATE:
                    //TODO how to get the accounts elegantly
                    break;

                case SIGN_OUT_STATE:
                    state = State.FINAL_STATE;
                    break;
            }
        }
    }

    private boolean initialize(BankManager m) {
        boolean setDate = false;
        System.out.println("Initialize Page");
        String[] logInfo = signIn().split(",");
        try {
            m.login(logInfo[0], logInfo[1]);
            System.out.println("Enter Time: yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            while (!setDate) {
                try {
                    Date date = format.parse(response.nextLine());
                    m.initialize(date);
                    setDate = true;
                } catch (ParseException e) {
                    System.out.println("Incorrect format. Try again");
                }
            }
        } catch (WrongPasswordException|UserNotExistException e) {
            System.out.println(e.getMessage());
        }
        return setDate;
    }

    private User welcome(BankManager m) {
        User user = null;
        int choice = console.displayMenu("Welcome", menu.welcome());
        switch (choice) {
            case 1:
                String[] logInfo = signIn().split(",");
                user = m.validateUserLogin(logInfo[0], logInfo[1]);
                if (user != null) break;
            case 2:
                System.out.println("Enter new passwords");
                String newPasswords = response.nextLine();
                //TODO handel NullPointer Exception
                try {
                    user.changePassword(newPasswords);
                } catch (NullPointerException npe) {
                    System.out.println(npe.getMessage());
                }
            case 3:
                System.out.println("Enter a new user name");
                String newUserName = response.nextLine();
                try {
                    String password = m.createUser(newUserName);
                    System.out.println("Your temporary password" + password);
                    //this way if it will be continued if the user name is not entered correctly
                    break;
                } catch (UsernameAlreadyExistException e) {
                    System.out.println(e.getMessage());
            }
            case 4:
                //TODO reset is the same as initialize??
        }
        return user;
    }

    private String signIn() {
        System.out.println("Enter username:");
        String userName = response.nextLine();
        System.out.println("Enter password:");
        String password = response.nextLine();
        return userName+","+password;
    }

    private Transaction createTransaction(User user, AtmMachine atm, int choice) {
        ArrayList<Depositable> userDepositableAccounts;
        ArrayList<Withdrawable> userWithdrawableAccounts;
        int toAccountChoice;
        int fromAccountChoice;
        int amount;
        switch (choice) {
            case 1:
                //directly to primal account
                userDepositableAccounts = user.getAccountListOfType(Depositable.class);
                toAccountChoice = console.displayMenu("Select a account", userDepositableAccounts);
                //return new DepositTransaction(user, atm, userDepositableAccounts.get(toAccountChoice - 1));
            case 2:
                userWithdrawableAccounts = user.getAccountListOfType(Withdrawable.class);
                fromAccountChoice = console.displayMenu("Select a account", userWithdrawableAccounts);
                amount = console.getAmount();
                return new WithdrawTransaction(user, atm, userWithdrawableAccounts.get(fromAccountChoice-1), amount);
            case 3:
                userWithdrawableAccounts = user.getAccountListOfType(Withdrawable.class);
                fromAccountChoice = console.displayMenu("Select a account", userWithdrawableAccounts);
                userDepositableAccounts = user.getAccountListOfType(Depositable.class);
                toAccountChoice = console.displayMenu("Select a account", userDepositableAccounts);
                amount = console.getAmount();
                return new TransferTransaction(user, userWithdrawableAccounts.get(fromAccountChoice-1), userDepositableAccounts.get(toAccountChoice-1), amount);
        }
        return null;
    }

    private void getAccountInfo(User user) {
        int choice = console.displayMenu(null, menu.getAccountInfo());
        switch (choice) {
            case 1:
                System.out.println(user.getAccountsSummary());
                break;
            case 2:
                System.out.println(user.getNetTotal());
                break;
            case 3:
                getMostRecentTransaction(user);
                break;
            case 4:
                getCreationDate(user);
                break;
        }
    }

    private void getMostRecentTransaction(User user) {
        int choice = console.displayMenu("Select A account", user.getAccountListOfType(Cancellable.class));
        //TODO how to get the recent transaction
    }

    private void getCreationDate(User user) {
        int choice = console.displayMenu("Select A account", user.getAccountListOfType(Cancellable.class));
        //TODO how to get the creation date
    }
}
