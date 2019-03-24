package atm;

import java.util.TreeMap;

class BalancedCashDistributor extends CashDistributor {
    /**
     * Sort the cash stock provided.
     *
     * @param stock The cash stock you want to sort.
     * @return The sort result.
     */
    private int[][] getSortedAmountValue(TreeMap<Integer, Integer> stock) {
        int[] amounts = stock.values().stream().mapToInt(i -> i).toArray();
        int[] types = stock.keySet().stream().mapToInt(i -> i).toArray();

        int[][] result = new int[2][amounts.length];

        //  selection sort
        for (int index = 0; index < amounts.length; index++) {
            int maxIndex = -1;
            int maxVal = Integer.MIN_VALUE;

            for (int i = index; i < amounts.length; i++) {
                if (amounts[i] > maxVal) {
                    maxIndex = i;
                    maxVal = amounts[i];
                }
            }

            if (maxIndex != index) {
                int temp = amounts[index];
                amounts[index] = amounts[maxIndex];
                amounts[maxIndex] = temp;

                temp = types[index];
                types[index] = types[maxIndex];
                types[maxIndex] = temp;
            }

        }

        result[0] = amounts;
        result[1] = types;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    TreeMap<Integer, Integer> distribute(TreeMap<Integer, Integer> orgStock, int amount) throws CashShortageException {
        int balance = amount;
        TreeMap<Integer, Integer> result = getInitializedResultMap(orgStock);

        int[][] amountsTypes = getSortedAmountValue(orgStock);

        int[] amounts = amountsTypes[0];
        int[] types = amountsTypes[1];

        int prevBalance = -1;
        outer:
        for (int index = 0; index < types.length; index++) {
            int currentType = types[index];
            int minAlign = amounts[types.length - 1];

            /*  If last item is reached and no changes detected for the current loop, then call StepCashDistributor
            to complete the left-over distribution. */
            if (index == types.length - 1) {
                if (prevBalance == balance) {
                    TreeMap<Integer, Integer> midstep = new TreeMap<>();

                    for (int key : orgStock.keySet())
                        midstep.put(key, orgStock.get(key) - result.get(key));

                    TreeMap<Integer, Integer> newResult = (new StepCashDistributor()).distribute(midstep, balance);

                    for (int key : newResult.keySet())
                        result.put(key, result.get(key) + newResult.get(key));

                    break;
                } else {
                    prevBalance = balance;
                    index = 0;
                    continue;
                }
            }

            //  Take as much as possible from the current type (as the array is sorted by amount from highest to lowest)
            int nextDiff = amounts[index] - minAlign;

            while (nextDiff > 0) {
                if (nextDiff * currentType <= balance) {
                    balance -= nextDiff * currentType;
                    amounts[index] -= nextDiff;
                    result.put(currentType, result.get(currentType) + nextDiff);

                    if (balance == 0)
                        break outer;

                    continue outer;
                } else {
                    nextDiff--;
                }
            }

        }

        return result;
    }
}
