package com.example.demo.Services;

import com.example.demo.Models.Message;
import com.example.demo.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public void newMessage(Message message) {
        messageRepository.newMessage(message);
    }

}
