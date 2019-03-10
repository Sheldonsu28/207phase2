package transaction;

import account.WithdrawException;
import account.Withdrawable;
import atm.AtmMachine;
import atm.CashShortageException;
import atm.EmptyStockException;
import atm.User;

import java.util.TreeMap;

public class WithdrawTransaction extends Transaction {
    private final int withdrawAmount;
    private final Withdrawable targetAccount;
    private final AtmMachine machine;
    private TreeMap<Integer, Integer> stock;

    public WithdrawTransaction(User user, AtmMachine machine, Withdrawable account, int amount) {
        super(user);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to withdraw negative amount: " + amount);

        withdrawAmount = amount;
        targetAccount = account;
        this.machine = machine;
        stock = null;
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format("User %s's Account %s WITHDRAW $%d", getFromUser(), targetAccount, withdrawAmount);
    }

    @Override
    protected boolean doPerform() {
        try {
            targetAccount.withdraw(withdrawAmount, this);
        } catch (WithdrawException e) {
            System.out.println(e.getMessage());
            return false;
        }

        try {
            stock = machine.reduceStock(withdrawAmount);
        } catch (EmptyStockException | CashShortageException e) {
            System.out.println(e.getMessage());
            targetAccount.cancelWithdraw(withdrawAmount);
            return false;
        }

        return true;
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
