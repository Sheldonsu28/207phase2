package account;

import atm.User;
import transaction.Transaction;

import java.util.Date;
import java.util.Observable;

/**
 * Defines behaviours of savings account.
 *
 * @author zhaojuna
 * @version 1.0
 */
public class SavingsAccount extends AssetAccount implements Growable {
    /**
     * Constant represents default growth rate
     */
    public static final double DEFAULT_GROWTH_RATE = 0.001;

    /**
     * Constant represents the default growth day
     */
    public static final String DEFAULT_GROWTH_DAY = "01";

    private double growthRate;
    private String growthDay;

    /**
     * Construct a default savings account with {@link #DEFAULT_GROWTH_DAY} and {@link #DEFAULT_GROWTH_RATE}.
     *
     * @param time  time of creation
     * @param owner owner user
     * @see Account#Account(Date, User)
     */
    public SavingsAccount(Date time, User owner) {
        super(time, owner);
        growthRate = DEFAULT_GROWTH_RATE;
        growthDay = DEFAULT_GROWTH_DAY;
    }

    /**
     * Constructs a savings account with given growth rate and date and initial balance
     *
     * @param time           time of creation
     * @param owner          owner user
     * @param growthRate     growth rate (must be positive)
     * @param growthDay      growth day (must be 2-digit number in range 01 - 28)
     * @param initialBalance the initial balance of the account
     * @see Account#Account(Date, User, double)
     */
    public SavingsAccount(Date time, User owner, double growthRate, String growthDay, double initialBalance) {
        super(time, owner, initialBalance);
        if (growthRate < 0)
            throw new IllegalArgumentException("Monthly rate can not be negative!");

        if (!isInDayFormat(growthDay))
            throw new IllegalArgumentException("Growth day must be 2-digit number in range 01-28");

        this.growthRate = growthRate;
        this.growthDay = growthDay;
    }

    private boolean isInDayFormat(String day) {
        return day.matches("\\d\\d") && Integer.parseInt(day) >= 1 && Integer.parseInt(day) <= 28;
    }

    /**
     * {@inheritDoc}
     */
    public void changeGrowthRate(double newGrowthRate) {
        if (newGrowthRate < 0)
            throw new IllegalArgumentException("Monthly rate can not be negative!");

        growthRate = newGrowthRate;
    }

    /**
     * {@inheritDoc}
     */
    public void changeGrowthDay(String newGrowthDay) {
        if (!isInDayFormat(newGrowthDay))
            throw new IllegalArgumentException("Illegal format of day. Proper format is \"1\" - \"28\"");

        growthDay = newGrowthDay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void grow() {
        balance += balance * growthRate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGrowthRate() {
        return growthRate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGrowthDay() {
        return growthDay;
    }

    /**
     * {@inheritDoc}
     * The transaction will be registered into account's transaction list.
     *
     * @throws WithdrawException if the requested amount is greater than the balance left in the account
     */
    @Override
    public void withdraw(double amount, Transaction register) throws WithdrawException {
        if (balance - amount < 0)
            throw new InsufficientFundException(this, amount);

        balance -= amount;

        registerTransaction(register);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelWithdraw(double amount) {
        balance += amount;
    }

    /**
     * The implementation of the observer design pattern for timed growth.
     * This is invoked everyday at about 00:00 when the time detected a day-change.
     * The day is then compared to the growth day of this account to decide whether the balance should grow or not.
     *
     * @param o   the Observable {@link atm.AtmTime} instance
     * @param arg guaranteed to be a String representing the current day
     */
    @Override
    public void update(Observable o, Object arg) {
        String currDay = (String) arg;

        if (currDay.equals(growthDay))
            grow();
    }
}
