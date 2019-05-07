package com.example.demo.Controllers;

import com.example.demo.Models.User;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/newUser")
    public String newUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", userService.newUser(user));
        return "redirect:/employeeMain/" + user.getId();
    }

}
