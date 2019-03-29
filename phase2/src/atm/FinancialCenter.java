package atm;

import transaction.Transaction;
import account.ChequingAccount;

import java.util.Observable;
import java.util.Observer;
import java.util.Date;

public class FinancialCenter implements Observer {

    private ChequingAccount withdrawAccount;
    private double amount;
    private Transaction register;
    private double duration;

    public FinancialCenter(ChequingAccount withdrawAccount, double amount, Transaction register, double duration){
        this.withdrawAccount = withdrawAccount;
        this.amount = amount;
        this.register = register;
        this.duration = duration;
    }

    @Override
    public void update(Observable o, Object arg) {
        Date currTime = (Date) arg;
        if(canWithdraw(currTime)){
            deposit();
        }
    }

    public boolean canWithdraw(Date currTime) {
        double timePass = 365*(Integer.parseInt(date.substring(0,3))-Integer.parseInt(depositTime.substring(0,3)))
                +30*(Integer.parseInt(date.substring(4,5))-Integer.parseInt(depositTime.substring(4,5)))+
                (Integer.parseInt(date.substring(6,7))-Integer.parseInt(depositTime.substring(6,7)));
        return timePass <= duration;
    }

    public void deposit(){
        withdrawAccount.deposit(amount, register);
    }
}
