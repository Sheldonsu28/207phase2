package transaction;

//  TODO implement toString and comparison
public abstract class Transaction {

    private boolean performed, cancelled;
    private final String id;
    private static int prev_id = 0;

    Transaction() {
        performed = false;
        cancelled = false;
        id = String.format("TRA%04d", prev_id);
        prev_id++;
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

    public void perform() {
        if (performed || cancelled)
            throw new IllegalStateException("You can not perform a transaction that has already been performed!");

        if (doPerform())
            performed = true;
    }

    public void cancel() {
        if (!isCancellable())
            throw new IllegalStateException("Transaction not cancellable!");

        if (!performed)
            throw new IllegalStateException("You can not cancel a transaction that has not been performed yet!");

        if (cancelled)
            throw new IllegalStateException("You can not cancel a transaction that has already been cancelled!");

        if (doCancel())
            cancelled = true;
    }

    protected abstract boolean doPerform();

    protected abstract boolean doCancel();

    public abstract boolean isCancellable();
}
