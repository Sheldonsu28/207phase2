package ui;

import account.Account;
import atm.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

class AccountRequestedCreation extends SubMenu {
    private final HashMap<JButton, Request> requestList;

    AccountRequestedCreation(BankManager manager) {
        super("Account Creation");

        requestList = new HashMap<>();

        ArrayList<String> requests = new FileHandler().readFrom(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE);

        ActionListener listener = e -> {
            JButton source = (JButton) e.getSource();

            requestList.get(source).approveRequest();

            MainFrame.showInfoMessage("Account successfully created!", "Success");

            requestList.remove(source);

            container.removeAll();
            initializeLayout();
        };

        for (String requestStr : requests) {
            Request request;

            try {
                request = new Request(requestStr, manager);
            } catch (IllegalFileFormatException | UserNotExistException e) {
                MainFrame.showErrorMessage(e.getMessage());
                continue;
            }

            JButton requestButton = request.getButton();
            requestButton.addActionListener(listener);

            requestList.put(requestButton, request);
        }

        initializeLayout();

        setVisible(true);
    }

    void setClosingAction() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(AccountRequestedCreation.this,
                        "Are you sure to end current action and go back to previous menu?",
                        "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                        JOptionPane.YES_OPTION) {
                    AccountRequestedCreation.this.dispose();
                    new FileHandler().clearFile(ExternalFiles.ACCOUNT_CREATION_REQUEST_FILE);
                }
            }
        });
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

    private final class Request implements Serializable {
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
