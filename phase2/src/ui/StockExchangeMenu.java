package ui;

import account.Account;
import account.StockAccount;
import account.Withdrawable;
import atm.StockInfoGetter;
import atm.SymbolNotFoundException;
import atm.User;
import transaction.StockTransaction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class StockExchangeMenu extends SubMenu {
    private JComboBox<StockAccount> accountSelection;
    private JTextField searchBox;
    private JTextField quantityBox;
    private JLabel stockPrice;
    private JButton search;
    private JButton buy;
    private JButton sell;

    private StockInfoGetter stockInfoGetter = new StockInfoGetter();

    StockExchangeMenu(User user) {
        super("Stock Exchange Center");
        ArrayList<Withdrawable> withdrawables = user.getAccountListOfType(Withdrawable.class);
        ArrayList<StockAccount> stockAccounts = new ArrayList<>();

        searchBox = new JTextField(15);
        quantityBox = new JTextField(5);
        stockPrice = new JLabel("/");
        search = new JButton("Search");
        buy = new JButton("Buy");
        sell = new JButton("Sell");
        buy.setEnabled(false);
        sell.setEnabled(false);

        for (Withdrawable w: withdrawables) {
            if (w instanceof StockAccount)
                stockAccounts.add((StockAccount) w);
        }

        if (stockAccounts.isEmpty()) {
            MainFrame.showInfoMessage("You don't have a Stock Account yet! " +
                    "You can trade after you opened a Stock Account.", "Attention");
        }

        accountSelection = new JComboBox<>(stockAccounts.toArray(new StockAccount[0]));
        accountSelection.addActionListener(e -> {
            StockAccount selectedAccount = (StockAccount) accountSelection.getSelectedItem();

            if (selectedAccount != null) {
                buy.setEnabled(true);
                sell.setEnabled(true);
            } else {
                buy.setEnabled(false);
                sell.setEnabled(false);
            }
        });

        search.addActionListener(e -> {
            try {
                String stockSymbol = stockInfoGetter.getSymbol(searchBox.getText());
                double price = stockInfoGetter.getQuote(stockSymbol);
                stockPrice.setText(String.valueOf(price));
            } catch (SymbolNotFoundException | IOException e2) {
                MainFrame.showErrorMessage("Stock Info not found!");
            }
        });

        buy.addActionListener(e -> {
            stockExchange(user, true);
        });

        sell.addActionListener(e -> {
            stockExchange(user, false);
        });

        initializeLayout();
        setVisible(true);
    }

    private void stockExchange(User user, boolean buy) {
        int shares;
        double price = 0;
        String action;
        String companyName = searchBox.getText();
        String stockSymbol;
        StockTransaction stockTransaction;

        if (buy) action = "buy";
            else action = "sell";

        try {
            shares = Integer.parseInt(companyName);
        } catch (NumberFormatException e1) {
            shares = 0;
        }

        try {
            stockSymbol = stockInfoGetter.getSymbol(searchBox.getText());
            price = stockInfoGetter.getQuote(stockSymbol);
        } catch (SymbolNotFoundException | IOException e2) {
            MainFrame.showErrorMessage("Stock Info not found!");
        }

        StockAccount selectedAccount = (StockAccount) accountSelection.getSelectedItem();
        stockTransaction = new StockTransaction(user, selectedAccount, shares, price, searchBox.getText(), buy);

        if (JOptionPane.showConfirmDialog(this,
                "Are you sure to " + action + " " + shares + " shares of " + companyName + "stocks?\n Total: "
                        + price*shares, "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            if (stockTransaction.perform()) {
                MainFrame.showInfoMessage(action + "successful!\n" + stockTransaction, "Success");
            } else {
                MainFrame.showErrorMessage(action + " stock failed because something went wrong");
            }

        }
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);

        JPanel accountSelectionPanel = new JPanel(flowLayout);
        accountSelectionPanel.add(new JLabel("Select Account: "));
        accountSelectionPanel.add(accountSelection);

        JPanel stockSearchPanel = new JPanel(flowLayout);
        stockSearchPanel.add(new JLabel("Enter company name "));
        stockSearchPanel.add(searchBox);
        stockSearchPanel.add(search);

        JPanel stockInfoPanel = new JPanel(flowLayout);
        stockInfoPanel.add(stockPrice);
        stockInfoPanel.add(new JLabel("Quantity "));
        stockInfoPanel.add(quantityBox);
        stockInfoPanel.add(buy);
        stockInfoPanel.add(sell);

        JPanel upperPanel = new JPanel(new GridLayout(1, 2));
        upperPanel.add(accountSelectionPanel);
        upperPanel.add(stockSearchPanel);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.add(upperPanel);
        mainPanel.add(stockInfoPanel);

        container.add(mainPanel);

    }

}
