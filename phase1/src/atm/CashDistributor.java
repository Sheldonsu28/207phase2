package atm;

import java.util.TreeMap;

abstract class CashDistributor {
    abstract TreeMap<Integer, Integer> distribute(TreeMap<Integer, Integer> cashStock, int amount)
            throws CashShortageException;

    TreeMap<Integer, Integer> getInitializedResultMap(TreeMap<Integer, Integer> stock) {
        TreeMap<Integer, Integer> result = new TreeMap<>();

        for (int key : stock.keySet())
            result.put(key, 0);

        return result;
    }
}
