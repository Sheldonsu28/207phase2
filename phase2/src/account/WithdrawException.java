package account;

/**
 * Defines the exception that happens during withdraw action.
 * It forces it child classes to implement {@link #getMessage()} to formulate their own error message.
 *
 * @author zhaojuna
 * @version 1.0
 */
public abstract class WithdrawException extends Exception {

    WithdrawException() {
        super();
    }

    public abstract String getMessage();

}
