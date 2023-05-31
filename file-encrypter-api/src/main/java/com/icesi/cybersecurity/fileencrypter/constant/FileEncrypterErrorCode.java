package com.icesi.cybersecurity.fileencrypter.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileEncrypterErrorCode {

	ENCRYPT_00("", ""),
	ENCRYPT_02("", ""),


	DECRYPT_01("Fail in hashing comparison", "01");


	private final String message;
	private final String code;

}
