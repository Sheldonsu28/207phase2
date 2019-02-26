package account;

import atm.AtmTime;
import transaction.Transaction;

import java.util.ArrayList;
import java.util.Date;

public abstract class Account {
    private final Date timeCreated;
    private final String id;
    private static int prev_id = 0;

    Account() {
        id = String.format("A%04d", prev_id);
        prev_id++;
        timeCreated = AtmTime.getCurrentTime();
    }

    public String getId() {
        return id;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public abstract double getBalance();

    abstract ArrayList<Transaction> getTransactions();

    public abstract double getNetBalance();
}
