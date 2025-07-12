package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    // 1. Register new account
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.badRequest().body("");
        }

        Optional<Account> createdAccount = accountService.register(account);
        return createdAccount.isPresent()
                ? ResponseEntity.ok(createdAccount.get())
                : ResponseEntity.status(409).body(""); // username already exists
    }

    // 2. Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        Optional<Account> found = accountService.login(account.getUsername(), account.getPassword());
        return found.isPresent()
                ? ResponseEntity.ok(found.get())
                : ResponseEntity.status(401).body("");
    }

    // 3. Create a new message
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() ||
            message.getMessageText().length() > 255 ||
            !accountService.existsById(message.getPostedBy())) {
            return ResponseEntity.badRequest().body("");
        }

        Message created = messageService.createMessage(message);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/messages/{messageId}")
public ResponseEntity<?> deleteMessageById(@PathVariable int messageId) {
    int rowsAffected = messageService.deleteMessageById(messageId);
    if (rowsAffected == 1) {
        return ResponseEntity.ok(1); // message existed and was deleted
    } else {
        return ResponseEntity.ok().build(); // message not found – 200 with empty body
    }
}

@GetMapping("/messages")
public ResponseEntity<List<Message>> getAllMessages() {
    List<Message> messages = messageService.getAllMessages();
    return ResponseEntity.ok(messages); // Always returns 200 with list (can be empty)
}

@GetMapping("/messages/{messageId}")
public ResponseEntity<?> getMessageById(@PathVariable int messageId) {
    Optional<Message> message = messageService.getMessageById(messageId);
    return message.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.ok().build()); // 200 with empty body
}

@PatchMapping("/messages/{messageId}")
public ResponseEntity<?> updateMessage(@PathVariable int messageId, @RequestBody Map<String, String> body) {
    String newText = body.get("messageText");
    int result = messageService.updateMessage(messageId, newText);

    if (result == 1) {
        return ResponseEntity.ok(result); // success
    } else if (result == 0) {
        return ResponseEntity.badRequest().build(); // message not found
    } else {
        return ResponseEntity.badRequest().build(); // invalid text
    }
}


}


