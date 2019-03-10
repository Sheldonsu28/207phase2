package atm;

import ui.Console;

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
    public static final String FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss";
    private static boolean hasRunningInstance = false;
    private SimpleDateFormat dateFormat;
    private final SimpleDateFormat dayFormat, hmsFormat;
    private Timer timer;
    private final Date initialTime;
    private Date currentTime;
    private long prevMills, currMills;
    private Console activateConsole;

    /**
     * Constructs an atm time starting at the given time
     *
     * @param initialTime the starting time
     */
    AtmTime(Date initialTime, Console console) {
        dateFormat = new SimpleDateFormat(FORMAT_STRING);
        this.initialTime = initialTime;
        dayFormat = new SimpleDateFormat("dd");
        hmsFormat = new SimpleDateFormat("HH:mm:ss");
        activateConsole = console;
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
        currMills = System.currentTimeMillis();
        prevMills = currMills;

        timer = new Timer();
        timer.schedule(getTimerTask(), 0L, 100L);
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                currMills = System.currentTimeMillis();
                String prevDay = dayFormat.format(currentTime);
                currentTime = new Date(currentTime.getTime() + (currMills - prevMills));
                String currDay = dayFormat.format(currentTime);
                prevMills = currMills;

                if (!prevDay.equals(currDay)) {
                    setChanged();
                    notifyObservers(currDay);
                }

                switch (hmsFormat.format(currentTime)) {
                    case "23:59:00":
                        activateConsole.shutdown();
                        break;

                    case "00:01:00":
                        activateConsole.start();
                        break;

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
    Date getCurrentTime() {
        if (initialTime == null)
            throw new IllegalStateException("ATM time not initialized by Bank Manager yet");

        return new Date(currentTime.getTime());
    }

    /**
     * @return the String stamp of the relative-real-time using the formatter
     */
    String getCurrentTimeStamp() {
        return dateFormat.format(getCurrentTime());
    }

    /**
     * @return the starting time of this instance
     */
    Date getInitialTime() {
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
