package ui;

import atm.BankManager;
import atm.UsernameAlreadyExistException;
import atm.UsernameOutOfRangeException;

import javax.swing.*;
import java.awt.*;

public class UserCreationMenu extends SubMenu {

    private JTextField usernameField;
    private JButton submitButton;

    UserCreationMenu(BankManager manager) {
        super("User Creation");

        usernameField = new JTextField(10);
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String password;

            try {
                password = manager.createUser(usernameField.getText());
            } catch (UsernameAlreadyExistException | UsernameOutOfRangeException ex) {
                MainFrame.showMessage(ex.getMessage());
                return;
            }

            JOptionPane.showMessageDialog(MainFrame.mainFrame, "Your password: " + password,
                    "Creation Successful", JOptionPane.INFORMATION_MESSAGE);

            UserCreationMenu.this.dispose();
        });

        initializeLayout();

        setVisible(true);
    }

    private void initializeLayout() {
        container.setLayout(new GridLayout(2, 1));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);

        JPanel infoPanel = new JPanel(flowLayout);
        infoPanel.add(new JLabel("Enter desired username: "));
        infoPanel.add(usernameField);

        JPanel buttonPanel = new JPanel(flowLayout);
        buttonPanel.add(submitButton);

        container.add(infoPanel);
        container.add(buttonPanel);
    }
}
