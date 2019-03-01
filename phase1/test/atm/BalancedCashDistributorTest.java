package atm;

import org.junit.Before;
import org.junit.Test;

public class BalancedCashDistributorTest extends CashDistributorTest {

    @Before
    public void before() {
        setDistributor(new BalancedCashDistributor());
    }

    @Test
    public void testSingleImbalanceDistribute() {
        setStock(5, 5, 20, 5);

        testParam(160, true, 0, 0, 0, 16, 0);
        testParam(260, true, 0, 2, 0, 16, 0);
    }

    @Test
    public void testMultipleImbalanceDistribute() {
        setStock(5, 8, 9, 3);

        testParam(295, true, 1, 2, 6, 7, 1);

        setStock(10, 20, 35, 15);

        testParam(785, true, 1, 6, 10, 26, 5);
    }

}
