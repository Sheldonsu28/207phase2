package atm;

abstract class IntraUserTransaction extends Transaction {
    private final User user;

    IntraUserTransaction(User user) {
        super();
        this.user = user;
    }

    User getUser() {
        return user;
    }
}
