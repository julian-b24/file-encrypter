package com.icesi.cybersecurity.fileencrypter.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/file")
public interface FileAPI {

    @PostMapping("/upload")
    Object upload(@RequestParam("file") MultipartFile multipartFile);

    @GetMapping("/download/{fileName}")
    Object download(@PathVariable String fileName, @RequestParam("destinyFolder") String destinyFolder);

}
