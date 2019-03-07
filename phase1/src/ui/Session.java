package ui;

import account.Account;
import account.Depositable;
import account.Withdrawable;
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

        Transaction currentTransaction = null;

        while (state != State.FINAL_STATE) {
            switch (state) {
                case INITIALIZE_STATE:
                    if (initialize(bankManager)) state = State.WELCOME_STATE;
                    break;

                case WELCOME_STATE:
                    System.out.println("welcome");
                    System.out.println("Sign in\tyes/no");
                    if (response.nextLine().equals("yes")) state = State.SIGN_IN_STATE;
                    break;

                case SIGN_IN_STATE:
                    String[] userInfo = signIn().split(",");
                    //what if incorrect info
                    currUser = bankManager.validateUserLogin(userInfo[0], userInfo[1]);
                    if (currUser != null) state = State.MAIN_STATE;
                    break;

                case MAIN_STATE:
                    int choice = console.displayMenu(null, menu.mainMenu());
                    if (choice > 0 && choice < 4) {
                        if (createTransaction(currUser, atm, choice) != null) state = State.PERFORM_TRANSACTION_STATE;
                    } else if (choice == 4) {
                        //TODO handle info
                    }
                    break;

                case PERFORM_TRANSACTION_STATE:
                    break;

                case SIGN_OUT_STATE:
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
        } catch (WrongPasswordException e) {
            System.out.println("The password doesn't match. Try again.");
        } catch (UserNotExistException i) {
            System.out.println("The user doesn't exist. Try again");
        }
        return setDate;
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

}
