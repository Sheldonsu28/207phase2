package account;

/**
 * Formulates error message for the exception caused by withdrawing without sufficient balance left
 * in the {@linkplain Withdrawable} accounts.
 *
 * @author zhaojuna
 * @version 1.0
 */
public class InsufficientTimeException extends WithdrawException {

    InsufficientTimeException() {
        super();
    }

    /**
     * @return exception message
     */
    @Override
    public String getMessage() {
        return String.format("There is insufficient time for deposit in your account to complete your withdraw ");
    }
}