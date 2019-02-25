package atm;

import java.util.ArrayList;

class ChequingAccount extends AssetAccount {
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

    @Override
    public void withdraw(int amount) {

    }
}
