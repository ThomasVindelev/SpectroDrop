package com.example.demo.Repositories;

import com.example.demo.Models.User;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserRepository {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private String query;

    public UserRepository() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://spectrodb.cbiha888el7r.eu-central-1.rds.amazonaws.com/SpectroDB",
                    "SpectroDB",
                    "SpectroDB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean newUser(User user) {
        query = "INSERT INTO SpectroDB.Users (username, firstname, lastname, email, fk_roles) VALUES (LOWER (?), ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, user.getFk_roles());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ResultSet verifyUserLogin(User user) {
        query = "SELECT * FROM SpectroDB.Users " +
                "INNER JOIN Roles ON Users.fk_roles = Roles.id_roles WHERE username = ? AND password = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getEmployees() {
        query = "SELECT * FROM SpectroDB.Users WHERE fk_roles = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, 1);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getRoles() {
        query = "SELECT * FROM SpectroDB.Roles";
        try {
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet verifyUser(User user) {
        query = "SELECT username FROM SpectroDB.Users WHERE username LIKE ? OR email LIKE ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
