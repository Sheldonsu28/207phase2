package transaction;

import account.Depositable;
import atm.User;

public class DepositTransaction extends IntraUserTransaction {
    private final int depositAmount;
    private final Depositable targetAccount;

    public DepositTransaction(User user, Depositable account, int amount) {
        super(user);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to deposit negative amount: " + amount);

        depositAmount = amount;
        targetAccount = account;
    }

    @Override
    protected boolean doPerform() {
        targetAccount.deposit(depositAmount, this);
        return true;
    }

    @Override
    protected boolean doCancel() {
        targetAccount.cancelDeposit(depositAmount);
        return true;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

}
