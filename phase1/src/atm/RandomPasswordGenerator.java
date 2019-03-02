package atm;

import java.util.Random;

public class RandomPasswordGenerator {
    private int leastDigit, mostDigit;
    private char[] allowedChars;
    private Random random;

    RandomPasswordGenerator(int leastDigit, int mostDigit, String allowedChars) {
        this.leastDigit = leastDigit;
        this.mostDigit = mostDigit;
        this.allowedChars = allowedChars.toCharArray();
        random = new Random();
    }

    String generatePassword() {
        int totalDigit = random.nextInt(mostDigit - leastDigit + 1) + leastDigit;
        StringBuilder password = new StringBuilder();

        for (int digit = 0; digit < totalDigit; digit++)
            password.append(allowedChars[random.nextInt(allowedChars.length)]);

        return password.toString();
    }

}
