package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;
import java.util.List;

/**
 * Defines behaviours of line of credit account.
 *
 * @author zhaojuna
 * @version 1.0
 */
public class LineOfCreditAccount extends DebtAccount implements Withdrawable {

    /**
     * @param time  time of creation
     * @param owner owner user
     * @see Account#Account(Date, List)
     */
    public LineOfCreditAccount(Date time, List<User> owner) {
        super(time, owner);
    }

    /**
     * @param time      time of creation
     * @param owner     owner user
     * @param debtLimit the debt limit of this account
     * @see DebtAccount#DebtAccount(Date, List, double)
     */
    public LineOfCreditAccount(Date time, List<User> owner, double debtLimit) {
        super(time, owner, debtLimit);
    }

    /**
     * {@inheritDoc}
     * The transaction will be registered into account's transaction list.
     *
     * @throws WithdrawException if requested amount exceeds debt limit
     */
    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException {
        if (balance + amount > debtLimit)
            throw new DebtLimitExceededException(this, amount);

        balance += amount;

        registerTransaction(register);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelWithdraw(double amount) {
        balance -= amount;
    }
}
