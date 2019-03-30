package atm;

/**
 * Defines the exception happen during login when the username is not found.
 */
public class UserNotExistException extends Exception {
    /**
     * @param username The user name that is does not exist in the system.
     */
    public UserNotExistException(String username) {
        super(String.format("User \"%s\" does not exist!", username));
    }
}
