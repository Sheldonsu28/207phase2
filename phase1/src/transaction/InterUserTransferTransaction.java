package transaction;

import account.Depositable;
import account.WithdrawException;
import account.Withdrawable;
import atm.User;

public class InterUserTransferTransaction extends Transaction {
    private final double transferAmount;
    private final Withdrawable fromAccount;
    private final Depositable toAccount;

    public InterUserTransferTransaction(User from, Withdrawable fromAccount, User to, double amount) {
        super(from, to);

        transferAmount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = to.getPrimaryAccount();
    }

    public InterUserTransferTransaction(User from, Withdrawable fromAccount, User to, Depositable toAccount,
                                        double amount) {
        super(from, to);

        transferAmount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    @Override
    protected boolean doPerform() {
        try {
            fromAccount.withdraw(transferAmount, this);
        } catch (WithdrawException e) {
            System.out.println(e.getMessage());
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
