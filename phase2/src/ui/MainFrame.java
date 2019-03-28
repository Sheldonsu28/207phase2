package ui;

import atm.BankManager;
import atm.FileHandler;

import javax.swing.*;

public class MainFrame {

    static JFrame mainFrame;
    private static BankManager manager;
    private static boolean isRunning;

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        BankManager readData = fileHandler.readManagerData();

        if (readData == null) {
            readData = new BankManager();

            new Initialization(readData);
        }

        manager = readData;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> fileHandler.saveManagerData(manager)));

        isRunning = false;
        start();
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private static void initFrame() {
        mainFrame = new JFrame();
        mainFrame.setVisible(false);
    }

    private static void start() {
        initFrame();
        new LoginTypeSelection(manager);
    }

    public static void shutdown() {
        if (isRunning) {
            isRunning = false;
            mainFrame.dispose();
            initFrame();

            JDialog shutdownDialog = new JDialog(mainFrame);

            shutdownDialog.getContentPane().add(new JLabel("---SYSTEM IS CURRENTLY UNAVAILABLE---"));

            shutdownDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            shutdownDialog.setBounds(10, 10, 300, 300);
            shutdownDialog.setVisible(true);
        } else {
            throw new IllegalStateException("Can't shutdown when system is already shutdown");
        }
    }

    public static void restart() {
        if (!isRunning) {
            isRunning = true;
            mainFrame.dispose();
            start();
        } else {
            throw new IllegalStateException("Can't start when system is already running");
        }
    }

}
