package account;

public abstract class DebtAccount extends Account implements Depositable {

    DebtAccount() {
        super();
    }

    @Override
    // TODO confirm what happens when debt is overpayed
    public double getNetBalance() {
        return -getBalance();
    }
}
