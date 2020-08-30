package com.minesweeper.model;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.data.annotation.Id;

import com.minesweeper.service.GameInfo;

/**
 * 
 * This class represents a Minesweeper Game
 *
 */
public class Game {

    public enum Status {
        Started, Won, Lost
    }

    @Id
    private UUID id;

    /** game's board **/
    private Board board;

    /** status of the game **/
    private Status status;

    /** game's start time **/
    private LocalDateTime startTime;

    /** game's end time **/
    private LocalDateTime endTime;

    public Game() {
    }

    public Game(int rows, int columns, int mines) {
        this.id = UUID.randomUUID();
        this.board = new Board(rows, columns, mines);
        this.status = Status.Started;
        this.startTime = LocalDateTime.now(getClock());
    }

    /**
     * Reveals/uncovers a cell given its position. If the cell contains a mine,
     * then the game is over. If the cell has no adjacent mines, then the
     * uncover is propagated to its adjacent cells (square) in cascade
     * 
     * @param row
     * @param column
     * @return
     */
    public Game uncoverCell(int row, int column) {
        validateAndExecute(row, column, board -> board.uncover(row, column));
        if (getBoard().isFinished()) {
            finishGame();
        }
        return this;
    }

    /**
     * Sets a red flag on a cell given its position.
     * 
     * Note: cells with mine are not required to have set a red flag at the end
     * of the game
     * 
     * @param row
     * @param column
     * @return
     */
    public Game setRedFlag(int row, int column) {
        validateAndExecute(row, column, board -> board.setRedFlag(row, column));
        return this;
    }

    /**
     * Removes a red flag from a cell
     * 
     * @param row
     * @param column
     * @return
     */
    public Game removeRedFlag(int row, int column) {
        validateAndExecute(row, column, board -> board.removeRedFlag(row, column));
        return this;
    }

    /**
     * Sets a question mark on a cell given its position.
     * 
     * @param row
     * @param column
     * @return
     */
    public Game setQuestionMark(int row, int column) {
        validateAndExecute(row, column, board -> board.setQuestionMark(row, column));
        return this;
    }

    /**
     * Removes a red flag from a cell
     * 
     * @param row
     * @param column
     * @return
     */
    public Game removeQuestionMark(int row, int column) {
        validateAndExecute(row, column, board -> board.removeQuestionMark(row, column));
        return this;
    }

    public UUID getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public long getDuration() {
        LocalDateTime end = (endTime != null ? endTime : LocalDateTime.now());
        return Duration.between(startTime, end).getSeconds();
    }

    public GameInfo toGameInfo() {
        GameInfo gameInfo = new GameInfo();
        gameInfo.id = this.getId();
        gameInfo.status = this.getStatus().name();
        gameInfo.duration = this.getDuration();
        gameInfo.board = this.board.toBoardInfo();
        return gameInfo;
    }

    protected Clock getClock() {
        return Clock.systemDefaultZone();
    }

    protected Board getBoard() {
        return board;
    }

    private void validateCanDoAction() {
        if (isFinished()) {
            throw new GameException("action not allowed, the game is finished");
        }
    }

    protected boolean isFinished() {
        return status != Status.Started;
    }

    private void finishGame() {
        status = (getBoard().areAllCellsWithoutMineUncovered() ? Status.Won : Status.Lost);
        endTime = LocalDateTime.now(getClock());
    }

    /**
     * Validates the action and executes it
     * 
     * @param row
     * @param column
     * @param action
     */
    private void validateAndExecute(int row, int column, Consumer<Board> action) {
        validateCanDoAction();
        action.accept(getBoard());
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}
