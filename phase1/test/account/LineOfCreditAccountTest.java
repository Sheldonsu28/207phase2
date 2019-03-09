package account;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import transaction.WithdrawTransaction;
import atm.User;
import transaction.DepositTransaction;
import transaction.Transaction;
import java.util.Date;

import static org.junit.Assert.assertEquals;

import java.util.Date;

public class LineOfCreditAccountTest {

    private Date time;
    private LineOfCreditAccount lineOfCreditAccount;
    private Transaction register;
    private User owner;


    @Before
    public void setup(){
        time = new Date();
        owner = Mockito.mock(User.class);
    }

    @Test
    public void testWithdrawal(){
        lineOfCreditAccount= new LineOfCreditAccount(time, owner, -0.5);
        register = Mockito.mock(WithdrawTransaction.class);

        try {
            lineOfCreditAccount.withdraw(50, register);
        } catch (DebtLimitExceededException i) {
            i.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Incorrect exception thrown!");
        }
    }

    @Test
    public void testCancelWithdrawal() {
        testWithdrawal();
        double withdrawAmount = 50;

        lineOfCreditAccount.cancelWithdraw(withdrawAmount);

        assertEquals(-withdrawAmount, lineOfCreditAccount.getBalance(), 0.0);
    }

    @Test
    public void testDeposit() {
        lineOfCreditAccount = new LineOfCreditAccount(time, owner);
        register = Mockito.mock(Transaction.class);
        lineOfCreditAccount.deposit(3.0, register);
        assertEquals(3.0, lineOfCreditAccount.getNetBalance(), 0.0);
    }

    @Test
    public void testCancelDeposit(){
        testDeposit();
        lineOfCreditAccount.cancelDeposit(3.0);
        assertEquals(0.0, lineOfCreditAccount.getNetBalance(), 0.0);
    }

}
