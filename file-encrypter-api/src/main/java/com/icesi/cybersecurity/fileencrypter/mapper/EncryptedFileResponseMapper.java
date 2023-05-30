package com.icesi.cybersecurity.fileencrypter.mapper;

import com.icesi.cybersecurity.fileencrypter.dto.EncryptedFileResponseDTO;
import com.icesi.cybersecurity.fileencrypter.model.EncryptedFileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EncryptedFileResponseMapper {

    EncryptedFileResponse fromDTO(EncryptedFileResponseDTO encryptedFileResponseDTO);
    EncryptedFileResponseDTO fromEncryptedFileResponse(EncryptedFileResponse encryptedFileResponse);

}
