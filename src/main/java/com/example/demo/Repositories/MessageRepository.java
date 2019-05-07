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

    public void newMessage(Message message) {
        query = "INSERT INTO Messages (text, fk_sent_to, fk_sent_from) VALUES (?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, message.getText());
            preparedStatement.setInt(2, message.getSent_to());
            preparedStatement.setInt(3, message.getSent_from());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getMessagesByUser(int id) {
        query = "SELECT * FROM Messages WHERE fk_sent_to = ? OR fk_sent_from = ? ORDER BY id DESC";
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

}
