package com.example.demo.Controllers;

import com.example.demo.Models.Task;
import com.example.demo.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping
    public String newTask(@ModelAttribute Task task) {
        taskService
    }

}
