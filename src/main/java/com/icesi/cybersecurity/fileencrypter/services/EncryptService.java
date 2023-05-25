package com.icesi.cybersecurity.fileencrypter.services;

import org.springframework.web.multipart.MultipartFile;

public interface EncryptService {

    String encryptFile(MultipartFile file, String password);

    void decryptFile();

}
