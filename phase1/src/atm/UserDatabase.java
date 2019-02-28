package atm;

import java.util.HashMap;

public class UserDatabase {
    private HashMap<String, User> users;

    UserDatabase() {
        users = new HashMap<>();
    }

    boolean registerNewUser(User user) {
        if (users.containsValue(user) || users.containsKey(user.getUserName()))
            return false;

        users.put(user.getUserName(), user);
        return true;
    }

    void removeUser(String username) {
        users.remove(username);
    }

    public User loginUser(String username, String password) throws WrongPasswordException, UserNotExistException {
        if (!users.containsKey(username))
            throw new UserNotExistException(username);

        User targetUser = users.get(username);

        if (!targetUser.verifyPassword(password))
            throw new WrongPasswordException(username);

        return targetUser;
    }

}
