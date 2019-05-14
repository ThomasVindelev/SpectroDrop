package com.example.demo.Controllers;

import com.example.demo.Services.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
public class FileController {

    private AmazonClient amazonClient;
    @Autowired
    FileController(AmazonClient amazonClient) {
        this.amazonClient =amazonClient;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file, HttpSession session) {
        System.out.println("hello");
        System.out.println(amazonClient.uploadFile(file));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile (@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket (fileUrl);
    }
}