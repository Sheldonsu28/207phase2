package atm;

import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class StepCashDistributorTest {

    private StepCashDistributor distributor;

    @Before
    public void before() {
        distributor = new StepCashDistributor();
    }

    @Test
    public void testPlentyStockDistribute() {
        TreeMap<Integer, Integer> stock = new TreeMap<>();

        stock.put(5, 100);
        stock.put(10, 100);
        stock.put(20, 100);
        stock.put(50, 100);

        testDifferentParameter(stock, 285, 5, 1, 1, 1);
        testDifferentParameter(stock, 245, 4, 2, 0, 1);
    }

    private void testDifferentParameter(TreeMap<Integer, Integer> stock, int amount,
                                        int expected50, int expected20, int expected10, int expected5) {
        TreeMap<Integer, Integer> result = new TreeMap<>();

        try {
            result = distributor.distribute(stock, amount);
        } catch (CashShortageException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.getResult());
        }

        assertEquals(expected50, (int) result.get(50));
        assertEquals(expected20, (int) result.get(20));
        assertEquals(expected10, (int) result.get(10));
        assertEquals(expected5, (int) result.get(5));
    }

    //  TODO more test cases needed

}
