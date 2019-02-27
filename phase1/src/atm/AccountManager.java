package atm;

import java.util.*;

public class AccountManager {
    private final List<Class> validClassTypes;
    private Map<Class<?>, ArrayList<Object>> accountMapByType = new HashMap<>();

    AccountManager(Class[] validTypes) {
        validClassTypes = new ArrayList<>(Arrays.asList(validTypes));

        for (Class klass : validClassTypes)
            accountMapByType.put(klass, new ArrayList<>());
    }

    public <T> void addAccountOfType(Class<T> klass, T account) {
        accountMapByType.get(klass).add(account);
    }

    public <T> ArrayList<T> getAccountListOfType(Class<T> klass) {
        if (!validClassTypes.contains(klass))
            throw new IllegalArgumentException("Class type required is not a valid classifier!");


        ArrayList<T> result = new ArrayList<>();

        for (Object obj : accountMapByType.get(klass)) {
            result.add(klass.cast(obj));
        }

        return result;
    }

}
