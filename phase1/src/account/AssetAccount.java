package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;

public abstract class AssetAccount extends Account implements Depositable, Withdrawable {
    AssetAccount(Date time, User owner) {
        super(time, owner);
    }

    AssetAccount(Date time, User owner, double initialBalance) {
        super(time, owner, initialBalance);
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
