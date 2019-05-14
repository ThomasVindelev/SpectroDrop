package com.example.demo.Controllers;

import com.example.demo.Models.User;
import com.example.demo.Services.AmazonClient;
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
    private LoginService loginService;

    @Autowired
    private AmazonClient amazonClient;

    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, Model model) {
        if (loginService.verify(user)) {
            session.setAttribute("id", user.getId());
            session.setAttribute("role", user.getFk_roles());
            if (user.getFk_roles() == 1) {
                return "redirect:/employeeMain/" + user.getId();
            } else {
                return "redirect:/customerMain/" + user.getId();
            }
        } else {
            model.addAttribute("invalid", true);
            return "index";
        }
    }

    @PostMapping("/createBucket")
    public String createBucket() {
        amazonClient.createBucket();
        return "redirect:/employeeMain/" + 1;
    }

}
