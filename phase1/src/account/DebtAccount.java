package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;

public abstract class DebtAccount extends Account implements Depositable, Indebtable {

    double debtLimit;

    DebtAccount(Date time, User owner) {
        super(time, owner);
        debtLimit = DEFAULT_DEBT_LIMIT;
    }

    DebtAccount(Date time, User owner, double debtLimit) {
        super(time, owner);
        this.debtLimit = debtLimit;
    }

    @Override
    public double getNetBalance() {
        return -balance;
    }

    public void setDebtLimit(double debtLimit) {
        this.debtLimit = debtLimit;
    }

    @Override
    public double getDebtLimit() {
        return debtLimit;
    }

    @Override
    //  TODO test
    public void deposit(double amount, Transaction register) {
        balance -= amount;

        registerTransaction(register);
    }

    @Override
    //  TODO test
    public void cancelDeposit(double amount) {
        balance += amount;
    }
}
