package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;

/**
 * Defines behaviours for an asset account. Both deposit and withdraw are allowed.
 *
 * @author zhaojuna
 * @version 1.0
 */
public abstract class AssetAccount extends Account implements Depositable, Withdrawable {

    /**
     * @param time  time of creation
     * @param owner owner user
     * @see Account#Account(Date, User)
     */
    AssetAccount(Date time, User owner) {
        super(time, owner);
    }

    /**
     * @param time           time of creation
     * @param owner          owner user
     * @param initialBalance initial apparent balance of the account
     * @see Account#Account(Date, User, double)
     */
    AssetAccount(Date time, User owner, double initialBalance) {
        super(time, owner, initialBalance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getNetBalance() {
        return balance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deposit(double amount, Transaction register) {
        balance += amount;

        registerTransaction(register);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelDeposit(double amount) {
        balance -= amount;
    }
}
