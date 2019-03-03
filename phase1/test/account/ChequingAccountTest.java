package account;
import atm.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import transaction.WithdrawTransaction;
import static org.junit.Assert.assertTrue;
import java.util.Date;


public class ChequingAccountTest {

    private Date time;
    private double initialBalance;

    @Before
    public void setup(){
        time = new Date();
        initialBalance = 0;
    }

    @Mock
    protected User owner;
    WithdrawTransaction register;


    @Test
    public void testDebtLimitExceededException(){
        ChequingAccount chequingAccount = new ChequingAccount(time, owner, initialBalance);
        int amount = 1000;
        double amount1 = 1000;
        try { chequingAccount.withdraw(amount1, register);
        } catch (DebtLimitExceededException d){
            d.printStackTrace();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    public void testInsufficientFundException() {
        ChequingAccount chequingAccount = new ChequingAccount(time, owner, initialBalance);
        int amount = 50;
        try {
            chequingAccount.withdraw(amount, register);
        } catch (InsufficientFundException d) {
            d.printStackTrace();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    public void testWithdrawal() throws WithdrawException {
        ChequingAccount chequingAccount = new ChequingAccount(time, owner, initialBalance);
        int amount = 50;
        double amount1 = 50;
        chequingAccount.withdraw(amount1, register);
        double balance = chequingAccount.getNetBalance();
        assertTrue(balance == -50);
    }

    @Test
    public void testDeposit(){
        ChequingAccount chequingAccount = new ChequingAccount(time, owner, initialBalance);
        int amount1 = 50;
        chequingAccount.deposit(amount1, register);
        double balance = chequingAccount.getNetBalance();
        assertTrue(balance == 50);
    }


    @Test
    public void testCancelWithdrawal() throws WithdrawException {
        ChequingAccount chequingAccount = new ChequingAccount(time, owner, initialBalance);
        int amount1 = 50;
        chequingAccount.cancelWithdraw(amount1);
        double balance = chequingAccount.getNetBalance();
        assertTrue(balance == 50);
    }

    @Test
    public void testCancelDeposit(){
        ChequingAccount chequingAccount = new ChequingAccount(time, owner, initialBalance);
        int amount1 = 50;
        chequingAccount.cancelDeposit(amount1);
        double balance = chequingAccount.getNetBalance();
        assertTrue(balance == -50);
    }



}
