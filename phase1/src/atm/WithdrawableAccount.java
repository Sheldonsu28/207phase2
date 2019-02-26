package atm;

interface WithdrawableAccount {
    void withdraw(int amount);

    void cancelWithdraw(int amount);
}
