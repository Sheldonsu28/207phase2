package atm;

class DepositTransaction extends IntraUserTransaction {
    DepositTransaction(User user, DepositableAccount account) {
        super(user);
    }

    @Override
    void perform() {

    }

    @Override
    void cancel() {

    }
}
