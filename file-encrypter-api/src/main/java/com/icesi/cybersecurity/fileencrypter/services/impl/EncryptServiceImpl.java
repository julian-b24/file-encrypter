package com.icesi.cybersecurity.fileencrypter.services.impl;

import com.icesi.cybersecurity.fileencrypter.model.DecryptedFileResponse;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
@AllArgsConstructor
public class EncryptServiceImpl implements EncryptService {

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	private static final String CRYPTO_TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final String HASHING_ALGORITHM = "SHA-256";

	@Override
	public EncryptedFileResponse encryptFile(MultipartFile file, String password, String outputPath) {
		try {

			SecretKey secretKey = generateKeyFromPassword(password);
			IvParameterSpec iv = generateIv();
			File encryptedContent = doCrypto(Cipher.ENCRYPT_MODE, file, secretKey, iv, outputPath);

			String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			String ivString = bytesToHex(iv.getIV());

			byte[] dataEncrypted = Files.readAllBytes(encryptedContent.toPath());
			String hashOriginal = getHash(file.getBytes());
			String hashEncrypted = getHash(dataEncrypted);

            return EncryptedFileResponse.builder()
                    .status("Successfully encrypted")
                    .secretKey(secretKeyString)
                    .iv(ivString)
                    .hashOriginalFile(hashOriginal)
                    .hashEncryptedFile(hashEncrypted)
                    .build();

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
	public DecryptedFileResponse decryptFile(MultipartFile file, String key, String iv, String outputfilePath) {

		String content = "";
		File decryptedContent;
		try {

			SecretKey secretKey = fromStringToKey(key);
			IvParameterSpec decodedIV = fromStringToIV(iv);
			decryptedContent = doCrypto(Cipher.DECRYPT_MODE, file, secretKey, decodedIV, outputfilePath);

			byte[] dataDecrypted = Files.readAllBytes(decryptedContent.toPath());
			String hashEncrypted = getHash(file.getBytes());
			String hashDecrypted = getHash(dataDecrypted);

			DecryptedFileResponse response = DecryptedFileResponse.builder()
					.status("Successfully decrypted")
					.hashEncryptedFile(hashEncrypted)
					.hashDecryptedFile(hashDecrypted)
					.build();

			return response;
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

	}


	private File doCrypto(int cipherMode, MultipartFile content, SecretKey secretKey, IvParameterSpec iv, String outputPath)
			throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, IOException {

		Cipher cipher = Cipher.getInstance(CRYPTO_TRANSFORMATION);
		cipher.init(cipherMode, secretKey, iv);

		File fileInput = convertToFile(content, "file.tmp");
		FileInputStream fis = new FileInputStream(fileInput);
		byte[] inputBytes = new byte[(int) fileInput.length()];
		fis.read(inputBytes);

		byte[] outputBytes = cipher.doFinal(inputBytes);

		File fileOutput = new File(outputPath + content.getName() + ".out");
		fileOutput.createNewFile();
		FileOutputStream fos = new FileOutputStream(fileOutput);
		fos.write(outputBytes);

		fis.close();
		fos.close();
		fileInput.delete();
		return fileOutput;
	}

	private SecretKey generateKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

		byte[] salt = generateSalt();
		SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
		return new SecretKeySpec(factory.generateSecret(spec)
				.getEncoded(), "AES");
	}

	private byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	private IvParameterSpec generateIv() throws NoSuchAlgorithmException, NoSuchPaddingException {
		SecureRandom random = SecureRandom.getInstanceStrong();
		byte[] iv = new byte[Cipher.getInstance(CRYPTO_TRANSFORMATION).getBlockSize()];
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

	private SecretKey fromStringToKey(String key) {
		byte[] decodedKey = Base64.getDecoder().decode(key);
		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		return originalKey;
	}

	private IvParameterSpec fromStringToIV(String iv) throws DecoderException {
		return new IvParameterSpec(Hex.decodeHex(iv.toCharArray()));
	}

	private String getHash(byte[] bytes) throws NoSuchAlgorithmException {
		byte[] hashBytes = MessageDigest.getInstance("SHA-256").digest(bytes);
		return bytesToHex(hashBytes);
	}

	private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
		File tempFile = new File(fileName);
		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			fos.write(multipartFile.getBytes());
			fos.close();
		}
		return tempFile;
	}

}
