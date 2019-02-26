package account;

public interface Withdrawable extends Cancellable {
    void withdraw(int amount);

    void cancelWithdraw(int amount);
}
