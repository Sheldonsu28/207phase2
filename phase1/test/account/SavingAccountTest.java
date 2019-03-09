package account;

import atm.User;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import transaction.DepositTransaction;
import transaction.Transaction;
import transaction.WithdrawTransaction;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SavingAccountTest {

    private Date time;
    private User owner;
    private Transaction register;
    private SavingsAccount savingsAccount;
    private Object WithdrawException;


    @Before
    public void setup() {
        time = new Date();
        owner = Mockito.mock(User.class);
    }

    @Test
    public void testGrow(){
        savingsAccount = new SavingsAccount(time, owner, 0.001, "28", 100);
        savingsAccount.grow();
        assertEquals(100 * 1.001, savingsAccount.getBalance(), 0.0);
    }

    @Test
    public void testWithdrawal() {
        register = Mockito.mock(WithdrawTransaction.class);
        savingsAccount = new SavingsAccount(time, owner);
        System.out.println(savingsAccount.getBalance());
        try {
            savingsAccount.withdraw(50, register);
        } catch (InsufficientFundException i) {
            i.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Incorrect exception thrown!");
        }
    }

    @Test
    public void testCancelWithdrawal(){
        savingsAccount = new SavingsAccount(time, owner);
        double withdrawAmount = 50;
        savingsAccount.cancelWithdraw(withdrawAmount);
        assertEquals(withdrawAmount, savingsAccount.getBalance(), 0.0);
    }

    @Test
    public void testDeposit() {
        savingsAccount = new SavingsAccount(time, owner);
        register = Mockito.mock(Transaction.class);
        savingsAccount.deposit(3.0, register);
        assertEquals(3.0, savingsAccount.getNetBalance(), 0.0);
    }

    @Test
    public void testCancelDeposit(){
        testDeposit();
        savingsAccount.cancelDeposit(3.0);
        assertEquals(0.0, savingsAccount.getNetBalance(), 0.0);
    }
}
