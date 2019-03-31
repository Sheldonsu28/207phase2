package atm;

public class IllegalWithdrawAmount extends Exception {
    public IllegalWithdrawAmount(int minCashType) {
        super("Can not complete the withdraw since the machine does not have cash with value less than " + minCashType);
    }
}
