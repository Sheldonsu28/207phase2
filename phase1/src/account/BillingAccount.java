package account;

import transaction.Transaction;

import java.util.ArrayList;

//  TODO everything to be implemented
public class BillingAccount extends Account {
    private final String payeeName;

    BillingAccount(String payeeName) {
        super();
        this.payeeName = payeeName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    @Override
    public double getBalance() {
        return 0;
    }

    @Override
    ArrayList<Transaction> getTransactions() {
        return null;
    }

    @Override
    public double getNetBalance() {
        return 0;
    }


    public void receivePay(int amount) {

    }
}
