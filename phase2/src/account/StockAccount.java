package account;

import atm.StockQuoteGetter;
import atm.User;
import transaction.Transaction;

import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;

public class StockAccount extends Account{

    private double cash;
    private TreeMap<String, Double> stocks;

    StockQuoteGetter quoteGetter = new StockQuoteGetter();

    StockAccount(Date time, User owner) {
        super(time, owner);
    }

    @Override
    public double getNetBalance() {
        return balance;
    }

    public void buyStock(String stockSymbol, Transaction register) throws IOException {
        double stockQuote = quoteGetter.getQuote(stockSymbol);
    }

    public void withdraw(int stockAmount, double stockQuote, String stockSymbol ,Transaction register) throws WithdrawException {
        double moneyWithdraw = stockAmount*stockQuote;
        balance -= moneyWithdraw;
        registerTransaction(register);
    }
}
