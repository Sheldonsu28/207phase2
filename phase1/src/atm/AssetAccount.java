package atm;

abstract class AssetAccount extends Account implements DepositableAccount, WithdrawableAccount {
    AssetAccount() {
        super();
    }

    @Override
    int getNetBalance() {
        return getBalance();
    }
}
