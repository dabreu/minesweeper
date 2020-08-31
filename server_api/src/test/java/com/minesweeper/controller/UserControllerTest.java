package com.minesweeper.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minesweeper.model.User;
import com.minesweeper.repository.UserRepository;
import com.minesweeper.security.LoginInfo;
import com.minesweeper.security.PasswordAuthenticator;

@SpringBootTest(properties = "spring.data.mongodb.database=test")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String REGISTER_URI = "/minesweeper/register";
    private static final String LOGIN_URI = "/minesweeper/login";
    private static final String PRINCIPAL = "testuser";
    private static final String PRINCIPAL_PASSWORD = "testuserpassword";

    @BeforeEach
    public void createUser() throws Exception {
        User user = new User(PRINCIPAL, PasswordAuthenticator.hashPassword(PRINCIPAL_PASSWORD));
        repository.save(user);
    }

    @AfterEach
    public void deleteUser() {
        repository.deleteAll();
    }

    @Test
    public void testRegisterUserReturnsBadRequestIfNoUsername() throws Exception {
        mockMvc.perform(post(REGISTER_URI).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void testRegisterUserReturnsBadRequestIfNoPassword() throws Exception {
        mockMvc.perform(
                post(REGISTER_URI).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("username", "testuser"))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void testRegisterUserReturnsConflictIfUsernameExists() throws Exception {
        mockMvc.perform(post(REGISTER_URI).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("username", PRINCIPAL)
                .param("password", "anotherpassword")).andExpect(status().isConflict()).andReturn();
    }

    @Test
    public void testRegisterUserReturnsOkIfUsernameNotExists() throws Exception {
        mockMvc.perform(post(REGISTER_URI).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("username", "newusername").param("password", "anotherpassword")).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testLoginReturnsUnauthorizedIfUserNotExists() throws Exception {
        mockMvc.perform(post(LOGIN_URI).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("username", "newusername")
                .param("password", "anotherpassword")).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void testLoginReturnsUnauthorizedIfPasswordIsIncorrect() throws Exception {
        mockMvc.perform(post(LOGIN_URI).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("username", PRINCIPAL)
                .param("password", "anotherpassword")).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void testLoginReturnsLoginInfoIfSuccess() throws Exception {
        MvcResult result = mockMvc.perform(post(LOGIN_URI).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("username", PRINCIPAL).param("password", PRINCIPAL_PASSWORD)).andExpect(status().isOk()).andReturn();
        LoginInfo loginInfo = fromJson(result, LoginInfo.class);
        assertNotNull(loginInfo.getToken());
    }

    private <T> T fromJson(MvcResult result, Class<T> tClass) throws Exception {
        return mapper.readValue(result.getResponse().getContentAsString(), tClass);
    }
}
