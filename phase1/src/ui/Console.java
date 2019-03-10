package ui;

import java.util.*;
import java.util.stream.Collectors;

class Console {
    private Scanner response = new Scanner(System.in);

    //TODO null exception
    int displayMenu(Menu menu, Object[]... args) {
        System.out.println(menu);
        List<String> options;

        switch (menu) {
            case USER_SELECTION_MENU:
            case ACCOUNT_SELECTION_MENU:
                options = Arrays.stream(args[0]).map(Object::toString).collect(Collectors.toList());
                printOptions(options);
                System.out.printf("%d) Back to main", options.size() + 1);
                return getChoice(options.size() + 1);

            case CONFIRM_MENU:
                System.out.println(args[0][0]);
                options = menu.getMenuOptions();
                printOptions(options);
                return getChoice(options.size());

            default:
                options = menu.getMenuOptions();
                printOptions(options);
                return getChoice(options.size());
        }

    }

    private void printOptions(List<String> options) {
        for (int i = 0; i < options.size(); i++)
            System.out.printf("%d) %s ", i + 1, options.get(i));
    }

    int getAmount() {
        System.out.println("Please enter amount of money");
        return getChoice(Integer.MAX_VALUE);
    }

    TreeMap<Integer, Integer> getInputStock() {
        TreeMap<Integer, Integer> result = new TreeMap<>();

        while (true) {
            try {
                System.out.println("Please enter your restock specification \"type-amount\" " +
                        "(ex. 5-7 10-3 means 7 $5 and 3 $10)");

                String[] data = response.nextLine().split(" ");

                for (String stock : data) {
                    String[] stockSpec = stock.split("-");

                    if (stockSpec.length != 2)
                        throw new IndexOutOfBoundsException();

                    int type = Integer.parseInt(stockSpec[0]);
                    int amount = Integer.parseInt(stockSpec[1]);

                    if (result.containsKey(type))
                        result.put(type, result.get(type) + amount);
                    else
                        result.put(type, amount);
                }

                break;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Invalid stock input!");
            }
        }

        return result;
    }

    private int getChoice(int limit) {
        int result;

        while (true) {
            try {
                result = response.nextInt();

                if (result >= 0 && result <= limit)
                    break;
                else
                    System.out.printf("Choice out of range(must be 0 - %d)", limit);
            } catch (InputMismatchException i) {
                System.out.println("Must be numeric integer");
            }
        }

        return result;
    }

    boolean setPrimary() {
        boolean valid = false;
        boolean result = false;
        while (!valid) {
            System.out.println("Do you want to set this chequing account primary?   yes/no");
            String answer = response.nextLine();
            if (answer.equals("yes")) {
                valid = true;
                result = true;
            } else if (answer.equals("no")) {
                valid = true;
                result = false;
            } else {
                valid = false;
                System.out.println("Invalid respond. Please try again.");
            }
        }
        return result;
    }

    int restockAmount(String s) {
        boolean valid = false;
        int amount = 0;
        while (!valid) {
            System.out.print(s);
            amount = response.nextInt();
            if (amount < 1) {
                valid = false;
                System.out.println("Invalid amount. Please enter again.");
            } else {
                valid = true;
            }
        }
        return amount;
    }


}
