package account;

public class IncorrectTimeException extends WithdrawException{
    IncorrectTimeException() {
        super();
    }

    /**
     * @return exception message
     */
    @Override
    public String getMessage() {
        return String.format("This is incorrect time for stock market to complete your withdraw ");
    }
}
