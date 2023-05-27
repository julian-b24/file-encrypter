package com.icesi.cybersecurity.fileencrypter.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
public interface EncryptAPI {

    @PostMapping("/encrypt")
    String encryptFile(@RequestParam("file") MultipartFile file, String password);

    @PostMapping("/decrypt")
    String decryptFile(@RequestParam("file") MultipartFile file, String password);

}
