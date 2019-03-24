package account;

import java.util.Observer;

/**
 * Characterizes any account which its balance grows monthly at at a specific date with a specific rate.
 * Uses observer design pattern to collaborate with {@link atm.AtmTime} to track growth period/time.
 *
 * @author zhaojuna
 * @version 1.0
 * @see atm.AtmTime
 */
public interface Growable extends Observer {

    /**
     * Performs the growth action. Should be invoked when the date trigger of growth is met.
     */
    void grow();

    /**
     * @return the monthly rate of this account
     */
    double getGrowthRate();

    /**
     * @return the monthly date when the growth happens
     */
    String getGrowthDay();

    /**
     * Update the monthly date of growth.
     *
     * @param newMonthlyGrowthDay the new growth date
     */
    void changeGrowthDay(String newMonthlyGrowthDay);

    /**
     * Update the monthly rate of growth
     *
     * @param newMonthlyRate the new growth rate
     */
    void changeGrowthRate(double newMonthlyRate);
}
