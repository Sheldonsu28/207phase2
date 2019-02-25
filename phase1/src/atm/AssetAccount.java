package atm;

abstract class AssetAccount extends Account implements Depositable, Withdrawable {
    AssetAccount() {
        super();
    }
}
