package atm;

/**
 * Define the exception happens during login operation where the password enter by the user does not match the username.
 */
public class WrongPasswordException extends Exception {
    /**
     * @param username Username of the account.
     */
    WrongPasswordException(String username) {
        super(String.format("Password you entered does not match user \"%s\".", username));
    }
}
