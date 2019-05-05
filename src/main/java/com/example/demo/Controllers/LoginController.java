package com.example.demo.Controllers;

import com.example.demo.Models.User;
import com.example.demo.Services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    @PostMapping("/login")
    public String login(HttpSession session, @ModelAttribute User user, Model model) {
        if () {

        }
    }

}
