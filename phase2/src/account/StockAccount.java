package account;

import atm.StockInfoGetter;
import atm.User;
import transaction.Transaction;

import java.io.IOException;
import java.util.*;

public class StockAccount extends AssetAccount implements Observer {
    private HashMap<String, Integer> stocks = new HashMap<>();
    private int dayOfWeek;
    private StockInfoGetter quoteGetter = new StockInfoGetter();

    public StockAccount(Date time, List<User> owner) {
        super(time, owner);

        updateDayOfWeek(time);
    }

    @Override
    public double getNetBalance() {
        double stockValue = 0;

        for (String stockSymbol: stocks.keySet()) {
            try {
                double currStockQuote = quoteGetter.getQuote(stockSymbol);
                stockValue += stocks.get(stockSymbol) * currStockQuote;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return balance + stockValue;
    }

    @Override
    public void deposit(double amount ,Transaction register){
        balance += amount;

        registerTransaction(register);
    }

    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException{
        if(balance < amount){
            throw new InsufficientFundException(this, amount);
        }

        balance -= amount;

        registerTransaction(register);
    }

    public void buyStock(int stockAmount, double stockPrice, String stockSymbol, Transaction register)
            throws IncorrectTimeException, WithdrawException {

        double moneyWithdraw = stockAmount*stockPrice;

        if(dayOfWeek == 7 || dayOfWeek == 1){
            throw new IncorrectTimeException();
        }

        stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + stockAmount);

        withdraw(moneyWithdraw, register);
    }

    public void sellStock(int stockAmount, double stockPrice, String stockSymbol ,Transaction register)
            throws InsufficientSharesException, IncorrectTimeException {
        double moneyDeposit = stockAmount*stockPrice;

        if(dayOfWeek == 7 || dayOfWeek == 1){
            throw new IncorrectTimeException();
        }

        if (!stocks.containsKey(stockSymbol) || stockAmount > stocks.get(stockSymbol)){
            throw new InsufficientSharesException();
        }

        stocks.put(stockSymbol, stocks.get(stockSymbol) - stockAmount);

        if (stocks.get(stockSymbol) == 0) {
            stocks.remove(stockSymbol);
        }

        deposit(moneyDeposit, register);
    }

    private void updateDayOfWeek(Date currTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTime);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public void cancelWithdraw(double amount) {
        throw new IllegalStateException("THIS CAN NOT BE CANCELLED");
    }


    @Override
    public void update(Observable o, Object arg) {
        updateDayOfWeek((Date) arg);
    }
}
