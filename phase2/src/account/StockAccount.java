package account;

import atm.StockInfoGetter;
import atm.User;
import transaction.Transaction;

import java.io.IOException;
import java.util.*;

public class StockAccount extends AssetAccount implements Observer {
    private double accountNetBalance;
    private HashMap<String, Integer> stocks = new HashMap<>();
    private int dayOfWeek;

    private StockInfoGetter quoteGetter = new StockInfoGetter();

    public StockAccount(Date time, List<User> owner) {
        super(time, owner);
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
        accountNetBalance += balance + stockValue;
        return accountNetBalance;
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
            throws WithdrawException {

        double moneyWithdraw = stockAmount*stockPrice;

        if(dayOfWeek == 7 || dayOfWeek == 1){
            throw new IncorrectTimeException();
        }

        stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + stockAmount);

        withdraw(moneyWithdraw, register);
    }

    public void sellStock(int stockAmount, double stockPrice, String stockSymbol ,Transaction register) throws InsufficientSharesException, IncorrectTimeException {
        double moneyDeposit = stockAmount*stockPrice;

        if(dayOfWeek == 7 || dayOfWeek == 1){
            throw new IncorrectTimeException();
        }

        if (stockAmount > stocks.get(stockSymbol)){
            throw new InsufficientSharesException();
        }

        stocks.put(stockSymbol, stocks.get(stockSymbol) - stockAmount);

        if (stocks.get(stockSymbol) == 0) {
            stocks.remove(stockSymbol);
        }

        deposit(moneyDeposit, register);
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
