package account;

import transaction.Transaction;

public class ChequingAccount extends AssetAccount implements Indebtable {

    @Override
    /* TODO Implementation */
    public void withdraw(double amount, Transaction register) throws WithdrawException {

    }

    @Override
    public void cancelWithdraw(double amount) {

    }

    @Override
    public double getDebtLimit() {
        return 0;
    }

    @Override
    public void setDebtLimit(double debtLimit) {

    }
}
