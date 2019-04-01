package atm;

import java.util.TreeMap;

/**
 * Define the exception that happen during withdraw from cash handler operation
 */
public class CashShortageException extends Exception {
    private final TreeMap<Integer, Integer> result;
    CashShortageException(TreeMap<Integer, Integer> result, int leftOver, int produced) {
        super(String.format("Unable to produce full amount due to partial cash shortage " +
                "(Produced $%d, surplus $%d).", produced, leftOver));
        this.result = result;
    }
    TreeMap<Integer, Integer> getResult() {
        return result;
    }

}
