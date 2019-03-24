package account;

/**
 * Formulates error message for the exception caused by exceeding debt limit on {@linkplain Indebtable} accounts.
 *
 * @author zhaojuna
 * @version 1.0
 */
public class DebtLimitExceededException extends WithdrawException {

    private Indebtable source;
    private double amountRequested;

    /**
     * @param source          the source account
     * @param amountRequested the requested amount that causes the exception
     */
    DebtLimitExceededException(Indebtable source, double amountRequested) {
        super();
        this.source = source;
        this.amountRequested = amountRequested;
    }

    /**
     * @return exception message
     */
    @Override
    public String getMessage() {
        return String.format("Your request to withdraw $%.2f exceeds maximum debt limit $%.2f on this account(" +
                "current balance: %s).", amountRequested, source.getDebtLimit(), ((Account) source).getBalance());
    }
}
