package com.icesi.cybersecurity.fileencrypter.services.impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.icesi.cybersecurity.fileencrypter.services.FileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final String BUCKET_NAME = "file-encryption-e3522.appspot.com";
    private final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/file-encryption-e3522/o/%s?alt=media";

    private final ClassPathResource GOOGLE_CREDENTIALS = new ClassPathResource("keys/credentials.json");

    @Override
    public Object upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            //fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

            File file = convertToFile(multipartFile, fileName);
            String url = uploadFile(file, fileName);
            return "Successfully Uploaded ! " + url;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unsuccessfully Uploaded!";
        }
    }

    @SneakyThrows
    @Override
    public Object download(String fileName, String destinyFolder) {
        String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
        String destFilePath = destinyFolder + "\\" + fileName;
        Credentials credentials = GoogleCredentials.fromStream(GOOGLE_CREDENTIALS.getInputStream());
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of(BUCKET_NAME, fileName));
        blob.downloadTo(Paths.get(destFilePath));
        return "200" + " Successfully Downloaded!";
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(GOOGLE_CREDENTIALS.getInputStream());
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

}
