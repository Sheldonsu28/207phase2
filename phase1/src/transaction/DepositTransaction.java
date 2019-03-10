package transaction;

import account.Depositable;
import atm.AtmMachine;
import atm.FileHandler;
import atm.User;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This class is responsible for the transactions between accounts in the system and outside of the system.
 */
public class DepositTransaction extends IntraUserTransaction {
    private enum DepositType {CHEQUE, CASH}

    private final DepositType depositType;
    private final TreeMap<Integer, Integer> depositStock;
    private int depositAmount;
    private final Depositable targetAccount;
    private final AtmMachine machine;

    /**
     * @param user Username.
     * @param machine The atm machine that the user is using perform the operation.
     * @param account Bank account in stored in user account.
     * @throws IllegalDepositInfoException If the deposit file does not exist or cannot be saved, this exception is thrown.
     */
    public DepositTransaction(User user, AtmMachine machine, Depositable account)
            throws IllegalDepositInfoException {
        super(user);

        targetAccount = account;
        this.machine = machine;
        depositStock = new TreeMap<>();

        depositType = interpretDepositInfo();
    }

    /**
     * This method is responsible for interpreting deposit.txt file, if the deposit is cheque,
     * it will return a deposit type Cheque, if the deposit is Cash, it wil return a deposit type cash, if neither,
     * it will throw a Illegal Deposit Info Exception.
     * @return Type of deposit.
     * @throws IllegalDepositInfoException Thrown if the information cannot be interpreted.
     */
    private DepositType interpretDepositInfo() throws IllegalDepositInfoException {
        ArrayList<String> depositFile = (new FileHandler()).readFrom("deposit.txt");

        //  check if file is empty
        if (depositFile == null || depositFile.size() < 1)
            throw new IllegalDepositInfoException();

        System.out.println("FILE NOT THERE");

        String[] depositInfo = depositFile.get(0).split(" ");

        //  deposit info array must be odd-sized greater than 1
        if (depositInfo.length == 1 || depositInfo.length % 2 == 0)
            throw new IllegalDepositInfoException();

        System.out.println("BEFORE TYPE DEF");

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
                    throw new IllegalDepositInfoException();
            }
        } catch (NumberFormatException e) {
            throw new IllegalDepositInfoException();
        }
    }

    /**
     * This method is responsible for interpret Cash deposit information.
     * @param depositInfo The deposit information in String array.
     * @throws IllegalDepositInfoException When the deposit info contains information that cannot be interpreted,
     *                                     this exception is thrown.
     * @throws NumberFormatException When a string in the deposit info cannot be converted into numeric type,
     * this exception is thrown.
     */
    private void interpretCashDepositInfo(String[] depositInfo)
            throws IllegalDepositInfoException, NumberFormatException {
        int sum = 0;

        for (int index = 1; index < depositInfo.length; index += 2) {
            //  implicit check for if info is integer
            int type = Integer.parseInt(depositInfo[index]);
            int amount = Integer.parseInt(depositInfo[index + 1]);

            List<Integer> validTypes = machine.getValidCashTypes();

            //  check for cash type validity for the machine
            if (!validTypes.contains(type))
                throw new IllegalDepositInfoException();

            sum += type * amount;
            if (depositStock.containsKey(type))
                depositStock.put(type, amount + depositStock.get(type));
            else
                depositStock.put(type, amount);
        }

        depositAmount = sum;
    }

    /**
     * This method will interpret the cheque deposit information to the proper deposit amount.
     * @param depositInfo Deposit info read from file.
     * @throws IllegalDepositInfoException This exception is thrown when the deposit info contains
     * information that cannot be interpreted.
     * @throws NumberFormatException When a string in the deposit info cannot be converted into numeric type,
     * this exception is thrown.
     */
    private void interpretChequeDepositInfo(String[] depositInfo)
            throws IllegalDepositInfoException, NumberFormatException {
        //  cheque deposit info should be array of length 2
        if (depositInfo.length != 2)
            throw new IllegalDepositInfoException();

        depositAmount = Integer.parseInt(depositInfo[1]);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doPerform() {
        if (depositType == DepositType.CASH)
            machine.increaseStock(depositStock);

        targetAccount.deposit(depositAmount, this);
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doCancel() {
        targetAccount.cancelDeposit(depositAmount);
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancellable() {
        return true;
    }

}
