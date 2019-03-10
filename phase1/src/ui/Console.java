package ui;

import atm.AtmTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Console {
    private Scanner response;
    private boolean isAlive;

    Console() {
        isAlive = true;
        response = new Scanner(System.in);
    }

    private void stateCheck() {

        while (!isAlive) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        if (isAlive) {
            System.out.println("\n========SYSTEM SHUTDOWN========\n");
            isAlive = false;
        }
    }

    public void start() {
        if (!isAlive) {
            String restartCode = "!?ReStArT+";
            System.out.println(String.format("\n========SYSTEM START (restart code: %s)========\n", restartCode));

            while (true) {
                String msg = response.nextLine();

                if (msg.equals(restartCode))
                    break;
            }

            isAlive = true;
        }
    }

    int displayMenu(Menu menu, Object[]... args) {
        stateCheck();

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
        stateCheck();

        for (int i = 0; i < options.size(); i++)
            System.out.printf("%d) %s\n", i + 1, options.get(i));

        System.out.println();
    }

    int getAmount() {
        stateCheck();

        System.out.println("Please enter amount of money");
        return getChoice(Integer.MAX_VALUE);
    }

    TreeMap<Integer, Integer> getInputStock() {
        stateCheck();

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
        stateCheck();

        int result;

        while (true) {
            try {
                result = response.nextInt();
                response.nextLine();

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
        stateCheck();

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

    String getRawInput() {
        stateCheck();

        return response.nextLine();
    }

    String[] getLoginInfo() {
        stateCheck();

        System.out.println("Enter username:");
        String username = response.nextLine();
        System.out.println("Enter password:");
        String password = response.nextLine();
        return new String[]{username, password};
    }


    Date getTime() {
        stateCheck();

        String formatString = AtmTime.FORMAT_STRING;

        SimpleDateFormat format = new SimpleDateFormat(formatString);

        Date result;

        while (true) {
            System.out.printf("Enter Time in format: %s", formatString);

            try {
                result = format.parse(response.nextLine());
            } catch (ParseException e) {
                System.out.println("Invalid time format!");
                continue;
            }

            break;
        }

        return result;
    }


}
