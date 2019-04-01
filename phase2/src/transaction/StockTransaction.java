package transaction;

import account.IncorrectTimeException;
import account.InsufficientSharesException;
import account.StockAccount;
import account.WithdrawException;
import atm.User;
import ui.MainFrame;

public class StockTransaction extends Transaction{

    private final String stockName;
    private final int stockAmount;
    private final double stockPrice;
    private final StockAccount fromAccount;
    private final boolean buy;


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
            } catch (IncorrectTimeException e) {
                MainFrame.showErrorMessage(e.getMessage());
                return false;
            } catch (WithdrawException e){
                MainFrame.showErrorMessage(e.getMessage());
                return false;
            }
            return true;
        } else {
            try {
                fromAccount.sellStock(stockAmount, stockPrice, stockName, this);
            }catch (IncorrectTimeException e) {
                    MainFrame.showErrorMessage(e.getMessage());
                    return false;
            } catch (InsufficientSharesException e) {
                MainFrame.showErrorMessage(e.getMessage());
                return false;
            }
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        return super.toString() + String.format("%s-%s BUY IN %d %s STOCK %.2f/share",
                getFromUser(), fromAccount.getId(), stockAmount, stockName, stockPrice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doCancel(){
        throw new IllegalStateException("Billing action is not cancellable!");
    }
}
