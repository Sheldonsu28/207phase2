package account;

import atm.AtmTime;
import transaction.Transaction;

public abstract class AssetAccount extends Account implements Depositable, Withdrawable {
    AssetAccount(AtmTime time) {
        super(time);
    }

    AssetAccount(AtmTime time, double initialBalance) {
        super(time, initialBalance);
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
