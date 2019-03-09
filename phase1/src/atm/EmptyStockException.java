package atm;

import java.util.Map;

public class EmptyStockException extends Exception {
    private CashHandler shortageStock;

    EmptyStockException(CashHandler shortageStock) {
        this.shortageStock = shortageStock;
    }

    public String getMessage() {
        Map<Integer, Integer> stock = shortageStock.getCashStock();
        boolean isEmptyStock = true;

        for (int value : stock.values()) {
            if (value != 0) {
                isEmptyStock = false;
                break;
            }
        }

        if (isEmptyStock)
            return "There is no cash left! Awaits restock from bank manager!";
        else
            return "We can not produce your requested amount since your request is higher than total " +
                    "amount of cash in this machine.";
    }
}
