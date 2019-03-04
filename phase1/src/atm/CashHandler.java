package atm;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.TreeMap;

class CashHandler extends Observable {
    private TreeMap<Integer, Integer> cashStock;
    private Currency currency;
    private int alertLevel;
    private AtmTime time;
    private CashDistributor cashDistributor;

    CashHandler(AtmTime time, FileHandler fileHandler, TreeMap<Integer, Integer> cashStock, Currency currency,
                int alertLevel, CashDistributor distributor) {
        this.currency = currency;
        this.alertLevel = alertLevel;
        this.cashStock = new TreeMap<>();
        this.time = time;
        cashDistributor = distributor;

        for (int key : cashStock.keySet())
            this.cashStock.put(key, cashStock.get(key));

        addObserver(fileHandler);
    }

    Currency getCurrency() {
        return currency;
    }

    int getCashTypeCount() {
        return cashStock.size();
    }

    int getTotalBalance() {
        int balance = 0;

        for (Entry<Integer, Integer> entry : cashStock.entrySet())
            balance += entry.getKey() * entry.getValue();

        return balance;
    }

    Map<Integer, Integer> getCashStock() {
        return Collections.unmodifiableMap(cashStock);
    }

    // TODO test required
    TreeMap<Integer, Integer> takeAmountOfCash(int amount) throws InsufficientStockException, CashShortageException {
        if (amount % 5 != 0)
            throw new IllegalArgumentException("Can't produce amount that is not a multiplier of 5!");

        if (getTotalBalance() < amount)
            throw new InsufficientStockException(this);

        TreeMap<Integer, Integer> take = cashDistributor.distribute(cashStock, amount);

        for (Entry<Integer, Integer> entry : take.entrySet())
            cashStock.put(entry.getKey(), cashStock.get(entry.getKey()) - entry.getValue());

        stockCheck();

        return take;
    }

    // TODO test required
    public void stockCheck() {
        StringBuilder alertMsg = new StringBuilder(time + "\tStock shortage: ");
        boolean hasAlert = false;

        for (Entry<Integer, Integer> entry : cashStock.entrySet()) {
            int type = entry.getKey();
            int stock = entry.getValue();

            if (stock < alertLevel) {
                hasAlert = true;
                alertMsg.append(String.format("$%d: %d left\t", type, stock));
            }
        }

        if (hasAlert) {
            setChanged();
            notifyObservers(new String[]{"alert.txt", alertMsg.toString()});
        }
    }

    public String toString(){
        return String.format("Five dollar: %s\nTen dollar: %s\nTwenty dollar: %s\nTotal amount: %s\nCurrency: %s",
                cashStock.get(5), cashStock.get(10), cashStock.get(20), getTotalBalance(), currency);
    }
}
