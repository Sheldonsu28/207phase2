package transaction;

import atm.User;

public abstract class Transaction {

    private boolean performed, cancelled;
    private final String id;
    private static int prev_id = 0;
    private User fromUser, toUser;

    private Transaction() {
        performed = false;
        cancelled = false;
        id = String.format("TRA%04d", prev_id);
        prev_id++;
    }

    Transaction(User fromUser) {
        this();

        this.fromUser = fromUser;
        this.toUser = fromUser;
    }

    Transaction(User fromUser, User toUser) {
        this();

        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public User getFromUser() {
        return fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public String getId() {
        return id;
    }

    public boolean isPerformed() {
        return performed;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean perform() {
        if (performed || cancelled)
            throw new IllegalStateException("You can not perform a transaction that has already been performed!");

        performed = doPerform();

        return performed;
    }

    public boolean cancel() {
        if (!isCancellable())
            throw new IllegalStateException("Transaction not cancellable!");

        if (!performed)
            throw new IllegalStateException("You can not cancel a transaction that has not been performed yet!");

        if (cancelled)
            throw new IllegalStateException("You can not cancel a transaction that has already been cancelled!");

        cancelled = doCancel();
        return cancelled;
    }

    public String toString() {
        return String.format("ID %s\tCAN_BE_CANCELLED %s\tIS_CANCELLED %s\t", getId(), isCancellable(), isCancelled());
    }

    protected abstract boolean doPerform();

    protected abstract boolean doCancel();

    public abstract boolean isCancellable();
}
