package atm;

import account.Account;
import account.Growable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Observer;

final class AccountFactory implements Serializable {

    <T extends Account> boolean generateDefaultAccount(User owner, Class<T> accountType, AtmTime time) {
        T account = null;

        try {
            Constructor<T> defaultConstructor = accountType.getDeclaredConstructor(Date.class, User.class);
            account = defaultConstructor.newInstance(time.getCurrentTime(), owner);
        } catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            System.out.println("Failed to create account");
            e.printStackTrace();
        }

        if (account != null) {
            owner.addAccount(account);

            if (account instanceof Growable)
                time.addObserver((Observer) account);

            //  time-related class can have their observer hook here

            return true;
        }

        return false;
    }

}
