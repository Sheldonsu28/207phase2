package account;


import atm.User;

import java.util.Date;

public class CreditCardAccount extends DebtAccount {

    public CreditCardAccount(Date time, User owner) {
        super(time, owner);
    }

    public CreditCardAccount(Date time, User owner, double debtLimit) {
        super(time, owner, debtLimit);
    }

}
