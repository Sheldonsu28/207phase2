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
        dateInput = new JTextField(15);
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String dateText = dateInput.getText();
            Date result = null;

            SimpleDateFormat format = new SimpleDateFormat(AtmTime.FORMAT_STRING);

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

        setTitle("System Time Initialization");
        setBounds(100, 100, 550, 250);
        setMinimumSize(new Dimension(550, 250));
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
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private void initializeLayout() {
        container.setLayout(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(20);
        flowLayout.setHgap(20);
        inputPanel.setLayout(flowLayout);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        inputPanel.add(new JLabel(
                String.format("Please enter system initial time in format of %s:", AtmTime.FORMAT_STRING)));
        inputPanel.add(dateInput);

        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        submitPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        submitPanel.add(submitButton);

        container.add(inputPanel);
        container.add(submitPanel);
    }

    BankManager getManager() {
        return null;
    }
}
