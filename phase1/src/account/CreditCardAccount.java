package account;


import atm.AtmTime;

public class CreditCardAccount extends DebtAccount {

    public CreditCardAccount(AtmTime time) {
        super(time);
    }

    public CreditCardAccount(AtmTime time, double debtLimit) {
        super(time, debtLimit);
    }

}
