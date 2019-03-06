package atm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

class StepCashDistributor extends CashDistributor {

    @Override
    TreeMap<Integer, Integer> distribute(TreeMap<Integer, Integer> orgStock, int amount) throws CashShortageException {
        TreeMap<Integer, Integer> result = getInitializedResultMap(orgStock);
        int balance = amount;

        ArrayList<Integer> cashTypes = new ArrayList<>(orgStock.keySet());
        cashTypes.sort(Collections.reverseOrder());

        for (int type : cashTypes) {
            int stock = orgStock.get(type);

            if (balance >= type && stock > 0) {
                int takeAmount = Math.min(stock, balance / type);

                result.put(type, takeAmount);

                balance -= takeAmount * type;

                if (balance == 0)
                    break;
            }
        }

        if (balance != 0)
            throw new CashShortageException(result, balance, amount - balance);

        return result;
    }
}
