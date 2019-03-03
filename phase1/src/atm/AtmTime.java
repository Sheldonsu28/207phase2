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
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
    private Timer timer;
    private final Date initialTime;
    private Date currentTime, prevTime;

    AtmTime(Date initialTime) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        this.initialTime = initialTime;
        initialize(initialTime);
    }

    AtmTime(Date initialTime, SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
        this.initialTime = initialTime;
        initialize(initialTime);
    }

    private void initialize(Date initialTime) {
        if (hasRunningInstance)
            throw new IllegalStateException("Only one Atm Time instance is allowed at one time!");
        hasRunningInstance = true;

        currentTime = initialTime;
        prevTime = currentTime;

        timer = new Timer();
        timer.schedule(getTimerTask(), 0L, 100L);
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                long currentMills = System.currentTimeMillis();
                long prevMills = prevTime.getTime();
                currentTime = new Date(currentTime.getTime() + (currentMills - prevMills));
                prevTime = new Date(currentMills);

                String prevDay = dayFormat.format(prevTime);
                String currDay = dayFormat.format(currentTime);
                if (!prevDay.equals(currDay)) {
                    setChanged();
                    notifyObservers(currDay);
                }
            }
        };
    }

    void changeDateFormat(SimpleDateFormat newFormat) {
        dateFormat = newFormat;
    }

    public Date getCurrentTime() {
        if (initialTime == null)
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

    public void terminate() {
        timer.cancel();
        hasRunningInstance = false;
    }

}
