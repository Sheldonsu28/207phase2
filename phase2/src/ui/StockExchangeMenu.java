package ui;

import account.StockAccount;
import atm.StockInfoGetter;
import atm.SymbolNotFoundException;
import atm.User;
import transaction.StockTransaction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

class StockExchangeMenu extends SubMenu {
    private JComboBox<StockAccount> accountSelection;
    private JTextField searchField, quantityField;
    private JLabel stockPriceLabel;
    private JButton searchButton, buyButton, sellButton;
    private JTextArea stockSummaryText;

    private final StockInfoGetter stockInfoGetter = new StockInfoGetter();

    StockExchangeMenu(User user) {
        super("Stock Exchange Center");

        ArrayList<StockAccount> stockAccounts = user.getAccountListOfType(StockAccount.class);

        if (stockAccounts.isEmpty()) {
            MainFrame.showErrorMessage("You don't have a Stock Account yet! " +
                    "You can trade after you opened a Stock Account.");
            this.dispose();
            return;
        }

        searchField = new JTextField(15);
        quantityField = getPositiveIntegerOnlyField();
        stockPriceLabel = new JLabel();
        accountSelection = new JComboBox<>(stockAccounts.toArray(new StockAccount[0]));

        stockSummaryText = new JTextArea();
        stockSummaryText.setLineWrap(false);
        stockSummaryText.setBorder(BorderFactory.createLineBorder(Color.black));
        stockSummaryText.setFont(new Font("Serif", Font.PLAIN, 18));
        stockSummaryText.setEditable(false);

        updateStockSummary();

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            try {
                String stockSymbol = stockInfoGetter.getSymbol(searchField.getText());
                double price = stockInfoGetter.getQuote(stockSymbol);
                stockPriceLabel.setText(String.format("%s: $%.2f", stockSymbol, price));
            } catch (SymbolNotFoundException | IOException e2) {
                MainFrame.showErrorMessage("Stock Info not found!");
            }
        });

        buyButton = new JButton("Buy");
        buyButton.addActionListener(e -> {
            StockAccount selectedAccount = (StockAccount) accountSelection.getSelectedItem();

            if (selectedAccount != null && !quantityField.getText().equals("")) {
                stockExchange(user, selectedAccount, true);
            } else {
                MainFrame.showErrorMessage("Account not selected!");
                return;
            }

            accountSelection.updateUI();
            updateStockSummary();
        });

        sellButton = new JButton("Sell");
        sellButton.addActionListener(e -> {
            StockAccount selectedAccount = (StockAccount) accountSelection.getSelectedItem();

            if (selectedAccount != null && !quantityField.getText().equals("")) {
                stockExchange(user, selectedAccount, false);
            } else {
                MainFrame.showErrorMessage("Account not selected!");
                return;
            }

            accountSelection.updateUI();
            updateStockSummary();
        });

        initializeLayout();

        setVisible(true);
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);

        JPanel searchPanel = new JPanel(flowLayout);
        searchPanel.add(new JLabel("Stock Symbol: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(stockPriceLabel);

        JPanel actionPanel = new JPanel(flowLayout);
        actionPanel.add(new JLabel("Amount to sell/buy: "));
        actionPanel.add(quantityField);
        actionPanel.add(buyButton);
        actionPanel.add(sellButton);

        defaultRowsLayout(new LinkedHashMap<JComponent, String>() {{
            put(accountSelection, "Select Stock Account:");
            put(stockSummaryText, null);
            put(searchPanel, null);
            put(actionPanel, null);
        }});
    }

    private void updateStockSummary() {
        StockAccount stockAccount = (StockAccount) accountSelection.getSelectedItem();

        if (stockAccount != null) {
            StringBuilder info = new StringBuilder();

            for (Map.Entry<String, Integer> entry : stockAccount.getBoughtStocks().entrySet())
                info.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");

            stockSummaryText.setText(info.toString());
        } else {
            stockSummaryText.setText("");
        }
    }

    private void stockExchange(User user, StockAccount selectedAccount, boolean buy) {
        int shares = Integer.parseInt(quantityField.getText());
        double price;
        String companyName = searchField.getText();
        String action, stockSymbol;
        StockTransaction stockTransaction;

        if (buy) action = "Buy";
        else action = "Sell";

        try {
            stockSymbol = stockInfoGetter.getSymbol(searchField.getText());
            price = stockInfoGetter.getQuote(stockSymbol);
        } catch (SymbolNotFoundException | IOException e2) {
            MainFrame.showErrorMessage("Stock Info not found!");
            return;
        }

        stockTransaction = new StockTransaction(user, selectedAccount, shares, price, companyName, buy);

        if (JOptionPane.showConfirmDialog(this,
                String.format("Are you sure to %s %d shares of %s stocks (Total: $%.2f)",
                        action, shares, companyName, price * shares), "Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            if (stockTransaction.perform()) {
                MainFrame.showInfoMessage(action + " successful!\n" + stockTransaction, "Success");
            } else {
                MainFrame.showErrorMessage(action + " failed because something went wrong");
            }

        }
    }

}