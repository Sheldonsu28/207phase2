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

    public String getTime(){
        return currTime;
    }


    public double getGrowthRate(){
        return growthRate;
    }

    public void deposit(double amount, Transaction register, double duration) {
        growthRate += (depositDuration/30)/10;
        balance += amount*growthRate;
        depositTime = AtmTime.FORMAT_STRING;
        depositDuration = duration;
//        format: DD
        registerTransaction(register);
    }

    private boolean isDate(String date) {
        double timePass = 365*(Integer.parseInt(date.substring(0,3))-Integer.parseInt(depositTime.substring(0,3)))
                +30*(Integer.parseInt(date.substring(4,5))-Integer.parseInt(depositTime.substring(4,5)))+
                (Integer.parseInt(date.substring(6,7))-Integer.parseInt(depositTime.substring(6,7)));
        return timePass <= 365*2;
    }

    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException{
//        if(currTime - depositTime < depositDuration){
        currTime = AtmTime.FORMAT_STRING;
        if (isDate(currTime)) {
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
