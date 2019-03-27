package account;

import atm.AtmTime;
import atm.StockQuoteGetter;
import atm.User;
import transaction.Transaction;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class StockAccount extends Account{

    private double cash;
    private TreeMap<String, Double> stocks;
    private String currTime;

    StockQuoteGetter quoteGetter = new StockQuoteGetter();

    StockAccount(Date time, List<User> owner) {
        super(time, owner);
    }

    public String getTime(){
        return currTime;
    }

    @Override
    public double getNetBalance() {
        return balance;
    }

    public void buyStock(String stockSymbol, Transaction register) throws IOException {
        double stockQuote = quoteGetter.getQuote(stockSymbol);
    }

    public void deposit(int stockAmount, double stockPrice, String stockSymbol ,Transaction register){
        double moneyDeposit = stockAmount*stockPrice;

        balance += moneyDeposit;

        registerTransaction(register);
    }

    public void withdraw(int stockAmount, double stockPrice, String stockSymbol, Transaction register)
            throws WithdrawException {
        currTime = AtmTime.FORMAT_STRING;

        double moneyWithdraw = stockAmount*stockPrice;

        if(Integer.parseInt(currTime.substring(14,15))<9 && Integer.parseInt(currTime.substring(14,15)) > 15){
            throw new IncorrectTimeException();
        }

        if(moneyWithdraw > balance){
            throw new InsufficientTimeException();
        }

        balance -= moneyWithdraw;

        registerTransaction(register);
    }
}
