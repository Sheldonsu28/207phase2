package ui;

import atm.BankManager;
import atm.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class MainFrame implements Serializable {

    static JFrame mainFrame;
    private static BankManager manager;
    private static boolean isRunning;
    private static JDialog shutdownDialog;

    public static void main(String[] args) {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 15));

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

    public static void showInfoMessage(String message, String title) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void initFrame() {
        mainFrame = new JFrame();
        mainFrame.setVisible(false);
    }

    private static void start() {
        initFrame();
        isRunning = true;
        new LoginTypeSelection(manager);
    }

    public static void shutdown() {
        if (isRunning) {
            isRunning = false;
            mainFrame.dispose();

            for (Window window : JDialog.getWindows()) {
                window.dispose();
            }

            initFrame();

            shutdownDialog = new JDialog(mainFrame);

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
            shutdownDialog.dispose();
            mainFrame.dispose();
            start();
        } else {
            throw new IllegalStateException("Can't start when system is already running");
        }
    }

}
