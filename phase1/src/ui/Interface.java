package ui;
import atm.User;
import atm.BankManager;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.Scanner;

public class Interface {
    private Scanner response = new Scanner(System.in);
    BankManager newBankManager = new BankManager();
    boolean goBack;
    private boolean exit;

    void activateInterface() {
        while (!exit) {
            //Welcome page
            String intent = welcomePage();

            if (intent.equals("yes")) {
                String[] currUser = signInPage().split(",");
                User user = newBankManager.validateLogin(currUser[0], currUser[1]);

                if (user != null) {
                    Outer:
                    while (true) {
                        //Homepage
                        String action = homePage();
                        switch (action) {
                            case "1":
                                String moneyType = depositMoneyTypePage();
                                //TODO
                                if (moneyType.equals("1")) {
                                    continue;
                                } else if (moneyType.equals("2")) {
                                    String moneyAmount = amountPage();
                                }
                            case "2":
                                String withdrawAccount = withdrawPage();
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
    private String depositMoneyTypePage() {
        System.out.println("Check\t1\n" +
                            "Cash\t2\n" +
                            "Previous\t-1\n" +
                            "Home page\t0\n");
        return response.nextLine();
    }

    private String amountPage() {
        System.out.println("Deposit amount\n +" +
                            "Previous\t-1\n" +
                            "Home page\t0\n");
        return response.nextLine();
    }

    //TODO whats the required info
    private String depositCheckPage() {
        return response.nextLine();
    }

    //TODO different transfer type and deposit type
    private String confirmPage(String s) {
        return response.nextLine();
    }

    private String thankyouPage() {
        System.out.println("Successful!\n" +
                            "Home page\t0\n" +
                            "Sign out\t-2\n");
        return response.nextLine();
    }

    //withdraw
    private String withdrawPage() {
        System.out.println("Select a account\n" +
                "SAVINGS ACCOUNT\t1\n" +
                "CHEQUING ACCOUNT\t2\n" +
                "Previous\t-1\n" +
                "Homepage\t0\n");
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


//    public static void main(String[] args) {
//        new Interface().homePage();
//        new Interface().depositMoneyTypePage();
//
//    }
}