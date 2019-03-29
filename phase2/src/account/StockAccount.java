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
    private int dayOfWeek;

    private StockQuoteGetter quoteGetter = new StockQuoteGetter();

    StockAccount(Date time, List<User> owner) {
        super(time, owner);
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
    @Override
    public void deposit(double amount ,Transaction register){
        balance += amount;

        registerTransaction(register);
    }

    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException{
        balance -= amount;

        registerTransaction(register);
    }

    public void sellStock(int stockAmount, double stockPrice, String stockSymbol, Transaction register)
            throws WithdrawException {

        double moneyWithdraw = stockAmount*stockPrice;

        if(dayOfWeek == 7 || dayOfWeek == 1){
            throw new IncorrectTimeException();
        }

        if(moneyWithdraw > cash){
            throw new InsufficientFundException(this, moneyWithdraw);
        }

        cash -= moneyWithdraw;

        stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + stockAmount);

        registerTransaction(register);
    }

    public void buyStock(int stockAmount, double stockPrice, String stockSymbol ,Transaction register) throws InsufficientSharesException {
        double moneyDeposit = stockAmount*stockPrice;

        if (stockAmount > stocks.get(stockSymbol)){
            throw new InsufficientSharesException();
        }

        stocks.put(stockSymbol, stocks.get(stockSymbol) - stockAmount);

        if (stocks.get(stockSymbol) == 0) {
            stocks.remove(stockSymbol);
        }

        cash += moneyDeposit;

        registerTransaction(register);
    }


    @Override
    public void cancelWithdraw(double amount) {

    }

    @Override
    public void update(Observable o, Object arg) {
        Date currTime = (Date) arg;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTime);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }
}
