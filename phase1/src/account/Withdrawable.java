package account;

import transaction.Transaction;

/**
 * Characterizes any account that allows withdraw action. This action can be cancelled.
 */
public interface Withdrawable extends Cancellable {

    /**
     * Withdraw money from this account.
     *
     * @param amount   amount to withdraw
     * @param register the source of the invocation of this withdraw action
     * @throws WithdrawException if an error occurred during withdraw
     */
    void withdraw(double amount, Transaction register) throws WithdrawException;

    /**
     * Cancel the withdraw by reversing the withdraw action.
     *
     * @param amount the amount of withdraw to cancel
     */
    void cancelWithdraw(double amount);
}
