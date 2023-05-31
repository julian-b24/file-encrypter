package com.icesi.cybersecurity.fileencrypter.mapper;

import com.icesi.cybersecurity.fileencrypter.dto.DecryptedFileResponseDTO;
import com.icesi.cybersecurity.fileencrypter.model.DecryptedFileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface DecryptedFileResponseMapper {

    DecryptedFileResponse fromDTO(DecryptedFileResponseDTO decryptedFileResponseDTO);
    DecryptedFileResponseDTO fromDecryptedFileResponse(DecryptedFileResponse decryptedFileResponse);

}
