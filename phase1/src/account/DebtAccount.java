package account;

import atm.AtmTime;
import transaction.Transaction;

public abstract class DebtAccount extends Account implements Depositable, Indebtable {

    double debtLimit;

    DebtAccount(AtmTime time) {
        super(time);
        debtLimit = DEFAULT_DEBT_LIMIT;
    }

    DebtAccount(AtmTime time, double debtLimit) {
        super(time);
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
