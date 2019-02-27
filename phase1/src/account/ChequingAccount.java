package account;

import transaction.Transaction;

public class ChequingAccount extends AssetAccount implements Indebtable {

    private double debtLimit;

    public ChequingAccount() {
        super();
        debtLimit = 100;
    }

    public ChequingAccount(double initialBalance) {
        super(initialBalance);
        debtLimit = 100;
    }

    public ChequingAccount(double initialBalance, double debtLimit) {
        super(initialBalance);
        this.debtLimit = debtLimit;
    }

    @Override
    // TODO test
    public void withdraw(double amount, Transaction register) throws WithdrawException {
        if (amount - balance > debtLimit)
            throw new DebtLimitExceededException(this, amount);

        if (balance < 0)
            throw new InsufficientFundException(this, amount);

        balance -= amount;

        registerTransaction(register);
    }

    @Override
    //  TODO test
    public void cancelWithdraw(double amount) {
        balance += amount;
    }

    @Override
    public double getDebtLimit() {
        return debtLimit;
    }

    @Override
    public void setDebtLimit(double debtLimit) {
        this.debtLimit = debtLimit;
    }
}
