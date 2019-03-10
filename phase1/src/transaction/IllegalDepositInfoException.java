package transaction;

/**
 * This class defines the exception that happens during interpreting the deposit file.
 */
public class IllegalDepositInfoException extends Exception {
    /**
     * The String message of the exception.
     */
    IllegalDepositInfoException() {
        super("File deposit.txt is in wrong format or does not exist! Check README.txt for more information!");
    }
}
