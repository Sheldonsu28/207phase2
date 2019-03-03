package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;
import java.util.Observable;


//  TODO implement saving growth feature
public class SavingsAccount extends AssetAccount implements Growable {

    private double growthRate;
    private String growthDay;

    SavingsAccount(Date time, User owner) {
        super(time, owner);
        growthRate = 0.001;
        growthDay = "01";
    }

    SavingsAccount(Date time, User owner, double growthRate, String growthDay) {
        super(time, owner);
        if (growthRate < 0)
            throw new IllegalArgumentException("Monthly rate can not be negative!");

        this.growthRate = growthRate;
        this.growthDay = growthDay;
    }

    SavingsAccount(Date time, User owner, double growthRate, String grwothDay, double initialBalance) {
        super(time, owner, initialBalance);
        if (growthRate < 0)
            throw new IllegalArgumentException("Monthly rate can not be negative!");

        this.growthRate = growthRate;
        this.growthDay = grwothDay;
    }

    public void changeGrowthRate(double newGrwothRate) {
        if (newGrwothRate < 0)
            throw new IllegalArgumentException("Monthly rate can not be negative!");

        growthRate = newGrwothRate;
    }

    //  TODO test growth day validity, maybe separated method needed
    public void changeGrowthDay(String newGrowthDay) {
        if (!newGrowthDay.matches("\\d\\d") || Integer.parseInt(newGrowthDay) <= 0 &&
                Integer.parseInt(newGrowthDay) > 28)
            throw new IllegalArgumentException("Illegal format of day. Proper format is \"1\" - \"28\"");

        growthDay = newGrowthDay;
    }

    @Override
    public void grow() {
        balance += balance * growthRate;
    }

    @Override
    public double getGrowthRate() {
        return growthRate;
    }

    @Override
    public String getGrowthDay() {
        return growthDay;
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
    public void update(Observable o, Object arg) {
        String currDay = (String) arg;

        if (currDay.equals(growthDay))
            grow();
    }
}
