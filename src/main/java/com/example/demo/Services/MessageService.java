package com.example.demo.Services;

import com.example.demo.Models.Message;
import com.example.demo.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void newMessage(Message message) {
        messageRepository.newMessage(message);
    }

    public List<Message> getMessages(int id) {
        ResultSet resultSet = messageRepository.getMessagesByUser(id);
        List<Message> messageList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Message message = new Message();
                message.setId(resultSet.getInt("id_messages"));
                message.setText(resultSet.getString("text"));
                message.setSent_from(resultSet.getString("username"));
                messageList.add(message);
            }
            return messageList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
