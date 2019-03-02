package account;

import atm.AtmTime;
import transaction.Transaction;

public class LineOfCreditAccount extends DebtAccount implements Withdrawable {

    LineOfCreditAccount(AtmTime time, int debtLimit) {
        super(time, debtLimit);
    }

    @Override
    //  TODO test
    public void withdraw(double amount, Transaction register) throws WithdrawException {
        if (balance + amount > debtLimit)
            throw new DebtLimitExceededException(this, amount);

        balance += amount;

        registerTransaction(register);
    }

    @Override
    //  TODO test
    public void cancelWithdraw(double amount) {
        balance -= amount;
    }
}
