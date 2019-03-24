package atm;

import account.Account;
import account.ChequingAccount;

import java.util.*;

/**
 * Stores & categorizes accounts for {@linkplain User}. Can perform filtered account search by type.
 *
 * @author zhaojuna
 * @version 1.0
 */
final class AccountStorageManager {
    private Map<Class, ArrayList<Object>> accountMapByType;
    private ChequingAccount primaryAccount;
    private final User owner;

    AccountStorageManager(User owner) {
        accountMapByType = new HashMap<>();
        this.owner = owner;
    }

    /**
     * @return current primary chequing account for the user.
     */
    public ChequingAccount getPrimaryAccount() {
        return primaryAccount;
    }

    /**
     * Assign a new chequing account to be the new primary account. Target account must be already in this vault.
     *
     * @param account the new primary account to be assigned
     */
    public void setPrimaryAccount(ChequingAccount account) {
        if (!Arrays.asList(account.getOwner()).contains(owner) || !getAllAccounts().contains(account))
            throw new IllegalArgumentException("Given account does not belong to this user.");

        primaryAccount = account;
    }

    /**
     * Add a new account to this vault. The new account must share the same owner as this vault.
     *
     * @param account the new account to be added
     * @param <T>     the type generic of the new account
     */
    public <T extends Account> void addAccount(T account) {
        if (!Arrays.asList(account.getOwner()).contains(owner))
            throw new IllegalArgumentException("Account does not belong to this user vault!");

        //  add this account to its own class' map value
        Class klass = account.getClass();
        add(klass, account);

        //  add this account to all its super classes' and their interfaces' map values
        Class parent = klass.getSuperclass();

        while (!parent.equals(Object.class)) {
            add(parent, account);

            for (Class c : parent.getInterfaces())
                add(c, account);

            parent = parent.getSuperclass();
        }

        //  add this account to its interfaces' map values
        Class[] interfaces = klass.getInterfaces();

        for (Class c : interfaces)
            add(c, account);

    }

    private void add(Class klass, Object account) {
        if (accountMapByType.containsKey(klass)) {
            accountMapByType.get(klass).add(account);
        } else {
            ArrayList<Object> list = new ArrayList<>();
            list.add(account);
            accountMapByType.put(klass, list);
        }
    }

    /**
     * Produces an ArrayList that holds all accounts in this vault that are the given type
     * (every class type of the objects in result ArrayList IS-A given class type)
     *
     * @param klass the type of class to be filtered
     * @param <T>   the generic type of the target class
     * @return an ArrayList contains all accounts satisfies requirement in this vault
     */
    public <T> ArrayList<T> getAccountListOfType(Class<T> klass) {
        ArrayList<T> result = new ArrayList<>();

        if (!accountMapByType.containsKey(klass))
            return result;

        for (Object obj : accountMapByType.get(klass))
            result.add(klass.cast(obj));

        return result;
    }

    /**
     * @return all accounts stored in this vault
     */
    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> result = new ArrayList<>();

        for (Object obj : accountMapByType.get(Account.class))
            result.add((Account) obj);

        return result;
    }

}
