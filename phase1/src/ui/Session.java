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

    public Session(BankManager m)
    {
        if (m == null) {
            bankManager = new BankManager();
            atm = bankManager.getMachineList().get(0);
            state = State.INITIALIZE_STATE;
        } else {
            //TODO null pointer
            atm = bankManager.getMachineList().get(0);
            state = State.WELCOME_STATE;
        }
    }

    /** Perform the Session Use Case
     */
    public void performSession()
    {
        Transaction currentTransaction = null;
        while (state != State.FINAL_STATE) {
            int choice;
            switch (state) {
                case INITIALIZE_STATE:
                    initialize(bankManager);
                    if (bankManager.hasInitialized()) state = State.WELCOME_STATE;
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
                    choice = console.displayMenu("MAIN PAGE", menu.main());
                    if (choice > 0 && choice < 5) {
                        currentTransaction = createTransaction(currUser, atm, choice);
                        if (currentTransaction != null) state = State.PERFORM_TRANSACTION_STATE;
                    } else if (choice == 5) {
                        state = State.ACCOUNT_INFO_STATE;
                    } else if (choice == -2) state = State.WELCOME_STATE;
                    break;

                case ACCOUNT_INFO_STATE:
                    getAccountInfo(currUser);

                case PERFORM_TRANSACTION_STATE:
                    currentTransaction.perform();
                    break;

                case SIGN_OUT_STATE:
                    state = State.WELCOME_STATE;
                    break;
            }
        }
    }

    private void initialize(BankManager m) {
        System.out.println("Initialize Page");
        String[] logInfo = signIn().split(",");
        try {
            m.login(logInfo[0], logInfo[1]);
            System.out.println("Enter Time: yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            while (!m.hasInitialized()) {
                try {
                    Date date = format.parse(response.nextLine());
                    m.initialize(date);
                } catch (ParseException e) {
                    System.out.println("Incorrect format. Try again");
                }
            }
        } catch (WrongPasswordException|UserNotExistException e) {
            System.out.println(e.getMessage());
        }
    }

    private User welcome(BankManager m) {
        User user = null;
        int choice = console.displayMenu("WELCOME", menu.welcome());
        switch (choice) {
            //Sign in
            case 1:
                String[] logInfo = signIn().split(",");
                user = m.validateUserLogin(logInfo[0], logInfo[1]);
                if (user != null) break;
            //Change password
            case 2:
                System.out.println("Enter new passwords");
                String newPasswords = response.nextLine();
                //TODO handel NullPointer Exception
                try {
                    user.changePassword(newPasswords);
                    break;
                } catch (NullPointerException npe) {
                    System.out.println(npe.getMessage());
                }
            //Create a new account
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
            //Bank manager set the atm
            case -3:
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
        int select;
        Withdrawable fromAccount;
        Depositable toAccount;
        String title;
        switch (choice) {
            //deposit
            case 1:
                //TODO deposit checks and cash
                userDepositableAccounts = user.getAccountListOfType(Depositable.class);
                toAccountChoice = console.displayMenu("Select a account", userDepositableAccounts);
                //return new DepositTransaction(user, atm, userDepositableAccounts.get(toAccountChoice - 1));
            //withdraw
            case 2:
                userWithdrawableAccounts = user.getAccountListOfType(Withdrawable.class);
                fromAccountChoice = console.displayMenu("Select a account", userWithdrawableAccounts);
                fromAccount = userWithdrawableAccounts.get(fromAccountChoice-1);

                amount = console.getAmount();
                title = "From" + fromAccount.toString() + "Withdraw" + amount;
                select = console.displayMenu(title, menu.confirmMenu());
                if (select == 1) return new WithdrawTransaction(user, atm, userWithdrawableAccounts.get(fromAccountChoice-1), amount);
                else state = State.MAIN_STATE;
                break;
            //transfer
            case 3:
                userWithdrawableAccounts = user.getAccountListOfType(Withdrawable.class);
                fromAccountChoice = console.displayMenu("Select a account", userWithdrawableAccounts);
                fromAccount = userWithdrawableAccounts.get(fromAccountChoice-1);

                userDepositableAccounts = user.getAccountListOfType(Depositable.class);
                toAccountChoice = console.displayMenu("Select a account", userDepositableAccounts);
                toAccount = userDepositableAccounts.get(toAccountChoice-1);

                amount = console.getAmount();
                title = "From" + fromAccount.toString() + "transfer to" + toAccount.toString() + amount;
                select = console.displayMenu(title, menu.confirmMenu());
                if (select == 1) {
                    return new TransferTransaction(user, userWithdrawableAccounts.get(fromAccountChoice-1),
                        userDepositableAccounts.get(toAccountChoice-1), amount);
                } else {
                    state = State.MAIN_STATE;
                    break;
                }

            //pay bill
            case 4:
                userWithdrawableAccounts = user.getAccountListOfType(Withdrawable.class);
                fromAccountChoice = console.displayMenu("Select a account", userWithdrawableAccounts);
                //TODO how the user select payee account
            case -1:
                state = State.MAIN_STATE;
        }

        return null;
    }

    private void getAccountInfo(User user) {
        int choice = console.displayMenu("INFORMATION", menu.getAccountInfo());
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
            case 5:
                //TODO create accounts
            case -1:
                state = State.MAIN_STATE;
        }
    }

    private void getMostRecentTransaction(User user) {
        ArrayList<Cancellable> accounts = user.getAccountListOfType(Cancellable.class);
        int choice = console.displayMenu("Select A account", accounts);
        Account selectAccount = (Account)accounts.get(choice - 1);
        System.out.println(selectAccount.getLastTransaction());
        System.out.println("Request to cancel the last transaction");
        //TODO request to cancel the last transaction

    }

    private void getCreationDate(User user) {
        ArrayList<Cancellable> accounts = user.getAccountListOfType(Cancellable.class);
        int choice = console.displayMenu("Select A account", accounts);
        Account selectAccount = (Account)accounts.get(choice - 1);
        System.out.println(selectAccount.getTimeCreated());

    }

}
