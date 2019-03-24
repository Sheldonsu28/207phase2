package atm;

public class UsernameOutOfRangeException extends Exception {

    UsernameOutOfRangeException() {
        super(String.format("Username length is out of range %d - %d", User.MIN_NAME_LENGTH, User.MAX_NAME_LENGTH));
    }

}
