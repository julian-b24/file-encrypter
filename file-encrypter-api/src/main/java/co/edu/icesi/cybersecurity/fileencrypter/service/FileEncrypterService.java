package co.edu.icesi.cybersecurity.fileencrypter.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileEncrypterService {

	String encrypt(MultipartFile file, String password);

	String decrypt(MultipartFile file, String password);

}
