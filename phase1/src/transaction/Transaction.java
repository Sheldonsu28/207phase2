package transaction;

public abstract class Transaction {

    private boolean performed, cancelled;
    private final String id;
    private static int prev_id = 0;

    Transaction() {
        performed = false;
        cancelled = false;
        id = String.format("T%04d", prev_id);
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
        performed = true;
        doPerform();
    }

    public void cancel() {
        if (!isCancellable())
            throw new IllegalStateException("Transaction not cancellable");

        cancelled = true;
        doCancel();
    }

    protected abstract void doPerform();

    protected abstract void doCancel();

    public abstract boolean isCancellable();
}
