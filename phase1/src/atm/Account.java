package atm;

import java.util.ArrayList;
import java.util.Date;

abstract class Account {
    private final Date timeCreated;
    private final String id;
    private static int prev_id = 0;

    Account() {
        id = String.format("%04d", prev_id);
        prev_id++;
        timeCreated = AtmTime.getCurrentTime();
    }

    String getId() {
        return id;
    }

    Date getTimeCreated() {
        return timeCreated;
    }

    abstract double getBalance();

    abstract ArrayList<Transaction> getTransactions();

    abstract double getNetBalance();
}
