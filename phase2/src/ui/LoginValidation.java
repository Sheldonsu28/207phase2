package ui;

import atm.BankManager;
import atm.User;
import atm.UserNotExistException;
import atm.WrongPasswordException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class LoginValidation extends JDialog {
    private final JButton loginButton;
    private final JTextField usernameField;
    private final JTextField passwordField;
    private final Container container;

    LoginValidation(JDialog parent, LoginType type, BankManager manager, boolean onlyValidate) {
        super(MainFrame.mainFrame, "Login as " + type.toString(), true);

        usernameField = new JTextField(10);
        passwordField = new JTextField(10);
        loginButton = new JButton("login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText(), password = passwordField.getText();

                try {
                    switch (type) {
                        case USER:
                            User user = manager.validateUserLogin(username, password);
                            successLogin();

                            if (!onlyValidate) {
                                new UserMainMenu(manager, user);
                            }
                            break;

                        case MANAGER:
                            manager.login(username, password);
                            successLogin();

                            if (!onlyValidate) {
                                new ManagerMainMenu(manager, LoginType.MANAGER);
                            }
                            break;

                        case EMPLOYEE:
                            manager.validateEmployeeLogin(username, password);
                            successLogin();

                            if (!onlyValidate) {
                                new ManagerMainMenu(manager, LoginType.EMPLOYEE);
                            }
                            break;
                    }

                } catch (WrongPasswordException | UserNotExistException ex) {
                    MainFrame.showErrorMessage(ex.getMessage());
                }

            }

            private void successLogin() {
                MainFrame.showInfoMessage("Login success!", "Loggedin");
                LoginValidation.this.dispose();

                if (parent != null) {
                    parent.dispose();
                }
            }
        });

        container = getContentPane();

        initializeLayout();

        setBounds(100, 100, 350, 250);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(LoginValidation.this,
                        "Are you sure to cancel this login?", "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    if (onlyValidate) {
                        System.exit(0);
                    } else {
                        LoginValidation.this.dispose();
                    }
                }
            }
        });
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(350, 200));
        setVisible(true);
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        usernamePanel.setLayout(flowLayout);
        usernamePanel.add(new JLabel("username: "));
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        passwordPanel.setLayout(flowLayout);
        passwordPanel.add(new JLabel("password: "));
        passwordPanel.add(passwordField);

        JPanel infoPanel = new JPanel();
        usernamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setLayout(new GridLayout(1, 2));
        infoPanel.add(usernamePanel);
        infoPanel.add(passwordPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(flowLayout);
        buttonPanel.add(loginButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.add(infoPanel);
        mainPanel.add(buttonPanel);

        container.add(mainPanel);
    }

}
