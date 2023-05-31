package com.icesi.cybersecurity.fileencrypter.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileEncrypterErrorCode {

	ENCRYPT_01("The input password is not valid for ket generation", "01"),
	ENCRYPT_02("Fail in IV generation", "02"),
	ENCRYPT_03("There was an error while reading the file or writing the encrypted file", "03"),


	DECRYPT_01("Fail in hashing comparison", "01"),
	DECRYPT_02("Fail in iv parsing", "02"),
	DECRYPT_03("There was an error while reading the encrypted file or writing the decrypted file", "03");


	private final String message;
	private final String code;

}
