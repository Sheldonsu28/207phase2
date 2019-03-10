package ui;

import account.Account;
import account.BillingAccount;
import account.Depositable;
import account.Withdrawable;
import atm.*;
import transaction.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Session {
    private Scanner response;
    private AtmMachine atm;
    private BankManager bankManager;
    private State state;
    private Console console;
    private User currUser;
    private FileHandler fileHandler;

    public Session(BankManager bankManager) {
        if (bankManager == null) {
            this.bankManager = new BankManager();
            //  phase 1 only 1 machine
            atm = this.bankManager.getMachineList().get(0);
            state = State.INITIALIZE_STATE;
        } else {
            this.bankManager = bankManager;
            //  phase 1 only 1 machine
            atm = this.bankManager.getMachineList().get(0);
            state = State.WELCOME_STATE;
        }
        console = new Console();
        response = new Scanner(System.in);
        fileHandler = new FileHandler();
    }

    /**
     * Perform the Session Use Case
     */
    public void performSession() {
        while (state != State.FINAL_STATE) {
            switch (state) {
                case INITIALIZE_STATE:
                    initialize();
                    if (bankManager.hasInitialized())
                        state = State.WELCOME_STATE;
                    break;

                case WELCOME_STATE:
                    welcome();
                    break;

                case MAIN_STATE:
                    handleMain();
                    break;

//                case ACCOUNT_INFO_STATE:
//                    getAccountInfo(currUser);
//                    break;
//
//                case PERFORM_TRANSACTION_STATE:
//                    if (currentTransaction == null)
//                        throw new IllegalStateException("This should not be invoked before transaction initialization");
//                    currentTransaction.perform();
//                    state = State.MAIN_STATE;
//                    break;

                case SIGN_OUT_STATE:
                    state = State.WELCOME_STATE;
                    break;

                case MANAGER_STATE:
                    manage(bankManager);
                    break;

            }
        }
    }

    private void initialize() {
        System.out.println("Bank manager initialization");

        while (!bankManager.hasLoggedin())
            managerLogin(signIn());

        String formatString = AtmTime.FORMAT_STRING;

        SimpleDateFormat format = new SimpleDateFormat(formatString);

        System.out.printf("Enter Time in format: %s", formatString);

        while (!bankManager.hasInitialized()) {
            try {
                Date date = format.parse(response.nextLine());
                bankManager.initialize(date);
            } catch (ParseException e) {
                System.out.println("Incorrect format. Try again");
            }
        }

        //  TEST CODE
        try {
            System.out.println(bankManager.createUser("snowsr"));
        } catch (UsernameAlreadyExistException e) {
            e.printStackTrace();
        }
    }

    private void managerLogin(String[] loginInfo) {
        try {
            bankManager.login(loginInfo[0], loginInfo[1]);
        } catch (UserNotExistException | WrongPasswordException e) {
            System.out.println(e.getMessage());
        }
    }

    private void welcome() {
        int choice = console.displayMenu(Menu.WELCOME_MENU);

        switch (choice) {

            case 1: //  Login
                String[] userLogin = signIn();
                currUser = bankManager.validateUserLogin(userLogin[0], userLogin[1]);
                if (currUser != null) {
                    state = State.MAIN_STATE;
                    break;
                }
            case 2: //  Bank manager login
                managerLogin(signIn());
                if (bankManager.hasLoggedin()) {
                    state = State.MANAGER_STATE;
                    break;
                }
        }

    }

    private String[] signIn() {
        System.out.println("Enter username:");
        String username = response.nextLine();
        System.out.println("Enter password:");
        String password = response.nextLine();
        return new String[]{username, password};

    }

    private void handleMain() {
        int choice = console.displayMenu(Menu.MAIN_MENU);
        switch (choice) {
            case 1: //create transaction
            case 2:
            case 3:
            case 4:
                Transaction currentTransaction = createTransaction(currUser, atm, choice);
                if (currentTransaction != null) {
                    currentTransaction.perform();
                }
                break;

            case 5:
                getAccountInfo(currUser);
                break;

            case 6: //create account
                console.displayMenu(Menu.ACCOUNT_MENU);
                break;

            case 7:
                state = State.WELCOME_STATE;
                break;
        }
    }


    private Transaction createTransaction(User user, AtmMachine atm, int choice) {
        ArrayList<Depositable> depositables = user.getAccountListOfType(Depositable.class);
        ArrayList<Withdrawable> withdrawables = user.getAccountListOfType(Withdrawable.class);
        int amount;
        Withdrawable fromAccount;
        Depositable toAccount;
        int inputChoice;

        switch (choice) {
            case 1: //deposit
                System.out.println("Select target deposit account\n");
                toAccount = depositables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, depositables.toArray()) - 1);
                try {
                    return new DepositTransaction(user, atm, toAccount);
                } catch (IllegalFileFormatException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case 2: // withdraw
                fromAccount = withdrawables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, withdrawables.toArray()) - 1);

                amount = console.getAmount();
                inputChoice = confirmation(String.format("WITHDRAW $%d FROM %s\n", amount, fromAccount));

                if (inputChoice == 1)
                    return new WithdrawTransaction(user, atm, fromAccount, amount);
                else
                    state = State.MAIN_STATE;
                break;

            case 3: //transfer
                toAccount = depositables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, depositables.toArray()) - 1);
                fromAccount = withdrawables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, withdrawables.toArray()) - 1);

                amount = console.getAmount();
                inputChoice = confirmation(
                        String.format("FROM %s\nTRANSFER $%d TO %s\n", fromAccount, amount, toAccount));

                if (inputChoice == 1)
                    return new TransferTransaction(user, fromAccount, toAccount, amount);
                else
                    state = State.MAIN_STATE;
                break;

            //pay bill
            case 4:
                fromAccount = withdrawables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, withdrawables.toArray()) - 1);
                List<BillingAccount> payeeList = bankManager.getPayeeList();
                BillingAccount payeeAccount = payeeList.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, payeeList.toArray()) - 1);

                amount = console.getAmount();
                inputChoice = confirmation(
                        String.format("FROM %s\nPAY $%d BILL TO %s\n", fromAccount, amount, payeeAccount));

                if (inputChoice == 1)
                    return new PayBillTransaction(user, fromAccount, payeeAccount, amount);
                else
                    state = State.MAIN_STATE;
                break;

            case 5:
                state = State.MAIN_STATE;
                break;
        }

        return null;
    }

    private int confirmation(String msg) {
        return console.displayMenu(Menu.CONFIRM_MENU, new Object[]{msg});
    }

    private void getAccountInfo(User user) {
        int choice = console.displayMenu(Menu.ACCOUNT_INFO_MENU);
        switch (choice) {
            case 1:
                System.out.println(user.getAccountsSummary());
                break;

            case 2:
                System.out.printf("Net total: %.2f", user.getNetTotal());
                break;

            case 3:
                getTransactions(user);
                break;

            case 4:
                state = State.MAIN_STATE;
                break;
        }
    }

    private void getTransactions(User user) {
        ArrayList<Account> accounts = user.getAllAccounts();
        int choice = console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, accounts.toArray());

        Account selectAccount = accounts.get(choice - 1);

        System.out.println(selectAccount.getTransactions());
    }

    private void manage(BankManager manager) {
        int choice = console.displayMenu(Menu.MANAGER_MENU);
        String path = fileHandler.getPath();
        FileInputStream alerts;
        switch (choice) {
            case 1://read alerts
                try {
                    alerts = new FileInputStream(path + "alert.txt");
                    System.out.println(alerts);
                } catch (FileNotFoundException e) {
                    e.getMessage();
                }
                break;
            case 2://Create user
                boolean legalName = false;
                while (!legalName) {
                    String userName = response.nextLine();
                    if (userName != null) legalName = true;
                    try {
                        manager.createUser(userName);
                    } catch (UsernameAlreadyExistException e) {
                        legalName = false;
                        e.getMessage();
                    }
                }
                break;
            case 3: //Read account creation request
                break;
            case 4: //Cancel recent transaction
                //manager.cancelLastTransaction();
                break;
            case 5: // Restock


        }
    }

}
