package com.example.demo.Controllers;

import com.example.demo.Models.Message;
import com.example.demo.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/newMessage")
    public String newMessage(@ModelAttribute Message message) {
        messageService.newMessage(message);
        return "redirect:/main";
    }

}
