package atm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

final class UserDatabase implements Serializable {
    private TreeMap<String, User> users;

    UserDatabase() {
        users = new TreeMap<>();
    }

    User registerNewUser(String username, String password)
            throws UsernameAlreadyExistException {
        if (users.containsKey(username))
            throw new UsernameAlreadyExistException(username);

        User user = new User(username, password);
        users.put(username, user);

        return user;
    }

    Employee registerNewEmployee(String username, String password, AtmTime time, BankManager manager)
            throws UsernameAlreadyExistException{
        if (users.containsKey(username)) {
            throw new UsernameAlreadyExistException(username);
        }
        Employee employee = new Employee(username, password, this, time,manager );
        users.put(username,employee);

        return employee;
    }

    void removeUser(String username) {
        users.remove(username);
    }

    User getUser(String username) {
        return users.get(username);
    }

    /**
     *
     * @param username
     * @param password
     * @param Parent
     * @return
     * @throws UsernameAlreadyExistException
     */
    ChildUser registerNewChildUser(String username, String password, User Parent) throws UsernameAlreadyExistException{
        if (users.containsKey(username))
            throw new UsernameAlreadyExistException(username);

        ChildUser user = new ChildUser(username, password,Parent);
        users.put(username, user);

        return user;
    }

    /**
     * Return a list containing all user instances saved in this class
     *
     * @return
     */
    List<User> getUserList() {
        return new ArrayList<>(users.values());
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
