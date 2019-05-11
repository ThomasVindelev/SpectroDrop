package com.example.demo.Controllers;

import com.example.demo.Models.Task;
import com.example.demo.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/newTask")
    public String newTask(@ModelAttribute Task task, HttpSession session) {
        taskService.newTask(task);
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

}
