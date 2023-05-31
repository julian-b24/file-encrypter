package com.icesi.cybersecurity.fileencrypter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecryptedFileResponseDTO {

    String status;
    boolean hashComparison;

}
