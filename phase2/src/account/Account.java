package account;

import atm.User;
import transaction.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Stores universal account information and defines universal behaviours for an account.
 *
 * @author zhaojuna
 * @version 1.0
 */
public abstract class Account implements Serializable {

    /**
     * Counts the number of account instances created to keep uniqueness of each id.
     */
    private static int prev_id = 0;

    /**
     * Time stamp when the account is created.
     */
    private final Date timeCreated;

    /**
     * The unique identifier of the account. Formatted as "ACC####".
     */
    private final String id;

    /**
     * List of owner of this account.
     */
    private final User[] owner = new User[2];

    /**
     * The list of transaction related to this account.
     */
    private List<Transaction> transactions;

    /**
     * The apparent balance(not net balance) of the account.
     */
    double balance;


    /**
     * Creates an account with given creation time stamp and owner user.
     * This is the default constructor for all accounts.
     *
     * @param time  time of creation
     * @param owner owner user
     */
    public Account(Date time, User owner) {
        this(time, owner, null, 0);
    }

    /**
     * Creates an account with an initial balance in addition to the default constructor - {@link #Account(Date, User)}.
     *
     * @param time           time of creation
     * @param owner          owner user
     * @param initialBalance initial apparent balance of the account
     */
    Account(Date time, User owner, double initialBalance) {
        this(time, owner, null, initialBalance);
    }

    /**
     * Creates a joint account in addition to the default constructor
     *
     * @param time           time of creation
     * @param owner          owner user
     * @param owner2         owner2 user
     */
    Account(Date time, User owner, User owner2) {
        this(time, owner, owner2, 0);
    }

    /**
     * Creates a joint account with an initial balance
     *
     * @param time           time of creation
     * @param owner          owner user
     * @param owner2         owner2 user
     * @param initialBalance initial apparent balance of the account
     */
    Account(Date time, User owner, User owner2, double initialBalance) {
        id = String.format("ACC%04d", prev_id);
        prev_id++;
        timeCreated = time;
        balance = 0.0;
        transactions = new ArrayList<>();
        this.owner[0] = owner;
        this.owner[1]= owner2;
        this.balance = initialBalance;
    }

    /**
     * @return owner of this account
     */
    public User[] getOwner() {
        return owner;
    }

    /**
     * @return unique identifier of this account formatted as "ACC####"
     */
    public String getId() {
        return id;
    }

    /**
     * @return the time of creation of this account
     */
    public Date getTimeCreated() {
        return timeCreated;
    }

    /**
     * Gets the apparent balance (not net balance) of this account.
     * Use {@link #getNetBalance()} for getting net balance.
     *
     * @return apparent balance of this account
     */
    public double getBalance() {
        return balance;
    }

    /**
     * @return a read-only recent transactions list
     */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /**
     * Link a currently-performing transaction to this account. Registration will fail if the given transaction has
     * already been performed.
     *
     * @param transaction the currently-performing transaction to be registered
     */
    void registerTransaction(Transaction transaction) {
        if (transaction.isPerformed())
            throw new IllegalStateException("Can not register performed transaction.");

        transactions.add(transaction);
    }

    /**
     * @return the last performed but not cancelled transaction. null if there is no such transaction registered.
     */
    public Transaction getLastTransaction() {

        for (int index = transactions.size() - 1; index >= 0; index--) {
            Transaction transaction = transactions.get(index);

            if (!transaction.isCancelled())
                return transaction;
        }

        return null;
    }

    public void setOwner2(User owner2) {
        this.owner[1] = owner2;
    }

    public List<Transaction> getFullTransaction(){
        return transactions;
    }

    /**
     * @return a String containing information about this account (owner, id, date of creation, apparent balance).
     */
    public String toString() {
        return String.format("Owner: %s, ID: %s, TYPE: %s, Date Created: %s, Balance: %s",
                owner, getId(), owner.getClass().getSimpleName(), timeCreated, balance);
    }

    /**
     * @return the net balance of this account
     */
    public abstract double getNetBalance();
}
