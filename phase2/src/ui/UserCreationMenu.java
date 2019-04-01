package ui;

import atm.BankManager;
import atm.UsernameAlreadyExistException;
import atm.UsernameOutOfRangeException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.LinkedHashMap;

class UserCreationMenu extends SubMenu {

    private final JTextField usernameField;
    private final JButton submitButton;
    private final JCheckBox employeeBox;

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
                MainFrame.showErrorMessage(ex.getMessage());
                return;
            }

            MainFrame.showInfoMessage("Password(copied to clipboard): " + password, "Creation Success");

            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(password), null);
        });

        initializeLayout();

        setVisible(true);
    }

    private void initializeLayout() {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(usernameField);
        infoPanel.add(employeeBox);

        defaultRowsLayout(new LinkedHashMap<JComponent, String>() {{
            put(infoPanel, "Enter desired username: ");
            put(submitButton, null);
        }});
    }
}
