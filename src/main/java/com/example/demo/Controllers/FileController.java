package com.example.demo.Controllers;

import com.example.demo.Services.AmazonClient;
import com.example.demo.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String uploadFile(@RequestPart(value = "file") MultipartFile file, @ModelAttribute("id") int id) {
        String name = fileService.addFileToTask(id, file.getOriginalFilename());
        amazonClient.uploadFile(file, name);
        return "redirect:/taskInfo/" + id;
    }

    @PostMapping("/downloadFile/{name}")
    public String downloadFile(@PathVariable("name") String name, @ModelAttribute("id") int id) {
        amazonClient.downloadFile(name);
        return "redirect:/taskInfo/" + id;
    }

    @PostMapping("/deleteFile/{name}")
    public String deleteFile(@PathVariable(value = "name") String fileName, @ModelAttribute("id") int id) {
        amazonClient.deleteFileFromS3Bucket(fileName);
        fileService.deleteFile(fileName);
        return "redirect:/taskInfo/" + id;
    }
}