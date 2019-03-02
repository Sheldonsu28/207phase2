package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;

public class LineOfCreditAccount extends DebtAccount implements Withdrawable {

    LineOfCreditAccount(Date time, User owner, int debtLimit) {
        super(time, owner, debtLimit);
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
