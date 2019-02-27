package account;

import transaction.Transaction;

public class BillingAccount extends Account {
    private final String payeeName;

    public BillingAccount(String payeeName) {
        super();
        this.payeeName = payeeName;
    }

    public BillingAccount(String payeeName, double initialBalance) {
        super(initialBalance);
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
