package atm;

import account.Account;
import account.BillingAccount;
import account.ChequingAccount;
import account.Growable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observer;

/**
 * Produces account object with given specifications.
 *
 * @author zhaojuna
 * @version 1.0
 */
final class AccountFactory implements Serializable {

    /**
     * Generate a specific type of {@linkplain Account} object by invoking {@linkplain Account}'s universal default
     * constructor {@link Account#Account(Date, User)}.
     *
     * @param owner       the user who owns this account
     * @param accountType the type of account being created
     * @param time        the common time for stamping creation time
     * @param isPrimary   this parameter is only considered when accountType is {@link ChequingAccount}.
     *                    set to true if this chequing account requested to be the primary, set to false otherwise
     * @param <T>         the type generics representing the account class type being created
     * @return true if the account is successfully generated & initialized and added to its owner user's vault,
     * false otherwise
     */
    <T extends Account> boolean generateDefaultAccount(User owner, Class<T> accountType, AtmTime time, boolean isPrimary) {
        T account = null;

        try {
            Constructor<T> defaultConstructor = accountType.getDeclaredConstructor(Date.class, User.class);
            account = defaultConstructor.newInstance(time.getCurrentTime(), owner);
        } catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            System.out.println("Failed to create account due to following reason: ");
            e.printStackTrace();
        }

        if (account != null) {
            owner.addAccount(account);

            if (accountType == ChequingAccount.class && isPrimary)
                owner.setPrimaryAccount((ChequingAccount) account);

            if (account instanceof Growable)
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
