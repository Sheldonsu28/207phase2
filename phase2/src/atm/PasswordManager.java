package atm;

import java.io.Serializable;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * This class generate Random passwords for account's default password when the account is been created.
 */
class PasswordManager implements Serializable {
    //Maximum and minimum length of the password.
    private final int leastDigit;
    private final int mostDigit;
    //The regex representation of characters that are allowed in the password.
    private final Pattern regexPattern;
    // Random number.
    private final Random random;
    private final char[] randomPool;

    /**
     * The class uses the random module from java to generate a random password.
     *
     * @param leastDigit The minimum length of the password.
     * @param mostDigit  The maximum length of the password.
     * @param regex      regular expression of allowed characters
     */
    PasswordManager(int leastDigit, int mostDigit, String regex) {
        this.leastDigit = leastDigit;
        this.mostDigit = mostDigit;
        regexPattern = Pattern.compile(regex);
        random = new Random();
        randomPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
                .toCharArray();
    }

    /**
     * Generate the password using java.util.Random module.
     *
     * @return The password generated.
     */
    String generateRandomPassword() {
        int totalDigit = random.nextInt(mostDigit - leastDigit + 1) + leastDigit;
        StringBuilder password = new StringBuilder();

        for (int digit = 0; digit < totalDigit; digit++) {
            String c = Character.toString(randomPool[random.nextInt(randomPool.length)]);
            if (regexPattern.matcher(c).matches()) {
                password.append(c);
            }
        }

        return password.toString();
    }

    public boolean isValidPassword(String password) {
        for (char c : password.toCharArray()) {
            if (!regexPattern.matcher(Character.toString(c)).matches())
                return false;
        }

        return true;
    }

}
