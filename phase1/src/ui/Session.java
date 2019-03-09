package ui;

import account.Account;
import account.Depositable;
import account.Withdrawable;
import atm.*;
import transaction.Transaction;
import transaction.TransferTransaction;
import transaction.WithdrawTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Session {
    private Scanner response;
    private AtmMachine atm;
    private BankManager bankManager;
    private State state;
    private Console console;
    private User currUser;

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
    }

    /**
     * Perform the Session Use Case
     */
    public void performSession() {
        Transaction currentTransaction = null;
        while (state != State.FINAL_STATE) {
            int choice;
            switch (state) {
                case INITIALIZE_STATE:
                    initialize();
                    if (bankManager.hasInitialized())
                        state = State.WELCOME_STATE;
                    break;

                case WELCOME_STATE:
                    welcome();
                    if (currUser != null)
                        state = State.MAIN_STATE;
                    break;

                case MAIN_STATE:
                    choice = console.displayMenu(Menu.MAIN_MENU);
                    if (choice > 0 && choice < 5) {
                        currentTransaction = createTransaction(currUser, atm, choice);
                        if (currentTransaction != null) state = State.PERFORM_TRANSACTION_STATE;
                    } else if (choice == 5) {
                        state = State.ACCOUNT_INFO_STATE;
                    } else if (choice == -2) state = State.WELCOME_STATE;
                    break;

                case ACCOUNT_INFO_STATE:
                    getAccountInfo(currUser);
                    break;

                case PERFORM_TRANSACTION_STATE:
                    if (currentTransaction == null)
                        throw new IllegalStateException("This should not be invoked before transaction initialization");
                    currentTransaction.perform();
                    break;

                case SIGN_OUT_STATE:
                    state = State.WELCOME_STATE;
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
                break;

            case 2: //  Bank manager login
                managerLogin(signIn());
                break;
        }

    }

    private String[] signIn() {
        System.out.println("Enter username:");
        String username = response.nextLine();
        System.out.println("Enter password:");
        String password = response.nextLine();
        return new String[]{username, password};

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
                //TODO deposit checks and cash
                System.out.println("Select target deposit account");
                toAccount = depositables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, depositables.toArray()) - 1);
                //return new DepositTransaction(user, atm, deposit.get(toAccountChoice - 1));

            case 2: // withdraw
                fromAccount = withdrawables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, withdrawables.toArray()) - 1);

                amount = console.getAmount();
                inputChoice = console.displayMenu(Menu.CONFIRM_MENU,
                        new Object[]{String.format("FROM %s\nWITHDRAW %d\n", fromAccount, amount)});

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
                inputChoice = console.displayMenu(Menu.CONFIRM_MENU,
                        new Object[]{String.format("FROM %s\nWITHDRAW %d\n", fromAccount, amount)});

                if (inputChoice == 1)
                    return new TransferTransaction(user, fromAccount, toAccount, amount);
                else
                    state = State.MAIN_STATE;
                break;

            //pay bill
            case 4:
                fromAccount = withdrawables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, withdrawables.toArray()) - 1);
                //TODO how the user inputChoice payee account

            case 5:
                state = State.MAIN_STATE;
                break;
        }

        return null;
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

}
