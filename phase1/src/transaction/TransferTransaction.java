package transaction;

import account.Depositable;
import account.WithdrawException;
import account.Withdrawable;
import atm.User;

public class TransferTransaction extends Transaction {
    private final double transferAmount;
    private final Withdrawable fromAccount;
    private final Depositable toAccount;

    public TransferTransaction(User user, Withdrawable fromAccount, Depositable toAccount, double amount) {
        super(user);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to transfer negative amount: " + amount);

        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = amount;
    }

    public TransferTransaction(User fromUser, Withdrawable fromAccount, User toUser, Depositable toAccount,
                               double amount) {
        super(fromUser, toUser);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to transfer negative amount: " + amount);

        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = amount;
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format("User %s's Account %s TRANSFERRED $%.2f TO User %s's Account %s",
                        getFromUser(), fromAccount, transferAmount, getToUser(), toAccount);
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
