package atm;

import account.Account;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public final class AccountFactory {

    <T extends Account> boolean generateDefaultAccount(User owner, Class<T> accountType, Date time) {
        T account = null;

        try {
            Constructor<T> defaultConstructor = accountType.getDeclaredConstructor(Date.class, User.class);
            account = defaultConstructor.newInstance(time, owner);
        } catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (account != null) {
            owner.addAccount(accountType, account);
            return true;
        }

        return false;
    }
}
