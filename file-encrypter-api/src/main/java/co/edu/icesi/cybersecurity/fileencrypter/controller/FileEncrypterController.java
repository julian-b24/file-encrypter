package co.edu.icesi.cybersecurity.fileencrypter.controller;

import co.edu.icesi.cybersecurity.fileencrypter.api.FileEncrypterAPI;
import co.edu.icesi.cybersecurity.fileencrypter.service.FileEncrypterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileEncrypterController implements FileEncrypterAPI {

	private final FileEncrypterService encryptFileEncrypterService;

	@Override
	public String encrypt(MultipartFile file, String password) {
		return encryptFileEncrypterService.encrypt(file, password);
	}

	@Override
	public String decrypt(MultipartFile file, String password) {
		return encryptFileEncrypterService.decrypt(file, password);
	}

}
