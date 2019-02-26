package atm;

public class TransferTransaction extends IntraUserTransaction {
    TransferTransaction(User user) {
        super(user);
    }

    @Override
    void perform() {

    }

    @Override
    void cancel() {

    }
}
