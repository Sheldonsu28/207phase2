package atm;

import java.util.*;

class CashHandler extends Observable {
    private TreeMap<Integer, Integer> cashStock;
    private Currency currency;
    private int alertLevel;

    CashHandler(BankManager manager, int bill5, int bill10, int bill20, int bill50, Currency currency, int alertLevel) {
        this.currency = currency;
        cashStock = new TreeMap<>();
        cashStock.put(5, bill5);
        cashStock.put(10, bill10);
        cashStock.put(20, bill20);
        cashStock.put(50, bill50);
        this.alertLevel = alertLevel;

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
    HashMap<Integer, Integer> takeAmountOfCash(int amount) throws InsufficientStockException {
        if (amount % 5 != 0)
            throw new IllegalArgumentException("Can't produce amount that is not a multiplier of 5!");

        if (getAmount() < amount)
            throw new InsufficientStockException(this);

        return null;
        // TODO best cash take logic to be implemented
    }

    // TODO to be implemented
    // TODO test required
    void sendAlert() {

    }
}
