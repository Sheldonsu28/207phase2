package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;

public class FinancialAccount extends AssetAccount implements Growable{

    public FinancialAccount(Date time, User owner){


    }


    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException{

    }
}
