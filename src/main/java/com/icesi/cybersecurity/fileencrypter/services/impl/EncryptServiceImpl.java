package com.icesi.cybersecurity.fileencrypter.services.impl;

import com.icesi.cybersecurity.fileencrypter.model.EncryptedFileResponse;
import com.icesi.cybersecurity.fileencrypter.services.EncryptService;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Service
@AllArgsConstructor
public class EncryptServiceImpl implements EncryptService {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @Override
    public EncryptedFileResponse encryptFile(MultipartFile file, String password) {
        try {
            String content = "";
            String encryptedContent = "";

            content = new String(file.getBytes());
            SecretKey secretKey = generateKeyFromPassword(password);
            IvParameterSpec iv = generateIv();
            encryptedContent = encrypt(content, secretKey, iv);

            String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            String ivString = bytesToHex(iv.getIV());

            EncryptedFileResponse response = EncryptedFileResponse.builder()
                    .status("Successfully encrypted, check content")
                    .secretKey(secretKeyString)
                    .iv(ivString)
                    .content(encryptedContent)
                    .build();

            return response;

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
    }

    @Override
    public String decryptFile(MultipartFile file, String key, String iv) {

        String content = "";
        String decryptedContent = "";
        try{

            content = new String(file.getBytes());
            SecretKey secretKey = fromStringToKey(key);
            IvParameterSpec decodedIV = fromStringToIV(iv);
            decryptedContent = decrypt(content, secretKey, decodedIV);

        } catch (IOException e) {
            System.out.println("Fail on read");
            throw new RuntimeException(e);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Fail on key generation");
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                NoSuchPaddingException | IllegalBlockSizeException e) {
            System.out.println("Fail on AES Decryption");
            throw new RuntimeException(e);
        } catch (DecoderException e) {
            System.out.println("Fail on IV parse");
            throw new RuntimeException(e);
        }


        return decryptedContent;
    }

    private String encrypt(String content, SecretKey secretKey, IvParameterSpec iv)
            throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] cipherText = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    private String decrypt(String content, SecretKey secretKey, IvParameterSpec iv)throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException
            {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        System.out.println(iv.getIV().length);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decryptedText = cipher.doFinal(Base64.getMimeDecoder().decode(content));
        return new String(decryptedText);
    }

    private SecretKey generateKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] salt = generateSalt();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }

    private byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private IvParameterSpec generateIv() throws NoSuchAlgorithmException, NoSuchPaddingException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] iv = new byte[Cipher.getInstance("AES/CBC/PKCS5Padding").getBlockSize()];
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private SecretKey fromStringToKey(String key){
        byte[] decodedKey = Base64.getDecoder().decode(key);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return originalKey;
    }

    private IvParameterSpec fromStringToIV(String iv) throws DecoderException {
        return new IvParameterSpec(Hex.decodeHex(iv.toCharArray()));
    }

}
