package atm;

public class UsernameAlreadyExistException extends Exception {
    UsernameAlreadyExistException(String username) {
        super(String.format("The username \"%s\" has already been registered! Please choose another one!", username));
    }
}
