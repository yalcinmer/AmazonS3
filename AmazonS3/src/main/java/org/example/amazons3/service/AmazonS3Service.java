package org.example.amazons3.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    @Value("${amazon.s3.bucketName}")
    private String bucketName;

    @Value("${windows.file.path}")
    private String windowsFilePath;

    private final S3Client s3Client;

    public void upload(String fileName, String filePath, String postUrl) throws IOException {

        URL url = new URL(postUrl);
        File file = new File(windowsFilePath + fileName);
        URLConnection connection = url.openConnection();

        FileUtils.writeByteArrayToFile(file, connection.getInputStream().readAllBytes());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(filePath).build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
        file.deleteOnExit();
    }

    public URL getObjectUrl(String fileName) {

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest);
    }

    public void createFolder(String folderName) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(folderName)
                .acl("public-read")
                .build();
        s3Client.putObject(request, RequestBody.empty());
    }
}
