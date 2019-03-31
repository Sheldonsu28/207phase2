package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;
import java.util.List;

/**
 * Defines behaviours of chequing account. Chequing account is considered the default account type for any user,
 * and is also considered default deposit account for a transaction.
 *
 * @author zhaojuna
 * @version 1.0
 */
public class ChequingAccount extends AssetAccount implements Indebtable {
    /**
     * A constant represents the default debt limit of a chequing account.
     */
    public static final int DEFAULT_CHEQUING_DEBT_LIMIT = 100;

    /**
     * The debt limit of this account. It should be positive. Default value is {@link #DEFAULT_CHEQUING_DEBT_LIMIT}.
     */
    private int debtLimit;

    /**
     * Create a default chequing account with {@link #DEFAULT_CHEQUING_DEBT_LIMIT}.
     *
     * @param time  time of creation
     * @param owner owner user
     * @see Account#Account(Date, List)
     */
    public ChequingAccount(Date time, List<User> owner) {
        super(time, owner);
        debtLimit = DEFAULT_CHEQUING_DEBT_LIMIT;
    }

    /**
     * Create a default chequing account with {@link #DEFAULT_CHEQUING_DEBT_LIMIT} with the given initial balance.
     *
     * @param time           time of creation
     * @param owner          owner user
     * @param initialBalance initial apparent balance of the account
     * @see Account#Account(Date, List, double)
     */
    public ChequingAccount(Date time, List<User> owner, double initialBalance) {
        super(time, owner, initialBalance);
        debtLimit = DEFAULT_CHEQUING_DEBT_LIMIT;
    }

    /**
     * Creates a chequing with the given debt limit and initial balance.
     *
     * @param time           time of creation
     * @param owner          owner user
     * @param initialBalance initial apparent balance of the account
     * @param debtLimit      the debt limit of this account
     * @see Account#Account(Date, List, double)
     */
    public ChequingAccount(Date time, List<User> owner, double initialBalance, int debtLimit) {
        super(time, owner, initialBalance);
        this.debtLimit = debtLimit;
    }

    /**
     * {@inheritDoc}
     * The transaction will be registered into account's transaction list.
     *
     * @throws WithdrawException if a withdraw is requested when current balance is already negative,
     *                           or the requested amount exceeds debt limit
     */
    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException {
        if (amount - balance > debtLimit)
            throw new DebtLimitExceededException(this, amount);

        if (balance < 0)
            throw new InsufficientFundException(this, amount);

        balance -= amount;

        registerTransaction(register);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelWithdraw(double amount) {
        balance += amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDebtLimit() {
        return debtLimit;
    }

    /**
     * {@inheritDoc}
     * Absolute value will be taken if given debt limit is negative.
     */
    @Override
    public void setDebtLimit(int debtLimit) {
        this.debtLimit = Math.abs(debtLimit);
    }
}
