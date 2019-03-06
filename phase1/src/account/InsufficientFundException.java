package account;

/**
 * Formulates error message for the exception caused by withdrawing without sufficient balance left
 * in the {@linkplain Withdrawable} accounts.
 *
 * @author zhaojuna
 * @version 1.0
 */
public class InsufficientFundException extends WithdrawException {

    private Withdrawable source;
    private double amountRequested;

    /**
     * @param source          the source account
     * @param amountRequested the requested amount that causes the exception
     */
    InsufficientFundException(Withdrawable source, double amountRequested) {
        super();
        this.source = source;
        this.amountRequested = amountRequested;
    }

    /**
     * @return exception message
     */
    @Override
    public String getMessage() {
        return String.format("There is insufficient fund left in your account to complete your withdraw " +
                "request of $%.2f(current balance: $%.2f).", amountRequested, ((Account) source).getBalance());
    }
}
