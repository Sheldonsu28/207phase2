package transaction;

import account.Account;
import account.Depositable;
import account.WithdrawException;
import account.Withdrawable;
import atm.User;
import ui.MainFrame;

/**
 * This class is responsible for Transaction between users and accounts.
 */
public class TransferTransaction extends Transaction {
    private final double transferAmount;
    private final Withdrawable fromAccount;
    private final Depositable toAccount;

    /**
     *  Initialize a transaction between two bank accounts within the same user account.
     *
     * @param user          User's user account.
     * @param fromAccount   Bank account to withdraw from.
     * @param toAccount     Bank account that receive the transaction.
     * @param amount        Transaction amount.
     */
    public TransferTransaction(User user, Withdrawable fromAccount, Depositable toAccount, double amount) {
        super(user);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to transfer negative amount: " + amount);

        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = amount;
    }

    /**
     * Initialize a transaction between tow users.
     *
     * @param fromUser      The user account that will be withdraw from.
     * @param fromAccount   The Bank account that will be withdrawn from.
     * @param toUser        The user account that will be receiving the transaction.
     * @param toAccount     The bank account that will be receiving the transaction.
     * @param amount        The transaction amount.
     */
    public TransferTransaction(User fromUser, Withdrawable fromAccount, User toUser, Depositable toAccount,
                               double amount) {
        super(fromUser, toUser);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to transfer negative amount: " + amount);

        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = amount;
    }

    /**
     * Initialize a transaction between user in side the system and user out side the system.
     *
     * @param fromUser      The user that will be withdraw from.
     * @param fromAccount   The bank account that will be withdrawn from.
     * @param toUser        The user that will receive the transaction.
     * @param amount        The transaction amount.
     */
    public TransferTransaction(User fromUser, Withdrawable fromAccount, User toUser, double amount) {
        super(fromUser, toUser);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to transfer negative amount: " + amount);

        this.fromAccount = fromAccount;
        this.toAccount = toUser.getPrimaryAccount();
        this.transferAmount = amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return super.toString() + String.format("%s-%s TRANSFERRED $%.2f TO %s-%s", getFromUser(), ((Account) fromAccount).getId(),
                transferAmount, getToUser(), ((Account) toAccount).getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doPerform() {
        try {
            fromAccount.withdraw(transferAmount, this);
        } catch (WithdrawException exception) {
            MainFrame.showErrorMessage(exception.getMessage());
            return false;
        }

        toAccount.deposit(transferAmount, this);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doCancel() {
        fromAccount.cancelWithdraw(transferAmount);
        toAccount.cancelDeposit(transferAmount);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancellable() {
        return true;
    }
}
