package com.icesi.cybersecurity.fileencrypter.controller;

import com.icesi.cybersecurity.fileencrypter.api.EncryptAPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class EncryptController implements EncryptAPI {

    @Override
    public void encryptFile() {

    }

    @Override
    public void decryptFile() {

    }
}
