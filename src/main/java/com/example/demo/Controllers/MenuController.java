package com.example.demo.Controllers;

import com.example.demo.Services.MessageService;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MenuController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/main/{id}")
    public String getMainMenu(@PathVariable("id") int id, Model model) {
        model.addAttribute("employeeList", userService.getEmployees());
        model.addAttribute("messageList", messageService.getMessages(id));
        return "main";
    }

    @GetMapping("/employeeMain/{id}")
    public String getEmployeeMenu(@PathVariable("id") int id, Model model) {
        model.addAttribute("roleList", userService.getRoles());
        model.addAttribute("messageList", messageService.getMessages(id));
        return "employee";
    }

}
