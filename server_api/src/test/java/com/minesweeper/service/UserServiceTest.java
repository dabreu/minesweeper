package com.minesweeper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.minesweeper.model.User;
import com.minesweeper.repository.UserRepository;
import com.minesweeper.security.InvalidLoginException;
import com.minesweeper.security.LoginInfo;
import com.minesweeper.security.PasswordAuthenticator;
import com.minesweeper.security.UserAlreadyExistsException;

public class UserServiceTest {

    private UserRepository repository;
    private UserService service;

    @BeforeEach
    public void before() {
        repository = mock(UserRepository.class);
        service = new UserService(repository);
    }

    @Test
    public void testRegisterUserThrowsErrorIfNoUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.register(null, "password");
        });
        assertTrue(exception.getMessage().contains("Invalid username"));
    }

    @Test
    public void testRegisterUserThrowsErrorIfNoPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.register("username", null);
        });
        assertTrue(exception.getMessage().contains("Invalid password"));
    }

    @Test
    public void testRegisterUserThrowsErrorIfUsernameAlreadyExists() {
        String username = "testusername";
        Optional<User> user = Optional.of(new User(username, "password"));
        when(repository.findById(username)).thenReturn(user);
        assertThrows(UserAlreadyExistsException.class, () -> {
            service.register(username, "password");
        });
    }

    @Test
    public void testRegisterUserSavesChanges() {
        String username = "testuser";
        String password = "testpassword";
        ArgumentCaptor<User> argCaptor = ArgumentCaptor.forClass(User.class);
        service.register(username, password);
        verify(repository, times(1)).save(argCaptor.capture());
        assertEquals(username, argCaptor.getValue().getUsername());
    }

    @Test
    public void testRegisterUserSavesHashedPassword() throws Exception {
        String username = "testuser";
        String password = "testpassword";
        ArgumentCaptor<User> argCaptor = ArgumentCaptor.forClass(User.class);
        service.register(username, password);
        verify(repository, times(1)).save(argCaptor.capture());
        assertEquals(username, argCaptor.getValue().getUsername());
        assertNotEquals(password, argCaptor.getValue().getPassword());
        try {
            PasswordAuthenticator.checkPassword(argCaptor.getValue().getPassword(), password);
        } catch (Exception e) {
            fail("shouldn't have thrown exception");
        }
    }

    @Test
    public void testRegisterUserDoesNotSetToken() {
        String username = "testuser";
        String password = "testpassword";
        ArgumentCaptor<User> argCaptor = ArgumentCaptor.forClass(User.class);
        service.register(username, password);
        verify(repository, times(1)).save(argCaptor.capture());
        assertNull(argCaptor.getValue().getActiveToken());
    }

    @Test
    public void testLoginThrowsErrorIfNotExistingUser() {
        String username = "testusername";
        when(repository.findById(username)).thenReturn(Optional.empty());
        assertThrows(InvalidLoginException.class, () -> {
            service.login(username, "password");
        });
    }

    @Test
    public void testLoginThrowsErrorIfInvalidPassword() throws Exception {
        String username = "testuser";
        String password = "testpassword";
        User user = new User(username, PasswordAuthenticator.hashPassword(password));
        when(repository.findById(username)).thenReturn(Optional.of(user));
        assertThrows(InvalidLoginException.class, () -> {
            service.login(username, "anotherpassword");
        });
    }

    @Test
    public void testLoginReturnsLoginInfoIfUserAuthenticated() throws Exception {
        String username = "testuser";
        String password = "testpassword";
        User user = new User(username, PasswordAuthenticator.hashPassword(password));
        when(repository.findById(username)).thenReturn(Optional.of(user));
        LoginInfo login = service.login(username, password);
        assertNotNull(login.getToken());
    }

    @Test
    public void testLoginSavesTheGeneratedToken() throws Exception {
        String username = "testuser";
        String password = "testpassword";
        User user = new User(username, PasswordAuthenticator.hashPassword(password));
        when(repository.findById(username)).thenReturn(Optional.of(user));
        ArgumentCaptor<User> argCaptor = ArgumentCaptor.forClass(User.class);
        assertNull(user.getActiveToken());
        service.login(username, password);
        verify(repository, times(1)).save(argCaptor.capture());
        assertEquals(username, argCaptor.getValue().getUsername());
        assertNotNull(argCaptor.getValue().getActiveToken());
    }
}
