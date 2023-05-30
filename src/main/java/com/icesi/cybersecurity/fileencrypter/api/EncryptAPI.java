package com.icesi.cybersecurity.fileencrypter.api;

import com.icesi.cybersecurity.fileencrypter.dto.EncryptedFileResponseDTO;
import com.icesi.cybersecurity.fileencrypter.model.DecryptedFileResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
public interface EncryptAPI {

    @PostMapping("/encrypt")
    EncryptedFileResponseDTO encryptFile(@RequestParam("file") MultipartFile file, String password);

    @PostMapping("/decrypt")
    DecryptedFileResponse decryptFile(@RequestParam("file") MultipartFile file, String key, String iv,String outPutFilePath);

}
