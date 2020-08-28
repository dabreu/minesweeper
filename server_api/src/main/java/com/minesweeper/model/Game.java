package com.minesweeper.model;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 
 * This class represents a Minesweeper Game
 *
 */
public class Game {

	public enum Status {
		Started, Won, Lost
	}

	/** game's board **/
	private Board board;

	/** status of the game **/
	private Status status;

	/** game's start time **/
	private LocalDateTime startTime;

	/** game's end time **/
	private LocalDateTime endTime;

	public Game(int rows, int columns, int mines) {
		this.board = new Board(rows, columns, mines);
		this.status = Status.Started;
		this.startTime = LocalDateTime.now(getClock());
	}

	/**
	 * Reveals/uncovers a cell given its position. If the cell contains a mine, then
	 * the game is over. If the cell has no adjacent mines, then the uncover is
	 * propagated to its adjacent cells (square) in cascade
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public Game uncoverCell(int row, int column) {
		validateCanDoAction();
		getBoard().uncover(row, column);
		if (getBoard().isFinished()) {
			finishGame();
		}
		return this;
	}

	/**
	 * Sets a red flag on a cell given its position.
	 * 
	 * Note: cells with mine are not required to have set a red flag at the end of
	 * the game
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public Game setRedFlag(int row, int column) {
		validateCanDoAction();
		getBoard().setRedFlag(row, column);
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
		validateCanDoAction();
		getBoard().setQuestionMark(row, column);
		return this;
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
		return Duration.between(startTime, endTime).getSeconds();
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

}
