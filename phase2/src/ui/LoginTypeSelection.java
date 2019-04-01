package ui;

import atm.BankManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class LoginTypeSelection extends JDialog {

    private final Container container;
    private JButton userButton, managerButton, employeeButton;

    LoginTypeSelection(BankManager manager) {
        super(MainFrame.mainFrame, "Login Type", true);

        manager.logout();

        ActionListener actionListener = e -> {
            JButton pressed = (JButton) e.getSource();

            if (userButton == pressed) {
                new LoginValidation(this, LoginType.USER, manager, false);
            } else if (managerButton == pressed) {
                new LoginValidation(this, LoginType.MANAGER, manager, false);
            } else if (employeeButton == pressed) {
                new LoginValidation(this, LoginType.EMPLOYEE, manager, false);
            }
        };

        userButton = new JButton("User");
        userButton.addActionListener(actionListener);
        managerButton = new JButton("Manager");
        managerButton.addActionListener(actionListener);
        employeeButton = new JButton("Employee");
        employeeButton.addActionListener(actionListener);
        userButton.setSize(employeeButton.getSize());
        managerButton.setSize(employeeButton.getSize());

        container = getContentPane();

        initializeLayout();

        setBounds(10, 10, 400, 200);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(LoginTypeSelection.this,
                        "Are you sure to exit the program?", "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setMinimumSize(new Dimension(400, 200));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        flowLayout.setHgap(40);

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(flowLayout);
        selectionPanel.add(userButton);
        selectionPanel.add(employeeButton);
        selectionPanel.add(managerButton);

        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(flowLayout);
        instructionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        instructionPanel.add(new JLabel("Please choose the type of login:"));

        container.setLayout(new GridLayout(2, 1));
        container.add(instructionPanel);
        container.add(selectionPanel);
    }
}
