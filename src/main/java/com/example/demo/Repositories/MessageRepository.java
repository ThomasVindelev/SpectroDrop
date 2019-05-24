package com.example.demo.Repositories;

import com.example.demo.Models.Message;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class MessageRepository {

    //Alle klasser skal tjekkes for eventuel tilbagemelding omkring overførsel af data

    public MessageRepository() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://spectrodb.cbiha888el7r.eu-central-1.rds.amazonaws.com/SpectroDB",
                    "SpectroDB",
                    "SpectroDB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connection;
    private PreparedStatement preparedStatement;
    private String query;

    public boolean newMessage(Message message) {
        query = "INSERT INTO Messages (text, fk_sent_to, fk_sent_from) VALUES (?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, message.getText());
            preparedStatement.setInt(2, message.getFk_sent_to());
            preparedStatement.setInt(3, message.getFk_sent_from());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public ResultSet getMessagesByUser(int id) {
        query = "SELECT id_messages, text, fk_sent_from, is_read, username FROM Messages " +
                "INNER JOIN Users ON Messages.fk_sent_from = Users.id_users WHERE fk_sent_to = ? ORDER BY id_messages DESC LIMIT 5";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getMessageById(int id) {
        query = "SELECT text, fk_sent_from, username FROM SpectroDB.Messages INNER JOIN Users ON Messages.fk_sent_from = Users.id_users WHERE id_messages = " + id + "; " +
                "UPDATE SpectroDB.Messages SET is_read = " + 1 + " WHERE id_messages = " + id;
        try {
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
