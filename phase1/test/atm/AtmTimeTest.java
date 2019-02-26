package atm;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class AtmTimeTest {

    @Test
    public void testTimeElapse() {
        Date startTime = new Date();
        AtmTime.setInitialTime(startTime);
        long timeElapsed = 0;

        for (int repetition = 0; repetition < 5; repetition++) {
            int interval = (int) (Math.random() * 41 + 10);
            timeElapsed += interval;

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long timeDiff = AtmTime.getCurrentTime().getTime() - startTime.getTime();
            assertTrue(timeDiff >= timeElapsed - 5 && timeDiff <= timeElapsed + 5);
        }
    }
}