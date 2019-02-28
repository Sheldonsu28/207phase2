package atm;

public class UserNotExistException extends Exception {
    UserNotExistException(String username) {
        super(String.format("User \"%s\" does not exist!", username));
    }
}
