package com.minesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minesweeper.model.Game.Status;
import com.minesweeper.service.GameInfo;

@SpringBootTest(properties = "spring.data.mongodb.database=test")
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateNewGameReturnsTheGameInfo() throws Exception {
        GameInfo gameInfo = createNewGame();
        assertNotNull(gameInfo);
    }

    @Test
    public void testGetGameReturns404IfNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/minesweeper/" + UUID.randomUUID())).andExpect(status().isNotFound()).andReturn();
        verifyError(result, HttpStatus.NOT_FOUND, "Game not found");
    }

    @Test
    public void testGetGameReturnsTheGameInfo() throws Exception {
        UUID gameId = createNewGame().id;
        MvcResult result = mockMvc.perform(get("/minesweeper/" + gameId)).andExpect(status().isOk()).andReturn();
        GameInfo gameInfo = fromJson(result, GameInfo.class);
        assertNotNull(gameInfo);
        assertEquals(gameId, gameInfo.id);
        assertEquals(Status.Started.name(), gameInfo.status);
        assertNotNull(gameInfo.duration);
    }

    @Test
    public void testUncoverCellReturns404IfNotFound() throws Exception {
        ResultActions resultActions = doPutActionOnCell("uncover", UUID.randomUUID(), 10, 10);
        MvcResult result = resultActions.andExpect(status().isNotFound()).andReturn();
        verifyError(result, HttpStatus.NOT_FOUND, "Game not found");
    }

    @Test
    public void testUncoverCellReturns400IfInvalidCellPosition() throws Exception {
        UUID gameId = createNewGame().id;
        ResultActions resultActions = doPutActionOnCell("uncover", gameId, 11, 8);
        MvcResult result = resultActions.andExpect(status().isBadRequest()).andReturn();
        verifyError(result, HttpStatus.BAD_REQUEST, "invalid row");
    }

    @Test
    public void testUncoverCellReturnsTheGameInfo() throws Exception {
        UUID gameId = createNewGame().id;
        ResultActions resultActions = doPutActionOnCell("uncover", gameId, 8, 8);
        MvcResult result = resultActions.andExpect(status().isOk()).andReturn();
        GameInfo gameInfo = fromJson(result, GameInfo.class);
        assertNotNull(gameInfo);
        assertEquals(gameId, gameInfo.id);
    }

    @Test
    public void testSetRedFlagReturns404IfNotFound() throws Exception {
        ResultActions resultActions = doPutActionOnCell("red_flag", UUID.randomUUID(), 10, 10);
        MvcResult result = resultActions.andExpect(status().isNotFound()).andReturn();
        verifyError(result, HttpStatus.NOT_FOUND, "Game not found");
    }

    @Test
    public void testSetRedFlagReturnsTheGameInfo() throws Exception {
        UUID gameId = createNewGame().id;
        ResultActions resultActions = doPutActionOnCell("red_flag", gameId, 8, 8);
        MvcResult result = resultActions.andExpect(status().isOk()).andReturn();
        GameInfo gameInfo = fromJson(result, GameInfo.class);
        assertNotNull(gameInfo);
        assertEquals(gameId, gameInfo.id);
    }

    @Test
    public void testRemoveRedFlagReturns400IfCellHasNotRedFlag() throws Exception {
        UUID gameId = createNewGame().id;
        ResultActions resultActions = doDeleteActionOnCell("red_flag", gameId, 8, 8);
        MvcResult result = resultActions.andExpect(status().isBadRequest()).andReturn();
        verifyError(result, HttpStatus.BAD_REQUEST, "cannot remove red flag");
    }

    @Test
    public void testSetQuestionMarkReturns404IfNotFound() throws Exception {
        ResultActions resultActions = doPutActionOnCell("question_mark", UUID.randomUUID(), 10, 10);
        MvcResult result = resultActions.andExpect(status().isNotFound()).andReturn();
        verifyError(result, HttpStatus.NOT_FOUND, "Game not found");
    }

    @Test
    public void testSetQuestionMarkReturnsTheGameInfo() throws Exception {
        UUID gameId = createNewGame().id;
        ResultActions resultActions = doPutActionOnCell("question_mark", gameId, 8, 8);
        MvcResult result = resultActions.andExpect(status().isOk()).andReturn();
        GameInfo gameInfo = fromJson(result, GameInfo.class);
        assertNotNull(gameInfo);
        assertEquals(gameId, gameInfo.id);
    }

    @Test
    public void testRemoveQuestionMarkReturns400IfCellHasNotQuestionMark() throws Exception {
        UUID gameId = createNewGame().id;
        ResultActions resultActions = doDeleteActionOnCell("question_mark", gameId, 8, 8);
        MvcResult result = resultActions.andExpect(status().isBadRequest()).andReturn();
        verifyError(result, HttpStatus.BAD_REQUEST, "cannot remove question mark");
    }

    private GameInfo createNewGame() throws Exception {
        MvcResult result = mockMvc.perform(post("/minesweeper/").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk()).andReturn();
        return fromJson(result, GameInfo.class);
    }

    private <T> T fromJson(MvcResult result, Class<T> tClass) throws Exception {
        return mapper.readValue(result.getResponse().getContentAsString(), tClass);
    }

    private ResultActions doPutActionOnCell(String action, UUID id, int row, int column) throws Exception {
        return mockMvc.perform(put("/minesweeper/" + id + "/" + action).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("row", String.valueOf(row)).param("column", String.valueOf(column)));
    }

    private ResultActions doDeleteActionOnCell(String action, UUID id, int row, int column) throws Exception {
        return mockMvc
                .perform(delete("/minesweeper/" + id + "/" + action).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("row", String.valueOf(row)).param("column", String.valueOf(column)));
    }

    @SuppressWarnings("unchecked")
    private void verifyError(MvcResult result, HttpStatus status, String expectedError) throws Exception {
        Map<String, Object> mapResult = fromJson(result, Map.class);
        assertEquals(status.value(), mapResult.get("status"));
        assertTrue(mapResult.containsKey("error"));
        String error = (String) mapResult.get("error");
        assertTrue(error.contains(expectedError));
    }
}
