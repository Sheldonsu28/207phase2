package account;

import atm.User;
import transaction.Transaction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

    public static final Class[] OWNABLE_ACCOUNT_TYPES = {ChequingAccount.class, CreditCardAccount.class,
            LineOfCreditAccount.class, SavingsAccount.class, StockAccount.class};

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
    private final ArrayList<User> owners;

    /**
     * The list of transaction related to this account.
     */
    private List<Transaction> transactions;

    /**
     * The apparent balance(not net balance) of the account.
     */
    double balance;

    /**
     * Creates an account
     *
     * @param time   time of creation
     * @param owners owner users
     */
    Account(Date time, List<User> owners) {
        id = String.format("ACC%04d", prev_id);
        prev_id++;
        timeCreated = time;
        balance = 0.0;
        transactions = new ArrayList<>();
        this.owners = new ArrayList<>();
        this.owners.addAll(owners);
    }

    /**
     * Creates an account with a given initial balance
     *
     * @param time           time of creation
     * @param owners         owner users
     * @param initialBalance the initial balance on the account
     */
    Account(Date time, List<User> owners, double initialBalance) {
        this(time, owners);
        this.balance = initialBalance;
    }

    /**
     * @return owner of this account
     */
    public List<User> getOwners() {
        return owners;
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

    /**
     * @return a String containing information about this account (owner, id, date of creation, apparent balance).
     */
    public String toString() {
        return String.format("%s  %s  %s  %s  $%s",
                owners.toString(), getId(), getClass().getSimpleName(),
                new SimpleDateFormat("yyyy-MM-dd").format(timeCreated), balance);
    }

    /**
     * @return the net balance of this account
     */
    public abstract double getNetBalance();
}
