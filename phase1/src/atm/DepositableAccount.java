package atm;

interface DepositableAccount extends Cancellable {
    void deposit(int amount);

    void cancelDeposit(int amount);
}
