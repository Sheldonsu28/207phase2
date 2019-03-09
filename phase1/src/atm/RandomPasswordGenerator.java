package atm;

import java.io.Serializable;
import java.util.Random;

/**
 * This class generate Random passwords for account's default password when the account is been created.
 */
public class RandomPasswordGenerator implements Serializable {
    //Maximum and minimum length of the password.
    private int leastDigit, mostDigit;
    //The characters that are allowed in the password.
    private char[] allowedChars;
    // Random number.
    private Random random;

    /**
     * The class uses the random module from java to generate a random password.
     * @param leastDigit The minimum length of the password.
     * @param mostDigit The maximum length of the password.
     * @param allowedChars Characters allowed in the password.
     */
    RandomPasswordGenerator(int leastDigit, int mostDigit, String allowedChars) {
        this.leastDigit = leastDigit;
        this.mostDigit = mostDigit;
        this.allowedChars = allowedChars.toCharArray();
        random = new Random();
    }

    /**
     * Generate the password using java.util.Random module.
     * @return The password generated.
     */
    String generatePassword() {
        int totalDigit = random.nextInt(mostDigit - leastDigit + 1) + leastDigit;
        StringBuilder password = new StringBuilder();

        for (int digit = 0; digit < totalDigit; digit++)
            password.append(allowedChars[random.nextInt(allowedChars.length)]);

        return password.toString();
    }

}
