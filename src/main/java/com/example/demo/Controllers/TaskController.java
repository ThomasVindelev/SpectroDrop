package com.example.demo.Controllers;

import com.example.demo.Models.Task;
import com.example.demo.Services.FileService;
import com.example.demo.Services.TaskService;
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
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @PostMapping("/newTask")
    public String newTask(@ModelAttribute Task task, HttpSession session, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("taskError", taskService.newTask(task));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @GetMapping("/taskInfo/{id}")
    public String getTaskInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("TheTask", taskService.getTaskById(id));
        model.addAttribute("Files", fileService.getFilesByTask(id));
        return "task";
    }

    @PostMapping("/editTask")
    public String editTask(@ModelAttribute Task task, HttpSession session, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("editTaskError", taskService.editTask(task));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @PostMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable("id") int id, HttpSession session, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("deleteTaskError", taskService.deleteTask(id));
        Integer userId = (Integer) session.getAttribute("id");
        return "redirect:/employeeMain/" + userId;
    }

    @GetMapping("/viewAllTasks/{id}")
    public String getAllTasks(@PathVariable("id") int id, Model model) {
        if (id == 1) {
            model.addAttribute("taskList", taskService.getTasks(false, false, id));
        } else {
            model.addAttribute("taskList", taskService.getTasks(false, true, id));
        }
        model.addAttribute("userList", userService.getUsers("All"));
        model.addAttribute("statusList", taskService.getStatus());
        return "viewAllTasks";
    }

}
