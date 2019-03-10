package transaction;

import account.Depositable;
import atm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DepositTransaction extends Transaction {
    private enum DepositType {CHEQUE, CASH}

    private final DepositType depositType;
    private final TreeMap<Integer, Integer> depositStock;
    private final ExternalFiles file;
    private int depositAmount;
    private final Depositable targetAccount;
    private final AtmMachine machine;

    public DepositTransaction(User user, AtmMachine machine, Depositable account)
            throws IllegalFileFormatException {
        super(user);

        targetAccount = account;
        this.machine = machine;
        depositStock = new TreeMap<>();
        file = ExternalFiles.DEPOSIT_FILE;

        depositType = interpretDepositInfo();
    }

    private DepositType interpretDepositInfo() throws IllegalFileFormatException {
        ArrayList<String> depositFile = (new FileHandler()).readFrom(file);

        //  check if file is empty
        if (depositFile == null || depositFile.size() < 1)
            throw new IllegalFileFormatException(file);

        String[] depositInfo = depositFile.get(0).split(" ");

        //  deposit info array must be odd-sized greater than 1
        if (depositInfo.length == 1)
            throw new IllegalFileFormatException(file);

        try {
            //  check deposit type (cheque or cash)
            switch (depositInfo[0]) {
                case "CHEQUE":
                    interpretChequeDepositInfo(depositInfo);
                    return DepositType.CHEQUE;

                case "CASH":
                    interpretCashDepositInfo(depositInfo);
                    return DepositType.CASH;

                default:
                    throw new IllegalFileFormatException(file);
            }
        } catch (NumberFormatException e) {
            throw new IllegalFileFormatException(file);
        }
    }

    private void interpretCashDepositInfo(String[] depositInfo)
            throws IllegalFileFormatException, NumberFormatException {
        int sum = 0;

        for (int index = 1; index < depositInfo.length; index += 2) {
            //  implicit check for if info is integer
            int type = Integer.parseInt(depositInfo[index]);
            int amount = Integer.parseInt(depositInfo[index + 1]);

            List<Integer> validTypes = machine.getValidCashTypes();

            //  check for cash type validity for the machine
            if (!validTypes.contains(type))
                throw new IllegalFileFormatException(file);

            sum += type * amount;
            if (depositStock.containsKey(type))
                depositStock.put(type, amount + depositStock.get(type));
            else
                depositStock.put(type, amount);
        }

        depositAmount = sum;
    }

    private void interpretChequeDepositInfo(String[] depositInfo)
            throws IllegalFileFormatException, NumberFormatException {
        //  cheque deposit info should be array of length 2
        if (depositInfo.length != 2)
            throw new IllegalFileFormatException(file);

        depositAmount = Integer.parseInt(depositInfo[1]);
    }

    @Override
    protected boolean doPerform() {
        if (depositType == DepositType.CASH)
            machine.increaseStock(depositStock);

        targetAccount.deposit(depositAmount, this);
        return true;
    }

    @Override
    protected boolean doCancel() {
        targetAccount.cancelDeposit(depositAmount);
        return true;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

}
