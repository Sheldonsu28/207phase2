package transaction;

import account.*;
import atm.User;

public class StockTransaction extends Transaction{

    private String stockName;
    private int stockAmount;
    private double stockPrice;
    private StockAccount fromAccount;
    private boolean buy;
    //if True, buy; if


    public StockTransaction(User user, StockAccount account, int Amount, double price, String stockName, boolean buy){
        super(user);
        this.stockAmount = Amount;
        this.stockPrice = price;
        this.fromAccount = account;
        this.stockName = stockName;
        this.buy = buy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancellable() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doPerform(){
        if(buy) {
            try {
                fromAccount.buyStock(stockAmount, stockPrice, stockName, this);
            } catch (WithdrawException e) {
                return false;
            }
            return true;
        }

        try {
            fromAccount.sellStock(stockAmount, stockPrice, stockName, this);
        } catch (InsufficientSharesException | IncorrectTimeException e) {
            return false;
        }
        return true;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        return super.toString() +
                String.format("User %s's Account %s\t BUY IN %s With amount %d At price %d",
                        getFromUser(),fromAccount,stockName, stockAmount, stockPrice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doCancel(){
        throw new IllegalStateException("Billing action is not cancellable!");
    }
}
