package transaction;

import account.ChequingAccount;
import atm.AtmMachine;
import atm.IllegalFileFormatException;
import atm.User;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class DepositTransactionTest {

    private User owner;
    private AtmMachine machine;
    private ChequingAccount chequingAccount;
    private DepositTransaction register;

    @Test
    public void testDepositTransaction() throws IllegalFileFormatException {
        owner = Mockito.mock(User.class);
        machine = Mockito.mock(AtmMachine.class);
        chequingAccount = Mockito.mock(ChequingAccount.class);
        register = new DepositTransaction(owner, machine, chequingAccount);
        register.doPerform();
        verify(chequingAccount, times(1)).deposit(50.0, register);
    }

    @Test
    public void testCancelDepositTransaction() throws IllegalFileFormatException {
        owner = Mockito.mock(User.class);
        machine = Mockito.mock(AtmMachine.class);
        chequingAccount = Mockito.mock(ChequingAccount.class);
        register = new DepositTransaction(owner, machine, chequingAccount);
        register.doCancel();
        verify(chequingAccount, times(1)).cancelDeposit(50.0);
    }

}
