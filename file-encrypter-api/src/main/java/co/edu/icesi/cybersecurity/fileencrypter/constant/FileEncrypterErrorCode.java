package co.edu.icesi.cybersecurity.fileencrypter.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileEncrypterErrorCode {

	DECRYPT_01("El archivo no está en el formato correcto"),
	DECRYPT_02("El archivo no tiene una salt válido"),
	DECRYPT_03("El archivo no tiene un iv válido"),
	DECRYPT_04("El archivo no tiene un contenido válido"),
	DECRYPT_05("El archivo no tiene un hash válido"),
	DECRYPT_06("Los hashes no coinciden"),
	DECRYPT_07("La contraseña no es correcta");

	private final String message;

}
