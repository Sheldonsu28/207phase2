package account;

import atm.User;

import java.util.Date;
import java.util.List;

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
     */
    public CreditCardAccount(Date time, List<User> owner) {
        super(time, owner);
    }

    /**
     * @param time      time of creation
     * @param owner     owner user
     * @param debtLimit the debt limit of this account
     * @see DebtAccount#DebtAccount(Date, List, int)
     */
    public CreditCardAccount(Date time, List<User> owner, int debtLimit) {
        super(time, owner, debtLimit);
    }

}
