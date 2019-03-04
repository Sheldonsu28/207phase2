package atm;

import java.util.TreeMap;

public class AtmMachine {
    private CashHandler cashHandler;
    private CashDistributor cashDistributor;
    private AtmTime time;

    AtmMachine(AtmTime time, TreeMap<Integer, Integer> initialStock, CashDistributor distributor) {
        this.time = time;
        cashHandler = new CashHandler(time, initialStock, Currency.CAD, 20, distributor);
    }

    public void reduceStock(int amount) {

    }

}
