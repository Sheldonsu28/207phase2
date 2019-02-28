package atm;

public class WrongPasswordException extends Exception {
    WrongPasswordException(String username) {
        super(String.format("Password you entered does not match user \"%s\".", username));
    }
}
