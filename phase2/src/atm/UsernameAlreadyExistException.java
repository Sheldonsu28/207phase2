package atm;

/**
 * Defines the exception happen during the creation of a Bank account.
 */
public class UsernameAlreadyExistException extends Exception {
    /**
     * @param username The username that is already exist.
     */
    UsernameAlreadyExistException(String username) {
        super(String.format("The username \"%s\" has already been registered! Please choose another one!", username));
    }
}
