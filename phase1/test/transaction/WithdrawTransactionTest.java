package transaction;

import account.ChequingAccount;
import account.WithdrawException;
import atm.AtmMachine;
import org.junit.Test;
import org.mockito.Mockito;
import atm.User;
import static org.mockito.Mockito.*;


public class WithdrawTransactionTest {
    private ChequingAccount chequingAccount;
    private WithdrawTransaction register;
    private DepositTransaction register1;
    private User owner;
    private AtmMachine machine;

//    @Before
//    public void setup(){
//        owner = Mockito.mock(User.class);
//        machine = Mockito.mock(AtmMachine.class);
//    }

    @Test
    public void testWithdrawTransaction() throws WithdrawException {
        owner = Mockito.mock(User.class);
        machine = Mockito.mock(AtmMachine.class);
        chequingAccount = Mockito.mock(ChequingAccount.class);
        int withdrawAmount = 50;
        register = new WithdrawTransaction(owner, machine, chequingAccount, withdrawAmount);
        register.doPerform();
        verify(chequingAccount, times(1)).withdraw(withdrawAmount, register);
    }


    @Test
    public void testCancelWithdrawTransaction() throws WithdrawException{
        owner = Mockito.mock(User.class);
        machine = Mockito.mock(AtmMachine.class);
        chequingAccount = Mockito.mock(ChequingAccount.class);
        int withdrawAmount = 50;
        register = new WithdrawTransaction(owner, machine, chequingAccount, withdrawAmount);
        chequingAccount.withdraw(withdrawAmount, register);
        register.doCancel();
        verify(chequingAccount, times(1)).cancelWithdraw(withdrawAmount);
    }

    @Test
    public void testException(){
        owner = Mockito.mock(User.class);
        machine = Mockito.mock(AtmMachine.class);
        chequingAccount = Mockito.mock(ChequingAccount.class);
        int withdrawAmount = -50;
        try{
            register = new WithdrawTransaction(owner, machine, chequingAccount, withdrawAmount);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
