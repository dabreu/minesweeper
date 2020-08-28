package com.minesweeper.model;

public class CellException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CellException(String message) {
        super(message);
    }
}
