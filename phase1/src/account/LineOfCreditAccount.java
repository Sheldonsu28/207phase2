package account;

import transaction.Transaction;

import java.util.ArrayList;

public class LineOfCreditAccount extends DebtAccount implements Withdrawable {

    @Override
    public
        /* TODO Implementation */
    double getBalance() {
        return 0;
    }

    @Override
        /* TODO Implementation */
    ArrayList<Transaction> getTransactions() {
        return null;
    }

    @Override
    /* TODO Implementation */
    public void deposit(int amount) {

    }

    @Override
    public void cancelDeposit(int amount) {

    }

    @Override
    /* TODO Implementation */
    public void withdraw(int amount) {

    }

    @Override
    public void cancelWithdraw(int amount) {

    }
}
