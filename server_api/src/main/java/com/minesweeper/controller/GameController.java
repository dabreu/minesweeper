package com.minesweeper.controller;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minesweeper.service.GameInfo;
import com.minesweeper.service.GameService;
import com.minesweeper.service.IGameService;

@RestController
public class GameController {

    private final IGameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    /**
     * Entry point to create a new game
     * 
     * @param parameters
     *            rows, columns and mines
     * @return the id of the created game
     */
    @PostMapping(value = "/minesweeper", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameInfo createGame(Integer rows, Integer columns, Integer mines) {
        return service.createGame(rows, columns, mines);
    }

    /**
     * Returns the game with the given id
     * 
     * @param gameId
     *            of the game to return
     * @return the information of the game
     */
    @GetMapping("/minesweeper/{gameId}")
    public GameInfo getGame(@PathVariable String gameId) {
        return service.getGame(getId(gameId));
    }

    /**
     * Uncovers/reveals a cell of a given game
     * 
     * @param gameId
     *            the id of the game where the cell belongs to
     * @param row
     *            of the cell to reveal
     * @param column
     *            of the cell to reveal
     * @return the information of the game
     */
    @PutMapping(value = "/minesweeper/{gameId}/uncover", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameInfo uncoverCell(@PathVariable String gameId, Integer row, Integer column) {
        return service.uncoverCell(getId(gameId), row, column);
    }

    /**
     * Sets a red flag on a cell of a given game
     * 
     * @param gameId
     *            the id of the game where the cell belongs to
     * @param row
     *            of the cell
     * @param column
     *            of the cell
     * @return the information of the game
     */
    @PutMapping(value = "/minesweeper/{gameId}/red_flag", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameInfo setRedFlag(@PathVariable String gameId, Integer row, Integer column) {
        return service.setRedFlag(getId(gameId), row, column);
    }

    /**
     * Removes a red flag on a cell of a given game
     * 
     * @param gameId
     *            the id of the game where the cell belongs to
     * @param row
     *            of the cell
     * @param column
     *            of the cell
     * @return the information of the game
     */
    @DeleteMapping(value = "/minesweeper/{gameId}/red_flag", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameInfo removeRedFlag(@PathVariable String gameId, Integer row, Integer column) {
        return service.removeRedFlag(getId(gameId), row, column);
    }

    /**
     * Sets a question mark on a cell of a given game
     * 
     * @param gameId
     *            the id of the game where the cell belongs to
     * @param row
     *            of the cell
     * @param column
     *            of the cell
     * @return the information of the game
     */
    @PutMapping(value = "/minesweeper/{gameId}/question_mark", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameInfo setQuestionMark(@PathVariable String gameId, Integer row, Integer column) {
        return service.setQuestionMark(getId(gameId), row, column);
    }

    /**
     * Removes a question mark on a cell of a given game
     * 
     * @param gameId
     *            the id of the game where the cell belongs to
     * @param row
     *            of the cell
     * @param column
     *            of the cell
     * @return the information of the game
     */
    @DeleteMapping(value = "/minesweeper/{gameId}/question_mark", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameInfo removeQuestionMark(@PathVariable String gameId, Integer row, Integer column) {
        return service.removeQuestionMark(getId(gameId), row, column);
    }

    private UUID getId(String id) {
        return UUID.fromString(id);
    }
}
