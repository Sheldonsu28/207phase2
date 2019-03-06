package atm;

import java.util.List;
import java.util.TreeMap;

public class AtmMachine {
    private CashHandler cashHandler;
    private AtmTime time;
    private static int prev_id = 0;
    private final String id;

    AtmMachine(AtmTime time, TreeMap<Integer, Integer> initialStock,
               CashDistributor distributor) {
        this.time = time;
        id = String.format("ATM%04d", prev_id);
        prev_id++;
        cashHandler = new CashHandler(time, initialStock, Currency.CAD, 20, distributor);
    }

    public List<Integer> getValidCashTypes() {
        return cashHandler.getValidCashTypes();
    }

    public String toString() {
        return String.format("ID %s STOCK %s", id, cashHandler);
    }

    public String getId() {
        return id;
    }

    public TreeMap<Integer, Integer> reduceStock(int amount) throws CashShortageException, InsufficientStockException {
        return cashHandler.takeAmountOfCash(amount);
    }

    public void increaseStock(TreeMap<Integer, Integer> increment) {
        cashHandler.storeCashStock(increment);
    }

    public String displayTime() {
        return time.getCurrentTimeStamp();
    }
}
