package atm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The time class representing "current" time based on a start time using timer thread.
 * Upon instantiation the time will start ticking from the start time and being updated every 100ms
 * according to the real-time.
 * The relative-real-time or its stamp can be acquired at any time.
 * <p>
 * Only one instance of this class is allowed at one time.
 *
 * @author zhaojuna
 * @version 1.0
 */
public final class AtmTime extends Observable implements Serializable {
    private static boolean hasRunningInstance = false;
    private SimpleDateFormat dateFormat;
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
    private Timer timer;
    private final Date initialTime;
    private Date currentTime, prevTime;

    /**
     * Constructs an atm time starting at the given time
     *
     * @param initialTime the starting time
     */
    AtmTime(Date initialTime) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        this.initialTime = initialTime;
        initialize(initialTime);
    }

    /**
     * Constructs an atm time starting at the given time with a specific date format used to create String time stamps
     *
     * @param initialTime the starting time
     * @param dateFormat  the format of time stamp
     */
    AtmTime(Date initialTime, SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
        this.initialTime = initialTime;
        initialize(initialTime);
    }

    /**
     * Initializes current instance. Assures that no instance of this class is currently running.
     *
     * @param initialTime the starting time
     */
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

    /**
     * @param newFormat the new stamp String format
     */
    void changeDateFormat(SimpleDateFormat newFormat) {
        dateFormat = newFormat;
    }

    /**
     * @return the relative-real-time
     */
    public Date getCurrentTime() {
        if (initialTime == null)
            throw new IllegalStateException("ATM time not initialized by Bank Manager yet");

        return new Date(currentTime.getTime());
    }

    /**
     * @return the String stamp of the relative-real-time using the formatter
     */
    public String getCurrentTimeStamp() {
        return dateFormat.format(getCurrentTime());
    }

    /**
     * @return the starting time of this instance
     */
    public Date getInitialTime() {
        return new Date(initialTime.getTime());
    }

    /**
     * @return the String stamp of the starting time using the formatter
     */
    public String getInitialTimeStamp() {
        return dateFormat.format(getInitialTime());
    }

    /**
     * Terminate the current running instance by cancelling the scheduled timer task (terminating the thread).
     */
    public void terminate() {
        timer.cancel();
        hasRunningInstance = false;
    }

}
