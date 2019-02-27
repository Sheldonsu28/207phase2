package account;

public abstract class WithdrawException extends Exception {

    WithdrawException() {
        super();
    }

    public abstract String getMessage();

}
