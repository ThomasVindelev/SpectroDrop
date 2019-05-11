package com.example.demo.Services;

import com.example.demo.Models.Task;
import com.example.demo.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public boolean newTask(Task task) {
        if (!taskRepository.newTask(task)) {
            return true;
        } else {
            return false;
        }
    }



}
