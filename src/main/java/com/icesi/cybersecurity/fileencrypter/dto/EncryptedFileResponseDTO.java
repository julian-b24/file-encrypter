package com.icesi.cybersecurity.fileencrypter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedFileResponseDTO {

    String status;
    String secretKey;
    String iv;
    String hashOriginalFile;
    String hashEncryptedFile;
    String content;

}
