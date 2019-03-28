package ui;

import atm.BankManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginTypeSelection extends JDialog {

    private Container container;
    private JButton userButton, managerButton;

    LoginTypeSelection(BankManager manager) {
        super(MainFrame.mainFrame, "Login Type", true);

        ActionListener actionListener = e -> {
            JButton pressed = (JButton) e.getSource();

            if (userButton == pressed) {
                new LoginValidation(this, LoginType.USER, manager, false);
            } else if (managerButton == pressed) {
                new LoginValidation(this, LoginType.MANAGER, manager, false);
            }
        };

        userButton = new JButton("User");
        userButton.addActionListener(actionListener);
        managerButton = new JButton("Manager");
        managerButton.addActionListener(actionListener);

        container = getContentPane();

        initializeLayout();

        setBounds(10, 10, 300, 300);
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
        setVisible(true);
    }

    private void initializeLayout() {
        Box buttons = Box.createHorizontalBox();
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttons.add(userButton);
        buttons.add(managerButton);

        container.setLayout(new GridLayout(2, 1));
        container.add(new JLabel("Please choose the type of login:"));
        container.add(buttons);
    }
}
