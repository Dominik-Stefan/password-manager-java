package hr.tvz.stefan.project.passwordmanager.util;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.exceptions.HashException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Hash {

    static String hashString(String input) throws HashException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            Launcher.logger.error("Hashing algorithm not found!", ex);
            throw new HashException(ex);
        }
    }

    static boolean checkHash(String input, String hash) throws HashException {
        String inputHashString = hashString(input);
        return inputHashString.equals(hash);
    }

}
