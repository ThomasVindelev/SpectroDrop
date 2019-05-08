package com.example.demo.Controllers;

import com.example.demo.Services.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping ("/storage/")
public class FileController {
    private AmazonClient amazonClient;
    private static final String PATH = "/error";

    @Autowired
    FileController(AmazonClient amazonClient) {
        this.amazonClient =amazonClient;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestPart(value = "file")
                             MultipartFile file) {
        System.out.println("testing in progress");
        return this.amazonClient.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile (@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket (fileUrl);

    }

    @RequestMapping(value = PATH)
    public String error() {
        return "Error handling";

    }
    @GetMapping("/test")
    public void test() {
        this.amazonClient.downloadFile("sadfatcat.jpg");
    }
}
