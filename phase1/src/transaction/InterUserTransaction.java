package transaction;

import atm.User;

/**
 * This class is responsible for transactions between users in the system.
 */
public abstract class InterUserTransaction extends Transaction {
    private final User fromUser, toUser;

    /**
     * @param from User sending the transaction.
     * @param to User that received the transaction.
     */
    InterUserTransaction(User from, User to) {
        super();
        fromUser = from;
        toUser = to;
    }

    /**
     * Get the user that sends money.
     * @return User that sends money.
     */
    User getFromUser() {
        return fromUser;
    }

    /**
     * Get the user that received money.
     * @return User that received money.
     */
    User getToUser() {
        return toUser;
    }
}
