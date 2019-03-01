package atm;

import java.util.Map;

public class CashShortageException extends Exception {
    private Map<Integer, Integer> result;

    CashShortageException(Map<Integer, Integer> result, int leftOver, int produced) {
        super(String.format("Unable to produce full amount due to partial cash shortage " +
                "(Produced $%d, surplus $%d).\n", produced, leftOver));
        this.result = result;
    }

    Map<Integer, Integer> getResult() {
        return result;
    }

}
