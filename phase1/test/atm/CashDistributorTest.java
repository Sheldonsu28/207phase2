package atm;

import java.util.TreeMap;

import static org.junit.Assert.assertTrue;

abstract class CashDistributorTest {

    private CashDistributor distributor;
    private TreeMap<Integer, Integer> stock;

    void setDistributor(CashDistributor distributor) {
        this.distributor = distributor;
    }

    void setStock(int stock50, int stock20, int stock10, int stock5) {
        stock = new TreeMap<>();

        stock.put(50, stock50);
        stock.put(20, stock20);
        stock.put(10, stock10);
        stock.put(5, stock5);
    }

    void testParam(int amount, boolean hasEnough, int offset,
                   int expected50, int expected20, int expected10, int expected5) {
        if (distributor == null)
            throw new IllegalStateException("Distributor not initialized!");

        if (stock == null)
            throw new IllegalStateException("Stock not initialized!");

        TreeMap<Integer, Integer> result = null;

        try {
            result = distributor.distribute(stock, amount);
        } catch (CashShortageException e) {
            System.out.println(e.getMessage());

            if (hasEnough)
                throw new AssertionError("Expected to have sufficient stock but actually not.");

            result = e.getResult();
            return;
        } finally {
            System.out.println(result);
        }

        assertTrue(Math.abs(expected50 - result.get(50)) <= offset);
        assertTrue(Math.abs(expected20 - result.get(20)) <= offset);
        assertTrue(Math.abs(expected10 - result.get(10)) <= offset);
        assertTrue(Math.abs(expected5 - result.get(5)) <= offset);
    }
}
