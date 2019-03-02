package atm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public final class AtmTime extends Observable implements Serializable {
    private static boolean hasRunningInstance = false;
    private SimpleDateFormat dateFormat;
    private Date initialTime, currentTime;
    private long prevMills = -1;

    AtmTime(Date initialTime) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        initialize(initialTime);
    }

    AtmTime(Date initialTime, SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
        initialize(initialTime);
    }

    private void initialize(Date initialTime) {
        if (hasRunningInstance)
            throw new IllegalStateException("Only one Atm Time instance is allowed at one time!");
        hasRunningInstance = true;

        this.initialTime = initialTime;
        currentTime = initialTime;
        prevMills = System.currentTimeMillis();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long currentMills = System.currentTimeMillis();
                currentTime = new Date(currentTime.getTime() + (currentMills - prevMills));
                prevMills = currentMills;
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 0L, 100L);
    }

    void changeDateFormat(SimpleDateFormat newFormat) {
        dateFormat = newFormat;
    }

    public Date getCurrentTime() {
        if (initialTime == null || prevMills == -1)
            throw new IllegalStateException("ATM time not initialized by Bank Manager yet");

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
