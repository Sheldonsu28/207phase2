package ui;

import account.Account;
import atm.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AccountRequestedCreation extends SubMenu {
    private HashMap<JButton, Request> requestList;

    AccountRequestedCreation(BankManager manager) {
        super("Account Creation");

        requestList = new HashMap<>();

        ArrayList<String> requests = new FileHandler().readFrom(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE);

        ActionListener listener = e -> {
            JButton source = (JButton) e.getSource();

            requestList.get(source).approveRequest();

            JOptionPane.showMessageDialog(this, "Account successfully created!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            AccountRequestedCreation.this.dispose();
        };

        for (String requestStr : requests) {
            Request request;

            try {
                request = new Request(requestStr, manager);
            } catch (IllegalFileFormatException | UserNotExistException e) {
                MainFrame.showMessage(e.getMessage());
                continue;
            }

            JButton requestButton = request.getButton();
            requestButton.addActionListener(listener);

            requestList.put(requestButton, request);
        }

        initializeLayout();

        setVisible(true);
    }


    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);

        Set<JButton> buttons = requestList.keySet();
        container.setLayout(new GridLayout(buttons.size(), 1));

        for (JButton button : buttons) {
            JPanel buttonPanel = new JPanel(flowLayout);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            buttonPanel.add(button);

            container.add(buttonPanel);
        }

    }

    private final class Request {
        private final String username;
        private final boolean isPrimary;
        private final Class<Account> accountType;
        private final JButton requestButton;
        private final BankManager manager;

        private Request(String request, BankManager manager) throws IllegalFileFormatException, UserNotExistException {
            this.manager = manager;
            String[] info = request.split(" ");

            if (info.length != 3)
                throw new IllegalFileFormatException(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE);

            username = info[0];

            if (!manager.hasUser(username))
                throw new UserNotExistException(username);

            accountType = getValidAccountType(info[1]);
            isPrimary = Boolean.parseBoolean(info[2]);

            String primary;

            if (isPrimary)
                primary = "Primary";
            else
                primary = "notPrimary";

            requestButton = new JButton(String.format("%s %s %s", username, accountType.getSimpleName(), primary));
        }

        @SuppressWarnings("unchecked")
        private Class<Account> getValidAccountType(String classType) throws IllegalFileFormatException {

            Class<Account> result;

            try {
                result = (Class<Account>) Class.forName("account." + classType);
            } catch (ClassCastException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new IllegalFileFormatException(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE);
            }

            return result;
        }

        private void approveRequest() {
            manager.createAccount(username, accountType, isPrimary);
        }

        private JButton getButton() {
            return requestButton;
        }

    }
}
