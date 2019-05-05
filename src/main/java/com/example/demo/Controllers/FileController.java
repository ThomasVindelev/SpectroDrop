package com.example.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FileController {

    public static String uploadDirectory = System.getProperty("user.dir");

    @PostMapping("/upload")
    public String uploadFile() {
        return null;
    }

}
