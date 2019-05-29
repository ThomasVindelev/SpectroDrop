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
    private LoginService loginService;

    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

	@PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, Model model) {
        System.out.println("error 1");
        if (loginService.verify(user)) {
            System.out.println("error 2");
            session.setAttribute("id", user.getId());
            session.setAttribute("role", user.getFk_roles());
            if (user.isActive()) {
                System.out.println("error 3");
                if (user.getFk_roles() == 1) {
                    System.out.println("error 4");
                    return "redirect:/employeeMain/" + user.getId();
                } else {
                    return "redirect:/customerMain/" + user.getId();
                }
            } else {
                return "welcome";
            }
        } else {
            model.addAttribute("invalid", true);
            return "index";
        }
    }

    /**
	 * 
	 * @param session
	 */
	@PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    /**
	 * 
	 * @param password
	 * @param passWordConfirm
	 * @param model
	 * @param session
	 */
	@PostMapping("/newLogin")
    public String newLogin(@ModelAttribute("password") String password, @ModelAttribute("passwordConfirm") String passWordConfirm, Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("id");
        Integer role = (Integer) session.getAttribute("role");
        if (loginService.activateUser(password, passWordConfirm, userId)) {
            if (role == 1) {
                return "redirect:/employeeMain/" + userId;
            } else {
                return "redirect:/customerMain/" + userId;
            }
        } else {
            model.addAttribute("invalid", true);
            return "welcome";
        }
    }


}
