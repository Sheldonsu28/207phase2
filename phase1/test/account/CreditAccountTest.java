package account;
import atm.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import transaction.Transaction;
import transaction.WithdrawTransaction;
import static org.junit.Assert.assertTrue;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class CreditAccountTest{

    private Date time;
    private User owner;
    private Transaction register;
    private CreditCardAccount creditAccount;

    @Before
    public void setup(){
        time = new Date();
        owner = Mockito.mock(User.class);
    }

    @Test
    public void testDeposit() {
        creditAccount = new CreditCardAccount(time, owner);
        register = Mockito.mock(Transaction.class);
        creditAccount.deposit(3.0, register);
        assertEquals(3.0, creditAccount.getNetBalance(), 0.0);
    }

    @Test
    public void testCancelDeposit(){
        testDeposit();
        creditAccount.cancelDeposit(3.0);
        assertEquals(0.0, creditAccount.getNetBalance(), 0.0);
    }
}