package com.example.demo.Services;

import com.example.demo.Models.Message;
import com.example.demo.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Lavet af Thomas Vindelev

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public boolean newMessage(Message message) {
        return messageRepository.newMessage(message);
    }

    public List<Message> getMessages(int userId, boolean getNew) {
        ResultSet resultSet;
        if (getNew) {
            resultSet = messageRepository.getNewMessages(userId);
        } else {
            resultSet = messageRepository.getMessages(userId);
        }
        List<Message> messageList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Message message = new Message();
                message.setId(resultSet.getInt("id_messages"));
                message.setText(resultSet.getString("text"));
                message.setSent_from(resultSet.getString("username"));
                message.setIsRead(resultSet.getBoolean("is_read"));
                messageList.add(message);
            }
            messageRepository.closeConnections(resultSet);
            return messageList;
        } catch (SQLException e) {
            messageRepository.closeConnections(resultSet);
            e.printStackTrace();
        }
        return null;
    }

    public Message getMessageById(int id, boolean isRead) {
        ResultSet resultSet = messageRepository.getMessageById(id);
        if (!isRead) {
            messageRepository.readMessage(id);
        }
        try {
            Message message = new Message();
            while (resultSet.next()) {
                message.setText(resultSet.getString("text"));
                message.setSent_from(resultSet.getString("username"));
                message.setFk_sent_from(resultSet.getInt("fk_sent_from"));
            }
            messageRepository.closeConnections(resultSet);
            return message;
        } catch (SQLException e) {
            messageRepository.closeConnections(resultSet);
            e.printStackTrace();
        }
        return null;
    }

}
