package transaction;

import account.Account;
import account.Depositable;
import atm.*;
import ui.MainFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This class is responsible for deposit money to the accounts.
 */
public class DepositTransaction extends Transaction {
    private double depositAmount;

    private final DepositType depositType;
    private final TreeMap<Integer, Integer> depositStock;
    private final ExternalFiles file;

    public DepositTransaction(User user, AtmMachine machine, Depositable account, double depositAmount) {
        super(user);

        targetAccount = account;
        this.machine = machine;
        depositStock = new TreeMap<>();
        file = null;

        depositType = DepositType.MANUAL;

        this.depositAmount = depositAmount;
    }
    private final Depositable targetAccount;
    private final AtmMachine machine;

    /**
     * Initialize a new deposit transaction with user, ATM machine and user's bank account.
     *
     * @param user The user which the money will be deposited to.
     * @param machine The ATM machine that perform the action.
     * @param account The account which the money will deposit to.
     */
    public DepositTransaction(User user, AtmMachine machine, Depositable account)
            throws IllegalFileFormatException {
        super(user);

        targetAccount = account;
        this.machine = machine;
        depositStock = new TreeMap<>();
        file = ExternalFiles.DEPOSIT_FILE;

        depositType = interpretDepositInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doPerform() {
        if (depositType == DepositType.CASH) {
            try {
                machine.increaseStock(depositStock);
            } catch (InvalidCashTypeException e) {
                MainFrame.showErrorMessage(e.getMessage());
                return false;
            }
        }

        targetAccount.deposit(depositAmount, this);
        return true;
    }

    /**
     * This method is responsible for interpret the deposit information from the deposit.txt file,
     * if the information cannot be interpreted, it will throw a IllegalFileFormatException.
     * It returns types of the deposit base on the interpretation of the file.
     *
     * @return The type of the deposit.
     * @throws IllegalFileFormatException throws illegal file format exception
     */
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

    /**
     * This method is responsible for interpreting a cash deposit info from deposit.txt.
     * If the info cannot be interpreted, it will throw a IllegalFileFormatException.
     * If it cannot convert the integer String( depositInfo[1] ) to integer, it will throw a NumberFormatException.
     *
     * @param depositInfo                 The transaction info read from deposit.txt.
     * @throws IllegalFileFormatException When the format of the file cannot be interpreted,
     *                                    this exception will be thrown.
     * @throws NumberFormatException      When the integer String cannot be converted to integer, this exception is thrown.
     */
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

    /**
     * This method is responsible for interpreting a cheque deposit info from deposit.txt.
     * If the info cannot be interpreted, it will throw a IllegalFileFormatException.
     * If it cannot convert the integer String( depositInfo[1] ) to integer, it will throw a NumberFormatException.
     *
     * @param depositInfo                 The transaction info read from deposit.txt.
     * @throws IllegalFileFormatException When the format of the file cannot be interpreted,
     *                                    this exception will be thrown.
     * @throws NumberFormatException      When the integer String cannot be converted to integer, this exception is thrown.
     */
    private void interpretChequeDepositInfo(String[] depositInfo)
            throws IllegalFileFormatException, NumberFormatException {
        //  cheque deposit info should be array of length 2
        if (depositInfo.length != 2)
            throw new IllegalFileFormatException(file);

        depositAmount = Double.parseDouble(depositInfo[1]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return super.toString() + String.format("%s-%s DEPOSIT $%.2f",
                getFromUser(), ((Account) targetAccount).getId(), depositAmount);
    }

    private enum DepositType {CHEQUE, CASH, MANUAL}

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
