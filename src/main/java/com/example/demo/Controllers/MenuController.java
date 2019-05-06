package com.example.demo.Controllers;

import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @Autowired
    UserService userService;

    @GetMapping("/main")
    public String getMainMenu(Model model) {
        model.addAttribute("employeeList", userService.getEmployees());
        return "main";
    }

}
