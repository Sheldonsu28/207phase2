package account;

import atm.StockQuoteGetter;
import atm.User;
import transaction.Transaction;

import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;

public class StockAccount extends AssetAccount{

    private double cash;
    private TreeMap<String, Double> stocks;

    StockQuoteGetter quoteGetter = new StockQuoteGetter();

    StockAccount(Date time, User owner) {
        super(time, owner);
    }

    public void buyStock(String stockSymbol) throws IOException {
        double stockQuote = quoteGetter.getQuote(stockSymbol);

    }

    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException {

    }

    @Override
    public void cancelWithdraw(double amount) {

    }
}
