package hr.tvz.stefan.project.passwordmanager.util;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.exceptions.DecryptionException;
import hr.tvz.stefan.project.passwordmanager.exceptions.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public interface PasswordEncryptor {

    String ENCRYPTION_ALGORITHM = "AES";

    String ENCRYPTION_KEY = "dstefan";

    static String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ENCRYPTION_ALGORITHM);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception ex) {
            Launcher.logger.error("Password encryption failed!", ex);
            throw new EncryptionException(ex);
        }
    }

    static String decryptPassword(String encryptedPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ENCRYPTION_ALGORITHM);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            Launcher.logger.error("Password decryption failed!", ex);
            throw new DecryptionException(ex);
        }
    }

}
