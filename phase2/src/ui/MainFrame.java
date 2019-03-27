package ui;

import atm.BankManager;
import atm.FileHandler;

public class MainFrame {

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        BankManager readData = fileHandler.readManagerData();
        final BankManager manager;

        if (readData == null) {
            manager = (new Initialization()).getManager();
        } else {
            manager = readData;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> fileHandler.saveManagerData(manager)));
    }

    public static void shutdown() {

    }

    public static void start() {
        new LoginTypeSelection();
    }


}
