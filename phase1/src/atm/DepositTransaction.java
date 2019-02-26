package atm;

class DepositTransaction extends IntraUserTransaction {
    private final int depositAmount;
    private final Depositable targetAccount;

    DepositTransaction(User user, Depositable account, int amount) {
        super(user);

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to deposit negative amount: " + amount);

        depositAmount = amount;
        targetAccount = account;
    }

    @Override
    void perform() {
        targetAccount.deposit(depositAmount);
    }

    @Override
    void cancel() {

    }
}
