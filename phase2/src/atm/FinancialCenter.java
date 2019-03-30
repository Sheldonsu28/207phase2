package atm;

import account.WithdrawException;
import transaction.Transaction;
import account.ChequingAccount;

import java.util.Observable;
import java.util.Observer;
import java.util.Date;
import java.util.Calendar;

public class FinancialCenter implements Observer {

    private ChequingAccount withdrawAccount;
    private double amount;
    private Transaction register;
    private double duration;
    private double growthRate = 0.01;
    private Date depositTime;

    public FinancialCenter(ChequingAccount withdrawAccount, double amount, Transaction register, double duration){
        this.withdrawAccount = withdrawAccount;
        this.amount = amount;
        this.register = register;
        this.duration = duration;
        this.depositTime = new Date();
    }

    @Override
    public void update(Observable o, Object arg) {
        Date currTime = (Date) arg;
        if(canWithdraw(currTime)){
            deposit();
        }
    }

    public void buyProduct() throws WithdrawException {
        withdrawAccount.withdraw(amount, register);
    }

    public boolean canWithdraw(Date currTime) {
        Calendar currTimeCal = Calendar.getInstance();
        Calendar depositTimeCal = Calendar.getInstance();
        currTimeCal.setTime(depositTime);
        depositTimeCal.setTime(currTime);
        int currYear = currTimeCal.get(Calendar.YEAR);
        int currMonth = currTimeCal.get(Calendar.MONTH);
        int currDay = currTimeCal.get(Calendar.DAY_OF_MONTH);
        int depositYear = depositTimeCal.get(Calendar.YEAR);
        int depositMonth = depositTimeCal.get(Calendar.MONTH);
        int depositDay = depositTimeCal.get(Calendar.DAY_OF_MONTH);
        double timePass = 365*(currYear - depositYear) +30*(currMonth - depositMonth)+
                (currDay - depositDay);
        return timePass >= duration;
    }

    public void deposit(){
        amount += amount*(growthRate + duration * 0.001);
        withdrawAccount.deposit(amount, register);
    }
}
