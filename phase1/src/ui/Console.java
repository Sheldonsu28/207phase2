package ui;

import java.util.ArrayList;
import java.util.Scanner;

class Console {
    private Scanner response = new Scanner(System.in);
    //TODO null exception
    int displayMenu(String title, ArrayList menu) {
        System.out.println(title);
        for (int i = 0; i < menu.size(); i ++)
            System.out.println((i+1) + ") " + menu.get(i));
        if (title.equals("MAIN PAGE")) {
            System.out.println("-2) Log out");
        } else if (title.equals("WELCOME")) {
            System.out.println("-3) Reset atm");
        } else if (title.equals("Select a account")){
            System.out.println("-1) Previous");
            //System.out.println("0) Homepage");
        } else {
            System.out.println("-4) Cancel");
        }
        return convertStr(response.nextLine());
    }

    int getAmount() {
        System.out.println("Please enter amount of money");
        return convertStr(response.nextLine());
    }

    String enterPayee() {
        System.out.println("Please enter the receiving account");
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

}
