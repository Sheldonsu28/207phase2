package atm;

public class InvalidCashTypeException extends Exception {
    public InvalidCashTypeException(int type) {
        super(String.format("The type of cash $%d is not allowed in the current machine/stock!", type));
    }
}
