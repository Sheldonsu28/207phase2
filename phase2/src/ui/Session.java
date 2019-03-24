package ui;

import account.*;
import atm.*;
import transaction.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Session {
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

                case SIGN_OUT_STATE:
                    state = State.WELCOME_STATE;
                    break;

                case MANAGER_STATE:
                    manage();
                    break;

            }
        }
    }

    private void initialize() {
        System.out.println("Bank manager initialization");

        while (!bankManager.hasLoggedin())
            managerLogin(console.getLoginInfo());

        Date date = console.getTime();
        bankManager.initialize(date, console);
        bankManager.logout();
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
                String[] userLogin = console.getLoginInfo();
                currUser = bankManager.validateUserLogin(userLogin[0], userLogin[1]);
                if (currUser != null) {
                    state = State.MAIN_STATE;
                }
                break;
            case 2: //  Bank manager login
                managerLogin(console.getLoginInfo());
                if (bankManager.hasLoggedin()) {
                    state = State.MANAGER_STATE;
                }
                break;
        }

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

            case 6: //open account
                boolean setPrimary = false;
                List<String> accounts = Menu.ACCOUNT_MENU.getMenuOptions();
                String requestAccount = accounts.get(console.displayMenu(Menu.ACCOUNT_MENU) - 1);
                if (requestAccount.equals("ChequingAccount")) {
                    setPrimary = console.setPrimary();
                }
                fileHandler.saveTo(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE,
                        String.format("%s %s %s", currUser.getUserName(), requestAccount, setPrimary));
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
                    e.printStackTrace();
                }
                break;

            case 2: // withdraw
                System.out.println("Select target withdraw account\n");
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
                System.out.println("Transfer from: \n");
                fromAccount = withdrawables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, withdrawables.toArray()) - 1);

                System.out.println("Transfer to: \n");
                toAccount = depositables.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, depositables.toArray()) - 1);


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
                while (true) {
                    System.out.println("Please enter new password");
                    String newPassword = console.getRawInput();

                    if (bankManager.isValidPassword(newPassword)) {
                        user.changePassword(newPassword);
                        break;
                    } else {
                        System.out.println("Invalid password. Please try again");
                    }
                }
                break;
            case 5:
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

    private void manage() {
        int choice = console.displayMenu(Menu.MANAGER_MENU);

        switch (choice) {
            case 1://read alerts
                for (String msg : fileHandler.readFrom(ExternalFiles.CASH_ALERT_FILE))
                    System.out.println(msg);
                break;

            case 2://Create user
                while (true) {
                    System.out.println("Enter desired username: ");

                    try {
                        System.out.println(String.format("Your password: %s",
                                bankManager.createUser(console.getRawInput())));
                    } catch (UsernameAlreadyExistException | UsernameOutOfRangeException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }

                    break;
                }
                break;
            case 3: //Read account creation request
                for (String msg : fileHandler.readFrom(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE))
                    System.out.println(msg);
                break;

            case 4: //Cancel recent transaction
                List<User> userList = bankManager.getAllUsers();
                User userSelect = userList.get(console.displayMenu(Menu.USER_SELECTION_MENU, userList.toArray()) - 1);
                ArrayList<Cancellable> userAccounts = userSelect.getAccountListOfType(Cancellable.class);
                Cancellable accountSelect = userAccounts.get(
                        console.displayMenu(Menu.ACCOUNT_SELECTION_MENU, userAccounts.toArray()) - 1);
                bankManager.cancelLastTransaction((Account) accountSelect);
                break;

            case 5: // Restock
                bankManager.restockMachine(console.getInputStock());
                break;

            case 6: //Create account
                ArrayList<String> requests = fileHandler.readFrom(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE);

                for (String request : requests) {
                    String[] info = request.split(" ");

                    try {
                        if (info.length != 3)
                            throw new IllegalFileFormatException(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE);

                        bankManager.createAccount(info[0], getValidAccountType(info[1]),
                                Boolean.parseBoolean(info[2]));

                    } catch (IllegalFileFormatException i) {
                        System.out.println(i.getMessage());
                    }
                }

                fileHandler.clearFile(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE);

                break;

            case 7:
                state = State.WELCOME_STATE;
                bankManager.logout();
                break;

        }
    }

    @SuppressWarnings("unchecked")
    private Class<Account> getValidAccountType(String classType) throws IllegalFileFormatException {

        Class<Account> result;

        try {
            result = (Class<Account>) Class.forName("account." + classType);
        } catch (ClassCastException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalFileFormatException(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE);
        }

        return result;
    }

}
