package atm;

import java.util.*;

class CashHandler extends Observable {
    private TreeMap<Integer, Integer> cashStock;
    private Currency currency;
    private int alertLevel;
    private CashDistributor cashDistributor;

    CashHandler(BankManager manager, TreeMap<Integer, Integer> cashStock, Currency currency, int alertLevel) {
        this.currency = currency;
        this.alertLevel = alertLevel;
        this.cashStock = new TreeMap<>();
        cashDistributor = new StepCashDistributor();

        for (int key : cashStock.keySet()) {
            this.cashStock.put(key, cashStock.get(key));
        }

        addObserver(manager);
    }

    Currency getCurrency() {
        return currency;
    }

    int getCashTypeCount() {
        return cashStock.size();
    }

    int getAmount() {
        return cashStock.get(5) * 5 + cashStock.get(10) * 10 + cashStock.get(20) * 20 + cashStock.get(50) * 50;
    }

    Map<Integer, Integer> getCashStock() {
        return Collections.unmodifiableMap(cashStock);
    }

    // TODO test required
    TreeMap<Integer, Integer> takeAmountOfCash(int amount) throws InsufficientStockException, CashShortageException {
        if (amount % 5 != 0)
            throw new IllegalArgumentException("Can't produce amount that is not a multiplier of 5!");

        if (getAmount() < amount)
            throw new InsufficientStockException(this);

        TreeMap<Integer, Integer> take = cashDistributor.distribute(cashStock, amount);

        for (Map.Entry<Integer, Integer> entry : take.entrySet())
            cashStock.put(entry.getKey(), cashStock.get(entry.getKey()) - entry.getValue());

        return take;
    }

    // TODO to be implemented
    // TODO test required
    void sendAlert() {

    }

    public String toString(){
        return String.format("Five dollar: %s\nTen dollar: %s\nTwenty dollar: %s\nTotal amount: %s\nCurrency: %s",
                cashStock.get(5), cashStock.get(10),cashStock.get(20),getAmount(), currency);
    }
}
