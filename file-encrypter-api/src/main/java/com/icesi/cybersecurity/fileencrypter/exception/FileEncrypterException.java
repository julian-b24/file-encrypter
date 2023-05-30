package com.icesi.cybersecurity.fileencrypter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class FileEncrypterException extends RuntimeException {

	private HttpStatus httpStatus;
	private FileEncrypterError error;

}
