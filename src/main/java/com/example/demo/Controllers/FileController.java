package com.example.demo.Controllers;

import com.example.demo.Services.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/*@Controller
public class FileController {

    public static String uploadDirectory = System.getProperty("user.dir");

    @PostMapping("/upload")
    public String uploadFile() {
        return null;
    }*/

}

@RestController
@RequestMapping ("/storage/")
public class BucketController {
    private AmazonClient amazonClient;
    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient =amazonClient;
    }

    @PostMapping("uploadFile")
    public String uploadFile(@RequestPart(value = "file")
                             MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile (@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket (fileUrl);
    }
}