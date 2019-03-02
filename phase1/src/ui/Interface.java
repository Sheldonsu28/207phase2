package ui;
import atm.User;
import atm.AtmMachine;
import atm.BankManager;
import java.util.Scanner;
public class Interface {

    //AtmMachine newAtmMachine = new AtmMachine();
    //BankManager newBankManager = new BankManager();

    void activateInterface() {
        Scanner response = new Scanner(System.in);

        //Welcome page
        System.out.println("welcome");
        System.out.println("Sign in\tyes/no");
        String intent = response.nextLine();

        //User sign in
        if (intent.equals("yes")){
            System.out.println("Enter username:");
            String userName = response.nextLine();
            System.out.println("Enter password:");
            String password = response.nextLine();

        }

    }

}
