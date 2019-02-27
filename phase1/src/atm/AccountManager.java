package atm;

import account.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//  TODO this needs heavy testing
public class AccountManager {
    private Map<Class, ArrayList<Object>> accountMapByType;

    AccountManager() {
        accountMapByType = new HashMap<>();
    }

    public <T> void addAccount(Class<T> klass, T account) {
        add(klass, account);
        Class parent = klass.getSuperclass();

        while (!parent.equals(Object.class)) {
            add(parent, account);
            parent = parent.getSuperclass();
        }

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

    public <T> ArrayList<T> getAccountListOfType(Class<T> klass) {
        ArrayList<T> result = new ArrayList<>();

        for (Object obj : accountMapByType.get(klass))
            result.add(klass.cast(obj));

        return result;
    }

    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> result = new ArrayList<>();

        for (Object obj : accountMapByType.get(Account.class))
            result.add((Account) obj);

        return result;
    }

}
