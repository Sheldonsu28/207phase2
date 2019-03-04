package account;

import atm.User;
import transaction.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//  TODO implement toString and comparison
public abstract class Account implements Serializable {
    private final Date timeCreated;
    private final String id;
    private final User owner;
    private static int prev_id = 0;
    double balance;
    List<Transaction> transactions;

    private Account() {
        throw new IllegalStateException("Account must be registered with an initial time");
    }

    Account(Date time, User owner) {
        id = String.format("A%04d", prev_id);
        prev_id++;
        timeCreated = time;
        balance = 0.0;
        transactions = new ArrayList<>();
        this.owner = owner;
    }

    Account(Date time, User owner, double initialBalance) {
        this(time, owner);
        balance = initialBalance;
    }

    public User getOwner() {
        return owner;
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

    public Transaction getLastTransaction() {
        return transactions.get(transactions.size() - 1);
    }

    public abstract double getNetBalance();
}
