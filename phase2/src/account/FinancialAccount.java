package account;
package atm;

import atm.AtmTime;
import atm.User;
import transaction.Transaction;

import java.util.Date;

public class FinancialAccount extends AssetAccount implements Withdrawable{

    private String time;
    private String depositTime;

    public FinancialAccount(Date time, User owner){
    super(time, owner);


    }
    public void getTime(){
        time = AtmTime.FORMAT_STRING;
    }

    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException{
    }
    @Override
    public void cancelWithdraw(double amount) {
        balance += amount;
    }

    @Override
    public void deposit(double amount, Transaction register) {
        balance += amount;
        depositTime = AtmTime.FORMAT_STRING;

        registerTransaction(register);
    }
}
