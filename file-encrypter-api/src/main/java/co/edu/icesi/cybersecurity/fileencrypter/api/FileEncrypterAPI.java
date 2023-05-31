package co.edu.icesi.cybersecurity.fileencrypter.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
public interface FileEncrypterAPI {

	@PostMapping("/encrypt")
	String encrypt(@RequestParam("file") MultipartFile file, @RequestParam("password") String password);

	@PostMapping("/decrypt")
	String decrypt(@RequestParam("file") MultipartFile file, @RequestParam("password") String password);

}
