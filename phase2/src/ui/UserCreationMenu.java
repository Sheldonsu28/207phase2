package ui;

import atm.BankManager;
import atm.UsernameAlreadyExistException;
import atm.UsernameOutOfRangeException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class UserCreationMenu extends SubMenu {

    private JTextField usernameField;
    private JButton submitButton;
    private JCheckBox employeeBox;

    UserCreationMenu(BankManager manager) {
        super("User Creation");

        usernameField = new JTextField(10);
        employeeBox = new JCheckBox("isEmployee");
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password;

            try {
                if (employeeBox.isSelected()) {
                    password = manager.createEmployee(username);
                } else {
                    password = manager.createUser(username);
                }
            } catch (UsernameAlreadyExistException | UsernameOutOfRangeException ex) {
                MainFrame.showMessage(ex.getMessage());
                return;
            }

            JOptionPane.showMessageDialog(MainFrame.mainFrame, "Password(copied to clipboard): " + password,
                    "Creation Successful", JOptionPane.INFORMATION_MESSAGE);

            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(password), null);

            UserCreationMenu.this.dispose();
        });

        initializeLayout();

        setVisible(true);
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);

        JPanel infoPanel = new JPanel(flowLayout);
        infoPanel.add(new JLabel("Enter desired username: "));
        infoPanel.add(usernameField);
        infoPanel.add(employeeBox);

        JPanel buttonPanel = new JPanel(flowLayout);
        buttonPanel.add(submitButton);

        container.setLayout(new GridLayout(2, 1));
        container.add(infoPanel);
        container.add(buttonPanel);
    }
}
