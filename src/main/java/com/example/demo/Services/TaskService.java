package com.example.demo.Services;

import com.example.demo.Models.Status;
import com.example.demo.Models.Task;
import com.example.demo.Repositories.FileRepository;
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

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AmazonClient amazonClient;

    public boolean newTask(Task task) {
        return taskRepository.newTask(task);
    }

    public boolean editTask(Task task) {
        return taskRepository.editTask(task);
    }

    public boolean deleteTask(int id) {
        ResultSet resultSet = fileRepository.getFilesByTask(id);
        try {
            while (resultSet.next()) {
                fileRepository.deleteFile(resultSet.getString("name"));
                amazonClient.deleteFileFromS3Bucket(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
        return taskRepository.deleteTask(id);
    }

    public List<Task> getTasks(boolean getNew, boolean isCustomer, int id) {
        ResultSet resultSet = taskRepository.getTasks(getNew, isCustomer, id);
        List<Task> taskList = new ArrayList<>();
        try {
            while (resultSet.next()) {
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

    public boolean taskResponsibility(int userId, int roleId) {
        if (roleId == 2) {
            ResultSet tasks = taskRepository.getTaskByUser(userId);
            List<String> fileNames = new ArrayList<>();
            int taskId;
            try {
                while (tasks.next()) {
                    taskId = tasks.getInt("id_tasks");
                    System.out.println(taskId);
                    ResultSet files = fileRepository.getFilesByTask(taskId);
                    while (files.next()) {
                        fileNames.add(files.getString("name"));
                    }
                }
                taskRepository.deleteTaskByUser(userId);
                for (String fileName : fileNames) {
                    amazonClient.deleteFileFromS3Bucket(fileName);
                }
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return taskRepository.autoTransferResponsibility(userId);
        }
        return true;
    }
}