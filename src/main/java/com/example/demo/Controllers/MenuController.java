package com.example.demo.Controllers;

import com.example.demo.Services.MessageService;
import com.example.demo.Services.TaskService;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class MenuController implements ErrorController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private TaskService taskService;

    /**
     * Overfører kunde til deres html ved successfull login
     */

    @GetMapping("/customerMain/{id}")
    public String getCustomerMenu(@PathVariable("id") int id, Model model, HttpSession session) {
        Integer roleId = (Integer) session.getAttribute("role");
        if (roleId == null) {
            session.invalidate();
            return "index";
        }
        model.addAttribute("employeeList", userService.getUsers("Employees"));
        model.addAttribute("messageList", messageService.getMessages(id, true));
        model.addAttribute("newTasks", taskService.getTasks(true, true, id));
        return "customerMain";
    }

    /**
     * Overfører ansat til deres html ved successfull login
     */

    @GetMapping("/employeeMain/{id}")
    public String getEmployeeMenu(@PathVariable("id") int id, Model model, HttpSession session) {
        Integer roleId = (Integer) session.getAttribute("role");
        if (roleId == null) {
            session.invalidate();
            return "index";
        }
        System.out.println("error 5");
        model.addAttribute("roleList", userService.getRoles());
        System.out.println("error 6");
        model.addAttribute("messageList", messageService.getMessages(id, true));
        System.out.println("error 7");
        model.addAttribute("newTasks", taskService.getTasks(true, false, id));
        System.out.println("error 8");
        model.addAttribute("userList", userService.getUsers("All"));
        System.out.println("error 9");
        model.addAttribute("newUsers", userService.getUsers("New"));
        System.out.println("error 10");
        model.addAttribute("statusList", taskService.getStatus());
        System.out.println("error 11");
        return "employeeMain";
    }


    private final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error(HttpSession session, Error error, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("error", error.getMessage());
        return "errorHandling";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    /*@GetMapping("/customerMain/{id}/tasks")
    public String getAllTasks(@PathVariable("id") int id, Model model, HttpSession session) {
        Integer roleId = (Integer) session.getAttribute("role");
        if (roleId == null) {
            session.invalidate();
            return "index";
        }
        model.addAttribute("taskList", taskService.getTasks(false, true, id));
        return "tasks";
    }*/

    //https://stackoverflow.com/questions/25356781/spring-boot-remove-whitelabel-error-page
}