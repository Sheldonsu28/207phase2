package transaction;

public class IllegalDepositInfoException extends Exception {
    IllegalDepositInfoException() {
        super("File deposit.txt is in wrong format or does not exist! Check README.txt for more information!");
    }
}
