package account;

import transaction.Transaction;

public abstract class AssetAccount extends Account implements Depositable, Withdrawable {
    AssetAccount() {
        super();
    }

    AssetAccount(double initialBalance) {
        super(initialBalance);
    }

    @Override
    public double getNetBalance() {
        return balance;
    }

    @Override
    //  TODO test
    public void deposit(double amount, Transaction register) {
        balance += amount;

        registerTransaction(register);
    }

    @Override
    //  TODO test
    public void cancelDeposit(double amount) {
        balance -= amount;
    }
}
