package com.icesi.cybersecurity.fileencrypter.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface EncryptAPI {

    @PostMapping("/encrypt/{filename}")
    void encryptFile();

    @PostMapping("/decrypt/{filename}")
    void decryptFile();

}
