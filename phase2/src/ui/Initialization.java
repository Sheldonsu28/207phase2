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

class Initialization extends JDialog {
    private final Container container;
    private final JTextField dateInput;
    private final JButton submitButton;
    private final JButton setToCurrentButton;

    Initialization(BankManager manager) {
        super(MainFrame.mainFrame, "Initialization", true);

        MainFrame.showInfoMessage("System not initialized. Please login as manager to initialize.",
                "Initialization");
        new LoginValidation(null, LoginType.MANAGER, manager, true);

        container = getContentPane();
        dateInput = new JTextField(15);
        setToCurrentButton = new JButton("Set to Current Time");
        setToCurrentButton.addActionListener(e -> finishInitialization(new Date(System.currentTimeMillis()), manager));

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String dateText = dateInput.getText();
            Date result;

            SimpleDateFormat format = new SimpleDateFormat(AtmTime.FORMAT_STRING);

            try {
                result = format.parse(dateText);
            } catch (ParseException ex) {
                MainFrame.showErrorMessage("Invalid time format!");
                return;
            }

            if (result != null) {
                finishInitialization(result, manager);
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
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(20);
        flowLayout.setHgap(20);

        JPanel inputPanel = new JPanel(flowLayout);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        inputPanel.add(new JLabel(
                String.format("Please enter system initial time in format of %s:", AtmTime.FORMAT_STRING)));
        inputPanel.add(dateInput);
        inputPanel.add(setToCurrentButton);

        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        submitPanel.add(submitButton);

        container.add(inputPanel);
        container.add(submitPanel);
    }

    private void finishInitialization(Date date, BankManager manager) {
        MainFrame.showInfoMessage("Date successfully set to " + date, "Date Initialized");
        manager.initialize(date);
        this.dispose();
    }
}
