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
        redirectAttributes.addFlashAttribute("userCreationError", userService.newUser(user));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @PostMapping("/editUser")
    public String editUser(@ModelAttribute User user, RedirectAttributes redirectAttributes, HttpSession session) {
        redirectAttributes.addFlashAttribute("editUserError", userService.verifyUpdate(user));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id, @ModelAttribute("role") int roleId, RedirectAttributes redirectAttributes, HttpSession session) {
        redirectAttributes.addFlashAttribute("deleteUserError", userService.deleteUserById(id, roleId));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @GetMapping("/viewAllUsers")
    public String viewAllUsers(Model model) {
        model.addAttribute("userList", userService.getUsers("All"));
        model.addAttribute("roleList", userService.getRoles());
        return "viewAllUsers";
    }

    @PostMapping("/newPassword")
    public String newPassword(
            @ModelAttribute("oldPassword") String oldPassword,
            @ModelAttribute("newPassword") String newPassword,
            @ModelAttribute("newPasswordValidation") String newPasswordValidation,
            HttpSession session, RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("id");
        Integer roleId = (Integer) session.getAttribute("role");
        redirectAttributes.addFlashAttribute("passwordError", userService.changePassword(oldPassword, newPassword, newPasswordValidation, userId));
        if (roleId == 1) {
            return "redirect:/employeeMain/" + userId;
        } else {
            return "redirect:/customerMain/" + userId;
        }
    }
}