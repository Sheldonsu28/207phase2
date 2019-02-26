package atm;

abstract class DebtAccount extends Account implements DepositableAccount {

    DebtAccount() {
        super();
    }

    @Override
        // TODO confirm what happens when debt is overpayed
    double getNetBalance() {
        return -getBalance();
    }
}
