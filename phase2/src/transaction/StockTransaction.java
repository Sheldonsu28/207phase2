package transaction;

import account.WithdrawException;
import atm.User;
import account.StockAccount;

public class StockTransaction extends Transaction{

    private String stockName;
    private int stockAmount;
    private int stockPrice;
    private StockAccount fromAccount;

    public StockTransaction(User user, StockAccount account, int Amount, int price, String stockName){
        super(user);
        this.stockAmount = Amount;
        this.stockPrice = price;
        this.fromAccount = account;
        this.stockName = stockName;
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
        try {
            fromAccount.withdraw(stockAmount, stockPrice, stockName, this);
        }catch (WithdrawException e){
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
