package atm;

import java.text.DecimalFormat;

/**
 * This class defines the type of currency.
 * The currency have two types, USD or CAD.
 * The rate for CAD to USD is 0.76 and USD to USD is 1.0.
 */
public enum Currency {
    /**
     * Initializer if the currency is USD.
     */
    USD(1.0),
    /**
     * Initializer if the currency is CAD.
     */
    CAD(0.76);
    private final double rateToUsd;
    private final DecimalFormat decimalFormat;

    /**
     * Initialize the Currency object with conversation to USD rate.
     * @param rate The conversion rate to USD.
     */
    Currency(double rate) {
        rateToUsd = rate;
        decimalFormat = new DecimalFormat(".##");
    }

    /**
     * Convert the amount to USD using its conversation rate.
     * @param amount The amount of currency that need to be converted.
     * @return The amount after converted to USD.
     */
    double convertToUSD(double amount) {
        return Double.parseDouble(decimalFormat.format(amount * rateToUsd));
    }

    /**
     * Covert from USD to the Currency of the current class.
     * @param amount Amount of USD.
     * @return Amount of currency after conversion.
     */
    double convertFromUSD(double amount) {
        return Double.parseDouble(decimalFormat.format(amount / rateToUsd));
    }

}
