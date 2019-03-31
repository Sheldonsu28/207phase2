package account;

public class IncorrectTimeException extends Exception{
    IncorrectTimeException() {
        super();
    }

    /**
     * @return exception message
     */
    @Override
    public String getMessage() {
        return String.format("The stock market is not open for weekend");
    }
}
