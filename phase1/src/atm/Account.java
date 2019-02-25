package atm;

import java.util.ArrayList;
import java.util.Date;

abstract class Account {
    private final Date timeCreated;

    Account() {
        timeCreated = AtmTime.getCurrentTime();
    }

    Date getTimeCreated() {
        return timeCreated;
    }

    abstract int getBalance();

    abstract ArrayList<Transaction> getTransactions();

    abstract int getNetBalance();
}
