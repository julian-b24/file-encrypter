package com.icesi.cybersecurity.fileencrypter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DecryptedFileResponse {

    String status;
    String hashEncryptedFile;
    String hashDecryptedFile;


}
