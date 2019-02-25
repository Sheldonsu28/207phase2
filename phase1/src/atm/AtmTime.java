package atm;

import java.text.SimpleDateFormat;
import java.util.Date;

abstract class AtmTime {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static Date initialTime;
    private static long prevMills = -1;

    static void setInitialTime(Date start) {
        initialTime = start;
        prevMills = System.currentTimeMillis();
    }

    static Date getCurrentTime() {
        if (initialTime == null || prevMills == -1)
            throw new IllegalStateException("ATM time not initialized by Bank Manager yet");

        long currentMills = System.currentTimeMillis();
        Date newDate = new Date(initialTime.getTime() + (currentMills - prevMills));
        prevMills = currentMills;
        return newDate;
    }

    static Date getInitialTime() {
        return initialTime;
    }

}
