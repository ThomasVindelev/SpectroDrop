package com.example.demo.Repositories;

import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class FileRepository implements CloseHelper {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private String query;
    private boolean isError;

    /**
     * Tillader reetablering af connection til vores database efter denne bliver lukket.
     */

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://spectrodb.cbiha888el7r.eu-central-1.rds.amazonaws.com/SpectroDB?useSSL=false&autoReconnect=true",
                    "SpectroDB",
                    "SpectroDB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getFilesByTask(int id) {
        connection = getConnection();
        query = "SELECT name FROM SpectroDB.Files WHERE fk_tasks = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
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
            closeConnections(preparedStatement, connection);
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
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
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
            closeConnections(preparedStatement, connection);
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnections(PreparedStatement preparedStatement, Connection connection) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnections(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.preparedStatement != null) {
            try {
                this.preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /*public FileRepository() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://spectrodb.cbiha888el7r.eu-central-1.rds.amazonaws.com/SpectroDB",
                    "SpectroDB",
                    "SpectroDB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

}
