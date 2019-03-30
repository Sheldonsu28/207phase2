package atm;

import account.*;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Produces account object with given specifications.
 *
 * @author zhaojuna
 * @version 1.0
 */
final class AccountFactory implements Serializable {

//    <T extends Account> boolean generateDefaultAccount(User owner, Class<T> accountType, AtmTime time, boolean isPrimary) {
//        T account = null;
//
//        try {
//            Constructor<T> defaultConstructor = accountType.getDeclaredConstructor(Date.class, User.class);
//            account = defaultConstructor.newInstance(time.getCurrentTime(), owner);
//        } catch (NoSuchMethodException | InstantiationException |
//                IllegalAccessException | InvocationTargetException e) {
//            System.out.println("Failed to create account due to following reason: ");
//            e.printStackTrace();
//        }
//
//        if (account != null) {
//            owner.addAccount(account);
//
//            if (accountType == ChequingAccount.class && isPrimary)
//                owner.setPrimaryAccount((ChequingAccount) account);
//
//            if (account instanceof Growable)
//                time.addObserver((Observer) account);
//
//            //  time-related class can have their observer hook here
//
//            return true;
//        }
//
//        return false;
//    }


    <T extends Account> boolean generateDefaultAccount(List<User> owners, Class<T> accountType, AtmTime time,
                                                       boolean isPrimary) {
        if (!Arrays.asList(Account.OWNABLE_ACCOUNT_TYPES).contains(accountType))
            throw new IllegalArgumentException("Class type given is not an ownable account type");

        T account = null;

        try {
            Constructor<T> defaultConstructor = accountType.getDeclaredConstructor(Date.class, List.class);
            account = defaultConstructor.newInstance(time.getCurrentTime(), owners);
        } catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            System.out.println("Failed to create account due to following reason: ");
            e.printStackTrace();
        }

        if (account != null) {
            for (User owner : owners) {
                owner.addAccount(account);

                if (accountType == ChequingAccount.class && isPrimary)
                    owner.setPrimaryAccount((ChequingAccount) account);
            }

            if (account instanceof Growable)
                time.addObserver((Observer) account);

            if (account instanceof StockAccount)
                time.addObserver((Observer) account);

            //  time-related class can have their observer hook here

            return true;
        }

        return false;
    }

    List<BillingAccount> getPayeesFromFile(AtmTime time) throws IllegalFileFormatException {
        ExternalFiles file = ExternalFiles.PAYEE_DATA_FILE;
        List<BillingAccount> billingAccounts = new ArrayList<>();

        for (String payeeStr : (new FileHandler()).readFrom(file)) {
            String[] payeeInfo = payeeStr.split(" ");

            //  no space allowed
            if (payeeInfo.length != 1)
                throw new IllegalFileFormatException(file);

            billingAccounts.add(new BillingAccount(time.getInitialTime(), payeeStr));
        }

        return billingAccounts;
    }

}
