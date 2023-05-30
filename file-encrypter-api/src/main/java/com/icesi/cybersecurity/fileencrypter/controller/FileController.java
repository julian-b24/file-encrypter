package com.icesi.cybersecurity.fileencrypter.controller;

import com.icesi.cybersecurity.fileencrypter.api.FileAPI;
import com.icesi.cybersecurity.fileencrypter.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
public class FileController implements FileAPI {

    private FileService fileService;

    @Override
    public Object upload(@RequestParam("file") MultipartFile multipartFile) {
        return fileService.upload(multipartFile);
    }

    @Override
    public Object download(@PathVariable String fileName, @RequestParam("destinyFolder") String destinyFolder) {
        return fileService.download(fileName, destinyFolder);
    }

}
