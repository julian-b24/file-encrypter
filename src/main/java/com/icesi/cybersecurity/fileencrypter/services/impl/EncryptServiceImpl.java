package com.icesi.cybersecurity.fileencrypter.services.impl;

import com.icesi.cybersecurity.fileencrypter.services.EncryptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
@AllArgsConstructor
public class EncryptServiceImpl implements EncryptService {

    @Override
    public String encryptFile(MultipartFile file, String password) {
        String content = "";
        String encryptedContent = "";
        try {
            content = new String(file.getBytes());
            SecretKey secretKey = generateKeyFromPassword(password);
            IvParameterSpec iv = generateIv();
            encryptedContent = encrypt(content, secretKey, iv);

        } catch (IOException e) {
            System.out.println("Fail on read");
            throw new RuntimeException(e);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Fail on key generation");
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 NoSuchPaddingException | IllegalBlockSizeException e) {
            System.out.println("Fail on AES encryption");
            throw new RuntimeException(e);
        }

        return encryptedContent;
    }

    @Override
    public void decryptFile() {

    }

    private String encrypt(String content, SecretKey secretKey, IvParameterSpec iv)
            throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] cipherText = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    private SecretKey generateKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] salt = generateSalt();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        System.out.println(secret);
        return secret;
    }

    private byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

}
