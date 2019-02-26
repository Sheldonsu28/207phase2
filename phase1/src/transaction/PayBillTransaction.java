package transaction;

import account.BillingAccount;
import account.WithdrawException;
import account.Withdrawable;
import atm.User;

public class PayBillTransaction extends IntraUserTransaction {
    private final int payAmount;
    private final BillingAccount payee;
    private final Withdrawable payer;

    PayBillTransaction(User from, Withdrawable payer, BillingAccount payee, int amount) {
        super(from);

        payAmount = amount;
        this.payee = payee;
        this.payer = payer;
    }

    @Override
    protected boolean doPerform() {
        try {
            payer.withdraw(payAmount);
        } catch (WithdrawException exception) {

            // TODO exception handling & passing message

            return false;
        }

        payee.receivePay(payAmount);
        return true;
    }

    @Override
    protected boolean doCancel() {
        throw new IllegalStateException("Billing action is not cancellable!");
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
