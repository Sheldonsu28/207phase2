package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;

/**
 * Defines behaviours for a billing account. Billing accounts are independent (i.e. not owned by any user).
 * Only action allowed is paying bills. Interactions with billing accounts will be recorded in /data/outgoing.txt
 *
 * @author zhaojuna
 * @version 1.0
 */
public class BillingAccount extends Account {

    private final String payeeName;

    /**
     * In addition to {@linkplain Account}'s default constructor, set this billing account's payee name.
     *
     * @param time      time of creation
     * @param payeeName name of this billing account(payee)
     * @see Account#Account(Date, User)
     */
    public BillingAccount(Date time, String payeeName) {
        super(time, null);
        this.payeeName = payeeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getNetBalance() {
        return balance;
    }

    /**
     * @return unique identifier of this account formatted as "BILLACC####"
     */
    public String getId() {
        return "BILL" + super.getId();
    }

    /**
     * @return name of this billing account (payee name)
     */
    public String getPayeeName() {
        return payeeName;
    }

    /**
     * Receive an amount of pay. This should only be invoked by a {@linkplain Transaction}
     *
     * @param amount   amount of pay
     * @param register the transaction to be registered to this account
     */
    public void receivePay(double amount, Transaction register) {
        balance += amount;

        registerTransaction(register);
    }
}
