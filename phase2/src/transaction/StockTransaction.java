package transaction;

import atm.User;

public class StockTransaction extends Transaction{

    private int stockAmount;
    private int stockPrice;

    public StockTransaction(User user, int Amount, int price){
        super(user);
        this.stockAmount = Amount;
        this.stockPrice = price;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public boolean doPerform(){

        return true;
    }

    @Override
    public boolean doCancel(){
        return true;
    }
}
