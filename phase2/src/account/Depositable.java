package account;

import transaction.Transaction;

/**
 * Characterizes any account that allows deposit action. This action can be cancelled.
 */
public interface Depositable extends Cancellable {

    /**
     * Deposit the amount of money to this account.
     *
     * @param amount   amount to deposit
     * @param register the source of the invocation of this deposit action
     */
    void deposit(double amount, Transaction register);

    /**
     * Cancel the deposit by reversing the deposit action.
     *
     * @param amount the amount of deposit to cancel
     */
    void cancelDeposit(double amount);
}
