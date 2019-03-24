package account;

import atm.AtmTime;
import atm.User;
import transaction.Transaction;

import java.util.Date;

public class FinancialAccount extends AssetAccount implements Withdrawable{

    private String currTime;
    private String depositTime;
    private double depositDuration;

    private double growthRate = 1.0;

    public FinancialAccount(Date time, User owner){
    super(time, owner);
    }

    public void getTime(){
        currTime = AtmTime.FORMAT_STRING;
    }

    public void deposit(double amount, Transaction register, double duration) {
        balance += amount;
        depositTime = AtmTime.FORMAT_STRING;
        depositDuration = duration;
//        format: DD
        registerTransaction(register);
    }

    public void getGrowthRate(){
        growthRate += (depositDuration/30)/10;
    }

    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException{
        if(currTime - depositTime < depositDuration){
            throw new InsufficientTimeException();
        }

        balance -= amount;

        registerTransaction(register);
    }
    @Override
    public void cancelWithdraw(double amount) {
        balance += amount;
    }
}
