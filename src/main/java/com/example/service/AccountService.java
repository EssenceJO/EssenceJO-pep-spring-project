package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> register(Account account) {
        return accountRepository.findByUsername(account.getUsername()).isEmpty()
            ? Optional.of(accountRepository.save(account))
            : Optional.empty();
    }

    public Optional<Account> login(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password);
    }

    public boolean existsById(int id) {
        return accountRepository.existsById(id);
    }
}
