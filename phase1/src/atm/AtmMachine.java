package atm;

import java.util.TreeMap;

public class AtmMachine {
    private CashHandler cashHandler;
    private AtmTime time;

    AtmMachine(AtmTime time, FileHandler fileHandler, TreeMap<Integer, Integer> initialStock,
               CashDistributor distributor) {
        this.time = time;
        cashHandler = new CashHandler(time, fileHandler, initialStock, Currency.CAD, 20, distributor);
    }

    public void reduceStock(int amount) {

    }

}
