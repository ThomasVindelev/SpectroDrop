package com.example.demo.Services;

import com.example.demo.Models.Status;
import com.example.demo.Models.Task;
import com.example.demo.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public boolean newTask(Task task) {
        if (!taskRepository.newTask(task)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean editTask(Task task) {
        if (!taskRepository.editTask(task)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteTask(int id) {
        if (!taskRepository.deleteTask(id)) {
            return true;
        } else {
            return false;
        }
    }

    public List<Task> getTasks(boolean getNew, boolean isCustomer, int id) {
        ResultSet resultSet = taskRepository.getTasks(getNew, isCustomer, id);
        List<Task> taskList = new ArrayList<>();
        try {
            while(resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getInt("id_tasks"));
                task.setFk_customer(resultSet.getInt("fk_customer"));
                task.setFk_employee(resultSet.getInt("fk_employee"));
                task.setCustomer(resultSet.getString("customer.username"));
                task.setEmployee(resultSet.getString("employee.username"));
                task.setFk_status(resultSet.getInt("fk_status"));
                task.setStatus(resultSet.getString("Status.name"));
                task.setTaskName(resultSet.getString("Tasks.name"));
                taskList.add(task);
            }
            return taskList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Task getTaskById(int id) {
        ResultSet resultSet = taskRepository.getTaskById(id);
        try {
            Task task = new Task();
            while (resultSet.next()) {
                task.setId(resultSet.getInt("id_tasks"));
                task.setCustomer(resultSet.getString("customer.username"));
                task.setEmployee(resultSet.getString("employee.username"));
                task.setFk_status(resultSet.getInt("fk_status"));
                task.setStatus(resultSet.getString("Status.name"));
                task.setTaskName(resultSet.getString("Tasks.name"));
            }
            return task;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Status> getStatus() {
        ResultSet resultSet = taskRepository.getStatus();
        List<Status> statusList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Status status = new Status();
                status.setId(resultSet.getInt("id_status"));
                status.setName(resultSet.getString("name"));
                statusList.add(status);
            }
            return statusList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}
