package atm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

public final class AtmTime extends Observable {
    private SimpleDateFormat dateFormat;
    private static Date initialTime, currentTime;
    private static long prevMills = -1;

    AtmTime() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    AtmTime(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    void setInitialTime(Date start) {
        initialTime = start;
        currentTime = start;
        prevMills = System.currentTimeMillis();
    }

    void changeDateFormat(SimpleDateFormat newFormat) {
        dateFormat = newFormat;
    }

    //  FIXME how to best synchronize with all savings observers? Is real-time possible?
    public static Date getCurrentTime() {
        if (initialTime == null || prevMills == -1)
            throw new IllegalStateException("ATM time not initialized by Bank Manager yet");

        long currentMills = System.currentTimeMillis();
        currentTime = new Date(currentTime.getTime() + (currentMills - prevMills));
        prevMills = currentMills;
        return new Date(currentTime.getTime());
    }

    public String getCurrentTimeStamp() {
        return dateFormat.format(getCurrentTime());
    }

    public Date getInitialTime() {
        return new Date(initialTime.getTime());
    }

    public String getInitialTimeStamp() {
        return dateFormat.format(getInitialTime());
    }

}
