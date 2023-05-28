package com.icesi.cybersecurity.fileencrypter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedFileResponse {

    String status;
    String secretKey;
    String iv;
    String hashOriginalFile;
    String hashEncryptedFile;
    String content;

}
