package com.minesweeper.model;

/**
 * 
 * This class models a single Cell of a Game Board
 */
public class Cell {

    public enum Status {
        Uncovered, Covered, Flagged, QuestionMarked;
    }

    /** indicates whether the cell contains a mine **/
    private boolean hasMine;

    /**
     * indicates the number of mines that are adjacent to the cell (zero if
     * none)
     **/
    private int adjacentMinesCounter;

    /** indicates the status of the cell **/
    private Status status;

    public Cell() {
        this.hasMine = false;
        this.adjacentMinesCounter = 0;
        this.status = Status.Covered;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public int getAdjacentMinesCounter() {
        return adjacentMinesCounter;
    }

    public boolean hasAdjacentMines() {
        return getAdjacentMinesCounter() > 0;
    }

    public void addMine() {
        hasMine = true;
        adjacentMinesCounter = 0;
    }

    public void incrementAdjacentMinesCounter() {
        adjacentMinesCounter++;
    }

    public void uncover() {
        status = Status.Uncovered;
    }

    public boolean isCovered() {
        return status != Status.Uncovered;
    }

    public void setRedFlag() {
        if (!isCovered()) {
            throw new CellException("cannot add red flag on an uncovered cell");
        }
        status = Status.Flagged;
    }

    public void setQuestionMark() {
        if (!isCovered()) {
            throw new CellException("cannot add question mark on an uncovered cell");
        }
        status = Status.QuestionMarked;
    }

}
