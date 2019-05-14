package com.example.demo.Controllers;

import com.example.demo.Services.AmazonClient;
import com.example.demo.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class FileController {

    private AmazonClient amazonClient;
    @Autowired
    FileController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file, @ModelAttribute("id") int id, HttpSession session) {
        fileService.addFileToTask(id, amazonClient.uploadFile(file));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @PostMapping("/downloadFile/{name}")
    public String downloadFile(@PathVariable("name") String name) {
        amazonClient.downloadFile(name);
        return "redirect:/employeeMain/" + 1;
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile (@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket (fileUrl);
    }
}