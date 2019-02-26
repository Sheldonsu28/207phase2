package atm;

class WithdrawTransaction extends IntraUserTransaction {

    WithdrawTransaction(User user) {
        super(user);
    }

    @Override
    void perform() {

    }

    @Override
    void cancel() {

    }
}
