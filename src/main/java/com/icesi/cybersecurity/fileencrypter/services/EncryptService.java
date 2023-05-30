package com.icesi.cybersecurity.fileencrypter.services;

import com.icesi.cybersecurity.fileencrypter.model.DecryptedFileResponse;
import com.icesi.cybersecurity.fileencrypter.model.EncryptedFileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface EncryptService {

    EncryptedFileResponse encryptFile(MultipartFile file, String password,String outputFilePath);

    DecryptedFileResponse decryptFile(MultipartFile file, String key, String iv, String outputFilePath);

}
