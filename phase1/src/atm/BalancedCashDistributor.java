package atm;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

class BalancedCashDistributor extends CashDistributor {
    @Override
        //  TODO this definitely needs test
    TreeMap<Integer, Integer> distribute(TreeMap<Integer, Integer> orgStock, int amount) throws CashShortageException {
        int balance = amount;
        TreeMap<Integer, Integer> result = getInitializedResultMap(orgStock);
        TreeMap<Integer, Integer> cashStock = new TreeMap<>(Collections.reverseOrder());

        for (Map.Entry<Integer, Integer> entry : orgStock.entrySet())
            cashStock.put(entry.getValue(), entry.getKey());

        int[] keys = cashStock.keySet().stream().mapToInt(i -> i).toArray();

        int prevBalance = balance;
        outer:
        for (int index = 0; index < keys.length; index++) {
            int currentAmount = keys[index];
            int currentType = cashStock.get(currentAmount);
            int nextDiff;
            boolean isLastItem = index == keys.length - 1;

            if (isLastItem)
                nextDiff = keys[index] - keys[index - 1] + 1;
            else
                nextDiff = keys[index] - keys[index + 1] + 1;

            while (nextDiff > 0) {
                if (nextDiff * currentType <= balance) {
                    balance -= nextDiff * currentType;
                    result.put(currentType, nextDiff);

                    if (balance == 0)
                        break outer;

                    break;
                } else {
                    nextDiff--;
                }
            }

            if (isLastItem) {
                if (prevBalance == balance) {
                    TreeMap<Integer, Integer> midstep = new TreeMap<>();

                    for (int key : orgStock.keySet()) {
                        if (result.containsKey(key))
                            midstep.put(key, orgStock.get(key) - result.get(key));
                        else
                            midstep.put(key, orgStock.get(key) - result.get(key));
                    }

                    TreeMap<Integer, Integer> newResult = (new StepCashDistributor()).distribute(midstep, balance);

                    for (int key : newResult.keySet()) {
                        if (result.containsKey(key))
                            result.put(key, result.get(key) + newResult.get(key));
                        else
                            result.put(key, newResult.get(key));
                    }

                    break;
                } else {
                    index = 0;
                }
            }

            prevBalance = balance;
        }

        return result;
    }
}
