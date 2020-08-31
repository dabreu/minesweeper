package com.minesweeper.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.minesweeper.model.User;
import com.minesweeper.repository.UserRepository;
import com.minesweeper.security.InvalidLoginException;
import com.minesweeper.security.LoginInfo;
import com.minesweeper.security.PasswordAuthenticator;
import com.minesweeper.security.UserAlreadyExistsException;

@Service
public class UserService {

    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers a new user
     * 
     * @param username
     * @param password
     * @throws Exception
     */
    public void register(String username, String password) {
        validateParameters(username, password);
        validateUserNotExist(username);
        try {
            User user = new User(username, PasswordAuthenticator.hashPassword(password));
            repository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + username);
        }
    }

    private void validateParameters(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Invalid username");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    private void validateUserNotExist(String username) {
        Optional<User> user = repository.findById(username);
        if (user.isPresent()) {
            throw new UserAlreadyExistsException();
        }
    }

    public LoginInfo login(String username, String password) {
        User user = repository.findById(username).orElseThrow(() -> new InvalidLoginException());
        try {
            PasswordAuthenticator.checkPassword(user.getPassword(), password);
            String token = PasswordAuthenticator.generateNewToken();
            user.setActiveToken(token);
            repository.save(user);
            return new LoginInfo(token);
        } catch (Exception e) {
            throw new InvalidLoginException();
        }
    }
}
