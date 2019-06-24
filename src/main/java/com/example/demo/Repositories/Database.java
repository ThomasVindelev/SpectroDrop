package com.example.demo.Repositories;

import java.sql.*;

public abstract class Database {

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

    public void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(Connection connection, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
