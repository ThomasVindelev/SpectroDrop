package com.example.demo.Repositories;

import com.example.demo.Models.Task;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            preparedStatement.setInt(1, task.getCustomer());
            preparedStatement.setInt(2, task.getEmployee());
            preparedStatement.setString(3, task.getTaskName());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
