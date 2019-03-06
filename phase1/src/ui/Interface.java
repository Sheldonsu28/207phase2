package ui;

import account.*;
import atm.*;
import transaction.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Interface {
    private Scanner response = new Scanner(System.in);
    private BankManager bankManager;
    boolean goBack;
    private boolean exit;

    public Interface(BankManager m) {
        bankManager = m;
    }

    public void activateInterface() {
        if (bankManager == null) {
            boolean initialized = false;
            boolean setDate = false;
            bankManager = new BankManager();

            //initialize page
            while (!initialized) {
                String[] logInfo = initializePage().split(",");
                try {
                    bankManager.login(logInfo[0], logInfo[1]);
                    System.out.println("Enter Time: yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    while (!setDate) {
                        try {
                            Date date = format.parse(response.nextLine());
                            bankManager.initialize(date);
                            setDate = true;
                        } catch (ParseException e) {
                            System.out.println("Incorrect format. Try again");
                        }
                    }
                    initialized = true;
                } catch (WrongPasswordException e) {
                    System.out.println("The password doesn't match. Try again.");
                } catch (UserNotExistException i) {
                    System.out.println("The user doesn't exist. Try again");
                }
            }

        }

        while (!exit) {
            //Welcome page
            String intent = welcomePage();

            if (intent.equals("yes")) {
                String[] currUser = signInPage().split(",");
                //what if incorrect info
                User user = bankManager.validateLogin(currUser[0], currUser[1]);

                if (user != null) {
                    Outer:
                    while (true) {
                        //Homepage
                        String action = homePage();
                        switch (action) {
                            case "1":
                                depositMoneyTypePage(user);
                            case "2":
                                String withdrawAccount = withdrawPage(user);
                            case "3":
                                String transferAccount = transferPage();
                            case "4":
                                String info = accountInfoPage();
                            case "-2":
                                break Outer;
                        }
                    }
                }
            }
        }
    }

    //TODO different User has different amount accounts
    //initialize page
    private String initializePage() {
        System.out.println("Initialize Page");
        System.out.println("Enter username:");
        String userName = response.nextLine();
        System.out.println("Enter password:");
        String password = response.nextLine();
        return userName+","+password;
    }


    private String welcomePage() {
        System.out.println("welcome");
        System.out.println("Sign in\tyes/no");
        return response.nextLine();
    }

    private String signInPage() {
        System.out.println("Enter username:");
        String userName = response.nextLine();
        System.out.println("Enter password:");
        String password = response.nextLine();
        return userName+","+password;
    }


    private String homePage() {
        System.out.println("Deposit\t1\n" +
                            "Withdraw\t2\n" +
                            "Transfer\t3\n" +
                            "Info\t4\n" +
                            "Sign out\t-2\n");
        return response.nextLine();
    }

    //deposit
    private void depositMoneyTypePage(User u) {
        System.out.println("Check\t1\n" +
                            "Cash\t2\n" +
                            "Previous\t-1\n" +
                            "Home page\t0\n");
        String moneyType = response.nextLine();

        if (moneyType.equals("1")) {
            //TODO
        } else if (moneyType.equals("2")) {
            int money = amountPage();
            ArrayList depositableAccounts = u.getAccountListOfType(Depositable.class);
            for (Object obj: depositableAccounts) {
                if (obj instanceof ChequingAccount) {
                    DepositTransaction newDeposit = new DepositTransaction(user, obj, money);
                    //What to write (From) when its deposit
                    if (confirmPage(,obj,money, newDeposit)) thankyouPage();
                }
             }
        }
    }

    private int amountPage() {
        System.out.println("Deposit amount\n +" +
                            "Previous\t-1\n" +
                            "Home page\t0\n");
        return convertStr(response.nextLine());
    }

    //TODO whats the required info
    private String depositCheckPage() {
        return response.nextLine();
    }

    //TODO different transfer type and deposit type
    private boolean confirmPage(Account f, Account t, int amount, Transaction u) {
        boolean result = false;
        String output = String.format("From: %s To: %s Amount: $d", f.toString(), t.toString(), amount);
        System.out.println(output);
        System.out.println("Confirm\t1");
        String decision = response.nextLine();
        if (decision.equals("1")) {
            //result = u.doPerferm();
        }
        return result;
    }

    private String thankyouPage() {
        System.out.println("Successful!\n" +
                            "Home page\t0\n" +
                            "Sign out\t-2\n");
        return response.nextLine();
    }

    //withdraw
    private String withdrawPage(User u) {
        System.out.println("Select a account\n");
        ArrayList withdrawableAccounts = u.getAccountListOfType(Withdrawable.class);
        int idx = 1;
        for (Object obj : withdrawableAccounts) {
            System.out.println(obj + Integer.toString(idx));
            idx += 1;
        }
        System.out.println("Previous\t-1\n" + "Homepage\t0\n");
        return response.nextLine();
    }

    //transfer
    private String transferPage() {
        System.out.println("Between My Accounts\1\n" +
                "Pay Bill\2\n" +
                "Previous\t-1\n" +
                "Homepage\t0\n");
        return response.nextLine();
    }

    //what's is transferable accounts
    private String transferFromPage() {
        System.out.println("Select a account\n" +
                "SAVINGS ACCOUNT\t1\n" +
                "CHEQUING ACCOUNT\t2\n" +
                "LINE OF CREDIT ACCOUNT\t3\n" +
                "Previous\t-1\n" +
                "Homepage\t0\n");
        return response.nextLine();
    }

    private String transferToPage() {
        System.out.println("Select a account\n" +
                "SAVINGS ACCOUNT\t1\n" +
                "CHEQUING ACCOUNT\t2\n" +
                "LINE OF CREDIT ACCOUNT\t3\n" +
                "CREDIT CARDS ACCOUNT" +
                "Previous\t-1\n" +
                "Homepage\t0\n");
        return response.nextLine();
    }

    //TODO pay bill required info
    private String payBillPage() {
        return response.nextLine();
    }

    //Account Info
    private String accountInfoPage() {
        System.out.println("View account balance\t1\n" +
                "Net total\t2\n" +
                "Most recent transaction\t3\n" +
                "Accounts creation date\t4\n" +
                "Previous\t-1\n" +
                "Homepage\t0\n");
        return response.nextLine();
    }

    private String accountBalancePage() {
        System.out.println("Select account\n" +
                "SAVINGS ACCOUNT\t1\n" +
                "CHEQUING ACCOUNT\t2\n" +
                "LINE OF CREDIT ACCOUNT\t3\n" +
                "CREDIT CARDS ACCOUNT" +
                "Previous\t-1\n" +
                "Homepage\t0\n");
        return response.nextLine();
    }

    private String creationDatePage() {
        System.out.println("Select account\n" +
                "SAVINGS ACCOUNT\t1\n" +
                "CHEQUING ACCOUNT\t2\n" +
                "LINE OF CREDIT ACCOUNT\t3\n" +
                "CREDIT CARDS ACCOUNT" +
                "Previous\t-1\n" +
                "Homepage\t0\n");
        return response.nextLine();
    }

    private int convertStr(String s) {
        int amount = 0;
        boolean correct = false;
        while (!correct) {
            try {
                amount = Integer.parseInt(s);
                correct = true;
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter number correctly");
            }
        }
        return amount;
    }



//    public static void main(String[] args) {
//        BankManager m = new BankManager();
//        new Interface(m).confirmPage("a", "b", 100);
//
//    }
}