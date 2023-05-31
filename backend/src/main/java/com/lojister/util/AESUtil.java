package com.lojister.util;


import org.springframework.stereotype.Component;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AESUtil {

    private static final String AES_ALGORITHM = "AES";

    public static String encrypt(String plainText) throws Exception {
        String key = "SemSiye12345678x";
        SecretKey secretKey = generateSecretKey(key);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        String key = "SemSiye12345678x";
        SecretKey secretKey = generateSecretKey(key);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getUrlDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static SecretKey generateSecretKey(String key) throws Exception {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
        return secretKeySpec;
    }

    public static void main(String[] args) throws Exception {
        String plainText = "Merhaba, bu bir AES örneğidir!";

        // Şifreleme
        String encryptedText = encrypt(plainText);
        System.out.println("Şifreli Metin: " + encryptedText);

        // Şifre çözme
        String decryptedText = decrypt(encryptedText);
        System.out.println("Çözülmüş Metin: " + decryptedText);
    }
}
