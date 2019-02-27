package transaction;

import account.WithdrawException;
import account.Withdrawable;
import atm.User;

public class WithdrawTransaction extends IntraUserTransaction {
    private final int withdrawAmount;
    private final Withdrawable targetAccount;

    public WithdrawTransaction(User user, Withdrawable account, int amount) {
        super(user);
        withdrawAmount = amount;
        targetAccount = account;
    }

    @Override
    protected boolean doPerform() {
        try {
            targetAccount.withdraw(withdrawAmount, this);
            return true;
        } catch (WithdrawException exception) {

            // TODO exception handling & passing message

            return false;
        }
    }

    @Override
    protected boolean doCancel() {
        targetAccount.cancelWithdraw(withdrawAmount);
        return true;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
