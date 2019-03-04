package account;

import atm.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import transaction.DepositTransaction;
import transaction.Transaction;
import transaction.WithdrawTransaction;

import java.util.Date;

import static org.junit.Assert.assertEquals;


public class ChequingAccountTest {

    private Date time;
    private User owner;
    private Transaction register;
    private ChequingAccount chequingAccount;

    @Before
    public void setup() {
        time = new Date();
        owner = Mockito.mock(User.class);
    }


    @Test
    public void testDebtLimitExceededException() {
        chequingAccount = new ChequingAccount(time, owner, 0);
        register = Mockito.mock(WithdrawTransaction.class);

        try {
            chequingAccount.withdraw(1000, register);
        } catch (DebtLimitExceededException d) {
            d.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Incorrect exception thrown!");
        }
    }

    @Test
    public void testInsufficientFundException() {
        chequingAccount = new ChequingAccount(time, owner, -0.5);
        register = Mockito.mock(WithdrawTransaction.class);

        try {
            chequingAccount.withdraw(50, register);
        } catch (InsufficientFundException i) {
            i.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Incorrect exception thrown!");
        }
    }

    @Test
    public void testWithdrawal() {
        chequingAccount = new ChequingAccount(time, owner, 0);
        register = Mockito.mock(WithdrawTransaction.class);

        int withdrawAmount = 50;

        try {
            chequingAccount.withdraw(withdrawAmount, register);
        } catch (WithdrawException e) {
            e.printStackTrace();
            throw new AssertionError("Incorrect exception thrown!");
        }

        assertEquals(-withdrawAmount, chequingAccount.getBalance(), 0.0);
    }

    @Test
    public void testCancelWithdrawal() {
        testWithdrawal();

        chequingAccount.cancelWithdraw(50);

        assertEquals(0, chequingAccount.getBalance(), 0.0);
    }

    @Test
    public void testDeposit() {
        chequingAccount = new ChequingAccount(time, owner, 0);
        register = Mockito.mock(DepositTransaction.class);

        int depositAmount = 50;

        chequingAccount.deposit(depositAmount, register);
        assertEquals(depositAmount, chequingAccount.getBalance(), 0.0);
    }

    @Test
    public void testCancelDeposit() {
        testDeposit();
        chequingAccount.cancelDeposit(50);

        assertEquals(0, chequingAccount.getBalance(), 0.0);
    }

}
