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

    public boolean editTask(Task task) {
        query = "UPDATE SpectroDB.Tasks SET fk_customer = ?, fk_employee = ?, fk_status = ?, name = ? WHERE id_tasks = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, task.getFk_customer());
            preparedStatement.setInt(2, task.getFk_employee());
            preparedStatement.setInt(3, task.getFk_status());
            preparedStatement.setString(4, task.getTaskName());
            preparedStatement.setInt(5, task.getId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteTask(int id) {
        query = "DELETE FROM SpectroDB.Tasks WHERE id_tasks = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void deleteTaskByUser(int id) {
        query = "DELETE FROM SpectroDB.Tasks WHERE fk_customer = ? OR fk_employee = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getTasks(boolean getNew, boolean isCustomer, int id) {
        query = "SELECT * FROM Tasks " +
                "INNER JOIN Users customer ON fk_customer = customer.id_users " +
                "INNER JOIN Users employee ON fk_employee = employee.id_users " +
                "INNER JOIN Status ON fk_status = id_status ";
        if (isCustomer) {
            query += "WHERE fk_customer = " + id + " ";
        } else if (getNew) {
            query += "ORDER BY id_tasks DESC LIMIT 5";
        }
        try {
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ResultSet getTaskById(int id) {
        query = "SELECT * FROM Tasks " +
                "INNER JOIN Users customer ON fk_customer = customer.id_users " +
                "INNER JOIN Users employee ON fk_employee = employee.id_users " +
                "INNER JOIN Status ON fk_status = id_status WHERE id_tasks = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getTaskByUser(int id) {
        query = "SELECT id_tasks From SpectroDB.Tasks WHERE fk_customer = ? OR fk_employee = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getStatus() {
        query = "SELECT * FROM SpectroDB.Status";
        try {
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean autoTransferResponsibility(int userId) {
        query = "UPDATE SpectroDB.Tasks SET fk_employee = ? WHERE fk_employee = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, 39);
            preparedStatement.setInt(2, userId);
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}