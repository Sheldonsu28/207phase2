package account;

import transaction.Transaction;

public interface Depositable extends Cancellable {
    void deposit(double amount, Transaction register);

    void cancelDeposit(double amount);
}
