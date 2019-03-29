package account;

import atm.AtmTime;
import atm.StockQuoteGetter;
import atm.User;
import transaction.Transaction;

import java.io.IOException;
import java.util.*;

public class StockAccount extends AssetAccount implements Observer {
    private double cash;
    private HashMap<String, Integer> stocks = new HashMap<>();
    private String currTime;

    private StockQuoteGetter quoteGetter = new StockQuoteGetter();

    StockAccount(Date time, List<User> owner) {
        super(time, owner);
    }

    public String getTime(){
        return currTime;
    }

    @Override
    public double getNetBalance() {
        double stockNetValue = 0;
        for (String stockSymbol: stocks.keySet()) {
            try {
                double currStockQuote = quoteGetter.getQuote(stockSymbol);
                stockNetValue += stocks.get(stockSymbol) * currStockQuote;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        balance = cash + stockNetValue;
        return balance;
    }

    //TODO check time and weekday
    public void deposit(int stockAmount, double stockPrice, String stockSymbol ,Transaction register)
            throws InsufficientSharesException {
        double moneyDeposit = stockAmount*stockPrice;

        if (stockAmount > stocks.get(stockSymbol))
            throw new InsufficientSharesException();

        stocks.put(stockSymbol, stocks.get(stockSymbol) - stockAmount);

        if (stocks.get(stockSymbol) == 0)
            stocks.remove(stockSymbol);

        cash += moneyDeposit;

        registerTransaction(register);
    }

    public void withdraw(int stockAmount, double stockPrice, String stockSymbol, Transaction register)
            throws WithdrawException {
        currTime = AtmTime.FORMAT_STRING;

        double moneyWithdraw = stockAmount*stockPrice;

        if(Integer.parseInt(currTime.substring(14,16))< 9 || Integer.parseInt(currTime.substring(14,16)) > 16){
            throw new IncorrectTimeException();
        }

        if(moneyWithdraw > cash){
            throw new InsufficientFundException(this, moneyWithdraw);
        }

        cash -= moneyWithdraw;

        stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + stockAmount);

        registerTransaction(register);
    }

    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException {

    }

    @Override
    public void cancelWithdraw(double amount) {

    }

    @Override
    public void update(Observable o, Object arg) {
        Date currTime = (Date) arg;


    }
}
