package com.example.demo.Controllers;

import com.example.demo.Services.AmazonClient;
import com.example.demo.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class FileController {

    private AmazonClient amazonClient;

    @Autowired
    FileController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @Autowired
    private FileService fileService;

    /**
     * Sørger for at uploade enkelte filer, som bliver valgt af enten kunde eller medarbejder
     */

    @PostMapping("/upload")
    public String uploadFile(@RequestPart("file") MultipartFile file, @ModelAttribute("id") int id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("uploadInvalid", fileService.addFileToTask(id, file.getOriginalFilename(), file));
        return "redirect:/taskInfo/" + id;
    }

    /*HttpServletRequest request*/

    @PostMapping("/downloadFile/{name}")
    public /* String */ void downloadFile(HttpServletResponse response, @PathVariable("name") String name, @ModelAttribute("id") int id) throws IOException {
        amazonClient.downloadFile(name, response);
        response.getOutputStream().close();
        // return "redirect:/taskInfo/" + id;
    }

    /**
     * Sletter filer af bestemt navn fra både S3 og database
     */

    @PostMapping("/deleteFile/{name}")
    public String deleteFile(@PathVariable("name") String fileName, @ModelAttribute("id") int id) {
        amazonClient.deleteFileFromS3Bucket(fileName);
        fileService.deleteFile(fileName);
        return "redirect:/taskInfo/" + id;
    }
      /*@PostMapping("/downloadFile/{name}")
    public String downloadFile(@PathVariable("name") String name, @ModelAttribute("id") int id, RedirectAttributes redirectAttributes) {
        amazonClient.test3(name);
        //redirectAttributes.addFlashAttribute("downloadSuccess", amazonClient.downloadFile(name));
        return "redirect:/taskInfo/" + id;
    }*/
}