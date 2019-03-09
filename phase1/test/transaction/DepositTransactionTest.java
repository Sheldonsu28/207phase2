package transaction;

import account.ChequingAccount;
import atm.AtmMachine;
=import org.junit.Test;
import org.mockito.Mockito;
import atm.User;
import static org.mockito.Mockito.*;


public class DepositTransactionTest {

    private User owner;
    private AtmMachine machine;
    private ChequingAccount chequingAccount;
    private DepositTransaction register;

    @Test
    public void testDepositTransaction() throws IllegalDepositInfoException{
        owner = Mockito.mock(User.class);
        machine = Mockito.mock(AtmMachine.class);
        chequingAccount = Mockito.mock(ChequingAccount.class);
        register = new DepositTransaction(owner, machine, chequingAccount);
        register.doPerform();
        verify(chequingAccount, times(1)).deposit(50.0, register);
    }

    @Test
    public void testCancelDepositTransaction() throws IllegalDepositInfoException{
        owner = Mockito.mock(User.class);
        machine = Mockito.mock(AtmMachine.class);
        chequingAccount = Mockito.mock(ChequingAccount.class);
        register = new DepositTransaction(owner, machine, chequingAccount);
        register.doCancel();
        verify(chequingAccount, times(1)).cancelDeposit(50.0);
    }

}
