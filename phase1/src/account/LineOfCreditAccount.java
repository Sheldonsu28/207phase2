package account;

import transaction.Transaction;

public class LineOfCreditAccount extends DebtAccount implements Withdrawable {

    LineOfCreditAccount(int debtLimit) {
        super(debtLimit);
    }

    @Override
    /* TODO Implementation */
    public void withdraw(double amount, Transaction register) throws WithdrawException {

    }

    @Override
    public void cancelWithdraw(double amount) {

    }
}
