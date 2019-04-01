package transaction;

import atm.User;

import java.io.Serializable;

/**
 * This class defines the behavior of a transaction class.
 */
public abstract class Transaction implements Serializable {

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

    /**
     * Initialize a transaction with a user that will be withdraw from.
     *
     * @param fromUser The user that will be withdraw from.
     */
    Transaction(User fromUser) {
        this();

        this.fromUser = fromUser;
        this.toUser = fromUser;
    }

    /**
     * Initialize a transaction with a user that will be withdraw from and a user that will be the receiver.
     *
     * @param fromUser The user that will be with draw from.
     * @param toUser The user that will be the receiver.
     */
    Transaction(User fromUser, User toUser) {
        this();

        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    /**
     * Get the User that will be withdraw from in this transaction.
     *
     * @returnThe user that will be withdraw from.
     */
    User getFromUser() {
        return fromUser;
    }
    /**
     * Get the User that receive the transaction.
     *
     * @returnThe user that will receive the transaction.
     */
    User getToUser() {
        return toUser;
    }

    /**
     * Return the ID of the transaction.
     * @return ID of the transaction.
     */
    private String getId() {
        return id;
    }

    /**
     * Return the state of the transaction, return true if the action is performed, else false.
     *
     * @return State of transaction.
     */
    public boolean isPerformed() {
        return performed;
    }

    /**
     * Return whether or not the transaction is cancelled or not.
     *
     * @return State of cancellation of the transaction.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Update the state of the transaction.
     *
     * @return The state of the transaction.
     */
    public boolean perform() {
        if (performed || cancelled)
            throw new IllegalStateException("You can not perform a transaction that has already been performed!");

        performed = doPerform();

        return performed;
    }

    /**
     * This method is responsible for cancelling a transaction.
     *
     * @return The update to whether the transaction is cancelled.
     */
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

    /**
     * The String representation of this transaction in the following format:
     *
     * @return The String representation of the transaction.
     */
    public String toString() {
        String cancelText;

        if (isCancellable()) {
            if (isCancelled()) {
                cancelText = "IS-CANCELLED";
            } else {
                cancelText = "IS-CANCLABLE";
            }
        } else {
            cancelText = "NT-CANCLABLE";
        }

        return String.format("%s %s ", getId(), cancelText);
    }

    /**
     * Verify that the transaction is performed.
     *
     * @return Whether the transaction is performed or not.
     */
    protected abstract boolean doPerform();

    /**
     * Verify that the transaction is cancel.
     *
     * @return The state whether the transaction is cancelled.
     */
    protected abstract boolean doCancel();

    /**
     * Return whether or not the transaction is cancellable.
     *
     * @return Return whether or not the transaction is cancellable.
     */
    public abstract boolean isCancellable();
}
