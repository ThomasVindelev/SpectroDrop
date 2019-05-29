package com.example.demo.Repositories;

import com.example.demo.Models.Message;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class MessageRepository implements CloseHelper {

    private PreparedStatement preparedStatement;
    private String query;
    private Connection connection;
    private boolean isError;

    /*public MessageRepository() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://spectrodb.cbiha888el7r.eu-central-1.rds.amazonaws.com/SpectroDB",
                    "SpectroDB",
                    "SpectroDB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

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

    public boolean newMessage(Message message) {
        connection = getConnection();
        query = "INSERT INTO Messages (text, fk_sent_to, fk_sent_from) VALUES (?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, message.getText());
            preparedStatement.setInt(2, message.getFk_sent_to());
            preparedStatement.setInt(3, message.getFk_sent_from());
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public ResultSet getMessages(int userId) {
        connection = getConnection();
        query = "SELECT id_messages, text, fk_sent_from, is_read, username FROM Messages " +
                "INNER JOIN Users ON Messages.fk_sent_from = Users.id_users WHERE fk_sent_to = ? ORDER BY id_messages DESC";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnections(preparedStatement, connection);
        }
        return null;
    }

    public ResultSet getNewMessages(int userId) {
        connection = getConnection();
        query = "SELECT id_messages, text, fk_sent_from, is_read, username FROM Messages " +
                "INNER JOIN Users ON Messages.fk_sent_from = Users.id_users WHERE fk_sent_to = ? AND is_read = ? ORDER BY id_messages DESC LIMIT 5";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setBoolean(2, false);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnections(preparedStatement, connection);
        }
        return null;
    }

    public ResultSet getMessageById(int messageId) {
        connection = getConnection();
        query = "SELECT text, fk_sent_from, username FROM SpectroDB.Messages INNER JOIN Users ON Messages.fk_sent_from = Users.id_users WHERE id_messages = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, messageId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnections(preparedStatement, connection);
        }
        return null;
    }

    public void readMessage(int messageId) {
        connection = getConnection();
        query = "UPDATE SpectroDB.Messages SET is_read = ? WHERE id_messages = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, messageId);
            preparedStatement.execute();
            closeConnections(preparedStatement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnections(preparedStatement, connection);
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
}