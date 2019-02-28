package account;

import transaction.Transaction;

import java.util.Observable;
import java.util.Observer;


//  TODO implement saving growth feature
public class SavingsAccount extends AssetAccount implements Observer {

    private double monthlyRate;
    private String monthlyGrowthDay;

    SavingsAccount() {
        super();
        monthlyRate = 0.001;
        monthlyGrowthDay = "01";
    }

    SavingsAccount(double monthlyRate, String monthlyGrowthDay) {
        super();
        if (monthlyRate < 0)
            throw new IllegalArgumentException("Monthly rate can not be negative!");

        this.monthlyRate = monthlyRate;
        this.monthlyGrowthDay = monthlyGrowthDay;
    }

    SavingsAccount(double monthlyRate, String monthlyGrowthDay, double initialBalance) {
        super(initialBalance);
        if (monthlyRate < 0)
            throw new IllegalArgumentException("Monthly rate can not be negative!");

        this.monthlyRate = monthlyRate;
        this.monthlyGrowthDay = monthlyGrowthDay;
    }

    public void changeMonthlyRate(double newMonthlyRate) {
        if (newMonthlyRate < 0)
            throw new IllegalArgumentException("Monthly rate can not be negative!");

        monthlyRate = newMonthlyRate;
    }

    public double getMonthlyRate() {
        return monthlyRate;
    }

    //  TODO test growth day validity, maybe separated method needed
    public void changeMonthlyGrowthDay(String newMonthlyGrowthDay) {
        if (!newMonthlyGrowthDay.matches("\\d\\d") || Integer.parseInt(newMonthlyGrowthDay) <= 0 &&
                Integer.parseInt(newMonthlyGrowthDay) > 28)
            throw new IllegalArgumentException("Illegal format of day. Proper format is \"1\" - \"28\"");

        monthlyGrowthDay = newMonthlyGrowthDay;
    }

    public String getMonthlyGrowthDay() {
        return monthlyGrowthDay;
    }

    @Override
    //  TODO test
    public void withdraw(double amount, Transaction register) throws WithdrawException {
        if (balance - amount < 0)
            throw new InsufficientFundException(this, amount);

        balance -= amount;

        registerTransaction(register);
    }

    @Override
    //  TODO test
    public void cancelWithdraw(double amount) {
        balance += amount;
    }

    @Override
    //  TODO savings growth from AtmTime changes
    public void update(Observable o, Object arg) {

    }
}
