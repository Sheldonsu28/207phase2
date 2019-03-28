package atm;

import ui.MainFrame;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

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

    /**
     * Constructs an atm time starting at the given time
     *
     * @param initialTime the starting time
     */
    AtmTime(Date initialTime) {
        dateFormat = new SimpleDateFormat(FORMAT_STRING);
        this.initialTime = initialTime;
        dayFormat = new SimpleDateFormat("dd");
        hmsFormat = new SimpleDateFormat("HH:mm:ss");
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
                        MainFrame.shutdown();
                        break;

                    case "00:01:00":
                        MainFrame.restart();
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

    /**
     * Return the weekday of a given date.
     * @param mm month
     * @param dd day
     * @param yyyy year
     * @return weekday
     */
    public String getWeekday(int mm, int dd, int yyyy) {
        String[] Weekdays = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        int numOfWeekday = weekday(mm, dd, yyyy);
        return Weekdays[numOfWeekday -1];
    }

    private int weekday(int mm, int dd, int yyyy) {
        int yy, total, remainder;
        yy = yyyy - 1900;

        if (isLeap(yyyy)) {
            if (mm == 1)
                total = yy / 4 + yy + dd + monthOffset(mm) - 1;
            else if (mm == 2)
                total = yy / 4 + yy + dd + monthOffset(mm) - 1;
            else
                total = yy / 4 + yy + dd + monthOffset(mm);
        } else
            total = yy/4 + yy + dd + monthOffset(mm);

        remainder = total%7;

        return remainder;
    }

    private int monthOffset(int mm) {
        int result;
        int[] offset = new int[] {1, 4, 4, 0, 2, 5, 0, 3, 6, 1, 4, 6};
        result = offset[mm - 1];
        return result;
    }

    private boolean isLeap(int yyyy) {
        if (yyyy % 400 == 0)
            return true;
        else if (yyyy % 100 == 0)
            return false;
        else
            return (yyyy % 4 == 0);

    }
}



