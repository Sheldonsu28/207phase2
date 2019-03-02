package atm;

import java.util.TreeMap;

public class UserDatabase {
    private TreeMap<String, User> users;

    UserDatabase() {
        users = new TreeMap<>();
    }

    boolean registerNewUser(String username, String password) {
        if (users.containsKey(username))
            return false;

        User user = new User(username, password);
        users.put(username, user);
        return true;
    }

    void removeUser(String username) {
        users.remove(username);
    }

    User getUser(String username) {
        return users.get(username);
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
