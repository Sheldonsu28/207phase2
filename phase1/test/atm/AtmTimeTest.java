package atm;

import org.junit.Test;
import org.mockito.Mockito;
import ui.Console;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class AtmTimeTest {
    private AtmTime time;

    @Test
    public void testTimeElapse() {
        Date startTime = new Date();
        time = new AtmTime(startTime, Mockito.mock(Console.class));
        long timeElapsed = 0;

        for (int repetition = 0; repetition < 5; repetition++) {
            int interval = (int) (Math.random() * 41 + 10);
            timeElapsed += interval;

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long timeDiff = time.getCurrentTime().getTime() - startTime.getTime();
            assertTrue(timeDiff >= timeElapsed - 200 && timeDiff <= timeElapsed + 200);
        }
    }
}
