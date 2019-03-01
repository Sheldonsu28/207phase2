package atm;

import org.junit.Before;
import org.junit.Test;

public class StepCashDistributorTest extends CashDistributorTest {

    @Before
    public void before() {
        setDistributor(new StepCashDistributor());
    }

    @Test
    public void testPlentyStockDistribute() {
        setStock(100, 100, 100, 100);

        testParam(285, true, 0, 5, 1, 1, 1);
        testParam(245, true, 0, 4, 2, 0, 1);
    }

    @Test
    public void testShortageStockDistribute() {
        setStock(3, 1, 10, 5);

        testParam(240, true, 0, 3, 1, 7, 0);

        setStock(0, 3, 0, 6);

        testParam(85, true, 0, 0, 3, 0, 5);
        testParam(95, false, 0, 0, -3, 0, 6);

        setStock(100, 100, 100, 0);

        testParam(355, false, 0, -1, 0, 0, 0);
    }

}
