package com.minesweeper.service;

import java.util.UUID;

/**
 * Interface for the service that allows game interaction
 */
public interface IGameService {

    /**
     * Creates a new game with the given parameters, if they are not provided
     * default values are used
     * 
     * @param rows
     *            of the game's board
     * @param columns
     *            of the game's board
     * @param mines
     *            hidden on the game's board
     * @return the game information
     */
    public GameInfo createGame(Integer rows, Integer columns, Integer mines);

    /**
     * Returns the information of a game given its id
     * 
     * @param id
     *            of the game
     * @return the game information
     */
    public GameInfo getGame(UUID id);

    /**
     * Uncovers/reveals a given cell
     * 
     * @param id
     *            of the game where the cell belongs to
     * @param row
     *            of the cell
     * @param column
     *            of the cell
     * @return the game information
     */
    public GameInfo uncoverCell(UUID id, Integer row, Integer column);

    /**
     * Sets a red flag on a given cell
     * 
     * @param id
     *            of the game where the cell belongs to
     * @param row
     *            of the cell
     * @param column
     *            of the cell
     * @return the game information
     */
    public GameInfo setRedFlag(UUID id, Integer row, Integer column);

    /**
     * Removes a red flag on a given cell
     * 
     * @param id
     *            of the game where the cell belongs to
     * @param row
     *            of the cell
     * @param column
     *            of the cell
     * @return the game information
     */
    public GameInfo removeRedFlag(UUID id, Integer row, Integer column);

    /**
     * Sets a question mark on a given cell
     * 
     * @param id
     *            of the game where the cell belongs to
     * @param row
     *            of the cell
     * @param column
     *            of the cell
     * @return the game information
     */
    public GameInfo setQuestionMark(UUID id, Integer row, Integer column);

    /**
     * Removes a question mark on a given cell
     * 
     * @param id
     *            of the game where the cell belongs to
     * @param row
     *            of the cell
     * @param column
     *            of the cell
     * @return the game information
     */
    public GameInfo removeQuestionMark(UUID id, Integer row, Integer column);
}
