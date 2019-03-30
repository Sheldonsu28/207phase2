package ui;

import atm.BankManager;
import atm.User;

import javax.swing.*;

public class UserMainMenu extends MainMenu {

    private JButton toDepositMenu, toWithdrawMenu, toTransferMenu, toPayBillMenu, toInfoMenu, toOpenAccountMenu;
    private final User user;

    UserMainMenu(BankManager manager, User user) {
        super(manager, "User Main Menu");

        this.user = user;

        initializeLayout(new JButton[]{toDepositMenu, toWithdrawMenu, toTransferMenu, toPayBillMenu,
                toInfoMenu, toOpenAccountMenu});

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


        toDepositMenu.addActionListener(e -> {

        });

        toWithdrawMenu.addActionListener(e -> {

        });

        toTransferMenu.addActionListener(e -> {

        });

        toPayBillMenu.addActionListener(e -> {

        });

        toInfoMenu.addActionListener(e -> {

        });

        toOpenAccountMenu.addActionListener(e -> {

        });
    }
}
