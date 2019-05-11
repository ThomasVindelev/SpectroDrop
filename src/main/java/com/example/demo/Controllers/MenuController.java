package com.example.demo.Controllers;

import com.example.demo.Services.MessageService;
import com.example.demo.Services.TaskService;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class MenuController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/customerMain/{id}")
    public String getCustomerMenu(@PathVariable("id") int id, Model model) {
        model.addAttribute("employeeList", userService.getUsers("Employees"));
        model.addAttribute("messageList", messageService.getMessages(id));
        return "customerMain";
    }

    @GetMapping("/employeeMain/{id}")
    public String getEmployeeMenu(@PathVariable("id") int id, Model model, HttpSession session) {
        model.addAttribute("roleList", userService.getRoles());
        model.addAttribute("messageList", messageService.getMessages(id));
        model.addAttribute("newTasks", taskService.getTasks(true, false, id));
        model.addAttribute("employeeList", userService.getUsers("Employees"));
        model.addAttribute("customerList", userService.getUsers("Customers"));
        return "employeeMain";
    }

    @GetMapping("/customerMain/{id}/tasks")
    public String getAllTasks(@PathVariable("id") int id, Model model, HttpSession session) {
        model.addAttribute("taskList", taskService.getTasks(false, true, id));
        return "tasks";
    }

}