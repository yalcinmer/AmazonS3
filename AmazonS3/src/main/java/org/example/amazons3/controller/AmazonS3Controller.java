package org.example.amazons3.controller;

import org.example.amazons3.service.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("amazon/")
public class AmazonS3Controller {

    @Autowired
    private AmazonS3Service amazonS3Service;

    @GetMapping("/upload")
    public void upload(@RequestParam String mediaUrl) throws IOException {

        List<String> details = getFileDetails(mediaUrl);
        amazonS3Service.createFolder(details.get(2));
        amazonS3Service.upload(details.get(0), details.get(1), mediaUrl);
    }

    @GetMapping("/download")
    public URL download(@RequestParam String mediaUrl) {

        List<String> details = getFileDetails(mediaUrl);
        return amazonS3Service.getObjectUrl(details.get(1));
    }

    private List<String> getFileDetails(String mediaUrl) {

        String folderPath;
        String[] parsedUrl =mediaUrl.split("/");
        String fileName = parsedUrl[parsedUrl.length - 1];
        if(fileName.length() > 32) fileName = fileName.substring(fileName.length() - 32);
        if(mediaUrl.contains(".jpg") || mediaUrl.contains(".png") || mediaUrl.contains(".jpeg")) {
            folderPath = "image/";
        } else if(mediaUrl.contains(".mp4")) {
            folderPath = "video/";
        } else {
            folderPath = "other/";
        }

        String filePath = folderPath + fileName;
        return List.of(fileName, filePath, folderPath);
    }
}
