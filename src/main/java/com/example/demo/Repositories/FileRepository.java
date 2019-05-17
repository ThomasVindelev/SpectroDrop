package com.example.demo.Repositories;

import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class FileRepository {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private String query;

    public FileRepository() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://spectrodb.cbiha888el7r.eu-central-1.rds.amazonaws.com/SpectroDB",
                    "SpectroDB",
                    "SpectroDB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getFilesByTask(int id) {
        query = "SELECT * FROM SpectroDB.Files WHERE fk_tasks = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addFileToTask(int id, String name) {
        query = "INSERT into SpectroDB.Files (fk_tasks, name) VALUES (?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String name) {
        query = "DELETE FROM SpectroDB.Files WHERE name = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
