package account;

import transaction.Transaction;

public abstract class AssetAccount extends Account implements Depositable, Withdrawable {
    AssetAccount() {
        super();
    }

    AssetAccount(int initialBalance) {
        super(initialBalance);
    }

    @Override
    public double getNetBalance() {
        return balance;
    }

    @Override
    public void deposit(double amount, Transaction register) {

    }

    @Override
    public void cancelDeposit(double amount) {

    }
}
