package account;

import atm.AtmTime;
import transaction.Transaction;

public class BillingAccount extends Account {
    private final String payeeName;

    public BillingAccount(AtmTime time, String payeeName) {
        super(time);
        this.payeeName = payeeName;
    }

    public BillingAccount(AtmTime time, String payeeName, double initialBalance) {
        super(time, initialBalance);
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
