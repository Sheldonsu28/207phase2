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

    @Test
    public void testwithdrawException(){
        ChequingAccount chequingAccount = new ChequingAccount(time, owner, initialBalance);
        int amount = 1000;
        WithdrawTransaction register = new WithdrawTransaction(owner, chequingAccount, amount);

        try { chequingAccount.withdraw(amount, register);
        } catch (DebtLimitExceededException d){
            d.printStackTrace();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    public void testwithdrawal() throws WithdrawException {
        ChequingAccount chequingAccount = new ChequingAccount(time, owner, initialBalance);
        int amount1 = 50;
        WithdrawTransaction register1 = new WithdrawTransaction(owner, chequingAccount, amount1);
        chequingAccount.withdraw(amount1, register1);
        double balance = chequingAccount.getNetBalance();
        System.out.println(balance);
        assertTrue(balance == -50);
    }


}
