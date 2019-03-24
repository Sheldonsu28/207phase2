package atm;

import java.util.*;
import java.util.Map.Entry;

class CashHandler {
    private TreeMap<Integer, Integer> cashStock;
    private Currency currency;
    private int alertLevel;
    private AtmTime time;
    private CashDistributor cashDistributor;

    /**
     * Create a default Cash Handler that
     * @param time ATM's time.
     * @param cashStock The cash Stock.
     * @param currency The currency type.
     * @param alertLevel The alert level.
     * @param distributor The distributor related to this Cash handler.
     */
    CashHandler(AtmTime time, TreeMap<Integer, Integer> cashStock, Currency currency,
                int alertLevel, CashDistributor distributor) {
        this.currency = currency;
        this.alertLevel = alertLevel;
        this.cashStock = new TreeMap<>();
        this.time = time;
        cashDistributor = distributor;

        for (int key : cashStock.keySet())
            this.cashStock.put(key, cashStock.get(key));
    }

    /**
     * Get the type of the currency
     * @return The type of the currency(USD or CAD).
     */
    Currency getCurrency() {
        return currency;
    }

    /**
     * Get the valid cash type such as 10 dollar bill, 20 dollar bill.
     * @return Cash types in the stock.
     */
    List<Integer> getValidCashTypes() {
        return new ArrayList<>(cashStock.keySet());
    }

    /**
     * Determine how many type of cashes with different values is in the stock.
     * @return Number of different cash types in the cashStock.
     */
    int getCashTypeCount() {
        return cashStock.size();
    }

    /**
     * Return the total amount of cash available in the cash stock.
     * @return The total balance available in the cash stock.
     */
    int getTotalBalance() {
        int balance = 0;

        for (Entry<Integer, Integer> entry : cashStock.entrySet())
            balance += entry.getKey() * entry.getValue();

        return balance;
    }

    /**
     * Get and return the cash stock in this cash handler
     * @return cash stock within the cash handler.
     */
    Map<Integer, Integer> getCashStock() {
        return Collections.unmodifiableMap(cashStock);
    }

    /**
     * Take in a cash stock, store the new sock to the old stock if the new cash stock have the same type of currency.
     * @param stock A cash stock that need to be stored.
     */
    void storeCashStock(TreeMap<Integer, Integer> stock) throws InvalidCashTypeException {
        for (int type : stock.keySet()) {
            if (cashStock.containsKey(type))
                cashStock.put(type, cashStock.get(type) + stock.get(type));
            else
                throw new InvalidCashTypeException(type);
        }
    }

    /**
     * Take the amount of cash from the
     * @param amount The mount of cash that will be remove from the stock.
     * @return  Return a TreeMap with cash type as the keyset and amount as the value.
     * @throws EmptyStockException When the stock is empty, this exception is thrown.
     * @throws CashShortageException When there is not enough cash, this exception is thrown.
     */
    TreeMap<Integer, Integer> takeAmountOfCash(int amount) throws EmptyStockException, CashShortageException {
        if (amount % 5 != 0)
            throw new IllegalArgumentException("Can't produce amount that is not a multiplier of 5!");

        if (getTotalBalance() < amount)
            throw new EmptyStockException(this);

        TreeMap<Integer, Integer> take = cashDistributor.distribute(cashStock, amount);

        for (Entry<Integer, Integer> entry : take.entrySet())
            cashStock.put(entry.getKey(), cashStock.get(entry.getKey()) - entry.getValue());

        stockCheck();

        return take;
    }

    /**
     * Go through the cash stock and check the amount of cash in the stock, if the cash is lower than
     * the threshold set by alertLevel, write the alert into the file called alert.txt.
     */
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

        if (hasAlert)
            (new FileHandler()).saveTo(ExternalFiles.CASH_ALERT_FILE, alertMsg.toString());
    }

    /**
     * The string representation of Cash Handler in format:
     * "Five dollar: amount"
     * "Ten dollar: amount"
     * "Twenty dollar: amount"
     * "Total amount: amount"
     * "Currency: currency type"
     * @return The String representation of this class
     */
    public String toString() {
        return String.format("Five dollar: %s\nTen dollar: %s\nTwenty dollar: %s\nTotal amount: %s\nCurrency: %s",
                cashStock.get(5), cashStock.get(10), cashStock.get(20), getTotalBalance(), currency);
    }
}
