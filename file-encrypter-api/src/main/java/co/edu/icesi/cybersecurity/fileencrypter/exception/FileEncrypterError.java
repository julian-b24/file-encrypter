package co.edu.icesi.cybersecurity.fileencrypter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileEncrypterError {

	private String code;
	private String message;

}