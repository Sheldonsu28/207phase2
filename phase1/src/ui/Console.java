package ui;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Console {
    private Scanner response = new Scanner(System.in);

    //TODO null exception
    int displayMenu(Menu menu, Object[]... args) {
        System.out.println(menu);
        List<String> options;

        switch (menu) {
            case ACCOUNT_SELECTION_MENU:
                options = Arrays.stream(args[0]).map(Object::toString).collect(Collectors.toList());
                printOptions(options);
                System.out.printf("%d) Back to main", options.size() + 1);
                return getInput(options.size() + 1);

            case CONFIRM_MENU:
                System.out.println(args[0][0]);
                options = menu.getMenuOptions();
                printOptions(options);
                return getInput(options.size());

            default:
                options = menu.getMenuOptions();
                printOptions(options);
                return getInput(options.size());
        }

    }

    private void printOptions(List<String> options) {
        for (int i = 0; i < options.size(); i++)
            System.out.printf("%d) %s", i + 1, options.get(i));
    }

    int getAmount() {
        System.out.println("Please enter amount of money");
        return getInput(Integer.MAX_VALUE);
    }

    private int getInput(int limit) {
        int result;

        while (true) {
            try {
                result = response.nextInt();

                if (result >= 1 && result <= limit)
                    break;
                else
                    System.out.printf("Choice out of range(must be 1 - %d)", limit);
            } catch (InputMismatchException i) {
                System.out.println("Number choice only!");
            }
        }

        return result;
    }

}
