package account;

import java.util.Observer;

public interface Growable extends Observer {
    void grow();

    double getGrowthRate();

    String getGrowthDay();

    void changeGrowthDay(String newMonthlyGrowthDay);

    void changeGrowthRate(double newMonthlyRate);
}
