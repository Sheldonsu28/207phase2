package account;

import transaction.Transaction;

public class BillingAccount extends Account {
    private final String payeeName;

    BillingAccount(String payeeName) {
        super();
        this.payeeName = payeeName;
    }

    BillingAccount(String payeeName, double initialBalance) {
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

    public void receivePay(int amount, Transaction register) {
        balance += amount;

        registerTransaction(register);
    }
}
