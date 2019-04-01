package account;

import atm.StockInfoGetter;
import atm.User;
import transaction.Transaction;

import java.io.IOException;
import java.util.*;

/**
 * Defines behaviours of savings account.
 */
public class StockAccount extends AssetAccount implements Observer {
    /**
     * HashMap stores stock transactions.
     */
    private final HashMap<String, Integer> stocks = new HashMap<>();
    private int dayOfWeek;
    private final StockInfoGetter quoteGetter = new StockInfoGetter();

    /**
     * Construct a stock account.
     *
     * @param time  time of creation
     * @param owner owner user
     * @see Account#Account(Date, List)
     */
    public StockAccount(Date time, List<User> owner) {
        super(time, owner);

        updateDayOfWeek(time);
    }

    public Map<String, Integer> getBoughtStocks() {
        return Collections.unmodifiableMap(stocks);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void deposit(double amount ,Transaction register){
        balance += amount;

        registerTransaction(register);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException{
        if(balance < amount){
            throw new InsufficientFundException(this, amount);
        }

        balance -= amount;

        registerTransaction(register);
    }

    /**
     * Check if the weekday of given time is during weekdays.
     * @throws IncorrectTimeException Throws when it's weekend.
     */
    private void checkState() throws IncorrectTimeException {
        if (dayOfWeek == 7 || dayOfWeek == 1) {
            throw new IncorrectTimeException();
        }
    }

    public void buyStock(int stockAmount, double stockPrice, String stockSymbol, Transaction register)
            throws IncorrectTimeException, WithdrawException {
        checkState();

        double moneyWithdraw = stockAmount*stockPrice;

        if (balance < moneyWithdraw) {
            throw new InsufficientFundException(this, moneyWithdraw);
        }

        balance -= moneyWithdraw;

        stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + stockAmount);

        registerTransaction(register);
    }

    public void sellStock(int stockAmount, double stockPrice, String stockSymbol ,Transaction register)
            throws InsufficientSharesException, IncorrectTimeException {
        checkState();

        double moneyDeposit = stockAmount*stockPrice;

        if (!stocks.containsKey(stockSymbol) || stockAmount > stocks.get(stockSymbol)){
            throw new InsufficientSharesException();
        }

        balance += moneyDeposit;

        stocks.put(stockSymbol, stocks.get(stockSymbol) - stockAmount);

        if (stocks.get(stockSymbol) == 0) {
            stocks.remove(stockSymbol);
        }

        registerTransaction(register);
    }

    private void updateDayOfWeek(Date currTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTime);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }


    @Override
    public void update(Observable o, Object arg) {
        updateDayOfWeek((Date) arg);
    }
}
