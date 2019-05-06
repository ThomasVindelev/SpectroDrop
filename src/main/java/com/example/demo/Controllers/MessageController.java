package com.example.demo.Controllers;

import com.example.demo.Models.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MessageController {

    @PostMapping("/newMessage")
    public String newMessage(@ModelAttribute Message message) {
        return null;
    }

}
