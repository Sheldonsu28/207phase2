package account;

import transaction.Transaction;

public abstract class DebtAccount extends Account implements Depositable, Indebtable {

    double debtLimit;

    DebtAccount() {
        super();
        debtLimit = DEFAULT_DEBT_LIMIT;
    }

    DebtAccount(double debtLimit) {
        super();
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
    /* TODO Implementation */
    public void deposit(double amount, Transaction register) {
        balance -= amount;
    }

    @Override
    public void cancelDeposit(double amount) {

    }
}
