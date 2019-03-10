package transaction;

import account.Depositable;
import account.WithdrawException;
import account.Withdrawable;
import atm.User;

public class IntraUserTransferTransaction extends Transaction {
    private final double transferAmount;
    private final Withdrawable fromAccount;
    private final Depositable toAccount;

    public IntraUserTransferTransaction(User user, Withdrawable fromAccount, Depositable toAccount, double amount) {
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
            System.out.println(exception.getMessage());
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
