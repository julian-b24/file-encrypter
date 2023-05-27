package com.icesi.cybersecurity.fileencrypter.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Object upload(MultipartFile multipartFile);

    Object download(String fileName, String destinyFolder);

}
