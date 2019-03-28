package ui;

import atm.AtmTime;
import atm.BankManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Initialization extends JDialog {
    private Container container;
    private JTextField dateInput;
    private JButton submitButton;

    Initialization(BankManager manager) {
        super(MainFrame.mainFrame, "Initialization", true);

        MainFrame.showMessage("System not initialized. Please login as manager to initialize.");
        new LoginValidation(null, LoginType.MANAGER, manager, true);

        container = getContentPane();
        dateInput = new JTextField(10);
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String dateText = dateInput.getText();
            Date result = null;

            String formatString = AtmTime.FORMAT_STRING;
            SimpleDateFormat format = new SimpleDateFormat(formatString);

            try {
                result = format.parse(dateText);
            } catch (ParseException ex) {
                MainFrame.showMessage("Invalid time format!");
            }

            if (result != null) {
                MainFrame.showMessage("Date successfully set to " + result);
                manager.initialize(result);
                this.dispose();
            }
        });

        initializeLayout();

        setTitle("System Initialization");
        setBounds(10, 10, 300, 300);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(Initialization.this,
                        "Are you sure to exit the program?", "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setVisible(true);
    }

    private void initializeLayout() {
        container.setLayout(new GridLayout(2, 1));
        container.add(dateInput);
        container.add(submitButton);
    }

    BankManager getManager() {
        return null;
    }
}
