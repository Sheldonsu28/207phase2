package transaction;

import account.Account;
import account.BillingAccount;
import account.WithdrawException;
import account.Withdrawable;
import atm.ExternalFiles;
import atm.FileHandler;
import atm.User;
import ui.MainFrame;

/**
 * This class is responsible for transaction that is paying bills.
 */
public class PayBillTransaction extends Transaction {
    private final double payAmount;
    private final BillingAccount payee;
    private final Withdrawable payer;
    private final ExternalFiles file;

    /**
     * Initialize a new transaction object with user, payer, payee and amount.
     *
     * @param from      The user that is paying the bill.
     * @param payer     The bank account to withdraw from.
     * @param payee     The bank account that is receiving the bill.
     * @param amount    Amount of money transferred.
     */
    public PayBillTransaction(User from, Withdrawable payer, BillingAccount payee, double amount) {
        super(from);
        file = ExternalFiles.BILLING_FILE;

        if (amount < 0)
            throw new IllegalArgumentException("Not allowed to pay negative amount: " + amount);

        payAmount = amount;
        this.payee = payee;
        this.payer = payer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return super.toString() + String.format("%s-%s PAYED $%.2f BILL TO %s",
                getFromUser(), ((Account) payer).getId(), payAmount, payee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doPerform() {
        try {
            payer.withdraw(payAmount, this);
        } catch (WithdrawException e) {
            MainFrame.showErrorMessage(e.getMessage());
            return false;
        }

        payee.receivePay(payAmount, this);

        String msg = String.format("%s received $%.2f payment from %s",
                payee.getId(), payAmount, ((Account) payer).getId());

        (new FileHandler()).saveTo(file, msg);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doCancel() {
        throw new IllegalStateException("Billing action is not cancellable!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancellable() {
        return false;
    }
}
