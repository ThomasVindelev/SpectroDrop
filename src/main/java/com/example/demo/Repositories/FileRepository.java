package com.example.demo.Repositories;

import org.springframework.stereotype.Repository;

import java.sql.*;

//Lavet af Marco Pedersen og Thomas Vindelev

@Repository
public class FileRepository extends Database {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private String query;
    private boolean isError;

    public ResultSet getFilesByTask(int id) {
        connection = getConnection();
        query = "SELECT name FROM SpectroDB.Files WHERE fk_tasks = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections();
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet findDuplicate(String name) {
        connection = getConnection();
        query = "SELECT id_files FROM SpectroDB.Files WHERE name = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections();
            e.printStackTrace();
        }
        return null;
    }

    public boolean addFileToTask(int id, String name) {
        connection = getConnection();
        query = "INSERT into SpectroDB.Files (fk_tasks, name) VALUES (?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            isError = preparedStatement.execute();
            closeConnections();
            return isError;
        } catch (SQLException e) {
            closeConnections();
            e.printStackTrace();
        }
        return true;
    }

    public void deleteFile(String name) {
        connection = getConnection();
        query = "DELETE FROM SpectroDB.Files WHERE name = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
            closeConnections();
        } catch (SQLException e) {
            closeConnections();
            e.printStackTrace();
        }
    }

    public void closeConnections() {
        super.closeConnection(this.connection);
    }

    public void closeConnections(ResultSet resultSet) {
        super.closeConnection(this.connection, resultSet);
    }
}
