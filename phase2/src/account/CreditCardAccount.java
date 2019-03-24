package account;

import atm.User;

import java.util.Date;

/**
 * Defines behaviours of credit card account.
 *
 * @author zhaojuna
 * @version 1.0
 */
public class CreditCardAccount extends DebtAccount {

    /**
     * @param time  time of creation
     * @param owner owner user
     * @see Account#Account(Date, User)
     */
    public CreditCardAccount(Date time, User owner) {
        super(time, owner);
    }

    public CreditCardAccount(Date time, User owner, User owner2) { super(time, owner, owner2); }

    /**
     * @param time      time of creation
     * @param owner     owner user
     * @param debtLimit the debt limit of this account
     * @see DebtAccount#DebtAccount(Date, User, double)
     */
    public CreditCardAccount(Date time, User owner, double debtLimit) {
        super(time, owner, debtLimit);
    }

    public CreditCardAccount(Date time, User owner, User owner2, double debtLimit) {
        super(time, owner, owner2, debtLimit);
    }

}
