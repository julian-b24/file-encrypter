package co.edu.icesi.cybersecurity.fileencrypter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class FileEncrypterException extends RuntimeException {

	private HttpStatus status;
	private FileEncrypterError error;

}
