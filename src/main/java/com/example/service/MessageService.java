package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Create a new message
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Get message by ID
    public Optional<Message> getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }

   public int deleteMessageById(int id) {
    if (messageRepository.existsById(id)) {
        messageRepository.deleteById(id);
        return 1; 
    }
    return 0; // message not found
}


   public int updateMessage(int id, String newText) {
    if (newText == null || newText.isBlank() || newText.length() > 255) {
        return -1; // invalid input
    }

    Optional<Message> messageOpt = messageRepository.findById(id);
    if (messageOpt.isPresent()) {
        Message message = messageOpt.get();
        message.setMessageText(newText);
        messageRepository.save(message);
        return 1;
    } else {
        return 0; // message not found
    }
}


    // Get messages by user ID (accountId)
    public List<Message> getMessagesByUser(int postedBy) {
        return messageRepository.findByPostedBy(postedBy);
    }

    

    
}
