package account;

import atm.AtmTime;
import atm.StockQuoteGetter;
import atm.User;
import transaction.Transaction;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class StockAccount extends AssetAccount {
    private double cash;
    private HashMap<String, Integer> stocks = new HashMap<>();
    private String currTime;

    private StockQuoteGetter quoteGetter = new StockQuoteGetter();

    StockAccount(Date time, User owner) {
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
    public void deposit(int stockAmount, double stockPrice, String stockSymbol ,Transaction register){
        double moneyDeposit = stockAmount*stockPrice;
        cash += moneyDeposit;

        stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + stockAmount);

        registerTransaction(register);
    }

    public void withdraw(int stockAmount, double stockPrice, String stockSymbol ,Transaction register) throws WithdrawException {
        currTime = AtmTime.FORMAT_STRING;

        double moneyWithdraw = stockAmount*stockPrice;

        if(Integer.parseInt(currTime.substring(14,15))<9 || Integer.parseInt(currTime.substring(14,15)) > 15){
            throw new IncorrectTimeException();
        }

        if(moneyWithdraw > cash){
            throw new InsufficientFundException(this, moneyWithdraw);
        }

        cash -= moneyWithdraw;

        stocks.put(stockSymbol, stocks.get(stockSymbol) - stockAmount);

        if (stocks.get(stockSymbol) == 0)
            stocks.remove(stockSymbol);

        registerTransaction(register);
    }

    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException {

    }

    @Override
    public void cancelWithdraw(double amount) {

    }
}
