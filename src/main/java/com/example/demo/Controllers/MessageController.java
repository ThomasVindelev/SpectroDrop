package com.example.demo.Controllers;

import com.example.demo.Models.Message;
import com.example.demo.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/newMessage")
    public String newMessage(@ModelAttribute Message message, HttpSession session) {
        messageService.newMessage(message);
        Integer userId = (Integer) session.getAttribute("id");
        Integer roleId = (Integer) session.getAttribute("role");
        if (roleId == 1) {
            return "redirect:/employeeMain/" + userId;
        } else {
            return "redirect:/customerMain/" + userId;
        }

    }

}
