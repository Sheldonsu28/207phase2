package atm;

import java.util.Map;

class CashShortageException extends Exception {
    private CashHandler shortageStock;

    CashShortageException(CashHandler shortageStock) {
        this.shortageStock = shortageStock;
    }

    String getActionMessage() {
        Map<Integer, Integer> stock = shortageStock.getCashStock();
        boolean isEmptyStock = true;

        for (int value : stock.values()) {
            if (value != 0) {
                isEmptyStock = false;
                break;
            }
        }

        if (isEmptyStock) {
            return "There is no cash left! Awaits restock from bank manager!";
        } else {
            StringBuilder msg = new StringBuilder("We can not produce your requested amount, but we do have");

            for (int cashVal : stock.keySet()) {
                if (stock.get(cashVal) != 0) {
                    msg.append(String.format(" %d $%d,", stock.get(cashVal), cashVal));
                }
            }

            msg.append(String.format(" left with a sum total of $%d.", shortageStock.getAmount()));

            return msg.toString();
        }
    }
}
