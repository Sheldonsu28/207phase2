package atm;

import account.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class AccountStorageManagerTest {
    private AccountStorageManager vaults;

    @Before
    public void before() {
        vaults = new AccountStorageManager();
    }

    @Test
    public void testStorage() {
        ChequingAccount ca = Mockito.mock(ChequingAccount.class);
        SavingsAccount sa = Mockito.mock(SavingsAccount.class);
        LineOfCreditAccount lca = Mockito.mock(LineOfCreditAccount.class);
        CreditCardAccount cca = Mockito.mock(CreditCardAccount.class);

        vaults.addAccount(ca);
        vaults.addAccount(sa);
        vaults.addAccount(lca);
        vaults.addAccount(cca);

        assertTrue(vaults.getAllAccounts().containsAll(Arrays.asList(ca, sa, lca, cca)));
        assertTrue(vaults.getAccountListOfType(AssetAccount.class).containsAll(Arrays.asList(ca, sa)));
        assertTrue(vaults.getAccountListOfType(DebtAccount.class).containsAll(Arrays.asList(lca, cca)));
        assertTrue(vaults.getAccountListOfType(Depositable.class).containsAll(Arrays.asList(ca, sa, lca, cca)));
        assertTrue(vaults.getAccountListOfType(Withdrawable.class).containsAll(Arrays.asList(ca, sa, lca)));
        assertTrue(vaults.getAccountListOfType(Indebtable.class).containsAll(Arrays.asList(ca, lca, cca)));
        assertTrue(vaults.getAccountListOfType(Growable.class).contains(sa));
    }
}
