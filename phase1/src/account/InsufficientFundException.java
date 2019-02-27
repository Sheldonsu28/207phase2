package account;

public class InsufficientFundException extends WithdrawException {
    private Withdrawable source;
    private double amountRequested;

    InsufficientFundException(Withdrawable source, double amountRequested) {
        super();
        this.source = source;
        this.amountRequested = amountRequested;
    }

    @Override
    public String getMessage() {
        return String.format("There is insufficient fund left in your account to complete your withdraw " +
                "request of $%.2f(current balance: $%.2f).", amountRequested, ((Account) source).getBalance());
    }
}
