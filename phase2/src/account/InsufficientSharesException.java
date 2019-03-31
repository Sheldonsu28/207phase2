package account;

public class InsufficientSharesException extends WithdrawException {
    InsufficientSharesException() {
        super();
    }

    /**
     * @return exception message
     */
    @Override
    public String getMessage() {
        return "There is insufficient shares in your account to sell";
    }
}
