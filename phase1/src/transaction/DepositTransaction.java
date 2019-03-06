package transaction;

import account.Depositable;
import atm.AtmMachine;
import atm.User;

import java.util.Map.Entry;
import java.util.TreeMap;

public class DepositTransaction extends IntraUserTransaction {
    private final int depositAmount;
    private final TreeMap<Integer, Integer> depositStock;
    private final Depositable targetAccount;
    private final AtmMachine machine;

    public DepositTransaction(User user, AtmMachine machine, Depositable account,
                              TreeMap<Integer, Integer> depositStock) {
        super(user);

        // TODO maybe this can be simplified by creating a TreeMap class for CashStocks
        int amount = 0;
        for (Entry<Integer, Integer> entry : depositStock.entrySet())
            amount += entry.getKey() * entry.getValue();

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to deposit negative amount: " + amount);

        this.depositStock = depositStock;
        depositAmount = amount;
        targetAccount = account;
        this.machine = machine;
    }

    @Override
    protected boolean doPerform() {
        machine.increaseStock(depositStock);
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
