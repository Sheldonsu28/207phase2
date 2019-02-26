package account;

public abstract class AssetAccount extends Account implements Depositable, Withdrawable {
    AssetAccount() {
        super();
    }

    @Override
    public double getNetBalance() {
        return getBalance();
    }
}
