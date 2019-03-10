package transaction;

import account.ChequingAccount;
import account.SavingsAccount;
import account.WithdrawException;
import atm.User;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class TransferTransactionTest {

    private User owner;
    private ChequingAccount chequingAccount;
    private SavingsAccount savingsAccount;
    private TransferTransaction register;

    @Test
    public void testTransferTransaction() throws WithdrawException{
        owner = Mockito.mock(User.class);
        chequingAccount = Mockito.mock(ChequingAccount.class);
        savingsAccount = Mockito.mock(SavingsAccount.class);
        int amount = 50;
        register = new TransferTransaction(owner, savingsAccount, chequingAccount, amount);
        register.doPerform();
        verify(chequingAccount, times(1)).deposit(50.0, register);
        verify(savingsAccount, times(1)).withdraw(50.0, register);
    }

    @Test
    public void testCancelTransferTransaction() throws WithdrawException {
        owner = Mockito.mock(User.class);
        chequingAccount = Mockito.mock(ChequingAccount.class);
        savingsAccount = Mockito.mock(SavingsAccount.class);
        int amount = 50;
        register = new TransferTransaction(owner, savingsAccount, chequingAccount, amount);
        register.doCancel();
        verify(chequingAccount, times(1)).cancelDeposit(50.0);
        verify(savingsAccount, times(1)).cancelWithdraw(50.0);
    }
}
