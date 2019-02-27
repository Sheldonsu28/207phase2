package account;

public class DebtLimitExceededException extends WithdrawException {
    private Indebtable source;
    private double amountRequested;

    DebtLimitExceededException(Indebtable source, double amountRequested) {
        super();
        this.source = source;
        this.amountRequested = amountRequested;
    }

    @Override
    public String getMessage() {
        return String.format("Your request to withdraw $%.2f exceeds maximum debt limit $%.2f on this account(" +
                "current balance: %s).", amountRequested, source.getDebtLimit(), ((Account) source).getBalance());
    }
}
