package atm;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class AtmTimeTest {

    @Test
    public void testTimeElapse() {
        for (int repetition = 0; repetition < 5; repetition++) {
            Date startTime = new Date();
            AtmTime.setInitialTime(startTime);
            int interval = (int) (Math.random() * 41 + 10);

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long timeDiff = AtmTime.getCurrentTime().getTime() - startTime.getTime();
            assertTrue(timeDiff >= interval - 2 && timeDiff <= interval + 2);
        }
    }
}
