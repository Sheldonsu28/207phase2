package transaction;

import account.Depositable;
import account.WithdrawException;
import account.Withdrawable;
import atm.User;

public class TransferTransaction extends IntraUserTransaction {
    private final int transferAmount;
    private final Withdrawable fromAccount;
    private final Depositable toAccount;

    public TransferTransaction(User user, Withdrawable fromAccount, Depositable toAccount, int amount) {
        super(user);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to transfer negative amount: " + amount);

        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = amount;
    }

    @Override
    protected boolean doPerform() {
        try {
            fromAccount.withdraw(transferAmount, this);
        } catch (WithdrawException exception) {

            // TODO exception handling & passing message

            return false;
        }

        toAccount.deposit(transferAmount, this);
        return true;
    }

    @Override
    protected boolean doCancel() {
        fromAccount.cancelWithdraw(transferAmount);
        toAccount.cancelDeposit(transferAmount);
        return true;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
