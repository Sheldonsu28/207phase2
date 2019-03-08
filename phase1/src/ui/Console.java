package ui;

import java.util.ArrayList;
import java.util.Scanner;

class Console {
    private Scanner response = new Scanner(System.in);

    int displayMenu(String title, ArrayList menu) {
        System.out.println(title);
        for (int i = 0; i < menu.size(); i ++)
            System.out.println((i+1) + ") " + menu.indexOf(i));
        return convertStr(response.nextLine());
    }

    int getAmount() {
        System.out.println("Please enter amount of money");
        return convertStr(response.nextLine());
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
