package atm;

import account.Account;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest{
    private Account account;
    private Account account1;
    private User user;

    @Before
    public void Setup() {
        account = mock(Account.class);
        account1 = mock(Account.class);
        user = new User("Test account", "csc207");
        user.addAccount(account);
        user.addAccount(account1);
        when(account.getNetBalance()).thenReturn((double)20);
        when(account1.getNetBalance()).thenReturn((double)40);
        when(account.getBalance()).thenReturn((double)20);
        when(account1.getBalance()).thenReturn((double) 40);
        when(account.getId()).thenReturn("id_1");
        when(account1.getId()).thenReturn("id_2");

    }

    @Test
    public void getNetTotalTest() {
        assertEquals((int) user.getNetTotal(), 60);
    }

    @Test
    public void getAccountSummaryTest(){
        String output = String.format("Account Summary: \nID id_1   TYPE %s   BAL $20.00\nID id_2   TYPE %s   BAL $40.00\n",
                account.getClass().getSimpleName(),account1.getClass().getSimpleName());
        assertEquals(output, user.getAccountsSummary());
    }


}