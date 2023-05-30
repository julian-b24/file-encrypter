package com.icesi.cybersecurity.fileencrypter.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileEncrypterErrorCode {

	ENCRYPT_00("");

	private final String message;

}
