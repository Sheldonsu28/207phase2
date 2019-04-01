package atm;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Defines the Behaviors of CashDistributors.
 */
abstract class CashDistributor implements Serializable {
    abstract TreeMap<Integer, Integer> distribute(TreeMap<Integer, Integer> cashStock, int amount)
            throws CashShortageException;

    /**
     * Initialize a stock.
     * @param stock the stock you want to initialize
     * @return A initialized Stock.
     */
    TreeMap<Integer, Integer> getInitializedResultMap(TreeMap<Integer, Integer> stock) {
        TreeMap<Integer, Integer> result = new TreeMap<>();

        for (int key : stock.keySet())
            result.put(key, 0);

        return result;
    }

}
