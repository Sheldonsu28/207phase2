package atm;

public interface Cancellable extends AccountAction {

    default boolean isCancellable() {
        return !(this instanceof Account);
    }
}
