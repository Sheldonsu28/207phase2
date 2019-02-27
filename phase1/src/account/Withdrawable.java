package account;

import transaction.Transaction;

public interface Withdrawable extends Cancellable {

    void withdraw(double amount, Transaction register) throws WithdrawException;

    void cancelWithdraw(double amount);
}
