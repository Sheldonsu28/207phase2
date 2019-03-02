package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;

public class ChequingAccount extends AssetAccount implements Indebtable {

    private double debtLimit;

    public ChequingAccount(Date time, User owner) {
        super(time, owner);
        debtLimit = 100;
    }

    public ChequingAccount(Date time, User owner, double initialBalance) {
        super(time, owner, initialBalance);
        debtLimit = 100;
    }

    public ChequingAccount(Date time, User owner, double initialBalance, double debtLimit) {
        super(time, owner, initialBalance);
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
