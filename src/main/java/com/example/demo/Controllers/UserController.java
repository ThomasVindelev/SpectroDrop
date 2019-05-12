package com.example.demo.Controllers;

import com.example.demo.Models.User;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/newUser")
    public String newUser(@ModelAttribute User user, RedirectAttributes redirectAttributes, HttpSession session) {
        redirectAttributes.addFlashAttribute("message", userService.newUser(user));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("TheUser", userService.getUserById(id));
        return "editUser";
    }

    @PostMapping("/editUser")
    public String editUser(@ModelAttribute User user, RedirectAttributes redirectAttributes, HttpSession session) {
        System.out.println(user.getId());
        System.out.println(user.getFirstName());
        redirectAttributes.addFlashAttribute("Edit", userService.updateUser(user));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id, RedirectAttributes redirectAttributes, HttpSession session) {
        System.out.println(id);
        redirectAttributes.addFlashAttribute("Delete", userService.deleteUserById(id));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

}