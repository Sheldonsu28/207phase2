package account;

public interface Depositable extends Cancellable {
    void deposit(int amount);

    void cancelDeposit(int amount);
}
