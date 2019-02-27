package account;

abstract class WithdrawException extends Exception {

    WithdrawException() {
        super();
    }

    public abstract String getMessage();

}
