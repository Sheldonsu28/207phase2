package account;

public class InsufficientSharesException extends Throwable {
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
