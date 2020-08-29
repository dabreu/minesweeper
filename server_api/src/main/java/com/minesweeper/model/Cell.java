package com.minesweeper.model;

/**
 * 
 * This class models a single Cell of a Game Board
 */
public class Cell {

    public enum Code {
        U, C, F, Q, M;
    }

    public enum Status {
        Uncovered(Code.U), Covered(Code.C), Flagged(Code.F), QuestionMarked(Code.Q);

        private Code code;

        private Status(Code code) {
            this.code = code;
        }

        public String getCode() {
            return code.name();
        }
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

    public void removeRedFlag() {
        if (status != Status.Flagged) {
            throw new CellException("cannot remove red flag on a cell without it");
        }
        status = Status.Covered;
    }

    public void setQuestionMark() {
        if (!isCovered()) {
            throw new CellException("cannot add question mark on an uncovered cell");
        }
        status = Status.QuestionMarked;
    }

    public void removeQuestionMark() {
        if (status != Status.QuestionMarked) {
            throw new CellException("cannot remove question mark on a cell without it");
        }
        status = Status.Covered;
    }

    public String getInfo() {
        if (isCovered()) {
            return status.getCode();
        }
        if (hasMine()) {
            return Code.M.name();
        }
        return String.valueOf(adjacentMinesCounter);
    }
}
