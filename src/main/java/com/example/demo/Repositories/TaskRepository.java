package com.example.demo.Repositories;

import com.example.demo.Models.Task;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class TaskRepository {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private String query;

    public TaskRepository() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://spectrodb.cbiha888el7r.eu-central-1.rds.amazonaws.com/SpectroDB",
                    "SpectroDB",
                    "SpectroDB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean newTask(Task task) {
        query = "INSERT INTO Tasks (fk_customer, fk_employee, name) VALUES (?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, task.getFk_customer());
            preparedStatement.setInt(2, task.getFk_employee());
            preparedStatement.setString(3, task.getTaskName());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ResultSet getNewTasks() {
        query = "SELECT * FROM Tasks " +
                "INNER JOIN Users a ON fk_customer = a.id_users " +
                "INNER JOIN Users b ON fk_employee = b.id_users " +
                "INNER JOIN Status ON fk_status = id_status ORDER BY Tasks.id_tasks DESC LIMIT 3";
        try {
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}