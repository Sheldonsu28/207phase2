package transaction;

import atm.User;

public abstract class InterUserTransaction extends Transaction {
    private final User fromUser, toUser;

    InterUserTransaction(User from, User to) {
        super();
        fromUser = from;
        toUser = to;
    }

    User getFromUser() {
        return fromUser;
    }

    User getToUser() {
        return toUser;
    }
}
