package ui;

import atm.BankManager;
import atm.User;

import javax.swing.*;
import java.awt.event.ActionListener;

class UserMainMenu extends MainMenu {

    private JButton toDepositMenu, toWithdrawMenu, toTransferMenu,
            toPayBillMenu, toInfoMenu, toOpenAccountMenu, toBuySellStockMenu;
    private final User user;

    UserMainMenu(BankManager manager, User user) {
        super(manager, "User Main Menu");

        this.user = user;

        initializeLayout(new JButton[]{toDepositMenu, toWithdrawMenu, toTransferMenu, toPayBillMenu,
                toInfoMenu, toOpenAccountMenu, toBuySellStockMenu});

        setVisible(true);
    }


    @Override
    void initializeButtons() {
        toDepositMenu = new JButton("Deposit");
        toWithdrawMenu = new JButton("Withdraw");
        toTransferMenu = new JButton("Transfer");
        toPayBillMenu = new JButton("Pay Bill");
        toInfoMenu = new JButton("Account Info");
        toOpenAccountMenu = new JButton("Request New Account");
        toBuySellStockMenu = new JButton("Buy/Sell Stocks");

        ActionListener listener = e -> {
            JButton source = (JButton) e.getSource();
            UserMainMenu.this.setVisible(false);

            if (source == toDepositMenu) {
                int choice = getBranchChoice("Choose deposit method", "Deposit Method",
                        new String[]{"From Deposit File", "Manual"});

                switch (choice) {
                    case 0:
                        new ExternalDepositMenu(manager, user);
                        break;

                    case 1:
                        new ManualDepositMenu(manager, user);
                        break;
                }

            } else if (source == toWithdrawMenu) {
                new WithdrawMenu(manager, user);
            } else if (source == toTransferMenu) {
                int choice = getBranchChoice("Choose type of transfer", "Transfer Type",
                        new String[]{"Intra-user", "Inter-user"});

                switch (choice) {
                    case 0:
                        new IntraTransferMenu(user);
                        break;

                    case 1:
                        new InterTransferMenu(manager, user);
                        break;
                }

            } else if (source == toPayBillMenu) {
                new PayBillMenu(manager, user);

            } else if (source == toInfoMenu) {
                new AccountInfoMenu(user);

            } else if (source == toOpenAccountMenu) {
                new RequestAccountCreationMenu(user);

            } else if (source == toBuySellStockMenu) {
                new StockExchangeMenu(user);
            }

            UserMainMenu.this.setVisible(true);
        };

        toDepositMenu.addActionListener(listener);
        toWithdrawMenu.addActionListener(listener);
        toTransferMenu.addActionListener(listener);
        toPayBillMenu.addActionListener(listener);
        toInfoMenu.addActionListener(listener);
        toOpenAccountMenu.addActionListener(listener);
        toBuySellStockMenu.addActionListener(listener);
    }

}
