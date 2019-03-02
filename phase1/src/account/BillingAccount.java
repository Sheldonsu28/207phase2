package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;

public class BillingAccount extends Account {
    private final String payeeName;

    public BillingAccount(Date time, User owner, String payeeName) {
        super(time, owner);
        this.payeeName = payeeName;
    }

    public BillingAccount(Date time, User owner, String payeeName, double initialBalance) {
        super(time, owner, initialBalance);
        this.payeeName = payeeName;
    }

    @Override
    public double getNetBalance() {
        return balance;
    }

    public String getPayeeName() {
        return payeeName;
    }

    // TODO test
    public void receivePay(int amount, Transaction register) {
        balance += amount;

        registerTransaction(register);
    }
}
