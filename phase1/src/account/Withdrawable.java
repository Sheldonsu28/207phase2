package account;

public interface Withdrawable extends Cancellable {

    void withdraw(int amount) throws WithdrawException;

    void cancelWithdraw(int amount);
}
