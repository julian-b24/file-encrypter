package co.edu.icesi.cybersecurity.fileencrypter.service.impl;

import co.edu.icesi.cybersecurity.fileencrypter.exception.FileEncrypterError;
import co.edu.icesi.cybersecurity.fileencrypter.exception.FileEncrypterException;
import co.edu.icesi.cybersecurity.fileencrypter.service.FileEncrypterService;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import static co.edu.icesi.cybersecurity.fileencrypter.constant.FileEncrypterErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class FileEncrypterServiceImpl implements FileEncrypterService {

	private static final String CIPHER = "AES/CBC/PKCS5Padding";
	private static final char[] HEX = "0123456789ABCDEF".toCharArray();

	@Override
	@SneakyThrows
	public String encrypt(MultipartFile file, String password) {
		IvParameterSpec iv = generateIv();
		byte[] salt = generateSalt();
		SecretKey key = generateKey(password.toCharArray(), salt);

		Cipher cipher = Cipher.getInstance(CIPHER);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		byte[] encryted = cipher.doFinal(file.getBytes());

		String contentString = bytesToHex(encryted);
		String ivString = bytesToHex(iv.getIV());
		String saltString = bytesToHex(salt);
		String hashString = computeHash(file.getBytes());

		return "Salt: " + saltString + "\nIV: " + ivString + "\nContent: " + contentString + "\nHash: " + hashString;
	}

	@Override
	@SneakyThrows
	public String decrypt(MultipartFile file, String password) {
		byte[] bytes = file.getBytes();

		String[] lines = new String(bytes).split("\n");
		if (lines.length != 4)
			throw new FileEncrypterException(BAD_REQUEST, new FileEncrypterError(DECRYPT_01.name(), DECRYPT_01.getMessage()));

		String lineSalt = lines[0];
		if (!lineSalt.startsWith("Salt: ") || lineSalt.length() != "Salt: ".length() + 32)
			throw new FileEncrypterException(BAD_REQUEST, new FileEncrypterError(DECRYPT_02.name(), DECRYPT_02.getMessage()));
		byte[] salt = Hex.decodeHex(lineSalt.replace("Salt: ", "").toCharArray());

		String lineIv = lines[1];
		if (!lineIv.startsWith("IV: ") || lineIv.length() != "IV: ".length() + 32)
			throw new FileEncrypterException(BAD_REQUEST, new FileEncrypterError(DECRYPT_03.name(), DECRYPT_03.getMessage()));
		byte[] iv = Hex.decodeHex(lineIv.replace("IV: ", "").toCharArray());

		String lineContent = lines[2];
		if (!lineContent.startsWith("Content: ") || lineContent.length() == "Content: ".length())
			throw new FileEncrypterException(BAD_REQUEST, new FileEncrypterError(DECRYPT_04.name(), DECRYPT_04.getMessage()));
		byte[] content = Hex.decodeHex(lineContent.replace("Content: ", "").toCharArray());

		String lineHash = lines[3];
		if (!lineHash.startsWith("Hash: ") || lineHash.length() != "Hash: ".length() + 64)
			throw new FileEncrypterException(BAD_REQUEST, new FileEncrypterError(DECRYPT_05.name(), DECRYPT_05.getMessage()));
		String hash = lineHash.replace("Hash: ", "");

		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		SecretKey key = generateKey(password.toCharArray(), salt);

		Cipher cipher = Cipher.getInstance(CIPHER);
		cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

		try {
			byte[] decrypted = cipher.doFinal(content);

			String decryptedHash = computeHash(decrypted);

			if (!decryptedHash.equals(hash)) {
				throw new FileEncrypterException(BAD_REQUEST, new FileEncrypterError(DECRYPT_06.name(), DECRYPT_06.getMessage()));
			}

			return Base64.getMimeEncoder().encodeToString(decrypted);
		} catch (BadPaddingException e) {
			throw new FileEncrypterException(BAD_REQUEST, new FileEncrypterError(DECRYPT_07.name(), DECRYPT_07.getMessage()));
		}
	}

	@SneakyThrows
	private IvParameterSpec generateIv() {
		Cipher cipher = Cipher.getInstance(CIPHER);
		byte[] iv = new byte[cipher.getBlockSize()];
		SecureRandom random = SecureRandom.getInstanceStrong();
		random.nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	private byte[] generateSalt() {
		byte[] salt = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		return salt;
	}

	@SneakyThrows
	private SecretKey generateKey(char[] password, byte[] salt) {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
		return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	}

	@SneakyThrows
	private String computeHash(byte[] bytes) {
		MessageDigest instance = MessageDigest.getInstance("SHA-256");
		byte[] hash = instance.digest(bytes);
		return bytesToHex(hash);
	}

	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX[v >>> 4];
			hexChars[j * 2 + 1] = HEX[v & 0x0F];
		}
		return new String(hexChars);
	}

}
