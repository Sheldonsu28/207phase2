package atm;


import java.util.TreeMap;

public class AtmMachine {
    private BankManager manager;
    private CashHandler cashHandler;

    AtmMachine(BankManager manager, TreeMap<Integer, Integer> initialStock) {
        this.manager = manager;
        cashHandler = new CashHandler(manager, initialStock, Currency.CAD, 20);
    }
}
