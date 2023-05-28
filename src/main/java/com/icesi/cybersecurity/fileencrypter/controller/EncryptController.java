package com.icesi.cybersecurity.fileencrypter.controller;

import com.icesi.cybersecurity.fileencrypter.api.EncryptAPI;
import com.icesi.cybersecurity.fileencrypter.dto.EncryptedFileResponseDTO;
import com.icesi.cybersecurity.fileencrypter.mapper.EncryptedFileResponseMapper;
import com.icesi.cybersecurity.fileencrypter.services.EncryptService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
public class EncryptController implements EncryptAPI {

    private EncryptService encryptService;
    private EncryptedFileResponseMapper encryptedFileResponseMapper;

    @Override
    public EncryptedFileResponseDTO encryptFile(@RequestParam("file") MultipartFile file, String password) {
        return encryptedFileResponseMapper.fromEncryptedFileResponse(encryptService.encryptFile(file, password));
    }

    @Override
    public String decryptFile(@RequestParam("file")MultipartFile file,String password) {
        return encryptService.decryptFile(file, password);
    }
}
