package atm;

import java.util.ArrayList;

class CreditCardAccount extends DebtAccount {

    @Override
    int getBalance() {
        return 0;
    }

    @Override
    ArrayList<Transaction> getTransactions() {
        return null;
    }

    @Override
    public void deposit(int amount) {

    }
}
