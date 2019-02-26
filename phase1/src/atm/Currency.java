package atm;

import java.text.DecimalFormat;

public enum Currency {
    USD(1.0),
    CAD(0.76);

    public double rateToUsd;
    public DecimalFormat decimalFormat;

    Currency(double rate) {
        rateToUsd = rate;
        decimalFormat = new DecimalFormat(".##");
    }

    //  TODO test required
    double convertToUSD(double amount) {
        return Double.parseDouble(decimalFormat.format(amount * rateToUsd));
    }

    //  TODO test required
    double convertFromUSD(double amount) {
        return Double.parseDouble(decimalFormat.format(amount / rateToUsd));
    }
}
