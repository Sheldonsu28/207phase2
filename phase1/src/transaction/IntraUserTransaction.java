package transaction;

import atm.User;

/**
 * This class defines the behavior of transaction between user inside the system and outside of the system.
 */
public abstract class IntraUserTransaction extends Transaction {
    private final User user;

    /**
     * @param user The user that is involved in the transaction.
     */
    IntraUserTransaction(User user) {
        super();
        this.user = user;
    }

    /**
     * Get the user that is involved in the transaction.
     * @return The user that is involved in the transaction.
     */
    User getUser() {
        return user;
    }
}
