package hr.tvz.stefan.project.passwordmanager.util;

import java.util.Random;

public class PasswordGenerator {

    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*_=+-/";
    private static final String ALL_CHARACTERS = LOWERCASE_LETTERS + UPPERCASE_LETTERS + NUMBERS + SYMBOLS;
    private static final Random random = new Random();

    public static String generatePassword(int length, boolean includeLowercase, boolean includeUppercase,
                                          boolean includeNumbers, boolean includeSymbols) {

        StringBuilder password = new StringBuilder();

        String characterSet = "";

        if (includeLowercase) {
            characterSet += LOWERCASE_LETTERS;
        }
        if (includeUppercase) {
            characterSet += UPPERCASE_LETTERS;
        }
        if (includeNumbers) {
            characterSet += NUMBERS;
        }
        if (includeSymbols) {
            characterSet += SYMBOLS;
        }

        int characterSetLength = characterSet.length();

        for (int i = 0; i < length; i++) {
            password.append(characterSet.charAt(random.nextInt(characterSetLength)));
        }

        return password.toString();
    }

    public static String generatePassword(int length) {

        StringBuilder password = new StringBuilder();

        String characterSet = ALL_CHARACTERS;

        int characterSetLength = characterSet.length();

        for (int i = 0; i < length; i++) {
            password.append(characterSet.charAt(random.nextInt(characterSetLength)));
        }

        return password.toString();
    }

}
