package account;

import atm.AtmTime;
import transaction.Transaction;

import java.util.Date;
import java.util.List;

//  TODO implement toString and comparison
public abstract class Account {
    private final Date timeCreated;
    private final String id;
    private static int prev_id = 0;
    double balance;
    List<Transaction> transactions;

    private Account() {
        throw new IllegalStateException("Account must be registered with an initial time");
    }

    Account(AtmTime time) {
        id = String.format("A%04d", prev_id);
        prev_id++;
        timeCreated = time.getCurrentTime();
        balance = 0.0;
    }

    Account(AtmTime time, double initialBalance) {
        this(time);
        balance = initialBalance;
    }

    public String getId() {
        return id;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    void registerTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public abstract double getNetBalance();
}
