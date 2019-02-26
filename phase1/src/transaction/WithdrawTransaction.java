package transaction;

import atm.User;

public class WithdrawTransaction extends IntraUserTransaction {

    WithdrawTransaction(User user) {
        super(user);
    }

    @Override
    protected void doPerform() {

    }

    @Override
    protected void doCancel() {

    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
