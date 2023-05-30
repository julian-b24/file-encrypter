package com.icesi.cybersecurity.fileencrypter.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FileEncrypterControllerAdvice {

	@ExceptionHandler(FileEncrypterException.class)
	public ResponseEntity<FileEncrypterError> handleException(FileEncrypterException exception) {
		return new ResponseEntity<>(exception.getError(), exception.getHttpStatus());
	}

}
