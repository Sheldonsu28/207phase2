package atm;

import account.Account;
import account.Growable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Observer;

public final class AccountFactory implements Serializable {

    <T extends Account> boolean generateDefaultAccount(User owner, Class<T> accountType, AtmTime time) {
        T account = null;

        try {
            Constructor<T> defaultConstructor = accountType.getDeclaredConstructor(Date.class, User.class);
            account = defaultConstructor.newInstance(time.getCurrentTime(), owner);
        } catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (account != null) {
            owner.addAccount(accountType, account);

            if (containsInterface(accountType, Growable.class))
                time.addObserver((Observer) account);

            return true;
        }

        return false;
    }

    private boolean containsInterface(Class original, Class targetInterface) {
        Class[] interfaces = original.getInterfaces();

        for (Class klass : interfaces) {
            if (klass == targetInterface)
                return true;
        }

        return false;
    }
}
