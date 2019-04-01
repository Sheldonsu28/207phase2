package transaction;

import account.Account;
import account.WithdrawException;
import account.Withdrawable;
import atm.*;
import ui.MainFrame;

import java.util.TreeMap;

/**
 * This class is responsible for withdrawing from a bank account through ATM machine.
 */
public class WithdrawTransaction extends Transaction {
    private final int withdrawAmount;
    private final Withdrawable targetAccount;
    private final AtmMachine machine;
    private TreeMap<Integer, Integer> withdrawStock;

    /**
     * Initialize a new withdraw transaction.
     * @param user      The user account.
     * @param machine   The machine that the user is operate on.
     * @param account   The account to withdraw from.
     * @param amount    The amount that will be withdraw.
     */
    public WithdrawTransaction(User user, AtmMachine machine, Withdrawable account, int amount) {
        super(user);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to withdraw negative amount: " + amount);

        withdrawAmount = amount;
        targetAccount = account;
        this.machine = machine;
        withdrawStock = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return super.toString() + String.format("%s-%s WITHDRAW $%d", getFromUser(), ((Account) targetAccount).getId(),
                withdrawAmount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doPerform() {
        try {
            withdrawStock = machine.reduceStock(withdrawAmount);
        } catch (EmptyStockException | CashShortageException | IllegalWithdrawAmount e) {
            MainFrame.showErrorMessage(e.getMessage());
            return false;
        }

        try {
            targetAccount.withdraw(withdrawAmount, this);
        } catch (WithdrawException e) {
            MainFrame.showErrorMessage(e.getMessage());
            try {
                machine.increaseStock(withdrawStock);
            } catch (InvalidCashTypeException ex) {
                ex.printStackTrace();
            }
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doCancel() {
        targetAccount.cancelWithdraw(withdrawAmount);
        try {
            machine.increaseStock(withdrawStock);
        } catch (InvalidCashTypeException e) {
            MainFrame.showErrorMessage(e.getMessage());
            return false;
        }
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
